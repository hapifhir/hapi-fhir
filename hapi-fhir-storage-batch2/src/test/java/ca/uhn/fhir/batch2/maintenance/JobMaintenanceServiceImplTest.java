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
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelProducer;
import com.google.common.collect.Lists;
import org.hl7.fhir.r4.model.DateTimeType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.Message;

import java.util.ArrayList;
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
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
	@Captor
	private ArgumentCaptor<JobInstance> myInstanceCaptor;
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
		List<WorkChunk> chunks = new ArrayList<>();
		chunks.add(JobCoordinatorImplTest.createWorkChunk(STEP_1, null).setStatus(StatusEnum.COMPLETED));

		myJobDefinitionRegistry.addJobDefinition(createJobDefinition());
		when(myJobPersistence.fetchInstances(anyInt(), eq(0))).thenReturn(Lists.newArrayList(createInstance()));

		mySvc.runMaintenancePass();

		verify(myJobPersistence, never()).updateInstance(any());
	}

	@Test
	public void testInProgress_CalculateProgress_FirstStepComplete() {
		List<WorkChunk> chunks = Arrays.asList(
			createWorkChunkStep1().setStatus(StatusEnum.COMPLETED).setStartTime(parseTime("2022-02-12T14:00:00-04:00")),
			JobCoordinatorImplTest.createWorkChunkStep2().setStatus(StatusEnum.IN_PROGRESS).setStartTime(parseTime("2022-02-12T14:00:01-04:00")),
			JobCoordinatorImplTest.createWorkChunkStep2().setStatus(StatusEnum.IN_PROGRESS).setStartTime(parseTime("2022-02-12T14:00:02-04:00")),
			JobCoordinatorImplTest.createWorkChunkStep2().setStatus(StatusEnum.IN_PROGRESS).setStartTime(parseTime("2022-02-12T14:00:03-04:00")),
			JobCoordinatorImplTest.createWorkChunkStep2().setStatus(StatusEnum.COMPLETED).setStartTime(parseTime("2022-02-12T14:00:00-04:00")).setEndTime(parseTime("2022-02-12T14:10:00-04:00")).setRecordsProcessed(25),
			JobCoordinatorImplTest.createWorkChunkStep3().setStatus(StatusEnum.COMPLETED).setStartTime(parseTime("2022-02-12T14:01:00-04:00")).setEndTime(parseTime("2022-02-12T14:10:00-04:00")).setRecordsProcessed(25)
		);
		myJobDefinitionRegistry.addJobDefinition(createJobDefinition());
		when(myJobPersistence.fetchInstance(eq(INSTANCE_ID))).thenReturn(Optional.of(createInstance()));
		when(myJobPersistence.fetchInstances(anyInt(), eq(0))).thenReturn(Lists.newArrayList(createInstance()));
		when(myJobPersistence.fetchAllWorkChunksIterator(eq(INSTANCE_ID), eq(false)))
			.thenReturn(chunks.iterator());

		mySvc.runMaintenancePass();

		verify(myJobPersistence, times(1)).updateInstance(myInstanceCaptor.capture());
		JobInstance instance = myInstanceCaptor.getValue();

		assertEquals(0.5, instance.getProgress());
		assertEquals(50, instance.getCombinedRecordsProcessed());
		assertEquals(0.08333333333333333, instance.getCombinedRecordsProcessedPerSecond());
		assertNotNull(instance.getStartTime());
		assertEquals(parseTime("2022-02-12T14:00:00-04:00"), instance.getStartTime());
		assertNull(instance.getEndTime());
		assertEquals("00:10:00", instance.getEstimatedTimeRemaining());

		verifyNoMoreInteractions(myJobPersistence);
	}

	@Test
	public void testInProgress_CalculateProgress_InstanceHasErrorButNoChunksAreErrored() {
		// Setup
		List<WorkChunk> chunks = Arrays.asList(
			createWorkChunkStep1().setStatus(StatusEnum.COMPLETED).setStartTime(parseTime("2022-02-12T14:00:00-04:00")),
			JobCoordinatorImplTest.createWorkChunkStep2().setStatus(StatusEnum.IN_PROGRESS).setStartTime(parseTime("2022-02-12T14:00:01-04:00")),
			JobCoordinatorImplTest.createWorkChunkStep2().setStatus(StatusEnum.IN_PROGRESS).setStartTime(parseTime("2022-02-12T14:00:02-04:00")).setErrorCount(2),
			JobCoordinatorImplTest.createWorkChunkStep2().setStatus(StatusEnum.IN_PROGRESS).setStartTime(parseTime("2022-02-12T14:00:03-04:00")).setErrorCount(2),
			JobCoordinatorImplTest.createWorkChunkStep2().setStatus(StatusEnum.COMPLETED).setStartTime(parseTime("2022-02-12T14:00:00-04:00")).setEndTime(parseTime("2022-02-12T14:10:00-04:00")).setRecordsProcessed(25),
			JobCoordinatorImplTest.createWorkChunkStep3().setStatus(StatusEnum.COMPLETED).setStartTime(parseTime("2022-02-12T14:01:00-04:00")).setEndTime(parseTime("2022-02-12T14:10:00-04:00")).setRecordsProcessed(25)
		);
		myJobDefinitionRegistry.addJobDefinition(createJobDefinition());
		JobInstance instance1 = createInstance();
		instance1.setErrorMessage("This is an error message");
		when(myJobPersistence.fetchInstance(eq(INSTANCE_ID))).thenReturn(Optional.of(createInstance()));
		when(myJobPersistence.fetchInstances(anyInt(), eq(0))).thenReturn(Lists.newArrayList(instance1));
		when(myJobPersistence.fetchAllWorkChunksIterator(eq(INSTANCE_ID), eq(false)))
			.thenReturn(chunks.iterator());

		// Execute
		mySvc.runMaintenancePass();

		// Verify
		verify(myJobPersistence, times(1)).updateInstance(myInstanceCaptor.capture());
		JobInstance instance = myInstanceCaptor.getValue();

		assertNull(instance.getErrorMessage());
		assertEquals(4, instance.getErrorCount());
		assertEquals(0.5, instance.getProgress());
		assertEquals(50, instance.getCombinedRecordsProcessed());
		assertEquals(0.08333333333333333, instance.getCombinedRecordsProcessedPerSecond());

		verifyNoMoreInteractions(myJobPersistence);
	}

	@Test
	public void testInProgress_GatedExecution_FirstStepComplete() {
		// Setup
		List<WorkChunk> chunks = Arrays.asList(
			JobCoordinatorImplTest.createWorkChunkStep2().setStatus(StatusEnum.QUEUED).setId(CHUNK_ID),
			JobCoordinatorImplTest.createWorkChunkStep2().setStatus(StatusEnum.QUEUED).setId(CHUNK_ID_2)
		);
		when (myJobPersistence.canAdvanceInstanceToNextStep(any(), any())).thenReturn(true);
		myJobDefinitionRegistry.addJobDefinition(createJobDefinition(JobDefinition.Builder::gatedExecution));

		when(myJobPersistence.fetchAllWorkChunksIterator(eq(INSTANCE_ID), eq(false)))
			.thenReturn(chunks.iterator());

		when(myJobPersistence.fetchallchunkidsforstepWithStatus(eq(INSTANCE_ID), eq(STEP_2), eq(StatusEnum.QUEUED)))
			.thenReturn(chunks.stream().map(chunk -> chunk.getId()).collect(Collectors.toList()));

		JobInstance instance1 = createInstance();
		instance1.setCurrentGatedStepId(STEP_1);
		when(myJobPersistence.fetchInstances(anyInt(), eq(0))).thenReturn(Lists.newArrayList(instance1));
		when(myJobPersistence.fetchInstance(eq(INSTANCE_ID))).thenReturn(Optional.of(instance1));

		// Execute
		mySvc.runMaintenancePass();

		// Verify
		verify(myWorkChannelProducer, times(2)).send(myMessageCaptor.capture());
		verify(myJobPersistence, times(2)).updateInstance(myInstanceCaptor.capture());
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
		when(myJobPersistence.fetchInstance(eq(INSTANCE_ID))).thenReturn(Optional.of(instance));

		mySvc.runMaintenancePass();

		verify(myJobPersistence, times(1)).deleteInstanceAndChunks(eq(INSTANCE_ID));
		verifyNoMoreInteractions(myJobPersistence);
	}

	@Test
	public void testInProgress_CalculateProgress_AllStepsComplete() {
		// Setup
		List<WorkChunk> chunks = new ArrayList<>();

		chunks.add(
			createWorkChunkStep1().setStatus(StatusEnum.COMPLETED).setStartTime(parseTime("2022-02-12T14:00:00-04:00")).setEndTime(parseTime("2022-02-12T14:01:00-04:00")).setRecordsProcessed(25)
		);
		chunks.add(
			JobCoordinatorImplTest.createWorkChunkStep2().setStatus(StatusEnum.COMPLETED).setStartTime(parseTime("2022-02-12T14:00:01-04:00")).setEndTime(parseTime("2022-02-12T14:06:00-04:00")).setRecordsProcessed(25)
		);
		chunks.add(
			JobCoordinatorImplTest.createWorkChunkStep2().setStatus(StatusEnum.COMPLETED).setStartTime(parseTime("2022-02-12T14:00:02-04:00")).setEndTime(parseTime("2022-02-12T14:06:00-04:00")).setRecordsProcessed(25)
		);
		chunks.add(
			JobCoordinatorImplTest.createWorkChunkStep2().setStatus(StatusEnum.COMPLETED).setStartTime(parseTime("2022-02-12T14:00:03-04:00")).setEndTime(parseTime("2022-02-12T14:06:00-04:00")).setRecordsProcessed(25)
		);
		chunks.add(
			JobCoordinatorImplTest.createWorkChunkStep2().setStatus(StatusEnum.COMPLETED).setStartTime(parseTime("2022-02-12T14:00:00-04:00")).setEndTime(parseTime("2022-02-12T14:10:00-04:00")).setRecordsProcessed(25)
		);
		chunks.add(
			JobCoordinatorImplTest.createWorkChunkStep3().setStatus(StatusEnum.COMPLETED).setStartTime(parseTime("2022-02-12T14:01:00-04:00")).setEndTime(parseTime("2022-02-12T14:10:00-04:00")).setRecordsProcessed(25)
		);

		myJobDefinitionRegistry.addJobDefinition(createJobDefinition(t -> t.completionHandler(myCompletionHandler)));
		JobInstance instance1 = createInstance();
		when(myJobPersistence.fetchInstances(anyInt(), eq(0))).thenReturn(Lists.newArrayList(instance1));
		when(myJobPersistence.fetchAllWorkChunksIterator(eq(INSTANCE_ID), anyBoolean())).thenAnswer(t->chunks.iterator());
		when(myJobPersistence.updateInstance(any())).thenReturn(true);
		when(myJobPersistence.fetchInstance(INSTANCE_ID)).thenReturn(Optional.of(instance1));

		// Execute

		mySvc.runMaintenancePass();

		// Verify

		verify(myJobPersistence, times(2)).updateInstance(myInstanceCaptor.capture());
		JobInstance instance = myInstanceCaptor.getAllValues().get(0);

		assertEquals(1.0, instance.getProgress());
		assertEquals(StatusEnum.COMPLETED, instance.getStatus());
		assertEquals(150, instance.getCombinedRecordsProcessed());
		assertEquals(0.25, instance.getCombinedRecordsProcessedPerSecond());
		assertEquals(parseTime("2022-02-12T14:10:00-04:00"), instance.getEndTime());

		verify(myJobPersistence, times(1)).deleteChunksAndMarkInstanceAsChunksPurged(eq(INSTANCE_ID));
		verify(myCompletionHandler, times(1)).jobComplete(myJobCompletionCaptor.capture());

		verifyNoMoreInteractions(myJobPersistence);

		assertEquals(INSTANCE_ID, myJobCompletionCaptor.getValue().getInstance().getInstanceId());
		assertEquals(PARAM_1_VALUE, myJobCompletionCaptor.getValue().getParameters().getParam1());
	}

	@Test
	public void testInProgress_CalculateProgress_OneStepFailed() {
		ArrayList<WorkChunk> chunks = new ArrayList<>();
		chunks.add(
			createWorkChunkStep1().setStatus(StatusEnum.COMPLETED).setStartTime(parseTime("2022-02-12T14:00:00-04:00")).setEndTime(parseTime("2022-02-12T14:01:00-04:00")).setRecordsProcessed(25)
		);
		chunks.add(
			JobCoordinatorImplTest.createWorkChunkStep2().setStatus(StatusEnum.COMPLETED).setStartTime(parseTime("2022-02-12T14:00:01-04:00")).setEndTime(parseTime("2022-02-12T14:06:00-04:00")).setRecordsProcessed(25)
		);
		chunks.add(
			JobCoordinatorImplTest.createWorkChunkStep2().setStatus(StatusEnum.FAILED).setStartTime(parseTime("2022-02-12T14:00:02-04:00")).setEndTime(parseTime("2022-02-12T14:06:00-04:00")).setRecordsProcessed(25).setErrorMessage("This is an error message")
		);
		chunks.add(
			JobCoordinatorImplTest.createWorkChunkStep2().setStatus(StatusEnum.COMPLETED).setStartTime(parseTime("2022-02-12T14:00:03-04:00")).setEndTime(parseTime("2022-02-12T14:06:00-04:00")).setRecordsProcessed(25)
		);
		chunks.add(
			JobCoordinatorImplTest.createWorkChunkStep2().setStatus(StatusEnum.COMPLETED).setStartTime(parseTime("2022-02-12T14:00:00-04:00")).setEndTime(parseTime("2022-02-12T14:10:00-04:00")).setRecordsProcessed(25)
		);
		chunks.add(
			JobCoordinatorImplTest.createWorkChunkStep3().setStatus(StatusEnum.COMPLETED).setStartTime(parseTime("2022-02-12T14:01:00-04:00")).setEndTime(parseTime("2022-02-12T14:10:00-04:00")).setRecordsProcessed(25)
		);

		myJobDefinitionRegistry.addJobDefinition(createJobDefinition());
		when(myJobPersistence.fetchInstance(eq(INSTANCE_ID))).thenReturn(Optional.of(createInstance()));
		when(myJobPersistence.fetchInstances(anyInt(), eq(0))).thenReturn(Lists.newArrayList(createInstance()));
		when(myJobPersistence.fetchAllWorkChunksIterator(eq(INSTANCE_ID), anyBoolean()))
			.thenAnswer(t->chunks.iterator());

		mySvc.runMaintenancePass();

		verify(myJobPersistence, times(3)).updateInstance(myInstanceCaptor.capture());
		JobInstance instance = myInstanceCaptor.getAllValues().get(0);

		assertEquals(0.8333333333333334, instance.getProgress());
		assertEquals(StatusEnum.FAILED, instance.getStatus());
		assertEquals("This is an error message", instance.getErrorMessage());
		assertEquals(150, instance.getCombinedRecordsProcessed());
		assertEquals(0.25, instance.getCombinedRecordsProcessedPerSecond());
		assertEquals(parseTime("2022-02-12T14:10:00-04:00"), instance.getEndTime());

		verify(myJobPersistence, times(1)).deleteChunksAndMarkInstanceAsChunksPurged(eq(INSTANCE_ID));

		verifyNoMoreInteractions(myJobPersistence);
	}


	@Nested
	public class CancellationTests {

		@Test
		public void afterFirstMaintenancePass() {
			// Setup
			ArrayList<WorkChunk> chunks = new ArrayList<>();
			chunks.add(
				JobCoordinatorImplTest.createWorkChunkStep2().setStatus(StatusEnum.QUEUED).setId(CHUNK_ID)
			);
			chunks.add(
				JobCoordinatorImplTest.createWorkChunkStep2().setStatus(StatusEnum.QUEUED).setId(CHUNK_ID_2)
			);
			myJobDefinitionRegistry.addJobDefinition(createJobDefinition(JobDefinition.Builder::gatedExecution));
			when(myJobPersistence.fetchAllWorkChunksIterator(eq(INSTANCE_ID), anyBoolean())).thenAnswer(t->chunks.iterator());
			JobInstance instance1 = createInstance();
			instance1.setCurrentGatedStepId(STEP_1);
			when(myJobPersistence.fetchInstances(anyInt(), eq(0))).thenReturn(Lists.newArrayList(instance1));
			when(myJobPersistence.fetchInstance(eq(INSTANCE_ID))).thenReturn(Optional.of(instance1));

			mySvc.runMaintenancePass();

			// Execute
			instance1.setCancelled(true);

			mySvc.runMaintenancePass();

			// Verify
			verify(myJobPersistence, times(2)).updateInstance(myInstanceCaptor.capture());
			assertEquals(StatusEnum.CANCELLED, instance1.getStatus());
			assertTrue(instance1.getErrorMessage().startsWith("Job instance cancelled"));
		}

		@Test
		public void afterSecondMaintenancePass() {
			// Setup
			ArrayList<WorkChunk> chunks = new ArrayList<>();
			chunks.add(
				JobCoordinatorImplTest.createWorkChunkStep2().setStatus(StatusEnum.QUEUED).setId(CHUNK_ID)
			);
			chunks.add(
				JobCoordinatorImplTest.createWorkChunkStep2().setStatus(StatusEnum.QUEUED).setId(CHUNK_ID_2)
			);
			myJobDefinitionRegistry.addJobDefinition(createJobDefinition(JobDefinition.Builder::gatedExecution));
			when(myJobPersistence.fetchAllWorkChunksIterator(eq(INSTANCE_ID), anyBoolean())).thenAnswer(t->chunks.iterator());
			JobInstance instance1 = createInstance();
			instance1.setCurrentGatedStepId(STEP_1);
			when(myJobPersistence.fetchInstances(anyInt(), eq(0))).thenReturn(Lists.newArrayList(instance1));
			when(myJobPersistence.fetchInstance(eq(INSTANCE_ID))).thenReturn(Optional.of(instance1));

			mySvc.runMaintenancePass();
			mySvc.runMaintenancePass();

			// Execute
			instance1.setCancelled(true);

			mySvc.runMaintenancePass();

			// Verify
			assertEquals(StatusEnum.CANCELLED, instance1.getStatus());
			assertTrue(instance1.getErrorMessage().startsWith("Job instance cancelled"));
		}

	}

	@Test
	void triggerMaintenancePass_noneInProgress_runsMaintenace() {
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

}
