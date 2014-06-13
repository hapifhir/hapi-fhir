package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.jpa.entity.ResourceEncodingEnum;

public class DaoConfig {

	private int myHardSearchLimit = 1000;
	private int myHardTagListLimit = 1000;
	private ResourceEncodingEnum myResourceEncoding=ResourceEncodingEnum.JSONC;

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

}
