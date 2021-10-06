package org.hl7.fhir.dstu3.hapi.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.CodeSystem;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class DefaultProfileValidationSupportTest {

	private static FhirContext ourCtx = FhirContext.forR4Cached();
	private DefaultProfileValidationSupport mySvc = new DefaultProfileValidationSupport(ourCtx);
	
	@Test
	public void testGetStructureDefinitionsWithRelativeUrls() {
		assertNotNull(mySvc.fetchStructureDefinition("http://hl7.org/fhir/StructureDefinition/Extension"));
		assertNotNull(mySvc.fetchStructureDefinition("StructureDefinition/Extension"));
		assertNotNull(mySvc.fetchStructureDefinition("Extension"));
		
		assertNull(mySvc.fetchStructureDefinition("http://hl7.org/fhir/StructureDefinition/Extension2"));
		assertNull(mySvc.fetchStructureDefinition("StructureDefinition/Extension2"));
		assertNull(mySvc.fetchStructureDefinition("Extension2"));

	}
	
	@Test
	public void testLoadCodeSystemWithVersion() {
		CodeSystem cs = (CodeSystem) mySvc.fetchCodeSystem("http://terminology.hl7.org/CodeSystem/v2-0291");
		assertNotNull(cs);
		String version = cs.getVersion();
		assertEquals("2.9", version);

		cs = (CodeSystem) mySvc.fetchCodeSystem("http://terminology.hl7.org/CodeSystem/v2-0291|" + version);
		assertNotNull(cs);

		cs = (CodeSystem) mySvc.fetchCodeSystem("http://terminology.hl7.org/CodeSystem/v2-0291|999");
		assertNotNull(cs);
	}
}
