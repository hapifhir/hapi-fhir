package ca.uhn.fhir.jpa.empi.svc.candidate;

/*-
 * #%L
 * HAPI FHIR JPA Server - Enterprise Master Patient Index
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import ca.uhn.fhir.empi.api.IEmpiSettings;
import ca.uhn.fhir.empi.log.Logs;
import ca.uhn.fhir.empi.rules.json.EmpiFilterSearchParamJson;
import ca.uhn.fhir.empi.rules.json.EmpiResourceSearchParamJson;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.index.IdHelperService;
import ca.uhn.fhir.jpa.empi.svc.EmpiSearchParamSvc;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static ca.uhn.fhir.empi.api.EmpiConstants.ALL_RESOURCE_SEARCH_PARAM_TYPE;

@Service
public class EmpiCandidateSearchSvc {
	private static final Logger ourLog = Logs.getEmpiTroubleshootingLog();

	@Autowired
	private IEmpiSettings myEmpiConfig;
	@Autowired
	private EmpiSearchParamSvc myEmpiSearchParamSvc;
	@Autowired
	private DaoRegistry myDaoRegistry;
	@Autowired
	private IdHelperService myIdHelperService;
	@Autowired
	private EmpiCandidateSearchCriteriaBuilderSvc myEmpiCandidateSearchCriteriaBuilderSvc;

	public EmpiCandidateSearchSvc() {
	}

	/**
	 * Given a target resource, search for all resources that are considered an EMPI match based on defined EMPI rules.
	 *
	 *
	 * @param theResourceType
	 * @param theResource the target {@link IBaseResource} we are attempting to match.
	 *
	 * @return the list of candidate {@link IBaseResource} which could be matches to theResource
	 */
	public Collection<IAnyResource> findCandidates(String theResourceType, IAnyResource theResource) {
		Map<Long, IAnyResource> matchedPidsToResources = new HashMap<>();

		List<EmpiFilterSearchParamJson> filterSearchParams = myEmpiConfig.getEmpiRules().getCandidateFilterSearchParams();

		List<String> filterCriteria = buildFilterQuery(filterSearchParams, theResourceType);

		List<EmpiResourceSearchParamJson> candidateSearchParams = myEmpiConfig.getEmpiRules().getCandidateSearchParams();
		if (candidateSearchParams == null || candidateSearchParams.isEmpty()) {
			searchForIdsAndAddToMap(theResourceType, theResource, matchedPidsToResources, filterCriteria, null);
		} else {
			for (EmpiResourceSearchParamJson resourceSearchParam : candidateSearchParams) {

				if (!isSearchParamForResource(theResourceType, resourceSearchParam)) {
					continue;
				}

				searchForIdsAndAddToMap(theResourceType, theResource, matchedPidsToResources, filterCriteria, resourceSearchParam);
			}
		}
		//Obviously we don't want to consider the freshly added resource as a potential candidate.
		//Sometimes, we are running this function on a resource that has not yet been persisted,
		//so it may not have an ID yet, precluding the need to remove it.
		if (theResource.getIdElement().getIdPart() != null) {
			matchedPidsToResources.remove(myIdHelperService.getPidOrNull(theResource));
		}
		return matchedPidsToResources.values();
	}

	private boolean isSearchParamForResource(String theResourceType, EmpiResourceSearchParamJson resourceSearchParam) {
		String resourceType = resourceSearchParam.getResourceType();
		return resourceType.equals(theResourceType) || resourceType.equalsIgnoreCase(ALL_RESOURCE_SEARCH_PARAM_TYPE);
	}

	/*
	 * Helper method which performs too much work currently.
	 * 1. Build a full query string for the given filter and resource criteria.
	 * 2. Convert that URL to a SearchParameterMap.
	 * 3. Execute a Synchronous search on the DAO using that parameter map.
	 * 4. Store all results in `theMatchedPidsToResources`
	 */
	@SuppressWarnings("rawtypes")
	private void searchForIdsAndAddToMap(String theResourceType, IAnyResource theResource, Map<Long, IAnyResource> theMatchedPidsToResources, List<String> theFilterCriteria, EmpiResourceSearchParamJson resourceSearchParam) {
		//1.
		Optional<String> oResourceCriteria = myEmpiCandidateSearchCriteriaBuilderSvc.buildResourceQueryString(theResourceType, theResource, theFilterCriteria, resourceSearchParam);
		if (!oResourceCriteria.isPresent()) {
			return;
		}
		String resourceCriteria = oResourceCriteria.get();
		ourLog.debug("Searching for {} candidates with {}", theResourceType, resourceCriteria);

		//2.
		SearchParameterMap searchParameterMap = myEmpiSearchParamSvc.mapFromCriteria(theResourceType, resourceCriteria);

		searchParameterMap.setLoadSynchronous(true);

		//TODO EMPI this will blow up under large scale i think.
		//3.
		IFhirResourceDao<?> resourceDao = myDaoRegistry.getResourceDao(theResourceType);
		IBundleProvider search = resourceDao.search(searchParameterMap);
		List<IBaseResource> resources = search.getResources(0, search.size());

		int initialSize = theMatchedPidsToResources.size();

		//4.
		resources.forEach(resource -> theMatchedPidsToResources.put(myIdHelperService.getPidOrNull(resource), (IAnyResource) resource));

		int newSize = theMatchedPidsToResources.size();

		if (ourLog.isDebugEnabled()) {
			ourLog.debug("Candidate search added {} {}s", newSize - initialSize, theResourceType);
		}
	}

	private List<String> buildFilterQuery(List<EmpiFilterSearchParamJson> theFilterSearchParams, String theResourceType) {
		return Collections.unmodifiableList(theFilterSearchParams.stream()
			.filter(spFilterJson -> paramIsOnCorrectType(theResourceType, spFilterJson))
			.map(this::convertToQueryString)
			.collect(Collectors.toList()));
	}

	private boolean paramIsOnCorrectType(String theResourceType, EmpiFilterSearchParamJson spFilterJson) {
		return spFilterJson.getResourceType().equals(theResourceType) || spFilterJson.getResourceType().equalsIgnoreCase(ALL_RESOURCE_SEARCH_PARAM_TYPE);
	}

	private String convertToQueryString(EmpiFilterSearchParamJson theSpFilterJson) {
		String qualifier = theSpFilterJson.getTokenParamModifierAsString();
		return theSpFilterJson.getSearchParam() + qualifier + "=" + theSpFilterJson.getFixedValue();
	}
}
