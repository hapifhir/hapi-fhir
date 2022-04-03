package ca.uhn.fhir.jpa.bulk.export.job;

/*-
 * #%L
 * HAPI FHIR Storage api
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
import ca.uhn.fhir.jpa.batch.log.Logs;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.param.DateRangeParam;
import org.slf4j.Logger;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public abstract class BaseBulkItemReader implements ItemReader<List<ResourcePersistentId>> {
	private static final Logger ourLog = Logs.getBatchTroubleshootingLog();

	@Value("#{stepExecutionContext['resourceType']}")
	protected String myResourceType;
	@Value("#{jobParameters['readChunkSize']}")
	protected Long myReadChunkSize;
	@Autowired
	protected FhirContext myContext;
	@Autowired
	private MatchUrlService myMatchUrlService;

	private Iterator<ResourcePersistentId> myPidIterator;
	private RuntimeResourceDefinition myResourceDefinition;

	/**
	 * Generate the list of PIDs of all resources of the given myResourceType, which reference any group member of the given myGroupId.
	 * Store them in a member iterator.
	 */
	protected void loadResourcePIDs() {
		//Initialize an array to hold the PIDs of the target resources to be exported.
		myPidIterator = getResourcePidIterator();
	}

	protected abstract Iterator<ResourcePersistentId> getResourcePidIterator();

	protected abstract String[] getTypeFilterList();

	protected abstract Date getSinceDate();

	protected abstract String getLogInfoForRead();

	protected List<SearchParameterMap> createSearchParameterMapsForResourceType() {
		RuntimeResourceDefinition theDef = getResourceDefinition();
		String[] typeFilters = getTypeFilterList();
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
		if (getSinceDate() != null) {
			map.setLastUpdated(new DateRangeParam(getSinceDate(), null));
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

	@Override
	public List<ResourcePersistentId> read() {
		ourLog.info(getLogInfoForRead());

		if (myPidIterator == null) {
			loadResourcePIDs();
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
