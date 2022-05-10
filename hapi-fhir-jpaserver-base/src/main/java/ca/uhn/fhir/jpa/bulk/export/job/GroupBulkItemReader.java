package ca.uhn.fhir.jpa.bulk.export.job;

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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.batch.config.BatchConstants;
import ca.uhn.fhir.jpa.batch.log.Logs;
import ca.uhn.fhir.jpa.dao.IResultIterator;
import ca.uhn.fhir.jpa.dao.ISearchBuilder;
import ca.uhn.fhir.jpa.dao.data.IMdmLinkDao;
import ca.uhn.fhir.jpa.dao.index.IJpaIdHelperService;
import ca.uhn.fhir.jpa.dao.mdm.MdmExpansionCacheSvc;
import ca.uhn.fhir.jpa.model.search.SearchRuntimeDetails;
import ca.uhn.fhir.jpa.partition.SystemRequestDetails;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.util.QueryChunker;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.param.HasOrListParam;
import ca.uhn.fhir.rest.param.HasParam;
import ca.uhn.fhir.rest.param.ReferenceOrListParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.slf4j.Logger;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static ca.uhn.fhir.jpa.batch.config.BatchConstants.PATIENT_BULK_EXPORT_FORWARD_REFERENCE_RESOURCE_TYPES;

/**
 * Bulk Item reader for the Group Bulk Export job.
 * Instead of performing a normal query on the resource type using type filters, we instead
 *
 * 1. Get the group ID defined for this job
 * 2. Expand its membership so we get references to all patients in the group
 * 3. Optionally further expand that into all MDM-matched Patients (including golden resources)
 * 4. Then perform normal bulk export, filtered so that only results that refer to members are returned.
 */
public class GroupBulkItemReader extends BaseJpaBulkItemReader implements ItemReader<List<ResourcePersistentId>> {
	private static final Logger ourLog = Logs.getBatchTroubleshootingLog();
	public static final int QUERY_CHUNK_SIZE = 100;

	@Value("#{jobParameters['" + BatchConstants.GROUP_ID_PARAMETER + "']}")
	private String myGroupId;
	@Value("#{jobParameters['" + BatchConstants.EXPAND_MDM_PARAMETER+ "'] ?: false}")
	private boolean myMdmEnabled;

	@Autowired
	private IIdHelperService myIdHelperService;
	@Autowired
	private IMdmLinkDao myMdmLinkDao;
	@Autowired
	private MdmExpansionCacheSvc myMdmExpansionCacheSvc;
	@Autowired
	private IJpaIdHelperService myJpaIdHelperService;

	@Override
	protected Iterator<ResourcePersistentId> getResourcePidIterator() {

		//Short circuit out if we detect we are attempting to extract patients
		if (myResourceType.equalsIgnoreCase("Patient")) {
			return getExpandedPatientIterator();
		}



		//First lets expand the group so we get a list of all patient IDs of the group, and MDM-matched patient IDs of the group.
		Set<String> expandedMemberResourceIds = expandAllPatientPidsFromGroup();
		if (ourLog.isDebugEnabled()) {
			ourLog.debug("Group/{} has been expanded to members:[{}]", myGroupId, String.join(",", expandedMemberResourceIds));
		}

		//Next, let's search for the target resources, with their correct patient references, chunked.
		//The results will be jammed into myReadPids
		Set<ResourcePersistentId> myExpandedMemberPids = new HashSet<>();
		QueryChunker<String> queryChunker = new QueryChunker<>();
		queryChunker.chunk(new ArrayList<>(expandedMemberResourceIds), QUERY_CHUNK_SIZE, (idChunk) -> {
			queryResourceTypeWithReferencesToPatients(myExpandedMemberPids, idChunk);
		});

		if (ourLog.isDebugEnabled()) {
			ourLog.debug("Resource PIDs to be Bulk Exported: {}", myExpandedMemberPids);
		}
		return myExpandedMemberPids.iterator();
	}

