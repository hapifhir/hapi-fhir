package ca.uhn.fhir.context;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Patient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class RuntimeResourceDefinitionDstu3Test {

	private FhirContext ourCtx = FhirContext.forDstu3();
	
	@Test
	public void testAsClass() {
		assertEquals(Bundle.class, ourCtx.getResourceDefinition("Bundle").getImplementingClass(Bundle.class));
	}
	
	@Test()
	public void testAsClassWrong() {
		try {
			ourCtx.getResourceDefinition("Bundle").getImplementingClass(Patient.class);
			fail();
		} catch (ConfigurationException e) {
			assertEquals(Msg.code(1732) + "Unable to convert class org.hl7.fhir.dstu3.model.Bundle to class org.hl7.fhir.dstu3.model.Patient", e.getMessage());
		}
	}

	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.randomizeLocaleAndTimezone();
	}


}
