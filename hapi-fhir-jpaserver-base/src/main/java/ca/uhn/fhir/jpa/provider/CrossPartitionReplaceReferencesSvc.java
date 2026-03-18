/*-
 * #%L
 * HAPI FHIR JPA Server
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
package ca.uhn.fhir.jpa.provider;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.data.IResourceLinkDao;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.replacereferences.ReplaceReferencesProvenanceSvc;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.util.BundleBuilder;
import ca.uhn.fhir.util.FhirTerser;
import ca.uhn.fhir.util.ResourceReferenceInfo;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Discovers resources referencing a source resource, copies them to the target resource's
 * partition via a transaction bundle (CREATEs + PUTs), and returns the originals for the caller to delete.
 * <p>
 * All operations are performed within a single DB transaction for atomicity.
 */
// Created by claude-opus-4-6
public class CrossPartitionReplaceReferencesSvc {
	private static final Logger ourLog = LoggerFactory.getLogger(CrossPartitionReplaceReferencesSvc.class);

	private final DaoRegistry myDaoRegistry;
	private final IResourceLinkDao myResourceLinkDao;
	private final IRequestPartitionHelperSvc myRequestPartitionHelperSvc;
	private final FhirContext myFhirContext;

	public CrossPartitionReplaceReferencesSvc(
			DaoRegistry theDaoRegistry,
			IResourceLinkDao theResourceLinkDao,
			IRequestPartitionHelperSvc theRequestPartitionHelperSvc) {
		myDaoRegistry = theDaoRegistry;
		myResourceLinkDao = theResourceLinkDao;
		myRequestPartitionHelperSvc = theRequestPartitionHelperSvc;
		myFhirContext = theDaoRegistry.getFhirContext();
	}

	/**
	 * Copies referencing resources from the source resource's partition to the target resource's partition,
	 * and updates references in resources that don't change partition. Assumes the caller provides the outer
	 * transaction — all internal operations use {@code transactionNested()}.
	 * <p>
	 * Does NOT delete the source copies — the caller is responsible for deleting them after
	 * provenance creation using the returned {@link CrossPartitionReplaceReferencesResult#getCopiedResourceOriginalIds()}.
	 * Deletion cannot happen here because provenance must reference the originals (as tombstones),
	 * and deleting first would violate referential integrity checks.
	 *
	 * @return a {@link CrossPartitionReplaceReferencesResult} containing references to created/updated resources
	 *         and versioned references to the original source copies for deferred deletion.
	 */
	public CrossPartitionReplaceReferencesResult copyCompartmentResourcesAndReplaceReferences(
			IBaseResource theSourceResource, IBaseResource theTargetResource, RequestDetails theRequestDetails) {

		IIdType sourceId = theSourceResource.getIdElement().toUnqualifiedVersionless();
		IIdType targetId = theTargetResource.getIdElement().toUnqualifiedVersionless();

		RequestPartitionId sourcePartitionId = RequestPartitionId.getPartitionFromUserDataIfPresent(theSourceResource)
				.orElse(null);
		RequestPartitionId targetPartitionId = RequestPartitionId.getPartitionFromUserDataIfPresent(theTargetResource)
				.orElse(null);

		ourLog.info(
				"Cross-partition merge: copying referencing resources from {} (partition {}) to {} (partition {})",
				sourceId.getValue(),
				sourcePartitionId,
				targetId.getValue(),
				targetPartitionId);

		// Step 1: Discover all resources referencing the source resource
		List<IBaseResource> allReferencingResources = discoverReferencingResources(sourceId, theRequestDetails);

		if (allReferencingResources.isEmpty()) {
			ourLog.info("No referencing resources found for {}", sourceId.getValue());
			return new CrossPartitionReplaceReferencesResult(List.of(), List.of());
		}

		// Step 2: Classify into COPY (partition changes after rewrite) vs UPDATE (same partition)
		List<IBaseResource> copyList = new ArrayList<>();
		List<IBaseResource> updateList = new ArrayList<>();
		replaceSourceReferencesAndClassifyResources(
				allReferencingResources,
				sourceId.getValue(),
				targetId.getValue(),
				theRequestDetails,
				copyList,
				updateList);

		ourLog.info(
				"Classified {} resources: {} to copy, {} to update references",
				allReferencingResources.size(),
				copyList.size(),
				updateList.size());

		if (copyList.isEmpty() && updateList.isEmpty()) {
			return new CrossPartitionReplaceReferencesResult(List.of(), List.of());
		}

		// Capture versioned IDs from copyList before buildCombinedBundle clears them
		List<IIdType> copiedResourceOriginalIds =
				copyList.stream().map(IBaseResource::getIdElement).toList();

		// Step 3: Discover additional resources to update BEFORE bundle execution.
		discoverAndAddAdditionalResourcesToUpdate(copyList, updateList, theRequestDetails);

		// Step 4: Build and execute a single combined transaction bundle.
		Map<String, String> oldIdToPlaceholder = new HashMap<>();
		IBaseBundle combinedBundle = buildCombinedBundle(copyList, updateList, oldIdToPlaceholder);
		@SuppressWarnings("unchecked")
		IBaseBundle combinedResponse =
				(IBaseBundle) myDaoRegistry.getSystemDao().transactionNested(theRequestDetails, combinedBundle);
		List<IIdType> changedResourceIds =
				ReplaceReferencesProvenanceSvc.extractChangedResourceIds(List.of((Bundle) combinedResponse));

		ourLog.info(
				"Cross-partition merge complete: copied {} resources, updated {} references",
				copyList.size(),
				updateList.size());

		return new CrossPartitionReplaceReferencesResult(changedResourceIds, copiedResourceOriginalIds);
	}

