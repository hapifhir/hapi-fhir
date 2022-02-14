package ca.uhn.fhir.batch2.model;

import ca.uhn.fhir.batch2.api.IJobStepWorker;
import org.apache.commons.lang3.Validate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JobDefinition {

	public static final int ID_MAX_LENGTH = 100;

	private final String myJobDefinitionId;
	private final int myJobDefinitionVersion;
	private final List<JobDefinitionParameter> myParameters;
	private final List<JobDefinitionStep> mySteps;
	private final String myJobDescription;
	private final Map<String, JobDefinitionParameter> myNameToParameter;

	/**
	 * Constructor
	 */
	private JobDefinition(String theJobDefinitionId, int theJobDefinitionVersion, String theJobDescription, List<JobDefinitionParameter> theParameters, List<JobDefinitionStep> theSteps) {
		Validate.isTrue(theJobDefinitionId.length() <= ID_MAX_LENGTH, "Maximum ID length is %d", ID_MAX_LENGTH);
		Validate.notBlank(theJobDefinitionId, "No job definition ID supplied");
		Validate.notBlank(theJobDescription, "No job description supplied");
		Validate.isTrue(theJobDefinitionVersion >= 1, "No job definition version supplied (must be >= 1)");
		Validate.isTrue(theSteps.size() >= 2, "At least 2 steps must be supplied");
		myJobDefinitionId = theJobDefinitionId;
		myJobDefinitionVersion = theJobDefinitionVersion;
		myJobDescription = theJobDescription;
		mySteps = theSteps;
		myParameters = theParameters;
		myNameToParameter = theParameters
			.stream()
			.collect(Collectors.toMap(t -> t.getName(), t -> t));
	}

	public String getJobDescription() {
		return myJobDescription;
	}

	/**
	 * @return Returns a unique identifier for the job definition (i.e. for the "kind" of job)
	 */
	public String getJobDefinitionId() {
		return myJobDefinitionId;
	}

	/**
	 * @return Returns a unique identifier for the version of the job definition. Higher means newer but numbers have no other meaning. Must be greater than 0.
	 */
	public int getJobDefinitionVersion() {
		return myJobDefinitionVersion;
	}

	/**
	 * @return Returns the parameters that this job can accept as input to create a new instance
	 */
	public List<JobDefinitionParameter> getParameters() {
		return myParameters;
	}

	/**
	 * @return Returns the processing steps for this job
	 */
	public List<JobDefinitionStep> getSteps() {
		return mySteps;
	}

	/**
	 * Fetch a parameter by name
	 */
	public JobDefinitionParameter getParameter(String theName) {
		return myNameToParameter.get(theName);
	}

	public static Builder newBuilder() {
		return new Builder();
	}

	public static class Builder {

		private final List<JobDefinitionParameter> myParameters = new ArrayList<>();
		private final List<JobDefinitionStep> mySteps = new ArrayList<>();
		private String myJobDefinitionId;
		private int myJobDefinitionVersion;
		private String myJobDescription;

		/**
		 * @param theJobDefinitionId A unique identifier for the job definition (i.e. for the "kind" of job)
		 */
		public Builder setJobDefinitionId(String theJobDefinitionId) {
			myJobDefinitionId = theJobDefinitionId;
			return this;
		}

		/**
		 * @param theJobDefinitionVersion A unique identifier for the version of the job definition. Higher means newer but numbers have no other meaning. Must be greater than 0.
		 */
		public Builder setJobDefinitionVersion(int theJobDefinitionVersion) {
			Validate.isTrue(theJobDefinitionVersion > 0, "theJobDefinitionVersion must be > 0");
			myJobDefinitionVersion = theJobDefinitionVersion;
			return this;
		}

		/**
		 * Adds a parameter that this job can accept as input to create a new instance
		 */
		public Builder addParameter(String theName, String theDescription, JobDefinitionParameter.ParamTypeEnum theType, boolean theRequired, boolean theRepeating) {
			myParameters.add(new JobDefinitionParameter(theName, theDescription, theType, theRequired, theRepeating));
			return this;
		}

		/**
		 * Adds a processing steps for this job.
		 *
		 * @param theStepId          A unique identifier for this step. This only needs to be unique within the scope
		 *                           of the individual job definition (i.e. diuplicates are fine for different jobs, or
		 *                           even different versions of the same job)
		 * @param theStepDescription A description of this step
		 * @param theStepWorker      The worker that will actually perform this step
		 */
		public Builder addStep(String theStepId, String theStepDescription, IJobStepWorker theStepWorker) {
			mySteps.add(new JobDefinitionStep(theStepId, theStepDescription, theStepWorker));
			return this;
		}

		public JobDefinition build() {
			return new JobDefinition(myJobDefinitionId, myJobDefinitionVersion, myJobDescription, Collections.unmodifiableList(myParameters), Collections.unmodifiableList(mySteps));
		}

		public Builder setJobDescription(String theJobDescription) {
			myJobDescription = theJobDescription;
			return this;
		}
	}

}
