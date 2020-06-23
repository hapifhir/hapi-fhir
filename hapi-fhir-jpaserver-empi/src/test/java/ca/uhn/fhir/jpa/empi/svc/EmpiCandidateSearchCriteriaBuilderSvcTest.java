package ca.uhn.fhir.jpa.empi.svc;

import ca.uhn.fhir.empi.rules.json.EmpiResourceSearchParamJson;
import ca.uhn.fhir.jpa.empi.BaseEmpiR4Test;
import org.hl7.fhir.r4.model.Patient;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.assertFalse;

public class EmpiCandidateSearchCriteriaBuilderSvcTest extends BaseEmpiR4Test {
	@Autowired
	EmpiCandidateSearchCriteriaBuilderSvc myEmpiCandidateSearchCriteriaBuilderSvc;

	@Test
	public void testEmptyCase() {
		Patient patient = new Patient();
		EmpiResourceSearchParamJson searchParamJson = new EmpiResourceSearchParamJson();
		searchParamJson.setSearchParam("family");
		Optional<String> result = myEmpiCandidateSearchCriteriaBuilderSvc.buildResourceQueryString("Patient", patient, Collections.emptyList(), searchParamJson);
		assertFalse(result.isPresent());
	}
}
