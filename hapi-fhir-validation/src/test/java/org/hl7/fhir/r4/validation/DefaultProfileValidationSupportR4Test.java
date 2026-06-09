package org.hl7.fhir.r4.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.fhirpath.BaseValidationTestWithInlineMocks;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ValidationResult;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.CodeSystem;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DefaultProfileValidationSupportR4Test extends BaseValidationTestWithInlineMocks {

	private static final Logger ourLog = LoggerFactory.getLogger(DefaultProfileValidationSupportR4Test.class);
	private static final FhirContext ourCtx = FhirContext.forR4Cached();
	private final DefaultProfileValidationSupport mySvc = new DefaultProfileValidationSupport(ourCtx);

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

	@Test
	public void testLoadStructureDefinitionWithVersion() {
		// Test that versioned HL7 base StructureDefinition URLs fall back to non-versioned URLs
		// This addresses issues where IG Publisher sets versions on profile references
		IBaseResource sd = mySvc.fetchStructureDefinition("http://hl7.org/fhir/StructureDefinition/Medication");
		assertNotNull(sd);

		// Should find the same resource with version appended (fallback behavior for HL7 URLs)
		IBaseResource sdVersioned = mySvc.fetchStructureDefinition("http://hl7.org/fhir/StructureDefinition/Medication|4.0.1");
		assertNotNull(sdVersioned);
		assertEquals(sd, sdVersioned);

		// Should also work with different version numbers for HL7 base URLs
		IBaseResource sdVersioned2 = mySvc.fetchStructureDefinition("http://hl7.org/fhir/StructureDefinition/Medication|999");
		assertNotNull(sdVersioned2);
		assertEquals(sd, sdVersioned2);
	}

	@Test
	public void testValidateBuiltInProfile() {
		IBaseResource address = mySvc.fetchStructureDefinition("http://hl7.org/fhir/StructureDefinition/Address");
		ourLog.info("SD: {}", ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(address));

		FhirValidator val = ourCtx.newValidator();
		val.registerValidatorModule(new FhirInstanceValidator(ourCtx));

		ValidationResult result = val.validateWithResult(address);
		ourLog.info("Validation: {}", ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(result.toOperationOutcome()));
		assertTrue(result.isSuccessful());
	}

	@Test
	public void testFetchAllSearchParams() {
		// Test
		List<IBaseResource> allSps = mySvc.fetchAllSearchParameters();

		// Verify
		assertNotNull(allSps);
		assertEquals(1375, allSps.size());
	}

}
