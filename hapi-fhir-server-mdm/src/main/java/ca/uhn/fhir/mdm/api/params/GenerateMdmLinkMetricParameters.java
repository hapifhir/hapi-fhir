package ca.uhn.fhir.mdm.api.params;

import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;

import java.util.ArrayList;
import java.util.List;

public class GenerateMdmLinkMetricParameters {

	/**
	 * The resource type of interest.
	 * Must be provided!
	 */
	private final String myResourceType;

	/**
	 * The MDM MatchResult types of interest.
	 * Specified MatchResults will be included.
	 * If none are specified, all will be included.
	 */
	private List<MdmMatchResultEnum> myMatchResultFilters;

	/**
	 * The MDM Link values of interest.
	 * Specified LinkSources will be included.
	 * If none are specified, all are included.
	 */
	private List<MdmLinkSourceEnum> myLinkSourceFilters;

	public GenerateMdmLinkMetricParameters(String theResourceType) {
		myResourceType = theResourceType;
	}

	public String getResourceType() {
		return myResourceType;
	}

	public List<MdmMatchResultEnum> getMatchResultFilters() {
		if (myMatchResultFilters == null) {
			myMatchResultFilters = new ArrayList<>();
		}
		return myMatchResultFilters;
	}

	public void addMatchResultFilter(MdmMatchResultEnum theMdmMatchResultEnum) {
		getMatchResultFilters().add(theMdmMatchResultEnum);
	}

	public List<MdmLinkSourceEnum> getLinkSourceFilters() {
		if (myLinkSourceFilters == null) {
			myLinkSourceFilters = new ArrayList<>();
		}
		return myLinkSourceFilters;
	}

	public void addLinkSourceFilter(MdmLinkSourceEnum theLinkSource) {
		getLinkSourceFilters().add(theLinkSource);
	}
}
