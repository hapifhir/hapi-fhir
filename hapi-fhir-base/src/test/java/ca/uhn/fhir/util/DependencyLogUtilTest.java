package ca.uhn.fhir.util;

import static org.junit.Assert.*;

import org.junit.Test;

import ca.uhn.fhir.util.jar.DependencyLogFactory;
import ca.uhn.fhir.util.jar.IDependencyLog;

public class DependencyLogUtilTest {

	@Test
	public void testDependencyLogFactory() {
		IDependencyLog logger = DependencyLogFactory.createJarLogger();
		assertNotNull(logger);
		logger.logStaxImplementation(DependencyLogUtilTest.class);
	}
}
