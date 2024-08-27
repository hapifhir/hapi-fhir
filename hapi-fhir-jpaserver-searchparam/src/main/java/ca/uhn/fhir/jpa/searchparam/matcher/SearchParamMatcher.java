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
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class SearchParamMatcher {
	@Autowired
	private FhirContext myFhirContext;

	@Autowired
	private IndexedSearchParamExtractor myIndexedSearchParamExtractor;

	@Autowired
	private InMemoryResourceMatcher myInMemoryResourceMatcher;

	public InMemoryMatchResult match(String theCriteria, IBaseResource theResource, RequestDetails theRequest) {
		return myInMemoryResourceMatcher.match(theCriteria, theResource, null, theRequest);
	}

	public InMemoryMatchResult match(SearchParameterMap theSearchParameterMap, IBaseResource theResource) {
		if (theSearchParameterMap.isEmpty()) {
			return InMemoryMatchResult.successfulMatch();
		}
		ResourceIndexedSearchParams resourceIndexedSearchParams =
				myIndexedSearchParamExtractor.extractIndexedSearchParams(
						theResource, null, getFilter(theSearchParameterMap));
		RuntimeResourceDefinition resourceDefinition = myFhirContext.getResourceDefinition(theResource);
		return myInMemoryResourceMatcher.match(
				theSearchParameterMap, theResource, resourceDefinition, resourceIndexedSearchParams);
	}

	private ISearchParamExtractor.ISearchParamFilter getFilter(SearchParameterMap searchParameterMap) {
		return theSearchParams -> theSearchParams.stream()
				.filter(runtimeSearchParam -> searchParameterMap.keySet().contains(runtimeSearchParam.getName()))
				.collect(Collectors.toList());
	}
}
