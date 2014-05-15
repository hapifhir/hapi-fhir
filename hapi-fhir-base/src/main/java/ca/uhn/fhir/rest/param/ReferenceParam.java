package ca.uhn.fhir.rest.param;

import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.api.IResource;

public class ReferenceParam implements IQueryParameterType {

	private String myChain;
	private Class<? extends IResource> myType;
	private String myValue;
	
	public ReferenceParam() {
	}

	public ReferenceParam(String theValue) {
		setValueAsQueryToken(theValue);
	}

	public ReferenceParam(String theChain, String theValue) {
		setValueAsQueryToken(theValue);
		setChain(theChain);
	}

	public ReferenceParam(Class<? extends IResource> theType, String theChain, String theValue) {
		setType(theType);
		setValueAsQueryToken(theValue);
		setChain(theChain);
	}
	
	public String getChain() {
		return myChain;
	}

	public Class<? extends IResource> getType() {
		return myType;
	}

	@Override
	public String getValueAsQueryToken() {
		return myValue;
	}

	public void setChain(String theChain) {
		myChain = theChain;
	}

	public void setType(Class<? extends IResource> theType) {
		myType = theType;
	}

	@Override
	public void setValueAsQueryToken(String theParameter) {
		myValue=theParameter;
	}

}
