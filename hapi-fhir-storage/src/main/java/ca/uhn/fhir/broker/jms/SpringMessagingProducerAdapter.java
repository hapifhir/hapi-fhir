package ca.uhn.fhir.broker.jms;

import ca.uhn.fhir.broker.api.IChannelProducer;
import ca.uhn.fhir.broker.api.ISendResult;
import ca.uhn.fhir.broker.impl.SpringMessagingSendResult;
import ca.uhn.fhir.rest.server.messaging.IMessage;
import org.springframework.messaging.support.ChannelInterceptor;

public class SpringMessagingProducerAdapter<T> implements IChannelProducer<T> {
	private final ISpringMessagingChannelProducer mySpringMessagingChannelProducer;

	public SpringMessagingProducerAdapter(ISpringMessagingChannelProducer theSpringMessagingChannelProducer) {
		mySpringMessagingChannelProducer = theSpringMessagingChannelProducer;
	}

	@Override
	public String getChannelName() {
		return "unknown legacy channel name";
	}

	@Override
	public ISendResult send(IMessage<T> theMessage) {
		SpringMessagingMessageAdapter<T> springMessage = new SpringMessagingMessageAdapter<>(theMessage);
		return new SpringMessagingSendResult(mySpringMessagingChannelProducer.send(springMessage));
	}

	public void addInterceptor(ChannelInterceptor theInterceptor) {
		mySpringMessagingChannelProducer.addInterceptor(theInterceptor);
	}

	public ISpringMessagingChannelProducer getSpringMessagingProducer() {
		return mySpringMessagingChannelProducer;
	}
}
