package ca.uhn.fhir.testmindeps;

import static org.junit.Assert.*;

import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.model.dstu.resource.Patient;

public class FhirContextTest {

	@Test
	public void testWrongVersionDoesntGetInContext1() {

		FhirContext ctx = FhirContext.forDstu1();
		RuntimeResourceDefinition def = ctx.getResourceDefinition("Patient");
		assertEquals(Patient.class, def.getImplementingClass());
	}

}
