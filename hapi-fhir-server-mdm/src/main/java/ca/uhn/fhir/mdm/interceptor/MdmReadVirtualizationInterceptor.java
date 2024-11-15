/*-
 * #%L
 * HAPI FHIR - Master Data Management
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.mdm.interceptor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.PersistentIdToForcedIdMap;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.mdm.dao.IMdmLinkDao;
import ca.uhn.fhir.mdm.model.MdmPidTuple;
import ca.uhn.fhir.mdm.svc.MdmSearchExpansionSvc;
import ca.uhn.fhir.rest.api.server.IPreResourceShowDetails;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import ca.uhn.fhir.util.FhirTerser;
import ca.uhn.fhir.util.ResourceReferenceInfo;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.MultimapBuilder;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * <b>This class is experimental and subject to change. Use with caution.</b>
 * <p>
 * This interceptor provided an "MDM Virtualized" endpoint, meaning that
 * searches are expanded to include MDM-linked resources (including any
 * linked golden resource, and also including any other resources linked
 * to that golden resource). Searches for non-MDM resources which have
 * a reference to an MDM resource will have their reference parameter
 * expanded to include the golden and linked resources.
 * </p>
 * <p>
 * In addition, responses are cleaned up so that only the golden resource
 * is included in responses, and references to non-golden resources
 * are rewritten.
 * </p>
 * <p>
 * This interceptor does not modify data that is being stored/written
 * in any way, it only modifies data that is being returned by the
 * server.
 * </p>
 *
 * @since 8.0.0
 */
public class MdmReadVirtualizationInterceptor<P extends IResourcePersistentId<?>> {

	public static final String CURRENTLY_PROCESSING_FLAG =
			MdmReadVirtualizationInterceptor.class.getName() + "-CURRENTLY-PROCESSING";

	@Autowired
	private FhirContext myFhirContext;

	@Autowired
	private IMdmLinkDao<P, ?> myMdmLinkDao;

	@Autowired
	private IIdHelperService<P> myIdHelperService;

	@Autowired
	private DaoRegistry myDaoRegistry;

	@Autowired
	private MdmSearchExpansionSvc myMdmSearchExpansionSvc;

	@Autowired
	private HapiTransactionService myTxService;

	@Hook(Pointcut.STORAGE_PRESEARCH_REGISTERED)
	public void hook(RequestDetails theRequestDetails, SearchParameterMap theSearchParameterMap) {
		myMdmSearchExpansionSvc.expandSearch(theRequestDetails, theSearchParameterMap, t -> true);
	}

	@Hook(Pointcut.STORAGE_PRESHOW_RESOURCES)
	public void preShowResources(RequestDetails theRequestDetails, IPreResourceShowDetails theDetails) {
		if (theRequestDetails.getUserData().get(CURRENTLY_PROCESSING_FLAG) == Boolean.TRUE) {
			return;
		}

		// Gather all the resource IDs we might need to remap
		ListMultimap<String, Integer> candidateResourceIds = extractRemapCandidateResources(theDetails);
		ListMultimap<String, ResourceReferenceInfo> candidateReferences = extractRemapCandidateReferences(theDetails);

		CandidateMdmLinkedResources<P> candidates =
				findCandidateMdmLinkedResources(candidateResourceIds, candidateReferences);

		// Loop through each link and figure out whether we need to remap anything
		for (MdmPidTuple<P> tuple : candidates.getTuples()) {
			Optional<String> sourceIdOpt = candidates.getFhirIdForPersistentId(tuple.getSourcePid());
			if (sourceIdOpt.isPresent()) {
				String sourceId = sourceIdOpt.get();

				// Remap references from source to golden
				List<ResourceReferenceInfo> referencesToRemap = candidateReferences.get(sourceId);
				if (!referencesToRemap.isEmpty()) {
					P associatedGoldenResourcePid = tuple.getGoldenPid();
					Optional<String> associatedGoldenResourceId =
							candidates.getFhirIdForPersistentId(associatedGoldenResourcePid);
					if (associatedGoldenResourceId.isPresent()) {
						for (ResourceReferenceInfo referenceInfoToRemap : referencesToRemap) {
							IBaseReference referenceToRemap = referenceInfoToRemap.getResourceReference();
							referenceToRemap.setReference(associatedGoldenResourceId.get());
						}
					}
				}

				// Filter out source resources
				Optional<String> targetIdOpt = candidates.getFhirIdForPersistentId(tuple.getGoldenPid());
				if (targetIdOpt.isPresent()) {
					Integer filteredIndex = null;
					for (int sourceIdResourceIndex : candidateResourceIds.get(sourceId)) {
						theDetails.setResource(sourceIdResourceIndex, null);
						if (filteredIndex == null) {
							filteredIndex = sourceIdResourceIndex;
						}
					}

					if (filteredIndex != null) {
						String targetId = targetIdOpt.get();
						if (candidateResourceIds.get(targetId).isEmpty()) {
							// If we filtered a resource out because it's not a golden record,
							// and the golden record itself isn't already a part of the results,
							// then we'll manually add it
							IIdType targetResourceId = newIdType(targetId);
							IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(targetResourceId.getResourceType());

							theRequestDetails.getUserData().put(CURRENTLY_PROCESSING_FLAG, Boolean.TRUE);
							IBaseResource goldenResource;
							try {
								goldenResource = dao.read(targetResourceId, theRequestDetails);
							} finally {
								theRequestDetails.getUserData().remove(CURRENTLY_PROCESSING_FLAG);
							}
							theDetails.setResource(filteredIndex, goldenResource);
							candidateResourceIds.put(targetId, filteredIndex);
						}
					}
				}
			}
		}
	}

