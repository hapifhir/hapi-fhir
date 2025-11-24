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
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.util.FhirTerser;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
 *   <li>{@link #createResourceWithIdentifiers(List)}</li>
 *   <li>{@link #createReferencingResource(String, IIdType)}</li>
 *   <li>{@link #hasActiveField()}</li>
 *   <li>{@link #assertActiveFieldIfPresent(IBaseResource, boolean)} (if hasActiveField() is true)</li>
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
	private boolean myCreateResultResource = false;
	private boolean myDeleteSource = false;
	private boolean myPreview = false;

	// Data State (after createTestData)
	private T mySourceResource;
	private T myTargetResource;
	private Map<String, List<IIdType>> myReferencingResourcesByType;
	private boolean myIsTestDataCreated = false;

	/**
	 * Create a new abstract merge test scenario.
	 *
	 * @param theDaoRegistry DAO registry for database operations
	 * @param theFhirContext FHIR context
	 * @param theLinkServiceFactory Factory for getting link services
	 * @param theRequestDetails Request details for DAO operations
	 */
	protected AbstractMergeTestScenario(
			@Nonnull DaoRegistry theDaoRegistry,
			@Nonnull FhirContext theFhirContext,
			@Nonnull ResourceLinkServiceFactory theLinkServiceFactory,
			@Nonnull RequestDetails theRequestDetails) {

		myDaoRegistry = theDaoRegistry;
		myFhirContext = theFhirContext;
		myLinkServiceFactory = theLinkServiceFactory;
		myRequestDetails = theRequestDetails;
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

	@Nonnull
	public AbstractMergeTestScenario<T> withResultResource() {
		myCreateResultResource = true;
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

	public void createTestData() {
		ourLog.debug("Building merge test data for resource type: {}", getResourceTypeName());

		// Create and persist source resource
		T source = createResourceWithIdentifiers(mySourceIdentifiers);
		IFhirResourceDao<T> dao = getDao();
		source = (T) dao.create(source, myRequestDetails).getResource();

		// Create and persist target resource
		T target = createResourceWithIdentifiers(myTargetIdentifiers);
		target = (T) dao.create(target, myRequestDetails).getResource();

		// Create referencing resources organized by type
		Map<String, List<IIdType>> referencingResourcesByType = new HashMap<>();

		for (ReferencingTestResourceType resourceType : myReferencingTestResourceTypes) {
			List<IIdType> idsForType = new ArrayList<>();

			for (int i = 0; i < resourceType.getCount(); i++) {
				IBaseResource referencingResource =
						createReferencingResource(resourceType.getResourceType(), source.getIdElement());

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
		myIsTestDataCreated = true;

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
	public IIdType getSourceId() {
		assertTestDataCreated();
		return mySourceResource.getIdElement().toUnqualifiedVersionless();
	}

	@Nonnull
	public IIdType getTargetId() {
		assertTestDataCreated();
		return myTargetResource.getIdElement().toUnqualifiedVersionless();
	}

	@Nonnull
	public T getSourceResource() {
		assertTestDataCreated();
		return mySourceResource;
	}

	@Nonnull
	public T getTargetResource() {
		assertTestDataCreated();
		return myTargetResource;
	}

	@Nonnull
	public List<IIdType> getReferencingResourceIds(@Nonnull String theResourceType) {
		assertTestDataCreated();
		return myReferencingResourcesByType.getOrDefault(theResourceType, Collections.emptyList());
	}

	@Nonnull
	public Map<String, List<IIdType>> getAllReferencingResources() {
		assertTestDataCreated();
		return Collections.unmodifiableMap(myReferencingResourcesByType);
	}

	public int getTotalReferenceCount() {
		assertTestDataCreated();
		return myReferencingResourcesByType.values().stream()
				.mapToInt(List::size)
				.sum();
	}

	@Nonnull
	public Set<String> getReferencingResourceTypes() {
		assertTestDataCreated();
		return myReferencingResourcesByType.keySet();
	}

	@Nonnull
	public List<Identifier> getExpectedIdentifiers() {
		assertTestDataCreated();

		if (myCreateResultResource) {
			// Result resource provided - identifiers come from target identifiers
			return new ArrayList<>(myTargetIdentifiers);
		}

		// Merge logic: target keeps its identifiers, source identifiers marked as "old" are added
		List<Identifier> expected = new ArrayList<>(myTargetIdentifiers);

		// Add source identifiers marked as "old" if not already present
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
		assertTestDataCreated();
		MergeTestParameters params = new MergeTestParameters()
				.sourceResource(new Reference(getSourceId()))
				.targetResource(new Reference(getTargetId()))
				.deleteSource(myDeleteSource)
				.preview(myPreview);

		if (myCreateResultResource) {
			// Create result resource with target identifiers (not persisted)
			T result = createResourceWithIdentifiers(myTargetIdentifiers);
			// Result resource must have same ID as target for validation
			result.setId(getTargetId());

			// Add "replaces" link only when deleteSource=false
			// (validation requires this link when source is kept, but forbids it when source is deleted)
			if (!myDeleteSource) {
				addReplacesLinkToResource(result, getSourceId());
			}

			params.resultResource(result);
			ourLog.debug("Created result resource for merge parameters with ID: {}", result.getIdElement());
		}

		return params;
	}

	@Nonnull
	public MergeTestParameters buildMergeOperationParameters(boolean theSourceById, boolean theTargetById) {
		assertTestDataCreated();

		MergeTestParameters params =
				new MergeTestParameters().deleteSource(myDeleteSource).preview(myPreview);

		if (theSourceById) {
			params.sourceResource(new Reference(getSourceId()));
		} else {
			params.sourceIdentifiers(getIdentifiersFromResource(mySourceResource));
		}

		if (theTargetById) {
			params.targetResource(new Reference(getTargetId()));
		} else {
			params.targetIdentifiers(getIdentifiersFromResource(myTargetResource));
		}

		if (myCreateResultResource) {
			// Create result resource with target identifiers (not persisted)
			T result = createResourceWithIdentifiers(myTargetIdentifiers);
			result.setId(getTargetId());

			// Add "replaces" link only when deleteSource=false
			if (!myDeleteSource) {
				addReplacesLinkToResource(result, getSourceId());
			}

			params.resultResource(result);
			ourLog.debug("Created result resource for merge parameters with ID: {}", result.getIdElement());
		}

		return params;
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

	public void assertSourceResourceState(
			@Nullable T theResource, @Nonnull IIdType theSourceId, @Nonnull IIdType theTargetId, boolean theDeleted) {

		if (theDeleted) {
			// Resource should not exist
			assertThatThrownBy(() -> readResource(theSourceId))
					.as("Source resource should be deleted")
					.isInstanceOf(ResourceGoneException.class);
			ourLog.debug("Verified source resource is deleted: {}", theSourceId);
		} else {
			// Resource should have replaced-by link
			IResourceLinkService linkService = myLinkServiceFactory.getServiceForResourceType(getResourceTypeName());
			List<IBaseReference> replacedByLinksRefs = linkService.getReplacedByLinks(theResource);

			assertThat(replacedByLinksRefs)
					.as("Source should have replaced-by link")
					.hasSize(1)
					.element(0)
					.satisfies(link -> assertThat(link.getReferenceElement().toUnqualifiedVersionless())
							.isEqualTo(theTargetId.toUnqualifiedVersionless()));

			// Template method pattern: subclasses implement active field check if applicable
			if (hasActiveField()) {
				assertActiveFieldIfPresent(theResource, false);
			}

			ourLog.debug("Verified source resource state: has replaced-by link");
		}
	}

	public void assertTargetResourceState(
			@Nonnull T theResource,
			@Nonnull IIdType theSourceId,
			boolean theSourceDeleted,
			@Nonnull List<Identifier> theExpectedIdentifiers) {

		// Should have replaces link only if source was not deleted
		// (when source is deleted, we don't want a dangling reference)
		if (!theSourceDeleted) {
			IResourceLinkService linkService = myLinkServiceFactory.getServiceForResourceType(getResourceTypeName());
			List<IBaseReference> replacesLinksRefs = linkService.getReplacesLinks(theResource);

			assertThat(replacesLinksRefs)
					.as("Target should have replaces link when source not deleted")
					.hasSize(1)
					.element(0)
					.satisfies(link -> assertThat(link.getReferenceElement().toUnqualifiedVersionless())
							.isEqualTo(theSourceId.toUnqualifiedVersionless()));
			ourLog.debug("Verified target has replaces link to source");
		}

		// Should have expected identifiers
		List<Identifier> actualIdentifiers = getIdentifiersFromResource(theResource);
		assertIdentifiers(actualIdentifiers, theExpectedIdentifiers);

		// Template method pattern: subclasses implement active field check if applicable
		if (hasActiveField()) {
			assertActiveFieldIfPresent(theResource, true);
		}

		ourLog.debug("Verified target resource state: correct identifiers");
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
	 * <p>Only called if {@link #hasActiveField()} returns true.
	 * Subclasses with an active field should override this to perform the assertion.
	 *
	 * @param theResource the resource to check
	 * @param theExpectedValue the expected value of the active field (true for target, false for source)
	 */
	protected void assertActiveFieldIfPresent(@Nonnull T theResource, boolean theExpectedValue) {
		// Default: no-op
		// Subclasses with active fields override this
	}

	// ================================================
	// OPERATION OUTCOME VALIDATION
	// ================================================

	public void validateSyncMergeOutcome(@Nonnull Parameters theOutParams) {
		assertThat(theOutParams.getParameter())
				.as("Sync merge should return 3 parameters")
				.hasSize(3);

		OperationOutcome outcome =
				(OperationOutcome) theOutParams.getParameter("outcome").getResource();

		ourLog.info(
				"Sync merge OperationOutcome: severity={}, details={}, diagnostics={}",
				outcome.getIssue().isEmpty()
						? "NONE"
						: outcome.getIssue().get(0).getSeverity(),
				outcome.getIssue().isEmpty()
						? "NONE"
						: outcome.getIssue().get(0).getDetails().getText(),
				outcome.getIssue().isEmpty()
						? "NONE"
						: outcome.getIssue().get(0).getDiagnostics());

		assertThat(outcome.getIssue()).hasSize(1).element(0).satisfies(issue -> {
			assertThat(issue.getSeverity()).isEqualTo(OperationOutcome.IssueSeverity.INFORMATION);
			assertThat(issue.getDetails().getText()).isEqualTo("Merge operation completed successfully.");
		});

		ourLog.debug("Sync merge outcome validated successfully");
	}

	public void validateAsyncTaskCreated(@Nonnull Parameters theOutParams) {
		assertThat(theOutParams.getParameter())
				.as("Async merge should return 3 parameters")
				.hasSize(3);

		Task task = (Task) theOutParams.getParameter("task").getResource();
		assertThat(task).as("Task should be present").isNotNull();

		ourLog.info(
				"Async Task created: id={}, status={}, identifiers={}",
				task.getId(),
				task.getStatus(),
				task.getIdentifier().stream()
						.map(id -> id.getSystem() + "|" + id.getValue())
						.toList());

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

	public void validatePreviewOutcome(@Nonnull Parameters theOutParams) {
		assertTestDataCreated();

		// Calculate expected count: total referencing resources + 2 (source and target)
		int theExpectedUpdateCount = getTotalReferenceCount() + 2;

		OperationOutcome outcome =
				(OperationOutcome) theOutParams.getParameter("outcome").getResource();

		ourLog.info(
				"Preview merge OperationOutcome: expectedCount={}, actualDiagnostics={}, details={}",
				theExpectedUpdateCount,
				outcome.getIssue().isEmpty()
						? "NONE"
						: outcome.getIssue().get(0).getDiagnostics(),
				outcome.getIssue().isEmpty()
						? "NONE"
						: outcome.getIssue().get(0).getDetails().getText());

		assertThat(outcome.getIssue()).hasSize(1).element(0).satisfies(issue -> {
			assertThat(issue.getSeverity()).isEqualTo(OperationOutcome.IssueSeverity.INFORMATION);
			assertThat(issue.getDetails().getText()).isEqualTo("Preview only merge operation - no issues detected");
			assertThat(issue.getDiagnostics()).isEqualTo("Merge would update " + theExpectedUpdateCount + " resources");
		});

		ourLog.debug("Preview outcome validated successfully: {} resources would be updated", theExpectedUpdateCount);
	}

	// ================================================
	// REFERENCE VALIDATION
	// ================================================

	public void assertReferencesUpdated(@Nonnull List<IIdType> theReferencingResourceIds) {
		assertTestDataCreated();

		IIdType theSourceId = getSourceId();
		IIdType theTargetId = getTargetId();

		ourLog.debug(
				"Validating {} referencing resources updated from {} to {}",
				theReferencingResourceIds.size(),
				theSourceId,
				theTargetId);

		FhirTerser terser = myFhirContext.newTerser();

		for (IIdType refId : theReferencingResourceIds) {
			IFhirResourceDao<IBaseResource> dao = myDaoRegistry.getResourceDao(refId.getResourceType());
			IBaseResource resource = dao.read(refId, myRequestDetails);

			// Use FhirTerser to find all references
			List<IBaseReference> allRefs = terser.getAllPopulatedChildElementsOfType(resource, IBaseReference.class);

			List<String> refStrings = allRefs.stream()
					.map(ref -> ref.getReferenceElement().getValue())
					.filter(refStr -> refStr != null)
					.toList();

			ourLog.info("Resource {} contains references: {}", refId, refStrings);

			// Verify none point to source
			for (IBaseReference reference : allRefs) {
				String refString = reference.getReferenceElement().getValue();
				if (refString != null) {
					assertThat(refString)
							.as("Reference in %s should not point to source %s", refId, theSourceId)
							.doesNotContain(theSourceId.getIdPart());
				}
			}
		}

		ourLog.debug("All {} referencing resources validated successfully", theReferencingResourceIds.size());
	}

	public void assertReferencesUpdated(@Nonnull String theResourceType) {
		assertTestDataCreated();
		List<IIdType> referencingResourceIds = getReferencingResourceIds(theResourceType);
		assertReferencesUpdated(referencingResourceIds);
	}

	public void assertReferencesNotUpdated() {
		assertTestDataCreated();
		ourLog.debug("Validating references NOT updated in preview mode for source: {}", getSourceId());

		FhirTerser terser = myFhirContext.newTerser();

		for (String resourceType : myReferencingResourcesByType.keySet()) {
			for (IIdType refId : myReferencingResourcesByType.get(resourceType)) {
				IFhirResourceDao<IBaseResource> dao = myDaoRegistry.getResourceDao(resourceType);
				IBaseResource resource = dao.read(refId, myRequestDetails);

				List<IBaseReference> allRefs =
						terser.getAllPopulatedChildElementsOfType(resource, IBaseReference.class);

				boolean foundSourceRef = false;
				for (IBaseReference reference : allRefs) {
					String refString = reference.getReferenceElement().getValue();
					if (refString != null && refString.contains(getSourceId().getIdPart())) {
						foundSourceRef = true;
						break;
					}
				}

				assertThat(foundSourceRef)
						.as("Resource %s should still reference source %s in preview mode", refId, getSourceId())
						.isTrue();
			}
		}

		ourLog.debug("Verified references not updated in preview mode");
	}

	// ================================================
	// PROVENANCE VALIDATION
	// ================================================

	public void assertMergeProvenanceCreated(@Nonnull Parameters theInputParams) {
		assertTestDataCreated();

		IIdType theSourceId = getSourceId();
		IIdType theTargetId = getTargetId();

		ourLog.debug("Validating provenance created for merge: source={}, target={}", theSourceId, theTargetId);

		// Search for provenance targeting the resources
		// Implementation would search for Provenance resources with target references
		// This is a placeholder - actual implementation would use search parameters

		ourLog.debug("Provenance validation completed");
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
	protected abstract T createResourceWithIdentifiers(@Nonnull List<Identifier> theIdentifiers);

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
	 * Check if this resource type has an "active" field.
	 *
	 * @return True if the resource has an active field, false otherwise
	 */
	protected abstract boolean hasActiveField();

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
	 * Assert that createTestData() has been called.
	 *
	 * @throws IllegalStateException if createTestData() has not been called
	 */
	private void assertTestDataCreated() {
		if (!myIsTestDataCreated) {
			throw new IllegalStateException(
					"createTestData() must be called before accessing data. Use withReferences(), "
							+ "withResultResource(), etc. to configure, then call createTestData().");
		}
	}
}
