package ca.uhn.fhir.mdm.api.params;

public class GenerateMdmResourceMetricsParameters {

	/**
	 * We only allow finding metrics by resource type
	 */
	private final String myResourceType;

	public GenerateMdmResourceMetricsParameters(String theResourceType) {
		myResourceType = theResourceType;
	}

	public String getResourceType() {
		return myResourceType;
	}
}
