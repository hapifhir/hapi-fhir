// Created by claude-sonnet-4-5
package ca.uhn.fhir.jpa.merge;

/*-
 * #%L
 * HAPI FHIR JPA Server Test Utilities
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

import ca.uhn.fhir.batch2.jobs.merge.IResourceLinkService;
import ca.uhn.fhir.batch2.jobs.merge.ResourceLinkServiceFactory;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.replacereferences.ReplaceReferencesTestHelper;
import ca.uhn.fhir.model.api.IProvenanceAgent;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.util.FhirTerser;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseReference;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static ca.uhn.fhir.jpa.provider.ReplaceReferencesSvcImpl.RESOURCE_TYPES_SYSTEM;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_MERGE_OUTPUT_PARAM_INPUT;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_MERGE_OUTPUT_PARAM_OUTCOME;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_MERGE_OUTPUT_PARAM_RESULT;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_MERGE_OUTPUT_PARAM_TASK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Abstract base class for merge test scenarios.
 *
 * <p>This class provides a unified implementation combining three responsibilities:
 * <ol>
 *   <li><b>Builder</b>: Fluent API for configuring test data</li>
 *   <li><b>Data Holder</b>: Stores created resources after createTestData()</li>
 *   <li><b>Strategy</b>: Common implementations for resource operations and validations</li>
 * </ol>
 *
 * <p>Subclasses only need to implement resource-specific methods:
 * <ul>
 *   <li>{@link #getResourceTypeName()}</li>
 *   <li>{@link #getResourceClass()}</li>
 *   <li>{@link #createResource(List)}</li>
 *   <li>{@link #createReferencingResource(String, IIdType)}</li>
 *   <li>{@link #assertActiveFieldIfSupported(IBaseResource, boolean)}</li>
 * </ul>
 *
 * <p>The scenario has two states:
 * <ul>
 *   <li><b>Configuration State</b>: Before createTestData() - builder methods available</li>
 *   <li><b>Data State</b>: After createTestData() - data accessors available</li>
 * </ul>
 *
 * @param <T> the FHIR resource type this scenario handles
 */
public abstract class AbstractMergeTestScenario<T extends IBaseResource> {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(AbstractMergeTestScenario.class);

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
	private T mySourceResource;
	private T myTargetResource;
	private Map<String, List<IIdType>> myReferencingResourcesByType;
	private boolean myIsTestDataPersisted = false;
	private Parameters myInputParameters;

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

	// ================================================
	// BUILDER METHODS
	// ================================================

	@Nonnull
	public AbstractMergeTestScenario<T> withReferences(@Nonnull List<ReferencingTestResourceType> theConfigs) {
		myReferencingTestResourceTypes.addAll(theConfigs);
		return this;
	}

