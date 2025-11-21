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
import org.hl7.fhir.r4.model.Parameters;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Unified interface for resource-specific merge test scenarios.
 *
 * <p>This interface combines three responsibilities into a single abstraction:
 * <ol>
 *   <li><b>Builder</b>: Configure test data (identifiers, references, result resources)</li>
 *   <li><b>Data Holder</b>: Access created resources and expected outcomes</li>
 *   <li><b>Strategy</b>: Resource-specific operations and validations</li>
 * </ol>
 *
 * <p>Example usage:</p>
 * <pre>
 * // Create scenario and configure test data
 * PractitionerMergeTestScenario scenario = new PractitionerMergeTestScenario(...);
 * scenario
 *     .withSourceIdentifiers("source-1", "source-2", "common")
 *     .withTargetIdentifiers("target-1", "target-2", "common")
 *     .withReferences(ReferencingResourceConfig.of("PractitionerRole", "practitioner", 5))
 *     .createTestData();  // Creates and persists resources
 *
 * // Build merge parameters
 * MergeTestParameters params = scenario.buildMergeParameters(false, false);
 *
 * // Execute merge
 * helper.callMergeOperation("Practitioner", params, false);
 *
 * // Validate outcome using strategy methods
 * Practitioner target = scenario.readResource(scenario.getTargetId());
 * scenario.assertTargetResourceState(target, scenario.getSourceId(), false, scenario.getExpectedIdentifiers());
 * </pre>
 *
 * @param <T> The FHIR resource type (e.g., Practitioner, Observation)
 */
public interface MergeTestScenario<T extends IBaseResource> {

	// ================================================
	// BUILDER METHODS - Configure test data
	// ================================================

	/**
	 * Set the identifier values for the source resource.
	 *
	 * Default is ["source-1", "source-2", "common"].
	 *
	 * @param theValues Identifier values
	 * @return This scenario for chaining
	 */
	@Nonnull
	MergeTestScenario<T> withSourceIdentifiers(@Nonnull String... theValues);

	/**
	 * Set the identifier values for the target resource.
	 *
	 * Default is ["target-1", "target-2", "common"].
	 *
	 * @param theValues Identifier values
	 * @return This scenario for chaining
	 */
	@Nonnull
	MergeTestScenario<T> withTargetIdentifiers(@Nonnull String... theValues);

	/**
	 * Add referencing resource configurations to create.
	 *
	 * @param theConfigs One or more referencing resource configurations
	 * @return This scenario for chaining
	 */
	@Nonnull
	MergeTestScenario<T> withReferences(@Nonnull ReferencingResourceConfig... theConfigs);

	/**
	 * Add referencing resource configurations to create.
	 *
	 * @param theConfigs List of referencing resource configurations
	 * @return This scenario for chaining
	 */
	@Nonnull
	MergeTestScenario<T> withReferences(@Nonnull List<ReferencingResourceConfig> theConfigs);

	/**
	 * Use the scenario's standard reference configurations.
	 *
	 * @return This scenario for chaining
	 */
	@Nonnull
	MergeTestScenario<T> withStandardReferences();

	/**
	 * Create a result resource that can be provided as the result-resource parameter.
	 *
	 * The result resource is created but NOT persisted.
	 *
	 * @return This scenario for chaining
	 */
	@Nonnull
	MergeTestScenario<T> withResultResource();

	/**
	 * Build the test data by creating and persisting all resources.
	 *
	 * This method:
	 * <ol>
	 *   <li>Creates and persists source and target resources with configured identifiers</li>
	 *   <li>Creates and persists all referencing resources (if any)</li>
	 *   <li>Creates result resource (if requested, but does NOT persist it)</li>
	 *   <li>Calculates expected identifiers after merge</li>
	 * </ol>
	 *
	 * After calling this method, the scenario transitions from configuration state to data state,
	 * and data accessor methods become available.
	 */
	void createTestData();

	// ================================================
	// DATA ACCESSORS - Access created resources
	// ================================================

	/**
	 * @return The versionless ID of the source resource
	 * @throws IllegalStateException if createTestData() has not been called
	 */
	@Nonnull
	IIdType getSourceId();

	/**
	 * @return The versionless ID of the target resource
	 * @throws IllegalStateException if createTestData() has not been called
	 */
	@Nonnull
	IIdType getTargetId();

	/**
	 * @return The source resource instance
	 * @throws IllegalStateException if createTestData() has not been called
	 */
	@Nonnull
	T getSourceResource();

	/**
	 * @return The target resource instance
	 * @throws IllegalStateException if createTestData() has not been called
	 */
	@Nonnull
	T getTargetResource();

	/**
	 * Get the IDs of referencing resources for a specific resource type.
	 *
	 * @param theResourceType The resource type (e.g., "PractitionerRole", "Encounter")
	 * @return List of resource IDs, or empty list if none exist for this type
	 * @throws IllegalStateException if createTestData() has not been called
	 */
	@Nonnull
	List<IIdType> getReferencingResourceIds(@Nonnull String theResourceType);

