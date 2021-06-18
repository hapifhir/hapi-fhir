package ca.uhn.fhir.jpa.mdm.svc;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.jpa.mdm.BaseMdmR4Test;
import ca.uhn.fhir.jpa.mdm.svc.candidate.MdmCandidateSearchSvc;
import ca.uhn.fhir.mdm.rules.config.MdmSettings;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class MdmCandidateSearchSvcIT extends BaseMdmR4Test {

	@Autowired
	MdmCandidateSearchSvc myMdmCandidateSearchSvc;
	@Autowired
	MdmSettings myMdmSettings;

	@AfterEach
	public void resetMdmSettings() {
		myMdmSettings.setCandidateSearchLimit(MdmSettings.DEFAULT_CANDIDATE_SEARCH_LIMIT);
	}

	@Test
	public void testFindCandidates() {
		createActivePatient();
		Patient newJane = buildJanePatient();

		Collection<IAnyResource> result = myMdmCandidateSearchSvc.findCandidates("Patient", newJane);
		assertEquals(1, result.size());
	}


	@Test
	public void findCandidatesMultipleMatchesDoNotCauseDuplicates() {
		Date today = new Date();
		Patient jane = buildJaneWithBirthday(today);

		jane.setActive(true);
		createPatient(jane);

		Patient newJane = buildJaneWithBirthday(today);

		Collection<IAnyResource> result = myMdmCandidateSearchSvc.findCandidates("Patient", newJane);
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

		Collection<IAnyResource> patient = myMdmCandidateSearchSvc.findCandidates("Patient", incomingPatient);
		assertThat(patient, hasSize(1));
	}

	@Test
	public void testTooManyMatches() {
		myMdmSettings.setCandidateSearchLimit(3);

		Patient newJane = buildJanePatient();

		createActivePatient();
		assertEquals(1, myMdmCandidateSearchSvc.findCandidates("Patient", newJane).size());
		createActivePatient();
		assertEquals(2, myMdmCandidateSearchSvc.findCandidates("Patient", newJane).size());

		try {
			createActivePatient();
			myMdmCandidateSearchSvc.findCandidates("Patient", newJane);
			fail();
		} catch (ConfigurationException e) {
			assertEquals("More than 3 candidate matches found for Patient?identifier=http%3A%2F%2Fa.tv%2F%7CID.JANE.123&active=true.  Aborting mdm matching.", e.getMessage());
		}
	}

	private Patient createActivePatient() {
		Patient jane = buildJanePatient();
		jane.setActive(true);
		return createPatient(jane);
	}
}
