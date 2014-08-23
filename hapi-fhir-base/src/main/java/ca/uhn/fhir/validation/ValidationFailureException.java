package ca.uhn.fhir.validation;

import ca.uhn.fhir.model.dstu.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu.valueset.IssueSeverityEnum;

public class ValidationFailureException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private OperationOutcome myOperationOutcome;

	public ValidationFailureException(String theProblem) {
		this(theProblem, IssueSeverityEnum.FATAL, null);
	}

	public ValidationFailureException(String theProblem, Exception theCause) {
		this(theProblem, IssueSeverityEnum.FATAL, theCause);
	}

	public ValidationFailureException(String theProblem, IssueSeverityEnum theSeverity, Exception theCause) {
		super(theProblem, theCause);
		myOperationOutcome = new OperationOutcome();
		myOperationOutcome.addIssue().setSeverity(theSeverity).setDetails(theProblem);
	}

	public ValidationFailureException(String theProblem, OperationOutcome theOperationOutcome) {
		super(theProblem);
		myOperationOutcome = theOperationOutcome;
	}

	public OperationOutcome getOperationOutcome() {
		return myOperationOutcome;
	}

}
