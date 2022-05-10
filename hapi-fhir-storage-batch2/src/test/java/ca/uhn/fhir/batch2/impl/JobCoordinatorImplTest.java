package ca.uhn.fhir.batch2.impl;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobParametersValidator;
import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.batch2.model.JobWorkNotification;
import ca.uhn.fhir.batch2.model.JobWorkNotificationJsonMessage;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelReceiver;
import ca.uhn.fhir.jpa.subscription.channel.impl.LinkedBlockingChannel;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.MessageDeliveryException;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.MethodName.class)
public class JobCoordinatorImplTest extends BaseBatch2Test {

	private static final Logger ourLog = LoggerFactory.getLogger(JobCoordinatorImplTest.class);
	private final IChannelReceiver myWorkChannelReceiver = LinkedBlockingChannel.newSynchronous("receiver");
	private JobCoordinatorImpl mySvc;
	@Mock
	private BatchJobSender myBatchJobSender;
	@Mock
	private IJobPersistence myJobInstancePersister;
	@Mock
	private JobDefinitionRegistry myJobDefinitionRegistry;
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
	@Captor
	private ArgumentCaptor<String> myErrorMessageCaptor;

	@BeforeEach
	public void beforeEach() {
		mySvc = new JobCoordinatorImpl(myBatchJobSender, myWorkChannelReceiver, myJobInstancePersister, myJobDefinitionRegistry);
	}

	@Test
	public void testCancelInstance() {

		// Execute

		mySvc.cancelInstance(INSTANCE_ID);

		// Verify

		verify(myJobInstancePersister, times(1)).cancelInstance(eq(INSTANCE_ID));
	}

	@Test
	public void testFetchInstance_PasswordsRedacted() {

		// Setup

		JobDefinition<?> definition = createJobDefinition();
		JobInstance instance = createInstance();

		when(myJobDefinitionRegistry.getJobDefinition(eq(JOB_DEFINITION_ID), eq(1))).thenReturn(Optional.of(definition));
		when(myJobInstancePersister.fetchInstance(eq(INSTANCE_ID))).thenReturn(Optional.of(instance));

		// Execute

		JobInstance outcome = mySvc.getInstance(INSTANCE_ID);
		ourLog.info("Job instance: {}", outcome);
		ourLog.info("Parameters: {}", outcome.getParameters());
		assertEquals(PARAM_1_VALUE, outcome.getParameters(TestJobParameters.class).getParam1());
		assertEquals(PARAM_2_VALUE, outcome.getParameters(TestJobParameters.class).getParam2());
		assertEquals(null, outcome.getParameters(TestJobParameters.class).getPassword());

	}

	@Test
	public void testFetchInstances() {

		// Setup

		when(myJobDefinitionRegistry.getJobDefinition(eq(JOB_DEFINITION_ID), eq(1))).thenReturn(Optional.of(createJobDefinition()));
		when(myJobInstancePersister.fetchInstances(eq(100), eq(0))).thenReturn(Lists.newArrayList(createInstance()));

		// Execute

		List<JobInstance> outcome = mySvc.getInstances(100, 0);

		// Verify

		assertEquals(1, outcome.size());

	}

	@SuppressWarnings("unchecked")
	@Test
	public void testPerformStep_FirstStep() {

		// Setup

		when(myJobDefinitionRegistry.getJobDefinition(eq(JOB_DEFINITION_ID), eq(1))).thenReturn(Optional.of(createJobDefinition()));
		when(myJobInstancePersister.fetchInstanceAndMarkInProgress(eq(INSTANCE_ID))).thenReturn(Optional.of(createInstance()));
		when(myJobInstancePersister.fetchWorkChunkSetStartTimeAndMarkInProgress(eq(CHUNK_ID))).thenReturn(Optional.of(createWorkChunkStep1()));
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

		verify(myJobInstancePersister, times(1)).markWorkChunkAsCompletedAndClearData(any(), eq(50));
		verify(myJobInstancePersister, times(0)).fetchWorkChunksWithoutData(any(), anyInt(), anyInt());
		verify(myBatchJobSender, times(2)).sendWorkChannelMessage(any());
	}

