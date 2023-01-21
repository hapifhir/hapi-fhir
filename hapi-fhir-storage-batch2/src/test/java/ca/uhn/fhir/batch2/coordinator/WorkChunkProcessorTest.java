package ca.uhn.fhir.batch2.coordinator;

import ca.uhn.fhir.batch2.api.ChunkExecutionDetails;
import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.ILastJobStepWorker;
import ca.uhn.fhir.batch2.api.IReductionStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.JobStepFailedException;
import ca.uhn.fhir.batch2.api.ReductionStepExecutionDetails;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.channel.BatchJobSender;
import ca.uhn.fhir.batch2.model.ChunkOutcome;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobDefinitionReductionStep;
import ca.uhn.fhir.batch2.model.JobDefinitionStep;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobWorkCursor;
import ca.uhn.fhir.batch2.model.MarkWorkChunkAsErrorRequest;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.batch2.model.WorkChunkData;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.util.JsonUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings({"unchecked", "rawtypes"})
@ExtendWith(MockitoExtension.class)
public class WorkChunkProcessorTest {
	private static final String INSTANCE_ID = "instanceId";
	private static final String JOB_DEFINITION_ID = "jobDefId";
	public static final String REDUCTION_STEP_ID = "step last";

	// static internal use classes

	private enum StepType {
		REDUCTION,
		INTERMEDIATE,
		FINAL
	}

	private static class TestJobParameters implements IModelJson { }

	private static class StepInputData implements IModelJson { }

	private static class StepOutputData implements IModelJson { }

	private static class TestDataSink<OT extends IModelJson> extends BaseDataSink<TestJobParameters, StepInputData, OT> {

		private BaseDataSink<?, ?, ?> myActualDataSink;

		TestDataSink(JobWorkCursor<TestJobParameters, StepInputData, OT> theWorkCursor) {
			super(INSTANCE_ID,
				theWorkCursor);
		}

		public void setDataSink(BaseDataSink<?, ?, ?> theSink) {
			myActualDataSink = theSink;
		}

		@Override
		public void accept(WorkChunkData<OT> theData) {

		}

		@Override
		public int getWorkChunkCount() {
			return 0;
		}
	}

	// our test class
	private class TestWorkChunkProcessor extends WorkChunkProcessor {

		public TestWorkChunkProcessor(IJobPersistence thePersistence, BatchJobSender theSender) {
			super(thePersistence, theSender);
		}

		@Override
		protected  <PT extends IModelJson, IT extends IModelJson, OT extends IModelJson> BaseDataSink<PT, IT, OT> getDataSink(
			JobWorkCursor<PT, IT, OT> theCursor,
			JobDefinition<PT> theJobDefinition,
			String theInstanceId
		) {
			// cause we don't want to test the actual DataSink class here!
			myDataSink.setDataSink(super.getDataSink(theCursor, theJobDefinition, theInstanceId));
			return (BaseDataSink<PT, IT, OT>) myDataSink;
		}
	}

	// general mocks

	private TestDataSink myDataSink;

	// step worker mocks
	private final IJobStepWorker<TestJobParameters, StepInputData, StepOutputData> myNonReductionStep = mock(IJobStepWorker.class);

	private final IReductionStepWorker<TestJobParameters, StepInputData, StepOutputData> myReductionStep = mock(IReductionStepWorker.class);

	private final ILastJobStepWorker<TestJobParameters, StepInputData> myLastStep = mock(ILastJobStepWorker.class);

	// class specific mocks
	@Mock
	private IJobPersistence myJobPersistence;

	@Mock
	private BatchJobSender myJobSender;

	private TestWorkChunkProcessor myExecutorSvc;

	@BeforeEach
	public void init() {
		myExecutorSvc = new TestWorkChunkProcessor(myJobPersistence, myJobSender);
	}

