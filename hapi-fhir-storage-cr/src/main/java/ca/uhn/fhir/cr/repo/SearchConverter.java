package ca.uhn.fhir.cr.repo;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.hl7.fhir.instance.model.api.IBaseResource;

/*-
 * #%L
 * HAPI FHIR - Clinical Reasoning
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

import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.api.IQueryParameterType;

/**
 * The IGenericClient API represents searches with OrLists, while the FhirRepository API uses nested
 * lists. This class (will eventually) convert between them
 */
class SearchConverter {
	/**
	 * hardcoded from FHIR specs: https://www.hl7.org/fhir/search.html
	 */
	private final List<String> searchResultParameters = Arrays.asList("_sort", "_count", "_include",
			"_revinclude", "_summary", "_total", "_elements", "_contained", "_containedType");

	private final Map<String, List<IQueryParameterType>> separatedSearchParameters = new HashMap<>();
	private final Map<String, List<IQueryParameterType>> separatedResultParameters = new HashMap<>();
	SearchParameterMap searchParameterMap;
	Map<String, String[]> resultParameters;
	IBaseResource searchParameters;

	void convertParameters(Map<String, List<IQueryParameterType>> theParameters) {
		this.separateParameterTypes(theParameters);
		this.searchParameterMap = this.convertToParameterMap(this.separatedSearchParameters);
		this.resultParameters = this.getResultParameters(this.separatedResultParameters);
	}

	private Map<String, String[]> getResultParameters(
			Map<String, List<IQueryParameterType>> theParameters) {
		Map<String, String[]> result = new HashMap<>();
		for (var entry : theParameters.entrySet()) {
			String[] values = entry.getValue().stream().map(Objects::toString).toArray(String[]::new);
			result.put(entry.getKey(), values);
		}
		return result;
	}

	// private IBaseResource getSearchParameters(Map<String, List<IQueryParameterType>>
	// theParameters) {
	// // TODO
	// }

	private SearchParameterMap convertToParameterMap(
			Map<String, List<IQueryParameterType>> theSearchMap) {
		var converted = new SearchParameterMap();
		if (theSearchMap == null) {
			return converted;
		}
		// TODO: This logic is known to be bad. Just prototyping some stuff...
		for (var entry : theSearchMap.entrySet()) {
			for (IQueryParameterType value : entry.getValue()) {
				this.searchParameterMap.add(entry.getKey(), value);
			}
		}
	}

	public void separateParameterTypes(
			@Nonnull Map<String, List<IQueryParameterType>> theParameters) {
		for (var entry : theParameters.entrySet()) {
			if (isSearchParameter(entry.getKey())) {
				this.separatedSearchParameters.put(entry.getKey(), entry.getValue());
			} else {
				this.separatedResultParameters.put(entry.getKey(), entry.getValue());
			}
		}
	}

	public boolean isSearchParameter(String theParameterName) {
		return this.searchResultParameters.contains(theParameterName);
	}
}
