package ca.uhn.fhir.parser;

/**
 * The default error handler, which logs issues but does not abort parsing
 * 
 * @see IParser#setParserErrorHandler(IParserErrorHandler)
 */
public class LenientErrorHandler implements IParserErrorHandler {
	
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(LenientErrorHandler.class);
	
	@Override
	public void unknownElement(IParseLocation theLocation, String theElementName) {
		ourLog.warn("Unknown element '{}' found while parsing", theElementName);
	}

	@Override
	public void unknownAttribute(IParseLocation theLocation, String theElementName) {
		ourLog.warn("Unknown attribute '{}' found while parsing", theElementName);
	}

}
