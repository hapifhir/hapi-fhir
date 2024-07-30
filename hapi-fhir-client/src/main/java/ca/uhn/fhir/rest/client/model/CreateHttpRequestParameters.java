package ca.uhn.fhir.rest.client.model;

import ca.uhn.fhir.rest.api.RequestTypeEnum;
import org.apache.http.HttpEntity;

@Deprecated
public class CreateHttpRequestParameters {
	/**
	 * GET, DELETE, etc...
	 * This is the minimum info needed to make a request
	 */
	private final RequestTypeEnum myRequestTypeEnum;

	private HttpEntity myEntity;

	private String myUrl;

	public CreateHttpRequestParameters(RequestTypeEnum theRequestTypeEnum) {
		myRequestTypeEnum = theRequestTypeEnum;
	}

	public RequestTypeEnum getRequestTypeEnum() {
		return myRequestTypeEnum;
	}

	public HttpEntity getEntity() {
		return myEntity;
	}

	public CreateHttpRequestParameters setEntity(HttpEntity theEntity) {
		myEntity = theEntity;
		return this;
	}

	public String getUrl() {
		return myUrl;
	}

	public CreateHttpRequestParameters setUrl(String theUrl) {
		myUrl = theUrl;
		return this;
	}
}
