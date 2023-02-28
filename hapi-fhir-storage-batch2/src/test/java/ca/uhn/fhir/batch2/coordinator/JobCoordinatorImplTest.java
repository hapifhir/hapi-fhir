package ca.uhn.fhir.batch2.coordinator;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobMaintenanceService;
import ca.uhn.fhir.batch2.api.IJobParametersValidator;
import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.channel.BatchJobSender;
import ca.uhn.fhir.batch2.model.FetchJobInstancesRequest;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.batch2.model.JobWorkNotification;
import ca.uhn.fhir.batch2.model.JobWorkNotificationJsonMessage;
import ca.uhn.fhir.batch2.model.MarkWorkChunkAsErrorRequest;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.dao.tx.NonTransactionalHapiTransactionService;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelReceiver;
import ca.uhn.fhir.jpa.subscription.channel.impl.LinkedBlockingChannel;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.messaging.MessageDeliveryException;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.MethodName.class)
public class JobCoordinatorImplTest extends BaseBatch2Test {
	private final IChannelReceiver myWorkChannelReceiver = LinkedBlockingChannel.newSynchronous("receiver");
	private final JobInstance ourQueuedInstance = createInstance(JOB_DEFINITION_ID, StatusEnum.QUEUED);
	private JobCoordinatorImpl mySvc;
	@Mock
	private BatchJobSender myBatchJobSender;
	@Mock
	private IJobPersistence myJobInstancePersister;
	@Mock
	private JobDefinitionRegistry myJobDefinitionRegistry;
	@Mock
	private IJobMaintenanceService myJobMaintenanceService;
	private IHapiTransactionService myTransactionService = new NonTransactionalHapiTransactionService();
	@Captor
	private ArgumentCaptor<StepExecutionDetails<TestJobParameters, VoidModel>> myStep1ExecutionDetailsCaptor;
	@Captor
	private ArgumentCaptor<StepExecutionDetails<TestJobParameters, TestJobStep2InputType>> myStep2ExecutionDetailsCaptor;
	@Captor
	private ArgumentCaptor<StepExecutionDetails<TestJobParameters, TestJobStep3InputType>> myStep3ExecutionDetailsCaptor;
	@Captor
	private ArgumentCaptor<JobWorkNotification> myJobWorkNotificationCaptor;
	@Captor
	private ArgumentCaptor<JobInstance> myJobInstanceCaptor;

	@BeforeEach
	public void beforeEach() {
		// The code refactored to keep the same functionality,
		// but in this service (so it's a real service here!)
		WorkChunkProcessor jobStepExecutorSvc = new WorkChunkProcessor(myJobInstancePersister, myBatchJobSender);
		mySvc = new JobCoordinatorImpl(myBatchJobSender, myWorkChannelReceiver, myJobInstancePersister, myJobDefinitionRegistry, jobStepExecutorSvc, myJobMaintenanceService);
	}

	@AfterEach
	public void afterEach() {
		// TODO KHS add verify for the other mocks
		verifyNoMoreInteractions(myJobMaintenanceService);
	}

