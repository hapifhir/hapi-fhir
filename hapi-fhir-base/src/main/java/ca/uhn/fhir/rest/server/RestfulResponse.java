package ca.uhn.fhir.rest.server;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.rest.api.SummaryEnum;
import ca.uhn.fhir.rest.method.RequestDetails;

public abstract class RestfulResponse<T extends RequestDetails> implements IRestfulResponse {

	private T theRequestDetails;
	private ConcurrentHashMap<String, String> theHeaders = new ConcurrentHashMap<String, String>();

	public RestfulResponse(T requestDetails) {
		this.theRequestDetails = requestDetails;
	}

	@Override
	public final Object streamResponseAsResource(IBaseResource resource, boolean prettyPrint, Set<SummaryEnum> summaryMode,
			int statusCode, boolean respondGzip, boolean addContentLocationHeader)
					throws IOException {
		return RestfulServerUtils.streamResponseAsResource(theRequestDetails.getServer(), resource, prettyPrint, summaryMode, statusCode, respondGzip, addContentLocationHeader,
				respondGzip, getRequestDetails());

	}

	@Override
	public Object streamResponseAsBundle(Bundle bundle, Set<SummaryEnum> summaryMode, boolean respondGzip, boolean requestIsBrowser)
					throws IOException {
		return RestfulServerUtils.streamResponseAsBundle(theRequestDetails.getServer(), bundle, summaryMode, requestIsBrowser, respondGzip, getRequestDetails());
	}

	@Override
	public void addHeader(String headerKey, String headerValue) {
		this.getHeaders().put(headerKey, headerValue);
	}

	/**
	 * Get the http headers
	 * @return the headers
	 */
	public ConcurrentHashMap<String, String> getHeaders() {
		return theHeaders;
	}

	/**
	 * Get the requestDetails
	 * @return the requestDetails
	 */
	public T getRequestDetails() {
		return theRequestDetails;
	}

	/**
	 * Set the requestDetails
	 * @param requestDetails the requestDetails to set
	 */
	public void setRequestDetails(T requestDetails) {
		this.theRequestDetails = requestDetails;
	}

}
