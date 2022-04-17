package ca.uhn.fhir.batch2.impl;

import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobWorkNotification;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelProducer;
import ca.uhn.fhir.rest.server.interceptor.ResponseSizeCapturingInterceptor;
import com.google.common.collect.Lists;
import org.hl7.fhir.r4.model.DateTimeType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.Message;

import java.util.Date;
import java.util.EnumSet;

import static ca.uhn.fhir.batch2.impl.JobCoordinatorImplTest.INSTANCE_ID;
import static ca.uhn.fhir.batch2.impl.JobCoordinatorImplTest.STEP_1;
import static ca.uhn.fhir.batch2.impl.JobCoordinatorImplTest.createInstance;
import static ca.uhn.fhir.batch2.impl.JobCoordinatorImplTest.createWorkChunk;
import static ca.uhn.fhir.batch2.impl.JobCoordinatorImplTest.createWorkChunkStep1;
import static ca.uhn.fhir.batch2.impl.JobCoordinatorImplTest.createWorkChunkStep2;
import static ca.uhn.fhir.batch2.impl.JobCoordinatorImplTest.createWorkChunkStep3;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JobCleanerServiceImplTest extends BaseBatch2Test {

	@Mock
	private ISchedulerService mySchedulerService;
	@Mock
	private IJobPersistence myJobPersistence;
	private JobCleanerServiceImpl mySvc;
	@Captor
	private ArgumentCaptor<JobInstance> myInstanceCaptor;
	private JobDefinitionRegistry myJobDefinitionRegistry;
	private BatchJobSender myBatchJobSender;
	@Mock
	private IChannelProducer myWorkChannelProducer;
	@Captor
	private ArgumentCaptor<Message<JobWorkNotification>> myMessageCaptor;

	@BeforeEach
	public void beforeEach() {
		myJobDefinitionRegistry = new JobDefinitionRegistry();
		myBatchJobSender = new BatchJobSender(myWorkChannelProducer);
		mySvc = new JobCleanerServiceImpl(mySchedulerService, myJobPersistence, myJobDefinitionRegistry, myBatchJobSender);
	}

	@Test
	public void testInProgress_CalculateProgress_FirstCompleteButNoOtherStepsYetComplete() {
		myJobDefinitionRegistry.addJobDefinition(createJobDefinition());
		when(myJobPersistence.fetchInstances(anyInt(), eq(0))).thenReturn(Lists.newArrayList(createInstance()));
		when(myJobPersistence.fetchWorkChunksWithoutData(eq(INSTANCE_ID), anyInt(), eq(0))).thenReturn(Lists.newArrayList(
			createWorkChunk(STEP_1, null).setStatus(StatusEnum.COMPLETED)
		));

		mySvc.runMaintenancePass();

		verify(myJobPersistence, never()).updateInstance(any());
	}

	@Test
	public void testInProgress_CalculateProgress_FirstStepComplete() {
		myJobDefinitionRegistry.addJobDefinition(createJobDefinition());
		when(myJobPersistence.fetchInstances(anyInt(), eq(0))).thenReturn(Lists.newArrayList(createInstance()));
		when(myJobPersistence.fetchWorkChunksWithoutData(eq(INSTANCE_ID), anyInt(), eq(0))).thenReturn(Lists.newArrayList(
			createWorkChunkStep1().setStatus(StatusEnum.COMPLETED).setStartTime(parseTime("2022-02-12T14:00:00-04:00")),
			createWorkChunkStep2().setStatus(StatusEnum.IN_PROGRESS).setStartTime(parseTime("2022-02-12T14:00:01-04:00")),
			createWorkChunkStep2().setStatus(StatusEnum.IN_PROGRESS).setStartTime(parseTime("2022-02-12T14:00:02-04:00")),
			createWorkChunkStep2().setStatus(StatusEnum.IN_PROGRESS).setStartTime(parseTime("2022-02-12T14:00:03-04:00")),
			createWorkChunkStep2().setStatus(StatusEnum.COMPLETED).setStartTime(parseTime("2022-02-12T14:00:00-04:00")).setEndTime(parseTime("2022-02-12T14:10:00-04:00")).setRecordsProcessed(25),
			createWorkChunkStep3().setStatus(StatusEnum.COMPLETED).setStartTime(parseTime("2022-02-12T14:01:00-04:00")).setEndTime(parseTime("2022-02-12T14:10:00-04:00")).setRecordsProcessed(25)
		));

		mySvc.runMaintenancePass();

		verify(myJobPersistence, times(1)).updateInstance(myInstanceCaptor.capture());
		JobInstance instance = myInstanceCaptor.getValue();

		assertEquals(0.5, instance.getProgress());
		assertEquals(50, instance.getCombinedRecordsProcessed());
		assertEquals(0.08333333333333333, instance.getCombinedRecordsProcessedPerSecond());
		assertNotNull(instance.getStartTime());
		assertEquals(parseTime("2022-02-12T14:00:00-04:00"), instance.getStartTime());
		assertEquals(null, instance.getEndTime());
		assertEquals("00:10:00", instance.getEstimatedTimeRemaining());

		verifyNoMoreInteractions(myJobPersistence);
	}

	@Test
	public void testInProgress_CalculateProgress_InstanceHasErrorButNoChunksAreErrored() {
		// Setup
		myJobDefinitionRegistry.addJobDefinition(createJobDefinition());
		JobInstance instance1 = createInstance();
		instance1.setErrorMessage("This is an error message");
		when(myJobPersistence.fetchInstances(anyInt(), eq(0))).thenReturn(Lists.newArrayList(instance1));
		when(myJobPersistence.fetchWorkChunksWithoutData(eq(INSTANCE_ID), anyInt(), eq(0))).thenReturn(Lists.newArrayList(
			createWorkChunkStep1().setStatus(StatusEnum.COMPLETED).setStartTime(parseTime("2022-02-12T14:00:00-04:00")),
			createWorkChunkStep2().setStatus(StatusEnum.IN_PROGRESS).setStartTime(parseTime("2022-02-12T14:00:01-04:00")),
			createWorkChunkStep2().setStatus(StatusEnum.IN_PROGRESS).setStartTime(parseTime("2022-02-12T14:00:02-04:00")).setErrorCount(2),
			createWorkChunkStep2().setStatus(StatusEnum.IN_PROGRESS).setStartTime(parseTime("2022-02-12T14:00:03-04:00")).setErrorCount(2),
			createWorkChunkStep2().setStatus(StatusEnum.COMPLETED).setStartTime(parseTime("2022-02-12T14:00:00-04:00")).setEndTime(parseTime("2022-02-12T14:10:00-04:00")).setRecordsProcessed(25),
			createWorkChunkStep3().setStatus(StatusEnum.COMPLETED).setStartTime(parseTime("2022-02-12T14:01:00-04:00")).setEndTime(parseTime("2022-02-12T14:10:00-04:00")).setRecordsProcessed(25)
		));

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
		myJobDefinitionRegistry.addJobDefinition(createJobDefinition(t->t.gatedExecution()));
		when(myJobPersistence.fetchWorkChunksWithoutData(eq(INSTANCE_ID), anyInt(), eq(0))).thenReturn(Lists.newArrayList(
			createWorkChunkStep2().setStatus(StatusEnum.QUEUED).setId(CHUNK_ID),
			createWorkChunkStep2().setStatus(StatusEnum.QUEUED).setId(CHUNK_ID_2)
		));
		when(myJobPersistence.fetchWorkChunksWithoutData(eq(INSTANCE_ID), eq(STEP_1), eq(StatusEnum.INCOMPLETE_STATUSES), eq(1), eq(0))).thenReturn(Lists.newArrayList());
		when(myJobPersistence.fetchWorkChunksWithoutData(eq(INSTANCE_ID), eq(STEP_2), eq(EnumSet.of(StatusEnum.QUEUED)), eq(100), eq(0))).thenReturn(Lists.newArrayList(
			createWorkChunkStep2().setStatus(StatusEnum.QUEUED).setId(CHUNK_ID),
			createWorkChunkStep2().setStatus(StatusEnum.QUEUED).setId(CHUNK_ID_2)
		));
		JobInstance instance1 = createInstance();
		instance1.setCurrentGatedStepId(STEP_1);
		when(myJobPersistence.fetchInstances(anyInt(), eq(0))).thenReturn(Lists.newArrayList(instance1));

		// Execute
		mySvc.runMaintenancePass();

		// Verify
		verify(myWorkChannelProducer, times(2)).send(myMessageCaptor.capture());
		JobWorkNotification payload0 = myMessageCaptor.getAllValues().get(0).getPayload();
		assertEquals(STEP_2, payload0.getTargetStepId());
		assertEquals(CHUNK_ID, payload0.getChunkId());
		JobWorkNotification payload1 = myMessageCaptor.getAllValues().get(1).getPayload();
		assertEquals(STEP_2, payload1.getTargetStepId());
		assertEquals(CHUNK_ID_2, payload1.getChunkId());
	}


	@Test
	public void testFailed_PurgeOldInstance() {
		JobInstance instance = createInstance();
		instance.setStatus(StatusEnum.FAILED);
		instance.setEndTime(parseTime("2001-01-01T12:12:12Z"));
		when(myJobPersistence.fetchInstances(anyInt(), eq(0))).thenReturn(Lists.newArrayList(instance));

		mySvc.runMaintenancePass();

		verify(myJobPersistence, times(1)).deleteInstanceAndChunks(eq(INSTANCE_ID));
		verifyNoMoreInteractions(myJobPersistence);
	}

	@Test
	public void testInProgress_CalculateProgress_AllStepsComplete() {
		when(myJobPersistence.fetchInstances(anyInt(), eq(0))).thenReturn(Lists.newArrayList(createInstance()));
		when(myJobPersistence.fetchWorkChunksWithoutData(eq(INSTANCE_ID), anyInt(), eq(0))).thenReturn(Lists.newArrayList(
			createWorkChunkStep1().setStatus(StatusEnum.COMPLETED).setStartTime(parseTime("2022-02-12T14:00:00-04:00")).setEndTime(parseTime("2022-02-12T14:01:00-04:00")).setRecordsProcessed(25),
			createWorkChunkStep2().setStatus(StatusEnum.COMPLETED).setStartTime(parseTime("2022-02-12T14:00:01-04:00")).setEndTime(parseTime("2022-02-12T14:06:00-04:00")).setRecordsProcessed(25),
			createWorkChunkStep2().setStatus(StatusEnum.COMPLETED).setStartTime(parseTime("2022-02-12T14:00:02-04:00")).setEndTime(parseTime("2022-02-12T14:06:00-04:00")).setRecordsProcessed(25),
			createWorkChunkStep2().setStatus(StatusEnum.COMPLETED).setStartTime(parseTime("2022-02-12T14:00:03-04:00")).setEndTime(parseTime("2022-02-12T14:06:00-04:00")).setRecordsProcessed(25),
			createWorkChunkStep2().setStatus(StatusEnum.COMPLETED).setStartTime(parseTime("2022-02-12T14:00:00-04:00")).setEndTime(parseTime("2022-02-12T14:10:00-04:00")).setRecordsProcessed(25),
			createWorkChunkStep3().setStatus(StatusEnum.COMPLETED).setStartTime(parseTime("2022-02-12T14:01:00-04:00")).setEndTime(parseTime("2022-02-12T14:10:00-04:00")).setRecordsProcessed(25)
		));

		mySvc.runMaintenancePass();

		verify(myJobPersistence, times(2)).updateInstance(myInstanceCaptor.capture());
		JobInstance instance = myInstanceCaptor.getAllValues().get(0);

		assertEquals(1.0, instance.getProgress());
		assertEquals(StatusEnum.COMPLETED, instance.getStatus());
		assertEquals(150, instance.getCombinedRecordsProcessed());
		assertEquals(0.25, instance.getCombinedRecordsProcessedPerSecond());
		assertEquals(parseTime("2022-02-12T14:10:00-04:00"), instance.getEndTime());

		verify(myJobPersistence, times(1)).deleteChunks(eq(INSTANCE_ID));

		verifyNoMoreInteractions(myJobPersistence);
	}

	@Test
	public void testInProgress_CalculateProgress_OneStepFailed() {
		when(myJobPersistence.fetchInstances(anyInt(), eq(0))).thenReturn(Lists.newArrayList(createInstance()));
		when(myJobPersistence.fetchWorkChunksWithoutData(eq(INSTANCE_ID), anyInt(), eq(0))).thenReturn(Lists.newArrayList(
			createWorkChunkStep1().setStatus(StatusEnum.COMPLETED).setStartTime(parseTime("2022-02-12T14:00:00-04:00")).setEndTime(parseTime("2022-02-12T14:01:00-04:00")).setRecordsProcessed(25),
			createWorkChunkStep2().setStatus(StatusEnum.COMPLETED).setStartTime(parseTime("2022-02-12T14:00:01-04:00")).setEndTime(parseTime("2022-02-12T14:06:00-04:00")).setRecordsProcessed(25),
			createWorkChunkStep2().setStatus(StatusEnum.FAILED).setStartTime(parseTime("2022-02-12T14:00:02-04:00")).setEndTime(parseTime("2022-02-12T14:06:00-04:00")).setRecordsProcessed(25).setErrorMessage("This is an error message"),
			createWorkChunkStep2().setStatus(StatusEnum.COMPLETED).setStartTime(parseTime("2022-02-12T14:00:03-04:00")).setEndTime(parseTime("2022-02-12T14:06:00-04:00")).setRecordsProcessed(25),
			createWorkChunkStep2().setStatus(StatusEnum.COMPLETED).setStartTime(parseTime("2022-02-12T14:00:00-04:00")).setEndTime(parseTime("2022-02-12T14:10:00-04:00")).setRecordsProcessed(25),
			createWorkChunkStep3().setStatus(StatusEnum.COMPLETED).setStartTime(parseTime("2022-02-12T14:01:00-04:00")).setEndTime(parseTime("2022-02-12T14:10:00-04:00")).setRecordsProcessed(25)
		));

		mySvc.runMaintenancePass();

		verify(myJobPersistence, times(2)).updateInstance(myInstanceCaptor.capture());
		JobInstance instance = myInstanceCaptor.getAllValues().get(0);

		assertEquals(0.8333333333333334, instance.getProgress());
		assertEquals(StatusEnum.FAILED, instance.getStatus());
		assertEquals("This is an error message", instance.getErrorMessage());
		assertEquals(150, instance.getCombinedRecordsProcessed());
		assertEquals(0.25, instance.getCombinedRecordsProcessedPerSecond());
		assertEquals(parseTime("2022-02-12T14:10:00-04:00"), instance.getEndTime());

		verify(myJobPersistence, times(1)).deleteChunks(eq(INSTANCE_ID));

		verifyNoMoreInteractions(myJobPersistence);
	}

	private static Date parseTime(String theDate) {
		return new DateTimeType(theDate).getValue();
	}

}
