package ca.uhn.fhir.broker.impl;

import ca.uhn.fhir.broker.api.IMessage;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

import java.util.Map;

public class SpringMessageAdapter<T> implements Message<T> {
	private final IMessage<T> myMessage;
	private final MessageHeaders myHeaders;

	public SpringMessageAdapter(IMessage<T> theMessage) {
		myMessage = theMessage;
		myHeaders = new MessageHeaders((Map<String, Object>)(Map<?,?>)myMessage.getHeaders());
	}

	@Override
	public T getPayload() {
		return myMessage.getValue();
	}

	@Override
	public MessageHeaders getHeaders() {
		return myHeaders;
	}
}
