package ca.uhn.fhir.broker.impl;


import ca.uhn.fhir.broker.api.Message;
import ca.uhn.fhir.jpa.subscription.channel.api.ILegacyChannelReceiver;
import ca.uhn.fhir.rest.server.messaging.json.BaseJsonMessage;

import java.util.Map;

public class LegacyMessageAdapter<T> implements Message<T> {
	private final ILegacyChannelReceiver myLegacyChannelReceiver;
	private final org.springframework.messaging.Message<T> myMessage;

	public LegacyMessageAdapter(ILegacyChannelReceiver theLegacyChannelReceiver, org.springframework.messaging.Message<T> theMessage) {
		myLegacyChannelReceiver = theLegacyChannelReceiver;
		myMessage = theMessage;
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
	public String getChannelName() {
		return myLegacyChannelReceiver.getName();
	}

	@Override
	public T getValue() {
		return myMessage.getPayload();
	}
}