	/**
	 * In case we are doing a Group Bulk Export and resourceType `Patient` is requested, we can just return the group members,
	 * possibly expanded by MDM, and don't have to go and fetch other resource DAOs.
	 */
	private Iterator<ResourcePersistentId> getExpandedPatientIterator() {
		List<String> members = getMembers();
		List<IIdType> ids = members.stream().map(member -> new IdDt("Patient/" + member)).collect(Collectors.toList());
		List<Long> pidsOrThrowException =myJpaIdHelperService.getPidsOrThrowException(ids);
		Set<Long> patientPidsToExport = new HashSet<>(pidsOrThrowException);

		if (myMdmEnabled) {
			SystemRequestDetails srd = SystemRequestDetails.newSystemRequestAllPartitions();
			IBaseResource group = myDaoRegistry.getResourceDao("Group").read(new IdDt(myGroupId), srd);
			Long pidOrNull = myJpaIdHelperService.getPidOrNull(group);
			List<IMdmLinkDao.MdmPidTuple> goldenPidSourcePidTuple = myMdmLinkDao.expandPidsFromGroupPidGivenMatchResult(pidOrNull, MdmMatchResultEnum.MATCH);
			goldenPidSourcePidTuple.forEach(tuple -> {
				patientPidsToExport.add(tuple.getGoldenPid());
				patientPidsToExport.add(tuple.getSourcePid());
			});
			populateMdmResourceCache(goldenPidSourcePidTuple);
		}
		List<ResourcePersistentId> resourcePersistentIds = patientPidsToExport
			.stream()
			.map(ResourcePersistentId::new)
			.collect(Collectors.toList());
		return resourcePersistentIds.iterator();
	}

