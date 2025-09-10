package de.bhopp.ml.ui.tritonStatus;

import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.spring.annotation.UIScope;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@UIScope
@Component
public class TritonStatusInfo extends Checkbox {
    TritonStatusInfo() {
        setReadOnly(true);
        setLabel("Triton Live Status");
        getStyle().setAlignSelf(Style.AlignSelf.CENTER);
    }

    @Scheduled(fixedRate = 2000)
    public void checkTritonStatus(){
        //TODO query the live state from the triton server
        final var serverIsLive = false;

        getUI().ifPresent(ui -> ui.access(() -> setValue(serverIsLive)));
    }
}
