package ca.uhn.fhir.batch2.api;

import ca.uhn.fhir.batch2.model.JobDefinitionParameter;
import ca.uhn.fhir.batch2.model.JobDefinitionStep;

import java.util.List;

public interface IBatchJobDefinition {

	/**
	 * @return Returns a unique identifier for the job definition (i.e. for the "kind" of job)
	 */
	String getJobDefinitionId();

	/**
	 * @return Returns a unique identifier for the version of the job definition. Higher means newer but numbers have no other meaning.
	 */
	int getJobDefinitionVersion();

	/**
	 * @return Returns the parameters that this job can accept as input to create a new instance
	 */
	List<JobDefinitionParameter> getParameters();

	/**
	 * @return Returns the processing steps for this job
	 */
	List<JobDefinitionStep> getSteps();

}