	/**
	 * Configure scenario with one referencing resource of the primary type.
	 * Subclasses define which resource type and count are used.
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
		ourLog.debug("Building merge test data for resource type: {}", getResourceTypeName());

		// Create and persist source resource
		T source = createResource(mySourceIdentifiers);
		IFhirResourceDao<T> dao = getDao();
		source = (T) dao.create(source, myRequestDetails).getResource();

		// Create and persist target resource
		T target = createResource(myTargetIdentifiers);
		target = (T) dao.create(target, myRequestDetails).getResource();

		// Create referencing resources organized by type
		Map<String, List<IIdType>> referencingResourcesByType = new HashMap<>();

		for (ReferencingTestResourceType resourceType : myReferencingTestResourceTypes) {
			List<IIdType> idsForType = new ArrayList<>();

			for (int i = 0; i < resourceType.getCount(); i++) {
				IBaseResource referencingResource = createReferencingResource(
						resourceType.getResourceType(), source.getIdElement().toUnqualifiedVersionless());

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
		mySourceResource = source;
		myTargetResource = target;
		myReferencingResourcesByType = referencingResourcesByType;
		myIsTestDataPersisted = true;

		ourLog.info(
				"Merge test data built successfully: source={}, target={}, referencing resource types={}",
				source.getIdElement(),
				target.getIdElement(),
				referencingResourcesByType.keySet());
	}

	// ================================================
	// DATA ACCESSORS
	// ================================================

	@Nonnull
	public IIdType getVersionlessSourceId() {
		assertTestDataPersisted();
		return mySourceResource.getIdElement().toUnqualifiedVersionless();
	}

	@Nonnull
	public IIdType getVersionlessTargetId() {
		assertTestDataPersisted();
		return myTargetResource.getIdElement().toUnqualifiedVersionless();
	}

	@Nonnull
	public T getSourceResource() {
		assertTestDataPersisted();
		return mySourceResource;
	}

	@Nonnull
	public T getTargetResource() {
		assertTestDataPersisted();
		return myTargetResource;
	}

	@Nonnull
	public List<IIdType> getReferencingResourceIds(@Nonnull String theResourceType) {
		assertTestDataPersisted();
		return myReferencingResourcesByType.getOrDefault(theResourceType, Collections.emptyList());
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
	 * Get identifiers for result resource, defaulting to target identifiers plus an extra identifier
	 * to ensure the result resource is different from target.
	 */
	@Nonnull
	private List<Identifier> getResultResourceIdentifiers() {
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
	public List<Identifier> getExpectedIdentifiers() {
		assertTestDataPersisted();

		if (myCreateResultResource) {
			// Result resource provided - identifiers come from result resource identifiers
			return new ArrayList<>(getResultResourceIdentifiers());
		}

		// Merge logic: target keeps its identifiers, source identifiers marked as "old" are added
		List<Identifier> expected = new ArrayList<>(myTargetIdentifiers);

		// Add source identifiers marked as "old" if not already present in target
		for (Identifier sourceId : mySourceIdentifiers) {
			boolean isCommonIdentifier = myTargetIdentifiers.stream()
					.anyMatch(targetId -> sourceId.getSystem().equals(targetId.getSystem())
							&& sourceId.getValue().equals(targetId.getValue()));

			if (!isCommonIdentifier) {
				Identifier copy = sourceId.copy();
				copy.setUse(Identifier.IdentifierUse.OLD);
				expected.add(copy);
			}
		}

		return expected;
	}

	@Nonnull
	public MergeTestParameters buildMergeOperationParameters() {
		return buildMergeOperationParameters(true, true);
	}

	@Nonnull
	public MergeTestParameters buildMergeOperationParameters(boolean theSourceById, boolean theTargetById) {
		assertTestDataPersisted();

		MergeTestParameters params =
				new MergeTestParameters().deleteSource(myDeleteSource).preview(myPreview);

		if (theSourceById) {
			params.sourceResource(new Reference(getVersionlessSourceId()));
		} else {
			params.sourceIdentifiers(getIdentifiersFromResource(mySourceResource));
		}

		if (theTargetById) {
			params.targetResource(new Reference(getVersionlessTargetId()));
		} else {
			params.targetIdentifiers(getIdentifiersFromResource(myTargetResource));
		}

		addResultResourceIfNeeded(params);
		myInputParameters = params.asParametersResource();
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
		assertTestDataPersisted();

		// Build parameters with custom identifier resolution
		MergeTestParameters params = buildMergeOperationParameters(theSourceById, theTargetById);

		// Call merge operation via helper and return result
		return myHelper.callMergeOperation(getResourceTypeName(), params, myAsync);
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
			ourLog.debug("Created result resource for merge parameters with ID: {}", result.getIdElement());
		}
	}

	// ================================================
	// STRATEGY METHODS - Common Implementations
	// ================================================

	@Nonnull
	public T readResource(@Nonnull IIdType theId) {
		IFhirResourceDao<T> dao = myDaoRegistry.getResourceDao(getResourceClass());
		return dao.read(theId, myRequestDetails);
	}

