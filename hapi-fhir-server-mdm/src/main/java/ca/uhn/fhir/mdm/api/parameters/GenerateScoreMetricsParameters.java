package ca.uhn.fhir.mdm.api.parameters;

import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;

import java.util.ArrayList;
import java.util.List;

public class GenerateScoreMetricsParameters {
	/**
	 * The resource type of interest.
	 */
	private final String myResourceType;

	private List<MdmMatchResultEnum> myMatchTypeFilters;

	public GenerateScoreMetricsParameters(String theResourceType) {
		myResourceType = theResourceType;
	}

	public String getResourceType() {
		return myResourceType;
	}

	public List<MdmMatchResultEnum> getMatchTypes() {
		if (myMatchTypeFilters == null) {
			myMatchTypeFilters = new ArrayList<>();
		}
		return myMatchTypeFilters;
	}

	public void addMatchType(MdmMatchResultEnum theMatchType) {
		getMatchTypes().add(theMatchType);
	}
}
