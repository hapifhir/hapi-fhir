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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.batch.log.Logs;
import ca.uhn.fhir.jpa.dao.IResultIterator;
import ca.uhn.fhir.jpa.dao.ISearchBuilder;
import ca.uhn.fhir.jpa.dao.SearchBuilderFactory;
import ca.uhn.fhir.jpa.dao.data.IBulkExportJobDao;
import ca.uhn.fhir.jpa.dao.data.IMdmLinkDao;
import ca.uhn.fhir.jpa.dao.index.IdHelperService;
import ca.uhn.fhir.jpa.entity.BulkExportJobEntity;
import ca.uhn.fhir.jpa.model.search.SearchRuntimeDetails;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.util.QueryChunker;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.param.ReferenceOrListParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.util.UrlUtil;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.slf4j.Logger;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class GroupBulkItemReader implements ItemReader<List<ResourcePersistentId>> {
	private static final Logger ourLog = Logs.getBatchTroubleshootingLog();
	Iterator<ResourcePersistentId> myPidIterator;

	@Value("#{stepExecutionContext['resourceType']}")
	private String myResourceType;
	@Value("#{jobParameters['" + BulkExportJobConfig.GROUP_ID_PARAMETER + "']}")
	private String myGroupId;
	@Value("#{jobExecutionContext['"+ BulkExportJobConfig.JOB_UUID_PARAMETER+"']}")
	private String myJobUUID;
	@Value("#{jobParameters['" + BulkExportJobConfig.READ_CHUNK_PARAMETER + "']}")
	private Long myReadChunkSize;
	@Value("#{jobParameters['" + BulkExportJobConfig.EXPAND_MDM_PARAMETER+ "']}")
	private String myMdmEnabled;

	@Autowired
	private IBulkExportJobDao myBulkExportJobDao;
	@Autowired
	private DaoRegistry myDaoRegistry;
	@Autowired
	private FhirContext myContext;
	@Autowired
	private SearchBuilderFactory mySearchBuilderFactory;
	@Autowired
	private IdHelperService myIdHelperService;
	@Autowired
	private IMdmLinkDao myMdmLinkDao;
	@Autowired
	private MatchUrlService myMatchUrlService;

	private ISearchBuilder mySearchBuilder;
	private RuntimeSearchParam myPatientSearchParam;
	private BulkExportJobEntity myJobEntity;
	private RuntimeResourceDefinition myResourceDefinition;

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
		if (Boolean.valueOf(myMdmEnabled)) {
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

	/**
	 * Generate the list of pids of all resources of the given myResourceType, which reference any group member of the given myGroupId.
	 * Store them in a member iterator.
	 */
	private void loadResourcePids() {
		//Initialize an array to hold the pids of the target resources to be exported.
		List<ResourcePersistentId> myReadPids = new ArrayList<>();

		//First lets expand the group so we get a list of all patient IDs of the group, and MDM-matched patient IDs of the group.
		Set<String> expandedMemberResourceIds = expandAllPatientPidsFromGroup();


		//Next, let's search for the target resources, with their correct patient references, chunked.
		//The results will be jammed into myReadPids
		QueryChunker<String> queryChunker = new QueryChunker<>();
		queryChunker.chunk(new ArrayList<>(expandedMemberResourceIds), 100, (idChunk) -> {
			queryTargetResourceWithReferencesToPatients(myReadPids, idChunk);
		});
		myPidIterator = myReadPids.iterator();
	}

	private void queryTargetResourceWithReferencesToPatients(List<ResourcePersistentId> myReadPids, List<String> idChunk) {
		//Build SP map
		//First, inject the _typeFilters from the export job
		SearchParameterMap expandedSpMap = createSearchParameterMapFromTypeFilter();

		//Reject any attempts for users to filter on the patient searchparameter, as we have to manually set it.
		RuntimeSearchParam runtimeSearchParam = getPatientSearchParam();
		if (expandedSpMap.get(runtimeSearchParam.getName()) != null) {
			throw new IllegalArgumentException(String.format("Group Bulk Export manually modifies the Search Parameter called [{}], so you may not include this search parameter in your _typeFilter!", runtimeSearchParam.getName()));
		}

		ReferenceOrListParam orList =  new ReferenceOrListParam();
		idChunk.forEach(id -> orList.add(new ReferenceParam(id)));
		expandedSpMap.add(runtimeSearchParam.getName(), orList);

		// Fetch and cache a search builder for this resource type
		ISearchBuilder searchBuilder = getSearchBuilderForLocalResourceType();

		//Execute query and all found pids to our local iterator.
		IResultIterator resultIterator = searchBuilder.createQuery(expandedSpMap, new SearchRuntimeDetails(null, myJobUUID), null, RequestPartitionId.allPartitions());
		while (resultIterator.hasNext()) {
			myReadPids.add(resultIterator.next());
		}
	}

	/**
	 * Get and cache an ISearchBuilder for the given resource type this partition is responsible for.
	 */
	private ISearchBuilder getSearchBuilderForLocalResourceType() {
		if (mySearchBuilder == null) {
			IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(myResourceType);
			RuntimeResourceDefinition def = myContext.getResourceDefinition(myResourceType);
			Class<? extends IBaseResource> nextTypeClass = def.getImplementingClass();
			mySearchBuilder = mySearchBuilderFactory.newSearchBuilder(dao, myResourceType, nextTypeClass);
		}
		return mySearchBuilder;
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

	private SearchParameterMap createSearchParameterMapFromTypeFilter() {
		SearchParameterMap map = new SearchParameterMap();
		Map<String, String[]> requestUrl = UrlUtil.parseQueryStrings(getJobEntity().getRequest());
		String[] typeFilters = requestUrl.get(JpaConstants.PARAM_EXPORT_TYPE_FILTER);
		if (typeFilters != null) {
			Optional<String> filter = Arrays.stream(typeFilters).filter(t -> t.startsWith(myResourceType + "?")).findFirst();
			if (filter.isPresent()) {
				String matchUrl = filter.get();
				map = myMatchUrlService.translateMatchUrl(matchUrl, getResourceDefinition());
			}
		}
		map.setLoadSynchronous(true);
		return map;
	}

	private RuntimeResourceDefinition getResourceDefinition() {
		if (myResourceDefinition == null) {
			myResourceDefinition = myContext.getResourceDefinition(myResourceType);
		}
		return myResourceDefinition;
	}

	private BulkExportJobEntity getJobEntity() {
		if (myJobEntity == null) {
			Optional<BulkExportJobEntity> jobOpt = myBulkExportJobDao.findByJobId(myJobUUID);
			if (jobOpt.isPresent()) {
				myJobEntity = jobOpt.get();
			} else {
				String errorMessage  = String.format("Job with UUID %s does not exist!", myJobUUID);
				throw new IllegalStateException(errorMessage);
			}
		}
		return myJobEntity;
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

	@Override
	public List<ResourcePersistentId> read() {

		ourLog.info("Group Bulk export starting generation for batch export job: [{}] with resourceType [{}] and UUID [{}]", getJobEntity(), myResourceType, myJobUUID);

		if (myPidIterator == null) {
			loadResourcePids();
		}

		int count = 0;
		List<ResourcePersistentId> outgoing = new ArrayList<>();
		while (myPidIterator.hasNext() && count < myReadChunkSize) {
			outgoing.add(myPidIterator.next());
			count += 1;
		}

		return outgoing.size() == 0 ? null : outgoing;

	}

}
