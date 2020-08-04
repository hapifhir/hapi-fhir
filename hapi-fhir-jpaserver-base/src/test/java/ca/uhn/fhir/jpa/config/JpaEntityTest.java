package ca.uhn.fhir.jpa.config;

import ca.uhn.fhir.jpa.util.TestUtil;
import org.junit.jupiter.api.Test;

public class JpaEntityTest {

	@Test
	public void testEntitiesAreValid() throws Exception {
		TestUtil.scanEntities(ca.uhn.fhir.jpa.model.entity.ResourceTable.class.getPackage().getName());
		TestUtil.scanEntities(ca.uhn.fhir.jpa.entity.TermConcept.class.getPackage().getName());
	}

	
}
