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
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.replacereferences.ReplaceReferencesProvenanceSvc;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
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
public class PartitionAwareReplaceReferencesSvc {
	private static final Logger ourLog = LoggerFactory.getLogger(PartitionAwareReplaceReferencesSvc.class);

	private final DaoRegistry myDaoRegistry;
	private final ReferencingResourcesQuerySvc myReferencingResourcesQuerySvc;
	private final IRequestPartitionHelperSvc myRequestPartitionHelperSvc;
	private final IHapiTransactionService myHapiTransactionService;
	private final FhirContext myFhirContext;

	public PartitionAwareReplaceReferencesSvc(
			DaoRegistry theDaoRegistry,
			ReferencingResourcesQuerySvc theReferencingResourcesQuerySvc,
			IRequestPartitionHelperSvc theRequestPartitionHelperSvc,
			IHapiTransactionService theHapiTransactionService) {
		myDaoRegistry = theDaoRegistry;
		myReferencingResourcesQuerySvc = theReferencingResourcesQuerySvc;
		myRequestPartitionHelperSvc = theRequestPartitionHelperSvc;
		myHapiTransactionService = theHapiTransactionService;
		myFhirContext = theDaoRegistry.getFhirContext();
	}

	/**
	 * Copies referencing resources from the source resource's partition to the target resource's partition,
	 * and updates references in resources that don't change partition. Assumes the caller provides the outer
	 * transaction — all internal operations use {@code transactionNested()}.
	 * <p>
	 * Does NOT delete the source copies — the caller is responsible for deleting them after
	 * provenance creation using the returned {@link PartitionAwareReplaceReferencesResult#getCopiedResourceOriginalIdsByOriginalPartition()}.
	 * Deletion cannot happen here because provenance must reference the originals (as tombstones),
	 * and deleting first would violate referential integrity checks.
	 *
	 * @return a {@link PartitionAwareReplaceReferencesResult} containing references to created/updated resources
	 *         and versioned references to the original source copies for deferred deletion.
	 */
	public PartitionAwareReplaceReferencesResult copyCompartmentResourcesAndReplaceReferences(
			IBaseResource theSourceResource,
			IBaseResource theTargetResource,
			int theResourceLimit,
			RequestDetails theRequestDetails) {

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
			return new PartitionAwareReplaceReferencesResult(Map.of(), Map.of(), Map.of());
		}

