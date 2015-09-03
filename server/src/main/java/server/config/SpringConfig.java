package server.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;



import server.layouts.HomeLayout;
import server.pages.DifferentFeaturesForDifferentClients;
import server.pages.HomePage;

@Configuration
public class SpringConfig implements ApplicationContextAware {
    ApplicationContext appContext;

    /*
     * Since there is no explicit Main class, we can't explicitly set the
     * context. Thus... It is necessary to make Spring context aware by
     * implementing ApplicationContextAware
     */
    public void setApplicationContext(ApplicationContext context)
            throws BeansException {
        appContext = context;
    }
    
    // UIs
    @Bean
    @Scope("prototype")
    public DifferentFeaturesForDifferentClients differentFeaturesForDifferentClients() {
        return new DifferentFeaturesForDifferentClients();
    }

    @Bean
    @Scope("prototype")
    public HomePage HomePage() {
        return new HomePage();
    }

    @Bean
    @Scope("request")
    public HomeLayout homeLayout() {
        return new HomeLayout();
    }
}
