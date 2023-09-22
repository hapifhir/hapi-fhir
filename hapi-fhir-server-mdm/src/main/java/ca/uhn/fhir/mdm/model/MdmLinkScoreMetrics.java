package ca.uhn.fhir.mdm.model;

import java.util.LinkedHashMap;
import java.util.Map;

public class MdmLinkScoreMetrics {

	private String myResourceType;

	/**
	 * Map of Score:Count
	 * Scores are typically Doubles. But we cast to string because
	 * Score is not a non-null field, and so "NULL" is a value.
	 */
	private Map<String, Long> myScoreCounts;

	public void setResourceType(String theResourceType) {
		myResourceType = theResourceType;
	}

	public String getResourceType() {
		return myResourceType;
	}

	public Map<String, Long> getScoreCounts() {
		if (myScoreCounts == null) {
			myScoreCounts = new LinkedHashMap<>();
		}
		return myScoreCounts;
	}

	public void addScore(String theScore, Long theCount) {
		getScoreCounts().put(theScore, theCount);
	}
}
