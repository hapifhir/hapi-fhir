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
class ImportLoincStep2HandleConceptsTest extends BaseImportLoincStepTest {


	@InjectMocks
	private ImportLoincStep2HandleConcepts mySvc;

	@Test
	void run_LoadCodes() {
		// Setup
		String classpath = "loinc-ver/v269/LoincTable/Loinc.csv";
		mockFetchAttachment(classpath);
		mockFetchJobMetadataAttachment();
		UploadStatistics uploadStatistics = new UploadStatistics(new IdType());
		uploadStatistics.incrementConceptsAddedCount();
		uploadStatistics.incrementConceptsAddedCount();
		uploadStatistics.incrementConceptsAddedCount();
		when(myTermCodeSystemStorageSvc.uploadCodeSystemConcepts(any())).thenReturn(uploadStatistics);

		// Test
		mySvc.run(newStepExecutionDetails(classpath), myDataSink);

		// Verify
		verify(myTermCodeSystemStorageSvc, times(1)).uploadCodeSystemConcepts(myCodeSystemCaptor.capture());
		CodeSystem cs = (CodeSystem) myCodeSystemCaptor.getValue();
		assertThat(cs.getConcept().stream().map(CodeSystem.ConceptDefinitionComponent::getCode)).containsExactly(
			"10013-1",
			"10014-9",
			"10015-6",
			"10016-4",
			"1001-7",
			"10017-2",
			"10018-0",
			"10019-8",
			"10020-6",
			"61438-8",
			"10000-8",
			"17787-3",
			"17788-1",
			"11488-4",
			"47239-9"
		);
		assertThat(cs.getConcept().get(0).getProperty().stream().map(t->t.getCode() + "=" + t.getValue().toString()))
			.containsExactly("CLASSTYPE=2",
				"VersionLastChanged=2.48",
				"STATUS=ACTIVE",
				"CHNG_TYPE=MIN",
				"ORDER_OBS=Observation",
				"COMMON_TEST_RANK=0",
				"SHORTNAME=R' wave Amp L-I",
				"LONG_COMMON_NAME=R' wave amplitude in lead I",
				"COMMON_ORDER_RANK=0",
				"UNITSREQUIRED=Y",
				"RELATEDNAMES2=Cardiac; ECG; EKG.MEASUREMENTS; Electrical potential; Electrocardiogram; Electrocardiograph; Hrt; Painter's colic; PB; Plumbism; Point in time; QNT; Quan; Quant; Quantitative; R prime; R' wave Amp L-I; R wave Amp L-I; Random; Right; Voltage",
				"EXAMPLE_UNITS=mV",
				"EXAMPLE_UCUM_UNITS=mV");

		verify(myDataSink, times(1)).acceptForFutureStep(myStepIdCaptor.capture(), myFileSetCaptor.capture());
		assertThat(renderEmittedChunks()).containsExactly(
			"finalize-import -> RecordsAdded: From[step-1] Counts[conceptsAdded=3]"
		);
	}



}
