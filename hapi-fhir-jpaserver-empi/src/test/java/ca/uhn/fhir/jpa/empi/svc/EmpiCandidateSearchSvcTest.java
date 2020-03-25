package ca.uhn.fhir.jpa.empi.svc;

import ca.uhn.fhir.jpa.empi.BaseEmpiR4Test;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Patient;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

public class EmpiCandidateSearchSvcTest extends BaseEmpiR4Test {
	@Autowired
	EmpiCandidateSearchSvc myEmpiCandidateSearchSvc;

	@Test
	public void findCandidates() {
		Patient createdJane = createPatient(buildPatientWithNameAndId("Jane", JANE_ID));
		Patient newJane = buildPatientWithNameAndId("Jane", JANE_ID);

		List<IBaseResource> result = myEmpiCandidateSearchSvc.findCandidates(newJane);
		assertEquals(1, result.size());
	}
}
