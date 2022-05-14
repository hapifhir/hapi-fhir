package ca.uhn.fhir.batch2.model;

public class JobWorkCursor {
	public final boolean isFirstStep;
	public final JobDefinitionStep targetStep;
	public final JobDefinitionStep nextStep;

	public JobWorkCursor(boolean theIsFirstStep, JobDefinitionStep theTargetStep, JobDefinitionStep theNextStep) {
		isFirstStep = theIsFirstStep;
		targetStep = theTargetStep;
		nextStep = theNextStep;
	}

	public String getTargetStepId() {
		return targetStep.getStepId();
	}
}
