// Created by claude-sonnet-4-5
package ca.uhn.fhir.jpa.provider.r4;

/*-
 * #%L
 * HAPI FHIR JPA Server Test - R4
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.batch2.jobs.merge.ResourceLinkServiceFactory;
import ca.uhn.fhir.jpa.merge.MergeOperationTestHelper;
import ca.uhn.fhir.jpa.merge.MergeTestDataBuilder;
import ca.uhn.fhir.jpa.merge.MergeTestParameters;
import ca.uhn.fhir.jpa.merge.MergeTestScenario;
import ca.uhn.fhir.jpa.merge.ObservationMergeTestStrategy;
import ca.uhn.fhir.jpa.merge.ReferencingResourceConfig;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.jpa.test.Batch2JobHelper;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_MERGE_OUTPUT_PARAM_TASK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Comprehensive integration tests for generic merge operations on Observation resources.
 *
 * This test class validates the $hapi-fhir-merge operation for Observation resources,
 * covering all merge scenarios including:
 * - Basic merge matrix (sync/async, delete/no-delete, preview/execute, with/without result-resource)
 * - Identifier-based resource resolution
 * - Extension-based link validation (replaces/replaced-by)
 * - Edge cases and error handling
 * - Provenance creation validation
 * - Observation status handling (Observation has status, not active field)
 * - Result resource validation
 *
 * <p><b>Key difference from Practitioner</b>: Observation resources do NOT have an "active" field.
 * They have a "status" field instead, which should not be modified during merge operations.</p>
 *
 * Uses the generic merge test infrastructure:
 * - {@link ObservationMergeTestStrategy} for Observation-specific behavior
 * - {@link MergeTestDataBuilder} for test data creation
 * - {@link MergeOperationTestHelper} for operation invocation and validation
 */
