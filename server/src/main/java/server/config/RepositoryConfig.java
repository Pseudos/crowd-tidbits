package server.config;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;

/**
 * 
 * @author Sydney
 *
 */

@Configuration
public class RepositoryConfig {

	@Value("${jdbc.driverClassName}")
	private String driverClassName;
	@Value("${jdbc.url}")
	private String url;
	@Value("${jdbc.username}")
	private String username;
	@Value("${jdbc.password}")
	private String password;
	
	@Value("${hibernate.dialect}")
	private String hibernateDialect;
	@Value("${hibernate.show_sql}")
	private String hibernateShowSql;
	@Value("${hibernate.hbm2ddl.auto}")
	private String hibernateHbm2ddlAuto;

	@Value("${jndi.ds.name}")
	private String jndiDsName;

	@Value("${dbcp.initialSize}")
	private String initialSize;
	@Value("${dbcp.maxActive}")
	private String maxActive;
	@Value("${dbcp.maxIdle}")
	private String maxIdle;

	@Bean()
	public DataSource getDataSource() {
		DataSource ret = null;
		try {
			Context cxt = new InitialContext();
			ret = (DataSource) cxt.lookup(jndiDsName);
			getLog().debug("We got this datasource from jndi lookup: {}", ret);
		}
		catch (Exception e) {
			getLog().warn(
					"Failed to instantiate InitialContext, and/or lookup DataSource. Bummer. Anycase, we will not try to do it manually, based on stuff found in application.properties",
					e);
		}

		if (ret == null) {
			ret = buildDbcpDs();
		}

		if (ret == null) {
			DriverManagerDataSource ds = new DriverManagerDataSource();
			ds.setDriverClassName(driverClassName);
			ds.setUrl(url);
			ds.setUsername(username);
			ds.setPassword(password);
			ret = ds;
		}
		return ret;
	}

	private DataSource buildDbcpDs() {
		BasicDataSource bds = new BasicDataSource();
		bds.setDriverClassName(driverClassName);
		bds.setUrl(url);
		bds.setUsername(username);
		bds.setPassword(password);
		bds.setInitialSize(Integer.valueOf(initialSize));
		bds.setMaxActive(Integer.valueOf(maxActive));
		bds.setMaxIdle(Integer.valueOf(maxIdle));

		return bds;
	}

	private Logger getLog() {
		return LoggerFactory.getLogger(getClass());
	}

	@Bean
	public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
		HibernateTransactionManager htm = new HibernateTransactionManager();
		htm.setSessionFactory(sessionFactory().getObject());
		return htm;
	}

	@Bean
	public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
		return new PersistenceExceptionTranslationPostProcessor();
	}

	@Bean
	public LocalSessionFactoryBean sessionFactory() {
		LocalSessionFactoryBean asfb = new LocalSessionFactoryBean();
		asfb.setDataSource(getDataSource());
		asfb.setHibernateProperties(hibernateProperties());
		asfb.setPackagesToScan(new String[] { "server.entity" });
		return asfb;
	}

	@Bean
	public Properties hibernateProperties() {
		Properties properties = new Properties();
		properties.put("hibernate.dialect", hibernateDialect);
		properties.put("hibernate.show_sql", hibernateShowSql);
		properties.put("hibernate.hbm2ddl.auto", hibernateHbm2ddlAuto);

		properties.put("hibernate.ejb.event.post-insert",
				"org.hibernate.ejb.event.EJB3PostInsertEventListener,org.hibernate.envers.event.AuditEventListener");
		properties.put("hibernate.ejb.event.post-update",
				"org.hibernate.ejb.event.EJB3PostUpdateEventListener,org.hibernate.envers.event.AuditEventListener");
		properties.put("hibernate.ejb.event.post-delete",
				"org.hibernate.ejb.event.EJB3PostDeleteEventListener,org.hibernate.envers.event.AuditEventListener");
		properties.put("hibernate.ejb.event.pre-collection-update", "org.hibernate.envers.event.AuditEventListener");
		properties.put("hibernate.ejb.event.pre-collection-remove", "org.hibernate.envers.event.AuditEventListener");
		properties.put("hibernate.ejb.event.post-collection-recreate", "org.hibernate.envers.event.AuditEventListener");

		return properties;
	}
}
