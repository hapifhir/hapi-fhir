package ca.uhn.fhir.exception;

import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

public class TokenParamFormatInvalidRequestException extends InvalidRequestException {

	public TokenParamFormatInvalidRequestException(String theCode, String theParamName, String theTokenValue) {
		super(theCode + "Missing " + theParamName +
			" parameter (must supply a value/code and not just a system): " + theTokenValue);
	}


}
