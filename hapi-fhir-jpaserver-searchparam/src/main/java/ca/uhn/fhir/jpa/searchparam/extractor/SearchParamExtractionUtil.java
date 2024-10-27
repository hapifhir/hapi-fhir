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
package ca.uhn.fhir.jpa.searchparam.extractor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.jpa.model.entity.BaseResourceIndexedSearchParam;
import ca.uhn.fhir.jpa.model.entity.NormalizedQuantitySearchLevel;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamCoords;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamDate;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamNumber;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamQuantity;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamQuantityNormalized;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamString;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamToken;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamUri;
import ca.uhn.fhir.jpa.model.entity.StorageSettings;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.instance.model.api.IBaseResource;

import static ca.uhn.fhir.jpa.searchparam.extractor.SearchParamExtractorService.handleWarnings;

public class SearchParamExtractionUtil {

	private final FhirContext myFhirContext;
	private final StorageSettings myStorageSettings;
	private final ISearchParamExtractor mySearchParamExtractor;
	private final IInterceptorBroadcaster myInterceptorBroadcaster;

	public SearchParamExtractionUtil(
			FhirContext theFhirContext,
			StorageSettings theStorageSettings,
			ISearchParamExtractor theSearchParamExtractor,
			IInterceptorBroadcaster theInterceptorBroadcaster) {
		myFhirContext = theFhirContext;
		myStorageSettings = theStorageSettings;
		mySearchParamExtractor = theSearchParamExtractor;
		myInterceptorBroadcaster = theInterceptorBroadcaster;
	}

	public void extractSearchIndexParameters(
			RequestDetails theRequestDetails,
			ResourceIndexedSearchParams theParams,
			IBaseResource theResource,
			@Nonnull ISearchParamExtractor.ISearchParamFilter theSearchParamFilter) {

		// Strings
		ISearchParamExtractor.SearchParamSet<ResourceIndexedSearchParamString> strings =
				extractSearchParamStrings(theResource, theSearchParamFilter);
		handleWarnings(theRequestDetails, myInterceptorBroadcaster, strings);
		theParams.myStringParams.addAll(strings);

		// Numbers
		ISearchParamExtractor.SearchParamSet<ResourceIndexedSearchParamNumber> numbers =
				extractSearchParamNumber(theResource, theSearchParamFilter);
		handleWarnings(theRequestDetails, myInterceptorBroadcaster, numbers);
		theParams.myNumberParams.addAll(numbers);

		// Quantities
		ISearchParamExtractor.SearchParamSet<ResourceIndexedSearchParamQuantity> quantities =
				extractSearchParamQuantity(theResource, theSearchParamFilter);
		handleWarnings(theRequestDetails, myInterceptorBroadcaster, quantities);
		theParams.myQuantityParams.addAll(quantities);

		if (myStorageSettings
						.getNormalizedQuantitySearchLevel()
						.equals(NormalizedQuantitySearchLevel.NORMALIZED_QUANTITY_STORAGE_SUPPORTED)
				|| myStorageSettings
						.getNormalizedQuantitySearchLevel()
						.equals(NormalizedQuantitySearchLevel.NORMALIZED_QUANTITY_SEARCH_SUPPORTED)) {
			ISearchParamExtractor.SearchParamSet<ResourceIndexedSearchParamQuantityNormalized> quantitiesNormalized =
					extractSearchParamQuantityNormalized(theResource, theSearchParamFilter);
			handleWarnings(theRequestDetails, myInterceptorBroadcaster, quantitiesNormalized);
			theParams.myQuantityNormalizedParams.addAll(quantitiesNormalized);
		}

		// Dates
		ISearchParamExtractor.SearchParamSet<ResourceIndexedSearchParamDate> dates =
				extractSearchParamDates(theResource, theSearchParamFilter);
		handleWarnings(theRequestDetails, myInterceptorBroadcaster, dates);
		theParams.myDateParams.addAll(dates);

		// URIs
		ISearchParamExtractor.SearchParamSet<ResourceIndexedSearchParamUri> uris =
				extractSearchParamUri(theResource, theSearchParamFilter);
		handleWarnings(theRequestDetails, myInterceptorBroadcaster, uris);
		theParams.myUriParams.addAll(uris);

		// Tokens (can result in both Token and String, as we index the display name for
		// the types: Coding, CodeableConcept)
		ISearchParamExtractor.SearchParamSet<BaseResourceIndexedSearchParam> tokens =
				extractSearchParamTokens(theResource, theSearchParamFilter);
		for (BaseResourceIndexedSearchParam next : tokens) {
			if (next instanceof ResourceIndexedSearchParamToken) {
				theParams.myTokenParams.add((ResourceIndexedSearchParamToken) next);
			} else if (next instanceof ResourceIndexedSearchParamCoords) {
				theParams.myCoordsParams.add((ResourceIndexedSearchParamCoords) next);
			} else {
				theParams.myStringParams.add((ResourceIndexedSearchParamString) next);
			}
		}

		// Composites
		// dst2 composites use stuff like value[x] , and we don't support them.
		if (myFhirContext.getVersion().getVersion().isEqualOrNewerThan(FhirVersionEnum.DSTU3)) {
			ISearchParamExtractor.SearchParamSet<ResourceIndexedSearchParamComposite> composites =
					extractSearchParamComposites(theResource, theSearchParamFilter);
			handleWarnings(theRequestDetails, myInterceptorBroadcaster, composites);
			theParams.myCompositeParams.addAll(composites);
		}

		// Specials
		ISearchParamExtractor.SearchParamSet<BaseResourceIndexedSearchParam> specials =
				extractSearchParamSpecial(theResource, theSearchParamFilter);
		for (BaseResourceIndexedSearchParam next : specials) {
			if (next instanceof ResourceIndexedSearchParamCoords) {
				theParams.myCoordsParams.add((ResourceIndexedSearchParamCoords) next);
			}
		}
	}

