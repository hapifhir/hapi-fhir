package ca.uhn.fhir.batch2.model;

import ca.uhn.fhir.batch2.api.IJobStepWorker;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nonnull;

import static ca.uhn.fhir.batch2.model.JobDefinition.ID_MAX_LENGTH;

public class JobDefinitionStep {

	private final String myStepId;
	private final String myStepDescription;
	private final IJobStepWorker myJobStepWorker;

	public JobDefinitionStep(@Nonnull String theStepId, @Nonnull String theStepDescription, @Nonnull IJobStepWorker theJobStepWorker) {
		Validate.notBlank(theStepId);
		Validate.isTrue(theStepId.length() <= ID_MAX_LENGTH, "Maximum ID length is %d", ID_MAX_LENGTH);
		Validate.notBlank(theStepDescription);
		myStepId = theStepId;
		myStepDescription = theStepDescription;
		myJobStepWorker = theJobStepWorker;
	}

	public String getStepId() {
		return myStepId;
	}

	public String getStepDescription() {
		return myStepDescription;
	}

	public IJobStepWorker getJobStepWorker() {
		return myJobStepWorker;
	}
}