	@Nonnull
	public List<Identifier> getIdentifiersFromResource(@Nonnull T theResource) {
		// Use FhirTerser to extract identifiers - works for any resource type with identifier field
		List<Identifier> identifiers = new ArrayList<>();
		List<IBase> values = myFhirContext.newTerser().getValues(theResource, "identifier");
		for (IBase value : values) {
			if (value instanceof Identifier) {
				identifiers.add((Identifier) value);
			}
		}
		return identifiers;
	}

	public void addReplacesLinkToResource(@Nonnull T theResource, @Nonnull IIdType theTargetId) {
		IResourceLinkService linkService = myLinkServiceFactory.getServiceForResource(theResource);
		Reference targetRef = new Reference(theTargetId.toVersionless());
		linkService.addReplacesLink(theResource, targetRef);
		ourLog.debug("Added replaces link to {} pointing to {}", theResource.getIdElement(), theTargetId);
	}

	public void assertSourceResourceState() {
		assertTestDataPersisted();

		IIdType versionlessSourceId = getVersionlessSourceId();
		IIdType versionlessTargetId = getVersionlessTargetId();

		if (myDeleteSource) {
			// Resource should not exist
			assertThatThrownBy(() -> readResource(versionlessSourceId))
					.as("Source resource should be deleted")
					.isInstanceOf(ResourceGoneException.class);
		} else {
			// Read the resource internally
			T source = readResource(versionlessSourceId);

			// Resource should have replaced-by link
			IResourceLinkService linkService = myLinkServiceFactory.getServiceForResourceType(getResourceTypeName());
			List<IBaseReference> replacedByLinksRefs = linkService.getReplacedByLinks(source);

			assertThat(replacedByLinksRefs)
					.as("Source should have replaced-by link")
					.hasSize(1)
					.element(0)
					.satisfies(link -> assertThat(link.getReferenceElement()).isEqualTo(versionlessTargetId));

			// active field on the source should be set to false, if the resourceType has an active field
			assertActiveFieldIfSupported(source, false);
		}
	}

	public void assertTargetResourceState() {
		assertTestDataPersisted();

		// Read target resource
		T target = readResource(getVersionlessTargetId());

		// Should have replaces link only if source was not deleted
		// (when source is deleted, we don't want a dangling reference)
		if (!myDeleteSource) {
			IResourceLinkService linkService = myLinkServiceFactory.getServiceForResourceType(getResourceTypeName());
			List<IBaseReference> replacesLinksRefs = linkService.getReplacesLinks(target);

			assertThat(replacesLinksRefs)
					.as("Target should have replaces link when source not deleted")
					.hasSize(1)
					.element(0)
					.satisfies(link -> assertThat(link.getReferenceElement()).isEqualTo(getVersionlessSourceId()));
		}

		// Should have expected identifiers
		List<Identifier> actualIdentifiers = getIdentifiersFromResource(target);
		assertIdentifiers(actualIdentifiers, getExpectedIdentifiers());
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
		assertTestDataPersisted();

		// Validate source resource state
		assertSourceResourceState();

		// Validate target resource state
		assertTargetResourceState();

		// Validate references to source in referencing resources now point to target (i.e. replace-refences worked)
		for (String resourceType : getReferencingResourceTypes()) {
			List<IIdType> referencingIds = getReferencingResourceIds(resourceType);
			if (!referencingIds.isEmpty()) {
				assertReferencesUpdated(resourceType);
			}
		}

		// Validate provenance created for merge operation
		assertMergeProvenanceCreated(theExpectTargetToBeUpdated);
	}

