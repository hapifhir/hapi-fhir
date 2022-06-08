package ca.uhn.fhir.jpa.searchparam.provider;

/*-
 * #%L
 * HAPI FHIR Search Parameters
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
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.matcher.InMemoryMatchResult;
import ca.uhn.fhir.jpa.searchparam.matcher.SearchParamMatcher;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.provider.HashMapResourceProvider;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class SearchableHashMapResourceProvider<T extends IBaseResource> extends HashMapResourceProvider<T> {
	private final SearchParamMatcher mySearchParamMatcher;

	/**
	 * Constructor
	 *
	 * @param theFhirContext  The FHIR context
	 * @param theResourceType The resource type to support
	 */
	public SearchableHashMapResourceProvider(FhirContext theFhirContext, Class<T> theResourceType, SearchParamMatcher theSearchParamMatcher) {
		super(theFhirContext, theResourceType);
		mySearchParamMatcher = theSearchParamMatcher;
	}

	public List<IBaseResource> searchByCriteria(String theCriteria, RequestDetails theRequest) {
		return searchBy(resource -> mySearchParamMatcher.match(theCriteria, resource, theRequest), theRequest);

	}

	public List<IBaseResource> searchByParams(SearchParameterMap theSearchParams, RequestDetails theRequest) {
		return searchBy(resource -> mySearchParamMatcher.match(theSearchParams.toNormalizedQueryString(getFhirContext()), resource, theRequest), theRequest);
	}

	private List<IBaseResource> searchBy(Function<IBaseResource, InMemoryMatchResult> theMatcher, RequestDetails theRequest) {
		mySearchCount.incrementAndGet();
		List<T> allEResources = getAllResources();

		List<T> matches = new ArrayList<>();
		for (T resource : allEResources) {
			InMemoryMatchResult result = theMatcher.apply(resource);
			if (!result.supported()) {
				throw new InvalidRequestException(Msg.code(502) + "Search not supported by in-memory matcher: "+result.getUnsupportedReason());
			}
			if (result.matched()) {
				matches.add(resource);
			}
		}
		return fireInterceptorsAndFilterAsNeeded(matches, theRequest);
	}
}
