package server.pages;

import org.springframework.stereotype.Component;

import ru.xpoft.vaadin.SpringUIProvider;

import com.vaadin.server.VaadinRequest;

@Component
public class DifferentFeaturesForDifferentClients extends SpringUIProvider {
    protected String getUIBeanName(VaadinRequest request) {
                //Display home page if root URL is called
        if (request.getPathInfo() == null) {
            return "HomePage";
        }
                //Again, the returned String must be the same as the UI class name
        if (request.getPathInfo().contains("page1")) {
            return "Page1UI";
        }
        else if (request.getPathInfo().contains("page2")) {
            return "Page2UI";
        }
                //Display the home page is the URL doesn't match anything here
        else {return "HomePage";}
    }
}