	@Nonnull
	private CandidateMdmLinkedResources<P> findCandidateMdmLinkedResources(
			ListMultimap<String, Integer> candidateResourceIds,
			ListMultimap<String, ResourceReferenceInfo> candidateReferences) {
		return myTxService.withSystemRequest().read(() -> {
			// Resolve all the resource IDs we've seen that could be MDM candidates,
			// and look for MDM links that have these IDs as either the source or the
			// golden resource side of the link
			Set<IIdType> allIds = new HashSet<>();
			candidateResourceIds.keySet().forEach(t -> allIds.add(newIdType(t)));
			candidateReferences.keySet().forEach(t -> allIds.add(newIdType(t)));
			List<P> sourcePids =
					myIdHelperService.getPidsOrThrowException(RequestPartitionId.allPartitions(), List.copyOf(allIds));
			Collection<MdmPidTuple<P>> tuples = myMdmLinkDao.resolveGoldenResources(sourcePids);

			// Resolve the link PIDs into FHIR IDs
			Set<P> allPersistentIds = new HashSet<>();
			tuples.forEach(t -> allPersistentIds.add(t.getGoldenPid()));
			tuples.forEach(t -> allPersistentIds.add(t.getSourcePid()));
			PersistentIdToForcedIdMap<P> persistentIdToFhirIdMap =
					myIdHelperService.translatePidsToForcedIds(allPersistentIds);
			return new CandidateMdmLinkedResources<>(tuples, persistentIdToFhirIdMap);
		});
	}

	/**
	 * @return Returns a map where the keys are a typed ID (Patient/ABC) and the values are the index of
	 * that resource within the {@link IPreResourceShowDetails}
	 */
	private ListMultimap<String, Integer> extractRemapCandidateResources(IPreResourceShowDetails theDetails) {
		ListMultimap<String, Integer> retVal =
				MultimapBuilder.hashKeys().arrayListValues().build();
		for (int resourceIdx = 0; resourceIdx < theDetails.size(); resourceIdx++) {
			IBaseResource resource = theDetails.getResource(resourceIdx);

			// Extract the IDs of the actual resources being returned in case
			// we want to replace them with golden equivalents
			if (isRemapCandidate(resource.getIdElement().getResourceType())) {
				IIdType id = resource.getIdElement().toUnqualifiedVersionless();
				retVal.put(id.getValue(), resourceIdx);
			}
		}

		return retVal;
	}

	/**
	 * @return Returns a map where the keys are a typed ID (Patient/ABC) and the values are references
	 * found in any of the resources that are referring to that ID.
	 */
	private ListMultimap<String, ResourceReferenceInfo> extractRemapCandidateReferences(
			IPreResourceShowDetails theDetails) {
		ListMultimap<String, ResourceReferenceInfo> retVal =
				MultimapBuilder.hashKeys().arrayListValues().build();
		FhirTerser terser = myFhirContext.newTerser();

		for (int resourceIdx = 0; resourceIdx < theDetails.size(); resourceIdx++) {
			IBaseResource resource = theDetails.getResource(resourceIdx);

			// Extract all the references in the resources we're returning
			// in case we need to remap them to golden equivalents
			List<ResourceReferenceInfo> referenceInfos = terser.getAllResourceReferences(resource);
			for (ResourceReferenceInfo referenceInfo : referenceInfos) {
				IIdType referenceId = referenceInfo.getResourceReference().getReferenceElement();

				if (isRemapCandidate(referenceId.getResourceType())) {
					IIdType id = referenceId.toUnqualifiedVersionless();
					retVal.put(id.getValue(), referenceInfo);
				}
			}
		}

		return retVal;
	}

	private IIdType newIdType(String targetId) {
		return myFhirContext.getVersion().newIdType().setValue(targetId);
	}

	/**
	 * Is the given resource a candidate for virtualization?
	 */
	private boolean isRemapCandidate(String theResourceType) {
		return "Patient".equals(theResourceType);
	}

	private static class CandidateMdmLinkedResources<P extends IResourcePersistentId<?>> {
		private final Collection<MdmPidTuple<P>> myTuples;
		private final PersistentIdToForcedIdMap<P> myPersistentIdToFhirIdMap;

		public CandidateMdmLinkedResources(
				Collection<MdmPidTuple<P>> thePidTuples, PersistentIdToForcedIdMap<P> thePersistentIdToForcedIdMap) {
			this.myTuples = thePidTuples;
			this.myPersistentIdToFhirIdMap = thePersistentIdToForcedIdMap;
		}

		public Collection<MdmPidTuple<P>> getTuples() {
			return myTuples;
		}

		public Optional<String> getFhirIdForPersistentId(P theSourcePid) {
			return myPersistentIdToFhirIdMap.get(theSourcePid);
		}
	}
}
