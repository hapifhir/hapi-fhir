package ca.uhn.fhir.jpa.empi.svc;

import ca.uhn.fhir.empi.api.EmpiLinkSourceEnum;
import ca.uhn.fhir.empi.api.EmpiMatchOutcome;
import ca.uhn.fhir.empi.api.EmpiMatchResultEnum;
import ca.uhn.fhir.empi.api.IEmpiLinkSvc;
import ca.uhn.fhir.jpa.empi.BaseEmpiR4Test;
import ca.uhn.fhir.jpa.entity.EmpiLink;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class EmpiLinkSvcTest extends BaseEmpiR4Test {
	private static final EmpiMatchOutcome POSSIBLE_MATCH = new EmpiMatchOutcome(null, null).setMatchResultEnum(EmpiMatchResultEnum.POSSIBLE_MATCH);
	@Autowired
	IEmpiLinkSvc myEmpiLinkSvc;

	@Override
	@AfterEach
	public void after() throws IOException {
		myExpungeEverythingService.expungeEverythingByType(EmpiLink.class);
		super.after();
	}

	@Test
	public void compareEmptyPatients() {
		Patient patient = new Patient();
		patient.setId("Patient/1");
		EmpiMatchResultEnum result = myEmpiResourceMatcherSvc.getMatchResult(patient, patient).getMatchResultEnum();
		assertEquals(EmpiMatchResultEnum.NO_MATCH, result);
	}

	@Test
	public void testCreateRemoveLink() {
		assertLinkCount(0);
		Person person = createPerson();
		IdType personId = person.getIdElement().toUnqualifiedVersionless();
		assertEquals(0, person.getLink().size());
		Patient patient = createPatient();

		{
			myEmpiLinkSvc.updateLink(person, patient, POSSIBLE_MATCH, EmpiLinkSourceEnum.AUTO, createContextForCreate());
			assertLinkCount(1);
			Person newPerson = myPersonDao.read(personId);
			assertEquals(1, newPerson.getLink().size());
		}

		{
			myEmpiLinkSvc.updateLink(person, patient, EmpiMatchOutcome.NO_MATCH, EmpiLinkSourceEnum.MANUAL, createContextForCreate());
			assertLinkCount(1);
			Person newPerson = myPersonDao.read(personId);
			assertEquals(0, newPerson.getLink().size());
		}
	}


	@Test
	public void testPossibleDuplicate() {
		assertLinkCount(0);
		Person person = createPerson();
		Person target = createPerson();

		myEmpiLinkSvc.updateLink(person, target, EmpiMatchOutcome.POSSIBLE_DUPLICATE, EmpiLinkSourceEnum.AUTO, createContextForCreate());
		assertLinkCount(1);
	}

	@Test
	public void testNoMatchBlocksPossibleDuplicate() {
		assertLinkCount(0);
		Person person = createPerson();
		Person target = createPerson();

		Long personPid = myIdHelperService.getPidOrNull(person);
		Long targetPid = myIdHelperService.getPidOrNull(target);
		assertFalse(myEmpiLinkDaoSvc.getLinkByPersonPidAndTargetPid(personPid, targetPid).isPresent());
		assertFalse(myEmpiLinkDaoSvc.getLinkByPersonPidAndTargetPid(targetPid, personPid).isPresent());

		saveNoMatchLink(personPid, targetPid);

		myEmpiLinkSvc.updateLink(person, target, EmpiMatchOutcome.POSSIBLE_DUPLICATE, EmpiLinkSourceEnum.AUTO, createContextForCreate());
		assertFalse(myEmpiLinkDaoSvc.getEmpiLinksByPersonPidTargetPidAndMatchResult(personPid, targetPid, EmpiMatchResultEnum.POSSIBLE_DUPLICATE).isPresent());
		assertLinkCount(1);
	}

	@Test
	public void testNoMatchBlocksPossibleDuplicateReversed() {
		assertLinkCount(0);
		Person person = createPerson();
		Person target = createPerson();

		Long personPid = myIdHelperService.getPidOrNull(person);
		Long targetPid = myIdHelperService.getPidOrNull(target);
		assertFalse(myEmpiLinkDaoSvc.getLinkByPersonPidAndTargetPid(personPid, targetPid).isPresent());
		assertFalse(myEmpiLinkDaoSvc.getLinkByPersonPidAndTargetPid(targetPid, personPid).isPresent());

		saveNoMatchLink(targetPid, personPid);

		myEmpiLinkSvc.updateLink(person, target, EmpiMatchOutcome.POSSIBLE_DUPLICATE, EmpiLinkSourceEnum.AUTO, createContextForCreate());
		assertFalse(myEmpiLinkDaoSvc.getEmpiLinksByPersonPidTargetPidAndMatchResult(personPid, targetPid, EmpiMatchResultEnum.POSSIBLE_DUPLICATE).isPresent());
		assertLinkCount(1);
	}

	private void saveNoMatchLink(Long thePersonPid, Long theTargetPid) {
		EmpiLink noMatchLink = myEmpiLinkDaoSvc.newEmpiLink()
			.setPersonPid(thePersonPid)
			.setTargetPid(theTargetPid)
			.setLinkSource(EmpiLinkSourceEnum.MANUAL)
			.setMatchResult(EmpiMatchResultEnum.NO_MATCH);
		saveLink(noMatchLink);
	}

	@Test
	public void testManualEmpiLinksCannotBeModifiedBySystem() {
		Person person = createPerson(buildJanePerson());
		Patient patient = createPatient(buildJanePatient());

		myEmpiLinkSvc.updateLink(person, patient, EmpiMatchOutcome.NO_MATCH, EmpiLinkSourceEnum.MANUAL, createContextForCreate());
		try {
			myEmpiLinkSvc.updateLink(person, patient, EmpiMatchOutcome.NEW_PERSON_MATCH, EmpiLinkSourceEnum.AUTO, null);
			fail();
		} catch (InternalErrorException e) {
			assertThat(e.getMessage(), is(equalTo("EMPI system is not allowed to modify links on manually created links")));
		}
	}

	@Test
	public void testAutomaticallyAddedNO_MATCHEmpiLinksAreNotAllowed() {
		Person person = createPerson(buildJanePerson());
		Patient patient = createPatient(buildJanePatient());

		// Test: it should be impossible to have a AUTO NO_MATCH record.  The only NO_MATCH records in the system must be MANUAL.
		try {
			myEmpiLinkSvc.updateLink(person, patient, EmpiMatchOutcome.NO_MATCH, EmpiLinkSourceEnum.AUTO, null);
			fail();
		} catch (InternalErrorException e) {
			assertThat(e.getMessage(), is(equalTo("EMPI system is not allowed to automatically NO_MATCH a resource")));
		}
	}

	@Test
	public void testSyncDoesNotSyncNoMatchLinks() {
		Person person = createPerson(buildJanePerson());
		Patient patient1 = createPatient(buildJanePatient());
		Patient patient2 = createPatient(buildJanePatient());
		assertEquals(0, myEmpiLinkDao.count());

		myEmpiLinkDaoSvc.createOrUpdateLinkEntity(person, patient1, EmpiMatchOutcome.NEW_PERSON_MATCH, EmpiLinkSourceEnum.MANUAL, createContextForCreate());
		myEmpiLinkDaoSvc.createOrUpdateLinkEntity(person, patient2, EmpiMatchOutcome.NO_MATCH, EmpiLinkSourceEnum.MANUAL, createContextForCreate());
		myEmpiLinkSvc.syncEmpiLinksToPersonLinks(person, createContextForCreate());
		assertTrue(person.hasLink());
		assertEquals(patient1.getIdElement().toVersionless().getValue(), person.getLinkFirstRep().getTarget().getReference());
	}
}
