package de.bhopp.ml.ui.cancer;

import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("cancer")
@PageTitle("Cancer Detection")
@Menu(order = 1, icon = "vaadin:doctor", title = "Cancer Detection")
class CancerView extends Main {
    public CancerView(CancerMainLayout mainLayout) {
        setSizeFull();
        add(mainLayout);
    }
}
