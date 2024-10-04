/*-
 * #%L
 * HAPI FHIR JPA - Search Parameters
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
package ca.uhn.fhir.jpa.searchparam.matcher;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.extractor.ISearchParamExtractor;
import ca.uhn.fhir.jpa.searchparam.extractor.ResourceIndexedSearchParams;
import ca.uhn.fhir.jpa.searchparam.models.SearchMatchParameters;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import com.google.common.collect.Sets;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SearchParamMatcher {

	public static final Set<String> UNSUPPORTED_PARAMETER_NAMES = Sets.newHashSet(Constants.PARAM_HAS);
	private static final org.slf4j.Logger ourLog = LoggerFactory.getLogger(InMemoryResourceMatcher.class);

	private final FhirContext myFhirContext;

	private final IndexedSearchParamExtractor myIndexedSearchParamExtractor;

	protected final InMemoryResourceMatcher myInMemoryResourceMatcher;

	public SearchParamMatcher(
		FhirContext theFhirContext,
		IndexedSearchParamExtractor theIndexedSearchParamExtractor,
		InMemoryResourceMatcher theInMemoryResourceMatcher
	) {
		myFhirContext = theFhirContext;
		myIndexedSearchParamExtractor = theIndexedSearchParamExtractor;
		myInMemoryResourceMatcher = theInMemoryResourceMatcher;
	}

	public void addSearchParamMatchHandler(IParameterMatchHandler theMatchHandler) {
		myInMemoryResourceMatcher.addSearchParameterHandler(theMatchHandler);
	}

	public InMemoryMatchResult match(SearchMatchParameters theParameters) {
		return myInMemoryResourceMatcher.match(theParameters);
	}

	@Deprecated
	public InMemoryMatchResult match(String theCriteria, IBaseResource theResource, RequestDetails theRequest) {
//		return myInMemoryResourceMatcher.match(theCriteria, theResource, null, theRequest);
		SearchMatchParameters parameters = new SearchMatchParameters();
		parameters.setCriteria(theCriteria);
		parameters.setBaseResource(theResource);
		parameters.setRequestDetails(theRequest);
		return match(parameters);
	}

	public InMemoryMatchResult match(SearchParameterMap theSearchParameterMap, IBaseResource theResource) {
		if (theSearchParameterMap.isEmpty()) {
			return InMemoryMatchResult.successfulMatch();
		}
		ResourceIndexedSearchParams resourceIndexedSearchParams =
				myIndexedSearchParamExtractor.extractIndexedSearchParams(
						theResource, null, getFilter(theSearchParameterMap));
		RuntimeResourceDefinition resourceDefinition = myFhirContext.getResourceDefinition(theResource);
		SearchMatchParameters searchMatchParameters = new SearchMatchParameters();
		searchMatchParameters.setSearchParameterMap(theSearchParameterMap);
		searchMatchParameters.setBaseResource(theResource);
		searchMatchParameters.setRuntimeResourceDefinition(resourceDefinition);
		searchMatchParameters.setIndexedSearchParams(resourceIndexedSearchParams);
//		return myInMemoryResourceMatcher.match(
//				theSearchParameterMap, theResource, resourceDefinition, resourceIndexedSearchParams);
		return myInMemoryResourceMatcher.match(searchMatchParameters);
	}

	private ISearchParamExtractor.ISearchParamFilter getFilter(SearchParameterMap searchParameterMap) {
		return theSearchParams -> theSearchParams.stream()
				.filter(runtimeSearchParam -> searchParameterMap.keySet().contains(runtimeSearchParam.getName()))
				.collect(Collectors.toList());
	}
}
