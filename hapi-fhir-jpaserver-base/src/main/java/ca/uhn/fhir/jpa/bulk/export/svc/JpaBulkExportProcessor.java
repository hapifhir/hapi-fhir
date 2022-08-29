package ca.uhn.fhir.jpa.bulk.export.svc;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.fhirpath.IFhirPath;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.PersistentIdToForcedIdMap;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.bulk.export.api.IBulkExportProcessor;
import ca.uhn.fhir.jpa.bulk.export.model.ExportPIDIteratorParameters;
import ca.uhn.fhir.jpa.dao.IResultIterator;
import ca.uhn.fhir.jpa.dao.ISearchBuilder;
import ca.uhn.fhir.jpa.dao.SearchBuilderFactory;
import ca.uhn.fhir.jpa.dao.mdm.MdmExpansionCacheSvc;
import ca.uhn.fhir.jpa.model.search.SearchRuntimeDetails;
import ca.uhn.fhir.jpa.partition.SystemRequestDetails;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.util.QueryChunker;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.dao.IMdmLinkDao;
import ca.uhn.fhir.mdm.model.MdmPidTuple;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.server.bulk.BulkDataExportOptions;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.param.HasOrListParam;
import ca.uhn.fhir.rest.param.HasParam;
import ca.uhn.fhir.rest.param.ReferenceOrListParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.util.ExtensionUtil;
import ca.uhn.fhir.util.HapiExtensions;
import ca.uhn.fhir.util.SearchParameterUtil;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBaseExtension;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class JpaBulkExportProcessor implements IBulkExportProcessor {
	private static final Logger ourLog = LoggerFactory.getLogger(JpaBulkExportProcessor.class);

	public static final int QUERY_CHUNK_SIZE = 100;
	public static final List<String> PATIENT_BULK_EXPORT_FORWARD_REFERENCE_RESOURCE_TYPES = List.of("Practitioner", "Organization");

	@Autowired
	private FhirContext myContext;

	@Autowired
	private BulkExportHelperService myBulkExportHelperSvc;

	@Autowired
	private DaoConfig myDaoConfig;

	@Autowired
	private DaoRegistry myDaoRegistry;

	@Autowired
	protected SearchBuilderFactory mySearchBuilderFactory;

	@Autowired
	private IIdHelperService myIdHelperService;

	@Autowired
	private IMdmLinkDao myMdmLinkDao;

	@Autowired
	private MdmExpansionCacheSvc myMdmExpansionCacheSvc;

	private final HashMap<String, ISearchBuilder> myResourceTypeToSearchBuilder = new HashMap<>();

	private final HashMap<String, String> myResourceTypeToFhirPath = new HashMap<>();

	private IFhirPath myFhirPath;

	@Transactional
	@Override
	public Iterator<ResourcePersistentId> getResourcePidIterator(ExportPIDIteratorParameters theParams) {
		String resourceType = theParams.getResourceType();
		String jobId = theParams.getJobId();
		RuntimeResourceDefinition def = myContext.getResourceDefinition(resourceType);

		Set<ResourcePersistentId> pids = new HashSet<>();

		if (theParams.getExportStyle() == BulkDataExportOptions.ExportStyle.PATIENT) {
			// Patient
			if (myDaoConfig.getIndexMissingFields() == DaoConfig.IndexEnabledEnum.DISABLED) {
				String errorMessage = "You attempted to start a Patient Bulk Export, but the system has `Index Missing Fields` disabled. It must be enabled for Patient Bulk Export";
				ourLog.error(errorMessage);
				throw new IllegalStateException(Msg.code(797) + errorMessage);
			}

			List<SearchParameterMap> maps = myBulkExportHelperSvc.createSearchParameterMapsForResourceType(def, theParams);
			String patientSearchParam = getPatientSearchParamForCurrentResourceType(theParams.getResourceType()).get().getName();

			for (SearchParameterMap map : maps) {
				//Ensure users did not monkey with the patient compartment search parameter.
				validateSearchParametersForPatient(map, theParams);

				ISearchBuilder searchBuilder = getSearchBuilderForLocalResourceType(theParams);

				if (!resourceType.equalsIgnoreCase("Patient")) {
					map.add(patientSearchParam, new ReferenceParam().setMissing(false));
				}

				IResultIterator resultIterator = searchBuilder.createQuery(map,
					new SearchRuntimeDetails(null, jobId),
					null,
					RequestPartitionId.allPartitions());
				while (resultIterator.hasNext()) {
					pids.add(resultIterator.next());
				}
			}
		}
		else if (theParams.getExportStyle() == BulkDataExportOptions.ExportStyle.GROUP) {
//			GET /Group/123/$export?_type=Patient&_typeFilter=Patient?gender=male

			Set<String> expandedPatientResourceIds = expandAllPatientPidsFromGroup(theParams);
			if (ourLog.isDebugEnabled()) {
				ourLog.debug("Group/{} has been expanded to members:[{}]", theParams, String.join(",", expandedPatientResourceIds));
			}

			//Next, let's search for the target resources, with their correct patient references, chunked.
			//The results will be jammed into myReadPids
			QueryChunker<String> queryChunker = new QueryChunker<>();
			queryChunker.chunk(new ArrayList<>(expandedPatientResourceIds), QUERY_CHUNK_SIZE, (idChunk) -> {
				queryResourceTypeWithReferencesToPatients(pids, idChunk, theParams, def);
			});
		}
		else {
//			GET /Group/$export?_type=Patient&_typeFilter=Patient?gender=male
			// System
			List<SearchParameterMap> maps = myBulkExportHelperSvc.createSearchParameterMapsForResourceType(def, theParams);
			ISearchBuilder searchBuilder = getSearchBuilderForLocalResourceType(theParams);

			for (SearchParameterMap map : maps) {
				// requires a transaction
				IResultIterator resultIterator = searchBuilder.createQuery(map,
					new SearchRuntimeDetails(null, jobId),
					null,
					RequestPartitionId.allPartitions());
				while (resultIterator.hasNext()) {
					pids.add(resultIterator.next());
				}
			}
		}

		return pids.iterator();
	}

	/**
	 * Get and cache an ISearchBuilder for the given resource type this partition is responsible for.
	 */
	protected ISearchBuilder getSearchBuilderForLocalResourceType(ExportPIDIteratorParameters theParams) {
		String resourceType = theParams.getResourceType();

		if (!myResourceTypeToSearchBuilder.containsKey(resourceType)) {
			IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(resourceType);
			RuntimeResourceDefinition def = myContext.getResourceDefinition(resourceType);
			Class<? extends IBaseResource> nextTypeClass = def.getImplementingClass();
			ISearchBuilder sb = mySearchBuilderFactory.newSearchBuilder(dao, resourceType, nextTypeClass);
			myResourceTypeToSearchBuilder.put(resourceType, sb);
		}

		return myResourceTypeToSearchBuilder.get(resourceType);
	}

	protected Optional<RuntimeSearchParam> getPatientSearchParamForCurrentResourceType(String theResourceType) {
		return SearchParameterUtil.getOnlyPatientSearchParamForResourceType(myContext, theResourceType);
	}

	protected Optional<RuntimeSearchParam> getIdSearchParamForPatientResourceType() {
		return SearchParameterUtil.getIdSearchParamForResourceType(myContext, "Patient");
	}


	@Override
	public void expandMdmResources(List<IBaseResource> theResources) {
		for (IBaseResource resource : theResources) {
			if (!PATIENT_BULK_EXPORT_FORWARD_REFERENCE_RESOURCE_TYPES.contains(resource.fhirType())) {
				annotateBackwardsReferences(resource);
			}
		}

		// is this necessary?
		myResourceTypeToFhirPath.clear();
	}

	/**
	 * For Patient
	 **/

	private RuntimeSearchParam validateSearchParametersForPatient(SearchParameterMap expandedSpMap, ExportPIDIteratorParameters theParams) {
		RuntimeSearchParam runtimeSearchParam = getPatientSearchParamForCurrentResourceType(theParams.getResourceType()).get();
		if (expandedSpMap.get(runtimeSearchParam.getName()) != null) {
			throw new IllegalArgumentException(Msg.code(796) + String.format("Patient Bulk Export manually modifies the Search Parameter called [%s], so you may not include this search parameter in your _typeFilter!", runtimeSearchParam.getName()));
		}
		return runtimeSearchParam;
	}

	/**
	 * for group exports
	 **/

	private void validateSearchParametersForGroup(SearchParameterMap expandedSpMap, String theResourceType) {
		// we only validate for certain types
		if (!PATIENT_BULK_EXPORT_FORWARD_REFERENCE_RESOURCE_TYPES.contains(theResourceType)) {
			RuntimeSearchParam runtimeSearchParam = getPatientSearchParamForCurrentResourceType(theResourceType).get();
			if (expandedSpMap.get(runtimeSearchParam.getName()) != null) {
				throw new IllegalArgumentException(Msg.code(792) + String.format("Group Bulk Export manually modifies the Search Parameter called [%s], so you may not include this search parameter in your _typeFilter!", runtimeSearchParam.getName()));
			}
		}
	}

	/**
	 * In case we are doing a Group Bulk Export and resourceType `Patient` is requested, we can just return the group members,
	 * possibly expanded by MDM, and don't have to go and fetch other resource DAOs.
	 */
	private Iterator<ResourcePersistentId> getExpandedPatientIterator(ExportPIDIteratorParameters theParameters, RuntimeResourceDefinition theDef) {
		String groupId = theParameters.getGroupId();

		// create searchParameters for _typeFilters
		List<SearchParameterMap> expandedSpMaps = myBulkExportHelperSvc.createSearchParameterMapsForResourceType(theDef, theParameters);



		List<String> members = getPatientIdsForMembersOfGroup(theParameters.getGroupId());
		List<IIdType> ids = members.stream().map(member -> new IdDt("Patient/" + member)).collect(Collectors.toList());

		// Are bulk exports partition aware or care about partition at all? This does
		List<ResourcePersistentId> pidsOrThrowException = myIdHelperService.getPidsOrThrowException(RequestPartitionId.allPartitions(), ids);
		Set<ResourcePersistentId> patientPidsToExport = new HashSet<>(pidsOrThrowException);

		if (theParameters.isExpandMdm()) {
			SystemRequestDetails srd = SystemRequestDetails.newSystemRequestAllPartitions();
			IBaseResource group = myDaoRegistry.getResourceDao("Group").read(new IdDt(theParameters.getGroupId()), srd);
			ResourcePersistentId pidOrNull = myIdHelperService.getPidOrNull(RequestPartitionId.allPartitions(), group);
			List<MdmPidTuple> goldenPidSourcePidTuple = myMdmLinkDao.expandPidsFromGroupPidGivenMatchResult(pidOrNull, MdmMatchResultEnum.MATCH);
			goldenPidSourcePidTuple.forEach(tuple -> {
				patientPidsToExport.add(tuple.getGoldenPid());
				patientPidsToExport.add(tuple.getSourcePid());
			});
			populateMdmResourceCache(goldenPidSourcePidTuple);
		}

		return patientPidsToExport.iterator();
	}

	/**
	 * Given the local myGroupId, read this group, and find all members' patient references.
	 *
	 * @return A list of strings representing the Patient IDs of the members (e.g. ["P1", "P2", "P3"]
	 */
	private List<String> getPatientIdsForMembersOfGroup(String theGroupId) {
		SystemRequestDetails requestDetails = SystemRequestDetails.newSystemRequestAllPartitions();
		IBaseResource group = myDaoRegistry.getResourceDao("Group").read(new IdDt(theGroupId), requestDetails);

		List<IPrimitiveType> evaluate = myContext.newFhirPath().evaluate(group, "member.entity.reference", IPrimitiveType.class);

		return evaluate.stream().map(IPrimitiveType::getValueAsString).collect(Collectors.toList());
	}

	/**
	 * @param thePidTuples
	 */
	private void populateMdmResourceCache(List<MdmPidTuple> thePidTuples) {
		if (myMdmExpansionCacheSvc.hasBeenPopulated()) {
			return;
		}
		//First, convert this zipped set of tuples to a map of
		//{
		//   patient/gold-1 -> [patient/1, patient/2]
		//   patient/gold-2 -> [patient/3, patient/4]
		//}
		Map<ResourcePersistentId, Set<ResourcePersistentId>> goldenResourceToSourcePidMap = new HashMap<>();
		extract(thePidTuples, goldenResourceToSourcePidMap);

		//Next, lets convert it to an inverted index for fast lookup
		// {
		//   patient/1 -> patient/gold-1
		//   patient/2 -> patient/gold-1
		//   patient/3 -> patient/gold-2
		//   patient/4 -> patient/gold-2
		// }
		Map<String, String> sourceResourceIdToGoldenResourceIdMap = new HashMap<>();
		goldenResourceToSourcePidMap.forEach((key, value) -> {
			String goldenResourceId = myIdHelperService.translatePidIdToForcedIdWithCache(new ResourcePersistentId(key)).orElse(key.toString());
			PersistentIdToForcedIdMap pidsToForcedIds = myIdHelperService.translatePidsToForcedIds(value);

			Set<String> sourceResourceIds = pidsToForcedIds.getResolvedResourceIds();

			sourceResourceIds
				.forEach(sourceResourceId -> sourceResourceIdToGoldenResourceIdMap.put(sourceResourceId, goldenResourceId));
		});

		//Now that we have built our cached expansion, store it.
		myMdmExpansionCacheSvc.setCacheContents(sourceResourceIdToGoldenResourceIdMap);
	}

	private void extract(List<MdmPidTuple> theGoldenPidTargetPidTuples, Map<ResourcePersistentId, Set<ResourcePersistentId>> theGoldenResourceToSourcePidMap) {
		for (MdmPidTuple goldenPidTargetPidTuple : theGoldenPidTargetPidTuples) {
			ResourcePersistentId goldenPid = goldenPidTargetPidTuple.getGoldenPid();
			ResourcePersistentId sourcePid = goldenPidTargetPidTuple.getSourcePid();
			theGoldenResourceToSourcePidMap.computeIfAbsent(goldenPid, key -> new HashSet<>()).add(sourcePid);
		}
	}

	private void queryResourceTypeWithReferencesToPatients(Set<ResourcePersistentId> myReadPids,
																			 List<String> idChunk,
																			 ExportPIDIteratorParameters theParams,
																			 RuntimeResourceDefinition theDef) {
		//Build SP map
		//First, inject the _typeFilters and _since from the export job
		List<SearchParameterMap> expandedSpMaps = myBulkExportHelperSvc.createSearchParameterMapsForResourceType(theDef, theParams);

		for (SearchParameterMap expandedSpMap : expandedSpMaps) {

			//Since we are in a bulk job, we have to ensure the user didn't jam in a patient search param, since we need to manually set that.
			// FIXME: 2022-08-25 is this needed still?
			validateSearchParametersForGroup(expandedSpMap, theParams.getResourceType());

			// Fetch and cache a search builder for this resource type
			ISearchBuilder searchBuilder = getSearchBuilderForLocalResourceType(theParams);

			// Now, further filter the query with patient references defined by the chunk of IDs we have.
			if (PATIENT_BULK_EXPORT_FORWARD_REFERENCE_RESOURCE_TYPES.contains(theParams.getResourceType())) {
				filterSearchByHasParam(idChunk, expandedSpMap, theParams);
			} else {
				filterSearchByResourceIds(idChunk, expandedSpMap, theParams);
			}

			//Execute query and all found pids to our local iterator.
			IResultIterator resultIterator = searchBuilder.createQuery(expandedSpMap,
				new SearchRuntimeDetails(null, theParams.getJobId()),
				null,
				RequestPartitionId.allPartitions());
			while (resultIterator.hasNext()) {
				myReadPids.add(resultIterator.next());
			}
		}
	}

	/**
	 * Must not be called for resources types listed in PATIENT_BULK_EXPORT_FORWARD_REFERENCE_RESOURCE_TYPES
	 *
	 * @param idChunk
	 * @param expandedSpMap
	 * @param theParams
	 */
	private void filterSearchByResourceIds(List<String> idChunk, SearchParameterMap expandedSpMap, ExportPIDIteratorParameters theParams) {
		ReferenceOrListParam orList = new ReferenceOrListParam();
		idChunk.forEach(id -> orList.add(new ReferenceParam(id)));

		Optional<RuntimeSearchParam> searchParamOptional;

		if ("Patient".equalsIgnoreCase(theParams.getResourceType())) {
			//		Building searchParam of kind Patient?_id=123,345
			searchParamOptional = getIdSearchParamForPatientResourceType();
		} else {

			//		Building searchParam of kind Observation?patient=123,345
			searchParamOptional = getPatientSearchParamForCurrentResourceType(theParams.getResourceType());
		}

		if (searchParamOptional.isPresent()){
			String searchParamString = searchParamOptional.get().getName();
			expandedSpMap.add(searchParamString, orList);
		}

	}



	/**
	 * @param idChunk
	 * @param expandedSpMap
	 */
	private void filterSearchByHasParam(List<String> idChunk, SearchParameterMap expandedSpMap, ExportPIDIteratorParameters theParams) {
		HasOrListParam hasOrListParam = new HasOrListParam();
		idChunk.stream().forEach(id -> hasOrListParam.addOr(buildHasParam(id, theParams.getResourceType())));
		expandedSpMap.add("_has", hasOrListParam);
	}

	private HasParam buildHasParam(String theId, String theResourceType) {
		if ("Practitioner".equalsIgnoreCase(theResourceType)) {
			return new HasParam("Patient", "general-practitioner", "_id", theId);
		} else if ("Organization".equalsIgnoreCase(theResourceType)) {
			return new HasParam("Patient", "organization", "_id", theId);
		} else {
			throw new IllegalArgumentException(Msg.code(2077) + " We can't handle forward references onto type " + theResourceType);
		}
	}

	/**
	 * Given the local myGroupId, perform an expansion to retrieve all resource IDs of member patients.
	 * if myMdmEnabled is set to true, we also reach out to the IMdmLinkDao to attempt to also expand it into matched
	 * patients.
	 *
	 * @return a Set of Strings representing the resource IDs of all members of a group.
	 */
	private Set<String> expandAllPatientPidsFromGroup(ExportPIDIteratorParameters theParams) {
		Set<String> expandedIds = new HashSet<>();
		SystemRequestDetails requestDetails = SystemRequestDetails.newSystemRequestAllPartitions();
		IBaseResource group = myDaoRegistry.getResourceDao("Group").read(new IdDt(theParams.getGroupId()), requestDetails);
		ResourcePersistentId pidOrNull = myIdHelperService.getPidOrNull(RequestPartitionId.allPartitions(), group);

		//Attempt to perform MDM Expansion of membership
		if (theParams.isExpandMdm()) {
			List<MdmPidTuple> goldenPidTargetPidTuples = myMdmLinkDao.expandPidsFromGroupPidGivenMatchResult(pidOrNull, MdmMatchResultEnum.MATCH);
			//Now lets translate these pids into resource IDs
			Set<ResourcePersistentId> uniquePids = new HashSet<>();
			goldenPidTargetPidTuples.forEach(tuple -> {
				uniquePids.add(tuple.getGoldenPid());
				uniquePids.add(tuple.getSourcePid());
			});
			PersistentIdToForcedIdMap pidToForcedIdMap = myIdHelperService.translatePidsToForcedIds(uniquePids);

			Map<ResourcePersistentId, Set<ResourcePersistentId>> goldenResourceToSourcePidMap = new HashMap<>();
			extract(goldenPidTargetPidTuples, goldenResourceToSourcePidMap);
			populateMdmResourceCache(goldenPidTargetPidTuples);

			//If the result of the translation is an empty optional, it means there is no forced id, and we can use the PID as the resource ID.
			Set<String> resolvedResourceIds = pidToForcedIdMap.getResolvedResourceIds();
			expandedIds.addAll(resolvedResourceIds);
		}

		//Now manually add the members of the group (its possible even with mdm expansion that some members dont have MDM matches,
		//so would be otherwise skipped
		expandedIds.addAll(getPatientIdsForMembersOfGroup(theParams.getGroupId()));

		return expandedIds;
	}

	/* Mdm Expansion */

	private RuntimeSearchParam getRuntimeSearchParam(IBaseResource theResource) {
		Optional<RuntimeSearchParam> oPatientSearchParam = SearchParameterUtil.getOnlyPatientSearchParamForResourceType(myContext, theResource.fhirType());
		if (!oPatientSearchParam.isPresent()) {
			String errorMessage = String.format("[%s] has  no search parameters that are for patients, so it is invalid for Group Bulk Export!", theResource.fhirType());
			throw new IllegalArgumentException(Msg.code(2103) + errorMessage);
		} else {
			return oPatientSearchParam.get();
		}
	}

	private void annotateBackwardsReferences(IBaseResource iBaseResource) {
		Optional<String> patientReference = getPatientReference(iBaseResource);
		if (patientReference.isPresent()) {
			addGoldenResourceExtension(iBaseResource, patientReference.get());
		} else {
			ourLog.error("Failed to find the patient reference information for resource {}. This is a bug, " +
				"as all resources which can be exported via Group Bulk Export must reference a patient.", iBaseResource);
		}
	}

	private Optional<String> getPatientReference(IBaseResource iBaseResource) {
		String fhirPath;

		String resourceType = iBaseResource.fhirType();
		if (myResourceTypeToFhirPath.containsKey(resourceType)) {
			fhirPath = myResourceTypeToFhirPath.get(resourceType);
		} else {
			RuntimeSearchParam runtimeSearchParam = getRuntimeSearchParam(iBaseResource);
			fhirPath = getPatientFhirPath(runtimeSearchParam);
			myResourceTypeToFhirPath.put(resourceType, fhirPath);
		}

		if (iBaseResource.fhirType().equalsIgnoreCase("Patient")) {
			return Optional.of(iBaseResource.getIdElement().getIdPart());
		} else {
			Optional<IBaseReference> optionalReference = getFhirParser().evaluateFirst(iBaseResource, fhirPath, IBaseReference.class);
			if (optionalReference.isPresent()) {
				return optionalReference.map(theIBaseReference -> theIBaseReference.getReferenceElement().getIdPart());
			} else {
				return Optional.empty();
			}
		}
	}

	private void addGoldenResourceExtension(IBaseResource iBaseResource, String sourceResourceId) {
		String goldenResourceId = myMdmExpansionCacheSvc.getGoldenResourceId(sourceResourceId);
		IBaseExtension<?, ?> extension = ExtensionUtil.getOrCreateExtension(iBaseResource, HapiExtensions.ASSOCIATED_GOLDEN_RESOURCE_EXTENSION_URL);
		if (!StringUtils.isBlank(goldenResourceId)) {
			ExtensionUtil.setExtension(myContext, extension, "reference", prefixPatient(goldenResourceId));
		}
	}

	private String prefixPatient(String theResourceId) {
		return "Patient/" + theResourceId;
	}

	private IFhirPath getFhirParser() {
		if (myFhirPath == null) {
			myFhirPath = myContext.newFhirPath();
		}
		return myFhirPath;
	}

	private String getPatientFhirPath(RuntimeSearchParam theRuntimeParam) {
		String path = theRuntimeParam.getPath();
		// GGG: Yes this is a stupid hack, but by default this runtime search param will return stuff like
		// Observation.subject.where(resolve() is Patient) which unfortunately our FHIRpath evaluator doesn't play nicely with
		// our FHIRPath evaluator.
		if (path.contains(".where")) {
			path = path.substring(0, path.indexOf(".where"));
		}
		return path;
	}
}
