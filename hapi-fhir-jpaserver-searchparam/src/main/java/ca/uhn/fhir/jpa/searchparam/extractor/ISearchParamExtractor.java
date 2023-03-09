package ca.uhn.fhir.jpa.searchparam.extractor;


/*
 * #%L
 * HAPI FHIR Search Parameters
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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

import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.jpa.model.entity.BaseResourceIndexedSearchParam;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedComboStringUnique;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedComboTokenNonUnique;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamDate;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamNumber;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamQuantity;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamQuantityNormalized;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamString;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamToken;
import ca.uhn.fhir.jpa.model.entity.ResourceIndexedSearchParamUri;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public interface ISearchParamExtractor {

	/**
	 * Constant for the {@literal theParamsToIndex} parameters on this interface
	 * indicating that all search parameters should be indexed.
	 */
	Set<String> ALL_PARAMS = Set.of("*");

	default SearchParamSet<ResourceIndexedSearchParamDate> extractSearchParamDates(IBaseResource theResource) {
		return extractSearchParamDates(theResource, ALL_PARAMS);
	}

	SearchParamSet<ResourceIndexedSearchParamDate> extractSearchParamDates(IBaseResource theResource, Set<String> theParamsToIndex);

	default SearchParamSet<ResourceIndexedSearchParamNumber> extractSearchParamNumber(IBaseResource theResource) {
		return extractSearchParamNumber(theResource, ALL_PARAMS);
	}

	SearchParamSet<ResourceIndexedSearchParamNumber> extractSearchParamNumber(IBaseResource theResource, Set<String> theParamsToIndex);

	default SearchParamSet<ResourceIndexedSearchParamQuantity> extractSearchParamQuantity(IBaseResource theResource) {
		return extractSearchParamQuantity(theResource, ALL_PARAMS);
	}

	SearchParamSet<ResourceIndexedSearchParamQuantity> extractSearchParamQuantity(IBaseResource theResource, Set<String> theParamsToIndex);

	default SearchParamSet<ResourceIndexedSearchParamQuantityNormalized> extractSearchParamQuantityNormalized(IBaseResource theResource) {
		return extractSearchParamQuantityNormalized(theResource, ALL_PARAMS);
	}

	SearchParamSet<ResourceIndexedSearchParamQuantityNormalized> extractSearchParamQuantityNormalized(IBaseResource theResource, Set<String> theParamsToIndex);

	default SearchParamSet<ResourceIndexedSearchParamString> extractSearchParamStrings(IBaseResource theResource) {
		return extractSearchParamStrings(theResource, ALL_PARAMS);
	}

	SearchParamSet<ResourceIndexedSearchParamString> extractSearchParamStrings(IBaseResource theResource, Set<String> theParamsToIndex);

	default SearchParamSet<ResourceIndexedSearchParamComposite> extractSearchParamComposites(IBaseResource theResource) {
		return extractSearchParamComposites(theResource, ALL_PARAMS);
	}

	SearchParamSet<ResourceIndexedSearchParamComposite> extractSearchParamComposites(IBaseResource theResource, Set<String> theParamsToIndex);
	default SearchParamSet<BaseResourceIndexedSearchParam> extractSearchParamTokens(IBaseResource theResource) {
		return extractSearchParamTokens(theResource, ALL_PARAMS);
	}

	SearchParamSet<BaseResourceIndexedSearchParam> extractSearchParamTokens(IBaseResource theResource, Set<String> theParamsToIndex);

	SearchParamSet<BaseResourceIndexedSearchParam> extractSearchParamTokens(IBaseResource theResource, RuntimeSearchParam theSearchParam);

	SearchParamSet<BaseResourceIndexedSearchParam> extractSearchParamSpecial(IBaseResource theResource, Set<String> theParamsToIndex);
	SearchParamSet<ResourceIndexedComboStringUnique> extractSearchParamComboUnique(String theResourceType, ResourceIndexedSearchParams theParams);

	SearchParamSet<ResourceIndexedComboTokenNonUnique> extractSearchParamComboNonUnique(String theResourceType, ResourceIndexedSearchParams theParams);

	default SearchParamSet<ResourceIndexedSearchParamUri> extractSearchParamUri(IBaseResource theResource) {
		return extractSearchParamUri(theResource, ALL_PARAMS);
	}


	SearchParamSet<ResourceIndexedSearchParamUri> extractSearchParamUri(IBaseResource theResource, Set<String> theParamsToIndex);

	SearchParamSet<PathAndRef> extractResourceLinks(IBaseResource theResource, boolean theWantLocalReferences);

	String[] split(String theExpression);

	List<String> extractParamValuesAsStrings(RuntimeSearchParam theActiveSearchParam, IBaseResource theResource);

	List<IBase> extractValues(String thePaths, IBase theResource);

	String toRootTypeName(IBase nextObject);

	String toTypeName(IBase nextObject);

	PathAndRef extractReferenceLinkFromResource(IBase theValue, String thePath);

	Date extractDateFromResource(IBase theValue, String thePath);

	ResourceIndexedSearchParamToken createSearchParamForCoding(String theResourceType, RuntimeSearchParam theSearchParam, IBase theValue);

	String getDisplayTextForCoding(IBase theValue);

	BaseSearchParamExtractor.IValueExtractor getPathValueExtractor(IBase theResource, String theSinglePath);

	List<IBase> getCodingsFromCodeableConcept(IBase theValue);

	String getDisplayTextFromCodeableConcept(IBase theValue);

	class SearchParamSet<T> extends HashSet<T> {

		private List<String> myWarnings;

		public void addWarning(String theWarning) {
			if (myWarnings == null) {
				myWarnings = new ArrayList<>();
			}
			myWarnings.add(theWarning);
		}

		List<String> getWarnings() {
			if (myWarnings == null) {
				return Collections.emptyList();
			}
			return myWarnings;
		}

	}


}
