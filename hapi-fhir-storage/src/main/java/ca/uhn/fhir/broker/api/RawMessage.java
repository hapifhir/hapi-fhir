package ca.uhn.fhir.broker.api;

import ca.uhn.fhir.rest.server.messaging.IMessage;
import org.springframework.messaging.support.GenericMessage;

import java.util.Map;

public class RawMessage extends GenericMessage<String> implements IMessage<String> {
	public RawMessage(String thePayload) {
		super(thePayload);
	}

	public RawMessage(String thePayload, Map<String, Object> theHeaders) {
		super(thePayload, theHeaders);
	}

	// FIXME KHS
	@Override
	public String toString() {
		return getPayload();
	}
}
