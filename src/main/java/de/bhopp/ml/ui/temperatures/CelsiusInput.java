package de.bhopp.ml.ui.temperatures;

import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.spring.annotation.UIScope;

import de.bhopp.ml.backend.TemperatureConverter;

import org.springframework.stereotype.Component;

@UIScope
@Component
public class CelsiusInput extends NumberField {
    public CelsiusInput(TemperatureConverter temperatureConverter, FahrenheitOutput fahrenheitOutput) {
        setLabel("Celsius");
        addValueChangeListener(e -> {
            final var fahrenheit = temperatureConverter.convertToFahrenheit(e.getValue().floatValue());

            fahrenheitOutput.setFahrenheit(fahrenheit);
        });
    }
}
