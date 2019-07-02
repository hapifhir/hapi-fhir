package ca.uhn.fhir.jpa.config;

import ca.uhn.fhir.jpa.util.TestUtil;
import org.junit.Test;

public class IdentifierLengthTest {

	@Test
	public void testIdentifierLength() throws Exception {
		TestUtil.scanEntities(ca.uhn.fhir.jpa.model.entity.ResourceTable.class.getPackage().getName());
		TestUtil.scanEntities(ca.uhn.fhir.jpa.entity.TermConcept.class.getPackage().getName());
	}

	
}
