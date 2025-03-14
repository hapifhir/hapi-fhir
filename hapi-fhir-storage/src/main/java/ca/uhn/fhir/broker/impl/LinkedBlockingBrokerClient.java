package ca.uhn.fhir.broker.impl;

import ca.uhn.fhir.broker.api.ChannelConsumerSettings;
import ca.uhn.fhir.broker.api.ChannelProducerSettings;
import ca.uhn.fhir.broker.api.IBrokerClient;
import ca.uhn.fhir.broker.api.IChannelConsumer;
import ca.uhn.fhir.broker.api.IChannelNamer;
import ca.uhn.fhir.broker.api.IChannelProducer;
import ca.uhn.fhir.broker.api.IMessageListener;
import ca.uhn.fhir.jpa.subscription.channel.api.ILegacyChannelReceiver;
import ca.uhn.fhir.jpa.subscription.channel.impl.LinkedBlockingChannelFactory;
import org.springframework.messaging.MessageHandler;

public class LinkedBlockingBrokerClient implements IBrokerClient<Boolean> {
	private final LinkedBlockingChannelFactory myLinkedBlockingChannelFactory;

	public LinkedBlockingBrokerClient(LinkedBlockingChannelFactory theChannelFactory) {
		myLinkedBlockingChannelFactory = theChannelFactory;
	}

	@Override
	public <T> IChannelConsumer<T> getOrCreateConsumer(String theChannelName, Class<T> theMessageType, IMessageListener<T> theMessageListener, ChannelConsumerSettings theChannelConsumerSettings) {
		ILegacyChannelReceiver legacyChannelReceiver = myLinkedBlockingChannelFactory.getOrCreateReceiver(theChannelName, theMessageType, theChannelConsumerSettings);
		LegacyChannelReceiverAdapter<T> retval = new LegacyChannelReceiverAdapter<>(legacyChannelReceiver);
		MessageHandler handler = message -> theMessageListener.received(retval, new LegacyMessage<>((org.springframework.messaging.Message<T>)message));
		retval.subscribe(handler);
		return retval;
	}

	@Override
	public <T> IChannelProducer<T, Boolean> getOrCreateProducer(String theChannelName, Class<T> theMessageType, ChannelProducerSettings theChannelProducerSettings) {
		return new LegacyChannelProducerAdapter<>(myLinkedBlockingChannelFactory.getOrCreateProducer(theChannelName, theMessageType, theChannelProducerSettings));
	}

	@Override
	public IChannelNamer getChannelNamer() {
		return null;
	}
}
