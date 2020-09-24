package ca.uhn.fhir.jpa.subscription.model;

import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.rest.server.messaging.json.BaseJsonMessage;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

import java.util.HashMap;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DebeziumMessage extends BaseJsonMessage<PayloadPOJO> {

	private static final long serialVersionUID = 1L;
	@JsonProperty("payload")
	private PayloadPOJO myPayload;

	@JsonProperty("headers")
	private MessageHeaders myHeaders;

	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	public DebeziumMessage() {

	}

	public String getResourceBytes() {
		return myPayload.getPayload().getPayload();
	}

	public int getResVer() {
		return myPayload.getPayload().getResVer();
	}

	public int getResId() {
		return myPayload.getPayload().getResId();
	}

	public long getResDeletedAt() {
		return myPayload.getPayload().getResDeletedAt();
	}

	public String getResourceType() {
		return myPayload.getPayload().getResourceType();
	}

	/**
	 * Constructor
	 */
	public DebeziumMessage(PayloadPOJO thePayload) {
		myPayload = thePayload;
	}

	public PayloadPOJO getPayload() {
		return myPayload;
	}

	@Override
	public MessageHeaders getHeaders() {
		return myHeaders;
	}

	public void setHeaders(MessageHeaders theHeaders) {
		myHeaders = theHeaders;
	}
}

