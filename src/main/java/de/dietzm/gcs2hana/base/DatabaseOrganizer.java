package de.dietzm.gcs2hana.base;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Properties;

import javax.sql.DataSource;

import com.sap.db.jdbcext.DataSourceSAP;

public class DatabaseOrganizer {

	private static HashMap<String, DatabaseOrganizer> instances;


	public static DatabaseOrganizer getInstance(String destinationName) {
		if(instances == null){
			instances = new HashMap<String, DatabaseOrganizer>();
		}
		
		if(!instances.containsKey(destinationName)){
			DatabaseOrganizer newInstance = new DatabaseOrganizer(destinationName);
			instances.put(destinationName, newInstance);
			return newInstance;
		} else {
			return instances.get(destinationName);
		}

	}
	
	private DataSourceSAP ds = null;
	private String destinationName; 

	private DatabaseOrganizer(String destinationName) {
		this.destinationName = destinationName;
	}

	public void initalize() throws IOException{
		
		if(ds != null)
			return;

		Properties prop = new Properties();
		prop.load(new FileReader("destinations/" + destinationName + ".destination"));
		
		String username = prop.getProperty("USERNAME");
		String password = prop.getProperty("PASSWORD");
		String hostname = prop.getProperty("HOST");
		String portnumb = prop.getProperty("PORT");
		String schema   = prop.getProperty("SCHEMA");

		int port = new Integer(portnumb);
		
		ds = new DataSourceSAP();
		ds.setServerName(hostname);
		ds.setPort(port);
		ds.setUser(username);
		ds.setPassword(password);
		
		ds.setSchema(schema);
		
	}
	
	private DataSource getDataSourceSAP() throws Exception{
		initalize();
		return ds;
	}
	
	public Connection getConnection() throws Exception{		
		DataSource datasource = getDataSourceSAP();
		Connection c=  datasource.getConnection();
		
		return c;
	}
}
