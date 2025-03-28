package ca.uhn.fhir.broker.jms;

import ca.uhn.fhir.rest.server.messaging.IMessage;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

public class SpringMessagingMessageAdapter<T extends IMessage<?>> implements Message<T> {
	private final T myHapiMessage;
	private final MessageHeaders myHeaders;

	public SpringMessagingMessageAdapter(T theHapiMessage) {
		myHapiMessage = theHapiMessage;
		myHeaders = new MessageHeaders(myHapiMessage.getHeaders());
	}

	@Override
	public T getPayload() {
		return myHapiMessage;
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
