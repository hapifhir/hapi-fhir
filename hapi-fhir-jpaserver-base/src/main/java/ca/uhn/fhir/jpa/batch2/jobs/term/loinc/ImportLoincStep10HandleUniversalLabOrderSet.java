package ca.uhn.fhir.jpa.batch2.jobs.term.loinc;

import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.jpa.term.api.ITermLoaderSvc;
import ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum;
import jakarta.annotation.Nonnull;
import org.apache.commons.csv.CSVRecord;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.ValueSet;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.trim;

public class ImportLoincStep10HandleUniversalLabOrderSet
		extends BaseImportLoincStepWithValueSetsAndConceptMaps<
				BaseImportLoincStepWithValueSetsAndConceptMaps.MyBaseContext> {

	private static final String VS_ID_BASE = "loinc-universal-order-set";
	private static final String VS_URI = "http://loinc.org/vs/loinc-universal-order-set";
	private static final String VS_NAME = "LOINC Universal Order Set";

	@Override
	protected MyBaseContext newContextObject(
			StepExecutionDetails<ImportLoincJobParameters, ImportLoincFileSetJson> theStepExecutionDetails) {
		return new MyBaseContext(theStepExecutionDetails);
	}

	@Nonnull
	@Override
	protected List<LoincFileNameSpecification> getFilesToProcess(StepExecutionDetails<ImportLoincJobParameters, ?> theStepExecutionDetails) {
		return List.of(new LoincFileNameSpecification(
				LoincUploadPropertiesEnum.LOINC_UNIVERSAL_LAB_ORDER_VALUESET_FILE,
				LoincUploadPropertiesEnum.LOINC_UNIVERSAL_LAB_ORDER_VALUESET_FILE_DEFAULT));
	}

	@Override
	protected void handleRecord(
		StepExecutionDetails<ImportLoincJobParameters, ImportLoincFileSetJson> theStepExecutionDetails, ImportLoincJobParameters theJobParameters,
		MyBaseContext theContext,
		CSVRecord theRecord,
		CodeSystem theCodeSystemToPopulate,
		ImportLoincFileSetJson theData, String theSourceFilename) {
		String loincNumber = trim(theRecord.get("LOINC_NUM"));
		String displayName = trim(theRecord.get("LONG_COMMON_NAME"));
		String orderObs = trim(theRecord.get("ORDER_OBS"));

		ValueSet valueSet = getValueSet(theStepExecutionDetails, theJobParameters, theData, theContext, VS_ID_BASE, VS_URI, VS_NAME, null);
		addCodeAsIncludeToValueSet(valueSet, ITermLoaderSvc.LOINC_URI, loincNumber, displayName);
	}
}
