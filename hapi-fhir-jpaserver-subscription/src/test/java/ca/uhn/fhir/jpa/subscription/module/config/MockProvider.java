package ca.uhn.fhir.jpa.subscription.module.config;

import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.server.SimpleBundleProvider;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

public class MockProvider {
	private IBundleProvider myBundleProvider = new SimpleBundleProvider();
	private int myFailCount = 0;
	private IBaseResource myReadResource;

	public void setBundleProvider(IBundleProvider theBundleProvider) {
		myBundleProvider = theBundleProvider;
	}
	public void setReadResource(IBaseResource theReadResource) {
		myReadResource = theReadResource;
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

	public IBaseResource read(IIdType theId) {
		return myReadResource;
	}
}
