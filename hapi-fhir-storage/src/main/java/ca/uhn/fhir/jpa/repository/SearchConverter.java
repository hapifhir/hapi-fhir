/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.repository;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.api.IQueryParameterAnd;
import ca.uhn.fhir.model.api.IQueryParameterOr;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import jakarta.annotation.Nonnull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The IGenericClient API represents searches with OrLists, while the FhirRepository API uses nested
 * lists. This class (will eventually) convert between them
 */
public class SearchConverter {
	// hardcoded list from FHIR specs: https://www.hl7.org/fhir/search.html
	private final List<String> mySearchResultParameters = Arrays.asList(
			"_sort",
			"_count",
			"_include",
			"_revinclude",
			"_summary",
			"_total",
			"_elements",
			"_contained",
			"_containedType");
	public final Multimap<String, List<IQueryParameterType>> mySeparatedSearchParameters = ArrayListMultimap.create();
	public final Multimap<String, List<IQueryParameterType>> mySeparatedResultParameters = ArrayListMultimap.create();
	public final SearchParameterMap mySearchParameterMap = new SearchParameterMap();
	public final Map<String, String[]> myResultParameters = new HashMap<>();

	public void convertParameters(
			Multimap<String, List<IQueryParameterType>> theParameters, FhirContext theFhirContext) {
		if (theParameters == null) {
			return;
		}
		separateParameterTypes(theParameters);
		convertToSearchParameterMap(mySeparatedSearchParameters);
		convertToStringMap(mySeparatedResultParameters, theFhirContext);
	}

	public void convertToStringMap(
			@Nonnull Multimap<String, List<IQueryParameterType>> theParameters, @Nonnull FhirContext theFhirContext) {
		for (Map.Entry<String, List<IQueryParameterType>> entry : theParameters.entries()) {
			String[] values = new String[entry.getValue().size()];
			for (int i = 0; i < entry.getValue().size(); i++) {
				values[i] = entry.getValue().get(i).getValueAsQueryToken(theFhirContext);
			}
			myResultParameters.put(entry.getKey(), values);
		}
	}

	public void convertToSearchParameterMap(Multimap<String, List<IQueryParameterType>> theSearchMap) {
		if (theSearchMap == null) {
			return;
		}
		for (Map.Entry<String, List<IQueryParameterType>> entry : theSearchMap.entries()) {
			// if list of parameters is the value
			if (entry.getValue().size() > 1 && !isOrList(entry.getValue()) && !isAndList(entry.getValue())) {
				// is value a TokenParam
				addTokenToSearchIfNeeded(entry);

				// parameter type is single value list
			} else {
				for (IQueryParameterType value : entry.getValue()) {
					setParameterTypeValue(entry.getKey(), value);
				}
			}
		}
	}

	private void addTokenToSearchIfNeeded(Map.Entry<String, List<IQueryParameterType>> theEntry) {
		if (isTokenParam(theEntry.getValue().get(0))) {
			String tokenKey = theEntry.getKey();
			TokenOrListParam tokenList = new TokenOrListParam();
			for (IQueryParameterType rec : theEntry.getValue()) {
				tokenList.add((TokenParam) rec);
			}
			mySearchParameterMap.add(tokenKey, tokenList);
		}
	}

	public <T> void setParameterTypeValue(@Nonnull String theKey, @Nonnull T theParameterType) {
		if (isOrList(theParameterType)) {
			mySearchParameterMap.add(theKey, (IQueryParameterOr<?>) theParameterType);
		} else if (isAndList(theParameterType)) {
			mySearchParameterMap.add(theKey, (IQueryParameterAnd<?>) theParameterType);
		} else {
			mySearchParameterMap.add(theKey, (IQueryParameterType) theParameterType);
		}
	}

	public void separateParameterTypes(@Nonnull Multimap<String, List<IQueryParameterType>> theParameters) {
		for (Map.Entry<String, List<IQueryParameterType>> entry : theParameters.entries()) {
			if (isSearchResultParameter(entry.getKey())) {
				mySeparatedResultParameters.put(entry.getKey(), entry.getValue());
			} else {
				mySeparatedSearchParameters.put(entry.getKey(), entry.getValue());
			}
		}
	}

	public boolean isSearchResultParameter(String theParameterName) {
		return mySearchResultParameters.contains(theParameterName);
	}

	public <T> boolean isOrList(@Nonnull T theParameterType) {
		return IQueryParameterOr.class.isAssignableFrom(theParameterType.getClass());
	}

	public <T> boolean isAndList(@Nonnull T theParameterType) {
		return IQueryParameterAnd.class.isAssignableFrom(theParameterType.getClass());
	}

	public <T> boolean isTokenParam(@Nonnull T theParameterType) {
		return TokenParam.class.isAssignableFrom(theParameterType.getClass());
	}
}
