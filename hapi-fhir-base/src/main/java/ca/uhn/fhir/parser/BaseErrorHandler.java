package ca.uhn.fhir.parser;

class BaseErrorHandler {

	String describeLocation(IParserErrorHandler.IParseLocation theLocation) {
		if (theLocation == null) {
			return "";
		} else {
			return theLocation.toString() + " ";
		}
	}

}

