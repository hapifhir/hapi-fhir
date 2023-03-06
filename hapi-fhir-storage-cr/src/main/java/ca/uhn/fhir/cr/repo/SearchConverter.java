package ca.uhn.fhir.cr.repo;

import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.api.IQueryParameterType;

import java.util.List;
import java.util.Map;

/**
 * The IGenericClient API represents searches with OrLists, while the FhirRepository API uses nested lists.
 * This class (will eventually) convert between them
 */
class SearchConverter {
	static SearchParameterMap convert(Map<String, List<IQueryParameterType>> theSearchMap) {
		var converted = new SearchParameterMap();

		// TODO: This logic is known to be bad. Just prototyping some stuff...
		for (var entry : theSearchMap.entrySet()) {
			for(var value : entry.getValue()) {
				converted.add(entry.getKey(), value);
			}
		}

		return converted;
	}
}
