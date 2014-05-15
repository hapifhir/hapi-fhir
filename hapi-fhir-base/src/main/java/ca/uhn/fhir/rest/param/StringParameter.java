package ca.uhn.fhir.rest.param;

import ca.uhn.fhir.model.primitive.StringDt;

public class StringParameter extends StringDt {

	private boolean myExact;

	public StringParameter() {
	}
	
	public StringParameter(String theValue) {
		setValue(theValue);
	}

	public boolean isExact() {
		return myExact;
	}

	public void setExact(boolean theExact) {
		myExact = theExact;
	}
	
}
