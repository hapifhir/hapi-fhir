package ca.uhn.fhir.mdm.api.parameters;

public class GetGoldenResourceCountParameters {

	private String myResourceType;

	public String getResourceType() {
		return myResourceType;
	}

	public void setResourceType(String theResourceType) {
		myResourceType = theResourceType;
	}
}
