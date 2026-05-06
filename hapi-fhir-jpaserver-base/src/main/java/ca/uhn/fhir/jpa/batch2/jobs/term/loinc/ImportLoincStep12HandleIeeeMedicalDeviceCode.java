package ca.uhn.fhir.jpa.batch2.jobs.term.loinc;

import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.jpa.term.api.ITermLoaderSvc;
import ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum;
import jakarta.annotation.Nonnull;
import org.apache.commons.csv.CSVRecord;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.Enumerations;

import java.util.List;

import static ca.uhn.fhir.jpa.term.loinc.LoincIeeeMedicalDeviceCodeHandler.LOINC_IEEE_CM_ID;
import static ca.uhn.fhir.jpa.term.loinc.LoincIeeeMedicalDeviceCodeHandler.LOINC_IEEE_CM_NAME;
import static ca.uhn.fhir.jpa.term.loinc.LoincIeeeMedicalDeviceCodeHandler.LOINC_IEEE_CM_URI;
import static ca.uhn.fhir.jpa.term.loinc.LoincRsnaPlaybookHandler.CM_COPYRIGHT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_CONCEPTMAP_VERSION;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_IEEE_MEDICAL_DEVICE_CODE_MAPPING_TABLE_FILE;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_IEEE_MEDICAL_DEVICE_CODE_MAPPING_TABLE_FILE_DEFAULT;
import static org.apache.commons.lang3.StringUtils.trim;

public class ImportLoincStep12HandleIeeeMedicalDeviceCode extends BaseImportLoincStepWithValueSetsAndConceptMaps<ImportLoincStep12HandleIeeeMedicalDeviceCode.MyContext> {

	@Override
	protected MyContext newContextObject(StepExecutionDetails<LoincJobImportParameters, ImportLoincFileSetJson> theStepExecutionDetails) {
		return new MyContext();
	}

	@Nonnull
	@Override
	protected List<PropertyNameAndDefault> getFilesToProcess() {
		return List.of(
			new PropertyNameAndDefault(LoincUploadPropertiesEnum.LOINC_IEEE_MEDICAL_DEVICE_CODE_MAPPING_TABLE_FILE, LoincUploadPropertiesEnum.LOINC_IEEE_MEDICAL_DEVICE_CODE_MAPPING_TABLE_FILE_DEFAULT)
		);
	}

	@Override
	protected void handleRecord(LoincJobImportParameters theJobParameters, MyContext theContext, CSVRecord theRecord, CodeSystem theCodeSystemToPopulate, ImportLoincFileSetJson theData) {
		String codeSystemVersionId = theData.getLoincCodeSystem().getVersion();
		String loincIeeeCmVersion;
		if (codeSystemVersionId != null) {
			loincIeeeCmVersion =
				theJobParameters.getProperties().getProperty(LOINC_CONCEPTMAP_VERSION.getCode()) + "-" + codeSystemVersionId;
		} else {
			loincIeeeCmVersion = theJobParameters.getProperties().getProperty(LOINC_CONCEPTMAP_VERSION.getCode());
		}
		String loincNumber = trim(theRecord.get("LOINC_NUM"));
		String longCommonName = trim(theRecord.get("LOINC_LONG_COMMON_NAME"));
		String ieeeCode = trim(theRecord.get("IEEE_CF_CODE10"));
		String ieeeDisplayName = trim(theRecord.get("IEEE_REFID"));

		// LOINC Part -> IEEE 11073:10101 Mappings
		String sourceCodeSystemUri = ITermLoaderSvc.LOINC_URI;
		String targetCodeSystemUri = ITermLoaderSvc.IEEE_11073_10101_URI;
		String loincCopyrightStatement = theData.getLoincCodeSystem().getCopyright();
		addConceptMapEntry(
			theData,
			theContext,
			new ConceptMapping()
				.setConceptMapId(LOINC_IEEE_CM_ID)
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
				.setCopyright(loincCopyrightStatement + " " + CM_COPYRIGHT)
			);
	}


	protected static class MyContext extends MyBaseContext {

	}

}
