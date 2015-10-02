package server.layouts;

import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class HomeLayout {
    public VerticalLayout createLayout() {
        VerticalLayout base = new VerticalLayout();
        Label hello = new Label("Hello Vaadin!");
        base.addComponent(hello);
        return base;
    }
}
