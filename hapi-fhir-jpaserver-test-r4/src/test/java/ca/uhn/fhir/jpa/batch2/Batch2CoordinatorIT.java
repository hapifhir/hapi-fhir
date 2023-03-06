package ca.uhn.fhir.jpa.batch2;

import ca.uhn.fhir.batch2.api.ChunkExecutionDetails;
import ca.uhn.fhir.batch2.api.IJobCompletionHandler;
import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobMaintenanceService;
import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.ILastJobStepWorker;
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
import ca.uhn.fhir.batch2.model.JobWorkNotificationJsonMessage;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.batch2.models.JobInstanceFetchRequest;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import ca.uhn.fhir.jpa.subscription.channel.api.ChannelConsumerSettings;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelFactory;
import ca.uhn.fhir.jpa.subscription.channel.impl.LinkedBlockingChannel;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.jpa.test.Batch2JobHelper;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.util.JsonUtil;
import ca.uhn.test.concurrency.PointcutLatch;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static ca.uhn.fhir.batch2.config.BaseBatch2Config.CHANNEL_NAME;
import static ca.uhn.fhir.batch2.coordinator.WorkChunkProcessor.MAX_CHUNK_ERROR_COUNT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
	private IChannelFactory myChannelFactory;

	@Autowired
	IJobPersistence myJobPersistence;

	private final PointcutLatch myFirstStepLatch = new PointcutLatch("First Step");
	private final PointcutLatch myLastStepLatch = new PointcutLatch("Last Step");
	private IJobCompletionHandler<TestJobParameters> myCompletionHandler;
	private LinkedBlockingChannel myWorkChannel;

	private static RunOutcome callLatch(PointcutLatch theLatch, StepExecutionDetails<?, ?> theStep) {
		theLatch.call(theStep);
		return RunOutcome.SUCCESS;
	}

	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();

		myCompletionHandler = details -> {};
		myWorkChannel = (LinkedBlockingChannel) myChannelFactory.getOrCreateReceiver(CHANNEL_NAME, JobWorkNotificationJsonMessage.class, new ChannelConsumerSettings());
		myStorageSettings.setJobFastTrackingEnabled(true);
	}

	@AfterEach
	public void after() {
		myWorkChannel.clearInterceptorsForUnitTest();
	}

	@Test
	public void fetchAllJobInstances_withValidInput_returnsPage() {
		int maxJobsToSave = 10;

		// create a job
		// step 1
		IJobStepWorker<TestJobParameters, VoidModel, FirstStepOutput> first = (step, sink) -> {
			return RunOutcome.SUCCESS;
		};
		// final step
		ILastJobStepWorker<TestJobParameters, FirstStepOutput> last = (step, sink) -> {
			return RunOutcome.SUCCESS;
		};
		// job definition
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
			.addLastStep(
				LAST_STEP_ID,
				"Test last step",
				last
			)
			.build();
		myJobDefinitionRegistry.addJobDefinition(jd);

		// start a number of jobs
		List<String> jobIds = new ArrayList<>();
		for (int i = 0; i < maxJobsToSave; i++) {
			JobInstanceStartRequest request = buildRequest(jobId);
			Batch2JobStartResponse response = myJobCoordinator.startInstance(request);
			jobIds.add(response.getInstanceId());
		}

		// run the test
		// see if we can fetch jobs
		int index = 0;
		int size = 2;
		JobInstanceFetchRequest request = new JobInstanceFetchRequest();
		request.setPageStart(index);
		request.setBatchSize(size);
		request.setSort(Sort.unsorted());

		Page<JobInstance> page;
		Iterator<JobInstance> iterator;
		int pageIndex = 0;
		List<String> fetched = new ArrayList<>();
		do {
			// create / update our request
			request.setPageStart(pageIndex);
			page = myJobCoordinator.fetchAllJobInstances(request);
			iterator = page.iterator();

			while (iterator.hasNext()) {
				JobInstance next = iterator.next();
				assertTrue(jobIds.contains(next.getInstanceId()));
				fetched.add(next.getInstanceId());
			}

			pageIndex++;
		} while (page.hasNext());

		assertEquals(maxJobsToSave, fetched.size());
	}

	@Test
	public void testFirstStepNoSink() throws InterruptedException {
		IJobStepWorker<TestJobParameters, VoidModel, FirstStepOutput> firstStep = (step, sink) -> callLatch(myFirstStepLatch, step);
		IJobStepWorker<TestJobParameters, FirstStepOutput, VoidModel> lastStep = (step, sink) -> fail();

		String jobId = new Exception().getStackTrace()[0].getMethodName();
		JobDefinition<? extends IModelJson> definition = buildGatedJobDefinition(jobId, firstStep, lastStep);

		myJobDefinitionRegistry.addJobDefinition(definition);

		JobInstanceStartRequest request = buildRequest(jobId);

		myFirstStepLatch.setExpectedCount(1);
		Batch2JobStartResponse startResponse = myJobCoordinator.startInstance(request);
		myFirstStepLatch.awaitExpected();

		myBatch2JobHelper.awaitJobCompletion(startResponse.getInstanceId());
	}

	@Test
	public void testFirstStepToSecondStep_singleChunkFasttracks() throws InterruptedException {
		IJobStepWorker<TestJobParameters, VoidModel, FirstStepOutput> firstStep = (step, sink) -> {
			sink.accept(new FirstStepOutput());
			callLatch(myFirstStepLatch, step);
			return RunOutcome.SUCCESS;
		};
		IJobStepWorker<TestJobParameters, FirstStepOutput, VoidModel> lastStep = (step, sink) -> callLatch(myLastStepLatch, step);

		String jobDefId = "test-job-2";
		JobDefinition<? extends IModelJson> definition = buildGatedJobDefinition(jobDefId, firstStep, lastStep);

		myJobDefinitionRegistry.addJobDefinition(definition);

		JobInstanceStartRequest request = buildRequest(jobDefId);

		myFirstStepLatch.setExpectedCount(1);
		myLastStepLatch.setExpectedCount(1);
		String batchJobId = myJobCoordinator.startInstance(request).getInstanceId();
		myFirstStepLatch.awaitExpected();

		myBatch2JobHelper.assertFastTracking(batchJobId);

		// Since there was only one chunk, the job should proceed without requiring a maintenance pass
		myBatch2JobHelper.awaitJobCompletion(batchJobId);
		myLastStepLatch.awaitExpected();
	}

	private void createThreeStepReductionJob(
		String theJobId,
		IJobStepWorker<TestJobParameters, VoidModel, FirstStepOutput> theFirstStep,
		IJobStepWorker<TestJobParameters, FirstStepOutput, SecondStepOutput> theSecondStep,
		IReductionStepWorker<TestJobParameters, SecondStepOutput, ReductionStepOutput> theReductionsStep
	) {
		// create job definition (it's the test method's name)
		JobDefinition<? extends IModelJson> jd = JobDefinition.newBuilder()
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
			.addIntermediateStep("SECOND",
				"Second step",
				SecondStepOutput.class,
				theSecondStep)
			.addFinalReducerStep(
				LAST_STEP_ID,
				"Test last step",
				ReductionStepOutput.class,
				theReductionsStep
			)
			.build();
		myJobDefinitionRegistry.addJobDefinition(jd);
	}

	@ParameterizedTest
	@ValueSource(booleans = { true, false })
	public void testJobDefinitionWithReductionStepIT(boolean theDelayReductionStepBool) throws InterruptedException {
		// setup
		String jobId = new Exception().getStackTrace()[0].getMethodName() + "_" + theDelayReductionStepBool;
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

			private final AtomicBoolean myBoolean = new AtomicBoolean();

			private final AtomicInteger mySecondGate = new AtomicInteger();

			@Override
			public ChunkOutcome consume(ChunkExecutionDetails<TestJobParameters, SecondStepOutput> theChunkDetails) {
				myOutput.add(theChunkDetails.getData());
				// 1 because we know 2 packets are coming.
				// we'll fire the second maintenance run on the second packet
				// which should cause multiple maintenance runs to run simultaneously
				if (theDelayReductionStepBool && mySecondGate.getAndIncrement() == 1) {
					ourLog.info("SECOND FORCED MAINTENANCE PASS FORCED");
					myBatch2JobHelper.forceRunMaintenancePass();
				}
				return ChunkOutcome.SUCCESS();
			}

			@Nonnull
			@Override
			public RunOutcome run(
				@Nonnull StepExecutionDetails<TestJobParameters, SecondStepOutput> theStepExecutionDetails,
				@Nonnull IJobDataSink<ReductionStepOutput> theDataSink
			) throws JobExecutionFailedException {
				boolean isRunAlready = myBoolean.getAndSet(true);
				assertFalse(isRunAlready, "Reduction step should only be called once!");

				complete(theStepExecutionDetails, theDataSink);
				return RunOutcome.SUCCESS;
			}

			private void complete(
				@Nonnull StepExecutionDetails<TestJobParameters, SecondStepOutput> theStepExecutionDetails,
				@Nonnull IJobDataSink<ReductionStepOutput> theDataSink
			) {
				assertTrue(myBoolean.get());
				theDataSink.accept(new ReductionStepOutput(myOutput));
				callLatch(myLastStepLatch, theStepExecutionDetails);
			}
		};
		createThreeStepReductionJob(jobId, first, second, last);

		// run test
		JobInstanceStartRequest request = buildRequest(jobId);
		myFirstStepLatch.setExpectedCount(1);
		Batch2JobStartResponse startResponse = myJobCoordinator.startInstance(request);

		String instanceId = startResponse.getInstanceId();
		myFirstStepLatch.awaitExpected();
		assertNotNull(instanceId);

		myBatch2JobHelper.awaitGatedStepId(FIRST_STEP_ID, instanceId);

		// wait for last step to finish
		ourLog.info("Setting last step latch");
		myLastStepLatch.setExpectedCount(1);

		// waiting
		myBatch2JobHelper.awaitJobCompletion(instanceId);
		myLastStepLatch.awaitExpected();
		ourLog.info("awaited the last step");

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
			return callLatch(myFirstStepLatch, step);
		};
		IJobStepWorker<TestJobParameters, FirstStepOutput, VoidModel> lastStep = (step, sink) -> callLatch(myLastStepLatch, step);

		String jobDefId = new Exception().getStackTrace()[0].getMethodName();
		JobDefinition<? extends IModelJson> definition = buildGatedJobDefinition(jobDefId, firstStep, lastStep);

		myJobDefinitionRegistry.addJobDefinition(definition);

		JobInstanceStartRequest request = buildRequest(jobDefId);

		myFirstStepLatch.setExpectedCount(1);
		Batch2JobStartResponse startResponse = myJobCoordinator.startInstance(request);
		String instanceId = startResponse.getInstanceId();
		myFirstStepLatch.awaitExpected();

		myLastStepLatch.setExpectedCount(2);
		myBatch2JobHelper.awaitJobCompletion(instanceId);
		myLastStepLatch.awaitExpected();

		// Now we've processed 2 chunks so we are no longer fast tracking
		myBatch2JobHelper.assertNotFastTracking(instanceId);
	}


	@Test
	public void JobExecutionFailedException_CausesInstanceFailure() {
		// setup
		IJobStepWorker<TestJobParameters, VoidModel, FirstStepOutput> firstStep = (step, sink) -> {
			throw new JobExecutionFailedException("Expected Test Exception");
		};
		IJobStepWorker<TestJobParameters, FirstStepOutput, VoidModel> lastStep = (step, sink) -> fail();

		String jobDefId = new Exception().getStackTrace()[0].getMethodName();
		JobDefinition<? extends IModelJson> definition = buildGatedJobDefinition(jobDefId, firstStep, lastStep);

		myJobDefinitionRegistry.addJobDefinition(definition);

		JobInstanceStartRequest request = buildRequest(jobDefId);

		// execute
		Batch2JobStartResponse startResponse = myJobCoordinator.startInstance(request);
		String instanceId = startResponse.getInstanceId();

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

		String jobDefId = new Exception().getStackTrace()[0].getMethodName();
		JobDefinition<? extends IModelJson> definition = buildGatedJobDefinition(jobDefId, firstStep, lastStep);

		myJobDefinitionRegistry.addJobDefinition(definition);

		JobInstanceStartRequest request = buildRequest(jobDefId);

		// execute
		myFirstStepLatch.setExpectedCount(1);
		Batch2JobStartResponse startResponse = myJobCoordinator.startInstance(request);
		String instanceId = startResponse.getInstanceId();
		myFirstStepLatch.awaitExpected();

		// validate
		myBatch2JobHelper.awaitJobInProgress(instanceId);

		// execute
		myJobCoordinator.cancelInstance(instanceId);

		// validate
		myBatch2JobHelper.awaitJobCancelled(instanceId);
	}

	@Test
	public void testStepRunFailure_continuouslyThrows_marksJobFailed() {

		// setup
		AtomicInteger counter = new AtomicInteger();
		// step 1
		IJobStepWorker<TestJobParameters, VoidModel, FirstStepOutput> first = (step, sink) -> {
			counter.getAndIncrement();
			throw new RuntimeException("Exception");
		};
		// final step
		ILastJobStepWorker<TestJobParameters, FirstStepOutput> last = (step, sink) -> {
			fail("We should never hit this last step");
			return RunOutcome.SUCCESS;
		};
		// job definition
		String jobDefId = new Exception().getStackTrace()[0].getMethodName();
		JobDefinition<? extends IModelJson> jd = JobDefinition.newBuilder()
			.setJobDefinitionId(jobDefId)
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
			.addLastStep(
				LAST_STEP_ID,
				"Test last step",
				last
			)
			.build();
		myJobDefinitionRegistry.addJobDefinition(jd);
		// test
		JobInstanceStartRequest request = buildRequest(jobDefId);
		myFirstStepLatch.setExpectedCount(1);
		Batch2JobStartResponse response = myJobCoordinator.startInstance(request);
		JobInstance instance = myBatch2JobHelper.awaitJobHasStatus(response.getInstanceId(),
			12, // we want to wait a long time (2 min here) cause backoff is incremental
			StatusEnum.FAILED
		);

		assertEquals(MAX_CHUNK_ERROR_COUNT + 1, counter.get());

		assertTrue(instance.getStatus() == StatusEnum.FAILED);
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
