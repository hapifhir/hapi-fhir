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
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.data.IResourceLinkDao;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.replacereferences.ReplaceReferencesProvenanceSvc;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.util.BundleBuilder;
import ca.uhn.fhir.util.FhirTerser;
import ca.uhn.fhir.util.ResourceReferenceInfo;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

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
	private final IHapiTransactionService myHapiTransactionService;
	private final FhirContext myFhirContext;

	public CrossPartitionReplaceReferencesSvc(
			DaoRegistry theDaoRegistry,
			IResourceLinkDao theResourceLinkDao,
			IRequestPartitionHelperSvc theRequestPartitionHelperSvc,
			IHapiTransactionService theHapiTransactionService) {
		myDaoRegistry = theDaoRegistry;
		myResourceLinkDao = theResourceLinkDao;
		myRequestPartitionHelperSvc = theRequestPartitionHelperSvc;
		myHapiTransactionService = theHapiTransactionService;
		myFhirContext = theDaoRegistry.getFhirContext();
	}

	/**
	 * Copies the resources referencing the source into the target's partition and rewrites references, executed as
	 * its OWN single FHIR transaction (a combined CREATE + PUT bundle). The source copies are NOT deleted here — the
	 * caller deletes them after creating the merge Provenance so the Provenance can still reference the originals (as
	 * tombstones), using the returned {@link CrossPartitionReplaceReferencesResult#getCopiedResourceOriginalIds()}.
	 * <p>
	 * Resources are discovered across every shard (the reference-link index is partitioned by source resource), so
	 * this works in MegaScale where the referrers live on a different shard than the merge request's partition.
	 *
	 * @return the changed-resource ids from the transaction response, plus the versioned ids of the original copies
	 *         for deferred deletion.
	 */
	public CrossPartitionReplaceReferencesResult copyCompartmentResourcesAndReplaceReferences(
			IBaseResource theSourceResource, IBaseResource theTargetResource, RequestDetails theRequestDetails) {

		IIdType sourceId = theSourceResource.getIdElement().toUnqualifiedVersionless();
		IIdType targetId = theTargetResource.getIdElement().toUnqualifiedVersionless();

		RequestPartitionId sourcePartitionId = getRequiredPartition(theSourceResource);
		RequestPartitionId targetPartitionId = getRequiredPartition(theTargetResource);

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

		// Step 2: Classify into COPY (partition changes after rewrite) vs UPDATE (same partition).
		// copiesByDestPartition: keyed by destination partition, insertion-ordered for response correlation.
		Map<RequestPartitionId, List<IBaseResource>> copiesByDestPartition = new LinkedHashMap<>();
		List<IBaseResource> updateList = new ArrayList<>();
		replaceSourceReferencesAndClassifyResources(
				allReferencingResources,
				sourceId.getValue(),
				targetId.getValue(),
				theRequestDetails,
				copiesByDestPartition,
				updateList);

		List<IBaseResource> copyList =
				copiesByDestPartition.values().stream().flatMap(List::stream).toList();

		ourLog.info(
				"Classified {} resources: {} to copy, {} to update references",
				allReferencingResources.size(),
				copyList.size(),
				updateList.size());

		if (copyList.isEmpty() && updateList.isEmpty()) {
			return new CrossPartitionReplaceReferencesResult(List.of(), List.of());
		}

		// Capture the versioned ids of the original source copies before buildCombinedBundle clears them
		// (RESOURCE_PARTITION_ID and id are cleared by buildCombinedBundle for copies). The caller deletes these
		// after the Provenance is created.
		List<IIdType> copiedResourceOriginalIds =
				copyList.stream().map(IBaseResource::getIdElement).toList();

		// Step 3: Discover additional resources to update BEFORE bundle assembly.
		discoverAndAddAdditionalResourcesToUpdate(copyList, updateList, theRequestDetails);

		// Step 4: Build and execute a single combined transaction bundle (copies as CREATE, referrer updates as PUT).
		Map<String, String> oldIdToPlaceholder = new HashMap<>();
		BundleBuilder bundleBuilder = new BundleBuilder(myFhirContext);
		buildCombinedBundle(copyList, updateList, oldIdToPlaceholder, bundleBuilder);
		Bundle combinedResponse =
				(Bundle) myDaoRegistry.getSystemDao().transactionNested(theRequestDetails, bundleBuilder.getBundle());
		List<IIdType> changedResourceIds =
				ReplaceReferencesProvenanceSvc.extractChangedResourceIds(List.of(combinedResponse));

		return new CrossPartitionReplaceReferencesResult(changedResourceIds, copiedResourceOriginalIds);
	}

	/**
	 * Finds all resource IDs that have a reference link pointing to the given source resource,
	 * then loads and returns those resources.
	 */
	private List<IBaseResource> discoverReferencingResources(IIdType theSourceId, RequestDetails theRequestDetails) {
		List<ReferencingResourceId> ids = findReferencingResourceIds(theSourceId, theRequestDetails);
		return loadResources(ids, theRequestDetails);
	}

	/**
	 * A referencing resource id discovered via the HFJ_RES_LINK index, paired with the partition the
	 * source resource lives in. The partition pins the subsequent read to the correct shard rather
	 * than relying on id-based partition resolution (which fails for client-assigned ids in MegaScale
	 * Patient ID mode). A null partition id maps to the default partition.
	 */
	private record ReferencingResourceId(IdDt id, RequestPartitionId partitionId) {}

	/**
	 * Returns the IDs of all resources that have a reference link pointing to {@code theTargetId}.
	 * <p>
	 * Fans out to every shard — HFJ_RES_LINK rows are partitioned by source resource, so on
	 * MegaScale a single-DB query only sees outgoing refs from that shard. REQUIRES_NEW is
	 * mandatory: with the default REQUIRED propagation the per-shard thunks would join the
	 * outer tx and skip the fan-out entirely. On non-MegaScale HAPI this is a single query.
	 */
	private List<ReferencingResourceId> findReferencingResourceIds(
			IIdType theTargetId, RequestDetails theRequestDetails) {
		return myHapiTransactionService
				.withRequest(theRequestDetails)
				.withRequestPartitionId(RequestPartitionId.allPartitions())
				.withPropagation(Propagation.REQUIRES_NEW)
				.searchList(partition -> myResourceLinkDao
						.streamSourceIdsAndPartitionForTargetFhirId(
								theTargetId.getResourceType(), theTargetId.getIdPart())
						.map(view -> new ReferencingResourceId(
								view.toIdDt(), RequestPartitionId.fromPartitionId(view.partitionId())))
						.toList());
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

		List<ReferencingResourceId> additionalIds = new ArrayList<>();
		for (IBaseResource resource : theCopyList) {
			IIdType oldId = resource.getIdElement();
			List<ReferencingResourceId> referrers = findReferencingResourceIds(oldId, theRequestDetails);
			for (ReferencingResourceId referrer : referrers) {
				if (alreadyDiscoveredIds.add(
						referrer.id().toUnqualifiedVersionless().getValue())) {
					additionalIds.add(referrer);
				}
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

	private static RequestPartitionId getRequiredPartition(IBaseResource theResource) {
		return RequestPartitionId.getPartitionFromUserDataIfPresent(theResource)
				.orElseThrow(() -> new IllegalStateException(
						"Resource " + theResource.getIdElement().getValue() + " has no partition info"));
	}

	private List<IBaseResource> loadResources(List<ReferencingResourceId> theIds, RequestDetails theRequestDetails) {
		List<IBaseResource> result = new ArrayList<>();
		for (ReferencingResourceId referencingId : theIds) {
			IdDt id = referencingId.id();
			try {
				@SuppressWarnings("unchecked")
				IFhirResourceDao<IBaseResource> dao = myDaoRegistry.getResourceDao(id.getResourceType());
				// Pin the read to the source resource's partition (from the link index) instead of relying
				// on id-based partition resolution, which fails for client-assigned (non-partition-decodable)
				// ids in MegaScale Patient ID mode. A pinned partition is incompatible with the outer
				// allPartitions tx, so in MegaScale REQUIRES_NEW-on-change escapes to the correct shard.
				IBaseResource resource = myHapiTransactionService
						.withRequest(theRequestDetails)
						.withRequestPartitionId(referencingId.partitionId())
						.execute(() -> dao.read(id.toVersionless(), theRequestDetails));
				result.add(resource);
			} catch (ResourceGoneException e) {
				// Genuinely deleted between discovery and load — tolerated, the link is stale.
				ourLog.warn("Skipping deleted resource: {}", id.getValue());
			} catch (ResourceNotFoundException e) {
				// The link index proves this resource existed; failing to read it now is contradictory
				// (e.g. a partition-pinning miss). Fail the merge loudly rather than silently dropping it.
				throw new InternalErrorException(
						Msg.code(2975) + "Resource " + id.getValue()
								+ " was found in the reference link index but could not be loaded; aborting cross-partition merge to avoid silently losing data.");
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
			Map<RequestPartitionId, List<IBaseResource>> theCopiesByDestPartition,
			List<IBaseResource> theUpdateList) {

		for (IBaseResource resource : theResources) {
			Integer currentPartitionId = RequestPartitionId.getPartitionFromUserDataIfPresent(resource)
					.map(RequestPartitionId::getFirstPartitionIdOrNull)
					.orElse(null);

			// Rewrite source→target references so determineCreatePartitionForRequest
			// routes based on the post-merge state.
			replaceVersionlessReferences(resource, Map.of(theSourceRef, theTargetRef));

			RequestPartitionId newPartition = determinePartition(resource, theRequestDetails);
			Integer newPartitionId = newPartition.getFirstPartitionIdOrNull();

			if (Objects.equals(currentPartitionId, newPartitionId)) {
				theUpdateList.add(resource);
			} else {
				theCopiesByDestPartition
						.computeIfAbsent(newPartition, k -> new ArrayList<>())
						.add(resource);
			}
		}
	}

	/**
	 * Determines the partition for a resource by temporarily clearing its existing
	 * RESOURCE_PARTITION_ID and asking the partition helper to compute a fresh partition
	 * based on the resource's current references. The original partition is restored afterward.
	 */
	private RequestPartitionId determinePartition(IBaseResource theResource, RequestDetails theRequestDetails) {
		Object savedPartitionUserData = theResource.getUserData(Constants.RESOURCE_PARTITION_ID);
		try {
			theResource.setUserData(Constants.RESOURCE_PARTITION_ID, null);
			String resourceType = myFhirContext.getResourceType(theResource);
			return myRequestPartitionHelperSvc.determineCreatePartitionForRequest(
					theRequestDetails, theResource, resourceType);
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
	 * @param theBundleBuilder      the bundle being assembled; copy (POST) then update (PUT) entries are appended
	 */
	private void buildCombinedBundle(
			List<IBaseResource> theCopyList,
			List<IBaseResource> theUpdateList,
			Map<String, String> theOldIdToPlaceholder,
			BundleBuilder theBundleBuilder) {

		// Build old ID → urn:uuid placeholder map from the copy list
		for (IBaseResource resource : theCopyList) {
			String oldId = resource.getIdElement().toUnqualifiedVersionless().getValue();
			theOldIdToPlaceholder.put(oldId, IdDt.newRandomUuid().getValue());
		}

		// Replace inter-resource references with urn:uuid placeholders in BOTH lists.
		// Source→target references were already rewritten by replaceSourceReferencesAndClassifyResources.
		replaceVersionlessReferences(theCopyList, theOldIdToPlaceholder);
		replaceVersionlessReferences(theUpdateList, theOldIdToPlaceholder);

		// POST entries: clear partition + ID on copied resources, add as CREATE with placeholder fullUrl
		for (IBaseResource resource : theCopyList) {
			String oldId = resource.getIdElement().toUnqualifiedVersionless().getValue();
			String placeholder = theOldIdToPlaceholder.get(oldId);
			resource.setUserData(Constants.RESOURCE_PARTITION_ID, null);
			resource.setId((IIdType) null);
			theBundleBuilder.addTransactionCreateEntry(resource, placeholder);
		}

		// PUT entries: update list resources keep their partition ID intact
		for (IBaseResource resource : theUpdateList) {
			theBundleBuilder.addTransactionUpdateEntry(resource);
		}
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
			// Skip references with no literal reference value (e.g. identifier-only or display-only references).
			// Such references have a null refValue, which would NPE on lookups against an immutable map.
			if (refValue == null || refValue.isEmpty()) {
				continue;
			}
			String replacement = theReferenceMap.get(refValue);
			if (replacement != null) {
				refInfo.getResourceReference().setReference(replacement);
			}
		}
	}
}