	public void assertLinksPresent(
			@Nonnull T theResource, @Nonnull List<IIdType> theExpectedLinks, @Nonnull String theLinkType) {

		IResourceLinkService linkService = myLinkServiceFactory.getServiceForResourceType(getResourceTypeName());

		List<IBaseReference> actualLinksRefs;
		if ("replaces".equals(theLinkType)) {
			actualLinksRefs = linkService.getReplacesLinks(theResource);
		} else if ("replaced-by".equals(theLinkType)) {
			actualLinksRefs = linkService.getReplacedByLinks(theResource);
		} else {
			throw new IllegalArgumentException("Unknown link type: " + theLinkType);
		}

		// Convert IBaseReference list to IIdType list for comparison
		List<IIdType> actualLinks = actualLinksRefs.stream()
				.map(IBaseReference::getReferenceElement)
				.map(IIdType::toUnqualifiedVersionless)
				.collect(Collectors.toList());

		List<IIdType> expectedLinksUnversioned =
				theExpectedLinks.stream().map(IIdType::toUnqualifiedVersionless).collect(Collectors.toList());

		assertThat(actualLinks)
				.as("Resource should have expected %s links", theLinkType)
				.containsExactlyInAnyOrderElementsOf(expectedLinksUnversioned);

		ourLog.debug("Verified {} links present: {}", theLinkType, actualLinks.size());
	}

	public void assertNoLinks(@Nonnull T theResource) {
		IResourceLinkService linkService = myLinkServiceFactory.getServiceForResourceType(getResourceTypeName());

		List<IBaseReference> replacesLinks = linkService.getReplacesLinks(theResource);
		List<IBaseReference> replacedByLinks = linkService.getReplacedByLinks(theResource);

		assertThat(replacesLinks).as("Resource should have no replaces links").isEmpty();

		assertThat(replacedByLinks)
				.as("Resource should have no replaced-by links")
				.isEmpty();

		ourLog.debug("Verified no links present on resource");
	}

	public void assertIdentifiers(
			@Nonnull List<Identifier> theActualIdentifiers, @Nonnull List<Identifier> theExpectedIdentifiers) {
		assertThat(theActualIdentifiers).hasSize(theExpectedIdentifiers.size());

		for (int i = 0; i < theExpectedIdentifiers.size(); i++) {
			Identifier expectedIdentifier = theExpectedIdentifiers.get(i);
			Identifier actualIdentifier = theActualIdentifiers.get(i);
			assertThat(actualIdentifier.equalsDeep(expectedIdentifier)).isTrue();
		}

		ourLog.debug("Verified {} identifiers match expected", theActualIdentifiers.size());
	}

	/**
	 * Template method for subclasses to implement active field assertions.
	 *
	 * <p>Subclasses must implement this method to either:
	 * <ul>
	 *   <li>Perform active field validation if the resource supports it (e.g., Practitioner)</li>
	 *   <li>Provide an explicit no-op if the resource doesn't support it (e.g., Observation)</li>
	 * </ul>
	 *
	 * <p>This forces implementers to consciously decide and document whether
	 * their resource type supports the active field.
	 *
	 * @param theResource the resource to check
	 * @param theExpectedValue the expected value of the active field (true for target, false for source)
	 */
	protected abstract void assertActiveFieldIfSupported(@Nonnull T theResource, boolean theExpectedValue);

	// ================================================
	// OPERATION OUTCOME VALIDATION
	// ================================================

	public void validateSyncMergeOutcome(@Nonnull Parameters theOutParams) {
		// Validate input parameters returned
		validateInputParametersReturned(theOutParams);

		// Assert outcome
		OperationOutcome outcome = (OperationOutcome)
				theOutParams.getParameter(OPERATION_MERGE_OUTPUT_PARAM_OUTCOME).getResource();
		assertThat(outcome.getIssue()).hasSize(1).element(0).satisfies(issue -> {
			assertThat(issue.getSeverity()).isEqualTo(OperationOutcome.IssueSeverity.INFORMATION);
			assertThat(issue.getDetails().getText()).isEqualTo("Merge operation completed successfully.");
		});

		// In sync mode, the result resource is returned in the output,
		// assert what is returned is the same as the one in the db
		T targetResourceInOutput = (T)
				theOutParams.getParameter(OPERATION_MERGE_OUTPUT_PARAM_RESULT).getResource();
		T targetResourceReadFromDB = readResource(getVersionlessTargetId());
		IParser parser = myFhirContext.newJsonParser();
		assertThat(parser.encodeResourceToString(targetResourceInOutput))
				.isEqualTo(parser.encodeResourceToString(targetResourceReadFromDB));
	}

