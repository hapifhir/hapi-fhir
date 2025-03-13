package ca.uhn.fhir.broker.impl;

import ca.uhn.fhir.broker.api.ChannelConsumerSettings;
import ca.uhn.fhir.broker.api.ChannelProducerSettings;
import ca.uhn.fhir.broker.api.IBrokerClient;
import ca.uhn.fhir.broker.api.IChannelConsumer;
import ca.uhn.fhir.broker.api.IChannelNamer;
import ca.uhn.fhir.broker.api.IChannelProducer;
import ca.uhn.fhir.jpa.subscription.channel.impl.LinkedBlockingChannelFactory;

public class LinkedBlockingBrokerClient implements IBrokerClient<Boolean> {
	private final LinkedBlockingChannelFactory myLinkedBlockingChannelFactory;

	public LinkedBlockingBrokerClient(LinkedBlockingChannelFactory theChannelFactory) {
		myLinkedBlockingChannelFactory = theChannelFactory;
	}

	@Override
	public <T> IChannelConsumer<T> getOrCreateConsumer(String theChannelName, Class<T> theMessageType, ChannelConsumerSettings theChannelConsumerSettings) {
		return new LegacyChannelReceiverAdapter(myLinkedBlockingChannelFactory.getOrCreateReceiver(theChannelName, theMessageType, theChannelConsumerSettings));
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
