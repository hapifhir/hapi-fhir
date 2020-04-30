package ca.uhn.fhir.jpa.packages;

import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import org.hl7.fhir.instance.model.api.IBaseResource;

/**
 * For internal use only
 */
public interface SearchUtilities {
	/**
	 * Defines the parameter(s) to search for existing resources on
	 */
	SearchParameterMap createSearchParameterMapFor(IBaseResource resource);

	IBaseResource verifySearchResultFor(IBaseResource resource, IBundleProvider searchResult);
}
