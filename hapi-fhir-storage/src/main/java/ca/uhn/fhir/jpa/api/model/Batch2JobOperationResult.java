package ca.uhn.fhir.jpa.api.model;

public class Batch2JobOperationResult {
	// operation name
	private String myOperation;
	// if the operation is successful
	private Boolean mySuccess;
	// message of the operation
	private String myMessage;

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
