package ca.uhn.fhir.jpa.subscription.module.subscriber.websocket;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.cache.IResourceChangeListenerRegistry;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.matcher.InMemoryResourceMatcher;
import ca.uhn.fhir.jpa.searchparam.matcher.SearchParamMatcher;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import ca.uhn.fhir.jpa.subscription.channel.config.SubscriptionChannelConfig;
import ca.uhn.fhir.jpa.subscription.channel.subscription.SubscriptionChannelFactory;
import ca.uhn.fhir.jpa.subscription.match.config.SubscriptionProcessorConfig;
import ca.uhn.fhir.jpa.subscription.match.deliver.websocket.WebsocketConnectionValidator;
import ca.uhn.fhir.jpa.subscription.match.deliver.websocket.WebsocketValidationResponse;
import ca.uhn.fhir.jpa.subscription.match.registry.ActiveSubscription;
import ca.uhn.fhir.jpa.subscription.match.registry.SubscriptionRegistry;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscription;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscriptionChannelType;
import org.hl7.fhir.r4.model.IdType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class WebsocketConnectionValidatorTest {
	public static String RESTHOOK_SUBSCRIPTION_ID = "1";
	public static String WEBSOCKET_SUBSCRIPTION_ID = "2";
	public static String NON_EXISTENT_SUBSCRIPTION_ID = "3";

	@MockBean
	MatchUrlService myMatchUrlService;
	@MockBean
	DaoRegistry myDaoRegistry;
	@MockBean
	PlatformTransactionManager myPlatformTransactionManager;
	@MockBean
	SearchParamMatcher mySearchParamMatcher;
	@MockBean
	SubscriptionChannelConfig mySubscriptionChannelConfig;
	@MockBean
	SubscriptionChannelFactory mySubscriptionChannelFactory;
	@MockBean
	IInterceptorBroadcaster myInterceptorBroadcaster;
	@MockBean
	InMemoryResourceMatcher myInMemoryResourceMatcher;
	@MockBean
	ISchedulerService mySchedulerService;
	@MockBean
	SubscriptionRegistry mySubscriptionRegistry;
	@MockBean
	ISearchParamRegistry mySearchParamRegistry;

	@Autowired
	WebsocketConnectionValidator myWebsocketConnectionValidator;
	@Autowired
	IResourceChangeListenerRegistry myResourceChangeListenerRegistry;

	@BeforeEach
	public void before() {
		CanonicalSubscription resthookSubscription = new CanonicalSubscription();
		resthookSubscription.setChannelType(CanonicalSubscriptionChannelType.RESTHOOK);
		ActiveSubscription resthookActiveSubscription = new ActiveSubscription(resthookSubscription, null);
		when(mySubscriptionRegistry.get(RESTHOOK_SUBSCRIPTION_ID)).thenReturn(resthookActiveSubscription);

		CanonicalSubscription websocketSubscription = new CanonicalSubscription();
		websocketSubscription.setChannelType(CanonicalSubscriptionChannelType.WEBSOCKET);
		ActiveSubscription websocketActiveSubscription = new ActiveSubscription(websocketSubscription, null);
		when(mySubscriptionRegistry.get(WEBSOCKET_SUBSCRIPTION_ID)).thenReturn(websocketActiveSubscription);
	}

	@Test
	public void validateRequest() {
		IdType idType;
		WebsocketValidationResponse response;

		idType = new IdType();
		response = myWebsocketConnectionValidator.validate(idType);
		assertFalse(response.isValid());
		assertEquals("Invalid bind request - No ID included: null", response.getMessage());

		idType = new IdType(NON_EXISTENT_SUBSCRIPTION_ID);
		response = myWebsocketConnectionValidator.validate(idType);
		assertFalse(response.isValid());
		assertEquals("Invalid bind request - Unknown subscription: Subscription/" + NON_EXISTENT_SUBSCRIPTION_ID, response.getMessage());

		idType = new IdType(RESTHOOK_SUBSCRIPTION_ID);
		response = myWebsocketConnectionValidator.validate(idType);
		assertFalse(response.isValid());
		assertEquals("Subscription Subscription/" + RESTHOOK_SUBSCRIPTION_ID + " is not a WEBSOCKET subscription", response.getMessage());

		idType = new IdType(WEBSOCKET_SUBSCRIPTION_ID);
		response = myWebsocketConnectionValidator.validate(idType);
		assertTrue(response.isValid());
	}

	@Configuration
	public static class SpringConfig extends SubscriptionProcessorConfig {

		@Bean
		public DaoConfig daoConfig() {
			return new DaoConfig();
		}

		@Bean
		public PartitionSettings partitionSettings() {
			return new PartitionSettings();
		}

		@Bean
		public ModelConfig modelConfig() {
			return new ModelConfig();
		}

		@Bean
		public FhirContext fhirContext() {
			return FhirContext.forR4();
		}

		@Bean("hapiJpaTaskExecutor")
		public AsyncTaskExecutor taskExecutor() {
			return mock(AsyncTaskExecutor.class);
		}

		@Bean
		public WebsocketConnectionValidator websocketConnectionValidator() {
			return new WebsocketConnectionValidator();
		}

		@Bean
		public IResourceChangeListenerRegistry resourceChangeListenerRegistry() {
			return mock(IResourceChangeListenerRegistry.class, RETURNS_DEEP_STUBS);
		}

	}
}
