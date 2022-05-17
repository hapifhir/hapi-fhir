package ca.uhn.fhir.jpa.mdm.svc;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.mdm.BaseMdmR4Test;
import ca.uhn.fhir.jpa.mdm.svc.candidate.MdmCandidateSearchSvc;
import ca.uhn.fhir.jpa.mdm.svc.candidate.TooManyCandidatesException;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.nickname.NicknameInterceptor;
import ca.uhn.fhir.mdm.rules.config.MdmSettings;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
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
	@Autowired
	MatchUrlService myMatchUrlService;

	private NicknameInterceptor myNicknameInterceptor;

	@BeforeEach
	public void before() throws IOException {
		myNicknameInterceptor = new NicknameInterceptor();
		myInterceptorRegistry.registerInterceptor(myNicknameInterceptor);
	}

	@AfterEach
	public void resetMdmSettings() {
		myMdmSettings.setCandidateSearchLimit(MdmSettings.DEFAULT_CANDIDATE_SEARCH_LIMIT);
		myInterceptorRegistry.unregisterInterceptor(myNicknameInterceptor);
	}

	@Test
	public void testFindCandidates() {
		createActivePatient();
		Patient newJane = buildJanePatient();

		Collection<IAnyResource> result = myMdmCandidateSearchSvc.findCandidates("Patient", newJane, RequestPartitionId.allPartitions());
		assertEquals(1, result.size());
	}

	@Test
	public void testNickname() {
		Practitioner formal = new Practitioner();
		formal.getNameFirstRep().addGiven("William");
		formal.getNameFirstRep().setFamily("Shatner");
		formal.setActive(true);
		myPractitionerDao.create(formal);

		{
			// First confirm we can search for this practitioner using a nickname search
			SearchParameterMap map = myMatchUrlService.getResourceSearch("Practitioner?given:nickname=Bill&family=Shatner").getSearchParameterMap();
			map.setLoadSynchronous(true);
			IBundleProvider result = myPractitionerDao.search(map);
			assertEquals(1, result.size());
			Practitioner first = (Practitioner) result.getResources(0, 1).get(0);
			assertEquals("William", first.getNameFirstRep().getGivenAsSingleString());
		}

		{
			// Now achieve the same match via mdm
			Practitioner nick = new Practitioner();
			nick.getNameFirstRep().addGiven("Bill");
			nick.getNameFirstRep().setFamily("Shatner");
			Collection<IAnyResource> result = myMdmCandidateSearchSvc.findCandidates("Practitioner", nick, RequestPartitionId.allPartitions());
			assertEquals(1, result.size());
		}

		{
			// Should not match Bob
			Practitioner noMatch = new Practitioner();
			noMatch.getNameFirstRep().addGiven("Bob");
			noMatch.getNameFirstRep().setFamily("Shatner");
			Collection<IAnyResource> result = myMdmCandidateSearchSvc.findCandidates("Practitioner", noMatch, RequestPartitionId.allPartitions());
			assertEquals(0, result.size());
		}
	}

	@Test
	public void findCandidatesMultipleMatchesDoNotCauseDuplicates() {
		Date today = new Date();
		Patient jane = buildJaneWithBirthday(today);

		jane.setActive(true);
		createPatient(jane);

		Patient newJane = buildJaneWithBirthday(today);

		Collection<IAnyResource> result = myMdmCandidateSearchSvc.findCandidates("Patient", newJane, RequestPartitionId.allPartitions());
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

		Collection<IAnyResource> patient = myMdmCandidateSearchSvc.findCandidates("Patient", incomingPatient, RequestPartitionId.allPartitions());
		assertThat(patient, hasSize(1));
	}

	@Test
	public void testTooManyMatches() {
		myMdmSettings.setCandidateSearchLimit(3);

		Patient newJane = buildJanePatient();

		createActivePatient();
		assertEquals(1, runInTransaction(() -> myMdmCandidateSearchSvc.findCandidates("Patient", newJane, RequestPartitionId.allPartitions()).size()));
		createActivePatient();
		assertEquals(2, runInTransaction(() -> myMdmCandidateSearchSvc.findCandidates("Patient", newJane, RequestPartitionId.allPartitions()).size()));

		try {
			createActivePatient();
			myMdmCandidateSearchSvc.findCandidates("Patient", newJane, RequestPartitionId.allPartitions());
			fail();
		} catch (TooManyCandidatesException e) {
			assertEquals("More than 3 candidate matches found for Patient?identifier=http%3A%2F%2Fa.tv%2F%7CID.JANE.123&active=true.  Aborting mdm matching.", e.getMessage());
		}
	}

	private Patient createActivePatient() {
		Patient jane = buildJanePatient();
		jane.setActive(true);
		return createPatient(jane);
	}
}
