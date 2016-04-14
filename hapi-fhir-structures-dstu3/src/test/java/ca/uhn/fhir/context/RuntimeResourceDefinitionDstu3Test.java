package ca.uhn.fhir.context;

import static org.junit.Assert.*;

import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Patient;
import org.junit.AfterClass;
import org.junit.Test;

import ca.uhn.fhir.util.TestUtil;

public class RuntimeResourceDefinitionDstu3Test {

	private FhirContext ourCtx = FhirContext.forDstu3();
	
	@Test
	public void testAsClass() {
		assertEquals(Bundle.class, ourCtx.getResourceDefinition("Bundle").getImplementingClass(Bundle.class));
	}
	
	@Test(expected=ConfigurationException.class)
	public void testAsClassWrong() {
		ourCtx.getResourceDefinition("Bundle").getImplementingClass(Patient.class);
	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}


}
