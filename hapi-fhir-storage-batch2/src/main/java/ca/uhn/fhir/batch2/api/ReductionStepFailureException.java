package ca.uhn.fhir.batch2.api;

import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.util.BaseUnrecoverableRuntimeException;

/**
 * This class holds the report produced at the end of a bulk import
 */
public class ReductionStepFailureException extends BaseUnrecoverableRuntimeException {

	private final IModelJson myReportMsg;

	public ReductionStepFailureException(String theErrorMessage, IModelJson theReportMsg) {
		super(theErrorMessage);

		myReportMsg = theReportMsg;
	}

	public IModelJson getReportMsg() {
		return myReportMsg;
	}
}