	private <OT extends IModelJson> JobDefinitionStep<TestJobParameters, StepInputData, OT> mockOutWorkCursor(
		StepType theStepType,
		JobWorkCursor<TestJobParameters, StepInputData, OT> theWorkCursor,
		boolean theMockOutTargetStep,
		boolean mockFinalWorkCursor
	) {
		JobDefinition<TestJobParameters> jobDefinition = createTestJobDefinition(
			theStepType == StepType.REDUCTION
		);

		JobDefinitionStep<TestJobParameters, StepInputData, OT> step = (JobDefinitionStep<TestJobParameters, StepInputData, OT>) getJobDefinitionStep(
			REDUCTION_STEP_ID,
			theStepType
		);

		when(theWorkCursor.getJobDefinition())
			.thenReturn(jobDefinition);
		when(theWorkCursor.getCurrentStep())
			.thenReturn(step);

		myDataSink = spy(new TestDataSink<>(theWorkCursor));
		if (theMockOutTargetStep) {
			when(myDataSink.getTargetStep())
				.thenReturn(step);
		}
		if (mockFinalWorkCursor) {
			JobWorkCursor<TestJobParameters, StepInputData, VoidModel> finalWorkCursor = mock(JobWorkCursor.class);

			when(finalWorkCursor.getJobDefinition())
				.thenReturn(jobDefinition);
			when(theWorkCursor.asFinalCursor())
				.thenReturn(finalWorkCursor);
		}

		return step;
	}

	@Test
	public void doExecution_reductionStepWithValidInput_executesAsExpected() {
		// setup
		List<String> chunkIds = Arrays.asList("chunk1", "chunk2");
		List<WorkChunk> chunks = new ArrayList<>();
		for (String id : chunkIds) {
			chunks.add(createWorkChunk(id));
		}
		JobInstance jobInstance = getTestJobInstance();
		JobWorkCursor<TestJobParameters, StepInputData, StepOutputData> workCursor = mock(JobWorkCursor.class);
		JobDefinitionStep<TestJobParameters, StepInputData, StepOutputData> step = mockOutWorkCursor(StepType.REDUCTION, workCursor, true, false);

		// when
		when(workCursor.isReductionStep())
			.thenReturn(true);
		when(myJobPersistence.fetchAllWorkChunksForStepIterator(eq(INSTANCE_ID), eq(REDUCTION_STEP_ID)))
			.thenReturn(chunks.iterator());
		when(myJobPersistence.markInstanceAsStatus(eq(INSTANCE_ID), eq(StatusEnum.FINALIZE))).thenReturn(true);
		when(myReductionStep.consume(any(ChunkExecutionDetails.class)))
			.thenReturn(ChunkOutcome.SUCCESS());
		when(myReductionStep.run(
			any(StepExecutionDetails.class), any(IJobDataSink.class)
		)).thenReturn(RunOutcome.SUCCESS);

		// test
		JobStepExecutorOutput<?, ?, ?> result = myExecutorSvc.doExecution(
			workCursor,
			jobInstance,
			null
		);

		// verify
		ArgumentCaptor<ChunkExecutionDetails> chunkCaptor = ArgumentCaptor.forClass(ChunkExecutionDetails.class);
		verify(myReductionStep, times(chunks.size()))
			.consume(chunkCaptor.capture());
		List<ChunkExecutionDetails> chunksSubmitted = chunkCaptor.getAllValues();
		assertEquals(chunks.size(), chunksSubmitted.size());
		for (ChunkExecutionDetails submitted : chunksSubmitted) {
			assertTrue(chunkIds.contains(submitted.getChunkId()));
		}

		assertTrue(result.isSuccessful());
		assertTrue(myDataSink.myActualDataSink instanceof ReductionStepDataSink);
		ArgumentCaptor<StepExecutionDetails> executionDetsCaptor = ArgumentCaptor.forClass(StepExecutionDetails.class);
		verify(myReductionStep).run(executionDetsCaptor.capture(), eq(myDataSink));
		assertTrue(executionDetsCaptor.getValue() instanceof ReductionStepExecutionDetails);
		ArgumentCaptor<List<String>> chunkIdCaptor = ArgumentCaptor.forClass(List.class);
		verify(myJobPersistence).markWorkChunksWithStatusAndWipeData(eq(INSTANCE_ID),
			chunkIdCaptor.capture(), eq(StatusEnum.COMPLETED), eq(null));
		List<String> capturedIds = chunkIdCaptor.getValue();
		assertEquals(chunkIds.size(), capturedIds.size());
		for (String chunkId : chunkIds) {
			assertTrue(capturedIds.contains(chunkId));
		}

		// nevers
		verifyNoErrors(0);
		verify(myNonReductionStep, never()).run(any(), any());
		verify(myLastStep, never()).run(any(), any());
	}

