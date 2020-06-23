package ca.uhn.fhir.jpa.empi.svc;

import ca.uhn.fhir.empi.rules.json.EmpiResourceSearchParamJson;
import ca.uhn.fhir.jpa.empi.BaseEmpiR4Test;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Patient;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EmpiCandidateSearchCriteriaBuilderSvcTest extends BaseEmpiR4Test {
	@Autowired
	EmpiCandidateSearchCriteriaBuilderSvc myEmpiCandidateSearchCriteriaBuilderSvc;

	@Test
	public void testEmptyCase() {
		Patient patient = new Patient();
		EmpiResourceSearchParamJson searchParamJson = new EmpiResourceSearchParamJson();
		searchParamJson.addSearchParam("family");
		Optional<String> result = myEmpiCandidateSearchCriteriaBuilderSvc.buildResourceQueryString("Patient", patient, Collections.emptyList(), searchParamJson);
		assertFalse(result.isPresent());
	}

	@Test
	public void testSimpleCase() {
		Patient patient = new Patient();
		patient.addName().setFamily("Fernandez");
		EmpiResourceSearchParamJson searchParamJson = new EmpiResourceSearchParamJson();
		searchParamJson.addSearchParam("family");
		Optional<String> result = myEmpiCandidateSearchCriteriaBuilderSvc.buildResourceQueryString("Patient", patient, Collections.emptyList(), searchParamJson);
		assertTrue(result.isPresent());
		assertEquals("Patient?family=Fernandez", result.get());
	}

	@Test
	public void testComplexCase() {
		Patient patient = new Patient();
		HumanName humanName = patient.addName();
		humanName.addGiven("Jose");
		humanName.setFamily("Fernandez");
		EmpiResourceSearchParamJson searchParamJson = new EmpiResourceSearchParamJson();
		searchParamJson.addSearchParam("given");
		searchParamJson.addSearchParam("family");
		Optional<String> result = myEmpiCandidateSearchCriteriaBuilderSvc.buildResourceQueryString("Patient", patient, Collections.emptyList(), searchParamJson);
		assertTrue(result.isPresent());
		assertEquals("Patient?given=Jose&family=Fernandez", result.get());
	}
}
