package server.pages;

import org.springframework.stereotype.Component;

import ru.xpoft.vaadin.SpringUIProvider;

import com.vaadin.server.VaadinRequest;

@Component
public class DifferentFeaturesForDifferentClients extends SpringUIProvider {
    protected String getUIBeanName(VaadinRequest request) {
        // Display home page if root URL is called
        if (request.getPathInfo() == null) {
            return "HomePage";
        }

        if (request.getPathInfo().contains("users")) {
            return "UsersUI";
        }

        // Again, the returned String must be the same as the UI class name
        // Display the home page is the URL doesn't match anything here
        else {
            return "HomePage";
        }
    }
}
