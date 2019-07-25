package org.hl7.fhir.dstu3.hapi.validation;

import static org.junit.Assert.*;

import org.hl7.fhir.dstu3.hapi.validation.DefaultProfileValidationSupport;
import org.junit.AfterClass;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.util.TestUtil;

public class DefaultProfileValidationSupportTest {

	private DefaultProfileValidationSupport mySvc = new DefaultProfileValidationSupport();
	private static FhirContext ourCtx = FhirContext.forDstu3();
	
	@Test
	public void testGetStructureDefinitionsWithRelativeUrls() {
		assertNotNull(mySvc.fetchStructureDefinition(ourCtx, "http://hl7.org/fhir/StructureDefinition/Extension"));
		assertNotNull(mySvc.fetchStructureDefinition(ourCtx, "StructureDefinition/Extension"));
		assertNotNull(mySvc.fetchStructureDefinition(ourCtx, "Extension"));
		
		assertNull(mySvc.fetchStructureDefinition(ourCtx, "http://hl7.org/fhir/StructureDefinition/Extension2"));
		assertNull(mySvc.fetchStructureDefinition(ourCtx, "StructureDefinition/Extension2"));
		assertNull(mySvc.fetchStructureDefinition(ourCtx, "Extension2"));

	}
	
	
	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
