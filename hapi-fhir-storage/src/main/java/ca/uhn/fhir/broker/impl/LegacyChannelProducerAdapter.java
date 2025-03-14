package ca.uhn.fhir.broker.impl;

import ca.uhn.fhir.broker.api.IChannelProducer;
import ca.uhn.fhir.broker.api.IMessage;
import ca.uhn.fhir.jpa.subscription.channel.api.ILegacyChannelProducer;
import org.springframework.messaging.Message;

public class LegacyChannelProducerAdapter<T> implements IChannelProducer<T, Boolean> {
	private final ILegacyChannelProducer myLegacyChannelProducer;

	public LegacyChannelProducerAdapter(ILegacyChannelProducer theLegacyChannelProducer) {
		myLegacyChannelProducer = theLegacyChannelProducer;
	}

	@Override
	public String getProducerName() {
		return "";
	}

	@Override
	public String getChannelName() {
		return "";
	}

	@Override
	public Boolean send(IMessage<T> theMessage) {
		SpringMessageAdapter<T> springMessage = new SpringMessageAdapter<>(theMessage);
		return myLegacyChannelProducer.send(springMessage);
	}
}
