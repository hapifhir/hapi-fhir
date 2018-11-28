package ca.uhn.fhir.jpa.subscription.config;

import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.subscription.FhirClientSearchParamProvider;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.server.SimpleBundleProvider;

public class MockSearchParamProvider extends FhirClientSearchParamProvider {
	private IBundleProvider myBundleProvider = new SimpleBundleProvider();

	public MockSearchParamProvider() {
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
