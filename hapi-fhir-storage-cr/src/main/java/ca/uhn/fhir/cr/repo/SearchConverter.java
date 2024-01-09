package ca.uhn.fhir.cr.repo;

/*-
 * #%L
 * HAPI FHIR - Clinical Reasoning
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.api.IQueryParameterAnd;
import ca.uhn.fhir.model.api.IQueryParameterOr;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
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
	private final List<String> searchResultParameters = Arrays.asList(
			"_sort",
			"_count",
			"_include",
			"_revinclude",
			"_summary",
			"_total",
			"_elements",
			"_contained",
			"_containedType");
	public final Map<String, List<IQueryParameterType>> separatedSearchParameters = new HashMap<>();
	public final Map<String, List<IQueryParameterType>> separatedResultParameters = new HashMap<>();
	public final SearchParameterMap searchParameterMap = new SearchParameterMap();
	public final Map<String, String[]> resultParameters = new HashMap<>();

	public void convertParameters(Map<String, List<IQueryParameterType>> theParameters, FhirContext theFhirContext) {
		if (theParameters == null) {
			return;
		}
		separateParameterTypes(theParameters);
		convertToSearchParameterMap(separatedSearchParameters);
		convertToStringMap(separatedResultParameters, theFhirContext);
	}

	public void convertToStringMap(
			@Nonnull Map<String, List<IQueryParameterType>> theParameters, @Nonnull FhirContext theFhirContext) {
		for (var entry : theParameters.entrySet()) {
			String[] values = new String[entry.getValue().size()];
			for (int i = 0; i < entry.getValue().size(); i++) {
				values[i] = entry.getValue().get(i).getValueAsQueryToken(theFhirContext);
			}
			resultParameters.put(entry.getKey(), values);
		}
	}

	public void convertToSearchParameterMap(Map<String, List<IQueryParameterType>> theSearchMap) {
		if (theSearchMap == null) {
			return;
		}
		for (var entry : theSearchMap.entrySet()) {
			// if list of parameters is the value
			if (entry.getValue().size() > 1 && !isOrList(entry.getValue()) && !isAndList(entry.getValue())) {
				// is value a TokenParam
				if (isTokenParam(entry.getValue().get(0))) {
					var tokenKey = entry.getKey();
					var tokenList = new TokenOrListParam();
					for (IQueryParameterType rec : entry.getValue()) {
						tokenList.add((TokenParam) rec);
					}
					searchParameterMap.add(tokenKey, tokenList);
				}

				// parameter type is single value list
			} else {
				for (IQueryParameterType value : entry.getValue()) {
					setParameterTypeValue(entry.getKey(), value);
				}
			}
		}
	}

	public <T> void setParameterTypeValue(@Nonnull String theKey, @Nonnull T theParameterType) {
		if (isOrList(theParameterType)) {
			searchParameterMap.add(theKey, (IQueryParameterOr<?>) theParameterType);
		} else if (isAndList(theParameterType)) {
			searchParameterMap.add(theKey, (IQueryParameterAnd<?>) theParameterType);
		} else {
			searchParameterMap.add(theKey, (IQueryParameterType) theParameterType);
		}
	}

	public void separateParameterTypes(@Nonnull Map<String, List<IQueryParameterType>> theParameters) {
		for (var entry : theParameters.entrySet()) {
			if (isSearchResultParameter(entry.getKey())) {
				separatedResultParameters.put(entry.getKey(), entry.getValue());
			} else {
				separatedSearchParameters.put(entry.getKey(), entry.getValue());
			}
		}
	}

	public boolean isSearchResultParameter(String theParameterName) {
		return searchResultParameters.contains(theParameterName);
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
