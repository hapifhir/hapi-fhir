package ca.uhn.fhir.jpa.mdm.dao;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.jpa.mdm.BaseMdmR4Test;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.entity.EnversRevision;
import ca.uhn.fhir.jpa.util.TestUtil;
import ca.uhn.fhir.mdm.api.IMdmLink;
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmLinkWithRevision;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.api.params.MdmHistorySearchParameters;
import ca.uhn.fhir.mdm.model.MdmPidTuple;
import ca.uhn.fhir.mdm.rules.json.MdmRulesJson;
import jakarta.annotation.Nonnull;
import org.hibernate.envers.RevisionType;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MdmLinkDaoSvcTest extends BaseMdmR4Test {

	private static final Logger ourLog = LoggerFactory.getLogger(MdmLinkDaoSvcTest.class);

	@Test
	public void testCreate() {
		MdmLink mdmLink = createResourcesAndBuildTestMDMLink();
		assertNull(mdmLink.getCreated());
		assertNull(mdmLink.getUpdated());
		myMdmLinkDaoSvc.save(mdmLink);
		assertNotNull(mdmLink.getCreated());
		assertNotNull(mdmLink.getUpdated());
		assertTrue(mdmLink.getUpdated().getTime() - mdmLink.getCreated().getTime() < 1000);
	}

	@Test
	public void testUpdate() {
		MdmLink createdLink = myMdmLinkDaoSvc.save(createResourcesAndBuildTestMDMLink());
		assertEquals(MdmLinkSourceEnum.MANUAL, createdLink.getLinkSource());
		TestUtil.sleepOneClick();
		createdLink.setLinkSource(MdmLinkSourceEnum.AUTO);
		MdmLink updatedLink = myMdmLinkDaoSvc.save(createdLink);
		assertThat(updatedLink.getUpdated()).isNotEqualTo(updatedLink.getCreated());
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

		assertThat(lists).hasSize(10);

		lists.stream()
			.forEach(tuple -> {
			assertEquals(myIdHelperService.getPidOrThrowException(golden).getId(), tuple.getGoldenPid().getId());
			assertThat(tuple.getSourcePid().getId()).isIn(expectedExpandedPids);
				});
	}

	@Test
	public void testHistoryForMultipleIdsCrud() {
		final List<MdmLink> mdmLinksWithLinkedPatients1 = createMdmLinksWithLinkedPatients(MdmMatchResultEnum.MATCH, 3);
		final List<MdmLink> mdmLinksWithLinkedPatients2 = createMdmLinksWithLinkedPatients(MdmMatchResultEnum.MATCH, 4);
		final List<MdmLink> mdmLinksWithLinkedPatients3 = createMdmLinksWithLinkedPatients(MdmMatchResultEnum.MATCH, 2);
		flipLinksTo(mdmLinksWithLinkedPatients3, MdmMatchResultEnum.NO_MATCH);

		final MdmHistorySearchParameters mdmHistorySearchParametersResourceIds =
			new MdmHistorySearchParameters()
				.setGoldenResourceIds(getIdsFromMdmLinks(MdmLink::getGoldenResourcePersistenceId, mdmLinksWithLinkedPatients1.get(0), mdmLinksWithLinkedPatients3.get(0)));

		final List<MdmLinkWithRevision<MdmLink>> actualMdmLinkRevisionsResourceIds = myMdmLinkDaoSvc.findMdmLinkHistory(mdmHistorySearchParametersResourceIds);

		final MdmHistorySearchParameters mdmHistorySearchParametersGoldenResourceIds =
			new MdmHistorySearchParameters()
				.setSourceIds(getIdsFromMdmLinks(MdmLink::getSourcePersistenceId, mdmLinksWithLinkedPatients1.get(0), mdmLinksWithLinkedPatients2.get(0)));

		final List<MdmLinkWithRevision<MdmLink>> actualMdmLinkRevisionsGoldenResourceIds = myMdmLinkDaoSvc.findMdmLinkHistory(mdmHistorySearchParametersGoldenResourceIds);

		final List<MdmLinkWithRevision<MdmLink>> actualMdmLinkRevisionsJoined = joinAndHandleRepetitiveLinks(actualMdmLinkRevisionsResourceIds, actualMdmLinkRevisionsGoldenResourceIds);

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
			buildMdmLinkWithRevision(10, RevisionType.MOD, MdmMatchResultEnum.NO_MATCH, goldenResourceId3, sourceId3_1),
			buildMdmLinkWithRevision(8, RevisionType.ADD, MdmMatchResultEnum.MATCH, goldenResourceId3, sourceId3_1),
			buildMdmLinkWithRevision(11, RevisionType.MOD, MdmMatchResultEnum.NO_MATCH, goldenResourceId3, sourceId3_2),
			buildMdmLinkWithRevision(9, RevisionType.ADD, MdmMatchResultEnum.MATCH, goldenResourceId3, sourceId3_2),
			buildMdmLinkWithRevision(4, RevisionType.ADD, MdmMatchResultEnum.MATCH, goldenResourceId2, sourceId2_1)
		);

		assertMdmRevisionsEqual(expectedMdLinkRevisions, actualMdmLinkRevisionsJoined);
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
	public void testHistoryForBothSourceAndGoldenResourceIds(){
		// setup
		MdmLink targetMdmLink = createMdmLinksWithLinkedPatients(MdmMatchResultEnum.MATCH, 2).get(0);

		// link both patient to the GR
		String goldenPatientId = targetMdmLink.getGoldenResourcePersistenceId().getId().toString();
		String sourcePatientId = targetMdmLink.getSourcePersistenceId().getId().toString();

		// execute
		MdmHistorySearchParameters mdmHistorySearchParameters = new MdmHistorySearchParameters()
			.setSourceIds(List.of(sourcePatientId))
			.setGoldenResourceIds(List.of(goldenPatientId));

		List<MdmLinkWithRevision<MdmLink>> actualMdmLinkRevisions = myMdmLinkDaoSvc.findMdmLinkHistory(mdmHistorySearchParameters);

		// verify
		assertThat(actualMdmLinkRevisions).hasSize(1);
		MdmLink actualMdmLink = actualMdmLinkRevisions.get(0).getMdmLink();
		assertEquals(goldenPatientId, actualMdmLink.getGoldenResourcePersistenceId().getId().toString());
		assertEquals(sourcePatientId, actualMdmLink.getSourcePersistenceId().getId().toString());
	}

	@Test
	public void testHistoryForNoIdsOnly() {
		assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> myMdmLinkDaoSvc.findMdmLinkHistory(new MdmHistorySearchParameters()));
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
		assertThat(actualMdmLinkRevisions.size()).as("Both Patient/p123 and Practitioner/p123 should be returned").isEqualTo(2);
	}

	@ParameterizedTest
	@ValueSource(strings = {"allUnknown", "someUnknown"})
	public void testHistoryForUnknownIdsSourceIdOnly(String mode) {
		// setup
		final List<MdmLink> mdmLinksWithLinkedPatients1 = createMdmLinksWithLinkedPatients(MdmMatchResultEnum.MATCH, 3);
		final List<MdmLink> mdmLinksWithLinkedPatients2 = createMdmLinksWithLinkedPatients(MdmMatchResultEnum.MATCH, 4);

		MdmHistorySearchParameters mdmHistorySearchParameters = null;
		List<MdmLinkWithRevision<MdmLink>> expectedMdLinkRevisions = null;
		switch (mode) {
			// $mdm-link-history?resourceId=Patient/unknown
			case "allUnknown" -> {
				mdmHistorySearchParameters = new MdmHistorySearchParameters().setSourceIds(List.of("unknown"));
				expectedMdLinkRevisions = new ArrayList<>();
			}
			// $mdm-link-history?resourceId=Patient/1,Patient/2,Patient/unknown
			case "someUnknown" -> {
				List<String> resourceIdsWithSomeUnknown = new ArrayList<>(getIdsFromMdmLinks(MdmLink::getSourcePersistenceId, mdmLinksWithLinkedPatients1.get(0), mdmLinksWithLinkedPatients2.get(0)));
				resourceIdsWithSomeUnknown.add("unknown");
				mdmHistorySearchParameters = new MdmHistorySearchParameters().setSourceIds(resourceIdsWithSomeUnknown);

				final JpaPid goldenResourceId1 = mdmLinksWithLinkedPatients1.get(0).getGoldenResourcePersistenceId();
				final JpaPid goldenResourceId2 = mdmLinksWithLinkedPatients2.get(0).getGoldenResourcePersistenceId();
				final JpaPid sourceId1_1 = mdmLinksWithLinkedPatients1.get(0).getSourcePersistenceId();
				final JpaPid sourceId2_1 = mdmLinksWithLinkedPatients2.get(0).getSourcePersistenceId();
				expectedMdLinkRevisions = List.of(
					buildMdmLinkWithRevision(1, RevisionType.ADD, MdmMatchResultEnum.MATCH, goldenResourceId1, sourceId1_1),
					buildMdmLinkWithRevision(4, RevisionType.ADD, MdmMatchResultEnum.MATCH, goldenResourceId2, sourceId2_1)
				);
			}
		}

		// execute
		final List<MdmLinkWithRevision<MdmLink>> actualMdmLinkRevisions = myMdmLinkDaoSvc.findMdmLinkHistory(mdmHistorySearchParameters);

		// verify
		assert expectedMdLinkRevisions != null;
		assertMdmRevisionsEqual(expectedMdLinkRevisions, actualMdmLinkRevisions);
	}

	@ParameterizedTest
	@ValueSource(strings = {"allUnknown", "someUnknown"})
	public void testHistoryForUnknownIdsGoldenResourceIdOnly(String mode) {
		// setup
		final List<MdmLink> mdmLinksWithLinkedPatients1 = createMdmLinksWithLinkedPatients(MdmMatchResultEnum.MATCH, 3);
		final List<MdmLink> mdmLinksWithLinkedPatients2 = createMdmLinksWithLinkedPatients(MdmMatchResultEnum.MATCH, 4);

		MdmHistorySearchParameters mdmHistorySearchParameters = null;
		List<MdmLinkWithRevision<MdmLink>> expectedMdLinkRevisions = null;
		switch (mode) {
			// $mdm-link-history?goldenResourceId=Patient/unknown
			case "allUnknown" -> {
				mdmHistorySearchParameters = new MdmHistorySearchParameters().setGoldenResourceIds(List.of("unknown"));
				expectedMdLinkRevisions = new ArrayList<>();
			}
			// $mdm-link-history?goldenResourceId=Patient/1,Patient/2,Patient/unknown
			case "someUnknown" -> {
				List<String> resourceIdsWithSomeUnknown = new ArrayList<>(getIdsFromMdmLinks(MdmLink::getGoldenResourcePersistenceId, mdmLinksWithLinkedPatients1.get(0), mdmLinksWithLinkedPatients2.get(0)));
				resourceIdsWithSomeUnknown.add("unknown");
				mdmHistorySearchParameters = new MdmHistorySearchParameters().setGoldenResourceIds(resourceIdsWithSomeUnknown);

				final JpaPid goldenResourceId1 = mdmLinksWithLinkedPatients1.get(0).getGoldenResourcePersistenceId();
				final JpaPid goldenResourceId2 = mdmLinksWithLinkedPatients2.get(0).getGoldenResourcePersistenceId();
				final JpaPid sourceId1_1 = mdmLinksWithLinkedPatients1.get(0).getSourcePersistenceId();
				final JpaPid sourceId1_2 = mdmLinksWithLinkedPatients1.get(1).getSourcePersistenceId();
				final JpaPid sourceId1_3 = mdmLinksWithLinkedPatients1.get(2).getSourcePersistenceId();
				final JpaPid sourceId2_1 = mdmLinksWithLinkedPatients2.get(0).getSourcePersistenceId();
				final JpaPid sourceId2_2 = mdmLinksWithLinkedPatients2.get(1).getSourcePersistenceId();
				final JpaPid sourceId2_3 = mdmLinksWithLinkedPatients2.get(2).getSourcePersistenceId();
				final JpaPid sourceId2_4 = mdmLinksWithLinkedPatients2.get(3).getSourcePersistenceId();
				expectedMdLinkRevisions = List.of(
					buildMdmLinkWithRevision(1, RevisionType.ADD, MdmMatchResultEnum.MATCH, goldenResourceId1, sourceId1_1),
					buildMdmLinkWithRevision(2, RevisionType.ADD, MdmMatchResultEnum.MATCH, goldenResourceId1, sourceId1_2),
					buildMdmLinkWithRevision(3, RevisionType.ADD, MdmMatchResultEnum.MATCH, goldenResourceId1, sourceId1_3),
					buildMdmLinkWithRevision(4, RevisionType.ADD, MdmMatchResultEnum.MATCH, goldenResourceId2, sourceId2_1),
					buildMdmLinkWithRevision(5, RevisionType.ADD, MdmMatchResultEnum.MATCH, goldenResourceId2, sourceId2_2),
					buildMdmLinkWithRevision(6, RevisionType.ADD, MdmMatchResultEnum.MATCH, goldenResourceId2, sourceId2_3),
					buildMdmLinkWithRevision(7, RevisionType.ADD, MdmMatchResultEnum.MATCH, goldenResourceId2, sourceId2_4)
				);
			}
		}

		// execute
		final List<MdmLinkWithRevision<MdmLink>> actualMdmLinkRevisions = myMdmLinkDaoSvc.findMdmLinkHistory(mdmHistorySearchParameters);

		// verify
		assert expectedMdLinkRevisions != null;
		assertMdmRevisionsEqual(expectedMdLinkRevisions, actualMdmLinkRevisions);
	}

	@ParameterizedTest
	@ValueSource(strings = {
		"allUnknownSourceId",
		"allUnknownGoldenId",
		"allUnknownBoth",
		"someUnknownSourceId",
		"someUnknownGoldenId",
		"someUnknownBoth"
	})
	public void testHistoryForUnknownIdsBothSourceAndGoldenResourceId(String mode) {
		// setup
		final List<MdmLink> mdmLinksWithLinkedPatients1 = createMdmLinksWithLinkedPatients(MdmMatchResultEnum.MATCH, 3);
		final List<MdmLink> mdmLinksWithLinkedPatients2 = createMdmLinksWithLinkedPatients(MdmMatchResultEnum.MATCH, 4);

		MdmHistorySearchParameters mdmHistorySearchParameters = null;
		final JpaPid goldenResourceId1 = mdmLinksWithLinkedPatients1.get(0).getGoldenResourcePersistenceId();
		final JpaPid goldenResourceId2 = mdmLinksWithLinkedPatients2.get(0).getGoldenResourcePersistenceId();
		final JpaPid sourceId1_1 = mdmLinksWithLinkedPatients1.get(0).getSourcePersistenceId();
		final JpaPid sourceId2_1 = mdmLinksWithLinkedPatients2.get(0).getSourcePersistenceId();
		List<MdmLinkWithRevision<MdmLink>> expectedMdLinkRevisions = List.of(
			buildMdmLinkWithRevision(1, RevisionType.ADD, MdmMatchResultEnum.MATCH, goldenResourceId1, sourceId1_1),
			buildMdmLinkWithRevision(4, RevisionType.ADD, MdmMatchResultEnum.MATCH, goldenResourceId2, sourceId2_1)
		);
		switch (mode) {
			// $mdm-link-history?resourceId=Patient/unknown&goldenResourceId=Patient/1,Patient/2
			case "allUnknownSourceId" -> {
				mdmHistorySearchParameters = new MdmHistorySearchParameters()
					.setSourceIds(List.of("unknown"))
					.setGoldenResourceIds(new ArrayList<>(getIdsFromMdmLinks(MdmLink::getGoldenResourcePersistenceId, mdmLinksWithLinkedPatients1.get(0), mdmLinksWithLinkedPatients2.get(0))));
				expectedMdLinkRevisions = new ArrayList<>();
			}
			// $mdm-link-history?resourceId=Patient/1,Patient/2&goldenResourceId=Patient/unknown
			case "allUnknownGoldenId" -> {
				mdmHistorySearchParameters = new MdmHistorySearchParameters()
					.setSourceIds(new ArrayList<>(getIdsFromMdmLinks(MdmLink::getSourcePersistenceId, mdmLinksWithLinkedPatients1.get(0), mdmLinksWithLinkedPatients2.get(0))))
					.setGoldenResourceIds(List.of("unknown"));
				expectedMdLinkRevisions = new ArrayList<>();
			}
			// $mdm-link-history?resourceId=Patient/unknown&goldenResourceId=Patient/unknownGolden
			case "allUnknownBoth" -> {
				mdmHistorySearchParameters = new MdmHistorySearchParameters()
					.setSourceIds(List.of("unknown"))
					.setGoldenResourceIds(List.of("unknownGolden"));
				expectedMdLinkRevisions = new ArrayList<>();
			}
			// $mdm-link-history?resourceId=Patient/1,Patient/2,Patient/unknown&goldenResourceId=Patient/3,Patient/4
			case "someUnknownSourceId" -> {
				List<String> sourceIdsWithSomeUnknown = new ArrayList<>(getIdsFromMdmLinks(MdmLink::getSourcePersistenceId, mdmLinksWithLinkedPatients1.get(0), mdmLinksWithLinkedPatients2.get(0)));
				sourceIdsWithSomeUnknown.add("unknown");
				List<String> goldenResourceIds = new ArrayList<>(getIdsFromMdmLinks(MdmLink::getGoldenResourcePersistenceId, mdmLinksWithLinkedPatients1.get(0), mdmLinksWithLinkedPatients2.get(0)));
				mdmHistorySearchParameters = new MdmHistorySearchParameters()
					.setSourceIds(sourceIdsWithSomeUnknown)
					.setGoldenResourceIds(goldenResourceIds);
			}
			// $mdm-link-history?resourceId=Patient/1,Patient/2&goldenResourceId=Patient/3,Patient/4,Patient/unknown
			case "someUnknownGoldenId" -> {
				List<String> sourceIds = new ArrayList<>(getIdsFromMdmLinks(MdmLink::getSourcePersistenceId, mdmLinksWithLinkedPatients1.get(0), mdmLinksWithLinkedPatients2.get(0)));
				List<String> goldenResourceIdsSomeUnknown = new ArrayList<>(getIdsFromMdmLinks(MdmLink::getGoldenResourcePersistenceId, mdmLinksWithLinkedPatients1.get(0), mdmLinksWithLinkedPatients2.get(0)));
				goldenResourceIdsSomeUnknown.add("unknown");
				mdmHistorySearchParameters = new MdmHistorySearchParameters()
					.setSourceIds(sourceIds)
					.setGoldenResourceIds(goldenResourceIdsSomeUnknown);
			}
			// $mdm-link-history?resourceId=Patient/1,Patient/2,Patient/unknown&goldenResourceId=Patient/3,Patient/4,Patient/unknownGolden
			case "someUnknownBoth" -> {
				List<String> sourceIdsSomeUnknown = new ArrayList<>(getIdsFromMdmLinks(MdmLink::getSourcePersistenceId, mdmLinksWithLinkedPatients1.get(0), mdmLinksWithLinkedPatients2.get(0)));
				sourceIdsSomeUnknown.add("unknown");
				List<String> goldenResourceIdsSomeUnknown = new ArrayList<>(getIdsFromMdmLinks(MdmLink::getGoldenResourcePersistenceId, mdmLinksWithLinkedPatients1.get(0), mdmLinksWithLinkedPatients2.get(0)));
				goldenResourceIdsSomeUnknown.add("unknownGolden");
				mdmHistorySearchParameters = new MdmHistorySearchParameters()
					.setSourceIds(sourceIdsSomeUnknown)
					.setGoldenResourceIds(goldenResourceIdsSomeUnknown);
			}
		}

		// execute
		final List<MdmLinkWithRevision<MdmLink>> actualMdmLinkRevisions = myMdmLinkDaoSvc.findMdmLinkHistory(mdmHistorySearchParameters);

		// verify
		assert expectedMdLinkRevisions != null;
		assertMdmRevisionsEqual(expectedMdLinkRevisions, actualMdmLinkRevisions);
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

		assertThat(actualMdmLinkRevisions).hasSize(expectedMdmLinkRevisions.size());

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

	private List<MdmLinkWithRevision<MdmLink>> joinAndHandleRepetitiveLinks(List<MdmLinkWithRevision<MdmLink>> toLinks, List<MdmLinkWithRevision<MdmLink>> fromLinks){
		List<MdmLinkWithRevision<MdmLink>> joinedLinks = new ArrayList<>(toLinks);
		Set<String> joinedLinkIds = new HashSet<>();
		toLinks.forEach(link -> joinedLinkIds.add(link.getMdmLink().getId().toString()));
		for (MdmLinkWithRevision<MdmLink> link : fromLinks){
			if (!joinedLinkIds.contains(link.getMdmLink().getId().toString())){
				joinedLinks.add(link);
			}
		}

		return joinedLinks;
	}
}
