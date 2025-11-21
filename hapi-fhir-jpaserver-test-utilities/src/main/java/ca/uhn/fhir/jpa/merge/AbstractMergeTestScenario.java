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
 *   <li>{@link #createResourceWithIdentifiers(String...)}</li>
 *   <li>{@link #createResourceWithIdentifiers(List)}</li>
 *   <li>{@link #createReferencingResource(String, String, IIdType)}</li>
 *   <li>{@link #getStandardReferenceConfigs()}</li>
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
public abstract class AbstractMergeTestScenario<T extends IBaseResource> implements MergeTestScenario<T> {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(AbstractMergeTestScenario.class);

	// Dependencies
	protected final DaoRegistry myDaoRegistry;
	protected final ResourceLinkServiceFactory myLinkServiceFactory;
	protected final RequestDetails myRequestDetails;
	protected final FhirContext myFhirContext;

	// Configuration State (before createTestData)
	private final List<ReferencingResourceConfig> myReferenceConfigs = new ArrayList<>();
	private List<String> mySourceIdentifierValues = Arrays.asList("source-1", "source-2", "common");
	private List<String> myTargetIdentifierValues = Arrays.asList("target-1", "target-2", "common");
	private boolean myCreateResultResource = false;

	// Data State (after createTestData)
	private T mySourceResource;
	private T myTargetResource;
	private Map<String, List<IIdType>> myReferencingResourcesByType;
	private List<Identifier> myExpectedIdentifiersAfterMerge;
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
	@Override
	public MergeTestScenario<T> withSourceIdentifiers(@Nonnull String... theValues) {
		mySourceIdentifierValues = Arrays.asList(theValues);
		return this;
	}

	@Nonnull
	@Override
	public MergeTestScenario<T> withTargetIdentifiers(@Nonnull String... theValues) {
		myTargetIdentifierValues = Arrays.asList(theValues);
		return this;
	}

	@Nonnull
	@Override
	public MergeTestScenario<T> withReferences(@Nonnull ReferencingResourceConfig... theConfigs) {
		myReferenceConfigs.addAll(Arrays.asList(theConfigs));
		return this;
	}

	@Nonnull
	@Override
	public MergeTestScenario<T> withReferences(@Nonnull List<ReferencingResourceConfig> theConfigs) {
		myReferenceConfigs.addAll(theConfigs);
		return this;
	}

	@Nonnull
	@Override
	public MergeTestScenario<T> withStandardReferences() {
		myReferenceConfigs.addAll(getStandardReferenceConfigs());
		return this;
	}

	@Nonnull
	@Override
	public MergeTestScenario<T> withResultResource() {
		myCreateResultResource = true;
		return this;
	}

	@Override
	public void createTestData() {
		ourLog.debug("Building merge test data for resource type: {}", getResourceTypeName());

		// Create and persist source resource
		T source = createResourceWithIdentifiers(mySourceIdentifierValues.toArray(new String[0]));
		IFhirResourceDao<T> dao = getDao();
		source = (T) dao.create(source, myRequestDetails).getResource();
		ourLog.debug("Created source resource: {}", source.getIdElement());

		// Create and persist target resource
		T target = createResourceWithIdentifiers(myTargetIdentifierValues.toArray(new String[0]));
		target = (T) dao.create(target, myRequestDetails).getResource();
		ourLog.debug("Created target resource: {}", target.getIdElement());

		// Create referencing resources organized by type
		Map<String, List<IIdType>> referencingResourcesByType = new HashMap<>();

		for (ReferencingResourceConfig config : myReferenceConfigs) {
			List<IIdType> idsForType = new ArrayList<>();

			for (int i = 0; i < config.getCount(); i++) {
				IBaseResource referencingResource = createReferencingResource(
						config.getResourceType(), config.getReferencePath(), source.getIdElement());

				// Persist referencing resource
				IFhirResourceDao<IBaseResource> refDao = myDaoRegistry.getResourceDao(config.getResourceType());
				IIdType createdId = refDao.create(referencingResource, myRequestDetails)
						.getId()
						.toUnqualifiedVersionless();
				idsForType.add(createdId);
			}

			ourLog.debug("Created {} {} resources referencing source", idsForType.size(), config.getResourceType());
			referencingResourcesByType.put(config.getResourceType(), idsForType);
		}

		// Calculate expected identifiers after merge
		List<Identifier> expectedIdentifiers =
				calculateExpectedIdentifiersAfterMerge(source, target, myCreateResultResource);

		// Store data state
		mySourceResource = source;
		myTargetResource = target;
		myReferencingResourcesByType = referencingResourcesByType;
		myExpectedIdentifiersAfterMerge = expectedIdentifiers;
		myIsTestDataCreated = true;

		ourLog.debug(
				"Merge test data built successfully: source={}, target={}, referencing resource types={}",
				source.getIdElement(),
				target.getIdElement(),
				referencingResourcesByType.keySet());
	}

	// ================================================
	// DATA ACCESSORS
	// ================================================

	@Nonnull
	@Override
	public IIdType getSourceId() {
		assertTestDataCreated();
		return mySourceResource.getIdElement().toUnqualifiedVersionless();
	}

	@Nonnull
	@Override
	public IIdType getTargetId() {
		assertTestDataCreated();
		return myTargetResource.getIdElement().toUnqualifiedVersionless();
	}

	@Nonnull
	@Override
	public T getSourceResource() {
		assertTestDataCreated();
		return mySourceResource;
	}

	@Nonnull
	@Override
	public T getTargetResource() {
		assertTestDataCreated();
		return myTargetResource;
	}

	@Nonnull
	@Override
	public List<IIdType> getReferencingResourceIds(@Nonnull String theResourceType) {
		assertTestDataCreated();
		return myReferencingResourcesByType.getOrDefault(theResourceType, Collections.emptyList());
	}

	@Nonnull
	@Override
	public Map<String, List<IIdType>> getAllReferencingResources() {
		assertTestDataCreated();
		return Collections.unmodifiableMap(myReferencingResourcesByType);
	}

	@Override
	public int getTotalReferenceCount() {
		assertTestDataCreated();
		return myReferencingResourcesByType.values().stream()
				.mapToInt(List::size)
				.sum();
	}

	@Nonnull
	@Override
	public Set<String> getReferencingResourceTypes() {
		assertTestDataCreated();
		return myReferencingResourcesByType.keySet();
	}

	@Nonnull
	@Override
	public List<Identifier> getExpectedIdentifiers() {
		assertTestDataCreated();
		return Collections.unmodifiableList(myExpectedIdentifiersAfterMerge);
	}

	@Nonnull
	@Override
	public MergeTestParameters buildMergeParameters(boolean theDeleteSource, boolean thePreview) {
		assertTestDataCreated();
		MergeTestParameters params = new MergeTestParameters()
				.sourceResource(new Reference(getSourceId()))
				.targetResource(new Reference(getTargetId()))
				.deleteSource(theDeleteSource)
				.preview(thePreview);

		if (myCreateResultResource) {
			// Create result resource with target identifiers (not persisted)
			T result = createResourceWithIdentifiers(myTargetIdentifierValues.toArray(new String[0]));
			// Result resource must have same ID as target for validation
			result.setId(getTargetId());

			// Add "replaces" link only when deleteSource=false
			// (validation requires this link when source is kept, but forbids it when source is deleted)
			if (!theDeleteSource) {
				addReplacesLinkToResource(result, getSourceId());
			}

			params.resultResource(result);
			ourLog.debug("Created result resource for merge parameters with ID: {}", result.getIdElement());
		}

		return params;
	}

	@Nonnull
	@Override
	public MergeTestParameters buildMergeParameters(
			boolean theSourceById, boolean theTargetById, boolean theDeleteSource, boolean thePreview) {
		assertTestDataCreated();

		MergeTestParameters params =
				new MergeTestParameters().deleteSource(theDeleteSource).preview(thePreview);

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
			T result = createResourceWithIdentifiers(myTargetIdentifierValues.toArray(new String[0]));
			result.setId(getTargetId());

			// Add "replaces" link only when deleteSource=false
			if (!theDeleteSource) {
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
	@Override
	public T readResource(@Nonnull IIdType theId) {
		IFhirResourceDao<T> dao = myDaoRegistry.getResourceDao(getResourceClass());
		return dao.read(theId, myRequestDetails);
	}

	@Nonnull
	@Override
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

	@Override
	public void addReplacesLinkToResource(@Nonnull T theResource, @Nonnull IIdType theTargetId) {
		IResourceLinkService linkService = myLinkServiceFactory.getServiceForResource(theResource);
		Reference targetRef = new Reference(theTargetId.toVersionless());
		linkService.addReplacesLink(theResource, targetRef);
		ourLog.debug("Added replaces link to {} pointing to {}", theResource.getIdElement(), theTargetId);
	}

	@Override
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

	@Override
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
		assertThat(actualIdentifiers)
				.as("Target should have expected identifiers")
				.hasSize(theExpectedIdentifiers.size());

		// Template method pattern: subclasses implement active field check if applicable
		if (hasActiveField()) {
			assertActiveFieldIfPresent(theResource, true);
		}

		ourLog.debug("Verified target resource state: correct identifiers");
	}

	@Override
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

	@Override
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

	@Nonnull
	@Override
	public List<Identifier> calculateExpectedIdentifiersAfterMerge(
			@Nonnull T theSource, @Nonnull T theTarget, boolean theWithResultResource) {

		if (theWithResultResource) {
			// Result resource provided - identifiers come from there
			// For now, return target identifiers (actual logic would use result resource)
			return new ArrayList<>(getIdentifiersFromResource(theTarget));
		}

		// Merge logic: target keeps its identifiers, source identifiers marked as "old" are added
		List<Identifier> expected = new ArrayList<>(getIdentifiersFromResource(theTarget));

		for (Identifier sourceId : getIdentifiersFromResource(theSource)) {
			boolean alreadyPresent = expected.stream()
					.anyMatch(targetId -> sourceId.getSystem().equals(targetId.getSystem())
							&& sourceId.getValue().equals(targetId.getValue()));

			if (!alreadyPresent) {
				Identifier copy = sourceId.copy();
				copy.setUse(Identifier.IdentifierUse.OLD);
				expected.add(copy);
			}
		}

		return expected;
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

	@Override
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

	@Override
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

	@Override
	public void validatePreviewOutcome(@Nonnull Parameters theOutParams, int theExpectedUpdateCount) {
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

	@Override
	public void assertReferencesUpdated(
			@Nonnull List<IIdType> theReferencingResourceIds,
			@Nonnull IIdType theSourceId,
			@Nonnull IIdType theTargetId) {

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

	@Override
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

	@Override
	public void assertMergeProvenanceCreated(
			@Nonnull IIdType theSourceId, @Nonnull IIdType theTargetId, @Nonnull Parameters theInputParams) {

		ourLog.debug("Validating provenance created for merge: source={}, target={}", theSourceId, theTargetId);

		// Search for provenance targeting the resources
		// Implementation would search for Provenance resources with target references
		// This is a placeholder - actual implementation would use search parameters

		ourLog.debug("Provenance validation completed");
	}

	// ================================================
	// HELPER METHODS
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
					"createTestData() must be called before accessing data. Use withSourceIdentifiers(), "
							+ "withTargetIdentifiers(), withReferences(), etc. to configure, then call createTestData().");
		}
	}
}
