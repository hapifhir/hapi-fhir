package ca.uhn.fhir.jpa.batch2;

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
import ca.uhn.fhir.batch2.model.JobWorkNotificationJsonMessage;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.batch2.model.WorkChunkStatusEnum;
import ca.uhn.fhir.jpa.dao.data.IBatch2JobInstanceRepository;
import ca.uhn.fhir.jpa.dao.data.IBatch2WorkChunkRepository;
import ca.uhn.fhir.jpa.entity.Batch2WorkChunkEntity;
import ca.uhn.fhir.jpa.subscription.channel.api.ChannelConsumerSettings;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelFactory;
import ca.uhn.fhir.jpa.subscription.channel.impl.LinkedBlockingChannel;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.jpa.test.Batch2JobHelper;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.test.concurrency.PointcutLatch;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static ca.uhn.fhir.batch2.config.BaseBatch2Config.CHANNEL_NAME;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

public class Batch2JobMaintenanceDatabaseIT extends BaseJpaR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(Batch2JobMaintenanceDatabaseIT.class);

	public static final int TEST_JOB_VERSION = 1;
	public static final String FIRST_STEP_ID = "first-step";
	public static final String LAST_STEP_ID = "last-step";
	private static final String JOB_DEF_ID = "test-job-definition";
	private static final JobDefinition<? extends IModelJson> ourJobDef = buildJobDefinition();

	@Autowired
	JobDefinitionRegistry myJobDefinitionRegistry;
	@Autowired
	IJobMaintenanceService myJobMaintenanceService;
	@Autowired
	Batch2JobHelper myBatch2JobHelper;
	@Autowired
	private IChannelFactory myChannelFactory;

	@Autowired
	IJobPersistence myJobPersistence;
	@Autowired
	IBatch2JobInstanceRepository myJobInstanceRepository;
	@Autowired
	IBatch2WorkChunkRepository myWorkChunkRepository;

	private LinkedBlockingChannel myWorkChannel;
	private final List<StackTraceElement[]> myStackTraceElements = new ArrayList<>();
	private static final AtomicInteger ourCounter = new AtomicInteger(0);


	private static RunOutcome callLatch(PointcutLatch theLatch, StepExecutionDetails<?, ?> theStep) {
		theLatch.call(theStep);
		return RunOutcome.SUCCESS;
	}

	@BeforeEach
	public void before() {
		myJobDefinitionRegistry.addJobDefinition(ourJobDef);
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
		myWorkChunkRepository.deleteAll();
		myJobInstanceRepository.deleteAll();
	}

	@Test
	public void testCreateInstance() {
		assertInstanceCount( 0);
		storeNewInstance(ourJobDef);
		assertInstanceCount( 1);
		myJobMaintenanceService.runMaintenancePass();
		assertInstanceCount(1);
	}

	@Test
	public void testSingleQueuedChunk() {
		String instanceId = storeNewInstance(ourJobDef);

		assertWorkChunkCount(0);

		storeNewWorkChunk(instanceId, FIRST_STEP_ID, WorkChunkStatusEnum.QUEUED);

		assertWorkChunkCount(instanceId, FIRST_STEP_ID, WorkChunkStatusEnum.QUEUED, 1);
		myJobMaintenanceService.runMaintenancePass();
		assertWorkChunkCount(1);
		assertWorkChunkCount(instanceId, FIRST_STEP_ID, WorkChunkStatusEnum.QUEUED, 1);
	}

	@Test
	public void testSingleInProgressChunk() {
		String instanceId = storeNewInstance(ourJobDef);

		assertWorkChunkCount(0);

		storeNewWorkChunk(instanceId, FIRST_STEP_ID, WorkChunkStatusEnum.IN_PROGRESS);

		assertWorkChunkCount(instanceId, FIRST_STEP_ID, WorkChunkStatusEnum.IN_PROGRESS, 1);
		myJobMaintenanceService.runMaintenancePass();
		assertWorkChunkCount(1);
		assertWorkChunkCount(instanceId, FIRST_STEP_ID, WorkChunkStatusEnum.IN_PROGRESS, 1);
	}

	@Test
	public void testSingleCompleteChunk() {
		String instanceId = storeNewInstance(ourJobDef);

		assertWorkChunkCount(0);

		storeNewWorkChunk(instanceId, FIRST_STEP_ID, WorkChunkStatusEnum.COMPLETED);
		storeNewWorkChunk(instanceId, LAST_STEP_ID, WorkChunkStatusEnum.QUEUED);

		assertWorkChunkCount(instanceId, FIRST_STEP_ID, WorkChunkStatusEnum.COMPLETED, 1);
		assertWorkChunkCount(instanceId, LAST_STEP_ID, WorkChunkStatusEnum.QUEUED, 1);

		myJobMaintenanceService.runMaintenancePass();

		assertWorkChunkCount(2);
		assertWorkChunkCount(instanceId, FIRST_STEP_ID, WorkChunkStatusEnum.COMPLETED, 1);
		assertWorkChunkCount(instanceId, LAST_STEP_ID, WorkChunkStatusEnum.IN_PROGRESS, 1);
	}

	private void assertWorkChunkCount(String theInstanceId, String theStepId, WorkChunkStatusEnum theStatus, int theExpectedCount) {
		List<String> workChunks = myJobPersistence.fetchAllChunkIdsForStepWithStatus(theInstanceId, theStepId, theStatus);
		assertThat(workChunks, hasSize(theExpectedCount));
	}

	@NotNull
	private void storeNewWorkChunk(String instance, String theStepId, WorkChunkStatusEnum theStatus) {
		Batch2WorkChunkEntity workChunk = new Batch2WorkChunkEntity();
		workChunk.setId("chunk" + ourCounter.getAndIncrement());
		workChunk.setJobDefinitionId(JOB_DEF_ID);
		workChunk.setStatus(theStatus);
		workChunk.setJobDefinitionVersion(TEST_JOB_VERSION);
		workChunk.setCreateTime(new Date());
		workChunk.setInstanceId(instance);
		workChunk.setTargetStepId(theStepId);
		myWorkChunkRepository.save(workChunk);
	}

	private void assertWorkChunkCount(int theCount) {
		assertThat(myWorkChunkRepository.findAll(), hasSize(theCount));
	}

	@NotNull
	private static JobDefinition<? extends IModelJson> buildJobDefinition() {
		IJobStepWorker<TestJobParameters, VoidModel, FirstStepOutput> firstStep = (step, sink) -> RunOutcome.SUCCESS;
		IJobStepWorker<TestJobParameters, FirstStepOutput, VoidModel> lastStep = (step, sink) -> RunOutcome.SUCCESS;

		JobDefinition<? extends IModelJson> definition = buildGatedJobDefinition(JOB_DEF_ID, firstStep, lastStep);
		return definition;
	}

	private String storeNewInstance(JobDefinition<? extends IModelJson> definition) {
		JobInstance instance = new JobInstance();
		instance.setStatus(StatusEnum.IN_PROGRESS);
		instance.setJobDefinition(definition);
		instance.setParameters(new TestJobParameters());
		instance.setCurrentGatedStepId(FIRST_STEP_ID);
		return myJobPersistence.storeNewInstance(instance);
	}

	private void assertInstanceCount(int size) {
		assertThat(myJobPersistence.fetchInstancesByJobDefinitionId(JOB_DEF_ID, 100, 0), hasSize(size));
	}

	@Nonnull
	private static JobDefinition<? extends IModelJson> buildGatedJobDefinition(String theJobId, IJobStepWorker<TestJobParameters, VoidModel, FirstStepOutput> theFirstStep, IJobStepWorker<TestJobParameters, FirstStepOutput, VoidModel> theLastStep) {
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
			.completionHandler(details -> {})
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
