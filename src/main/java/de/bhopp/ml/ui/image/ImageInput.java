package de.bhopp.ml.ui.image;

import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.spring.annotation.UIScope;

import org.springframework.stereotype.Component;

@UIScope
@Component
public class ImageInput extends Upload {
    public ImageInput(ImageUploadHandler imageUploadHandler) {
        setMaxFiles(1);
        setUploadHandler(imageUploadHandler);
    }
}
