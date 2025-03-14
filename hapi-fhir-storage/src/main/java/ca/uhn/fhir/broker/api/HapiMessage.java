package ca.uhn.fhir.broker.api;

import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.messaging.json.BaseJsonMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

public class HapiMessage<T> implements IMessage<T> {
	private final String myKey;
	private final T myValue;
	private final Map<String, String> myHeaders = new HashMap<>();

	public HapiMessage(String theKey, T theValue) {
		myKey = theKey;
		myValue = theValue;
	}

	public HapiMessage(T theValue) {
		myValue = theValue;
		if (myValue != null && myValue instanceof BaseJsonMessage) {
			myKey = ((BaseJsonMessage<?>) myValue).getMessageKey();
		} else {
			myKey = null;
		}
	}

	@Override
	public byte[] getData() {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.writeValueAsBytes(getValue());
		} catch (JsonProcessingException e) {
			throw new InternalErrorException("Unable to serialize " + getValue().getClass() + " JSON instance", e);
		}
	}

	@Override
	public String getKey() {
		return myKey;
	}

	@Override
	public Map<String, String> getHeaders() {
		return myHeaders;
	}

	@Override
	public String getHeader(String theHeaderName) {
		return myHeaders.get(theHeaderName);
	}

	@Override
	public T getValue() {
		return myValue;
	}
}
