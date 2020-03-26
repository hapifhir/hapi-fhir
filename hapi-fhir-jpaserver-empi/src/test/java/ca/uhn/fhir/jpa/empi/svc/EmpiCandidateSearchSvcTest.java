package ca.uhn.fhir.jpa.empi.svc;

import ca.uhn.fhir.jpa.empi.BaseEmpiR4Test;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Patient;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;

import static org.junit.Assert.assertEquals;

public class EmpiCandidateSearchSvcTest extends BaseEmpiR4Test {
	@Autowired
	EmpiCandidateSearchSvc myEmpiCandidateSearchSvc;

	@Test
	public void findCandidates() {
		Patient jane = buildPatientWithNameAndId("Jane", JANE_ID);
		jane.setActive(true);
		Patient createdJane = createPatient(jane);
		Patient newJane = buildPatientWithNameAndId("Jane", JANE_ID);

		Collection<IBaseResource> result = myEmpiCandidateSearchSvc.findCandidates("Patient", newJane);
		assertEquals(1, result.size());
	}
}
