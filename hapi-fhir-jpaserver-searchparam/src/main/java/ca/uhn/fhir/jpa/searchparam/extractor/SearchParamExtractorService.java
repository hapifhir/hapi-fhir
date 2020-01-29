package ca.uhn.fhir.jpa.searchparam.extractor;

/*-
 * #%L
 * HAPI FHIR Search Parameters
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

import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.model.entity.*;
import ca.uhn.fhir.jpa.model.search.StorageProcessingMessage;
import ca.uhn.fhir.jpa.util.JpaInterceptorBroadcaster;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import com.google.common.annotations.VisibleForTesting;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@Lazy
public class SearchParamExtractorService {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SearchParamExtractorService.class);

	@Autowired
	private ISearchParamExtractor mySearchParamExtractor;
	@Autowired
	private IInterceptorBroadcaster myInterceptorBroadcaster;

	public void extractFromResource(RequestDetails theRequestDetails, ResourceIndexedSearchParams theParams, ResourceTable theEntity, IBaseResource theResource) {
		ISearchParamExtractor.SearchParamSet<ResourceIndexedSearchParamString> strings = extractSearchParamStrings(theResource);
		handleWarnings(theRequestDetails, myInterceptorBroadcaster, strings);
		theParams.myStringParams.addAll(strings);

		ISearchParamExtractor.SearchParamSet<ResourceIndexedSearchParamNumber> numbers = extractSearchParamNumber(theResource);
		handleWarnings(theRequestDetails, myInterceptorBroadcaster, numbers);
		theParams.myNumberParams.addAll(numbers);

		ISearchParamExtractor.SearchParamSet<ResourceIndexedSearchParamQuantity> quantities = extractSearchParamQuantity(theResource);
		handleWarnings(theRequestDetails, myInterceptorBroadcaster, quantities);
		theParams.myQuantityParams.addAll(quantities);

		ISearchParamExtractor.SearchParamSet<ResourceIndexedSearchParamDate> dates = extractSearchParamDates(theResource);
		handleWarnings(theRequestDetails, myInterceptorBroadcaster, dates);
		theParams.myDateParams.addAll(dates);

		ISearchParamExtractor.SearchParamSet<ResourceIndexedSearchParamUri> uris = extractSearchParamUri(theResource);
		handleWarnings(theRequestDetails, myInterceptorBroadcaster, uris);
		theParams.myUriParams.addAll(uris);

		ourLog.trace("Storing date indexes: {}", theParams.myDateParams);

		for (BaseResourceIndexedSearchParam next : extractSearchParamTokens(theResource)) {
			if (next instanceof ResourceIndexedSearchParamToken) {
				theParams.myTokenParams.add((ResourceIndexedSearchParamToken) next);
			} else if (next instanceof ResourceIndexedSearchParamCoords) {
				theParams.myCoordsParams.add((ResourceIndexedSearchParamCoords) next);
			} else {
				theParams.myStringParams.add((ResourceIndexedSearchParamString) next);
			}
		}

		for (BaseResourceIndexedSearchParam next : extractSearchParamSpecial(theResource)) {
			if (next instanceof ResourceIndexedSearchParamCoords) {
				theParams.myCoordsParams.add((ResourceIndexedSearchParamCoords) next);
			}
		}

		populateResourceTable(theParams.myStringParams, theEntity);
		populateResourceTable(theParams.myNumberParams, theEntity);
		populateResourceTable(theParams.myQuantityParams, theEntity);
		populateResourceTable(theParams.myDateParams, theEntity);
		populateResourceTable(theParams.myUriParams, theEntity);
		populateResourceTable(theParams.myCoordsParams, theEntity);
		populateResourceTable(theParams.myTokenParams, theEntity);
	}

	static void handleWarnings(RequestDetails theRequestDetails, IInterceptorBroadcaster theInterceptorBroadcaster, ISearchParamExtractor.SearchParamSet<?> theSearchParamSet) {
		if (theSearchParamSet.getWarnings().isEmpty()) {
			return;
		}

		// If extraction generated any warnings, broadcast an error
		for (String next : theSearchParamSet.getWarnings()) {
			StorageProcessingMessage messageHolder = new StorageProcessingMessage();
			messageHolder.setMessage(next);
			HookParams params = new HookParams()
				.add(RequestDetails.class, theRequestDetails)
				.addIfMatchesType(ServletRequestDetails.class, theRequestDetails)
				.add(StorageProcessingMessage.class, messageHolder);
			JpaInterceptorBroadcaster.doCallHooks(theInterceptorBroadcaster, theRequestDetails, Pointcut.JPA_PERFTRACE_WARNING, params);
		}
	}

	private void populateResourceTable(Collection<? extends BaseResourceIndexedSearchParam> theParams, ResourceTable theResourceTable) {
		for (BaseResourceIndexedSearchParam next : theParams) {
			next.setResource(theResourceTable);
		}
	}

	private ISearchParamExtractor.SearchParamSet<ResourceIndexedSearchParamDate> extractSearchParamDates(IBaseResource theResource) {
		return mySearchParamExtractor.extractSearchParamDates(theResource);
	}

	private ISearchParamExtractor.SearchParamSet<ResourceIndexedSearchParamNumber> extractSearchParamNumber(IBaseResource theResource) {
		return mySearchParamExtractor.extractSearchParamNumber(theResource);
	}

	private ISearchParamExtractor.SearchParamSet<ResourceIndexedSearchParamQuantity> extractSearchParamQuantity(IBaseResource theResource) {
		return mySearchParamExtractor.extractSearchParamQuantity(theResource);
	}

	private ISearchParamExtractor.SearchParamSet<ResourceIndexedSearchParamString> extractSearchParamStrings(IBaseResource theResource) {
		return mySearchParamExtractor.extractSearchParamStrings(theResource);
	}

	private ISearchParamExtractor.SearchParamSet<BaseResourceIndexedSearchParam> extractSearchParamTokens(IBaseResource theResource) {
		return mySearchParamExtractor.extractSearchParamTokens(theResource);
	}

	private ISearchParamExtractor.SearchParamSet<BaseResourceIndexedSearchParam> extractSearchParamSpecial(IBaseResource theResource) {
		return mySearchParamExtractor.extractSearchParamSpecial(theResource);
	}

	private ISearchParamExtractor.SearchParamSet<ResourceIndexedSearchParamUri> extractSearchParamUri(IBaseResource theResource) {
		return mySearchParamExtractor.extractSearchParamUri(theResource);
	}

	@VisibleForTesting
	void setInterceptorBroadcasterForUnitTest(IInterceptorBroadcaster theJpaInterceptorBroadcaster) {
		myInterceptorBroadcaster = theJpaInterceptorBroadcaster;
	}
}

