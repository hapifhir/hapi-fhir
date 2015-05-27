package ca.uhn.fhir.parser;

/**
 * Adapter implementation with NOP implementations of all {@link IParserErrorHandler} methods.
 */
public class ErrorHandlerAdapter implements IParserErrorHandler {
	
	@Override
	public void unknownElement(IParseLocation theLocation, String theElementName) {
		// NOP
	}

	@Override
	public void unknownAttribute(IParseLocation theLocation, String theElementName) {
		// NOP
	}

}