	@Test
	public void doExecution_reductionStepWithErrors_returnsFalseAndMarksPreviousChunksFailed() {
		// setup
		String errorMsg = "Exceptional!";
		List<String> chunkIds = Arrays.asList("chunk1", "chunk2");
		List<WorkChunk> chunks = new ArrayList<>();
		for (String id : chunkIds) {
			chunks.add(createWorkChunk(id));
		}
		JobInstance jobInstance = getTestJobInstance();
		JobWorkCursor<TestJobParameters, StepInputData, StepOutputData> workCursor = mock(JobWorkCursor.class);
		JobDefinitionStep<TestJobParameters, StepInputData, StepOutputData> step = mockOutWorkCursor(StepType.REDUCTION, workCursor, false, false);

		// when
		when(workCursor.isReductionStep())
			.thenReturn(true);
		when(myJobPersistence.fetchAllWorkChunksForStepIterator(eq(INSTANCE_ID), eq(REDUCTION_STEP_ID)))
			.thenReturn(chunks.iterator());
		when(myJobPersistence.markInstanceAsStatus(eq(INSTANCE_ID), eq(StatusEnum.FINALIZE))).thenReturn(true);
		doThrow(new RuntimeException(errorMsg))
			.when(myReductionStep).consume(any(ChunkExecutionDetails.class));

		// test
		JobStepExecutorOutput<?, ?, ?> result = myExecutorSvc.doExecution(
			workCursor,
			jobInstance,
			null
		);

		// verify
		assertFalse(result.isSuccessful());
		ArgumentCaptor<String> chunkIdCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<String> errorCaptor = ArgumentCaptor.forClass(String.class);
		verify(myJobPersistence, times(chunkIds.size()))
			.markWorkChunkAsFailed(chunkIdCaptor.capture(), errorCaptor.capture());
		List<String> chunkIdsCaptured = chunkIdCaptor.getAllValues();
		List<String> errorsCaptured = errorCaptor.getAllValues();
		for (int i = 0; i < chunkIds.size(); i++) {
			String cId = chunkIdsCaptured.get(i);
			String error = errorsCaptured.get(i);

			assertTrue(chunkIds.contains(cId));
			assertTrue(error.contains("Reduction step failed to execute chunk reduction for chunk"));
		}
		verify(myJobPersistence, never())
			.markWorkChunksWithStatusAndWipeData(anyString(), anyList(), any(), anyString());
		verify(myReductionStep, never())
			.run(any(), any());
	}

