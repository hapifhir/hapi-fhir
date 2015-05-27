package ca.uhn.fhir.parser;

/**
 * Error handler
 */
public interface IParserErrorHandler {

	/**
	 * Invoked when an unknown element is found in the document. 
	 * 
	 * @param theLocation The location in the document. WILL ALWAYS BE NULL currently, as this is not yet implemented, but this parameter is included so that locations can be added in the future without changing the API.
	 * @param theAttributeName The name of the attribute that was found.
	 */
	void unknownAttribute(IParseLocation theLocation, String theAttributeName);

	/**
	 * Invoked when an unknown element is found in the document. 
	 * 
	 * @param theLocation The location in the document. WILL ALWAYS BE NULL currently, as this is not yet implemented, but this parameter is included so that locations can be added in the future without changing the API.
	 * @param theElementName The name of the element that was found.
	 */
	void unknownElement(IParseLocation theLocation, String theElementName);

	/**
	 * For now this is an empty interface. Error handling methods include a parameter of this
	 * type which will currently always be set to null. This interface is included here so that
	 * locations can be added to the API in a future release without changing the API.
	 */
	public interface IParseLocation {
		// nothing for now
	}
	
}
