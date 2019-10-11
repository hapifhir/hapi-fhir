package ca.uhn.fhir.jpa.subscription.module.channel;

import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.subscription.module.CanonicalSubscription;
import ca.uhn.fhir.jpa.subscription.module.cache.ActiveSubscription;
import ca.uhn.fhir.model.primitive.IdDt;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class SubscriptionChannelRegistryTest {
	private static final String TEST_CHANNEL_NAME = "TEST_CHANNEL";
	@Autowired
	SubscriptionChannelRegistry mySubscriptionChannelRegistry;
	@MockBean
	SubscriptionDeliveryHandlerFactory mySubscriptionDeliveryHandlerFactory;
	@MockBean
	SubscriptionChannelFactory mySubscriptionDeliveryChannelFactory;
	@MockBean
	ModelConfig myModelConfig;

	@Configuration
	static class SpringConfig {
		@Bean
		SubscriptionChannelRegistry subscriptionChannelRegistry() {
			return new SubscriptionChannelRegistry();
		}
	}


	@Test
	public void testAddAddRemoveRemove() {
		when(myModelConfig.isSubscriptionMatchingEnabled()).thenReturn(true);

		CanonicalSubscription cansubA = new CanonicalSubscription();
		cansubA.setIdElement(new IdDt("A"));
		ActiveSubscription activeSubscriptionA = new ActiveSubscription(cansubA, TEST_CHANNEL_NAME);
		CanonicalSubscription cansubB = new CanonicalSubscription();
		cansubB.setIdElement(new IdDt("B"));
		ActiveSubscription activeSubscriptionB = new ActiveSubscription(cansubB, TEST_CHANNEL_NAME);

		assertNull(mySubscriptionChannelRegistry.get(TEST_CHANNEL_NAME));
		mySubscriptionChannelRegistry.add(activeSubscriptionA);
		assertNotNull(mySubscriptionChannelRegistry.get(TEST_CHANNEL_NAME));
		mySubscriptionChannelRegistry.add(activeSubscriptionB);
		mySubscriptionChannelRegistry.remove(activeSubscriptionB);
		assertNotNull(mySubscriptionChannelRegistry.get(TEST_CHANNEL_NAME));
		mySubscriptionChannelRegistry.remove(activeSubscriptionA);
		assertNull(mySubscriptionChannelRegistry.get(TEST_CHANNEL_NAME));
	}
}
