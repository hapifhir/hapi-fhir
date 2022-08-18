package ca.uhn.fhir.jpa.search.builder.searchquery;

import ca.uhn.fhir.jpa.model.search.SearchRuntimeDetails;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.RequestDetails;

public class QueryIteratorParameters {
	public SearchRuntimeDetails SearchRuntimeDetails;
	public RequestDetails Request;
	public SearchBuilder SearchBuilderParent;
	public SearchParameterMap Params;
	public Integer MaxResultsToFetch;

	public QueryIteratorParameters(ca.uhn.fhir.jpa.model.search.SearchRuntimeDetails theSearchRuntimeDetails,
											 RequestDetails theRequest,
											 SearchBuilder theSearchBuilderParent,
											 SearchParameterMap theParams,
											 Integer theMaxResultsToFetch) {
		SearchRuntimeDetails = theSearchRuntimeDetails;
		Request = theRequest;
		SearchBuilderParent = theSearchBuilderParent;
		Params = theParams;
		MaxResultsToFetch = theMaxResultsToFetch;
	}
}
