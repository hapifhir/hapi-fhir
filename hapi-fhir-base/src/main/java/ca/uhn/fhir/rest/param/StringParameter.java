package ca.uhn.fhir.rest.param;

import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.server.Constants;

public class StringParameter extends StringDt {

	private boolean myExact;

	public StringParameter() {
	}
	
	public StringParameter(String theValue) {
		setValue(theValue);
	}

	@Override
	public void setValueAsQueryToken(String theQualifier, String theValue) {
		if (Constants.PARAMQUALIFIER_STRING_EXACT.equals(theQualifier)) {
			setExact(true);
		}else {
			setExact(false);
		}
		super.setValueAsQueryToken(theQualifier, theValue);
	}

	public boolean isExact() {
		return myExact;
	}

	public void setExact(boolean theExact) {
		myExact = theExact;
	}
	
}
