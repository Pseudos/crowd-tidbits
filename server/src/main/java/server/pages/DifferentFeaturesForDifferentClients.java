package server.pages;

import org.springframework.stereotype.Component;

import ru.xpoft.vaadin.SpringUIProvider;

import com.vaadin.server.VaadinRequest;

/**
 * 
 * @author Sydney
 *
 */

@Component
public class DifferentFeaturesForDifferentClients extends SpringUIProvider {
    protected String getUIBeanName(VaadinRequest request) {
        // Display home page if root URL is called
        if (request.getPathInfo() == null) {
            return "HomePage";
        }
        else if (request.getPathInfo().contains("users")) {
            return "UsersUI";
        }
        else if (request.getPathInfo().contains("posts")) {
            return "PostsUI";
        }
        else {
            return "HomePage";
        }
    }
}
