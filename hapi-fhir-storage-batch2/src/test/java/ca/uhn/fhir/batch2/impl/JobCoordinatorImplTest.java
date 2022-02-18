package ca.uhn.fhir.batch2.impl;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobDefinitionParameter;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobInstanceParameter;
import ca.uhn.fhir.batch2.model.JobInstanceParameters;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.batch2.model.JobWorkNotification;
import ca.uhn.fhir.batch2.model.JobWorkNotificationJsonMessage;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.batch2.model.WorkChunkData;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelProducer;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelReceiver;
import ca.uhn.fhir.jpa.subscription.channel.impl.LinkedBlockingChannel;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Assertions;
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
import java.util.function.Consumer;

import static java.util.Collections.singletonMap;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.MethodName.class)
public class JobCoordinatorImplTest {

	public static final String JOB_DEFINITION_ID = "JOB_DEFINITION_ID";
	public static final String PARAM_1_NAME = "PARAM_1_NAME";
	public static final String PARAM_1_VALUE = "PARAM 1 VALUE";
	public static final String STEP_1 = "STEP_1";
	public static final String STEP_2 = "STEP_2";
	public static final String STEP_3 = "STEP_3";
	public static final String INSTANCE_ID = "INSTANCE-ID";
	public static final String CHUNK_ID = "CHUNK-ID";
	private static final Logger ourLog = LoggerFactory.getLogger(JobCoordinatorImplTest.class);
	private final IChannelReceiver myWorkChannelReceiver = LinkedBlockingChannel.newSynchronous("receiver");
	private JobCoordinatorImpl mySvc;
	@Mock
	private IChannelProducer myWorkChannelProducer;
	@Mock
	private IJobPersistence myJobInstancePersister;
	@Mock
	private JobDefinitionRegistry myJobDefinitionRegistry;
	@Mock
	private IJobStepWorker myStep1Worker;
	@Mock
	private IJobStepWorker myStep2Worker;
	@Mock
	private IJobStepWorker myStep3Worker;
	@Captor
	private ArgumentCaptor<StepExecutionDetails> myStepExecutionDetailsCaptor;
	@Captor
	private ArgumentCaptor<JobWorkNotificationJsonMessage> myMessageCaptor;
	@Captor
	private ArgumentCaptor<JobInstance> myJobInstanceCaptor;
	@Captor
	private ArgumentCaptor<String> myErrorMessageCaptor;