	@Test
	public void doExecution_reductionStepWithChunkFailures_marksChunkAsFailedButExecutesRestAsSuccess() {
		// setup
		List<String> chunkIds = Arrays.asList("chunk1", "chunk2");
		List<WorkChunk> chunks = new ArrayList<>();
		for (String id : chunkIds) {
			chunks.add(createWorkChunk(id));
		}
		JobInstance jobInstance = getTestJobInstance();
		JobWorkCursor<TestJobParameters, StepInputData, StepOutputData> workCursor = mock(JobWorkCursor.class);
		JobDefinitionStep<TestJobParameters, StepInputData, StepOutputData> step = mockOutWorkCursor(StepType.REDUCTION, workCursor, false, false);

		// when
		when(workCursor.isReductionStep())
			.thenReturn(true);
		when(myJobPersistence.fetchAllWorkChunksForStepIterator(eq(INSTANCE_ID), eq(REDUCTION_STEP_ID)))
			.thenReturn(chunks.iterator());
		when(myJobPersistence.markInstanceAsStatus(eq(INSTANCE_ID), eq(StatusEnum.FINALIZE))).thenReturn(true);
		when(myReductionStep.consume(any(ChunkExecutionDetails.class)))
			.thenReturn(ChunkOutcome.SUCCESS())
			.thenReturn(new ChunkOutcome(ChunkOutcome.Status.FAIL));
		when(myReductionStep.run(any(StepExecutionDetails.class), any(BaseDataSink.class)))
			.thenReturn(RunOutcome.SUCCESS);

		// test
		JobStepExecutorOutput<?, ?, ?> result = myExecutorSvc.doExecution(
			workCursor,
			jobInstance,
			null
		);

		// verify
		assertFalse(result.isSuccessful());
		verify(myJobPersistence)
			.markWorkChunkAsFailed(eq(chunkIds.get(1)), anyString());
		ArgumentCaptor<List> chunkListCaptor = ArgumentCaptor.forClass(List.class);
		verify(myJobPersistence)
			.markWorkChunksWithStatusAndWipeData(eq(INSTANCE_ID),
				chunkListCaptor.capture(),
				eq(StatusEnum.COMPLETED),
				any());
		List<String> completedIds = chunkListCaptor.getValue();
		assertEquals(1, completedIds.size());
		assertEquals(chunkIds.get(0), completedIds.get(0));
	}

	@Test
	public void doExecution_reductionWithChunkAbort_marksAllFutureChunksAsFailedButPreviousAsSuccess() {
		// setup
		List<String> chunkIds = Arrays.asList("chunk1", "chunk2");
		List<WorkChunk> chunks = new ArrayList<>();
		for (String id : chunkIds) {
			chunks.add(createWorkChunk(id));
		}
		JobInstance jobInstance = getTestJobInstance();
		JobWorkCursor<TestJobParameters, StepInputData, StepOutputData> workCursor = mock(JobWorkCursor.class);
		JobDefinitionStep<TestJobParameters, StepInputData, StepOutputData> step = mockOutWorkCursor(StepType.REDUCTION, workCursor, false, false);

		// when
		when(workCursor.isReductionStep())
			.thenReturn(true);
		when(myJobPersistence.markInstanceAsStatus(eq(INSTANCE_ID), eq(StatusEnum.FINALIZE))).thenReturn(true);
		when(myJobPersistence.fetchAllWorkChunksForStepIterator(eq(INSTANCE_ID), eq(REDUCTION_STEP_ID)))
			.thenReturn(chunks.iterator());
		when(myReductionStep.consume(any(ChunkExecutionDetails.class)))
			.thenReturn(ChunkOutcome.SUCCESS())
			.thenReturn(new ChunkOutcome(ChunkOutcome.Status.ABORT));
		when(myReductionStep.run(any(StepExecutionDetails.class), any(BaseDataSink.class)))
			.thenReturn(RunOutcome.SUCCESS);

		// test
		JobStepExecutorOutput<?, ?, ?> result = myExecutorSvc.doExecution(
			workCursor,
			jobInstance,
			null
		);

		// verification
		assertFalse(result.isSuccessful());
		ArgumentCaptor<List> submittedListIds = ArgumentCaptor.forClass(List.class);
		ArgumentCaptor<StatusEnum> statusCaptor = ArgumentCaptor.forClass(StatusEnum.class);
		verify(myJobPersistence, times(chunkIds.size()))
			.markWorkChunksWithStatusAndWipeData(
				eq(INSTANCE_ID),
				submittedListIds.capture(),
				statusCaptor.capture(),
				any()
			);
		assertEquals(2, submittedListIds.getAllValues().size());
		List<String> list1 = submittedListIds.getAllValues().get(0);
		List<String> list2 = submittedListIds.getAllValues().get(1);
		assertTrue(list1.contains(chunkIds.get(0)));
		assertTrue(list2.contains(chunkIds.get(1)));

		// assumes the order of which is called first
		// successes, then failures
		assertEquals(2, statusCaptor.getAllValues().size());
		List<StatusEnum> statuses = statusCaptor.getAllValues();
		assertEquals(StatusEnum.COMPLETED, statuses.get(0));
		assertEquals(StatusEnum.FAILED, statuses.get(1));
	}

