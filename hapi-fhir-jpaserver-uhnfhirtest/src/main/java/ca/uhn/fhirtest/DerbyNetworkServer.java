package ca.uhn.fhirtest;

import java.sql.DriverManager;

import org.apache.derby.drda.NetworkServerControl;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

public class DerbyNetworkServer implements InitializingBean, DisposableBean {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(DerbyNetworkServer.class);
	private NetworkServerControl server;
	
	@Override
	public void destroy() throws Exception {
		server.shutdown();
		try {
			ourLog.info("Shutting down derby");
//			DriverManager.getConnection("jdbc:derby:directory:" + System.getProperty("fhir.db.location") + ";shutdown=true");
		} catch (Exception e) {
			ourLog.info("Failed to create database: {}", e.getMessage());
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		server = new NetworkServerControl();
		server.start (null);
	}

}
