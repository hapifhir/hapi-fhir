package ca.uhn.fhir.jpa.search.lastn;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;

import java.util.List;

public interface IElasticsearchSvc {
	List<String> executeLastN(SearchParameterMap theSearchParameterMap, FhirContext theFhirContext, Integer theMaxResultsToFetch);
}
