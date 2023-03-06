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

public interface ISearchParamExtractor {
	ISearchParamInclusionChecker ALWAYS_TRUE = n -> true;

	default SearchParamSet<ResourceIndexedSearchParamDate> extractSearchParamDates(IBaseResource theResource) {
		return extractSearchParamDates(theResource, ALWAYS_TRUE);
	}

	SearchParamSet<ResourceIndexedSearchParamDate> extractSearchParamDates(IBaseResource theResource, ISearchParamInclusionChecker theInclusionChecker);

	default SearchParamSet<ResourceIndexedSearchParamNumber> extractSearchParamNumber(IBaseResource theResource) {
		return extractSearchParamNumber(theResource, ALWAYS_TRUE);
	}

	SearchParamSet<ResourceIndexedSearchParamNumber> extractSearchParamNumber(IBaseResource theResource, ISearchParamInclusionChecker theInclusionChecker);

	default SearchParamSet<ResourceIndexedSearchParamQuantity> extractSearchParamQuantity(IBaseResource theResource) {
		return extractSearchParamQuantity(theResource, ALWAYS_TRUE);
	}

	SearchParamSet<ResourceIndexedSearchParamQuantity> extractSearchParamQuantity(IBaseResource theResource, ISearchParamInclusionChecker theInclusionChecker);

	default SearchParamSet<ResourceIndexedSearchParamQuantityNormalized> extractSearchParamQuantityNormalized(IBaseResource theResource) {
		return extractSearchParamQuantityNormalized(theResource, ALWAYS_TRUE);
	}

	SearchParamSet<ResourceIndexedSearchParamQuantityNormalized> extractSearchParamQuantityNormalized(IBaseResource theResource, ISearchParamInclusionChecker theInclusionChecker);

	default SearchParamSet<ResourceIndexedSearchParamString> extractSearchParamStrings(IBaseResource theResource) {
		return extractSearchParamStrings(theResource, ALWAYS_TRUE);
	}

	SearchParamSet<ResourceIndexedSearchParamString> extractSearchParamStrings(IBaseResource theResource, ISearchParamInclusionChecker theInclusionChecker);

	default SearchParamSet<ResourceIndexedSearchParamComposite> extractSearchParamComposites(IBaseResource theResource) {
		return extractSearchParamComposites(theResource, ALWAYS_TRUE);
	}

	SearchParamSet<ResourceIndexedSearchParamComposite> extractSearchParamComposites(IBaseResource theResource, ISearchParamInclusionChecker theInclusionChecker);
	default SearchParamSet<BaseResourceIndexedSearchParam> extractSearchParamTokens(IBaseResource theResource) {
		return extractSearchParamTokens(theResource, ALWAYS_TRUE);
	}

	SearchParamSet<BaseResourceIndexedSearchParam> extractSearchParamTokens(IBaseResource theResource, ISearchParamInclusionChecker theInclusionChecker);

	SearchParamSet<BaseResourceIndexedSearchParam> extractSearchParamTokens(IBaseResource theResource, RuntimeSearchParam theSearchParam);

	default SearchParamSet<BaseResourceIndexedSearchParam> extractSearchParamSpecial(IBaseResource theResource) {
		return extractSearchParamSpecial(theResource, ALWAYS_TRUE);
	}

	SearchParamSet<BaseResourceIndexedSearchParam> extractSearchParamSpecial(IBaseResource theResource, ISearchParamInclusionChecker theInclusionChecker);
	SearchParamSet<ResourceIndexedComboStringUnique> extractSearchParamComboUnique(String theResourceType, ResourceIndexedSearchParams theParams);

	SearchParamSet<ResourceIndexedComboTokenNonUnique> extractSearchParamComboNonUnique(String theResourceType, ResourceIndexedSearchParams theParams);

	default SearchParamSet<ResourceIndexedSearchParamUri> extractSearchParamUri(IBaseResource theResource) {
		return extractSearchParamUri(theResource, ALWAYS_TRUE);
	}


	SearchParamSet<ResourceIndexedSearchParamUri> extractSearchParamUri(IBaseResource theResource, ISearchParamInclusionChecker theInclusionChecker);
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


	/**
	 * This interface can optionally be passed to extraction methods such as
	 * {@link #extractSearchParamTokens(IBaseResource, ISearchParamInclusionChecker)}.
	 * It is then queried in order to evaluate which search parameters to actually
	 * extract.
	 *
	 * @since 6.6.0
	 */
	interface ISearchParamInclusionChecker {

		/**
		 * @param theSearchParameterName The name of the search parameter, e.g. "subject" or "family"
		 * @return Returns {@literal true} if the search parameter should be indexed
		 */
		boolean applies(String theSearchParameterName);

	}


}
