package ca.uhn.fhir.context.support.conceptproperty;

public class StringConceptProperty extends BaseConceptProperty {
	private final String myValue;

	/**
	 * Constructor
	 *
	 * @param theName The name
	 */
	public StringConceptProperty(String theName, String theValue) {
		super(theName);
		myValue = theValue;
	}

	public String getValue() {
		return myValue;
	}
}
