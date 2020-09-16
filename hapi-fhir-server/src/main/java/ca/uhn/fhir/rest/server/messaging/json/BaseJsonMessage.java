package ca.uhn.fhir.rest.server.messaging.json;


import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

public abstract class BaseJsonMessage<T> implements Message<T>, IModelJson {

	private static final long serialVersionUID = 1L;
	@JsonProperty("headers")
	private HapiMessageHeaders myHeaders;

	/**
	 * Constructor
	 */
	public BaseJsonMessage() {
		super();
		setDefaultRetryHeaders();
	}

	protected void setDefaultRetryHeaders() {
		HapiMessageHeaders messageHeaders = new HapiMessageHeaders();
		setHeaders(messageHeaders);
	}

	@Override
	public MessageHeaders getHeaders() {
		return myHeaders.toMessageHeaders();
	}

	public HapiMessageHeaders getHapiHeaders() {
		return myHeaders;
	}


	public void setHeaders(HapiMessageHeaders theHeaders) {
		myHeaders = theHeaders;
	}
}
