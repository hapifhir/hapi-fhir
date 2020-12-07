package ca.uhn.fhir.context.support.conceptproperty;

public abstract class BaseConceptProperty {
	private final String myPropertyName;

	/**
	 * Constructor
	 */
	protected BaseConceptProperty(String thePropertyName) {
		myPropertyName = thePropertyName;
	}

	public String getPropertyName() {
		return myPropertyName;
	}
}
