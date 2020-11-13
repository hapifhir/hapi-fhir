package ca.uhn.fhir.jpa.empi.svc;

import ca.uhn.fhir.empi.api.EmpiLinkSourceEnum;
import ca.uhn.fhir.empi.api.EmpiMatchOutcome;
import ca.uhn.fhir.empi.api.EmpiMatchResultEnum;
import ca.uhn.fhir.empi.api.IEmpiLinkSvc;
import ca.uhn.fhir.empi.util.EIDHelper;
import ca.uhn.fhir.jpa.empi.BaseEmpiR4Test;
import ca.uhn.fhir.jpa.entity.EmpiLink;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.assertj.core.util.Lists;
import org.hamcrest.Matchers;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
		Patient sourcePatient = createGoldenPatient();
		IdType sourcePatientId = sourcePatient.getIdElement().toUnqualifiedVersionless();
		// TODO NG should be ok to remove - assertEquals(0, sourcePatient.getLink().size());
		Patient patient = createPatient();

		{
			myEmpiLinkSvc.updateLink(sourcePatient, patient, POSSIBLE_MATCH, EmpiLinkSourceEnum.AUTO, createContextForCreate("Patient"));
			assertLinkCount(1);
			// TODO NG should be ok to remove
			// Patient newSourcePatient = myPatientDao.read(sourcePatientId);
			// assertEquals(1, newSourcePatient.getLink().size());
		}

		{
			myEmpiLinkSvc.updateLink(sourcePatient, patient, EmpiMatchOutcome.NO_MATCH, EmpiLinkSourceEnum.MANUAL, createContextForCreate("Patient"));
			assertLinkCount(1);
			// TODO NG should be ok to remove
			// Patient newSourcePatient = myPatientDao.read(sourcePatientId);
			// assertEquals(0, newSourcePatient.getLink().size());
		}
	}


	@Test
	public void testPossibleDuplicate() {
		assertLinkCount(0);
		Patient sourcePatient1 = createGoldenPatient();
		Patient sourcePatient2 = createGoldenPatient();
		// TODO GGG MDM NOT VALID
		myEmpiLinkSvc.updateLink(sourcePatient1, sourcePatient2, EmpiMatchOutcome.POSSIBLE_DUPLICATE, EmpiLinkSourceEnum.AUTO, createContextForCreate("Patient"));
		assertLinkCount(1);
	}

	@Test
	public void testNoMatchBlocksPossibleDuplicate() {
		assertLinkCount(0);
		Patient sourcePatient1 = createGoldenPatient();
		Patient sourcePatient2 = createGoldenPatient();

		Long sourcePatient1Pid = myIdHelperService.getPidOrNull(sourcePatient1);
		Long sourcePatient2Pid = myIdHelperService.getPidOrNull(sourcePatient2);
		assertFalse(myEmpiLinkDaoSvc.getLinkBySourceResourcePidAndTargetResourcePid(sourcePatient1Pid, sourcePatient2Pid).isPresent());
		assertFalse(myEmpiLinkDaoSvc.getLinkBySourceResourcePidAndTargetResourcePid(sourcePatient2Pid, sourcePatient1Pid).isPresent());

		saveNoMatchLink(sourcePatient1Pid, sourcePatient2Pid);

		myEmpiLinkSvc.updateLink(sourcePatient1, sourcePatient2, EmpiMatchOutcome.POSSIBLE_DUPLICATE, EmpiLinkSourceEnum.AUTO, createContextForCreate("Person"));
		assertFalse(myEmpiLinkDaoSvc.getEmpiLinksByPersonPidTargetPidAndMatchResult(sourcePatient1Pid, sourcePatient2Pid, EmpiMatchResultEnum.POSSIBLE_DUPLICATE).isPresent());
		assertLinkCount(1);
	}

	@Test
	public void testNoMatchBlocksPossibleDuplicateReversed() {
		assertLinkCount(0);
		Patient sourcePatient1 = createGoldenPatient();
		Patient sourcePatient2 = createGoldenPatient();

		Long sourcePatient1Pid = myIdHelperService.getPidOrNull(sourcePatient1);
		Long sourcePatient2Pid = myIdHelperService.getPidOrNull(sourcePatient2);
		assertFalse(myEmpiLinkDaoSvc.getLinkBySourceResourcePidAndTargetResourcePid(sourcePatient1Pid, sourcePatient2Pid).isPresent());
		assertFalse(myEmpiLinkDaoSvc.getLinkBySourceResourcePidAndTargetResourcePid(sourcePatient2Pid, sourcePatient1Pid).isPresent());

		saveNoMatchLink(sourcePatient2Pid, sourcePatient1Pid);

		myEmpiLinkSvc.updateLink(sourcePatient1, sourcePatient2, EmpiMatchOutcome.POSSIBLE_DUPLICATE, EmpiLinkSourceEnum.AUTO, createContextForCreate("Person"));
		assertFalse(myEmpiLinkDaoSvc.getEmpiLinksByPersonPidTargetPidAndMatchResult(sourcePatient1Pid, sourcePatient2Pid, EmpiMatchResultEnum.POSSIBLE_DUPLICATE).isPresent());
		assertLinkCount(1);
	}

	private void saveNoMatchLink(Long thePersonPid, Long theTargetPid) {
		EmpiLink noMatchLink = myEmpiLinkDaoSvc.newEmpiLink()
			.setGoldenResourcePid(thePersonPid)
			.setTargetPid(theTargetPid)
			.setLinkSource(EmpiLinkSourceEnum.MANUAL)
			.setMatchResult(EmpiMatchResultEnum.NO_MATCH);
		saveLink(noMatchLink);
	}

	@Test
	public void testManualEmpiLinksCannotBeModifiedBySystem() {
		Patient sourcePatient = createGoldenPatient(buildJaneSourcePatient());
		Patient patient = createPatient(buildJanePatient());

		myEmpiLinkSvc.updateLink(sourcePatient, patient, EmpiMatchOutcome.NO_MATCH, EmpiLinkSourceEnum.MANUAL, createContextForCreate("Patient"));
		try {
			myEmpiLinkSvc.updateLink(sourcePatient, patient, EmpiMatchOutcome.NEW_PERSON_MATCH, EmpiLinkSourceEnum.AUTO, null);
			fail();
		} catch (InternalErrorException e) {
			assertThat(e.getMessage(), is(equalTo("EMPI system is not allowed to modify links on manually created links")));
		}
	}

	@Test
	public void testAutomaticallyAddedNO_MATCHEmpiLinksAreNotAllowed() {
//		Patient sourcePatient = createGoldenPatient(buildJaneSourcePatient());
		Patient sourcePatient = createGoldenPatient(buildJanePatient());
		Patient patient = createPatient(buildJanePatient());

		// Test: it should be impossible to have a AUTO NO_MATCH record.  The only NO_MATCH records in the system must be MANUAL.
		try {
			myEmpiLinkSvc.updateLink(sourcePatient, patient, EmpiMatchOutcome.NO_MATCH, EmpiLinkSourceEnum.AUTO, createContextForUpdate("Patient"));
			fail();
		} catch (InternalErrorException e) {
			assertThat(e.getMessage(), is(equalTo("EMPI system is not allowed to automatically NO_MATCH a resource")));
		}
	}

	@Test
	public void testSyncDoesNotSyncNoMatchLinks() {
//		Patient sourcePatient = createGoldenPatient(buildJaneSourcePatient());
		Patient goldenPatient = createGoldenPatient(buildJanePatient());
		Patient patient1 = createPatient(buildJanePatient());
		Patient patient2 = createPatient(buildJanePatient());
		assertEquals(0, myEmpiLinkDao.count());

		myEmpiLinkDaoSvc.createOrUpdateLinkEntity(goldenPatient, patient1, EmpiMatchOutcome.NEW_PERSON_MATCH, EmpiLinkSourceEnum.MANUAL, createContextForCreate("Patient"));
		myEmpiLinkDaoSvc.createOrUpdateLinkEntity(goldenPatient, patient2, EmpiMatchOutcome.NO_MATCH, EmpiLinkSourceEnum.MANUAL, createContextForCreate("Patient"));
//		myEmpiLinkSvc.syncEmpiLinksToPersonLinks(sourcePatient, createContextForCreate("Patient"));

		List<EmpiLink> targets = myEmpiLinkDaoSvc.findEmpiLinksByGoldenResource(goldenPatient);
		assertFalse(targets.isEmpty());
		assertEquals(2, targets.size());
		// TODO NG - OK? original assertTrue(goldenPatient.hasLink());

		//TODO GGG update this test once we decide what has to happen here. There is no more "syncing links"
		//assertEquals(patient1.getIdElement().toVersionless().getValue(), sourcePatient.getLinkFirstRep().getTarget().getReference());
		List<String> actual = targets
			.stream()
			.map(link -> link.getTargetPid().toString())
			.collect(Collectors.toList());

		List<String> expected = Arrays.asList(patient1, patient2)
			.stream().map(p -> p.getIdElement().toVersionless().getIdPart())
			.collect(Collectors.toList());

		System.out.println(actual);
		System.out.println(expected);

		assertThat(actual, Matchers.containsInAnyOrder(expected.toArray()));
	}
}
