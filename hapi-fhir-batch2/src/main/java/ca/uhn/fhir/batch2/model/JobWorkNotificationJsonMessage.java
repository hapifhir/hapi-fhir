package ca.uhn.fhir.batch2.model;

import ca.uhn.fhir.rest.server.messaging.json.BaseJsonMessage;
import com.fasterxml.jackson.annotation.JsonProperty;

public class JobWorkNotificationJsonMessage extends BaseJsonMessage<JobWorkNotification> {

	@JsonProperty("payload")
	private JobWorkNotification myPayload;

	/**
	 * Constructor
	 */
	public JobWorkNotificationJsonMessage() {
		super();
	}

	/**
	 * Constructor
	 */
	public JobWorkNotificationJsonMessage(JobWorkNotification thePayload) {
		myPayload = thePayload;
	}

	@Override
	public JobWorkNotification getPayload() {
		return myPayload;
	}

	public void setPayload(JobWorkNotification thePayload) {
		myPayload = thePayload;
	}

}
