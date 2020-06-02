package ca.uhn.fhir.jpa.search.lastn;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;

import java.util.List;

public interface IElasticsearchSvc {

	/**
	 * Returns identifiers for the last most recent N observations that meet the specified criteria.
	 * @param theSearchParameterMap SearchParameterMap containing search parameters used for filtering the last N observations. Supported parameters include Subject, Patient, Code, Category and Max (the parameter used to determine N).
	 * @param theFhirContext Current FhirContext.
	 * @param theMaxResultsToFetch The maximum number of results to return for the purpose of paging.
	 * @return
	 */
	List<String> executeLastN(SearchParameterMap theSearchParameterMap, FhirContext theFhirContext, Integer theMaxResultsToFetch);
}
