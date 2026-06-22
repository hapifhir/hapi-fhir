package ca.uhn.fhir.jpa.provider.merge;

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.dao.TransactionPrePartitionResponse;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.interceptor.PatientIdPartitionInterceptor;
import ca.uhn.fhir.jpa.merge.MergeOperationTestHelper;
import ca.uhn.fhir.jpa.merge.MergeTestParameters;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.jpa.replacereferences.ReplaceReferencesTestHelper;
import ca.uhn.fhir.jpa.searchparam.extractor.ISearchParamExtractor;
import ca.uhn.fhir.jpa.test.Batch2JobHelper;
import ca.uhn.fhir.merge.ResourceLinkServiceFactory;
import ca.uhn.fhir.parser.StrictErrorHandler;
import ca.uhn.fhir.rest.api.CacheControlDirective;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.gclient.ReferenceClientParam;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.NotImplementedOperationException;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Group;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.ListResource;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.OperationOutcome;
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
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import static ca.uhn.fhir.jpa.config.r4.FhirContextR4Config.DEFAULT_PRESERVE_VERSION_REFS_R4_AND_LATER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


/**
 * Integration tests for cross-partition Patient $merge in PATIENT_ID unnamed partition mode.
 *
 * <p>Tests the behavior of {@code CrossPartitionReplaceReferencesSvc} when merging
 * patients that reside in different partitions (as determined by {@code PatientIdPartitionInterceptor}).
 */
// Created by claude-opus-4-6
public class PatientIdModePatientMergeR4Test extends BaseResourceProviderR4Test {

	private static final Logger ourLog = LoggerFactory.getLogger(PatientIdModePatientMergeR4Test.class);

	// Prefix of the outcome message a fully-rolled-back merge reports, up to the failure cause; each test
	// appends its own cause (exception type and message).
	private static final String FULLY_ROLLED_BACK_MESSAGE_PREFIX =
		"Cross-partition merge failed and was fully rolled back; no resources remain in a merged state. "
			+ "Merge failure cause: ";

	@Autowired
	private Batch2JobHelper myBatch2JobHelper;
	@Autowired
	private ISearchParamExtractor mySearchParamExtractor;
	@Autowired
	private ResourceLinkServiceFactory myResourceLinkServiceFactory;
	@Autowired
	private HapiTransactionService myHapiTransactionService;
	@Autowired
	private CrossPartitionMergeRollbackService myCrossPartitionMergeRollbackService;
	private PatientIdPartitionInterceptor myPartitionInterceptor;
	private MergeOperationTestHelper myMergeHelper;
	private ReplaceReferencesTestHelper myReplaceReferencesHelper;
	private IIdType myPatientIdSrc;
	private IIdType myPatientIdTgt;
	private List<Identifier> mySourceIdentifiers;
	private List<Identifier> myTargetIdentifiers;

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

		myFhirContext.setParserErrorHandler(new StrictErrorHandler());
		myFhirContext.getParserOptions().setDontStripVersionsFromReferencesAtPaths("Provenance.target");

		myMergeHelper = new MergeOperationTestHelper(
			myClient, myBatch2JobHelper, myFhirContext, myResourceLinkServiceFactory);
		myReplaceReferencesHelper = new ReplaceReferencesTestHelper(myFhirContext, myDaoRegistry);

		mySourceIdentifiers = List.of(createTestIdentifier("patient-src"));
		myTargetIdentifiers = List.of(createTestIdentifier("patient-tgt"));

		myPatientIdSrc = createPatient("Patient/src", mySourceIdentifiers).getIdElement().toUnqualifiedVersionless();
		myPatientIdTgt = createPatient("Patient/tgt", myTargetIdentifiers).getIdElement().toUnqualifiedVersionless();
		assertInDifferentPartitions(myPatientIdSrc, myPatientIdTgt);
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

	/**
	 * Verifies that resources with the given identifiers were moved to the target patient's partition
	 * and that their original IDs are deleted (410 Gone). Returns the moved resources.
	 */
	private <T extends IBaseResource> List<T> assertResourcesMovedToTarget(
		Class<T> theResourceClass,
		Map<String, IIdType> theExpectedIdentifierToOldId,
		IIdType theTargetPatient,
		Function<T, List<Identifier>> theIdentifierExtractor) {

		// Search for all resources referencing the target patient
		List<T> found = searchBySubject(theResourceClass, theTargetPatient.getValue());

		// Index found resources by identifier for lookup
		Map<String, T> foundByIdentifier = new HashMap<>();
		for (T resource : found) {
			String identifier = theIdentifierExtractor.apply(resource).get(0).getValue();
			foundByIdentifier.put(identifier, resource);
		}

		// For each expected identifier: verify the resource exists, is in the correct partition, and old ID is gone
		List<T> newResources = new ArrayList<>();
		for (Map.Entry<String, IIdType> identifierToOldId : theExpectedIdentifierToOldId.entrySet()) {
			assertThat(foundByIdentifier).containsKey(identifierToOldId.getKey());
			T resource = foundByIdentifier.get(identifierToOldId.getKey());
			assertInSamePartition(resource.getIdElement().toUnqualifiedVersionless(), theTargetPatient);
			assertResourceDeleted(identifierToOldId.getValue());
			newResources.add(resource);
		}
		return newResources;
	}

	private <T extends IBaseResource> T assertSingleResourceMovedToTarget(
		Class<T> theResourceClass,
		Map<String, IIdType> theExpectedIdentifierToOldId,
		IIdType theTargetPatient,
		Function<T, List<Identifier>> theIdentifierExtractor) {

		return assertResourcesMovedToTarget(
			theResourceClass, theExpectedIdentifierToOldId, theTargetPatient, theIdentifierExtractor).get(0);
	}

	private Patient createPatient(String theId, List<Identifier> theIdentifiers) {
		Patient patient = new Patient();
		patient.setId(theId);
		patient.setIdentifier(theIdentifiers);
		myClient.update().resource(patient).execute();
		return patient;
	}

