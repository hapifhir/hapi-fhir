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
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
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

import static ca.uhn.fhir.jpa.batch2.jobs.term.loinc.ImportLoincStep6HandleRsnaPlaybookTest.renderConceptMap;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.nullable;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ImportLoincStep7HandlePartRelatedCodeMappingTest extends BaseImportLoincStepTest {

	@InjectMocks
	private ImportLoincStep7HandlePartRelatedCodeMapping mySvc;

	@Test
	void testProcess() {
		// Setup
		String classpath = "loinc-ver/v269/AccessoryFiles/PartFile/PartRelatedCodeMapping.csv";
		mockFetchAttachment(classpath);
		when(myJobPersistence.fetchAttachmentByFilename(eq("my-instance-id"), eq(LoincUploadPropertiesEnum.LOINC_UPLOAD_PROPERTIES_FILE.getCode()))).thenThrow(new ResourceNotFoundException("Not found", null));
		when(myConceptMapDao.read(eq(new IdType("loinc-parts-to-snomed-ct-1.234")), any())).thenThrow(new ResourceNotFoundException(new IdType("ConceptMap/loinc-to-radlex-1.234")));
		when(myConceptMapDao.read(eq(new IdType("loinc-parts-to-pubchem-1.234")), any())).thenThrow(new ResourceNotFoundException(new IdType("ConceptMap/loinc-to-radlex-1.234")));
		when(myConceptMapDao.read(eq(new IdType("httpfoobar-1.234")), any())).thenThrow(new ResourceNotFoundException(new IdType("ConceptMap/loinc-to-radlex-1.234")));
		mockDaoRegistryConceptMap();
		mockJobExecutionServices();

		// Test
		mySvc.run(newStepExecutionDetails(classpath), myDataSink);

		// Verify
		verify(myTermCodeSystemStorageSvc, never()).uploadCodeSystemConcepts(myCodeSystemCaptor.capture());
		verify(myDataSink, times(1)).accept(myFileSetCaptor.capture());
		assertEquals("[conceptMapsAdded=3,conceptMapMappingsAdded=11]", myFileSetCaptor.getAllValues().get(0).getRecordsAddedCounter("step-1").toString());
		verify(myValueSetDao, never()).update(myValueSetCaptor.capture(), nullable(RequestDetails.class));

		// ConceptMaps
		verify(myConceptMapDao, times(3)).create(myConceptMapCaptor.capture(), nullable(RequestDetails.class));
		ConceptMap actualConceptMap = myConceptMapCaptor.getAllValues().get(0);
		String actualRender = renderConceptMap(actualConceptMap);
		String expected = """
			Group: http://loinc.org -> http://snomed.info/sct
			  Code[LP18172-4] -> 420710006
			  Code[LP31706-2] -> 1018001
			  Code[LP15826-8] -> 10192006
			  Code[LP7400-7] -> 10200004
			  Code[LP29165-5] -> 10200004
			  Code[LP15666-8] -> 102640000
			  Code[LP15943-1] -> 102641001
			  Code[LP15791-4] -> 102642008
			  Code[LP15721-1] -> 102648007
			""";
		assertEquals(expected, actualRender);

		actualConceptMap = myConceptMapCaptor.getAllValues().get(1);
		actualRender = renderConceptMap(actualConceptMap);
		expected = """
			Group: http://loinc.org -> http://pubchem.ncbi.nlm.nih.gov
			  Code[LP15842-5] -> 1054
			""";
		assertEquals(expected, actualRender);

		actualConceptMap = myConceptMapCaptor.getAllValues().get(2);
		actualRender = renderConceptMap(actualConceptMap);
		expected = """
			Group: http://loinc.org -> http://foo/bar
			  Code[LP15842-5] -> 1054
			""";
		assertEquals(expected, actualRender);
	}



}
