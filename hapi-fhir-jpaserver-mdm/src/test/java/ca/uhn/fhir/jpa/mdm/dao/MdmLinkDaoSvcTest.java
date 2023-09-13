package ca.uhn.fhir.jpa.mdm.dao;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.jpa.mdm.BaseMdmR4Test;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.entity.EnversRevision;
import ca.uhn.fhir.jpa.util.TestUtil;
import ca.uhn.fhir.mdm.api.IMdmLink;
import ca.uhn.fhir.mdm.api.params.MdmHistorySearchParameters;
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmLinkWithRevision;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.model.MdmPidTuple;
import ca.uhn.fhir.mdm.rules.json.MdmRulesJson;
import org.hibernate.envers.RevisionType;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.in;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MdmLinkDaoSvcTest extends BaseMdmR4Test {

	private static final Logger ourLog = LoggerFactory.getLogger(MdmLinkDaoSvcTest.class);

	@Test
	public void testCreate() {
		MdmLink mdmLink = createResourcesAndBuildTestMDMLink();
		assertThat(mdmLink.getCreated(), is(nullValue()));
		assertThat(mdmLink.getUpdated(), is(nullValue()));
		myMdmLinkDaoSvc.save(mdmLink);
		assertThat(mdmLink.getCreated(), is(notNullValue()));
		assertThat(mdmLink.getUpdated(), is(notNullValue()));
		assertTrue(mdmLink.getUpdated().getTime() - mdmLink.getCreated().getTime() < 1000);
	}

	@Test
	public void testUpdate() {
		MdmLink createdLink = myMdmLinkDaoSvc.save(createResourcesAndBuildTestMDMLink());
		assertThat(createdLink.getLinkSource(), is(MdmLinkSourceEnum.MANUAL));
		TestUtil.sleepOneClick();
		createdLink.setLinkSource(MdmLinkSourceEnum.AUTO);
		MdmLink updatedLink = myMdmLinkDaoSvc.save(createdLink);
		assertNotEquals(updatedLink.getCreated(), updatedLink.getUpdated());
	}

	@Test
	public void testNew() {
		IMdmLink newLink = myMdmLinkDaoSvc.newMdmLink();
		MdmRulesJson rules = myMdmSettings.getMdmRules();
		assertEquals("1", rules.getVersion());
		assertEquals(rules.getVersion(), newLink.getVersion());
	}

	@Test
	public void testExpandPidsWorks() {

		Patient golden = createGoldenPatient();

		//Create 10 linked patients.
		List<MdmLink> mdmLinks = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			mdmLinks.add(createGoldenPatientAndLinkToSourcePatient(golden.getIdElement().getIdPartAsLong(), MdmMatchResultEnum.MATCH));
		}

		//Now lets connect a few as just POSSIBLE_MATCHes and ensure they aren't returned.
		for (int i = 0 ; i < 5; i++) {
			createGoldenPatientAndLinkToSourcePatient(golden.getIdElement().getIdPartAsLong(), MdmMatchResultEnum.POSSIBLE_MATCH);
		}

		List<Long> expectedExpandedPids = mdmLinks.stream().map(MdmLink::getSourcePid).collect(Collectors.toList());

		//SUT
		List<MdmPidTuple<JpaPid>> lists = runInTransaction(() -> myMdmLinkDao.expandPidsBySourcePidAndMatchResult(JpaPid.fromId(mdmLinks.get(0).getSourcePid()), MdmMatchResultEnum.MATCH));

		assertThat(lists, hasSize(10));

		lists.stream()
			.forEach(tuple -> {
					assertThat(tuple.getGoldenPid().getId(), is(equalTo(myIdHelperService.getPidOrThrowException(golden).getId())));
					assertThat(tuple.getSourcePid().getId(), is(in(expectedExpandedPids)));
				});
	}

	@Test
	public void testHistoryForMultipleIdsCrud() {
		final List<MdmLink> mdmLinksWithLinkedPatients1 = createMdmLinksWithLinkedPatients(MdmMatchResultEnum.MATCH, 3);
		final List<MdmLink> mdmLinksWithLinkedPatients2 = createMdmLinksWithLinkedPatients(MdmMatchResultEnum.MATCH, 4);
		final List<MdmLink> mdmLinksWithLinkedPatients3 = createMdmLinksWithLinkedPatients(MdmMatchResultEnum.MATCH, 2);
		flipLinksTo(mdmLinksWithLinkedPatients3, MdmMatchResultEnum.NO_MATCH);

		final MdmHistorySearchParameters mdmHistorySearchParameters =
			new MdmHistorySearchParameters()
				.setGoldenResourceIds(getIdsFromMdmLinks(MdmLink::getGoldenResourcePersistenceId, mdmLinksWithLinkedPatients1.get(0), mdmLinksWithLinkedPatients3.get(0)))
				.setSourceIds(getIdsFromMdmLinks(MdmLink::getSourcePersistenceId, mdmLinksWithLinkedPatients1.get(0), mdmLinksWithLinkedPatients2.get(0)));

		final List<MdmLinkWithRevision<MdmLink>> actualMdmLinkRevisions = myMdmLinkDaoSvc.findMdmLinkHistory(mdmHistorySearchParameters);

		final JpaPid goldenResourceId1 = mdmLinksWithLinkedPatients1.get(0).getGoldenResourcePersistenceId();
		final JpaPid goldenResourceId2 = mdmLinksWithLinkedPatients2.get(0).getGoldenResourcePersistenceId();
		final JpaPid goldenResourceId3 = mdmLinksWithLinkedPatients3.get(0).getGoldenResourcePersistenceId();

		final JpaPid sourceId1_1 = mdmLinksWithLinkedPatients1.get(0).getSourcePersistenceId();
		final JpaPid sourceId1_2 = mdmLinksWithLinkedPatients1.get(1).getSourcePersistenceId();
		final JpaPid sourceId1_3 = mdmLinksWithLinkedPatients1.get(2).getSourcePersistenceId();

		final JpaPid sourceId2_1 = mdmLinksWithLinkedPatients2.get(0).getSourcePersistenceId();

		final JpaPid sourceId3_1 = mdmLinksWithLinkedPatients3.get(0).getSourcePersistenceId();
		final JpaPid sourceId3_2 = mdmLinksWithLinkedPatients3.get(1).getSourcePersistenceId();

		final List<MdmLinkWithRevision<MdmLink>> expectedMdLinkRevisions = List.of(
			buildMdmLinkWithRevision(1, RevisionType.ADD, MdmMatchResultEnum.MATCH, goldenResourceId1, sourceId1_1),
			buildMdmLinkWithRevision(2, RevisionType.ADD, MdmMatchResultEnum.MATCH, goldenResourceId1, sourceId1_2),
			buildMdmLinkWithRevision(3, RevisionType.ADD, MdmMatchResultEnum.MATCH, goldenResourceId1, sourceId1_3),
			buildMdmLinkWithRevision(4, RevisionType.ADD, MdmMatchResultEnum.MATCH, goldenResourceId2, sourceId2_1),
			buildMdmLinkWithRevision(10, RevisionType.MOD, MdmMatchResultEnum.NO_MATCH, goldenResourceId3, sourceId3_1),
			buildMdmLinkWithRevision(8, RevisionType.ADD, MdmMatchResultEnum.MATCH, goldenResourceId3, sourceId3_1),
			buildMdmLinkWithRevision(11, RevisionType.MOD, MdmMatchResultEnum.NO_MATCH, goldenResourceId3, sourceId3_2),
			buildMdmLinkWithRevision(9, RevisionType.ADD, MdmMatchResultEnum.MATCH, goldenResourceId3, sourceId3_2)
		);

		assertMdmRevisionsEqual(expectedMdLinkRevisions, actualMdmLinkRevisions);
	}

	@Test
	public void testHistoryForGoldenResourceIdsOnly() {
		final List<MdmLink> mdmLinksWithLinkedPatients1 = createMdmLinksWithLinkedPatients(MdmMatchResultEnum.MATCH, 3);
		createMdmLinksWithLinkedPatients(MdmMatchResultEnum.MATCH, 4);
		final List<MdmLink> mdmLinksWithLinkedPatients3 = createMdmLinksWithLinkedPatients(MdmMatchResultEnum.MATCH, 2);
		flipLinksTo(mdmLinksWithLinkedPatients3, MdmMatchResultEnum.NO_MATCH);

		final MdmHistorySearchParameters mdmHistorySearchParameters =
			new MdmHistorySearchParameters()
				.setGoldenResourceIds(getIdsFromMdmLinks(MdmLink::getGoldenResourcePersistenceId, mdmLinksWithLinkedPatients1.get(0), mdmLinksWithLinkedPatients3.get(0)));

		final List<MdmLinkWithRevision<MdmLink>> actualMdmLinkRevisions = myMdmLinkDaoSvc.findMdmLinkHistory(mdmHistorySearchParameters);

		final JpaPid goldenResourceId1 = mdmLinksWithLinkedPatients1.get(0).getGoldenResourcePersistenceId();
		final JpaPid goldenResourceId3 = mdmLinksWithLinkedPatients3.get(0).getGoldenResourcePersistenceId();

		final JpaPid sourceId1_1 = mdmLinksWithLinkedPatients1.get(0).getSourcePersistenceId();
		final JpaPid sourceId1_2 = mdmLinksWithLinkedPatients1.get(1).getSourcePersistenceId();
		final JpaPid sourceId1_3 = mdmLinksWithLinkedPatients1.get(2).getSourcePersistenceId();

		final JpaPid sourceId3_1 = mdmLinksWithLinkedPatients3.get(0).getSourcePersistenceId();
		final JpaPid sourceId3_2 = mdmLinksWithLinkedPatients3.get(1).getSourcePersistenceId();

		final List<MdmLinkWithRevision<MdmLink>> expectedMdLinkRevisions = List.of(
			buildMdmLinkWithRevision(1, RevisionType.ADD, MdmMatchResultEnum.MATCH, goldenResourceId1, sourceId1_1),
			buildMdmLinkWithRevision(2, RevisionType.ADD, MdmMatchResultEnum.MATCH, goldenResourceId1, sourceId1_2),
			buildMdmLinkWithRevision(3, RevisionType.ADD, MdmMatchResultEnum.MATCH, goldenResourceId1, sourceId1_3),
			buildMdmLinkWithRevision(10, RevisionType.MOD, MdmMatchResultEnum.NO_MATCH, goldenResourceId3, sourceId3_1),
			buildMdmLinkWithRevision(8, RevisionType.ADD, MdmMatchResultEnum.MATCH, goldenResourceId3, sourceId3_1),
			buildMdmLinkWithRevision(11, RevisionType.MOD, MdmMatchResultEnum.NO_MATCH, goldenResourceId3, sourceId3_2),
			buildMdmLinkWithRevision(9, RevisionType.ADD, MdmMatchResultEnum.MATCH, goldenResourceId3, sourceId3_2)
		);

		assertMdmRevisionsEqual(expectedMdLinkRevisions, actualMdmLinkRevisions);
	}

	@Test
	public void testHistoryForSourceIdsOnly() {
		final List<MdmLink> mdmLinksWithLinkedPatients1 = createMdmLinksWithLinkedPatients(MdmMatchResultEnum.MATCH, 3);
		final List<MdmLink> mdmLinksWithLinkedPatients2 = createMdmLinksWithLinkedPatients(MdmMatchResultEnum.MATCH, 4);
		final List<MdmLink> mdmLinksWithLinkedPatients3 = createMdmLinksWithLinkedPatients(MdmMatchResultEnum.MATCH, 2);
		flipLinksTo(mdmLinksWithLinkedPatients3, MdmMatchResultEnum.NO_MATCH);

		final MdmHistorySearchParameters mdmHistorySearchParameters =
			new MdmHistorySearchParameters()
				.setSourceIds(getIdsFromMdmLinks(MdmLink::getSourcePersistenceId, mdmLinksWithLinkedPatients1.get(0), mdmLinksWithLinkedPatients2.get(0)));

		final List<MdmLinkWithRevision<MdmLink>> actualMdmLinkRevisions = myMdmLinkDaoSvc.findMdmLinkHistory(mdmHistorySearchParameters);

		final JpaPid goldenResourceId1 = mdmLinksWithLinkedPatients1.get(0).getGoldenResourcePersistenceId();
		final JpaPid goldenResourceId2 = mdmLinksWithLinkedPatients2.get(0).getGoldenResourcePersistenceId();

		final JpaPid sourceId1_1 = mdmLinksWithLinkedPatients1.get(0).getSourcePersistenceId();

		final JpaPid sourceId2_1 = mdmLinksWithLinkedPatients2.get(0).getSourcePersistenceId();

		final List<MdmLinkWithRevision<MdmLink>> expectedMdLinkRevisions = List.of(
			buildMdmLinkWithRevision(1, RevisionType.ADD, MdmMatchResultEnum.MATCH, goldenResourceId1, sourceId1_1),
			buildMdmLinkWithRevision(4, RevisionType.ADD, MdmMatchResultEnum.MATCH, goldenResourceId2, sourceId2_1)
		);

		assertMdmRevisionsEqual(expectedMdLinkRevisions, actualMdmLinkRevisions);
	}

	@Test
	public void testHistoryForNoIdsOnly() {
		assertThrows(IllegalArgumentException.class, () -> myMdmLinkDaoSvc.findMdmLinkHistory(new MdmHistorySearchParameters()));
	}

	@Test
	public void testHistoryForIdWithMultipleMatches(){
		// setup
		String commonId = "p123";

		// Patient/p123 and its golden resource
		Patient goldenPatient = createPatient();
		Patient sourcePatient = (Patient) createResourceWithId(new Patient(), commonId, Enumerations.ResourceType.PATIENT);

		MdmLink mdmPatientLink = linkGoldenAndSourceResource(MdmMatchResultEnum.MATCH, goldenPatient, sourcePatient);
		JpaPid goldenPatientId = mdmPatientLink.getGoldenResourcePersistenceId();
		// Practitioner/p123 and its golden resource
		Practitioner goldenPractitioner = createPractitioner(new Practitioner());
		Practitioner sourcePractitioner = (Practitioner) createResourceWithId(new Practitioner(), commonId, Enumerations.ResourceType.PRACTITIONER);

		linkGoldenAndSourceResource(MdmMatchResultEnum.MATCH, goldenPractitioner, sourcePractitioner);

		// execute
		MdmHistorySearchParameters mdmHistorySearchParameters = new MdmHistorySearchParameters().setSourceIds(List.of(commonId));
		List<MdmLinkWithRevision<MdmLink>> actualMdmLinkRevisions = myMdmLinkDaoSvc.findMdmLinkHistory(mdmHistorySearchParameters);

		// verify
		assertEquals(2, actualMdmLinkRevisions.size(), "Both Patient/p123 and Practitioner/p123 should be returned");
	}

	@Nonnull
	private static List<String> getIdsFromMdmLinks(Function<MdmLink, JpaPid> getIdFunction, MdmLink... mdmLinks) {
		return Arrays.stream(mdmLinks)
			.map(getIdFunction)
			.map(JpaPid::getId).map(along -> Long.toString(along))
			.collect(Collectors.toUnmodifiableList());
	}

	private void assertMdmRevisionsEqual(List<MdmLinkWithRevision<MdmLink>> expectedMdmLinkRevisions, List<MdmLinkWithRevision<MdmLink>> actualMdmLinkRevisions) {
		assertNotNull(actualMdmLinkRevisions);

		assertEquals(expectedMdmLinkRevisions.size(), actualMdmLinkRevisions.size());

		for (int index = 0; index < expectedMdmLinkRevisions.size(); index++) {
			final MdmLinkWithRevision<MdmLink> expectedMdmLinkRevision = expectedMdmLinkRevisions.get(index);
			final MdmLinkWithRevision<MdmLink> actualMdmLinkRevision = actualMdmLinkRevisions.get(index);

			final EnversRevision expectedEnversRevision = expectedMdmLinkRevision.getEnversRevision();
			final EnversRevision actualEnversRevision = actualMdmLinkRevision.getEnversRevision();
			final MdmLink expectedMdmLink = expectedMdmLinkRevision.getMdmLink();
			final MdmLink actualMdmLink = actualMdmLinkRevision.getMdmLink();

			assertEquals(expectedMdmLink.getMatchResult(), actualMdmLink.getMatchResult());
			assertEquals(expectedMdmLink.getGoldenResourcePersistenceId(), actualMdmLinkRevision.getMdmLink().getGoldenResourcePersistenceId());
			assertEquals(expectedMdmLink.getSourcePersistenceId(), actualMdmLinkRevision.getMdmLink().getSourcePersistenceId());

			assertEquals(expectedEnversRevision.getRevisionType(), actualEnversRevision.getRevisionType());
			// TODO:  LD:  when running this unit test on a pipeline, it's impossible to assert a revision number because of all the other MdmLinks
			// created by other tests.  So for now, simply assert the revision is greater than 0
			assertTrue(actualEnversRevision.getRevisionNumber() > 0);
		}
	}

	private MdmLinkWithRevision<MdmLink> buildMdmLinkWithRevision(long theRevisionNumber, RevisionType theRevisionType, MdmMatchResultEnum theMdmMatchResultEnum, JpaPid theGolderResourceId, JpaPid theSourceId) {
		final MdmLink mdmLink = new MdmLink();

		mdmLink.setMatchResult(theMdmMatchResultEnum);
		mdmLink.setGoldenResourcePersistenceId(theGolderResourceId);
		mdmLink.setSourcePersistenceId(theSourceId);

		final MdmLinkWithRevision<MdmLink> mdmLinkWithRevision = new MdmLinkWithRevision<>(mdmLink, new EnversRevision(theRevisionType, theRevisionNumber, new Date()));

		return mdmLinkWithRevision;
	}

	private void flipLinksTo(List<MdmLink> theMdmLinksWithLinkedPatients, MdmMatchResultEnum theMdmMatchResultEnum) {
		theMdmLinksWithLinkedPatients.forEach(mdmLink -> {
			mdmLink.setMatchResult(theMdmMatchResultEnum);
			myMdmLinkDaoSvc.save(mdmLink);
		});
	}

	private List<MdmLink> createMdmLinksWithLinkedPatients(MdmMatchResultEnum theFirstMdmMatchResultEnum, int numTargetPatients) {
		final Patient goldenPatient = createPatient();

		return IntStream.range(0, numTargetPatients).mapToObj(myInt -> {
			final Patient targetPatient = createPatient();
			return linkGoldenAndSourceResource(theFirstMdmMatchResultEnum, goldenPatient, targetPatient);
		}).toList();
	}

	private MdmLink linkGoldenAndSourceResource(MdmMatchResultEnum theFirstMdmMatchResultEnum, IBaseResource theGoldenResource, IBaseResource theTargetResource) {
		MdmLink mdmLink = (MdmLink) myMdmLinkDaoSvc.newMdmLink();
		mdmLink.setLinkSource(MdmLinkSourceEnum.MANUAL);
		mdmLink.setMatchResult(theFirstMdmMatchResultEnum);
		mdmLink.setCreated(new Date());
		mdmLink.setUpdated(new Date());
		mdmLink.setGoldenResourcePersistenceId(runInTransaction(() -> myIdHelperService.getPidOrNull(RequestPartitionId.allPartitions(), theGoldenResource)));
		mdmLink.setSourcePersistenceId(runInTransaction(() -> myIdHelperService.getPidOrNull(RequestPartitionId.allPartitions(), theTargetResource)));
		return myMdmLinkDao.save(mdmLink);
	}
}
