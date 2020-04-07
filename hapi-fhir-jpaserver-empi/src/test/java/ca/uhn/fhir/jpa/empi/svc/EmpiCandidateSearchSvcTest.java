package ca.uhn.fhir.jpa.empi.svc;

import ca.uhn.fhir.empi.api.IEmpiCandidateSearchSvc;
import ca.uhn.fhir.jpa.empi.BaseEmpiR4Test;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Patient;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class EmpiCandidateSearchSvcTest extends BaseEmpiR4Test {
	@Autowired
	IEmpiCandidateSearchSvc myEmpiCandidateSearchSvc;

	@Test
	public void findCandidates() {
		Patient jane = buildJanePatient();
		jane.setActive(true);
		Patient createdJane = createPatient(jane);
		Patient newJane = buildJanePatient();

		Collection<IBaseResource> result = myEmpiCandidateSearchSvc.findCandidates("Patient", newJane);
		assertEquals(1, result.size());
	}

	@Test
	public void findCandidatesMultipleMatchesDoNotCauseDuplicates() {
		Date today = new Date();
		Patient jane = buildJaneWithBirthday(today);

		jane.setActive(true);
		createPatient(jane);

		Patient newJane = buildJaneWithBirthday(today);

		Collection<IBaseResource> result = myEmpiCandidateSearchSvc.findCandidates("Patient", newJane);
		assertEquals(1, result.size());
	}
}
