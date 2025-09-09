package de.bhopp.ml.ui.cancer;

import com.vaadin.flow.spring.annotation.UIScope;
import de.bhopp.ml.ui.base.AbstractMainLayout;
import org.springframework.stereotype.Component;

@UIScope
@Component
public class CancerMainLayout extends AbstractMainLayout {
    CancerMainLayout(ImageInput imageInput, CancerProbabilityOutput cancerProbabilityOutput) {
        super(imageInput, cancerProbabilityOutput);
    }
}
