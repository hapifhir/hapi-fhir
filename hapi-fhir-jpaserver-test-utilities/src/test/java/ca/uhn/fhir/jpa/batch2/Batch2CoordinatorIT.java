package ca.uhn.fhir.jpa.batch2;

import ca.uhn.fhir.batch2.api.ChunkExecutionDetails;
import ca.uhn.fhir.batch2.api.IJobCompletionHandler;
import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobMaintenanceService;
import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.IReductionStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.coordinator.JobDefinitionRegistry;
import ca.uhn.fhir.batch2.model.ChunkOutcome;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.jpa.test.Batch2JobHelper;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.util.JsonUtil;
import ca.uhn.test.concurrency.LatchTimedOutError;
import ca.uhn.test.concurrency.PointcutLatch;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class Batch2CoordinatorIT extends BaseJpaR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(Batch2CoordinatorIT.class);

	public static final int TEST_JOB_VERSION = 1;
	public static final String FIRST_STEP_ID = "first-step";
	public static final String LAST_STEP_ID = "last-step";
	@Autowired
	JobDefinitionRegistry myJobDefinitionRegistry;
	@Autowired
	IJobCoordinator myJobCoordinator;
	@Autowired
	IJobMaintenanceService myJobMaintenanceService;
	@Autowired
	Batch2JobHelper myBatch2JobHelper;

	@Autowired
	IJobPersistence myJobPersistence;

	private final PointcutLatch myFirstStepLatch = new PointcutLatch("First Step");
	private final PointcutLatch myLastStepLatch = new PointcutLatch("Last Step");
	private IJobCompletionHandler<TestJobParameters> myCompletionHandler;

	private static RunOutcome callLatch(PointcutLatch theLatch, StepExecutionDetails<?, ?> theStep) {
		theLatch.call(theStep);
		return RunOutcome.SUCCESS;
	}

	@BeforeEach
	public void before() {
		 myCompletionHandler = details -> {};
	}

	@Test
	public void testFirstStepNoSink() throws InterruptedException {
		IJobStepWorker<TestJobParameters, VoidModel, FirstStepOutput> firstStep = (step, sink) -> callLatch(myFirstStepLatch, step);
		IJobStepWorker<TestJobParameters, FirstStepOutput, VoidModel> lastStep = (step, sink) -> fail();

		String jobId = "test-job-1";
		JobDefinition<? extends IModelJson> definition = buildGatedJobDefinition(jobId, firstStep, lastStep);

		myJobDefinitionRegistry.addJobDefinition(definition);

		JobInstanceStartRequest request = buildRequest(jobId);

		myFirstStepLatch.setExpectedCount(1);
		Batch2JobStartResponse startResponse = myJobCoordinator.startInstance(request);
		myFirstStepLatch.awaitExpected();

		myBatch2JobHelper.awaitSingleChunkJobCompletion(startResponse.getJobId());
	}

	@Test
	public void testFirstStepToSecondStep_singleChunkFasttracks() throws InterruptedException {
		IJobStepWorker<TestJobParameters, VoidModel, FirstStepOutput> firstStep = (step, sink) -> {
			sink.accept(new FirstStepOutput());
			callLatch(myFirstStepLatch, step);
			return RunOutcome.SUCCESS;
		};
		IJobStepWorker<TestJobParameters, FirstStepOutput, VoidModel> lastStep = (step, sink) -> callLatch(myLastStepLatch, step);

		String jobId = "test-job-2";
		JobDefinition<? extends IModelJson> definition = buildGatedJobDefinition(jobId, firstStep, lastStep);

		myJobDefinitionRegistry.addJobDefinition(definition);

		JobInstanceStartRequest request = buildRequest(jobId);

		myFirstStepLatch.setExpectedCount(1);
		myLastStepLatch.setExpectedCount(1);
		Batch2JobStartResponse startResponse = myJobCoordinator.startInstance(request);
		myFirstStepLatch.awaitExpected();

		myBatch2JobHelper.assertNoGatedStep(startResponse.getJobId());

		// Since there was only one chunk, the job should proceed without requiring a maintenance pass
		myBatch2JobHelper.awaitSingleChunkJobCompletion(startResponse.getJobId());
		myLastStepLatch.awaitExpected();
	}


	@Test
	public void testFastTrack_Maintenance_do_not_both_call_CompletionHandler() throws InterruptedException {
		IJobStepWorker<TestJobParameters, VoidModel, FirstStepOutput> firstStep = (step, sink) -> {
			sink.accept(new FirstStepOutput());
			callLatch(myFirstStepLatch, step);
			return RunOutcome.SUCCESS;
		};

		IJobStepWorker<TestJobParameters, FirstStepOutput, VoidModel> lastStep = (step, sink) -> callLatch(myLastStepLatch, step);

		String jobId = "test-job-2a";
		String completionHandlerLatchName = "Completion Handler";
		PointcutLatch calledLatch = new PointcutLatch(completionHandlerLatchName);
		CountDownLatch waitLatch = new CountDownLatch(2);

		myCompletionHandler = details -> {
			try {
				calledLatch.call(details);
				waitLatch.await();
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		};

		JobDefinition<? extends IModelJson> definition = buildGatedJobDefinition(jobId, firstStep, lastStep);

		myJobDefinitionRegistry.addJobDefinition(definition);

		JobInstanceStartRequest request = buildRequest(jobId);

		myFirstStepLatch.setExpectedCount(1);
		myLastStepLatch.setExpectedCount(1);
		calledLatch.setExpectedCount(1);
		String instanceId = myJobCoordinator.startInstance(request);
		myFirstStepLatch.awaitExpected();
		calledLatch.awaitExpected();

		myBatch2JobHelper.assertNoGatedStep(instanceId);

		// Start a maintenance run in the background
		ExecutorService executor = Executors.newSingleThreadExecutor();

		// Now queue up the maintenance call
		calledLatch.setExpectedCount(1);
		executor.submit(() -> myJobMaintenanceService.runMaintenancePass());

		// We should have only called the completion handler once
		try {
			// This test will pause for 5 seconds here.  This should be more than enough time on most servers to hit the
			// spot where the maintenance services calls the completion handler
			calledLatch.awaitExpectedWithTimeout(5);
			fail();
		} catch (LatchTimedOutError e) {
			assertEquals("HAPI-1483: " + completionHandlerLatchName + " PointcutLatch timed out waiting 5 seconds for latch to countdown from 1 to 0.  Is 1.", e.getMessage());
		}

		// Now release the latches
		waitLatch.countDown();
		waitLatch.countDown(); // This shouldn't be necessary, but just in case

		// Since there was only one chunk, the job should proceed without requiring a maintenance pass
		myBatch2JobHelper.awaitSingleChunkJobCompletion(instanceId);
		myLastStepLatch.awaitExpected();
	}


	@Test
	public void testJobDefinitionWithReductionStepIT() throws InterruptedException {
		// setup
		String testInfo = "test";
		AtomicInteger secondStepInt = new AtomicInteger();

		// step 1
		IJobStepWorker<TestJobParameters, VoidModel, FirstStepOutput> first = (step, sink) -> {
			sink.accept(new FirstStepOutput());
			sink.accept(new FirstStepOutput());
			callLatch(myFirstStepLatch, step);
			return RunOutcome.SUCCESS;
		};

		// step 2
		IJobStepWorker<TestJobParameters, FirstStepOutput, SecondStepOutput> second = (step, sink) -> {
			SecondStepOutput output = new SecondStepOutput();
			output.setValue(testInfo + secondStepInt.getAndIncrement());
			sink.accept(output);
			return RunOutcome.SUCCESS;
		};

		// step 3
		IReductionStepWorker<TestJobParameters, SecondStepOutput, ReductionStepOutput> last = new IReductionStepWorker<TestJobParameters, SecondStepOutput, ReductionStepOutput>() {

			private final ArrayList<SecondStepOutput> myOutput = new ArrayList<>();

			@Override
			public ChunkOutcome consume(ChunkExecutionDetails<TestJobParameters, SecondStepOutput> theChunkDetails) {
				myOutput.add(theChunkDetails.getData());
				return ChunkOutcome.SUCCESS();
			}

			@Nonnull
			@Override
			public RunOutcome run(@Nonnull StepExecutionDetails<TestJobParameters, SecondStepOutput> theStepExecutionDetails,
										 @Nonnull IJobDataSink<ReductionStepOutput> theDataSink) throws JobExecutionFailedException {
				theDataSink.accept(new ReductionStepOutput(myOutput));
				callLatch(myLastStepLatch, theStepExecutionDetails);
				return RunOutcome.SUCCESS;
			}
		};

		// create job definition
		String jobId = new Exception().getStackTrace()[0].getMethodName();
		JobDefinition<? extends IModelJson> jd = JobDefinition.newBuilder()
			.setJobDefinitionId(jobId)
			.setJobDescription("test job")
			.setJobDefinitionVersion(TEST_JOB_VERSION)
			.setParametersType(TestJobParameters.class)
			.gatedExecution()
			.addFirstStep(
				FIRST_STEP_ID,
				"Test first step",
				FirstStepOutput.class,
				first
			)
			.addIntermediateStep("SECOND",
				"Second step",
				SecondStepOutput.class,
				second)
			.addFinalReducerStep(
				LAST_STEP_ID,
				"Test last step",
				ReductionStepOutput.class,
				last
			)
			.build();
		myJobDefinitionRegistry.addJobDefinition(jd);

		// run test
		JobInstanceStartRequest request = buildRequest(jobId);
		myFirstStepLatch.setExpectedCount(1);
		Batch2JobStartResponse startResponse = myJobCoordinator.startInstance(request);
		String instanceId = startResponse.getJobId();
		myFirstStepLatch.awaitExpected();


		myBatch2JobHelper.awaitGatedStepId(FIRST_STEP_ID, instanceId);

		// wait for last step to finish
		myLastStepLatch.setExpectedCount(1);
		myBatch2JobHelper.awaitJobCompletion(instanceId);
		myLastStepLatch.awaitExpected();

		// verify
		Optional<JobInstance> instanceOp = myJobPersistence.fetchInstance(instanceId);
		assertTrue(instanceOp.isPresent());
		int secondStepCalls = secondStepInt.get();
		assertEquals(2, secondStepCalls);
		JobInstance instance = instanceOp.get();
		ourLog.info(JsonUtil.serialize(instance, true));
		assertNotNull(instance.getReport());

		for (int i = 0; i < secondStepInt.get(); i++) {
			assertTrue(instance.getReport().contains(
				testInfo + i
			));
		}
	}

	@Test
	public void testFirstStepToSecondStep_doubleChunk_doesNotFastTrack() throws InterruptedException {
		IJobStepWorker<TestJobParameters, VoidModel, FirstStepOutput> firstStep = (step, sink) -> {
			sink.accept(new FirstStepOutput());
			sink.accept(new FirstStepOutput());
			callLatch(myFirstStepLatch, step);
			return RunOutcome.SUCCESS;
		};
		IJobStepWorker<TestJobParameters, FirstStepOutput, VoidModel> lastStep = (step, sink) -> callLatch(myLastStepLatch, step);

		String jobId = "test-job-5";
		JobDefinition<? extends IModelJson> definition = buildGatedJobDefinition(jobId, firstStep, lastStep);

		myJobDefinitionRegistry.addJobDefinition(definition);

		JobInstanceStartRequest request = buildRequest(jobId);

		myFirstStepLatch.setExpectedCount(1);
		Batch2JobStartResponse startResponse = myJobCoordinator.startInstance(request);
		String instanceId = startResponse.getJobId();
		myFirstStepLatch.awaitExpected();

		myBatch2JobHelper.awaitGatedStepId(FIRST_STEP_ID, instanceId);

		myLastStepLatch.setExpectedCount(2);
		myBatch2JobHelper.awaitJobCompletion(instanceId);
		myLastStepLatch.awaitExpected();
	}


	@Test
	public void JobExecutionFailedException_CausesInstanceFailure() {
		// setup
		IJobStepWorker<TestJobParameters, VoidModel, FirstStepOutput> firstStep = (step, sink) -> {
			throw new JobExecutionFailedException("Expected Test Exception");
		};
		IJobStepWorker<TestJobParameters, FirstStepOutput, VoidModel> lastStep = (step, sink) -> fail();

		String jobId = "test-job-3";
		JobDefinition<? extends IModelJson> definition = buildGatedJobDefinition(jobId, firstStep, lastStep);

		myJobDefinitionRegistry.addJobDefinition(definition);

		JobInstanceStartRequest request = buildRequest(jobId);

		// execute
		Batch2JobStartResponse startResponse = myJobCoordinator.startInstance(request);
		String instanceId = startResponse.getJobId();

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
		JobDefinition<? extends IModelJson> definition = buildGatedJobDefinition(jobId, firstStep, lastStep);

		myJobDefinitionRegistry.addJobDefinition(definition);

		JobInstanceStartRequest request = buildRequest(jobId);

		// execute
		myFirstStepLatch.setExpectedCount(1);
		Batch2JobStartResponse startResponse = myJobCoordinator.startInstance(request);
		String instanceId = startResponse.getJobId();
		myFirstStepLatch.awaitExpected();

		// validate
		myBatch2JobHelper.awaitJobInProgress(instanceId);

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
	private JobDefinition<? extends IModelJson> buildGatedJobDefinition(String theJobId, IJobStepWorker<TestJobParameters, VoidModel, FirstStepOutput> theFirstStep, IJobStepWorker<TestJobParameters, FirstStepOutput, VoidModel> theLastStep) {
		return JobDefinition.newBuilder()
			.setJobDefinitionId(theJobId)
			.setJobDescription("test job")
			.setJobDefinitionVersion(TEST_JOB_VERSION)
			.setParametersType(TestJobParameters.class)
			.gatedExecution()
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
			.completionHandler(myCompletionHandler)
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

	static class SecondStepOutput implements IModelJson {
		@JsonProperty("test")
		private String myTestValue;

		SecondStepOutput() {
		}

		public void setValue(String theV) {
			myTestValue = theV;
		}
	}

	static class ReductionStepOutput implements IModelJson {
		@JsonProperty("result")
		private List<?> myResult;

		ReductionStepOutput(List<?> theResult) {
			myResult = theResult;
		}
	}
}
