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
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.batch2.model.JobWorkNotificationJsonMessage;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.jpa.subscription.channel.api.ChannelConsumerSettings;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelFactory;
import ca.uhn.fhir.jpa.subscription.channel.impl.LinkedBlockingChannel;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.jpa.test.Batch2JobHelper;
import ca.uhn.fhir.jpa.test.config.Batch2FastSchedulerConfig;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.test.utilities.UnregisterScheduledProcessor;
import ca.uhn.fhir.testjob.TestJobDefinitionUtils;
import ca.uhn.fhir.testjob.models.FirstStepOutput;
import ca.uhn.fhir.testjob.models.ReductionStepOutput;
import ca.uhn.fhir.testjob.models.TestJobParameters;
import ca.uhn.test.concurrency.PointcutLatch;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nonnull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;

import static ca.uhn.fhir.batch2.config.BaseBatch2Config.CHANNEL_NAME;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * The on-enter actions are defined in
 * {@link ca.uhn.fhir.batch2.progress.JobInstanceStatusUpdater#handleStatusChange(JobInstance)}}
 * {@link ca.uhn.fhir.batch2.progress.InstanceProgress#updateStatus(JobInstance)}
 * {@link ca.uhn.fhir.batch2.maintenance.JobInstanceProcessor#cleanupInstance()}

 * For chunks:
 *   {@link ca.uhn.fhir.jpa.batch2.JpaJobPersistenceImpl#onWorkChunkCreate}
 *   {@link JpaJobPersistenceImpl#onWorkChunkDequeue(String)}
 *   Chunk execution {@link ca.uhn.fhir.batch2.coordinator.StepExecutor#executeStep}
*/
@TestPropertySource(properties = {
	UnregisterScheduledProcessor.SCHEDULING_DISABLED_EQUALS_FALSE
})
@ContextConfiguration(classes = {Batch2FastSchedulerConfig.class})
public class Batch2JobMaintenanceIT extends BaseJpaR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(Batch2JobMaintenanceIT.class);

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
		myStorageSettings.setJobFastTrackingEnabled(true);
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
		String batchJobId = myJobCoordinator.startInstance(new SystemRequestDetails(), request).getInstanceId();

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
		assertThat(myStackTraceElements.size() >= theSize).as("Expected at least " + theSize + " calls to job maintenance but got " + myStackTraceElements.size()).isTrue();
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
		assertThat(found).as("Job maintenance should be called by Quartz thread").isTrue();
	}

	@Test
	public void testFirstStepToSecondStepFasttrackingDisabled_singleChunkDoesNotFasttrack() throws InterruptedException {
		myStorageSettings.setJobFastTrackingEnabled(false);

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
		String batchJobId = myJobCoordinator.startInstance(new SystemRequestDetails(), request).getInstanceId();
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
		return TestJobDefinitionUtils.buildGatedJobDefinition(
			theJobId,
			theFirstStep,
			theLastStep,
			myCompletionHandler
		);
	}

	static class OurReductionStepOutput extends ReductionStepOutput {
		@JsonProperty("result")
		private List<?> myResult;

		OurReductionStepOutput(List<?> theResult) {
			myResult = theResult;
		}
	}
}
