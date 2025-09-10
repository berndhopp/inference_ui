package de.bhopp.ml.backend;

import com.google.protobuf.ByteString;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import inference.GRPCInferenceServiceGrpc;
import inference.GrpcService;
import inference.GrpcService.InferTensorContents;
import inference.GrpcService.ModelInferRequest.InferInputTensor;

@Component
@Scope("singleton")
public class CancerDetector {
    private final GRPCInferenceServiceGrpc.GRPCInferenceServiceBlockingStub serviceBlockingStub;

    public CancerDetector(GRPCInferenceServiceGrpc.GRPCInferenceServiceBlockingStub serviceBlockingStub) {
        this.serviceBlockingStub = serviceBlockingStub;
    }

    public double getCancerProbability(byte[] image) {
        final var inferTensorContents = InferTensorContents
                .newBuilder()
                .addBytesContents(ByteString.copyFrom(image))
                .build();

        final var inferInputTensor = InferInputTensor
                .newBuilder()
                .setContents(inferTensorContents)
                .build();

        final var request = GrpcService.ModelInferRequest
                .newBuilder()
                .addInputs(inferInputTensor)
                .setId("foo")
                .setModelName("bar")
                .build();

        final var response = serviceBlockingStub.modelInfer(request);

        //TODO process response

        throw new UnsupportedOperationException("implement me");
    }
}
