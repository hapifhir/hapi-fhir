package ca.uhn.fhir.jpa.mdm.models;

import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.model.MdmLinkScoreMetrics;

import java.util.ArrayList;
import java.util.List;

public class LinkScoreMetricTestParams {
	private String myInitialState;

	private List<MdmMatchResultEnum> myMatchFilter;

	private MdmLinkScoreMetrics myExpectedLinkDataMetrics;

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

	public MdmLinkScoreMetrics getExpectedLinkDataMetrics() {
		return myExpectedLinkDataMetrics;
	}

	public void setExpectedLinkDataMetrics(MdmLinkScoreMetrics theExpectedLinkDataMetrics) {
		myExpectedLinkDataMetrics = theExpectedLinkDataMetrics;
	}

	public List<MdmMatchResultEnum> getMatchFilter() {
		if (myMatchFilter == null) {
			myMatchFilter = new ArrayList<>();
		}
		return myMatchFilter;
	}

	public void addMatchType(MdmMatchResultEnum theResultEnum) {
		getMatchFilter().add(theResultEnum);
	}

	public List<Double> getScores() {
		if (myScores == null) {
			myScores = new ArrayList<>();
		}
		return myScores;
	}

	public void setScores(List<Double> theScores) {
		myScores = theScores;
	}
}
