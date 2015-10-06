package ca.uhn.fhir.util;

import static org.junit.Assert.*;

import org.junit.Test;

import ca.uhn.fhir.util.jar.DependencyLogFactory;

public class DependencyLogUtilTest {

	@Test
	public void testDependencyLogFactory() {
		assertNotNull(DependencyLogFactory.createJarLogger());
	}
}
