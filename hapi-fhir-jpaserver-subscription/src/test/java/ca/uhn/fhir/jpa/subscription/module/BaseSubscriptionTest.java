package ca.uhn.fhir.jpa.subscription.module;

import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.interceptor.executor.InterceptorService;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.searchparam.config.SearchParamConfig;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamRegistry;
import ca.uhn.fhir.jpa.subscription.channel.impl.LinkedBlockingChannelFactory;
import ca.uhn.fhir.jpa.subscription.channel.subscription.SubscriptionChannelFactory;
import ca.uhn.fhir.jpa.subscription.match.config.SubscriptionProcessorConfig;
import ca.uhn.fhir.jpa.subscription.module.config.MockFhirClientSearchParamProvider;
import ca.uhn.fhir.jpa.subscription.module.config.TestSubscriptionConfig;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import org.junit.After;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
	SearchParamConfig.class,
	SubscriptionProcessorConfig.class,
	TestSubscriptionConfig.class,
	BaseSubscriptionTest.MyConfig.class
})
public abstract class BaseSubscriptionTest {

	@Autowired
	protected IInterceptorService myInterceptorRegistry;

	@Autowired
	ISearchParamRegistry mySearchParamRegistry;

	@Autowired
	MockFhirClientSearchParamProvider myMockFhirClientSearchParamProvider;

	@After
	public void afterClearAnonymousLambdas() {
		myInterceptorRegistry.unregisterAllInterceptors();
	}

	public void initSearchParamRegistry(IBundleProvider theBundleProvider) {
		myMockFhirClientSearchParamProvider.setBundleProvider(theBundleProvider);
		mySearchParamRegistry.forceRefresh();
	}

	@Configuration
	public static class MyConfig {

		@Bean
		public DaoConfig daoConfig() {
			return new DaoConfig();
		}

		@Bean
		public SubscriptionChannelFactory mySubscriptionChannelFactory() {
			return new SubscriptionChannelFactory(new LinkedBlockingChannelFactory());
		}

		@Bean
		public IInterceptorService interceptorService() {
			return new InterceptorService();
		}


	}
}
