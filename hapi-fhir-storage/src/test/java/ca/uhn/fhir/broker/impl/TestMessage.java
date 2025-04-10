package ca.uhn.fhir.broker.impl;

import ca.uhn.fhir.rest.server.messaging.IMessage;

import java.util.HashMap;
import java.util.Map;

public class TestMessage<T> implements IMessage<T> {
	private final T myPayload;
	private final Map<String, Object> myHeaders = new HashMap<>();

	public TestMessage(T thePayload) {
		myPayload = thePayload;
	}

	@Override
	public Map<String, Object> getHeaders() {
		return myHeaders;
	}

	@Override
	public T getPayload() {
		return myPayload;
	}
}
