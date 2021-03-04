package ca.uhn.fhir.jpa.bulk.job;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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

import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.batch.log.Logs;
import ca.uhn.fhir.jpa.dao.IResultIterator;
import ca.uhn.fhir.jpa.dao.ISearchBuilder;
import ca.uhn.fhir.jpa.dao.data.IMdmLinkDao;
import ca.uhn.fhir.jpa.dao.index.IdHelperService;
import ca.uhn.fhir.jpa.model.search.SearchRuntimeDetails;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.util.QueryChunker;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.param.ReferenceOrListParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Bulk Item reader for the Group Bulk Export job.
 * Instead of performing a normal query on the resource type using type filters, we instead
 *
 * 1. Get the group ID defined for this job
 * 2. Expand its membership so we get references to all patients in the group
 * 3. Optionally further expand that into all MDM-matched Patients (including golden resources)
 * 4. Then perform normal bulk export, filtered so that only results that refer to members are returned.
 */
public class GroupBulkItemReader extends BaseBulkItemReader implements ItemReader<List<ResourcePersistentId>> {
	private static final Logger ourLog = Logs.getBatchTroubleshootingLog();
	public static final int QUERY_CHUNK_SIZE = 100;

	@Value("#{jobParameters['" + BulkExportJobConfig.GROUP_ID_PARAMETER + "']}")
	private String myGroupId;
	@Value("#{jobParameters['" + BulkExportJobConfig.EXPAND_MDM_PARAMETER+ "'] ?: false}")
	private boolean myMdmEnabled;

	@Autowired
	private IdHelperService myIdHelperService;
	@Autowired
	private IMdmLinkDao myMdmLinkDao;

	private RuntimeSearchParam myPatientSearchParam;

	@Override
	Iterator<ResourcePersistentId> getResourcePidIterator() {
		List<ResourcePersistentId> myReadPids = new ArrayList<>();

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
		QueryChunker<String> queryChunker = new QueryChunker<>();
		queryChunker.chunk(new ArrayList<>(expandedMemberResourceIds), QUERY_CHUNK_SIZE, (idChunk) -> {
			queryResourceTypeWithReferencesToPatients(myReadPids, idChunk);
		});

		if (ourLog.isDebugEnabled()) {
			ourLog.debug("Resource PIDs to be Bulk Exported: [{}]", myReadPids.stream().map(ResourcePersistentId::toString).collect(Collectors.joining(",")));
		}
		return myReadPids.iterator();
	}

	/**
	 * In case we are doing a Group Bulk Export and resourceType `Patient` is requested, we can just return the group members,
	 * possibly expanded by MDM, and don't have to go and fetch other resource DAOs.
	 */
	private Iterator<ResourcePersistentId> getExpandedPatientIterator() {
		Set<Long> patientPidsToExport = new HashSet<>();
		//This gets all member pids
		List<String> members = getMembers();
		List<IIdType> ids = members.stream().map(member -> new IdDt("Patient/" + member)).collect(Collectors.toList());
		List<Long> pidsOrThrowException = myIdHelperService.getPidsOrThrowException(ids);
		patientPidsToExport.addAll(pidsOrThrowException);

		if (myMdmEnabled) {
			IBaseResource group = myDaoRegistry.getResourceDao("Group").read(new IdDt(myGroupId));
			Long pidOrNull = myIdHelperService.getPidOrNull(group);
			List<List<Long>> lists = myMdmLinkDao.expandPidsFromGroupPidGivenMatchResult(pidOrNull, MdmMatchResultEnum.MATCH);
			lists.forEach(patientPidsToExport::addAll);
		}
		List<ResourcePersistentId> resourcePersistentIds = patientPidsToExport
			.stream()
			.map(ResourcePersistentId::new)
			.collect(Collectors.toList());
		return resourcePersistentIds.iterator();
	}

	/**
	 * Given the local myGroupId, read this group, and find all members' patient references.
	 * @return A list of strings representing the Patient IDs of the members (e.g. ["P1", "P2", "P3"]
	 */
	private List<String> getMembers() {
		IBaseResource group = myDaoRegistry.getResourceDao("Group").read(new IdDt(myGroupId));
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
		IBaseResource group = myDaoRegistry.getResourceDao("Group").read(new IdDt(myGroupId));
		Long pidOrNull = myIdHelperService.getPidOrNull(group);

		//Attempt to perform MDM Expansion of membership
		if (myMdmEnabled) {
			List<List<Long>> goldenPidTargetPidTuple = myMdmLinkDao.expandPidsFromGroupPidGivenMatchResult(pidOrNull, MdmMatchResultEnum.MATCH);
			//Now lets translate these pids into resource IDs
			Set<Long> uniquePids = new HashSet<>();
			goldenPidTargetPidTuple.forEach(uniquePids::addAll);
			Map<Long, Optional<String>> longOptionalMap = myIdHelperService.translatePidsToForcedIds(uniquePids);
			expandedIds = longOptionalMap.values().stream().map(Optional::get).collect(Collectors.toSet());
		}

		//Now manually add the members of the group (its possible even with mdm expansion that some members dont have MDM matches,
		//so would be otherwise skipped
		expandedIds.addAll(getMembers());

		return expandedIds;
	}


