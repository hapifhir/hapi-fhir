package ca.uhn.fhir.broker.impl;

import ca.uhn.fhir.broker.api.ChannelConsumerSettings;
import ca.uhn.fhir.broker.api.ChannelProducerSettings;
import ca.uhn.fhir.broker.api.IBrokerClient;
import ca.uhn.fhir.broker.api.IChannelConsumer;
import ca.uhn.fhir.broker.api.IChannelNamer;
import ca.uhn.fhir.broker.api.IChannelProducer;
import ca.uhn.fhir.broker.api.IMessageListener;
import ca.uhn.fhir.broker.jms.ILegacyChannelFactory;
import ca.uhn.fhir.broker.jms.ISpringMessagingChannelReceiver;
import ca.uhn.fhir.broker.jms.SpringMessagingMessage;
import ca.uhn.fhir.broker.jms.SpringMessagingProducerAdapter;
import ca.uhn.fhir.broker.jms.SpringMessagingReceiverAdapter;
import org.springframework.messaging.MessageHandler;

// FIXME KHS delete this?
public class LegacyBrokerClient implements IBrokerClient {
	private final ILegacyChannelFactory myLinkedBlockingChannelFactory;

	public LegacyBrokerClient(ILegacyChannelFactory theChannelFactory) {
		myLinkedBlockingChannelFactory = theChannelFactory;
	}

	@Override
	public <T> IChannelConsumer<T> getOrCreateConsumer(
			String theChannelName,
			Class<T> theMessageType,
			IMessageListener<T> theMessageListener,
			ChannelConsumerSettings theChannelConsumerSettings) {
		ISpringMessagingChannelReceiver legacyChannelReceiver = myLinkedBlockingChannelFactory.getOrCreateReceiver(
				theChannelName, theMessageType, theChannelConsumerSettings);
		SpringMessagingReceiverAdapter<T> retval = new SpringMessagingReceiverAdapter<>(legacyChannelReceiver);
		MessageHandler handler = message -> theMessageListener.handleMessage(
				new SpringMessagingMessage<>((org.springframework.messaging.Message<T>) message));
		retval.subscribe(handler);
		return retval;
	}

	@Override
	public <T> IChannelProducer<T> getOrCreateProducer(
			String theChannelName, Class<T> theMessageType, ChannelProducerSettings theChannelProducerSettings) {
		return new SpringMessagingProducerAdapter<>(myLinkedBlockingChannelFactory.getOrCreateProducer(
				theChannelName, theMessageType, theChannelProducerSettings));
	}

	@Override
	public IChannelNamer getChannelNamer() {
		return null;
	}
}
