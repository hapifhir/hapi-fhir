package ca.uhn.fhir.jpa.subscription.channel.subscription;

import ca.uhn.fhir.broker.api.ChannelProducerSettings;
import ca.uhn.fhir.broker.api.IChannelConsumer;
import ca.uhn.fhir.broker.api.IChannelNamer;
import ca.uhn.fhir.broker.api.IChannelProducer;
import ca.uhn.fhir.broker.api.IMessageListener;
import ca.uhn.fhir.broker.impl.LinkedBlockingBrokerClient;
import ca.uhn.fhir.broker.jms.SpringMessagingProducerAdapter;
import ca.uhn.fhir.jpa.subscription.channel.impl.LinkedBlockingChannelFactory;
import ca.uhn.fhir.jpa.subscription.channel.impl.RetryPolicyProvider;
import ca.uhn.fhir.jpa.subscription.model.ResourceDeliveryJsonMessage;
import ca.uhn.fhir.jpa.subscription.model.ResourceDeliveryMessage;
import ca.uhn.fhir.rest.server.messaging.IMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.support.ChannelInterceptor;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SubscriptionChannelFactoryTest {

	private SubscriptionChannelFactory mySvc;

	@Mock
	private IChannelNamer myChannelNamer;
	@Captor
	private ArgumentCaptor<Exception> myExceptionCaptor;
	private LinkedBlockingChannelFactory myLinkedBlockingChannelFactory;

	@BeforeEach
	public void before() {
		when(myChannelNamer.getChannelName(any(), any())).thenReturn("CHANNEL_NAME");
		myLinkedBlockingChannelFactory = new LinkedBlockingChannelFactory(myChannelNamer, new RetryPolicyProvider());
		LinkedBlockingBrokerClient brokerClient = new LinkedBlockingBrokerClient(myChannelNamer);
		brokerClient.setLinkedBlockingChannelFactory(myLinkedBlockingChannelFactory);
		mySvc = new SubscriptionChannelFactory(brokerClient);
	}

	/**
	 * Make sure the channel doesn't silently swallow exceptions
	 */
	@Test
	@Disabled // This case no longer applies now that we moved the multiplexer from the sender to the receiver
	public void testInterceptorsOnChannelWrapperArePropagated() {

		IChannelProducer<ResourceDeliveryMessage> producer = mySvc.newDeliveryProducer("CHANNEL_NAME", new ChannelProducerSettings());
		IMessageListener<ResourceDeliveryMessage> listener = new NpeThrowingListener();
		try (IChannelConsumer<ResourceDeliveryMessage> consumer = mySvc.newDeliveryConsumer("CHANNEL_NAME", listener,null)) {
			ChannelInterceptor channelInterceptor = new ChannelInterceptor() {
				@Override
				public Message<?> preSend(Message<?> message, MessageChannel channel) {
					return message;
				}
			};
			((SpringMessagingProducerAdapter) producer).addInterceptor(channelInterceptor);

			ResourceDeliveryJsonMessage message = new ResourceDeliveryJsonMessage();

			try {
				producer.send(message);
				fail("");
			} catch (MessageDeliveryException e) {
				assertTrue(e.getCause() instanceof NullPointerException);
			}

			verify(channelInterceptor, times(1)).afterSendCompletion(any(), any(), anyBoolean(), myExceptionCaptor.capture());

			assertTrue(myExceptionCaptor.getValue() instanceof NullPointerException);
		}
	}


	private class NpeThrowingListener implements IMessageListener<ResourceDeliveryMessage> {
		@Override
		public void handleMessage(IMessage<ResourceDeliveryMessage> theMessage) {
			throw new NullPointerException("THIS IS THE MESSAGE");
		}

		@Override
		public Class<ResourceDeliveryMessage> getPayloadType() {
			return ResourceDeliveryMessage.class;
		}
	}
}
