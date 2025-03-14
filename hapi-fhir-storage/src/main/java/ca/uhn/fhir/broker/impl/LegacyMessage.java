package ca.uhn.fhir.broker.impl;


import ca.uhn.fhir.broker.api.IMessage;
import ca.uhn.fhir.rest.server.messaging.json.BaseJsonMessage;

import java.util.Map;

public class LegacyMessage<T> implements IMessage<T> {
	private final org.springframework.messaging.Message<T> myMessage;

	public LegacyMessage(org.springframework.messaging.Message<T> theMessage) {
		myMessage = theMessage;
	}

	org.springframework.messaging.Message<T> getLegacyMessage() {
		return myMessage;
	}

	@Override
	public byte[] getData() {
		T payload = myMessage.getPayload();
		return payload == null ? null : payload.toString().getBytes();
	}

	@Override
	public String getKey() {
		T payload = myMessage.getPayload();
		if (payload instanceof BaseJsonMessage) {
			return ((BaseJsonMessage<?>) payload).getMessageKey();
		}
		return null;
	}

	@Override
	public Map<String, String> getHeaders() {
		return myMessage.getHeaders().entrySet().stream().collect(java.util.stream.Collectors.toMap(Map.Entry::getKey, e -> e.getValue().toString()));
	}

	@Override
	public String getHeader(String theHeaderName) {
		return myMessage.getHeaders().get(theHeaderName) == null ? null : myMessage.getHeaders().get(theHeaderName).toString();
	}

	@Override
	public T getValue() {
		return myMessage.getPayload();
	}
}
