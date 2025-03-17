package ca.uhn.fhir.broker.impl;

import ca.uhn.fhir.broker.api.ChannelConsumerSettings;
import ca.uhn.fhir.broker.api.ChannelProducerSettings;
import ca.uhn.fhir.broker.api.IBrokerClient;
import ca.uhn.fhir.broker.api.IChannelConsumer;
import ca.uhn.fhir.broker.api.IChannelNamer;
import ca.uhn.fhir.broker.api.IChannelProducer;
import ca.uhn.fhir.broker.api.IMessageListener;
import ca.uhn.fhir.broker.legacy.ILegacyChannelFactory;
import ca.uhn.fhir.broker.legacy.ILegacyChannelReceiver;
import ca.uhn.fhir.broker.legacy.LegacyChannelProducerAdapter;
import ca.uhn.fhir.broker.legacy.LegacyChannelReceiverAdapter;
import ca.uhn.fhir.broker.legacy.LegacyMessage;
import org.springframework.messaging.MessageHandler;

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
		ILegacyChannelReceiver legacyChannelReceiver = myLinkedBlockingChannelFactory.getOrCreateReceiver(
				theChannelName, theMessageType, theChannelConsumerSettings);
		LegacyChannelReceiverAdapter<T> retval = new LegacyChannelReceiverAdapter<>(legacyChannelReceiver);
		MessageHandler handler = message -> theMessageListener.handleMessage(
				new LegacyMessage<>((org.springframework.messaging.Message<T>) message));
		retval.subscribe(handler);
		return retval;
	}

	@Override
	public <T> IChannelProducer<T> getOrCreateProducer(
			String theChannelName, Class<T> theMessageType, ChannelProducerSettings theChannelProducerSettings) {
		return new LegacyChannelProducerAdapter<>(myLinkedBlockingChannelFactory.getOrCreateProducer(
				theChannelName, theMessageType, theChannelProducerSettings));
	}

	@Override
	public IChannelNamer getChannelNamer() {
		return null;
	}
}
