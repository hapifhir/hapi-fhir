package ca.uhn.fhir.jpa.batch2;

import ca.uhn.fhir.batch2.api.IJobCompletionHandler;
import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.api.IJobMaintenanceService;
import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.coordinator.JobDefinitionRegistry;
import ca.uhn.fhir.batch2.maintenance.JobMaintenanceServiceImpl;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.batch2.model.JobWorkNotificationJsonMessage;
import ca.uhn.fhir.jpa.subscription.channel.api.ChannelConsumerSettings;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelFactory;
import ca.uhn.fhir.jpa.subscription.channel.impl.LinkedBlockingChannel;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.jpa.test.Batch2JobHelper;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.test.utilities.UnregisterScheduledProcessor;
import ca.uhn.test.concurrency.PointcutLatch;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

import static ca.uhn.fhir.batch2.config.BaseBatch2Config.CHANNEL_NAME;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The on-enter actions are defined in
 * {@link ca.uhn.fhir.batch2.progress.JobInstanceStatusUpdater#handleStatusChange}
 * {@link ca.uhn.fhir.batch2.progress.InstanceProgress#updateStatus(JobInstance)}
 * {@link JobInstanceProcessor#cleanupInstance()}

 * For chunks:
 *   {@link ca.uhn.fhir.jpa.batch2.JpaJobPersistenceImpl#onWorkChunkCreate}
 *   {@link JpaJobPersistenceImpl#onWorkChunkDequeue(String)}
 *   Chunk execution {@link ca.uhn.fhir.batch2.coordinator.StepExecutor#executeStep}
*/
@TestPropertySource(properties = {
	UnregisterScheduledProcessor.SCHEDULING_DISABLED_EQUALS_FALSE
})
@ContextConfiguration(classes = {Batch2JobMaintenanceIT.SpringConfig.class})
public class Batch2JobMaintenanceIT extends BaseJpaR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(Batch2JobMaintenanceIT.class);

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
	private final List<StackTraceElement[]> myStackTraceElements = new ArrayList<>();

	private static RunOutcome callLatch(PointcutLatch theLatch, StepExecutionDetails<?, ?> theStep) {
		theLatch.call(theStep);
		return RunOutcome.SUCCESS;
	}

	@BeforeEach
	public void before() {
		myCompletionHandler = details -> {};
		myWorkChannel = (LinkedBlockingChannel) myChannelFactory.getOrCreateReceiver(CHANNEL_NAME, JobWorkNotificationJsonMessage.class, new ChannelConsumerSettings());
		JobMaintenanceServiceImpl jobMaintenanceService = (JobMaintenanceServiceImpl) myJobMaintenanceService;
		jobMaintenanceService.setMaintenanceJobStartedCallback(() -> {
			ourLog.info("Batch maintenance job started");
			myStackTraceElements.add(Thread.currentThread().getStackTrace());
		});
	}

	@AfterEach
	public void after() {
		myWorkChannel.clearInterceptorsForUnitTest();
		myDaoConfig.setJobFastTrackingEnabled(true);
		JobMaintenanceServiceImpl jobMaintenanceService = (JobMaintenanceServiceImpl) myJobMaintenanceService;
		jobMaintenanceService.setMaintenanceJobStartedCallback(() -> {});
	}

	@Test
	public void testFirstStepToSecondStep_singleChunkFasttracks() throws InterruptedException {
		IJobStepWorker<TestJobParameters, VoidModel, FirstStepOutput> firstStep = (step, sink) -> {
			sink.accept(new FirstStepOutput());
			callLatch(myFirstStepLatch, step);
			return RunOutcome.SUCCESS;
		};
		IJobStepWorker<TestJobParameters, FirstStepOutput, VoidModel> lastStep = (step, sink) -> callLatch(myLastStepLatch, step);

		String jobDefId = new Exception().getStackTrace()[0].getMethodName();
		JobDefinition<? extends IModelJson> definition = buildGatedJobDefinition(jobDefId, firstStep, lastStep);

		myJobDefinitionRegistry.addJobDefinition(definition);

		JobInstanceStartRequest request = buildRequest(jobDefId);

		myFirstStepLatch.setExpectedCount(1);
		myLastStepLatch.setExpectedCount(1);
		String batchJobId = myJobCoordinator.startInstance(request).getJobId();
		myFirstStepLatch.awaitExpected();

		myBatch2JobHelper.assertFastTracking(batchJobId);

		// Since there was only one chunk, the job should proceed without requiring a maintenance pass
		myBatch2JobHelper.awaitJobCompletion(batchJobId);
		myBatch2JobHelper.assertFastTracking(batchJobId);
		myLastStepLatch.awaitExpected();
		myBatch2JobHelper.assertFastTracking(batchJobId);
		assertJobMaintenanceCalledByQuartzThread();
		assertJobMaintenanceCalledAtLeast(2);
	}

	private void assertJobMaintenanceCalledAtLeast(int theSize) {
		assertTrue(myStackTraceElements.size() >= theSize, "Expected at least " + theSize + " calls to job maintenance but got " + myStackTraceElements.size());
	}

	private void assertJobMaintenanceCalledByQuartzThread() {
		StackTraceElement[] stackTrace = myStackTraceElements.get(0);
		boolean found = false;
		for (StackTraceElement stackTraceElement : stackTrace) {
			if (stackTraceElement.getClassName().equals("org.quartz.core.JobRunShell")) {
				found = true;
				break;
			}
		}
		assertTrue(found, "Job maintenance should be called by Quartz thread");
	}

	@Test
	public void testFirstStepToSecondStepFasttrackingDisabled_singleChunkDoesNotFasttrack() throws InterruptedException {
		myDaoConfig.setJobFastTrackingEnabled(false);

		IJobStepWorker<Batch2JobMaintenanceIT.TestJobParameters, VoidModel, Batch2JobMaintenanceIT.FirstStepOutput> firstStep = (step, sink) -> {
			sink.accept(new Batch2JobMaintenanceIT.FirstStepOutput());
			callLatch(myFirstStepLatch, step);
			return RunOutcome.SUCCESS;
		};
		IJobStepWorker<Batch2JobMaintenanceIT.TestJobParameters, Batch2JobMaintenanceIT.FirstStepOutput, VoidModel> lastStep = (step, sink) -> callLatch(myLastStepLatch, step);

		String jobDefId = new Exception().getStackTrace()[0].getMethodName();

		JobDefinition<? extends IModelJson> definition = buildGatedJobDefinition(jobDefId, firstStep, lastStep);

		myJobDefinitionRegistry.addJobDefinition(definition);

		JobInstanceStartRequest request = buildRequest(jobDefId);

		myFirstStepLatch.setExpectedCount(1);
		myLastStepLatch.setExpectedCount(1);
		String batchJobId = myJobCoordinator.startInstance(request).getJobId();
		myFirstStepLatch.awaitExpected();

		myBatch2JobHelper.assertFastTracking(batchJobId);

		// Since there was only one chunk, the job should request fasttracking
		myBatch2JobHelper.awaitJobCompletionWithoutMaintenancePass(batchJobId);

		// However since we disabled fasttracking, the job should not have fasttracked
		myBatch2JobHelper.assertNotFastTracking(batchJobId);
		myLastStepLatch.awaitExpected();
		myBatch2JobHelper.assertNotFastTracking(batchJobId);
		assertJobMaintenanceCalledByQuartzThread();
		assertJobMaintenanceCalledAtLeast(2);
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

	static class SpringConfig {
		@Autowired
		IJobMaintenanceService myJobMaintenanceService;

		@PostConstruct
		void fastScheduler() {
			((JobMaintenanceServiceImpl)myJobMaintenanceService).setScheduledJobFrequencyMillis(200);
		}
	}
}
