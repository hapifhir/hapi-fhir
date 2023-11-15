package ca.uhn.fhir.jpa.config;

import ca.uhn.fhir.test.utilities.jpa.JpaModelScannerAndVerifier;
import org.junit.jupiter.api.Test;

public class JpaEntityTest {

	@Test
	public void testEntitiesAreValid() throws Exception {
		new JpaModelScannerAndVerifier().scanEntities(
			ca.uhn.fhir.jpa.model.entity.ResourceTable.class.getPackage().getName(),
			ca.uhn.fhir.jpa.entity.TermConcept.class.getPackage().getName()
		);
	}

	
}
