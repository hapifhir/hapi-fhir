package ca.uhn.fhir.jpa.subscription.channel.subscription;

import ca.uhn.fhir.jpa.subscription.channel.api.BaseChannelSettings;
import ca.uhn.fhir.jpa.subscription.channel.api.ChannelConsumerSettings;
import ca.uhn.fhir.jpa.subscription.channel.api.ChannelProducerSettings;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelProducer;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelReceiver;
import ca.uhn.fhir.jpa.subscription.match.registry.ActiveSubscription;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscription;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscriptionChannelType;
import ca.uhn.fhir.jpa.subscription.model.ChannelRetryConfiguration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SubscriptionChannelRegistryTest {

	@Mock
	private SubscriptionDeliveryHandlerFactory mySubscriptionDeliveryHandlerFactory;

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
		MessageHandler messageHandler = mock(MessageHandler.class);
		IChannelReceiver receiver = mock(IChannelReceiver.class);
		IChannelProducer producer = mock(IChannelProducer.class);

		// when
		when(mySubscriptionChannelFactory.newDeliveryReceivingChannel(
			anyString(),
			any(ChannelConsumerSettings.class)
		)).thenReturn(receiver);
		when(mySubscriptionChannelFactory.newDeliverySendingChannel(
			anyString(),
			any(ChannelProducerSettings.class)
		)).thenReturn(producer);
		when(mySubscriptionDeliveryHandlerFactory.createDeliveryHandler(any(CanonicalSubscriptionChannelType.class)))
			.thenReturn(Optional.of(messageHandler));

		// test
		mySubscriptionChannelRegistry.add(activeSubscription);

		// verify
		// the receiver and sender should've been added to the maps
		SubscriptionChannelWithHandlers receiverChannel = mySubscriptionChannelRegistry.getDeliveryReceiverChannel(channelName);
		MessageChannel senderChannel = mySubscriptionChannelRegistry.getDeliverySenderChannel(channelName);

		Assertions.assertEquals(producer, senderChannel);
		Assertions.assertEquals(receiver, receiverChannel.getChannel());

		// verify the creation of the sender/receiver
		// both have retry values provided
		ArgumentCaptor<ChannelConsumerSettings> consumerCaptor = ArgumentCaptor.forClass(ChannelConsumerSettings.class);
		verify(mySubscriptionChannelFactory)
			.newDeliveryReceivingChannel(anyString(),
				consumerCaptor.capture());
		ChannelConsumerSettings consumerSettings = consumerCaptor.getValue();
		verifySettingsHaveRetryConfig(consumerSettings, retryCount);

		ArgumentCaptor<ChannelProducerSettings> producerCaptor = ArgumentCaptor.forClass(ChannelProducerSettings.class);
		verify(mySubscriptionChannelFactory)
			.newDeliverySendingChannel(anyString(),
				producerCaptor.capture());
		verifySettingsHaveRetryConfig(producerCaptor.getValue(), retryCount);
	}

	/**
	 * Verifies the retry configs for the channel
	 * @param theSettings
	 * @param theRetryCount
	 */
	private void verifySettingsHaveRetryConfig(BaseChannelSettings theSettings, int theRetryCount) {
		Assertions.assertNotNull(theSettings);
		Assertions.assertNotNull(theSettings.getRetryConfigurationParameters());
		Assertions.assertEquals(theRetryCount, theSettings.getRetryConfigurationParameters().getRetryCount());
	}
}
