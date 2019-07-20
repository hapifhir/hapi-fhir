package ca.uhn.fhir.jpa.subscription.module.subscriber.websocket;

import ca.uhn.fhir.jpa.subscription.module.CanonicalSubscription;
import ca.uhn.fhir.jpa.subscription.module.CanonicalSubscriptionChannelType;
import ca.uhn.fhir.jpa.subscription.module.cache.ActiveSubscription;
import ca.uhn.fhir.jpa.subscription.module.cache.SubscriptionRegistry;
import org.hl7.fhir.r4.model.IdType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class WebsocketConnectionValidatorTest {
	public static String RESTHOOK_SUBSCRIPTION_ID = "1";
	public static String WEBSOCKET_SUBSCRIPTION_ID = "2";
	public static String NON_EXISTENT_SUBSCRIPTION_ID = "3";

	@Configuration
	@ComponentScan("ca.uhn.fhir.jpa.subscription.module.subscriber.websocket")
	public static class SpringConfig {
	}

	@MockBean
	SubscriptionRegistry mySubscriptionRegistry;

	@Autowired
	WebsocketConnectionValidator myWebsocketConnectionValidator;

	@Before
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
}
