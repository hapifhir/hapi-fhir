package ca.uhn.fhir.jpa.batch2.jobs.term.loinc;

import ca.uhn.fhir.jpa.term.UploadStatistics;
import org.hl7.fhir.r5.model.CodeSystem;
import org.hl7.fhir.r5.model.IdType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ImportLoincStep16PartLinkTest extends BaseImportLoincStepTest {

	@InjectMocks
	private ImportLoincStep16PartLink mySvc;


	@Test
	void testProcess() {
		// Setup
		String classpath = "loinc-ver/v269/AccessoryFiles/PartFile/LoincPartLink.csv";
		mockFetchAttachment(classpath);
		mockFetchJobMetadataAttachment();
		when(myTermCodeSystemStorageSvc.uploadCodeSystemConcepts(any())).thenReturn(new UploadStatistics(new IdType()).incrementConceptsAddedCount());

		// Test
		mySvc.run(newStepExecutionDetails(classpath), myDataSink);

		// Verify
		verify(myTermCodeSystemStorageSvc, times(1)).uploadCodeSystemConcepts(myCodeSystemCaptor.capture());
		CodeSystem cs = myCodeSystemCaptor.getValue();
		String hierarchy = renderHierarchy(cs, true);
		String expected = """
			-10013-1
			  -Property[COMPONENT: {"system":"http://loinc.org","code":"LP31101-6","display":"R' wave amplitude.lead I"}
			  -Property[PROPERTY: {"system":"http://loinc.org","code":"LP6802-5","display":"Elpot"}
			  -Property[TIME_ASPCT: {"system":"http://loinc.org","code":"LP6960-1","display":"Pt"}
			  -Property[SYSTEM: {"system":"http://loinc.org","code":"LP7289-4","display":"Heart"}
			  -Property[SCALE_TYP: {"system":"http://loinc.org","code":"LP7753-9","display":"Qn"}
			  -Property[METHOD_TYP: {"system":"http://loinc.org","code":"LP6244-0","display":"EKG"}
			  -Property[analyte: {"system":"http://loinc.org","code":"LP31101-6","display":"R' wave amplitude.lead I"}
			  -Property[time-core: {"system":"http://loinc.org","code":"LP6960-1","display":"Pt"}
			  -Property[STATUS: STRING_PART_VALUE
			""";
		assertEquals(expected, hierarchy);

		verify(myDataSink, times(1)).acceptForFutureStep(myStepIdCaptor.capture(), myFileSetCaptor.capture());
		assertThat(renderEmittedChunks()).containsExactly(
			"finalize-import -> RecordsAdded: From[step-1] Counts[conceptsAdded=1]"
		);
	}

}
