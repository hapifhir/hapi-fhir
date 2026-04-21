// Created by claude-sonnet-4-5
package ca.uhn.fhir.jpa.merge;

/*-
 * #%L
 * HAPI FHIR JPA Server Test Utilities
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.replacereferences.ReplaceReferencesTestHelper;
import ca.uhn.fhir.merge.AbstractMergeOperationInputParameterNames;
import ca.uhn.fhir.merge.IResourceLinkService;
import ca.uhn.fhir.merge.ResourceLinkServiceFactory;
import ca.uhn.fhir.model.api.IProvenanceAgent;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static ca.uhn.fhir.jpa.provider.ReplaceReferencesSvcImpl.RESOURCE_TYPES_SYSTEM;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_MERGE_OUTPUT_PARAM_INPUT;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_MERGE_OUTPUT_PARAM_OUTCOME;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_MERGE_OUTPUT_PARAM_TASK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Abstract base class for merge test scenarios.
 *
 * <p>This class provides a unified implementation combining three responsibilities:
 * <ol>
 *   <li><b>Builder</b>: Fluent API for configuring test data</li>
 *   <li><b>Data Holder</b>: Stores info about created resources after persistTestData()</li>
 *   <li><b>Strategy</b>: Common implementations for resource operations and validations</li>
 * </ol>
 *
 * <p>Subclasses must implement resource-specific abstract methods:
 * <ul>
 *   <li>{@link #getResourceTypeName()}</li>
 *   <li>{@link #getResourceClass()}</li>
 *   <li>{@link #createResource(List)}</li>
 *   <li>{@link #createReferencingResource(String, IIdType)}</li>
 *   <li>{@link #createReferencingResource()}</li>
 *   <li>{@link #withOneReferencingResource()}</li>
 *   <li>{@link #withMultipleReferencingResources()}</li>
 *   <li>{@link #withMultipleReferencingResources(int)}</li>
 * </ul>
 *
 * <p>The scenario has two states:
 * <ul>
 *   <li><b>Configuration State</b>: Before persistTestData() - builder methods available</li>
 *   <li><b>Data State</b>: After persistTestData() - data accessors available</li>
 * </ul>
 *
 * @param <T> the FHIR resource type this scenario handles
 */
public abstract class AbstractMergeTestScenario<T extends IBaseResource> {

	private static final Logger ourLog = LoggerFactory.getLogger(AbstractMergeTestScenario.class);

	// Dependencies
	protected final DaoRegistry myDaoRegistry;
	protected final ResourceLinkServiceFactory myLinkServiceFactory;
	protected final RequestDetails myRequestDetails;
	protected final FhirContext myFhirContext;
	protected final MergeOperationTestHelper myHelper;

	// Configuration State (before createTestData)
	private final List<ReferencingTestResourceType> myReferencingTestResourceTypes = new ArrayList<>();
	private List<Identifier> mySourceIdentifiers = Arrays.asList(
			new Identifier().setSystem("http://test.org").setValue("source-1"),
			new Identifier().setSystem("http://test.org").setValue("source-2"),
			new Identifier().setSystem("http://test.org").setValue("common"));
	private List<Identifier> myTargetIdentifiers = Arrays.asList(
			new Identifier().setSystem("http://test.org").setValue("target-1"),
			new Identifier().setSystem("http://test.org").setValue("target-2"),
			new Identifier().setSystem("http://test.org").setValue("common"));
	private List<Identifier> myResultResourceIdentifiers = null;
	private boolean myCreateResultResource = false;
	private boolean myDeleteSource = false;
	private boolean myPreview = false;
	private boolean myAsync = false;

	@Nullable
	private List<IProvenanceAgent> myExpectedProvenanceAgents = null;

	// Data State (after createTestData)
	private IIdType myVersionlessSourceId;
	private IIdType myVersionlessTargetId;
	private Map<String, List<IIdType>> myReferencingResourcesByType;
	private boolean myIsTestDataPersisted = false;
	private Parameters myInputParameters;
	private MergeTestParameters myMergeTestParameters;

