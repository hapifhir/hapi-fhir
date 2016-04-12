package ca.uhn.fhirtest;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

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
			
			Enumeration<Driver> drivers = DriverManager.getDrivers();
		    while (drivers.hasMoreElements()) {
		        Driver driver = drivers.nextElement();
		        try {
		      	  ourLog.error("Unregistering driver: {}", driver.getClass());
		            DriverManager.deregisterDriver(driver);
		        } catch (SQLException e) {
		      	  ourLog.error("Failed to unregister driver", e);
		        }
		    }

		} catch (Exception e) {
			ourLog.info("Failed to shut down database: {}", e.getMessage());
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		server = new NetworkServerControl();
		Writer w = new Writer() {

			@Override
			public void write(char[] theCbuf, int theOff, int theLen) throws IOException {
				ourLog.info("[DERBY] " + new String(theCbuf, theOff, theLen));
			}

			@Override
			public void flush() throws IOException {
				// nothing
			}

			@Override
			public void close() throws IOException {
				// nothing
			}};
		server.start (new PrintWriter(w));
	}

	
	public static void main(String[] args) throws Exception {
		DerbyNetworkServer s = new DerbyNetworkServer();
		s.afterPropertiesSet();
		s.destroy();
	}
	
}
