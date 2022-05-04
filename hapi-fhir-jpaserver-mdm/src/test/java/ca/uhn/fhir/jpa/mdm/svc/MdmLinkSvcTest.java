package ca.uhn.fhir.jpa.mdm.svc;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.dao.expunge.IExpungeEverythingService;
import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.jpa.mdm.BaseMdmR4Test;
import ca.uhn.fhir.mdm.api.IMdmLinkSvc;
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchOutcome;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.hamcrest.Matchers;
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
import static org.junit.jupiter.api.Assertions.fail;

public class MdmLinkSvcTest extends BaseMdmR4Test {
	private static final MdmMatchOutcome POSSIBLE_MATCH = new MdmMatchOutcome(null, null).setMatchResultEnum(MdmMatchResultEnum.POSSIBLE_MATCH);
	@Autowired
	IMdmLinkSvc myMdmLinkSvc;
	@Autowired
	IExpungeEverythingService myExpungeEverythingService;

	@Override
	@AfterEach
	public void after() throws IOException {
		myExpungeEverythingService.expungeEverythingByType(MdmLink.class);
		super.after();
	}

	@Test
	public void compareEmptyPatients() {
		Patient patient = new Patient();
		patient.setId("Patient/1");
		MdmMatchResultEnum result = myMdmResourceMatcherSvc.getMatchResult(patient, patient).getMatchResultEnum();
		assertEquals(MdmMatchResultEnum.NO_MATCH, result);
	}

	@Test
	public void testCreateRemoveLink() {
		assertLinkCount(0);
		Patient goldenPatient = createGoldenPatient();
		IdType sourcePatientId = goldenPatient.getIdElement().toUnqualifiedVersionless();
		Patient patient = createPatient();

		{
			myMdmLinkSvc.updateLink(goldenPatient, patient, POSSIBLE_MATCH, MdmLinkSourceEnum.AUTO, createContextForCreate("Patient"));
			assertLinkCount(1);
		}

		{
			myMdmLinkSvc.updateLink(goldenPatient, patient, MdmMatchOutcome.NO_MATCH, MdmLinkSourceEnum.MANUAL, createContextForCreate("Patient"));
			assertLinkCount(1);
		}
	}


	@Test
	public void testPossibleDuplicate() {
		assertLinkCount(0);
		Patient goldenPatient1 = createGoldenPatient();
		Patient goldenPatient2 = createGoldenPatient();
		// TODO GGG MDM NOT VALID
		myMdmLinkSvc.updateLink(goldenPatient1, goldenPatient2, MdmMatchOutcome.POSSIBLE_DUPLICATE, MdmLinkSourceEnum.AUTO, createContextForCreate("Patient"));
		assertLinkCount(1);
	}

	@Test
	public void testNoMatchBlocksPossibleDuplicate() {
		assertLinkCount(0);
		Patient goldenPatient1 = createGoldenPatient();
		Patient goldenPatient2 = createGoldenPatient();

		Long goldenPatient1Pid = runInTransaction(()->myIdHelperService.getPidOrNull(goldenPatient1));
		Long goldenPatient2Pid = runInTransaction(()->myIdHelperService.getPidOrNull(goldenPatient2));
		assertFalse(myMdmLinkDaoSvc.getLinkByGoldenResourcePidAndSourceResourcePid(goldenPatient1Pid, goldenPatient2Pid).isPresent());
		assertFalse(myMdmLinkDaoSvc.getLinkByGoldenResourcePidAndSourceResourcePid(goldenPatient2Pid, goldenPatient1Pid).isPresent());

		saveNoMatchLink(goldenPatient1Pid, goldenPatient2Pid);

		myMdmLinkSvc.updateLink(goldenPatient1, goldenPatient2, MdmMatchOutcome.POSSIBLE_DUPLICATE, MdmLinkSourceEnum.AUTO, createContextForCreate("Patient"));
		assertFalse(myMdmLinkDaoSvc.getMdmLinksByGoldenResourcePidSourcePidAndMatchResult(goldenPatient1Pid, goldenPatient2Pid, MdmMatchResultEnum.POSSIBLE_DUPLICATE).isPresent());
		assertLinkCount(1);
	}

	@Test
	public void testNoMatchBlocksPossibleDuplicateReversed() {
		assertLinkCount(0);
		Patient goldenPatient1 = createGoldenPatient();
		Patient goldenPatient2 = createGoldenPatient();

		Long goldenPatient1Pid = runInTransaction(()->myIdHelperService.getPidOrNull(goldenPatient1));
		Long goldenPatient2Pid = runInTransaction(()->myIdHelperService.getPidOrNull(goldenPatient2));
		assertFalse(myMdmLinkDaoSvc.getLinkByGoldenResourcePidAndSourceResourcePid(goldenPatient1Pid, goldenPatient2Pid).isPresent());
		assertFalse(myMdmLinkDaoSvc.getLinkByGoldenResourcePidAndSourceResourcePid(goldenPatient2Pid, goldenPatient1Pid).isPresent());

		saveNoMatchLink(goldenPatient2Pid, goldenPatient1Pid);

		myMdmLinkSvc.updateLink(goldenPatient1, goldenPatient2, MdmMatchOutcome.POSSIBLE_DUPLICATE, MdmLinkSourceEnum.AUTO, createContextForCreate("Patient"));
		assertFalse(myMdmLinkDaoSvc.getMdmLinksByGoldenResourcePidSourcePidAndMatchResult(goldenPatient1Pid, goldenPatient2Pid, MdmMatchResultEnum.POSSIBLE_DUPLICATE).isPresent());
		assertLinkCount(1);
	}

