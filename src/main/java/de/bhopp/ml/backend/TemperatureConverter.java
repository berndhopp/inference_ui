package de.bhopp.ml.backend;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import inference.GRPCInferenceServiceGrpc;
import inference.GrpcService;

@Component
@Scope("singleton")
public class TemperatureConverter {
    private final GRPCInferenceServiceGrpc.GRPCInferenceServiceBlockingStub serviceBlockingStub;

    public TemperatureConverter(GRPCInferenceServiceGrpc.GRPCInferenceServiceBlockingStub serviceBlockingStub) {
        this.serviceBlockingStub = serviceBlockingStub;
    }

    public double convertToFahrenheit(float celsius) {
        final var inferTensorContents = GrpcService
                .InferTensorContents
                .newBuilder()
                .addFp32Contents(celsius)
                .build();

        final var inferInputTensor = GrpcService
                .ModelInferRequest
                .InferInputTensor
                .newBuilder()
                .setContents(inferTensorContents)
                .build();

        final var request = GrpcService
                .ModelInferRequest
                .newBuilder()
                .addInputs(inferInputTensor)
                .build();

        final var response = serviceBlockingStub.modelInfer(request);

        return response
                .getOutputs(0)
                .getContents()
                .getFp32Contents(0);
    }
}
