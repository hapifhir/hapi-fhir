package ca.uhn.fhir.narrative2;

import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Medication;
import org.hl7.fhir.dstu3.model.Patient;
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


}
