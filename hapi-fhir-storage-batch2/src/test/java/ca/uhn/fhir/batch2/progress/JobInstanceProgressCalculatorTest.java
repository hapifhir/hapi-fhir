package ca.uhn.fhir.batch2.progress;

import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.IReductionStepWorker;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.coordinator.BaseBatch2Test;
import ca.uhn.fhir.batch2.coordinator.JobCoordinatorImplTest;
import ca.uhn.fhir.batch2.coordinator.JobDefinitionRegistry;
import ca.uhn.fhir.batch2.maintenance.JobChunkProgressAccumulator;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.batch2.model.WorkChunkStatusEnum;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.interceptor.executor.InterceptorService;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
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
	private final JobChunkProgressAccumulator myProgressAccumulator = new JobChunkProgressAccumulator();
	private final JobDefinitionRegistry myJobDefinitionRegistry = new JobDefinitionRegistry();
	private final IInterceptorService myInterceptorSvc = new InterceptorService();
	@Mock
	private IJobStepWorker<MyJsonModel, VoidModel, MyJsonModel> myFirstStep;
	@Mock
	private IJobStepWorker<MyJsonModel, MyJsonModel, MyJsonModel> myIntermediateStep;
	@Mock
	private IJobStepWorker<MyJsonModel, MyJsonModel, VoidModel> myLastStep;

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
	void testCalculateProgress_MultipleStepsHaveMinimalWeight() {
		// Setup
		myJobDefinitionRegistry.addJobDefinition(createJobDefinitionWithReduction(
			b -> b.setStepWeightForProgressCalculator(STEP_1, 0.01),
			b -> b.setStepWeightForProgressCalculator(STEP_2, 0.01)
		));

		JobInstance jobInstance = newJobInstance();

		mockFetchAndUpdateInstance(jobInstance);
		List<WorkChunk> chunks = new ArrayList<>();
		chunks.addAll(createWorkChunks(STEP_1, 5000, WorkChunkStatusEnum.QUEUED));
		chunks.addAll(createWorkChunks(STEP_1, 50, WorkChunkStatusEnum.COMPLETED));
		chunks.addAll(createWorkChunks(STEP_2, 5000, WorkChunkStatusEnum.IN_PROGRESS));
		chunks.addAll(createWorkChunks(STEP_2, 50, WorkChunkStatusEnum.COMPLETED));
		// Only this step has normal weight
		chunks.addAll(createWorkChunks(STEP_3, 1, WorkChunkStatusEnum.IN_PROGRESS));
		chunks.addAll(createWorkChunks(STEP_3, 1, WorkChunkStatusEnum.COMPLETED));
		when(myJobPersistence.fetchAllWorkChunksIterator(eq(INSTANCE_ID), eq(false))).thenReturn(chunks.iterator());

		// Test
		mySvc.calculateAndStoreInstanceProgress(INSTANCE_ID);

		// Verify
		assertEquals(0.49, jobInstance.getProgress(), 0.001);
	}


	@Test
	void testCalculateProgress_NoChunks() {
		// Setup
		myJobDefinitionRegistry.addJobDefinition(createJobDefinitionWithReduction(
			b -> b.setStepWeightForProgressCalculator(STEP_2, 0.01)
		));

		JobInstance jobInstance = newJobInstance();

		mockFetchAndUpdateInstance(jobInstance);
		List<WorkChunk> chunks = List.of();
		when(myJobPersistence.fetchAllWorkChunksIterator(eq(INSTANCE_ID), eq(false))).thenReturn(chunks.iterator());

		// Test
		mySvc.calculateAndStoreInstanceProgress(INSTANCE_ID);

		// Verify
		assertEquals(0.0, jobInstance.getProgress(), 0.001);
	}



	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	void tesCalculateProgress_WeightedStepWithNoChunksPassedToIt(boolean theGatedJob) {

		JobDefinition<MyJsonModel> jobDefinition = JobDefinition.newBuilder()
			.setJobDefinitionId("TEST_JOB")
			.setJobDescription("Import Terminology - LOINC")
			.setJobDefinitionVersion(1)
			.gatedExecution(theGatedJob)
			.setParametersType(MyJsonModel.class)
			.setParametersValidator(new MyJsonModelValidator())
			.addFirstStep(
				STEP_1,
				"Step 1",
				MyJsonModel.class,
				myFirstStep)
			.addIntermediateStep(
				STEP_2,
				"Import LOINC concepts",
				MyJsonModel.class,
				myIntermediateStep)
			.setStepWeightForProgressCalculator(STEP_2, 0.5)
			.addLastStep(
				STEP_3,
				"Step 3",
				myLastStep)
			.build();
		myJobDefinitionRegistry.addJobDefinition(jobDefinition);

		JobInstance jobInstance = newJobInstance();
		jobInstance.setJobDefinitionId(jobDefinition.getJobDefinitionId());
		jobInstance.setJobDefinitionVersion(jobDefinition.getJobDefinitionVersion());
		jobInstance.setCurrentGatedStepId(STEP_3);
		mockFetchAndUpdateInstance(jobInstance);

		// No chunks in step 2, and we're already in step 3
		List<WorkChunk> chunks = new ArrayList<>();
		chunks.addAll(createWorkChunks(STEP_1, 2, WorkChunkStatusEnum.COMPLETED));
		chunks.addAll(createWorkChunks(STEP_3, 2, WorkChunkStatusEnum.IN_PROGRESS));
		chunks.addAll(createWorkChunks("import-univeral-lab-orderset", 1, WorkChunkStatusEnum.COMPLETED));
		when(myJobPersistence.fetchAllWorkChunksIterator(eq(INSTANCE_ID), eq(false))).thenReturn(chunks.iterator());

		// Test
		mySvc.calculateAndStoreInstanceProgress(INSTANCE_ID);

		// Verify

		// 50% of progress is the second step which had a 0.5 weighting and is complete
		// despite having no chunks. The other 50% is the other two steps, which have equal
		// parts COMPLETE and IN_PROGRESS, so the total is 75%.
		assertEquals(0.75, jobInstance.getProgress(), 0.001);

	}
	


	private void mockFetchAndUpdateInstance(JobInstance jobInstance) {
		when(myJobPersistence.fetchInstance(eq(INSTANCE_ID))).thenReturn(Optional.of(jobInstance));
		when(myJobPersistence.updateInstance(eq(INSTANCE_ID), any())).thenAnswer(t -> t.getArgument(1, IJobPersistence.JobInstanceUpdateCallback.class).doUpdate(jobInstance));
	}

	private static List<WorkChunk> createWorkChunks(String theStepId, int theCount, WorkChunkStatusEnum theStatus) {
		List<WorkChunk> retVal = new ArrayList<>();
		for (int i = 0; i < theCount; i++) {
			WorkChunk chunk = JobCoordinatorImplTest.createWorkChunk(theStepId, null);
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

	private class MyJsonModel implements IModelJson {
	}

	private class MyJsonModelValidator implements ca.uhn.fhir.batch2.api.IJobParametersValidator<MyJsonModel> {
		@Nullable
		@Override
		public List<String> validate(RequestDetails theRequestDetails, @Nonnull MyJsonModel theParameters) {
			return List.of();
		}
	}
}
