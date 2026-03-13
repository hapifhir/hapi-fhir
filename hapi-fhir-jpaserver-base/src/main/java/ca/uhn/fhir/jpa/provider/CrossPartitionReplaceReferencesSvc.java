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
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
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
import org.hl7.fhir.r4.model.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Discovers resources referencing a source resource, moves them to the target resource's
 * partition via a transaction bundle (CREATEs + PUTs), and deletes the originals.
 * <p>
 * All operations are performed within a single DB transaction for atomicity.
 */
// Created by claude-opus-4-6
public class CrossPartitionReplaceReferencesSvc {
	private static final Logger ourLog = LoggerFactory.getLogger(CrossPartitionReplaceReferencesSvc.class);

	private final DaoRegistry myDaoRegistry;
	private final IResourceLinkDao myResourceLinkDao;
	private final IRequestPartitionHelperSvc myRequestPartitionHelperSvc;
	private final IFhirSystemDao<IBaseBundle, ?> mySystemDao;
	private final FhirContext myFhirContext;

	public CrossPartitionReplaceReferencesSvc(
			DaoRegistry theDaoRegistry,
			IResourceLinkDao theResourceLinkDao,
			IRequestPartitionHelperSvc theRequestPartitionHelperSvc,
			IFhirSystemDao<IBaseBundle, ?> theSystemDao) {
		myDaoRegistry = theDaoRegistry;
		myResourceLinkDao = theResourceLinkDao;
		myRequestPartitionHelperSvc = theRequestPartitionHelperSvc;
		mySystemDao = theSystemDao;
		myFhirContext = theDaoRegistry.getFhirContext();
	}

	/**
	 * Moves referencing resources from the source resource's partition to the target resource's partition,
	 * and updates references in resources that don't change partition. Assumes the caller provides the outer
	 * transaction — all internal operations use {@code transactionNested()}.
	 * <p>
	 * Does NOT delete the source copies — the caller is responsible for deleting them after
	 * provenance creation using the returned {@link CrossPartitionMoveResult#getReferencesToMovedResourceOriginals()}.
	 *
	 * @return a {@link CrossPartitionMoveResult} containing references to created/updated resources
	 *         and versioned references to the original source copies for deferred deletion.
	 */
	public CrossPartitionMoveResult moveCompartmentResourcesAndReplaceReferences(
			IBaseResource theSourceResource, IBaseResource theTargetResource, RequestDetails theRequestDetails) {

		IIdType sourceId = theSourceResource.getIdElement().toUnqualifiedVersionless();
		IIdType targetId = theTargetResource.getIdElement().toUnqualifiedVersionless();

		RequestPartitionId sourcePartitionId =
				RequestPartitionId.getPartitionIfAssigned(theSourceResource).orElse(null);
		RequestPartitionId targetPartitionId =
				RequestPartitionId.getPartitionIfAssigned(theTargetResource).orElse(null);

		ourLog.info(
				"Cross-partition merge: moving referencing resources from {} (partition {}) to {} (partition {})",
				sourceId.getValue(),
				sourcePartitionId,
				targetId.getValue(),
				targetPartitionId);

		// Step 1: Discover all resources referencing the source resource
		List<IBaseResource> allReferencingResources = discoverReferencingResources(sourceId, theRequestDetails);

		if (allReferencingResources.isEmpty()) {
			ourLog.info("No referencing resources found for {}", sourceId.getValue());
			return new CrossPartitionMoveResult(List.of(), List.of());
		}

		// Step 2: Classify into MOVE (partition changes after rewrite) vs UPDATE (same partition)
		List<IBaseResource> moveList = new ArrayList<>();
		List<IBaseResource> updateList = new ArrayList<>();
		classifyResourcesAndReplaceSourceReferences(
				allReferencingResources,
				sourceId.getValue(),
				targetId.getValue(),
				theRequestDetails,
				moveList,
				updateList);

		ourLog.info(
				"Classified {} resources: {} to move, {} to update references",
				allReferencingResources.size(),
				moveList.size(),
				updateList.size());

		if (moveList.isEmpty() && updateList.isEmpty()) {
			return new CrossPartitionMoveResult(List.of(), List.of());
		}

		// Capture versioned IDs from moveList before buildCombinedBundle clears them
		List<Reference> movedResourceOriginals = new ArrayList<>();
		for (IBaseResource resource : moveList) {
			movedResourceOriginals.add(new Reference(resource.getIdElement().getValue()));
		}

		// Step 3: Discover additional resources to update BEFORE bundle execution.
		// This only needs the old IDs of moved resources, not the new IDs.
		Set<IIdType> movedResourceOldIds = moveList.stream()
				.map(r -> r.getIdElement().toUnqualifiedVersionless())
				.collect(Collectors.toCollection(LinkedHashSet::new));
		discoverAndAddAdditionalResourcesToUpdate(movedResourceOldIds, updateList, theRequestDetails);

		// Step 4: Build and execute a single combined transaction bundle.
		// TransactionSorter processes POST entries before PUT entries, and
		// IdSubstitutionMap resolves urn:uuid placeholders from POST fullUrl
		// values in PUT entry references — so PUT entries can reference
		// moved resources via the same urn:uuid placeholders used by POST entries.
		Map<String, String> oldIdToPlaceholder = new HashMap<>();
		IBaseBundle combinedBundle = buildCombinedBundle(moveList, updateList, oldIdToPlaceholder);
		IBaseBundle combinedResponse = mySystemDao.transactionNested(theRequestDetails, combinedBundle);
		List<Reference> referencesToChangedResources =
				ReplaceReferencesProvenanceSvc.extractChangedResourceReferences(List.of((Bundle) combinedResponse));

		ourLog.info(
				"Cross-partition merge complete: moved {} resources, updated {} references",
				moveList.size(),
				updateList.size());

		return new CrossPartitionMoveResult(referencesToChangedResources, movedResourceOriginals);
	}

