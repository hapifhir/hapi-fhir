package ca.uhn.fhir.jpa.subscription.module.channel;

import ca.uhn.fhir.broker.api.IChannelConsumer;
import ca.uhn.fhir.broker.api.IChannelProducer;
import ca.uhn.fhir.broker.impl.MultiplexingListener;
import ca.uhn.fhir.jpa.model.entity.StorageSettings;
import ca.uhn.fhir.jpa.subscription.api.ISubscriptionDeliveryValidator;
import ca.uhn.fhir.jpa.subscription.channel.subscription.SubscriptionChannelFactory;
import ca.uhn.fhir.jpa.subscription.channel.subscription.SubscriptionChannelRegistry;
import ca.uhn.fhir.jpa.subscription.channel.subscription.SubscriptionDeliveryListenerFactory;
import ca.uhn.fhir.jpa.subscription.match.registry.ActiveSubscription;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscription;
import ca.uhn.fhir.jpa.subscription.model.ResourceDeliveryMessage;
import ca.uhn.fhir.model.primitive.IdDt;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class SubscriptionChannelRegistryTest {
	private static final String TEST_CHANNEL_NAME = "TEST_CHANNEL";
	@Autowired
	SubscriptionChannelRegistry mySubscriptionChannelRegistry;

	@MockBean
	SubscriptionDeliveryListenerFactory mySubscriptionDeliveryListenerFactory;
	@MockBean
	SubscriptionChannelFactory mySubscriptionDeliveryChannelFactory;
	@MockBean
	ISubscriptionDeliveryValidator mySubscriptionDeliveryValidator;
	@MockBean
    StorageSettings myStorageSettings;

	@Test
	public void testAddAddRemoveRemove() {
		CanonicalSubscription cansubA = new CanonicalSubscription();
		cansubA.setIdElement(new IdDt("A"));
		ActiveSubscription activeSubscriptionA = new ActiveSubscription(cansubA, TEST_CHANNEL_NAME);
		CanonicalSubscription cansubB = new CanonicalSubscription();
		cansubB.setIdElement(new IdDt("B"));
		ActiveSubscription activeSubscriptionB = new ActiveSubscription(cansubB, TEST_CHANNEL_NAME);

		when(mySubscriptionDeliveryChannelFactory.newDeliveryProducer(any(), any())).thenAnswer(t -> mock(IChannelProducer.class));
		IChannelConsumer<ResourceDeliveryMessage> consumer = mock(IChannelConsumer.class);
		when(consumer.getMessageListener()).thenReturn(new MultiplexingListener<>(ResourceDeliveryMessage.class));
		when(mySubscriptionDeliveryChannelFactory.newDeliveryConsumer(any(), any(), any())).thenAnswer(t -> consumer);

		assertNull(mySubscriptionChannelRegistry.getDeliveryConsumerWithListeners(TEST_CHANNEL_NAME));
		mySubscriptionChannelRegistry.add(activeSubscriptionA);
		assertNotNull(mySubscriptionChannelRegistry.getDeliveryConsumerWithListeners(TEST_CHANNEL_NAME));
		mySubscriptionChannelRegistry.add(activeSubscriptionB);
		mySubscriptionChannelRegistry.remove(activeSubscriptionB);
		assertNotNull(mySubscriptionChannelRegistry.getDeliveryConsumerWithListeners(TEST_CHANNEL_NAME));
		mySubscriptionChannelRegistry.remove(activeSubscriptionA);
		assertNull(mySubscriptionChannelRegistry.getDeliveryConsumerWithListeners(TEST_CHANNEL_NAME));
	}

	@Configuration
	static class SpringConfig {
		@Bean
		SubscriptionChannelRegistry subscriptionChannelRegistry() {
			return new SubscriptionChannelRegistry();
		}
	}
}
