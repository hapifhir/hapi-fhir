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
package ca.uhn.fhir.jpa.partition.move;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.dao.data.IResourceLinkDao;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.searchparam.extractor.ISearchParamExtractor;
import ca.uhn.fhir.jpa.util.ResourceCompartmentUtil;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Quick/dirty implementation of cross-partition resource mover for patient merge.
 * Discovers compartment resources for a source patient, moves them to the target patient's
 * partition via a transaction bundle (CREATEs + PUTs), and deletes the originals.
 * <p>
 * All operations are performed within a single DB transaction for atomicity.
 */
// Created by claude-opus-4-6
public class CrossPartitionResourceMoverSvc {
	private static final Logger ourLog = LoggerFactory.getLogger(CrossPartitionResourceMoverSvc.class);

	private final DaoRegistry myDaoRegistry;
	private final IResourceLinkDao myResourceLinkDao;
	private final IHapiTransactionService myHapiTransactionService;
	private final ISearchParamExtractor mySearchParamExtractor;
	private final IFhirSystemDao<IBaseBundle, ?> mySystemDao;
	private final FhirContext myFhirContext;

	public CrossPartitionResourceMoverSvc(
			DaoRegistry theDaoRegistry,
			IResourceLinkDao theResourceLinkDao,
			IHapiTransactionService theHapiTransactionService,
			ISearchParamExtractor theSearchParamExtractor,
			IFhirSystemDao<IBaseBundle, ?> theSystemDao) {
		myDaoRegistry = theDaoRegistry;
		myResourceLinkDao = theResourceLinkDao;
		myHapiTransactionService = theHapiTransactionService;
		mySearchParamExtractor = theSearchParamExtractor;
		mySystemDao = theSystemDao;
		myFhirContext = theDaoRegistry.getFhirContext();
	}

	/**
	 * Moves compartment resources from source patient's partition to target patient's partition,
	 * and updates references in non-compartment resources. All within a single DB transaction.
	 */
	public void moveCompartmentResourcesAndReplaceReferences(
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

		myHapiTransactionService.withRequest(theRequestDetails).execute(() -> {
			// Step 1: Discover all resources referencing the source patient
			List<IBaseResource> allReferencingResources = discoverReferencingResources(sourcePatientId);

			if (allReferencingResources.isEmpty()) {
				ourLog.info("No referencing resources found for Patient/{}", sourcePatientId);
				return;
			}

			// Step 2: Classify into MOVE (in source compartment + source partition) vs UPDATE
			List<IBaseResource> moveList = new ArrayList<>();
			List<IBaseResource> updateList = new ArrayList<>();
			classifyResources(allReferencingResources, sourcePatientId, sourcePartitionId, moveList, updateList);

			ourLog.info(
					"Classified {} resources: {} to move, {} to update references",
					allReferencingResources.size(),
					moveList.size(),
					updateList.size());

			if (moveList.isEmpty() && updateList.isEmpty()) {
				return;
			}

			// Step 3: Execute CREATE bundle — new SystemRequestDetails() (no explicit partition)
			// lets the PatientIdPartitionInterceptor fire and route each resource to the
			// correct partition based on its patient reference.
			LinkedHashMap<String, String> oldIdToPlaceholder = new LinkedHashMap<>();
			Map<String, String> oldToNewIdMap = new HashMap<>();
			if (!moveList.isEmpty()) {
				IBaseBundle createBundle =
						buildCreateBundle(moveList, sourcePatientId, targetPatientId, oldIdToPlaceholder);
				IBaseBundle createResponse = mySystemDao.transactionNested(new SystemRequestDetails(), createBundle);
				oldToNewIdMap = extractOldToNewIdMap(new ArrayList<>(oldIdToPlaceholder.keySet()), createResponse);
			}

			// Step 4: Execute UPDATE bundle — UPDATE resources were loaded from DB so
			// RESOURCE_PARTITION_ID is already set on each; determineCreatePartitionForRequest()
			// uses that directly, ignoring SystemRequestDetails.
			if (!updateList.isEmpty()) {
				IBaseBundle updateBundle =
						buildUpdateBundle(updateList, sourcePatientId, targetPatientId, oldToNewIdMap);
				mySystemDao.transactionNested(new SystemRequestDetails(), updateBundle);
			}

			// Step 5: Delete source copies of moved resources via a single transaction bundle
			if (!oldIdToPlaceholder.isEmpty()) {
				BundleBuilder deleteBuilder = new BundleBuilder(myFhirContext);
				for (String oldId : oldIdToPlaceholder.keySet()) {
					deleteBuilder.addTransactionDeleteEntry(new IdDt(oldId));
				}
				mySystemDao.transactionNested(
						SystemRequestDetails.forRequestPartitionId(sourcePartitionId), deleteBuilder.getBundle());
			}

			ourLog.info(
					"Cross-partition merge complete: moved {} resources, updated {} references",
					moveList.size(),
					updateList.size());
		});
	}

	private List<IBaseResource> discoverReferencingResources(String theSourcePatientId) {
		List<IBaseResource> result = new ArrayList<>();

		Stream<IdDt> idStream = myResourceLinkDao.streamSourceIdsForTargetFhirId("Patient", theSourcePatientId);
		List<IdDt> ids = idStream.distinct().collect(java.util.stream.Collectors.toList());

		SystemRequestDetails readRequest =
				SystemRequestDetails.forRequestPartitionId(RequestPartitionId.allPartitions());

		for (IdDt id : ids) {
			// Skip Patient — the merge flow handles patients separately
			if ("Patient".equals(id.getResourceType())) {
				continue;
			}

			try {
				@SuppressWarnings("unchecked")
				IFhirResourceDao<IBaseResource> dao = myDaoRegistry.getResourceDao(id.getResourceType());
				IBaseResource resource = dao.read(id.toVersionless(), readRequest);
				result.add(resource);
			} catch (ResourceGoneException | ResourceNotFoundException e) {
				ourLog.debug("Skipping deleted/not-found resource: {}", id.getValue());
			}
		}

		return result;
	}