	/**
	 * Create a new abstract merge test scenario.
	 *
	 * @param theDaoRegistry DAO registry for database operations
	 * @param theFhirContext FHIR context
	 * @param theLinkServiceFactory Factory for getting link services
	 * @param theRequestDetails Request details for DAO operations
	 * @param theHelper Helper for merge operations
	 */
	protected AbstractMergeTestScenario(
			@Nonnull DaoRegistry theDaoRegistry,
			@Nonnull FhirContext theFhirContext,
			@Nonnull ResourceLinkServiceFactory theLinkServiceFactory,
			@Nonnull RequestDetails theRequestDetails,
			@Nonnull MergeOperationTestHelper theHelper) {

		myDaoRegistry = theDaoRegistry;
		myFhirContext = theFhirContext;
		myLinkServiceFactory = theLinkServiceFactory;
		myRequestDetails = theRequestDetails;
		myHelper = theHelper;
	}

	/**
	 * Get the FHIR resource type name (e.g., "Practitioner", "Observation").
	 *
	 * @return The resource type name
	 */
	@Nonnull
	protected abstract String getResourceTypeName();

	/**
	 * Get the Java class for this resource type.
	 *
	 * @return The resource class
	 */
	@Nonnull
	protected abstract Class<T> getResourceClass();

	/**
	 * Create a resource instance with the given identifier objects.
	 *
	 * @param theIdentifiers Identifiers to add
	 * @return A new resource with the identifiers
	 */
	@Nonnull
	public abstract T createResource(@Nonnull List<Identifier> theIdentifiers);

	/**
	 * Create a referencing resource (e.g., Encounter.subject, DiagnosticReport.result).
	 *
	 * @param theReferencingResourceType The resource type that will contain the reference
	 * @param theReferencedId            The ID of the resource being referenced
	 * @return A new referencing resource
	 */
	@Nonnull
	protected abstract IBaseResource createReferencingResource(
			@Nonnull String theReferencingResourceType, @Nonnull IIdType theReferencedId);

	/**
	 * Creates a single referencing resource for concurrent modification testing.
	 * This resource references the source resource and is used to simulate
	 * race conditions during async merge operations.
	 *
	 * <p>Implementations should call {@link #createReferencingResource(String, IIdType)}
	 * with a hardcoded resource type appropriate for this scenario.
	 *
	 * @return A new referencing resource that references the source resource
	 */
	@Nonnull
	public abstract IBaseResource createReferencingResource();
	/**
	 * Configure scenario with one referencing resource of the primary type.
	 * Subclasses define which resource type is used.
	 *
	 * @return this scenario for chaining
	 */
	public abstract AbstractMergeTestScenario<T> withOneReferencingResource();

	/**
	 * Configure scenario with multiple referencing resources (possibly of different types).
	 * Subclasses define which resource types and counts are used.
	 *
	 * @return this scenario for chaining
	 */
	public abstract AbstractMergeTestScenario<T> withMultipleReferencingResources();

	/**
	 * Configure scenario with a specific number of referencing resources.
	 * Subclasses define which resource types are used and how they're distributed.
	 *
	 * @param theCount number of referencing resources to create
	 * @return this scenario for chaining
	 */
	public abstract AbstractMergeTestScenario<T> withMultipleReferencingResources(int theCount);

	// ================================================
	// BUILDER METHODS
	// ================================================

	@Nonnull
	public AbstractMergeTestScenario<T> withReferences(@Nonnull List<ReferencingTestResourceType> theConfigs) {
		myReferencingTestResourceTypes.addAll(theConfigs);
		return this;
	}

	@Nonnull
	public AbstractMergeTestScenario<T> withResultResource(boolean theCreateResultResource) {
		myCreateResultResource = theCreateResultResource;
		return this;
	}

	@Nonnull
	public AbstractMergeTestScenario<T> withDeleteSource(boolean theDeleteSource) {
		myDeleteSource = theDeleteSource;
		return this;
	}

	@Nonnull
	public AbstractMergeTestScenario<T> withPreview(boolean thePreview) {
		myPreview = thePreview;
		return this;
	}

	@Nonnull
	public AbstractMergeTestScenario<T> withAsync(boolean theAsync) {
		myAsync = theAsync;
		return this;
	}

	@Nonnull
	public AbstractMergeTestScenario<T> withSourceIdentifiers(@Nonnull List<Identifier> theIdentifiers) {
		mySourceIdentifiers = theIdentifiers;
		return this;
	}

