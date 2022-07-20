package ca.uhn.fhir.exception;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

public class TokenParamFormatInvalidRequestException extends InvalidRequestException {

	public static final String MSG_CODE = Msg.code(1218);

	public TokenParamFormatInvalidRequestException(String theParamName, String theTokenValue) {
		super(MSG_CODE + "Invalid " + theParamName +
			" parameter (must supply a value/code and not just a system): " + theTokenValue);
	}

	public static TokenParamFormatInvalidRequestException forTagParamName(String theTokenValue){
		return new TokenParamFormatInvalidRequestException(Constants.PARAM_TAG, theTokenValue);
	}


}