	@Test
	public void doExecution_nonReductionIntermediateStepWithValidInput_executesAsExpected() {
		doExecution_nonReductionStep(0);
	}

	@Test
	public void doExecution_withRecoveredErrors_marksThoseErrorsToChunks() {
		doExecution_nonReductionStep(3);
	}

	private void doExecution_nonReductionStep(int theRecoveredErrorsForDataSink) {
		// setup
		JobInstance jobInstance = getTestJobInstance();
		WorkChunk chunk = new WorkChunk();
		chunk.setId("chunkId");
		chunk.setData(new StepInputData());

		JobWorkCursor<TestJobParameters, StepInputData, StepOutputData> workCursor = mock(JobWorkCursor.class);

		JobDefinitionStep<TestJobParameters, StepInputData, StepOutputData> step = mockOutWorkCursor(StepType.INTERMEDIATE, workCursor, true, false);

		// when
		when(myNonReductionStep.run(
			any(StepExecutionDetails.class), any(IJobDataSink.class)
		)).thenReturn(RunOutcome.SUCCESS);
		when(myDataSink.getRecoveredErrorCount())
			.thenReturn(theRecoveredErrorsForDataSink);

		// test
		JobStepExecutorOutput<?, ?, ?> result = myExecutorSvc.doExecution(
			workCursor,
			jobInstance,
			chunk
		);

		// verify
		assertTrue(result.isSuccessful());
		verify(myJobPersistence)
			.markWorkChunkAsCompletedAndClearData(eq(chunk.getId()), anyInt());
		assertTrue(myDataSink.myActualDataSink instanceof JobDataSink);

		if (theRecoveredErrorsForDataSink > 0) {
			verify(myJobPersistence)
				.incrementWorkChunkErrorCount(anyString(), eq(theRecoveredErrorsForDataSink));
		}

		// nevers
		verifyNoErrors(theRecoveredErrorsForDataSink);
		verifyNonReductionStep();
		verify(myLastStep, never()).run(any(), any());
		verify(myReductionStep, never()).run(any(), any());
	}

	@Test
	public void doExecution_finalNonReductionStep_executesAsExpected() {
		// setup
		JobInstance jobInstance = getTestJobInstance();
		WorkChunk chunk = new WorkChunk();
		chunk.setId("chunkId");
		chunk.setData(new StepInputData());

		JobWorkCursor<TestJobParameters, StepInputData, VoidModel> workCursor = mock(JobWorkCursor.class);

		JobDefinitionStep<TestJobParameters, StepInputData, VoidModel> step = mockOutWorkCursor(StepType.FINAL, workCursor, true, true);

		// when
		when(workCursor.isFinalStep())
			.thenReturn(true);
		when(myLastStep.run(any(StepExecutionDetails.class), any(BaseDataSink.class)))
			.thenReturn(RunOutcome.SUCCESS);

		// test
		JobStepExecutorOutput<?, ?, ?> result = myExecutorSvc.doExecution(
			workCursor,
			jobInstance,
			chunk
		);

		// verify
		assertTrue(result.isSuccessful());
		assertTrue(myDataSink.myActualDataSink instanceof FinalStepDataSink);

		// nevers
		verifyNoErrors(0);
		verifyNonReductionStep();
		verify(myReductionStep, never()).run(any(), any());
		verify(myNonReductionStep, never()).run(any(), any());
	}