	/**
	 * If the first step doesn't produce any work chunks, then
	 * the instance should be marked as complete right away.
	 */
	@Test
	public void testPerformStep_FirstStep_NoWorkChunksProduced() {

		// Setup

		when(myJobDefinitionRegistry.getJobDefinition(eq(JOB_DEFINITION_ID), eq(1))).thenReturn(Optional.of(createJobDefinition()));
		when(myJobInstancePersister.fetchInstanceAndMarkInProgress(eq(INSTANCE_ID))).thenReturn(Optional.of(createInstance()));
		when(myJobInstancePersister.fetchWorkChunkSetStartTimeAndMarkInProgress(eq(CHUNK_ID))).thenReturn(Optional.of(createWorkChunkStep1()));
		when(myStep1Worker.run(any(), any())).thenReturn(new RunOutcome(50));
		mySvc.start();

		// Execute

		myWorkChannelReceiver.send(new JobWorkNotificationJsonMessage(createWorkNotification(STEP_1)));

		// Verify

		verify(myStep1Worker, times(1)).run(myStep1ExecutionDetailsCaptor.capture(), any());
		TestJobParameters params = myStep1ExecutionDetailsCaptor.getValue().getParameters();
		assertEquals(PARAM_1_VALUE, params.getParam1());
		assertEquals(PARAM_2_VALUE, params.getParam2());
		assertEquals(PASSWORD_VALUE, params.getPassword());

		verify(myJobInstancePersister, times(1)).markInstanceAsCompleted(eq(INSTANCE_ID));
	}

	@Test
	public void testPerformStep_FirstStep_GatedExecutionMode() {

		// Setup

		JobDefinition<TestJobParameters> jobDefinition = createJobDefinition(
			t -> t.gatedExecution()
		);
		when(myJobDefinitionRegistry.getJobDefinition(eq(JOB_DEFINITION_ID), eq(1))).thenReturn(Optional.of(jobDefinition));
		when(myJobInstancePersister.fetchInstanceAndMarkInProgress(eq(INSTANCE_ID))).thenReturn(Optional.of(createInstance()));
		when(myJobInstancePersister.fetchWorkChunkSetStartTimeAndMarkInProgress(eq(CHUNK_ID))).thenReturn(Optional.of(createWorkChunkStep1()));
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

		verify(myJobInstancePersister, times(1)).markWorkChunkAsCompletedAndClearData(any(), eq(50));
		verify(myBatchJobSender, times(0)).sendWorkChannelMessage(any());
	}

	@Test
	public void testPerformStep_SecondStep() {

		// Setup

		when(myJobInstancePersister.fetchWorkChunkSetStartTimeAndMarkInProgress(eq(CHUNK_ID))).thenReturn(Optional.of(createWorkChunk(STEP_2, new TestJobStep2InputType(DATA_1_VALUE, DATA_2_VALUE))));
		when(myJobDefinitionRegistry.getJobDefinition(eq(JOB_DEFINITION_ID), eq(1))).thenReturn(Optional.of(createJobDefinition()));
		when(myJobInstancePersister.fetchInstanceAndMarkInProgress(eq(INSTANCE_ID))).thenReturn(Optional.of(createInstance()));
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

		verify(myJobInstancePersister, times(1)).markWorkChunkAsCompletedAndClearData(eq(CHUNK_ID), eq(50));
	}

	@Test
	public void testPerformStep_SecondStep_WorkerFailure() {

		// Setup

		when(myJobDefinitionRegistry.getJobDefinition(eq(JOB_DEFINITION_ID), eq(1))).thenReturn(Optional.of(createJobDefinition()));
		when(myJobInstancePersister.fetchWorkChunkSetStartTimeAndMarkInProgress(eq(CHUNK_ID))).thenReturn(Optional.of(createWorkChunk(STEP_2, new TestJobStep2InputType(DATA_1_VALUE, DATA_2_VALUE))));
		when(myJobInstancePersister.fetchInstanceAndMarkInProgress(eq(INSTANCE_ID))).thenReturn(Optional.of(createInstance()));
		when(myStep2Worker.run(any(), any())).thenThrow(new NullPointerException("This is an error message"));
		mySvc.start();

		// Execute

		try {
			myWorkChannelReceiver.send(new JobWorkNotificationJsonMessage(createWorkNotification(STEP_2)));
			fail();
		} catch (MessageDeliveryException e) {
			assertEquals("This is an error message", e.getMostSpecificCause().getMessage());
		}

		// Verify

		verify(myStep2Worker, times(1)).run(myStep2ExecutionDetailsCaptor.capture(), any());
		TestJobParameters params = myStep2ExecutionDetailsCaptor.getValue().getParameters();
		assertEquals(PARAM_1_VALUE, params.getParam1());
		assertEquals(PARAM_2_VALUE, params.getParam2());
		assertEquals(PASSWORD_VALUE, params.getPassword());

		verify(myJobInstancePersister, times(1)).markWorkChunkAsErroredAndIncrementErrorCount(eq(CHUNK_ID), myErrorMessageCaptor.capture());
		assertEquals("java.lang.NullPointerException: This is an error message", myErrorMessageCaptor.getValue());
	}

