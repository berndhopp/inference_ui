package de.bhopp.ml.ui.temperatures;

import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.annotation.UIScope;

import org.springframework.stereotype.Component;

@UIScope
@Component
public class FahrenheitOutput extends TextField {
    public FahrenheitOutput() {
        setLabel("Fahrenheit");
        setReadOnly(true);
    }

    public void setFahrenheit(double fahrenheit) {
        setValue("" + fahrenheit);
    }
}
