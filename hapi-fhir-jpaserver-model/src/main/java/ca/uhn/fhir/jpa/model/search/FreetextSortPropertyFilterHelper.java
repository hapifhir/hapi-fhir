package ca.uhn.fhir.jpa.model.search;

import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * initializes and invokes the IFreetextSortPropertyFilter list
 */
public class FreetextSortPropertyFilterHelper {

	private final List<IFreetextSortPropertyFilter> myImplementedFilters = new ArrayList<>();


	public FreetextSortPropertyFilterHelper() {
		registerFilters();
	}

	/**
	 * Registers required filters
	 */
	private void registerFilters() {
		myImplementedFilters.add( new FreetextSortPropertyFilterQuantity() );
	}


	public List<String> filterProperties(RestSearchParameterTypeEnum theParamPropType, List<String> theProperties) {
		for (IFreetextSortPropertyFilter aFilter : myImplementedFilters) {
			if (aFilter.accepts(theParamPropType)) {
				return aFilter.filter(theProperties);
			}
		}

		// no filter defined for theParamPropType, so return unfiltered
		return theProperties;
	}


}