	@Test
	public void testCancelInstance() {

		// Execute

		mySvc.cancelInstance(INSTANCE_ID);

		// Verify

		verify(myJobInstancePersister, times(1)).cancelInstance(eq(INSTANCE_ID));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testPerformStep_FirstStep() {

		// Setup

		setupMocks(createJobDefinition(), createWorkChunkStep1());
		when(myStep1Worker.run(any(), any())).thenAnswer(t -> {
			IJobDataSink<TestJobStep2InputType> sink = t.getArgument(1, IJobDataSink.class);
			sink.accept(new TestJobStep2InputType("data value 1a", "data value 2a"));
			sink.accept(new TestJobStep2InputType("data value 1b", "data value 2b"));
			return new RunOutcome(50);
		});
		mySvc.start();

		// Execute

		myWorkChannelReceiver.send(new JobWorkNotificationJsonMessage(createWorkNotification(STEP_1)));

		// Verify

		verify(myStep1Worker, times(1)).run(myStep1ExecutionDetailsCaptor.capture(), any());
		TestJobParameters params = myStep1ExecutionDetailsCaptor.getValue().getParameters();
		assertEquals(PARAM_1_VALUE, params.getParam1());
		assertEquals(PARAM_2_VALUE, params.getParam2());
		assertEquals(PASSWORD_VALUE, params.getPassword());

		verify(myJobInstancePersister, times(1)).markWorkChunkAsCompletedAndClearData(eq(INSTANCE_ID), any(), eq(50));
		verify(myJobInstancePersister, times(0)).fetchWorkChunksWithoutData(any(), anyInt(), anyInt());
		verify(myBatchJobSender, times(2)).sendWorkChannelMessage(any());
	}

	private void setupMocks(JobDefinition<TestJobParameters> theJobDefinition, WorkChunk theWorkChunk) {
		mockJobRegistry(theJobDefinition);
		when(myJobInstancePersister.fetchInstance(eq(INSTANCE_ID))).thenReturn(Optional.of(createInstance()));
		when(myJobInstancePersister.fetchWorkChunkSetStartTimeAndMarkInProgress(eq(CHUNK_ID))).thenReturn(Optional.of(theWorkChunk));
	}

	private void mockJobRegistry(JobDefinition<TestJobParameters> theJobDefinition) {
		doReturn(theJobDefinition)
			.when(myJobDefinitionRegistry).getJobDefinitionOrThrowException(eq(JOB_DEFINITION_ID), eq(1));
	}

	@Test
	public void startInstance_usingExistingCache_returnsExistingIncompleteJobFirst() {
		// setup
		String completedInstanceId = "completed-id";
		String inProgressInstanceId = "someId";
		JobInstanceStartRequest startRequest = new JobInstanceStartRequest();
		startRequest.setJobDefinitionId(JOB_DEFINITION_ID);
		startRequest.setUseCache(true);
		startRequest.setParameters("parameters");

		JobDefinition<?> def = createJobDefinition();

		JobInstance existingInProgInstance = createInstance();
		existingInProgInstance.setInstanceId(inProgressInstanceId);
		existingInProgInstance.setStatus(StatusEnum.IN_PROGRESS);

		JobInstance existingCompletedInstance = createInstance();
		existingCompletedInstance.setStatus(StatusEnum.COMPLETED);
		existingCompletedInstance.setInstanceId(completedInstanceId);

		// when
		when(myJobDefinitionRegistry.getLatestJobDefinition(eq(JOB_DEFINITION_ID)))
			.thenReturn(Optional.of(def));
		when(myJobInstancePersister.fetchInstances(any(FetchJobInstancesRequest.class), anyInt(), anyInt()))
			.thenReturn(Arrays.asList(existingInProgInstance));

		// test
		Batch2JobStartResponse startResponse = mySvc.startInstance(startRequest);

		// verify
		assertEquals(inProgressInstanceId, startResponse.getInstanceId()); // make sure it's the completed one
		assertTrue(startResponse.isUsesCachedResult());
		ArgumentCaptor<FetchJobInstancesRequest> requestArgumentCaptor = ArgumentCaptor.forClass(FetchJobInstancesRequest.class);
		verify(myJobInstancePersister)
			.fetchInstances(requestArgumentCaptor.capture(), anyInt(), anyInt());
		FetchJobInstancesRequest req = requestArgumentCaptor.getValue();
		assertEquals(2, req.getStatuses().size());
		assertTrue(
			req.getStatuses().contains(StatusEnum.IN_PROGRESS)
				&& req.getStatuses().contains(StatusEnum.QUEUED)
		);
	}

	/**
	 * If the first step doesn't produce any work chunks, then
	 * the instance should be marked as complete right away.
	 */
	@Test
	public void testPerformStep_FirstStep_NoWorkChunksProduced() {

		// Setup

		setupMocks(createJobDefinition(), createWorkChunkStep1());
		when(myStep1Worker.run(any(), any())).thenReturn(new RunOutcome(50));
		when(myJobInstancePersister.fetchInstance(INSTANCE_ID)).thenReturn(Optional.of(ourQueuedInstance));

		mySvc.start();

		// Execute

		myWorkChannelReceiver.send(new JobWorkNotificationJsonMessage(createWorkNotification(STEP_1)));

		// Verify

		verify(myStep1Worker, times(1)).run(myStep1ExecutionDetailsCaptor.capture(), any());
		TestJobParameters params = myStep1ExecutionDetailsCaptor.getValue().getParameters();
		assertEquals(PARAM_1_VALUE, params.getParam1());
		assertEquals(PARAM_2_VALUE, params.getParam2());
		assertEquals(PASSWORD_VALUE, params.getPassword());

		// QUEUED -> IN_PROGRESS and IN_PROGRESS -> COMPLETED
		verify(myJobInstancePersister, times(2)).updateInstance(any());
	}

	@Test
	public void testPerformStep_FirstStep_GatedExecutionMode() {

		// Setup

		JobDefinition<TestJobParameters> jobDefinition = createJobDefinition(JobDefinition.Builder::gatedExecution);
		setupMocks(jobDefinition, createWorkChunkStep1());
		Answer<RunOutcome> answer = t -> {
			IJobDataSink<TestJobStep2InputType> sink = t.getArgument(1);
			sink.accept(new TestJobStep2InputType("data value 1a", "data value 2a"));
			sink.accept(new TestJobStep2InputType("data value 1b", "data value 2b"));
			return new RunOutcome(50);
		};
		when(myStep1Worker.run(any(), any())).thenAnswer(answer);
		mySvc.start();

		// Execute

		myWorkChannelReceiver.send(new JobWorkNotificationJsonMessage(createWorkNotification(STEP_1)));

		// Verify

		verify(myStep1Worker, times(1)).run(myStep1ExecutionDetailsCaptor.capture(), any());
		TestJobParameters params = myStep1ExecutionDetailsCaptor.getValue().getParameters();
		assertEquals(PARAM_1_VALUE, params.getParam1());
		assertEquals(PARAM_2_VALUE, params.getParam2());
		assertEquals(PASSWORD_VALUE, params.getPassword());

		verify(myJobInstancePersister, times(1)).markWorkChunkAsCompletedAndClearData(eq(INSTANCE_ID), any(), eq(50));
		verify(myBatchJobSender, times(0)).sendWorkChannelMessage(any());
	}

	@Test
	public void testPerformStep_SecondStep() {

		// Setup

		when(myJobInstancePersister.fetchWorkChunkSetStartTimeAndMarkInProgress(eq(CHUNK_ID))).thenReturn(Optional.of(createWorkChunk(STEP_2, new TestJobStep2InputType(DATA_1_VALUE, DATA_2_VALUE))));
		doReturn(createJobDefinition()).when(myJobDefinitionRegistry).getJobDefinitionOrThrowException(eq(JOB_DEFINITION_ID), eq(1));
		when(myJobInstancePersister.fetchInstance(eq(INSTANCE_ID))).thenReturn(Optional.of(createInstance()));
		when(myStep2Worker.run(any(), any())).thenReturn(new RunOutcome(50));
		mySvc.start();

		// Execute

		myWorkChannelReceiver.send(new JobWorkNotificationJsonMessage(createWorkNotification(STEP_2)));

		// Verify

		verify(myStep2Worker, times(1)).run(myStep2ExecutionDetailsCaptor.capture(), any());
		TestJobParameters params = myStep2ExecutionDetailsCaptor.getValue().getParameters();
		assertEquals(PARAM_1_VALUE, params.getParam1());
		assertEquals(PARAM_2_VALUE, params.getParam2());
		assertEquals(PASSWORD_VALUE, params.getPassword());

		verify(myJobInstancePersister, times(1)).markWorkChunkAsCompletedAndClearData(eq(INSTANCE_ID), eq(CHUNK_ID), eq(50));
	}

	@Test
	public void testPerformStep_SecondStep_WorkerFailure() {

		// Setup
		AtomicInteger counter = new AtomicInteger();
		doReturn(createJobDefinition()).when(myJobDefinitionRegistry).getJobDefinitionOrThrowException(eq(JOB_DEFINITION_ID), eq(1));
		when(myJobInstancePersister.fetchWorkChunkSetStartTimeAndMarkInProgress(eq(CHUNK_ID))).thenReturn(Optional.of(createWorkChunk(STEP_2, new TestJobStep2InputType(DATA_1_VALUE, DATA_2_VALUE))));
		when(myJobInstancePersister.fetchInstance(eq(INSTANCE_ID))).thenReturn(Optional.of(createInstance()));
		when(myStep2Worker.run(any(), any())).thenAnswer(t -> {
			if (counter.getAndIncrement() == 0) {
				throw new NullPointerException("This is an error message");
			} else {
				return RunOutcome.SUCCESS;
			}
		});
		mySvc.start();

		// Execute

		myWorkChannelReceiver.send(new JobWorkNotificationJsonMessage(createWorkNotification(STEP_2)));

		// Verify

		verify(myStep2Worker, times(2)).run(myStep2ExecutionDetailsCaptor.capture(), any());
		TestJobParameters params = myStep2ExecutionDetailsCaptor.getAllValues().get(0).getParameters();
		assertEquals(PARAM_1_VALUE, params.getParam1());
		assertEquals(PARAM_2_VALUE, params.getParam2());
		assertEquals(PASSWORD_VALUE, params.getPassword());

		ArgumentCaptor<MarkWorkChunkAsErrorRequest> parametersArgumentCaptor = ArgumentCaptor.forClass(MarkWorkChunkAsErrorRequest.class);
		verify(myJobInstancePersister, times(1)).markWorkChunkAsErroredAndIncrementErrorCount(parametersArgumentCaptor.capture());
		MarkWorkChunkAsErrorRequest capturedParams = parametersArgumentCaptor.getValue();
		assertEquals(CHUNK_ID, capturedParams.getChunkId());
		assertEquals("This is an error message", capturedParams.getErrorMsg());

		verify(myJobInstancePersister, times(1)).markWorkChunkAsCompletedAndClearData(eq(INSTANCE_ID), eq(CHUNK_ID), eq(0));

	}

	@Test
	public void testPerformStep_SecondStep_WorkerReportsRecoveredErrors() {

		// Setup

		when(myJobInstancePersister.fetchWorkChunkSetStartTimeAndMarkInProgress(eq(CHUNK_ID))).thenReturn(Optional.of(createWorkChunk(STEP_2, new TestJobStep2InputType(DATA_1_VALUE, DATA_2_VALUE))));
		doReturn(createJobDefinition()).when(myJobDefinitionRegistry).getJobDefinitionOrThrowException(eq(JOB_DEFINITION_ID), eq(1));
		when(myJobInstancePersister.fetchInstance(eq(INSTANCE_ID))).thenReturn(Optional.of(createInstance()));
		when(myStep2Worker.run(any(), any())).thenAnswer(t -> {
			IJobDataSink<?> sink = t.getArgument(1, IJobDataSink.class);
			sink.recoveredError("Error message 1");
			sink.recoveredError("Error message 2");
			return new RunOutcome(50);
		});
		mySvc.start();

		// Execute

		myWorkChannelReceiver.send(new JobWorkNotificationJsonMessage(createWorkNotification(STEP_2)));

		// Verify

		verify(myStep2Worker, times(1)).run(myStep2ExecutionDetailsCaptor.capture(), any());
		TestJobParameters params = myStep2ExecutionDetailsCaptor.getValue().getParameters();
		assertEquals(PARAM_1_VALUE, params.getParam1());
		assertEquals(PARAM_2_VALUE, params.getParam2());
		assertEquals(PASSWORD_VALUE, params.getPassword());

		verify(myJobInstancePersister, times(1)).incrementWorkChunkErrorCount(eq(CHUNK_ID), eq(2));
		verify(myJobInstancePersister, times(1)).markWorkChunkAsCompletedAndClearData(eq(INSTANCE_ID), eq(CHUNK_ID), eq(50));
	}

	@Test
	public void testPerformStep_FinalStep() {

		// Setup

		when(myJobInstancePersister.fetchWorkChunkSetStartTimeAndMarkInProgress(eq(CHUNK_ID))).thenReturn(Optional.of(createWorkChunkStep3()));
		doReturn(createJobDefinition()).when(myJobDefinitionRegistry).getJobDefinitionOrThrowException(eq(JOB_DEFINITION_ID), eq(1));
		when(myJobInstancePersister.fetchInstance(eq(INSTANCE_ID))).thenReturn(Optional.of(createInstance()));
		when(myStep3Worker.run(any(), any())).thenReturn(new RunOutcome(50));
		mySvc.start();

		// Execute

		myWorkChannelReceiver.send(new JobWorkNotificationJsonMessage(createWorkNotification(STEP_3)));

		// Verify

		verify(myStep3Worker, times(1)).run(myStep3ExecutionDetailsCaptor.capture(), any());
		TestJobParameters params = myStep3ExecutionDetailsCaptor.getValue().getParameters();
		assertEquals(PARAM_1_VALUE, params.getParam1());
		assertEquals(PARAM_2_VALUE, params.getParam2());
		assertEquals(PASSWORD_VALUE, params.getPassword());

		verify(myJobInstancePersister, times(1)).markWorkChunkAsCompletedAndClearData(eq(INSTANCE_ID), eq(CHUNK_ID), eq(50));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testPerformStep_FinalStep_PreventChunkWriting() {

		// Setup

		when(myJobInstancePersister.fetchWorkChunkSetStartTimeAndMarkInProgress(eq(CHUNK_ID))).thenReturn(Optional.of(createWorkChunk(STEP_3, new TestJobStep3InputType().setData3(DATA_3_VALUE).setData4(DATA_4_VALUE))));
		doReturn(createJobDefinition()).when(myJobDefinitionRegistry).getJobDefinitionOrThrowException(eq(JOB_DEFINITION_ID), eq(1));
		when(myJobInstancePersister.fetchInstance(eq(INSTANCE_ID))).thenReturn(Optional.of(createInstance()));
		when(myStep3Worker.run(any(), any())).thenAnswer(t -> {
			IJobDataSink<VoidModel> sink = t.getArgument(1, IJobDataSink.class);
			sink.accept(new VoidModel());
			return new RunOutcome(50);
		});
		mySvc.start();

		// Execute

		myWorkChannelReceiver.send(new JobWorkNotificationJsonMessage(createWorkNotification(STEP_3)));

		// Verify

		verify(myStep3Worker, times(1)).run(myStep3ExecutionDetailsCaptor.capture(), any());
		verify(myJobInstancePersister, times(1)).markWorkChunkAsFailed(eq(CHUNK_ID), any());
	}

	@Test
	public void testPerformStep_DefinitionNotKnown() {

		// Setup

		String exceptionMessage = "badbadnotgood";
		when(myJobDefinitionRegistry.getJobDefinitionOrThrowException(eq(JOB_DEFINITION_ID), eq(1))).thenThrow(new JobExecutionFailedException(exceptionMessage));
		when(myJobInstancePersister.fetchWorkChunkSetStartTimeAndMarkInProgress(eq(CHUNK_ID))).thenReturn(Optional.of(createWorkChunkStep2()));
		mySvc.start();

		// Execute

		try {
			myWorkChannelReceiver.send(new JobWorkNotificationJsonMessage(createWorkNotification(STEP_2)));
			fail();
		} catch (MessageDeliveryException e) {

			// Verify
			assertEquals(exceptionMessage, e.getMostSpecificCause().getMessage());
		}

	}

	/**
	 * If a notification is received for an unknown chunk, that probably means
	 * it has been deleted from the database, so we should log an error and nothing
	 * else.
	 */
	@Test
	public void testPerformStep_ChunkNotKnown() {

		// Setup

		when(myJobInstancePersister.fetchWorkChunkSetStartTimeAndMarkInProgress(eq(CHUNK_ID))).thenReturn(Optional.empty());
		mySvc.start();

		// Execute

		myWorkChannelReceiver.send(new JobWorkNotificationJsonMessage(createWorkNotification(STEP_2)));

		// Verify
		verifyNoMoreInteractions(myStep1Worker);
		verifyNoMoreInteractions(myStep2Worker);
		verifyNoMoreInteractions(myStep3Worker);

	}

	/**
	 * If a notification is received for a chunk that should have data but doesn't, we can just ignore that
	 * (just caused by double delivery of a chunk notification message)
	 */
	@Test
	public void testPerformStep_ChunkAlreadyComplete() {

		// Setup

		WorkChunk chunk = createWorkChunkStep2();
		chunk.setData((String) null);
		setupMocks(createJobDefinition(), chunk);
		mySvc.start();

		// Execute

		myWorkChannelReceiver.send(new JobWorkNotificationJsonMessage(createWorkNotification(STEP_2)));

		// Verify
		verifyNoMoreInteractions(myStep1Worker);
		verifyNoMoreInteractions(myStep2Worker);
		verifyNoMoreInteractions(myStep3Worker);

	}

	@Test
	public void testStartInstance() {

		// Setup

		when(myJobDefinitionRegistry.getLatestJobDefinition(eq(JOB_DEFINITION_ID)))
			.thenReturn(Optional.of(createJobDefinition()));
		when(myJobInstancePersister.storeNewInstance(any()))
			.thenReturn(INSTANCE_ID).thenReturn(INSTANCE_ID);

		// Execute

		JobInstanceStartRequest startRequest = new JobInstanceStartRequest();
		startRequest.setJobDefinitionId(JOB_DEFINITION_ID);
		startRequest.setParameters(new TestJobParameters().setParam1(PARAM_1_VALUE).setParam2(PARAM_2_VALUE).setPassword(PASSWORD_VALUE));
		mySvc.startInstance(startRequest);

		// Verify

		verify(myJobInstancePersister, times(1))
			.storeNewInstance(myJobInstanceCaptor.capture());
		assertNull(myJobInstanceCaptor.getValue().getInstanceId());
		assertEquals(JOB_DEFINITION_ID, myJobInstanceCaptor.getValue().getJobDefinitionId());
		assertEquals(1, myJobInstanceCaptor.getValue().getJobDefinitionVersion());
		assertEquals(PARAM_1_VALUE, myJobInstanceCaptor.getValue().getParameters(TestJobParameters.class).getParam1());
		assertEquals(PARAM_2_VALUE, myJobInstanceCaptor.getValue().getParameters(TestJobParameters.class).getParam2());
		assertEquals(PASSWORD_VALUE, myJobInstanceCaptor.getValue().getParameters(TestJobParameters.class).getPassword());
		assertEquals(StatusEnum.QUEUED, myJobInstanceCaptor.getValue().getStatus());

		verify(myBatchJobSender, times(1)).sendWorkChannelMessage(myJobWorkNotificationCaptor.capture());
		assertNull(myJobWorkNotificationCaptor.getAllValues().get(0).getChunkId());
		assertEquals(JOB_DEFINITION_ID, myJobWorkNotificationCaptor.getAllValues().get(0).getJobDefinitionId());
		assertEquals(1, myJobWorkNotificationCaptor.getAllValues().get(0).getJobDefinitionVersion());
		assertEquals(STEP_1, myJobWorkNotificationCaptor.getAllValues().get(0).getTargetStepId());

		BatchWorkChunk expectedWorkChunk = new BatchWorkChunk(JOB_DEFINITION_ID, 1, STEP_1, INSTANCE_ID, 0, null);
		verify(myJobInstancePersister, times(1)).storeWorkChunk(eq(expectedWorkChunk));

		verifyNoMoreInteractions(myJobInstancePersister);
		verifyNoMoreInteractions(myStep1Worker);
		verifyNoMoreInteractions(myStep2Worker);
		verifyNoMoreInteractions(myStep3Worker);
	}

	@Test
	public void testStartInstance_InvalidParameters() {

		// Setup

		when(myJobDefinitionRegistry.getLatestJobDefinition(eq(JOB_DEFINITION_ID))).thenReturn(Optional.of(createJobDefinition()));

		// Execute

		JobInstanceStartRequest startRequest = new JobInstanceStartRequest();
		startRequest.setJobDefinitionId(JOB_DEFINITION_ID);
		startRequest.setParameters(new TestJobParameters().setParam2("aa"));

		try {
			mySvc.startInstance(startRequest);
			fail();
		} catch (InvalidRequestException e) {

			// Verify
			String expected = """
				HAPI-2039: Failed to validate parameters for job of type JOB_DEFINITION_ID:\s
				 * myParam1 - must not be blank
				 * myParam2 - length must be between 5 and 100""";
			assertEquals(expected, e.getMessage());

		}
	}

	@Test
	public void testStartInstance_InvalidParameters_UsingProgrammaticApi() {

		// Setup

		IJobParametersValidator<TestJobParameters> v = p -> {
			if (p.getParam1().equals("bad")) {
				return Lists.newArrayList("Bad Parameter Value", "Bad Parameter Value 2");
			}
			return null;
		};
		JobDefinition<?> jobDefinition = createJobDefinition(t -> t.setParametersValidator(v));
		when(myJobDefinitionRegistry.getLatestJobDefinition(eq(JOB_DEFINITION_ID))).thenReturn(Optional.of(jobDefinition));

		// Execute

		JobInstanceStartRequest startRequest = new JobInstanceStartRequest();
		startRequest.setJobDefinitionId(JOB_DEFINITION_ID);
		startRequest.setParameters(new TestJobParameters().setParam1("bad").setParam2("aa"));

		try {
			mySvc.startInstance(startRequest);
			fail();
		} catch (InvalidRequestException e) {

			// Verify
			String expected = """
				HAPI-2039: Failed to validate parameters for job of type JOB_DEFINITION_ID:\s
				 * myParam2 - length must be between 5 and 100
				 * Bad Parameter Value
				 * Bad Parameter Value 2""";
			assertEquals(expected, e.getMessage());

		}
	}

	@Nonnull
	private JobWorkNotification createWorkNotification(String theStepId) {
		JobWorkNotification payload = new JobWorkNotification();
		payload.setJobDefinitionId(JOB_DEFINITION_ID);
		payload.setJobDefinitionVersion(1);
		payload.setInstanceId(INSTANCE_ID);
		payload.setChunkId(BaseBatch2Test.CHUNK_ID);
		payload.setTargetStepId(theStepId);
		return payload;
	}

	@Nonnull
	public static WorkChunk createWorkChunk(String theTargetStepId, IModelJson theData) {
		return createWorkChunk(JOB_DEFINITION_ID, theTargetStepId, theData);
	}

	static WorkChunk createWorkChunk(String theJobId, String theTargetStepId, IModelJson theData) {
		return new WorkChunk()
			.setId(CHUNK_ID)
			.setJobDefinitionId(theJobId)
			.setJobDefinitionVersion(1)
			.setTargetStepId(theTargetStepId)
			.setData(theData)
			.setStatus(StatusEnum.IN_PROGRESS)
			.setInstanceId(INSTANCE_ID);
	}

	@Nonnull
	public static WorkChunk createWorkChunkStep1() {
		return createWorkChunk(STEP_1, null);
	}

	public static WorkChunk createWorkChunkStep2() {
		return createWorkChunkStep2(JOB_DEFINITION_ID);
	}

	@Nonnull
	static WorkChunk createWorkChunkStep2(String theJobId) {
		return createWorkChunk(theJobId, STEP_2, new TestJobStep2InputType(DATA_1_VALUE, DATA_2_VALUE));
	}

	@Nonnull
	public static WorkChunk createWorkChunkStep3() {
		return createWorkChunkStep3(JOB_DEFINITION_ID);
	}

	@Nonnull
	static WorkChunk createWorkChunkStep3(String theJobId) {
		return createWorkChunk(theJobId, STEP_3, new TestJobStep3InputType().setData3(DATA_3_VALUE).setData4(DATA_4_VALUE));
	}
}
