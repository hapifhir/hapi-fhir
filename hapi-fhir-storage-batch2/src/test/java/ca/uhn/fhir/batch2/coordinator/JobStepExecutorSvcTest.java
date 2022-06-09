package ca.uhn.fhir.batch2.coordinator;

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
import ca.uhn.fhir.batch2.maintenance.JobChunkProgressAccumulator;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobDefinitionReductionStep;
import ca.uhn.fhir.batch2.model.JobDefinitionStep;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobWorkCursor;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.batch2.model.WorkChunkData;
import ca.uhn.fhir.batch2.progress.JobInstanceProgressCalculator;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings({"unchecked", "rawtypes"})
@ExtendWith(MockitoExtension.class)
public class JobStepExecutorSvcTest {
	private static final String INSTANCE_ID = "instanceId";
	private static final String JOB_DEFINITION_ID = "jobDefId";

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
				theWorkCursor,
				JOB_DEFINITION_ID);
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
	private class TestJobStepExecutorSvc extends JobStepExecutorSvc {

		public TestJobStepExecutorSvc(IJobPersistence thePersistence, BatchJobSender theSender) {
			super(thePersistence, theSender);
		}

		@Override
		protected JobInstanceProgressCalculator getProgressCalculator(JobInstance theInstance, JobChunkProgressAccumulator theAccumulator) {
			return myCalculator;
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
	@Mock
	private JobInstanceProgressCalculator myCalculator;

	private TestDataSink myDataSink;

	private final JobChunkProgressAccumulator myAccumulator = mock(JobChunkProgressAccumulator.class);

	// step worker mocks
	private final IJobStepWorker<TestJobParameters, StepInputData, StepOutputData> myNonReductionStep = mock(IJobStepWorker.class);

	private final IReductionStepWorker<TestJobParameters, StepInputData, StepOutputData> myReductionStep = mock(IReductionStepWorker.class);

	private final ILastJobStepWorker<TestJobParameters, StepInputData> myLastStep = mock(ILastJobStepWorker.class);

	// class specific mocks
	@Mock
	private IJobPersistence myJobPersistence;

	@Mock
	private BatchJobSender myJobSender;

	private TestJobStepExecutorSvc myExecutorSvc;

	@BeforeEach
	public void init() {
		myExecutorSvc = new TestJobStepExecutorSvc(myJobPersistence, myJobSender);
	}

	private <OT extends IModelJson> JobDefinitionStep<TestJobParameters, StepInputData, OT> mockOutWorkCursor(
		StepType theStepType,
		JobWorkCursor<TestJobParameters, StepInputData, OT> theWorkCursor
	) {
		myDataSink = spy(new TestDataSink<>(theWorkCursor));
		JobDefinition<TestJobParameters> jobDefinition = createTestJobDefinition(
			theStepType == StepType.REDUCTION
		);

		JobDefinitionStep<TestJobParameters, StepInputData, OT> step = (JobDefinitionStep<TestJobParameters, StepInputData, OT>) getJobDefinitionStep(
			"stepId",
			theStepType
		);

		when(theWorkCursor.getJobDefinition())
			.thenReturn(jobDefinition);
		when(theWorkCursor.getCurrentStep())
			.thenReturn(step);
		when(myDataSink.getTargetStep())
			.thenReturn(step);

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
		String newChunkId = "newChunk";
		JobInstance jobInstance = getTestJobInstance();
		JobWorkCursor<TestJobParameters, StepInputData, StepOutputData> workCursor = mock(JobWorkCursor.class);
		JobDefinitionStep<TestJobParameters, StepInputData, StepOutputData> step = mockOutWorkCursor(StepType.REDUCTION, workCursor);

		// when
		when(myAccumulator.getChunkIdsWithStatus(eq(jobInstance.getInstanceId()),
			eq(step.getStepId()),
			any())
		).thenReturn(chunkIds);
		when(myJobPersistence.fetchWorkChunks(eq(INSTANCE_ID), anyList()))
			.thenReturn(chunks);
		when(myJobPersistence.reduceWorkChunksToSingleChunk(eq(jobInstance.getInstanceId()), anyList(), any(BatchWorkChunk.class)))
			.thenReturn(newChunkId);
		when(myReductionStep.run(
			any(StepExecutionDetails.class), any(IJobDataSink.class)
		)).thenReturn(RunOutcome.SUCCESS);

		// test
		JobStepExecutorOutput<?, ?, ?> result = myExecutorSvc.doExecution(
			workCursor,
			jobInstance,
			null,
			myAccumulator
		);

		// verify
		assertTrue(result.isIsSuccessful());
		assertTrue(myDataSink.myActualDataSink instanceof ReductionStepDataSink);
		verify(myCalculator).calculateAndStoreInstanceProgress();
		ArgumentCaptor<StepExecutionDetails> executionDetsCaptor = ArgumentCaptor.forClass(StepExecutionDetails.class);
		verify(myReductionStep).run(executionDetsCaptor.capture(), eq(myDataSink));
		assertTrue(executionDetsCaptor.getValue() instanceof ReductionStepExecutionDetails);
		ArgumentCaptor<List<String>> chunkIdCaptor = ArgumentCaptor.forClass(List.class);
		verify(myJobPersistence).reduceWorkChunksToSingleChunk(eq(INSTANCE_ID),
			chunkIdCaptor.capture(), any(BatchWorkChunk.class));
		List<String> capturedIds = chunkIdCaptor.getValue();
		assertEquals(chunkIds.size(), capturedIds.size());
		for (String chunkId : chunkIds) {
			assertTrue(capturedIds.contains(chunkId));
		}
		verify(myJobPersistence).markWorkChunkAsCompletedAndClearData(eq(newChunkId),
			eq(0));

		// nevers
		verifyNoErrors(0);
		verify(myNonReductionStep, never()).run(any(), any());
		verify(myLastStep, never()).run(any(), any());
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

		JobDefinitionStep<TestJobParameters, StepInputData, StepOutputData> step = mockOutWorkCursor(StepType.INTERMEDIATE, workCursor);

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
			chunk,
			myAccumulator
		);

		// verify
		assertTrue(result.isIsSuccessful());
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

		verify(myAccumulator, never())
			.getChunkIdsWithStatus(anyString(), anyString(), any());
	}

	@Test
	public void doExecution_finalNonReductionStep_executesAsExpected() {
		// setup
		JobInstance jobInstance = getTestJobInstance();
		WorkChunk chunk = new WorkChunk();
		chunk.setId("chunkId");
		chunk.setData(new StepInputData());

		JobWorkCursor<TestJobParameters, StepInputData, VoidModel> workCursor = mock(JobWorkCursor.class);

		JobDefinitionStep<TestJobParameters, StepInputData, VoidModel> step = mockOutWorkCursor(StepType.FINAL, workCursor);

		// when
		when(workCursor.isFinalStep())
			.thenReturn(true);
		when(myLastStep.run(any(StepExecutionDetails.class), any(BaseDataSink.class)))
			.thenReturn(RunOutcome.SUCCESS);

		// test
		JobStepExecutorOutput<?, ?, ?> result = myExecutorSvc.doExecution(
			workCursor,
			jobInstance,
			chunk,
			myAccumulator
		);

		// verify
		assertTrue(result.isIsSuccessful());
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
		} catch (Exception anythingElse) {
			fail(anythingElse.getMessage());
		}
	}

	private void runExceptionThrowingTest(Exception theExceptionToThrow) {
		// setup
		JobInstance jobInstance = getTestJobInstance();
		WorkChunk chunk = new WorkChunk();
		chunk.setId("chunkId");
		chunk.setData(new StepInputData());

		JobWorkCursor<TestJobParameters, StepInputData, StepOutputData> workCursor = mock(JobWorkCursor.class);

		JobDefinitionStep<TestJobParameters, StepInputData, StepOutputData> step = mockOutWorkCursor(StepType.INTERMEDIATE, workCursor);

		// when
		when(myNonReductionStep.run(any(), any()))
			.thenThrow(theExceptionToThrow);

		// test
		JobStepExecutorOutput<?, ?, ?> output = myExecutorSvc.doExecution(
			workCursor,
			jobInstance,
			chunk,
			myAccumulator
		);

		// verify
		assertFalse(output.isIsSuccessful());
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
			.reduceWorkChunksToSingleChunk(anyString(), anyList(), any());
	}

	private JobInstance getTestJobInstance() {
		JobInstance instance = new JobInstance();
		instance.setInstanceId(INSTANCE_ID);
		instance.setParameters(new TestJobParameters());

		return instance;
	}

	private WorkChunk createWorkChunk(String theId) {
		WorkChunk chunk = new WorkChunk();
		chunk.setInstanceId(INSTANCE_ID);
		chunk.setId(theId);
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
				.setParametersType(TestJobParameters.class)
				.addFirstStep(
					"step 1",
					"description 1",
					VoidModel.class,
					mock(IJobStepWorker.class) // we don't care about this step - we just need it
				)
				.addLastReducerStep(
					"step last",
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
