package ca.uhn.fhir.jpa.batch2.jobs.term.loinc;

import ca.uhn.fhir.batch2.api.ChunkExecutionDetails;
import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobStepExecutionServices;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.model.BatchInstanceStepStatisticsDTO;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ImportTerminologyJobParameters;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ImportTerminologyResultJson;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ImportTerminologyStepFinalize;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyFileSetJson;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemStorageSvc;
import ca.uhn.fhir.rest.api.PatchTypeEnum;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ImportTerminologyStepFinalizeTest extends BaseImportLoincStepTest {

	private static final Logger ourLog = LoggerFactory.getLogger(ImportTerminologyStepFinalizeTest.class);
	@Mock
	private IJobStepExecutionServices myStepExecutionSvc;
	@Mock
	private ITermCodeSystemStorageSvc myTermCodeSystemStorageSvc;
	@Mock
	private IJobDataSink<ImportTerminologyResultJson> myDataSink;
	@InjectMocks
	private ImportTerminologyStepFinalize<ImportTerminologyJobParameters> myStep;
	@Captor
	private ArgumentCaptor<ImportTerminologyResultJson> myDataCaptor;
	@Captor
	private ArgumentCaptor<IIdType> myIdCaptor;
	@Captor
	private ArgumentCaptor<String> myPatchBodyCaptor;

	@Test
	void testProcess_GenerateReport() {
		mockFetchJobMetadataAttachment();
		when(myJobPersistence.calculateStepStatistics(eq("my-instance-id"))).thenReturn(new BatchInstanceStepStatisticsDTO(Map.of(
			"import-concepts", new BatchInstanceStepStatisticsDTO.StepStatistics(10, 20),
			"import-hierarchy", new BatchInstanceStepStatisticsDTO.StepStatistics(20, 30),
			"import-answer-lists", new BatchInstanceStepStatisticsDTO.StepStatistics(40, 50)
		)));

		// Test
		ImportTerminologyJobParameters parameters = new ImportTerminologyJobParameters();
		TerminologyFileSetJson data = newData();
		data.getRecordsAddedCounter("import-concepts").incrementConceptsAdded(1);
		data.getRecordsAddedCounter("import-hierarchy").incrementConceptsAdded(2);
		data.getRecordsAddedCounter("import-answer-lists").incrementConceptsAdded(3);
		myStep.consume(new ChunkExecutionDetails<>(data, parameters, "my-instance-id", "chunk-id"));

		data = newData();
		data.getRecordsAddedCounter("import-concepts").incrementConceptsAdded(2);
		data.getRecordsAddedCounter("import-hierarchy").incrementConceptsAdded(3);
		data.getRecordsAddedCounter("import-answer-lists").incrementConceptsAdded(4);
		myStep.consume(new ChunkExecutionDetails<>(data, parameters, "my-instance-id", "chunk-id"));

		JobDefinition<ImportTerminologyJobParameters> jobDefinition = new ImportLoincJobAppCtx(myDaoRegistry, myTermCodeSystemStorageSvc, myJobPersistence, myTransactionService).importLoincJobDefinition();
		JobInstance instance = new JobInstance();
		instance.setInstanceId("my-instance-id");
		myStep.run(new StepExecutionDetails<>(parameters, null, instance, new WorkChunk(), myStepExecutionSvc, jobDefinition, null, null), myDataSink);

		// Verify
		verify(myDataSink, times(1)).accept(myDataCaptor.capture());
		ImportTerminologyResultJson result = myDataCaptor.getValue();
		String report = result.getReport();
		ourLog.info("Report:\n{}", report);

		assertThat(report).containsSubsequence(
			"Concepts Added             : 15",
			"Step: import-concepts (Import LOINC concepts)",
			"   Total Work Chunks          : 10",
			"   Total Processing Time      : 20ms",
			"   Concepts Added             : 3"
		);
	}

	@Test
	void testProcess_ActivateValueSets() {
		// Setup
		when(myJobPersistence.calculateStepStatistics(any())).thenReturn(new BatchInstanceStepStatisticsDTO(Map.of()));
		mockFetchJobMetadataAttachment();
		when(myDaoRegistry.getFhirContext()).thenReturn(FhirContext.forR4Cached());
		when(myDaoRegistry.getResourceDao(eq("ValueSet"))).thenReturn(myValueSetDao);

		// Test
		ImportTerminologyJobParameters parameters = new ImportTerminologyJobParameters();
		TerminologyFileSetJson data = newData();
		data.addResourceToActivate("ValueSet/A");
		myStep.consume(new ChunkExecutionDetails<>(data, parameters, "instance-id", "chunk-id"));

		data = newData();
		data.addResourceToActivate("ValueSet/A");
		data.addResourceToActivate("ValueSet/B");
		myStep.consume(new ChunkExecutionDetails<>(data, parameters, "instance-id", "chunk-id"));

		JobDefinition<ImportTerminologyJobParameters> jobDefinition = new ImportLoincJobAppCtx(myDaoRegistry, myTermCodeSystemStorageSvc, myJobPersistence, myTransactionService).importLoincJobDefinition();
		JobInstance instance = new JobInstance();
		instance.setInstanceId("my-instance-id");
		myStep.run(new StepExecutionDetails<>(parameters, null, instance, new WorkChunk(), myStepExecutionSvc, jobDefinition, null, null), myDataSink);

		// Verify
		verify(myValueSetDao, times(2)).patch(myIdCaptor.capture(), isNull(), eq(PatchTypeEnum.FHIR_PATCH_JSON), myPatchBodyCaptor.capture(), any(), any());

		assertThat(myIdCaptor.getAllValues().stream().map(IIdType::getValue)).containsExactlyInAnyOrder("ValueSet/A", "ValueSet/B");

		String expectedPatchBody = "{\"resourceType\":\"Parameters\",\"parameter\":[{\"name\":\"operation\",\"part\":[{\"name\":\"type\",\"valueString\":\"replace\"},{\"name\":\"path\",\"valueString\":\"status\"},{\"name\":\"value\",\"valueCode\":\"active\"}]}]}";
		assertEquals(expectedPatchBody, myPatchBodyCaptor.getAllValues().get(0));
	}

}
