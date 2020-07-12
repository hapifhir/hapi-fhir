package ca.uhn.fhir.jpa.empi.svc;

import ca.uhn.fhir.jpa.empi.BaseEmpiR4Test;
import ca.uhn.fhir.jpa.empi.svc.candidate.EmpiCandidateSearchSvc;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EmpiCandidateSearchSvcTest extends BaseEmpiR4Test {

	@Autowired
    EmpiCandidateSearchSvc myEmpiCandidateSearchSvc;

	@Test
	public void testFindCandidates() {
		Patient jane = buildJanePatient();
		jane.setActive(true);
		createPatient(jane);
		Patient newJane = buildJanePatient();

		Collection<IAnyResource> result = myEmpiCandidateSearchSvc.findCandidates("Patient", newJane);
		assertEquals(1, result.size());
	}


	@Test
	public void findCandidatesMultipleMatchesDoNotCauseDuplicates() {
		Date today = new Date();
		Patient jane = buildJaneWithBirthday(today);

		jane.setActive(true);
		createPatient(jane);

		Patient newJane = buildJaneWithBirthday(today);

		Collection<IAnyResource> result = myEmpiCandidateSearchSvc.findCandidates("Patient", newJane);
		assertEquals(1, result.size());
	}

	@Test
	public void testFindCandidatesCorrectlySearchesWithReferenceParams() {
		Practitioner practitioner = new Practitioner();
		Practitioner practitionerAndUpdateLinks = createPractitionerAndUpdateLinks(practitioner);

		Patient managedPatient = new Patient();
		managedPatient.setActive(true);
		managedPatient.setGeneralPractitioner(Collections.singletonList(new Reference(practitionerAndUpdateLinks.getId())));
		createPatient(managedPatient);

		Patient incomingPatient = new Patient();
		incomingPatient.setActive(true);
		incomingPatient.setGeneralPractitioner(Collections.singletonList(new Reference(practitionerAndUpdateLinks.getId())));

		Collection<IAnyResource> patient = myEmpiCandidateSearchSvc.findCandidates("Patient", incomingPatient);
		assertThat(patient, hasSize(1));
	}
}
