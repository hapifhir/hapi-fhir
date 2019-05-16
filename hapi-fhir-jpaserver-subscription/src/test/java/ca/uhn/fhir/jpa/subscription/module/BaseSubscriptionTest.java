package ca.uhn.fhir.jpa.subscription.module;

import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamRegistry;
import ca.uhn.fhir.jpa.subscription.module.cache.SubscriptionLoader;
import ca.uhn.fhir.jpa.subscription.module.config.MockFhirClientSearchParamProvider;
import ca.uhn.fhir.jpa.subscription.module.config.MockFhirClientSubscriptionProvider;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import org.junit.After;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public abstract class BaseSubscriptionTest {

	@Autowired
	MockFhirClientSubscriptionProvider myMockFhirClientSubscriptionProvider;

	@Autowired
	ISearchParamRegistry mySearchParamRegistry;

	@Autowired
	MockFhirClientSearchParamProvider myMockFhirClientSearchParamProvider;

	@Autowired
	SubscriptionLoader mySubscriptionLoader;

	@Autowired
	protected
	IInterceptorService myInterceptorRegistry;

	@After
	public void afterClearAnonymousLambdas() {
		myInterceptorRegistry.unregisterAllInterceptors();
	}

	public void initSearchParamRegistry(IBundleProvider theBundleProvider) {
		myMockFhirClientSearchParamProvider.setBundleProvider(theBundleProvider);
		mySearchParamRegistry.forceRefresh();
	}

	public void initSubscriptionLoader(IBundleProvider theBundleProvider) {
		myMockFhirClientSubscriptionProvider.setBundleProvider(theBundleProvider);
		mySubscriptionLoader.doSyncSubscriptionsForUnitTest();
	}
}
