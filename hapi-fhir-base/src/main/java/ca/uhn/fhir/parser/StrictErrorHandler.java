package ca.uhn.fhir.parser;

/**
 * Parser error handler which throws a {@link DataFormatException} any time an 
 * issue is found while parsing.
 * 
 * @see IParser#setParserErrorHandler(IParserErrorHandler)
 */
public class StrictErrorHandler implements IParserErrorHandler {

	@Override
	public void unknownElement(IParseLocation theLocation, String theElementName) {
		throw new DataFormatException("Unknown element '" + theElementName + "' found during parse");
	}

	@Override
	public void unknownAttribute(IParseLocation theLocation, String theAttributeName) {
		throw new DataFormatException("Unknown attribute '" + theAttributeName + "' found during parse");
	}

}