	@Nonnull
	public AbstractMergeTestScenario<T> withTargetIdentifiers(@Nonnull List<Identifier> theIdentifiers) {
		myTargetIdentifiers = theIdentifiers;
		return this;
	}

	@Nonnull
	public AbstractMergeTestScenario<T> withResultResourceIdentifiers(@Nonnull List<Identifier> theIdentifiers) {
		myResultResourceIdentifiers = theIdentifiers;
		return this;
	}

	@Nonnull
	public AbstractMergeTestScenario<T> withExpectedProvenanceAgents(@Nullable List<IProvenanceAgent> theAgents) {
		myExpectedProvenanceAgents = theAgents;
		return this;
	}

	public void persistTestData() {
		// Create and persist source resource
		T source = createResource(mySourceIdentifiers);
		IFhirResourceDao<T> dao = getDao();
		IIdType versionlessSourceId =
				dao.create(source, myRequestDetails).getId().toUnqualifiedVersionless();

		// Create and persist target resource
		T target = createResource(myTargetIdentifiers);
		IIdType versionlessTargetId =
				dao.create(target, myRequestDetails).getId().toUnqualifiedVersionless();

		// Create referencing resources organized by type
		Map<String, List<IIdType>> referencingResourcesByType = new HashMap<>();

		for (ReferencingTestResourceType resourceType : myReferencingTestResourceTypes) {
			List<IIdType> idsForType = new ArrayList<>();

			for (int i = 0; i < resourceType.getCount(); i++) {
				IBaseResource referencingResource =
						createReferencingResource(resourceType.getResourceType(), versionlessSourceId);

				// Persist referencing resource
				IFhirResourceDao<IBaseResource> refDao = myDaoRegistry.getResourceDao(resourceType.getResourceType());
				IIdType createdId = refDao.create(referencingResource, myRequestDetails)
						.getId()
						.toUnqualifiedVersionless();
				idsForType.add(createdId);
			}

			referencingResourcesByType.put(resourceType.getResourceType(), idsForType);
		}

		// Store data state
		myVersionlessSourceId = versionlessSourceId;
		myVersionlessTargetId = versionlessTargetId;
		myReferencingResourcesByType = referencingResourcesByType;
		myIsTestDataPersisted = true;

		ourLog.info(
				"Merge test data built successfully: source={}, target={}, referencing resource types={}",
				versionlessSourceId,
				versionlessTargetId,
				referencingResourcesByType.keySet());
	}

	// ================================================
	// DATA ACCESSORS
	// ================================================

	@Nonnull
	public IIdType getVersionlessSourceId() {
		assertTestDataPersisted();
		return myVersionlessSourceId;
	}

	@Nonnull
	public IIdType getVersionlessTargetId() {
		assertTestDataPersisted();
		return myVersionlessTargetId;
	}

	/**
	 * Reads the source resource from the database.
	 * Always returns fresh data from the database.
	 *
	 * @return the source resource read from database
	 */
	@Nonnull
	public T readSourceResource() {
		assertTestDataPersisted();
		return readResource(myVersionlessSourceId);
	}

	/**
	 * Reads the target resource from the database.
	 * Always returns fresh data from the database.
	 *
	 * @return the target resource read from database
	 */
	@Nonnull
	public T readTargetResource() {
		assertTestDataPersisted();
		return readResource(myVersionlessTargetId);
	}

	/**
	 * Get the source identifiers used to create the source resource.
	 *
	 * @return list of source identifiers from configuration
	 */
	@Nonnull
	public List<Identifier> getSourceIdentifiers() {
		return Collections.unmodifiableList(mySourceIdentifiers);
	}

	/**
	 * Get the target identifiers used to create the target resource.
	 *
	 * @return list of target identifiers from configuration
	 */
	@Nonnull
	public List<Identifier> getTargetIdentifiers() {
		return Collections.unmodifiableList(myTargetIdentifiers);
	}

	public List<IIdType> getReferencingResourceIds(@Nonnull String theResourceType) {
		assertTestDataPersisted();
		return myReferencingResourcesByType.get(theResourceType);
	}

	@Nonnull
	public Map<String, List<IIdType>> getAllReferencingResources() {
		assertTestDataPersisted();
		return Collections.unmodifiableMap(myReferencingResourcesByType);
	}

