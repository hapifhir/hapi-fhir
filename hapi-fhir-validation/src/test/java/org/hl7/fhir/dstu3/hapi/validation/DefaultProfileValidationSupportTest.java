package org.hl7.fhir.dstu3.hapi.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.util.TestUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class DefaultProfileValidationSupportTest {

	private DefaultProfileValidationSupport mySvc = new DefaultProfileValidationSupport(ourCtx);
	private static FhirContext ourCtx = FhirContext.forDstu3();
	
	@Test
	public void testGetStructureDefinitionsWithRelativeUrls() {
		assertNotNull(mySvc.fetchStructureDefinition("http://hl7.org/fhir/StructureDefinition/Extension"));
		assertNotNull(mySvc.fetchStructureDefinition("StructureDefinition/Extension"));
		assertNotNull(mySvc.fetchStructureDefinition("Extension"));
		
		assertNull(mySvc.fetchStructureDefinition("http://hl7.org/fhir/StructureDefinition/Extension2"));
		assertNull(mySvc.fetchStructureDefinition("StructureDefinition/Extension2"));
		assertNull(mySvc.fetchStructureDefinition("Extension2"));

	}
	
	
	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