		// Step 2: Classify each referencing resource as COPY (moves partition once its references are rewritten)
		// or UPDATE (stays put, not grouped). Copies are grouped by destination partition only to key the returned
		// per-partition result (which the provenance and undo are built from); the write itself is routed by the
		// transaction re-deriving each copy's partition from its rewritten references, not by this map.
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
			return new PartitionAwareReplaceReferencesResult(Map.of(), Map.of(), Map.of());
		}

		// Capture versioned IDs from copyList before buildCombinedBundle clears them
		Map<RequestPartitionId, List<IIdType>> copiedResourceOriginalIdsByOriginalPartition =
				groupIdsByPartition(copyList);

		// Step 3: Discover additional resources to update BEFORE building the plan.
		discoverAndAddAdditionalResourcesToUpdate(copyList, updateList, theRequestDetails);

		// Step 4: Build one ordered plan of bundle entries, each carrying its partition and whether it's a create
		// (copy) or update, so the response can be interpreted without parallel bookkeeping. Copies come first so
		// their POSTed placeholders resolve before the PUT updates that reference them.
		List<PlannedEntry> plan = new ArrayList<>();
		copiesByDestPartition.forEach((partition, resources) ->
				resources.forEach(resource -> plan.add(new PlannedEntry(resource, partition, ChangeType.CREATE))));
		updateList.forEach(
				resource -> plan.add(new PlannedEntry(resource, getRequiredPartition(resource), ChangeType.UPDATE)));

		if (plan.size() > theResourceLimit) {
			throw new PreconditionFailedException(Msg.code(3023)
					+ String.format(
							"Number of resources that would be moved or updated by merging %s into %s exceeds the"
									+ " resource-limit %d.",
							sourceId.getValue(), targetId.getValue(), theResourceLimit));
		}

		Bundle combinedResponse =
				(Bundle) myDaoRegistry.getSystemDao().transactionNested(theRequestDetails, buildCombinedBundle(plan));

		// Step 5: Map each response entry to its partition and change type via the plan (entry i ↔ plan i).
		List<Bundle.BundleEntryComponent> responseEntries = combinedResponse.getEntry();
		Map<RequestPartitionId, List<IIdType>> createdResourceIdsByPartition = new LinkedHashMap<>();
		Map<RequestPartitionId, List<IIdType>> updatedResourceIdsByPartition = new LinkedHashMap<>();
		for (int i = 0; i < responseEntries.size(); i++) {
			PlannedEntry planned = plan.get(i);
			Map<RequestPartitionId, List<IIdType>> idsByPartition = planned.changeType() == ChangeType.CREATE
					? createdResourceIdsByPartition
					: updatedResourceIdsByPartition;
			ReplaceReferencesProvenanceSvc.extractChangedResourceId(responseEntries.get(i))
					.ifPresent(id -> idsByPartition
							.computeIfAbsent(planned.partition(), k -> new ArrayList<>())
							.add(id));
		}

		return new PartitionAwareReplaceReferencesResult(
				createdResourceIdsByPartition,
				updatedResourceIdsByPartition,
				copiedResourceOriginalIdsByOriginalPartition);
	}

	/**
	 * Finds all resource IDs that have a reference link pointing to the given source resource,
	 * then loads and returns those resources.
	 */
	private List<IBaseResource> discoverReferencingResources(IIdType theSourceId, RequestDetails theRequestDetails) {
		List<JpaPid> ids = myReferencingResourcesQuerySvc.findReferencingResourcePidsAcrossAllPartitions(
				theSourceId, theRequestDetails);
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

		List<JpaPid> additionalIds = new ArrayList<>();
		for (IBaseResource resource : theCopyList) {
			IIdType oldId = resource.getIdElement();
			List<JpaPid> referrers = myReferencingResourcesQuerySvc.findReferencingResourcePidsAcrossAllPartitions(
					oldId, theRequestDetails);
			for (JpaPid referrer : referrers) {
				if (alreadyDiscoveredIds.add(referrer.getAssociatedResourceId()
						.toUnqualifiedVersionless()
						.getValue())) {
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
				.orElseThrow(() -> new IllegalStateException(Msg.code(3015) + "Resource "
						+ theResource.getIdElement().getValue() + " has no partition info"));
	}

	private static Map<RequestPartitionId, List<IIdType>> groupIdsByPartition(List<IBaseResource> theResources) {
		Map<RequestPartitionId, List<IIdType>> result = new LinkedHashMap<>();
		for (IBaseResource resource : theResources) {
			RequestPartitionId partition = getRequiredPartition(resource);
			result.computeIfAbsent(partition, k -> new ArrayList<>()).add(resource.getIdElement());
		}
		return result;
	}

	private List<IBaseResource> loadResources(List<JpaPid> theIds, RequestDetails theRequestDetails) {
		List<IBaseResource> result = new ArrayList<>();
		for (JpaPid referencingId : theIds) {
			IIdType id = referencingId.getAssociatedResourceId();
			try {
				@SuppressWarnings("unchecked")
				IFhirResourceDao<IBaseResource> dao = myDaoRegistry.getResourceDao(id.getResourceType());
				// Pin the read to the partition the referrer actually lives in (captured from the link index),
				// rather than relying on the id to resolve the shard — the id may not identify the partition.
				IBaseResource resource = myHapiTransactionService
						.withRequest(theRequestDetails)
						.withRequestPartitionId(RequestPartitionId.fromPartitionId(referencingId.getPartitionId()))
						.execute(() -> dao.read(id.toVersionless(), theRequestDetails));
				result.add(resource);
			} catch (ResourceGoneException e) {
				ourLog.warn("Skipping deleted resource: {}", id.getValue());
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
			RequestPartitionId currentPartition = determinePartition(resource, theRequestDetails);

			// Rewrite source→target references so determineCreatePartitionForRequest
			// routes based on the post-merge state.
			replaceVersionlessReferences(resource, Map.of(theSourceRef, theTargetRef));

			RequestPartitionId newPartition = determinePartition(resource, theRequestDetails);

			if (Objects.equals(currentPartition, newPartition)) {
				theUpdateList.add(resource);
			} else {
				theCopiesByDestPartition
						.computeIfAbsent(newPartition, k -> new ArrayList<>())
						.add(resource);
			}
		}
	}

	private RequestPartitionId determinePartition(IBaseResource theResource, RequestDetails theRequestDetails) {
		return myRequestPartitionHelperSvc.determineCreatePartitionForRequestIgnoringCachedPartition(
				theRequestDetails, theResource, myFhirContext.getResourceType(theResource));
	}

	private enum ChangeType {
		CREATE,
		UPDATE
	}

	private record PlannedEntry(IBaseResource resource, RequestPartitionId partition, ChangeType changeType) {}

	/**
	 * Builds a single combined transaction bundle containing POST (CREATE) entries for copied
	 * resources and PUT (UPDATE) entries for reference-only changes. References to copied resources
	 * are replaced with {@code urn:uuid} placeholders — the transaction processor's
	 * {@code IdSubstitutionMap} resolves these after the POST entries create the new resources.
	 * <p>
	 * Source→target references are already rewritten by {@link #replaceSourceReferencesAndClassifyResources}.
	 */
	private IBaseBundle buildCombinedBundle(List<PlannedEntry> thePlan) {
		Map<String, String> oldIdToPlaceholder = new HashMap<>();
		BundleBuilder bundleBuilder = new BundleBuilder(myFhirContext);

		// Assign a urn:uuid placeholder to each created (copied) resource's old ID
		for (PlannedEntry entry : thePlan) {
			if (entry.changeType() == ChangeType.CREATE) {
				String oldId = entry.resource()
						.getIdElement()
						.toUnqualifiedVersionless()
						.getValue();
				oldIdToPlaceholder.put(oldId, IdDt.newRandomUuid().getValue());
			}
		}

		// Point every entry's inter-resource references at those placeholders (source→target refs were already
		// rewritten during classification).
		for (PlannedEntry entry : thePlan) {
			replaceVersionlessReferences(entry.resource(), oldIdToPlaceholder);
		}

		for (PlannedEntry entry : thePlan) {
			IBaseResource resource = entry.resource();
			if (entry.changeType() == ChangeType.CREATE) {
				// CREATE: clear partition + ID, add with placeholder fullUrl
				String placeholder = oldIdToPlaceholder.get(
						resource.getIdElement().toUnqualifiedVersionless().getValue());
				resource.setUserData(Constants.RESOURCE_PARTITION_ID, null);
				resource.setId((IIdType) null);
				bundleBuilder.addTransactionCreateEntry(resource, placeholder);
			} else {
				// UPDATE: keep partition ID intact
				bundleBuilder.addTransactionUpdateEntry(resource);
			}
		}

		return bundleBuilder.getBundle();
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
			// Skip references with no literal value (e.g. identifier-only or display-only): they can't match a
			// key, and a null key would throw NPE on the immutable Map.of used by the source→target caller.
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
