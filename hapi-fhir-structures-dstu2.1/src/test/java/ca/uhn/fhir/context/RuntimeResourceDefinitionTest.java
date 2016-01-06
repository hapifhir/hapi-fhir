package ca.uhn.fhir.context;

import static org.junit.Assert.*;

import org.hl7.fhir.dstu21.model.Bundle;
import org.hl7.fhir.dstu21.model.Patient;
import org.junit.Test;

public class RuntimeResourceDefinitionTest {

	private FhirContext ourCtx = FhirContext.forDstu2_1();
	
	@Test
	public void testAsClass() {
		assertEquals(Bundle.class, ourCtx.getResourceDefinition("Bundle").getImplementingClass(Bundle.class));
	}
	
	@Test(expected=ConfigurationException.class)
	public void testAsClassWrong() {
		ourCtx.getResourceDefinition("Bundle").getImplementingClass(Patient.class);
	}

}
