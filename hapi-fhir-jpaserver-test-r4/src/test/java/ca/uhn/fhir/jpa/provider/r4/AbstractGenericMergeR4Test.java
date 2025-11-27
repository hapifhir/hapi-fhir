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
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.jpa.replacereferences.ReplaceReferencesTestHelper;
import ca.uhn.fhir.jpa.test.Batch2JobHelper;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Reference;
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
import static org.assertj.core.api.Assertions.catchException;

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
	protected ReplaceReferencesTestHelper myReplaceReferencesTestHelper;

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
		myReplaceReferencesTestHelper = new ReplaceReferencesTestHelper(myFhirContext, myDaoRegistry);
	}

	@AfterEach
	public void afterGenericMerge() {
		// Cleanup
	}

	// ================================================
	// BASIC MERGE MATRIX TESTS
	// ================================================

	/**
	 * Comprehensive matrix test covering all combinations of:
	 * - deleteSource: true/false
	 * - resultResource: true/false
	 * - preview: true/false
	 * - async: true/false
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
		scenario.validateResourcesAfterMerge();
	}

	// ================================================
	// IDENTIFIER RESOLUTION TESTS
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
		scenario.validateResourcesAfterMerge();
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

		scenario.validateResourcesAfterMerge();
	}

	// ================================================
	// IDENTIFIER EDGE CASES
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
	// EDGE CASES AND ERROR HANDLING TESTS
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

	@Test
	protected void testMerge_errorHandling_previouslyMergedSourceAsSource() {
		// Setup: Create and merge source with another resource first
		AbstractMergeTestScenario<T> scenario = createScenario();
		scenario.createTestData();
		myHelper.callMergeOperation(scenario, false);

		// Create a minimal target resource (no identifiers needed)
		T simpleTarget = createScenario().createResource(Collections.emptyList());
		IIdType newTargetIdVersionless = myClient.create().resource(simpleTarget).execute().getId().toUnqualifiedVersionless();

		// Try to use the already-merged source in another merge
		MergeTestParameters params = new MergeTestParameters()
				.sourceResource(new Reference(scenario.getVersionlessSourceId())) // Previously merged
				.targetResource(new Reference(newTargetIdVersionless))
				.deleteSource(false)
				.preview(false);

		// Validate error - should mention replaced-by link
		Exception ex = catchException(() -> myHelper.callMergeOperation(getResourceTypeName(), params, false));
		assertThat(ex).isInstanceOf(UnprocessableEntityException.class);
		UnprocessableEntityException baseEx = (UnprocessableEntityException) ex;
		String diagnosticMessage = myReplaceReferencesTestHelper.extractFailureMessageFromOutcomeParameter(baseEx);
		assertThat(diagnosticMessage)
				.contains("Source resource was previously replaced by a resource with reference")
				.contains("not a suitable source for merging");
	}

	@Test
	protected void testMerge_errorHandling_previouslyMergedSourceAsTarget() {
		// Setup: Create and merge to get a resource with replaced-by link
		AbstractMergeTestScenario<T> scenario = createScenario();
		scenario.createTestData();
		myHelper.callMergeOperation(scenario, false);

		// Create a minimal source resource (no identifiers needed)
		T simpleSource = createScenario().createResource(Collections.emptyList());
		IIdType newSourceIdVersionless = myClient.create().resource(simpleSource).execute().getId().toUnqualifiedVersionless();

		// Try to use the already-merged source as target
		MergeTestParameters params = new MergeTestParameters()
				.sourceResource(new Reference(newSourceIdVersionless))
				.targetResource(new Reference(scenario.getVersionlessSourceId())) // Previously merged
				.deleteSource(false)
				.preview(false);

		// Validate error - for resources with active field, may fail on "not active" first
		// For resources without active field, should mention replaced-by link
		Exception ex = catchException(() -> myHelper.callMergeOperation(getResourceTypeName(), params, false));
		assertThat(ex).isInstanceOf(UnprocessableEntityException.class);
		UnprocessableEntityException baseEx = (UnprocessableEntityException) ex;
		String diagnosticMessage = myReplaceReferencesTestHelper.extractFailureMessageFromOutcomeParameter(baseEx);

		// Validate that error prevents using previously merged resource
		// Message may be about "not active" OR "previously replaced by"
		assertThat(diagnosticMessage).satisfiesAnyOf(
				msg -> assertThat(msg).contains("not active"),
				msg -> assertThat(msg).contains("Target resource was previously replaced by a resource with reference")
		);
	}

}