	/**
	 * @param thePidTuples
	 */
	private void populateMdmResourceCache(List<IMdmLinkDao.MdmPidTuple> thePidTuples) {
		if (myMdmExpansionCacheSvc.hasBeenPopulated()) {
			return;
		}
		//First, convert this zipped set of tuples to a map of
		//{
		//   patient/gold-1 -> [patient/1, patient/2]
		//   patient/gold-2 -> [patient/3, patient/4]
		//}
		Map<Long, Set<Long>> goldenResourceToSourcePidMap = new HashMap<>();
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
			Map<Long, Optional<String>> pidsToForcedIds = myIdHelperService.translatePidsToForcedIds(value);

			Set<String> sourceResourceIds = pidsToForcedIds.entrySet().stream()
				.map(ent -> ent.getValue().isPresent() ? ent.getValue().get() : ent.getKey().toString())
				.collect(Collectors.toSet());

			sourceResourceIds
				.forEach(sourceResourceId -> sourceResourceIdToGoldenResourceIdMap.put(sourceResourceId, goldenResourceId));
		});

		//Now that we have built our cached expansion, store it.
		myMdmExpansionCacheSvc.setCacheContents(sourceResourceIdToGoldenResourceIdMap);
	}

	/**
	 * Given the local myGroupId, read this group, and find all members' patient references.
	 * @return A list of strings representing the Patient IDs of the members (e.g. ["P1", "P2", "P3"]
	 */
	private List<String> getMembers() {
		SystemRequestDetails requestDetails = SystemRequestDetails.newSystemRequestAllPartitions();
		IBaseResource group = myDaoRegistry.getResourceDao("Group").read(new IdDt(myGroupId), requestDetails);
		List<IPrimitiveType> evaluate = myContext.newFhirPath().evaluate(group, "member.entity.reference", IPrimitiveType.class);
		return  evaluate.stream().map(IPrimitiveType::getValueAsString).collect(Collectors.toList());
	}

	/**
	 * Given the local myGroupId, perform an expansion to retrieve all resource IDs of member patients.
	 * if myMdmEnabled is set to true, we also reach out to the IMdmLinkDao to attempt to also expand it into matched
	 * patients.
	 *
	 * @return a Set of Strings representing the resource IDs of all members of a group.
	 */
	private Set<String> expandAllPatientPidsFromGroup() {
		Set<String> expandedIds = new HashSet<>();
		SystemRequestDetails requestDetails = SystemRequestDetails.newSystemRequestAllPartitions();
		IBaseResource group = myDaoRegistry.getResourceDao("Group").read(new IdDt(myGroupId), requestDetails);
		Long pidOrNull = myJpaIdHelperService.getPidOrNull(group);

		//Attempt to perform MDM Expansion of membership
		if (myMdmEnabled) {
			List<IMdmLinkDao.MdmPidTuple> goldenPidTargetPidTuples = myMdmLinkDao.expandPidsFromGroupPidGivenMatchResult(pidOrNull, MdmMatchResultEnum.MATCH);
			//Now lets translate these pids into resource IDs
			Set<Long> uniquePids = new HashSet<>();
			goldenPidTargetPidTuples.forEach(tuple -> {
				uniquePids.add(tuple.getGoldenPid());
				uniquePids.add(tuple.getSourcePid());
			});
			Map<Long, Optional<String>> pidToForcedIdMap = myIdHelperService.translatePidsToForcedIds(uniquePids);

			Map<Long, Set<Long>> goldenResourceToSourcePidMap = new HashMap<>();
			extract(goldenPidTargetPidTuples, goldenResourceToSourcePidMap);
			populateMdmResourceCache(goldenPidTargetPidTuples);

			//If the result of the translation is an empty optional, it means there is no forced id, and we can use the PID as the resource ID.
			Set<String> resolvedResourceIds = pidToForcedIdMap.entrySet().stream()
				.map(entry -> entry.getValue().isPresent() ? entry.getValue().get() : entry.getKey().toString())
				.collect(Collectors.toSet());

			expandedIds.addAll(resolvedResourceIds);
		}

		//Now manually add the members of the group (its possible even with mdm expansion that some members dont have MDM matches,
		//so would be otherwise skipped
		expandedIds.addAll(getMembers());

		return expandedIds;
	}

	private void extract(List<IMdmLinkDao.MdmPidTuple> theGoldenPidTargetPidTuples, Map<Long, Set<Long>> theGoldenResourceToSourcePidMap) {
		for (IMdmLinkDao.MdmPidTuple goldenPidTargetPidTuple : theGoldenPidTargetPidTuples) {
			Long goldenPid = goldenPidTargetPidTuple.getGoldenPid();
			Long sourcePid = goldenPidTargetPidTuple.getSourcePid();
			theGoldenResourceToSourcePidMap.computeIfAbsent(goldenPid, key -> new HashSet<>()).add(sourcePid);
		}
	}

	private void queryResourceTypeWithReferencesToPatients(Set<ResourcePersistentId> myReadPids, List<String> idChunk) {

		//Build SP map
		//First, inject the _typeFilters and _since from the export job
		List<SearchParameterMap> expandedSpMaps = createSearchParameterMapsForResourceType();
		for (SearchParameterMap expandedSpMap: expandedSpMaps) {

			//Since we are in a bulk job, we have to ensure the user didn't jam in a patient search param, since we need to manually set that.
			validateSearchParameters(expandedSpMap);

			// Fetch and cache a search builder for this resource type
			ISearchBuilder searchBuilder = getSearchBuilderForLocalResourceType();

			// Now, further filter the query with patient references defined by the chunk of IDs we have.
			if (PATIENT_BULK_EXPORT_FORWARD_REFERENCE_RESOURCE_TYPES.contains(myResourceType)) {
				filterSearchByHasParam(idChunk, expandedSpMap);
			} else {
				filterSearchByResourceIds(idChunk, expandedSpMap);
			}

			//Execute query and all found pids to our local iterator.
			IResultIterator resultIterator = searchBuilder.createQuery(expandedSpMap, new SearchRuntimeDetails(null, myJobUUID), null, RequestPartitionId.allPartitions());
			while (resultIterator.hasNext()) {
				myReadPids.add(resultIterator.next());
			}
		}
	}

	/**
	 *
	 * @param idChunk
	 * @param expandedSpMap
	 */
	private void filterSearchByHasParam(List<String> idChunk, SearchParameterMap expandedSpMap) {
		HasOrListParam hasOrListParam = new HasOrListParam();
		idChunk.stream().forEach(id -> hasOrListParam.addOr(buildHasParam(id)));
		expandedSpMap.add("_has", hasOrListParam);
	}

	private HasParam buildHasParam(String theId) {
		if ("Practitioner".equalsIgnoreCase(myResourceType)) {
			return new HasParam("Patient", "general-practitioner", "_id", theId);
		} else if ("Organization".equalsIgnoreCase(myResourceType)) {
			return new HasParam("Patient", "organization", "_id", theId);
		} else {
			throw new IllegalArgumentException(Msg.code(2077) + " We can't handle forward references onto type " + myResourceType);
		}
	}

	private void filterSearchByResourceIds(List<String> idChunk, SearchParameterMap expandedSpMap) {
		ReferenceOrListParam orList =  new ReferenceOrListParam();
		idChunk.forEach(id -> orList.add(new ReferenceParam(id)));
		expandedSpMap.add(getPatientSearchParamForCurrentResourceType().getName(), orList);
	}

	private void validateSearchParameters(SearchParameterMap expandedSpMap) {
		if (PATIENT_BULK_EXPORT_FORWARD_REFERENCE_RESOURCE_TYPES.contains(myResourceType)) {
			return;
		} else {
			RuntimeSearchParam runtimeSearchParam = getPatientSearchParamForCurrentResourceType();
			if (expandedSpMap.get(runtimeSearchParam.getName()) != null) {
				throw new IllegalArgumentException(Msg.code(792) + String.format("Group Bulk Export manually modifies the Search Parameter called [%s], so you may not include this search parameter in your _typeFilter!", runtimeSearchParam.getName()));
			}
		}
	}
}
