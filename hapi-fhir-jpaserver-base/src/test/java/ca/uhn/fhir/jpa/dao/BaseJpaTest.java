package ca.uhn.fhir.jpa.dao;

import java.sql.SQLException;

import org.junit.AfterClass;

public class BaseJpaTest {

	@AfterClass
	public static void afterClassShutdownDerby() throws SQLException {
//		try {
//		DriverManager.getConnection("jdbc:derby:memory:myUnitTestDB;drop=true");
//		} catch (SQLNonTransientConnectionException e) {
//			// expected.. for some reason....
//		}
	}
	
}