	private void classifyResources(
			List<IBaseResource> theResources,
			String theSourcePatientId,
			RequestPartitionId theSourcePartitionId,
			List<IBaseResource> theMoveList,
			List<IBaseResource> theUpdateList) {

		for (IBaseResource resource : theResources) {
			RequestPartitionId resourcePartition =
					RequestPartitionId.getPartitionIfAssigned(resource).orElse(null);

			RuntimeResourceDefinition resDef = myFhirContext.getResourceDefinition(resource);
			List<RuntimeSearchParam> compartmentSps = ResourceCompartmentUtil.getPatientCompartmentSearchParams(resDef);
			Optional<String> compartmentPatient = compartmentSps.isEmpty()
					? Optional.empty()
					: ResourceCompartmentUtil.getResourceCompartment(
							"Patient", resource, compartmentSps, mySearchParamExtractor);

			boolean inSourceCompartment =
					compartmentPatient.isPresent() && compartmentPatient.get().equals(theSourcePatientId);
			// Compare by numeric partition ID only — the source patient's RequestPartitionId may include
			// a partition name (e.g. {id:65, name:"A"}) while the resource loaded from the DB may have
			// only the numeric id (e.g. {id:65}). RequestPartitionId.equals() compares names too, so we
			// compare just the first partition ID to avoid false negatives.
			Integer sourcePartId =
					theSourcePartitionId != null ? theSourcePartitionId.getFirstPartitionIdOrNull() : null;
			Integer resourcePartId = resourcePartition != null ? resourcePartition.getFirstPartitionIdOrNull() : null;
			boolean inSourcePartition = sourcePartId != null && sourcePartId.equals(resourcePartId);

			if (inSourceCompartment && inSourcePartition) {
				theMoveList.add(resource);
			} else {
				theUpdateList.add(resource);
			}
		}
	}

	/**
	 * Builds a transaction bundle containing POST (CREATE) entries for all compartment resources
	 * to be moved to the target partition. References to the source patient and to other moved
	 * resources are updated before building the bundle.
	 * <p>
	 * {@code theOldIdToPlaceholder} is populated as a {@link LinkedHashMap} whose key insertion
	 * order matches the order of entries added to the returned bundle, so that
	 * {@link #extractOldToNewIdMap} can correlate response entries by index.
	 */
	private IBaseBundle buildCreateBundle(
			List<IBaseResource> theMoveList,
			String theSourcePatientId,
			String theTargetPatientId,
			LinkedHashMap<String, String> theOldIdToPlaceholder) {

		FhirTerser terser = myFhirContext.newTerser();
		String sourcePatientRef = "Patient/" + theSourcePatientId;
		String targetPatientRef = "Patient/" + theTargetPatientId;

		// Build placeholder map; insertion order is preserved so keys
		// can be correlated by index with CREATE response entries in extractOldToNewIdMap.
		for (IBaseResource resource : theMoveList) {
			String oldId = resource.getIdElement().toUnqualifiedVersionless().getValue();
			theOldIdToPlaceholder.put(oldId, IdDt.newRandomUuid().getValue());
		}

		// Replace Patient/A→Patient/B and inter-move refs with urn:uuid placeholders
		for (IBaseResource resource : theMoveList) {
			for (ResourceReferenceInfo refInfo : terser.getAllResourceReferences(resource)) {
				String refValue = refInfo.getResourceReference()
						.getReferenceElement()
						.toUnqualifiedVersionless()
						.getValue();
				// TODO Emre: is this the right way to compare?
				if (sourcePatientRef.equals(refValue)) {
					refInfo.getResourceReference().setReference(targetPatientRef);
				} else if (theOldIdToPlaceholder.containsKey(refValue)) {
					refInfo.getResourceReference().setReference(theOldIdToPlaceholder.get(refValue));
				}
			}
		}

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
	 * whose references to the source patient (or moved resources) need updating.
	 */
	private IBaseBundle buildUpdateBundle(
			List<IBaseResource> theUpdateList,
			String theSourcePatientId,
			String theTargetPatientId,
			Map<String, String> theOldToNewIdMap) {

		FhirTerser terser = myFhirContext.newTerser();
		String sourcePatientRef = "Patient/" + theSourcePatientId;
		String targetPatientRef = "Patient/" + theTargetPatientId;

		for (IBaseResource resource : theUpdateList) {
			for (ResourceReferenceInfo refInfo : terser.getAllResourceReferences(resource)) {
				String refValue = refInfo.getResourceReference()
						.getReferenceElement()
						.toUnqualifiedVersionless()
						.getValue();
				if (sourcePatientRef.equals(refValue)) {
					refInfo.getResourceReference().setReference(targetPatientRef);
				} else if (theOldToNewIdMap.containsKey(refValue)) {
					refInfo.getResourceReference().setReference(theOldToNewIdMap.get(refValue));
				}
			}
		}

		BundleBuilder bundleBuilder = new BundleBuilder(myFhirContext);
		for (IBaseResource resource : theUpdateList) {
			bundleBuilder.addTransactionUpdateEntry(resource);
		}
		return bundleBuilder.getBundle();
	}
}
