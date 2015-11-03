package server.layouts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vaadin.ui.VerticalLayout;

import server.dao.PostDao;

@Component
public class PostAdminLayout {
    @Autowired
    private PostDao postDao;

    public VerticalLayout createLayout() {
        // TODO Auto-generated method stub
        return null;
    }
}
