package de.bhopp.ml.backend;

import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class SoftMaxer implements Function<float[], double[]> {
    @Override
    public double[] apply(float[] input) {
        //TODO implement
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
