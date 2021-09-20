package ca.uhn.fhir.jpa.bulk.export.job;

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
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.batch.config.BatchConstants;
import ca.uhn.fhir.jpa.batch.log.Logs;
import ca.uhn.fhir.jpa.dao.ISearchBuilder;
import ca.uhn.fhir.jpa.dao.SearchBuilderFactory;
import ca.uhn.fhir.jpa.dao.data.IBulkExportJobDao;
import ca.uhn.fhir.jpa.entity.BulkExportJobEntity;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.util.SearchParameterUtil;
import ca.uhn.fhir.util.UrlUtil;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class BaseBulkItemReader implements ItemReader<List<ResourcePersistentId>> {
	private static final Logger ourLog = Logs.getBatchTroubleshootingLog();

	@Value("#{stepExecutionContext['resourceType']}")
	protected String myResourceType;
	@Value("#{jobExecutionContext['" + BatchConstants.JOB_UUID_PARAMETER + "']}")
	protected String myJobUUID;
	@Value("#{jobParameters['" + BulkExportJobConfig.READ_CHUNK_PARAMETER + "']}")
	protected Long myReadChunkSize;
	@Autowired
	protected DaoRegistry myDaoRegistry;
	@Autowired
	protected FhirContext myContext;
	@Autowired
	protected SearchBuilderFactory mySearchBuilderFactory;
	@Autowired
	private IBulkExportJobDao myBulkExportJobDao;
	@Autowired
	private MatchUrlService myMatchUrlService;

	private ISearchBuilder mySearchBuilder;
	private BulkExportJobEntity myJobEntity;
	private RuntimeResourceDefinition myResourceDefinition;

	private Iterator<ResourcePersistentId> myPidIterator;
	private RuntimeSearchParam myPatientSearchParam;

	/**
	 * Get and cache an ISearchBuilder for the given resource type this partition is responsible for.
	 */
	protected ISearchBuilder getSearchBuilderForLocalResourceType() {
		if (mySearchBuilder == null) {
			IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(myResourceType);
			RuntimeResourceDefinition def = myContext.getResourceDefinition(myResourceType);
			Class<? extends IBaseResource> nextTypeClass = def.getImplementingClass();
			mySearchBuilder = mySearchBuilderFactory.newSearchBuilder(dao, myResourceType, nextTypeClass);
		}
		return mySearchBuilder;
	}

	/**
	 * Generate the list of pids of all resources of the given myResourceType, which reference any group member of the given myGroupId.
	 * Store them in a member iterator.
	 */
	protected void loadResourcePids() {
		//Initialize an array to hold the pids of the target resources to be exported.
		myPidIterator = getResourcePidIterator();
	}

	protected abstract Iterator<ResourcePersistentId> getResourcePidIterator();

	protected List<SearchParameterMap> createSearchParameterMapsForResourceType() {
		BulkExportJobEntity jobEntity = getJobEntity();
		RuntimeResourceDefinition theDef = getResourceDefinition();
		Map<String, String[]> requestUrl = UrlUtil.parseQueryStrings(jobEntity.getRequest());
		String[] typeFilters = requestUrl.get(JpaConstants.PARAM_EXPORT_TYPE_FILTER);
		List<SearchParameterMap> spMaps = null;
		if (typeFilters != null) {
			spMaps = Arrays.stream(typeFilters)
				.filter(typeFilter -> typeFilter.startsWith(myResourceType + "?"))
				.map(filter -> buildSearchParameterMapForTypeFilter(filter, theDef))
				.collect(Collectors.toList());
		}

		//None of the _typeFilters applied to the current resource type, so just make a simple one.
		if (spMaps == null || spMaps.isEmpty()) {
			SearchParameterMap defaultMap = new SearchParameterMap();
			enhanceSearchParameterMapWithCommonParameters(defaultMap);
			spMaps = Collections.singletonList(defaultMap);
		}

		return spMaps;
	}

	private void enhanceSearchParameterMapWithCommonParameters(SearchParameterMap map) {
		map.setLoadSynchronous(true);
		if (getJobEntity().getSince() != null) {
			map.setLastUpdated(new DateRangeParam(getJobEntity().getSince(), null));
		}
	}

	public SearchParameterMap buildSearchParameterMapForTypeFilter(String theFilter, RuntimeResourceDefinition theDef) {
		SearchParameterMap searchParameterMap = myMatchUrlService.translateMatchUrl(theFilter, theDef);
		enhanceSearchParameterMapWithCommonParameters(searchParameterMap);
		return searchParameterMap;
	}

	protected RuntimeResourceDefinition getResourceDefinition() {
		if (myResourceDefinition == null) {
			myResourceDefinition = myContext.getResourceDefinition(myResourceType);
		}
		return myResourceDefinition;
	}

	protected BulkExportJobEntity getJobEntity() {
		if (myJobEntity == null) {
			Optional<BulkExportJobEntity> jobOpt = myBulkExportJobDao.findByJobId(myJobUUID);
			if (jobOpt.isPresent()) {
				myJobEntity = jobOpt.get();
			} else {
				String errorMessage = String.format("Job with UUID %s does not exist!", myJobUUID);
				throw new IllegalStateException(errorMessage);
			}
		}
		return myJobEntity;
	}

	@Override
	public List<ResourcePersistentId> read() {

		ourLog.info("Bulk export starting generation for batch export job: [{}] with resourceType [{}] and UUID [{}]", getJobEntity(), myResourceType, myJobUUID);

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

	protected RuntimeSearchParam getPatientSearchParamForCurrentResourceType() {
		if (myPatientSearchParam == null) {
			Optional<RuntimeSearchParam> onlyPatientSearchParamForResourceType = SearchParameterUtil.getOnlyPatientSearchParamForResourceType(myContext, myResourceType);
			if (onlyPatientSearchParamForResourceType.isPresent()) {
				myPatientSearchParam = onlyPatientSearchParamForResourceType.get();
			} else {

			}
		}
		return myPatientSearchParam;
	}
}