	/**
	 * Finds all resource IDs that have a reference link pointing to the given source resource,
	 * then loads and returns those resources.
	 */
	private List<IBaseResource> discoverReferencingResources(IIdType theSourceId, RequestDetails theRequestDetails) {
		List<IdDt> ids;
		try (Stream<IdDt> stream = myResourceLinkDao.streamSourceIdsForTargetFhirId(
				theSourceId.getResourceType(), theSourceId.getIdPart())) {
			ids = stream.toList(); // never null — returns empty list if no results
		}
		return loadResources(ids, theRequestDetails);
	}

	/**
	 * Discovers resources that reference copied resources (by their old IDs) and appends
	 * them to {@code theUpdateList}. Resources already in the copy/update lists are excluded
	 * to avoid duplicate entries in the transaction bundle.
	 * <p>
	 * This handles cases where resources that do not reference the source resource but do reference
	 * a copied resource that got a new ID (e.g., a FHIR List resource referencing a copied Encounter).
	 */
	private void discoverAndAddAdditionalResourcesToUpdate(
			List<IBaseResource> theCopyList, List<IBaseResource> theUpdateList, RequestDetails theRequestDetails) {
		if (theCopyList.isEmpty()) {
			return;
		}

		Set<String> alreadyDiscoveredIds = new HashSet<>();
		alreadyDiscoveredIds.addAll(theCopyList.stream()
				.map(r -> r.getIdElement().toUnqualifiedVersionless().getValue())
				.toList());
		alreadyDiscoveredIds.addAll(theUpdateList.stream()
				.map(r -> r.getIdElement().toUnqualifiedVersionless().getValue())
				.toList());

		List<IdDt> additionalIds = new ArrayList<>();
		for (IBaseResource resource : theCopyList) {
			IIdType oldId = resource.getIdElement();
			try (Stream<IdDt> stream =
					myResourceLinkDao.streamSourceIdsForTargetFhirId(oldId.getResourceType(), oldId.getIdPart())) {
				stream.forEach(id -> {
					if (alreadyDiscoveredIds.add(id.toUnqualifiedVersionless().getValue())) {
						additionalIds.add(id);
					}
				});
			}
		}

		if (!additionalIds.isEmpty()) {
			List<IBaseResource> additionalResources = loadResources(additionalIds, theRequestDetails);
			ourLog.info(
					"Discovered {} additional resources referencing resources to be copied across partitions",
					additionalResources.size());
			theUpdateList.addAll(additionalResources);
		}
	}

	private List<IBaseResource> loadResources(List<IdDt> theIds, RequestDetails theRequestDetails) {
		List<IBaseResource> result = new ArrayList<>();
		for (IdDt id : theIds) {
			try {
				@SuppressWarnings("unchecked")
				IFhirResourceDao<IBaseResource> dao = myDaoRegistry.getResourceDao(id.getResourceType());
				result.add(dao.read(id.toVersionless(), theRequestDetails));
			} catch (ResourceGoneException | ResourceNotFoundException e) {
				ourLog.warn("Skipping deleted/not-found resource: {}", id.getValue());
			}
		}
		return result;
	}

	/**
	 * Rewrites source→target references in each resource, then classifies it as either a COPY
	 * (partition changes after the rewrite) or an UPDATE (partition stays the same). Resources
	 * whose new partition differs from their current partition need to be copied to the target
	 * partition; the rest only need an in-place reference update. Classification results are
	 * populated into the provided {@code theCopyList} and {@code theUpdateList} parameters.
	 */
	private void replaceSourceReferencesAndClassifyResources(
			List<IBaseResource> theResources,
			String theSourceRef,
			String theTargetRef,
			RequestDetails theRequestDetails,
			List<IBaseResource> theCopyList,
			List<IBaseResource> theUpdateList) {

		for (IBaseResource resource : theResources) {
			Integer currentPartitionId = RequestPartitionId.getPartitionFromUserDataIfPresent(resource)
					.map(RequestPartitionId::getFirstPartitionIdOrNull)
					.orElse(null);

			// Rewrite source→target references so determineCreatePartitionForRequest
			// routes based on the post-merge state.
			replaceVersionlessReferences(resource, Map.of(theSourceRef, theTargetRef));

			Integer newPartitionId = determinePartition(resource, theRequestDetails);

			if (Objects.equals(currentPartitionId, newPartitionId)) {
				theUpdateList.add(resource);
			} else {
				theCopyList.add(resource);
			}
		}
	}

