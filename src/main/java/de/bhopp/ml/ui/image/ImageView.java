package de.bhopp.ml.ui.image;

import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("image_classifier")
@PageTitle("Image Classifier")
@Menu(order = 1, icon = "vaadin:image", title = "Image Classifier")
class ImageView extends Main {
    public ImageView(ImageMainLayout mainLayout) {
        setSizeFull();
        add(mainLayout);
    }
}
