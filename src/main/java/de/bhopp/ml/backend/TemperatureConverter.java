package de.bhopp.ml.backend;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import inference.GRPCInferenceServiceGrpc;
import inference.GrpcService.ModelInferRequest;

@Component
@Scope("singleton")
public class TemperatureConverter {
    private final GRPCInferenceServiceGrpc.GRPCInferenceServiceBlockingStub serviceBlockingStub;
    private final ResponseToFloatArrayConverter responseToFloatArrayConverter;

    public TemperatureConverter(GRPCInferenceServiceGrpc.GRPCInferenceServiceBlockingStub serviceBlockingStub, ResponseToFloatArrayConverter responseToFloatArrayConverter) {
        this.serviceBlockingStub = serviceBlockingStub;
        this.responseToFloatArrayConverter = responseToFloatArrayConverter;
    }

    public double convertToFahrenheit(float celsius) {
        final var request = ModelInferRequest
            .newBuilder()
            /*TODO
            You should not need hints to configure this request,
            once you implemented the one in
            ImageClassification
            */
            .build();

        final var response = serviceBlockingStub.modelInfer(request);

        final var responseAsFloatArray = responseToFloatArrayConverter.apply(response);

        return responseAsFloatArray[0];
    }
}
