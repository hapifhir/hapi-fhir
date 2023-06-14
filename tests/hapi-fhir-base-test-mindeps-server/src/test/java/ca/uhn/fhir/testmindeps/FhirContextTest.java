package ca.uhn.fhir.testmindeps;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import org.junit.jupiter.api.Test;

public class FhirContextTest {

    @Test
    public void testWrongVersionDoesntGetInContext2() {

        FhirContext ctx = FhirContext.forDstu2();
        RuntimeResourceDefinition def = ctx.getResourceDefinition("Patient");
        assertEquals(Patient.class, def.getImplementingClass());
    }
}
