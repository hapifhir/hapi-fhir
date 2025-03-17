package ca.uhn.fhir.broker.legacy;

import ca.uhn.fhir.rest.server.messaging.IMessage;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

import java.util.Map;

public class SpringMessageAdapter<T> implements Message<T> {
	private final IMessage<T> myMessage;
	private final MessageHeaders myHeaders;

	public SpringMessageAdapter(IMessage<T> theMessage) {
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
}
