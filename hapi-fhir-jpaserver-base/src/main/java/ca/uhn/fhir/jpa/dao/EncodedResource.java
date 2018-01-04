package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.jpa.entity.ResourceEncodingEnum;

class EncodedResource {

	private boolean myChanged;
	private byte[] myResource;
	private ResourceEncodingEnum myEncoding;

	public ResourceEncodingEnum getEncoding() {
		return myEncoding;
	}

	public void setEncoding(ResourceEncodingEnum theEncoding) {
		myEncoding = theEncoding;
	}

	public byte[] getResource() {
		return myResource;
	}

	public void setResource(byte[] theResource) {
		myResource = theResource;
	}

	public boolean isChanged() {
		return myChanged;
	}

	public void setChanged(boolean theChanged) {
		myChanged = theChanged;
	}

}
