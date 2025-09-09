package de.bhopp.ml.backend;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class TemperatureConverter {
    public double convertToFahrenheit(double celsius) {
        return 0.0;
    }
}
