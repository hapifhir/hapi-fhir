package ca.uhn.fhir.batch2.jobs.parameters;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.ResourceSearch;
import ca.uhn.fhir.rest.api.server.RequestDetails;

public class UrlPartitioner {
	private final MatchUrlService myMatchUrlService;
	private final IRequestPartitionHelperSvc myRequestPartitionHelperSvc;

	public UrlPartitioner(MatchUrlService theMatchUrlService, IRequestPartitionHelperSvc theRequestPartitionHelperSvc) {
		myMatchUrlService = theMatchUrlService;
		myRequestPartitionHelperSvc = theRequestPartitionHelperSvc;

	}

	public PartitionedUrl partitionUrl(String theUrl, RequestDetails theRequestDetails) {
		ResourceSearch resourceSearch = myMatchUrlService.getResourceSearch(theUrl);
		RequestPartitionId requestPartitionId = myRequestPartitionHelperSvc.determineReadPartitionForRequestForSearchType(theRequestDetails, resourceSearch.getResourceName(), resourceSearch.getSearchParameterMap(), null);
		PartitionedUrl retval = new PartitionedUrl();
		retval.setUrl(theUrl);
		retval.setRequestPartitionId(requestPartitionId);
		return retval;
	}
}
