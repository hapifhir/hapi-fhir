package ca.uhn.fhir.batch2.maintenance;

import ca.uhn.fhir.batch2.api.IJobCompletionHandler;
import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.api.IReductionStepExecutorService;
import ca.uhn.fhir.batch2.api.JobCompletionDetails;
import ca.uhn.fhir.batch2.channel.BatchJobSender;
import ca.uhn.fhir.batch2.coordinator.BaseBatch2Test;
import ca.uhn.fhir.batch2.coordinator.JobCoordinatorImplTest;
import ca.uhn.fhir.batch2.coordinator.JobDefinitionRegistry;
import ca.uhn.fhir.batch2.coordinator.TestJobParameters;
import ca.uhn.fhir.batch2.coordinator.WorkChunkProcessor;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobWorkNotification;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.batch2.model.WorkChunkMetadata;
import ca.uhn.fhir.batch2.model.WorkChunkStatusEnum;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelProducer;
import ca.uhn.test.util.LogbackTestExtension;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.google.common.collect.Lists;
import org.hl7.fhir.r4.model.DateTimeType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.Message;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static ca.uhn.fhir.batch2.coordinator.JobCoordinatorImplTest.createWorkChunkStep1;
import static ca.uhn.fhir.batch2.coordinator.JobCoordinatorImplTest.createWorkChunkStep2;
import static ca.uhn.fhir.batch2.coordinator.JobCoordinatorImplTest.createWorkChunkStep3;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JobMaintenanceServiceImplTest extends BaseBatch2Test {

	@RegisterExtension
	LogbackTestExtension myLogCapture = new LogbackTestExtension((Logger) LoggerFactory.getLogger("ca.uhn.fhir.log.batch_troubleshooting"), Level.WARN);
	@Mock
	IJobCompletionHandler<TestJobParameters> myCompletionHandler;
	@Mock
	private ISchedulerService mySchedulerService;
	@Mock
	private IJobPersistence myJobPersistence;
	@Mock
	private WorkChunkProcessor myJobExecutorSvc;
	@Spy
	private JpaStorageSettings myStorageSettings = new JpaStorageSettings();
	private JobMaintenanceServiceImpl mySvc;
	private JobDefinitionRegistry myJobDefinitionRegistry;
	@Mock
	private IChannelProducer myWorkChannelProducer;
	@Mock
	private PlatformTransactionManager myTransactionService;
	@Captor
	private ArgumentCaptor<Message<JobWorkNotification>> myMessageCaptor;
	@Captor
	private ArgumentCaptor<JobCompletionDetails<TestJobParameters>> myJobCompletionCaptor;
	@Mock
	private IReductionStepExecutorService myReductionStepExecutorService;

	@BeforeEach
	public void beforeEach() {
		myJobDefinitionRegistry = new JobDefinitionRegistry();
		BatchJobSender batchJobSender = new BatchJobSender(myWorkChannelProducer);
		mySvc = new JobMaintenanceServiceImpl(mySchedulerService,
			myJobPersistence,
			myStorageSettings,
			myJobDefinitionRegistry,
			batchJobSender,
			myJobExecutorSvc,
			myReductionStepExecutorService
		);
		myStorageSettings.setJobFastTrackingEnabled(true);
	}

	@Test
	public void testInProgress_CalculateProgress_FirstCompleteButNoOtherStepsYetComplete() {
		List<WorkChunk> chunks = List.of(
			JobCoordinatorImplTest.createWorkChunk(STEP_1, null).setStatus(WorkChunkStatusEnum.COMPLETED),
			JobCoordinatorImplTest.createWorkChunk(STEP_2, null).setStatus(WorkChunkStatusEnum.QUEUED)
		);
		when(myJobPersistence.fetchAllWorkChunksIterator(eq(INSTANCE_ID), eq(false)))
			.thenReturn(chunks.iterator());
		Page<WorkChunkMetadata> page = getPageOfData(chunks);

		myJobDefinitionRegistry.addJobDefinition(createJobDefinition());
		JobInstance instance = createInstance();
		when(myJobPersistence.fetchInstances(anyInt(), eq(0))).thenReturn(List.of(instance));
		when(myJobPersistence.fetchInstance(INSTANCE_ID)).thenReturn(Optional.of(instance));
		when(myJobPersistence.fetchAllWorkChunkMetadataForJobInStates(any(Pageable.class), eq(INSTANCE_ID), any()))
			.thenReturn(page);

		mySvc.runMaintenancePass();

		verify(myJobPersistence, times(1)).updateInstance(any(), any());
	}

	@Test
	public void testInProgress_Calculate_progresss_JobDefinitionMissing() {
		ArgumentCaptor<ILoggingEvent> logCaptor = ArgumentCaptor.forClass(ILoggingEvent.class);
		List<WorkChunk> chunks = List.of(
			JobCoordinatorImplTest.createWorkChunk(STEP_1, null).setStatus(WorkChunkStatusEnum.COMPLETED),
			JobCoordinatorImplTest.createWorkChunk(STEP_2, null).setStatus(WorkChunkStatusEnum.QUEUED)
		);

		JobInstance instance = createInstance();
		when(myJobPersistence.fetchInstances(anyInt(), eq(0))).thenReturn(List.of(instance));

		mySvc.runMaintenancePass();

		String assumedRoleLogText = String.format("Job definition %s for instance %s is currently unavailable", JOB_DEFINITION_ID,  instance.getInstanceId());

		List<ILoggingEvent> fetchedCredentialLogs = myLogCapture.getLogEvents().stream().filter(event -> event.getFormattedMessage().equals(assumedRoleLogText)).collect(Collectors.toList());
		assertThat(fetchedCredentialLogs).hasSize(1);

		verify(myJobPersistence, never()).updateInstance(any(), any());
	}

	@Test
	public void testInProgress_CalculateProgress_FirstStepComplete() {
		List<WorkChunk> chunks = Arrays.asList(
			createWorkChunkStep1().setStatus(WorkChunkStatusEnum.COMPLETED).setStartTime(parseTime("2022-02-12T14:00:00-04:00")),
			JobCoordinatorImplTest.createWorkChunkStep2().setStatus(WorkChunkStatusEnum.IN_PROGRESS).setStartTime(parseTime("2022-02-12T14:00:01-04:00")),
			JobCoordinatorImplTest.createWorkChunkStep2().setStatus(WorkChunkStatusEnum.IN_PROGRESS).setStartTime(parseTime("2022-02-12T14:00:02-04:00")),
			JobCoordinatorImplTest.createWorkChunkStep2().setStatus(WorkChunkStatusEnum.IN_PROGRESS).setStartTime(parseTime("2022-02-12T14:00:03-04:00")),
			JobCoordinatorImplTest.createWorkChunkStep2().setStatus(WorkChunkStatusEnum.COMPLETED).setStartTime(parseTime("2022-02-12T14:00:00-04:00")).setEndTime(parseTime("2022-02-12T14:10:00-04:00")).setRecordsProcessed(25),
			createWorkChunkStep3().setStatus(WorkChunkStatusEnum.COMPLETED).setStartTime(parseTime("2022-02-12T14:01:00-04:00")).setEndTime(parseTime("2022-02-12T14:10:00-04:00")).setRecordsProcessed(25)
		);
		myJobDefinitionRegistry.addJobDefinition(createJobDefinition());
		JobInstance instance = createInstance();
		when(myJobPersistence.fetchInstance(eq(INSTANCE_ID))).thenReturn(Optional.of(instance));
		when(myJobPersistence.fetchInstances(anyInt(), eq(0))).thenReturn(Lists.newArrayList(instance));
		when(myJobPersistence.fetchAllWorkChunksIterator(eq(INSTANCE_ID), eq(false)))
			.thenReturn(chunks.iterator());
		when(myJobPersistence.fetchAllWorkChunkMetadataForJobInStates(any(Pageable.class), eq(instance.getInstanceId()), eq(Set.of(WorkChunkStatusEnum.READY))))
			.thenReturn(Page.empty());
		stubUpdateInstanceCallback(instance);

		mySvc.runMaintenancePass();

		verify(myJobPersistence, times(1)).updateInstance(eq(INSTANCE_ID), any());

		assertEquals(0.5, instance.getProgress());
		assertEquals(50, instance.getCombinedRecordsProcessed());
		assertEquals(0.08333333333333333, instance.getCombinedRecordsProcessedPerSecond());
		assertNotNull(instance.getStartTime());
		assertEquals(parseTime("2022-02-12T14:00:00-04:00"), instance.getStartTime());
		assertNull(instance.getEndTime());
		assertEquals("00:10:00", instance.getEstimatedTimeRemaining());

		verify(myJobPersistence).updatePollWaitingChunksForJobIfReady(eq(instance.getInstanceId()));
		verifyNoMoreInteractions(myJobPersistence);
	}

	private void stubUpdateInstanceCallback(JobInstance theJobInstance) {
		when(myJobPersistence.updateInstance(eq(INSTANCE_ID), any())).thenAnswer(call->{
			IJobPersistence.JobInstanceUpdateCallback callback = call.getArgument(1);
			return callback.doUpdate(theJobInstance);
		});
	}

	@Test
	public void testInProgress_CalculateProgress_InstanceHasErrorButNoChunksAreErrored() {
		// Setup
		List<WorkChunk> chunks = Arrays.asList(
			createWorkChunkStep1().setStatus(WorkChunkStatusEnum.COMPLETED).setStartTime(parseTime("2022-02-12T14:00:00-04:00")),
			JobCoordinatorImplTest.createWorkChunkStep2().setStatus(WorkChunkStatusEnum.IN_PROGRESS).setStartTime(parseTime("2022-02-12T14:00:01-04:00")),
			JobCoordinatorImplTest.createWorkChunkStep2().setStatus(WorkChunkStatusEnum.IN_PROGRESS).setStartTime(parseTime("2022-02-12T14:00:02-04:00")).setErrorCount(2),
			JobCoordinatorImplTest.createWorkChunkStep2().setStatus(WorkChunkStatusEnum.IN_PROGRESS).setStartTime(parseTime("2022-02-12T14:00:03-04:00")).setErrorCount(2),
			JobCoordinatorImplTest.createWorkChunkStep2().setStatus(WorkChunkStatusEnum.COMPLETED).setStartTime(parseTime("2022-02-12T14:00:00-04:00")).setEndTime(parseTime("2022-02-12T14:10:00-04:00")).setRecordsProcessed(25),
			createWorkChunkStep3().setStatus(WorkChunkStatusEnum.COMPLETED).setStartTime(parseTime("2022-02-12T14:01:00-04:00")).setEndTime(parseTime("2022-02-12T14:10:00-04:00")).setRecordsProcessed(25)
		);
		myJobDefinitionRegistry.addJobDefinition(createJobDefinition());
		JobInstance instance = createInstance();
		instance.setErrorMessage("This is an error message");
		when(myJobPersistence.fetchInstance(eq(INSTANCE_ID))).thenReturn(Optional.of(createInstance()));
		when(myJobPersistence.fetchInstances(anyInt(), eq(0))).thenReturn(Lists.newArrayList(instance));
		when(myJobPersistence.fetchAllWorkChunksIterator(eq(INSTANCE_ID), eq(false)))
			.thenReturn(chunks.iterator());
		when(myJobPersistence.fetchAllWorkChunkMetadataForJobInStates(any(Pageable.class), eq(instance.getInstanceId()), eq(Set.of(WorkChunkStatusEnum.READY))))
			.thenReturn(Page.empty());
		stubUpdateInstanceCallback(instance);

		// Execute
		mySvc.runMaintenancePass();

		// Verify
		verify(myJobPersistence, times(1)).updateInstance(eq(INSTANCE_ID), any());

		assertNull(instance.getErrorMessage());
		assertEquals(4, instance.getErrorCount());
		assertEquals(0.5, instance.getProgress());
		assertEquals(50, instance.getCombinedRecordsProcessed());
		assertEquals(0.08333333333333333, instance.getCombinedRecordsProcessedPerSecond());

		verify(myJobPersistence).updatePollWaitingChunksForJobIfReady(eq(instance.getInstanceId()));
		verifyNoMoreInteractions(myJobPersistence);
	}

	@Test
	public void testInProgress_GatedExecution_FirstStepComplete() {
		// Setup
		List<WorkChunk> completedChunks = List.of(createWorkChunkStep1().setStatus(WorkChunkStatusEnum.COMPLETED).setId(CHUNK_ID));

		List<WorkChunk> chunks = Arrays.asList(
			JobCoordinatorImplTest.createWorkChunkStep2().setStatus(WorkChunkStatusEnum.GATE_WAITING).setId(CHUNK_ID),
			JobCoordinatorImplTest.createWorkChunkStep2().setStatus(WorkChunkStatusEnum.QUEUED).setId(CHUNK_ID_2)
		);
		myJobDefinitionRegistry.addJobDefinition(createJobDefinition(JobDefinition.Builder::gatedExecution));

		when(myJobPersistence.fetchAllWorkChunksIterator(eq(INSTANCE_ID), eq(false)))
			.thenReturn(chunks.iterator());
		when(myJobPersistence.getDistinctWorkChunkStatesForJobAndStep(anyString(), anyString()))
			.thenReturn(completedChunks.stream().map(WorkChunk::getStatus).collect(Collectors.toSet()));

		JobInstance instance1 = createInstance();
		instance1.setCurrentGatedStepId(STEP_1);
		when(myJobPersistence.fetchInstances(anyInt(), eq(0))).thenReturn(Lists.newArrayList(instance1));
		when(myJobPersistence.fetchInstance(INSTANCE_ID)).thenReturn(Optional.of(instance1));
		when(myJobPersistence.fetchAllWorkChunkMetadataForJobInStates(any(Pageable.class), anyString(), eq(Set.of(WorkChunkStatusEnum.READY))))
			.thenAnswer((args) -> {
				// new page every time (called more than once)
				return getPageOfData(new ArrayList<>(chunks));
			});
		doAnswer(a -> {
			Consumer<Integer> callback = a.getArgument(1);
			callback.accept(1);
			return null;
		}).when(myJobPersistence).enqueueWorkChunkForProcessing(anyString(), any());
		stubUpdateInstanceCallback(instance1);

		// Execute
		mySvc.runMaintenancePass();

		// Verify
		verify(myWorkChannelProducer, times(2)).send(myMessageCaptor.capture());
		verify(myJobPersistence, times(1)).updateInstance(eq(INSTANCE_ID), any());
		verify(myJobPersistence, times(1)).advanceJobStepAndUpdateChunkStatus(eq(INSTANCE_ID), eq(STEP_2), eq(false));
		verify(myJobPersistence).updatePollWaitingChunksForJobIfReady(eq(INSTANCE_ID));
		verifyNoMoreInteractions(myJobPersistence);
		JobWorkNotification payload0 = myMessageCaptor.getAllValues().get(0).getPayload();
		assertEquals(STEP_2, payload0.getTargetStepId());
		assertEquals(CHUNK_ID, payload0.getChunkId());
		JobWorkNotification payload1 = myMessageCaptor.getAllValues().get(1).getPayload();
		assertEquals(STEP_2, payload1.getTargetStepId());
		assertEquals(CHUNK_ID_2, payload1.getChunkId());
	}

	@Test
	public void testFailed_PurgeOldInstance() {
		myJobDefinitionRegistry.addJobDefinition(createJobDefinition());
		JobInstance instance = createInstance();
		instance.setStatus(StatusEnum.FAILED);
		instance.setEndTime(parseTime("2001-01-01T12:12:12Z"));
		when(myJobPersistence.fetchInstances(anyInt(), eq(0))).thenReturn(Lists.newArrayList(instance));
		when(myJobPersistence.fetchInstance(INSTANCE_ID)).thenReturn(Optional.of(instance));
		when(myJobPersistence.fetchAllWorkChunkMetadataForJobInStates(any(Pageable.class), eq(instance.getInstanceId()), eq(Set.of(WorkChunkStatusEnum.READY))))
			.thenReturn(Page.empty());

		mySvc.runMaintenancePass();

		verify(myJobPersistence, times(1)).deleteInstanceAndChunks(eq(INSTANCE_ID));
		verify(myJobPersistence).updatePollWaitingChunksForJobIfReady(eq(instance.getInstanceId()));
		verifyNoMoreInteractions(myJobPersistence);
	}

	@Test
	public void testInProgress_CalculateProgress_AllStepsComplete() {
		// Setup
		List<WorkChunk> chunks = List.of(
			createWorkChunkStep1().setStatus(WorkChunkStatusEnum.COMPLETED).setStartTime(parseTime("2022-02-12T14:00:00-04:00")).setEndTime(parseTime("2022-02-12T14:01:00-04:00")).setRecordsProcessed(25),
			JobCoordinatorImplTest.createWorkChunkStep2().setStatus(WorkChunkStatusEnum.COMPLETED).setStartTime(parseTime("2022-02-12T14:00:01-04:00")).setEndTime(parseTime("2022-02-12T14:06:00-04:00")).setRecordsProcessed(25),
			JobCoordinatorImplTest.createWorkChunkStep2().setStatus(WorkChunkStatusEnum.COMPLETED).setStartTime(parseTime("2022-02-12T14:00:02-04:00")).setEndTime(parseTime("2022-02-12T14:06:00-04:00")).setRecordsProcessed(25),
			JobCoordinatorImplTest.createWorkChunkStep2().setStatus(WorkChunkStatusEnum.COMPLETED).setStartTime(parseTime("2022-02-12T14:00:03-04:00")).setEndTime(parseTime("2022-02-12T14:06:00-04:00")).setRecordsProcessed(25),
			JobCoordinatorImplTest.createWorkChunkStep2().setStatus(WorkChunkStatusEnum.COMPLETED).setStartTime(parseTime("2022-02-12T14:00:00-04:00")).setEndTime(parseTime("2022-02-12T14:10:00-04:00")).setRecordsProcessed(25), createWorkChunkStep3().setStatus(WorkChunkStatusEnum.COMPLETED).setStartTime(parseTime("2022-02-12T14:01:00-04:00")).setEndTime(parseTime("2022-02-12T14:10:00-04:00")).setRecordsProcessed(25)
		);

		myJobDefinitionRegistry.addJobDefinition(createJobDefinition(t -> t.completionHandler(myCompletionHandler)));
		JobInstance instance = createInstance();
		when(myJobPersistence.fetchInstances(anyInt(), eq(0))).thenReturn(Lists.newArrayList(instance));
		when(myJobPersistence.fetchAllWorkChunksIterator(eq(INSTANCE_ID), anyBoolean())).thenAnswer(t->chunks.iterator());
		when(myJobPersistence.fetchInstance(INSTANCE_ID)).thenReturn(Optional.of(instance));
		when(myJobPersistence.fetchAllWorkChunkMetadataForJobInStates(any(Pageable.class), eq(INSTANCE_ID), eq(Set.of(WorkChunkStatusEnum.READY))))
			.thenReturn(Page.empty());
		stubUpdateInstanceCallback(instance);

		// Execute

		mySvc.runMaintenancePass();

		// Verify

		verify(myJobPersistence, times(1)).updateInstance(eq(INSTANCE_ID), any());

		assertEquals(1.0, instance.getProgress());
		assertEquals(StatusEnum.COMPLETED, instance.getStatus());
		assertEquals(150, instance.getCombinedRecordsProcessed());
		assertEquals(0.25, instance.getCombinedRecordsProcessedPerSecond());
		assertEquals(parseTime("2022-02-12T14:10:00-04:00"), instance.getEndTime());

		verify(myJobPersistence, times(1)).deleteChunksAndMarkInstanceAsChunksPurged(eq(INSTANCE_ID));
		verify(myCompletionHandler, times(1)).jobComplete(myJobCompletionCaptor.capture());
		verify(myJobPersistence).updatePollWaitingChunksForJobIfReady(eq(instance.getInstanceId()));
		verifyNoMoreInteractions(myJobPersistence);

		assertEquals(INSTANCE_ID, myJobCompletionCaptor.getValue().getInstance().getInstanceId());
		assertEquals(PARAM_1_VALUE, myJobCompletionCaptor.getValue().getParameters().getParam1());
	}

	@Test
	public void testInProgress_CalculateProgress_OneStepFailed() {
		List<WorkChunk> chunks = List.of(
			createWorkChunkStep1().setStatus(WorkChunkStatusEnum.COMPLETED).setStartTime(parseTime("2022-02-12T14:00:00-04:00")).setEndTime(parseTime("2022-02-12T14:01:00-04:00")).setRecordsProcessed(25),
			JobCoordinatorImplTest.createWorkChunkStep2().setStatus(WorkChunkStatusEnum.COMPLETED).setStartTime(parseTime("2022-02-12T14:00:01-04:00")).setEndTime(parseTime("2022-02-12T14:06:00-04:00")).setRecordsProcessed(25),
			JobCoordinatorImplTest.createWorkChunkStep2().setStatus(WorkChunkStatusEnum.FAILED).setStartTime(parseTime("2022-02-12T14:00:02-04:00")).setEndTime(parseTime("2022-02-12T14:06:00-04:00")).setRecordsProcessed(25).setErrorMessage("This is an error message"),
			JobCoordinatorImplTest.createWorkChunkStep2().setStatus(WorkChunkStatusEnum.COMPLETED).setStartTime(parseTime("2022-02-12T14:00:03-04:00")).setEndTime(parseTime("2022-02-12T14:06:00-04:00")).setRecordsProcessed(25),
			JobCoordinatorImplTest.createWorkChunkStep2().setStatus(WorkChunkStatusEnum.COMPLETED).setStartTime(parseTime("2022-02-12T14:00:00-04:00")).setEndTime(parseTime("2022-02-12T14:10:00-04:00")).setRecordsProcessed(25),
			createWorkChunkStep3().setStatus(WorkChunkStatusEnum.COMPLETED).setStartTime(parseTime("2022-02-12T14:01:00-04:00")).setEndTime(parseTime("2022-02-12T14:10:00-04:00")).setRecordsProcessed(25)
		);

		myJobDefinitionRegistry.addJobDefinition(createJobDefinition());
		JobInstance instance = createInstance();
		when(myJobPersistence.fetchInstance(eq(INSTANCE_ID))).thenReturn(Optional.of(instance));
		when(myJobPersistence.fetchInstances(anyInt(), eq(0))).thenReturn(Lists.newArrayList(instance));
		when(myJobPersistence.fetchAllWorkChunksIterator(eq(INSTANCE_ID), anyBoolean()))
			.thenAnswer(t->chunks.iterator());
		when(myJobPersistence.fetchAllWorkChunkMetadataForJobInStates(any(Pageable.class), eq(instance.getInstanceId()), eq(Set.of(WorkChunkStatusEnum.READY))))
			.thenReturn(Page.empty());
		stubUpdateInstanceCallback(instance);

		mySvc.runMaintenancePass();

		assertEquals(0.8333333333333334, instance.getProgress());
		assertEquals(StatusEnum.FAILED, instance.getStatus());
		assertEquals("This is an error message", instance.getErrorMessage());
		assertEquals(150, instance.getCombinedRecordsProcessed());
		assertEquals(0.25, instance.getCombinedRecordsProcessedPerSecond());
		assertEquals(parseTime("2022-02-12T14:10:00-04:00"), instance.getEndTime());

		// twice - once to move to FAILED, and once to purge the chunks
		verify(myJobPersistence, times(1)).updateInstance(eq(INSTANCE_ID), any());
		verify(myJobPersistence, times(1)).deleteChunksAndMarkInstanceAsChunksPurged(eq(INSTANCE_ID));
		verify(myJobPersistence).updatePollWaitingChunksForJobIfReady(eq(instance.getInstanceId()));
		verifyNoMoreInteractions(myJobPersistence);
	}

	private void runEnqueueReadyChunksTest(List<WorkChunk> theChunks, JobDefinition<TestJobParameters> theJobDefinition) {
		myJobDefinitionRegistry.addJobDefinition(theJobDefinition);
		JobInstance instance = createInstance();
		// we'll set the instance to the first step id
		theChunks.stream().findFirst().ifPresent(c -> {
			instance.setCurrentGatedStepId(c.getTargetStepId());
		});
		instance.setJobDefinitionId(theJobDefinition.getJobDefinitionId());

		// mocks
		when(myJobPersistence.fetchInstances(anyInt(), eq(0))).thenReturn(Lists.newArrayList(instance));
		when(myJobPersistence.fetchInstance(eq(INSTANCE_ID))).thenReturn(Optional.of(instance));
		when(myJobPersistence.fetchAllWorkChunksIterator(eq(INSTANCE_ID), anyBoolean()))
			.thenAnswer(t -> theChunks.stream().map(c -> c.setStatus(WorkChunkStatusEnum.READY)).toList().iterator());

		// test
		mySvc.runMaintenancePass();
	}

	@Test
	public void testMaintenancePass_withREADYWorkChunksForReductionSteps_notQueuedButProcessed() {
		// setup
		List<WorkChunk> chunks = List.of(
			createWorkChunkStep3().setStatus(WorkChunkStatusEnum.READY),
			createWorkChunkStep3().setStatus(WorkChunkStatusEnum.READY)
		);
		List<WorkChunk> previousChunks = List.of(
			createWorkChunkStep2().setStatus(WorkChunkStatusEnum.COMPLETED),
			createWorkChunkStep2().setStatus(WorkChunkStatusEnum.COMPLETED)
		);

		String lastStepId = chunks.get(0).getTargetStepId();

		// when
		when(myJobPersistence.getDistinctWorkChunkStatesForJobAndStep(eq(INSTANCE_ID), eq(lastStepId)))
			.thenReturn(chunks.stream().map(WorkChunk::getStatus).collect(Collectors.toSet()));

		// test
		runEnqueueReadyChunksTest(chunks, createJobDefinitionWithReduction());

		// verify never updated (should remain in ready state)
		verify(myJobPersistence, never()).fetchAllWorkChunkMetadataForJobInStates(any(), anyString(), any());
		verify(myJobPersistence, never()).enqueueWorkChunkForProcessing(anyString(), any());
		verify(myWorkChannelProducer, never()).send(any());
		verify(myReductionStepExecutorService)
			.triggerReductionStep(anyString(), any());
	}

	@Test
	public void testMaintenancePass_withREADYworkChunksForNonReductionStep_movedToQUEUEDandPublished() {
		// setup
		List<WorkChunk> chunks = List.of(
			createWorkChunkStep2().setStatus(WorkChunkStatusEnum.READY),
			createWorkChunkStep2().setStatus(WorkChunkStatusEnum.READY)
		);

		// when
		doAnswer(args -> {
			Consumer<Integer> consumer = args.getArgument(1);
			consumer.accept(1);
			return 1;
		}).when(myJobPersistence).enqueueWorkChunkForProcessing(anyString(), any());

		Page<WorkChunkMetadata> page = getPageOfData(chunks);
		when(myJobPersistence.fetchAllWorkChunkMetadataForJobInStates(any(Pageable.class), eq(INSTANCE_ID), any())).thenReturn(page);

		// test
		runEnqueueReadyChunksTest(chunks, createJobDefinition());

		// verify
		verify(myJobPersistence, times(2)).enqueueWorkChunkForProcessing(anyString(), any());
		verify(myWorkChannelProducer, times(2)).send(myMessageCaptor.capture());
		List<Message<JobWorkNotification>> sentMessages = myMessageCaptor.getAllValues();
		for (Message<JobWorkNotification> msg : sentMessages) {
			JobWorkNotification payload = msg.getPayload();
			assertEquals(STEP_2, payload.getTargetStepId());
			assertEquals(CHUNK_ID, payload.getChunkId());
		}
	}

	@Test
	public void testMaintenancePass_whenUpdateFails_skipsWorkChunkAndLogs() {
		// setup
		List<WorkChunk> chunks = List.of(
			createWorkChunkStep2().setStatus(WorkChunkStatusEnum.READY),
			createWorkChunkStep2().setStatus(WorkChunkStatusEnum.READY)
		);
		JobInstance instance = createInstance();
		instance.setCurrentGatedStepId(STEP_2);

		myLogCapture.setUp(Level.ERROR);

		// when
		doAnswer(args -> {
			Consumer<Integer> consumer = args.getArgument(1);
			consumer.accept(0); // nothing processed
			return 1;
		}).when(myJobPersistence).enqueueWorkChunkForProcessing(anyString(), any());
		doAnswer(args -> {
			IJobPersistence.JobInstanceUpdateCallback callback = args.getArgument(1);

			callback.doUpdate(instance);
			return true;
		}).when(myJobPersistence).updateInstance(any(), any());
		when(myJobPersistence.getDistinctWorkChunkStatesForJobAndStep(eq(instance.getInstanceId()), eq(STEP_2)))
			.thenReturn(chunks.stream().map(WorkChunkMetadata::getStatus).collect(Collectors.toSet()));
		Page<WorkChunkMetadata> page = getPageOfData(chunks);
		when(myJobPersistence.fetchAllWorkChunkMetadataForJobInStates(any(Pageable.class), eq(INSTANCE_ID), any())).thenReturn(page);


		// test
		runEnqueueReadyChunksTest(chunks, createJobDefinitionWithReduction());

		// verify
		verify(myJobPersistence, times(2)).enqueueWorkChunkForProcessing(anyString(), any());
		verify(myWorkChannelProducer, never()).send(any());

		List<ILoggingEvent> events = myLogCapture.getLogEvents();
		assertEquals(2, events.size());
		for (ILoggingEvent evt : events) {
			assertTrue(evt.getMessage().contains("skipping work chunk"));
		}
	}

	@Test
	void triggerMaintenancePass_noneInProgress_runsMaintenance() {
		when(myJobPersistence.fetchInstances(anyInt(), eq(0))).thenReturn(Collections.emptyList());
		mySvc.triggerMaintenancePass();

		// Verify maintenance was only called once
		verify(myJobPersistence, times(1)).fetchInstances(anyInt(), eq(0));
	}

	@Test
	void triggerMaintenancePassDisabled_noneInProgress_doesNotRunMaintenace() {
		myStorageSettings.setJobFastTrackingEnabled(false);
		mySvc.triggerMaintenancePass();
		verifyNoMoreInteractions(myJobPersistence);
	}

	@Test
	void triggerMaintenancePass_twoSimultaneousRequests_onlyCallOnce() throws InterruptedException, ExecutionException {
		CountDownLatch simulatedMaintenancePasslatch = new CountDownLatch(1);
		CountDownLatch maintenancePassCalled = new CountDownLatch(1);
		CountDownLatch secondCall = new CountDownLatch(1);

		when(myJobPersistence.fetchInstances(anyInt(), eq(0)))
			.thenAnswer(t -> {
				maintenancePassCalled.countDown();
				simulatedMaintenancePasslatch.await();
				return Collections.emptyList();
			})
			.thenAnswer(t -> {
				secondCall.countDown();
				return Collections.emptyList();
			});

		// Trigger a thread blocking on our latch maintenance pass in the background
		Future<Boolean> result1 = Executors.newSingleThreadExecutor().submit(() -> mySvc.triggerMaintenancePass());
		// Trigger a thread blocking on the semaphore maintenance pass in the background
		Future<Boolean> result2 = Executors.newSingleThreadExecutor().submit(() -> mySvc.triggerMaintenancePass());

		// Wait for the first background maintenance pass to block
		maintenancePassCalled.await();
		// Wait for the second background maintenance pass to block
		await().until(() -> mySvc.getQueueLength() > 0);

		// Now trigger a maintenance pass in the foreground.  It should abort right away since there is already one thread in queue
		assertFalse(mySvc.triggerMaintenancePass());

		// Now release the background task
		simulatedMaintenancePasslatch.countDown();

		// Now wait for the second maintenance pass to be called
		secondCall.await();

		// Verify maintenance was only called once
		verify(myJobPersistence, times(2)).fetchInstances(anyInt(), eq(0));
		assertTrue(result1.get());
		assertTrue(result2.get());
	}

	private static Date parseTime(String theDate) {
		return new DateTimeType(theDate).getValue();
	}

	private Page<WorkChunkMetadata> getPageOfData(List<WorkChunk> theChunks) {
		return new PageImpl<>(theChunks.stream().map(c -> (WorkChunkMetadata)c).collect(Collectors.toList()));
	}
}
