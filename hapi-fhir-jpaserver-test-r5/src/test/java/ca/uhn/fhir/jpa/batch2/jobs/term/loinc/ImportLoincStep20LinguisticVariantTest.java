package ca.uhn.fhir.jpa.batch2.jobs.term.loinc;

import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.BaseImportTerminologyFileCsvStep;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyFileSetJson;
import ca.uhn.fhir.jpa.term.UploadStatistics;
import org.hl7.fhir.r5.model.CodeSystem;
import org.hl7.fhir.r5.model.IdType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;

import java.util.List;
import java.util.Properties;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ImportLoincStep20LinguisticVariantTest extends BaseImportLoincStepTest {

	@InjectMocks
	private ImportLoincStep20LinguisticVariant mySvc;

	@Test
	void testProcess() {
		// Setup
		String classpath = "loinc-ver/v269/AccessoryFiles/LinguisticVariants/frCA8LinguisticVariant.csv";
		mockFetchAttachment(classpath);
		mockFetchJobMetadataAttachment();
		when(myTermCodeSystemStorageSvc.uploadCodeSystemConcepts(any())).thenReturn(new UploadStatistics(new IdType()).incrementConceptsAddedCount());

		// Test
		mySvc.run(newStepExecutionDetails(classpath), myDataSink);

		// Verify
		verify(myTermCodeSystemStorageSvc, times(1)).uploadCodeSystemConcepts(myCodeSystemCaptor.capture());
		CodeSystem cs = (CodeSystem) myCodeSystemCaptor.getValue();
		String result = renderHierarchy(cs, true);
		String expected = """
			-61438-8
			  -Designation[lang=fr-CA, use={"system":"http://loinc.org","code":"FullySpecifiedName","display":"FullySpecifiedName"}]: Cellules de Purkinje cytoplasmique type 2 , IgG:Titre:Temps ponctuel:Sérum:Quantitatif:Immunofluorescence
			  -Designation[lang=fr-CA, use={"system":"http://loinc.org","code":"LONG_COMMON_NAME","display":"LONG_COMMON_NAME"}]: (example long french common name)
			-11704-4
			  -Designation[lang=fr-CA, use={"system":"http://loinc.org","code":"FullySpecifiedName","display":"FullySpecifiedName"}]: Gliale nucléaire de type 1 , IgG:Titre:Temps ponctuel:LCR:Quantitatif:Immunofluorescence
			-17787-3
			  -Designation[lang=fr-CA, use={"system":"http://loinc.org","code":"FullySpecifiedName","display":"FullySpecifiedName"}]: Virus respiratoire syncytial bovin:Présence-Seuil:Temps ponctuel:XXX:Ordinal:Culture spécifique à un microorganisme
			-17788-1
			  -Designation[lang=fr-CA, use={"system":"http://loinc.org","code":"FullySpecifiedName","display":"FullySpecifiedName"}]: Cellules de Purkinje cytoplasmique type 2 , IgG:Titre:Temps ponctuel:Sérum:Quantitatif:
			""";
		assertEquals(expected, result);

		verify(myDataSink, times(1)).acceptForFutureStep(myStepIdCaptor.capture(), myFileSetCaptor.capture());
		assertThat(renderEmittedChunks()).containsExactly(
			"finalize-import -> RecordsAdded: From[step-1] Counts[conceptsAdded=1]"
		);
	}

	@Test
	void testProcess_FileNotListedInLinguisticVariantFile() {
		// Setup
		mockFetchJobMetadataAttachment();
		mockFetchAttachment("loinc-ver/v269/AccessoryFiles/LinguisticVariants/frCA8LinguisticVariant.csv");
		when(myTermCodeSystemStorageSvc.uploadCodeSystemConcepts(any())).thenReturn(new UploadStatistics(new IdType()).incrementConceptsAddedCount());

		// Test
		StepExecutionDetails<ImportLoincJobParameters, TerminologyFileSetJson> stepExecutionDetails = newStepExecutionDetails("loinc-ver/v269/AccessoryFiles/LinguisticVariants/zzZZ8LinguisticVariant.csv");

		mySvc.run(stepExecutionDetails, myDataSink);

		// Verify
		verify(myTermCodeSystemStorageSvc, times(1)).uploadCodeSystemConcepts(myCodeSystemCaptor.capture());
		CodeSystem cs = myCodeSystemCaptor.getValue();
		String result = renderHierarchy(cs, true);
		String expected = """
			-61438-8
			  -Designation[lang=zz-ZZ, use={"system":"http://loinc.org","code":"FullySpecifiedName","display":"FullySpecifiedName"}]: Cellules de Purkinje cytoplasmique type 2 , IgG:Titre:Temps ponctuel:Sérum:Quantitatif:Immunofluorescence
			  -Designation[lang=zz-ZZ, use={"system":"http://loinc.org","code":"LONG_COMMON_NAME","display":"LONG_COMMON_NAME"}]: (example long french common name)
			-11704-4
			  -Designation[lang=zz-ZZ, use={"system":"http://loinc.org","code":"FullySpecifiedName","display":"FullySpecifiedName"}]: Gliale nucléaire de type 1 , IgG:Titre:Temps ponctuel:LCR:Quantitatif:Immunofluorescence
			-17787-3
			  -Designation[lang=zz-ZZ, use={"system":"http://loinc.org","code":"FullySpecifiedName","display":"FullySpecifiedName"}]: Virus respiratoire syncytial bovin:Présence-Seuil:Temps ponctuel:XXX:Ordinal:Culture spécifique à un microorganisme
			-17788-1
			  -Designation[lang=zz-ZZ, use={"system":"http://loinc.org","code":"FullySpecifiedName","display":"FullySpecifiedName"}]: Cellules de Purkinje cytoplasmique type 2 , IgG:Titre:Temps ponctuel:Sérum:Quantitatif:
			""";
		assertEquals(expected, result);

		verify(myDataSink, times(1)).acceptForFutureStep(myStepIdCaptor.capture(), myFileSetCaptor.capture());
		assertThat(renderEmittedChunks()).containsExactly(
			"finalize-import -> RecordsAdded: From[step-1] Counts[conceptsAdded=1]"
		);
	}

	@ParameterizedTest
	@CsvSource(delimiter = '|', textBlock = """
		deAT24, frCA8    | true
		deAT24           | true
		frCA8            | false
		                 | false
		""")
	void testGetFilesToProcess(String thePropertyValue, boolean theExpectMatch) {

		Properties properties = new Properties();
		if (isNotBlank(thePropertyValue)) {
			properties.setProperty(LoincUploadPropertiesEnum.LOINC_LINGUISTIC_VARIANTS_CODES.getCode(), thePropertyValue);
		}

		String filename = "Loinc_2.82/AccessoryFiles/LinguisticVariants/deAT24LinguisticVariant.csv";
		ImportLoincJobParameters jobParameters = new ImportLoincJobParameters();
		jobParameters.setJobProperties(properties);
		StepExecutionDetails<ImportLoincJobParameters, VoidModel> stepExecutionDetails = new StepExecutionDetails<>(jobParameters, new VoidModel(), new JobInstance(), new WorkChunk(), myJobExecutionServices, myJobDefinition, "step-1", "step-2");

		// Test
		List<BaseImportTerminologyFileCsvStep.LoincFileNameSpecification> outcome = mySvc.getFilesToProcess(stepExecutionDetails);

		// Verify
		boolean actual = outcome.stream().anyMatch(o -> o.fileNameTester().test(filename));
		assertEquals(theExpectMatch, actual);
	}

}