	private ISearchParamExtractor.SearchParamSet<ResourceIndexedSearchParamDate> extractSearchParamDates(
			IBaseResource theResource, ISearchParamExtractor.ISearchParamFilter theSearchParamFilter) {
		return mySearchParamExtractor.extractSearchParamDates(theResource, theSearchParamFilter);
	}

	private ISearchParamExtractor.SearchParamSet<ResourceIndexedSearchParamNumber> extractSearchParamNumber(
			IBaseResource theResource, ISearchParamExtractor.ISearchParamFilter theSearchParamFilter) {
		return mySearchParamExtractor.extractSearchParamNumber(theResource, theSearchParamFilter);
	}

	private ISearchParamExtractor.SearchParamSet<ResourceIndexedSearchParamQuantity> extractSearchParamQuantity(
			IBaseResource theResource, ISearchParamExtractor.ISearchParamFilter theSearchParamFilter) {
		return mySearchParamExtractor.extractSearchParamQuantity(theResource, theSearchParamFilter);
	}

	private ISearchParamExtractor.SearchParamSet<ResourceIndexedSearchParamQuantityNormalized>
			extractSearchParamQuantityNormalized(
					IBaseResource theResource, ISearchParamExtractor.ISearchParamFilter theSearchParamFilter) {
		return mySearchParamExtractor.extractSearchParamQuantityNormalized(theResource, theSearchParamFilter);
	}

	private ISearchParamExtractor.SearchParamSet<ResourceIndexedSearchParamString> extractSearchParamStrings(
			IBaseResource theResource, ISearchParamExtractor.ISearchParamFilter theSearchParamFilter) {
		return mySearchParamExtractor.extractSearchParamStrings(theResource, theSearchParamFilter);
	}

	private ISearchParamExtractor.SearchParamSet<BaseResourceIndexedSearchParam> extractSearchParamTokens(
			IBaseResource theResource, ISearchParamExtractor.ISearchParamFilter theSearchParamFilter) {
		return mySearchParamExtractor.extractSearchParamTokens(theResource, theSearchParamFilter);
	}

	private ISearchParamExtractor.SearchParamSet<BaseResourceIndexedSearchParam> extractSearchParamSpecial(
			IBaseResource theResource, ISearchParamExtractor.ISearchParamFilter theSearchParamFilter) {
		return mySearchParamExtractor.extractSearchParamSpecial(theResource, theSearchParamFilter);
	}

	private ISearchParamExtractor.SearchParamSet<ResourceIndexedSearchParamUri> extractSearchParamUri(
			IBaseResource theResource, ISearchParamExtractor.ISearchParamFilter theSearchParamFilter) {
		return mySearchParamExtractor.extractSearchParamUri(theResource, theSearchParamFilter);
	}

	private ISearchParamExtractor.SearchParamSet<ResourceIndexedSearchParamComposite> extractSearchParamComposites(
			IBaseResource theResource, ISearchParamExtractor.ISearchParamFilter theSearchParamFilter) {
		return mySearchParamExtractor.extractSearchParamComposites(theResource, theSearchParamFilter);
	}
}
