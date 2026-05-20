package ca.uhn.fhir.jpa.batch2.jobs.term.loinc;

import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.jpa.term.api.ITermLoaderSvc;
import ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum;
import jakarta.annotation.Nonnull;
import org.apache.commons.csv.CSVRecord;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.ValueSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static ca.uhn.fhir.jpa.term.loinc.LoincTop2000LabResultsUsHandler.TOP_2000_US_VS_ID;
import static ca.uhn.fhir.jpa.term.loinc.LoincTop2000LabResultsUsHandler.TOP_2000_US_VS_NAME;
import static ca.uhn.fhir.jpa.term.loinc.LoincTop2000LabResultsUsHandler.TOP_2000_US_VS_URI;
import static org.apache.commons.lang3.StringUtils.trim;

public class ImportLoincStep9HandleTop2000CodesUs
		extends BaseImportLoincStepWithValueSetsAndConceptMaps<ImportLoincStep9HandleTop2000CodesUs.MyBaseContext> {
	private static final Logger ourLog = LoggerFactory.getLogger(ImportLoincStep9HandleTop2000CodesUs.class);

	private final String myValueSetId;
	private final String myValueSetUri;
	private final String myValueSetName;

	/**
	 * Constructor for US LOINC Top 2000 Codes
	 */
	public ImportLoincStep9HandleTop2000CodesUs() {
		this(TOP_2000_US_VS_ID, TOP_2000_US_VS_URI, TOP_2000_US_VS_NAME);
	}

	/**
	 * Constructor
	 */
	protected ImportLoincStep9HandleTop2000CodesUs(
			String theValueSetId, String theValueSetUri, String theValueSetName) {
		myValueSetId = theValueSetId;
		myValueSetUri = theValueSetUri;
		myValueSetName = theValueSetName;
	}

	@Override
	protected MyBaseContext newContextObject(
			StepExecutionDetails<ImportLoincJobParameters, ImportLoincFileSetJson> theStepExecutionDetails) {
		return new MyBaseContext(theStepExecutionDetails);
	}

	@Nonnull
	@Override
	protected List<LoincFileNameSpecification> getFilesToProcess(StepExecutionDetails<ImportLoincJobParameters, ?> theStepExecutionDetails) {
		return List.of(new LoincFileNameSpecification(
				LoincUploadPropertiesEnum.LOINC_TOP2000_COMMON_LAB_RESULTS_US_FILE,
				LoincUploadPropertiesEnum.LOINC_TOP2000_COMMON_LAB_RESULTS_US_FILE_DEFAULT));
	}

	@Override
	protected void handleRecord(
		StepExecutionDetails<ImportLoincJobParameters, ImportLoincFileSetJson> theStepExecutionDetails, ImportLoincJobParameters theJobParameters,
		MyBaseContext theContext,
		CSVRecord theRecord,
		CodeSystem theCodeSystemToPopulate,
		ImportLoincFileSetJson theData, String theSourceFilename) {
		String loincNumber = trim(theRecord.get("LOINC #"));
		String displayName = trim(theRecord.get("Long Common Name"));

		ValueSet valueSet =
				getValueSet(theStepExecutionDetails, theJobParameters, theData, theContext, myValueSetId, myValueSetUri, myValueSetName, null);
		addCodeAsIncludeToValueSet(valueSet, ITermLoaderSvc.LOINC_URI, loincNumber, displayName);
	}

}
