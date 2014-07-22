package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.jpa.entity.ResourceEncodingEnum;

public class DaoConfig {

	private int myHardSearchLimit = 1000;
	private int myHardTagListLimit = 1000;
	private ResourceEncodingEnum myResourceEncoding=ResourceEncodingEnum.JSONC;
	private int myIncludeLimit = 2000;

	/**
	 * This is the maximum number of resources that will be added to a single page of 
	 * returned resources. Because of includes with wildcards and other possibilities it is possible for a client to make 
	 * requests that include very large amounts of data, so this hard limit can be imposed to prevent runaway
	 * requests.
	 */
	public void setIncludeLimit(int theIncludeLimit) {
		myIncludeLimit = theIncludeLimit;
	}

	/**
	 * See {@link #setIncludeLimit(int)}
	 */
	public int getHardSearchLimit() {
		return myHardSearchLimit;
	}

	public int getHardTagListLimit() {
		return myHardTagListLimit;
	}

	public void setHardSearchLimit(int theHardSearchLimit) {
		myHardSearchLimit = theHardSearchLimit;
	}

	public void setHardTagListLimit(int theHardTagListLimit) {
		myHardTagListLimit = theHardTagListLimit;
	}

	public ResourceEncodingEnum getResourceEncoding() {
		return myResourceEncoding;
	}

	public void setResourceEncoding(ResourceEncodingEnum theResourceEncoding) {
		myResourceEncoding = theResourceEncoding;
	}

	public int getIncludeLimit() {
		return myIncludeLimit;
	}

}
