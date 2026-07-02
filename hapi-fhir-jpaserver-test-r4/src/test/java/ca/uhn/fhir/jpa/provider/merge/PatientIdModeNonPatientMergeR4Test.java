package ca.uhn.fhir.jpa.provider.merge;

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
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.DiagnosticReport;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;

import static ca.uhn.fhir.jpa.config.r4.FhirContextR4Config.DEFAULT_PRESERVE_VERSION_REFS_R4_AND_LATER;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for non-Patient resource $merge in PATIENT_ID unnamed partition mode.
 *
 * <p>Verifies that merging non-Patient resources (Observation, Organization) succeeds
 * when {@code PatientIdPartitionInterceptor} is active, now that Provenance uses the
 * {@code NON_UNIQUE_COMPARTMENT_IN_DEFAULT} policy.
 */
// Created by claude-opus-4-6
class PatientIdModeNonPatientMergeR4Test extends BaseResourceProviderR4Test {

	@Autowired
	private Batch2JobHelper myBatch2JobHelper;
	@Autowired
	private ISearchParamExtractor mySearchParamExtractor;
	@Autowired
	private ResourceLinkServiceFactory myResourceLinkServiceFactory;
	private MergeOperationTestHelper myMergeHelper;

	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();

		// Registered here, unregistered automatically by BaseJpaTest.@AfterEach
		PatientIdPartitionInterceptor partitionInterceptor = new PatientIdPartitionInterceptor(
			getFhirContext(), mySearchParamExtractor, myPartitionSettings, myDaoRegistry);
		registerInterceptor(partitionInterceptor);

		myPartitionSettings.setPartitioningEnabled(true);
		myPartitionSettings.setUnnamedPartitionMode(true);
		myPartitionSettings.setAllowReferencesAcrossPartitions(
			PartitionSettings.CrossPartitionReferenceMode.ALLOWED_UNQUALIFIED);

		myFhirContext.setParserErrorHandler(new StrictErrorHandler());
		myFhirContext.getParserOptions().setDontStripVersionsFromReferencesAtPaths("Provenance.target");

		myMergeHelper = new MergeOperationTestHelper(
			myClient, myBatch2JobHelper, myFhirContext, myResourceLinkServiceFactory, myDaoRegistry);
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

	private Integer getPartitionId(IIdType theId) {
		return runInTransaction(() -> {
			ResourceTable resourceTable = myResourceTableDao
				.findByTypeAndFhirId(theId.getResourceType(), theId.getIdPart())
				.orElseThrow(() -> new AssertionError("Resource not found: " + theId.toUnqualifiedVersionless()));
			return resourceTable.getPartitionId().getPartitionId();
		});
	}

	private void assertInSamePartition(IIdType theId1, IIdType theId2) {
		Integer partitionId1 = getPartitionId(theId1);
		Integer partitionId2 = getPartitionId(theId2);
		assertThat(partitionId1)
			.as("Expected %s and %s to be in the same partition", theId1, theId2)
			.isEqualTo(partitionId2);
	}

	private <T extends IBaseResource> T readResource(Class<T> theType, IIdType theId) {
		return myClient.read().resource(theType).withId(theId).execute();
	}

	private Identifier createTestIdentifier(String theValue) {
		return new Identifier().setSystem("http://test").setValue(theValue);
	}

	// Observation merge in Patient ID Partition mode: both Observations reference the same Patient
	// and are in the same partition.
	@Test
	void testMergeObservations_ThenUndoMergeThem_BothSucceed() {
		// Setup: create a Patient for the Observations to reference
		Patient patient = new Patient();
		patient.setId("Patient/obs-owner");
		myClient.update().resource(patient).execute();
		IIdType patientId = new IdType("Patient/obs-owner");

		// Setup: two Observations referencing the same Patient (same compartment)
		Observation sourceObs = new Observation();
		sourceObs.getSubject().setReference(patientId.getValue());
		sourceObs.addIdentifier(createTestIdentifier("obs-source"));
		IIdType sourceObsId = myClient.create().resource(sourceObs).execute().getId().toUnqualifiedVersionless();

		Observation targetObs = new Observation();
		targetObs.getSubject().setReference(patientId.getValue());
		targetObs.addIdentifier(createTestIdentifier("obs-target"));
		IIdType targetObsId = myClient.create().resource(targetObs).execute().getId().toUnqualifiedVersionless();

		assertInSamePartition(sourceObsId, targetObsId);

		// Create a DiagnosticReport that references the source Observation
		DiagnosticReport report = new DiagnosticReport();
		report.getSubject().setReference(patientId.getValue());
		report.addResult().setReference(sourceObsId.getValue());
		IIdType reportId = myClient.create().resource(report).execute().getId().toUnqualifiedVersionless();

		// Save snapshots before merge
		IBaseResource sourceObsBefore = readResource(Observation.class, sourceObsId);
		IBaseResource targetObsBefore = readResource(Observation.class, targetObsId);
		IBaseResource reportBefore = readResource(DiagnosticReport.class, reportId);

		// Execute: merge Observation via $hapi.fhir.merge
		MergeTestParameters params = new MergeTestParameters()
			.sourceResource(new Reference(sourceObsId))
			.targetResource(new Reference(targetObsId))
			.deleteSource(false);
		Parameters result = myMergeHelper.callMergeOperation("Observation", params, false);

		// Verify: merge succeeded
		myMergeHelper.validateSyncMergeOutcome(result, params.asParametersResource(), targetObsId);

		// Validate resources after merge
		IIdType expectedVersionedSourceId = sourceObsId.withVersion("2");
		IIdType expectedVersionedTargetId = targetObsId.withVersion("2");
		IIdType expectedVersionedReportId = reportId.withVersion("2");
		List<IIdType> referencingResourceIds = List.of(reportId);
		Set<String> expectedProvenanceTargets = Set.of(
			expectedVersionedSourceId.toString(),
			expectedVersionedTargetId.toString(),
			expectedVersionedReportId.toString());
		List<Identifier> sourceIdentifiers = List.of(createTestIdentifier("obs-source"));
		List<Identifier> targetIdentifiers = List.of(createTestIdentifier("obs-target"));
		List<Identifier> expectedTargetIdentifiers = myMergeHelper.computeIdentifiersExpectedOnTargetAfterMerge(
			targetIdentifiers, sourceIdentifiers, null);
		myMergeHelper.validateResourcesAfterMerge(params,
			expectedVersionedSourceId, expectedVersionedTargetId,
			referencingResourceIds, expectedProvenanceTargets,
			expectedTargetIdentifiers, null);

		// Undo merge
		Parameters undoParams = new Parameters();
		undoParams.addParameter().setName("source-resource").setValue(new Reference(sourceObsId));
		undoParams.addParameter().setName("target-resource").setValue(new Reference(targetObsId));
		myMergeHelper.callUndoMergeOperation("Observation", undoParams);

		// Verify: all resources match their pre-merge state
		myMergeHelper.assertResourcesAreEqualIgnoringVersionAndLastUpdated(sourceObsBefore, readResource(Observation.class, sourceObsId));
		myMergeHelper.assertResourcesAreEqualIgnoringVersionAndLastUpdated(targetObsBefore, readResource(Observation.class, targetObsId));
		myMergeHelper.assertResourcesAreEqualIgnoringVersionAndLastUpdated(reportBefore, readResource(DiagnosticReport.class, reportId));
	}

