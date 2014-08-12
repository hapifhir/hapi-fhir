package ca.uhn.fhir.rest.param;

import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.rest.server.Constants;

/**
 * Base class for RESTful operation parameter types
 */
public class BaseParam implements IQueryParameterType {

	private Boolean  myMissing;
	
	/**
	 * If set to non-null value, indicates that this parameter has been populated with a "[name]:missing=true" or "[name]:missing=false" vale 
	 * instead of a normal value 
	 */
	public Boolean getMissing() {
		return myMissing;
	}

	
	
	@Override
	public String getQueryParameterQualifier() {
		if (myMissing != null) {
			return Constants.PARAMQUALIFIER_MISSING;
		}
		return null;
	}


	@Override
	public String getValueAsQueryToken() {
		if (myMissing != null) {
			return myMissing ? "true" : "false";
		}
		return null;
	}



	/**
	 * If set to non-null value, indicates that this parameter has been populated with a "[name]:missing=true" or "[name]:missing=false" vale 
	 * instead of a normal value 
	 */
	public void setMissing(Boolean theMissing) {
		myMissing = theMissing;
	}

	@Override
	public void setValueAsQueryToken(String theQualifier, String theValue) {
		if (Constants.PARAMQUALIFIER_MISSING.equals(theQualifier)) {
			myMissing = "true".equals(theValue);
		} else {
			myMissing = null;
		}
	}

}
