package ca.uhn.fhir.jpa.config;

import ca.uhn.fhir.jpa.util.TestUtil;
import org.junit.jupiter.api.Test;

public class JpaEntityTest {

	@Test
	public void testEntitiesAreValid() throws Exception {
		//TODO GGG HS: no clue what these tests are truly doing but Subselect is deprecated
		TestUtil.scanEntities(ca.uhn.fhir.jpa.model.entity.ResourceTable.class.getPackage().getName());
		TestUtil.scanEntities(ca.uhn.fhir.jpa.entity.TermConcept.class.getPackage().getName());
	}

	
}
