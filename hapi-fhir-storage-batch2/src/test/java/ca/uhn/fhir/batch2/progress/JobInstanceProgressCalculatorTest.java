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
	private IReductionStepWorker<MyJsonModel, MyJsonModel, MyJsonModel> myFinalStep;

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



	@Test
	void tesCalculateProgress_() {

		JobDefinition<MyJsonModel> jobDefinition = JobDefinition.newBuilder()
			.setJobDefinitionId("TEST_JOB")
			.setJobDescription("Import Terminology - LOINC")
			.setJobDefinitionVersion(1)
			.gatedExecution()
			.setParametersType(MyJsonModel.class)
			.setParametersValidator(new MyJsonModelValidator())
			.addFirstStep(
				"expand-zip",
				"Expand LOINC distribution",
				MyJsonModel.class,
				myFirstStep)
			.addIntermediateStep(
				"import-concepts",
				"Import LOINC concepts",
				MyJsonModel.class,
				myIntermediateStep)
			.addIntermediateStep(
				"import-hierarchy-concepts",
				"Import LOINC hierarchy Concepts",
				MyJsonModel.class,
				myIntermediateStep)
			.addIntermediateStep(
				"import-hierarchy",
				"Import LOINC hierarchy",
				MyJsonModel.class,
				myIntermediateStep)
			.addIntermediateStep(
				"import-answer-lists",
				"Import LOINC answer lists",
				MyJsonModel.class,
				myIntermediateStep)
			.addIntermediateStep(
				"import-answer-list-links",
				"Import LOINC answer list links",
				MyJsonModel.class,
				myIntermediateStep)
			.addIntermediateStep(
				"import-rsna-playbook",
				"Import LOINC RSNA playbook",
				MyJsonModel.class,
				myIntermediateStep)
			.addIntermediateStep(
				"import-part-related-code-mapping",
				"Import LOINC Part Related Code Mappings",
				MyJsonModel.class,
				myIntermediateStep)
			.addIntermediateStep(
				"import-document-ontology",
				"Import LOINC Document Ontology",
				MyJsonModel.class,
				myIntermediateStep)
			.addIntermediateStep(
				"import-univeral-lab-orderset",
				"Import LOINC Lab Order Set",
				MyJsonModel.class,
				myIntermediateStep)
			.addIntermediateStep(
				"import-ieee-medical-device-code",
				"Import LOINC IEEE Medical Device Codes",
				MyJsonModel.class,
				myIntermediateStep)
			.addIntermediateStep(
				"import-imaging-document-code",
				"Import LOINC Imaging Document Codes",
				MyJsonModel.class,
				myIntermediateStep)
			.addIntermediateStep(
				"import-group-file",
				"Import LOINC Group File",
				MyJsonModel.class,
				myIntermediateStep)
			.addIntermediateStep(
				"import-group-terms-file",
				"Import LOINC Group Terms File",
				MyJsonModel.class,
				myIntermediateStep)
			.addIntermediateStep(
				"import-parent-group-file",
				"Import LOINC Parent Group File",
				MyJsonModel.class,
				myIntermediateStep)
			.addIntermediateStep(
				"import-part-file",
				"Import LOINC Part File",
				MyJsonModel.class,
				myIntermediateStep)
			.addIntermediateStep(
				"import-part-link-file",
				"Import LOINC Part Link File",
				MyJsonModel.class,
				myIntermediateStep)
			// Part link file uses a large number of smaller files, so limit its weight
			.setStepWeightForProgressCalculator("import-part-link-file", 0.1)
			.addIntermediateStep(
				"import-consumer-name",
				"Import LOINC Consumer Names",
				MyJsonModel.class,
				myIntermediateStep)
			.addIntermediateStep(
				"import-coding-properties",
				"Import LOINC Coding Properties",
				MyJsonModel.class,
				myIntermediateStep)
			.addIntermediateStep(
				"import-linguistic-variant",
				"Import LOINC Linguistic Variants",
				MyJsonModel.class,
				myIntermediateStep)
			.addIntermediateStep(
				"chunk-concepts-for-closure-generation",
				"Create work chunks for calculating concept closures",
				MyJsonModel.class,
				myIntermediateStep)
			.addIntermediateStep(
				"generate-concept-closures",
				"Generate concept closures",
				MyJsonModel.class,
				myIntermediateStep)
			.setStepWeightForProgressCalculator("generate-concept-closures", 0.3)
			.addFinalReducerStep(
				"finalize-import",
				"Finalize LOINC Import",
				MyJsonModel.class,
				myFinalStep)
			.setStepWeightForProgressCalculator("finalize-import", 0.01)
			.build();
		myJobDefinitionRegistry.addJobDefinition(jobDefinition);

		JobInstance jobInstance = newJobInstance();
		jobInstance.setJobDefinitionId(jobDefinition.getJobDefinitionId());
		jobInstance.setJobDefinitionVersion(jobDefinition.getJobDefinitionVersion());
		mockFetchAndUpdateInstance(jobInstance);

		List<WorkChunk> chunks = new ArrayList<>();
		chunks.addAll(createWorkChunks("chunk-concepts-for-closure-generation", 1, WorkChunkStatusEnum.COMPLETED));
		chunks.addAll(createWorkChunks("expand-zip", 1, WorkChunkStatusEnum.COMPLETED));
		chunks.addAll(createWorkChunks("finalize-import", 0, WorkChunkStatusEnum.COMPLETED));
		chunks.addAll(createWorkChunks("generate-concept-closures", 127, WorkChunkStatusEnum.COMPLETED));
		chunks.addAll(createWorkChunks("import-answer-list-links", 1, WorkChunkStatusEnum.COMPLETED));
		chunks.addAll(createWorkChunks("import-answer-lists", 1, WorkChunkStatusEnum.COMPLETED));
		chunks.addAll(createWorkChunks("import-coding-properties", 0, WorkChunkStatusEnum.COMPLETED));
		chunks.addAll(createWorkChunks("import-concepts", 110, WorkChunkStatusEnum.COMPLETED));
		chunks.addAll(createWorkChunks("import-consumer-name", 68, WorkChunkStatusEnum.COMPLETED));
		chunks.addAll(createWorkChunks("import-document-ontology", 1, WorkChunkStatusEnum.COMPLETED));
		chunks.addAll(createWorkChunks("import-group-file", 1, WorkChunkStatusEnum.COMPLETED));
		chunks.addAll(createWorkChunks("import-group-terms-file", 1, WorkChunkStatusEnum.COMPLETED));
		chunks.addAll(createWorkChunks("import-hierarchy", 135, WorkChunkStatusEnum.COMPLETED));
		chunks.addAll(createWorkChunks("import-hierarchy-concepts", 135, WorkChunkStatusEnum.COMPLETED));
		chunks.addAll(createWorkChunks("import-ieee-medical-device-code", 1, WorkChunkStatusEnum.COMPLETED));
		chunks.addAll(createWorkChunks("import-imaging-document-code", 1, WorkChunkStatusEnum.COMPLETED));
		chunks.addAll(createWorkChunks("import-linguistic-variant", 0, WorkChunkStatusEnum.COMPLETED));
		chunks.addAll(createWorkChunks("import-parent-group-file", 1, WorkChunkStatusEnum.COMPLETED));
		chunks.addAll(createWorkChunks("import-part-file", 2, WorkChunkStatusEnum.COMPLETED));
		chunks.addAll(createWorkChunks("import-part-link-file", 1900, WorkChunkStatusEnum.IN_PROGRESS));
		chunks.addAll(createWorkChunks("import-part-link-file", 88, WorkChunkStatusEnum.COMPLETED));
		chunks.addAll(createWorkChunks("import-part-related-code-mapping", 1, WorkChunkStatusEnum.COMPLETED));
		chunks.addAll(createWorkChunks("import-rsna-playbook", 1, WorkChunkStatusEnum.COMPLETED));
		chunks.addAll(createWorkChunks("import-univeral-lab-orderset", 1, WorkChunkStatusEnum.COMPLETED));
		when(myJobPersistence.fetchAllWorkChunksIterator(eq(INSTANCE_ID), eq(false))).thenReturn(chunks.iterator());

		// Test
		mySvc.calculateAndStoreInstanceProgress(INSTANCE_ID);

		// Verify
		assertEquals(0.894, jobInstance.getProgress(), 0.001);


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
