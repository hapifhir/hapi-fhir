package ca.uhn.fhir.jpa.batch2.jobs.term.loinc;

import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.LookupCodeRequest;
import ca.uhn.fhir.jpa.term.UploadStatistics;
import org.hl7.fhir.r5.model.CodeSystem;
import org.hl7.fhir.r5.model.IdType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ImportLoincStep18CodingPropertiesTest extends BaseImportLoincStepTest {

	@InjectMocks
	private ImportLoincStep18CodingProperties mySvc;

	@Test
	void testProcess() {
		// Setup
		String classpath = "loinc-ver/v269/LoincTable/Loinc.csv";
		mockFetchAttachment(classpath);
		mockFetchJobMetadataAttachment();
		when(myTermCodeSystemStorageSvc.uploadCodeSystemConcepts(any())).thenReturn(new UploadStatistics(new IdType()).incrementConceptsAddedCount());

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
		mySvc.run(newStepExecutionDetails(classpath), myDataSink);

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

		verify(myDataSink, times(1)).acceptForFutureStep(myStepIdCaptor.capture(), myFileSetCaptor.capture());
		assertThat(renderEmittedChunks()).containsExactly(
			"finalize-import -> RecordsAdded: From[step-1] Counts[conceptsAdded=1]"
		);
	}

}
