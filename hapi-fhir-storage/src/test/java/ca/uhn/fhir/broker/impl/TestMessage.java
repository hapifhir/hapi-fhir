package ca.uhn.fhir.broker.impl;

import ca.uhn.fhir.rest.server.messaging.IMessage;
import ca.uhn.fhir.rest.server.messaging.json.BaseJsonMessage;

import java.util.HashMap;
import java.util.Map;

public class TestMessage<T> implements IMessage<T> {
	private final String myKey;
	private final T myValue;
	private final Map<String, Object> myHeaders = new HashMap<>();

	public TestMessage(String theKey, T theValue) {
		myKey = theKey;
		myValue = theValue;
	}

	public TestMessage(T theValue) {
		myValue = theValue;
		if (myValue != null && myValue instanceof BaseJsonMessage) {
			myKey = ((BaseJsonMessage<?>) myValue).getMessageKey();
		} else {
			myKey = null;
		}
	}
//
//	@Override
//	public byte[] getData() {
//		ObjectMapper objectMapper = new ObjectMapper();
//		try {
//			return objectMapper.writeValueAsBytes(getValue());
//		} catch (JsonProcessingException e) {
//			throw new InternalErrorException("Unable to serialize " + getValue().getClass() + " JSON instance", e);
//		}
//	}

	@Override
	public String getMessageKey() {
		return myKey;
	}

	@Override
	public Map<String, Object> getHeaders() {
		return myHeaders;
	}

	@Override
	public Object getHeader(String theHeaderName) {
		return myHeaders.get(theHeaderName);
	}

	@Override
	public T getPayload() {
		return myValue;
	}
}
