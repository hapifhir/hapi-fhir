package ca.uhn.fhir.rest.server.exceptions;

import ca.uhn.fhir.model.dstu.resource.OperationOutcome;

/**
 * Represents an <b>HTTP 422 Unprocessable Entity</b> response, which means that a resource was rejected by the server because it "violated applicable FHIR profiles or server business rules".
 * 
 * <p>
 * This exception will generally contain an {@link OperationOutcome} instance which details the failure.
 * </p>
 */
public class UnprocessableEntityException extends BaseServerResponseException {

	private OperationOutcome myOperationOutcome;

	public UnprocessableEntityException(OperationOutcome theOperationOutcome) {
		super(422, "Unprocessable Entity");

		myOperationOutcome = theOperationOutcome;
	}

	public UnprocessableEntityException(String theMessage) {
		super(422, theMessage);

		myOperationOutcome = new OperationOutcome();
		myOperationOutcome.addIssue().setDetails(theMessage);
	}

	public UnprocessableEntityException(String... theMessage) {
		super(422, theMessage != null && theMessage.length > 0 ? theMessage[0] : "");

		myOperationOutcome = new OperationOutcome();
		if (theMessage != null) {
			for (String next : theMessage) {
				myOperationOutcome.addIssue().setDetails(next);
			}
		}
	}

	public OperationOutcome getOperationOutcome() {
		return myOperationOutcome;
	}

	private static final long serialVersionUID = 1L;

}
