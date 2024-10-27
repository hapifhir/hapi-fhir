/*-
 * #%L
 * HAPI FHIR Storage api
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
package ca.uhn.fhir.jpa.bulk.export.svc;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.jpa.bulk.export.model.ExportPIDIteratorParameters;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.param.DateRangeParam;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

public class BulkExportHelperService {
	private static final Logger ourLog = getLogger(BulkExportHelperService.class);

	@Autowired
	private MatchUrlService myMatchUrlService;

	@Autowired
	private FhirContext myContext;

	/**
	 * Given the parameters, create the search parameter map based on type filters and the _since parameter.
	 *
	 * The input boolean theConsiderSince determines whether to consider the lastUpdated date in the search parameter map.
	 */
	public List<SearchParameterMap> createSearchParameterMapsForResourceType(
			RuntimeResourceDefinition theDef, ExportPIDIteratorParameters theParams, boolean theConsiderSince) {
		String resourceType = theDef.getName();
		List<String> typeFilters = theParams.getFilters();
		List<SearchParameterMap> spMaps = null;
		spMaps = typeFilters.stream()
				.filter(typeFilter -> typeFilter.startsWith(resourceType + "?"))
				.map(filter -> buildSearchParameterMapForTypeFilter(filter, theDef, theParams.getStartDate()))
				.collect(Collectors.toList());

		typeFilters.stream().filter(filter -> !filter.contains("?")).forEach(filter -> {
			ourLog.warn(
					"Found a strange _typeFilter that we could not process: {}. _typeFilters should follow the format ResourceType?searchparameter=value .",
					filter);
		});

		// None of the _typeFilters applied to the current resource type, so just make a simple one.
		if (spMaps.isEmpty()) {
			SearchParameterMap defaultMap = new SearchParameterMap();
			if (theConsiderSince) {
				enhanceSearchParameterMapWithCommonParameters(defaultMap, theParams.getStartDate());
			}
			spMaps = Collections.singletonList(defaultMap);
		}

		return spMaps;
	}

	private SearchParameterMap buildSearchParameterMapForTypeFilter(
			String theFilter, RuntimeResourceDefinition theDef, Date theSinceDate) {
		SearchParameterMap searchParameterMap = myMatchUrlService.translateMatchUrl(theFilter, theDef);
		enhanceSearchParameterMapWithCommonParameters(searchParameterMap, theSinceDate);
		return searchParameterMap;
	}

	private void enhanceSearchParameterMapWithCommonParameters(SearchParameterMap map, Date theSinceDate) {
		map.setLoadSynchronous(true);
		if (theSinceDate != null) {
			map.setLastUpdated(new DateRangeParam(theSinceDate, null));
		}
	}

	/**
	 * Converts the ResourceId to an IIdType.
	 * Eg: Patient/123 -> IIdType
	 * @param theResourceId - string version if the id
	 * @return - the IIdType
	 */
	public IIdType toId(String theResourceId) {
		IIdType retVal = myContext.getVersion().newIdType();
		retVal.setValue(theResourceId);
		return retVal;
	}
}
