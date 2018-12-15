package ca.uhn.fhir.jpa.subscription.module.config;

import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.subscription.module.standalone.FhirClientSubscriptionProvider;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.server.SimpleBundleProvider;

public class MockFhirClientSubscriptionProvider extends FhirClientSubscriptionProvider {
	private IBundleProvider myBundleProvider = new SimpleBundleProvider();


	public MockFhirClientSubscriptionProvider() {
		super(null);
	}

	public void setBundleProvider(IBundleProvider theBundleProvider) {
		myBundleProvider = theBundleProvider;
	}

	@Override
	public IBundleProvider search(SearchParameterMap theParams) {
		return myBundleProvider;
	}
}
