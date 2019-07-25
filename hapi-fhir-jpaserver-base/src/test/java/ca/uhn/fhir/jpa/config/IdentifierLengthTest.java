package ca.uhn.fhir.jpa.config;

import org.junit.Test;

import ca.uhn.fhir.jpa.entity.ResourceTable;
import ca.uhn.fhir.jpa.util.TestUtil;

public class IdentifierLengthTest {

	@Test
	public void testIdentifierLength() throws Exception {
		TestUtil.scanEntities(ResourceTable.class.getPackage().getName());
	}

	
}
