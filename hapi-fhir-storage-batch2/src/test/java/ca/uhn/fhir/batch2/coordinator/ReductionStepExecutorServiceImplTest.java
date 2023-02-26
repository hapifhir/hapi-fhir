package ca.uhn.fhir.batch2.coordinator;

import ca.uhn.fhir.batch2.api.ChunkExecutionDetails;
import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.IReductionStepWorker;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.model.ChunkOutcome;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobDefinitionStep;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobWorkCursor;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.dao.tx.NonTransactionalHapiTransactionService;
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

import static ca.uhn.fhir.batch2.coordinator.WorkChunkProcessorTest.INSTANCE_ID;
import static ca.uhn.fhir.batch2.coordinator.WorkChunkProcessorTest.JOB_DEFINITION_ID;
import static ca.uhn.fhir.batch2.coordinator.WorkChunkProcessorTest.REDUCTION_STEP_ID;
import static ca.uhn.fhir.batch2.coordinator.WorkChunkProcessorTest.StepInputData;
import static ca.uhn.fhir.batch2.coordinator.WorkChunkProcessorTest.StepOutputData;
import static ca.uhn.fhir.batch2.coordinator.WorkChunkProcessorTest.TestJobParameters;
import static ca.uhn.fhir.batch2.coordinator.WorkChunkProcessorTest.createWorkChunk;
import static ca.uhn.fhir.batch2.coordinator.WorkChunkProcessorTest.getTestJobInstance;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReductionStepExecutorServiceImplTest {

	private final IHapiTransactionService myTransactionService = new NonTransactionalHapiTransactionService();
	@Mock
	private IJobPersistence myJobPersistence;
	@Mock
	private IReductionStepWorker<TestJobParameters, StepInputData, StepOutputData> myReductionStepWorker;
	//	@Mock
//	private JobDefinitionStep<TestJobParameters, StepInputData, StepOutputData> myPreviousStep;
//	@Mock
//	private JobDefinitionStep<TestJobParameters, StepInputData, StepOutputData> myCurrentStep;
	private ReductionStepExecutorServiceImpl mySvc;
	private JobDefinitionRegistry myJobDefinitionRegistry = new JobDefinitionRegistry();

	@BeforeEach
	public void before() {
		mySvc = new ReductionStepExecutorServiceImpl(myJobPersistence, myTransactionService, myJobDefinitionRegistry);
	}


	@Test
	public void doExecution_reductionWithChunkFailed_marksAllFutureChunksAsFailedButPreviousAsSuccess() {
		// setup
		List<String> chunkIds = Arrays.asList("chunk1", "chunk2");
		List<WorkChunk> chunks = new ArrayList<>();
		for (String id : chunkIds) {
			chunks.add(createWorkChunk(id));
		}
		JobInstance jobInstance = getTestJobInstance();
		jobInstance.setStatus(StatusEnum.IN_PROGRESS);
		JobWorkCursor<TestJobParameters, StepInputData, StepOutputData> workCursor = mock(JobWorkCursor.class);


		// when
		when(workCursor.getCurrentStep()).thenReturn((JobDefinitionStep<TestJobParameters, StepInputData, StepOutputData>) createJobDefinition().getSteps().get(1));
		when(workCursor.getJobDefinition()).thenReturn(createJobDefinition());
		when(myJobPersistence.fetchInstance(eq(INSTANCE_ID))).thenReturn(Optional.of(jobInstance));
		when(myJobPersistence.markInstanceAsStatus(eq(INSTANCE_ID), eq(StatusEnum.FINALIZE))).thenReturn(true);
		when(myJobPersistence.fetchAllWorkChunksForStepStream(eq(INSTANCE_ID), eq(REDUCTION_STEP_ID)))
			.thenReturn(chunks.stream());
		when(myReductionStepWorker.consume(any(ChunkExecutionDetails.class)))
			.thenReturn(ChunkOutcome.SUCCESS())
			.thenReturn(new ChunkOutcome(ChunkOutcome.Status.FAILED));

		// test
		ReductionStepChunkProcessingResponse result = mySvc.executeReductionStep(INSTANCE_ID, workCursor);

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
	public void doExecution_reductionStepWithValidInput_executesAsExpected() {
		// setup
		List<String> chunkIds = Arrays.asList("chunk1", "chunk2");
		List<WorkChunk> chunks = new ArrayList<>();
		for (String id : chunkIds) {
			chunks.add(createWorkChunk(id));
		}
		JobInstance jobInstance = getTestJobInstance();
		jobInstance.setStatus(StatusEnum.IN_PROGRESS);
		JobWorkCursor<TestJobParameters, StepInputData, StepOutputData> workCursor = mock(JobWorkCursor.class);


		// when
		when(workCursor.getCurrentStep()).thenReturn((JobDefinitionStep<TestJobParameters, StepInputData, StepOutputData>) createJobDefinition().getSteps().get(1));
		when(workCursor.getJobDefinition()).thenReturn(createJobDefinition());
		when(myJobPersistence.fetchInstance(eq(INSTANCE_ID))).thenReturn(Optional.of(jobInstance));
		when(myJobPersistence.markInstanceAsStatus(eq(INSTANCE_ID), eq(StatusEnum.FINALIZE))).thenReturn(true);
		when(myJobPersistence.fetchAllWorkChunksForStepStream(eq(INSTANCE_ID), eq(REDUCTION_STEP_ID)))
			.thenReturn(chunks.stream());
		when(myReductionStepWorker.consume(any(ChunkExecutionDetails.class)))
			.thenReturn(ChunkOutcome.SUCCESS());
		when(myReductionStepWorker.run(any(StepExecutionDetails.class), any(BaseDataSink.class)))
			.thenReturn(RunOutcome.SUCCESS);

		// test
		ReductionStepChunkProcessingResponse result = mySvc.executeReductionStep(INSTANCE_ID, workCursor);

		// verify
		ArgumentCaptor<ChunkExecutionDetails> chunkCaptor = ArgumentCaptor.forClass(ChunkExecutionDetails.class);
		verify(myReductionStepWorker, times(chunks.size()))
			.consume(chunkCaptor.capture());
		List<ChunkExecutionDetails> chunksSubmitted = chunkCaptor.getAllValues();
		assertEquals(chunks.size(), chunksSubmitted.size());
		for (ChunkExecutionDetails submitted : chunksSubmitted) {
			assertTrue(chunkIds.contains(submitted.getChunkId()));
		}

		assertTrue(result.isSuccessful());
		ArgumentCaptor<List<String>> chunkIdCaptor = ArgumentCaptor.forClass(List.class);
		verify(myJobPersistence).markWorkChunksWithStatusAndWipeData(eq(INSTANCE_ID),
			chunkIdCaptor.capture(), eq(StatusEnum.COMPLETED), eq(null));
		List<String> capturedIds = chunkIdCaptor.getValue();
		assertEquals(chunkIds.size(), capturedIds.size());
		for (String chunkId : chunkIds) {
			assertTrue(capturedIds.contains(chunkId));
		}

	}


	@Test
	public void doExecution_reductionStepWithErrors_returnsFalseAndMarksPreviousChunksFailed() {
		// setup
		List<String> chunkIds = Arrays.asList("chunk1", "chunk2");
		List<WorkChunk> chunks = new ArrayList<>();
		for (String id : chunkIds) {
			chunks.add(createWorkChunk(id));
		}
		JobInstance jobInstance = getTestJobInstance();
		jobInstance.setStatus(StatusEnum.IN_PROGRESS);
		JobWorkCursor<TestJobParameters, StepInputData, StepOutputData> workCursor = mock(JobWorkCursor.class);

		// when
		when(workCursor.getCurrentStep()).thenReturn((JobDefinitionStep<TestJobParameters, StepInputData, StepOutputData>) createJobDefinition().getSteps().get(1));
		when(workCursor.getJobDefinition()).thenReturn(createJobDefinition());
		when(myJobPersistence.fetchInstance(eq(INSTANCE_ID))).thenReturn(Optional.of(jobInstance));
		when(myJobPersistence.fetchAllWorkChunksForStepStream(eq(INSTANCE_ID), eq(REDUCTION_STEP_ID))).thenReturn(chunks.stream());
		when(myJobPersistence.markInstanceAsStatus(eq(INSTANCE_ID), eq(StatusEnum.FINALIZE))).thenReturn(true);
		doThrow(new RuntimeException("This is an error")).when(myReductionStepWorker).consume(any(ChunkExecutionDetails.class));

		// test
		ReductionStepChunkProcessingResponse result = mySvc.executeReductionStep(INSTANCE_ID, workCursor);

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
		verify(myReductionStepWorker, never())
			.run(any(), any());
	}

	@SuppressWarnings("unchecked")
	private JobDefinition<TestJobParameters> createJobDefinition() {
		return JobDefinition.newBuilder()
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
				myReductionStepWorker
			)
			.build();
	}


}
