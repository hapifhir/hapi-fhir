package ca.uhn.fhir.rest.gclient;

public class Include {

	private String myInclude;

	public Include(String theInclude) {
		myInclude = theInclude;
	}

	public String getInclude() {
		return myInclude;
	}
	
}
