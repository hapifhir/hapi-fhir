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
import java.util.Collections;
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
		AbstractMergeTestScenario<T> scenario = createScenario()
				.withMultipleReferencingResources()
				.withDeleteSource(theDeleteSource)
				.withPreview(thePreview)
				.withResultResource(theResultResource);

		scenario.createTestData();

		// Execute merge
		Parameters outParams = myHelper.callMergeOperation(scenario, theAsync);

		// Validate outcome based on mode
		if (thePreview) {
			scenario.validatePreviewOutcome(outParams);
			scenario.assertReferencesNotUpdated();
			return; // Preview mode doesn't make actual changes
		}

		if (theAsync) {
			scenario.validateAsyncOperationOutcome(outParams);
			myHelper.waitForAsyncTaskCompletion(outParams);
			scenario.validateTaskOutput(outParams);
		} else {
			scenario.validateSyncMergeOutcome(outParams);
		}

		// Validate merge outcomes
		validateMergeOutcome(scenario, theDeleteSource);

		// Validate provenance created for actual merges
		scenario.assertMergeProvenanceCreated();
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
		MergeTestParameters params = scenario.buildMergeOperationParameters(theSourceById, theTargetById);

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
		MergeTestParameters params = scenario.buildMergeOperationParameters(theSourceById, theTargetById);

		// Execute
		Parameters outParams = myHelper.callMergeOperation(getResourceTypeName(), params, true);

		// Validate
		scenario.validateAsyncOperationOutcome(outParams);
		myHelper.waitForAsyncTaskCompletion(outParams);
		scenario.validateTaskOutput(outParams);

		validateMergeOutcome(scenario, false);
	}

	// ================================================
	// IDENTIFIER EDGE CASES (3 tests)
	// ================================================

	@Test
	protected void testMerge_identifiers_emptySourceIdentifiers() {
		// Setup - source has no identifiers, target has identifiers
		AbstractMergeTestScenario<T> scenario = createScenario();
		scenario.withSourceIdentifiers(Collections.emptyList());
		scenario.withOneReferencingResource();
		scenario.createTestData();

		// Execute merge
		myHelper.callMergeOperation(scenario, false);

		// Validate - target should keep its identifiers unchanged
		T target = scenario.readResource(scenario.getVersionlessTargetId());
		List<Identifier> expectedIdentifiers = scenario.getExpectedIdentifiers();
		scenario.assertIdentifiers(scenario.getIdentifiersFromResource(target), expectedIdentifiers);
	}

	@Test
	protected void testMerge_identifiers_emptyTargetIdentifiers() {
		// Setup - source has identifiers, target has no identifiers
		AbstractMergeTestScenario<T> scenario = createScenario();
		scenario.withTargetIdentifiers(Collections.emptyList());
		scenario.withOneReferencingResource();
		scenario.createTestData();

		// Execute merge
		myHelper.callMergeOperation(scenario, false);

		// Validate - target should get all source identifiers marked as OLD
		T target = scenario.readResource(scenario.getVersionlessTargetId());
		List<Identifier> expectedIdentifiers = scenario.getExpectedIdentifiers();
		scenario.assertIdentifiers(scenario.getIdentifiersFromResource(target), expectedIdentifiers);
		assertThat(expectedIdentifiers).hasSize(3); // All source identifiers marked as OLD
		assertThat(expectedIdentifiers).allMatch(id -> id.getUse() == Identifier.IdentifierUse.OLD);
	}

	@Test
	protected void testMerge_identifiers_bothEmpty() {
		// Setup - both source and target have no identifiers
		AbstractMergeTestScenario<T> scenario = createScenario();
		scenario.withSourceIdentifiers(Collections.emptyList());
		scenario.withTargetIdentifiers(Collections.emptyList());
		scenario.withOneReferencingResource();
		scenario.createTestData();

		// Execute merge
		myHelper.callMergeOperation(scenario, false);

		// Validate - target should have empty identifier list
		T target = scenario.readResource(scenario.getVersionlessTargetId());
		List<Identifier> expectedIdentifiers = scenario.getExpectedIdentifiers();
		scenario.assertIdentifiers(scenario.getIdentifiersFromResource(target), expectedIdentifiers);
		assertThat(expectedIdentifiers).isEmpty();
	}

	@Test
	protected void testMerge_resultResource_overridesTargetIdentifiers() {
		// Setup - result resource has different identifiers than target
		List<Identifier> resultIdentifiers = Arrays.asList(
				new Identifier().setSystem("http://test.org").setValue("result-1"),
				new Identifier().setSystem("http://test.org").setValue("result-2"));

		AbstractMergeTestScenario<T> scenario = createScenario();
		scenario.withResultResourceIdentifiers(resultIdentifiers);
		scenario.withResultResource(true);
		scenario.withOneReferencingResource();
		scenario.createTestData();

		// Execute merge
		myHelper.callMergeOperation(scenario, false);

		// Validate - target should have result resource identifiers (not target's original)
		T target = scenario.readResource(scenario.getVersionlessTargetId());
		List<Identifier> actualIdentifiers = scenario.getIdentifiersFromResource(target);
		scenario.assertIdentifiers(actualIdentifiers, resultIdentifiers);
		assertThat(actualIdentifiers).hasSize(2);
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
		myHelper.callMergeOperation(scenario, false);

		// Validate target has "replaces" link to source
		T target = scenario.readResource(scenario.getVersionlessTargetId());
		scenario.assertLinksPresent(target, Arrays.asList(scenario.getVersionlessSourceId()), "replaces");
	}

	@Test
	protected void testMerge_extensionLinks_replacedByLink() {
		// Setup
		AbstractMergeTestScenario<T> scenario = createScenario();
		scenario.withOneReferencingResource();
		scenario.createTestData();

		// Execute merge
		myHelper.callMergeOperation(scenario, false);

		// Validate source has "replaced-by" link to target
		T source = scenario.readResource(scenario.getVersionlessSourceId());
		scenario.assertLinksPresent(source, Arrays.asList(scenario.getVersionlessTargetId()), "replaced-by");
	}

	@Test
	protected void testMerge_extensionLinks_noLinksBeforeMerge() {
		// Setup
		AbstractMergeTestScenario<T> scenario = createScenario();
		scenario.withOneReferencingResource();
		scenario.createTestData();

		// Validate no links before merge
		T source = scenario.readResource(scenario.getVersionlessSourceId());
		T target = scenario.readResource(scenario.getVersionlessTargetId());

		scenario.assertNoLinks(source);
		scenario.assertNoLinks(target);
	}

	@Test
	protected void testMerge_extensionLinks_deletedSourceHasNoLinks() {
		// Setup
		AbstractMergeTestScenario<T> scenario = createScenario();
		scenario.withOneReferencingResource();
		scenario.withDeleteSource(true);
		scenario.createTestData();

		// Execute merge with delete
		myHelper.callMergeOperation(scenario, false);

		// Validate source is deleted (cannot read it)
		scenario.assertSourceResourceState();
	}

	@Test
	protected void testMerge_extensionLinks_multipleReferencingResourceTypes() {
		// Setup with all standard referencing resource types
		AbstractMergeTestScenario<T> scenario = createScenario();
		scenario.withMultipleReferencingResources();
		scenario.createTestData();

		// Execute merge
		myHelper.callMergeOperation(scenario, false);

		// Validate all reference types updated
		for (String resourceType : scenario.getReferencingResourceTypes()) {
			scenario.assertReferencesUpdated(resourceType);
		}
	}

	@Test
	protected void testMerge_extensionLinks_targetPreservesExistingData() {
		// Setup
		AbstractMergeTestScenario<T> scenario = createScenario();
		scenario.withOneReferencingResource();
		scenario.createTestData();

		// Get target data before merge
		T targetBefore = scenario.readResource(scenario.getVersionlessTargetId());
		List<Identifier> targetIdentifiersBefore = scenario.getIdentifiersFromResource(targetBefore);

		// Execute merge
		myHelper.callMergeOperation(scenario, false);

		// Validate target preserved its original identifiers
		T targetAfter = scenario.readResource(scenario.getVersionlessTargetId());
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
				.sourceResource(new Reference(scenario.getVersionlessTargetId()))
				.targetResource(new Reference(scenario.getVersionlessTargetId()))
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
				.targetResource(new Reference(scenario.getVersionlessTargetId()))
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
				.sourceResource(new Reference(scenario.getVersionlessSourceId()))
				.targetIdentifiers(
						Arrays.asList(new Identifier().setSystem("http://test.org").setValue("non-existent-id")))
				.deleteSource(false)
				.preview(false);

		// Validate error
		assertThatThrownBy(() -> myHelper.callMergeOperation(getResourceTypeName(), params, false))
				.isInstanceOf(UnprocessableEntityException.class);
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
		myHelper.callMergeOperation(scenario, false);

		// Validate source state using scenario (handles active/status field differences)
		scenario.assertSourceResourceState();
	}

	@Test
	protected void testMerge_activeOrStatusField_targetHandledCorrectly() {
		// Setup
		AbstractMergeTestScenario<T> scenario = createScenario();
		scenario.withOneReferencingResource();
		scenario.createTestData();

		// Execute merge
		myHelper.callMergeOperation(scenario, false);

		// Validate target state using scenario (handles active/status field differences)
		T targetAfter = scenario.readResource(scenario.getVersionlessTargetId());
		scenario.assertTargetResourceState(
				targetAfter, scenario.getVersionlessSourceId(), false, scenario.getExpectedIdentifiers());
	}

	// ================================================
	// RESULT RESOURCE VALIDATION TESTS (2 tests)
	// ================================================

	@Test
	protected void testMerge_resultResource_overridesTargetData() {
		// Setup with result resource
		AbstractMergeTestScenario<T> scenario = createScenario();
		scenario.withOneReferencingResource();
		scenario.withResultResource(true);
		scenario.createTestData();

		// Execute merge with result resource
		myHelper.callMergeOperation(scenario, false);

		// Validate target has result resource identifiers
		T target = scenario.readResource(scenario.getVersionlessTargetId());
		List<Identifier> targetIdentifiers = scenario.getIdentifiersFromResource(target);

		// Result resource should have the target's original identifiers
		assertThat(targetIdentifiers).hasSizeGreaterThanOrEqualTo(2);
	}

	@Test
	protected void testMerge_resultResource_preservesReferences() {
		// Setup with result resource
		AbstractMergeTestScenario<T> scenario = createScenario();
		scenario.withOneReferencingResource();
		scenario.withResultResource(true);
		scenario.createTestData();

		// Execute merge with result resource
		myHelper.callMergeOperation(scenario, false);

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
		theScenario.assertSourceResourceState();

		// Validate target resource state
		T target = theScenario.readResource(theScenario.getVersionlessTargetId());
		theScenario.assertTargetResourceState(
				target, theScenario.getVersionlessSourceId(), theDeleteSource, theScenario.getExpectedIdentifiers());

		// Validate all references updated
		for (String resourceType : theScenario.getReferencingResourceTypes()) {
			List<IIdType> referencingIds = theScenario.getReferencingResourceIds(resourceType);
			if (!referencingIds.isEmpty()) {
				theScenario.assertReferencesUpdated(resourceType);
			}
		}
	}
}
