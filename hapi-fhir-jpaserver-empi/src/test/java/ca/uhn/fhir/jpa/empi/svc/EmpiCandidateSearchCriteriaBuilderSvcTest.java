package ca.uhn.fhir.jpa.empi.svc;

import ca.uhn.fhir.empi.rules.json.EmpiResourceSearchParamJson;
import ca.uhn.fhir.jpa.empi.BaseEmpiR4Test;
import ca.uhn.fhir.jpa.empi.svc.candidate.EmpiCandidateSearchCriteriaBuilderSvc;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
		humanName.addGiven("Martin");
		humanName.setFamily("Fernandez");
		EmpiResourceSearchParamJson searchParamJson = new EmpiResourceSearchParamJson();
		searchParamJson.addSearchParam("given");
		searchParamJson.addSearchParam("family");
		Optional<String> result = myEmpiCandidateSearchCriteriaBuilderSvc.buildResourceQueryString("Patient", patient, Collections.emptyList(), searchParamJson);
		assertTrue(result.isPresent());
		assertThat(result.get(), anyOf(equalTo("Patient?given=Jose,Martin&family=Fernandez"), equalTo("Patient?given=Martin,Jose&family=Fernandez")));
	}

	@Test
	public void testIdentifier() {
		Patient patient = new Patient();
		patient.addIdentifier().setSystem("urn:oid:1.2.36.146.595.217.0.1").setValue("12345");
		EmpiResourceSearchParamJson searchParamJson = new EmpiResourceSearchParamJson();
		searchParamJson.addSearchParam("identifier");
		Optional<String> result = myEmpiCandidateSearchCriteriaBuilderSvc.buildResourceQueryString("Patient", patient, Collections.emptyList(), searchParamJson);
		assertTrue(result.isPresent());
		assertEquals(result.get(), "Patient?identifier=urn:oid:1.2.36.146.595.217.0.1|12345");
	}

	@Test
	public void testOmittingCandidateSearchParamsIsAllowed() {
		Patient patient = new Patient();
		Optional<String> result = myEmpiCandidateSearchCriteriaBuilderSvc.buildResourceQueryString("Patient", patient, Collections.emptyList(), null);
		assertThat(result.isPresent(), is(true));
		assertThat(result.get(), is(equalTo("Patient?")));
	}

	@Test
	public void testEmptyCandidateSearchParamsWorksInConjunctionWithFilterParams() {
		Patient patient = new Patient();
		List<String> filterParams = Collections.singletonList("active=true");
		Optional<String> result = myEmpiCandidateSearchCriteriaBuilderSvc.buildResourceQueryString("Patient", patient, filterParams, null);
		assertThat(result.isPresent(), is(true));
		assertThat(result.get(), is(equalTo("Patient?active=true")));
	}
}
