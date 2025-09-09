package de.bhopp.ml.ui.cancer;

import com.vaadin.flow.server.streams.InMemoryUploadHandler;
import com.vaadin.flow.spring.annotation.UIScope;

import de.bhopp.ml.backend.CancerDetector;

import org.springframework.stereotype.Component;

@UIScope
@Component
public class ImageUploadHandler extends InMemoryUploadHandler {
    public ImageUploadHandler(CancerDetector cancerDetector, CancerProbabilityOutput output) {
        super((uploadMetadata, image) -> {
            final var cancerProbability = cancerDetector.getCancerProbability(image);
            output.setCancerProbability(cancerProbability);
        });
    }
}
