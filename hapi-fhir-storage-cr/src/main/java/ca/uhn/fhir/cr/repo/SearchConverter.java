package ca.uhn.fhir.cr.repo;

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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.api.IQueryParameterType;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * The IGenericClient API represents searches with OrLists, while the FhirRepository API uses nested lists.
 * This class (will eventually) convert between them
 */
public class SearchConverter {
	/**
	 * hardcoded from FHIR specs: https://www.hl7.org/fhir/search.html
	 */
	private final List<String> searchResultParameters = Arrays.asList(
			"_sort",
			"_count",
			"_include",
			"_revinclude",
			"_summary",
			"_total",
			"_elements",
			"_contained",
			"_containedType"
	);

	public final Map<String, List<IQueryParameterType>> separatedSearchParameters = new HashMap<>();
	public final Map<String, List<IQueryParameterType>> separatedResultParameters = new HashMap<>();
	public SearchParameterMap searchParameterMap;
	public Map<String, String[]> resultParameters;
	public IBaseResource searchParameters;

	void convertParameters(Map<String, List<IQueryParameterType>> theParameters, FhirContext theFhirContext) {
		this.separateParameterTypes(theParameters);
		this.searchParameterMap = this.convertToSearchParameterMap(this.separatedSearchParameters);
		this.resultParameters = this.convertToStringMap(this.separatedResultParameters, theFhirContext);
	}

	 public Map<String, String[]> convertToStringMap(Map<String, List<IQueryParameterType>> theParameters, FhirContext theFhirContext) {
		Map<String, String[]> result = new HashMap<>();
		for (var entry : theParameters.entrySet()) {
			String[] values = new String[entry.getValue().size()];
			for(int i = 0; i < entry.getValue().size(); i++) {
				values[i] = entry.getValue().get(i).getValueAsQueryToken(theFhirContext);
			}
			result.put(entry.getKey(), values);
		}
		return result;
	}

	public SearchParameterMap convertToSearchParameterMap(Map<String, List<IQueryParameterType>> theSearchMap) {
		var converted = new SearchParameterMap();
		if (theSearchMap == null) {
			return  converted;
		}
		// or list => single list resource
		// and list => multiple list resources

		// TODO: This logic is known to be bad. Just prototyping some stuff...
		for (var entry : theSearchMap.entrySet()) {
			for(var value : entry.getValue()) {
				converted.add(entry.getKey(), value);
			}
		}
		return converted;
	}

	public void separateParameterTypes(Map<String, List<IQueryParameterType>> theParameters) {
		for (var entry : theParameters.entrySet()) {
			if (isSearchParameter(entry.getKey())) {
				this.separatedSearchParameters.put(entry.getKey(), entry.getValue());
			} else {
				this.separatedResultParameters.put(entry.getKey(), entry.getValue());
			}
		}
	}

	public boolean isSearchParameter(String theParameterName) {return this.searchResultParameters.contains(theParameterName);}
}
