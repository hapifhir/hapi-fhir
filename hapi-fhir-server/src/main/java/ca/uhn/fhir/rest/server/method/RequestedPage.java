package ca.uhn.fhir.rest.server.method;


/**
 * This is an intermediate record object that holds the offset and limit (count) the user requested for the page of results.
 */
public class RequestedPage {
	/**
	 * The search results offset requested by the user
	 */
	public final Integer offset;
	/**
	 * The number of results starting from the offset requested by the user
	 */
	public final Integer limit;

	public RequestedPage(Integer theOffset, Integer theLimit) {
		offset = theOffset;
		limit = theLimit;
	}
}
