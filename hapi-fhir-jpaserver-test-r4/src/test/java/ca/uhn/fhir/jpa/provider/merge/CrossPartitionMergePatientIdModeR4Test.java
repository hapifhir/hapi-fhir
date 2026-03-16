package ca.uhn.fhir.jpa.provider.merge;

import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.interceptor.PatientIdPartitionInterceptor;
import ca.uhn.fhir.jpa.merge.MergeOperationTestHelper;
import ca.uhn.fhir.jpa.merge.MergeTestParameters;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.jpa.searchparam.extractor.ISearchParamExtractor;
import ca.uhn.fhir.jpa.test.Batch2JobHelper;
import ca.uhn.fhir.merge.ResourceLinkServiceFactory;
import ca.uhn.fhir.parser.StrictErrorHandler;
import ca.uhn.fhir.rest.api.CacheControlDirective;
import ca.uhn.fhir.rest.gclient.ReferenceClientParam;
import ca.uhn.fhir.rest.server.exceptions.NotImplementedOperationException;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Group;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.ListResource;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Provenance;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static ca.uhn.fhir.jpa.config.r4.FhirContextR4Config.DEFAULT_PRESERVE_VERSION_REFS_R4_AND_LATER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.catchException;

/**
 * Integration tests for cross-partition Patient $merge in PATIENT_ID unnamed partition mode.
 *
 * <p>Tests the behavior of {@code CrossPartitionReplaceReferencesSvc} when merging
 * patients that reside in different partitions (as determined by {@code PatientIdPartitionInterceptor}).
 */
// Created by claude-opus-4-6
public class CrossPartitionMergePatientIdModeR4Test extends BaseResourceProviderR4Test {

	private static final Logger ourLog = LoggerFactory.getLogger(CrossPartitionMergePatientIdModeR4Test.class);

	@Autowired
	private Batch2JobHelper myBatch2JobHelper;
	@Autowired
	private ISearchParamExtractor mySearchParamExtractor;
	@Autowired
	private ResourceLinkServiceFactory myResourceLinkServiceFactory;
	private PatientIdPartitionInterceptor myPartitionInterceptor;
	private MergeOperationTestHelper myMergeHelper;
	private IIdType myPatientIdA;
	private IIdType myPatientIdB;

	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();

		myPartitionInterceptor = new PatientIdPartitionInterceptor(
			getFhirContext(), mySearchParamExtractor, myPartitionSettings, myDaoRegistry);
		registerInterceptor(myPartitionInterceptor);

		myPartitionSettings.setPartitioningEnabled(true);
		myPartitionSettings.setUnnamedPartitionMode(true);
		myPartitionSettings.setAllowReferencesAcrossPartitions(
			PartitionSettings.CrossPartitionReferenceMode.ALLOWED_UNQUALIFIED);

		// Disable search cache: merge mutates data mid-test and subsequent searches
		// would return stale cached results
		myStorageSettings.setReuseCachedSearchResultsForMillis(null);

		myFhirContext.setParserErrorHandler(new StrictErrorHandler());
		myFhirContext.getParserOptions().setDontStripVersionsFromReferencesAtPaths("Provenance.target");

		myMergeHelper = new MergeOperationTestHelper(
			myClient, myBatch2JobHelper, myFhirContext, myResourceLinkServiceFactory, myDaoRegistry);

