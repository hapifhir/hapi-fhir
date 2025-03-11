package ca.uhn.fhir.narrative2;

import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.Medication;
import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.Procedure;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NarrativeGeneratorTemplateUtilsTest {

	@Test
	public void testBundleHasEntriesWithResourceType_True() {
		Bundle bundle = new Bundle();
		bundle.addEntry().setResource(new Patient().setActive(true));
		bundle.addEntry().setResource(new Medication().setIsBrand(true));
		assertTrue(NarrativeGeneratorTemplateUtils.INSTANCE.bundleHasEntriesWithResourceType(bundle, "Patient"));
	}

	@Test
	public void testBundleHasEntriesWithResourceType_False() {
		Bundle bundle = new Bundle();
		bundle.addEntry().setResource(new Medication().setIsBrand(true));
		assertFalse(NarrativeGeneratorTemplateUtils.INSTANCE.bundleHasEntriesWithResourceType(bundle, "Patient"));
	}

	@Test
	public void testResourcesHaveCodeValue_isTrue() {
		Bundle bundle = new Bundle();
		bundle.addEntry().setResource(new Observation().setCode(
			 new CodeableConcept(
				  new Coding("http://loinc.org", "123", ""))));
		bundle.addEntry().setResource(new Observation().setCode(
			 new CodeableConcept(
				  new Coding("http://loinc.org", "456", ""))));

		assertTrue(NarrativeGeneratorTemplateUtils.INSTANCE
			 .bundleHasEntriesWithCode(bundle, "Observation", "http://loinc.org",  "123"));
	}

	@Test
	public void testResourcesHaveCodeValue_isTrueWhenOneCodeMatches() {
		Bundle bundle = new Bundle();
		bundle.addEntry().setResource(new Observation().setCode(
			 new CodeableConcept()
				  .addCoding(new Coding("http://loinc.org", "abc", ""))
				  .addCoding(new Coding("http://loinc.org", "123", ""))
		));
		bundle.addEntry().setResource(new Observation().setCode(
			 new CodeableConcept(
				  new Coding("http://loinc.org", "456", ""))));

		assertTrue(NarrativeGeneratorTemplateUtils.INSTANCE
			 .bundleHasEntriesWithCode(bundle, "Observation", "http://loinc.org",  "123"));
	}

	@Test
	public void testResourcesHaveCodeValue_isFalse() {
		Bundle bundle = new Bundle();
		bundle.addEntry().setResource(new Observation().setCode(
			 new CodeableConcept(
				  new Coding("http://loinc.org", "123", ""))));
		bundle.addEntry().setResource(new Observation().setCode(
			 new CodeableConcept(
				  new Coding("http://loinc.org", "456", ""))));

		assertFalse(NarrativeGeneratorTemplateUtils.INSTANCE
			 .bundleHasEntriesWithCode(bundle, "Observation", "http://loinc.org",  "789"));
	}

	@Test
	public void testResourcesHaveCodeValue_isFalseWhenNoResourcePresent() {
		Bundle bundle = new Bundle();
		bundle.addEntry().setResource(new Patient().setActive(true));
		bundle.addEntry().setResource(new Medication().setIsBrand(true));
		bundle.addEntry().setResource(new Procedure().setCode(new CodeableConcept(new Coding("http://loinc.org", "789", ""))));

		assertFalse(NarrativeGeneratorTemplateUtils.INSTANCE
			 .bundleHasEntriesWithCode(bundle, "Observation", "http://loinc.org",  "789"));
	}

	@Test
	public void testResourcesDoNotHaveCodeValue_isTrue() {
		Bundle bundle = new Bundle();
		bundle.addEntry().setResource(new Observation().setCode(
			 new CodeableConcept(
				  new Coding("http://loinc.org", "123", ""))));
		bundle.addEntry().setResource(new Observation().setCode(
			 new CodeableConcept(
				  new Coding("http://loinc.org", "456", ""))));

		assertTrue(NarrativeGeneratorTemplateUtils.INSTANCE
			 .bundleHasEntriesWithoutCode(bundle, "Observation", "http://loinc.org",  "456"));
	}

	@Test
	public void testResourcesDoNotHaveCodeValue_isFalse() {
		Bundle bundle = new Bundle();
		bundle.addEntry().setResource(new Observation().setCode(
			 new CodeableConcept(
				  new Coding("http://loinc.org", "456", ""))));
		bundle.addEntry().setResource(new Observation().setCode(
			 new CodeableConcept()
				  .addCoding(new Coding("http://loinc.org", "abc", ""))
				  .addCoding(new Coding("http://loinc.org", "456", ""))
		));
		assertFalse(NarrativeGeneratorTemplateUtils.INSTANCE
			 .bundleHasEntriesWithoutCode(bundle, "Observation", "http://loinc.org",  "456"));
	}
}
