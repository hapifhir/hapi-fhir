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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Identifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Fluent builder for creating merge test data scenarios.
 *
 * This builder creates and persists all necessary resources for a merge test,
 * including source/target resources and multiple types of referencing resources.
 *
 * <p>Example usage:</p>
 * <pre>
 * MergeTestDataBuilder&lt;Practitioner&gt; builder = new MergeTestDataBuilder&lt;&gt;(
 *     strategy, daoRegistry, requestDetails, fhirContext);
 *
 * MergeTestScenario&lt;Practitioner&gt; scenario = builder
 *     .withSourceIdentifiers("source-1", "source-2", "common")
 *     .withTargetIdentifiers("target-1", "target-2", "common")
 *     .withReferences(
 *         ReferencingResourceConfig.of("PractitionerRole", "practitioner", 10),
 *         ReferencingResourceConfig.of("Encounter", "participant.individual", 5)
 *     )
 *     .withResultResource()
 *     .build();
 * </pre>
 *
 * @param <T> The FHIR resource type being merged
 */
public class MergeTestDataBuilder<T extends IBaseResource> {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(MergeTestDataBuilder.class);

	private final ResourceMergeTestStrategy<T> myStrategy;
	private final DaoRegistry myDaoRegistry;
	private final RequestDetails myRequestDetails;
	private final FhirContext myFhirContext;

	// Configuration
	private final List<ReferencingResourceConfig> myReferenceConfigs = new ArrayList<>();
	private List<String> mySourceIdentifierValues = Arrays.asList("source-1", "source-2", "common");
	private List<String> myTargetIdentifierValues = Arrays.asList("target-1", "target-2", "common");
	private boolean myCreateResultResource = false;

	/**
	 * Create a new merge test data builder.
	 *
	 * @param theStrategy The resource-specific test strategy
	 * @param theDaoRegistry DAO registry for persisting resources
	 * @param theRequestDetails Request details for DAO operations
	 * @param theFhirContext FHIR context
	 */
	public MergeTestDataBuilder(
			@Nonnull ResourceMergeTestStrategy<T> theStrategy,
			@Nonnull DaoRegistry theDaoRegistry,
			@Nonnull RequestDetails theRequestDetails,
			@Nonnull FhirContext theFhirContext) {

		myStrategy = theStrategy;
		myDaoRegistry = theDaoRegistry;
		myRequestDetails = theRequestDetails;
		myFhirContext = theFhirContext;
	}

	/**
	 * Add referencing resource configurations to create.
	 *
	 * @param theConfigs One or more referencing resource configurations
	 * @return This builder for chaining
	 */
	@Nonnull
	public MergeTestDataBuilder<T> withReferences(@Nonnull ReferencingResourceConfig... theConfigs) {
		myReferenceConfigs.addAll(Arrays.asList(theConfigs));
		return this;
	}

	/**
	 * Add referencing resource configurations to create.
	 *
	 * @param theConfigs List of referencing resource configurations
	 * @return This builder for chaining
	 */
	@Nonnull
	public MergeTestDataBuilder<T> withReferences(@Nonnull List<ReferencingResourceConfig> theConfigs) {
		myReferenceConfigs.addAll(theConfigs);
		return this;
	}

	/**
	 * Use the strategy's standard reference configurations.
	 *
	 * @return This builder for chaining
	 */
	@Nonnull
	public MergeTestDataBuilder<T> withStandardReferences() {
		myReferenceConfigs.addAll(myStrategy.getStandardReferenceConfigs());
		return this;
	}

	/**
	 * Set the identifier values for the source resource.
	 *
	 * Default is ["source-1", "source-2", "common"].
	 *
	 * @param theValues Identifier values
	 * @return This builder for chaining
	 */
	@Nonnull
	public MergeTestDataBuilder<T> withSourceIdentifiers(@Nonnull String... theValues) {
		mySourceIdentifierValues = Arrays.asList(theValues);
		return this;
	}

	/**
	 * Set the identifier values for the target resource.
	 *
	 * Default is ["target-1", "target-2", "common"].
	 *
	 * @param theValues Identifier values
	 * @return This builder for chaining
	 */
	@Nonnull
	public MergeTestDataBuilder<T> withTargetIdentifiers(@Nonnull String... theValues) {
		myTargetIdentifierValues = Arrays.asList(theValues);
		return this;
	}

	/**
	 * Create a result resource that can be provided as the result-resource parameter.
	 *
	 * The result resource is created but NOT persisted.
	 *
	 * @return This builder for chaining
	 */
	@Nonnull
	public MergeTestDataBuilder<T> withResultResource() {
		myCreateResultResource = true;
		return this;
	}

	/**
	 * Build the test scenario by creating and persisting all resources.
	 *
	 * This method:
	 * 1. Creates and persists source and target resources with configured identifiers
	 * 2. Creates and persists all referencing resources (if any)
	 * 3. Creates result resource (if requested, but does NOT persist it)
	 * 4. Calculates expected identifiers after merge
	 * 5. Returns a complete test scenario
	 *
	 * @return The complete test scenario with all persisted resource IDs
	 */
	@Nonnull
	public MergeTestScenario<T> build() {
		ourLog.debug("Building merge test scenario for resource type: {}", myStrategy.getResourceTypeName());

		// Create and persist source resource
		T source = myStrategy.createResourceWithIdentifiers(mySourceIdentifierValues.toArray(new String[0]));
		IFhirResourceDao<T> dao = getDao();
		source = (T) dao.create(source, myRequestDetails).getResource();
		ourLog.debug("Created source resource: {}", source.getIdElement());

		// Create and persist target resource
		T target = myStrategy.createResourceWithIdentifiers(myTargetIdentifierValues.toArray(new String[0]));
		target = (T) dao.create(target, myRequestDetails).getResource();
		ourLog.debug("Created target resource: {}", target.getIdElement());

		// Create result resource if requested (NOT persisted)
		T result = null;
		if (myCreateResultResource) {
			result = myStrategy.createResourceWithIdentifiers(myTargetIdentifierValues.toArray(new String[0]));
			// Result resource must have same ID as target for validation
			result.setId(target.getIdElement().toUnqualifiedVersionless());

			// Note: The "replaces" link to source is NOT added here because it depends on deleteSource flag.
			// It will be added in MergeTestScenario.buildMergeParameters() if needed.

			ourLog.debug("Created result resource (not persisted) with ID: {}", result.getIdElement());
		}

		// Create referencing resources organized by type
		Map<String, List<IIdType>> referencingResourcesByType = new HashMap<>();

		for (ReferencingResourceConfig config : myReferenceConfigs) {
			List<IIdType> idsForType = new ArrayList<>();

			for (int i = 0; i < config.getCount(); i++) {
				IBaseResource referencingResource = myStrategy.createReferencingResource(
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
				myStrategy.calculateExpectedIdentifiersAfterMerge(source, target, myCreateResultResource);

		ourLog.debug(
				"Merge test scenario built successfully: source={}, target={}, referencing resource types={}",
				source.getIdElement(),
				target.getIdElement(),
				referencingResourcesByType.keySet());

		return new MergeTestScenario<>(
				myStrategy, source, target, result, referencingResourcesByType, expectedIdentifiers);
	}

	/**
	 * Get the DAO for this resource type.
	 *
	 * @return The resource DAO
	 */
	@Nonnull
	private IFhirResourceDao<T> getDao() {
		return myDaoRegistry.getResourceDao(myStrategy.getResourceClass());
	}
}
