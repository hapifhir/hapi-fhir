package ca.uhn.fhir.testjob;

import ca.uhn.fhir.batch2.api.IJobCompletionHandler;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.testjob.models.FirstStepOutput;
import ca.uhn.fhir.testjob.models.TestJobParameters;

@SuppressWarnings({"unchecked", "rawtypes"})
public class TestJobDefinitionUtils {

	public static final int TEST_JOB_VERSION = 1;
	public static final String FIRST_STEP_ID = "first-step";
	public static final String LAST_STEP_ID = "last-step";

	/**
	 * Creates a test job definition.
	 * This job will not be gated.
	 */
	public static JobDefinition<? extends IModelJson> buildJobDefinition(
		String theJobId,
		IJobStepWorker<TestJobParameters, VoidModel, FirstStepOutput> theFirstStep,
		IJobStepWorker<TestJobParameters, FirstStepOutput, VoidModel> theLastStep,
		IJobCompletionHandler<TestJobParameters> theCompletionHandler) {
		return getJobBuilder(theJobId, theFirstStep, theLastStep, theCompletionHandler).build();
	}

	/**
	 * Creates a test job defintion.
	 * This job will be gated.
	 */
	public static JobDefinition<? extends IModelJson> buildGatedJobDefinition(
		String theJobId,
		IJobStepWorker<TestJobParameters, VoidModel, FirstStepOutput> theFirstStep,
		IJobStepWorker<TestJobParameters, FirstStepOutput, VoidModel> theLastStep,
		IJobCompletionHandler<TestJobParameters> theCompletionHandler) {
		return getJobBuilder(theJobId, theFirstStep, theLastStep, theCompletionHandler)
			.gatedExecution().build();
	}

	private static JobDefinition.Builder getJobBuilder(
		String theJobId,
		IJobStepWorker<TestJobParameters, VoidModel, FirstStepOutput> theFirstStep,
		IJobStepWorker<TestJobParameters, FirstStepOutput, VoidModel> theLastStep,
		IJobCompletionHandler<TestJobParameters> theCompletionHandler
	) {
		return JobDefinition.newBuilder()
			.setJobDefinitionId(theJobId)
			.setJobDescription("test job")
			.setJobDefinitionVersion(TEST_JOB_VERSION)
			.setParametersType(TestJobParameters.class)
			.addFirstStep(
				FIRST_STEP_ID,
				"Test first step",
				FirstStepOutput.class,
				theFirstStep
			)
			.addLastStep(
				LAST_STEP_ID,
				"Test last step",
				theLastStep
			)
			.completionHandler(theCompletionHandler);
	}
}
