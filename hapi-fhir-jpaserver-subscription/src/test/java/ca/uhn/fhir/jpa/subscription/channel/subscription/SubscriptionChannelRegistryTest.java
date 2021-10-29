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
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import java.util.Optional;

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
		MessageHandler messageHandler = Mockito.mock(MessageHandler.class);
		IChannelReceiver receiver = Mockito.mock(IChannelReceiver.class);
		IChannelProducer producer = Mockito.mock(IChannelProducer.class);

		// when
		Mockito.when(mySubscriptionChannelFactory.newDeliveryReceivingChannel(
			Mockito.anyString(),
			Mockito.any(ChannelConsumerSettings.class)
		)).thenReturn(receiver);
		Mockito.when(mySubscriptionChannelFactory.newDeliverySendingChannel(
			Mockito.anyString(),
			Mockito.any(ChannelProducerSettings.class)
		)).thenReturn(producer);
		Mockito.when(mySubscriptionDeliveryHandlerFactory.createDeliveryHandler(Mockito.any(CanonicalSubscriptionChannelType.class)))
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
		Mockito.verify(mySubscriptionChannelFactory)
			.newDeliveryReceivingChannel(Mockito.anyString(),
				consumerCaptor.capture());
		ChannelConsumerSettings consumerSettings = consumerCaptor.getValue();
		verifySettingsHaveRetryConfig(consumerSettings, retryCount);

		ArgumentCaptor<ChannelProducerSettings> producerCaptor = ArgumentCaptor.forClass(ChannelProducerSettings.class);
		Mockito.verify(mySubscriptionChannelFactory)
			.newDeliverySendingChannel(Mockito.anyString(),
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
