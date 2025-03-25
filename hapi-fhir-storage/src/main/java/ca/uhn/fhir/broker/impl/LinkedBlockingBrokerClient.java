package ca.uhn.fhir.broker.impl;

import ca.uhn.fhir.broker.api.ChannelConsumerSettings;
import ca.uhn.fhir.broker.api.ChannelProducerSettings;
import ca.uhn.fhir.broker.api.IBrokerClient;
import ca.uhn.fhir.broker.api.IChannelConsumer;
import ca.uhn.fhir.broker.api.IChannelNamer;
import ca.uhn.fhir.broker.api.IChannelProducer;
import ca.uhn.fhir.broker.api.IMessageListener;
import ca.uhn.fhir.broker.jms.SpringMessagingMessageHandlerAdapter;
import ca.uhn.fhir.broker.jms.SpringMessagingProducerAdapter;
import ca.uhn.fhir.broker.jms.SpringMessagingReceiverAdapter;
import ca.uhn.fhir.jpa.subscription.channel.impl.LinkedBlockingChannel;
import ca.uhn.fhir.jpa.subscription.channel.impl.LinkedBlockingChannelFactory;
import ca.uhn.fhir.rest.server.messaging.IMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHandler;

public class LinkedBlockingBrokerClient implements IBrokerClient {
	private LinkedBlockingChannelFactory myLinkedBlockingChannelFactory;
	private final IChannelNamer myChannelNamer;

	public LinkedBlockingBrokerClient(IChannelNamer theChannelNamer) {
		myChannelNamer = theChannelNamer;
	}

	@Override
	public <T> IChannelConsumer<T> getOrCreateConsumer(
			String theChannelName,
			Class<? extends IMessage<T>> theMessageType,
			IMessageListener<T> theMessageListener,
			ChannelConsumerSettings theChannelConsumerSettings) {
		LinkedBlockingChannel legacyChannelReceiver =
				myLinkedBlockingChannelFactory.getOrCreateReceiver(theChannelName, theChannelConsumerSettings);
		SpringMessagingReceiverAdapter<T> retval =
				new SpringMessagingReceiverAdapter<>(theMessageType, legacyChannelReceiver, theMessageListener);
		MessageHandler handler = new SpringMessagingMessageHandlerAdapter<>(theMessageType, theMessageListener);
		retval.subscribe(handler);
		return retval;
	}

	@Override
	public <T> IChannelProducer<T> getOrCreateProducer(
			String theChannelName,
			Class<? extends IMessage<T>> theMessageType,
			ChannelProducerSettings theChannelProducerSettings) {
		return new SpringMessagingProducerAdapter<>(
				myLinkedBlockingChannelFactory.getOrCreateProducer(theChannelName, theChannelProducerSettings));
	}

	@Override
	public IChannelNamer getChannelNamer() {
		return myChannelNamer;
	}

	@Autowired
	public void setLinkedBlockingChannelFactory(LinkedBlockingChannelFactory theLegacyChannelFactory) {
		myLinkedBlockingChannelFactory = theLegacyChannelFactory;
	}
}
