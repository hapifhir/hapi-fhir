package ca.uhn.fhir.broker.legacy;

import ca.uhn.fhir.broker.api.IChannelProducer;
import ca.uhn.fhir.broker.api.ISendResult;
import ca.uhn.fhir.broker.impl.SpringMessagingSendResult;
import ca.uhn.fhir.rest.server.messaging.IMessage;
import org.springframework.messaging.support.ChannelInterceptor;

public class LegacyChannelProducerAdapter<T> implements IChannelProducer<T> {
	private final ILegacyChannelProducer myLegacyChannelProducer;

	public LegacyChannelProducerAdapter(ILegacyChannelProducer theLegacyChannelProducer) {
		myLegacyChannelProducer = theLegacyChannelProducer;
	}

	@Override
	public String getProducerName() {
		return "Legacy consumer";
	}

	@Override
	public String getChannelName() {
		return "unknown legacy channel name";
	}

	@Override
	public ISendResult send(IMessage<T> theMessage) {
		SpringMessageAdapter<T> springMessage = new SpringMessageAdapter<>(theMessage);
		return new SpringMessagingSendResult(myLegacyChannelProducer.send(springMessage));
	}

	public void addInterceptor(ChannelInterceptor theInterceptor) {
		myLegacyChannelProducer.addInterceptor(theInterceptor);
	}

	public ILegacyChannelProducer getLegacyProducer() {
		return myLegacyChannelProducer;
	}
}
