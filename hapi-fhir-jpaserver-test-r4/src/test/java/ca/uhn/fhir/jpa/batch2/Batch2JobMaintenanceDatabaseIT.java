package ca.uhn.fhir.jpa.batch2;

import ca.uhn.fhir.batch2.api.IJobMaintenanceService;
import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.RunOutcome;
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
import ca.uhn.fhir.jpa.entity.Batch2JobInstanceEntity;
import ca.uhn.fhir.jpa.entity.Batch2WorkChunkEntity;
import ca.uhn.fhir.jpa.subscription.channel.api.ChannelConsumerSettings;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelFactory;
import ca.uhn.fhir.jpa.subscription.channel.impl.LinkedBlockingChannel;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.jpa.test.Batch2JobHelper;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.util.JsonUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static ca.uhn.fhir.batch2.config.BaseBatch2Config.CHANNEL_NAME;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
	private TransactionTemplate myTxTemplate;

	@BeforeEach
	public void before() {
		myWorkChunkRepository.deleteAll();
		myJobInstanceRepository.deleteAll();

		myJobDefinitionRegistry.addJobDefinition(ourJobDef);
		myWorkChannel = (LinkedBlockingChannel) myChannelFactory.getOrCreateReceiver(CHANNEL_NAME, JobWorkNotificationJsonMessage.class, new ChannelConsumerSettings());
		JobMaintenanceServiceImpl jobMaintenanceService = (JobMaintenanceServiceImpl) myJobMaintenanceService;
		jobMaintenanceService.setMaintenanceJobStartedCallback(() -> {
			ourLog.info("Batch maintenance job started");
			myStackTraceElements.add(Thread.currentThread().getStackTrace());
		});

		myTxTemplate = new TransactionTemplate(myTxManager);
		storeNewInstance(ourJobDef);
	}

	@AfterEach
	public void after() {
		myWorkChannel.clearInterceptorsForUnitTest();
		JobMaintenanceServiceImpl jobMaintenanceService = (JobMaintenanceServiceImpl) myJobMaintenanceService;
		jobMaintenanceService.setMaintenanceJobStartedCallback(() -> {
		});
	}

	@Test
	public void testCreateInstance() {
		assertInstanceCount(1);
		myJobMaintenanceService.runMaintenancePass();
		assertInstanceCount(1);

		assertInstanceStatus(StatusEnum.IN_PROGRESS);
	}

	private void assertInstanceStatus(StatusEnum theInProgress) {
		Optional<Batch2JobInstanceEntity> instance = myJobInstanceRepository.findById(TEST_INSTANCE_ID);
		assertTrue(instance.isPresent());
		assertEquals(theInProgress, instance.get().getStatus());
	}

	@Test
	public void testSingleQueuedChunk() {
		WorkChunkExpectation expectation = new WorkChunkExpectation(
			"FIRST, QUEUED",
			"FIRST, QUEUED"
		);

		expectation.storeChunks();
		myJobMaintenanceService.runMaintenancePass();
		expectation.assertChunks();

		assertInstanceStatus(StatusEnum.IN_PROGRESS);
	}

	@Test
	public void testSingleInProgressChunk() {
		WorkChunkExpectation expectation = new WorkChunkExpectation(
			"FIRST, IN_PROGRESS",
			"FIRST, IN_PROGRESS"
		);

		expectation.storeChunks();
		myJobMaintenanceService.runMaintenancePass();
		expectation.assertChunks();

		assertInstanceStatus(StatusEnum.IN_PROGRESS);
	}

	@Test
	public void testSingleCompleteChunk() {
		assertCurrentGatedStep(FIRST);

		WorkChunkExpectation expectation = new WorkChunkExpectation(
			"FIRST, COMPLETED",
			"SECOND, QUEUED"
		);

		expectation.storeChunks();
		myJobMaintenanceService.runMaintenancePass();
		expectation.assertChunks();

		assertInstanceStatus(StatusEnum.IN_PROGRESS);
		assertCurrentGatedStep(SECOND);
	}

	@Test
	public void testDoubleCompleteChunk() {
		assertCurrentGatedStep(FIRST);

		WorkChunkExpectation expectation = new WorkChunkExpectation(
			"FIRST, COMPLETED\n" +
				"FIRST, COMPLETED",
			"SECOND, QUEUED\n" +
				"SECOND, QUEUED"
		);

		expectation.storeChunks();
		myJobMaintenanceService.runMaintenancePass();
		expectation.assertChunks();

		assertInstanceStatus(StatusEnum.IN_PROGRESS);
		assertCurrentGatedStep(SECOND);
	}

	private void assertCurrentGatedStep(String theNextStepId) {
		Optional<JobInstance> instance = myJobPersistence.fetchInstance(TEST_INSTANCE_ID);
		assertTrue(instance.isPresent());
		assertEquals(theNextStepId, instance.get().getCurrentGatedStepId());
	}

	@Nonnull
	private static Batch2WorkChunkEntity buildWorkChunkEntity(String theStepId, WorkChunkStatusEnum theStatus) {
		Batch2WorkChunkEntity workChunk = new Batch2WorkChunkEntity();
		workChunk.setId("chunk" + ourCounter.getAndIncrement());
		workChunk.setJobDefinitionId(JOB_DEF_ID);
		workChunk.setStatus(theStatus);
		workChunk.setJobDefinitionVersion(TEST_JOB_VERSION);
		workChunk.setCreateTime(new Date());
		workChunk.setInstanceId(TEST_INSTANCE_ID);
		workChunk.setTargetStepId(theStepId);
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

	private void storeNewInstance(JobDefinition<? extends IModelJson> definition) {
		Batch2JobInstanceEntity entity = new Batch2JobInstanceEntity();
		entity.setId(TEST_INSTANCE_ID);
		entity.setStatus(StatusEnum.IN_PROGRESS);
		entity.setDefinitionId(JOB_DEF_ID);
		entity.setDefinitionVersion(TEST_JOB_VERSION);
		entity.setParams(JsonUtil.serializeOrInvalidRequest(new TestJobParameters()));
		entity.setCurrentGatedStepId(FIRST);
		entity.setCreateTime(new Date());

		myTxTemplate.executeWithoutResult(t -> {
			myJobInstanceRepository.save(entity);
		});
	}

	private void assertInstanceCount(int size) {
		assertThat(myJobPersistence.fetchInstancesByJobDefinitionId(JOB_DEF_ID, 100, 0), hasSize(size));
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
		private final List<Batch2WorkChunkEntity> myInputChunks = new ArrayList();
		private final List<String> myOutputLines = new ArrayList();
		public WorkChunkExpectation(String theInput, String theOutput) {
			String[] inputLines = theInput.split("\n");
			for (String next : inputLines) {
				String[] parts = next.split(",");
				myInputChunks.add(buildWorkChunkEntity(parts[0].trim(), WorkChunkStatusEnum.valueOf(parts[1].trim())));
			}
			String[] outputLines = theInput.split("\n");
			Collections.addAll(myOutputLines, outputLines);
		}

		public void storeChunks() {
			myTxTemplate.executeWithoutResult(t -> {
				myWorkChunkRepository.saveAll(myInputChunks);
			});
		}

		public void assertChunks() {
			List<String> actualOutput = new ArrayList<>();
			myTxTemplate.executeWithoutResult(t -> {
				List<Batch2WorkChunkEntity> all = myWorkChunkRepository.findAll();
				for (Batch2WorkChunkEntity next : all) {
					actualOutput.add(next.getTargetStepId() + ", " + next.getStatus());
				}
			});
			assertThat(actualOutput, containsInAnyOrder(myOutputLines.toArray()));
		}
	}
}
