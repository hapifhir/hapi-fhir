package ca.uhn.fhir.broker.impl;

import ca.uhn.fhir.rest.server.messaging.IMessage;
import ca.uhn.fhir.rest.server.messaging.json.BaseJsonMessage;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class TestMessage<T> implements IMessage<T> {
	private final String myMessageKey;
	private final T myPayload;
	private final Map<String, Object> myHeaders = new HashMap<>();

	public TestMessage(String theMessageKey, T thePayload) {
		myMessageKey = theMessageKey;
		myPayload = thePayload;
	}

	public TestMessage(T thePayload) {
		myPayload = thePayload;
		if (myPayload != null && myPayload instanceof BaseJsonMessage) {
			myMessageKey = ((BaseJsonMessage<?>) myPayload).getMessageKey();
		} else {
			myMessageKey = null;
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
		return myPayload;
	}

	@Override
	@Nonnull
	public String getMessageKey() {
		if (myMessageKey != null) {
			return myMessageKey;
		} else if (getPayloadMessageKey() != null) {
			return getPayloadMessageKey();
		}

		return IMessage.super.getMessageKey();
	}

	@Nullable
	public String getPayloadMessageKey() {
		if (myPayload != null) {
			try {
				java.lang.reflect.Method method = myPayload.getClass().getMethod("getPayloadMessageKey");
				if (method != null) {
					Object result = method.invoke(myPayload);
					if (result instanceof String) {
						return (String) result;
					}
				}
			} catch (Exception e) {
				return null;
			}
		}
		return null;
	}

}
