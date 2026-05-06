package ca.uhn.fhir.jpa.batch2.jobs.term.loinc;

import ca.uhn.fhir.batch2.api.*;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoConceptMap;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoValueSet;
import ca.uhn.fhir.jpa.term.api.ITermCodeSystemStorageSvc;
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

import static ca.uhn.fhir.jpa.batch2.jobs.term.loinc.ImportLoincStep6HandleRsnaPlaybookTest.renderConceptMap;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImportLoincStep12HandleIeeeMedicalDeviceCodeTest {

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
	@Mock
	private IFhirResourceDaoValueSet<ValueSet> myValueSetDao;
	@Mock
	private IFhirResourceDaoConceptMap<ConceptMap> myConceptMapDao;

	@InjectMocks
	private ImportLoincStep12HandleIeeeMedicalDeviceCode mySvc;

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
		when(myJobPersistence.fetchAttachmentById(eq("my-instance-id"), eq("my-chunk-attachment-id"))).thenReturn(new AttachmentDetails(ClasspathUtil.loadResourceAsStream("loinc-ver/v269/AccessoryFiles/LoincIeeeMedicalDeviceCodeMappingTable/LoincIeeeMedicalDeviceCodeMappingTable.csv"), AttachmentContentTypeEnum.CSV, "Loinc.csv"));
		when(myConceptMapDao.read(any(), any())).thenThrow(new ResourceNotFoundException(new IdType("ConceptMap/loinc-to-radlex-1.234")));

		// Test
		JobInstance instance = new JobInstance();
		instance.setInstanceId("my-instance-id");

		ImportLoincFileSetJson importLoincFileSetJson = new ImportLoincFileSetJson();
		importLoincFileSetJson.setChunkAttachmentIdForCurrentStepId("my-chunk-attachment-id");
		importLoincFileSetJson.setLoincCodeSystemXml(ClasspathUtil.loadResource("loinc-ver/v269/loinc.xml"));
		importLoincFileSetJson.getLoincCodeSystem().setVersion("1.234");

		StepExecutionDetails<LoincJobImportParameters, ImportLoincFileSetJson> stepExecutionDetails = new StepExecutionDetails<>(new LoincJobImportParameters(), importLoincFileSetJson, instance, new WorkChunk(), myJobExecutionServices, myJobDefinition, "step-1", "step-2");

		mySvc.run(stepExecutionDetails, myDataSink);

		// Verify
		verify(myTermCodeSystemStorageSvc, never()).uploadCodeSystemConcepts(myCodeSystemCaptor.capture());
		verify(myDataSink, never()).accept(myFileSetCaptor.capture());
		verify(myValueSetDao, never()).update(myValueSetCaptor.capture(), nullable(RequestDetails.class));

		// ConceptMaps
		verify(myConceptMapDao, times(1)).update(myConceptMapCaptor.capture(), nullable(RequestDetails.class));
		ConceptMap actualConceptMap = myConceptMapCaptor.getAllValues().get(0);
		String actualRender = renderConceptMap(actualConceptMap);
		String expected = """
			Group: http://loinc.org -> urn:iso:std:iso:11073:10101
			  Code[11556-8] -> 160116
			  Code[11557-6] -> 160064
			  Code[11558-4] -> 160004
			  Code[12961-9] -> 160080
			  Code[14749-6] -> 160196, 160368
			  Code[15074-8] -> 160020, 160364
			  Code[17861-6] -> 160024
			""";
		assertEquals(expected, actualRender);
	}



}
