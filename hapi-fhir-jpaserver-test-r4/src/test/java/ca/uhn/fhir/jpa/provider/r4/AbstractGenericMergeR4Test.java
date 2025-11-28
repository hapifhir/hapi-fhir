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
import ca.uhn.fhir.model.api.IProvenanceAgent;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_MERGE_OUTPUT_PARAM_TASK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.catchException;
import static org.awaitility.Awaitility.await;

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

		scenario.persistTestData();

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
		scenario.persistTestData();

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
		scenario.persistTestData();

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
		scenario.persistTestData();

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
		scenario.persistTestData();

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
		scenario.persistTestData();

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
		scenario.persistTestData();

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
		Exception ex = catchException(() -> myHelper.callMergeOperation(getResourceTypeName(), params, false));
		assertThat(ex).isInstanceOf(InvalidRequestException.class);

		InvalidRequestException invalidEx = (InvalidRequestException) ex;
		String diagnosticMessage = myReplaceReferencesTestHelper.extractFailureMessageFromOutcomeParameter(invalidEx);
		assertThat(diagnosticMessage)
				.contains("There are no source resource parameters provided, include either a 'source-resource', or a 'source-resource-identifier' parameter");
	}

	@Test
	protected void testMerge_errorHandling_missingTargetResource() {
		// Build invalid parameters (no target)
		MergeTestParameters params = new MergeTestParameters()
				.sourceResource(new Reference(getResourceTypeName() + "/123"))
				.deleteSource(false)
				.preview(false);

		// Validate error
		Exception ex = catchException(() -> myHelper.callMergeOperation(getResourceTypeName(), params, false));
		assertThat(ex).isInstanceOf(InvalidRequestException.class);

		InvalidRequestException invalidEx = (InvalidRequestException) ex;
		String diagnosticMessage = myReplaceReferencesTestHelper.extractFailureMessageFromOutcomeParameter(invalidEx);
		assertThat(diagnosticMessage)
				.contains("There are no target resource parameters provided, include either a 'target-resource', or a 'target-resource-identifier' parameter");
	}

	@Test
	protected void testMerge_MissingRequiredParameters_Returns400BadRequest() {
		// Build completely empty parameters (no source, no target)
		MergeTestParameters params = new MergeTestParameters()
				.deleteSource(false)
				.preview(false);

		// Validate error - should return InvalidRequestException with 400 status and both error messages
		Exception ex = catchException(() -> myHelper.callMergeOperation(getResourceTypeName(), params, false));
		assertThat(ex)
				.isInstanceOf(InvalidRequestException.class)
				.extracting(InvalidRequestException.class::cast)
				.extracting(BaseServerResponseException::getStatusCode)
				.isEqualTo(400);

		// Validate both error messages are present
		InvalidRequestException invalidEx = (InvalidRequestException) ex;
		String diagnosticMessage = myReplaceReferencesTestHelper.extractFailureMessageFromOutcomeParameter(invalidEx);
		assertThat(diagnosticMessage)
				.contains("There are no source resource parameters provided")
				.contains("There are no target resource parameters provided");
	}

	@Test
	protected void testMerge_errorHandling_sourceEqualsTarget() {
		// Setup
		AbstractMergeTestScenario<T> scenario = createScenario();
		scenario.persistTestData();

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
		scenario.persistTestData();

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
		scenario.persistTestData();

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
	protected void testMerge_errorHandling_multipleTargetMatches() {
		// Setup: Create scenario with standard source and target
		AbstractMergeTestScenario<T> scenario = createScenario();
		scenario.persistTestData();

		// Create a duplicate resource with the same identifiers as target
		// This simulates the scenario where multiple resources match the target identifiers
		List<Identifier> targetIdentifiers = scenario.getIdentifiersFromResource(scenario.getTargetResource());
		T duplicateResource = scenario.createResource(targetIdentifiers);
		myClient.create().resource(duplicateResource).execute();

		// Build parameters with target resolved by identifier (not ID)
		// This will cause the identifier search to find both the original target and the duplicate
		MergeTestParameters params = scenario.buildMergeOperationParameters(
			true,  // sourceById = true (no ambiguity for source)
			false  // targetById = false (triggers identifier search → multiple matches)
		);

		// Validate error
		Exception ex = catchException(() -> myHelper.callMergeOperation(getResourceTypeName(), params, false));
		assertThat(ex).isInstanceOf(UnprocessableEntityException.class);
		UnprocessableEntityException baseEx = (UnprocessableEntityException) ex;
		String diagnosticMessage = myReplaceReferencesTestHelper.extractFailureMessageFromOutcomeParameter(baseEx);
		assertThat(diagnosticMessage).isEqualTo("Multiple resources found matching the identifier(s) specified in 'target-resource-identifier'");
	}

	@Test
	protected void testMerge_errorHandling_multipleSourceMatches() {
		// Setup: Create scenario with standard source and target
		AbstractMergeTestScenario<T> scenario = createScenario();
		scenario.persistTestData();

		// Create a duplicate resource with the same identifiers as source
		// This simulates the scenario where multiple resources match the source identifiers
		List<Identifier> sourceIdentifiers = scenario.getIdentifiersFromResource(scenario.getSourceResource());
		T duplicateResource = scenario.createResource(sourceIdentifiers);
		myClient.create().resource(duplicateResource).execute();

		// Build parameters with source resolved by identifier (not ID)
		// This will cause the identifier search to find both the original source and the duplicate
		MergeTestParameters params = scenario.buildMergeOperationParameters(
			false,  // sourceById = false (triggers identifier search → multiple matches)
			true    // targetById = true (no ambiguity for target)
		);

		// Validate error
		Exception ex = catchException(() -> myHelper.callMergeOperation(getResourceTypeName(), params, false));
		assertThat(ex).isInstanceOf(UnprocessableEntityException.class);
		UnprocessableEntityException baseEx = (UnprocessableEntityException) ex;
		String diagnosticMessage = myReplaceReferencesTestHelper.extractFailureMessageFromOutcomeParameter(baseEx);
		assertThat(diagnosticMessage).isEqualTo("Multiple resources found matching the identifier(s) specified in 'source-resource-identifier'");
	}

	@Test
	protected void testMerge_errorHandling_previouslyMergedSourceAsSource() {
		// Setup: Create and merge source with another resource first
		AbstractMergeTestScenario<T> scenario = createScenario();
		scenario.persistTestData();
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
		scenario.persistTestData();
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

	// ================================================
	// PROVENANCE AGENT INTERCEPTOR TESTS
	// ================================================

	/**
	 * Tests that Provenance.agent is correctly populated from interceptor hooks.
	 * Validates the PROVENANCE_AGENTS pointcut mechanism for generic resource merge operations.
	 */
	@ParameterizedTest(name = "{index}: async={0}, multipleAgents={1}")
	@CsvSource({
		"false, false",
		"false, true",
		"true, false",
		"true, true"
	})
	protected void testMerge_withProvenanceAgentInterceptor_Success(boolean theAsync, boolean theMultipleAgents) {
		// Setup: Create Provenance agents
		List<IProvenanceAgent> agents = new ArrayList<>();
		agents.add(myReplaceReferencesTestHelper.createTestProvenanceAgent());
		if (theMultipleAgents) {
			agents.add(myReplaceReferencesTestHelper.createTestProvenanceAgent());
		}

		// Register interceptor (will be unregistered in @AfterEach of base class)
		ReplaceReferencesTestHelper.registerProvenanceAgentInterceptor(myServer.getRestfulServer(), agents);

		// Create test scenario with referencing resources and expected agents
		AbstractMergeTestScenario<T> scenario = createScenario()
			.withOneReferencingResource()
			.withExpectedProvenanceAgents(agents);
		scenario.persistTestData();

		// Execute merge
		Parameters outParams = myHelper.callMergeOperation(scenario, theAsync);

		// Validate based on execution mode
		if (theAsync) {
			scenario.validateAsyncOperationOutcome(outParams);
			myHelper.waitForAsyncTaskCompletion(outParams);
			scenario.validateTaskOutput(outParams);
		} else {
			scenario.validateSyncMergeOutcome(outParams);
		}

		// Validate all merge outcomes including provenance with agents
		scenario.validateResourcesAfterMerge();
	}

	/**
	 * GAP #2: Test Provenance Agent Interceptor error handling when no agents provided.
	 * Tests that merge operation fails with InternalErrorException when interceptor
	 * returns empty agent list, validating defensive programming around hook points.
	 */
	@ParameterizedTest(name = "{index}: async={0}")
	@CsvSource({
		"false",
		"true"
	})
	void testMerge_withProvenanceAgentInterceptor_InterceptorReturnsNoAgent_ReturnsInternalError(boolean theAsync) {
		// this interceptor will be unregistered in @AfterEach of the base class, which unregisters all interceptors
		ReplaceReferencesTestHelper.registerProvenanceAgentInterceptor(
			myServer.getRestfulServer(),
			Collections.emptyList()
		);

		// Build merge parameters using scenario
		AbstractMergeTestScenario<T> scenario = createScenario();
		scenario.persistTestData();
		MergeTestParameters params = scenario.buildMergeOperationParameters();

		// Execute merge and expect error
		assertThatThrownBy(() -> myHelper.callMergeOperation(getResourceTypeName(), params, theAsync))
			.isInstanceOf(InternalErrorException.class)
			.hasMessageContaining("HAPI-2723: No Provenance Agent was provided by any interceptor for Pointcut.PROVENANCE_AGENTS")
			.extracting(InternalErrorException.class::cast)
			.extracting(BaseServerResponseException::getStatusCode)
			.isEqualTo(500);
	}

	/**
	 * Test that validates resource limit validation for merge operations.
	 * Creates more referencing resources than batch-size, expecting
	 * a PreconditionFailedException since the number of resources exceeds the limit.
	 */
	@Test
	void testMerge_smallResourceLimit() {
		// Create scenario with many referencing resources (6 total)
		AbstractMergeTestScenario<T> scenario = createScenario();
		scenario.withMultipleReferencingResources(6);
		scenario.persistTestData();

		// Build parameters and set batch size (which controls resource limit)
		MergeTestParameters params = scenario.buildMergeOperationParameters();
		params.batchSize(5);

		// Execute merge and expect error
		String sourceId = scenario.getSourceResource().getIdElement().toUnqualifiedVersionless().getValue();
		assertThatThrownBy(() -> myHelper.callMergeOperation(getResourceTypeName(), params, false))
			.isInstanceOf(PreconditionFailedException.class)
			.satisfies(ex -> assertThat(myReplaceReferencesTestHelper.extractFailureMessageFromOutcomeParameter((BaseServerResponseException) ex))
				.isEqualTo("HAPI-2597: Number of resources with references to " + sourceId + " exceeds the resource-limit 5. Submit the request asynchronsly by adding the HTTP Header 'Prefer: respond-async'."));
	}

	/**
	 * Test that validates merge behavior when source resource is not referenced by any other resources.
	 * This edge case tests minimal merge workflow where only source and target exist.
	 * Validates that merge succeeds, provenance is created, and links are properly established
	 * even when there are zero referencing resources.
	 */
	@ParameterizedTest(name = "{index}: deleteSource={0}, async={1}")
	@CsvSource({
		"false, false",
		"false, true",
		"true, false",
		"true, true"
	})
	void testMerge_sourceNotReferencedByAnyResource(boolean theDeleteSource, boolean theAsync) {
		// Setup: Create minimal scenario with NO referencing resources
		AbstractMergeTestScenario<T> scenario = createScenario()
			.withDeleteSource(theDeleteSource)
			.withPreview(false);
		// NOTE: Don't call .withOneReferencingResource() or .withMultipleReferencingResources()
		// This creates only source + target with default identifiers

		scenario.persistTestData();

		// Execute merge
		Parameters outParams = myHelper.callMergeOperation(scenario, theAsync);

		// Validate outcome based on mode
		if (theAsync) {
			scenario.validateAsyncOperationOutcome(outParams);
			myHelper.waitForAsyncTaskCompletion(outParams);
			scenario.validateTaskOutput(outParams);
		} else {
			scenario.validateSyncMergeOutcome(outParams);
		}

		// Comprehensive validation (handles all edge cases automatically)
		scenario.validateResourcesAfterMerge();
	}

	/**
	 *
	 * Test validates that when a concurrent modification occurs (new reference to source
	 * created while async job is running), the job fails safely rather than leaving orphaned
	 * references. This tests transaction isolation in multi-user environments.
	 */
	@Test
	void testMerge_concurrentModificationDuringAsyncJob_JobFails() {
		// Setup: Create scenario with many referencing resources
		AbstractMergeTestScenario<T> scenario = createScenario();
		scenario.withMultipleReferencingResources(25);
		scenario.persistTestData();

		// Small batch size to slow job execution
		int originalBatchSize = myStorageSettings.getDefaultTransactionEntriesForWrite();
		myStorageSettings.setDefaultTransactionEntriesForWrite(5);

		try {
			// Build merge parameters with deleteSource=true
			MergeTestParameters params = scenario.buildMergeOperationParameters()
					.deleteSource(true)
					.preview(false);

			// Execute async merge
			Parameters outParams = myHelper.callMergeOperation(getResourceTypeName(), params, true);
			Task task = (Task) outParams.getParameter(OPERATION_MERGE_OUTPUT_PARAM_TASK).getResource();
			String jobId = myHelper.getJobIdFromTask(task);

			// Wait for "query-ids" step to complete
			await()
					.until(() -> {
						myBatch2JobHelper.runMaintenancePass();
						String currentGatedStepId = myJobCoordinator.getInstance(jobId).getCurrentGatedStepId();
						return !"query-ids".equals(currentGatedStepId);
					});

			// Create NEW resource referencing source WHILE JOB IS RUNNING
			IBaseResource referencingResource = scenario.createReferencingResource();
			myClient.create().resource(referencingResource).execute();

			// Job should fail when trying to delete source
			myBatch2JobHelper.awaitJobFailure(jobId);

			// Verify task status
			Task taskAfterJobFailure = myClient.read()
					.resource(Task.class)
					.withId(task.getIdElement().toVersionless())
					.execute();
			assertThat(taskAfterJobFailure.getStatus()).isEqualTo(Task.TaskStatus.FAILED);
		} finally {
			// Restore original batch size
			myStorageSettings.setDefaultTransactionEntriesForWrite(originalBatchSize);
		}
	}

}
