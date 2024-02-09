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
import ca.uhn.fhir.batch2.model.WorkChunkStatusEnum;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelProducer;
import ca.uhn.test.util.LogbackCaptureTestExtension;
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
import org.springframework.messaging.Message;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static ca.uhn.fhir.batch2.coordinator.JobCoordinatorImplTest.createWorkChunkStep1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JobMaintenanceServiceImplTest extends BaseBatch2Test {

	@RegisterExtension
	LogbackCaptureTestExtension myLogCapture = new LogbackCaptureTestExtension((Logger) LoggerFactory.getLogger("ca.uhn.fhir.log.batch_troubleshooting"), Level.WARN);
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
			myReductionStepExecutorService);
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

		myJobDefinitionRegistry.addJobDefinition(createJobDefinition());
		JobInstance instance = createInstance();
		when(myJobPersistence.fetchInstances(anyInt(), eq(0))).thenReturn(List.of(instance));
		when(myJobPersistence.fetchInstance(INSTANCE_ID)).thenReturn(Optional.of(instance));

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
		List<ILoggingEvent> fetchedCredentialLogs = myLogCapture.filterLoggingEventsWithMessageEqualTo(assumedRoleLogText);
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
			JobCoordinatorImplTest.createWorkChunkStep3().setStatus(WorkChunkStatusEnum.COMPLETED).setStartTime(parseTime("2022-02-12T14:01:00-04:00")).setEndTime(parseTime("2022-02-12T14:10:00-04:00")).setRecordsProcessed(25)
		);
		myJobDefinitionRegistry.addJobDefinition(createJobDefinition());
		JobInstance instance = createInstance();
		when(myJobPersistence.fetchInstance(eq(INSTANCE_ID))).thenReturn(Optional.of(instance));
		when(myJobPersistence.fetchInstances(anyInt(), eq(0))).thenReturn(Lists.newArrayList(instance));
		when(myJobPersistence.fetchAllWorkChunksIterator(eq(INSTANCE_ID), eq(false)))
			.thenReturn(chunks.iterator());
		stubUpdateInstanceCallback(instance);

		mySvc.runMaintenancePass();

		verify(myJobPersistence, times(1)).updateInstance(eq(INSTANCE_ID), any());

		assertThat(instance.getProgress()).isEqualTo(0.5);
		assertThat(instance.getCombinedRecordsProcessed()).isEqualTo(50);
		assertThat(instance.getCombinedRecordsProcessedPerSecond()).isEqualTo(0.08333333333333333);
		assertThat(instance.getStartTime()).isNotNull();
		assertThat(instance.getStartTime()).isEqualTo(parseTime("2022-02-12T14:00:00-04:00"));
		assertThat(instance.getEndTime()).isNull();
		assertThat(instance.getEstimatedTimeRemaining()).isEqualTo("00:10:00");

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
			JobCoordinatorImplTest.createWorkChunkStep3().setStatus(WorkChunkStatusEnum.COMPLETED).setStartTime(parseTime("2022-02-12T14:01:00-04:00")).setEndTime(parseTime("2022-02-12T14:10:00-04:00")).setRecordsProcessed(25)
		);
		myJobDefinitionRegistry.addJobDefinition(createJobDefinition());
		JobInstance instance = createInstance();
		instance.setErrorMessage("This is an error message");
		when(myJobPersistence.fetchInstance(eq(INSTANCE_ID))).thenReturn(Optional.of(createInstance()));
		when(myJobPersistence.fetchInstances(anyInt(), eq(0))).thenReturn(Lists.newArrayList(instance));
		when(myJobPersistence.fetchAllWorkChunksIterator(eq(INSTANCE_ID), eq(false)))
			.thenReturn(chunks.iterator());
		stubUpdateInstanceCallback(instance);

		// Execute
		mySvc.runMaintenancePass();

		// Verify
		verify(myJobPersistence, times(1)).updateInstance(eq(INSTANCE_ID), any());

		assertThat(instance.getErrorMessage()).isNull();
		assertThat(instance.getErrorCount()).isEqualTo(4);
		assertThat(instance.getProgress()).isEqualTo(0.5);
		assertThat(instance.getCombinedRecordsProcessed()).isEqualTo(50);
		assertThat(instance.getCombinedRecordsProcessedPerSecond()).isEqualTo(0.08333333333333333);

		verifyNoMoreInteractions(myJobPersistence);
	}

	@Test
	public void testInProgress_GatedExecution_FirstStepComplete() {
		// Setup
		List<WorkChunk> chunks = Arrays.asList(
			JobCoordinatorImplTest.createWorkChunkStep1().setStatus(WorkChunkStatusEnum.COMPLETED).setId(CHUNK_ID + "abc"),
			JobCoordinatorImplTest.createWorkChunkStep2().setStatus(WorkChunkStatusEnum.QUEUED).setId(CHUNK_ID),
			JobCoordinatorImplTest.createWorkChunkStep2().setStatus(WorkChunkStatusEnum.QUEUED).setId(CHUNK_ID_2)
		);
		when (myJobPersistence.canAdvanceInstanceToNextStep(any(), any())).thenReturn(true);
		myJobDefinitionRegistry.addJobDefinition(createJobDefinition(JobDefinition.Builder::gatedExecution));

		when(myJobPersistence.fetchAllWorkChunksIterator(eq(INSTANCE_ID), eq(false)))
			.thenReturn(chunks.iterator());

		when(myJobPersistence.fetchAllChunkIdsForStepWithStatus(eq(INSTANCE_ID), eq(STEP_2), eq(WorkChunkStatusEnum.QUEUED)))
			.thenReturn(chunks.stream().filter(c->c.getTargetStepId().equals(STEP_2)).map(WorkChunk::getId).collect(Collectors.toList()));

		JobInstance instance1 = createInstance();
		instance1.setCurrentGatedStepId(STEP_1);
		when(myJobPersistence.fetchInstances(anyInt(), eq(0))).thenReturn(Lists.newArrayList(instance1));
		when(myJobPersistence.fetchInstance(INSTANCE_ID)).thenReturn(Optional.of(instance1));
		stubUpdateInstanceCallback(instance1);

		// Execute
		mySvc.runMaintenancePass();

		// Verify
		verify(myWorkChannelProducer, times(2)).send(myMessageCaptor.capture());
		verify(myJobPersistence, times(2)).updateInstance(eq(INSTANCE_ID), any());
		verifyNoMoreInteractions(myJobPersistence);
		JobWorkNotification payload0 = myMessageCaptor.getAllValues().get(0).getPayload();
		assertThat(payload0.getTargetStepId()).isEqualTo(STEP_2);
		assertThat(payload0.getChunkId()).isEqualTo(CHUNK_ID);
		JobWorkNotification payload1 = myMessageCaptor.getAllValues().get(1).getPayload();
		assertThat(payload1.getTargetStepId()).isEqualTo(STEP_2);
		assertThat(payload1.getChunkId()).isEqualTo(CHUNK_ID_2);
	}

	@Test
	public void testFailed_PurgeOldInstance() {
		myJobDefinitionRegistry.addJobDefinition(createJobDefinition());
		JobInstance instance = createInstance();
		instance.setStatus(StatusEnum.FAILED);
		instance.setEndTime(parseTime("2001-01-01T12:12:12Z"));
		when(myJobPersistence.fetchInstances(anyInt(), eq(0))).thenReturn(Lists.newArrayList(instance));
		when(myJobPersistence.fetchInstance(INSTANCE_ID)).thenReturn(Optional.of(instance));

		mySvc.runMaintenancePass();

		verify(myJobPersistence, times(1)).deleteInstanceAndChunks(eq(INSTANCE_ID));
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
			JobCoordinatorImplTest.createWorkChunkStep2().setStatus(WorkChunkStatusEnum.COMPLETED).setStartTime(parseTime("2022-02-12T14:00:00-04:00")).setEndTime(parseTime("2022-02-12T14:10:00-04:00")).setRecordsProcessed(25),JobCoordinatorImplTest.createWorkChunkStep3().setStatus(WorkChunkStatusEnum.COMPLETED).setStartTime(parseTime("2022-02-12T14:01:00-04:00")).setEndTime(parseTime("2022-02-12T14:10:00-04:00")).setRecordsProcessed(25)
		);

		myJobDefinitionRegistry.addJobDefinition(createJobDefinition(t -> t.completionHandler(myCompletionHandler)));
		JobInstance instance = createInstance();
		when(myJobPersistence.fetchInstances(anyInt(), eq(0))).thenReturn(Lists.newArrayList(instance));
		when(myJobPersistence.fetchAllWorkChunksIterator(eq(INSTANCE_ID), anyBoolean())).thenAnswer(t->chunks.iterator());
		when(myJobPersistence.fetchInstance(INSTANCE_ID)).thenReturn(Optional.of(instance));
		stubUpdateInstanceCallback(instance);

		// Execute

		mySvc.runMaintenancePass();

		// Verify

		verify(myJobPersistence, times(1)).updateInstance(eq(INSTANCE_ID), any());

		assertThat(instance.getProgress()).isEqualTo(1.0);
		assertThat(instance.getStatus()).isEqualTo(StatusEnum.COMPLETED);
		assertThat(instance.getCombinedRecordsProcessed()).isEqualTo(150);
		assertThat(instance.getCombinedRecordsProcessedPerSecond()).isEqualTo(0.25);
		assertThat(instance.getEndTime()).isEqualTo(parseTime("2022-02-12T14:10:00-04:00"));

		verify(myJobPersistence, times(1)).deleteChunksAndMarkInstanceAsChunksPurged(eq(INSTANCE_ID));
		verify(myCompletionHandler, times(1)).jobComplete(myJobCompletionCaptor.capture());

		verifyNoMoreInteractions(myJobPersistence);

		assertThat(myJobCompletionCaptor.getValue().getInstance().getInstanceId()).isEqualTo(INSTANCE_ID);
		assertThat(myJobCompletionCaptor.getValue().getParameters().getParam1()).isEqualTo(PARAM_1_VALUE);
	}

	@Test
	public void testInProgress_CalculateProgress_OneStepFailed() {
		List<WorkChunk> chunks = List.of(
			createWorkChunkStep1().setStatus(WorkChunkStatusEnum.COMPLETED).setStartTime(parseTime("2022-02-12T14:00:00-04:00")).setEndTime(parseTime("2022-02-12T14:01:00-04:00")).setRecordsProcessed(25),
			JobCoordinatorImplTest.createWorkChunkStep2().setStatus(WorkChunkStatusEnum.COMPLETED).setStartTime(parseTime("2022-02-12T14:00:01-04:00")).setEndTime(parseTime("2022-02-12T14:06:00-04:00")).setRecordsProcessed(25),
			JobCoordinatorImplTest.createWorkChunkStep2().setStatus(WorkChunkStatusEnum.FAILED).setStartTime(parseTime("2022-02-12T14:00:02-04:00")).setEndTime(parseTime("2022-02-12T14:06:00-04:00")).setRecordsProcessed(25).setErrorMessage("This is an error message"),
			JobCoordinatorImplTest.createWorkChunkStep2().setStatus(WorkChunkStatusEnum.COMPLETED).setStartTime(parseTime("2022-02-12T14:00:03-04:00")).setEndTime(parseTime("2022-02-12T14:06:00-04:00")).setRecordsProcessed(25),
			JobCoordinatorImplTest.createWorkChunkStep2().setStatus(WorkChunkStatusEnum.COMPLETED).setStartTime(parseTime("2022-02-12T14:00:00-04:00")).setEndTime(parseTime("2022-02-12T14:10:00-04:00")).setRecordsProcessed(25),
			JobCoordinatorImplTest.createWorkChunkStep3().setStatus(WorkChunkStatusEnum.COMPLETED).setStartTime(parseTime("2022-02-12T14:01:00-04:00")).setEndTime(parseTime("2022-02-12T14:10:00-04:00")).setRecordsProcessed(25)
		);

		myJobDefinitionRegistry.addJobDefinition(createJobDefinition());
		JobInstance instance = createInstance();
		when(myJobPersistence.fetchInstance(eq(INSTANCE_ID))).thenReturn(Optional.of(instance));
		when(myJobPersistence.fetchInstances(anyInt(), eq(0))).thenReturn(Lists.newArrayList(instance));
		when(myJobPersistence.fetchAllWorkChunksIterator(eq(INSTANCE_ID), anyBoolean()))
			.thenAnswer(t->chunks.iterator());
		stubUpdateInstanceCallback(instance);

		mySvc.runMaintenancePass();


		assertThat(instance.getProgress()).isEqualTo(0.8333333333333334);
		assertThat(instance.getStatus()).isEqualTo(StatusEnum.FAILED);
		assertThat(instance.getErrorMessage()).isEqualTo("This is an error message");
		assertThat(instance.getCombinedRecordsProcessed()).isEqualTo(150);
		assertThat(instance.getCombinedRecordsProcessedPerSecond()).isEqualTo(0.25);
		assertThat(instance.getEndTime()).isEqualTo(parseTime("2022-02-12T14:10:00-04:00"));

		// twice - once to move to FAILED, and once to purge the chunks
		verify(myJobPersistence, times(1)).updateInstance(eq(INSTANCE_ID), any());
		verify(myJobPersistence, times(1)).deleteChunksAndMarkInstanceAsChunksPurged(eq(INSTANCE_ID));

		verifyNoMoreInteractions(myJobPersistence);
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
		assertThat(mySvc.triggerMaintenancePass()).isFalse();

		// Now release the background task
		simulatedMaintenancePasslatch.countDown();

		// Now wait for the second maintenance pass to be called
		secondCall.await();

		// Verify maintenance was only called once
		verify(myJobPersistence, times(2)).fetchInstances(anyInt(), eq(0));
		assertThat(result1.get()).isTrue();
		assertThat(result2.get()).isTrue();
	}


	private static Date parseTime(String theDate) {
		return new DateTimeType(theDate).getValue();
	}

}
