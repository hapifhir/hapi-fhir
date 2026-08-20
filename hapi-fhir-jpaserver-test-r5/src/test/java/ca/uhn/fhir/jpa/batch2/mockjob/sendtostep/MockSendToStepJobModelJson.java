package ca.uhn.fhir.jpa.batch2.mockjob.sendtostep;

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Objects;

public class MockSendToStepJobModelJson implements IModelJson {

	@JsonProperty("sourceStepId")
	private String mySourceStepId;
	@JsonProperty("message")
	private String myMessage;

	/**
	 * Constructor
	 */
	public MockSendToStepJobModelJson() {
		// nothing
	}

	/**
	 * Constructor
	 */
	public MockSendToStepJobModelJson(String theSourceStepId, String theMessage) {
		mySourceStepId = theSourceStepId;
		myMessage = theMessage;
	}

	@Override
	public boolean equals(Object theO) {
		if (!(theO instanceof MockSendToStepJobModelJson that)) {
			return false;
		}
		return Objects.equals(mySourceStepId, that.mySourceStepId) && Objects.equals(myMessage, that.myMessage);
	}

	@Override
	public int hashCode() {
		return Objects.hash(mySourceStepId, myMessage);
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
			.append("sourceStepId", mySourceStepId)
			.append("message", myMessage)
			.toString();
	}

	public String getMessage() {
		return myMessage;
	}

	public void setMessage(String theMessage) {
		myMessage = theMessage;
	}

	public String getSourceStepId() {
		return mySourceStepId;
	}

	public void setSourceStepId(String theSourceStepId) {
		mySourceStepId = theSourceStepId;
	}

}
