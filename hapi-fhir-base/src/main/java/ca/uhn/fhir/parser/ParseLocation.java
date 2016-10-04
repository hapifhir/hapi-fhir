package ca.uhn.fhir.parser;

import ca.uhn.fhir.parser.IParserErrorHandler.IParseLocation;

class ParseLocation implements IParseLocation {

	private String myParentElementName;

	/**
	 * Constructor
	 */
	public ParseLocation(String theParentElementName) {
		super();
		myParentElementName = theParentElementName;
	}

	@Override
	public String getParentElementName() {
		return myParentElementName;
	}

}
