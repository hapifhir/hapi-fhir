package ca.uhn.fhir.jpa.mdm.dao.testmodels;

import java.util.ArrayList;
import java.util.List;

public class ResourceMetricTestParams {
	private String myInitialState;

	private List<String> myBlockedResourceGoldenResourceIds;

	private long myExpectedResourceCount;

	private long myExpectedGoldenResourceCount;

	public String getInitialState() {
		return myInitialState;
	}

	public void setInitialState(String theInitialState) {
		myInitialState = theInitialState;
	}

	public List<String> getBlockedResourceGoldenResourceIds() {
		if (myBlockedResourceGoldenResourceIds == null) {
			myBlockedResourceGoldenResourceIds = new ArrayList<>();
		}
		return myBlockedResourceGoldenResourceIds;
	}

	public void addBlockedResourceGoldenResources(String theBlockedResourceId) {
		getBlockedResourceGoldenResourceIds().add(theBlockedResourceId);
	}

	public long getExpectedResourceCount() {
		return myExpectedResourceCount;
	}

	public void setExpectedResourceCount(long theExpectedResourceCount) {
		myExpectedResourceCount = theExpectedResourceCount;
	}

	public long getExpectedGoldenResourceCount() {
		return myExpectedGoldenResourceCount;
	}

	public void setExpectedGoldenResourceCount(long theExpectedGoldenResourceCount) {
		myExpectedGoldenResourceCount = theExpectedGoldenResourceCount;
	}

	public long getExpectedBlockedResourceCount() {
		return getBlockedResourceGoldenResourceIds().size();
	}

}
