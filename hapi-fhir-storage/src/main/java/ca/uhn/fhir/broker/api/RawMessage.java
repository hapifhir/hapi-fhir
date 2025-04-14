package ca.uhn.fhir.broker.api;

import ca.uhn.fhir.rest.server.messaging.IMessage;
import org.springframework.messaging.support.GenericMessage;

import java.util.Map;

/**
 * A RawMessage is used to represent messages received from outside the system. The payload of these messages
 * is available as a string. There are no constraints on the contents of the String. It could be an HL7v2 message,
 * JSON, XML, CSV, etc.
 */
public class RawMessage extends GenericMessage<String> implements IMessage<String> {
	public RawMessage(String thePayload) {
		super(thePayload);
	}

	public RawMessage(String thePayload, Map<String, Object> theHeaders) {
		super(thePayload, theHeaders);
	}
}
