package de.bhopp.ml.backend;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class CancerDetector {
    public double getCancerProbability(byte[] image) {
        return 0.0;
    }
}
