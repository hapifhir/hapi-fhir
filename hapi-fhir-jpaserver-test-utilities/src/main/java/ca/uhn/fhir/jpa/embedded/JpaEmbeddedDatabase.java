package ca.uhn.fhir.jpa.embedded;


import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;

public abstract class JpaEmbeddedDatabase {

	private BasicDataSource myBasicDataSource;

	public abstract void stop();
	public abstract void clearDatabase();
	public abstract ConnectionDetails getConnectionDetails();

	public DataSource getDataSource(){
		if (myBasicDataSource == null) {
			ConnectionDetails connectionDetails = getConnectionDetails();
			BasicDataSource dataSource = new BasicDataSource();
			dataSource.setDriverClassName(connectionDetails.getDriverClassName());
			dataSource.setUsername(connectionDetails.getUsername());
			dataSource.setUrl(connectionDetails.getUrl());
			dataSource.setPassword(connectionDetails.getPassword());
			myBasicDataSource = dataSource;
		}
		return myBasicDataSource;
	}


}
