package ca.uhn.fhir.batch2.model;

import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.model.api.IModelJson;
import org.apache.commons.lang3.Validate;

public class JobWorkCursor<PT extends IModelJson, IT extends IModelJson, OT extends IModelJson> {
	public final boolean isFirstStep;
	public final JobDefinitionStep<PT, IT, OT> targetStep;
	public final JobDefinitionStep<PT, OT, ?> nextStep;

	public JobWorkCursor(boolean theIsFirstStep, JobDefinitionStep<PT, IT, OT> theTargetStep, JobDefinitionStep<PT, OT, ?> theNextStep) {
		isFirstStep = theIsFirstStep;
		targetStep = theTargetStep;
		nextStep = theNextStep;
	}

	public String getTargetStepId() {
		return targetStep.getStepId();
	}

	public boolean isFinalStep() {
		return nextStep == null;
	}

	public JobWorkCursor<PT,IT, VoidModel> asFinalCursor() {
		Validate.isTrue(isFinalStep());
		return (JobWorkCursor<PT,IT, VoidModel>)this;
	}
}
