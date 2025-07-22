package ca.uhn.fhir.util;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Include;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.Reference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ResourceReferenceInfoTest {

	private ResourceReferenceInfo resourceReferenceInfo;

	private final FhirContext fhirContext = FhirContext.forDstu3();

	@BeforeEach
	public void setUp() {
		Reference theElement = new Reference()
			 .setReference("Practitioner/123")
			 .setDisplay("Test Practitioner");

		Patient theOwningResource = new Patient()
			 .addGeneralPractitioner(theElement);

		resourceReferenceInfo =
			 new ResourceReferenceInfo(fhirContext, theOwningResource, List.of("generalPractitioner"), theElement);
	}

	@Test
	public void matchesInclude_hasTargetResourceType_matched() {
		assertTrue(resourceReferenceInfo.matchesInclude(new Include("Patient:general-practitioner:Practitioner")));
	}

	@Test
	public void matchesInclude_hasNotTargetResourceType_matched() {
		assertTrue(resourceReferenceInfo.matchesInclude(new Include("Patient:general-practitioner")));
	}

	@Test
	public void matchesInclude_hasDifferentTargetResourceType_notMatched() {
		assertFalse(resourceReferenceInfo.matchesInclude(new Include("Patient:general-practitioner:Organization")));
	}
}
