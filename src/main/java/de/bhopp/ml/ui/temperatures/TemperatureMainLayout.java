package de.bhopp.ml.ui.temperatures;

import com.vaadin.flow.spring.annotation.UIScope;

import de.bhopp.ml.ui.base.CenteringLayout;

import org.springframework.stereotype.Component;

@UIScope
@Component
public class TemperatureMainLayout extends CenteringLayout {
    TemperatureMainLayout(CelsiusInput celsiusInput, FahrenheitOutput fahrenheitOutput) {
        super(celsiusInput, fahrenheitOutput);
    }
}
