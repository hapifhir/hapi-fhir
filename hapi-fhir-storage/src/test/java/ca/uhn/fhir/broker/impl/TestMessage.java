package ca.uhn.fhir.broker.impl;

import ca.uhn.fhir.rest.server.messaging.IMessage;
import ca.uhn.fhir.rest.server.messaging.json.BaseJsonMessage;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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

	@Override
	public Map<String, Object> getHeaders() {
		return myHeaders;
	}

	@Override
	public <H> Optional<H> getHeader(String theHeaderName) {
		return (Optional<H>) Optional.ofNullable(myHeaders.get(theHeaderName));
	}

	@Override
	public T getPayload() {
		return myValue;
	}
}
