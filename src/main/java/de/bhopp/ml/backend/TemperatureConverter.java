package de.bhopp.ml.backend;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import inference.GRPCInferenceServiceGrpc;
import inference.GrpcService;

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
        final var inferTensorContents =
                GrpcService.InferTensorContents.newBuilder().addFp32Contents(celsius).build();

        final var inferInputTensor =
                GrpcService.ModelInferRequest.InferInputTensor.newBuilder()
                        .setName("input__0")
                        .addShape(1)
                        .setDatatype("FP32")
                        .setContents(inferTensorContents)
                        .build();

        final var outputTensorSpec =
                GrpcService.ModelInferRequest.InferRequestedOutputTensor.newBuilder()
                        .setName("output__0")
                        .build();

        final var request =
                GrpcService.ModelInferRequest.newBuilder()
                        .addInputs(inferInputTensor)
                        .addOutputs(outputTensorSpec)
                        .setModelName("cel2far")
                        .build();

        final var response = serviceBlockingStub.modelInfer(request);

        final var responseAsFloatArray = responseToFloatArrayConverter.apply(response);

        return responseAsFloatArray[0];
    }
}
