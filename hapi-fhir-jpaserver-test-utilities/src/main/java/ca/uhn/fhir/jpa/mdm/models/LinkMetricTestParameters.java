package ca.uhn.fhir.jpa.mdm.models;

import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.model.MdmLinkMetrics;

import java.util.ArrayList;
import java.util.List;

public class LinkMetricTestParameters {
	/**
	 * The initial state (as to be fed into MdmLinkHelper)
	 */
	private String myInitialState;

	/**
	 * The filters for MatchResult
	 */
	private List<MdmMatchResultEnum> myMatchFilters;

	/**
	 * The filters for LinkSource
	 */
	private List<MdmLinkSourceEnum> myLinkSourceEnums;

	/**
	 * The expected metrics to be returned
	 */
	private MdmLinkMetrics myExpectedMetrics;

	public String getInitialState() {
		return myInitialState;
	}

	public void setInitialState(String theInitialState) {
		myInitialState = theInitialState;
	}

	public List<MdmMatchResultEnum> getMatchFilters() {
		if (myMatchFilters == null) {
			myMatchFilters = new ArrayList<>();
		}
		return myMatchFilters;
	}

	public void setMatchFilters(List<MdmMatchResultEnum> theMatchFilters) {
		myMatchFilters = theMatchFilters;
	}

	public List<MdmLinkSourceEnum> getLinkSourceFilters() {
		if (myLinkSourceEnums == null) {
			myLinkSourceEnums = new ArrayList<>();
		}
		return myLinkSourceEnums;
	}

	public void setLinkSourceFilters(List<MdmLinkSourceEnum> theLinkSourceEnums) {
		myLinkSourceEnums = theLinkSourceEnums;
	}

	public MdmLinkMetrics getExpectedMetrics() {
		return myExpectedMetrics;
	}

	public void setExpectedMetrics(MdmLinkMetrics theExpectedMetrics) {
		myExpectedMetrics = theExpectedMetrics;
	}
}
