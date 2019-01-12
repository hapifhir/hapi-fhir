package ca.uhn.fhir.jpa.subscription.module.config;

import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.server.SimpleBundleProvider;

public class MockProvider {
	private IBundleProvider myBundleProvider = new SimpleBundleProvider();
	private int myFailCount = 0;

	public void setBundleProvider(IBundleProvider theBundleProvider) {
		myBundleProvider = theBundleProvider;
	}

	public IBundleProvider search(SearchParameterMap theParams) {
		if (myFailCount > 0) {
			--myFailCount;
			throw new RuntimeException("Mock Search Failed");
		}
		return myBundleProvider;
	}

	public void setFailCount(int theFailCount) {
		myFailCount = theFailCount;
	}

	public int getFailCount() {
		return myFailCount;
	}

}
