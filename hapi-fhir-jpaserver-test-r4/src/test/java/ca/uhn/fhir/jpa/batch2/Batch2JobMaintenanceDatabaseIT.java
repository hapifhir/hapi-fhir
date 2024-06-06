package ca.uhn.fhir.jpa.batch2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import ca.uhn.fhir.batch2.api.IJobMaintenanceService;
import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.coordinator.JobDefinitionRegistry;
import ca.uhn.fhir.batch2.maintenance.JobMaintenanceServiceImpl;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobWorkNotification;
import ca.uhn.fhir.batch2.model.JobWorkNotificationJsonMessage;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.batch2.model.WorkChunkStatusEnum;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.jpa.dao.data.IBatch2JobInstanceRepository;
import ca.uhn.fhir.jpa.dao.data.IBatch2WorkChunkRepository;
import ca.uhn.fhir.jpa.entity.Batch2JobInstanceEntity;
import ca.uhn.fhir.jpa.entity.Batch2WorkChunkEntity;
import ca.uhn.fhir.jpa.subscription.channel.api.ChannelConsumerSettings;
import ca.uhn.fhir.jpa.subscription.channel.api.ChannelProducerSettings;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelFactory;
import ca.uhn.fhir.jpa.subscription.channel.impl.LinkedBlockingChannel;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.util.JsonUtil;
import ca.uhn.test.concurrency.IPointcutLatch;
import ca.uhn.test.concurrency.PointcutLatch;
import jakarta.annotation.Nonnull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static ca.uhn.fhir.batch2.config.BaseBatch2Config.CHANNEL_NAME;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.assertj.core.api.Assertions.assertThat;

