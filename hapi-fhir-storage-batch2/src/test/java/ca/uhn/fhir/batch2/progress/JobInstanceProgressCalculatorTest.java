package ca.uhn.fhir.batch2.progress;

import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.coordinator.BaseBatch2Test;
import ca.uhn.fhir.batch2.coordinator.JobCoordinatorImplTest;
import ca.uhn.fhir.batch2.coordinator.JobDefinitionRegistry;
import ca.uhn.fhir.batch2.maintenance.JobChunkProgressAccumulator;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.batch2.model.WorkChunkStatusEnum;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.interceptor.executor.InterceptorService;
import jakarta.annotation.Nonnull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JobInstanceProgressCalculatorTest extends BaseBatch2Test {

	private JobInstanceProgressCalculator mySvc;
	@Mock
	private IJobPersistence myJobPersistence;
	private JobChunkProgressAccumulator myProgressAccumulator = new JobChunkProgressAccumulator();
	private JobDefinitionRegistry myJobDefinitionRegistry = new JobDefinitionRegistry();
	private IInterceptorService myInterceptorSvc = new InterceptorService();

	@BeforeEach
	void before() {
		mySvc = new JobInstanceProgressCalculator(myJobPersistence,
			myProgressAccumulator,
			myJobDefinitionRegistry,
			myInterceptorSvc);
	}

	@Test
	void testCalculateProgress_AllStepsEqualWeight() {
		// Setup
		myJobDefinitionRegistry.addJobDefinition(createJobDefinitionWithReduction());

		JobInstance jobInstance = newJobInstance();

		mockFetchAndUpdateInstance(jobInstance);
		List<WorkChunk> chunks = new ArrayList<>();
		chunks.addAll(createWorkChunks(STEP_1, 2, WorkChunkStatusEnum.COMPLETED));
		chunks.addAll(createWorkChunks(STEP_2, 1, WorkChunkStatusEnum.COMPLETED));
		chunks.addAll(createWorkChunks(STEP_2, 1, WorkChunkStatusEnum.IN_PROGRESS));
		chunks.addAll(createWorkChunks(STEP_3, 2, WorkChunkStatusEnum.QUEUED));
		when(myJobPersistence.fetchAllWorkChunksIterator(eq(INSTANCE_ID), eq(false))).thenReturn(chunks.iterator());

		// Test
		mySvc.calculateAndStoreInstanceProgress(INSTANCE_ID);

		// Verify
		assertEquals(0.5, jobInstance.getProgress());
	}

	@Test
	void testCalculateProgress_FinalStepHoldsMinimalWeight() {
		// Setup
		myJobDefinitionRegistry.addJobDefinition(createJobDefinitionWithReduction(
			b -> b.setStepWeightForProgressCalculator(STEP_3, 0.01)
		));

		JobInstance jobInstance = newJobInstance();

		mockFetchAndUpdateInstance(jobInstance);
		List<WorkChunk> chunks = new ArrayList<>();
		// Most of the weight is on these steps, 80% are done
		chunks.addAll(createWorkChunks(STEP_1, 2, WorkChunkStatusEnum.COMPLETED));
		chunks.addAll(createWorkChunks(STEP_2, 6, WorkChunkStatusEnum.COMPLETED));
		chunks.addAll(createWorkChunks(STEP_2, 2, WorkChunkStatusEnum.IN_PROGRESS));
		// Little weight on this step
		chunks.addAll(createWorkChunks(STEP_3, 200, WorkChunkStatusEnum.QUEUED));
		when(myJobPersistence.fetchAllWorkChunksIterator(eq(INSTANCE_ID), eq(false))).thenReturn(chunks.iterator());

		// Test
		mySvc.calculateAndStoreInstanceProgress(INSTANCE_ID);

		// Verify
		assertEquals(0.792, jobInstance.getProgress(), 0.001);
	}

	@Test
	void testCalculateProgress_MiddleStepHasMinimalWeight() {
		// Setup
		myJobDefinitionRegistry.addJobDefinition(createJobDefinitionWithReduction(
			b -> b.setStepWeightForProgressCalculator(STEP_2, 0.01)
		));

		JobInstance jobInstance = newJobInstance();

		mockFetchAndUpdateInstance(jobInstance);
		List<WorkChunk> chunks = new ArrayList<>();
		chunks.addAll(createWorkChunks(STEP_1, 1, WorkChunkStatusEnum.QUEUED));
		chunks.addAll(createWorkChunks(STEP_1, 1, WorkChunkStatusEnum.COMPLETED));
		chunks.addAll(createWorkChunks(STEP_2, 5000, WorkChunkStatusEnum.IN_PROGRESS));
		chunks.addAll(createWorkChunks(STEP_2, 50, WorkChunkStatusEnum.COMPLETED));
		chunks.addAll(createWorkChunks(STEP_3, 1, WorkChunkStatusEnum.IN_PROGRESS));
		chunks.addAll(createWorkChunks(STEP_3, 1, WorkChunkStatusEnum.COMPLETED));
		when(myJobPersistence.fetchAllWorkChunksIterator(eq(INSTANCE_ID), eq(false))).thenReturn(chunks.iterator());

		// Test
		mySvc.calculateAndStoreInstanceProgress(INSTANCE_ID);

		// Verify
		assertEquals(0.495, jobInstance.getProgress(), 0.001);
	}

	


	private void mockFetchAndUpdateInstance(JobInstance jobInstance) {
		when(myJobPersistence.fetchInstance(eq(INSTANCE_ID))).thenReturn(Optional.of(jobInstance));
		when(myJobPersistence.updateInstance(eq(INSTANCE_ID), any())).thenAnswer(t -> t.getArgument(1, IJobPersistence.JobInstanceUpdateCallback.class).doUpdate(jobInstance));
	}

	private static List<WorkChunk> createWorkChunks(String theStepId, int theCount, WorkChunkStatusEnum theStatus) {
		List<WorkChunk> retVal = new ArrayList<>();
		for (int i = 0; i < theCount; i++) {
			WorkChunk chunk = switch (theStepId) {
				case STEP_1 -> JobCoordinatorImplTest.createWorkChunkStep1();
				case STEP_2 -> JobCoordinatorImplTest.createWorkChunkStep2();
				case STEP_3 -> JobCoordinatorImplTest.createWorkChunkStep3();
				default -> throw new IllegalArgumentException(theStepId);
			};
			chunk.setStatus(theStatus);
			retVal.add(chunk);
		}
		return retVal;
	}

	@Nonnull
	private static JobInstance newJobInstance() {
		JobInstance jobInstance = new JobInstance();
		jobInstance.setJobDefinitionId(REDUCTION_JOB_ID);
		jobInstance.setJobDefinitionVersion(1);
		return jobInstance;
	}

}
