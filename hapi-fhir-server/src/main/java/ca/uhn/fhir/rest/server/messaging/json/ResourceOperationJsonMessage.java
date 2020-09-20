package ca.uhn.fhir.rest.server.messaging.json;

import ca.uhn.fhir.rest.server.messaging.ResourceOperationMessage;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class ResourceOperationJsonMessage extends BaseJsonMessage<ResourceOperationMessage> {


	@JsonProperty("payload")
	private ResourceOperationMessage myPayload;

	/**
	 * Constructor
	 */
	public ResourceOperationJsonMessage() {
		super();
	}

	/**
	 * Constructor
	 */
	public ResourceOperationJsonMessage(ResourceOperationMessage thePayload) {
		myPayload = thePayload;
		setDefaultRetryHeaders();
	}

	public ResourceOperationJsonMessage(HapiMessageHeaders theRetryMessageHeaders, ResourceOperationMessage thePayload) {
		myPayload = thePayload;
		setHeaders(theRetryMessageHeaders);
	}


	@Override
	public ResourceOperationMessage getPayload() {
		return myPayload;
	}

	public void setPayload(ResourceOperationMessage thePayload) {
		myPayload = thePayload;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("myPayload", myPayload)
			.toString();
	}
}