	public int getTotalReferenceCount() {
		assertTestDataPersisted();
		return myReferencingResourcesByType.values().stream()
				.mapToInt(List::size)
				.sum();
	}

	@Nonnull
	public Set<String> getReferencingResourceTypes() {
		assertTestDataPersisted();
		return myReferencingResourcesByType.keySet();
	}

	/**
	 * Get identifiers for result resource, or null if no result resource is configured.
	 * Defaults to target identifiers plus an extra identifier to ensure the result resource
	 * is different from target.
	 */
	@Nullable
	private List<Identifier> getResultResourceIdentifiers() {
		if (!myCreateResultResource) {
			return null;
		}

		if (myResultResourceIdentifiers != null) {
			return myResultResourceIdentifiers;
		}

		// Default: use target identifiers plus an extra identifier to ensure result is different
		// This ensures the target update is not a no-op in case deleteSource=true,
		// causing version of the target resource to increment from v1 to v2
		List<Identifier> result = new ArrayList<>(myTargetIdentifiers);
		result.add(new Identifier().setSystem("http://test.org/result").setValue("extra-result-identifier"));
		return result;
	}

	@Nonnull
	public List<Identifier> getExpectedIdentifiersOnTargetAfterMerge() {
		return myHelper.computeIdentifiersExpectedOnTargetAfterMerge(
				myTargetIdentifiers, mySourceIdentifiers, getResultResourceIdentifiers());
	}

	@Nonnull
	public MergeTestParameters buildMergeOperationParameters() {
		return buildMergeOperationParameters(true, true);
	}

	@Nonnull
	public MergeTestParameters buildMergeOperationParameters(boolean theSourceById, boolean theTargetById) {
		MergeTestParameters params =
				new MergeTestParameters().deleteSource(myDeleteSource).preview(myPreview);

		if (theSourceById) {
			params.sourceResource(new Reference(getVersionlessSourceId()));
		} else {
			params.sourceIdentifiers(getSourceIdentifiers());
		}

		if (theTargetById) {
			params.targetResource(new Reference(getVersionlessTargetId()));
		} else {
			params.targetIdentifiers(getTargetIdentifiers());
		}

		addResultResourceIfNeeded(params);
		myMergeTestParameters = params;
		myInputParameters = params.asParametersResource(myHelper.getParameterNames());
		return params;
	}

	/**
	 * Executes the merge operation using this scenario's configuration.
	 * Returns the output parameters for validation via {@link #validateSuccess(Parameters)}.
	 *
	 * @return the operation output parameters
	 */
	@Nonnull
	public Parameters callMergeOperation() {
		return callMergeOperation(true, true);
	}

	/**
	 * Executes the merge operation using custom identifier-based resolution.
	 * Returns the output parameters for validation via {@link #validateSuccess(Parameters)}.
	 *
	 * @param theSourceById if true, use source ID; if false, use source identifiers
	 * @param theTargetById if true, use target ID; if false, use target identifiers
	 * @return the operation output parameters
	 */
	@Nonnull
	public Parameters callMergeOperation(boolean theSourceById, boolean theTargetById) {
		// Build parameters with custom identifier resolution
		MergeTestParameters params = buildMergeOperationParameters(theSourceById, theTargetById);

		// Call merge operation via helper and return result
		return myHelper.callMergeOperation(getResourceTypeName(), params, myAsync);
	}

	/**
	 * Builds undo-merge operation parameters using custom identifier-based resolution.
	 *
	 * @param theSourceById if true, use source ID; if false, use source identifiers
	 * @param theTargetById if true, use target ID; if false, use target identifiers
	 * @return the operation input parameters
	 */
	@Nonnull
	public Parameters buildUndoMergeParameters(boolean theSourceById, boolean theTargetById) {
		return buildUndoMergeParameters(theSourceById, theTargetById, myHelper.getParameterNames());
	}

