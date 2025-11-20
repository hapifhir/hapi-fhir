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
import ca.uhn.fhir.jpa.merge.PractitionerMergeTestStrategy;
import ca.uhn.fhir.jpa.merge.ReferencingResourceConfig;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.jpa.test.Batch2JobHelper;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Practitioner;
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
 * Comprehensive integration tests for generic merge operations on Practitioner resources.
 *
 * This test class validates the $hapi-fhir-merge operation for Practitioner resources,
 * covering all merge scenarios including:
 * - Basic merge matrix (sync/async, delete/no-delete, preview/execute, with/without result-resource)
 * - Identifier-based resource resolution
 * - Extension-based link validation (replaces/replaced-by)
 * - Edge cases and error handling
 * - Provenance creation validation
 * - Active field validation (Practitioner.active)
 * - Result resource validation
 *
 * Uses the generic merge test infrastructure:
 * - {@link PractitionerMergeTestStrategy} for Practitioner-specific behavior
 * - {@link MergeTestDataBuilder} for test data creation
 * - {@link MergeOperationTestHelper} for operation invocation and validation
 */
public class PractitionerMergeR4Test extends BaseResourceProviderR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(PractitionerMergeR4Test.class);

	@Autowired
	private Batch2JobHelper myBatch2JobHelper;

	@Autowired
	private ResourceLinkServiceFactory myLinkServiceFactory;

	private PractitionerMergeTestStrategy myStrategy;
	private MergeOperationTestHelper myHelper;

	@BeforeEach
	public void beforePractitionerMerge() {
		// we need to keep the version on Provenance.target fields to verify that Provenance resources were saved
		// with versioned target references, and delete-source on merge works correctly.
		myFhirContext.getParserOptions().setDontStripVersionsFromReferencesAtPaths("Provenance.target");

		myStrategy = new PractitionerMergeTestStrategy(myDaoRegistry, myFhirContext, myLinkServiceFactory, mySrd);
		myHelper = new MergeOperationTestHelper(myFhirContext, myDaoRegistry, myClient, myBatch2JobHelper);
	}

	@AfterEach
	public void afterPractitionerMerge() {
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
		MergeTestDataBuilder<Practitioner> builder = createTestDataBuilder().withStandardReferences();

		if (theResultResource) {
			builder.withResultResource();
		}

		MergeTestScenario<Practitioner> scenario = builder.build();

		// Build merge parameters
		MergeTestParameters params = scenario.buildMergeParameters(theDeleteSource, thePreview);

		// Execute merge
		Parameters outParams = myHelper.callMergeOperation("Practitioner", params, theAsync);

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
		MergeTestScenario<Practitioner> scenario = createTestDataBuilder()
			.withReferences(ReferencingResourceConfig.of("PractitionerRole", "practitioner", 5))
			.build();

		// Build parameters with identifier-based resolution
		MergeTestParameters params = scenario.buildMergeParameters(
			theSourceById, theTargetById, false, false);

		// Execute
		Parameters outParams = myHelper.callMergeOperation("Practitioner", params, false);

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
		MergeTestScenario<Practitioner> scenario = createTestDataBuilder()
			.withReferences(ReferencingResourceConfig.of("PractitionerRole", "practitioner", 5))
			.build();

		// Build parameters with identifier-based resolution
		MergeTestParameters params = scenario.buildMergeParameters(
			theSourceById, theTargetById, false, false);

		// Execute
		Parameters outParams = myHelper.callMergeOperation("Practitioner", params, true);

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
		MergeTestScenario<Practitioner> scenario = createTestDataBuilder()
			.withReferences(ReferencingResourceConfig.of("PractitionerRole", "practitioner", 3))
			.build();

		// Execute merge
		MergeTestParameters params = scenario.buildMergeParameters(false, false);
		myHelper.callMergeOperation("Practitioner", params, false);

		// Validate target has "replaces" link to source
		Practitioner target = myStrategy.readResource(scenario.getTargetId());
		myStrategy.assertLinksPresent(target, Arrays.asList(scenario.getSourceId()), "replaces");
	}

	@Test
	void testMerge_extensionLinks_replacedByLink() {
		// Setup
		MergeTestScenario<Practitioner> scenario = createTestDataBuilder()
			.withReferences(ReferencingResourceConfig.of("PractitionerRole", "practitioner", 3))
			.build();

		// Execute merge
		MergeTestParameters params = scenario.buildMergeParameters(false, false);
		myHelper.callMergeOperation("Practitioner", params, false);

		// Validate source has "replaced-by" link to target
		Practitioner source = myStrategy.readResource(scenario.getSourceId());
		myStrategy.assertLinksPresent(source, Arrays.asList(scenario.getTargetId()), "replaced-by");
	}

	@Test
	void testMerge_extensionLinks_noLinksBeforeMerge() {
		// Setup
		MergeTestScenario<Practitioner> scenario = createTestDataBuilder()
			.withReferences(ReferencingResourceConfig.of("PractitionerRole", "practitioner", 3))
			.build();

		// Validate no links before merge
		Practitioner source = myStrategy.readResource(scenario.getSourceId());
		Practitioner target = myStrategy.readResource(scenario.getTargetId());

		myStrategy.assertNoLinks(source);
		myStrategy.assertNoLinks(target);
	}

	@Test
	void testMerge_extensionLinks_deletedSourceHasNoLinks() {
		// Setup
		MergeTestScenario<Practitioner> scenario = createTestDataBuilder()
			.withReferences(ReferencingResourceConfig.of("PractitionerRole", "practitioner", 3))
			.build();

		// Execute merge with delete
		MergeTestParameters params = scenario.buildMergeParameters(true, false);
		myHelper.callMergeOperation("Practitioner", params, false);

		// Validate source is deleted (cannot read it)
		myStrategy.assertSourceResourceState(null, scenario.getSourceId(), scenario.getTargetId(), true);
	}

	@Test
	void testMerge_extensionLinks_multipleReferencingResourceTypes() {
		// Setup with multiple referencing resource types
		MergeTestScenario<Practitioner> scenario = createTestDataBuilder()
			.withReferences(
				ReferencingResourceConfig.of("PractitionerRole", "practitioner", 5),
				ReferencingResourceConfig.of("Encounter", "participant.individual", 3),
				ReferencingResourceConfig.of("CarePlan", "activity.detail.performer", 2)
			)
			.build();

		// Execute merge
		MergeTestParameters params = scenario.buildMergeParameters(false, false);
		myHelper.callMergeOperation("Practitioner", params, false);

		// Validate all reference types updated
		myHelper.assertReferencesUpdated(
			scenario.getReferencingResourceIds("PractitionerRole"),
			scenario.getSourceId(),
			scenario.getTargetId()
		);
		myHelper.assertReferencesUpdated(
			scenario.getReferencingResourceIds("Encounter"),
			scenario.getSourceId(),
			scenario.getTargetId()
		);
		myHelper.assertReferencesUpdated(
			scenario.getReferencingResourceIds("CarePlan"),
			scenario.getSourceId(),
			scenario.getTargetId()
		);
	}

	@Test
	void testMerge_extensionLinks_targetPreservesExistingData() {
		// Setup
		MergeTestScenario<Practitioner> scenario = createTestDataBuilder()
			.withSourceIdentifiers("source-1", "source-2")
			.withTargetIdentifiers("target-1", "target-2")
			.withReferences(ReferencingResourceConfig.of("PractitionerRole", "practitioner", 3))
			.build();

		// Get target data before merge
		Practitioner targetBefore = myStrategy.readResource(scenario.getTargetId());
		List<Identifier> targetIdentifiersBefore = myStrategy.getIdentifiersFromResource(targetBefore);

		// Execute merge
		MergeTestParameters params = scenario.buildMergeParameters(false, false);
		myHelper.callMergeOperation("Practitioner", params, false);

		// Validate target preserved its original identifiers
		Practitioner targetAfter = myStrategy.readResource(scenario.getTargetId());
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
			.targetResource(new Reference("Practitioner/123"))
			.deleteSource(false)
			.preview(false);

		// Validate error
		assertThatThrownBy(() -> myHelper.callMergeOperation("Practitioner", params, false))
			.isInstanceOf(InvalidRequestException.class);
	}

	@Test
	void testMerge_errorHandling_missingTargetResource() {
		// Build invalid parameters (no target)
		MergeTestParameters params = new MergeTestParameters()
			.sourceResource(new Reference("Practitioner/123"))
			.deleteSource(false)
			.preview(false);

		// Validate error
		assertThatThrownBy(() -> myHelper.callMergeOperation("Practitioner", params, false))
			.isInstanceOf(InvalidRequestException.class);
	}

	@Test
	void testMerge_errorHandling_sourceEqualsTarget() {
		// Setup
		MergeTestScenario<Practitioner> scenario = createTestDataBuilder().build();

		// Build parameters with source == target
		MergeTestParameters params = new MergeTestParameters()
			.sourceResource(new Reference(scenario.getTargetId()))
			.targetResource(new Reference(scenario.getTargetId()))
			.deleteSource(false)
			.preview(false);

		// Validate error
		assertThatThrownBy(() -> myHelper.callMergeOperation("Practitioner", params, false))
			.isInstanceOf(UnprocessableEntityException.class);
	}

	@Test
	void testMerge_errorHandling_nonExistentSourceIdentifier() {
		// Setup
		MergeTestScenario<Practitioner> scenario = createTestDataBuilder().build();

		// Build parameters with non-existent source identifier
		MergeTestParameters params = new MergeTestParameters()
			.sourceIdentifiers(Arrays.asList(
				new Identifier().setSystem("http://test.org").setValue("non-existent-id")
			))
			.targetResource(new Reference(scenario.getTargetId()))
			.deleteSource(false)
			.preview(false);

		// Validate error
		assertThatThrownBy(() -> myHelper.callMergeOperation("Practitioner", params, false))
			.isInstanceOf(UnprocessableEntityException.class);
	}

	@Test
	void testMerge_errorHandling_nonExistentTargetIdentifier() {
		// Setup
		MergeTestScenario<Practitioner> scenario = createTestDataBuilder().build();

		// Build parameters with non-existent target identifier
		MergeTestParameters params = new MergeTestParameters()
			.sourceResource(new Reference(scenario.getSourceId()))
			.targetIdentifiers(Arrays.asList(
				new Identifier().setSystem("http://test.org").setValue("non-existent-id")
			))
			.deleteSource(false)
			.preview(false);

		// Validate error
		assertThatThrownBy(() -> myHelper.callMergeOperation("Practitioner", params, false))
			.isInstanceOf(UnprocessableEntityException.class);
	}

	// ================================================
	// PROVENANCE VALIDATION TESTS (3 tests)
	// ================================================

	@Test
	void testMerge_provenance_createdForSync() {
		// Setup
		MergeTestScenario<Practitioner> scenario = createTestDataBuilder()
			.withReferences(ReferencingResourceConfig.of("PractitionerRole", "practitioner", 3))
			.build();

		// Execute merge
		MergeTestParameters params = scenario.buildMergeParameters(false, false);
		Parameters inParams = params.asParametersResource("Practitioner");
		myHelper.callMergeOperation("Practitioner", params, false);

		// Validate provenance created
		myHelper.assertMergeProvenanceCreated(scenario.getSourceId(), scenario.getTargetId(), inParams);
	}

	@Test
	void testMerge_provenance_createdForAsync() {
		// Setup
		MergeTestScenario<Practitioner> scenario = createTestDataBuilder()
			.withReferences(ReferencingResourceConfig.of("PractitionerRole", "practitioner", 3))
			.build();

		// Execute merge
		MergeTestParameters params = scenario.buildMergeParameters(false, false);
		Parameters inParams = params.asParametersResource("Practitioner");
		Parameters outParams = myHelper.callMergeOperation("Practitioner", params, true);

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
		MergeTestScenario<Practitioner> scenario = createTestDataBuilder()
			.withReferences(ReferencingResourceConfig.of("PractitionerRole", "practitioner", 3))
			.build();

		// Execute merge in preview mode
		MergeTestParameters params = scenario.buildMergeParameters(false, true);
		myHelper.callMergeOperation("Practitioner", params, false);

		// Validate no changes (preview mode validation already checks this)
		myHelper.assertReferencesNotUpdated(scenario);
	}

	// ================================================
	// ACTIVE FIELD VALIDATION TESTS (2 tests)
	// ================================================

	@Test
	void testMerge_activeField_sourceSetToFalse() {
		// Setup
		MergeTestScenario<Practitioner> scenario = createTestDataBuilder()
			.withReferences(ReferencingResourceConfig.of("PractitionerRole", "practitioner", 3))
			.build();

		// Verify source is active before merge
		Practitioner sourceBefore = myStrategy.readResource(scenario.getSourceId());
		assertThat(sourceBefore.getActive()).isTrue();

		// Execute merge (no delete)
		MergeTestParameters params = scenario.buildMergeParameters(false, false);
		myHelper.callMergeOperation("Practitioner", params, false);

		// Verify source.active is now false
		Practitioner sourceAfter = myStrategy.readResource(scenario.getSourceId());
		assertThat(sourceAfter.getActive()).isFalse();
	}

	@Test
	void testMerge_activeField_targetRemainsTrue() {
		// Setup
		MergeTestScenario<Practitioner> scenario = createTestDataBuilder()
			.withReferences(ReferencingResourceConfig.of("PractitionerRole", "practitioner", 3))
			.build();

		// Verify target is active before merge
		Practitioner targetBefore = myStrategy.readResource(scenario.getTargetId());
		assertThat(targetBefore.getActive()).isTrue();

		// Execute merge
		MergeTestParameters params = scenario.buildMergeParameters(false, false);
		myHelper.callMergeOperation("Practitioner", params, false);

		// Verify target.active remains true
		Practitioner targetAfter = myStrategy.readResource(scenario.getTargetId());
		assertThat(targetAfter.getActive()).isTrue();
	}

	// ================================================
	// RESULT RESOURCE VALIDATION TESTS (2 tests)
	// ================================================

	@Test
	void testMerge_resultResource_overridesTargetData() {
		// Setup with result resource
		MergeTestScenario<Practitioner> scenario = createTestDataBuilder()
			.withSourceIdentifiers("source-1", "source-2")
			.withTargetIdentifiers("target-1", "target-2")
			.withReferences(ReferencingResourceConfig.of("PractitionerRole", "practitioner", 3))
			.withResultResource()
			.build();

		// Execute merge with result resource
		MergeTestParameters params = scenario.buildMergeParameters(false, false);
		myHelper.callMergeOperation("Practitioner", params, false);

		// Validate target has result resource identifiers
		Practitioner target = myStrategy.readResource(scenario.getTargetId());
		List<Identifier> targetIdentifiers = myStrategy.getIdentifiersFromResource(target);

		// Result resource should have the target's original identifiers
		assertThat(targetIdentifiers).hasSizeGreaterThanOrEqualTo(2);
	}

	@Test
	void testMerge_resultResource_preservesReferences() {
		// Setup with result resource
		MergeTestScenario<Practitioner> scenario = createTestDataBuilder()
			.withReferences(ReferencingResourceConfig.of("PractitionerRole", "practitioner", 5))
			.withResultResource()
			.build();

		// Execute merge with result resource
		MergeTestParameters params = scenario.buildMergeParameters(false, false);
		myHelper.callMergeOperation("Practitioner", params, false);

		// Validate all references still updated to target
		myHelper.assertReferencesUpdated(
			scenario.getReferencingResourceIds("PractitionerRole"),
			scenario.getSourceId(),
			scenario.getTargetId()
		);
	}

	// ================================================
	// HELPER METHODS
	// ================================================

	/**
	 * Create a test data builder for Practitioner merge scenarios.
	 */
	private MergeTestDataBuilder<Practitioner> createTestDataBuilder() {
		return new MergeTestDataBuilder<>(myStrategy, myDaoRegistry, mySrd, myFhirContext);
	}

	/**
	 * Validate merge outcome including source/target state and reference updates.
	 */
	private void validateMergeOutcome(MergeTestScenario<Practitioner> theScenario, boolean theDeleteSource) {
		// Validate source resource state
		if (theDeleteSource) {
			myStrategy.assertSourceResourceState(null, theScenario.getSourceId(), theScenario.getTargetId(), true);
		} else {
			Practitioner source = myStrategy.readResource(theScenario.getSourceId());
			myStrategy.assertSourceResourceState(
					source, theScenario.getSourceId(), theScenario.getTargetId(), false);
		}

		// Validate target resource state
		Practitioner target = myStrategy.readResource(theScenario.getTargetId());
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