	private List<IBaseResource> discoverReferencingResources(IIdType theSourceId, RequestDetails theRequestDetails) {
		List<IdDt> ids = myResourceLinkDao
				.streamSourceIdsForTargetFhirId(theSourceId.getResourceType(), theSourceId.getIdPart())
				.toList();
		return loadResources(ids, theRequestDetails);
	}

	/**
	 * Discovers resources that reference moved resources (by their old IDs) and appends
	 * them to {@code theUpdateList}. Resources already in the move/update lists are excluded
	 * to avoid duplicate entries in the transaction bundle.
	 * <p>
	 * This handles cases where resources that do not reference the source resource but do reference
	 * a moved resource that got a new ID (e.g., a List referencing a moved Encounter).
	 */
	private void discoverAndAddAdditionalResourcesToUpdate(
			Set<IIdType> theMovedResourceOldIds, List<IBaseResource> theUpdateList, RequestDetails theRequestDetails) {
		if (theMovedResourceOldIds.isEmpty()) {
			return;
		}

		Set<String> alreadyDiscoveredIds = new HashSet<>();
		alreadyDiscoveredIds.addAll(
				theMovedResourceOldIds.stream().map(IIdType::getValue).toList());
		alreadyDiscoveredIds.addAll(theUpdateList.stream()
				.map(r -> r.getIdElement().toUnqualifiedVersionless().getValue())
				.toList());

		List<IdDt> additionalIds = new ArrayList<>();
		for (IIdType oldId : theMovedResourceOldIds) {
			myResourceLinkDao
					.streamSourceIdsForTargetFhirId(oldId.getResourceType(), oldId.getIdPart())
					.forEach(id -> {
						if (alreadyDiscoveredIds.add(
								id.toUnqualifiedVersionless().getValue())) {
							additionalIds.add(id);
						}
					});
		}

		if (!additionalIds.isEmpty()) {
			List<IBaseResource> additionalResources = loadResources(additionalIds, theRequestDetails);
			ourLog.info(
					"Discovered {} additional resources referencing resources to be moved across partitions",
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

	private void classifyResourcesAndReplaceSourceReferences(
			List<IBaseResource> theResources,
			String theSourceRef,
			String theTargetRef,
			RequestDetails theRequestDetails,
			List<IBaseResource> theMoveList,
			List<IBaseResource> theUpdateList) {

		for (IBaseResource resource : theResources) {
			Integer currentPartitionId = RequestPartitionId.getPartitionIfAssigned(resource)
					.map(RequestPartitionId::getFirstPartitionIdOrNull)
					.orElse(null);

			// Rewrite source→target references so determineCreatePartitionForRequest
			// routes based on the post-merge state.
			replaceVersionlessReferences(resource, Map.of(theSourceRef, theTargetRef));

			Integer newPartitionId = determinePartition(resource, theRequestDetails);

			if (Objects.equals(currentPartitionId, newPartitionId)) {
				theUpdateList.add(resource);
			} else {
				theMoveList.add(resource);
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
		theResource.setUserData(Constants.RESOURCE_PARTITION_ID, null);
		try {
			String resourceType = myFhirContext.getResourceType(theResource);
			RequestPartitionId targetPartition = myRequestPartitionHelperSvc.determineCreatePartitionForRequest(
					theRequestDetails, theResource, resourceType);
			return targetPartition.getFirstPartitionIdOrNull();
		} finally {
			theResource.setUserData(Constants.RESOURCE_PARTITION_ID, savedPartitionUserData);
		}
	}

	/**
	 * Builds a single combined transaction bundle containing POST (CREATE) entries for moved
	 * resources and PUT (UPDATE) entries for reference-only changes. References to moved resources
	 * are replaced with {@code urn:uuid} placeholders in both lists — the transaction processor's
	 * {@code IdSubstitutionMap} resolves these after the POST entries create the new resources.
	 * <p>
	 * Source→target references are already rewritten by {@link #classifyResourcesAndReplaceSourceReferences}.
	 *
	 * @param theOldIdToPlaceholder populated by this method with old ID → urn:uuid mappings
	 */
	private IBaseBundle buildCombinedBundle(
			List<IBaseResource> theMoveList,
			List<IBaseResource> theUpdateList,
			Map<String, String> theOldIdToPlaceholder) {

		// Build old ID → urn:uuid placeholder map from the move list
		for (IBaseResource resource : theMoveList) {
			String oldId = resource.getIdElement().toUnqualifiedVersionless().getValue();
			theOldIdToPlaceholder.put(oldId, IdDt.newRandomUuid().getValue());
		}

		// Replace inter-resource references with urn:uuid placeholders in BOTH lists.
		// Source→target references were already rewritten by classifyResourcesAndReplaceSourceReferences.
		replaceVersionlessReferences(theMoveList, theOldIdToPlaceholder);
		replaceVersionlessReferences(theUpdateList, theOldIdToPlaceholder);

		BundleBuilder bundleBuilder = new BundleBuilder(myFhirContext);

		// POST entries: clear partition + ID on moved resources, add as CREATE with placeholder fullUrl
		for (IBaseResource resource : theMoveList) {
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
