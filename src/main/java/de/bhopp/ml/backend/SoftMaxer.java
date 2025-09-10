package de.bhopp.ml.backend;

import org.springframework.stereotype.Component;

import java.util.function.Function;

import static java.util.Arrays.stream;

@Component
public class SoftMaxer implements Function<float[], double[]> {
    @Override
    public double[] apply(float[] input) {
        final var softMaxed = new double[input.length];

        for (int i = 0; i < input.length; i++) {
            softMaxed[i] = Math.exp(input[i]);
        }

        final var sum = stream(softMaxed).sum();

        for (int i = 0; i < input.length; i++) {
            softMaxed[i] = softMaxed[i] / sum;
        }

        return softMaxed;
    }
}
