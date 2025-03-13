package ca.uhn.fhir.broker.impl;

import ca.uhn.fhir.broker.api.IChannelProducer;
import ca.uhn.fhir.jpa.subscription.channel.api.ILegacyChannelProducer;

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
	public Boolean send(T theMessage) {
		return null;
	}

	@Override
	public void close() throws Exception {

	}
}
