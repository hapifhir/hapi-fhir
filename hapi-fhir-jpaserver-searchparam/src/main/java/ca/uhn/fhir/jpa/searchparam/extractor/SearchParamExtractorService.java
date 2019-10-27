package ca.uhn.fhir.jpa.searchparam.extractor;

/*-
 * #%L
 * HAPI FHIR Search Parameters
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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

import ca.uhn.fhir.jpa.model.entity.*;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;

@Service
@Lazy
public class SearchParamExtractorService {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SearchParamExtractorService.class);

	@Autowired
	private ISearchParamExtractor mySearchParamExtractor;

	public void extractFromResource(ResourceIndexedSearchParams theParams, ResourceTable theEntity, IBaseResource theResource) {
		theParams.myStringParams.addAll(extractSearchParamStrings(theResource));
		theParams.myNumberParams.addAll(extractSearchParamNumber(theResource));
		theParams.myQuantityParams.addAll(extractSearchParamQuantity(theResource));
		theParams.myDateParams.addAll(extractSearchParamDates(theResource));
		theParams.myUriParams.addAll(extractSearchParamUri(theResource));
		theParams.myCoordsParams.addAll(extractSearchParamCoords(theResource));

		ourLog.trace("Storing date indexes: {}", theParams.myDateParams);

		for (BaseResourceIndexedSearchParam next : extractSearchParamTokens(theResource)) {
			if (next instanceof ResourceIndexedSearchParamToken) {
				theParams.myTokenParams.add((ResourceIndexedSearchParamToken) next);
			} else {
				theParams.myStringParams.add((ResourceIndexedSearchParamString) next);
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

	private void populateResourceTable(Collection<? extends BaseResourceIndexedSearchParam> theParams, ResourceTable theResourceTable) {
		for (BaseResourceIndexedSearchParam next : theParams) {
			next.setResource(theResourceTable);
		}
	}

	private Set<ResourceIndexedSearchParamCoords> extractSearchParamCoords(IBaseResource theResource) {
		return mySearchParamExtractor.extractSearchParamCoords(theResource);
	}

	private Set<ResourceIndexedSearchParamDate> extractSearchParamDates(IBaseResource theResource) {
		return mySearchParamExtractor.extractSearchParamDates(theResource);
	}

	private Set<ResourceIndexedSearchParamNumber> extractSearchParamNumber(IBaseResource theResource) {
		return mySearchParamExtractor.extractSearchParamNumber(theResource);
	}

	private Set<ResourceIndexedSearchParamQuantity> extractSearchParamQuantity(IBaseResource theResource) {
		return mySearchParamExtractor.extractSearchParamQuantity(theResource);
	}

	private Set<ResourceIndexedSearchParamString> extractSearchParamStrings(IBaseResource theResource) {
		return mySearchParamExtractor.extractSearchParamStrings(theResource);
	}

	private Set<BaseResourceIndexedSearchParam> extractSearchParamTokens(IBaseResource theResource) {
		return mySearchParamExtractor.extractSearchParamTokens(theResource);
	}

	private Set<ResourceIndexedSearchParamUri> extractSearchParamUri(IBaseResource theResource) {
		return mySearchParamExtractor.extractSearchParamUri(theResource);
	}


}

