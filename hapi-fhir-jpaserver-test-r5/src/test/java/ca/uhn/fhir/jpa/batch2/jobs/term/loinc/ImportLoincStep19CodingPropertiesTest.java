package ca.uhn.fhir.jpa.batch2.jobs.term.loinc;

import ca.uhn.fhir.batch2.api.*;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.LookupCodeRequest;
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

import java.util.concurrent.atomic.AtomicInteger;

import static ca.uhn.fhir.jpa.batch2.jobs.term.loinc.ImportLoincStep3HandleHierarchyTest.renderHierarchy;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImportLoincStep19CodingPropertiesTest {

	@Mock
	private IValidationSupport myValidationSupport;
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
	private ImportLoincStep19CodingProperties mySvc;

	@Captor
	private ArgumentCaptor<ImportLoincFileSetJson> myFileSetCaptor;
	@Captor
	private ArgumentCaptor<IBaseResource> myCodeSystemCaptor;


	@Test
	void testProcess() {
		// Setup
		when(myJobPersistence.fetchAttachmentById(eq("my-instance-id"), eq("my-chunk-attachment-id"))).thenReturn(new AttachmentDetails(ClasspathUtil.loadResourceAsStream("loinc-ver/v269/LoincTable/Loinc.csv"), AttachmentContentTypeEnum.CSV, "Loinc.csv"));
		when(myTermCodeSystemStorageSvc.uploadCodeSystemConcepts(any())).thenReturn(new UploadStatistics(new IdType()));

		AtomicInteger responseCounter = new AtomicInteger();
		when(myValidationSupport.lookupCode(any(), any(LookupCodeRequest.class))).thenAnswer(t->{
			LookupCodeRequest request = t.getArgument(1, LookupCodeRequest.class);
			assertEquals("http://loinc.org|my-staging-version-id", request.getSystem());
			IValidationSupport.LookupCodeResult result = new IValidationSupport.LookupCodeResult();
			result.setFound(true);
			result.setCodeDisplay("DISPLAY-" + responseCounter.incrementAndGet());
			return result;
		});

		// Test
		JobInstance instance = new JobInstance();
		instance.setInstanceId("my-instance-id");

		ImportLoincFileSetJson importLoincFileSetJson = new ImportLoincFileSetJson();
		importLoincFileSetJson.setCodeSystemStagingVersionId("my-staging-version-id");
		importLoincFileSetJson.setChunkForCurrentStep(new TerminologyFileSetJson.Chunk("file.csv", "my-chunk-attachment-id"));
		importLoincFileSetJson.setLoincCodeSystemXml(ClasspathUtil.loadResource("loinc-ver/v269/loinc.xml"));

		StepExecutionDetails<LoincJobImportParameters, ImportLoincFileSetJson> stepExecutionDetails = new StepExecutionDetails<>(new LoincJobImportParameters(), importLoincFileSetJson, instance, new WorkChunk(), myJobExecutionServices, myJobDefinition, "step-1", "step-2");

		mySvc.run(stepExecutionDetails, myDataSink);

		// Verify
		verify(myTermCodeSystemStorageSvc, times(1)).uploadCodeSystemConcepts(myCodeSystemCaptor.capture());
		CodeSystem cs = (CodeSystem) myCodeSystemCaptor.getValue();
		String result = renderHierarchy(cs, true);
		String expected = """
			-17787-3
			  -Property[AssociatedObservations: {"system":"http://loinc.org","code":"81220-6","display":"DISPLAY-1"}
			  -Property[AssociatedObservations: {"system":"http://loinc.org","code":"72230-6","display":"DISPLAY-2"}
			-11488-4
			  -Property[AssociatedObservations: {"system":"http://loinc.org","code":"81222-2","display":"DISPLAY-3"}
			  -Property[AssociatedObservations: {"system":"http://loinc.org","code":"72231-4","display":"DISPLAY-4"}
			  -Property[AssociatedObservations: {"system":"http://loinc.org","code":"81243-8","display":"DISPLAY-5"}
			""";
		assertEquals(expected, result);

		verify(myDataSink, never()).accept(any(ImportLoincFileSetJson.class));
	}

}
