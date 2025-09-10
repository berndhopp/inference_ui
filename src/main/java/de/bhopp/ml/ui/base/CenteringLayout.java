package de.bhopp.ml.ui.base;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.FlexLayout;

public abstract class CenteringLayout extends FlexLayout {
    protected CenteringLayout(Component... components) {
        setSizeFull();
        setFlexDirection(FlexDirection.ROW);
        getStyle().set("gap", "var(--lumo-space-m)");
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        add(components);
    }
}
