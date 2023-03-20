package ca.uhn.fhir.cr.repo;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	SearchParameterMap getSearchParameters(Map<String, List<IQueryParameterType>> theParameters) {
		Map<String, List<IQueryParameterType>> searchParameters =
				this.getOnlySearchParameters(theParameters);
		return this.convert(searchParameters);
	}

	// Map<String, String[]> getResultParameters(Map<String, List<IQueryParameterType>>
	// theParameters) {
	//
	// }

	private SearchParameterMap convert(Map<String, List<IQueryParameterType>> theSearchMap) {
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

	private Map<String, List<IQueryParameterType>> getOnlySearchParameters(
			Map<String, List<IQueryParameterType>> theParameters) {
		Map<String, List<IQueryParameterType>> searchParameters = new HashMap<>();
		for (var entry : theParameters.entrySet()) {
			if (isSearchParameter(entry.getKey())) {
				searchParameters.put(entry.getKey(), entry.getValue());
			}
		}
		return searchParameters;
	}
}
