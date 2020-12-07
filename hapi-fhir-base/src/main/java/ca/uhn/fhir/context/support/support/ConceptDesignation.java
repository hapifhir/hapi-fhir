package ca.uhn.fhir.context.support.support;

public class ConceptDesignation {
	private String myLanguage;
	private String myUseSystem;
	private String myUseCode;
	private String myUseDisplay;
	private String myValue;

	public String getLanguage() {
		return myLanguage;
	}

	public ConceptDesignation setLanguage(String theLanguage) {
		myLanguage = theLanguage;
		return this;
	}

	public String getUseSystem() {
		return myUseSystem;
	}

	public ConceptDesignation setUseSystem(String theUseSystem) {
		myUseSystem = theUseSystem;
		return this;
	}

	public String getUseCode() {
		return myUseCode;
	}

	public ConceptDesignation setUseCode(String theUseCode) {
		myUseCode = theUseCode;
		return this;
	}

	public String getUseDisplay() {
		return myUseDisplay;
	}

	public ConceptDesignation setUseDisplay(String theUseDisplay) {
		myUseDisplay = theUseDisplay;
		return this;
	}

	public String getValue() {
		return myValue;
	}

	public ConceptDesignation setValue(String theValue) {
		myValue = theValue;
		return this;
	}
}