	@BeforeEach
	public void beforeEach() {
		mySvc = new JobCoordinatorImpl(myWorkChannelProducer, myWorkChannelReceiver, myJobInstancePersister, myJobDefinitionRegistry);
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

		JobDefinition definition = createJobDefinition(
			t -> t.addParameter("p2", "Second parameter", JobDefinitionParameter.ParamTypeEnum.PASSWORD, true, true)
		);
		JobInstance instance = createInstance()
			.addParameter(new JobInstanceParameter("p2", "mypassword"));

		when(myJobDefinitionRegistry.getJobDefinition(eq(JOB_DEFINITION_ID), eq(1))).thenReturn(Optional.of(definition));
		when(myJobInstancePersister.fetchInstance(eq(INSTANCE_ID))).thenReturn(Optional.of(instance));

		// Execute

		JobInstance outcome = mySvc.getInstance(INSTANCE_ID);
		ourLog.info("Job instance: {}", outcome);
		ourLog.info("Parameters: {}", outcome.getParameters());
		assertThat(outcome.getParameters(), containsInAnyOrder(
			new JobInstanceParameter("PARAM_1_NAME", "PARAM 1 VALUE"),
			new JobInstanceParameter("p2", "(***)")
		));

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

	@Test
	public void testPerformStep_FirstStep() {

		// Setup

		when(myJobDefinitionRegistry.getJobDefinition(eq(JOB_DEFINITION_ID), eq(1))).thenReturn(Optional.of(createJobDefinition()));
		when(myJobInstancePersister.fetchInstanceAndMarkInProgress(eq(INSTANCE_ID))).thenReturn(Optional.of(createInstance()));
		when(myJobInstancePersister.fetchWorkChunkSetStartTimeAndMarkInProgress(eq(CHUNK_ID))).thenReturn(Optional.of(createWorkChunk().setTargetStepId(STEP_1)));
		when(myStep1Worker.run(any(), any())).thenAnswer(t -> {
			IJobDataSink sink = t.getArgument(1, IJobDataSink.class);
			sink.accept(WorkChunkData.withData("key", "value"));
			return new IJobStepWorker.RunOutcome(50);
		});
		mySvc.start();

		// Execute

		myWorkChannelReceiver.send(new JobWorkNotificationJsonMessage(createWorkNotification(STEP_1)));

		// Verify

		verify(myStep1Worker, times(1)).run(myStepExecutionDetailsCaptor.capture(), any());
		JobInstanceParameters params = myStepExecutionDetailsCaptor.getValue().getParameters();
		assertEquals(1, params.size());
		assertEquals(1, params.getValues(PARAM_1_NAME).size());
		assertEquals(PARAM_1_VALUE, params.getValue(PARAM_1_NAME).orElseThrow(() -> new IllegalArgumentException()));

		verify(myJobInstancePersister, times(1)).markWorkChunkAsCompletedAndClearData(any(), eq(50));
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
		when(myJobInstancePersister.fetchWorkChunkSetStartTimeAndMarkInProgress(eq(CHUNK_ID))).thenReturn(Optional.of(createWorkChunk().setTargetStepId(STEP_1)));
		when(myStep1Worker.run(any(), any())).thenReturn(new IJobStepWorker.RunOutcome(50));
		mySvc.start();

		// Execute

		myWorkChannelReceiver.send(new JobWorkNotificationJsonMessage(createWorkNotification(STEP_1)));

		// Verify

		verify(myStep1Worker, times(1)).run(myStepExecutionDetailsCaptor.capture(), any());
		JobInstanceParameters params = myStepExecutionDetailsCaptor.getValue().getParameters();
		assertEquals(1, params.size());
		assertEquals(1, params.getValues(PARAM_1_NAME).size());
		assertEquals(PARAM_1_VALUE, params.getValue(PARAM_1_NAME).orElseThrow(() -> new IllegalArgumentException()));

		verify(myJobInstancePersister, times(1)).markInstanceAsCompleted(eq(INSTANCE_ID));
	}

	@Test
	public void testPerformStep_SecondStep() {

		// Setup

		when(myJobInstancePersister.fetchWorkChunkSetStartTimeAndMarkInProgress(eq(CHUNK_ID))).thenReturn(Optional.of(createWorkChunk()));
		when(myJobDefinitionRegistry.getJobDefinition(eq(JOB_DEFINITION_ID), eq(1))).thenReturn(Optional.of(createJobDefinition()));
		when(myJobInstancePersister.fetchInstanceAndMarkInProgress(eq(INSTANCE_ID))).thenReturn(Optional.of(createInstance()));
		when(myStep2Worker.run(any(), any())).thenReturn(new IJobStepWorker.RunOutcome(50));
		mySvc.start();

		// Execute

		myWorkChannelReceiver.send(new JobWorkNotificationJsonMessage(createWorkNotification(STEP_2)));

		// Verify

		verify(myStep2Worker, times(1)).run(myStepExecutionDetailsCaptor.capture(), any());
		JobInstanceParameters params = myStepExecutionDetailsCaptor.getValue().getParameters();
		assertEquals(1, params.size());
		assertEquals(1, params.getValues(PARAM_1_NAME).size());
		assertEquals(PARAM_1_VALUE, params.getValue(PARAM_1_NAME).orElseThrow(() -> new IllegalArgumentException()));

		verify(myJobInstancePersister, times(1)).markWorkChunkAsCompletedAndClearData(eq(CHUNK_ID), eq(50));
	}

	@Test
	public void testPerformStep_SecondStep_WorkerFailure() {

		// Setup

		when(myJobDefinitionRegistry.getJobDefinition(eq(JOB_DEFINITION_ID), eq(1))).thenReturn(Optional.of(createJobDefinition()));
		when(myJobInstancePersister.fetchWorkChunkSetStartTimeAndMarkInProgress(eq(CHUNK_ID))).thenReturn(Optional.of(createWorkChunk()));
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

		verify(myStep2Worker, times(1)).run(myStepExecutionDetailsCaptor.capture(), any());
		JobInstanceParameters params = myStepExecutionDetailsCaptor.getValue().getParameters();
		assertEquals(1, params.size());
		assertEquals(1, params.getValues(PARAM_1_NAME).size());
		assertEquals(PARAM_1_VALUE, params.getValue(PARAM_1_NAME).orElseThrow(() -> new IllegalArgumentException()));

		verify(myJobInstancePersister, times(1)).markWorkChunkAsErroredAndIncrementErrorCount(eq(CHUNK_ID), myErrorMessageCaptor.capture());
		assertEquals("java.lang.NullPointerException: This is an error message", myErrorMessageCaptor.getValue());
	}

	@Test
	public void testPerformStep_FinalStep() {

		// Setup

		when(myJobInstancePersister.fetchWorkChunkSetStartTimeAndMarkInProgress(eq(CHUNK_ID))).thenReturn(Optional.of(createWorkChunk().setTargetStepId(STEP_3)));
		when(myJobDefinitionRegistry.getJobDefinition(eq(JOB_DEFINITION_ID), eq(1))).thenReturn(Optional.of(createJobDefinition()));
		when(myJobInstancePersister.fetchInstanceAndMarkInProgress(eq(INSTANCE_ID))).thenReturn(Optional.of(createInstance()));
		when(myStep3Worker.run(any(), any())).thenReturn(new IJobStepWorker.RunOutcome(50));
		mySvc.start();

		// Execute

		myWorkChannelReceiver.send(new JobWorkNotificationJsonMessage(createWorkNotification(STEP_3)));

		// Verify

		verify(myStep3Worker, times(1)).run(myStepExecutionDetailsCaptor.capture(), any());
		JobInstanceParameters params = myStepExecutionDetailsCaptor.getValue().getParameters();
		assertEquals(1, params.size());
		assertEquals(1, params.getValues(PARAM_1_NAME).size());
		assertEquals(PARAM_1_VALUE, params.getValue(PARAM_1_NAME).orElseThrow(() -> new IllegalArgumentException()));

		verify(myJobInstancePersister, times(1)).markWorkChunkAsCompletedAndClearData(eq(CHUNK_ID), eq(50));
	}

	@Test
	public void testPerformStep_FinalStep_PreventChunkWriting() {

		// Setup

		when(myJobInstancePersister.fetchWorkChunkSetStartTimeAndMarkInProgress(eq(CHUNK_ID))).thenReturn(Optional.of(createWorkChunk().setTargetStepId(STEP_3)));
		when(myJobDefinitionRegistry.getJobDefinition(eq(JOB_DEFINITION_ID), eq(1))).thenReturn(Optional.of(createJobDefinition()));
		when(myJobInstancePersister.fetchInstanceAndMarkInProgress(eq(INSTANCE_ID))).thenReturn(Optional.of(createInstance()));
		when(myStep3Worker.run(any(), any())).thenAnswer(t -> {
			IJobDataSink sink = t.getArgument(1, IJobDataSink.class);
			sink.accept(WorkChunkData.withData("key", "value"));
			return new IJobStepWorker.RunOutcome(50);
		});
		mySvc.start();

		// Execute

		myWorkChannelReceiver.send(new JobWorkNotificationJsonMessage(createWorkNotification(STEP_3)));

		// Verify

		verify(myStep3Worker, times(1)).run(myStepExecutionDetailsCaptor.capture(), any());
		JobInstanceParameters params = myStepExecutionDetailsCaptor.getValue().getParameters();
		assertEquals(1, params.size());
		assertEquals(1, params.getValues(PARAM_1_NAME).size());
		assertEquals(PARAM_1_VALUE, params.getValue(PARAM_1_NAME).orElseThrow(() -> new IllegalArgumentException()));

		verify(myJobInstancePersister, times(1)).markWorkChunkAsFailed(eq(CHUNK_ID), any());
	}

	@Test
	public void testPerformStep_DefinitionNotKnown() {

		// Setup

		when(myJobDefinitionRegistry.getJobDefinition(eq(JOB_DEFINITION_ID), eq(1))).thenReturn(Optional.empty());
		when(myJobInstancePersister.fetchWorkChunkSetStartTimeAndMarkInProgress(eq(CHUNK_ID))).thenReturn(Optional.of(createWorkChunk()));
		mySvc.start();

		// Execute

		try {
			myWorkChannelReceiver.send(new JobWorkNotificationJsonMessage(createWorkNotification(STEP_2)));
			fail();
		} catch (MessageDeliveryException e) {

			// Verify
			assertEquals("Unknown job definition ID[JOB_DEFINITION_ID] version[1]", e.getMostSpecificCause().getMessage());
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
		startRequest.addParameter(new JobInstanceParameter().setName(PARAM_1_NAME).setValue(PARAM_1_VALUE));
		mySvc.startInstance(startRequest);

		// Verify

		verify(myJobInstancePersister, times(1)).storeNewInstance(myJobInstanceCaptor.capture());
		assertNull(myJobInstanceCaptor.getValue().getInstanceId());
		assertEquals(JOB_DEFINITION_ID, myJobInstanceCaptor.getValue().getJobDefinitionId());
		assertEquals(1, myJobInstanceCaptor.getValue().getJobDefinitionVersion());
		assertEquals(1, myJobInstanceCaptor.getValue().getParameters().size());
		assertEquals(PARAM_1_NAME, myJobInstanceCaptor.getValue().getParameters().get(0).getName());
		assertEquals(PARAM_1_VALUE, myJobInstanceCaptor.getValue().getParameters().get(0).getValue());
		assertEquals(StatusEnum.QUEUED, myJobInstanceCaptor.getValue().getStatus());

		verify(myWorkChannelProducer, times(1)).send(myMessageCaptor.capture());
		assertNull(myMessageCaptor.getAllValues().get(0).getPayload().getChunkId());
		assertEquals(JOB_DEFINITION_ID, myMessageCaptor.getAllValues().get(0).getPayload().getJobDefinitionId());
		assertEquals(1, myMessageCaptor.getAllValues().get(0).getPayload().getJobDefinitionVersion());
		assertEquals(STEP_1, myMessageCaptor.getAllValues().get(0).getPayload().getTargetStepId());

		verify(myJobInstancePersister, times(1)).storeWorkChunk(eq(JOB_DEFINITION_ID), eq(1), eq(STEP_1), eq(INSTANCE_ID), eq(0), isNull());

		verifyNoMoreInteractions(myJobInstancePersister);
		verifyNoMoreInteractions(myStep1Worker);
		verifyNoMoreInteractions(myStep2Worker);
		verifyNoMoreInteractions(myStep3Worker);
	}

	@Test
	public void testValidateParameters_RequiredParamNotPresent() {
		List<JobDefinitionParameter> definitions = Lists.newArrayList(
			new JobDefinitionParameter("foo", "Foo Parameter", JobDefinitionParameter.ParamTypeEnum.STRING, true, false),
			new JobDefinitionParameter("bar", "Bar Parameter", JobDefinitionParameter.ParamTypeEnum.STRING, true, false)
		);

		List<JobInstanceParameter> instances = Lists.newArrayList(
			new JobInstanceParameter().setName("foo").setValue("foo value")
		);

		try {
			JobCoordinatorImpl.validateParameters(definitions, instances);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("Missing required parameter: bar", e.getMessage());
		}
	}

	@Test
	public void testValidateParameters_RequiredParamMissingValue() {
		List<JobDefinitionParameter> definitions = Lists.newArrayList(
			new JobDefinitionParameter("foo", "Foo Parameter", JobDefinitionParameter.ParamTypeEnum.STRING, true, false),
			new JobDefinitionParameter("bar", "Bar Parameter", JobDefinitionParameter.ParamTypeEnum.STRING, true, false)
		);

		List<JobInstanceParameter> instances = Lists.newArrayList(
			new JobInstanceParameter().setName("foo").setValue("foo value"),
			new JobInstanceParameter().setName("bar")
		);

		try {
			JobCoordinatorImpl.validateParameters(definitions, instances);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("Missing required parameter: bar", e.getMessage());
		}
	}

	@Test
	public void testValidateParameters_InvalidRepeatingParameter() {
		List<JobDefinitionParameter> definitions = Lists.newArrayList(
			new JobDefinitionParameter("foo", "Foo Parameter", JobDefinitionParameter.ParamTypeEnum.STRING, true, false)
		);

		List<JobInstanceParameter> instances = Lists.newArrayList(
			new JobInstanceParameter().setName("foo").setValue("foo value"),
			new JobInstanceParameter().setName("foo").setValue("foo value 2")
		);

		try {
			JobCoordinatorImpl.validateParameters(definitions, instances);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("Illegal repeating parameter: foo", e.getMessage());
		}
	}

	@Test
	public void testValidateParameters_OptionalParameterMissing() {
		List<JobDefinitionParameter> definitions = Lists.newArrayList(
			new JobDefinitionParameter("foo", "Foo Parameter", JobDefinitionParameter.ParamTypeEnum.STRING, false, true),
			new JobDefinitionParameter("bar", "Bar Parameter", JobDefinitionParameter.ParamTypeEnum.STRING, false, false)
		);

		List<JobInstanceParameter> instances = Lists.newArrayList();

		Assertions.assertDoesNotThrow(() -> JobCoordinatorImpl.validateParameters(definitions, instances));
	}

	@Test
	public void testValidateParameters_UnexpectedParameter() {
		List<JobDefinitionParameter> definitions = Lists.newArrayList();

		List<JobInstanceParameter> instances = Lists.newArrayList(
			new JobInstanceParameter().setName("foo").setValue("foo value")
		);

		try {
			JobCoordinatorImpl.validateParameters(definitions, instances);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("Unexpected parameter: foo", e.getMessage());
		}
	}

	@SafeVarargs
	private final JobDefinition createJobDefinition(Consumer<JobDefinition.Builder>... theModifiers) {
		JobDefinition.Builder builder = JobDefinition
			.newBuilder()
			.setJobDefinitionId(JOB_DEFINITION_ID)
			.setJobDescription("This is a job description")
			.setJobDefinitionVersion(1)
			.addParameter(PARAM_1_NAME, "Param 1", JobDefinitionParameter.ParamTypeEnum.STRING, true, false)
			.addStep(STEP_1, "Step 1", myStep1Worker)
			.addStep(STEP_2, "Step 2", myStep2Worker)
			.addStep(STEP_3, "Step 3", myStep3Worker);

		for (Consumer<JobDefinition.Builder> next : theModifiers) {
			next.accept(builder);
		}

		return builder.build();
	}

	@Nonnull
	static JobInstance createInstance() {
		JobInstance instance = new JobInstance();
		instance.setInstanceId(INSTANCE_ID);
		instance.setStatus(StatusEnum.IN_PROGRESS);
		instance.setJobDefinitionId(JOB_DEFINITION_ID);
		instance.setJobDefinitionVersion(1);
		instance.addParameter(new JobInstanceParameter().setName(PARAM_1_NAME).setValue(PARAM_1_VALUE));
		return instance;
	}

	@Nonnull
	static WorkChunk createWorkChunk() {
		return new WorkChunk()
			.setId(CHUNK_ID)
			.setJobDefinitionId(JOB_DEFINITION_ID)
			.setJobDefinitionVersion(1)
			.setTargetStepId(STEP_2)
			.setData(singletonMap("DATA_1_KEY", "DATA_1_VALUE"))
			.setStatus(StatusEnum.IN_PROGRESS)
			.setInstanceId(INSTANCE_ID);
	}

	@Nonnull
	private JobWorkNotification createWorkNotification(String theStepId) {
		JobWorkNotification payload = new JobWorkNotification();
		payload.setJobDefinitionId(JOB_DEFINITION_ID);
		payload.setJobDefinitionVersion(1);
		payload.setInstanceId(INSTANCE_ID);
		payload.setChunkId(JobCoordinatorImplTest.CHUNK_ID);
		payload.setTargetStepId(theStepId);
		return payload;
	}

}
