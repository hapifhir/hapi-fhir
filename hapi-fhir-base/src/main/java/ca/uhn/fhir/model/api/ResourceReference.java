package ca.uhn.fhir.model.api;


public class ResourceReference {

	private String myDisplay;
	private String myReference;

	public String getDisplay() {
		return myDisplay;
	}

	public String getReference() {
		return myReference;
	}

	public void setDisplay(String theDisplay) {
		myDisplay = theDisplay;
	}

	public void setReference(String theReference) {
		myReference = theReference;
	}

}
