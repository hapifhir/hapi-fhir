package ca.uhn.fhir.jpa.subscription.module;

import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.interceptor.executor.InterceptorService;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.searchparam.config.SearchParamConfig;
import ca.uhn.fhir.jpa.searchparam.registry.SearchParamRegistryImpl;
import ca.uhn.fhir.jpa.subscription.channel.api.ChannelConsumerSettings;
import ca.uhn.fhir.jpa.subscription.channel.api.ChannelProducerSettings;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelFactory;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelProducer;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelReceiver;
import ca.uhn.fhir.jpa.subscription.channel.impl.LinkedBlockingChannelFactory;
import ca.uhn.fhir.jpa.subscription.channel.subscription.IChannelNamer;
import ca.uhn.fhir.jpa.subscription.channel.subscription.SubscriptionChannelFactory;
import ca.uhn.fhir.jpa.subscription.match.config.SubscriptionProcessorConfig;
import ca.uhn.fhir.jpa.subscription.module.config.MockFhirClientSearchParamProvider;
import ca.uhn.fhir.model.primitive.IdDt;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
	SearchParamConfig.class,
	SubscriptionProcessorConfig.class,
	BaseSubscriptionTest.MyConfig.class
})
public abstract class BaseSubscriptionTest {

	static {
		System.setProperty("unit_test_mode", "true");
	}

	@Autowired
	protected IInterceptorService myInterceptorRegistry;

	@Autowired
	SearchParamRegistryImpl mySearchParamRegistry;

	@Autowired
	MockFhirClientSearchParamProvider myMockFhirClientSearchParamProvider;

	@BeforeEach
	public void before() {
		mySearchParamRegistry.handleInit(Collections.emptyList());
	}

	@AfterEach
	public void afterClearAnonymousLambdas() {
		myInterceptorRegistry.unregisterAllInterceptors();
	}

	public void initSearchParamRegistry(IBaseResource theReadResource) {
		myMockFhirClientSearchParamProvider.setReadResource(theReadResource);
		mySearchParamRegistry.handleInit(Collections.singletonList(new IdDt()));
	}

	@Configuration
	public static class MyConfig {

		@Bean
		public DaoConfig daoConfig() {
			return new DaoConfig();
		}

		@Bean
		public IChannelFactory channelFactory(IChannelNamer theNamer) {
			return new LinkedBlockingChannelFactory(theNamer);
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
