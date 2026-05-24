package ca.uhn.fhir.jpa.batch2.jobs.term.loinc;

import ca.uhn.fhir.jpa.term.UploadStatistics;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.IdType;
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
class ImportLoincStep3HandleHierarchyTest extends BaseImportLoincStepTest {

	@InjectMocks
	private ImportLoincStep3HandleHierarchy mySvc;


	@Test
	void run_LoadCodes() {
		// Setup
		String classpath = "loinc-ver/v269/AccessoryFiles/MultiAxialHierarchy/MultiAxialHierarchy.csv";
		mockFetchAttachment(classpath);
		mockFetchJobMetadataAttachment();
		when(myTermCodeSystemStorageSvc.uploadCodeSystemConcepts(any())).thenReturn(new UploadStatistics(new IdType()).incrementConceptsAddedCount());

		// Test
		mySvc.run(newStepExecutionDetails(classpath), myDataSink);

		// Verify
		verify(myTermCodeSystemStorageSvc, times(1)).uploadCodeSystemConcepts(myCodeSystemCaptor.capture());
		CodeSystem cs = (CodeSystem) myCodeSystemCaptor.getValue();
		String hierarchy = renderHierarchy(cs);
		String expected = """
			-LP31755-9
			  -LP14559-6
			    -LP98185-9
			      -LP14082-9
			        -LP52258-8
			          -41599-2
			        -LP52260-4
			          -41602-4
			        -LP52960-9
			""";
		assertEquals(expected, hierarchy);

		verify(myDataSink, times(1)).acceptForFutureStep(myStepIdCaptor.capture(), myFileSetCaptor.capture());
		assertThat(renderEmittedChunks()).containsExactly(
			"finalize-import -> RecordsAdded: From[step-1] Counts[conceptsAdded=1]"
		);
	}

}
