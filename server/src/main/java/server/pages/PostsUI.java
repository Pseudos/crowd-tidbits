package server.pages;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import server.layouts.PostAdminLayout;

import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Component
public class PostsUI extends UI{
    
    @Autowired
    PostAdminLayout postAdminLayout;
    
    protected void init(VaadinRequest request) {
        VerticalLayout view = postAdminLayout.createLayout();
        setContent(view);
    }

}
