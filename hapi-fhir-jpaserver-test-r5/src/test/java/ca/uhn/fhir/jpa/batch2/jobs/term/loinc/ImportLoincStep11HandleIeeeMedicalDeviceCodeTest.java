package ca.uhn.fhir.jpa.batch2.jobs.term.loinc;

import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.IdType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static ca.uhn.fhir.jpa.batch2.jobs.term.loinc.ImportLoincStep7HandleRsnaPlaybookTest.renderConceptMap;
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
class ImportLoincStep11HandleIeeeMedicalDeviceCodeTest extends BaseImportLoincStepTest {

	@InjectMocks
	private ImportLoincStep11HandleIeeeMedicalDeviceCode mySvc;

	@Test
	void testProcess() {
		// Setup
		String classpath = "loinc-ver/v269/AccessoryFiles/LoincIeeeMedicalDeviceCodeMappingTable/LoincIeeeMedicalDeviceCodeMappingTable.csv";
		mockFetchAttachment(classpath);
		when(myJobPersistence.fetchAttachmentByFilename(eq("my-instance-id"), eq(LoincUploadPropertiesEnum.LOINC_UPLOAD_PROPERTIES_FILE.getCode()))).thenThrow(new ResourceNotFoundException("Not found", null));
		when(myConceptMapDao.read(any(), any())).thenThrow(new ResourceNotFoundException(new IdType("ConceptMap/loinc-to-radlex-1.234")));
		mockDaoRegistryConceptMap();
		mockJobExecutionServices();

		// Test
		mySvc.run(newStepExecutionDetails(classpath), myDataSink);

		// Verify
		verify(myTermCodeSystemStorageSvc, never()).uploadCodeSystemConcepts(myCodeSystemCaptor.capture());
		verify(myDataSink, times(1)).acceptForFutureStep(myStepIdCaptor.capture(), myFileSetCaptor.capture());
		assertThat(renderEmittedChunks()).containsExactly(
			"finalize-import -> RecordsAdded: From[step-1] Counts[conceptMapsAdded=1,conceptMapMappingsAdded=9]"
		);
		verify(myValueSetDao, never()).update(myValueSetCaptor.capture(), nullable(RequestDetails.class));

		// ConceptMaps
		verify(myConceptMapDao, times(1)).create(myConceptMapCaptor.capture(), nullable(RequestDetails.class));
		ConceptMap actualConceptMap = myConceptMapCaptor.getAllValues().get(0);
		assertEquals("loinc-to-ieee-11073-10101-1.234", actualConceptMap.getUserData(JpaConstants.RESOURCE_ID_SERVER_ASSIGNED_VALUE));
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
