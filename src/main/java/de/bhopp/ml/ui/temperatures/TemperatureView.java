package de.bhopp.ml.ui.temperatures;

import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("")
@PageTitle("Temperature Calculator")
@Menu(order = 0, icon = "vaadin:dashboard", title = "Temperatures")
class TemperatureView extends Main {
    public TemperatureView(TemperatureMainLayout mainLayout) {
        setSizeFull();
        add(mainLayout);
    }
}
