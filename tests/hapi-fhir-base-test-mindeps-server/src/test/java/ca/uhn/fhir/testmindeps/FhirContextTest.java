package ca.uhn.fhir.testmindeps;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.model.dstu2.resource.Patient;

public class FhirContextTest {

	@Test
	public void testWrongVersionDoesntGetInContext2() {

		FhirContext ctx = FhirContext.forDstu2();
		RuntimeResourceDefinition def = ctx.getResourceDefinition("Patient");
		assertEquals(Patient.class, def.getImplementingClass());
	}

}
