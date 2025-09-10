package de.bhopp.ml.backend;

import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.function.Function;

import inference.GrpcService;

import static java.lang.Float.isNaN;

@Component
public class ResponseToFloatArrayConverter implements Function<GrpcService.ModelInferResponse, float[]> {
    @Override
    public float[] apply(GrpcService.ModelInferResponse response) {
        ByteBuffer bb =
                ByteBuffer.wrap(response.getRawOutputContents(0).toByteArray())
                        .order(ByteOrder.LITTLE_ENDIAN);
        FloatBuffer fb = bb.asFloatBuffer();
        float[] out = new float[fb.remaining()];
        fb.get(out);
        for (int i = 0; i < out.length; i++) {
            if (isNaN(out[i])) {
                out[i] = 0;
            }
        }

        return out;
    }
}