	public void validateAsyncTaskCreated(@Nonnull Parameters theOutParams) {
		assertThat(theOutParams.getParameter())
				.as("Async merge should return 3 parameters")
				.hasSize(3);

		Task task = (Task) theOutParams.getParameter("task").getResource();
		assertThat(task).as("Task should be present").isNotNull();

		assertThat(task.getIdElement().hasVersionIdPart())
				.as("Task should not have version")
				.isFalse();

		OperationOutcome outcome =
				(OperationOutcome) theOutParams.getParameter("outcome").getResource();

		assertThat(outcome.getIssue()).hasSize(1).element(0).satisfies(issue -> {
			assertThat(issue.getSeverity()).isEqualTo(OperationOutcome.IssueSeverity.INFORMATION);
			assertThat(issue.getDetails().getText()).contains("asynchronously");
		});

		ourLog.debug("Async task creation validated successfully: {}", task.getId());
	}

	/**
	 * Validates the completed async task output after job finishes.
	 * Should be called AFTER waitForAsyncTaskCompletion().
	 * Copied from PatientMergeR4Test.validateTaskOutput().
	 *
	 * @param theOutParams the original output parameters from merge operation
	 */
	public void validateTaskOutput(@Nonnull Parameters theOutParams) {
		assertTestDataPersisted();

		Task task = (Task)
				theOutParams.getParameter(OPERATION_MERGE_OUTPUT_PARAM_TASK).getResource();

		// Re-fetch task to get final state with outputs (use myDaoRegistry + myRequestDetails)
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
		assertTestDataPersisted();

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
	 * <p>The merge operation must echo back the original input parameters in the response,
	 * which allows clients to correlate requests with responses and provides an audit trail.
	 *
	 * @param theOutParams the output parameters from merge operation
	 */
	public void validateInputParametersReturned(@Nonnull Parameters theOutParams) {
		assertTestDataPersisted();

		// Extract returned input parameters
		Parameters returnedInput = (Parameters)
				theOutParams.getParameter(OPERATION_MERGE_OUTPUT_PARAM_INPUT).getResource();

		// Special handling for result-resource parameter if present
		// Log both resources for debugging if they don't match
		if (myCreateResultResource) {
			Resource originalResultResource =
					(Resource) myInputParameters.getParameter("result-resource").getResource();
			Resource returnedResultResource =
					(Resource) returnedInput.getParameter("result-resource").getResource();

			if (!originalResultResource.equalsDeep(returnedResultResource)) {
				ourLog.info(
						"Original result-resource:\n{}",
						myFhirContext
								.newJsonParser()
								.setPrettyPrint(true)
								.encodeResourceToString(originalResultResource));
				ourLog.info(
						"Returned result-resource:\n{}",
						myFhirContext
								.newJsonParser()
								.setPrettyPrint(true)
								.encodeResourceToString(returnedResultResource));
			}
		}

		// Deep equality assertion
		assertThat(returnedInput.equalsDeep(myInputParameters))
				.as("Returned input parameters should match original input parameters")
				.isTrue();
	}

	// ================================================
	// REFERENCE VALIDATION
	// ================================================

	/**
	 * Extract all reference strings from a resource.
	 *
	 * @param theRefId Resource ID to read and extract references from
	 * @return List of reference strings (resourceType/id format)
	 */
	private List<String> extractReferenceStrings(IIdType theRefId) {
		IFhirResourceDao<IBaseResource> dao = myDaoRegistry.getResourceDao(theRefId.getResourceType());
		IBaseResource resource = dao.read(theRefId, myRequestDetails);

		FhirTerser terser = myFhirContext.newTerser();
		List<IBaseReference> allRefs = terser.getAllPopulatedChildElementsOfType(resource, IBaseReference.class);

		List<String> refStrings = allRefs.stream()
				.map(ref -> ref.getReferenceElement().getValue())
				.filter(Objects::nonNull)
				.toList();

		ourLog.info("Resource {} contains references: {}", theRefId, refStrings);

		return refStrings;
	}

	public void assertReferencesUpdated(@Nonnull List<IIdType> theReferencingResourceIds) {
		assertTestDataPersisted();

		for (IIdType refId : theReferencingResourceIds) {
			List<String> refStrings = extractReferenceStrings(refId);

			// Verify references contain target and not source
			assertThat(refStrings)
					.as(
							"Resource %s should contain reference to target %s",
							refId, getVersionlessTargetId().getValue())
					.contains(getVersionlessTargetId().getValue());

			assertThat(refStrings)
					.as(
							"Resource %s should not contain reference to source %s",
							refId, getVersionlessSourceId().getValue())
					.doesNotContain(getVersionlessSourceId().getValue());
		}
	}

	public void assertReferencesUpdated(@Nonnull String theResourceType) {
		assertTestDataPersisted();
		List<IIdType> referencingResourceIds = getReferencingResourceIds(theResourceType);
		assertReferencesUpdated(referencingResourceIds);
	}

	public void assertReferencesNotUpdated() {
		assertTestDataPersisted();
		ourLog.debug("Validating references NOT updated in preview mode for source: {}", getVersionlessSourceId());

		for (String resourceType : myReferencingResourcesByType.keySet()) {
			for (IIdType refId : myReferencingResourcesByType.get(resourceType)) {
				List<String> refStrings = extractReferenceStrings(refId);

				// Verify references still contain source and not target
				assertThat(refStrings)
						.as(
								"Resource %s should still reference source %s in preview mode",
								refId, getVersionlessSourceId().getValue())
						.contains(getVersionlessSourceId().getValue());

				assertThat(refStrings)
						.as(
								"Resource %s should not reference target %s in preview mode",
								refId, getVersionlessTargetId().getValue())
						.doesNotContain(getVersionlessTargetId().getValue());
			}
		}

		ourLog.debug("Verified references not updated in preview mode");
	}

	// ================================================
	// PROVENANCE VALIDATION
	// ================================================
	public void assertMergeProvenanceCreated(boolean theExpectTargetToBeUpdated) {
		assertTestDataPersisted();

		// Source is always version 2 in provenance even when deleteSource=true
		// (provenance increments version to match what delete operation will create)
		IIdType expectedSourceId = getVersionlessSourceId().withVersion("2");

		// Target version depends on whether it was actually updated during merge
		String expectedTargetVersion = theExpectTargetToBeUpdated ? "2" : "1";
		IIdType expectedTargetId = getVersionlessTargetId().withVersion(expectedTargetVersion);

		// Calculate expected referencing resource IDs with versions
		// Referencing resources get updated during merge, so they should have version 2
		Set<String> expectedReferencingResourceIds = getAllReferencingResources().values().stream()
				.flatMap(List::stream)
				.map(id -> id.withVersion("2").toString())
				.collect(Collectors.toSet());

		// Delegate to ReplaceReferencesTestHelper for provenance validation
		ReplaceReferencesTestHelper helper = new ReplaceReferencesTestHelper(myFhirContext, myDaoRegistry);
		helper.assertMergeProvenance(
				myInputParameters,
				expectedSourceId,
				expectedTargetId,
				getTotalReferenceCount(),
				expectedReferencingResourceIds,
				myExpectedProvenanceAgents);
	}

	// ================================================
	// HELPER METHODS
	// ================================================

	// ================================================
	// ABSTRACT METHODS - Subclasses must implement
	// ================================================

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

	// ================================================
	// PRIVATE HELPER METHODS
	// ================================================

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
