package ca.uhn.fhir.jpa.provider.merge;

import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;

public class OperationOutcomeWithStatusCode {

	private IBaseOperationOutcome myOperationOutcome;
	private int myHttpStatusCode;

	public IBaseOperationOutcome getOperationOutcome() {
		return myOperationOutcome;
	}

	public void setOperationOutcome(IBaseOperationOutcome theOperationOutcome) {
		this.myOperationOutcome = theOperationOutcome;
	}

	public int getHttpStatusCode() {
		return myHttpStatusCode;
	}

	public void setHttpStatusCode(int theHttpStatusCode) {
		this.myHttpStatusCode = theHttpStatusCode;
	}
}