	/**
	 * @return All referencing resources organized by resource type
	 * @throws IllegalStateException if createTestData() has not been called
	 */
	@Nonnull
	Map<String, List<IIdType>> getAllReferencingResources();

	/**
	 * @return The total count of all referencing resources across all types
	 * @throws IllegalStateException if createTestData() has not been called
	 */
	int getTotalReferenceCount();

	/**
	 * @return Set of resource types that have referencing resources in this scenario
	 * @throws IllegalStateException if createTestData() has not been called
	 */
	@Nonnull
	Set<String> getReferencingResourceTypes();

	/**
	 * @return List of identifiers expected on the target resource after merge completes
	 * @throws IllegalStateException if createTestData() has not been called
	 */
	@Nonnull
	List<Identifier> getExpectedIdentifiers();

	/**
	 * Build merge parameters for executing this test scenario.
	 *
	 * @param theDeleteSource Whether to delete the source resource after merge
	 * @param thePreview Whether this is a preview-only merge (no actual changes)
	 * @return Configured merge test parameters ready for execution
	 * @throws IllegalStateException if createTestData() has not been called
	 */
	@Nonnull
	MergeTestParameters buildMergeParameters(boolean theDeleteSource, boolean thePreview);

	/**
	 * Build merge parameters with identifier-based resolution for source or target.
	 *
	 * @param theSourceById If true, use source ID; if false, use source identifiers
	 * @param theTargetById If true, use target ID; if false, use target identifiers
	 * @param theDeleteSource Whether to delete the source resource after merge
	 * @param thePreview Whether this is a preview-only merge
	 * @return Configured merge test parameters
	 * @throws IllegalStateException if createTestData() has not been called
	 */
	@Nonnull
	MergeTestParameters buildMergeParameters(
			boolean theSourceById, boolean theTargetById, boolean theDeleteSource, boolean thePreview);

	// ================================================
	// STRATEGY METHODS - Resource-specific operations
	// ================================================

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
	 * For example, PractitionerMergeTestScenario might create:
	 * - "PractitionerRole" resources with practitioner references
	 * - "Encounter" resources with participant.individual references
	 * - "CarePlan" resources with activity.detail.performer references
	 *
	 * @param theResourceType The FHIR resource type to create (e.g., "PractitionerRole")
	 * @param theReferencePath The path where the reference should be set (e.g., "practitioner")
	 * @param theTargetId The ID of the resource to reference
	 * @return A new referencing resource (not yet persisted)
	 * @throws IllegalArgumentException if the resource type is not supported by this scenario
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

	// ================================================
	// VALIDATION METHODS - Operation outcome validation
	// ================================================

	/**
	 * Validate that a synchronous merge completed successfully.
	 * <p>
	 * Checks:
	 * - Output has 3 parameters (input, outcome, result)
	 * - Outcome severity is INFORMATION
	 * - Outcome message indicates success
	 *
	 * @param theOutParams The operation output parameters
	 */
	void validateSyncMergeOutcome(@Nonnull Parameters theOutParams);

	/**
	 * Validate that an async merge task was created.
	 * <p>
	 * Checks:
	 * - Output has 3 parameters (input, outcome, task)
	 * - Task resource is present
	 * - Outcome indicates async processing
	 *
	 * @param theOutParams The operation output parameters
	 */
	void validateAsyncTaskCreated(@Nonnull Parameters theOutParams);

	/**
	 * Validate that a preview merge completed successfully.
	 * <p>
	 * Checks:
	 * - Outcome indicates preview mode
	 * - Diagnostics shows expected update count
	 *
	 * @param theOutParams           The operation output parameters
	 * @param theExpectedUpdateCount Expected number of resources that would be updated
	 */
	void validatePreviewOutcome(@Nonnull Parameters theOutParams, int theExpectedUpdateCount);

	/**
	 * Assert that referencing resources have been updated to point to the target.
	 * <p>
	 * Verifies that none of the specified resources contain references to the source resource.
	 *
	 * @param theReferencingResourceIds IDs of resources that should have been updated
	 * @param theSourceId               The source resource ID (references should be removed)
	 * @param theTargetId               The target resource ID (not used in validation, provided for logging)
	 */
	void assertReferencesUpdated(
			@Nonnull List<IIdType> theReferencingResourceIds,
			@Nonnull IIdType theSourceId,
			@Nonnull IIdType theTargetId);

	/**
	 * Assert that referencing resources have NOT been updated (for preview mode validation).
	 * <p>
	 * Verifies that resources still contain references to the source resource.
	 */
	void assertReferencesNotUpdated();

	/**
	 * Assert that merge provenance was created for the merge operation.
	 *
	 * @param theSourceId    The source resource ID
	 * @param theTargetId    The target resource ID
	 * @param theInputParams The input parameters used for the merge
	 */
	void assertMergeProvenanceCreated(
			@Nonnull IIdType theSourceId, @Nonnull IIdType theTargetId, @Nonnull Parameters theInputParams);

	/**
	 * @return true if this resource type has an "active" field (boolean)
	 */
	boolean hasActiveField();

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
