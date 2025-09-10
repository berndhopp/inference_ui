package de.bhopp.ml.backend;

import com.google.common.primitives.Doubles;
import com.google.common.primitives.Floats;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import inference.GRPCInferenceServiceGrpc;
import inference.GrpcService;
import inference.GrpcService.InferTensorContents;
import inference.GrpcService.ModelInferRequest.InferInputTensor;
import lombok.SneakyThrows;

import static java.lang.Float.isNaN;
import static java.util.Arrays.stream;

@Component
@Scope("singleton")
public class ImageClassifier {
  private final GRPCInferenceServiceGrpc.GRPCInferenceServiceBlockingStub serviceBlockingStub;
  private final RN50Preprocessor rn50Preprocessor;

  public ImageClassifier(
      GRPCInferenceServiceGrpc.GRPCInferenceServiceBlockingStub serviceBlockingStub,
      RN50Preprocessor rn50Preprocessor) {
    this.serviceBlockingStub = serviceBlockingStub;
    this.rn50Preprocessor = rn50Preprocessor;
  }

  @SneakyThrows
  public List<ImageClassification> classify(byte[] image) {
    final var preprocessed = rn50Preprocessor.rn50Preprocess(image);

    final var inferTensorContents =
        InferTensorContents.newBuilder().addAllFp32Contents(Floats.asList(preprocessed)).build();

    final var inferInputTensor =
        InferInputTensor.newBuilder()
            .setName("input__0")
            .addShape(3)
            .addShape(224)
            .addShape(224)
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
            .setModelName("resnet50")
            .build();

    final var response = serviceBlockingStub.modelInfer(request);

    final var labelProbabilities = getLabelProbabilities(response);

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

  private double[] getLabelProbabilities(GrpcService.ModelInferResponse response) {
    ByteBuffer bb =
        ByteBuffer.wrap(response.getRawOutputContents(0).toByteArray())
            .order(ByteOrder.LITTLE_ENDIAN);
    FloatBuffer fb = bb.asFloatBuffer();
    float[] out = new float[fb.remaining()];
    fb.get(out);
    for (int i = 0; i < out.length; i++) {
      if (isNaN(out[i])) {
        out[i] = 0;
      }
    }
    return softMax(out);
  }

  private double[] softMax(float[] input) {
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