	/**
	 * Determines the partition for a resource by temporarily clearing its existing
	 * RESOURCE_PARTITION_ID and asking the partition helper to compute a fresh partition
	 * based on the resource's current references. The original partition is restored afterward.
	 */
	private Integer determinePartition(IBaseResource theResource, RequestDetails theRequestDetails) {
		Object savedPartitionUserData = theResource.getUserData(Constants.RESOURCE_PARTITION_ID);
		try {
			theResource.setUserData(Constants.RESOURCE_PARTITION_ID, null);
			String resourceType = myFhirContext.getResourceType(theResource);
			RequestPartitionId targetPartition = myRequestPartitionHelperSvc.determineCreatePartitionForRequest(
					theRequestDetails, theResource, resourceType);
			return targetPartition.getFirstPartitionIdOrNull();
		} finally {
			theResource.setUserData(Constants.RESOURCE_PARTITION_ID, savedPartitionUserData);
		}
	}

	/**
	 * Builds a single combined transaction bundle containing POST (CREATE) entries for copied
	 * resources and PUT (UPDATE) entries for reference-only changes. References to copied resources
	 * are replaced with {@code urn:uuid} placeholders in both lists — the transaction processor's
	 * {@code IdSubstitutionMap} resolves these after the POST entries create the new resources.
	 * <p>
	 * Source→target references are already rewritten by {@link #replaceSourceReferencesAndClassifyResources}.
	 *
	 * @param theOldIdToPlaceholder populated by this method with old ID → urn:uuid mappings
	 */
	private IBaseBundle buildCombinedBundle(
			List<IBaseResource> theCopyList,
			List<IBaseResource> theUpdateList,
			Map<String, String> theOldIdToPlaceholder) {

		// Build old ID → urn:uuid placeholder map from the copy list
		for (IBaseResource resource : theCopyList) {
			String oldId = resource.getIdElement().toUnqualifiedVersionless().getValue();
			theOldIdToPlaceholder.put(oldId, IdDt.newRandomUuid().getValue());
		}

		// Replace inter-resource references with urn:uuid placeholders in BOTH lists.
		// Source→target references were already rewritten by replaceSourceReferencesAndClassifyResources.
		replaceVersionlessReferences(theCopyList, theOldIdToPlaceholder);
		replaceVersionlessReferences(theUpdateList, theOldIdToPlaceholder);

		BundleBuilder bundleBuilder = new BundleBuilder(myFhirContext);

		// POST entries: clear partition + ID on copied resources, add as CREATE with placeholder fullUrl
		for (IBaseResource resource : theCopyList) {
			String oldId = resource.getIdElement().toUnqualifiedVersionless().getValue();
			String placeholder = theOldIdToPlaceholder.get(oldId);
			resource.setUserData(Constants.RESOURCE_PARTITION_ID, null);
			resource.setId((IIdType) null);
			bundleBuilder.addTransactionCreateEntry(resource, placeholder);
		}

		// PUT entries: update list resources keep their partition ID intact
		for (IBaseResource resource : theUpdateList) {
			bundleBuilder.addTransactionUpdateEntry(resource);
		}

		return bundleBuilder.getBundle();
	}

	/**
	 * Rewrites versionless references in all resources in the list using the given map.
	 * Versioned references are left unchanged.
	 */
	private void replaceVersionlessReferences(List<IBaseResource> theResources, Map<String, String> theReferenceMap) {
		for (IBaseResource resource : theResources) {
			replaceVersionlessReferences(resource, theReferenceMap);
		}
	}

	/**
	 * Rewrites versionless references in a single resource using the given map.
	 * Versioned references are left unchanged.
	 */
	private void replaceVersionlessReferences(IBaseResource theResource, Map<String, String> theReferenceMap) {
		FhirTerser terser = myFhirContext.newTerser();
		for (ResourceReferenceInfo refInfo : terser.getAllResourceReferences(theResource)) {
			IIdType refElement = refInfo.getResourceReference().getReferenceElement();
			if (refElement.hasVersionIdPart()) {
				continue;
			}
			String refValue = refElement.toUnqualifiedVersionless().getValue();
			String replacement = theReferenceMap.get(refValue);
			if (replacement != null) {
				refInfo.getResourceReference().setReference(replacement);
			}
		}
	}
}
