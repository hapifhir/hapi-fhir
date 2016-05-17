package ca.uhn.fhir.jpa.term;

public class VersionIndependentConcept {

	private String mySystem;
	private String myCode;

	public VersionIndependentConcept(String theSystem, String theCode) {
		setSystem(theSystem);
		setCode(theCode);
	}

	public String getSystem() {
		return mySystem;
	}

	public void setSystem(String theSystem) {
		mySystem = theSystem;
	}

	public String getCode() {
		return myCode;
	}

	public void setCode(String theCode) {
		myCode = theCode;
	}

}