	private void queryResourceTypeWithReferencesToPatients(List<ResourcePersistentId> myReadPids, List<String> idChunk) {
		//Build SP map
		//First, inject the _typeFilters and _since from the export job
		SearchParameterMap expandedSpMap = createSearchParameterMapForJob();

		//Since we are in a bulk job, we have to ensure the user didn't jam in a patient search param, since we need to manually set that.
		validateSearchParameters(expandedSpMap);

		// Now, further filter the query with patient references defined by the chunk of IDs we have.
		filterSearchByResourceIds(idChunk, expandedSpMap);

		// Fetch and cache a search builder for this resource type
		ISearchBuilder searchBuilder = getSearchBuilderForLocalResourceType();

		//Execute query and all found pids to our local iterator.
		IResultIterator resultIterator = searchBuilder.createQuery(expandedSpMap, new SearchRuntimeDetails(null, myJobUUID), null, RequestPartitionId.allPartitions());
		while (resultIterator.hasNext()) {
			myReadPids.add(resultIterator.next());
		}
	}

	private void filterSearchByResourceIds(List<String> idChunk, SearchParameterMap expandedSpMap) {
		ReferenceOrListParam orList =  new ReferenceOrListParam();
		idChunk.forEach(id -> orList.add(new ReferenceParam(id)));
		expandedSpMap.add(getPatientSearchParam().getName(), orList);
	}

	private RuntimeSearchParam validateSearchParameters(SearchParameterMap expandedSpMap) {
		RuntimeSearchParam runtimeSearchParam = getPatientSearchParam();
		if (expandedSpMap.get(runtimeSearchParam.getName()) != null) {
			throw new IllegalArgumentException(String.format("Group Bulk Export manually modifies the Search Parameter called [%s], so you may not include this search parameter in your _typeFilter!", runtimeSearchParam.getName()));
		}
		return runtimeSearchParam;
	}

	/**
	 * Given the resource type, fetch its patient-based search parameter name
	 * 1. Attempt to find one called 'patient'
	 * 2. If that fails, find one called 'subject'
	 * 3. If that fails, find find by Patient Compartment.
	 *    3.1 If that returns >1 result, throw an error
	 *    3.2 If that returns 1 result, return it
	 */
	private RuntimeSearchParam getPatientSearchParam() {
		if (myPatientSearchParam == null) {
			RuntimeResourceDefinition runtimeResourceDefinition = myContext.getResourceDefinition(myResourceType);
			myPatientSearchParam = runtimeResourceDefinition.getSearchParam("patient");
			if (myPatientSearchParam == null) {
				myPatientSearchParam = runtimeResourceDefinition.getSearchParam("subject");
				if (myPatientSearchParam == null) {
					myPatientSearchParam = getRuntimeSearchParamByCompartment(runtimeResourceDefinition);
					if (myPatientSearchParam == null) {
						String errorMessage = String.format("[%s] has  no search parameters that are for patients, so it is invalid for Group Bulk Export!", myResourceType);
						throw new IllegalArgumentException(errorMessage);
					}
				}
			}
		}
		return myPatientSearchParam;
	}

	/**
	 * Search the resource definition for a compartment named 'patient' and return its related Search Parameter.
	 */
	private RuntimeSearchParam getRuntimeSearchParamByCompartment(RuntimeResourceDefinition runtimeResourceDefinition) {
		RuntimeSearchParam patientSearchParam;
		List<RuntimeSearchParam> searchParams = runtimeResourceDefinition.getSearchParamsForCompartmentName("Patient");
		if (searchParams == null || searchParams.size() == 0) {
			String errorMessage = String.format("Resource type [%s] is not eligible for Group Bulk export, as it contains no Patient compartment, and no `patient` or `subject` search parameter", myResourceType);
			throw new IllegalArgumentException(errorMessage);
		} else if (searchParams.size() == 1) {
			patientSearchParam = searchParams.get(0);
		} else {
			String errorMessage = String.format("Resource type [%s] is not eligible for Group Bulk export, as we are unable to disambiguate which patient search parameter we should be searching by.", myResourceType);
			throw new IllegalArgumentException(errorMessage);
		}
		return patientSearchParam;
	}
}