public class Batch2JobMaintenanceDatabaseIT extends BaseJpaR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(Batch2JobMaintenanceDatabaseIT.class);

	public static final int TEST_JOB_VERSION = 1;
	public static final String FIRST = "FIRST";
	public static final String SECOND = "SECOND";
	public static final String LAST = "LAST";
	private static final String JOB_DEF_ID = "test-job-definition";
	private static final JobDefinition<? extends IModelJson> ourJobDef = buildJobDefinition();
	private static final String TEST_INSTANCE_ID = "test-instance-id";

	@Autowired
	JobDefinitionRegistry myJobDefinitionRegistry;
	@Autowired
	IJobMaintenanceService myJobMaintenanceService;
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
	private TransactionTemplate myTxTemplate;
	private final MyChannelInterceptor myChannelInterceptor = new MyChannelInterceptor();

	@BeforeEach
	public void before() {
		myWorkChunkRepository.deleteAll();
		myJobInstanceRepository.deleteAll();

		myJobDefinitionRegistry.addJobDefinition(ourJobDef);
		myWorkChannel = (LinkedBlockingChannel) myChannelFactory.getOrCreateProducer(CHANNEL_NAME, JobWorkNotificationJsonMessage.class, new ChannelProducerSettings());
		JobMaintenanceServiceImpl jobMaintenanceService = (JobMaintenanceServiceImpl) myJobMaintenanceService;
		jobMaintenanceService.setMaintenanceJobStartedCallback(() -> {
			ourLog.info("Batch maintenance job started");
			myStackTraceElements.add(Thread.currentThread().getStackTrace());
		});

		myTxTemplate = new TransactionTemplate(myTxManager);
		storeNewInstance(ourJobDef);

		myWorkChannel = (LinkedBlockingChannel) myChannelFactory.getOrCreateReceiver(CHANNEL_NAME, JobWorkNotificationJsonMessage.class, new ChannelConsumerSettings());
		myChannelInterceptor.clear();
		myWorkChannel.addInterceptor(myChannelInterceptor);
	}

	@AfterEach
	public void after() {
		ourLog.debug("Maintenance traces: {}", myStackTraceElements);
		myWorkChannel.clearInterceptorsForUnitTest();
		JobMaintenanceServiceImpl jobMaintenanceService = (JobMaintenanceServiceImpl) myJobMaintenanceService;
		jobMaintenanceService.setMaintenanceJobStartedCallback(() -> {
		});
	}

	@Test
	public void runMaintenancePass_noChunks_noChange() {
		assertInstanceCount(1);
		myJobMaintenanceService.runMaintenancePass();
		assertInstanceCount(1);

		assertInstanceStatus(StatusEnum.IN_PROGRESS);
	}


	@Test
	public void runMaintenancePass_SingleQueuedChunk_noChange() {
		WorkChunkExpectation expectation = new WorkChunkExpectation(
			"chunk1, FIRST, QUEUED",
			""
		);

		expectation.storeChunks();
		myJobMaintenanceService.runMaintenancePass();
		expectation.assertNotifications();

		assertInstanceStatus(StatusEnum.IN_PROGRESS);
	}

	@Test
	public void runMaintenancePass_SingleInProgressChunk_noChange() {
		WorkChunkExpectation expectation = new WorkChunkExpectation(
			"chunk1, FIRST, IN_PROGRESS",
			""
		);

		expectation.storeChunks();
		myJobMaintenanceService.runMaintenancePass();
		expectation.assertNotifications();

		assertInstanceStatus(StatusEnum.IN_PROGRESS);
	}

	@Test
	public void runMaintenancePass_SingleCompleteChunk_notifiesAndChangesGatedStep() throws InterruptedException {
		assertCurrentGatedStep(FIRST);

		WorkChunkExpectation expectation = new WorkChunkExpectation(
			"""
				chunk1, FIRST, COMPLETED
				chunk2, SECOND, QUEUED
				""",
			"""
				chunk2
				"""
		);

		expectation.storeChunks();
		myChannelInterceptor.setExpectedCount(1);
		myJobMaintenanceService.runMaintenancePass();
		myChannelInterceptor.awaitExpected();
		expectation.assertNotifications();

		assertInstanceStatus(StatusEnum.IN_PROGRESS);
		assertCurrentGatedStep(SECOND);
	}

	@Test
	public void runMaintenancePass_DoubleCompleteChunk_notifiesAndChangesGatedStep() throws InterruptedException {
		assertCurrentGatedStep(FIRST);

		WorkChunkExpectation expectation = new WorkChunkExpectation(
			"""
			chunk1, FIRST, COMPLETED
			chunk2, FIRST, COMPLETED
			chunk3, SECOND, QUEUED
			chunk4, SECOND, QUEUED
			""", """
			chunk3
			chunk4
			"""
		);

		expectation.storeChunks();
		myChannelInterceptor.setExpectedCount(2);
		myJobMaintenanceService.runMaintenancePass();
		myChannelInterceptor.awaitExpected();
		expectation.assertNotifications();

		assertInstanceStatus(StatusEnum.IN_PROGRESS);
		assertCurrentGatedStep(SECOND);
	}

	@Test
	public void runMaintenancePass_DoubleIncompleteChunk_noChange() {
		assertCurrentGatedStep(FIRST);

		WorkChunkExpectation expectation = new WorkChunkExpectation(
			"""
				chunk1, FIRST, COMPLETED
				chunk2, FIRST, IN_PROGRESS
				chunk3, SECOND, QUEUED
				chunk4, SECOND, QUEUED
			""",
			""
		);

		expectation.storeChunks();
		myJobMaintenanceService.runMaintenancePass();
		expectation.assertNotifications();

		assertInstanceStatus(StatusEnum.IN_PROGRESS);
		assertCurrentGatedStep(FIRST);
	}
	
	@Test
	public void runMaintenancePass_allStepsComplete_jobCompletes() {
		assertCurrentGatedStep(FIRST);

		WorkChunkExpectation expectation = new WorkChunkExpectation(
			"""
				chunk1, FIRST, COMPLETED
				chunk3, SECOND, COMPLETED
				chunk4, SECOND, COMPLETED
				chunk5, SECOND, COMPLETED
				chunk6, SECOND, COMPLETED
				chunk7, LAST, COMPLETED
			""",
			""
		);

		expectation.storeChunks();


		myJobMaintenanceService.runMaintenancePass();


		expectation.assertNotifications();

		assertInstanceStatus(StatusEnum.COMPLETED);

	}


	/**
	 * If the first step doesn't produce any work chunks, then
	 * the instance should be marked as complete right away.
	 */
	@Test
	public void testPerformStep_FirstStep_NoWorkChunksProduced() {
		assertCurrentGatedStep(FIRST);

		WorkChunkExpectation expectation = new WorkChunkExpectation(
			"""
				chunk1, FIRST, COMPLETED
			""",
			""
		);

		expectation.storeChunks();

		myJobMaintenanceService.runMaintenancePass();
		myJobMaintenanceService.runMaintenancePass();
		myJobMaintenanceService.runMaintenancePass();
		myJobMaintenanceService.runMaintenancePass();

		expectation.assertNotifications();

		assertInstanceStatus(StatusEnum.COMPLETED);
	}


	/**
	 * Once all chunks are complete, the job should complete even if a step has no work.
	 * the instance should be marked as complete right away.
	 */
	@Test
	public void testPerformStep_secondStep_NoWorkChunksProduced() {
		assertCurrentGatedStep(FIRST);

		WorkChunkExpectation expectation = new WorkChunkExpectation(
			"""
				chunk1, FIRST, COMPLETED
				chunk3, SECOND, COMPLETED
				chunk4, SECOND, COMPLETED
			""",
			""
		);

		expectation.storeChunks();

		myJobMaintenanceService.runMaintenancePass();
		myJobMaintenanceService.runMaintenancePass();
		myJobMaintenanceService.runMaintenancePass();

		expectation.assertNotifications();

		assertInstanceStatus(StatusEnum.COMPLETED);
	}

	// TODO MB Ken and Nathan created these.  Do we want to make them real?
	@Test
	@Disabled("future plans")
	public void runMaintenancePass_MultipleStepsInProgress_CancelsInstance() {
		assertCurrentGatedStep(FIRST);

		WorkChunkExpectation expectation = new WorkChunkExpectation(
			"""
			chunk1, FIRST, IN_PROGRESS
			chunk2, SECOND, IN_PROGRESS
			""",
			""
		);

		expectation.storeChunks();
		myJobMaintenanceService.runMaintenancePass();
		expectation.assertNotifications();

		assertInstanceStatus(StatusEnum.FAILED);
		assertError("IN_PROGRESS Chunks found in both the FIRST and SECOND step.");
	}

	@Test
	@Disabled("future plans")
	public void runMaintenancePass_MultipleOtherStepsInProgress_CancelsInstance() {
		assertCurrentGatedStep(FIRST);

		WorkChunkExpectation expectation = new WorkChunkExpectation(
			"""
			chunk1, SECOND, IN_PROGRESS
			chunk2, LAST, IN_PROGRESS
			""",
			""
		);

		expectation.storeChunks();
		myJobMaintenanceService.runMaintenancePass();
		expectation.assertNotifications();

		assertInstanceStatus(StatusEnum.FAILED);
		assertError("IN_PROGRESS Chunks found both the SECOND and LAST step.");
	}

	@Test
	@Disabled("future plans")
	public void runMaintenancePass_MultipleStepsQueued_CancelsInstance() {
		assertCurrentGatedStep(FIRST);

		WorkChunkExpectation expectation = new WorkChunkExpectation(
			"""
					chunk1, FIRST, COMPLETED
					chunk2, SECOND, QUEUED
					chunk3, LAST, QUEUED
				""",
			""
		);

		expectation.storeChunks();
		myJobMaintenanceService.runMaintenancePass();
		expectation.assertNotifications();

		assertInstanceStatus(StatusEnum.FAILED);
		assertError("QUEUED Chunks found in both the SECOND and LAST step.");
	}

	private void assertError(String theExpectedErrorMessage) {
		Optional<Batch2JobInstanceEntity> instance = myJobInstanceRepository.findById(TEST_INSTANCE_ID);
		assertThat(instance).isPresent();
		assertEquals(theExpectedErrorMessage, instance.get().getErrorMessage());
	}


	private void assertCurrentGatedStep(String theNextStepId) {
		Optional<JobInstance> instance = myJobPersistence.fetchInstance(TEST_INSTANCE_ID);
		assertThat(instance).isPresent();
		assertEquals(theNextStepId, instance.get().getCurrentGatedStepId());
	}

	@Nonnull
	private static Batch2WorkChunkEntity buildWorkChunkEntity(String theChunkId, String theStepId, WorkChunkStatusEnum theStatus) {
		Batch2WorkChunkEntity workChunk = new Batch2WorkChunkEntity();
		workChunk.setId(theChunkId);
		workChunk.setJobDefinitionId(JOB_DEF_ID);
		workChunk.setStatus(theStatus);
		workChunk.setJobDefinitionVersion(TEST_JOB_VERSION);
		workChunk.setCreateTime(new Date());
		workChunk.setInstanceId(TEST_INSTANCE_ID);
		workChunk.setTargetStepId(theStepId);
		if (!theStatus.isIncomplete()) {
			workChunk.setEndTime(new Date());
		}

		return workChunk;
	}


	@Nonnull
	private static JobDefinition<? extends IModelJson> buildJobDefinition() {
		IJobStepWorker<TestJobParameters, VoidModel, FirstStepOutput> firstStep = (step, sink) -> {
			ourLog.info("First step for chunk {}", step.getChunkId());
			return RunOutcome.SUCCESS;
		};
		IJobStepWorker<TestJobParameters, FirstStepOutput, SecondStepOutput> secondStep = (step, sink) -> {
			ourLog.info("Second step for chunk {}", step.getChunkId());
			return RunOutcome.SUCCESS;
		};
		IJobStepWorker<TestJobParameters, SecondStepOutput, VoidModel> lastStep = (step, sink) -> {
			ourLog.info("Last step for chunk {}", step.getChunkId());
			return RunOutcome.SUCCESS;
		};

		JobDefinition<? extends IModelJson> definition = buildGatedJobDefinition(firstStep, secondStep, lastStep);
		return definition;
	}

	private void storeNewInstance(JobDefinition<? extends IModelJson> theJobDefinition) {
		Batch2JobInstanceEntity entity = new Batch2JobInstanceEntity();
		entity.setId(TEST_INSTANCE_ID);
		entity.setStatus(StatusEnum.IN_PROGRESS);
		entity.setDefinitionId(theJobDefinition.getJobDefinitionId());
		entity.setDefinitionVersion(theJobDefinition.getJobDefinitionVersion());
		entity.setParams(JsonUtil.serializeOrInvalidRequest(new TestJobParameters()));
		entity.setCurrentGatedStepId(FIRST);
		entity.setCreateTime(new Date());

		myTxTemplate.executeWithoutResult(t -> myJobInstanceRepository.save(entity));
	}

	private void assertInstanceCount(int size) {
		assertThat(myJobPersistence.fetchInstancesByJobDefinitionId(JOB_DEF_ID, 100, 0)).hasSize(size);
	}


	private void assertInstanceStatus(StatusEnum theInProgress) {
		Optional<Batch2JobInstanceEntity> instance = myJobInstanceRepository.findById(TEST_INSTANCE_ID);
		assertThat(instance).isPresent();
		assertEquals(theInProgress, instance.get().getStatus());
	}
	@Nonnull
	private static JobDefinition<? extends IModelJson> buildGatedJobDefinition(IJobStepWorker<TestJobParameters, VoidModel, FirstStepOutput> theFirstStep, IJobStepWorker<TestJobParameters, FirstStepOutput, SecondStepOutput> theSecondStep, IJobStepWorker<TestJobParameters, SecondStepOutput, VoidModel> theLastStep) {
		return JobDefinition.newBuilder()
			.setJobDefinitionId(JOB_DEF_ID)
			.setJobDescription("test job")
			.setJobDefinitionVersion(TEST_JOB_VERSION)
			.setParametersType(TestJobParameters.class)
			.gatedExecution()
			.addFirstStep(
				FIRST,
				"Test first step",
				FirstStepOutput.class,
				theFirstStep
			)
			.addIntermediateStep(
				SECOND,
				"Test second step",
				SecondStepOutput.class,
				theSecondStep
			)
			.addLastStep(
				LAST,
				"Test last step",
				theLastStep
			)
			.completionHandler(details -> {
			})
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
		SecondStepOutput() {
		}
	}

	private class WorkChunkExpectation {
		private final List<Batch2WorkChunkEntity> myInputChunks = new ArrayList<>();
		private final List<String> myExpectedChunkIdNotifications = new ArrayList<>();
		public WorkChunkExpectation(String theInput, String theOutputChunkIds) {
			String[] inputLines = theInput.split("\n");
			for (String next : inputLines) {
				String[] parts = next.split(",");
				Batch2WorkChunkEntity e = buildWorkChunkEntity(parts[0].trim(), parts[1].trim(), WorkChunkStatusEnum.valueOf(parts[2].trim()));
				myInputChunks.add(e);
			}
			if (!isBlank(theOutputChunkIds)) {
				String[] outputLines = theOutputChunkIds.split("\n");
				for (String next : outputLines) {
					myExpectedChunkIdNotifications.add(next.trim());
				}
			}
		}

		public void storeChunks() {
			myTxTemplate.executeWithoutResult(t -> myWorkChunkRepository.saveAll(myInputChunks));
		}

		public void assertNotifications() {
			assertThat(myChannelInterceptor.getReceivedChunkIds()).containsExactlyInAnyOrderElementsOf(myExpectedChunkIdNotifications);
		}
	}

	private static class MyChannelInterceptor implements ChannelInterceptor, IPointcutLatch {
		PointcutLatch myPointcutLatch = new PointcutLatch("BATCH CHUNK MESSAGE RECEIVED");
		List<String> myReceivedChunkIds = new ArrayList<>();
		@Override
		public Message<?> preSend(@Nonnull Message<?> message, @Nonnull MessageChannel channel) {
			ourLog.info("Sending message: {}", message);
			JobWorkNotification notification = ((JobWorkNotificationJsonMessage) message).getPayload();
			myReceivedChunkIds.add(notification.getChunkId());
			myPointcutLatch.call(message);
			return message;
		}

		@Override
		public void clear() {
			myPointcutLatch.clear();
		}

		@Override
		public void setExpectedCount(int count) {
			myPointcutLatch.setExpectedCount(count);
		}

		@Override
		public List<HookParams> awaitExpected() throws InterruptedException {
			return myPointcutLatch.awaitExpected();
		}

		List<String> getReceivedChunkIds() {
			return myReceivedChunkIds;
		}
	}
}
