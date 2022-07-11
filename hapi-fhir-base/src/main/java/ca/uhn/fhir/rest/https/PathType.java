package ca.uhn.fhir.rest.https;

public enum PathType {

	FILE("file://"),
	RESOURCE("classpath:");

	private String myPrefix;

	PathType(String thePrefix) {
		myPrefix = thePrefix;
	}

	public String getPrefix(){
		return myPrefix;
	}
}
