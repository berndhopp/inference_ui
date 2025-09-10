package de.bhopp.ml.backend;

import com.google.common.primitives.Doubles;
import com.google.common.primitives.Floats;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import inference.GRPCInferenceServiceGrpc;
import inference.GrpcService.InferTensorContents;
import inference.GrpcService.ModelInferRequest;
import inference.GrpcService.ModelInferRequest.InferInputTensor;
import inference.GrpcService.ModelInferRequest.InferRequestedOutputTensor;
import lombok.SneakyThrows;

@Component
@Scope("singleton")
public class ImageClassifier {
  private final GRPCInferenceServiceGrpc.GRPCInferenceServiceBlockingStub serviceBlockingStub;
  private final RN50Preprocessor rn50Preprocessor;
  private final SoftMaxer softMaxer;
  private final ResponseToFloatArrayConverter responseToFloatArrayConverter;

  public ImageClassifier(
          GRPCInferenceServiceGrpc.GRPCInferenceServiceBlockingStub serviceBlockingStub,
          RN50Preprocessor rn50Preprocessor, SoftMaxer softMaxer, ResponseToFloatArrayConverter responseToFloatArrayConverter) {
    this.serviceBlockingStub = serviceBlockingStub;
    this.rn50Preprocessor = rn50Preprocessor;
      this.softMaxer = softMaxer;
      this.responseToFloatArrayConverter = responseToFloatArrayConverter;
  }

  @SneakyThrows
  public List<ImageClassification> classify(byte[] image) {
    final var preprocessed = rn50Preprocessor.rn50Preprocess(image);

    final var inferTensorContents = InferTensorContents
            .newBuilder()
            .addAllFp32Contents(Floats.asList(preprocessed))
            .build();

    final var inferInputTensor = InferInputTensor
            .newBuilder()
            .setName("input__0")
            .addShape(3)
            .addShape(224)
            .addShape(224)
            .setDatatype("FP32")
            .setContents(inferTensorContents)
            .build();

    final var outputTensorSpec = InferRequestedOutputTensor
            .newBuilder()
            .setName("output__0")
            .build();

    final var request = ModelInferRequest
            .newBuilder()
            .addInputs(inferInputTensor)
            .addOutputs(outputTensorSpec)
            .setModelName("resnet50")
            .build();

    final var response = serviceBlockingStub.modelInfer(request);

    final var responseAsFloatArray = responseToFloatArrayConverter.apply(response);

    final var labelProbabilities = softMaxer.apply(responseAsFloatArray);

    final var classifications = new ArrayList<ImageClassification>();

    for (int i = 0; i < 3; i++) {
      final var maxProbability = Doubles.max(labelProbabilities);

      final var maxProbIndex = Doubles.indexOf(labelProbabilities, maxProbability);

      final var label = ImagenetLabel.values()[maxProbIndex];

      classifications.add(new ImageClassification(label, (float) maxProbability));

      labelProbabilities[maxProbIndex] = 0;
    }

    return classifications;
  }
}
