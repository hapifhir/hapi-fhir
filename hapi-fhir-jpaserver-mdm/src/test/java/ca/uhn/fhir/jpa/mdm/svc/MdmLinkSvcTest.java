package ca.uhn.fhir.jpa.mdm.svc;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.dao.expunge.ExpungeEverythingService;
import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.jpa.mdm.BaseMdmR4Test;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.mdm.api.IMdmLink;
import ca.uhn.fhir.mdm.api.IMdmLinkSvc;
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchOutcome;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;
import static org.slf4j.LoggerFactory.getLogger;

public class MdmLinkSvcTest extends BaseMdmR4Test {
	private static final Logger ourLog = getLogger(MdmLinkSvcTest.class);

	private static final MdmMatchOutcome POSSIBLE_MATCH = new MdmMatchOutcome(null, null).setMatchResultEnum(MdmMatchResultEnum.POSSIBLE_MATCH);
	@Autowired
	IMdmLinkSvc myMdmLinkSvc;
	@Autowired
	ExpungeEverythingService myExpungeEverythingService;

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

		JpaPid goldenPatient1Pid = runInTransaction(()->myIdHelperService.getPidOrNull(RequestPartitionId.allPartitions(), goldenPatient1));
		JpaPid goldenPatient2Pid = runInTransaction(()->myIdHelperService.getPidOrNull(RequestPartitionId.allPartitions(), goldenPatient2));
		assertFalse(myMdmLinkDaoSvc.getLinkByGoldenResourcePidAndSourceResourcePid(goldenPatient1Pid.getId(), goldenPatient2Pid.getId()).isPresent());
		assertFalse(myMdmLinkDaoSvc.getLinkByGoldenResourcePidAndSourceResourcePid(goldenPatient2Pid.getId(), goldenPatient1Pid.getId()).isPresent());

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

		JpaPid goldenPatient1Pid = runInTransaction(()->myIdHelperService.getPidOrNull(RequestPartitionId.allPartitions(), goldenPatient1));
		JpaPid goldenPatient2Pid = runInTransaction(()->myIdHelperService.getPidOrNull(RequestPartitionId.allPartitions(), goldenPatient2));
		assertFalse(myMdmLinkDaoSvc.getLinkByGoldenResourcePidAndSourceResourcePid(goldenPatient1Pid.getId(), goldenPatient2Pid.getId()).isPresent());
		assertFalse(myMdmLinkDaoSvc.getLinkByGoldenResourcePidAndSourceResourcePid(goldenPatient2Pid.getId(), goldenPatient1Pid.getId()).isPresent());

		saveNoMatchLink(goldenPatient2Pid, goldenPatient1Pid);

		myMdmLinkSvc.updateLink(goldenPatient1, goldenPatient2, MdmMatchOutcome.POSSIBLE_DUPLICATE, MdmLinkSourceEnum.AUTO, createContextForCreate("Patient"));
		assertFalse(myMdmLinkDaoSvc.getMdmLinksByGoldenResourcePidSourcePidAndMatchResult(goldenPatient1Pid, goldenPatient2Pid, MdmMatchResultEnum.POSSIBLE_DUPLICATE).isPresent());
		assertLinkCount(1);
	}

	private void saveNoMatchLink(JpaPid theGoldenResourcePid, JpaPid theTargetPid) {
		MdmLink noMatchLink = (MdmLink) myMdmLinkDaoSvc.newMdmLink();
		noMatchLink.setGoldenResourcePersistenceId(theGoldenResourcePid);
		noMatchLink.setSourcePersistenceId(theTargetPid);
		noMatchLink.setLinkSource(MdmLinkSourceEnum.MANUAL).setMatchResult(MdmMatchResultEnum.NO_MATCH);
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
			assertEquals(Msg.code(760) + "MDM system is not allowed to modify links on manually created links", e.getMessage());
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
			assertEquals(Msg.code(761) + "MDM system is not allowed to automatically NO_MATCH a resource", e.getMessage());
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
		assertThat(targets).isNotEmpty();
		assertThat(targets).hasSize(2);

		//TODO GGG update this test once we decide what has to happen here. There is no more "syncing links"
		//assertEquals(patient1.getIdElement().toVersionless().getValue(), sourcePatient.getLinkFirstRep().getTarget().getReference());
		List<String> actual = targets
			.stream()
			.map(link -> link.getSourcePersistenceId().getId().toString())
			.collect(Collectors.toList());

		List<String> expected = Stream.of(patient1, patient2)
			.map(p -> p.getIdElement().toVersionless().getIdPart())
			.collect(Collectors.toList());

		assertThat(actual).containsExactlyInAnyOrderElementsOf(expected);
	}

	@Test
	public void testMdmLinksHasPartitionIdForResourceOnNonDefaultPartition() {
		Patient goldenPatient = createGoldenPatient(buildJanePatient());
		Patient patient1 = createPatient(buildJanePatient());
		RequestPartitionId requestPartitionId = RequestPartitionId.fromPartitionId(1);
		patient1.setUserData(Constants.RESOURCE_PARTITION_ID, requestPartitionId);
		assertEquals(0, myMdmLinkDao.count());

		myMdmLinkDaoSvc.createOrUpdateLinkEntity(goldenPatient, patient1, MdmMatchOutcome.NEW_GOLDEN_RESOURCE_MATCH, MdmLinkSourceEnum.MANUAL, createContextForCreate("Patient"));
		List<? extends IMdmLink> targets = myMdmLinkDaoSvc.findMdmLinksByGoldenResource(goldenPatient);
		assertThat(targets).isNotEmpty();
		assertThat(targets).hasSize(1);
		assertEquals(requestPartitionId.getFirstPartitionIdOrNull(), targets.get(0).getPartitionId().getPartitionId());
	}
}
