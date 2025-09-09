package de.bhopp.ml.ui.cancer;

import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.stereotype.Component;

@UIScope
@Component
public class CancerProbabilityOutput extends TextField {
    public CancerProbabilityOutput() {
        setLabel("Cancer Probability");
        setReadOnly(true);
    }

    public void setCancerProbability(double probability) {
        setValue("" + probability);
    }
}
