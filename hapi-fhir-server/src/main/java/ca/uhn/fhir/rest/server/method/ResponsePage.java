package ca.uhn.fhir.rest.server.method;

import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.List;

/**
 * This is an intermediate record object that holds all the fields required to make the final bundle that will be returned to the client.
 */
public class ResponsePage {
	/**
	 * The id of the search used to page through search results
	 */
	public final String searchId;
	/**
	 * The list of resources that will be used to create the bundle
	 */
	public final List<IBaseResource> resourceList;
	/**
	 * The total number of results that matched the search
	 */
	public final Integer numTotalResults;
	/**
	 * The number of resources that should be returned in each page
	 */
	public final int pageSize;
	/**
	 * The number of resources that should be returned in the bundle.  Can be smaller than pageSize when the bundleProvider
	 * has fewer results than the page size.
	 */
	public final int numToReturn;

	public ResponsePage(String theSearchId, List<IBaseResource> theResourceList, int thePageSize, int theNumToReturn, Integer theNumTotalResults) {
		searchId = theSearchId;
		resourceList = theResourceList;
		pageSize = thePageSize;
		numToReturn = theNumToReturn;
		numTotalResults = theNumTotalResults;
	}

	public int size() {
		return resourceList.size();
	}
}
