package de.bhopp.ml.ui.image;

import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.spring.annotation.UIScope;

import de.bhopp.ml.backend.ImageClassification;

import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.joining;

@UIScope
@Component
public class ImageLabelOutput extends TextArea {
    public ImageLabelOutput() {
        setLabel("Predicted Labels");
        setReadOnly(true);
    }

    public void setClassification(List<ImageClassification> classifications) {
        setValue(
            classifications
                .stream()
                .map(classification -> "%s(%.2f%%)".formatted(classification.imagenetLabel(), classification.probability() * 100))
                .collect(joining("\n"))
        );
    }
}
