package com.server.test;



import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;

public class DsUtil 
{

	public static DataSource setupDataSource (String jndiName) throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, NamingException
	{
//		DriverManager.registerDriver((Driver) Class.forName("org.mysql.jdbc.Driver").newInstance());
//		BasicDataSource dataSource = new BasicDataSource();
//		dataSource.setUrl("jdbc:mysql://127.0.0.1:3306/ussd_configurator");
//		dataSource.setDriverClassName("org.mysql.jdbc.Driver");
		
		DriverManager.registerDriver((Driver) Class.forName("org.h2.Driver").newInstance());
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl("jdbc:h2:~/CrowdTidbits/test");
		dataSource.setDriverClassName("org.h2.Driver");
		

		SimpleNamingContextBuilder builder = SimpleNamingContextBuilder.emptyActivatedContextBuilder();
		builder.bind(jndiName, dataSource);
		return dataSource;
	}

}
