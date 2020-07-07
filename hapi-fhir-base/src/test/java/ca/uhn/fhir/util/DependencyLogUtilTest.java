package ca.uhn.fhir.util;

import ca.uhn.fhir.util.jar.DependencyLogFactory;
import ca.uhn.fhir.util.jar.IDependencyLog;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DependencyLogUtilTest {

	@Test
	public void testDependencyLogFactory() {
		IDependencyLog logger = DependencyLogFactory.createJarLogger();
		assertNotNull(logger);
		logger.logStaxImplementation(DependencyLogUtilTest.class);
	}
}
