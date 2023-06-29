package ca.uhn.fhir.rest.server.method;

import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.List;

public class ResponsePage {
	public final String searchId;
	public final List<IBaseResource> resourceList;
	public final int numToReturn;
	public final Integer numTotalResults;
	public final int pageSize;

	public ResponsePage(String theSearchId, List<IBaseResource> theResourceList, int theNumToReturn, Integer theNumTotalResults, int thePageSize) {
		searchId = theSearchId;
		resourceList = theResourceList;
		numToReturn = theNumToReturn;
		numTotalResults = theNumTotalResults;
		pageSize = thePageSize;
	}

	public int size() {
		return resourceList.size();
	}
}
