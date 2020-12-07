package ca.uhn.fhir.context.support.conceptproperty;

public class CodingConceptProperty extends BaseConceptProperty {
	private final String myCode;
	private final String myCodeSystem;
	private final String myDisplay;

	/**
	 * Constructor
	 *
	 * @param theName The name
	 */
	public CodingConceptProperty(String theName, String theCodeSystem, String theCode, String theDisplay) {
		super(theName);
		myCodeSystem = theCodeSystem;
		myCode = theCode;
		myDisplay = theDisplay;
	}

	public String getCode() {
		return myCode;
	}

	public String getCodeSystem() {
		return myCodeSystem;
	}

	public String getDisplay() {
		return myDisplay;
	}
}
