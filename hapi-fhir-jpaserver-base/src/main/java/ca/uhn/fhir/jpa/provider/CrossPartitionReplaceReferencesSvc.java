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
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Discovers compartment resources for a source patient, moves them to the target patient's
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
	 * Moves compartment resources from source patient's partition to target patient's partition,
	 * and updates references in non-compartment resources. Assumes the caller provides the outer
	 * transaction — all internal operations use {@code transactionNested()}.
	 * <p>
	 * Does NOT delete the source copies — the caller is responsible for deleting them after
	 * provenance creation using the returned {@link CrossPartitionMoveResult#getReferencesToMovedResourceOriginals()}.
	 *
	 * @return a {@link CrossPartitionMoveResult} containing references to created/updated resources
	 *         and versioned references to the original source copies for deferred deletion.
	 */
	public CrossPartitionMoveResult moveCompartmentResourcesAndReplaceReferences(
			IBaseResource theSourcePatient, IBaseResource theTargetPatient, RequestDetails theRequestDetails) {

		String sourcePatientId = theSourcePatient.getIdElement().getIdPart();
		String targetPatientId = theTargetPatient.getIdElement().getIdPart();

		RequestPartitionId sourcePartitionId =
				RequestPartitionId.getPartitionIfAssigned(theSourcePatient).orElse(null);
		RequestPartitionId targetPartitionId =
				RequestPartitionId.getPartitionIfAssigned(theTargetPatient).orElse(null);

		ourLog.info(
				"Cross-partition merge: moving compartment resources from Patient/{} (partition {}) to Patient/{} (partition {})",
				sourcePatientId,
				sourcePartitionId,
				targetPatientId,
				targetPartitionId);

		// Step 1: Discover all resources referencing the source patient
		List<IBaseResource> allReferencingResources = discoverReferencingResources(sourcePatientId, theRequestDetails);

		if (allReferencingResources.isEmpty()) {
			ourLog.info("No referencing resources found for Patient/{}", sourcePatientId);
			return new CrossPartitionMoveResult(List.of(), List.of(), List.of());
		}

		// Step 2: Classify into MOVE (in source compartment + source partition) vs UPDATE
		List<IBaseResource> moveList = new ArrayList<>();
		List<IBaseResource> updateList = new ArrayList<>();
		classifyResourcesAndReplaceSourceReferences(
				allReferencingResources, sourcePatientId, targetPatientId, theRequestDetails, moveList, updateList);

		ourLog.info(
				"Classified {} resources: {} to move, {} to update references",
				allReferencingResources.size(),
				moveList.size(),
				updateList.size());

		if (moveList.isEmpty() && updateList.isEmpty()) {
			return new CrossPartitionMoveResult(List.of(), List.of(), List.of());
		}

		// Capture versioned IDs from moveList before buildCreateBundle clears them
		List<Reference> movedResourceOriginals = new ArrayList<>();
		for (IBaseResource resource : moveList) {
			movedResourceOriginals.add(new Reference(resource.getIdElement().getValue()));
		}

		List<Reference> referencesToCreatedResources = List.of();
		List<Reference> referencesToUpdatedResources = List.of();

		// Step 3: Execute CREATE bundle — pass through the incoming RequestDetails so the
		// partition interceptor can determine the correct partition for each resource
		// (e.g. from tenant ID, headers, or patient reference depending on mode).
		// Note: buildCreateBundle clears RESOURCE_PARTITION_ID on each resource so the
		// interceptor determines the partition fresh rather than short-circuiting with
		// the source partition.
		LinkedHashMap<String, String> oldIdToPlaceholder = new LinkedHashMap<>();
		Map<String, String> movedResourceOldToNewIdMap = new HashMap<>();
		if (!moveList.isEmpty()) {
			IBaseBundle createBundle = buildCreateBundle(moveList, oldIdToPlaceholder);
			IBaseBundle createResponse = mySystemDao.transactionNested(theRequestDetails, createBundle);
			movedResourceOldToNewIdMap =
					extractOldToNewIdMap(new ArrayList<>(oldIdToPlaceholder.keySet()), createResponse);
			referencesToCreatedResources =
					ReplaceReferencesProvenanceSvc.extractChangedResourceReferences(List.of((Bundle) createResponse));
		}

		// Step 4: Execute UPDATE bundle — UPDATE resources were loaded from DB so
		// RESOURCE_PARTITION_ID is already set on each; determineCreatePartitionForRequest()
		// uses that directly.
		if (!updateList.isEmpty()) {
			IBaseBundle updateBundle = buildUpdateBundle(updateList, movedResourceOldToNewIdMap);
			IBaseBundle updateResponse = mySystemDao.transactionNested(theRequestDetails, updateBundle);
			referencesToUpdatedResources =
					ReplaceReferencesProvenanceSvc.extractChangedResourceReferences(List.of((Bundle) updateResponse));
		}

		ourLog.info(
				"Cross-partition merge complete: moved {} resources, updated {} references",
				moveList.size(),
				updateList.size());

		return new CrossPartitionMoveResult(
				referencesToCreatedResources, referencesToUpdatedResources, movedResourceOriginals);
	}

	private List<IBaseResource> discoverReferencingResources(
			String theSourcePatientId, RequestDetails theRequestDetails) {
		List<IBaseResource> result = new ArrayList<>();

		Stream<IdDt> idStream = myResourceLinkDao.streamSourceIdsForTargetFhirId("Patient", theSourcePatientId);
		List<IdDt> ids = idStream.distinct().toList();

		for (IdDt id : ids) {
			try {
				@SuppressWarnings("unchecked")
				IFhirResourceDao<IBaseResource> dao = myDaoRegistry.getResourceDao(id.getResourceType());
				IBaseResource resource = dao.read(id.toVersionless(), theRequestDetails);
				result.add(resource);
			} catch (ResourceGoneException | ResourceNotFoundException e) {
				ourLog.warn("Skipping deleted/not-found resource: {}", id.getValue());
			}
		}

		return result;
	}

	private void classifyResourcesAndReplaceSourceReferences(
			List<IBaseResource> theResources,
			String theSourcePatientId,
			String theTargetPatientId,
			RequestDetails theRequestDetails,
			List<IBaseResource> theMoveList,
			List<IBaseResource> theUpdateList) {

		String sourcePatientRef = "Patient/" + theSourcePatientId;
		String targetPatientRef = "Patient/" + theTargetPatientId;

		for (IBaseResource resource : theResources) {
			Integer currentPartitionId = RequestPartitionId.getPartitionIfAssigned(resource)
					.map(RequestPartitionId::getFirstPartitionIdOrNull)
					.orElse(null);

			// Rewrite source→target patient references so determineCreatePartitionForRequest
			// routes based on the post-merge state.
			replaceVersionlessReferences(resource, Map.of(sourcePatientRef, targetPatientRef));

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
	 * Builds a transaction bundle containing POST (CREATE) entries for all compartment resources
	 * to be moved to the target partition. References between moved resources are replaced with
	 * urn:uuid placeholders. Patient references are already rewritten by
	 * {@link #classifyResourcesAndReplaceSourceReferences}.
	 * <p>
	 * {@code theOldIdToPlaceholder} is populated as a {@link LinkedHashMap} whose key insertion
	 * order matches the order of entries added to the returned bundle, so that
	 * {@link #extractOldToNewIdMap} can correlate response entries by index.
	 */
	private IBaseBundle buildCreateBundle(
			List<IBaseResource> theMoveList, LinkedHashMap<String, String> theOldIdToPlaceholder) {

		// Build placeholder map; insertion order is preserved so keys
		// can be correlated by index with CREATE response entries in extractOldToNewIdMap.
		for (IBaseResource resource : theMoveList) {
			String oldId = resource.getIdElement().toUnqualifiedVersionless().getValue();
			theOldIdToPlaceholder.put(oldId, IdDt.newRandomUuid().getValue());
		}

		// Replace inter-move references with urn:uuid placeholders.
		// Patient references were already rewritten by classifyResourcesAndReplaceSourceReferences.
		replaceVersionlessReferences(theMoveList, theOldIdToPlaceholder);

		// Clear partition + ID and build CREATE entries
		BundleBuilder bundleBuilder = new BundleBuilder(myFhirContext);
		for (IBaseResource resource : theMoveList) {
			String oldId = resource.getIdElement().toUnqualifiedVersionless().getValue();
			String placeholder = theOldIdToPlaceholder.get(oldId);
			resource.setUserData(Constants.RESOURCE_PARTITION_ID, null);
			resource.setId((IIdType) null);
			bundleBuilder.addTransactionCreateEntry(resource, placeholder);
		}

		return bundleBuilder.getBundle();
	}

	/**
	 * Builds a map from each old resource ID to its new ID by correlating the CREATE response
	 * entries (in order) with the keys of {@code theOldIdToPlaceholder} (a {@link LinkedHashMap}
	 * whose key order matches the bundle entry order).
	 */
	@SuppressWarnings("unchecked")
	private Map<String, String> extractOldToNewIdMap(List<String> theOldIds, IBaseBundle theCreateResponse) {

		FhirTerser terser = myFhirContext.newTerser();
		List<IBase> entries = terser.getValues(theCreateResponse, "Bundle.entry", IBase.class);

		if (entries.size() != theOldIds.size()) {
			// TODO Emre: add Msg.Code
			throw new IllegalStateException("CREATE transaction response entry count (" + entries.size()
					+ ") does not match expected resource count (" + theOldIds.size() + ")");
		}

		Map<String, String> result = new HashMap<>();
		for (int i = 0; i < entries.size(); i++) {
			List<IBase> locationValues = terser.getValues(entries.get(i), "response.location", IBase.class);
			if (!locationValues.isEmpty() && locationValues.get(0) instanceof IPrimitiveType) {
				String location = ((IPrimitiveType<String>) locationValues.get(0)).getValueAsString();
				String newId = new IdDt(location)
						.toVersionless()
						.toUnqualifiedVersionless()
						.getValue();
				result.put(theOldIds.get(i), newId);
			}
		}

		if (result.size() != theOldIds.size()) {
			// TODO Emre: add Msg.Code
			throw new IllegalStateException("Could not extract new IDs for all moved resources: expected "
					+ theOldIds.size() + " but got " + result.size());
		}

		return result;
	}

	/**
	 * Builds a transaction bundle containing PUT (UPDATE) entries for non-compartment resources
	 * whose references to moved resources need updating. Patient references were already rewritten
	 * by {@link #classifyResourcesAndReplaceSourceReferences}.
	 */
	private IBaseBundle buildUpdateBundle(
			List<IBaseResource> theUpdateList, Map<String, String> theMovedResourceOldToNewIdMap) {

		replaceVersionlessReferences(theUpdateList, theMovedResourceOldToNewIdMap);

		BundleBuilder bundleBuilder = new BundleBuilder(myFhirContext);
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
