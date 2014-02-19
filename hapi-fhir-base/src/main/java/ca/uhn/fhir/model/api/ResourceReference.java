package ca.uhn.fhir.model.api;

public class ResourceReference /* <T extends BaseResource> */{

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
