package ca.uhn.fhir.rest.param;

import ca.uhn.fhir.model.api.IQueryParameterType;

public class ReferenceParam implements IQueryParameterType {

	private String myChain;
	private String myValue;
	
	public ReferenceParam() {
	}
	
	public ReferenceParam(String theValue) {
		setValueAsQueryToken(theValue);
	}

	public ReferenceParam(String theValue, String theChain) {
		setValueAsQueryToken(theValue);
		setChain(theChain);
	}

	public String getChain() {
		return myChain;
	}

	@Override
	public String getValueAsQueryToken() {
		return myValue;
	}

	public void setChain(String theChain) {
		myChain = theChain;
	}

	@Override
	public void setValueAsQueryToken(String theParameter) {
		myValue=theParameter;
	}

}
