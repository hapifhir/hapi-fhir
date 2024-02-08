package ca.uhn.fhir.util;

import ca.uhn.fhir.util.jar.DependencyLogFactory;
import ca.uhn.fhir.util.jar.IDependencyLog;
import org.junit.jupiter.api.Test;

public class DependencyLogUtilTest {

	@Test
	public void testDependencyLogFactory() {
		IDependencyLog logger = DependencyLogFactory.createJarLogger();
		assertThat(logger).isNotNull();
		logger.logStaxImplementation(DependencyLogUtilTest.class);
	}
}
