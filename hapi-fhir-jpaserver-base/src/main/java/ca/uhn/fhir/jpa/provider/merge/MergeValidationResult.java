package ca.uhn.fhir.jpa.provider.merge;

import org.hl7.fhir.r4.model.Patient;

class MergeValidationResult {
	protected final Patient sourceResource;
	protected final Patient targetResource;
	protected final boolean isValid;

	private MergeValidationResult(Patient theSourceResource, Patient theTargetResource, boolean theIsValid) {
		sourceResource = theSourceResource;
		targetResource = theTargetResource;
		isValid = theIsValid;
	}

	public static MergeValidationResult invalidResult() {
		return new MergeValidationResult(null, null, false);
	}

	public static MergeValidationResult validResult(Patient theSourceResource, Patient theTargetResource) {
		return new MergeValidationResult(theSourceResource, theTargetResource, true);
	}
}