	// Organization merge in Patient ID Partition mode: Organizations are not in the Patient
	// compartment (ALWAYS_USE_DEFAULT_PARTITION policy), so they live in the default partition.
	@Test
	void testMergeOrganizations_ThenUndoMergeThem_BothSucceed() {
		Organization sourceOrg = new Organization();
		sourceOrg.setName("Source Org");
		sourceOrg.addIdentifier(createTestIdentifier("org-source"));
		IIdType sourceOrgId = myClient.create().resource(sourceOrg).execute().getId().toUnqualifiedVersionless();

		Organization targetOrg = new Organization();
		targetOrg.setName("Target Org");
		targetOrg.addIdentifier(createTestIdentifier("org-target"));
		IIdType targetOrgId = myClient.create().resource(targetOrg).execute().getId().toUnqualifiedVersionless();

		// Create a Patient that references the source Organization
		Patient patient = new Patient();
		patient.setId("Patient/org-patient");
		patient.getManagingOrganization().setReference(sourceOrgId.getValue());
		myClient.update().resource(patient).execute();
		IIdType patientId = new IdType("Patient/org-patient");

		// Save snapshots before merge
		IBaseResource sourceOrgBefore = readResource(Organization.class, sourceOrgId);
		IBaseResource targetOrgBefore = readResource(Organization.class, targetOrgId);
		IBaseResource patientBefore = readResource(Patient.class, patientId);

		// Execute
		MergeTestParameters params = new MergeTestParameters()
			.sourceResource(new Reference(sourceOrgId))
			.targetResource(new Reference(targetOrgId))
			.deleteSource(false);
		Parameters result = myMergeHelper.callMergeOperation("Organization", params, false);

		// Verify: merge succeeded
		myMergeHelper.validateSyncMergeOutcome(result, params.asParametersResource(), targetOrgId);

		// Validate resources after merge
		IIdType expectedVersionedSourceId = sourceOrgId.withVersion("2");
		IIdType expectedVersionedTargetId = targetOrgId.withVersion("2");
		IIdType expectedVersionedPatientId = patientId.withVersion("2");
		List<IIdType> referencingResourceIds = List.of(patientId);
		Set<String> expectedProvenanceTargets = Set.of(
			expectedVersionedSourceId.toString(),
			expectedVersionedTargetId.toString(),
			expectedVersionedPatientId.toString());
		List<Identifier> sourceIdentifiers = List.of(createTestIdentifier("org-source"));
		List<Identifier> targetIdentifiers = List.of(createTestIdentifier("org-target"));
		List<Identifier> expectedTargetIdentifiers = myMergeHelper.computeIdentifiersExpectedOnTargetAfterMerge(
			targetIdentifiers, sourceIdentifiers, null);
		myMergeHelper.validateResourcesAfterMerge(params,
			expectedVersionedSourceId, expectedVersionedTargetId,
			referencingResourceIds, expectedProvenanceTargets,
			expectedTargetIdentifiers, null);

		// Undo merge
		Parameters undoParams = new Parameters();
		undoParams.addParameter().setName("source-resource").setValue(new Reference(sourceOrgId));
		undoParams.addParameter().setName("target-resource").setValue(new Reference(targetOrgId));
		myMergeHelper.callUndoMergeOperation("Organization", undoParams);

		// Verify: all resources match their pre-merge state
		myMergeHelper.assertResourcesAreEqualIgnoringVersionAndLastUpdated(sourceOrgBefore, readResource(Organization.class, sourceOrgId));
		myMergeHelper.assertResourcesAreEqualIgnoringVersionAndLastUpdated(targetOrgBefore, readResource(Organization.class, targetOrgId));
		myMergeHelper.assertResourcesAreEqualIgnoringVersionAndLastUpdated(patientBefore, readResource(Patient.class, patientId));
	}
}
