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
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemStorageSvc;
import ca.uhn.fhir.util.ClasspathUtil;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.CodeSystem;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ImportLoincStep2HandleConceptsTest {

	@Mock
	private IJobPersistence myJobPersistence;
	@Mock
	private ITermCodeSystemStorageSvc myTermCodeSystemStorageSvc;
	@Mock
	private IJobDataSink<ImportLoincFileSetJson> myDataSink;
	@Mock
	private IJobStepExecutionServices myJobExecutionServices;
	@Mock
	private JobDefinition<LoincJobImportParameters> myJobDefinition;

	@InjectMocks
	private ImportLoincStep2HandleConcepts mySvc;

	@Captor
	private ArgumentCaptor<ImportLoincFileSetJson> myFileSetCaptor;
	@Captor
	private ArgumentCaptor<IBaseResource> myCodeSystemCaptor;


	@Test
	void run_LoadCodes() {
		// Setup
		when(myJobPersistence.fetchAttachmentById(eq("my-instance-id"), eq("my-chunk-attachment-id"))).thenReturn(new AttachmentDetails(ClasspathUtil.loadResourceAsStream("loinc-ver/v269/LoincTable/Loinc.csv"), AttachmentContentTypeEnum.CSV, "Loinc.csv"));

		// Test
		JobInstance instance = new JobInstance();
		instance.setInstanceId("my-instance-id");

		ImportLoincFileSetJson importLoincFileSetJson = new ImportLoincFileSetJson();
		importLoincFileSetJson.setChunkAttachmentIdForCurrentStepId("my-chunk-attachment-id");
		importLoincFileSetJson.setLoincCodeSystemXml(ClasspathUtil.loadResource("loinc-ver/v269/loinc.xml"));

		StepExecutionDetails<LoincJobImportParameters, ImportLoincFileSetJson> stepExecutionDetails = new StepExecutionDetails<>(new LoincJobImportParameters(), importLoincFileSetJson, instance, new WorkChunk(), myJobExecutionServices, myJobDefinition, "step-1", "step-2");

		mySvc.run(stepExecutionDetails, myDataSink);

		// Verify
		verify(myTermCodeSystemStorageSvc, times(1)).uploadCodeSystemConcepts(myCodeSystemCaptor.capture());
		CodeSystem cs = (CodeSystem) myCodeSystemCaptor.getValue();
		assertThat(cs.getConcept().stream().map(CodeSystem.ConceptDefinitionComponent::getCode)).containsExactly(
			"10013-1",
			"10014-9",
			"10015-6",
			"10016-4",
			"1001-7",
			"10017-2",
			"10018-0",
			"10019-8",
			"10020-6",
			"61438-8",
			"10000-8",
			"17787-3",
			"17788-1",
			"11488-4",
			"47239-9"
		);
		assertThat(cs.getConcept().get(0).getProperty().stream().map(t->t.getCode() + "=" + t.getValue().toString()))
			.containsExactly("CLASSTYPE=2",
				"VersionLastChanged=2.48",
				"STATUS=ACTIVE",
				"ORDER_OBS=Observation");

		verify(myDataSink, never()).accept(any(ImportLoincFileSetJson.class));
	}

	@Test
	void run_PassThroughSubsequentStepChunkIds() {
		// Test
		JobInstance instance = new JobInstance();
		instance.setInstanceId("my-instance-id");

		ImportLoincFileSetJson importLoincFileSetJson = new ImportLoincFileSetJson();
		importLoincFileSetJson.addChunk("step-2", "step-2-chunk-1");
		importLoincFileSetJson.addChunk("step-2", "step-2-chunk-2");
		importLoincFileSetJson.addChunk("step-3", "step-3-chunk-1");
		importLoincFileSetJson.addChunk("step-3", "step-3-chunk-2");
		importLoincFileSetJson.setLoincCodeSystemXml(ClasspathUtil.loadResource("loinc-ver/v269/loinc.xml"));

		StepExecutionDetails<LoincJobImportParameters, ImportLoincFileSetJson> stepExecutionDetails = new StepExecutionDetails<>(new LoincJobImportParameters(), importLoincFileSetJson, instance, new WorkChunk(), myJobExecutionServices, myJobDefinition, "step-1", "step-2");

		mySvc.run(stepExecutionDetails, myDataSink);

		// Verify
		verify(myTermCodeSystemStorageSvc, never()).uploadCodeSystemConcepts(any());
		verify(myDataSink, times(3)).accept(myFileSetCaptor.capture());

		ImportLoincFileSetJson capturedFileSet0 = myFileSetCaptor.getAllValues().get(0);
		assertNull(capturedFileSet0.getChunkAttachmentIdForCurrentStepId());
		assertThat(capturedFileSet0.getAndRemoveFutureChunkAttachmentIdsForStepId("step-3"))
			.containsExactly("step-3-chunk-1", "step-3-chunk-2");

		ImportLoincFileSetJson capturedFileSet1 = myFileSetCaptor.getAllValues().get(1);
		assertThat(capturedFileSet1.getChunkAttachmentIdForCurrentStepId()).isEqualTo("step-2-chunk-1");
		assertThat(capturedFileSet1.getAndRemoveFutureChunkAttachmentIdsForStepId("step-3")).isEmpty();

		ImportLoincFileSetJson capturedFileSet2 = myFileSetCaptor.getAllValues().get(2);
		assertThat(capturedFileSet2.getChunkAttachmentIdForCurrentStepId()).isEqualTo("step-2-chunk-2");
		assertThat(capturedFileSet2.getAndRemoveFutureChunkAttachmentIdsForStepId("step-3")).isEmpty();
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

		StepExecutionDetails<LoincJobImportParameters, ImportLoincFileSetJson> stepExecutionDetails = new StepExecutionDetails<>(new LoincJobImportParameters(), importLoincFileSetJson, instance, new WorkChunk(), myJobExecutionServices, myJobDefinition, "step-1", "step-2");

		mySvc.run(stepExecutionDetails, myDataSink);

		// Verify
		verify(myTermCodeSystemStorageSvc, never()).uploadCodeSystemConcepts(any());
		verify(myDataSink, times(1)).accept(myFileSetCaptor.capture());

		ImportLoincFileSetJson capturedFileSet0 = myFileSetCaptor.getAllValues().get(0);
		assertNull(capturedFileSet0.getChunkAttachmentIdForCurrentStepId());
		assertThat(capturedFileSet0.getResourcesToActivate()).containsExactly("CodeSystem/A", "CodeSystem/B");
	}
}
