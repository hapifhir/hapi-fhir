package ca.uhn.fhir.jpa.subscription.module;

import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamProvider;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamRegistry;
import ca.uhn.fhir.jpa.subscription.module.cache.ISubscriptionProvider;
import ca.uhn.fhir.jpa.subscription.module.cache.SubscriptionLoader;
import ca.uhn.fhir.jpa.subscription.module.config.MockFhirClientSearchParamProvider;
import ca.uhn.fhir.jpa.subscription.module.config.MockFhirClientSubscriptionProvider;
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

	@Autowired
	ISubscriptionProvider mySubscriptionProvider;

	@Autowired
	SubscriptionLoader mySubscriptionLoader;

	public void initSearchParamRegistry(IBundleProvider theBundleProvider) {
		((MockFhirClientSearchParamProvider)mySearchParamProvider).setBundleProvider(theBundleProvider);
		mySearchParamRegistry.forceRefresh();
	}

	public void initSubscriptionLoader(IBundleProvider theBundleProvider) {
		((MockFhirClientSubscriptionProvider)mySubscriptionProvider).setBundleProvider(theBundleProvider);
		mySubscriptionLoader.doInitSubscriptionsForUnitTest();
	}

}
