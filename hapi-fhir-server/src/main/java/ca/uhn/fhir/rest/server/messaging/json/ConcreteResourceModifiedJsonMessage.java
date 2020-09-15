package ca.uhn.fhir.rest.server.messaging.json;

import ca.uhn.fhir.rest.server.messaging.ConcreteResourceModifiedMessage;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class ConcreteResourceModifiedJsonMessage extends BaseJsonMessage<ConcreteResourceModifiedMessage> {


	@JsonProperty("payload")
	private ConcreteResourceModifiedMessage myPayload;

	/**
	 * Constructor
	 */
	public ConcreteResourceModifiedJsonMessage() {
		super();
	}

	/**
	 * Constructor
	 */
	public ConcreteResourceModifiedJsonMessage(ConcreteResourceModifiedMessage thePayload) {
		myPayload = thePayload;
		setDefaultRetryHeaders();
	}

	public ConcreteResourceModifiedJsonMessage(HapiMessageHeaders theRetryMessageHeaders, ConcreteResourceModifiedMessage thePayload) {
		myPayload = thePayload;
		setHeaders(theRetryMessageHeaders);
	}


	@Override
	public ConcreteResourceModifiedMessage getPayload() {
		return myPayload;
	}

	public void setPayload(ConcreteResourceModifiedMessage thePayload) {
		myPayload = thePayload;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("myPayload", myPayload)
			.toString();
	}
}
