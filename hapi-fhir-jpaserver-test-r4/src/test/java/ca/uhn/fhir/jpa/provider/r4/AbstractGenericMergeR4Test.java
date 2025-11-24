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
import ca.uhn.fhir.jpa.merge.AbstractMergeTestScenario;
import ca.uhn.fhir.jpa.merge.MergeOperationTestHelper;
import ca.uhn.fhir.jpa.merge.MergeTestParameters;
import ca.uhn.fhir.jpa.merge.ReferencingTestResourceType;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.jpa.test.Batch2JobHelper;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Abstract base class for generic merge operation tests.
 *
 * <p>Provides common test methods for all resource types. Subclasses only need to:
 * <ol>
 *   <li>Define the resource type via {@link #createScenario()}</li>
 *   <li>Define the resource type name via {@link #getResourceTypeName()}</li>
 *   <li>Add resource-specific tests (optional)</li>
 * </ol>
 *
 * <p>This class contains comprehensive tests covering:
 * - Basic merge matrix (sync/async, delete/no-delete, preview/execute, with/without result-resource)
 * - Identifier-based resource resolution
 * - Extension-based link validation (replaces/replaced-by)
 * - Edge cases and error handling
 * - Provenance creation validation
 * - Active field validation (if resource has active field)
 * - Result resource validation
 *
 * @param <T> the FHIR resource type being tested
 */
public abstract class AbstractGenericMergeR4Test<T extends IBaseResource> extends BaseResourceProviderR4Test {

	private static final Logger ourLog = LoggerFactory.getLogger(AbstractGenericMergeR4Test.class);

	@Autowired
	protected Batch2JobHelper myBatch2JobHelper;

	@Autowired
	protected ResourceLinkServiceFactory myLinkServiceFactory;

	protected MergeOperationTestHelper myHelper;

	/**
	 * Template method for subclasses to create their test scenario.
	 */
	@Nonnull
	protected abstract AbstractMergeTestScenario<T> createScenario();

	/**
	 * Template method for subclasses to provide their resource type name.
	 */
	@Nonnull
	protected abstract String getResourceTypeName();

	@BeforeEach
	public void beforeGenericMerge() {
		// we need to keep the version on Provenance.target fields to verify that Provenance resources were saved
		// with versioned target references, and delete-source on merge works correctly.
		myFhirContext.getParserOptions().setDontStripVersionsFromReferencesAtPaths("Provenance.target");

		myHelper = new MergeOperationTestHelper(myClient, myBatch2JobHelper);
	}

	@AfterEach
	public void afterGenericMerge() {
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
	protected void testMerge_basicMatrix(
			boolean theDeleteSource, boolean theResultResource, boolean thePreview, boolean theAsync) {
		// Setup: Create and configure scenario with standard references
		AbstractMergeTestScenario<T> scenario = createScenario().withMultipleReferencingResources();

		if (theResultResource) {
			scenario.withResultResource();
		}

		scenario.createTestData();

		// Build merge parameters
		MergeTestParameters params = scenario.buildMergeOperationParameters(theDeleteSource, thePreview);

		// Execute merge
		Parameters outParams = myHelper.callMergeOperation(getResourceTypeName(), params, theAsync);

		// Validate outcome based on mode
		if (thePreview) {
			scenario.validatePreviewOutcome(outParams);
			scenario.assertReferencesNotUpdated();
			return; // Preview mode doesn't make actual changes
		}

		if (theAsync) {
			scenario.validateAsyncTaskCreated(outParams);
			Task task = (Task) outParams.getParameter("task").getResource();
			String jobId = myHelper.getJobIdFromTask(task);
			myHelper.awaitJobCompletion(jobId);
		} else {
			scenario.validateSyncMergeOutcome(outParams);
		}

		// Validate merge outcomes
		validateMergeOutcome(scenario, theDeleteSource);
	}

	// ================================================
	// IDENTIFIER RESOLUTION TESTS (8 tests)
	// ================================================

	@ParameterizedTest(name = "{index}: sourceById={0}, targetById={1}")
	@CsvSource({"true, true", "true, false", "false, true", "false, false"})
	protected void testMerge_identifierResolution_sync(boolean theSourceById, boolean theTargetById) {
		// Setup
		AbstractMergeTestScenario<T> scenario = createScenario();
		scenario.withOneReferencingResource();
		scenario.createTestData();

		// Build parameters with identifier-based resolution
		MergeTestParameters params = scenario.buildMergeOperationParameters(theSourceById, theTargetById, false, false);

		// Execute
		Parameters outParams = myHelper.callMergeOperation(getResourceTypeName(), params, false);

		// Validate
		scenario.validateSyncMergeOutcome(outParams);
		validateMergeOutcome(scenario, false);
	}

	@ParameterizedTest(name = "{index}: sourceById={0}, targetById={1}")
	@CsvSource({"true, true", "true, false", "false, true", "false, false"})
	protected void testMerge_identifierResolution_async(boolean theSourceById, boolean theTargetById) {
		// Setup
		AbstractMergeTestScenario<T> scenario = createScenario();
		scenario.withOneReferencingResource();
		scenario.createTestData();

		// Build parameters with identifier-based resolution
		MergeTestParameters params = scenario.buildMergeOperationParameters(theSourceById, theTargetById, false, false);

		// Execute
		Parameters outParams = myHelper.callMergeOperation(getResourceTypeName(), params, true);

		// Validate
		scenario.validateAsyncTaskCreated(outParams);
		Task task = (Task) outParams.getParameter("task").getResource();
		String jobId = myHelper.getJobIdFromTask(task);
		myHelper.awaitJobCompletion(jobId);

		validateMergeOutcome(scenario, false);
	}

	// ================================================
	// EXTENSION LINK VALIDATION TESTS (6 tests)
	// ================================================

	@Test
	protected void testMerge_extensionLinks_replacesLink() {
		// Setup
		AbstractMergeTestScenario<T> scenario = createScenario();
		scenario.withOneReferencingResource();
		scenario.createTestData();

		// Execute merge
		MergeTestParameters params = scenario.buildMergeOperationParameters(false, false);
		myHelper.callMergeOperation(getResourceTypeName(), params, false);

		// Validate target has "replaces" link to source
		T target = scenario.readResource(scenario.getTargetId());
		scenario.assertLinksPresent(target, Arrays.asList(scenario.getSourceId()), "replaces");
	}

	@Test
	protected void testMerge_extensionLinks_replacedByLink() {
		// Setup
		AbstractMergeTestScenario<T> scenario = createScenario();
		scenario.withOneReferencingResource();
		scenario.createTestData();

		// Execute merge
		MergeTestParameters params = scenario.buildMergeOperationParameters(false, false);
		myHelper.callMergeOperation(getResourceTypeName(), params, false);

		// Validate source has "replaced-by" link to target
		T source = scenario.readResource(scenario.getSourceId());
		scenario.assertLinksPresent(source, Arrays.asList(scenario.getTargetId()), "replaced-by");
	}

	@Test
	protected void testMerge_extensionLinks_noLinksBeforeMerge() {
		// Setup
		AbstractMergeTestScenario<T> scenario = createScenario();
		scenario.withOneReferencingResource();
		scenario.createTestData();

		// Validate no links before merge
		T source = scenario.readResource(scenario.getSourceId());
		T target = scenario.readResource(scenario.getTargetId());

		scenario.assertNoLinks(source);
		scenario.assertNoLinks(target);
	}

	@Test
	protected void testMerge_extensionLinks_deletedSourceHasNoLinks() {
		// Setup
		AbstractMergeTestScenario<T> scenario = createScenario();
		scenario.withOneReferencingResource();
		scenario.createTestData();

		// Execute merge with delete
		MergeTestParameters params = scenario.buildMergeOperationParameters(true, false);
		myHelper.callMergeOperation(getResourceTypeName(), params, false);

		// Validate source is deleted (cannot read it)
		scenario.assertSourceResourceState(null, scenario.getSourceId(), scenario.getTargetId(), true);
	}

	@Test
	protected void testMerge_extensionLinks_multipleReferencingResourceTypes() {
		// Setup with all standard referencing resource types
		AbstractMergeTestScenario<T> scenario = createScenario();
		scenario.withMultipleReferencingResources();
		scenario.createTestData();

		// Execute merge
		MergeTestParameters params = scenario.buildMergeOperationParameters(false, false);
		myHelper.callMergeOperation(getResourceTypeName(), params, false);

		// Validate all reference types updated
		for (String resourceType : scenario.getReferencingResourceTypes()) {
			scenario.assertReferencesUpdated(resourceType);
		}
	}

	@Test
	protected void testMerge_extensionLinks_targetPreservesExistingData() {
		// Setup
		AbstractMergeTestScenario<T> scenario = createScenario();
		scenario.withSourceIdentifiers("source-1", "source-2");
		scenario.withTargetIdentifiers("target-1", "target-2");
		scenario.withOneReferencingResource();
		scenario.createTestData();

		// Get target data before merge
		T targetBefore = scenario.readResource(scenario.getTargetId());
		List<Identifier> targetIdentifiersBefore = scenario.getIdentifiersFromResource(targetBefore);

		// Execute merge
		MergeTestParameters params = scenario.buildMergeOperationParameters(false, false);
		myHelper.callMergeOperation(getResourceTypeName(), params, false);

		// Validate target preserved its original identifiers
		T targetAfter = scenario.readResource(scenario.getTargetId());
		List<Identifier> targetIdentifiersAfter = scenario.getIdentifiersFromResource(targetAfter);

		// Target should have original identifiers plus source identifiers marked as OLD
		assertThat(targetIdentifiersAfter.size()).isGreaterThan(targetIdentifiersBefore.size());
	}

	// ================================================
	// EDGE CASES AND ERROR HANDLING TESTS (5 tests)
	// ================================================

	@Test
	protected void testMerge_errorHandling_missingSourceResource() {
		// Build invalid parameters (no source)
		MergeTestParameters params = new MergeTestParameters()
				.targetResource(new Reference(getResourceTypeName() + "/123"))
				.deleteSource(false)
				.preview(false);

		// Validate error
		assertThatThrownBy(() -> myHelper.callMergeOperation(getResourceTypeName(), params, false))
				.isInstanceOf(InvalidRequestException.class);
	}

	@Test
	protected void testMerge_errorHandling_missingTargetResource() {
		// Build invalid parameters (no target)
		MergeTestParameters params = new MergeTestParameters()
				.sourceResource(new Reference(getResourceTypeName() + "/123"))
				.deleteSource(false)
				.preview(false);

		// Validate error
		assertThatThrownBy(() -> myHelper.callMergeOperation(getResourceTypeName(), params, false))
				.isInstanceOf(InvalidRequestException.class);
	}

	@Test
	protected void testMerge_errorHandling_sourceEqualsTarget() {
		// Setup
		AbstractMergeTestScenario<T> scenario = createScenario();
		scenario.createTestData();

		// Build parameters with source == target
		MergeTestParameters params = new MergeTestParameters()
				.sourceResource(new Reference(scenario.getTargetId()))
				.targetResource(new Reference(scenario.getTargetId()))
				.deleteSource(false)
				.preview(false);

		// Validate error
		assertThatThrownBy(() -> myHelper.callMergeOperation(getResourceTypeName(), params, false))
				.isInstanceOf(UnprocessableEntityException.class);
	}

	@Test
	protected void testMerge_errorHandling_nonExistentSourceIdentifier() {
		// Setup
		AbstractMergeTestScenario<T> scenario = createScenario();
		scenario.createTestData();

		// Build parameters with non-existent source identifier
		MergeTestParameters params = new MergeTestParameters()
				.sourceIdentifiers(
						Arrays.asList(new Identifier().setSystem("http://test.org").setValue("non-existent-id")))
				.targetResource(new Reference(scenario.getTargetId()))
				.deleteSource(false)
				.preview(false);

		// Validate error
		assertThatThrownBy(() -> myHelper.callMergeOperation(getResourceTypeName(), params, false))
				.isInstanceOf(UnprocessableEntityException.class);
	}

	@Test
	protected void testMerge_errorHandling_nonExistentTargetIdentifier() {
		// Setup
		AbstractMergeTestScenario<T> scenario = createScenario();
		scenario.createTestData();

		// Build parameters with non-existent target identifier
		MergeTestParameters params = new MergeTestParameters()
				.sourceResource(new Reference(scenario.getSourceId()))
				.targetIdentifiers(
						Arrays.asList(new Identifier().setSystem("http://test.org").setValue("non-existent-id")))
				.deleteSource(false)
				.preview(false);

		// Validate error
		assertThatThrownBy(() -> myHelper.callMergeOperation(getResourceTypeName(), params, false))
				.isInstanceOf(UnprocessableEntityException.class);
	}

	// ================================================
	// PROVENANCE VALIDATION TESTS (3 tests)
	// ================================================

	@Test
	protected void testMerge_provenance_createdForSync() {
		// Setup
		AbstractMergeTestScenario<T> scenario = createScenario();
		scenario.withOneReferencingResource();
		scenario.createTestData();

		// Execute merge
		MergeTestParameters params = scenario.buildMergeOperationParameters(false, false);
		Parameters inParams = params.asParametersResource();
		myHelper.callMergeOperation(getResourceTypeName(), params, false);

		// Validate provenance created
		scenario.assertMergeProvenanceCreated(inParams);
	}

	@Test
	protected void testMerge_provenance_createdForAsync() {
		// Setup
		AbstractMergeTestScenario<T> scenario = createScenario();
		scenario.withOneReferencingResource();
		scenario.createTestData();

		// Execute merge
		MergeTestParameters params = scenario.buildMergeOperationParameters(false, false);
		Parameters inParams = params.asParametersResource();
		Parameters outParams = myHelper.callMergeOperation(getResourceTypeName(), params, true);

		// Wait for completion
		Task task = (Task) outParams.getParameter("task").getResource();
		String jobId = myHelper.getJobIdFromTask(task);
		myHelper.awaitJobCompletion(jobId);

		// Validate provenance created
		scenario.assertMergeProvenanceCreated(inParams);
	}

	@Test
	protected void testMerge_provenance_notCreatedForPreview() {
		// Setup
		AbstractMergeTestScenario<T> scenario = createScenario();
		scenario.withOneReferencingResource();
		scenario.createTestData();

		// Execute merge in preview mode
		MergeTestParameters params = scenario.buildMergeOperationParameters(false, true);
		myHelper.callMergeOperation(getResourceTypeName(), params, false);

		// Validate no changes (preview mode validation already checks this)
		scenario.assertReferencesNotUpdated();
	}

	// ================================================
	// ACTIVE FIELD / STATUS FIELD VALIDATION TESTS (2 tests)
	// ================================================

	@Test
	protected void testMerge_activeOrStatusField_sourceHandledCorrectly() {
		// Setup
		AbstractMergeTestScenario<T> scenario = createScenario();
		scenario.withOneReferencingResource();
		scenario.createTestData();

		// Execute merge (no delete)
		MergeTestParameters params = scenario.buildMergeOperationParameters(false, false);
		myHelper.callMergeOperation(getResourceTypeName(), params, false);

		// Validate source state using scenario (handles active/status field differences)
		T sourceAfter = scenario.readResource(scenario.getSourceId());
		scenario.assertSourceResourceState(sourceAfter, scenario.getSourceId(), scenario.getTargetId(), false);
	}

	@Test
	protected void testMerge_activeOrStatusField_targetHandledCorrectly() {
		// Setup
		AbstractMergeTestScenario<T> scenario = createScenario();
		scenario.withOneReferencingResource();
		scenario.createTestData();

		// Execute merge
		MergeTestParameters params = scenario.buildMergeOperationParameters(false, false);
		myHelper.callMergeOperation(getResourceTypeName(), params, false);

		// Validate target state using scenario (handles active/status field differences)
		T targetAfter = scenario.readResource(scenario.getTargetId());
		scenario.assertTargetResourceState(
				targetAfter, scenario.getSourceId(), false, scenario.getExpectedIdentifiers());
	}

	// ================================================
	// RESULT RESOURCE VALIDATION TESTS (2 tests)
	// ================================================

	@Test
	protected void testMerge_resultResource_overridesTargetData() {
		// Setup with result resource
		AbstractMergeTestScenario<T> scenario = createScenario();
		scenario.withSourceIdentifiers("source-1", "source-2");
		scenario.withTargetIdentifiers("target-1", "target-2");
		scenario.withOneReferencingResource();
		scenario.withResultResource();
		scenario.createTestData();

		// Execute merge with result resource
		MergeTestParameters params = scenario.buildMergeOperationParameters(false, false);
		myHelper.callMergeOperation(getResourceTypeName(), params, false);

		// Validate target has result resource identifiers
		T target = scenario.readResource(scenario.getTargetId());
		List<Identifier> targetIdentifiers = scenario.getIdentifiersFromResource(target);

		// Result resource should have the target's original identifiers
		assertThat(targetIdentifiers).hasSizeGreaterThanOrEqualTo(2);
	}

	@Test
	protected void testMerge_resultResource_preservesReferences() {
		// Setup with result resource
		AbstractMergeTestScenario<T> scenario = createScenario();
		scenario.withOneReferencingResource();
		scenario.withResultResource();
		scenario.createTestData();

		// Execute merge with result resource
		MergeTestParameters params = scenario.buildMergeOperationParameters(false, false);
		myHelper.callMergeOperation(getResourceTypeName(), params, false);

		// Validate all references still updated to target
		for (String resourceType : scenario.getReferencingResourceTypes()) {
			scenario.assertReferencesUpdated(resourceType);
		}
	}

	// ================================================
	// HELPER METHODS
	// ================================================

	/**
	 * Validate merge outcome including source/target state and reference updates.
	 */
	protected void validateMergeOutcome(AbstractMergeTestScenario<T> theScenario, boolean theDeleteSource) {
		// Validate source resource state
		if (theDeleteSource) {
			theScenario.assertSourceResourceState(null, theScenario.getSourceId(), theScenario.getTargetId(), true);
		} else {
			T source = theScenario.readResource(theScenario.getSourceId());
			theScenario.assertSourceResourceState(
					source, theScenario.getSourceId(), theScenario.getTargetId(), false);
		}

		// Validate target resource state
		T target = theScenario.readResource(theScenario.getTargetId());
		theScenario.assertTargetResourceState(
				target, theScenario.getSourceId(), theDeleteSource, theScenario.getExpectedIdentifiers());

		// Validate all references updated
		for (String resourceType : theScenario.getReferencingResourceTypes()) {
			List<IIdType> referencingIds = theScenario.getReferencingResourceIds(resourceType);
			if (!referencingIds.isEmpty()) {
				theScenario.assertReferencesUpdated(resourceType);
			}
		}
	}
}