	@Test
	public void doExecute_stepWorkerThrowsJobExecutionException_marksWorkChunkAsFailed() {
		runExceptionThrowingTest(new JobExecutionFailedException("Failure"));

		verify(myJobPersistence)
			.markWorkChunkAsFailed(anyString(), anyString());
	}

	@Test
	public void doExecution_stepWorkerThrowsRandomException_rethrowsJobStepFailedException() {
		String msg = "failure";
		try {
			runExceptionThrowingTest(new RuntimeException(msg));
			fail("Expected Exception to be thrown");
		} catch (JobStepFailedException jobStepFailedException) {
			assertTrue(jobStepFailedException.getMessage().contains(msg));
		}
	}

	@Test
	public void doExecution_stepWorkerThrowsRandomExceptionForever_eventuallyMarksAsFailedAndReturnsFalse() {
		// setup
		int counter = 0;
		AtomicInteger errorCounter = new AtomicInteger();
		String errorMsg = "my Error Message";
		JobInstance jobInstance = getTestJobInstance();
		WorkChunk chunk = new WorkChunk();
		chunk.setId("chunkId");
		chunk.setData(new StepInputData());

		JobWorkCursor<TestJobParameters, StepInputData, StepOutputData> workCursor = mock(JobWorkCursor.class);

		JobDefinitionStep<TestJobParameters, StepInputData, StepOutputData> step = mockOutWorkCursor(StepType.INTERMEDIATE, workCursor, true, false);

		// when
		when(myNonReductionStep.run(any(), any()))
			.thenThrow(new RuntimeException(errorMsg));
		when(myJobPersistence.markWorkChunkAsErroredAndIncrementErrorCount(any(MarkWorkChunkAsErrorRequest.class)))
			.thenAnswer((p) -> {
				WorkChunk ec = new WorkChunk();
				ec.setId(chunk.getId());
				int count = errorCounter.getAndIncrement();
				ec.setErrorCount(count);
				return Optional.of(ec);
			});

		// test
		Boolean processedOutcomeSuccessfully = null;
		do {
			try {
				JobStepExecutorOutput<?, ?, ?> output = myExecutorSvc.doExecution(
					workCursor,
					jobInstance,
					chunk
				);
				/*
				 * Getting a value here means we are no longer
				 * throwing exceptions. Which is the desired outcome.
				 * We just now need to ensure that this outcome is
				 * "false"
				 */
				processedOutcomeSuccessfully = output.isSuccessful();
			} catch (JobStepFailedException ex) {
				assertTrue(ex.getMessage().contains(errorMsg));
				counter++;
			}
			/*
			 * +2 because...
			 * we check for > MAX_CHUNK_ERROR_COUNT (+1)
			 * we want it to run one extra time here (+1)
			 */
		} while (processedOutcomeSuccessfully == null && counter < WorkChunkProcessor.MAX_CHUNK_ERROR_COUNT + 2);

		// verify
		assertNotNull(processedOutcomeSuccessfully);
		// +1 because of the > MAX_CHUNK_ERROR_COUNT check
		assertEquals(WorkChunkProcessor.MAX_CHUNK_ERROR_COUNT  + 1, counter);
		assertFalse(processedOutcomeSuccessfully);
	}

	private void runExceptionThrowingTest(Exception theExceptionToThrow) {
		// setup
		JobInstance jobInstance = getTestJobInstance();
		WorkChunk chunk = new WorkChunk();
		chunk.setId("chunkId");
		chunk.setData(new StepInputData());

		JobWorkCursor<TestJobParameters, StepInputData, StepOutputData> workCursor = mock(JobWorkCursor.class);

		JobDefinitionStep<TestJobParameters, StepInputData, StepOutputData> step = mockOutWorkCursor(StepType.INTERMEDIATE, workCursor, true, false);

		// when
		when(myNonReductionStep.run(any(), any()))
			.thenThrow(theExceptionToThrow);

		// test
		JobStepExecutorOutput<?, ?, ?> output = myExecutorSvc.doExecution(
			workCursor,
			jobInstance,
			chunk
		);

		// verify
		assertFalse(output.isSuccessful());
	}

