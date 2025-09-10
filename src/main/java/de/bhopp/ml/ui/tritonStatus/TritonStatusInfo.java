package de.bhopp.ml.ui.tritonStatus;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.spring.annotation.UIScope;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import inference.GRPCInferenceServiceGrpc;
import inference.GrpcService;

@UIScope
@Component
public class TritonStatusInfo extends Checkbox {
    private final GRPCInferenceServiceGrpc.GRPCInferenceServiceBlockingStub inferenceServiceBlockingStub;

    TritonStatusInfo(GRPCInferenceServiceGrpc.GRPCInferenceServiceBlockingStub inferenceServiceBlockingStub) {
        this.inferenceServiceBlockingStub = inferenceServiceBlockingStub;
        setReadOnly(true);
        setLabel("Triton Live Status");
        getStyle().setAlignSelf(Style.AlignSelf.CENTER);
    }

    @Scheduled(fixedRate = 2000)
    public void checkTritonStatus(){
        final var serverIsAlive = inferenceServiceBlockingStub.serverLive(GrpcService.ServerLiveRequest.getDefaultInstance()).getLive();

        getUI().ifPresent(ui -> ui.access(() -> setValue(serverIsAlive)));
    }
}
