package de.bhopp.ml.ui.image;

import com.vaadin.flow.spring.annotation.UIScope;
import de.bhopp.ml.ui.base.CenteringLayout;
import org.springframework.stereotype.Component;

@UIScope
@Component
public class ImageMainLayout extends CenteringLayout {
    ImageMainLayout(ImageInput imageInput, ImageLabelOutput imageLabelOutput) {
        super(imageInput, imageLabelOutput);
    }
}
