package de.dietzm.gcs2hana.db;

import java.io.IOException;
import java.sql.Connection;

import javax.sql.DataSource;

import com.sap.db.jdbcext.DataSourceSAP;

public class DatabaseOrganizer {

	private static DatabaseOrganizer instance;
	public static DatabaseOrganizer getInstance(){
		if(instance == null)
			instance = new DatabaseOrganizer();
		return instance;
	}
	
	DataSourceSAP ds = null;
	
	public void initalize() throws IOException{
		
		if(ds != null)
			return;
		
		String username = "SYSTEM";
		String password = "Walldorf1";
		String hostname = "mo-c3eba72a1.mo.sap.corp";
		String portnumb = "30015";
		String schema   = "SYSTEM";

		
		int port = new Integer(portnumb);
		
		ds = new DataSourceSAP();
		ds.setServerName(hostname);
		ds.setPort(port);
		ds.setUser(username);
		ds.setPassword(password);
		
		ds.setSchema(schema);
		
		
	}
	
	public DataSource getDataSourceSAP() throws Exception{
		initalize();
		return ds;
	}
	
	public Connection getConnection() throws Exception{		
		DataSource datasource = getDataSourceSAP();
		Connection c=  datasource.getConnection();
		
		return c;
	}
}
