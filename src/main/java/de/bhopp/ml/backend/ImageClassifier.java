package de.bhopp.ml.backend;

import com.google.common.primitives.Doubles;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import inference.GRPCInferenceServiceGrpc;
import inference.GrpcService.ModelInferRequest;
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
      RN50Preprocessor rn50Preprocessor,
      SoftMaxer softMaxer,
      ResponseToFloatArrayConverter responseToFloatArrayConverter) {
    this.serviceBlockingStub = serviceBlockingStub;
    this.rn50Preprocessor = rn50Preprocessor;
    this.softMaxer = softMaxer;
    this.responseToFloatArrayConverter = responseToFloatArrayConverter;
  }

  @SneakyThrows
  public List<ImageClassification> classify(byte[] image) {
    final var preprocessed = rn50Preprocessor.rn50Preprocess(image);

    final var request = ModelInferRequest
            .newBuilder()
            /*TODO
            The request needs to be configured with the correct inputs, outputs and model name.
            Here are some hints:

            First, create a InferTensorContentsVariable and add the preprocessed input via addAllFp32Contents.

            Then, create the InferInputTensor. It needs the correct name, shape and datatype set. Datatype is 'FP32',
            look up the other values in the config.pbtxt of the corresponding model.

            The InferRequestedOutputTensor just needs the name of the output that we're interested in (also in config.pbtxt).

            The modelName for the request is also to be taken from config.pbtxt.
            * */
            .build();

    final var response = serviceBlockingStub.modelInfer(request);

    final var responseAsFloatArray = responseToFloatArrayConverter.apply(response);

    final var labelProbabilities = softMaxer.apply(responseAsFloatArray);

    return top3Classifications(labelProbabilities);
  }

  private List<ImageClassification> top3Classifications(double[] labelProbabilities) {
    final var classifications = new ArrayList<ImageClassification>();

    for (int i = 0; i < 3; i++) {
      final var maxProbability = Doubles.max(labelProbabilities);

      final var maxProbIndex = Doubles.indexOf(labelProbabilities, maxProbability);

      final var label = ImagenetLabel.values()[maxProbIndex];

      classifications.add(new ImageClassification(label, maxProbability));

      labelProbabilities[maxProbIndex] = 0;
    }
    return classifications;
  }
}
