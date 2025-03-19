package ca.uhn.fhir.broker.jms;

import ca.uhn.fhir.rest.server.messaging.IMessage;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

import java.util.Map;

public class SpringMessagingMessageAdapter<T> implements Message<T> {
	private final IMessage<T> myMessage;
	private final MessageHeaders myHeaders;

	public SpringMessagingMessageAdapter(IMessage<T> theMessage) {
		myMessage = theMessage;
		myHeaders = new MessageHeaders((Map<String, Object>) (Map<?, ?>) myMessage.getHeaders());
	}

	@Override
	public T getPayload() {
		return myMessage.getPayload();
	}

	@Override
	public MessageHeaders getHeaders() {
		return myHeaders;
	}

	@Override
	public String toString() {
		return getPayload().toString();
	}
}
