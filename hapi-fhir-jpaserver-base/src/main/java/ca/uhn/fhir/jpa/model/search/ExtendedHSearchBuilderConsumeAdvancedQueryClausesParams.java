package ca.uhn.fhir.jpa.model.search;

import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;

public class ExtendedHSearchBuilderConsumeAdvancedQueryClausesParams {
	/**
	 * Resource type
	 */
	private String myResourceType;
	/**
	 * The registered search
	 */
	private SearchParameterMap mySearchParameterMap;
	/**
	 * Search param registry
	 */
	private ISearchParamRegistry mySearchParamRegistry;

	public String getResourceType() {
		return myResourceType;
	}

	public ExtendedHSearchBuilderConsumeAdvancedQueryClausesParams setResourceType(String theResourceType) {
		myResourceType = theResourceType;
		return this;
	}

	public SearchParameterMap getSearchParameterMap() {
		return mySearchParameterMap;
	}

	public ExtendedHSearchBuilderConsumeAdvancedQueryClausesParams setSearchParameterMap(SearchParameterMap theParams) {
		mySearchParameterMap = theParams;
		return this;
	}

	public ISearchParamRegistry getSearchParamRegistry() {
		return mySearchParamRegistry;
	}

	public ExtendedHSearchBuilderConsumeAdvancedQueryClausesParams setSearchParamRegistry(
			ISearchParamRegistry theSearchParamRegistry) {
		mySearchParamRegistry = theSearchParamRegistry;
		return this;
	}
}
