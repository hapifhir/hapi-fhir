package ca.uhn.fhir.jpa.batch2.jobs.term.loinc;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.jpa.term.api.ITermLoaderSvc;
import ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum;
import jakarta.annotation.Nonnull;
import org.apache.commons.csv.CSVRecord;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.Enumerations;

import java.util.List;
import java.util.Properties;

import static ca.uhn.fhir.jpa.term.loinc.LoincIeeeMedicalDeviceCodeHandler.LOINC_IEEE_CM_ID;
import static ca.uhn.fhir.jpa.term.loinc.LoincIeeeMedicalDeviceCodeHandler.LOINC_IEEE_CM_NAME;
import static ca.uhn.fhir.jpa.term.loinc.LoincIeeeMedicalDeviceCodeHandler.LOINC_IEEE_CM_URI;
import static ca.uhn.fhir.jpa.term.loinc.LoincRsnaPlaybookHandler.CM_COPYRIGHT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_CONCEPTMAP_VERSION;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.trim;

public class ImportLoincStep11HandleIeeeMedicalDeviceCode
		extends BaseImportLoincStepWithValueSetsAndConceptMaps<ImportLoincStep11HandleIeeeMedicalDeviceCode.MyBaseContext> {

	@Override
	protected MyBaseContext newContextObject(
			StepExecutionDetails<ImportLoincJobParameters, ImportLoincFileSetJson> theStepExecutionDetails) {
		return new MyBaseContext(theStepExecutionDetails);
	}

	@Nonnull
	@Override
	protected List<LoincFileNameSpecification> getFilesToProcess(StepExecutionDetails<ImportLoincJobParameters, ?> theStepExecutionDetails) {
		return List.of(new LoincFileNameSpecification(
				LoincUploadPropertiesEnum.LOINC_IEEE_MEDICAL_DEVICE_CODE_MAPPING_TABLE_FILE,
				LoincUploadPropertiesEnum.LOINC_IEEE_MEDICAL_DEVICE_CODE_MAPPING_TABLE_FILE_DEFAULT));
	}


	@Nonnull
	@Override
	public FileHandlingType getFileHandlingType() {
		return FileHandlingType.CSV_SPLIT_WITH_REPEAT_HEADER_50000_LINE_CHUNKS;
	}

	// FIXME: remove
	@Nonnull
	@Override
	public RunOutcome run(@Nonnull StepExecutionDetails<ImportLoincJobParameters, ImportLoincFileSetJson> theStepExecutionDetails, @Nonnull IJobDataSink<ImportLoincFileSetJson> theDataSink) throws JobExecutionFailedException {
		return super.run(theStepExecutionDetails, theDataSink);
	}

	@Override
	protected void handleRecord(
		StepExecutionDetails<ImportLoincJobParameters, ImportLoincFileSetJson> theStepExecutionDetails, ImportLoincJobParameters theJobParameters,
		MyBaseContext theContext,
		CSVRecord theRecord,
		CodeSystem theCodeSystemToPopulate,
		ImportLoincFileSetJson theData, String theSourceFilename) {
		String codeSystemVersionId = theData.getLoincCodeSystem().getVersion();
		String loincIeeeCmVersion;
		Properties jobProperties = getJobProperties(theStepExecutionDetails);
		if (isNotBlank(jobProperties.getProperty(LOINC_CONCEPTMAP_VERSION.getCode()))) {
				loincIeeeCmVersion = jobProperties.getProperty(LOINC_CONCEPTMAP_VERSION.getCode()) + "-"					+ codeSystemVersionId;
		} else {
			loincIeeeCmVersion = codeSystemVersionId;
		}
		String loincNumber = trim(theRecord.get("LOINC_NUM"));
		String longCommonName = trim(theRecord.get("LOINC_LONG_COMMON_NAME"));
		String ieeeCode = trim(theRecord.get("IEEE_CF_CODE10"));
		String ieeeDisplayName = trim(theRecord.get("IEEE_REFID"));

		// LOINC Part -> IEEE 11073:10101 Mappings
		String sourceCodeSystemUri = ITermLoaderSvc.LOINC_URI;
		String targetCodeSystemUri = ITermLoaderSvc.IEEE_11073_10101_URI;
		String loincCopyrightStatement = theData.getLoincCodeSystem().getCopyright();
		String conceptMapId = LOINC_IEEE_CM_ID + "-" + codeSystemVersionId;

		addConceptMapEntry(
			theContext,
				new ConceptMapping()
						.setConceptMapId(conceptMapId)
						.setConceptMapUri(LOINC_IEEE_CM_URI)
						.setConceptMapVersion(loincIeeeCmVersion)
						.setConceptMapName(LOINC_IEEE_CM_NAME)
						.setSourceCodeSystem(sourceCodeSystemUri)
						.setSourceCodeSystemVersion(codeSystemVersionId)
						.setSourceCode(loincNumber)
						.setSourceDisplay(longCommonName)
						.setTargetCodeSystem(targetCodeSystemUri)
						.setTargetCode(ieeeCode)
						.setTargetDisplay(ieeeDisplayName)
						.setEquivalence(Enumerations.ConceptMapEquivalence.EQUAL)
						.setCopyright(loincCopyrightStatement + " " + CM_COPYRIGHT));
	}

}
