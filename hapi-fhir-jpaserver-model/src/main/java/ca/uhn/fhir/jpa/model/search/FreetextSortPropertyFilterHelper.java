package ca.uhn.fhir.jpa.model.search;

import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Orchestrates calling each of the configured filters
 */
public class FreetextSortPropertyFilterHelper {

	@Autowired
	@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
	private List<IFreetextSortPropertyFilter> myFreetextSortPropertyFilters;


	public List<String> filterProperties(RestSearchParameterTypeEnum theParamPropType, List<String> theProperties) {
		for (IFreetextSortPropertyFilter aFilter : myFreetextSortPropertyFilters) {
			if (aFilter.accepts(theParamPropType)) {
				return aFilter.filter(theProperties);
			}
		}

		// no filter defined for theParamPropType, so return unfiltered
		return theProperties;
	}


}
