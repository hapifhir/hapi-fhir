package ca.uhn.fhir.jpa.subscription.module;

import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.interceptor.executor.InterceptorService;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.searchparam.config.SearchParamConfig;
import ca.uhn.fhir.jpa.searchparam.registry.SearchParamRegistryImpl;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelFactory;
import ca.uhn.fhir.jpa.subscription.channel.impl.LinkedBlockingChannelFactory;
import ca.uhn.fhir.jpa.subscription.channel.subscription.IChannelNamer;
import ca.uhn.fhir.jpa.subscription.channel.subscription.SubscriptionChannelFactory;
import ca.uhn.fhir.jpa.subscription.match.config.SubscriptionProcessorConfig;
import ca.uhn.fhir.jpa.subscription.match.deliver.email.IEmailSender;
import ca.uhn.fhir.jpa.subscription.module.config.MockFhirClientSearchParamProvider;
import ca.uhn.fhir.jpa.model.config.SubscriptionSettings;
import ca.uhn.fhir.jpa.subscription.util.SubscriptionDebugLogInterceptor;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.system.HapiSystemProperties;
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

import static org.mockito.Mockito.mock;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
	SearchParamConfig.class,
	SubscriptionProcessorConfig.class,
	BaseSubscriptionTest.MyConfig.class
})
public abstract class BaseSubscriptionTest {
	private static final SubscriptionDebugLogInterceptor ourSubscriptionDebugLogInterceptor = new SubscriptionDebugLogInterceptor();

	static {
		HapiSystemProperties.enableUnitTestMode();
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
		myInterceptorRegistry.registerInterceptor(ourSubscriptionDebugLogInterceptor);
	}

	@AfterEach
	public void afterClearAnonymousLambdas() {
		myInterceptorRegistry.unregisterAllInterceptors();
		myInterceptorRegistry.unregisterInterceptor(ourSubscriptionDebugLogInterceptor);
	}

	public void initSearchParamRegistry(IBaseResource theReadResource) {
		myMockFhirClientSearchParamProvider.setReadResource(theReadResource);
		mySearchParamRegistry.handleInit(Collections.singletonList(new IdDt()));
	}

	@Configuration
	public static class MyConfig {

		@Bean
		public JpaStorageSettings jpaStorageSettings() {
			return new JpaStorageSettings();
		}

		@Bean
		public SubscriptionSettings subscriptionSettings() {
			return new SubscriptionSettings();
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
		public IRequestPartitionHelperSvc requestPartitionHelperSvc() {
			return mock(IRequestPartitionHelperSvc.class);
		}

		@Bean
		// Default implementation returns the name unchanged
		public IChannelNamer channelNamer() {
			return (theNameComponent, theChannelSettings) -> theNameComponent;
		}

		@Bean
		public IEmailSender emailSender(){
			return mock(IEmailSender.class);
		}
	}
}
