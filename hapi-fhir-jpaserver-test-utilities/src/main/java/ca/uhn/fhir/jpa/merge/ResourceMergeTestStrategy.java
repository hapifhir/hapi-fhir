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

import java.util.List;

/**
 * Strategy interface for resource-specific test operations in merge tests.
 *
 * This interface encapsulates resource-type-specific logic for creating test data,
 * validating merge outcomes, and handling resource-specific field differences.
 *
 * Implementing a new strategy allows tests for a new resource type to be created
 * with minimal code duplication.
 *
 * <p>Example implementations:</p>
 * <ul>
 *   <li>{@code PractitionerMergeTestStrategy} - for Practitioner resources</li>
 *   <li>{@code ObservationMergeTestStrategy} - for Observation resources</li>
 *   <li>{@code OrganizationMergeTestStrategy} - for Organization resources</li>
 * </ul>
 *
 * @param <T> The FHIR resource type (e.g., Practitioner, Observation)
 */
public interface ResourceMergeTestStrategy<T extends IBaseResource> {

	// Resource type identification

	/**
	 * @return The FHIR resource type name (e.g., "Practitioner", "Observation")
	 */
	@Nonnull
	String getResourceTypeName();

	/**
	 * @return The Java class for this resource type
	 */
	@Nonnull
	Class<T> getResourceClass();

	// Test data creation

	/**
	 * Create a resource with the specified identifier values.
	 *
	 * The created resource should have:
	 * - Identifiers with system "http://test.org" and the provided values
	 * - Active field set to true (if the resource has an active field)
	 * - Any required fields populated with valid test values
	 *
	 * @param theIdentifierValues Identifier values to assign to the resource
	 * @return A new resource instance (not yet persisted)
	 */
	@Nonnull
	T createResourceWithIdentifiers(@Nonnull String... theIdentifierValues);

	/**
	 * Create a resource with the specified identifier instances.
	 *
	 * @param theIdentifiers Identifier instances to assign to the resource
	 * @return A new resource instance (not yet persisted)
	 */
	@Nonnull
	T createResourceWithIdentifiers(@Nonnull List<Identifier> theIdentifiers);

	/**
	 * Create a referencing resource of the specified type that references the target resource.
	 *
	 * The implementation should create appropriate resource types based on the resource being merged.
	 * For example, PractitionerMergeTestStrategy might create:
	 * - "PractitionerRole" resources with practitioner references
	 * - "Encounter" resources with participant.individual references
	 * - "CarePlan" resources with activity.detail.performer references
	 *
	 * @param theResourceType The FHIR resource type to create (e.g., "PractitionerRole")
	 * @param theReferencePath The path where the reference should be set (e.g., "practitioner")
	 * @param theTargetId The ID of the resource to reference
	 * @return A new referencing resource (not yet persisted)
	 * @throws IllegalArgumentException if the resource type is not supported by this strategy
	 */
	@Nonnull
	IBaseResource createReferencingResource(
			@Nonnull String theResourceType, @Nonnull String theReferencePath, @Nonnull IIdType theTargetId);

	/**
	 * Get predefined reference configurations for common test scenarios.
	 *
	 * This method returns a standard set of referencing resource configurations
	 * that are appropriate for this resource type. For example, Practitioner
	 * might return configs for PractitionerRole, Encounter, and CarePlan.
	 *
	 * @return List of standard reference configurations
	 */
	@Nonnull
	List<ReferencingResourceConfig> getStandardReferenceConfigs();

	// Resource reading

	/**
	 * Read a resource from the database by ID.
	 *
	 * @param theId The resource ID
	 * @return The resource instance
	 */
	@Nonnull
	T readResource(@Nonnull IIdType theId);

	/**
	 * Extract identifiers from a resource instance.
	 *
	 * @param theResource The resource to extract identifiers from
	 * @return List of identifiers (may be empty if resource has none)
	 */
	@Nonnull
	List<Identifier> getIdentifiersFromResource(@Nonnull T theResource);

	/**
	 * Add a "replaces" link from the resource to a target resource.
	 *
	 * This is used when creating result-resource test data, which must have
	 * a replaces link to the source resource for validation to pass.
	 *
	 * @param theResource The resource to add the link to
	 * @param theTargetId The ID of the resource being replaced
	 */
	void addReplacesLinkToResource(@Nonnull T theResource, @Nonnull IIdType theTargetId);

	/**
	 * Create a deep copy of the resource.
	 *
	 * @param theResource The resource to copy
	 * @return A deep copy of the resource
	 */
	@Nonnull
	T cloneResource(@Nonnull T theResource);

	// Validation methods

	/**
	 * Assert that the source resource is in the expected state after merge.
	 *
	 * If deleted, should verify the resource cannot be read.
	 * If not deleted, should verify:
	 * - Resource has replaced-by link to target
	 * - Active field is false (if resource has active field)
	 *
	 * @param theResource The source resource (null if deleted)
	 * @param theSourceId The ID of the source resource
	 * @param theTargetId The ID of the target resource
	 * @param theDeleted Whether the source was deleted
	 */
	void assertSourceResourceState(
			@Nullable T theResource, @Nonnull IIdType theSourceId, @Nonnull IIdType theTargetId, boolean theDeleted);

	/**
	 * Assert that the target resource is in the expected state after merge.
	 *
	 * Should verify:
	 * - Resource has replaces link to source
	 * - Identifiers match expected list
	 * - Active field remains true (if resource has active field)
	 *
	 * @param theResource The target resource
	 * @param theSourceId The ID of the source resource
	 * @param theSourceDeleted Whether the source was deleted
	 * @param theExpectedIdentifiers Expected identifiers on the target
	 */
	void assertTargetResourceState(
			@Nonnull T theResource,
			@Nonnull IIdType theSourceId,
			boolean theSourceDeleted,
			@Nonnull List<Identifier> theExpectedIdentifiers);

	/**
	 * Assert that the resource has the expected link/extension references.
	 *
	 * For Patient resources, checks native Patient.link field.
	 * For other resources, checks HapiExtensions-based extensions.
	 *
	 * @param theResource The resource to check
	 * @param theExpectedLinks List of expected link target IDs
	 * @param theLinkType The link type: "replaces" or "replaced-by"
	 */
	void assertLinksPresent(
			@Nonnull T theResource, @Nonnull List<IIdType> theExpectedLinks, @Nonnull String theLinkType);

	/**
	 * Assert that the resource has no merge-related links.
	 *
	 * @param theResource The resource to check
	 */
	void assertNoLinks(@Nonnull T theResource);

	// Field capability detection

	/**
	 * @return true if this resource type has an "active" field (boolean)
	 */
	boolean hasActiveField();

	/**
	 * @return true if this resource type has an "identifier" field
	 */
	boolean hasIdentifierField();

	// Expected state calculations

	/**
	 * Calculate the expected identifiers on the target resource after merge completes.
	 *
	 * Logic:
	 * - If result resource provided, return its identifiers
	 * - Otherwise, return target identifiers plus source identifiers marked as "old"
	 *   (excluding duplicates already on target)
	 *
	 * @param theSource The source resource
	 * @param theTarget The target resource
	 * @param theWithResultResource Whether a result resource will be provided
	 * @return List of expected identifiers after merge
	 */
	@Nonnull
	List<Identifier> calculateExpectedIdentifiersAfterMerge(
			@Nonnull T theSource, @Nonnull T theTarget, boolean theWithResultResource);
}
