package ca.uhn.fhir.jpa.batch2;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.api.IJobMaintenanceService;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.impl.JobDefinitionRegistry;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.jpa.test.Batch2JobHelper;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.test.concurrency.PointcutLatch;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class Batch2CoordinatorIT  extends BaseJpaR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(Batch2CoordinatorIT.class);

	private static final String TEST_JOB_ID = "test-job";
	public static final int TEST_JOB_VERSION = 1;
	@Autowired
	JobDefinitionRegistry myJobDefinitionRegistry;
	@Autowired
	IJobCoordinator myJobCoordinator;
	@Autowired
	Batch2JobHelper myBatch2JobHelper;
	@Autowired
	IJobMaintenanceService myJobMaintenanceService;

	private final PointcutLatch firstStepLatch = new PointcutLatch("First Step");
	private final PointcutLatch lastStepLatch = new PointcutLatch("Last Step");

	final IJobStepWorker<TestJobParameters, VoidModel, FirstStepOutput> myFirstStep = (step,sink) -> callLatch(firstStepLatch, step);
	final IJobStepWorker<TestJobParameters, FirstStepOutput, VoidModel> myLastStep = (step,sink) -> callLatch(lastStepLatch, step);

	private RunOutcome callLatch(PointcutLatch theLatch, StepExecutionDetails<?, ?> theStep) {
		theLatch.call(theStep);
		return new RunOutcome(1);
	}

	@Test
	public void testHappyPass() throws InterruptedException {
		JobDefinition<? extends IModelJson> definition = buildJobDefinition();

		myJobDefinitionRegistry.addJobDefinition(definition);

		JobInstanceStartRequest request = new JobInstanceStartRequest();
		request.setJobDefinitionId(TEST_JOB_ID);
		TestJobParameters parameters = new TestJobParameters();
		request.setParameters(parameters);

		firstStepLatch.setExpectedCount(1);
		String instanceId = myJobCoordinator.startInstance(request);
		firstStepLatch.awaitExpected();

		// FIXME KHS should the second step be called in this case?
		lastStepLatch.setExpectedCount(1);
		myJobMaintenanceService.runMaintenancePass();
		lastStepLatch.awaitExpected();

		myBatch2JobHelper.awaitJobCompletion(instanceId);
	}

	// FIXME KHS add a test to recover from poisoned head by cancelling job instance

	private JobDefinition<? extends IModelJson> buildJobDefinition() {
		return JobDefinition.newBuilder()
			.setJobDefinitionId(TEST_JOB_ID)
			.setJobDescription("test job")
			.setJobDefinitionVersion(TEST_JOB_VERSION)
			.setParametersType(TestJobParameters.class)
			.gatedExecution()
			.addFirstStep(
				"first-step",
				"Test first step",
				FirstStepOutput.class,
				myFirstStep
			)
			.addLastStep(
				"last-step",
				"Test last step",
				myLastStep
			)
			.build();
	}

	static class TestJobParameters implements IModelJson {
		TestJobParameters() {}
	}

	static class FirstStepOutput implements IModelJson {
		FirstStepOutput() {}
	}
}
