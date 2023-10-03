package ca.uhn.fhir.jpa.mdm.models;

import java.util.List;

public class GenerateMetricsTestParameters {

	private String myInitialState;

	/**
	 * The scores for each link.
	 * The order should match the order of the
	 * links listed in initial state.
	 */
	private List<Double> myScores;

	public String getInitialState() {
		return myInitialState;
	}

	public void setInitialState(String theInitialState) {
		myInitialState = theInitialState;
	}

	public List<Double> getScores() {
		return myScores;
	}

	public void setScores(List<Double> theScores) {
		myScores = theScores;
	}
}