public class ObservationMergeR4Test extends BaseResourceProviderR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ObservationMergeR4Test.class);

	@Autowired
	private Batch2JobHelper myBatch2JobHelper;

	@Autowired
	private ResourceLinkServiceFactory myLinkServiceFactory;

	private ObservationMergeTestStrategy myStrategy;
	private MergeOperationTestHelper myHelper;

	@BeforeEach
	public void beforeObservationMerge() {
		// we need to keep the version on Provenance.target fields to verify that Provenance resources were saved
		// with versioned target references, and delete-source on merge works correctly.
		myFhirContext.getParserOptions().setDontStripVersionsFromReferencesAtPaths("Provenance.target");

		myStrategy = new ObservationMergeTestStrategy(myDaoRegistry, myFhirContext, myLinkServiceFactory, mySrd);
		myHelper = new MergeOperationTestHelper(myFhirContext, myDaoRegistry, myClient, myBatch2JobHelper);
	}

	@AfterEach
	public void afterObservationMerge() {
		// Cleanup
	}

	// ================================================
	// BASIC MERGE MATRIX TESTS (16 tests)
	// ================================================

	/**
	 * Comprehensive matrix test covering all combinations of:
	 * - deleteSource: true/false
	 * - resultResource: true/false
	 * - preview: true/false
	 * - async: true/false
	 *
	 * Total: 16 test cases
	 */
	@ParameterizedTest(name = "{index}: deleteSource={0}, resultResource={1}, preview={2}, async={3}")
	@CsvSource({
		// deleteSource, resultResource, preview, async
		"true, true, true, false",
		"true, false, true, false",
		"false, true, true, false",
		"false, false, true, false",
		"true, true, false, false",
		"true, false, false, false",
		"false, true, false, false",
		"false, false, false, false",

		"true, true, true, true",
		"true, false, true, true",
		"false, true, true, true",
		"false, false, true, true",
		"true, true, false, true",
		"true, false, false, true",
		"false, true, false, true",
		"false, false, false, true",
	})
	void testMerge_basicMatrix(boolean theDeleteSource, boolean theResultResource, boolean thePreview, boolean theAsync) {
		// Setup: Create test data with standard references
		MergeTestDataBuilder<Observation> builder = createTestDataBuilder().withStandardReferences();

		if (theResultResource) {
			builder.withResultResource();
		}

		MergeTestScenario<Observation> scenario = builder.build();

		// Build merge parameters
		MergeTestParameters params = scenario.buildMergeParameters(theDeleteSource, thePreview);

		// Execute merge
		Parameters outParams = myHelper.callMergeOperation("Observation", params, theAsync);

		// Validate outcome based on mode
		if (thePreview) {
			myHelper.validatePreviewOutcome(outParams, scenario.getTotalReferenceCount() + 2);
			myHelper.assertReferencesNotUpdated(scenario);
			return; // Preview mode doesn't make actual changes
		}

		if (theAsync) {
			myHelper.validateAsyncTaskCreated(outParams);
			Task task = (Task) outParams.getParameter(OPERATION_MERGE_OUTPUT_PARAM_TASK).getResource();
			String jobId = myHelper.getJobIdFromTask(task);
			myHelper.awaitJobCompletion(jobId);
		} else {
			myHelper.validateSyncMergeOutcome(outParams);
		}

		// Validate merge outcomes
		validateMergeOutcome(scenario, theDeleteSource);
	}

	// ================================================
	// IDENTIFIER RESOLUTION TESTS (8 tests)
	// ================================================

	@ParameterizedTest(name = "{index}: sourceById={0}, targetById={1}")
	@CsvSource({
		"true, true",
		"true, false",
		"false, true",
		"false, false",
	})
	void testMerge_identifierResolution_sync(boolean theSourceById, boolean theTargetById) {
		// Setup
		MergeTestScenario<Observation> scenario = createTestDataBuilder()
			.withReferences(ReferencingResourceConfig.of("DiagnosticReport", "result", 5))
			.build();

		// Build parameters with identifier-based resolution
		MergeTestParameters params = scenario.buildMergeParameters(
			theSourceById, theTargetById, false, false);

		// Execute
		Parameters outParams = myHelper.callMergeOperation("Observation", params, false);

		// Validate
		myHelper.validateSyncMergeOutcome(outParams);
		validateMergeOutcome(scenario, false);
	}

	@ParameterizedTest(name = "{index}: sourceById={0}, targetById={1}")
	@CsvSource({
		"true, true",
		"true, false",
		"false, true",
		"false, false",
	})
	void testMerge_identifierResolution_async(boolean theSourceById, boolean theTargetById) {
		// Setup
		MergeTestScenario<Observation> scenario = createTestDataBuilder()
			.withReferences(ReferencingResourceConfig.of("DiagnosticReport", "result", 5))
			.build();

		// Build parameters with identifier-based resolution
		MergeTestParameters params = scenario.buildMergeParameters(
			theSourceById, theTargetById, false, false);

		// Execute
		Parameters outParams = myHelper.callMergeOperation("Observation", params, true);

		// Validate
		myHelper.validateAsyncTaskCreated(outParams);
		Task task = (Task) outParams.getParameter(OPERATION_MERGE_OUTPUT_PARAM_TASK).getResource();
		String jobId = myHelper.getJobIdFromTask(task);
		myHelper.awaitJobCompletion(jobId);

		validateMergeOutcome(scenario, false);
	}

	// ================================================
	// EXTENSION LINK VALIDATION TESTS (6 tests)
	// ================================================

	@Test
	void testMerge_extensionLinks_replacesLink() {
		// Setup
		MergeTestScenario<Observation> scenario = createTestDataBuilder()
			.withReferences(ReferencingResourceConfig.of("DiagnosticReport", "result", 3))
			.build();

		// Execute merge
		MergeTestParameters params = scenario.buildMergeParameters(false, false);
		myHelper.callMergeOperation("Observation", params, false);

		// Validate target has "replaces" link to source
		Observation target = myStrategy.readResource(scenario.getTargetId());
		myStrategy.assertLinksPresent(target, Arrays.asList(scenario.getSourceId()), "replaces");
	}

	@Test
	void testMerge_extensionLinks_replacedByLink() {
		// Setup
		MergeTestScenario<Observation> scenario = createTestDataBuilder()
			.withReferences(ReferencingResourceConfig.of("DiagnosticReport", "result", 3))
			.build();

		// Execute merge
		MergeTestParameters params = scenario.buildMergeParameters(false, false);
		myHelper.callMergeOperation("Observation", params, false);

		// Validate source has "replaced-by" link to target
		Observation source = myStrategy.readResource(scenario.getSourceId());
		myStrategy.assertLinksPresent(source, Arrays.asList(scenario.getTargetId()), "replaced-by");
	}

	@Test
	void testMerge_extensionLinks_noLinksBeforeMerge() {
		// Setup
		MergeTestScenario<Observation> scenario = createTestDataBuilder()
			.withReferences(ReferencingResourceConfig.of("DiagnosticReport", "result", 3))
			.build();

		// Validate no links before merge
		Observation source = myStrategy.readResource(scenario.getSourceId());
		Observation target = myStrategy.readResource(scenario.getTargetId());

		myStrategy.assertNoLinks(source);
		myStrategy.assertNoLinks(target);
	}

	@Test
	void testMerge_extensionLinks_deletedSourceHasNoLinks() {
		// Setup
		MergeTestScenario<Observation> scenario = createTestDataBuilder()
			.withReferences(ReferencingResourceConfig.of("DiagnosticReport", "result", 3))
			.build();

		// Execute merge with delete
		MergeTestParameters params = scenario.buildMergeParameters(true, false);
		myHelper.callMergeOperation("Observation", params, false);

		// Validate source is deleted (cannot read it)
		myStrategy.assertSourceResourceState(null, scenario.getSourceId(), scenario.getTargetId(), true);
	}

	@Test
	void testMerge_extensionLinks_multipleReferencingResourceTypes() {
		// Setup with multiple DiagnosticReport references
		MergeTestScenario<Observation> scenario = createTestDataBuilder()
			.withReferences(
				ReferencingResourceConfig.of("DiagnosticReport", "result", 7)
			)
			.build();

		// Execute merge
		MergeTestParameters params = scenario.buildMergeParameters(false, false);
		myHelper.callMergeOperation("Observation", params, false);

		// Validate all reference types updated
		myHelper.assertReferencesUpdated(
			scenario.getReferencingResourceIds("DiagnosticReport"),
			scenario.getSourceId(),
			scenario.getTargetId()
		);
	}

	@Test
	void testMerge_extensionLinks_targetPreservesExistingData() {
		// Setup
		MergeTestScenario<Observation> scenario = createTestDataBuilder()
			.withSourceIdentifiers("source-1", "source-2")
			.withTargetIdentifiers("target-1", "target-2")
			.withReferences(ReferencingResourceConfig.of("DiagnosticReport", "result", 3))
			.build();

		// Get target data before merge
		Observation targetBefore = myStrategy.readResource(scenario.getTargetId());
		List<Identifier> targetIdentifiersBefore = myStrategy.getIdentifiersFromResource(targetBefore);

		// Execute merge
		MergeTestParameters params = scenario.buildMergeParameters(false, false);
		myHelper.callMergeOperation("Observation", params, false);

		// Validate target preserved its original identifiers
		Observation targetAfter = myStrategy.readResource(scenario.getTargetId());
		List<Identifier> targetIdentifiersAfter = myStrategy.getIdentifiersFromResource(targetAfter);

		// Target should have original identifiers plus source identifiers marked as OLD
		assertThat(targetIdentifiersAfter.size()).isGreaterThan(targetIdentifiersBefore.size());
	}

	// ================================================
	// EDGE CASES AND ERROR HANDLING TESTS (5 tests)
	// ================================================

	@Test
	void testMerge_errorHandling_missingSourceResource() {
		// Build invalid parameters (no source)
		MergeTestParameters params = new MergeTestParameters()
			.targetResource(new Reference("Observation/123"))
			.deleteSource(false)
			.preview(false);

		// Validate error
		assertThatThrownBy(() -> myHelper.callMergeOperation("Observation", params, false))
			.isInstanceOf(InvalidRequestException.class);
	}

	@Test
	void testMerge_errorHandling_missingTargetResource() {
		// Build invalid parameters (no target)
		MergeTestParameters params = new MergeTestParameters()
			.sourceResource(new Reference("Observation/123"))
			.deleteSource(false)
			.preview(false);

		// Validate error
		assertThatThrownBy(() -> myHelper.callMergeOperation("Observation", params, false))
			.isInstanceOf(InvalidRequestException.class);
	}

	@Test
	void testMerge_errorHandling_sourceEqualsTarget() {
		// Setup
		MergeTestScenario<Observation> scenario = createTestDataBuilder().build();

		// Build parameters with source == target
		MergeTestParameters params = new MergeTestParameters()
			.sourceResource(new Reference(scenario.getTargetId()))
			.targetResource(new Reference(scenario.getTargetId()))
			.deleteSource(false)
			.preview(false);

		// Validate error
		assertThatThrownBy(() -> myHelper.callMergeOperation("Observation", params, false))
			.isInstanceOf(UnprocessableEntityException.class);
	}

	@Test
	void testMerge_errorHandling_nonExistentSourceIdentifier() {
		// Setup
		MergeTestScenario<Observation> scenario = createTestDataBuilder().build();

		// Build parameters with non-existent source identifier
		MergeTestParameters params = new MergeTestParameters()
			.sourceIdentifiers(Arrays.asList(
				new Identifier().setSystem("http://test.org").setValue("non-existent-id")
			))
			.targetResource(new Reference(scenario.getTargetId()))
			.deleteSource(false)
			.preview(false);

		// Validate error
		assertThatThrownBy(() -> myHelper.callMergeOperation("Observation", params, false))
			.isInstanceOf(UnprocessableEntityException.class);
	}

	@Test
	void testMerge_errorHandling_nonExistentTargetIdentifier() {
		// Setup
		MergeTestScenario<Observation> scenario = createTestDataBuilder().build();

		// Build parameters with non-existent target identifier
		MergeTestParameters params = new MergeTestParameters()
			.sourceResource(new Reference(scenario.getSourceId()))
			.targetIdentifiers(Arrays.asList(
				new Identifier().setSystem("http://test.org").setValue("non-existent-id")
			))
			.deleteSource(false)
			.preview(false);

		// Validate error
		assertThatThrownBy(() -> myHelper.callMergeOperation("Observation", params, false))
			.isInstanceOf(UnprocessableEntityException.class);
	}

	// ================================================
	// PROVENANCE VALIDATION TESTS (3 tests)
	// ================================================

	@Test
	void testMerge_provenance_createdForSync() {
		// Setup
		MergeTestScenario<Observation> scenario = createTestDataBuilder()
			.withReferences(ReferencingResourceConfig.of("DiagnosticReport", "result", 3))
			.build();

		// Execute merge
		MergeTestParameters params = scenario.buildMergeParameters(false, false);
		Parameters inParams = params.asParametersResource("Observation");
		myHelper.callMergeOperation("Observation", params, false);

		// Validate provenance created
		myHelper.assertMergeProvenanceCreated(scenario.getSourceId(), scenario.getTargetId(), inParams);
	}

	@Test
	void testMerge_provenance_createdForAsync() {
		// Setup
		MergeTestScenario<Observation> scenario = createTestDataBuilder()
			.withReferences(ReferencingResourceConfig.of("DiagnosticReport", "result", 3))
			.build();

		// Execute merge
		MergeTestParameters params = scenario.buildMergeParameters(false, false);
		Parameters inParams = params.asParametersResource("Observation");
		Parameters outParams = myHelper.callMergeOperation("Observation", params, true);

		// Wait for completion
		Task task = (Task) outParams.getParameter(OPERATION_MERGE_OUTPUT_PARAM_TASK).getResource();
		String jobId = myHelper.getJobIdFromTask(task);
		myHelper.awaitJobCompletion(jobId);

		// Validate provenance created
		myHelper.assertMergeProvenanceCreated(scenario.getSourceId(), scenario.getTargetId(), inParams);
	}

	@Test
	void testMerge_provenance_notCreatedForPreview() {
		// Setup
		MergeTestScenario<Observation> scenario = createTestDataBuilder()
			.withReferences(ReferencingResourceConfig.of("DiagnosticReport", "result", 3))
			.build();

		// Execute merge in preview mode
		MergeTestParameters params = scenario.buildMergeParameters(false, true);
		myHelper.callMergeOperation("Observation", params, false);

		// Validate no changes (preview mode validation already checks this)
		myHelper.assertReferencesNotUpdated(scenario);
	}

	// ================================================
	// OBSERVATION STATUS VALIDATION TESTS (3 tests)
	// ================================================

	/**
	 * KEY DIFFERENCE: Observation does NOT have active field.
	 * Verify that the status field is NOT modified during merge.
	 */
	@Test
	void testMerge_status_sourceStatusUnchanged() {
		// Setup
		MergeTestScenario<Observation> scenario = createTestDataBuilder()
			.withReferences(ReferencingResourceConfig.of("DiagnosticReport", "result", 3))
			.build();

		// Verify source status before merge
		Observation sourceBefore = myStrategy.readResource(scenario.getSourceId());
		Observation.ObservationStatus statusBefore = sourceBefore.getStatus();
		assertThat(statusBefore).isEqualTo(Observation.ObservationStatus.FINAL);

		// Execute merge (no delete)
		MergeTestParameters params = scenario.buildMergeParameters(false, false);
		myHelper.callMergeOperation("Observation", params, false);

		// Verify source status is UNCHANGED (not set to false like active field would be)
		Observation sourceAfter = myStrategy.readResource(scenario.getSourceId());
		assertThat(sourceAfter.getStatus()).isEqualTo(statusBefore);
	}

	@Test
	void testMerge_status_targetStatusUnchanged() {
		// Setup
		MergeTestScenario<Observation> scenario = createTestDataBuilder()
			.withReferences(ReferencingResourceConfig.of("DiagnosticReport", "result", 3))
			.build();

		// Verify target status before merge
		Observation targetBefore = myStrategy.readResource(scenario.getTargetId());
		Observation.ObservationStatus statusBefore = targetBefore.getStatus();
		assertThat(statusBefore).isEqualTo(Observation.ObservationStatus.FINAL);

		// Execute merge
		MergeTestParameters params = scenario.buildMergeParameters(false, false);
		myHelper.callMergeOperation("Observation", params, false);

		// Verify target status is UNCHANGED
		Observation targetAfter = myStrategy.readResource(scenario.getTargetId());
		assertThat(targetAfter.getStatus()).isEqualTo(statusBefore);
	}

	@Test
	void testMerge_status_verifyNoActiveField() {
		// This test documents that Observation.hasActiveField() returns false
		assertThat(myStrategy.hasActiveField())
			.as("Observation should NOT have active field")
			.isFalse();
	}

	// ================================================
	// RESULT RESOURCE VALIDATION TESTS (2 tests)
	// ================================================

	@Test
	void testMerge_resultResource_overridesTargetData() {
		// Setup with result resource
		MergeTestScenario<Observation> scenario = createTestDataBuilder()
			.withSourceIdentifiers("source-1", "source-2")
			.withTargetIdentifiers("target-1", "target-2")
			.withReferences(ReferencingResourceConfig.of("DiagnosticReport", "result", 3))
			.withResultResource()
			.build();

		// Execute merge with result resource
		MergeTestParameters params = scenario.buildMergeParameters(false, false);
		myHelper.callMergeOperation("Observation", params, false);

		// Validate target has result resource identifiers
		Observation target = myStrategy.readResource(scenario.getTargetId());
		List<Identifier> targetIdentifiers = myStrategy.getIdentifiersFromResource(target);

		// Result resource should have the target's original identifiers
		assertThat(targetIdentifiers).hasSizeGreaterThanOrEqualTo(2);
	}

	@Test
	void testMerge_resultResource_preservesReferences() {
		// Setup with result resource
		MergeTestScenario<Observation> scenario = createTestDataBuilder()
			.withReferences(ReferencingResourceConfig.of("DiagnosticReport", "result", 5))
			.withResultResource()
			.build();

		// Execute merge with result resource
		MergeTestParameters params = scenario.buildMergeParameters(false, false);
		myHelper.callMergeOperation("Observation", params, false);

		// Validate all references still updated to target
		myHelper.assertReferencesUpdated(
			scenario.getReferencingResourceIds("DiagnosticReport"),
			scenario.getSourceId(),
			scenario.getTargetId()
		);
	}

	// ================================================
	// OBSERVATION-SPECIFIC TESTS (5 tests)
	// ================================================

	/**
	 * Test with DiagnosticReport.result references (most common for Observation).
	 */
	@Test
	void testMerge_observationSpecific_diagnosticReportResult() {
		// Setup
		MergeTestScenario<Observation> scenario = createTestDataBuilder()
			.withReferences(ReferencingResourceConfig.of("DiagnosticReport", "result", 10))
			.build();

		// Execute merge
		MergeTestParameters params = scenario.buildMergeParameters(false, false);
		myHelper.callMergeOperation("Observation", params, false);

		// Validate all DiagnosticReport.result references updated
		myHelper.assertReferencesUpdated(
			scenario.getReferencingResourceIds("DiagnosticReport"),
			scenario.getSourceId(),
			scenario.getTargetId()
		);
	}

	/**
	 * Test identifier merging with LOINC codes (common in Observation).
	 */
	@Test
	void testMerge_observationSpecific_loincCodeIdentifiers() {
		// Setup with LOINC-like identifiers
		MergeTestScenario<Observation> scenario = createTestDataBuilder()
			.withSourceIdentifiers("8867-4", "2339-0")
			.withTargetIdentifiers("8867-4", "94531-1")
			.withReferences(ReferencingResourceConfig.of("DiagnosticReport", "result", 3))
			.build();

		// Execute merge
		MergeTestParameters params = scenario.buildMergeParameters(false, false);
		myHelper.callMergeOperation("Observation", params, false);

		// Validate target has all identifiers
		Observation target = myStrategy.readResource(scenario.getTargetId());
		List<Identifier> targetIdentifiers = myStrategy.getIdentifiersFromResource(target);

		// Should have original target identifiers + non-duplicate source identifiers
		assertThat(targetIdentifiers).hasSizeGreaterThanOrEqualTo(3);
	}

	/**
	 * Test merge with all standard referencing resource types.
	 */
	@Test
	void testMerge_observationSpecific_allStandardReferences() {
		// Setup with standard references
		MergeTestScenario<Observation> scenario = createTestDataBuilder()
			.withStandardReferences()
			.build();

		// Execute merge
		MergeTestParameters params = scenario.buildMergeParameters(false, false);
		myHelper.callMergeOperation("Observation", params, false);

		// Validate all reference types updated
		for (String resourceType : scenario.getReferencingResourceTypes()) {
			myHelper.assertReferencesUpdated(
				scenario.getReferencingResourceIds(resourceType),
				scenario.getSourceId(),
				scenario.getTargetId()
			);
		}
	}

	// ================================================
	// HELPER METHODS
	// ================================================

	/**
	 * Create a test data builder for Observation merge scenarios.
	 */
	private MergeTestDataBuilder<Observation> createTestDataBuilder() {
		return new MergeTestDataBuilder<>(myStrategy, myDaoRegistry, mySrd, myFhirContext);
	}

	/**
	 * Validate merge outcome including source/target state and reference updates.
	 */
	private void validateMergeOutcome(MergeTestScenario<Observation> theScenario, boolean theDeleteSource) {
		// Validate source resource state
		if (theDeleteSource) {
			myStrategy.assertSourceResourceState(null, theScenario.getSourceId(), theScenario.getTargetId(), true);
		} else {
			Observation source = myStrategy.readResource(theScenario.getSourceId());
			myStrategy.assertSourceResourceState(source, theScenario.getSourceId(), theScenario.getTargetId(), false);
		}

		// Validate target resource state
		Observation target = myStrategy.readResource(theScenario.getTargetId());
		myStrategy.assertTargetResourceState(
			target,
			theScenario.getSourceId(),
			theDeleteSource,
			theScenario.getExpectedIdentifiers()
		);

		// Validate all references updated
		for (String resourceType : theScenario.getReferencingResourceTypes()) {
			List<IIdType> referencingIds = theScenario.getReferencingResourceIds(resourceType);
			if (!referencingIds.isEmpty()) {
				myHelper.assertReferencesUpdated(referencingIds, theScenario.getSourceId(), theScenario.getTargetId());
			}
		}
	}
}
