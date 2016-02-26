package ca.uhn.fhir.util;

import static org.apache.commons.lang3.StringUtils.isBlank;

import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

public class ValidateUtil {

	public static void isNotNullOrThrowInvalidRequest(boolean theSuccess, String theMessage) {
		if (theSuccess == false) {
			throw new InvalidRequestException(theMessage);
		}
	}

	public static void isNotBlankOrThrowInvalidRequest(String theString, String theMessage) {
		if (isBlank(theString)) {
			throw new InvalidRequestException(theMessage);
		}
	}

}
