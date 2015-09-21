package server.config;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import server.layouts.HomeLayout;
import server.pages.DifferentFeaturesForDifferentClients;
import server.pages.HomePage;

@Import(RepositoryConfig.class)
@Configuration
@ComponentScan({ "server.rest", "server.security" })
@EnableTransactionManagement
public class SpringConfig implements ApplicationContextAware {

    private static final String HTTP_CLIENT_CONNECTIONS_MAX_PER_ROUTE = "http.client.connections.max.per.route";
    private static final String HTTP_CLIENT_CONNECTIONS_MAX_TOTAL = "http.client.connections.max.total";
    ApplicationContext appContext;
    
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
    
    @Bean
    public static PropertyPlaceholderConfigurer getPropertyPlaceholderConfigurer() {
        PropertyPlaceholderConfigurer ppc = new PropertyPlaceholderConfigurer();
        ppc.setLocation(new ClassPathResource("application.properties"));
        ppc.setIgnoreUnresolvablePlaceholders(true);
        return ppc;
    }
    
    @Bean
    @Autowired
    public HttpClient httpClient(org.apache.commons.configuration.Configuration appConfig) {
        DefaultHttpClient client = new DefaultHttpClient(httpConnectionManager(appConfig));
        return client;
    }

    @Bean
    @Autowired
    public ClientConnectionManager httpConnectionManager(org.apache.commons.configuration.Configuration appConfig) {
        getLog().debug("Returning new ClientConnectionManager");
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
        schemeRegistry.register(new Scheme("https", 443, SSLSocketFactory.getSocketFactory()));

        PoolingClientConnectionManager httpConnectionManager;

        httpConnectionManager = new PoolingClientConnectionManager(schemeRegistry);

        int maxTotal = appConfig.getInt(HTTP_CLIENT_CONNECTIONS_MAX_TOTAL, 30);
        int maxPerRoute = appConfig.getInt(HTTP_CLIENT_CONNECTIONS_MAX_PER_ROUTE, 2);
        getLog().trace("We got this http client configs: maxTotal= {}, maxPerRoute={}", maxTotal, maxPerRoute);

        httpConnectionManager.setMaxTotal(maxTotal);
        httpConnectionManager.setDefaultMaxPerRoute(maxPerRoute);

        return httpConnectionManager;
    }

    public void setApplicationContext(ApplicationContext appCxt) throws BeansException {
        appContext = appCxt;
        getLog().debug("AppContextSet");
    }
    
    private Logger getLog() {
        return LoggerFactory.getLogger(getClass());
    }
}
