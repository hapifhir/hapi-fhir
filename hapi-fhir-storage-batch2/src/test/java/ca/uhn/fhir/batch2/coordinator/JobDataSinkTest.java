package ca.uhn.fhir.batch2.coordinator;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.api.IJobStepExecutionServices;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.channel.BatchJobSender;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobDefinitionStep;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobWorkCursor;
import ca.uhn.fhir.batch2.model.JobWorkNotification;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.batch2.model.WorkChunkCreateEvent;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.dao.tx.NonTransactionalHapiTransactionService;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.util.JsonUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.support.TransactionCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JobDataSinkTest {
	private static final String JOB_DEF_ID = "Jeff";
	private static final String JOB_DESC = "Jeff is curious";
	private static final int JOB_DEF_VERSION = 1;
	private static final int PID_COUNT = 729;
	private static final String JOB_INSTANCE_ID = "17";
	private static final String CHUNK_ID = "289";
	private static final String PARENT_CHUNK_ID = "288";
	public static final String FIRST_STEP_ID = "firstStep";
	private static final String MIDDLE_STEP_ID = "middleStep";
	public static final String LAST_STEP_ID = "lastStep";

	@Mock
	private BatchJobSender myBatchJobSender;
	@Mock
	private IJobPersistence myJobPersistence;
	@Mock
	private IJobStepExecutionServices myJobStepExecutionServices;
	@Captor
	private ArgumentCaptor<JobWorkNotification> myJobWorkNotificationCaptor;
	@Captor
	private ArgumentCaptor<WorkChunkCreateEvent> myBatchWorkChunkCaptor;
	private final RecordingHapiTransactionService myHapiTransactionService = new RecordingHapiTransactionService();

	/** The transaction frame that was open when {@link IJobPersistence#onWorkChunkCreate} was invoked. */
	private TransactionFrame myFrameAtCreate;
	/** The transaction frame that was open when {@link IJobPersistence#enqueueWorkChunkForProcessing} was invoked. */
	private TransactionFrame myFrameAtEnqueue;
	/** The transaction frame that was open when {@link BatchJobSender#sendWorkChannelMessage} was invoked. */
	private TransactionFrame myFrameAtSend;
	private boolean mySendWorkChannelMessageInvoked;
	private boolean mySendWorkChannelMessageInvokedBeforeEnqueueReturned;

	@Test
	public void test_sink_accept() {
		// setup

		IJobStepWorker<TestJobParameters, VoidModel, Step1Output> firstStepWorker = new IJobStepWorker<>() {
			@Nonnull
			@Override
			public RunOutcome run(@Nonnull StepExecutionDetails<TestJobParameters, VoidModel> theStepExecutionDetails, @Nonnull IJobDataSink<Step1Output> theDataSink) throws JobExecutionFailedException {
				TestJobParameters params = theStepExecutionDetails.getParameters();
				int numPidsToGenerate = Integer.parseInt(params.getParam1());
				Step1Output output = new Step1Output();
				for (long i = 0; i < numPidsToGenerate; ++i) {
					output.addPid(i);
				}
				theDataSink.accept(output);
				return new RunOutcome(numPidsToGenerate);
			}
		};

		IJobStepWorker<TestJobParameters, Step1Output, VoidModel> lastStepWorker = (details, sink) -> {
			// Our test does not call this worker
			fail();
			return null;
		};

		JobDefinition<TestJobParameters> job = JobDefinition.newBuilder()
			.setJobDefinitionId(JOB_DEF_ID)
			.setJobDescription(JOB_DESC)
			.setJobDefinitionVersion(JOB_DEF_VERSION)
			.setParametersType(TestJobParameters.class)
			.addFirstStep(FIRST_STEP_ID, "s1desc", Step1Output.class, firstStepWorker)
			.addLastStep(LAST_STEP_ID, "s2desc", lastStepWorker)
			.build();

		JobDefinitionStep<TestJobParameters, VoidModel, Step1Output> firstStep = (JobDefinitionStep<TestJobParameters, VoidModel, Step1Output>) job.getSteps().get(0);
		JobDefinitionStep<TestJobParameters, Step1Output, VoidModel> lastStep = (JobDefinitionStep<TestJobParameters, Step1Output, VoidModel>) job.getSteps().get(1);

		// execute
		// Let's test our first step worker by calling run on it:
		when(myJobPersistence.onWorkChunkCreate(myBatchWorkChunkCaptor.capture())).thenReturn(CHUNK_ID);
		doAnswer(args -> {
			Consumer<Integer> consumer = args.getArgument(1);
			consumer.accept(1);
			return 1;
		}).when(myJobPersistence).enqueueWorkChunkForProcessing(anyString(), any());
		JobInstance instance = JobInstance.fromInstanceId(JOB_INSTANCE_ID);
		WorkChunk chunk = new WorkChunk().setId(CHUNK_ID);
		StepExecutionDetails<TestJobParameters, VoidModel> details = new StepExecutionDetails<>(new TestJobParameters().setParam1("" + PID_COUNT), null, instance, chunk, myJobStepExecutionServices);
		JobWorkCursor<TestJobParameters, VoidModel, Step1Output> cursor = new JobWorkCursor<>(job, true, firstStep, lastStep);
		JobDataSink<TestJobParameters, VoidModel, Step1Output> sink = new JobDataSink<>(myBatchJobSender, myJobPersistence, job, JOB_INSTANCE_ID, cursor, chunk, myHapiTransactionService);

		RunOutcome result = firstStepWorker.run(details, sink);

		// verify
		assertEquals(PID_COUNT, result.getRecordsProcessed());

		// theDataSink.accept(output) called by firstStepWorker above calls two services.  Let's validate them both.

		verify(myBatchJobSender).sendWorkChannelMessage(myJobWorkNotificationCaptor.capture());
		JobWorkNotification notification = myJobWorkNotificationCaptor.getValue();
		assertEquals(JOB_DEF_ID, notification.getJobDefinitionId());
		assertEquals(JOB_INSTANCE_ID, notification.getInstanceId());
		assertEquals(CHUNK_ID, notification.getChunkId());
		assertEquals(JOB_DEF_VERSION, notification.getJobDefinitionVersion());
		assertEquals(LAST_STEP_ID, notification.getTargetStepId());

		WorkChunkCreateEvent batchWorkChunk = myBatchWorkChunkCaptor.getValue();
		assertEquals(JOB_DEF_VERSION, batchWorkChunk.jobDefinitionVersion);
		assertEquals(0, batchWorkChunk.sequence);
		assertEquals(JOB_DEF_ID, batchWorkChunk.jobDefinitionId);
		assertEquals(JOB_INSTANCE_ID, batchWorkChunk.instanceId);
		assertEquals(LAST_STEP_ID, batchWorkChunk.targetStepId);
		assertNotNull(batchWorkChunk.serializedData);
		Step1Output stepOutput = JsonUtil.deserialize(batchWorkChunk.serializedData, Step1Output.class);
		assertThat(stepOutput.getPids()).hasSize(PID_COUNT);
	}

	/**
	 * A non-gated step (e.g. {@literal ResourceIdListStep} on the {@literal MDM_SUBMIT} job) sinks its child
	 * chunks from inside a long-lived caller transaction, because the resource id stream is wrapped in
	 * transaction advice so its {@literal ResultSet} stays open. The {@literal READY -> QUEUED} state update
	 * must not enlist in that caller transaction: it has to run in its own {@literal REQUIRES_NEW}
	 * transaction on the default partition, exactly like the chunk-creation call immediately above it.
	 * The outbox {@literal sendWorkChannelMessage} must happen inside that same transaction so that
	 * at-least-once delivery is preserved.
	 */
	@Test
	void testAccept_whenCallerHasOpenTransaction_enqueuesWorkChunkInItsOwnRequiresNewTransaction() {
		// setup
		JobDefinition<TestJobParameters> job = buildTwoStepJobDefinition(false);
		JobDataSink<TestJobParameters, VoidModel, Step1Output> sink = buildSink(job);

		when(myJobPersistence.onWorkChunkCreate(any())).thenReturn(CHUNK_ID);
		recordTransactionFrameOnEnqueue();
		recordTransactionFrameOnSend();

		Step1Output output = new Step1Output();
		output.addPid(1L);

		// execute
		// The caller (the step worker) already has a transaction open when it sinks the chunk
		myHapiTransactionService.withSystemRequest().execute(() -> {
			sink.accept(output);
			return null;
		});

		// verify
		assertThat(myFrameAtEnqueue)
			.as("enqueueWorkChunkForProcessing must be invoked inside a transaction")
			.isNotNull();
		assertThat(myFrameAtEnqueue.propagation())
			.as("enqueueWorkChunkForProcessing must run in its own REQUIRES_NEW transaction, not the caller's")
			.isEqualTo(Propagation.REQUIRES_NEW);

		assertThat(myFrameAtSend)
			.as("sendWorkChannelMessage must be invoked inside a transaction (transactional outbox)")
			.isNotNull();
		assertThat(myFrameAtSend.propagation())
			.as("sendWorkChannelMessage must stay inside the REQUIRES_NEW enqueue transaction")
			.isEqualTo(Propagation.REQUIRES_NEW);

		assertThat(myFrameAtEnqueue.requestPartitionId())
			.as("the enqueue must target the default partition, exactly as the chunk-creation call does")
			.isEqualTo(RequestPartitionId.defaultPartition(new PartitionSettings()));

		verify(myJobPersistence)
			.enqueueWorkChunkForProcessing(eq(CHUNK_ID), any());
	}

	/**
	 * A gated job must not enqueue the chunk at all - the maintenance pass does that
	 * later - but the chunk creation must still happen in its own REQUIRES_NEW transaction.
	 */
	@Test
	void testAccept_whenJobIsGated_createsChunkInRequiresNewTransactionAndDoesNotEnqueue() {
		// setup
		JobDefinition<TestJobParameters> job = buildTwoStepJobDefinition(true);
		JobDataSink<TestJobParameters, VoidModel, Step1Output> sink = buildSink(job);

		when(myJobPersistence.onWorkChunkCreate(myBatchWorkChunkCaptor.capture())).thenAnswer(theInvocation -> {
			myFrameAtCreate = myHapiTransactionService.getCurrentFrame();
			return CHUNK_ID;
		});

		Step1Output output = new Step1Output();
		output.addPid(1L);

		// execute
		myHapiTransactionService.withSystemRequest().execute(() -> {
			sink.accept(output);
			return null;
		});

		// verify
		verify(myJobPersistence, never()).enqueueWorkChunkForProcessing(anyString(), any());
		verify(myBatchJobSender, never()).sendWorkChannelMessage(any());

		assertThat(myFrameAtCreate)
			.as("onWorkChunkCreate must be invoked inside a transaction")
			.isNotNull();
		assertThat(myFrameAtCreate.propagation())
			.as("onWorkChunkCreate must run in its own REQUIRES_NEW transaction")
			.isEqualTo(Propagation.REQUIRES_NEW);
		assertThat(myBatchWorkChunkCaptor.getValue().isGatedExecution)
			.as("a gated job must create its chunk in the gated state")
			.isTrue();
	}

	/**
	 * {@literal acceptForFutureStep} is the second entry point into
	 * {@literal acceptForStepId} and must get the same transaction demarcation as {@literal accept}.
	 */
	@Test
	void testAcceptForFutureStep_whenCallerHasOpenTransaction_enqueuesWorkChunkInItsOwnRequiresNewTransaction() {
		// setup
		JobDefinition<TestJobParameters> job = buildThreeStepJobDefinition();
		JobDataSink<TestJobParameters, VoidModel, Step1Output> sink = buildSink(job);

		when(myJobPersistence.onWorkChunkCreate(any())).thenReturn(CHUNK_ID);
		recordTransactionFrameOnEnqueue();
		recordTransactionFrameOnSend();

		Step2Output output = new Step2Output().setValue("skip-ahead");

		// execute
		myHapiTransactionService.withSystemRequest().execute(() -> {
			sink.acceptForFutureStep(LAST_STEP_ID, output);
			return null;
		});

		// verify
		assertThat(myFrameAtEnqueue)
			.as("enqueueWorkChunkForProcessing must be invoked inside a transaction")
			.isNotNull();
		assertThat(myFrameAtEnqueue.propagation())
			.as("acceptForFutureStep must enqueue in its own REQUIRES_NEW transaction, not the caller's")
			.isEqualTo(Propagation.REQUIRES_NEW);

		assertThat(myFrameAtSend)
			.as("sendWorkChannelMessage must be invoked inside a transaction (transactional outbox)")
			.isNotNull();
		assertThat(myFrameAtSend.propagation())
			.as("sendWorkChannelMessage must stay inside the REQUIRES_NEW enqueue transaction")
			.isEqualTo(Propagation.REQUIRES_NEW);
	}

	/**
	 * The transactional-outbox ordering tripwire: the work channel message is
	 * deliberately sent from inside the {@literal enqueueWorkChunkForProcessing} callback, i.e. before the
	 * {@literal READY -> QUEUED} transition commits. Hoisting the send out of the callback would break
	 * at-least-once delivery.
	 */
	@Test
	void testAccept_sendsWorkChannelMessageFromInsideTheEnqueueCallback() {
		// setup
		JobDefinition<TestJobParameters> job = buildTwoStepJobDefinition(false);
		JobDataSink<TestJobParameters, VoidModel, Step1Output> sink = buildSink(job);

		when(myJobPersistence.onWorkChunkCreate(any())).thenReturn(CHUNK_ID);
		doAnswer(theInvocation -> {
			Consumer<Integer> consumer = theInvocation.getArgument(1);
			consumer.accept(1);
			mySendWorkChannelMessageInvokedBeforeEnqueueReturned = mySendWorkChannelMessageInvoked;
			return null;
		}).when(myJobPersistence).enqueueWorkChunkForProcessing(anyString(), any());
		doAnswer(theInvocation -> {
			mySendWorkChannelMessageInvoked = true;
			return null;
		}).when(myBatchJobSender).sendWorkChannelMessage(any());

		Step1Output output = new Step1Output();
		output.addPid(1L);

		// execute
		sink.accept(output);

		// verify
		assertThat(mySendWorkChannelMessageInvoked)
			.as("the work channel message must be sent")
			.isTrue();
		assertThat(mySendWorkChannelMessageInvokedBeforeEnqueueReturned)
			.as("the work channel message must be sent from inside the enqueue callback, before it returns")
			.isTrue();
	}

	private void recordTransactionFrameOnEnqueue() {
		doAnswer(theInvocation -> {
			myFrameAtEnqueue = myHapiTransactionService.getCurrentFrame();
			Consumer<Integer> consumer = theInvocation.getArgument(1);
			consumer.accept(1);
			return null;
		}).when(myJobPersistence).enqueueWorkChunkForProcessing(anyString(), any());
	}

	private void recordTransactionFrameOnSend() {
		doAnswer(theInvocation -> {
			myFrameAtSend = myHapiTransactionService.getCurrentFrame();
			return null;
		}).when(myBatchJobSender).sendWorkChannelMessage(any());
	}

	@Nonnull
	private JobDefinition<TestJobParameters> buildTwoStepJobDefinition(boolean theGatedExecution) {
		IJobStepWorker<TestJobParameters, VoidModel, Step1Output> firstStepWorker =
			(details, sink) -> new RunOutcome(0);
		IJobStepWorker<TestJobParameters, Step1Output, VoidModel> lastStepWorker =
			(details, sink) -> new RunOutcome(0);

		return JobDefinition.newBuilder()
			.setJobDefinitionId(JOB_DEF_ID)
			.setJobDescription(JOB_DESC)
			.setJobDefinitionVersion(JOB_DEF_VERSION)
			.setParametersType(TestJobParameters.class)
			.gatedExecution(theGatedExecution)
			.addFirstStep(FIRST_STEP_ID, "s1desc", Step1Output.class, firstStepWorker)
			.addLastStep(LAST_STEP_ID, "s2desc", lastStepWorker)
			.build();
	}

	@Nonnull
	private JobDefinition<TestJobParameters> buildThreeStepJobDefinition() {
		IJobStepWorker<TestJobParameters, VoidModel, Step1Output> firstStepWorker =
			(details, sink) -> new RunOutcome(0);
		IJobStepWorker<TestJobParameters, Step1Output, Step2Output> middleStepWorker =
			(details, sink) -> new RunOutcome(0);
		IJobStepWorker<TestJobParameters, Step2Output, VoidModel> lastStepWorker =
			(details, sink) -> new RunOutcome(0);

		return JobDefinition.newBuilder()
			.setJobDefinitionId(JOB_DEF_ID)
			.setJobDescription(JOB_DESC)
			.setJobDefinitionVersion(JOB_DEF_VERSION)
			.setParametersType(TestJobParameters.class)
			.addFirstStep(FIRST_STEP_ID, "s1desc", Step1Output.class, firstStepWorker)
			.addIntermediateStep(MIDDLE_STEP_ID, "s2desc", Step2Output.class, middleStepWorker)
			.addLastStep(LAST_STEP_ID, "s3desc", lastStepWorker)
			.build();
	}

	@Nonnull
	@SuppressWarnings("unchecked")
	private JobDataSink<TestJobParameters, VoidModel, Step1Output> buildSink(JobDefinition<TestJobParameters> theJobDefinition) {
		JobDefinitionStep<TestJobParameters, VoidModel, Step1Output> currentStep =
			(JobDefinitionStep<TestJobParameters, VoidModel, Step1Output>) theJobDefinition.getSteps().get(0);
		JobDefinitionStep<TestJobParameters, Step1Output, ?> nextStep =
			(JobDefinitionStep<TestJobParameters, Step1Output, ?>) theJobDefinition.getSteps().get(1);
		JobWorkCursor<TestJobParameters, VoidModel, Step1Output> cursor =
			new JobWorkCursor<>(theJobDefinition, true, currentStep, nextStep);
		WorkChunk chunk = new WorkChunk().setId(PARENT_CHUNK_ID);
		return new JobDataSink<>(myBatchJobSender, myJobPersistence, theJobDefinition, JOB_INSTANCE_ID, cursor, chunk, myHapiTransactionService);
	}

	/**
	 * A {@link NonTransactionalHapiTransactionService} that records the stack of transaction frames that
	 * are currently open, so that a test can observe which demarcation was in effect at the moment a
	 * collaborator was invoked.
	 */
	private static class RecordingHapiTransactionService extends NonTransactionalHapiTransactionService {
		/**
		 * Used as a stack. This is deliberately not an {@link java.util.ArrayDeque} - a frame opened by
		 * {@literal withSystemRequest()} carries a {@literal null} propagation, and ArrayDeque rejects nulls.
		 */
		private final List<TransactionFrame> myOpenFrames = new ArrayList<>();

		@Nullable
		@Override
		protected <T> T doExecute(ExecutionBuilder theExecutionBuilder, TransactionCallback<T> theCallback) {
			myOpenFrames.add(new TransactionFrame(
				theExecutionBuilder.getPropagation(), theExecutionBuilder.getRequestPartitionIdForTesting()));
			try {
				return super.doExecute(theExecutionBuilder, theCallback);
			} finally {
				myOpenFrames.remove(myOpenFrames.size() - 1);
			}
		}

		@Nullable
		TransactionFrame getCurrentFrame() {
			if (myOpenFrames.isEmpty()) {
				return null;
			}
			return myOpenFrames.get(myOpenFrames.size() - 1);
		}
	}

	/**
	 * The demarcation of a single in-flight {@literal IHapiTransactionService} execution.
	 */
	private record TransactionFrame(@Nullable Propagation propagation, @Nullable RequestPartitionId requestPartitionId) {}

	private static class Step1Output implements IModelJson {
		@JsonProperty("pids")
		private List<Long> myPids;

		public List<Long> getPids() {
			if (myPids == null) {
				myPids = new ArrayList<>();
			}
			return myPids;
		}

		public void addPid(long thePid) {
			getPids().add(thePid);
		}
	}

	private static class Step2Output implements IModelJson {
		@JsonProperty("value")
		private String myValue;

		public String getValue() {
			return myValue;
		}

		public Step2Output setValue(String theValue) {
			myValue = theValue;
			return this;
		}
	}
}
