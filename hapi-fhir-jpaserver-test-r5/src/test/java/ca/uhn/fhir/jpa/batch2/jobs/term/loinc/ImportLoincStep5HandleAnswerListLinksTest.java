package ca.uhn.fhir.jpa.batch2.jobs.term.loinc;

import ca.uhn.fhir.batch2.api.AttachmentContentTypeEnum;
import ca.uhn.fhir.batch2.api.AttachmentDetails;
import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.api.IJobStepExecutionServices;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyFileSetJson;
import ca.uhn.fhir.jpa.term.UploadStatistics;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemStorageSvc;
import ca.uhn.fhir.util.ClasspathUtil;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.IdType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ImportLoincStep5HandleAnswerListLinksTest {

	@Mock
	private IJobPersistence myJobPersistence;
	@Mock
	private ITermCodeSystemStorageSvc myTermCodeSystemStorageSvc;
	@Mock
	private IJobDataSink<ImportLoincFileSetJson> myDataSink;
	@Mock
	private IJobStepExecutionServices myJobExecutionServices;
	@Mock
	private JobDefinition<ImportLoincJobParameters> myJobDefinition;

	@InjectMocks
	private ImportLoincStep5HandleAnswerListLinks mySvc;

	@Captor
	private ArgumentCaptor<ImportLoincFileSetJson> myFileSetCaptor;
	@Captor
	private ArgumentCaptor<IBaseResource> myCodeSystemCaptor;


	@Test
	void run_LoadCodes() {
		// Setup
		when(myJobPersistence.fetchAttachmentById(eq("my-instance-id"), eq("my-chunk-attachment-id"))).thenReturn(new AttachmentDetails(ClasspathUtil.loadResourceAsStream("loinc-ver/v269/AccessoryFiles/AnswerFile/LoincAnswerListLink.csv"), AttachmentContentTypeEnum.CSV, "Loinc.csv"));
		when(myTermCodeSystemStorageSvc.uploadCodeSystemConcepts(any())).thenReturn(new UploadStatistics(new IdType()));

		// Test
		JobInstance instance = new JobInstance();
		instance.setInstanceId("my-instance-id");

		ImportLoincFileSetJson importLoincFileSetJson = new ImportLoincFileSetJson();
		importLoincFileSetJson.setChunkForCurrentStep(new TerminologyFileSetJson.Chunk("file.csv", "my-chunk-attachment-id"));
		importLoincFileSetJson.setLoincCodeSystemXml(ClasspathUtil.loadResource("loinc-ver/v269/loinc.xml"));

		StepExecutionDetails<ImportLoincJobParameters, ImportLoincFileSetJson> stepExecutionDetails = new StepExecutionDetails<>(new ImportLoincJobParameters(), importLoincFileSetJson, instance, new WorkChunk(), myJobExecutionServices, myJobDefinition, "step-1", "step-2");

		mySvc.run(stepExecutionDetails, myDataSink);

		// Verify
		verify(myTermCodeSystemStorageSvc, times(1)).uploadCodeSystemConcepts(myCodeSystemCaptor.capture());
		CodeSystem cs = (CodeSystem) myCodeSystemCaptor.getValue();
		assertThat(cs.getConcept().stream().map(CodeSystem.ConceptDefinitionComponent::getCode)).containsExactly(
			"61438-8",
			"LL1000-0",
			"10061-0",
			"LL1311-1",
			"10331-7",
			"LL360-9",
			"10389-5",
			"LL2413-4",
			"10390-3",
			"LL2422-5",
			"10393-7",
			"LL2420-9",
			"10395-2",
			"10401-8",
			"LL2421-7",
			"10410-9",
			"LL2417-5",
			"10568-4",
			"LL2427-4"
		);
		assertThat(cs.getConcept().get(0).getProperty().stream().map(t -> t.getCode() + "=" + t.getValue().toString()))
			.containsExactly("answer-list=LL1000-0");

		verify(myDataSink, times(1)).accept(myFileSetCaptor.capture());
	}

	@Test
	void testPassThroughUploadStatistics() {
		// Test
		JobInstance instance = new JobInstance();
		instance.setInstanceId("my-instance-id");

		ImportLoincFileSetJson importLoincFileSetJson = new ImportLoincFileSetJson();
		importLoincFileSetJson.getRecordsAddedCounter("step-2").incrementConceptsAdded(2);
		importLoincFileSetJson.getRecordsAddedCounter("step-3").incrementConceptsAdded(3);
		importLoincFileSetJson.setLoincCodeSystemXml(ClasspathUtil.loadResource("loinc-ver/v269/loinc.xml"));

		StepExecutionDetails<ImportLoincJobParameters, ImportLoincFileSetJson> stepExecutionDetails = new StepExecutionDetails<>(new ImportLoincJobParameters(), importLoincFileSetJson, instance, new WorkChunk(), myJobExecutionServices, myJobDefinition, "step-1", "step-2");

		mySvc.run(stepExecutionDetails, myDataSink);

		// Verify
		verify(myTermCodeSystemStorageSvc, never()).uploadCodeSystemConcepts(any());
		verify(myDataSink, times(1)).accept(myFileSetCaptor.capture());

		ImportLoincFileSetJson capturedFileSet0 = myFileSetCaptor.getAllValues().get(0);
		assertNull(capturedFileSet0.getChunkForCurrentStep());
		assertThat(capturedFileSet0.getAndRemoveFutureChunksForStepId("step-3")).isEmpty();
		assertEquals(2, capturedFileSet0.getRecordsAddedCounter("step-2").getConceptsAdded());
		assertEquals(3, capturedFileSet0.getRecordsAddedCounter("step-3").getConceptsAdded());
	}

	@Test
	void run_PassThroughSubsequentStepChunkIds() {
		// Test
		JobInstance instance = new JobInstance();
		instance.setInstanceId("my-instance-id");

		ImportLoincFileSetJson importLoincFileSetJson = new ImportLoincFileSetJson();
		importLoincFileSetJson.addChunk("step-2", "filename2.txt", "step-2-chunk-1");
		importLoincFileSetJson.addChunk("step-2", "filename2.txt", "step-2-chunk-2");
		importLoincFileSetJson.getRecordsAddedCounter("step-2").incrementConceptsAdded(2);
		importLoincFileSetJson.addChunk("step-3", "filename3.txt", "step-3-chunk-1");
		importLoincFileSetJson.addChunk("step-3", "filename3.txt", "step-3-chunk-2");
		importLoincFileSetJson.getRecordsAddedCounter("step-3").incrementConceptsAdded(3);
		importLoincFileSetJson.setLoincCodeSystemXml(ClasspathUtil.loadResource("loinc-ver/v269/loinc.xml"));

		StepExecutionDetails<ImportLoincJobParameters, ImportLoincFileSetJson> stepExecutionDetails = new StepExecutionDetails<>(new ImportLoincJobParameters(), importLoincFileSetJson, instance, new WorkChunk(), myJobExecutionServices, myJobDefinition, "step-1", "step-2");

		mySvc.run(stepExecutionDetails, myDataSink);

		// Verify
		verify(myTermCodeSystemStorageSvc, never()).uploadCodeSystemConcepts(any());
		verify(myDataSink, times(3)).accept(myFileSetCaptor.capture());

		ImportLoincFileSetJson capturedFileSet0 = myFileSetCaptor.getAllValues().get(0);
		assertNull(capturedFileSet0.getChunkForCurrentStep());
		assertThat(capturedFileSet0.getAndRemoveFutureChunksForStepId("step-3"))
			.containsExactly(
				new TerminologyFileSetJson.Chunk("filename3.txt", "step-3-chunk-1"),
				new TerminologyFileSetJson.Chunk("filename3.txt", "step-3-chunk-2")
			);
		assertEquals(2, capturedFileSet0.getRecordsAddedCounter("step-2").getConceptsAdded());
		assertEquals(3, capturedFileSet0.getRecordsAddedCounter("step-3").getConceptsAdded());

		ImportLoincFileSetJson capturedFileSet1 = myFileSetCaptor.getAllValues().get(1);
		assertThat(capturedFileSet1.getChunkForCurrentStep().getAttachmentId()).isEqualTo("step-2-chunk-1");
		assertThat(capturedFileSet1.getAndRemoveFutureChunksForStepId("step-3")).isEmpty();
		assertEquals(0, capturedFileSet1.getRecordsAddedCounter("step-2").getConceptsAdded());
		assertEquals(0, capturedFileSet1.getRecordsAddedCounter("step-3").getConceptsAdded());

		ImportLoincFileSetJson capturedFileSet2 = myFileSetCaptor.getAllValues().get(2);
		assertThat(capturedFileSet2.getChunkForCurrentStep().getAttachmentId()).isEqualTo("step-2-chunk-2");
		assertThat(capturedFileSet2.getAndRemoveFutureChunksForStepId("step-3")).isEmpty();
		assertEquals(0, capturedFileSet2.getRecordsAddedCounter("step-2").getConceptsAdded());
		assertEquals(0, capturedFileSet2.getRecordsAddedCounter("step-3").getConceptsAdded());
	}

	@Test
	void run_PassThroughResourcesToActivate() {
		// Test
		JobInstance instance = new JobInstance();
		instance.setInstanceId("my-instance-id");

		ImportLoincFileSetJson importLoincFileSetJson = new ImportLoincFileSetJson();
		importLoincFileSetJson.addResourceToActivate("CodeSystem/A");
		importLoincFileSetJson.addResourceToActivate("CodeSystem/B");
		importLoincFileSetJson.setLoincCodeSystemXml(ClasspathUtil.loadResource("loinc-ver/v269/loinc.xml"));

		StepExecutionDetails<ImportLoincJobParameters, ImportLoincFileSetJson> stepExecutionDetails = new StepExecutionDetails<>(new ImportLoincJobParameters(), importLoincFileSetJson, instance, new WorkChunk(), myJobExecutionServices, myJobDefinition, "step-1", "step-2");

		mySvc.run(stepExecutionDetails, myDataSink);

		// Verify
		verify(myTermCodeSystemStorageSvc, never()).uploadCodeSystemConcepts(any());
		verify(myDataSink, times(1)).accept(myFileSetCaptor.capture());

		ImportLoincFileSetJson capturedFileSet0 = myFileSetCaptor.getAllValues().get(0);
		assertNull(capturedFileSet0.getChunkForCurrentStep());
		assertThat(capturedFileSet0.getResourcesToActivate()).containsExactly("CodeSystem/A", "CodeSystem/B");
	}
}
