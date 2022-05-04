package ca.uhn.fhir.jpa.mdm.svc.candidate;

/*-
 * #%L
 * HAPI FHIR JPA Server - Master Data Management
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

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.dao.index.IdHelperService;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.dao.index.IJpaIdHelperService;
import ca.uhn.fhir.mdm.api.IMdmSettings;
import ca.uhn.fhir.mdm.log.Logs;
import ca.uhn.fhir.mdm.rules.json.MdmFilterSearchParamJson;
import ca.uhn.fhir.mdm.rules.json.MdmResourceSearchParamJson;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static ca.uhn.fhir.mdm.api.MdmConstants.ALL_RESOURCE_SEARCH_PARAM_TYPE;

@Service
public class MdmCandidateSearchSvc {

	private static final Logger ourLog = Logs.getMdmTroubleshootingLog();

	@Autowired
	private IMdmSettings myMdmSettings;
	@Autowired
	private IJpaIdHelperService myJpaIdHelperService;
	@Autowired
	private MdmCandidateSearchCriteriaBuilderSvc myMdmCandidateSearchCriteriaBuilderSvc;
	@Autowired
	private CandidateSearcher myCandidateSearcher;

	public MdmCandidateSearchSvc() {
	}

	/**
	 * Given a source resource, search for all resources that are considered an MDM match based on defined MDM rules.
	 *
	 * @param theResourceType the resource type of the resource being matched
	 * @param theResource the {@link IBaseResource} we are attempting to match.
	 * @param theRequestPartitionId  the {@link RequestPartitionId} representation of the partitions we are limited to when attempting to match
	 *
	 * @return the list of candidate {@link IBaseResource} which could be matches to theResource
	 */
	@Transactional
	public Collection<IAnyResource> findCandidates(String theResourceType, IAnyResource theResource, RequestPartitionId theRequestPartitionId) {
		Map<Long, IAnyResource> matchedPidsToResources = new HashMap<>();
		List<MdmFilterSearchParamJson> filterSearchParams = myMdmSettings.getMdmRules().getCandidateFilterSearchParams();
		List<String> filterCriteria = buildFilterQuery(filterSearchParams, theResourceType);
		List<MdmResourceSearchParamJson> candidateSearchParams = myMdmSettings.getMdmRules().getCandidateSearchParams();

		//If there are zero MdmResourceSearchParamJson, we end up only making a single search, otherwise we
		//must perform one search per MdmResourceSearchParamJson.
		if (candidateSearchParams.isEmpty()) {
			searchForIdsAndAddToMap(theResourceType, theResource, matchedPidsToResources, filterCriteria, null, theRequestPartitionId);
		} else {
			for (MdmResourceSearchParamJson resourceSearchParam : candidateSearchParams) {

				if (!isSearchParamForResource(theResourceType, resourceSearchParam)) {
					continue;
				}

				searchForIdsAndAddToMap(theResourceType, theResource, matchedPidsToResources, filterCriteria, resourceSearchParam, theRequestPartitionId);
			}
		}
		//Obviously we don't want to consider the freshly added resource as a potential candidate.
		//Sometimes, we are running this function on a resource that has not yet been persisted,
		//so it may not have an ID yet, precluding the need to remove it.
		if (theResource.getIdElement().getIdPart() != null) {
			matchedPidsToResources.remove(myJpaIdHelperService.getPidOrNull(theResource));
		}

		ourLog.info("Found {} resources for {}", matchedPidsToResources.size(), theResourceType);
		return matchedPidsToResources.values();
	}

	private boolean isSearchParamForResource(String theResourceType, MdmResourceSearchParamJson resourceSearchParam) {
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
	private void searchForIdsAndAddToMap(String theResourceType, IAnyResource theResource, Map<Long, IAnyResource> theMatchedPidsToResources, List<String> theFilterCriteria, MdmResourceSearchParamJson resourceSearchParam, RequestPartitionId theRequestPartitionId) {
		//1.
		Optional<String> oResourceCriteria = myMdmCandidateSearchCriteriaBuilderSvc.buildResourceQueryString(theResourceType, theResource, theFilterCriteria, resourceSearchParam);
		if (!oResourceCriteria.isPresent()) {
			return;
		}
		String resourceCriteria = oResourceCriteria.get();
		ourLog.debug("Searching for {} candidates with {}", theResourceType, resourceCriteria);

		//2.
		Optional<IBundleProvider> bundleProvider = myCandidateSearcher.search(theResourceType, resourceCriteria, theRequestPartitionId);
		if (!bundleProvider.isPresent()) {
			throw new TooManyCandidatesException(Msg.code(762) + "More than " + myMdmSettings.getCandidateSearchLimit() + " candidate matches found for " + resourceCriteria + ".  Aborting mdm matching.");
		}
		List<IBaseResource> resources = bundleProvider.get().getAllResources();

		int initialSize = theMatchedPidsToResources.size();

		//4.
		resources.forEach(resource -> theMatchedPidsToResources.put(myJpaIdHelperService.getPidOrNull(resource), (IAnyResource) resource));

		int newSize = theMatchedPidsToResources.size();

		if (ourLog.isDebugEnabled()) {
			ourLog.debug("Candidate search added {} {}s", newSize - initialSize, theResourceType);
		}
	}

	private List<String> buildFilterQuery(List<MdmFilterSearchParamJson> theFilterSearchParams, String theResourceType) {
		return Collections.unmodifiableList(theFilterSearchParams.stream()
			.filter(spFilterJson -> paramIsOnCorrectType(theResourceType, spFilterJson))
			.map(this::convertToQueryString)
			.collect(Collectors.toList()));
	}

	private boolean paramIsOnCorrectType(String theResourceType, MdmFilterSearchParamJson spFilterJson) {
		return spFilterJson.getResourceType().equals(theResourceType) || spFilterJson.getResourceType().equalsIgnoreCase(ALL_RESOURCE_SEARCH_PARAM_TYPE);
	}

	private String convertToQueryString(MdmFilterSearchParamJson theSpFilterJson) {
		String qualifier = theSpFilterJson.getTokenParamModifierAsString();
		return theSpFilterJson.getSearchParam() + qualifier + "=" + theSpFilterJson.getFixedValue();
	}
}
