package ca.uhn.fhir.jpa.subscription.module;

import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamProvider;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamRegistry;
import ca.uhn.fhir.jpa.subscription.module.config.MockSearchParamProviderFhirClient;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public abstract class BaseSubscriptionTest {

	@Autowired
	ISearchParamProvider mySearchParamProvider;

	@Autowired
	ISearchParamRegistry mySearchParamRegistry;

	public void setSearchParamBundleResponse(IBundleProvider theBundleProvider) {
		((MockSearchParamProviderFhirClient)mySearchParamProvider).setBundleProvider(theBundleProvider);
		mySearchParamRegistry.forceRefresh();
	}
}