	private IIdType createObservation(IIdType theSubject, IIdType theEncounter, IIdType thePerformer, String theIdentifierValue) {
		Observation obs = new Observation();
		obs.getSubject().setReference(theSubject.getValue());
		if (theEncounter != null) {
			obs.getEncounter().setReference(theEncounter.getValue());
		}
		if (thePerformer != null) {
			obs.addPerformer().setReference(thePerformer.getValue());
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

	private void assertResourceDeleted(IIdType theId) {
		var readRequest = myClient.read().resource(theId.getResourceType()).withId(theId);
		assertThatThrownBy(readRequest::execute)
			.isInstanceOf(ResourceGoneException.class);
	}

	private <T extends IBaseResource> T readResource(Class<T> theType, IIdType theId) {
		return myClient.read().resource(theType).withId(theId).execute();
	}

	private Identifier createTestIdentifier(String theValue) {
		return new Identifier().setSystem("http://test").setValue(theValue);
	}

	// ================================================
	// TEST SCENARIOS
	// ================================================

	@Nested
	class CompartmentResourceMovement {

		// Observation(subject=PatientSrc). Merges PatientSrc→PatientTgt: Obs moves to PatientTgt's partition with a new ID,
		// old ID returns 410. Tested with both deleteSource=true and deleteSource=false.
		@ParameterizedTest
		@CsvSource({"false", "true"})
		void testMerge_singleObservationMoves(boolean theDeleteSource) {
			// Setup
			IIdType obsId = createObservation(myPatientIdSrc, null, null, "obs-src");

			// Execute
			MergeTestParameters mergeParams = new MergeTestParameters()
				.sourceResource(new Reference(myPatientIdSrc))
				.targetResource(new Reference(myPatientIdTgt))
				.deleteSource(theDeleteSource);
			Parameters result = callMerge(mergeParams);
			myMergeHelper.validateSyncMergeOutcome(result, mergeParams.asParametersResource(), myPatientIdTgt);

			// Verify: Obs moved to PatientTgt with same identifier
			Observation movedObs = assertSingleResourceMovedToTarget(
				Observation.class, Map.of("obs-src", obsId), myPatientIdTgt, Observation::getIdentifier);
			IIdType newObsId = movedObs.getIdElement().toUnqualifiedVersionless();

			// Validate resources after merge
			IIdType expectedVersionedSourceId = myPatientIdSrc.withVersion("2");
			IIdType expectedVersionedTargetId = myPatientIdTgt.withVersion("2");
			List<IIdType> idsExpectedToReferenceTarget = List.of(newObsId);
			Set<String> expectedProvenanceTargets = Set.of(
				expectedVersionedSourceId.toString(),
				expectedVersionedTargetId.toString(),
				newObsId.withVersion("1").toString(),
				obsId.withVersion("2").toString());
			List<Identifier> expectedTargetIdentifiers = myMergeHelper.computeIdentifiersExpectedOnTargetAfterMerge(
				myTargetIdentifiers, mySourceIdentifiers, null);
			myMergeHelper.validateResourcesAfterCrossPartitionMerge(mergeParams,
				expectedVersionedSourceId, expectedVersionedTargetId,
				idsExpectedToReferenceTarget, expectedProvenanceTargets,
				expectedTargetIdentifiers, null);
		}

		// Encounter(subject=PatientSrc) + Observation(subject=PatientSrc, encounter=Enc). Merges PatientSrc→PatientTgt: both move
		// to PatientTgt's partition. The Obs's encounter reference is rewritten to the Encounter's new ID.
		// Tested with both deleteSource=true and deleteSource=false.
		@ParameterizedTest
		@CsvSource({"false", "true"})
		void testMerge_intraCompartmentCrossReferenceUpdated(boolean theDeleteSource) {
			// Setup
			IIdType encId = createEncounter(myPatientIdSrc, "enc-src");

			IIdType obsId = createObservation(myPatientIdSrc, encId, null, "obs-src");

			// Execute
			MergeTestParameters mergeParams = new MergeTestParameters()
				.sourceResource(new Reference(myPatientIdSrc))
				.targetResource(new Reference(myPatientIdTgt))
				.deleteSource(theDeleteSource);
			Parameters result = callMerge(mergeParams);
			myMergeHelper.validateSyncMergeOutcome(result, mergeParams.asParametersResource(), myPatientIdTgt);

			// Verify: New Encounter in PatientTgt's partition with identifier=enc-src
			Encounter newEnc = assertSingleResourceMovedToTarget(
				Encounter.class, Map.of("enc-src", encId), myPatientIdTgt, Encounter::getIdentifier);
			IIdType newEncId = newEnc.getIdElement().toUnqualifiedVersionless();

			// New Obs in PatientTgt's partition with identifier=obs-src and encounter=new Encounter ID
			Observation movedObs = assertSingleResourceMovedToTarget(
				Observation.class, Map.of("obs-src", obsId), myPatientIdTgt, Observation::getIdentifier);
			assertThat(movedObs.getEncounter().getReferenceElement().getValue())
				.isEqualTo(newEncId.getValue());
			IIdType newObsId = movedObs.getIdElement().toUnqualifiedVersionless();

			// Validate resources after merge
			IIdType expectedVersionedSourceId = myPatientIdSrc.withVersion("2");
			IIdType expectedVersionedTargetId = myPatientIdTgt.withVersion("2");
			List<IIdType> idsExpectedToReferenceTarget = List.of(newObsId, newEncId);
			Set<String> expectedProvenanceTargets = Set.of(
				expectedVersionedSourceId.toString(),
				expectedVersionedTargetId.toString(),
				newObsId.withVersion("1").toString(),
				newEncId.withVersion("1").toString(),
				obsId.withVersion("2").toString(),
				encId.withVersion("2").toString());
			List<Identifier> expectedTargetIdentifiers = myMergeHelper.computeIdentifiersExpectedOnTargetAfterMerge(
				myTargetIdentifiers, mySourceIdentifiers, null);
			myMergeHelper.validateResourcesAfterCrossPartitionMerge(mergeParams,
				expectedVersionedSourceId, expectedVersionedTargetId,
				idsExpectedToReferenceTarget, expectedProvenanceTargets,
				expectedTargetIdentifiers, null);
		}

		// Observation(subject=PatientSrc) that also carries an identifier-only performer reference (no literal .reference,
		// only .identifier). Merges PatientSrc→PatientTgt: the merge must not NPE on the reference-less performer, the Obs
		// moves to PatientTgt's partition, and the identifier-only performer reference is preserved untouched.
		@Test
		void testMerge_resourceWithIdentifierOnlyReference_succeedsAndPreservesReference() {
			// Setup: Observation with subject=PatientSrc and a performer reference that has only an identifier (no .reference)
			Observation obs = new Observation();
			obs.getSubject().setReference(myPatientIdSrc.getValue());
			obs.addIdentifier(createTestIdentifier("obs-src"));
			Identifier performerIdentifier = createTestIdentifier("performer-id-only");
			obs.addPerformer().setIdentifier(performerIdentifier);
			IIdType obsId = myClient.create().resource(obs).execute().getId().toUnqualifiedVersionless();

			// Execute
			MergeTestParameters mergeParams = new MergeTestParameters()
				.sourceResource(new Reference(myPatientIdSrc))
				.targetResource(new Reference(myPatientIdTgt))
				.deleteSource(false);
			Parameters result = callMerge(mergeParams);
			myMergeHelper.validateSyncMergeOutcome(result, mergeParams.asParametersResource(), myPatientIdTgt);

			// Verify: Obs moved to PatientTgt and the identifier-only performer reference is preserved untouched
			Observation movedObs = assertSingleResourceMovedToTarget(
				Observation.class, Map.of("obs-src", obsId), myPatientIdTgt, Observation::getIdentifier);
			Reference movedPerformer = movedObs.getPerformerFirstRep();
			assertThat(movedPerformer.hasReference()).isFalse();
			assertThat(movedPerformer.getIdentifier().getSystem()).isEqualTo(performerIdentifier.getSystem());
			assertThat(movedPerformer.getIdentifier().getValue()).isEqualTo(performerIdentifier.getValue());
		}
	}

	@Nested
	class ReferencingResourcesOutsideCompartment {

		// Group(member=PatientSrc) in default partition. Merges PatientSrc→PatientTgt: Group stays in the default partition
		// with its original ID, but its member reference is rewritten from PatientSrc to PatientTgt.
		// Tested with both deleteSource=true and deleteSource=false.
		@ParameterizedTest
		@CsvSource({"false", "true"})
		void testMerge_resourceInDefaultPartitionReferencingSourcePatient_staysInPlaceRefUpdated(boolean theDeleteSource) {
			// Setup
			IIdType groupId = createGroup(myPatientIdSrc);
			Integer groupPartitionBefore = getPartitionId(groupId);

			// Execute
			MergeTestParameters mergeParams = new MergeTestParameters()
				.sourceResource(new Reference(myPatientIdSrc))
				.targetResource(new Reference(myPatientIdTgt))
				.deleteSource(theDeleteSource);
			Parameters result = callMerge(mergeParams);
			myMergeHelper.validateSyncMergeOutcome(result, mergeParams.asParametersResource(), myPatientIdTgt);

			// Verify: Group keeps original ID and partition, reference updated
			Group updatedGroup = readResource(Group.class, groupId);
			assertThat(updatedGroup.getMemberFirstRep().getEntity().getReference()).isEqualTo(myPatientIdTgt.getValue());
			assertThat(getPartitionId(groupId)).isEqualTo(groupPartitionBefore);

			// Validate resources after merge
			IIdType expectedVersionedSourceId = myPatientIdSrc.withVersion("2");
			IIdType expectedVersionedTargetId = myPatientIdTgt.withVersion("2");
			List<IIdType> idsExpectedToReferenceTarget = List.of(groupId);
			Set<String> expectedProvenanceTargets = Set.of(
				expectedVersionedSourceId.toString(),
				expectedVersionedTargetId.toString(),
				groupId.withVersion("2").toString());
			List<Identifier> expectedTargetIdentifiers = myMergeHelper.computeIdentifiersExpectedOnTargetAfterMerge(
				myTargetIdentifiers, mySourceIdentifiers, null);
			myMergeHelper.validateResourcesAfterCrossPartitionMerge(mergeParams,
				expectedVersionedSourceId, expectedVersionedTargetId,
				idsExpectedToReferenceTarget, expectedProvenanceTargets,
				expectedTargetIdentifiers, null);
		}

		// Enc(subject=PatientSrc) + Obs(subject=PatientC, encounter=Enc). Merges PatientSrc→PatientTgt: Enc moves to PatientTgt's partition,
		// Obs stays in PatientC's partition but its encounter reference is rewritten to Enc's new ID.
		// Not a realistic clinical scenario — an Observation rarely, if at all, references an Encounter from
		// a different patient's compartment — but tests the edge case where a resource that
		// doesn't move has its reference to a moved resource updated.
		@Test
		void testMerge_anotherPatientsResourceReferencingMovedResource() {
			IIdType patientIdC = createPatient("Patient/C", List.of(createTestIdentifier("patient-c"))).getIdElement().toUnqualifiedVersionless();
			assertInDifferentPartitions(myPatientIdTgt, patientIdC);

			IIdType encId = createEncounter(myPatientIdSrc, "enc-src");

			IIdType obsId = createObservation(patientIdC, encId, null, "obs-c");

			// Execute: merge PatientSrc→PatientTgt
			MergeTestParameters mergeParams = new MergeTestParameters()
				.sourceResource(new Reference(myPatientIdSrc))
				.targetResource(new Reference(myPatientIdTgt))
				.deleteSource(false);
			Parameters result = callMerge(mergeParams);
			myMergeHelper.validateSyncMergeOutcome(result, mergeParams.asParametersResource(), myPatientIdTgt);

			// Verify: Encounter moved to PatientTgt's partition
			Encounter newEnc = assertSingleResourceMovedToTarget(
				Encounter.class, Map.of("enc-src", encId), myPatientIdTgt, Encounter::getIdentifier);
			IIdType newEncId = newEnc.getIdElement().toUnqualifiedVersionless();

			// Verify: Obs stays in PatientC's partition with unchanged subject, encounter ref rewritten
			Observation updatedObs = readResource(Observation.class, obsId);
			assertThat(updatedObs.getSubject().getReference()).isEqualTo(patientIdC.getValue());
			assertThat(updatedObs.getIdentifierFirstRep().getValue()).isEqualTo("obs-c");
			assertInSamePartition(obsId, patientIdC);
			assertThat(updatedObs.getEncounter().getReferenceElement().getValue())
				.isEqualTo(newEncId.getValue());

			// Validate resources after merge
			IIdType expectedVersionedSourceId = myPatientIdSrc.withVersion("2");
			IIdType expectedVersionedTargetId = myPatientIdTgt.withVersion("2");
			List<IIdType> idsExpectedToReferenceTarget = List.of(newEncId);
			Set<String> expectedProvenanceTargets = Set.of(
				expectedVersionedSourceId.toString(),
				expectedVersionedTargetId.toString(),
				newEncId.withVersion("1").toString(),
				encId.withVersion("2").toString(),
				obsId.withVersion("2").toString());
			List<Identifier> expectedTargetIdentifiers = myMergeHelper.computeIdentifiersExpectedOnTargetAfterMerge(
				myTargetIdentifiers, mySourceIdentifiers, null);
			myMergeHelper.validateResourcesAfterCrossPartitionMerge(mergeParams,
				expectedVersionedSourceId, expectedVersionedTargetId,
				idsExpectedToReferenceTarget, expectedProvenanceTargets,
				expectedTargetIdentifiers, null);
		}

		// List(entries=[Enc-Src, Enc-Tgt]) in default partition. Merges PatientSrc→PatientTgt: Enc-Src moves and gets a new ID,
		// List stays in default partition but its entry reference to Enc-Src is rewritten to the new ID.
		// The entry referencing the unrelated Enc-Tgt is unchanged.
		@Test
		void testMerge_resourceInDefaultPartitionReferencingMovedResource_staysInPlaceRefUpdated() {
			// Setup
			// Moved Encounter (subject=src)
			IIdType movedEncId = createEncounter(myPatientIdSrc, "enc-src");

			// Unrelated Encounter (subject=tgt) — same type but not moved
			Encounter unrelatedEnc = new Encounter();
			unrelatedEnc.getSubject().setReference(myPatientIdTgt.getValue());
			unrelatedEnc.setStatus(Encounter.EncounterStatus.PLANNED);
			unrelatedEnc.addIdentifier(createTestIdentifier("enc-tgt"));
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
				.sourceResource(new Reference(myPatientIdSrc))
				.targetResource(new Reference(myPatientIdTgt))
				.deleteSource(false);
			Parameters result = callMerge(mergeParams);
			myMergeHelper.validateSyncMergeOutcome(result, mergeParams.asParametersResource(), myPatientIdTgt);

			// Verify: List keeps original ID and partition
			ListResource updatedList = readResource(ListResource.class, listId);
			assertThat(getPartitionId(listId)).isEqualTo(listPartitionBefore);

			// Verify: moved Encounter in PatientTgt's partition, old ID deleted
			Encounter newMovedEnc = assertSingleResourceMovedToTarget(
				Encounter.class, Map.of("enc-src", movedEncId), myPatientIdTgt, Encounter::getIdentifier);
			IIdType newMovedEncId = newMovedEnc.getIdElement().toUnqualifiedVersionless();

			assertThat(updatedList.getEntry().get(0).getItem().getReferenceElement().getValue())
				.isEqualTo(newMovedEncId.getValue());

			// entry[1] → unrelated Enc ID unchanged
			assertThat(updatedList.getEntry().get(1).getItem().getReferenceElement().getValue())
				.isEqualTo(unrelatedEncId.getValue());

			// Unrelated Encounter still readable
			Encounter readUnrelated = readResource(Encounter.class, unrelatedEncId);
			assertThat(readUnrelated.getIdentifierFirstRep().getValue()).isEqualTo("enc-tgt");

			// Validate resources after merge
			IIdType expectedVersionedSourceId = myPatientIdSrc.withVersion("2");
			IIdType expectedVersionedTargetId = myPatientIdTgt.withVersion("2");
			List<IIdType> idsExpectedToReferenceTarget = List.of(newMovedEncId);
			Set<String> expectedProvenanceTargets = Set.of(
				expectedVersionedSourceId.toString(),
				expectedVersionedTargetId.toString(),
				newMovedEncId.withVersion("1").toString(),
				movedEncId.withVersion("2").toString(),
				listId.withVersion("2").toString());
			List<Identifier> expectedTargetIdentifiers = myMergeHelper.computeIdentifiersExpectedOnTargetAfterMerge(
				myTargetIdentifiers, mySourceIdentifiers, null);
			myMergeHelper.validateResourcesAfterCrossPartitionMerge(mergeParams,
				expectedVersionedSourceId, expectedVersionedTargetId,
				idsExpectedToReferenceTarget, expectedProvenanceTargets,
				expectedTargetIdentifiers, null);
		}

		// Obs(subject=PatientSrc, performer=Org). Merges PatientSrc→PatientTgt: Obs moves to PatientTgt's partition, subject is
		// rewritten to PatientTgt, but the performer reference to Organization is left unchanged.
		@Test
		void testMerge_multipleReferences_onlySourcePatientRefReplaced() {
			// Setup
			Organization org = new Organization();
			org.setName("Test Org");
			IIdType orgId = myClient.create().resource(org).execute().getId().toUnqualifiedVersionless();

			IIdType obsId = createObservation(myPatientIdSrc, null, orgId, "obs-src");

			// Execute
			MergeTestParameters mergeParams = new MergeTestParameters()
				.sourceResource(new Reference(myPatientIdSrc))
				.targetResource(new Reference(myPatientIdTgt))
				.deleteSource(false);
			Parameters result = callMerge(mergeParams);
			myMergeHelper.validateSyncMergeOutcome(result, mergeParams.asParametersResource(), myPatientIdTgt);

			// Verify
			Observation movedObs = assertSingleResourceMovedToTarget(
				Observation.class, Map.of("obs-src", obsId), myPatientIdTgt, Observation::getIdentifier);
			// src reference updated
			assertThat(movedObs.getSubject().getReference()).isEqualTo(myPatientIdTgt.getValue());
			// organization reference remains same
			assertThat(movedObs.getPerformerFirstRep().getReferenceElement().getValue())
				.isEqualTo(orgId.getValue());
			IIdType newObsId = movedObs.getIdElement().toUnqualifiedVersionless();

			// Validate resources after merge
			IIdType expectedVersionedSourceId = myPatientIdSrc.withVersion("2");
			IIdType expectedVersionedTargetId = myPatientIdTgt.withVersion("2");
			List<IIdType> idsExpectedToReferenceTarget = List.of(newObsId);
			Set<String> expectedProvenanceTargets = Set.of(
				expectedVersionedSourceId.toString(),
				expectedVersionedTargetId.toString(),
				newObsId.withVersion("1").toString(),
				obsId.withVersion("2").toString());
			List<Identifier> expectedTargetIdentifiers = myMergeHelper.computeIdentifiersExpectedOnTargetAfterMerge(
				myTargetIdentifiers, mySourceIdentifiers, null);
			myMergeHelper.validateResourcesAfterCrossPartitionMerge(mergeParams,
				expectedVersionedSourceId, expectedVersionedTargetId,
				idsExpectedToReferenceTarget, expectedProvenanceTargets,
				expectedTargetIdentifiers, null);
		}
	}

	@Nested
	class VersionedReferences {

		// Provenance(target=PatientSrc/_history/1). Merges PatientSrc→PatientTgt: Provenance.target is in
		// setDontStripVersionsFromReferencesAtPaths, so the parser preserves the version
		// and the merge does NOT rewrite it. Provenance stays in its original partition.
		@Test
		void testMerge_versionedReferencePreservedByParser_notRewritten() {
			// Create Provenance with versioned target reference to Patient/src
			Provenance provenance = new Provenance();
			provenance.addTarget().setReference(myPatientIdSrc.getValue() + "/_history/1");
			provenance.getActivity().addCoding()
				.setSystem("http://terminology.hl7.org/CodeSystem/v3-DocumentCompletion")
				.setCode("LA");
			provenance.setRecorded(new Date());
			IIdType provenanceId = myClient.create().resource(provenance).execute().getId().toUnqualifiedVersionless();
			Integer provenancePartitionBefore = getPartitionId(provenanceId);

			// Execute
			MergeTestParameters mergeParams = new MergeTestParameters()
				.sourceResource(new Reference(myPatientIdSrc))
				.targetResource(new Reference(myPatientIdTgt))
				.deleteSource(false);
			Parameters result = callMerge(mergeParams);
			myMergeHelper.validateSyncMergeOutcome(result, mergeParams.asParametersResource(), myPatientIdTgt);

			// Verify: Provenance's versioned reference is NOT rewritten
			Provenance readProvenance = readResource(Provenance.class, provenanceId);
			assertThat(readProvenance.getTargetFirstRep().getReference())
				.isEqualTo(myPatientIdSrc.getValue() + "/_history/1");
			// provenance Not updated — version stays at 1
			assertThat(readProvenance.getIdElement().getVersionIdPart()).isEqualTo("1");
			// Stays in original partition
			assertThat(getPartitionId(provenanceId)).isEqualTo(provenancePartitionBefore);

			// Validate resources after merge — no resources reference Patient/src (Provenance.target is versioned, not rewritten)
			IIdType expectedVersionedSourceId = myPatientIdSrc.withVersion("2");
			IIdType expectedVersionedTargetId = myPatientIdTgt.withVersion("2");
			List<IIdType> idsExpectedToReferenceTarget = List.of();
			Set<String> expectedProvenanceTargets = Set.of(
				expectedVersionedSourceId.toString(), expectedVersionedTargetId.toString());
			List<Identifier> expectedTargetIdentifiers = myMergeHelper.computeIdentifiersExpectedOnTargetAfterMerge(
				myTargetIdentifiers, mySourceIdentifiers, null);
			myMergeHelper.validateResourcesAfterCrossPartitionMerge(mergeParams,
				expectedVersionedSourceId, expectedVersionedTargetId,
				idsExpectedToReferenceTarget, expectedProvenanceTargets,
				expectedTargetIdentifiers, null);
		}

		// Obs(subject=PatientSrc/_history/1). Merges PatientSrc→PatientTgt: Observation.subject is NOT in
		// setDontStripVersionsFromReferencesAtPaths, so the parser strips the version
		// and the merge rewrites subject to PatientTgt. Obs moves to PatientTgt's partition.
		@Test
		void testMerge_versionedReferenceStrippedByParser_isRewritten() {
			// Create Observation with versioned subject reference
			// Since Observation.subject is NOT in setDontStripVersionsFromReferencesAtPaths,
			// the parser will strip the version when reading back, so merge rewrites it.
			Observation obs = new Observation();
			obs.getSubject().setReference(myPatientIdSrc.getValue() + "/_history/1");
			obs.addIdentifier(createTestIdentifier("obs-src"));
			IIdType obsId = myClient.create().resource(obs).execute().getId().toUnqualifiedVersionless();

			// Execute
			MergeTestParameters mergeParams = new MergeTestParameters()
				.sourceResource(new Reference(myPatientIdSrc))
				.targetResource(new Reference(myPatientIdTgt))
				.deleteSource(false);
			Parameters result = callMerge(mergeParams);
			myMergeHelper.validateSyncMergeOutcome(result, mergeParams.asParametersResource(), myPatientIdTgt);

			// Verify: Observation moved to PatientTgt's partition with subject rewritten to PatientTgt
			Observation movedObs = assertSingleResourceMovedToTarget(
				Observation.class, Map.of("obs-src", obsId), myPatientIdTgt, Observation::getIdentifier);
			IIdType newObsId = movedObs.getIdElement().toUnqualifiedVersionless();

			// Validate resources after merge
			IIdType expectedVersionedSourceId = myPatientIdSrc.withVersion("2");
			IIdType expectedVersionedTargetId = myPatientIdTgt.withVersion("2");
			List<IIdType> idsExpectedToReferenceTarget = List.of(newObsId);
			Set<String> expectedProvenanceTargets = Set.of(
				expectedVersionedSourceId.toString(),
				expectedVersionedTargetId.toString(),
				newObsId.withVersion("1").toString(),
				obsId.withVersion("2").toString());
			List<Identifier> expectedTargetIdentifiers = myMergeHelper.computeIdentifiersExpectedOnTargetAfterMerge(
				myTargetIdentifiers, mySourceIdentifiers, null);
			myMergeHelper.validateResourcesAfterCrossPartitionMerge(mergeParams,
				expectedVersionedSourceId, expectedVersionedTargetId,
				idsExpectedToReferenceTarget, expectedProvenanceTargets,
				expectedTargetIdentifiers, null);
		}
	}

	@Nested
	class NoReferencingResources {

		// No compartment or referencing resources exist — just two patients.
		// Merges PatientSrc→PatientTgt: succeeds with no resource movement.
		// Tested with both deleteSource=true and deleteSource=false.
		@ParameterizedTest
		@CsvSource({"false", "true"})
		void testMerge_noReferencingResources_succeeds(boolean theDeleteSource) {
			// Execute
			MergeTestParameters mergeParams = new MergeTestParameters()
				.sourceResource(new Reference(myPatientIdSrc))
				.targetResource(new Reference(myPatientIdTgt))
				.deleteSource(theDeleteSource);
			Parameters result = callMerge(mergeParams);

			// Verify: merge succeeds
			myMergeHelper.validateSyncMergeOutcome(result, mergeParams.asParametersResource(), myPatientIdTgt);
			IIdType expectedVersionedSourceId = myPatientIdSrc.withVersion("2");
			IIdType expectedVersionedTargetId = myPatientIdTgt.withVersion("2");
			List<IIdType> idsExpectedToReferenceTarget = List.of();
			Set<String> expectedProvenanceTargets = Set.of(
				expectedVersionedSourceId.toString(), expectedVersionedTargetId.toString());
			List<Identifier> expectedTargetIdentifiers = myMergeHelper.computeIdentifiersExpectedOnTargetAfterMerge(
				myTargetIdentifiers, mySourceIdentifiers, null);
			myMergeHelper.validateResourcesAfterCrossPartitionMerge(mergeParams,
				expectedVersionedSourceId, expectedVersionedTargetId,
				idsExpectedToReferenceTarget, expectedProvenanceTargets,
				expectedTargetIdentifiers, null);
		}

	}

	@Nested
	class ErrorCases {

		// Source patient has 3 Observations but resource-limit is set to 2.
		// Merge fails with PreconditionFailedException before any resources are moved.
		@Test
		void testMerge_resourceLimitExceeded_throwsPreconditionFailed() {
			for (int i = 0; i < 3; i++) {
				createObservation(myPatientIdSrc, null, null, null);
			}

			// Execute & verify
			MergeTestParameters params = new MergeTestParameters()
				.sourceResource(new Reference(myPatientIdSrc))
				.targetResource(new Reference(myPatientIdTgt))
				.deleteSource(false)
				.resourceLimit(2);

			myMergeHelper.callMergeAndValidateException(
				"Patient", params, PreconditionFailedException.class, "exceeds the resource-limit");
		}

		// Cross-partition merge with async=true is not supported and throws NotImplementedOperationException.
		@Test
		void testMerge_asyncRequested_throwsNotImplemented() {
			MergeTestParameters params = new MergeTestParameters()
				.sourceResource(new Reference(myPatientIdSrc))
				.targetResource(new Reference(myPatientIdTgt))
				.deleteSource(false);

			// Execute & verify — must pass async=true to trigger the cross-partition async check
			assertThatThrownBy(() -> myMergeHelper.callMergeOperation("Patient", params, true))
				.isInstanceOf(NotImplementedOperationException.class)
				.extracting(e -> ((NotImplementedOperationException) e).getResponseBody())
				.asString()
				.contains("Cross-partition merge does not support asynchronous processing.");
		}
	}

	@Nested
	class PreviewMode {

		// Merge with preview=true: no resources are moved, no references are rewritten,
		// no IDs return 410, and all resources remain in their original partitions.
		@Test
		void testMerge_previewMode_noResourcesMovedOrUpdated() {
			IIdType encId = createEncounter(myPatientIdSrc, "enc-src");
			Integer encPartitionBefore = getPartitionId(encId);

			IIdType obsId = createObservation(myPatientIdSrc, encId, null, "obs-src1");
			Integer obsPartitionBefore = getPartitionId(obsId);

			// Execute with preview=true
			callMerge(new MergeTestParameters()
				.sourceResource(new Reference(myPatientIdSrc))
				.targetResource(new Reference(myPatientIdTgt))
				.deleteSource(false)
				.preview(true));

			// Verify: All resources remain in original partitions
			assertThat(getPartitionId(encId)).isEqualTo(encPartitionBefore);
			assertThat(getPartitionId(obsId)).isEqualTo(obsPartitionBefore);

			// References still point to Patient/src
			myMergeHelper.assertReferencesNotUpdated(
				List.of(encId, obsId), myPatientIdSrc, myPatientIdTgt);

			// Not deleted, still at version 1
			assertThat(readResource(Encounter.class, encId)
				.getIdElement().getVersionIdPart()).isEqualTo("1");
			assertThat(readResource(Observation.class, obsId)
				.getIdElement().getVersionIdPart()).isEqualTo("1");
		}
	}

	@Nested
	class UndoMerge {

		// Full merge PatientSrc→PatientTgt (2 Obs + 1 Enc + 1 Group) followed by undo: all resources are
		// restored to their original IDs, partitions, and references. Verified by comparing
		// pre-merge snapshots to post-undo state. Tested with deleteSource=true and false.
		@ParameterizedTest
		@CsvSource({"false", "true"})
		void testUndoMerge_fullCrossPartitionReversal(boolean theDeleteSource) {
			Organization org = new Organization();
			org.setName("Test Org");
			IIdType orgId = myClient.create().resource(org).execute().getId().toUnqualifiedVersionless();

			IIdType encId = createEncounter(myPatientIdSrc, "enc-src");

			IIdType obs1Id = createObservation(myPatientIdSrc, encId, orgId, "obs-src1");

			IIdType obs2Id = createObservation(myPatientIdSrc, null, null, "obs-src2");

			IIdType groupId = createGroup(myPatientIdSrc);

			// Save snapshots before merge
			IBaseResource patientSrcBefore = readResource(Patient.class, myPatientIdSrc);
			IBaseResource patientTgtBefore = readResource(Patient.class, myPatientIdTgt);
			IBaseResource encBefore = readResource(Encounter.class, encId);
			IBaseResource obs1Before = readResource(Observation.class, obs1Id);
			IBaseResource obs2Before = readResource(Observation.class, obs2Id);
			IBaseResource groupBefore = readResource(Group.class, groupId);

			// Merge
			callMerge(new MergeTestParameters()
				.sourceResource(new Reference(myPatientIdSrc))
				.targetResource(new Reference(myPatientIdTgt))
				.deleteSource(theDeleteSource));

			// Verify merge happened and capture move-created IDs (to verify deletion after undo)
			List<Observation> movedObs = assertResourcesMovedToTarget(
				Observation.class, Map.of("obs-src1", obs1Id, "obs-src2", obs2Id),
				myPatientIdTgt, Observation::getIdentifier);
			List<IdType> movedObsIds = movedObs.stream()
				.map(r -> r.getIdElement().toUnqualifiedVersionless()).toList();

			Encounter movedEnc = assertSingleResourceMovedToTarget(
				Encounter.class, Map.of("enc-src", encId), myPatientIdTgt, Encounter::getIdentifier);
			IIdType movedEncId = movedEnc.getIdElement().toUnqualifiedVersionless();

			// Undo merge
			Parameters undoParams = new Parameters();
			undoParams.addParameter().setName("source-resource").setValue(new Reference(myPatientIdSrc));
			undoParams.addParameter().setName("target-resource").setValue(new Reference(myPatientIdTgt));
			myMergeHelper.callUndoMergeOperation("Patient", undoParams);

			// Verify: Obs and Enc back in PatientSrc's partition
			List<Observation> restoredObs = searchBySubject(Observation.class, myPatientIdSrc.getValue());
			assertThat(restoredObs).hasSize(2);
			for (Observation o : restoredObs) {
				assertInSamePartition(o.getIdElement().toUnqualifiedVersionless(), myPatientIdSrc);
			}

			List<Encounter> restoredEnc = searchBySubject(Encounter.class, myPatientIdSrc.getValue());
			assertThat(restoredEnc).hasSize(1);
			assertInSamePartition(restoredEnc.get(0).getIdElement().toUnqualifiedVersionless(), myPatientIdSrc);

			// Verify: All resources match pre-merge snapshots
			myMergeHelper.assertResourcesAreEqualIgnoringVersionAndLastUpdated(patientSrcBefore, readResource(Patient.class, myPatientIdSrc));
			myMergeHelper.assertResourcesAreEqualIgnoringVersionAndLastUpdated(patientTgtBefore, readResource(Patient.class, myPatientIdTgt));
			myMergeHelper.assertResourcesAreEqualIgnoringVersionAndLastUpdated(encBefore, readResource(Encounter.class, encId));
			myMergeHelper.assertResourcesAreEqualIgnoringVersionAndLastUpdated(obs1Before, readResource(Observation.class, obs1Id));
			myMergeHelper.assertResourcesAreEqualIgnoringVersionAndLastUpdated(obs2Before, readResource(Observation.class, obs2Id));
			myMergeHelper.assertResourcesAreEqualIgnoringVersionAndLastUpdated(groupBefore, readResource(Group.class, groupId));

			// Verify: Move-created copies are deleted
			for (IIdType movedObsId : movedObsIds) {
				assertResourceDeleted(movedObsId);
			}
			assertResourceDeleted(movedEncId);

			// Verify: No resources for Patient/tgt
			assertThat(searchBySubject(Observation.class, myPatientIdTgt.getValue())).isEmpty();
			assertThat(searchBySubject(Encounter.class, myPatientIdTgt.getValue())).isEmpty();
		}

		// A one-way reference where the referrer has a LOWER pid than its target. The Encounter is created
		// first (lower pid), then the Observation (higher pid), then the Encounter is updated to reference the
		// Observation (Encounter.reasonReference). The restore runs the source originals in pid order — Encounter
		// before Observation — so the Encounter's restore repoints at an Observation that is still tombstoned.
		// This exercises the restore-ordering hazard with a plain (acyclic) reference, no cycle required.
		@ParameterizedTest
		@CsvSource({"false", "true"})
		void testUndoMerge_referrerHasLowerPidThanTarget_restored(boolean theDeleteSource) {
			// Encounter created first → lower pid
			IIdType encId = createEncounter(myPatientIdSrc, "enc-src");

			// Observation created second → higher pid
			IIdType obsId = createObservation(myPatientIdSrc, null, null, "obs-src");

			// Update the Encounter to reference the Observation, making the lower-pid resource the referrer.
			Encounter enc = readResource(Encounter.class, encId);
			enc.addReasonReference().setReference(obsId.getValue());
			myClient.update().resource(enc).execute();

			// Snapshots before merge
			IBaseResource patientSrcBefore = readResource(Patient.class, myPatientIdSrc);
			IBaseResource encBefore = readResource(Encounter.class, encId);
			IBaseResource obsBefore = readResource(Observation.class, obsId);

			// Merge
			callMerge(new MergeTestParameters()
				.sourceResource(new Reference(myPatientIdSrc))
				.targetResource(new Reference(myPatientIdTgt))
				.deleteSource(theDeleteSource));

			// Verify merge happened and capture the move-created copies (to verify deletion after undo)
			Observation movedObs = assertSingleResourceMovedToTarget(
				Observation.class, Map.of("obs-src", obsId), myPatientIdTgt, Observation::getIdentifier);
			IIdType movedObsId = movedObs.getIdElement().toUnqualifiedVersionless();

			Encounter movedEnc = assertSingleResourceMovedToTarget(
				Encounter.class, Map.of("enc-src", encId), myPatientIdTgt, Encounter::getIdentifier);
			IIdType movedEncId = movedEnc.getIdElement().toUnqualifiedVersionless();

			// Undo merge — must restore both source originals despite the unfavourable pid order
			Parameters undoParams = new Parameters();
			undoParams.addParameter().setName("source-resource").setValue(new Reference(myPatientIdSrc));
			undoParams.addParameter().setName("target-resource").setValue(new Reference(myPatientIdTgt));
			myMergeHelper.callUndoMergeOperation("Patient", undoParams);

			// Both source originals restored, matching pre-merge snapshots
			myMergeHelper.assertResourcesAreEqualIgnoringVersionAndLastUpdated(
				patientSrcBefore, readResource(Patient.class, myPatientIdSrc));
			myMergeHelper.assertResourcesAreEqualIgnoringVersionAndLastUpdated(
				encBefore, readResource(Encounter.class, encId));
			myMergeHelper.assertResourcesAreEqualIgnoringVersionAndLastUpdated(
				obsBefore, readResource(Observation.class, obsId));

			// Move-created copies are deleted
			assertResourceDeleted(movedEncId);
			assertResourceDeleted(movedObsId);

			// No resources left for Patient/tgt
			assertThat(searchBySubject(Observation.class, myPatientIdTgt.getValue())).isEmpty();
			assertThat(searchBySubject(Encounter.class, myPatientIdTgt.getValue())).isEmpty();
		}

		// A cyclic reference between two compartment resources: the Observation references the Encounter
		// (Observation.encounter) and the Encounter references the Observation back (Encounter.reasonReference).
		// No restore order can satisfy both — whichever is undeleted first repoints at a still-tombstoned
		// target — so this is the irreducible case the pid-order heuristic cannot handle.
		@ParameterizedTest
		@CsvSource({"false", "true"})
		void testUndoMerge_cyclicReferenceBetweenCompartmentResources_restored(boolean theDeleteSource) {
			IIdType encId = createEncounter(myPatientIdSrc, "enc-src");

			// Observation references the Encounter
			IIdType obsId = createObservation(myPatientIdSrc, encId, null, "obs-src");

			// Close the cycle: Encounter references the Observation back
			Encounter enc = readResource(Encounter.class, encId);
			enc.addReasonReference().setReference(obsId.getValue());
			myClient.update().resource(enc).execute();

			// Snapshots before merge
			IBaseResource patientSrcBefore = readResource(Patient.class, myPatientIdSrc);
			IBaseResource encBefore = readResource(Encounter.class, encId);
			IBaseResource obsBefore = readResource(Observation.class, obsId);

			// Merge
			callMerge(new MergeTestParameters()
				.sourceResource(new Reference(myPatientIdSrc))
				.targetResource(new Reference(myPatientIdTgt))
				.deleteSource(theDeleteSource));

			// Verify merge happened and capture the move-created copies (to verify deletion after undo)
			Observation movedObs = assertSingleResourceMovedToTarget(
				Observation.class, Map.of("obs-src", obsId), myPatientIdTgt, Observation::getIdentifier);
			IIdType movedObsId = movedObs.getIdElement().toUnqualifiedVersionless();

			Encounter movedEnc = assertSingleResourceMovedToTarget(
				Encounter.class, Map.of("enc-src", encId), myPatientIdTgt, Encounter::getIdentifier);
			IIdType movedEncId = movedEnc.getIdElement().toUnqualifiedVersionless();

			// The cycle must survive into the copies: the moved Observation references the moved Encounter and
			// vice versa, so the undo's copy-delete bundle deletes two resources that reference each other. This
			// is the case that drives the deletes through a single same-partition bundle (intra-transaction
			// referential-integrity), not the order-dependent referrer-then-target sequence.
			assertThat(movedObs.getEncounter().getReference()).isEqualTo(movedEncId.getValue());
			assertThat(movedEnc.getReasonReferenceFirstRep().getReference()).isEqualTo(movedObsId.getValue());

			// Undo merge — must restore both source originals despite the reference cycle
			Parameters undoParams = new Parameters();
			undoParams.addParameter().setName("source-resource").setValue(new Reference(myPatientIdSrc));
			undoParams.addParameter().setName("target-resource").setValue(new Reference(myPatientIdTgt));
			myMergeHelper.callUndoMergeOperation("Patient", undoParams);

			// Both source originals restored, matching pre-merge snapshots
			myMergeHelper.assertResourcesAreEqualIgnoringVersionAndLastUpdated(
				patientSrcBefore, readResource(Patient.class, myPatientIdSrc));
			myMergeHelper.assertResourcesAreEqualIgnoringVersionAndLastUpdated(
				encBefore, readResource(Encounter.class, encId));
			myMergeHelper.assertResourcesAreEqualIgnoringVersionAndLastUpdated(
				obsBefore, readResource(Observation.class, obsId));

			// Move-created copies are deleted
			assertResourceDeleted(movedEncId);
			assertResourceDeleted(movedObsId);

			// No resources left for Patient/tgt
			assertThat(searchBySubject(Observation.class, myPatientIdTgt.getValue())).isEmpty();
			assertThat(searchBySubject(Encounter.class, myPatientIdTgt.getValue())).isEmpty();
		}

		// In a single database, a grouped undo runs in one transaction (partition changes use REQUIRED
		// propagation), so a failure partway through rolls the whole undo back — nothing is left reverted.
		// The source is restored first and would succeed, but a later sub-Provenance restore is forced to
		// fail; we assert the source was NOT left reverted (it still carries its post-merge replaced-by link).
		@Test
		void testUndoMerge_failurePartway_rollsBackAtomicallyInSingleDatabase() {
			IIdType groupId = createGroup(myPatientIdSrc);

			// Merge, keeping the source. Source gains a replaced-by link to target; the Group's member is
			// rewritten from source to target.
			callMerge(new MergeTestParameters()
				.sourceResource(new Reference(myPatientIdSrc))
				.targetResource(new Reference(myPatientIdTgt))
				.deleteSource(false));

			Patient mergedSource = readResource(Patient.class, myPatientIdSrc);
			assertThat(mergedSource.getLink())
				.as("source should carry a replaced-by link after merge")
				.isNotEmpty();
			assertThat(readResource(Group.class, groupId).getMemberFirstRep().getEntity().getReference())
				.isEqualTo(myPatientIdTgt.getValue());

			// Bump the Group's version so its restore fails the version check, forcing the undo to fail
			// after the source (restored first) has already been restored within the transaction.
			Group bump = readResource(Group.class, groupId);
			bump.setName("version-bumped-to-force-restore-conflict");
			myClient.update().resource(bump).execute();

			// Snapshot the post-merge state right before the undo. An atomic rollback must leave every
			// resource the undo touches — source, target, and the Group — exactly as it is now.
			IBaseResource sourceBeforeUndo = readResource(Patient.class, myPatientIdSrc);
			IBaseResource targetBeforeUndo = readResource(Patient.class, myPatientIdTgt);
			IBaseResource groupBeforeUndo = readResource(Group.class, groupId);

			// Undo must fail.
			Parameters undoParams = new Parameters();
			undoParams.addParameter().setName("source-resource").setValue(new Reference(myPatientIdSrc));
			undoParams.addParameter().setName("target-resource").setValue(new Reference(myPatientIdTgt));
			assertThatThrownBy(() -> myMergeHelper.callUndoMergeOperation("Patient", undoParams))
				.isInstanceOf(BaseServerResponseException.class);

			// Atomic rollback: the source restore ran first and would have removed the replaced-by link, but
			// it was rolled back together with the failing restore. Every touched resource must remain in its
			// pre-undo (merged) state.
			myMergeHelper.assertResourcesAreEqualIgnoringVersionAndLastUpdated(
				sourceBeforeUndo, readResource(Patient.class, myPatientIdSrc));
			myMergeHelper.assertResourcesAreEqualIgnoringVersionAndLastUpdated(
				targetBeforeUndo, readResource(Patient.class, myPatientIdTgt));
			myMergeHelper.assertResourcesAreEqualIgnoringVersionAndLastUpdated(
				groupBeforeUndo, readResource(Group.class, groupId));
		}
	}

	@Nested
	class MergeAutoRollbackWithSharedTransaction {

		// In a single database the whole merge runs in one transaction (REQUIRED propagation), so a failure
		// rolls everything back automatically — there is nothing committed to compensate, so the merge service
		// just propagates the original failure (like a same-partition merge would) rather than reporting a
		// rollback. We force a failure during the source/target update and assert every resource is back to its
		// pre-merge state.
		@Test
		void testMerge_failurePartway_rollsBackFullyInSingleDatabase() {
			// Data the merge will touch: a Group referencing the source (updated in place) and an Observation in
			// the source compartment (copied to the target partition).
			IIdType groupId = createGroup(myPatientIdSrc);
			IIdType obsId = createObservation(myPatientIdSrc, null, null, "obs-rollback");

			IBaseResource sourceBefore = readResource(Patient.class, myPatientIdSrc);
			IBaseResource targetBefore = readResource(Patient.class, myPatientIdTgt);
			IBaseResource groupBefore = readResource(Group.class, groupId);
			IBaseResource obsBefore = readResource(Observation.class, obsId);

			// Fail the merge while updating the target Patient (Step 3), after Step 1 (data) and Step 2
			// (per-partition Provenances) have run within the merge's transaction.
			FailOnTargetPatientUpdateInterceptor failer =
				new FailOnTargetPatientUpdateInterceptor(myPatientIdTgt.getIdPart());
			myInterceptorRegistry.registerInterceptor(failer);
			try {
				myMergeHelper.callMergeAndValidateException(
					"Patient",
					new MergeTestParameters()
						.sourceResource(new Reference(myPatientIdSrc))
						.targetResource(new Reference(myPatientIdTgt))
						.deleteSource(false),
					InternalErrorException.class,
					"Simulated failure during target Patient update");
			} finally {
				myInterceptorRegistry.unregisterInterceptor(failer);
			}

			// Single-database merge runs in one transaction, so the failure rolled everything back: every
			// resource matches its pre-merge snapshot.
			myMergeHelper.assertResourcesAreEqualIgnoringVersionAndLastUpdated(
				sourceBefore, readResource(Patient.class, myPatientIdSrc));
			myMergeHelper.assertResourcesAreEqualIgnoringVersionAndLastUpdated(
				targetBefore, readResource(Patient.class, myPatientIdTgt));
			myMergeHelper.assertResourcesAreEqualIgnoringVersionAndLastUpdated(
				groupBefore, readResource(Group.class, groupId));
			myMergeHelper.assertResourcesAreEqualIgnoringVersionAndLastUpdated(
				obsBefore, readResource(Observation.class, obsId));

			// No copy was created in the target's compartment; the original is still in the source's.
			assertThat(searchBySubject(Observation.class, myPatientIdTgt.getValue())).isEmpty();
			assertThat(searchBySubject(Observation.class, myPatientIdSrc.getValue())).hasSize(1);
		}
	}

	/**
	 * Test interceptor that fails the merge while the target Patient is being updated (Step 3 of the
	 * cross-partition forward flow), after the data bundle and per-partition Provenances have run.
	 */
	@Interceptor
	static class FailOnTargetPatientUpdateInterceptor {
		private final String myTargetIdPart;

		FailOnTargetPatientUpdateInterceptor(String theTargetIdPart) {
			myTargetIdPart = theTargetIdPart;
		}

		@Hook(Pointcut.STORAGE_PRESTORAGE_RESOURCE_UPDATED)
		public void preUpdate(
			RequestDetails theRequest, IBaseResource theOldResource, IBaseResource theNewResource) {
			if (theNewResource instanceof Patient
				&& myTargetIdPart.equals(theNewResource.getIdElement().getIdPart())) {
				throw new InternalErrorException("Simulated failure during target Patient update");
			}
		}
	}

	/**
	 * Exercises the actual rollback ({@code rollbackPartialCrossPartitionMerge}) — the path
	 * taken when partition writes commit in their own transactions. This is configured by flipping the
	 * transaction propagation to REQUIRES_NEW, which makes each partition-changing step (data copy,
	 * per-partition Provenances, src/tgt updates, deletes) commit independently. So when a later step
	 * fails, the earlier steps are durably committed and the merge service must revert them — unlike the
	 * single-database REQUIRED case ({@link MergeAutoRollbackWithSharedTransaction}) where the outer transaction rolls
	 * everything back and the rollback is a no-op report.
	 */
	@Nested
	class MergeAutoRollbackWithIndependentCommits {

		private MergeBundlePerPartitionSplitInterceptor myBundleSplitter;

		// Data every test in this class touches, with its pre-merge snapshot: a referrer Group (default
		// partition, updated in place) and an Observation in the source compartment (copied to the target).
		private IIdType myGroupId;
		private IIdType myObsId;
		private IBaseResource mySourceBefore;
		private IBaseResource myTargetBefore;
		private IBaseResource myGroupBefore;
		private IBaseResource myObsBefore;

		@BeforeEach
		void enableIndependentCommitsAndSeedData() {
			myHapiTransactionService.setTransactionPropagationWhenChangingPartitions(Propagation.REQUIRES_NEW);

			myGroupId = createGroup(myPatientIdSrc);
			myObsId = createObservation(myPatientIdSrc, null, null, "obs-rollback");
			mySourceBefore = readResource(Patient.class, myPatientIdSrc);
			myTargetBefore = readResource(Patient.class, myPatientIdTgt);
			myGroupBefore = readResource(Group.class, myGroupId);
			myObsBefore = readResource(Observation.class, myObsId);

			// With REQUIRES_NEW, the merge's combined cross-partition data bundle would otherwise be rejected
			// (HAPI-2541) because its entries span partitions; this slicer splits it into single-partition
			// transactions, standing in for the production transaction-partitioning interceptor that a
			// physically-sharded deployment would provide. The map records where each seeded resource lives.
			myBundleSplitter = new MergeBundlePerPartitionSplitInterceptor(Map.of(
				myPatientIdSrc.getIdPart(), MergeBundlePerPartitionSplitInterceptor.PARTITION_SOURCE,
				myObsId.getIdPart(), MergeBundlePerPartitionSplitInterceptor.PARTITION_SOURCE,
				myPatientIdTgt.getIdPart(), MergeBundlePerPartitionSplitInterceptor.PARTITION_TARGET,
				myGroupId.getIdPart(), MergeBundlePerPartitionSplitInterceptor.PARTITION_DEFAULT));
			myInterceptorRegistry.registerInterceptor(myBundleSplitter);
		}

		@AfterEach
		void resetPartitionCommitPropagation() {
			myInterceptorRegistry.unregisterInterceptor(myBundleSplitter);
			myHapiTransactionService.setTransactionPropagationWhenChangingPartitions(
				HapiTransactionService.DEFAULT_TRANSACTION_PROPAGATION_WHEN_CHANGING_PARTITIONS);
		}

		// Fail while updating the source/target Patients, after the data copy and per-partition Provenances
		// have each committed independently. The rollback must revert the committed copies and the referrer
		// Group, then report a clean full rollback. (The target is always updated, so this fails the same way
		// regardless of deleteSource.)
		@ParameterizedTest
		@ValueSource(booleans = {false, true})
		void testMerge_failAtSrcTgtUpdate_revertsCommittedSteps(boolean theDeleteSource) {
			FailOnTargetPatientUpdateInterceptor failer =
				new FailOnTargetPatientUpdateInterceptor(myPatientIdTgt.getIdPart());
			myInterceptorRegistry.registerInterceptor(failer);
			try {
				myMergeHelper.callMergeAndValidateException(
					"Patient",
					mergeParams(theDeleteSource),
					InternalErrorException.class,
					FULLY_ROLLED_BACK_MESSAGE_PREFIX
						+ "InternalErrorException: Simulated failure during target Patient update");
			} finally {
				myInterceptorRegistry.unregisterInterceptor(failer);
			}

			assertFullyRevertedToPreMerge();
		}

		// Fail while creating the Provenance, after the source/target updates have committed independently. The
		// rollback must additionally restore the committed target (and the source when it was kept). With
		// deleteSource the source is never updated (only the target) and its delete never runs, so it stays
		// untouched and needs no revert.
		@ParameterizedTest
		@ValueSource(booleans = {false, true})
		void testMerge_failAtProvenance_revertsCommittedSteps(boolean theDeleteSource) {
			FailOnProvenanceCreateInterceptor failer = new FailOnProvenanceCreateInterceptor();
			myInterceptorRegistry.registerInterceptor(failer);
			try {
				myMergeHelper.callMergeAndValidateException(
					"Patient",
					mergeParams(theDeleteSource),
					InternalErrorException.class,
					FULLY_ROLLED_BACK_MESSAGE_PREFIX
						+ "InternalErrorException: Simulated failure during Provenance creation");
			} finally {
				myInterceptorRegistry.unregisterInterceptor(failer);
			}

			assertFullyRevertedToPreMerge();
		}

		// Fail while deleting the source-side original, after everything else (data copy, Provenance,
		// source/target updates) has committed independently. This is the most complete revert: the whole merge
		// committed except the final cleanup delete.
		@ParameterizedTest
		@ValueSource(booleans = {false, true})
		void testMerge_failAtOriginalDelete_revertsEntireMerge(boolean theDeleteSource) {
			// Key on the original Observation's id part so the rollback's own delete of the target-side
			// copy (a different id) is not affected.
			FailOnObservationDeleteInterceptor failer =
				new FailOnObservationDeleteInterceptor(myObsId.getIdPart());
			myInterceptorRegistry.registerInterceptor(failer);
			try {
				myMergeHelper.callMergeAndValidateException(
					"Patient",
					mergeParams(theDeleteSource),
					InternalErrorException.class,
					FULLY_ROLLED_BACK_MESSAGE_PREFIX
						+ "InternalErrorException: Simulated failure during original Observation delete");
			} finally {
				myInterceptorRegistry.unregisterInterceptor(failer);
			}

			assertFullyRevertedToPreMerge();
		}

		// Partial rollback. The rollback reverts the recorded resources as one cross-partition transaction that
		// the partition splitter runs as independent per-partition sub-bundles (source, then target, then
		// default), stopping at the first that fails. We fail the Provenance creation to trigger the rollback,
		// then make the rollback itself fail on the Group:
		//   - the source partition (the source Patient when it was kept) and the target partition (the target
		//     Patient) restores commit
		//   - the default partition's Group restore then fails and aborts the revert transaction
		// The Observation copy is a v1 copy whose delete is a SEPARATE bundle that the restorer runs only AFTER
		// the update bundle; since the update bundle threw on the Group, the copy delete never runs. So both the
		// Group (its restore failed) and the Observation copy (its delete never ran) remain merged and are the
		// resources reported for manual revert (notReverted = what we tried to revert − what committed).
		@ParameterizedTest
		@ValueSource(booleans = {false, true})
		void testMerge_revertFailsMidSequence_reportsFailedAndSubsequentResources(boolean theDeleteSource) {
			FailProvenanceThenGroupRestoreInterceptor failer = new FailProvenanceThenGroupRestoreInterceptor();
			myInterceptorRegistry.registerInterceptor(failer);

			String diagnosticMessage;
			try {
				diagnosticMessage = myMergeHelper.callMergeAndExtractDiagnosticMessage(
					"Patient", mergeParams(theDeleteSource), InternalErrorException.class);
			} finally {
				myInterceptorRegistry.unregisterInterceptor(failer);
			}

			// The Observation copy lives in the target compartment; its server-assigned id is only known now.
			IIdType obsCopyId = searchBySubject(Observation.class, myPatientIdTgt.getValue())
					.get(0)
					.getIdElement();

			// Both the Group (v2, restore failed) and the Observation copy (v1, delete never ran) are reported.
			assertThat(diagnosticMessage)
				.contains("could not be reverted and remain in their merged state, and must be reverted manually:")
				.contains(myGroupId.withVersion("2").getValue())
				.contains(obsCopyId.toUnqualifiedVersionless().getValue() + "/_history/1")
				.contains(
					"Merge failure cause: InternalErrorException: Simulated failure during Provenance creation");

			// The Group's restore failed, so it stays merged (still pointing at the target).
			Group groupAfter = readResource(Group.class, myGroupId);
			assertThat(groupAfter.getMemberFirstRep().getEntity().getReference())
				.isEqualTo(myPatientIdTgt.getValue());

			// The source and target partitions' reverts committed before the failure: both Patients match their
			// pre-merge state, and the source-compartment original Observation is untouched (the delete step,
			// which runs after Provenance creation, never ran).
			myMergeHelper.assertResourcesAreEqualIgnoringVersionAndLastUpdated(
				mySourceBefore, readResource(Patient.class, myPatientIdSrc));
			myMergeHelper.assertResourcesAreEqualIgnoringVersionAndLastUpdated(
				myTargetBefore, readResource(Patient.class, myPatientIdTgt));
			myMergeHelper.assertResourcesAreEqualIgnoringVersionAndLastUpdated(
				myObsBefore, readResource(Observation.class, myObsId));
			// The Observation copy's delete never ran, so the copy remains in the target compartment.
			assertThat(searchBySubject(Observation.class, myPatientIdTgt.getValue())).hasSize(1);
			assertThat(searchBySubject(Observation.class, myPatientIdSrc.getValue())).hasSize(1);
		}

		private MergeTestParameters mergeParams(boolean theDeleteSource) {
			return new MergeTestParameters()
				.sourceResource(new Reference(myPatientIdSrc))
				.targetResource(new Reference(myPatientIdTgt))
				.deleteSource(theDeleteSource);
		}

		// Asserts the merge was fully reverted: source, target, referrer Group and the source-compartment
		// Observation all match their pre-merge snapshots, with no target-side Observation copy lingering.
		private void assertFullyRevertedToPreMerge() {
			Group groupAfter = readResource(Group.class, myGroupId);
			myMergeHelper.assertResourcesAreEqualIgnoringVersionAndLastUpdated(
				mySourceBefore, readResource(Patient.class, myPatientIdSrc));
			myMergeHelper.assertResourcesAreEqualIgnoringVersionAndLastUpdated(
				myTargetBefore, readResource(Patient.class, myPatientIdTgt));
			myMergeHelper.assertResourcesAreEqualIgnoringVersionAndLastUpdated(myGroupBefore, groupAfter);
			myMergeHelper.assertResourcesAreEqualIgnoringVersionAndLastUpdated(
				myObsBefore, readResource(Observation.class, myObsId));
			assertThat(searchBySubject(Observation.class, myPatientIdTgt.getValue())).isEmpty();
			assertThat(searchBySubject(Observation.class, myPatientIdSrc.getValue())).hasSize(1);

			// The rollback also deletes the merge Provenances (the main one plus the per-partition ones), all of
			// which target the target patient — none should remain.
			List<IBaseResource> remainingProvenances = myReplaceReferencesHelper.searchProvenance(myPatientIdTgt);
			assertThat(remainingProvenances).isEmpty();

			// NOTE: version-count assumption removed for the no-ordering single-bundle model. With per-shard
			// independent commits and no data-before-finalization phasing, whether the Group (a default-partition
			// referrer) committed before the injected failure depends on shard commit order, so we no longer assert
			// "forward update + restore = +2 versions". What matters is that its content matches the pre-merge state.
			// long groupVersionBefore = myGroupBefore.getIdElement().getVersionIdPartAsLong();
			// long groupVersionAfter = groupAfter.getIdElement().getVersionIdPartAsLong();
			// assertThat(groupVersionAfter).isEqualTo(groupVersionBefore + 2);
		}
	}

	/**
	 * Directly exercises the undelete branch of {@code rollbackPartialCrossPartitionMerge}: restoring
	 * source-side originals that the merge had durably deleted. This branch cannot be reached through
	 * end-to-end failure injection because the source-side delete is the terminal step — nothing runs after it
	 * that could throw to trigger a rollback. So we drive the rollback service directly with a hand-built
	 * context, which this test can do as it shares the service's package.
	 */
	@Nested
	class MergeRollbackUndeleteComponent {

		@Test
		void testRollback_undeletesCommittedOriginal_whenDeletesCommitted() {
			// Create a source-compartment Observation, capture its pre-delete state, then delete it so it
			// is tombstoned at version 2 — the state the merge's delete step would have left it in.
			IIdType obsId = createObservation(myPatientIdSrc, null, null, "obs-undelete");
			Observation obsBeforeDelete = readResource(Observation.class, obsId);
			myClient.delete().resourceById(obsId).execute();
			assertResourceDeleted(obsId);

			// Build the context exactly as the forward steps would after a committed delete: the original recorded
			// as a resource to undelete at its tombstone version (original + 1), and the failure cause that
			// triggered the rollback.
			MergeRollbackContext rollbackContext = new MergeRollbackContext();
			rollbackContext.addResourcesToUndelete(List.of(obsId.withVersion("2")));
			rollbackContext.setFailureCause(new InternalErrorException("Simulated forward failure"));

			OperationOutcomeWithStatusCode outcome = new OperationOutcomeWithStatusCode();
			outcome.setOperationOutcome(new OperationOutcome());

			myCrossPartitionMergeRollbackService.rollbackPartialCrossPartitionMerge(
				rollbackContext, newSrd(), outcome);

			// The tombstoned original is undeleted and matches its pre-delete content.
			myMergeHelper.assertResourcesAreEqualIgnoringVersionAndLastUpdated(
				obsBeforeDelete, readResource(Observation.class, obsId));

			// The outcome reports a clean full rollback, naming the failure cause.
			String diagnostics = ((OperationOutcome) outcome.getOperationOutcome())
				.getIssueFirstRep()
				.getDiagnostics();
			assertThat(diagnostics)
				.isEqualTo(FULLY_ROLLED_BACK_MESSAGE_PREFIX + "InternalErrorException: Simulated forward failure");
		}
	}

	/**
	 * Fails the merge while the Provenance is being created.
	 */
	@Interceptor
	static class FailOnProvenanceCreateInterceptor {

		@Hook(Pointcut.STORAGE_PRESTORAGE_RESOURCE_CREATED)
		public void preCreate(IBaseResource theResource) {
			// The merge's Provenance is the only resource created with contained resources (the input parameters
			// and operation outcome), so this fails on it specifically.
			if (theResource instanceof Provenance provenance && !provenance.getContained().isEmpty()) {
				throw new InternalErrorException("Simulated failure during Provenance creation");
			}
		}
	}

	/**
	 * Fails the merge while the source-side original Observation is being deleted. Keyed on the original's
	 * id part so the rollback's own delete of the target-side copy (a different id) proceeds.
	 */
	@Interceptor
	static class FailOnObservationDeleteInterceptor {
		private final String myObservationIdPart;

		FailOnObservationDeleteInterceptor(String theObservationIdPart) {
			myObservationIdPart = theObservationIdPart;
		}

		@Hook(Pointcut.STORAGE_PRESTORAGE_RESOURCE_DELETED)
		public void preDelete(IBaseResource theResource) {
			if (theResource instanceof Observation
				&& myObservationIdPart.equals(theResource.getIdElement().getIdPart())) {
				throw new InternalErrorException("Simulated failure during original Observation delete");
			}
		}
	}

	/**
	 * Splits the merge's combined data bundle into single-partition slices via the
	 * {@link Pointcut#STORAGE_TRANSACTION_PRE_PARTITION} hook, standing in for the transaction-partitioning
	 * interceptor a physically-sharded deployment would provide. Without it the bundle's cross-partition
	 * entries are rejected (HAPI-2541) under REQUIRES_NEW.
	 * Entries are grouped into one sub-bundle per partition, using a hardcoded id-to-partition map built by
	 * the test setup. Entries whose id is not in the map (the POSTed copies, which have no id yet, and the
	 * rollback's deletes of those copies) belong to the target's partition. Sub-bundles are emitted in a
	 * fixed partition order (source, target, default) so tests can rely on the commit/revert sequence.
	 * One quirk: DELETE entries carry no resource content, so the patient-id partition interceptor cannot
	 * resolve their partition and would reject them (HAPI-2541) if grouped with other entries; each one is
	 * therefore emitted as its own singleton sub-bundle, sequenced with the target partition it belongs to.
	 */
	@Interceptor
	static class MergeBundlePerPartitionSplitInterceptor {

		static final String PARTITION_SOURCE = "source";
		static final String PARTITION_TARGET = "target";
		static final String PARTITION_DEFAULT = "default";

		private final Map<String, String> myIdPartToPartition;

		MergeBundlePerPartitionSplitInterceptor(Map<String, String> theIdPartToPartition) {
			myIdPartToPartition = theIdPartToPartition;
		}

		@Hook(Pointcut.STORAGE_TRANSACTION_PRE_PARTITION)
		public TransactionPrePartitionResponse split(IBaseBundle theInputBundle) {
			Bundle input = (Bundle) theInputBundle;

			Map<String, Bundle> bundlesByPartition = new LinkedHashMap<>();
			for (String partition : List.of(PARTITION_SOURCE, PARTITION_TARGET, PARTITION_DEFAULT)) {
				Bundle bundle = new Bundle();
				bundle.setType(Bundle.BundleType.TRANSACTION);
				bundlesByPartition.put(partition, bundle);
			}
			List<IBaseBundle> deleteSingletonBundles = new ArrayList<>();

			for (Bundle.BundleEntryComponent entry : input.getEntry()) {
				if (entry.getResource() == null) {
					// DELETE entry — see the class doc for why it gets its own singleton sub-bundle.
					Bundle singleEntryBundle = new Bundle();
					singleEntryBundle.setType(Bundle.BundleType.TRANSACTION);
					singleEntryBundle.addEntry(entry);
					deleteSingletonBundles.add(singleEntryBundle);
					continue;
				}
				String idPart = entry.getResource().getIdElement().getIdPart();
				// POST entries have no id yet (null id part), mapping them to the target partition.
				String partition =
					idPart == null ? PARTITION_TARGET : myIdPartToPartition.getOrDefault(idPart, PARTITION_TARGET);
				bundlesByPartition.get(partition).addEntry(entry);
			}

			// Empty sub-bundles are filtered out by the partition processor.
			List<IBaseBundle> splitBundles = new ArrayList<>();
			splitBundles.add(bundlesByPartition.get(PARTITION_SOURCE));
			splitBundles.add(bundlesByPartition.get(PARTITION_TARGET));
			splitBundles.addAll(deleteSingletonBundles);
			splitBundles.add(bundlesByPartition.get(PARTITION_DEFAULT));
			return new TransactionPrePartitionResponse().setSplitBundles(splitBundles);
		}
	}

	/**
	 * Triggers a partial rollback: it fails the Provenance creation to start the rollback, then fails the
	 * rollback's restore of the referrer Group so that resource cannot be reverted and is reported for manual
	 * revert. The forward update of the Group during the data copy is left alone (the flag is only set once the
	 * rollback has begun).
	 */
	@Interceptor
	static class FailProvenanceThenGroupRestoreInterceptor {
		private boolean myInRollback = false;

		@Hook(Pointcut.STORAGE_PRESTORAGE_RESOURCE_CREATED)
		public void preCreate(IBaseResource theResource) {
			if (theResource instanceof Provenance provenance && !provenance.getContained().isEmpty()) {
				myInRollback = true;
				throw new InternalErrorException("Simulated failure during Provenance creation");
			}
		}

		// Both IBaseResource params are required so the framework can disambiguate them positionally
		// (old, then new); only the new resource is needed here.
		@Hook(Pointcut.STORAGE_PRESTORAGE_RESOURCE_UPDATED)
		public void preUpdate(IBaseResource theOldResource, IBaseResource theNewResource) {
			if (myInRollback && theNewResource instanceof Group) {
				throw new InternalErrorException("Simulated failure restoring the Group during rollback");
			}
		}
	}

}
