package ca.uhn.fhir.jpa.batch2.jobs.term.loinc;

import ca.uhn.fhir.batch2.api.ChunkExecutionDetails;
import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobStepExecutionServices;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ImportTerminologyResultJson;
import ca.uhn.fhir.rest.api.PatchTypeEnum;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ImportLoincStep21FinalizeTest {

	private static final Logger ourLog = LoggerFactory.getLogger(ImportLoincStep21FinalizeTest.class);
	@Mock
	private IFhirResourceDao<ValueSet> myValueSetDao;
	@Mock
	private DaoRegistry myDaoRegistry;
	@Mock
	private IJobStepExecutionServices myStepExecutionSvcs;
	@Mock
	private IJobDataSink<ImportTerminologyResultJson> myDataSink;
	@InjectMocks
	private ImportLoincStep21Finalize myStep;
	@Captor
	private ArgumentCaptor<ImportTerminologyResultJson> myDataCaptor;
	@Captor
	private ArgumentCaptor<IIdType> myIdCaptor;
	@Captor
	private ArgumentCaptor<String> myPatchBodyCaptor;

	@Test
	void testProcess_GenerateReport() {

		// Test
		ImportLoincJobParameters parameters = new ImportLoincJobParameters();
		ImportLoincFileSetJson data = new ImportLoincFileSetJson();
		data.getRecordsAddedCounter("import-concepts").incrementConceptsAdded(1);
		data.getRecordsAddedCounter("import-hierarchy").incrementConceptsAdded(2);
		data.getRecordsAddedCounter("import-answer-lists").incrementConceptsAdded(3);
		myStep.consume(new ChunkExecutionDetails<>(data, parameters, "instance-id", "chunk-id"));

		data = new ImportLoincFileSetJson();
		data.getRecordsAddedCounter("import-concepts").incrementConceptsAdded(2);
		data.getRecordsAddedCounter("import-hierarchy").incrementConceptsAdded(3);
		data.getRecordsAddedCounter("import-answer-lists").incrementConceptsAdded(4);
		myStep.consume(new ChunkExecutionDetails<>(data, parameters, "instance-id", "chunk-id"));

		JobDefinition<ImportLoincJobParameters> jobDefinition = new ImportLoincJobAppCtx(myDaoRegistry).importLoincJobDefinition();
		myStep.run(new StepExecutionDetails<>(parameters, null, new JobInstance(), new WorkChunk(), myStepExecutionSvcs, jobDefinition, null, null), myDataSink);

		// Verify
		verify(myDataSink, times(1)).accept(myDataCaptor.capture());
		ImportTerminologyResultJson result = myDataCaptor.getValue();
		String report = result.getReport();
		ourLog.info("Report:\n{}", report);

		assertThat(report).containsSubsequence(
			"Concepts Added             : 15",
			"Step: import-concepts (Import LOINC concepts)",
			"   Concepts Added             : 3"
		);
	}

	@Test
	void testProcess_ActivateValueSets() {
		// Setup
		when(myDaoRegistry.getFhirContext()).thenReturn(FhirContext.forR4Cached());
		when(myDaoRegistry.getResourceDao(eq("ValueSet"))).thenReturn(myValueSetDao);

		// Test
		ImportLoincJobParameters parameters = new ImportLoincJobParameters();
		ImportLoincFileSetJson data = new ImportLoincFileSetJson();
		data.addResourceToActivate("ValueSet/A");
		myStep.consume(new ChunkExecutionDetails<>(data, parameters, "instance-id", "chunk-id"));

		data = new ImportLoincFileSetJson();
		data.addResourceToActivate("ValueSet/A");
		data.addResourceToActivate("ValueSet/B");
		myStep.consume(new ChunkExecutionDetails<>(data, parameters, "instance-id", "chunk-id"));

		JobDefinition<ImportLoincJobParameters> jobDefinition = new ImportLoincJobAppCtx(myDaoRegistry).importLoincJobDefinition();
		myStep.run(new StepExecutionDetails<>(parameters, null, new JobInstance(), new WorkChunk(), myStepExecutionSvcs, jobDefinition, null, null), myDataSink);

		// Verify
		verify(myValueSetDao, times(2)).patch(myIdCaptor.capture(), isNull(), eq(PatchTypeEnum.FHIR_PATCH_JSON), myPatchBodyCaptor.capture(), any(), any());

		assertThat(myIdCaptor.getAllValues().stream().map(IIdType::getValue)).containsExactlyInAnyOrder("ValueSet/A", "ValueSet/B");

		String expectedPatchBody = "{\"resourceType\":\"Parameters\",\"parameter\":[{\"name\":\"operation\",\"part\":[{\"name\":\"type\",\"valueString\":\"replace\"},{\"name\":\"path\",\"valueString\":\"status\"},{\"name\":\"value\",\"valueCode\":\"active\"}]}]}";
		assertEquals(expectedPatchBody, myPatchBodyCaptor.getAllValues().get(0));
	}

}
