package ca.uhn.fhir.jpa.batch2.jobs.term.loinc;

import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyFileSetJson;
import ca.uhn.fhir.jpa.term.UploadStatistics;
import ca.uhn.fhir.util.ClasspathUtil;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.IdType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ImportLoincStep6HandleAnswerListLinksTest extends BaseImportLoincStepTest{

	@InjectMocks
	private ImportLoincStep6HandleAnswerListLinks mySvc;


	@Test
	void run_LoadCodes() {
		// Setup
		String classpath = "loinc-ver/v269/AccessoryFiles/AnswerFile/LoincAnswerListLink.csv";
		mockFetchAttachment(classpath);
		mockFetchJobMetadataAttachment();
		when(myTermCodeSystemStorageSvc.uploadCodeSystemConcepts(any())).thenReturn(new UploadStatistics(new IdType()).incrementConceptsAddedCount());

		// Test
		mySvc.run(newStepExecutionDetails(classpath), myDataSink);

		// Verify
		verify(myTermCodeSystemStorageSvc, times(1)).uploadCodeSystemConcepts(myCodeSystemCaptor.capture());
		CodeSystem cs = (CodeSystem) myCodeSystemCaptor.getValue();
		assertThat(cs.getConcept().stream().map(CodeSystem.ConceptDefinitionComponent::getCode)).containsExactly(
			"61438-8",
			"LL1000-0",
			"10061-0",
			"LL1311-1",
			"10331-7",
			"LL360-9",
			"10389-5",
			"LL2413-4",
			"10390-3",
			"LL2422-5",
			"10393-7",
			"LL2420-9",
			"10395-2",
			"10401-8",
			"LL2421-7",
			"10410-9",
			"LL2417-5",
			"10568-4",
			"LL2427-4"
		);
		assertThat(cs.getConcept().get(0).getProperty().stream().map(t -> t.getCode() + "=" + t.getValue().toString()))
			.containsExactly("answer-list=LL1000-0");

		verify(myDataSink, times(1)).acceptForFutureStep(myStepIdCaptor.capture(), myFileSetCaptor.capture());
		assertThat(renderEmittedChunks()).containsExactly(
			"finalize-import -> RecordsAdded: From[step-1] Counts[conceptsAdded=1]"
		);
	}


}
