package ca.uhn.fhir.rest.server.method;

public class RequestedPage {
	public final Integer offset;
	public final Integer limit;

	public RequestedPage(Integer theOffset, Integer theLimit) {
		offset = theOffset;
		limit = theLimit;
	}
}
