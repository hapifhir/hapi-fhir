package ca.uhn.fhir.jpa.subscription.module;

import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.interceptor.executor.InterceptorService;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.searchparam.config.SearchParamConfig;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamRegistry;
import ca.uhn.fhir.jpa.subscription.channel.impl.LinkedBlockingChannelFactory;
import ca.uhn.fhir.jpa.subscription.channel.subscription.IChannelNamer;
import ca.uhn.fhir.jpa.subscription.channel.subscription.SubscriptionChannelFactory;
import ca.uhn.fhir.jpa.subscription.match.config.SubscriptionProcessorConfig;
import ca.uhn.fhir.jpa.subscription.module.config.MockFhirClientSearchParamProvider;
import ca.uhn.fhir.jpa.subscription.module.config.TestSubscriptionConfig;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
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

	@AfterEach
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
		public SubscriptionChannelFactory mySubscriptionChannelFactory(IChannelNamer theChannelNamer) {
			return new SubscriptionChannelFactory(new LinkedBlockingChannelFactory(theChannelNamer));
		}

		@Bean
		public IInterceptorService interceptorService() {
			return new InterceptorService();
		}

		@Bean
		// Default implementation returns the name unchanged
		public IChannelNamer channelNamer() {
			return (theNameComponent, theChannelSettings) -> theNameComponent;
		}
	}
}
