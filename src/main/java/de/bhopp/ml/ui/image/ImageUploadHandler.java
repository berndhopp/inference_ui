package de.bhopp.ml.ui.image;

import com.vaadin.flow.server.streams.InMemoryUploadHandler;
import com.vaadin.flow.spring.annotation.UIScope;

import de.bhopp.ml.backend.ImageClassifier;

import org.springframework.stereotype.Component;

@UIScope
@Component
public class ImageUploadHandler extends InMemoryUploadHandler {
    public ImageUploadHandler(ImageClassifier imageClassifier, ImageLabelOutput output) {
        super((uploadMetadata, image) -> {
            final var classifications = imageClassifier.classify(image);
            output.setClassification(classifications);
        });
    }
}
