package ca.uhn.fhir.jpa.subscription.channel.subscription;

import ca.uhn.fhir.broker.api.BaseChannelSettings;
import ca.uhn.fhir.broker.api.ChannelConsumerSettings;
import ca.uhn.fhir.broker.api.ChannelProducerSettings;
import ca.uhn.fhir.broker.api.IChannelConsumer;
import ca.uhn.fhir.broker.api.IChannelProducer;
import ca.uhn.fhir.broker.api.IMessageListener;
import ca.uhn.fhir.jpa.subscription.match.registry.ActiveSubscription;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscription;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscriptionChannelType;
import ca.uhn.fhir.jpa.subscription.model.ChannelRetryConfiguration;
import ca.uhn.fhir.jpa.subscription.model.ResourceDeliveryMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SubscriptionChannelRegistryTest {

	@Mock
	private SubscriptionDeliveryListenerFactory mySubscriptionDeliveryListenerFactory;

	@Mock
	private SubscriptionChannelFactory mySubscriptionChannelFactory;

	@InjectMocks
	private SubscriptionChannelRegistry mySubscriptionChannelRegistry;

	private ActiveSubscription createActiveSubscription(String theChannelName, int theRetryCount) {
		CanonicalSubscription subscription = new CanonicalSubscription();
		subscription.setChannelType(CanonicalSubscriptionChannelType.RESTHOOK);
		ChannelRetryConfiguration configuration = new ChannelRetryConfiguration();
		configuration.setRetryCount(theRetryCount);
		ActiveSubscription activeSubscription = new ActiveSubscription(subscription, theChannelName);
		activeSubscription.setRetryConfiguration(configuration);
		return activeSubscription;
	}

	@Test
	public void add_subscriptionWithRetryConfigs_createsSendingAndReceivingChannelsWithRetryConfigs() {
		int retryCount = 5;
		String channelName = "test";
		ActiveSubscription activeSubscription = createActiveSubscription(channelName, retryCount);

		// mocks
		IMessageListener<ResourceDeliveryMessage> messageListener = mock(IMessageListener.class);
		when(messageListener.getPayloadType()).thenReturn(ResourceDeliveryMessage.class);
		IChannelConsumer<ResourceDeliveryMessage> consumer = mock(IChannelConsumer.class);
		IChannelProducer<ResourceDeliveryMessage> producer = mock(IChannelProducer.class);

		// when
		when(mySubscriptionChannelFactory.newDeliveryConsumer(
			anyString(),
			any(IMessageListener.class),
			any(ChannelConsumerSettings.class)
		)).thenReturn(consumer);
		when(mySubscriptionChannelFactory.newDeliveryProducer(
			anyString(),
			any(ChannelProducerSettings.class)
		)).thenReturn(producer);
		when(mySubscriptionDeliveryListenerFactory.createDeliveryListener(any(CanonicalSubscriptionChannelType.class)))
			.thenReturn(Optional.of(messageListener));

		// test
		mySubscriptionChannelRegistry.add(activeSubscription);

		// verify
		// the receiver and sender should've been added to the maps
		SubscriptionResourceDeliveryMessageConsumer channelWithListeners = mySubscriptionChannelRegistry.getDeliveryConsumerWithListeners(channelName);
		IChannelProducer<ResourceDeliveryMessage> producer2 = mySubscriptionChannelRegistry.getDeliveryChannelProducer(channelName);

		assertEquals(producer, producer2);
		assertEquals(consumer, channelWithListeners.getConsumer());

		// verify the creation of the sender/receiver
		// both have retry values provided
		ArgumentCaptor<ChannelConsumerSettings> consumerCaptor = ArgumentCaptor.forClass(ChannelConsumerSettings.class);
		verify(mySubscriptionChannelFactory)
			.newDeliveryConsumer(anyString(),
				any(IMessageListener.class),
				consumerCaptor.capture());
		ChannelConsumerSettings consumerSettings = consumerCaptor.getValue();
		verifySettingsHaveRetryConfig(consumerSettings, retryCount);

		ArgumentCaptor<ChannelProducerSettings> producerCaptor = ArgumentCaptor.forClass(ChannelProducerSettings.class);
		verify(mySubscriptionChannelFactory)
			.newDeliveryProducer(anyString(),
				producerCaptor.capture());
		verifySettingsHaveRetryConfig(producerCaptor.getValue(), retryCount);
	}

	/**
	 * Verifies the retry configs for the channel
	 * @param theSettings
	 * @param theRetryCount
	 */
	private void verifySettingsHaveRetryConfig(BaseChannelSettings theSettings, int theRetryCount) {
		assertNotNull(theSettings);
		assertNotNull(theSettings.getRetryConfigurationParameters());
		assertEquals(theRetryCount, theSettings.getRetryConfigurationParameters().getRetryCount());
	}
}
