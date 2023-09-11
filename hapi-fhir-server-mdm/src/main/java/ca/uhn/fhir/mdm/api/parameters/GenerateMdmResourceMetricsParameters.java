package ca.uhn.fhir.mdm.api.parameters;

public class GenerateMdmResourceMetricsParameters {

	/**
	 * We only allow finding metrics by resource type
	 */
	private String myResourceType;

	public GenerateMdmResourceMetricsParameters(String theResourceType) {
		myResourceType = theResourceType;
	}

	public String getResourceType() {
		return myResourceType;
	}
}
