package ca.uhn.fhir.rest.server.interceptor.consent;

import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;

public class ConsentOutcome {

	/**
	 * Convenience constant containing <code>new ConsentOutcome(ConsentOperationStatusEnum.PROCEED)</code>
	 */
	public static final ConsentOutcome PROCEED = new ConsentOutcome(ConsentOperationStatusEnum.PROCEED);

	private final ConsentOperationStatusEnum myStatus;
	private final IBaseOperationOutcome myOperationOutcome;
	private final IBaseResource myResource;

	public ConsentOutcome(ConsentOperationStatusEnum theStatus) {
		this(theStatus, null, null);
	}

	public ConsentOutcome(ConsentOperationStatusEnum theStatus, IBaseOperationOutcome theOperationOutcome) {
		this(theStatus, theOperationOutcome, null);
	}

	public ConsentOutcome(ConsentOperationStatusEnum theStatus, IBaseResource theResource) {
		this(theStatus, null, theResource);
	}

	private ConsentOutcome(ConsentOperationStatusEnum theStatus, IBaseOperationOutcome theOperationOutcome, IBaseResource theResource) {
		Validate.notNull(theStatus, "theStatus must not be null");
		Validate.isTrue(!(theOperationOutcome != null && theResource != null), "theOperationOutcome and theResource must not both be null");
		myStatus = theStatus;
		myOperationOutcome = theOperationOutcome;
		myResource = theResource;
	}

	public ConsentOperationStatusEnum getStatus() {
		return myStatus;
	}

	public IBaseOperationOutcome getOperationOutcome() {
		return myOperationOutcome;
	}

	public IBaseResource getResource() {
		return myResource;
	}

}
