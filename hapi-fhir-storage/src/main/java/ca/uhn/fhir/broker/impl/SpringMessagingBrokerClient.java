package ca.uhn.fhir.broker.impl;

import ca.uhn.fhir.broker.api.ChannelConsumerSettings;
import ca.uhn.fhir.broker.api.ChannelProducerSettings;
import ca.uhn.fhir.broker.api.IBrokerClient;
import ca.uhn.fhir.broker.api.IChannelConsumer;
import ca.uhn.fhir.broker.api.IChannelNamer;
import ca.uhn.fhir.broker.api.IChannelProducer;
import ca.uhn.fhir.broker.api.IMessageListener;
import ca.uhn.fhir.broker.jms.SpringMessagingChannelFactory;
import ca.uhn.fhir.broker.jms.ISpringMessagingChannelReceiver;
import ca.uhn.fhir.broker.jms.SpringMessagingMessageHandlerAdapter;
import ca.uhn.fhir.broker.jms.SpringMessagingProducerAdapter;
import ca.uhn.fhir.broker.jms.SpringMessagingReceiverAdapter;
import ca.uhn.fhir.rest.server.messaging.IMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHandler;

public class SpringMessagingBrokerClient implements IBrokerClient {
	private SpringMessagingChannelFactory myLinkedBlockingChannelFactory;
	private final IChannelNamer myChannelNamer;

	public SpringMessagingBrokerClient(IChannelNamer theChannelNamer) {
		myChannelNamer = theChannelNamer;
	}

	@Override
	public <T> IChannelConsumer<T> getOrCreateConsumer(
			String theChannelName,
			Class<? extends IMessage<T>> theMessageType,
			IMessageListener<T> theMessageListener,
			ChannelConsumerSettings theChannelConsumerSettings) {
		ISpringMessagingChannelReceiver legacyChannelReceiver = myLinkedBlockingChannelFactory.getOrCreateReceiver(
				theChannelName, theMessageType, theChannelConsumerSettings);
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
		return new SpringMessagingProducerAdapter<>(myLinkedBlockingChannelFactory.getOrCreateProducer(
				theChannelName, theMessageType, theChannelProducerSettings));
	}

	@Override
	public IChannelNamer getChannelNamer() {
		return myChannelNamer;
	}

	@Autowired
	public void setLegacyChannelFactory(SpringMessagingChannelFactory theLegacyChannelFactory) {
		myLinkedBlockingChannelFactory = theLegacyChannelFactory;
	}
}
