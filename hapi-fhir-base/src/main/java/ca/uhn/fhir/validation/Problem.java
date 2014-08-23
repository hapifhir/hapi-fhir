package ca.uhn.fhir.validation;

public class Problem {

	private String myDescription;

	public Problem(String theDescription) {
		myDescription=theDescription;
	}

	public String getDescription() {
		return myDescription;
	}

}
