package ca.uhn.fhir.rest.param;

import ca.uhn.fhir.model.api.IQueryParameterType;

public class ReferenceParam implements IQueryParameterType {

	private String myValue;
	
	@Override
	public void setValueAsQueryToken(String theParameter) {
		myValue=theParameter;
	}

	@Override
	public String getValueAsQueryToken() {
		return myValue;
	}

}
