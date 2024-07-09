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
import ca.uhn.fhir.batch2.model.WorkChunkStatusEnum;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.dao.tx.NonTransactionalHapiTransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
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
import static ca.uhn.fhir.batch2.model.StatusEnum.ERRORED;
import static ca.uhn.fhir.batch2.model.StatusEnum.IN_PROGRESS;
import static org.assertj.core.api.Assertions.assertThat;
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
	private ReductionStepExecutorServiceImpl mySvc;
	private final JobDefinitionRegistry myJobDefinitionRegistry = new JobDefinitionRegistry();

	@BeforeEach
	public void before() {
		mySvc = new ReductionStepExecutorServiceImpl(myJobPersistence, myTransactionService, myJobDefinitionRegistry);
	}

	// QUEUED, IN_PROGRESS are supported because of backwards compatibility
	// these statuses will stop being supported after 7.6
	@SuppressWarnings({"unchecked", "rawtypes"})
	@ParameterizedTest
	@EnumSource(value = WorkChunkStatusEnum.class, names = { "REDUCTION_READY", "QUEUED", "IN_PROGRESS" })
	public void doExecution_reductionWithChunkFailed_marksAllFutureChunksAsFailedButPreviousAsSuccess() {
		// setup
		List<String> chunkIds = Arrays.asList("chunk1", "chunk2");
		List<WorkChunk> chunks = new ArrayList<>();
		for (String id : chunkIds) {
			WorkChunk chunk = createWorkChunk(id);
			chunk.setStatus(WorkChunkStatusEnum.REDUCTION_READY);
			chunks.add(chunk);
		}
		JobInstance jobInstance = getTestJobInstance();
		jobInstance.setStatus(StatusEnum.IN_PROGRESS);
		JobWorkCursor<TestJobParameters, StepInputData, StepOutputData> workCursor = mock(JobWorkCursor.class);

		// when
		when(workCursor.getCurrentStep()).thenReturn((JobDefinitionStep<TestJobParameters, StepInputData, StepOutputData>) createJobDefinition().getSteps().get(1));
		when(workCursor.getJobDefinition()).thenReturn(createJobDefinition());
		when(myJobPersistence.fetchInstance(eq(INSTANCE_ID))).thenReturn(Optional.of(jobInstance));
		when(myJobPersistence.markInstanceAsStatusWhenStatusIn(INSTANCE_ID, StatusEnum.FINALIZE, EnumSet.of(StatusEnum.IN_PROGRESS, StatusEnum.ERRORED))).thenReturn(true);
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
		ArgumentCaptor<WorkChunkStatusEnum> statusCaptor = ArgumentCaptor.forClass(WorkChunkStatusEnum.class);
		verify(myJobPersistence, times(chunkIds.size()))
			.markWorkChunksWithStatusAndWipeData(
				eq(INSTANCE_ID),
				submittedListIds.capture(),
				statusCaptor.capture(),
				any()
			);
		assertThat(submittedListIds.getAllValues()).hasSize(2);
		List<String> list1 = submittedListIds.getAllValues().get(0);
		List<String> list2 = submittedListIds.getAllValues().get(1);
		assertThat(list1).contains(chunkIds.get(0));
		assertThat(list2).contains(chunkIds.get(1));

		// assumes the order of which is called first
		// successes, then failures
		assertThat(statusCaptor.getAllValues()).hasSize(2);
		List<WorkChunkStatusEnum> statuses = statusCaptor.getAllValues();
		assertEquals(WorkChunkStatusEnum.COMPLETED, statuses.get(0));
		assertEquals(WorkChunkStatusEnum.FAILED, statuses.get(1));
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	@Test
	public void doExecution_reductionStepWithValidInput_executesAsExpected() {
		// setup
		List<String> chunkIds = Arrays.asList("chunk1", "chunk2");
		List<WorkChunk> chunks = new ArrayList<>();
		for (String id : chunkIds) {
			WorkChunk chunk = createWorkChunk(id);
			chunk.setStatus(WorkChunkStatusEnum.REDUCTION_READY);
			chunks.add(chunk);
		}
		JobInstance jobInstance = getTestJobInstance();
		jobInstance.setStatus(StatusEnum.IN_PROGRESS);
		JobWorkCursor<TestJobParameters, StepInputData, StepOutputData> workCursor = mock(JobWorkCursor.class);

		// when
		when(workCursor.getCurrentStep()).thenReturn((JobDefinitionStep<TestJobParameters, StepInputData, StepOutputData>) createJobDefinition().getSteps().get(1));
		when(workCursor.getJobDefinition()).thenReturn(createJobDefinition());
		when(myJobPersistence.fetchInstance(eq(INSTANCE_ID))).thenReturn(Optional.of(jobInstance));
		when(myJobPersistence.markInstanceAsStatusWhenStatusIn(INSTANCE_ID, StatusEnum.FINALIZE, EnumSet.of(IN_PROGRESS, ERRORED))).thenReturn(true);
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
		assertThat(chunksSubmitted).hasSize(chunks.size());
		for (ChunkExecutionDetails submitted : chunksSubmitted) {
			assertThat(chunkIds).contains(submitted.getChunkId());
		}

		assertTrue(result.isSuccessful());
		ArgumentCaptor<List<String>> chunkIdCaptor = ArgumentCaptor.forClass(List.class);
		verify(myJobPersistence).markWorkChunksWithStatusAndWipeData(eq(INSTANCE_ID),
			chunkIdCaptor.capture(), eq(WorkChunkStatusEnum.COMPLETED), eq(null));
		List<String> capturedIds = chunkIdCaptor.getValue();
		assertThat(capturedIds).hasSize(chunkIds.size());
		for (String chunkId : chunkIds) {
			assertThat(capturedIds).contains(chunkId);
		}

	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	@Test
	public void doExecution_reductionStepWithErrors_returnsFalseAndMarksPreviousChunksFailed() {
		// setup
		List<String> chunkIds = Arrays.asList("chunk1", "chunk2");
		List<WorkChunk> chunks = new ArrayList<>();
		for (String id : chunkIds) {
			WorkChunk chunk = createWorkChunk(id);
			// reduction steps are done with REDUCTION_READY workchunks
			chunk.setStatus(WorkChunkStatusEnum.REDUCTION_READY);
			chunks.add(chunk);
		}
		JobInstance jobInstance = getTestJobInstance();
		jobInstance.setStatus(StatusEnum.IN_PROGRESS);
		JobWorkCursor<TestJobParameters, StepInputData, StepOutputData> workCursor = mock(JobWorkCursor.class);

		// when
		when(workCursor.getCurrentStep()).thenReturn((JobDefinitionStep<TestJobParameters, StepInputData, StepOutputData>) createJobDefinition().getSteps().get(1));
		when(workCursor.getJobDefinition()).thenReturn(createJobDefinition());
		when(myJobPersistence.fetchInstance(eq(INSTANCE_ID))).thenReturn(Optional.of(jobInstance));
		when(myJobPersistence.fetchAllWorkChunksForStepStream(eq(INSTANCE_ID), eq(REDUCTION_STEP_ID))).thenReturn(chunks.stream());
		when(myJobPersistence.markInstanceAsStatusWhenStatusIn(INSTANCE_ID, StatusEnum.FINALIZE, EnumSet.of(StatusEnum.IN_PROGRESS, StatusEnum.ERRORED))).thenReturn(true);
		doThrow(new RuntimeException("This is an error")).when(myReductionStepWorker).consume(any(ChunkExecutionDetails.class));

		// test
		ReductionStepChunkProcessingResponse result = mySvc.executeReductionStep(INSTANCE_ID, workCursor);

		// verify
		assertFalse(result.isSuccessful());
		ArgumentCaptor<String> chunkIdCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<String> errorCaptor = ArgumentCaptor.forClass(String.class);
		verify(myJobPersistence, times(chunkIds.size()))
			.onWorkChunkFailed(chunkIdCaptor.capture(), errorCaptor.capture());
		List<String> chunkIdsCaptured = chunkIdCaptor.getAllValues();
		List<String> errorsCaptured = errorCaptor.getAllValues();
		for (int i = 0; i < chunkIds.size(); i++) {
			String cId = chunkIdsCaptured.get(i);
			String error = errorsCaptured.get(i);

			assertThat(chunkIds).contains(cId);
			assertThat(error).contains("Reduction step failed to execute chunk reduction for chunk");
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