	@Nonnull
	public Parameters buildUndoMergeParameters(
			boolean theSourceById,
			boolean theTargetById,
			@Nonnull AbstractMergeOperationInputParameterNames theParameterNames) {

		Parameters params = new Parameters();

		if (theSourceById) {
			params.addParameter()
					.setName(theParameterNames.getSourceResourceParameterName())
					.setValue(new Reference(getVersionlessSourceId()));
		} else {
			for (Identifier identifier : getSourceIdentifiers()) {
				params.addParameter()
						.setName(theParameterNames.getSourceIdentifiersParameterName())
						.setValue(identifier);
			}
		}

		if (theTargetById) {
			params.addParameter()
					.setName(theParameterNames.getTargetResourceParameterName())
					.setValue(new Reference(getVersionlessTargetId()));
		} else {
			for (Identifier identifier : getTargetIdentifiers()) {
				params.addParameter()
						.setName(theParameterNames.getTargetIdentifiersParameterName())
						.setValue(identifier);
			}
		}

		return params;
	}

	/**
	 * Executes the undo-merge operation using this scenario's configuration.
	 * Uses resource IDs by default for both source and target.
	 *
	 * @return the operation output parameters
	 */
	@Nonnull
	public Parameters callUndoMergeOperation() {
		return callUndoMergeOperation(true, true);
	}

	/**
	 * Executes the undo-merge operation using custom identifier-based resolution.
	 *
	 * @param theSourceById if true, use source ID; if false, use source identifiers
	 * @param theTargetById if true, use target ID; if false, use target identifiers
	 * @return the operation output parameters
	 */
	@Nonnull
	public Parameters callUndoMergeOperation(boolean theSourceById, boolean theTargetById) {
		Parameters undoParams = buildUndoMergeParameters(theSourceById, theTargetById);
		return myHelper.callUndoMergeOperation(getResourceTypeName(), undoParams);
	}

	/**
	 * Add result resource to parameters if configured.
	 */
	private void addResultResourceIfNeeded(@Nonnull MergeTestParameters theParams) {
		if (myCreateResultResource) {
			// Create result resource with result resource identifiers
			// (defaults to target identifiers + extra identifier to ensure it's different from target)
			T result = createResource(getResultResourceIdentifiers());
			// Result resource must have same ID as target for validation
			result.setId(getVersionlessTargetId());

			// Add "replaces" link only when deleteSource=false
			// (validation requires this link when source is kept, but forbids it when source is deleted)
			if (!myDeleteSource) {
				addReplacesLinkToResource(result, getVersionlessSourceId());
			}

			theParams.resultResource(result);
		}
	}

	@Nonnull
	public T readResource(@Nonnull IIdType theId) {
		@SuppressWarnings("unchecked")
		IFhirResourceDao<T> dao = myDaoRegistry.getResourceDao(theId.getResourceType());
		return dao.read(theId, myRequestDetails);
	}

	public void addReplacesLinkToResource(@Nonnull T theResource, @Nonnull IIdType theTargetId) {
		IResourceLinkService linkService = myLinkServiceFactory.getServiceForResource(theResource);
		Reference targetRef = new Reference(theTargetId.toVersionless());
		linkService.addReplacesLink(theResource, targetRef);
	}

	/**
	 * Orchestrates complete validation of successful merge operation.
	 * Handles validation flow based on scenario configuration (preview, async, etc.).
	 *
	 * @param theOutParams the output parameters from {@link #callMergeOperation()}
	 */
	public void validateSuccess(@Nonnull Parameters theOutParams) {
		// Preview mode: validate preview outcome and early return
		if (myPreview) {
			validatePreviewOutcome(theOutParams);
			assertReferencesNotUpdated();
			return; // Preview mode doesn't make actual changes
		}

		// Execute mode: validate based on async vs sync
		if (myAsync) {
			validateAsyncOperationOutcome(theOutParams);
			myHelper.waitForAsyncTaskCompletion(theOutParams);
			validateTaskOutput(theOutParams);
		} else {
			validateSyncMergeOutcome(theOutParams);
		}

		// Validate final resource states (for both async and sync)
		validateResourcesAfterMerge();
	}

	/**
	 * Validates all resources after a merge operation.
	 * Comprehensive validation that checks source state, target state, and reference updates.
	 */
	public void validateResourcesAfterMerge() {
		validateResourcesAfterMerge(true);
	}

