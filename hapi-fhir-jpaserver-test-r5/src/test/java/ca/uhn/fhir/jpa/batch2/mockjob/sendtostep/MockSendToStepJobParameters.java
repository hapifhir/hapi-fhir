package ca.uhn.fhir.jpa.batch2.mockjob.sendtostep;

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MockSendToStepJobParameters implements IModelJson {

	@JsonProperty("sourceStepIdToMessages")
	private Map<String, List<MessageJson>> myMessages;

	public Map<String, List<MessageJson>> getMessages() {
		if (myMessages == null) {
			myMessages = new HashMap<>();
		}
		return myMessages;
	}

	public void addSendMessage(String theSourceStepId, String theTargetStepId, String theMessage) {
		getMessages().computeIfAbsent(theSourceStepId, k -> new ArrayList<>()).add(new MessageJson(theTargetStepId, theMessage));
	}

	public void addSendMessageUsingInvalidType(String theSourceStepId, String theTargetStepId) {
		getMessages().computeIfAbsent(theSourceStepId, k -> new ArrayList<>()).add(new MessageJson(theTargetStepId, null).withSendInvalidType());
	}


	public static class MessageJson implements IModelJson {

		@JsonProperty("targetStepId")
		private String myTargetStepId;
		@JsonProperty("message")
		private String myMessage;
		@JsonProperty("sendInvalidType")
		private boolean mySendInvalidType;

		public MessageJson() {
		}

		public MessageJson(String theTargetStepId, String theMessage) {
			myTargetStepId = theTargetStepId;
			myMessage = theMessage;
		}

		public String getTargetStepId() {
			return myTargetStepId;
		}

		public String getMessage() {
			return myMessage;
		}

		@Override
		public String toString() {
			return new ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
				.append("targetStepId", myTargetStepId)
				.append("message", myMessage)
				.toString();
		}

		public boolean isSendInvalidType() {
			return mySendInvalidType;
		}

		public MessageJson withSendInvalidType() {
			mySendInvalidType = true;
			return this;
		}
	}

}
