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
class ImportLoincStep18ConsumerNameTest extends BaseImportLoincStepTest {

	@InjectMocks
	private ImportLoincStep18ConsumerName mySvc;


	@Test
	void testProcess() {
		// Setup
		String classpath = "loinc-ver/v269/AccessoryFiles/ConsumerName/ConsumerName.csv";
		mockFetchAttachment(classpath);
		mockFetchJobMetadataAttachment();
		when(myTermCodeSystemStorageSvc.uploadCodeSystemConcepts(any())).thenReturn(new UploadStatistics(new IdType()).incrementDesignationsAddedCount(3));

		// Test
		mySvc.run(newStepExecutionDetails(classpath), myDataSink);

		// Verify
		verify(myTermCodeSystemStorageSvc, times(1)).uploadCodeSystemConcepts(myCodeSystemCaptor.capture());
		CodeSystem cs = (CodeSystem) myCodeSystemCaptor.getValue();
		String hierarchy = renderHierarchy(cs, true);
		String expected = """
			-61438-8
			  -Designation[lang=null, use={"display":"ConsumerName"}]: Consumer Name 61438-8
			-17787-3
			  -Designation[lang=null, use={"display":"ConsumerName"}]: Consumer Name 17787-3
			-38699-5
			  -Designation[lang=null, use={"display":"ConsumerName"}]: 1,1-Dichloroethane, Air
			""";
		assertEquals(expected, hierarchy);

		verify(myDataSink, times(1)).acceptForFutureStep(myStepIdCaptor.capture(), myFileSetCaptor.capture());
		assertThat(renderEmittedChunks()).containsExactly(
			"finalize-import -> RecordsAdded: From[step-1] Counts[designationsAdded=3]"
		);

	}

}