	/**
	 * Validates all resources after a merge operation with control over target update expectation.
	 * Comprehensive validation that checks source state, target state, and reference updates.
	 *
	 * @param theExpectTargetToBeUpdated whether the target resource is expected to have been updated (version incremented)
	 */
	public void validateResourcesAfterMerge(boolean theExpectTargetToBeUpdated) {
		IIdType expectedVersionedSourceId = getVersionlessSourceId().withVersion("2");
		String targetVersion = theExpectTargetToBeUpdated ? "2" : "1";
		IIdType expectedVersionedTargetId = getVersionlessTargetId().withVersion(targetVersion);

		List<IIdType> flatReferencingIds = getAllReferencingResources().values().stream()
				.flatMap(List::stream)
				.toList();

		Set<String> expectedProvenanceTargets =
				computeExpectedProvenanceTargets(expectedVersionedSourceId, expectedVersionedTargetId);

		myHelper.validateResourcesAfterMerge(
				myMergeTestParameters,
				expectedVersionedSourceId,
				expectedVersionedTargetId,
				flatReferencingIds,
				expectedProvenanceTargets,
				getExpectedIdentifiersOnTargetAfterMerge(),
				myExpectedProvenanceAgents);
	}

	// ================================================
	// OPERATION OUTCOME VALIDATION
	// ================================================

	public void validateSyncMergeOutcome(@Nonnull Parameters theOutParams) {
		myHelper.validateSyncMergeOutcome(theOutParams, myInputParameters, getVersionlessTargetId());
	}

	/**
	 * Validates the completed async task output after job finishes.
	 * Should be called AFTER waitForAsyncTaskCompletion().
	 *
	 * @param theOutParams the original output parameters from merge operation
	 */
	public void validateTaskOutput(@Nonnull Parameters theOutParams) {
		Task task = (Task)
				theOutParams.getParameter(OPERATION_MERGE_OUTPUT_PARAM_TASK).getResource();

		// Re-fetch task to get final state with outputs
		IFhirResourceDao<Task> taskDao = myDaoRegistry.getResourceDao(Task.class);
		task = taskDao.read(task.getIdElement(), myRequestDetails);

		assertThat(task.getStatus()).isEqualTo(Task.TaskStatus.COMPLETED);
		ourLog.info(
				"Complete Task: {}",
				myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(task));

		// Handle zero-reference case: when no references need patching, no output is created
		// The batch job completes successfully but doesn't create Task.output or Task.contained
		// because there are no patch results to include
		if (getTotalReferenceCount() == 0) {
			assertThat(task.getOutput()).isEmpty();
			assertThat(task.getContained()).isEmpty();
			ourLog.info("Verified: Task has no output (zero referencing resources)");
			return;
		}

		Task.TaskOutputComponent taskOutput = task.getOutputFirstRep();

		// Assert on the output type
		Coding taskOutputType = taskOutput.getType().getCodingFirstRep();
		assertEquals(RESOURCE_TYPES_SYSTEM, taskOutputType.getSystem());
		assertEquals("Bundle", taskOutputType.getCode());

		// Validate Task properly includes the patch result Bundle as a contained resource
		// FHIR Tasks return operation results using this pattern:
		// 1. Task.contained[] - the actual Bundle resource is embedded here with an ID (e.g., "b84941d0-...")
		// 2. Task.output[].valueReference - a reference pointing to that contained Bundle (e.g., "#b84941d0-...")
		// This validates the Bundle exists in contained[] and output properly references it (ensuring data integrity)
		List<Resource> containedResources = task.getContained();
		assertThat(containedResources).hasSize(1).element(0).isInstanceOf(Bundle.class);
		Bundle containedBundle = (Bundle) containedResources.get(0);
		Reference outputRef = (Reference) taskOutput.getValue();
		Bundle patchResultBundle = (Bundle) outputRef.getResource();
		assertTrue(containedBundle.equalsDeep(patchResultBundle));

		// Calculate expected patches from scenario data (use getTotalReferenceCount/getReferencingResourceTypes)
		int expectedPatches = getTotalReferenceCount();
		List<String> expectedResourceTypes = new ArrayList<>(getReferencingResourceTypes());
		ReplaceReferencesTestHelper.validatePatchResultBundle(
				patchResultBundle, expectedPatches, expectedResourceTypes);
	}

