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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ImportLoincStep20LinguisticVariantTest {

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
	private ImportLoincStep20LinguisticVariant mySvc;

	@Captor
	private ArgumentCaptor<ImportLoincFileSetJson> myFileSetCaptor;
	@Captor
	private ArgumentCaptor<IBaseResource> myCodeSystemCaptor;


	@Test
	void testProcess() {
		// Setup
		when(myJobPersistence.fetchAttachmentById(eq("my-instance-id"), eq("my-chunk-attachment-id"))).thenReturn(new AttachmentDetails(ClasspathUtil.loadResourceAsStream("loinc-ver/v269/AccessoryFiles/LinguisticVariants/frCA8LinguisticVariant.csv"), AttachmentContentTypeEnum.CSV, "Loinc.csv"));
		when(myTermCodeSystemStorageSvc.uploadCodeSystemConcepts(any())).thenReturn(new UploadStatistics(new IdType()));

		// Test
		JobInstance instance = new JobInstance();
		instance.setInstanceId("my-instance-id");

		ImportLoincFileSetJson importLoincFileSetJson = new ImportLoincFileSetJson();
		importLoincFileSetJson.setCodeSystemStagingVersionId("my-staging-version-id");
		importLoincFileSetJson.setChunkForCurrentStep(new TerminologyFileSetJson.Chunk("loinc-ver/v269/AccessoryFiles/LinguisticVariants/frCA8LinguisticVariant.csv", "my-chunk-attachment-id"));
		importLoincFileSetJson.setLoincCodeSystemXml(ClasspathUtil.loadResource("loinc-ver/v269/loinc.xml"));

		StepExecutionDetails<LoincJobImportParameters, ImportLoincFileSetJson> stepExecutionDetails = new StepExecutionDetails<>(new LoincJobImportParameters(), importLoincFileSetJson, instance, new WorkChunk(), myJobExecutionServices, myJobDefinition, "step-1", "step-2");

		mySvc.run(stepExecutionDetails, myDataSink);

		// Verify
		verify(myTermCodeSystemStorageSvc, times(1)).uploadCodeSystemConcepts(myCodeSystemCaptor.capture());
		CodeSystem cs = (CodeSystem) myCodeSystemCaptor.getValue();
		String result = renderHierarchy(cs, true);
		String expected = """
			-61438-8
			  -Designation[lang=fr-CA, use={"system":"http://loinc.org","code":"FullySpecifiedName","display":"FullySpecifiedName"}]: Cellules de Purkinje cytoplasmique type 2 , IgG:Titre:Temps ponctuel:Sérum:Quantitatif:Immunofluorescence
			  -Designation[lang=fr-CA, use={"system":"http://loinc.org","code":"LONG_COMMON_NAME","display":"LONG_COMMON_NAME"}]: (example long french common name)
			-11704-4
			  -Designation[lang=fr-CA, use={"system":"http://loinc.org","code":"FullySpecifiedName","display":"FullySpecifiedName"}]: Gliale nucléaire de type 1 , IgG:Titre:Temps ponctuel:LCR:Quantitatif:Immunofluorescence
			-17787-3
			  -Designation[lang=fr-CA, use={"system":"http://loinc.org","code":"FullySpecifiedName","display":"FullySpecifiedName"}]: Virus respiratoire syncytial bovin:Présence-Seuil:Temps ponctuel:XXX:Ordinal:Culture spécifique à un microorganisme
			-17788-1
			  -Designation[lang=fr-CA, use={"system":"http://loinc.org","code":"FullySpecifiedName","display":"FullySpecifiedName"}]: Cellules de Purkinje cytoplasmique type 2 , IgG:Titre:Temps ponctuel:Sérum:Quantitatif:
			""";
		assertEquals(expected, result);

		verify(myDataSink, never()).accept(any(ImportLoincFileSetJson.class));
	}

	@Test
	void testProcess_FileNotListedInLinguisticVariantFile() {
		// Setup
		when(myJobPersistence.fetchAttachmentById(eq("my-instance-id"), eq("my-chunk-attachment-id"))).thenReturn(new AttachmentDetails(ClasspathUtil.loadResourceAsStream("loinc-ver/v269/AccessoryFiles/LinguisticVariants/frCA8LinguisticVariant.csv"), AttachmentContentTypeEnum.CSV, "Loinc.csv"));
		when(myTermCodeSystemStorageSvc.uploadCodeSystemConcepts(any())).thenReturn(new UploadStatistics(new IdType()));

		// Test
		JobInstance instance = new JobInstance();
		instance.setInstanceId("my-instance-id");

		ImportLoincFileSetJson importLoincFileSetJson = new ImportLoincFileSetJson();
		importLoincFileSetJson.setCodeSystemStagingVersionId("my-staging-version-id");
		// The following filename doesn't line up with anything we put into the LinguisticVariants
		importLoincFileSetJson.setChunkForCurrentStep(new TerminologyFileSetJson.Chunk("loinc-ver/v269/AccessoryFiles/LinguisticVariants/zzZZ8LinguisticVariant.csv", "my-chunk-attachment-id"));
		importLoincFileSetJson.setLoincCodeSystemXml(ClasspathUtil.loadResource("loinc-ver/v269/loinc.xml"));

		StepExecutionDetails<LoincJobImportParameters, ImportLoincFileSetJson> stepExecutionDetails = new StepExecutionDetails<>(new LoincJobImportParameters(), importLoincFileSetJson, instance, new WorkChunk(), myJobExecutionServices, myJobDefinition, "step-1", "step-2");

		mySvc.run(stepExecutionDetails, myDataSink);

		// Verify
		verify(myTermCodeSystemStorageSvc, times(1)).uploadCodeSystemConcepts(myCodeSystemCaptor.capture());
		CodeSystem cs = (CodeSystem) myCodeSystemCaptor.getValue();
		String result = renderHierarchy(cs, true);
		String expected = """
			-61438-8
			  -Designation[lang=zz-ZZ, use={"system":"http://loinc.org","code":"FullySpecifiedName","display":"FullySpecifiedName"}]: Cellules de Purkinje cytoplasmique type 2 , IgG:Titre:Temps ponctuel:Sérum:Quantitatif:Immunofluorescence
			  -Designation[lang=zz-ZZ, use={"system":"http://loinc.org","code":"LONG_COMMON_NAME","display":"LONG_COMMON_NAME"}]: (example long french common name)
			-11704-4
			  -Designation[lang=zz-ZZ, use={"system":"http://loinc.org","code":"FullySpecifiedName","display":"FullySpecifiedName"}]: Gliale nucléaire de type 1 , IgG:Titre:Temps ponctuel:LCR:Quantitatif:Immunofluorescence
			-17787-3
			  -Designation[lang=zz-ZZ, use={"system":"http://loinc.org","code":"FullySpecifiedName","display":"FullySpecifiedName"}]: Virus respiratoire syncytial bovin:Présence-Seuil:Temps ponctuel:XXX:Ordinal:Culture spécifique à un microorganisme
			-17788-1
			  -Designation[lang=zz-ZZ, use={"system":"http://loinc.org","code":"FullySpecifiedName","display":"FullySpecifiedName"}]: Cellules de Purkinje cytoplasmique type 2 , IgG:Titre:Temps ponctuel:Sérum:Quantitatif:
			""";
		assertEquals(expected, result);

		verify(myDataSink, never()).accept(any(ImportLoincFileSetJson.class));
	}

}
