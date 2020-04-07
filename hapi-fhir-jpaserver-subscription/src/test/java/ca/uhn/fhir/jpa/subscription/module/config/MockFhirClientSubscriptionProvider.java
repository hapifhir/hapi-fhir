package ca.uhn.fhir.jpa.subscription.module.config;

import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.subscription.match.registry.ISubscriptionProvider;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import org.hl7.fhir.instance.model.api.IBaseResource;

public class MockFhirClientSubscriptionProvider implements ISubscriptionProvider {
	private final MockProvider myMockProvider = new MockProvider();

	public MockFhirClientSubscriptionProvider() {
		super();
	}

	public void setBundleProvider(IBundleProvider theBundleProvider) { myMockProvider.setBundleProvider(theBundleProvider); }
	public void setFailCount(int theFailCount) { myMockProvider.setFailCount(theFailCount); }
	public int getFailCount() { return myMockProvider.getFailCount(); }

	@Override
	public IBundleProvider search(SearchParameterMap theParams) { return myMockProvider.search(theParams); }

	@Override
	public boolean loadSubscription(IBaseResource theResource) {
		throw new UnsupportedOperationException();
	}
}
