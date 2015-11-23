package server.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
      auth.inMemoryAuthentication().withUser("sydney").password("sydney").roles("ADMIN");
      auth.inMemoryAuthentication().withUser("collen").password("collen").roles("ADMIN");
    }
    
    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("sydney").password("sydney").roles("ADMIN");
        auth.inMemoryAuthentication().withUser("collen").password("collen").roles("ADMIN");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

      http.authorizeRequests()
        .antMatchers("/ui/**").access("hasRole('ROLE_ADMIN')")
        .and().formLogin();
        
    }
    
    @org.springframework.context.annotation.Configuration
    @Order(1)
    public static class RestSecurityConfig extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.headers().contentTypeOptions().and().headers().frameOptions().and().headers().xssProtection()
                .and().headers().httpStrictTransportSecurity().and().csrf().disable().authorizeRequests()
                .antMatchers("/ui/**").access("hasRole('ROLE_ADMIN')")
                .and().formLogin().usernameParameter("username").passwordParameter("password")
                .and().logout().logoutSuccessUrl("/ui/");
        }
    }
}