	/**********************/

	private void verifyNoErrors(int theRecoveredErrorCount) {
		if (theRecoveredErrorCount == 0) {
			verify(myJobPersistence, never())
				.incrementWorkChunkErrorCount(anyString(), anyInt());
		}
		verify(myJobPersistence, never())
			.markWorkChunkAsFailed(anyString(), anyString());
		verify(myJobPersistence, never())
			.markWorkChunkAsErroredAndIncrementErrorCount(anyString(), anyString());
	}

	private void verifyNonReductionStep() {
		verify(myJobPersistence, never())
			.fetchWorkChunkSetStartTimeAndMarkInProgress(anyString());
		verify(myJobPersistence, never())
			.markWorkChunksWithStatusAndWipeData(anyString(), anyList(), any(), any());
		verify(myJobPersistence, never())
			.fetchAllWorkChunksForStepIterator(anyString(), anyString());
	}

	private JobInstance getTestJobInstance() {
		JobInstance instance = JobInstance.fromInstanceId(INSTANCE_ID);
		instance.setParameters(new TestJobParameters());

		return instance;
	}

	private WorkChunk createWorkChunk(String theId) {
		WorkChunk chunk = new WorkChunk();
		chunk.setInstanceId(INSTANCE_ID);
		chunk.setId(theId);
		chunk.setStatus(StatusEnum.QUEUED);
		chunk.setData(JsonUtil.serialize(
			new StepInputData()
		));
		return chunk;
	}

	@SuppressWarnings("unchecked")
	private JobDefinition<TestJobParameters> createTestJobDefinition(boolean theWithReductionStep) {
		JobDefinition<TestJobParameters> def = null;
		if (theWithReductionStep) {
			def = JobDefinition.newBuilder()
				.setJobDefinitionId(JOB_DEFINITION_ID)
				.setJobDescription("Reduction job description")
				.setJobDefinitionVersion(1)
				.gatedExecution()
				.setParametersType(TestJobParameters.class)
				.addFirstStep(
					"step 1",
					"description 1",
					VoidModel.class,
					mock(IJobStepWorker.class) // we don't care about this step - we just need it
				)
				.addFinalReducerStep(
					REDUCTION_STEP_ID,
					"description 2",
					StepOutputData.class,
					myReductionStep
				)
				.build();
		} else {
			def = JobDefinition.newBuilder()
				.setJobDefinitionId(JOB_DEFINITION_ID)
				.setJobDescription("Non reduction job description")
				.setJobDefinitionVersion(1)
				.setParametersType(TestJobParameters.class)
				.addFirstStep(
					"step 1",
					"description 1",
					VoidModel.class,
					mock(IJobStepWorker.class) // we don't care about this step
				)
				.addIntermediateStep(
					"Step 2",
					"description 2",
					StepInputData.class,
					myNonReductionStep
				)
				.addLastStep(
					"Step 3",
					"description 3",
					myLastStep
				)
				.build();
		}

		return def;
	}

	private JobDefinitionStep<TestJobParameters, StepInputData, ?> getJobDefinitionStep(
		String theId,
		StepType theStepType
	) {
		if (theStepType == StepType.REDUCTION) {
			return new JobDefinitionReductionStep<>(
				theId,
				"i'm a reduction step",
				myReductionStep,
				StepInputData.class,
				StepOutputData.class
			);
		} else if (theStepType == StepType.INTERMEDIATE) {
			return new JobDefinitionStep<>(
				theId,
				"i'm a step - many like me, but i'm unique",
				myNonReductionStep,
				StepInputData.class,
				StepOutputData.class
			);
		} else if (theStepType == StepType.FINAL) {
			return new JobDefinitionStep<>(
				theId,
				"I'm a final step",
				myLastStep,
				StepInputData.class,
				VoidModel.class
			);
		}

		/// TODO - log

		return null;
	}
}
