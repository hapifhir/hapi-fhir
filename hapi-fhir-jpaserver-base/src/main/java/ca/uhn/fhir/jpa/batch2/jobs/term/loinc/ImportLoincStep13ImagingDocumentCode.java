package ca.uhn.fhir.jpa.batch2.jobs.term.loinc;

import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.jpa.term.api.ITermLoaderSvc;
import ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum;
import jakarta.annotation.Nonnull;
import org.apache.commons.csv.CSVRecord;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.ValueSet;

import java.util.List;

import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_IMAGING_DOCUMENT_CODES_FILE;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_IMAGING_DOCUMENT_CODES_FILE_DEFAULT;
import static org.apache.commons.lang3.StringUtils.trim;

public class ImportLoincStep13ImagingDocumentCode extends BaseImportLoincStepWithValueSetsAndConceptMaps<BaseImportLoincStepWithValueSetsAndConceptMaps.MyBaseContext> {

	private static final String VS_ID_BASE = "loinc-imaging-document-codes";
	private static final String VS_URI = "http://loinc.org/vs/loinc-imaging-document-codes";
	private static final String VS_NAME = "LOINC Imaging Document Codes";

	@Override
	protected MyBaseContext newContextObject(StepExecutionDetails<LoincJobImportParameters, ImportLoincFileSetJson> theStepExecutionDetails) {
		return new MyBaseContext();
	}

	@Nonnull
	@Override
	protected List<PropertyNameAndDefault> getFilesToProcess() {
		return List.of(
			new PropertyNameAndDefault(LoincUploadPropertiesEnum.LOINC_IMAGING_DOCUMENT_CODES_FILE, LoincUploadPropertiesEnum.LOINC_IMAGING_DOCUMENT_CODES_FILE_DEFAULT)
		);
	}

	@Override
	protected void handleRecord(LoincJobImportParameters theJobParameters, MyBaseContext theContext, CSVRecord theRecord, CodeSystem theCodeSystemToPopulate, ImportLoincFileSetJson theData) {
		String loincNumber = trim(theRecord.get("LOINC_NUM"));
		String displayName = trim(theRecord.get("LONG_COMMON_NAME"));

		ValueSet valueSet = getValueSet(theJobParameters, theData, theContext, VS_ID_BASE, VS_URI, VS_NAME, null);
		addCodeAsIncludeToValueSet(valueSet, ITermLoaderSvc.LOINC_URI, loincNumber, displayName);
	}

}
