package ca.uhn.fhir.jpa.config;

import org.junit.jupiter.api.Test;

import ca.uhn.fhir.test.utilities.jpa.JpaModelScannerAndVerifier;

public class JpaEntityTest {

    @Test
    public void testEntitiesAreValid() throws Exception {
        new JpaModelScannerAndVerifier()
                .scanEntities(
                        ca.uhn.fhir.jpa.model.entity.ResourceTable.class.getPackage().getName(),
                        ca.uhn.fhir.jpa.entity.TermConcept.class.getPackage().getName());
    }
}
