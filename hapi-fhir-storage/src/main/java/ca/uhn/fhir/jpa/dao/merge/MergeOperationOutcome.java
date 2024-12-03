package ca.uhn.fhir.jpa.dao.merge;

import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;

public class MergeOperationOutcome {
	private IBaseOperationOutcome myOperationOutcome;
	private int myHttpStatusCode;
	private IBaseResource myUpdatedTargetResource;

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

	public IBaseResource getUpdatedTargetResource() {
		return myUpdatedTargetResource;
	}

	public void setUpdatedTargetResource(IBaseResource theUpdatedTargetResource) {
		this.myUpdatedTargetResource = theUpdatedTargetResource;
	}
}
