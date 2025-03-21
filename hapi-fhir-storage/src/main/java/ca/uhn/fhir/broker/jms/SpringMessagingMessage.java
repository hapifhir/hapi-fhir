package ca.uhn.fhir.broker.jms;

import ca.uhn.fhir.rest.server.messaging.IMessage;
import ca.uhn.fhir.rest.server.messaging.json.BaseJsonMessage;

import java.util.Map;

// FIXME KHS do we need this class?
public class SpringMessagingMessage<T> implements IMessage<T> {
	private final org.springframework.messaging.Message<T> myMessage;

	public SpringMessagingMessage(org.springframework.messaging.Message<T> theMessage) {
		myMessage = theMessage;
	}

	org.springframework.messaging.Message<T> getLegacyMessage() {
		return myMessage;
	}

	//	@Override
	//	public byte[] getData() {
	//		T payload = myMessage.getPayload();
	//		return payload == null ? null : payload.toString().getBytes();
	//	}

	@Override
	public String getMessageKey() {
		T payload = myMessage.getPayload();
		if (payload instanceof BaseJsonMessage) {
			return ((BaseJsonMessage<?>) payload).getMessageKey();
		}
		return null;
	}

	@Override
	public Map<String, Object> getHeaders() {
		return myMessage.getHeaders();
	}

	@Override
	public String getHeader(String theHeaderName) {
		return myMessage.getHeaders().get(theHeaderName) == null
				? null
				: myMessage.getHeaders().get(theHeaderName).toString();
	}

	@Override
	public T getPayload() {
		return myMessage.getPayload();
	}
}