	@Test
	public void testPerformStep_SecondStep_WorkerReportsRecoveredErrors() {

		// Setup

		when(myJobInstancePersister.fetchWorkChunkSetStartTimeAndMarkInProgress(eq(CHUNK_ID))).thenReturn(Optional.of(createWorkChunk(STEP_2, new TestJobStep2InputType(DATA_1_VALUE, DATA_2_VALUE))));
		when(myJobDefinitionRegistry.getJobDefinition(eq(JOB_DEFINITION_ID), eq(1))).thenReturn(Optional.of(createJobDefinition()));
		when(myJobInstancePersister.fetchInstanceAndMarkInProgress(eq(INSTANCE_ID))).thenReturn(Optional.of(createInstance()));
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
		verify(myJobInstancePersister, times(1)).markWorkChunkAsCompletedAndClearData(eq(CHUNK_ID), eq(50));

	}

	@Test
	public void testPerformStep_FinalStep() {

		// Setup

		when(myJobInstancePersister.fetchWorkChunkSetStartTimeAndMarkInProgress(eq(CHUNK_ID))).thenReturn(Optional.of(createWorkChunkStep3()));
		when(myJobDefinitionRegistry.getJobDefinition(eq(JOB_DEFINITION_ID), eq(1))).thenReturn(Optional.of(createJobDefinition()));
		when(myJobInstancePersister.fetchInstanceAndMarkInProgress(eq(INSTANCE_ID))).thenReturn(Optional.of(createInstance()));
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

		verify(myJobInstancePersister, times(1)).markWorkChunkAsCompletedAndClearData(eq(CHUNK_ID), eq(50));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testPerformStep_FinalStep_PreventChunkWriting() {

		// Setup

		when(myJobInstancePersister.fetchWorkChunkSetStartTimeAndMarkInProgress(eq(CHUNK_ID))).thenReturn(Optional.of(createWorkChunk(STEP_3, new TestJobStep3InputType().setData3(DATA_3_VALUE).setData4(DATA_4_VALUE))));
		when(myJobDefinitionRegistry.getJobDefinition(eq(JOB_DEFINITION_ID), eq(1))).thenReturn(Optional.of(createJobDefinition()));
		when(myJobInstancePersister.fetchInstanceAndMarkInProgress(eq(INSTANCE_ID))).thenReturn(Optional.of(createInstance()));
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

		when(myJobDefinitionRegistry.getJobDefinition(eq(JOB_DEFINITION_ID), eq(1))).thenReturn(Optional.empty());
		when(myJobInstancePersister.fetchWorkChunkSetStartTimeAndMarkInProgress(eq(CHUNK_ID))).thenReturn(Optional.of(createWorkChunkStep2()));
		mySvc.start();

		// Execute

		try {
			myWorkChannelReceiver.send(new JobWorkNotificationJsonMessage(createWorkNotification(STEP_2)));
			fail();
		} catch (MessageDeliveryException e) {

			// Verify
			assertEquals("HAPI-2043: Unknown job definition ID[JOB_DEFINITION_ID] version[1]", e.getMostSpecificCause().getMessage());
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

	@Test
	public void testStartInstance() {

		// Setup

		when(myJobDefinitionRegistry.getLatestJobDefinition(eq(JOB_DEFINITION_ID))).thenReturn(Optional.of(createJobDefinition()));
		when(myJobInstancePersister.storeNewInstance(any())).thenReturn(INSTANCE_ID).thenReturn(INSTANCE_ID);

		// Execute

		JobInstanceStartRequest startRequest = new JobInstanceStartRequest();
		startRequest.setJobDefinitionId(JOB_DEFINITION_ID);
		startRequest.setParameters(new TestJobParameters().setParam1(PARAM_1_VALUE).setParam2(PARAM_2_VALUE).setPassword(PASSWORD_VALUE));
		mySvc.startInstance(startRequest);

		// Verify

		verify(myJobInstancePersister, times(1)).storeNewInstance(myJobInstanceCaptor.capture());
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
	static WorkChunk createWorkChunk(String theTargetStepId, IModelJson theData) {
		return new WorkChunk()
			.setId(CHUNK_ID)
			.setJobDefinitionId(JOB_DEFINITION_ID)
			.setJobDefinitionVersion(1)
			.setTargetStepId(theTargetStepId)
			.setData(theData)
			.setStatus(StatusEnum.IN_PROGRESS)
			.setInstanceId(INSTANCE_ID);
	}

	@Nonnull
	static WorkChunk createWorkChunkStep1() {
		return createWorkChunk(STEP_1, null);
	}

	@Nonnull
	static WorkChunk createWorkChunkStep2() {
		return createWorkChunk(STEP_2, new TestJobStep2InputType(DATA_1_VALUE, DATA_2_VALUE));
	}

	@Nonnull
	static WorkChunk createWorkChunkStep3() {
		return createWorkChunk(STEP_3, new TestJobStep3InputType().setData3(DATA_3_VALUE).setData4(DATA_4_VALUE));
	}

}
