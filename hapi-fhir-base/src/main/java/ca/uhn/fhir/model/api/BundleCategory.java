package ca.uhn.fhir.model.api;

public class BundleCategory {

	private String myLabel;
	private String myScheme;
	private String myTerm;

	public String getLabel() {
		return myLabel;
	}

	public String getScheme() {
		return myScheme;
	}

	public String getTerm() {
		return myTerm;
	}

	public void setLabel(String theLabel) {
		myLabel = theLabel;
	}

	public void setScheme(String theScheme) {
		myScheme = theScheme;
	}

	public void setTerm(String theTerm) {
		myTerm = theTerm;
	}

}
