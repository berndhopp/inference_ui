package de.bhopp.ml.backend;

import org.springframework.stereotype.Component;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Utility to preprocess an image for RN50 (ResNet50) inference.
 * Output: float[] in CHW order (channel-first), length = 3 * 224 * 224.
 */
@Component
public class RN50Preprocessor {

    // Means and stds follow torchvision's normalization for RN50
    private static final float[] MEAN = {0.485f, 0.456f, 0.406f};
    private static final float[] STD  = {0.229f, 0.224f, 0.225f};

    /**
     * Read image, resize (shorter side -> 256), center crop 224x224,
     * convert to float32 CHW normalized by ImageNet mean/std.
     *
     * @param imageRaw raw image data
     * @return float[] with length 3*224*224 in CHW order (float32)
     * @throws IOException if image can't be read
     */
    public float[] rn50Preprocess(byte[] imageRaw) throws IOException {
        BufferedImage img = ImageIO.read(new ByteArrayInputStream(imageRaw));
        if (img == null) {
            throw new IOException("Unable to read image");
        }

        // 1) Resize shorter side to 256 preserving aspect ratio
        BufferedImage resized = resizeShorterSide(img);

        // 2) Center crop 224x224
        BufferedImage cropped = centerCrop(resized);

        // 3) Convert to float CHW and normalize
        return toCHWFloatNormalized(cropped);
    }

    private BufferedImage resizeShorterSide(BufferedImage img) {
        int w = img.getWidth();
        int h = img.getHeight();

        int newW, newH;
        if (w < h) {
            newW = 256;
            newH = Math.round((float) h * 256 / w);
        } else {
            newH = 256;
            newW = Math.round((float) w * 256 / h);
        }

        BufferedImage resized = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = resized.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.drawImage(img, 0, 0, newW, newH, null);
        g.dispose();
        return resized;
    }

    private BufferedImage centerCrop(BufferedImage img) {
        int x = Math.max(0, (img.getWidth() - 224) / 2);
        int y = Math.max(0, (img.getHeight() - 224) / 2);
        return img.getSubimage(x, y, 224, 224);
    }

    private float[] toCHWFloatNormalized(BufferedImage img) {
        final int W = img.getWidth();   // should be 224
        final int H = img.getHeight();  // should be 224
        final int C = 3;
        float[] out = new float[C * H * W];

        // Iterate over HxW and fill channels
        for (int y = 0; y < H; y++) {
            for (int x = 0; x < W; x++) {
                int rgb = img.getRGB(x, y);
                // extract channels (0..255)
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = (rgb) & 0xFF;

                // normalize to [0,1]
                float rf = r / 255.0f;
                float gf = g / 255.0f;
                float bf = b / 255.0f;

                // apply mean/std, and store in CHW order
                int base = y * W + x;
                out[base] = (rf - MEAN[0]) / STD[0]; // R channel
                out[H * W + base] = (gf - MEAN[1]) / STD[1]; // G channel
                out[2 * H * W + base] = (bf - MEAN[2]) / STD[2]; // B channel
            }
        }
        return out;
    }
}

