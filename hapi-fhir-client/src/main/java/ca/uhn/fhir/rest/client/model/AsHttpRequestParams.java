package ca.uhn.fhir.rest.client.model;

import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.client.api.IHttpClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AsHttpRequestParams {
	/**
	 * The URL for the request
	 */
	private String myUrlBase;

	/**
	 * Extra parameters
	 */
	private Map<String, List<String>> myExtraParams;

	/**
	 * Encoding to use (JSON, fhir+json, etc)
	 */
	private EncodingEnum myEncodingEnum;

	/**
	 * Whether logs should print pretty or not (for request parsing only)
	 */
	private Boolean myPrettyPrint;

	/**
	 * The client to use for this request
	 */
	private IHttpClient myClient;

	public String getUrlBase() {
		return myUrlBase;
	}

	public AsHttpRequestParams setUrlBase(String theUrlBase) {
		myUrlBase = theUrlBase;
		return this;
	}

	public Map<String, List<String>> getExtraParams() {
		if (myExtraParams == null) {
			myExtraParams = new HashMap<>();
		}
		return myExtraParams;
	}

	public void addExtraParam(String theKey, String theValue) {
		Map<String, List<String>> extraParams = getExtraParams();
		if (!extraParams.containsKey(theKey)) {
			extraParams.put(theKey, new ArrayList<>());
		}
		extraParams.get(theKey).add(theValue);
	}

	public AsHttpRequestParams setExtraParams(Map<String, List<String>> theExtraParams) {
		myExtraParams = theExtraParams;
		return this;
	}

	public EncodingEnum getEncodingEnum() {
		return myEncodingEnum;
	}

	public AsHttpRequestParams setEncodingEnum(EncodingEnum theEncodingEnum) {
		myEncodingEnum = theEncodingEnum;
		return this;
	}

	public Boolean getPrettyPrint() {
		return myPrettyPrint;
	}

	public AsHttpRequestParams setPrettyPrint(Boolean thePrettyPrint) {
		myPrettyPrint = thePrettyPrint;
		return this;
	}

	public IHttpClient getClient() {
		return myClient;
	}

	public AsHttpRequestParams setClient(IHttpClient theClient) {
		myClient = theClient;
		return this;
	}
}
