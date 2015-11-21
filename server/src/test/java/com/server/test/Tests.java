package com.server.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import server.config.SpringConfig;
import server.dao.UserDao;
import server.entity.User;
import server.rest.request.AuthenticateRequest;
import server.rest.response.DefaultResponse;
import server.service.UserService;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringConfig.class, loader = AnnotationConfigContextLoader.class)
public class Tests {
    private static ApplicationContext appContext;
    private static Logger LOG = LoggerFactory.getLogger(Tests.class);

    @Autowired
    UserDao userDao;
    @Autowired
    private UserService userService;
    
    static DataSource dataSource;

    @BeforeClass
    public static void classSetup() {
        try {
            LOG.debug("Starting tests");

            dataSource = DsUtil
                    .setupDataSource("jdbc/crowd_tidbits");

            Connection conn = null;
            Statement st = null;

            try {
                LOG.debug("Attempting to connect to DB");
                conn = dataSource.getConnection();
                st = conn.createStatement();

                //st.execute("insert into user (username,email,password,enabled) VALUES ('test1','test1@test.com','EAJq1n1RmUc0khsaESJD6ELaKis',1)");

                st.close();
                conn.close();
            } catch (Exception e) {
                LOG.error("DB Connection and setup failed...", e);
            }

            appContext = new AnnotationConfigApplicationContext(
                    SpringConfig.class);
            DataSource ds = appContext.getBean(DataSource.class);
            assertNotNull(ds);
        } catch (Exception e) {
            LoggerFactory.getLogger(Tests.class).error(
                    "ConfigurationTest setup failed...", e);
        }
    }

    @Test
    public void testUser() {
        LOG.debug("Testing user registration");
        LOG.debug("-------------------------");
        User user = new User();
        user.setEmail("test@test.com");
        user.setPassword("EAJq1n1RmUc0khsaESJD6ELaKis");
        user.setUsername("test");
        DefaultResponse resp = userService.registerUser(user);
        assertEquals((int) 202, (int) resp.getStatus());
        
        LOG.debug("Testing findByEmailAndUsername");
        LOG.debug("--------------------------");
        User tuser = userDao.findByEmailAndUsername("test@test.com", "test");

        assertNotNull(user);
        LOG.debug("Found a thing! Result: {}", user.getUsername());
        assertEquals("test", tuser.getUsername());
        assertEquals("EAJq1n1RmUc0khsaESJD6ELaKis", tuser.getPassword());

        LOG.debug("Testing findByNameAndGateway");
        LOG.debug("----------------------------");
        String hash = userDao.findHashByUsername("test");
        assertEquals("EAJq1n1RmUc0khsaESJD6ELaKis", hash);

        LOG.debug("Test user authentication");
        LOG.debug("------------------------");
        AuthenticateRequest req = new AuthenticateRequest();
        req.setTimestamp("100");
        req.setUsername("test");
        req.setAuthHash("HtW9ltBK_h-gmagFcnlP3FGSs6M");
        resp = userService.authenticateUser(req);
        assertEquals((int) 200, (int) resp.getStatus());
        
        userDao.delete(user);

    }

}
