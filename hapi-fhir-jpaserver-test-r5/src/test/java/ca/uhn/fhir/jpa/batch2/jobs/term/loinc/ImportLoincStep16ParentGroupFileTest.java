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
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoConceptMap;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoValueSet;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyFileSetJson;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemStorageSvc;
import ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.util.ClasspathUtil;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Comparator;
import java.util.List;

import static ca.uhn.fhir.jpa.batch2.jobs.term.loinc.ImportLoincStep4HandleAnswerListsTest.renderValueSetCompose;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.nullable;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ImportLoincStep16ParentGroupFileTest {

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
	@Mock
	private IFhirResourceDaoValueSet<ValueSet> myValueSetDao;
	@Mock
	private IFhirResourceDaoConceptMap<ConceptMap> myConceptMapDao;

	@InjectMocks
	private ImportLoincStep16ParentGroupFile mySvc;

	@Captor
	private ArgumentCaptor<IBaseResource> myCodeSystemCaptor;
	@Captor
	private ArgumentCaptor<ImportLoincFileSetJson> myFileSetCaptor;
	@Captor
	private ArgumentCaptor<ValueSet> myValueSetCaptor;
	@Captor
	private ArgumentCaptor<ConceptMap> myConceptMapCaptor;


	@Test
	void testProcess() {
		// Setup
		when(myJobPersistence.fetchAttachmentById(eq("my-instance-id"), eq("my-chunk-attachment-id"))).thenReturn(new AttachmentDetails(ClasspathUtil.loadResourceAsStream("loinc-ver/v269/AccessoryFiles/GroupFile/ParentGroup.csv"), AttachmentContentTypeEnum.CSV, "Loinc.csv"));
		when(myJobPersistence.fetchAttachmentByFilename(eq("my-instance-id"), eq(LoincUploadPropertiesEnum.LOINC_UPLOAD_PROPERTIES_FILE.getCode()))).thenThrow(new ResourceNotFoundException("Not found", null));
		when(myValueSetDao.read(any(), any())).thenThrow(new ResourceNotFoundException(new IdType("ValueSet/LL1000-0-1.234")));

		// Test
		JobInstance instance = new JobInstance();
		instance.setInstanceId("my-instance-id");

		ImportLoincFileSetJson importLoincFileSetJson = new ImportLoincFileSetJson();
		importLoincFileSetJson.setChunkForCurrentStep(new TerminologyFileSetJson.Chunk("file.csv", "my-chunk-attachment-id"));
		importLoincFileSetJson.setLoincCodeSystemXml(ClasspathUtil.loadResource("loinc-ver/v269/loinc.xml"));
		importLoincFileSetJson.getLoincCodeSystem().setVersion("1.234");

		StepExecutionDetails<ImportLoincJobParameters, ImportLoincFileSetJson> stepExecutionDetails = new StepExecutionDetails<>(new ImportLoincJobParameters(), importLoincFileSetJson, instance, new WorkChunk(), myJobExecutionServices, myJobDefinition, "step-1", "step-2");

		mySvc.run(stepExecutionDetails, myDataSink);

		// Verify
		verify(myTermCodeSystemStorageSvc, never()).uploadCodeSystemConcepts(myCodeSystemCaptor.capture());

		verify(myDataSink, times(1)).accept(myFileSetCaptor.capture());
		assertThat(myFileSetCaptor.getAllValues().get(0).getResourcesToActivate()).containsExactlyInAnyOrder(
			"ValueSet/LG100-4-1.234"
		);

		verify(myValueSetDao, times(1)).update(myValueSetCaptor.capture(), nullable(RequestDetails.class));
		List<ValueSet> allValueSets = myValueSetCaptor.getAllValues();
		allValueSets.sort(Comparator.comparing(a -> a.getIdElement().getIdPart()));
		ValueSet vs = allValueSets.get(0);
		assertEquals("LG100-4-1.234", vs.getIdElement().getIdPart());
		assertEquals("http://loinc.org/vs/LG100-4", vs.getUrl());
		assertEquals("1.234", vs.getVersion());
		assertEquals("Chem_DrugTox_Chal_Sero_Allergy<SAME:Comp|Prop|Tm|Syst (except intravascular and urine)><ANYBldSerPlas,ANYUrineUrineSed><ROLLUP:Method>", vs.getName());

		String valueSetCompose = renderValueSetCompose(vs);
		String expected = """
			""";
		assertEquals(expected, valueSetCompose);

	}

	@Test
	void testProcess_AlreadyExists() {
		// Setup
		when(myJobPersistence.fetchAttachmentById(eq("my-instance-id"), eq("my-chunk-attachment-id"))).thenReturn(new AttachmentDetails(ClasspathUtil.loadResourceAsStream("loinc-ver/v269/AccessoryFiles/GroupFile/ParentGroup.csv"), AttachmentContentTypeEnum.CSV, "Loinc.csv"));
		when(myJobPersistence.fetchAttachmentByFilename(eq("my-instance-id"), eq(LoincUploadPropertiesEnum.LOINC_UPLOAD_PROPERTIES_FILE.getCode()))).thenThrow(new ResourceNotFoundException("Not found", null));

		// This step adds names to valueset, so populate everything except the name
		ValueSet existing = new ValueSet();
		existing.setId("LG100-4-1.234");
		existing.setUrl("http://loinc.org/vs/LG100-4");
		existing.setVersion("1.234");

		when(myValueSetDao.read(any(), any())).thenReturn(existing);

		// Test
		JobInstance instance = new JobInstance();
		instance.setInstanceId("my-instance-id");

		ImportLoincFileSetJson importLoincFileSetJson = new ImportLoincFileSetJson();
		importLoincFileSetJson.setChunkForCurrentStep(new TerminologyFileSetJson.Chunk("file.csv", "my-chunk-attachment-id"));
		importLoincFileSetJson.setLoincCodeSystemXml(ClasspathUtil.loadResource("loinc-ver/v269/loinc.xml"));
		importLoincFileSetJson.getLoincCodeSystem().setVersion("1.234");

		StepExecutionDetails<ImportLoincJobParameters, ImportLoincFileSetJson> stepExecutionDetails = new StepExecutionDetails<>(new ImportLoincJobParameters(), importLoincFileSetJson, instance, new WorkChunk(), myJobExecutionServices, myJobDefinition, "step-1", "step-2");

		mySvc.run(stepExecutionDetails, myDataSink);

		// Verify
		verify(myTermCodeSystemStorageSvc, never()).uploadCodeSystemConcepts(myCodeSystemCaptor.capture());

		verify(myDataSink, times(1)).accept(myFileSetCaptor.capture());
		assertThat(myFileSetCaptor.getAllValues().get(0).getResourcesToActivate()).containsExactlyInAnyOrder(
			"ValueSet/LG100-4-1.234"
		);

		verify(myValueSetDao, times(1)).update(myValueSetCaptor.capture(), nullable(RequestDetails.class));
		List<ValueSet> allValueSets = myValueSetCaptor.getAllValues();
		allValueSets.sort(Comparator.comparing(a -> a.getIdElement().getIdPart()));
		ValueSet vs = allValueSets.get(0);
		assertEquals("LG100-4-1.234", vs.getIdElement().getIdPart());
		assertEquals("http://loinc.org/vs/LG100-4", vs.getUrl());
		assertEquals("1.234", vs.getVersion());
		assertEquals("Chem_DrugTox_Chal_Sero_Allergy<SAME:Comp|Prop|Tm|Syst (except intravascular and urine)><ANYBldSerPlas,ANYUrineUrineSed><ROLLUP:Method>", vs.getName());

		String valueSetCompose = renderValueSetCompose(vs);
		String expected = """
			""";
		assertEquals(expected, valueSetCompose);

	}




}
