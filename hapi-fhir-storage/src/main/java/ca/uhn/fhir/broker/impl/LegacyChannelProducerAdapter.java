package ca.uhn.fhir.broker.impl;

import ca.uhn.fhir.broker.api.IChannelProducer;

public class LegacyChannelProducerAdapter<T> implements IChannelProducer<T, Boolean> {
	private final ca.uhn.fhir.jpa.subscription.channel.api.IChannelProducer myLegacyProducer;

	public LegacyChannelProducerAdapter(ca.uhn.fhir.jpa.subscription.channel.api.IChannelProducer theLegacyProducer) {
		myLegacyProducer = theLegacyProducer;
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
	public Boolean send(T theMessage) {
		return null;
	}

	@Override
	public void close() throws Exception {

	}
}
