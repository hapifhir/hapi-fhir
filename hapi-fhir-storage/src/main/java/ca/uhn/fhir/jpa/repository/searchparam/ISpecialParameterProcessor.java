package ca.uhn.fhir.jpa.repository.searchparam;

import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.api.IQueryParameterType;

import java.util.List;

/**
 * Interface for processing special search parameters like "_count" and "_sort"
 * that are not stored as AND/OR lists in the SearchParameterMap.
 */
interface ISpecialParameterProcessor {
	static String paramAsQueryString(IQueryParameterType theParameter) {
		return theParameter.getValueAsQueryToken(null);
	}

	/**
	 * Apply this processor to the theValues and update the SearchParameterMap.
	 * @param theKey the key of the parameter being processed, e.g. "_sort"
	 * @param theValues the values of the parameter being processed, e.g. new TokenParam("-date")
	 * @param theSearchParameterMap the target to modify
	 */
	void process(String theKey, List<IQueryParameterType> theValues, SearchParameterMap theSearchParameterMap);
}
