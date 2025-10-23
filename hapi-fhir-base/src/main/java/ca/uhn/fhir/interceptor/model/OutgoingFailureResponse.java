package ca.uhn.fhir.interceptor.model;

import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;

/**
 * This object is used as a return type for interceptor hook methods implementing the
 * {@link ca.uhn.fhir.interceptor.api.Pointcut#SERVER_OUTGOING_FAILURE_OPERATIONOUTCOME}
 * pointcut.
 */
public class OutgoingFailureResponse {
	private int myStatusCode;
	private IBaseOperationOutcome myBaseOperationOutcome;

	public OutgoingFailureResponse() {
	}

	public int getStatusCode() {
		return myStatusCode;
	}

	public void setStatusCode(int theStatusCode) {
		this.myStatusCode = theStatusCode;
	}

	public IBaseOperationOutcome getBaseOperationOutcome() {
		return myBaseOperationOutcome;
	}

	public void setBaseOperationOutcome(IBaseOperationOutcome theBaseOperationOutcome) {
		this.myBaseOperationOutcome = theBaseOperationOutcome;
	}
}
