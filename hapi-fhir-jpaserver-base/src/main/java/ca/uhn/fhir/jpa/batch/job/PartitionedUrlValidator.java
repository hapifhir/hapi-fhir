package ca.uhn.fhir.jpa.batch.job;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.ResourceSearch;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PartitionedUrlValidator {
	@Autowired
	MatchUrlService myMatchUrlService;
	@Autowired
	IRequestPartitionHelperSvc myRequestPartitionHelperSvc;
	@Autowired
	FhirContext myFhirContext;

	/**
	 * This method will throw an exception if the user is not allowed to add the requested resource type on the partition determined by the request
	 */
	public List<RequestPartitionId> requestPartitionIdsFromRequestAndUrls(RequestDetails theRequest, List<String> theUrlsToProcess) {
		List<RequestPartitionId> retval = new ArrayList<>();
		for (String url : theUrlsToProcess) {
			ResourceSearch resourceSearch = myMatchUrlService.getResourceSearch(url);
			RequestPartitionId requestPartitionId = myRequestPartitionHelperSvc.determineReadPartitionForRequestForSearchType(theRequest, resourceSearch.getResourceName(), resourceSearch.getSearchParameterMap(), null);
			retval.add(requestPartitionId);
		}
		return retval;
	}

	public RequestPartitionId requestPartitionIdFromRequest(RequestDetails theRequest) {
		Set<String> allResourceNames = myFhirContext.getResourceTypes();
		SearchParameterMap map = SearchParameterMap.newSynchronous();
		// Test that the user has access to every resource type on the server:
		for (String resourceName : allResourceNames) {
			myRequestPartitionHelperSvc.determineReadPartitionForRequestForSearchType(theRequest, resourceName, map, null);
		}
		// Then return the partition for the Patient resource type
		return myRequestPartitionHelperSvc.determineReadPartitionForRequestForSearchType(theRequest, "Patient", map, null);
	}
}
