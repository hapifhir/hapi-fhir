package ca.uhn.fhir.jpa.batch2;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.api.IJobMaintenanceService;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.impl.JobDefinitionRegistry;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.jpa.test.Batch2JobHelper;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.test.concurrency.PointcutLatch;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class Batch2CoordinatorIT extends BaseJpaR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(Batch2CoordinatorIT.class);

	public static final int TEST_JOB_VERSION = 1;
	@Autowired
	JobDefinitionRegistry myJobDefinitionRegistry;
	@Autowired
	IJobCoordinator myJobCoordinator;
	@Autowired
	Batch2JobHelper myBatch2JobHelper;
	@Autowired
	IJobMaintenanceService myJobMaintenanceService;

	private final PointcutLatch myFirstStepLatch = new PointcutLatch("First Step");
	private final PointcutLatch myLastStepLatch = new PointcutLatch("Last Step");

	private RunOutcome callLatch(PointcutLatch theLatch, StepExecutionDetails<?, ?> theStep) {
		theLatch.call(theStep);
		return RunOutcome.SUCCESS;
	}

	@Test
	public void testFirstStepNoSink() throws InterruptedException {
		IJobStepWorker<TestJobParameters, VoidModel, FirstStepOutput> firstStep = (step, sink) -> callLatch(myFirstStepLatch, step);
		IJobStepWorker<TestJobParameters, FirstStepOutput, VoidModel> lastStep = (step, sink) -> fail();

		String jobId = "test-job-1";
		JobDefinition<? extends IModelJson> definition = buildJobDefinition(jobId, firstStep, lastStep);

		myJobDefinitionRegistry.addJobDefinition(definition);

		JobInstanceStartRequest request = buildRequest(jobId);

		myFirstStepLatch.setExpectedCount(1);
		String instanceId = myJobCoordinator.startInstance(request);
		myFirstStepLatch.awaitExpected();

		myBatch2JobHelper.awaitJobCompletion(instanceId);
	}


	@Test
	public void testFirstStepToSecondStep() throws InterruptedException {
		IJobStepWorker<TestJobParameters, VoidModel, FirstStepOutput> firstStep = (step, sink) -> {
			sink.accept(new FirstStepOutput());
			callLatch(myFirstStepLatch, step);
			return RunOutcome.SUCCESS;
		};
		IJobStepWorker<TestJobParameters, FirstStepOutput, VoidModel> lastStep = (step, sink) -> callLatch(myLastStepLatch, step);

		String jobId = "test-job-2";
		JobDefinition<? extends IModelJson> definition = buildJobDefinition(jobId, firstStep, lastStep);

		myJobDefinitionRegistry.addJobDefinition(definition);

		JobInstanceStartRequest request = buildRequest(jobId);

		myFirstStepLatch.setExpectedCount(1);
		String instanceId = myJobCoordinator.startInstance(request);
		myFirstStepLatch.awaitExpected();

		myLastStepLatch.setExpectedCount(1);
		myJobMaintenanceService.runMaintenancePass();
		myLastStepLatch.awaitExpected();

		myBatch2JobHelper.awaitJobCompletion(instanceId);
	}


	@Test
	public void JobExecutionFailedException_CausesInstanceFailure() {
		// setup
		IJobStepWorker<TestJobParameters, VoidModel, FirstStepOutput> firstStep = (step, sink) -> {
			throw new JobExecutionFailedException("Expected Test Exception");
		};
		IJobStepWorker<TestJobParameters, FirstStepOutput, VoidModel> lastStep = (step, sink) -> fail();

		String jobId = "test-job-3";
		JobDefinition<? extends IModelJson> definition = buildJobDefinition(jobId, firstStep, lastStep);

		myJobDefinitionRegistry.addJobDefinition(definition);

		JobInstanceStartRequest request = buildRequest(jobId);

		// execute
		String instanceId = myJobCoordinator.startInstance(request);

		// validate
		myBatch2JobHelper.awaitJobFailure(instanceId);
	}

	@Test
	public void testUnknownException_KeepsInProgress_CanCancelManually() throws InterruptedException {
		// setup
		IJobStepWorker<TestJobParameters, VoidModel, FirstStepOutput> firstStep = (step, sink) -> {
			callLatch(myFirstStepLatch, step);
			throw new RuntimeException("Expected Test Exception");
		};
		IJobStepWorker<TestJobParameters, FirstStepOutput, VoidModel> lastStep = (step, sink) -> fail();

		String jobId = "test-job-4";
		JobDefinition<? extends IModelJson> definition = buildJobDefinition(jobId, firstStep, lastStep);

		myJobDefinitionRegistry.addJobDefinition(definition);

		JobInstanceStartRequest request = buildRequest(jobId);

		// execute
		myFirstStepLatch.setExpectedCount(1);
		String instanceId = myJobCoordinator.startInstance(request);
		myFirstStepLatch.awaitExpected();

		myJobMaintenanceService.runMaintenancePass();

		// validate
		assertEquals(StatusEnum.IN_PROGRESS, myJobCoordinator.getInstance(instanceId).getStatus());

		// execute
		myJobCoordinator.cancelInstance(instanceId);

		// validate
		myBatch2JobHelper.awaitJobCancelled(instanceId);
	}

	@Nonnull
	private JobInstanceStartRequest buildRequest(String jobId) {
		JobInstanceStartRequest request = new JobInstanceStartRequest();
		request.setJobDefinitionId(jobId);
		TestJobParameters parameters = new TestJobParameters();
		request.setParameters(parameters);
		return request;
	}

	@Nonnull
	private JobDefinition<? extends IModelJson> buildJobDefinition(String theJobId, IJobStepWorker<TestJobParameters, VoidModel, FirstStepOutput> theFirstStep, IJobStepWorker<TestJobParameters, FirstStepOutput, VoidModel> theLastStep) {
		return JobDefinition.newBuilder()
			.setJobDefinitionId(theJobId)
			.setJobDescription("test job")
			.setJobDefinitionVersion(TEST_JOB_VERSION)
			.setParametersType(TestJobParameters.class)
			.gatedExecution()
			.addFirstStep(
				"first-step",
				"Test first step",
				FirstStepOutput.class,
				theFirstStep
			)
			.addLastStep(
				"last-step",
				"Test last step",
				theLastStep
			)
			.build();
	}

	static class TestJobParameters implements IModelJson {
		TestJobParameters() {
		}
	}

	static class FirstStepOutput implements IModelJson {
		FirstStepOutput() {
		}
	}
}
