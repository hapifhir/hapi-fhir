package ca.uhn.fhir.batch2.model;

import org.apache.commons.lang3.Validate;

import javax.annotation.Nonnull;

public class JobDefinitionStep {

	private final String myStepId;
	private final String myStepDescription;

	public JobDefinitionStep(@Nonnull String theStepId, @Nonnull String theStepDescription) {
		Validate.notBlank(theStepId);
		Validate.notBlank(theStepDescription);
		myStepId = theStepId;
		myStepDescription = theStepDescription;
	}
}