	private void saveNoMatchLink(Long theGoldenResourcePid, Long theTargetPid) {
		MdmLink noMatchLink = myMdmLinkDaoSvc.newMdmLink()
			.setGoldenResourcePid(theGoldenResourcePid)
			.setSourcePid(theTargetPid)
			.setLinkSource(MdmLinkSourceEnum.MANUAL)
			.setMatchResult(MdmMatchResultEnum.NO_MATCH);
		saveLink(noMatchLink);
	}

	@Test
	public void testManualMdmLinksCannotBeModifiedBySystem() {
		Patient goldenPatient = createGoldenPatient(buildJanePatient());
		Patient patient = createPatient(buildJanePatient());

		myMdmLinkSvc.updateLink(goldenPatient, patient, MdmMatchOutcome.NO_MATCH, MdmLinkSourceEnum.MANUAL, createContextForCreate("Patient"));
		try {
			myMdmLinkSvc.updateLink(goldenPatient, patient, MdmMatchOutcome.NEW_GOLDEN_RESOURCE_MATCH, MdmLinkSourceEnum.AUTO, null);
			fail();
		} catch (InternalErrorException e) {
			assertThat(e.getMessage(), is(equalTo(Msg.code(760) + "MDM system is not allowed to modify links on manually created links")));
		}
	}

	@Test
	public void testAutomaticallyAddedNO_MATCHMdmLinksAreNotAllowed() {
		Patient goldenPatient = createGoldenPatient(buildJanePatient());
		Patient patient = createPatient(buildJanePatient());

		// Test: it should be impossible to have a AUTO NO_MATCH record.  The only NO_MATCH records in the system must be MANUAL.
		try {
			myMdmLinkSvc.updateLink(goldenPatient, patient, MdmMatchOutcome.NO_MATCH, MdmLinkSourceEnum.AUTO, createContextForUpdate("Patient"));
			fail();
		} catch (InternalErrorException e) {
			assertThat(e.getMessage(), is(equalTo(Msg.code(761) + "MDM system is not allowed to automatically NO_MATCH a resource")));
		}
	}

	@Test
	public void testSyncDoesNotSyncNoMatchLinks() {
		Patient goldenPatient = createGoldenPatient(buildJanePatient());
		Patient patient1 = createPatient(buildJanePatient());
		Patient patient2 = createPatient(buildJanePatient());
		assertEquals(0, myMdmLinkDao.count());

		myMdmLinkDaoSvc.createOrUpdateLinkEntity(goldenPatient, patient1, MdmMatchOutcome.NEW_GOLDEN_RESOURCE_MATCH, MdmLinkSourceEnum.MANUAL, createContextForCreate("Patient"));
		myMdmLinkDaoSvc.createOrUpdateLinkEntity(goldenPatient, patient2, MdmMatchOutcome.NO_MATCH, MdmLinkSourceEnum.MANUAL, createContextForCreate("Patient"));

		List<MdmLink> targets = myMdmLinkDaoSvc.findMdmLinksByGoldenResource(goldenPatient);
		assertFalse(targets.isEmpty());
		assertEquals(2, targets.size());

		//TODO GGG update this test once we decide what has to happen here. There is no more "syncing links"
		//assertEquals(patient1.getIdElement().toVersionless().getValue(), sourcePatient.getLinkFirstRep().getTarget().getReference());
		List<String> actual = targets
			.stream()
			.map(link -> link.getSourcePid().toString())
			.collect(Collectors.toList());

		List<String> expected = Arrays.asList(patient1, patient2)
			.stream().map(p -> p.getIdElement().toVersionless().getIdPart())
			.collect(Collectors.toList());

		System.out.println(actual);
		System.out.println(expected);

		assertThat(actual, Matchers.containsInAnyOrder(expected.toArray()));
	}

	@Test
	public void testMdmLinksHasPartitionIdForResourceOnNonDefaultPartition() {
		Patient goldenPatient = createGoldenPatient(buildJanePatient());
		Patient patient1 = createPatient(buildJanePatient());
		RequestPartitionId requestPartitionId = RequestPartitionId.fromPartitionId(1);
		patient1.setUserData(Constants.RESOURCE_PARTITION_ID, requestPartitionId);
		assertEquals(0, myMdmLinkDao.count());

		myMdmLinkDaoSvc.createOrUpdateLinkEntity(goldenPatient, patient1, MdmMatchOutcome.NEW_GOLDEN_RESOURCE_MATCH, MdmLinkSourceEnum.MANUAL, createContextForCreate("Patient"));
		List<MdmLink> targets = myMdmLinkDaoSvc.findMdmLinksByGoldenResource(goldenPatient);
		assertFalse(targets.isEmpty());
		assertEquals(1, targets.size());
		assertEquals(requestPartitionId.getFirstPartitionIdOrNull(), targets.get(0).getPartitionId().getPartitionId());
	}
}
