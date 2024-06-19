package ca.uhn.fhir.jpa.model.search;

import ca.uhn.fhir.jpa.dao.search.ExtendedHSearchClauseBuilder;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;

/**
 * This is a parameter class for the
 * {@link ca.uhn.fhir.jpa.dao.search.ExtendedHSearchSearchBuilder#addAndConsumeAdvancedQueryClauses(ExtendedHSearchClauseBuilder, ExtendedHSearchBuilderConsumeAdvancedQueryClausesParams)}
 * method, so that we can keep the signature manageable (small) and allow for updates without breaking
 * implementers so often.
 */
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
