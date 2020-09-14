package ca.uhn.fhir.rest.server.messaging.json;

import ca.uhn.fhir.rest.server.messaging.BaseResourceModifiedMessage;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class BaseResourceModifiedJsonMessage extends BaseJsonMessage<BaseResourceModifiedMessage> {


	@JsonProperty("payload")
	private BaseResourceModifiedMessage myPayload;

	/**
	 * Constructor
	 */
	public BaseResourceModifiedJsonMessage() {
		super();
	}

	/**
	 * Constructor
	 */
	public BaseResourceModifiedJsonMessage(BaseResourceModifiedMessage thePayload) {
		myPayload = thePayload;
		setDefaultRetryHeaders();
	}

	public BaseResourceModifiedJsonMessage(HapiMessageHeaders theRetryMessageHeaders, BaseResourceModifiedMessage thePayload) {
		myPayload = thePayload;
		setHeaders(theRetryMessageHeaders);
	}


	@Override
	public BaseResourceModifiedMessage getPayload() {
		return myPayload;
	}

	public void setPayload(BaseResourceModifiedMessage thePayload) {
		myPayload = thePayload;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("myPayload", myPayload)
			.toString();
	}
}
