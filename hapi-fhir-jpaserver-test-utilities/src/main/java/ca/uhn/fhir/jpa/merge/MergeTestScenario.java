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

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Reference;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Container for a merge test scenario, including all created resources and expected outcomes.
 *
 * This class holds the complete state of a test scenario after test data has been created and
 * persisted. It provides access to source/target resources, referencing resources organized by
 * type, and expected validation outcomes.
 *
 * <p>Example usage:</p>
 * <pre>
 * MergeTestScenario&lt;Practitioner&gt; scenario = dataBuilder
 *     .withReferences(
 *         ReferencingResourceConfig.of("PractitionerRole", "practitioner", 10),
 *         ReferencingResourceConfig.of("Encounter", "participant.individual", 5)
 *     )
 *     .build();
 *
 * // Access created resources
 * IIdType sourceId = scenario.getSourceId();
 * List&lt;IIdType&gt; roleIds = scenario.getReferencingResourceIds("PractitionerRole");
 *
 * // Build merge parameters
 * MergeTestParameters params = scenario.buildMergeParameters(true, false);
 * </pre>
 *
 * @param <T> The FHIR resource type being merged (e.g., Practitioner, Observation)
 */
public class MergeTestScenario<T extends IBaseResource> {

	private final ResourceMergeTestStrategy<T> myStrategy;
	private final T mySourceResource;
	private final T myTargetResource;
	private final T myResultResource;
	private final Map<String, List<IIdType>> myReferencingResourcesByType;
	private final List<Identifier> myExpectedIdentifiersAfterMerge;

	/**
	 * Create a new merge test scenario.
	 *
	 * @param theStrategy The strategy for this resource type
	 * @param theSource The source resource (already persisted)
	 * @param theTarget The target resource (already persisted)
	 * @param theResult The result resource (nullable, not persisted)
	 * @param theReferencingResources Map of resource type to list of created resource IDs
	 * @param theExpectedIdentifiers Expected identifiers on target after merge
	 */
	public MergeTestScenario(
			@Nonnull ResourceMergeTestStrategy<T> theStrategy,
			@Nonnull T theSource,
			@Nonnull T theTarget,
			@Nullable T theResult,
			@Nonnull Map<String, List<IIdType>> theReferencingResources,
			@Nonnull List<Identifier> theExpectedIdentifiers) {

		myStrategy = theStrategy;
		mySourceResource = theSource;
		myTargetResource = theTarget;
		myResultResource = theResult;
		myReferencingResourcesByType = theReferencingResources;
		myExpectedIdentifiersAfterMerge = theExpectedIdentifiers;
	}

	// Access to core resources

	/**
	 * @return The versionless ID of the source resource
	 */
	@Nonnull
	public IIdType getSourceId() {
		return mySourceResource.getIdElement().toUnqualifiedVersionless();
	}

	/**
	 * @return The versionless ID of the target resource
	 */
	@Nonnull
	public IIdType getTargetId() {
		return myTargetResource.getIdElement().toUnqualifiedVersionless();
	}

	/**
	 * @return The source resource instance
	 */
	@Nonnull
	public T getSourceResource() {
		return mySourceResource;
	}

	/**
	 * @return The target resource instance
	 */
	@Nonnull
	public T getTargetResource() {
		return myTargetResource;
	}

	/**
	 * @return The result resource instance (may be null)
	 */
	@Nullable
	public T getResultResource() {
		return myResultResource;
	}

	// Access to referencing resources

	/**
	 * Get the IDs of referencing resources for a specific resource type.
	 *
	 * @param theResourceType The resource type (e.g., "PractitionerRole", "Encounter")
	 * @return List of resource IDs, or empty list if none exist for this type
	 */
	@Nonnull
	public List<IIdType> getReferencingResourceIds(@Nonnull String theResourceType) {
		return myReferencingResourcesByType.getOrDefault(theResourceType, Collections.emptyList());
	}

	/**
	 * @return All referencing resources organized by resource type
	 */
	@Nonnull
	public Map<String, List<IIdType>> getAllReferencingResources() {
		return Collections.unmodifiableMap(myReferencingResourcesByType);
	}

	/**
	 * @return The total count of all referencing resources across all types
	 */
	public int getTotalReferenceCount() {
		return myReferencingResourcesByType.values().stream()
				.mapToInt(List::size)
				.sum();
	}

	/**
	 * @return Set of resource types that have referencing resources in this scenario
	 */
	@Nonnull
	public Set<String> getReferencingResourceTypes() {
		return myReferencingResourcesByType.keySet();
	}

	// Expected outcomes

	/**
	 * @return List of identifiers expected on the target resource after merge completes
	 */
	@Nonnull
	public List<Identifier> getExpectedIdentifiers() {
		return Collections.unmodifiableList(myExpectedIdentifiersAfterMerge);
	}

	// Build merge parameters

	/**
	 * Build merge parameters for executing this test scenario.
	 *
	 * @param theDeleteSource Whether to delete the source resource after merge
	 * @param thePreview Whether this is a preview-only merge (no actual changes)
	 * @return Configured merge test parameters ready for execution
	 */
	@Nonnull
	public MergeTestParameters buildMergeParameters(boolean theDeleteSource, boolean thePreview) {
		MergeTestParameters params = new MergeTestParameters()
				.sourceResource(new Reference(getSourceId()))
				.targetResource(new Reference(getTargetId()))
				.deleteSource(theDeleteSource)
				.preview(thePreview);

		if (myResultResource != null) {
			// Clone result resource to avoid modifying the original
			T resultForMerge = myStrategy.cloneResource(myResultResource);

			// Add "replaces" link only when deleteSource=false
			// (validation requires this link when source is kept, but forbids it when source is deleted)
			if (!theDeleteSource) {
				myStrategy.addReplacesLinkToResource(resultForMerge, getSourceId());
			}

			params.resultResource(resultForMerge);
		}

		return params;
	}

	/**
	 * Build merge parameters with identifier-based resolution for source or target.
	 *
	 * @param theSourceById If true, use source ID; if false, use source identifiers
	 * @param theTargetById If true, use target ID; if false, use target identifiers
	 * @param theDeleteSource Whether to delete the source resource after merge
	 * @param thePreview Whether this is a preview-only merge
	 * @return Configured merge test parameters
	 */
	@Nonnull
	public MergeTestParameters buildMergeParameters(
			boolean theSourceById, boolean theTargetById, boolean theDeleteSource, boolean thePreview) {

		MergeTestParameters params =
				new MergeTestParameters().deleteSource(theDeleteSource).preview(thePreview);

		if (theSourceById) {
			params.sourceResource(new Reference(getSourceId()));
		} else {
			params.sourceIdentifiers(myStrategy.getIdentifiersFromResource(mySourceResource));
		}

		if (theTargetById) {
			params.targetResource(new Reference(getTargetId()));
		} else {
			params.targetIdentifiers(myStrategy.getIdentifiersFromResource(myTargetResource));
		}

		if (myResultResource != null) {
			params.resultResource(myResultResource);
		}

		return params;
	}

	/**
	 * @return The strategy instance for this resource type
	 */
	@Nonnull
	public ResourceMergeTestStrategy<T> getStrategy() {
		return myStrategy;
	}
}
