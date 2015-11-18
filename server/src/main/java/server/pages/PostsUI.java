package server.pages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import server.layouts.PostAdminLayout;

import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Component
public class PostsUI extends UI{
    
    private static final long serialVersionUID = 2983042721635017712L;
    @Autowired
    PostAdminLayout postAdminLayout;
    
    @Override
    protected void init(VaadinRequest request) {
        VerticalLayout view = postAdminLayout.createLayout();
        setContent(view);
    }

}
