package ca.uhn.fhir.narrative2;

import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.Medication;
import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.Procedure;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

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

	@Test
	void testNullSafeAccess_validPath() throws NoSuchMethodException {
		Observation obs = new Observation();
		obs.setCode(new CodeableConcept().addCoding(new Coding().setDisplay("Heart Rate")));

		Object result = NarrativeGeneratorTemplateUtils.INSTANCE.nullSafeAccess(obs, "getCode", "getCodingFirstRep", "getDisplay");
		assertEquals("Heart Rate", result);
	}

	@Test
	void testNullSafeAccess_nullIntermediateValue() throws NoSuchMethodException {
		Observation obs = new Observation();
		obs.setCode(new CodeableConcept()); // No coding set

		Object result = NarrativeGeneratorTemplateUtils.INSTANCE.nullSafeAccess(obs, "getCode", "getCodingFirstRep", "getDisplay");
		assertNull(result, "Should return null when accessing missing properties");
	}

	@Test
	void testNullSafeAccess_nullRootObject() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
		Object result = NarrativeGeneratorTemplateUtils.INSTANCE.nullSafeAccess(null, "getCode", "getCodingFirstRep", "getDisplay");
		assertNull(result, "Should return null when the root object is null");
	}

	@Test
	void testNullSafeAccess_partialPath() throws NoSuchMethodException {
		Observation obs = new Observation();
		obs.setCode(new CodeableConcept().addCoding(new Coding()));

		Object result = NarrativeGeneratorTemplateUtils.INSTANCE.nullSafeAccess(obs, "getCode", "getCodingFirstRep");
		assertNotNull(result, "Should return Coding object even if Display is missing");
		assertInstanceOf(Coding.class, result, "Should return an instance of Coding");
	}

	@Test
	void testNullSafeAccess_badMethodName() {
		Observation obs = new Observation();
		NoSuchMethodException nsm = null;
		try {
			NarrativeGeneratorTemplateUtils.INSTANCE.nullSafeAccess(obs, "no-such-method-exists");
		} catch (NoSuchMethodException e) {
			nsm = e;
		}
		assertNotNull(nsm);
	}
}
