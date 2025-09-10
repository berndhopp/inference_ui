package de.bhopp.ml.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import inference.GRPCInferenceServiceGrpc;
import io.grpc.ManagedChannelBuilder;

@Configuration
public class GrpcConfig {

    @Bean
    public GRPCInferenceServiceGrpc.GRPCInferenceServiceBlockingStub inferenceServiceBlockingStub(
            @Value("${triton.host}") String tritonHost,
            @Value("${triton.grpc_port}") int tritonGrpcPort
    ) {
        return GRPCInferenceServiceGrpc.newBlockingStub(
            ManagedChannelBuilder
                .forAddress(tritonHost, tritonGrpcPort)
                .usePlaintext()
                .build()
        );
    }
}