		myPatientIdA = createPatient("Patient/A").getIdElement().toUnqualifiedVersionless();
		myPatientIdB = createPatient("Patient/B").getIdElement().toUnqualifiedVersionless();
		assertInDifferentPartitions(myPatientIdA, myPatientIdB);
	}

	@Override
	@AfterEach
	public void after() throws Exception {
		super.after();

		PartitionSettings defaultPartitionSettings = new PartitionSettings();
		myPartitionSettings.setPartitioningEnabled(defaultPartitionSettings.isPartitioningEnabled());
		myPartitionSettings.setUnnamedPartitionMode(defaultPartitionSettings.isUnnamedPartitionMode());
		myPartitionSettings.setAllowReferencesAcrossPartitions(
			defaultPartitionSettings.getAllowReferencesAcrossPartitions());

		myStorageSettings.setReuseCachedSearchResultsForMillis(
			new JpaStorageSettings().getReuseCachedSearchResultsForMillis());
		myFhirContext.getParserOptions().setDontStripVersionsFromReferencesAtPaths(
			DEFAULT_PRESERVE_VERSION_REFS_R4_AND_LATER);
	}

	// ================================================
	// HELPER METHODS
	// ================================================

	/**
	 * Returns the partition ID for the given resource. May return null for resources
	 * in the default partition (e.g., non-compartment resources like Organization, Group).
	 */
	private Integer getPartitionId(IIdType theId) {
		return runInTransaction(() -> {
			ResourceTable resourceTable = myResourceTableDao
				.findByTypeAndFhirId(theId.getResourceType(), theId.getIdPart())
				.orElseThrow(() -> new AssertionError("Resource not found: " + theId.toUnqualifiedVersionless()));
			return resourceTable.getPartitionId().getPartitionId();
		});
	}

	private void assertInDifferentPartitions(IIdType theId1, IIdType theId2) {
		Integer partitionId1 = getPartitionId(theId1);
		Integer partitionId2 = getPartitionId(theId2);
		assertThat(partitionId1)
			.as("Expected %s and %s to be in different partitions", theId1, theId2)
			.isNotEqualTo(partitionId2);
	}

	private void assertInSamePartition(IIdType theId1, IIdType theId2) {
		Integer partitionId1 = getPartitionId(theId1);
		Integer partitionId2 = getPartitionId(theId2);
		assertThat(partitionId1)
			.as("Expected %s and %s to be in the same partition", theId1, theId2)
			.isEqualTo(partitionId2);
	}

	private <T extends IBaseResource> List<T> searchBySubject(Class<T> theResourceClass, String theSubjectRef) {
		Bundle bundle = myClient.search()
			.forResource(theResourceClass)
			.where(new ReferenceClientParam("subject").hasId(theSubjectRef))
			.cacheControl(new CacheControlDirective().setNoCache(true))
			.returnBundle(Bundle.class)
			.execute();
		return bundle.getEntry().stream()
			.map(e -> theResourceClass.cast(e.getResource()))
			.toList();
	}

	private <T extends IBaseResource> List<T> assertResourcesMovedToTarget(
		Class<T> theResourceClass,
		List<IIdType> theOldResourceIds,
		IIdType theTargetPatient) {

		List<T> found = searchBySubject(theResourceClass, theTargetPatient.getValue());
		assertThat(found).hasSize(theOldResourceIds.size());
		for (T resource : found) {
			assertInSamePartition(resource.getIdElement().toUnqualifiedVersionless(), theTargetPatient);
		}
		for (IIdType oldId : theOldResourceIds) {
			assertResourceGoneViaClient(oldId);
		}
		return found;
	}

	private <T extends IBaseResource> T assertSingleResourceMovedToTarget(
		Class<T> theResourceClass,
		IIdType theOldResourceId,
		IIdType theTargetPatient) {

		return assertResourcesMovedToTarget(theResourceClass, List.of(theOldResourceId), theTargetPatient).get(0);
	}

	private Patient createPatient(String theId) {
		Patient patient = new Patient();
		patient.setId(theId);
		patient.setActive(true);
		myClient.update().resource(patient).execute();
		return patient;
	}

	private IIdType createObservation(IIdType theSubject, IIdType theEncounter, String theIdentifierValue) {
		Observation obs = new Observation();
		obs.getSubject().setReference(theSubject.getValue());
		if (theEncounter != null) {
			obs.getEncounter().setReference(theEncounter.getValue());
		}
		if (theIdentifierValue != null) {
			obs.addIdentifier(createTestIdentifier(theIdentifierValue));
		}
		return myClient.create().resource(obs).execute().getId().toUnqualifiedVersionless();
	}

	private IIdType createEncounter(IIdType theSubject, String theIdentifierValue) {
		Encounter enc = new Encounter();
		enc.getSubject().setReference(theSubject.getValue());
		enc.setStatus(Encounter.EncounterStatus.FINISHED);
		if (theIdentifierValue != null) {
			enc.addIdentifier(createTestIdentifier(theIdentifierValue));
		}
		return myClient.create().resource(enc).execute().getId().toUnqualifiedVersionless();
	}

	private IIdType createGroup(IIdType theMember) {
		Group group = new Group();
		group.setType(Group.GroupType.PERSON);
		group.setActual(true);
		group.addMember().getEntity().setReference(theMember.getValue());
		return myClient.create().resource(group).execute().getId().toUnqualifiedVersionless();
	}

	private Parameters callMerge(MergeTestParameters theParams) {
		return myMergeHelper.callMergeOperation("Patient", theParams, false);
	}

	private void assertResourceGoneViaClient(IIdType theId) {
		var readRequest = myClient.read().resource(theId.getResourceType()).withId(theId);
		assertThatThrownBy(readRequest::execute)
			.isInstanceOf(ResourceGoneException.class);
	}

	private Identifier createTestIdentifier(String theValue) {
		return new Identifier().setSystem("http://test").setValue(theValue);
	}

	// ================================================
	// TEST SCENARIOS
	// ================================================

	@Nested
	class CompartmentResourceMovement {

		// Observation(subject=PatientA). Merges PatientA→PatientB: Obs moves to PatientB's partition with a new ID,
		// old ID returns 410. Tested with both deleteSource=true and deleteSource=false.
		@ParameterizedTest
		@CsvSource({"false", "true"})
		void testMerge_singleObservationMoves(boolean theDeleteSource) {
			// Setup
			IIdType obsId = createObservation(myPatientIdA, null, "obs-a");

			// Execute
			MergeTestParameters mergeParams = new MergeTestParameters()
				.sourceResource(new Reference(myPatientIdA))
				.targetResource(new Reference(myPatientIdB))
				.deleteSource(theDeleteSource);
			Parameters result = callMerge(mergeParams);
			myMergeHelper.validateSyncSuccessMessage(result);

			// Verify: Obs moved to PatientB with same identifier
			Observation movedObs = assertSingleResourceMovedToTarget(Observation.class, obsId, myPatientIdB);
			assertThat(movedObs.getIdentifierFirstRep().getValue()).isEqualTo("obs-a");
			IIdType newObsId = movedObs.getIdElement().toUnqualifiedVersionless();

			// Validate resources after merge
			IIdType expectedVersionedSourceId = myPatientIdA.withVersion("2");
			IIdType expectedVersionedTargetId = myPatientIdB.withVersion(theDeleteSource ? "1" : "2");
			List<IIdType> idsExpectedToReferenceTarget = List.of(newObsId);
			Set<String> expectedProvenanceTargets = Set.of(
				expectedVersionedSourceId.toString(),
				expectedVersionedTargetId.toString(),
				newObsId.withVersion("1").toString(),
				obsId.withVersion("2").toString());
			myMergeHelper.validateResourcesAfterMerge(mergeParams,
				expectedVersionedSourceId, expectedVersionedTargetId,
				idsExpectedToReferenceTarget, expectedProvenanceTargets);
		}

		// Encounter(subject=PatientA) + Observation(subject=PatientA, encounter=Enc). Merges PatientA→PatientB: both move
		// to PatientB's partition. The Obs's encounter reference is rewritten to the Encounter's new ID.
		// Tested with both deleteSource=true and deleteSource=false.
		@ParameterizedTest
		@CsvSource({"false", "true"})
		void testMerge_intraCompartmentCrossReferenceUpdated(boolean theDeleteSource) {
			// Setup
			IIdType encId = createEncounter(myPatientIdA, "enc-a");

			IIdType obsId = createObservation(myPatientIdA, encId, "obs-a");

			// Execute
			MergeTestParameters mergeParams = new MergeTestParameters()
				.sourceResource(new Reference(myPatientIdA))
				.targetResource(new Reference(myPatientIdB))
				.deleteSource(theDeleteSource);
			Parameters result = callMerge(mergeParams);
			myMergeHelper.validateSyncSuccessMessage(result);

			// Verify: New Encounter in PatientB's partition with identifier=enc-a
			Encounter newEnc = assertSingleResourceMovedToTarget(Encounter.class, encId, myPatientIdB);
			assertThat(newEnc.getIdentifierFirstRep().getValue()).isEqualTo("enc-a");
			IIdType newEncId = newEnc.getIdElement().toUnqualifiedVersionless();

			// New Obs in PatientB's partition with identifier=obs-a and encounter=new Encounter ID
			Observation movedObs = assertSingleResourceMovedToTarget(Observation.class, obsId, myPatientIdB);
			assertThat(movedObs.getIdentifierFirstRep().getValue()).isEqualTo("obs-a");
			assertThat(movedObs.getEncounter().getReferenceElement().getValue())
				.isEqualTo(newEncId.getValue());
			IIdType newObsId = movedObs.getIdElement().toUnqualifiedVersionless();

			// Validate resources after merge
			IIdType expectedVersionedSourceId = myPatientIdA.withVersion("2");
			IIdType expectedVersionedTargetId = myPatientIdB.withVersion(theDeleteSource ? "1" : "2");
			List<IIdType> idsExpectedToReferenceTarget = List.of(newObsId, newEncId);
			Set<String> expectedProvenanceTargets = Set.of(
				expectedVersionedSourceId.toString(),
				expectedVersionedTargetId.toString(),
				newObsId.withVersion("1").toString(),
				newEncId.withVersion("1").toString(),
				obsId.withVersion("2").toString(),
				encId.withVersion("2").toString());
			myMergeHelper.validateResourcesAfterMerge(mergeParams,
				expectedVersionedSourceId, expectedVersionedTargetId,
				idsExpectedToReferenceTarget, expectedProvenanceTargets);
		}
	}

	@Nested
	class ReferencingResourcesOutsideCompartment {

		// Group(member=PatientA) in default partition. Merges PatientA→PatientB: Group stays in the default partition
		// with its original ID, but its member reference is rewritten from PatientA to PatientB.
		// Tested with both deleteSource=true and deleteSource=false.
		@ParameterizedTest
		@CsvSource({"false", "true"})
		void testMerge_resourceInDefaultPartitionReferencingSourcePatient_staysInPlaceRefUpdated(boolean theDeleteSource) {
			// Setup
			IIdType groupId = createGroup(myPatientIdA);
			Integer groupPartitionBefore = getPartitionId(groupId);

			// Execute
			MergeTestParameters mergeParams = new MergeTestParameters()
				.sourceResource(new Reference(myPatientIdA))
				.targetResource(new Reference(myPatientIdB))
				.deleteSource(theDeleteSource);
			Parameters result = callMerge(mergeParams);
			myMergeHelper.validateSyncSuccessMessage(result);

			// Verify: Group keeps original ID and partition, reference updated
			Group updatedGroup = myClient.read().resource(Group.class).withId(groupId).execute();
			assertThat(updatedGroup.getMemberFirstRep().getEntity().getReference()).isEqualTo(myPatientIdB.getValue());
			assertThat(getPartitionId(groupId)).isEqualTo(groupPartitionBefore);

			// Validate resources after merge
			IIdType expectedVersionedSourceId = myPatientIdA.withVersion("2");
			IIdType expectedVersionedTargetId = myPatientIdB.withVersion(theDeleteSource ? "1" : "2");
			List<IIdType> idsExpectedToReferenceTarget = List.of(groupId);
			Set<String> expectedProvenanceTargets = Set.of(
				expectedVersionedSourceId.toString(),
				expectedVersionedTargetId.toString(),
				groupId.withVersion("2").toString());
			myMergeHelper.validateResourcesAfterMerge(mergeParams,
				expectedVersionedSourceId, expectedVersionedTargetId,
				idsExpectedToReferenceTarget, expectedProvenanceTargets);
		}

		// Enc(subject=PatientA) + Obs(subject=PatientC, encounter=Enc). Merges PatientA→PatientB: Enc moves to PatientB's partition,
		// Obs stays in PatientC's partition but its encounter reference is rewritten to Enc's new ID.
		// Not a realistic clinical scenario — an Observation rarely, if at all, references an Encounter from
		// a different patient's compartment — but tests the edge case where a resource that
		// doesn't move has its reference to a moved resource updated.
		@Test
		void testMerge_anotherPatientsResourceReferencingMovedResource() {
			IIdType patientIdC = createPatient("Patient/C").getIdElement().toUnqualifiedVersionless();
			assertInDifferentPartitions(myPatientIdB, patientIdC);

			IIdType encId = createEncounter(myPatientIdA, "enc-a");

			IIdType obsId = createObservation(patientIdC, encId, "obs-c");

			// Execute: merge PatientA→PatientB
			MergeTestParameters mergeParams = new MergeTestParameters()
				.sourceResource(new Reference(myPatientIdA))
				.targetResource(new Reference(myPatientIdB))
				.deleteSource(false);
			Parameters result = callMerge(mergeParams);
			myMergeHelper.validateSyncSuccessMessage(result);

			// Verify: Encounter moved to PatientB's partition
			Encounter newEnc = assertSingleResourceMovedToTarget(Encounter.class, encId, myPatientIdB);
			assertThat(newEnc.getIdentifierFirstRep().getValue()).isEqualTo("enc-a");
			IIdType newEncId = newEnc.getIdElement().toUnqualifiedVersionless();

			// Verify: Obs stays in PatientC's partition with unchanged subject, encounter ref rewritten
			Observation updatedObs = myClient.read().resource(Observation.class).withId(obsId).execute();
			assertThat(updatedObs.getSubject().getReference()).isEqualTo(patientIdC.getValue());
			assertThat(updatedObs.getIdentifierFirstRep().getValue()).isEqualTo("obs-c");
			assertInSamePartition(obsId, patientIdC);
			assertThat(updatedObs.getEncounter().getReferenceElement().getValue())
				.isEqualTo(newEncId.getValue());

			// Validate resources after merge
			IIdType expectedVersionedSourceId = myPatientIdA.withVersion("2");
			IIdType expectedVersionedTargetId = myPatientIdB.withVersion("2");
			List<IIdType> idsExpectedToReferenceTarget = List.of(newEncId);
			Set<String> expectedProvenanceTargets = Set.of(
				expectedVersionedSourceId.toString(),
				expectedVersionedTargetId.toString(),
				newEncId.withVersion("1").toString(),
				encId.withVersion("2").toString(),
				obsId.withVersion("2").toString());
			myMergeHelper.validateResourcesAfterMerge(mergeParams,
				expectedVersionedSourceId, expectedVersionedTargetId,
				idsExpectedToReferenceTarget, expectedProvenanceTargets);
		}

		// List(entries=[Enc-A, Enc-B]) in default partition. Merges PatientA→PatientB: Enc-A moves and gets a new ID,
		// List stays in default partition but its entry reference to Enc-A is rewritten to the new ID.
		// The entry referencing the unrelated Enc-B is unchanged.
		@Test
		void testMerge_resourceInDefaultPartitionReferencingMovedResource_staysInPlaceRefUpdated() {
			// Setup
			// Moved Encounter (subject=A)
			IIdType movedEncId = createEncounter(myPatientIdA, "enc-a");

			// Unrelated Encounter (subject=B) — same type but not moved
			Encounter unrelatedEnc = new Encounter();
			unrelatedEnc.getSubject().setReference(myPatientIdB.getValue());
			unrelatedEnc.setStatus(Encounter.EncounterStatus.PLANNED);
			unrelatedEnc.addIdentifier(createTestIdentifier("enc-b"));
			IIdType unrelatedEncId = myClient.create().resource(unrelatedEnc).execute().getId().toUnqualifiedVersionless();

			// List in default partition with two entries
			ListResource list = new ListResource();
			list.setStatus(ListResource.ListStatus.CURRENT);
			list.setMode(ListResource.ListMode.WORKING);
			list.addEntry().getItem().setReference(movedEncId.getValue());
			list.addEntry().getItem().setReference(unrelatedEncId.getValue());
			IIdType listId = myClient.create().resource(list).execute().getId().toUnqualifiedVersionless();
			Integer listPartitionBefore = getPartitionId(listId);

			// Execute
			MergeTestParameters mergeParams = new MergeTestParameters()
				.sourceResource(new Reference(myPatientIdA))
				.targetResource(new Reference(myPatientIdB))
				.deleteSource(false);
			Parameters result = callMerge(mergeParams);
			myMergeHelper.validateSyncSuccessMessage(result);

			// Verify: List keeps original ID and partition
			ListResource updatedList = myClient.read().resource(ListResource.class).withId(listId).execute();
			assertThat(getPartitionId(listId)).isEqualTo(listPartitionBefore);

			// entry[0] → new Encounter ID (rewritten)
			List<Encounter> newEncounters = searchBySubject(Encounter.class, myPatientIdB.getValue());
			// Both the moved encounter and the unrelated one have subject=B after merge (unrelated was already B)
			Encounter newMovedEnc = newEncounters.stream()
				.filter(e -> e.getIdentifierFirstRep().getValue().equals("enc-a"))
				.findFirst()
				.orElseThrow(() -> new AssertionError("Moved encounter with identifier=enc-a not found"));
			IIdType newMovedEncId = newMovedEnc.getIdElement().toUnqualifiedVersionless();

			assertThat(updatedList.getEntry().get(0).getItem().getReferenceElement().getValue())
				.isEqualTo(newMovedEncId.getValue());

			// entry[1] → unrelated Enc ID unchanged
			assertThat(updatedList.getEntry().get(1).getItem().getReferenceElement().getValue())
				.isEqualTo(unrelatedEncId.getValue());

			// Unrelated Encounter still readable
			Encounter readUnrelated = myClient.read().resource(Encounter.class).withId(unrelatedEncId).execute();
			assertThat(readUnrelated.getIdentifierFirstRep().getValue()).isEqualTo("enc-b");

			// Old moved Encounter → 410
			assertResourceGoneViaClient(movedEncId);

			// Validate resources after merge — Enc moved (new copy + tombstone), List updated in place
			// (encounter ref rewritten but doesn't reference Patient directly, so not a "referencing resource")
			IIdType expectedVersionedSourceId = myPatientIdA.withVersion("2");
			IIdType expectedVersionedTargetId = myPatientIdB.withVersion("2");
			List<IIdType> idsExpectedToReferenceTarget = List.of(newMovedEncId);
			Set<String> expectedProvenanceTargets = Set.of(
				expectedVersionedSourceId.toString(),
				expectedVersionedTargetId.toString(),
				newMovedEncId.withVersion("1").toString(),
				movedEncId.withVersion("2").toString(),
				listId.withVersion("2").toString());
			myMergeHelper.validateResourcesAfterMerge(mergeParams,
				expectedVersionedSourceId, expectedVersionedTargetId,
				idsExpectedToReferenceTarget, expectedProvenanceTargets);
		}

		// Obs(subject=PatientA, performer=Org). Merges PatientA→PatientB: Obs moves to PatientB's partition, subject is
		// rewritten to PatientB, but the performer reference to Organization is left unchanged.
		@Test
		void testMerge_mixedReferencesSubjectUpdatedPerformerUnchanged() {
			// Setup
			Organization org = new Organization();
			org.setName("Test Org");
			IIdType orgId = myClient.create().resource(org).execute().getId().toUnqualifiedVersionless();

			Observation obs = new Observation();
			obs.getSubject().setReference(myPatientIdA.getValue());
			obs.addPerformer().setReference(orgId.getValue());
			obs.addIdentifier(createTestIdentifier("obs-a"));
			IIdType obsId = myClient.create().resource(obs).execute().getId().toUnqualifiedVersionless();

			// Execute
			MergeTestParameters mergeParams = new MergeTestParameters()
				.sourceResource(new Reference(myPatientIdA))
				.targetResource(new Reference(myPatientIdB))
				.deleteSource(false);
			Parameters result = callMerge(mergeParams);
			myMergeHelper.validateSyncSuccessMessage(result);

			// Verify
			Observation movedObs = assertSingleResourceMovedToTarget(Observation.class, obsId, myPatientIdB);
			assertThat(movedObs.getIdentifierFirstRep().getValue()).isEqualTo("obs-a");
			assertThat(movedObs.getSubject().getReference()).isEqualTo(myPatientIdB.getValue());
			assertThat(movedObs.getPerformerFirstRep().getReferenceElement().getValue())
				.isEqualTo(orgId.getValue());
			IIdType newObsId = movedObs.getIdElement().toUnqualifiedVersionless();

			// Validate resources after merge
			IIdType expectedVersionedSourceId = myPatientIdA.withVersion("2");
			IIdType expectedVersionedTargetId = myPatientIdB.withVersion("2");
			List<IIdType> idsExpectedToReferenceTarget = List.of(newObsId);
			Set<String> expectedProvenanceTargets = Set.of(
				expectedVersionedSourceId.toString(),
				expectedVersionedTargetId.toString(),
				newObsId.withVersion("1").toString(),
				obsId.withVersion("2").toString());
			myMergeHelper.validateResourcesAfterMerge(mergeParams,
				expectedVersionedSourceId, expectedVersionedTargetId,
				idsExpectedToReferenceTarget, expectedProvenanceTargets);
		}
	}

	@Nested
	class VersionedReferences {

		// Provenance(target=PatientA/_history/1). Merges PatientA→PatientB: Provenance.target is in
		// setDontStripVersionsFromReferencesAtPaths, so the parser preserves the version
		// and the merge does NOT rewrite it. Provenance stays in its original partition.
		@Test
		void testMerge_versionedReferencePreservedByParser_notRewritten() {
			// Create Provenance with versioned target reference to Patient/A
			Provenance provenance = new Provenance();
			provenance.addTarget().setReference(myPatientIdA.getValue() + "/_history/1");
			provenance.getActivity().addCoding()
				.setSystem("http://terminology.hl7.org/CodeSystem/v3-DocumentCompletion")
				.setCode("LA");
			provenance.setRecorded(new Date());
			IIdType provenanceId = myClient.create().resource(provenance).execute().getId().toUnqualifiedVersionless();
			Integer provenancePartitionBefore = getPartitionId(provenanceId);

			// Execute
			MergeTestParameters mergeParams = new MergeTestParameters()
				.sourceResource(new Reference(myPatientIdA))
				.targetResource(new Reference(myPatientIdB))
				.deleteSource(false);
			Parameters result = callMerge(mergeParams);
			myMergeHelper.validateSyncSuccessMessage(result);

			// Verify: Provenance's versioned reference is NOT rewritten
			Provenance updatedProvenance = myClient.read().resource(Provenance.class).withId(provenanceId).execute();
			assertThat(updatedProvenance.getTargetFirstRep().getReference())
				.isEqualTo(myPatientIdA.getValue() + "/_history/1");
			// Stays in original partition
			assertThat(getPartitionId(provenanceId)).isEqualTo(provenancePartitionBefore);

			// Validate resources after merge — no resources reference Patient/A (Provenance.target is versioned, not rewritten)
			IIdType expectedVersionedSourceId = myPatientIdA.withVersion("2");
			IIdType expectedVersionedTargetId = myPatientIdB.withVersion("2");
			List<IIdType> idsExpectedToReferenceTarget = List.of();
			Set<String> expectedProvenanceTargets = Set.of(
				expectedVersionedSourceId.toString(), expectedVersionedTargetId.toString());
			myMergeHelper.validateResourcesAfterMerge(mergeParams,
				expectedVersionedSourceId, expectedVersionedTargetId,
				idsExpectedToReferenceTarget, expectedProvenanceTargets);
		}

		// Obs(subject=PatientA/_history/1). Merges PatientA→PatientB: Observation.subject is NOT in
		// setDontStripVersionsFromReferencesAtPaths, so the parser strips the version
		// and the merge rewrites subject to PatientB. Obs moves to PatientB's partition.
		@Test
		void testMerge_versionedReferenceStrippedByParser_isRewritten() {
			// Create Observation with versioned subject reference
			// Since Observation.subject is NOT in setDontStripVersionsFromReferencesAtPaths,
			// the parser will strip the version when reading back, so merge rewrites it.
			Observation obs = new Observation();
			obs.getSubject().setReference(myPatientIdA.getValue() + "/_history/1");
			obs.addIdentifier(createTestIdentifier("obs-a"));
			IIdType obsId = myClient.create().resource(obs).execute().getId().toUnqualifiedVersionless();

			// Execute
			MergeTestParameters mergeParams = new MergeTestParameters()
				.sourceResource(new Reference(myPatientIdA))
				.targetResource(new Reference(myPatientIdB))
				.deleteSource(false);
			Parameters result = callMerge(mergeParams);
			myMergeHelper.validateSyncSuccessMessage(result);

			// Verify: Observation moved to PatientB's partition with subject rewritten to PatientB
			Observation movedObs = assertSingleResourceMovedToTarget(Observation.class, obsId, myPatientIdB);
			assertThat(movedObs.getIdentifierFirstRep().getValue()).isEqualTo("obs-a");
			IIdType newObsId = movedObs.getIdElement().toUnqualifiedVersionless();

			// Validate resources after merge
			IIdType expectedVersionedSourceId = myPatientIdA.withVersion("2");
			IIdType expectedVersionedTargetId = myPatientIdB.withVersion("2");
			List<IIdType> idsExpectedToReferenceTarget = List.of(newObsId);
			Set<String> expectedProvenanceTargets = Set.of(
				expectedVersionedSourceId.toString(),
				expectedVersionedTargetId.toString(),
				newObsId.withVersion("1").toString(),
				obsId.withVersion("2").toString());
			myMergeHelper.validateResourcesAfterMerge(mergeParams,
				expectedVersionedSourceId, expectedVersionedTargetId,
				idsExpectedToReferenceTarget, expectedProvenanceTargets);
		}
	}

	@Nested
	class SpecialCases {

		// No compartment or referencing resources exist — just two patients.
		// Merges PatientA→PatientB: succeeds with no resource movement.
		@Test
		void testMerge_noReferencingResources_succeeds() {
			// Execute
			MergeTestParameters mergeParams = new MergeTestParameters()
				.sourceResource(new Reference(myPatientIdA))
				.targetResource(new Reference(myPatientIdB))
				.deleteSource(false);
			Parameters result = callMerge(mergeParams);

			// Verify: merge succeeds
			myMergeHelper.validateSyncSuccessMessage(result);
			IIdType expectedVersionedSourceId = myPatientIdA.withVersion("2");
			IIdType expectedVersionedTargetId = myPatientIdB.withVersion("2");
			List<IIdType> idsExpectedToReferenceTarget = List.of();
			Set<String> expectedProvenanceTargets = Set.of(
				expectedVersionedSourceId.toString(), expectedVersionedTargetId.toString());
			myMergeHelper.validateResourcesAfterMerge(mergeParams,
				expectedVersionedSourceId, expectedVersionedTargetId,
				idsExpectedToReferenceTarget, expectedProvenanceTargets);
		}

	}

	@Nested
	class ErrorCases {

		// Source patient has 3 Observations but resource-limit is set to 2.
		// Merge fails with PreconditionFailedException before any resources are moved.
		@Test
		void testMerge_resourceLimitExceeded_throwsPreconditionFailed() {
			for (int i = 0; i < 3; i++) {
				createObservation(myPatientIdA, null, null);
			}

			// Execute & verify
			MergeTestParameters params = new MergeTestParameters()
				.sourceResource(new Reference(myPatientIdA))
				.targetResource(new Reference(myPatientIdB))
				.deleteSource(false)
				.resourceLimit(2);

			myMergeHelper.callMergeAndValidateException(
				"Patient", params, PreconditionFailedException.class, "exceeds the resource-limit");
		}

		// Cross-partition merge with async=true is not supported and throws NotImplementedOperationException.
		@Test
		void testMerge_asyncRequested_throwsNotImplemented() {
			MergeTestParameters params = new MergeTestParameters()
				.sourceResource(new Reference(myPatientIdA))
				.targetResource(new Reference(myPatientIdB))
				.deleteSource(false);

			// Execute & verify — must pass async=true to trigger the cross-partition async check
			Exception ex = catchException(
				() -> myMergeHelper.callMergeOperation("Patient", params, true));
			assertThat(ex).isInstanceOf(NotImplementedOperationException.class);
		}
	}

	@Nested
	class PreviewMode {

		// Merge with preview=true: no resources are moved, no references are rewritten,
		// no IDs return 410, and all resources remain in their original partitions.
		@Test
		void testMerge_previewMode_noResourcesMovedOrUpdated() {
			IIdType encId = createEncounter(myPatientIdA, "enc-a");
			Integer encPartitionBefore = getPartitionId(encId);

			IIdType obsId = createObservation(myPatientIdA, encId, "obs-a1");
			Integer obsPartitionBefore = getPartitionId(obsId);

			// Execute with preview=true
			callMerge(new MergeTestParameters()
				.sourceResource(new Reference(myPatientIdA))
				.targetResource(new Reference(myPatientIdB))
				.deleteSource(false)
				.preview(true));

			// Verify: All resources remain in original partitions
			assertThat(getPartitionId(encId)).isEqualTo(encPartitionBefore);
			assertThat(getPartitionId(obsId)).isEqualTo(obsPartitionBefore);

			// References still point to Patient/A
			myMergeHelper.assertReferencesNotUpdated(
				List.of(encId, obsId), myPatientIdA, myPatientIdB);

			// No 410s
			assertThat(myClient.read().resource(Encounter.class).withId(encId).execute()).isNotNull();
			assertThat(myClient.read().resource(Observation.class).withId(obsId).execute()).isNotNull();
		}
	}

	@Nested
	class UndoMerge {

		// Full merge PatientA→PatientB (2 Obs + 1 Enc + 1 Group) followed by undo: all resources are
		// restored to their original IDs, partitions, and references. Verified by comparing
		// pre-merge snapshots to post-undo state. Tested with deleteSource=true and false.
		@ParameterizedTest
		@CsvSource({"false", "true"})
		void testUndoMerge_fullCrossPartitionReversal(boolean theDeleteSource) {
			Organization org = new Organization();
			org.setName("Test Org");
			IIdType orgId = myClient.create().resource(org).execute().getId().toUnqualifiedVersionless();

			IIdType encId = createEncounter(myPatientIdA, "enc-a");

			Observation obs1 = new Observation();
			obs1.getSubject().setReference(myPatientIdA.getValue());
			obs1.getEncounter().setReference(encId.getValue());
			obs1.addPerformer().setReference(orgId.getValue());
			obs1.addIdentifier(createTestIdentifier("obs-a1"));
			IIdType obs1Id = myClient.create().resource(obs1).execute().getId().toUnqualifiedVersionless();

			IIdType obs2Id = createObservation(myPatientIdA, null, "obs-a2");

			IIdType groupId = createGroup(myPatientIdA);

			// Save snapshots before merge
			IBaseResource encBefore = myClient.read().resource(Encounter.class).withId(encId).execute();
			IBaseResource obs1Before = myClient.read().resource(Observation.class).withId(obs1Id).execute();
			IBaseResource obs2Before = myClient.read().resource(Observation.class).withId(obs2Id).execute();

			// Merge
			callMerge(new MergeTestParameters()
				.sourceResource(new Reference(myPatientIdA))
				.targetResource(new Reference(myPatientIdB))
				.deleteSource(theDeleteSource));

			// Verify merge happened
			assertThat(searchBySubject(Observation.class, myPatientIdB.getValue())).hasSize(2);
			assertThat(searchBySubject(Observation.class, myPatientIdA.getValue())).isEmpty();

			// Undo merge
			Parameters undoParams = new Parameters();
			undoParams.addParameter().setName("source-resource").setValue(new Reference(myPatientIdA));
			undoParams.addParameter().setName("target-resource").setValue(new Reference(myPatientIdB));
			myMergeHelper.callUndoMergeOperation("Patient", undoParams);

			// Verify: Patient/A active with no links
			Patient patientAAfter = myClient.read().resource(Patient.class).withId(myPatientIdA).execute();
			assertThat(patientAAfter.getActive()).isTrue();
			assertThat(patientAAfter.getLink()).isEmpty();

			// Verify: Patient/B no links
			Patient patientBAfter = myClient.read().resource(Patient.class).withId(myPatientIdB).execute();
			assertThat(patientBAfter.getLink()).isEmpty();

			// Verify: 2 Obs back in PatientA's partition by subject=PatientA
			List<Observation> restoredObs = searchBySubject(Observation.class, myPatientIdA.getValue());
			assertThat(restoredObs).hasSize(2);
			Set<String> restoredObsIdentifiers = restoredObs.stream()
				.map(o -> o.getIdentifierFirstRep().getValue())
				.collect(Collectors.toSet());
			assertThat(restoredObsIdentifiers).containsExactlyInAnyOrder("obs-a1", "obs-a2");
			for (Observation o : restoredObs) {
				assertInSamePartition(o.getIdElement().toUnqualifiedVersionless(), myPatientIdA);
			}

			// Verify: Encounter back in PatientA's partition
			List<Encounter> restoredEnc = searchBySubject(Encounter.class, myPatientIdA.getValue());
			assertThat(restoredEnc).hasSize(1);
			assertThat(restoredEnc.get(0).getIdentifierFirstRep().getValue()).isEqualTo("enc-a");
			assertInSamePartition(restoredEnc.get(0).getIdElement().toUnqualifiedVersionless(), myPatientIdA);

			// Verify: Old IDs readable again
			assertThat(myClient.read().resource(Observation.class).withId(obs1Id).execute()).isNotNull();
			assertThat(myClient.read().resource(Observation.class).withId(obs2Id).execute()).isNotNull();
			assertThat(myClient.read().resource(Encounter.class).withId(encId).execute()).isNotNull();

			// Verify: Group references Patient/A
			Group groupAfter = myClient.read().resource(Group.class).withId(groupId).execute();
			assertThat(groupAfter.getMemberFirstRep().getEntity().getReference()).isEqualTo(myPatientIdA.getValue());

			// Verify: Restored resources match pre-merge snapshots
			myMergeHelper.assertResourcesAreEqualIgnoringVersionAndLastUpdated(
				encBefore, myClient.read().resource(Encounter.class).withId(encId).execute());
			myMergeHelper.assertResourcesAreEqualIgnoringVersionAndLastUpdated(
				obs1Before, myClient.read().resource(Observation.class).withId(obs1Id).execute());
			myMergeHelper.assertResourcesAreEqualIgnoringVersionAndLastUpdated(
				obs2Before, myClient.read().resource(Observation.class).withId(obs2Id).execute());

			// Verify: No observations for Patient/B
			assertThat(searchBySubject(Observation.class, myPatientIdB.getValue())).isEmpty();
			assertThat(searchBySubject(Encounter.class, myPatientIdB.getValue())).isEmpty();
		}
	}
}