	/**
	 * Validates the OperationOutcome from an async merge operation response.
	 * The outcome should contain an informational message about async processing.
	 * Should be called BEFORE waitForAsyncTaskCompletion() to validate the initial response.
	 *
	 * @param theOutParams the output parameters from merge operation
	 */
	public void validateAsyncOperationOutcome(@Nonnull Parameters theOutParams) {
		// Validate input parameters returned
		validateInputParametersReturned(theOutParams);

		OperationOutcome outcome = (OperationOutcome)
				theOutParams.getParameter(OPERATION_MERGE_OUTPUT_PARAM_OUTCOME).getResource();
		assertThat(outcome.getIssue()).hasSize(1).element(0).satisfies(issue -> {
			assertThat(issue.getSeverity()).isEqualTo(OperationOutcome.IssueSeverity.INFORMATION);
			assertThat(issue.getDetails().getText())
					.isEqualTo("Merge request is accepted, and will be "
							+ "processed asynchronously. See task resource returned in this response for details.");
		});
	}

	public void validatePreviewOutcome(@Nonnull Parameters theOutParams) {
		// Calculate expected count: total referencing resources + 2 (source and target)
		int theExpectedUpdateCount = getTotalReferenceCount() + 2;

		OperationOutcome outcome =
				(OperationOutcome) theOutParams.getParameter("outcome").getResource();

		assertThat(outcome.getIssue()).hasSize(1).element(0).satisfies(issue -> {
			assertThat(issue.getSeverity()).isEqualTo(OperationOutcome.IssueSeverity.INFORMATION);
			assertThat(issue.getDetails().getText()).isEqualTo("Preview only merge operation - no issues detected");
			assertThat(issue.getDiagnostics()).isEqualTo("Merge would update " + theExpectedUpdateCount + " resources");
		});
	}

	/**
	 * Validates that input parameters are correctly returned in the merge operation output.
	 * This validation applies to both synchronous and asynchronous merge operations.
	 *
	 * @param theOutParams the output parameters from merge operation
	 */
	public void validateInputParametersReturned(@Nonnull Parameters theOutParams) {
		assertTestDataPersisted();

		// Extract returned input parameters
		Parameters returnedInput = (Parameters)
				theOutParams.getParameter(OPERATION_MERGE_OUTPUT_PARAM_INPUT).getResource();

		// Deep equality assertion
		assertThat(returnedInput.equalsDeep(myInputParameters))
				.as("Returned input parameters should match original input parameters")
				.isTrue();
	}

	// ================================================
	// REFERENCE VALIDATION
	// ================================================

	public void assertReferencesNotUpdated() {
		for (String resourceType : getReferencingResourceTypes()) {
			List<IIdType> referencingResourceIds = getReferencingResourceIds(resourceType);
			assertReferencesNotUpdated(referencingResourceIds);
		}
	}

	public void assertReferencesNotUpdated(@Nonnull List<IIdType> theReferencingResourceIds) {
		myHelper.assertReferencesNotUpdated(
				theReferencingResourceIds, getVersionlessSourceId(), getVersionlessTargetId());
	}

	// ================================================
	// PRIVATE HELPER METHODS
	// ================================================

	private Set<String> computeExpectedProvenanceTargets(
			@Nonnull IIdType theExpectedVersionedSourceId, @Nonnull IIdType theExpectedVersionedTargetId) {
		Set<String> expectedReferencingResourceIds = getAllReferencingResources().values().stream()
				.flatMap(List::stream)
				.map(id -> id.withVersion("2").toString())
				.collect(Collectors.toSet());

		Set<String> allExpectedTargets = new java.util.HashSet<>(expectedReferencingResourceIds);
		allExpectedTargets.add(theExpectedVersionedSourceId.toString());
		allExpectedTargets.add(theExpectedVersionedTargetId.toString());
		return allExpectedTargets;
	}

	/**
	 * Get the DAO for this resource type.
	 *
	 * @return The resource DAO
	 */
	@Nonnull
	private IFhirResourceDao<T> getDao() {
		return myDaoRegistry.getResourceDao(getResourceClass());
	}

	/**
	 * Assert that persistTestData() has been called.
	 *
	 * @throws IllegalStateException if persistTestData() has not been called
	 */
	private void assertTestDataPersisted() {
		if (!myIsTestDataPersisted) {
			throw new IllegalStateException(
					"persistTestData() must be called before accessing data. Use withReferences(), "
							+ "withResultResource(), etc. to configure, then call persistTestData().");
		}
	}
}
