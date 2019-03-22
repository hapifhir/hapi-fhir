package ca.uhn.fhir.jpa.model.search;

import ca.uhn.fhir.util.StopWatch;

/**
 * This class contains a runtime in-memory description of a search operation,
 * including details on processing time and other things
 */
public class SearchRuntimeDetails {
	private final String mySearchUuid;
	private StopWatch myQueryStopwatch;
	private int myFoundMatchesCount;
	private boolean myLoadSynchronous;
	private String myQueryString;

	public SearchRuntimeDetails(String theSearchUuid) {
		mySearchUuid = theSearchUuid;
	}

	public String getSearchUuid() {
		return mySearchUuid;
	}

	public StopWatch getQueryStopwatch() {
		return myQueryStopwatch;
	}

	public void setQueryStopwatch(StopWatch theQueryStopwatch) {
		myQueryStopwatch = theQueryStopwatch;
	}

	public void setFoundMatchesCount(int theFoundMatchesCount) {
		myFoundMatchesCount = theFoundMatchesCount;
	}

	public int getFoundMatchesCount() {
		return myFoundMatchesCount;
	}

	public void setLoadSynchronous(boolean theLoadSynchronous) {
		myLoadSynchronous = theLoadSynchronous;
	}

	public boolean getLoadSynchronous() {
		return myLoadSynchronous;
	}

	public void setQueryString(String theQueryString) {
		myQueryString = theQueryString;
	}

	public String getQueryString() {
		return myQueryString;
	}
}
