package ca.uhn.fhir.batch2.api;

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;

public class JobOperationResultJson implements IModelJson {
	@JsonProperty("operation")
	private String myOperation;
	@JsonProperty("success")
	private Boolean mySuccess;
	@JsonProperty("message")
	private String myMessage;

	public static JobOperationResultJson newSuccess(String theOperation, String theMessage) {
		JobOperationResultJson result = new JobOperationResultJson();
		result.setSuccess(true);
		result.setOperation(theOperation);
		result.setMessage(theMessage);
		return result;
	}

	public static JobOperationResultJson newFailure(String theOperation, String theMessage) {
		JobOperationResultJson result = new JobOperationResultJson();
		result.setSuccess(false);
		result.setOperation(theOperation);
		result.setMessage(theMessage);
		return result;
	}

	public String getOperation() {
		return myOperation;
	}

	public void setOperation(String theOperation) {
		myOperation = theOperation;
	}

	public Boolean getSuccess() {
		return mySuccess;
	}

	public void setSuccess(Boolean theSuccess) {
		mySuccess = theSuccess;
	}

	public String getMessage() {
		return myMessage;
	}

	public void setMessage(String theMessage) {
		myMessage = theMessage;
	}
}
