package ca.uhn.fhir.jpa.subscription.module.config;

import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.subscription.module.standalone.FhirClientSubscriptionProvider;
import ca.uhn.fhir.rest.api.server.IBundleProvider;

public class MockFhirClientSubscriptionProvider extends FhirClientSubscriptionProvider {
	private final MockProvider myMockProvider = new MockProvider();

	public MockFhirClientSubscriptionProvider() {
		super(null);
	}

	public void setBundleProvider(IBundleProvider theBundleProvider) { myMockProvider.setBundleProvider(theBundleProvider); }
	public void setFailCount(int theFailCount) { myMockProvider.setFailCount(theFailCount); }
	public int getFailCount() { return myMockProvider.getFailCount(); }

	@Override
	public IBundleProvider search(SearchParameterMap theParams) { return myMockProvider.search(theParams); }
}
