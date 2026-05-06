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

import static ca.uhn.fhir.jpa.term.loinc.LoincTop2000LabResultsUsHandler.TOP_2000_US_VS_ID;
import static ca.uhn.fhir.jpa.term.loinc.LoincTop2000LabResultsUsHandler.TOP_2000_US_VS_NAME;
import static ca.uhn.fhir.jpa.term.loinc.LoincTop2000LabResultsUsHandler.TOP_2000_US_VS_URI;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_TOP2000_COMMON_LAB_RESULTS_US_FILE;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_TOP2000_COMMON_LAB_RESULTS_US_FILE_DEFAULT;
import static org.apache.commons.lang3.StringUtils.trim;

public class ImportLoincStep9HandleTop2000CodesUs extends BaseImportLoincStepWithValueSetsAndConceptMaps<ImportLoincStep9HandleTop2000CodesUs.MyContext> {
	private static final Logger ourLog = LoggerFactory.getLogger(ImportLoincStep9HandleTop2000CodesUs.class);

	private final String myValueSetId;
	private final String myValueSetUri;
	private final String myValueSetName;

	/**
	 * Constructor for US LOINC Top 2000 Codes
	 */
	public ImportLoincStep9HandleTop2000CodesUs() {
		this(
			TOP_2000_US_VS_ID,
			TOP_2000_US_VS_URI,
			TOP_2000_US_VS_NAME
		);
	}

	/**
	 * Constructor
	 */
	protected ImportLoincStep9HandleTop2000CodesUs(String theValueSetId, String theValueSetUri, String theValueSetName) {
		myValueSetId = theValueSetId;
		myValueSetUri = theValueSetUri;
		myValueSetName = theValueSetName;
	}

	@Override
	protected MyContext newContextObject(StepExecutionDetails<LoincJobImportParameters, ImportLoincFileSetJson> theStepExecutionDetails) {
		return new MyContext();
	}

	@Nonnull
	@Override
	protected LoincUploadPropertiesEnum provideFileNameDefault() {
		return LOINC_TOP2000_COMMON_LAB_RESULTS_US_FILE_DEFAULT;
	}

	@Nonnull
	@Override
	protected LoincUploadPropertiesEnum provideFileNamePropertyFileKey() {
		return LOINC_TOP2000_COMMON_LAB_RESULTS_US_FILE;
	}

	@Override
	protected void handleRecord(LoincJobImportParameters theJobParameters, MyContext theContext, CSVRecord theRecord, CodeSystem theCodeSystemToPopulate, ImportLoincFileSetJson theData) {
		String loincNumber = trim(theRecord.get("LOINC #"));
		String displayName = trim(theRecord.get("Long Common Name"));

		ValueSet valueSet = getValueSet(theJobParameters, theData, theContext, myValueSetId, myValueSetUri, myValueSetName, null);
		addCodeAsIncludeToValueSet(valueSet, ITermLoaderSvc.LOINC_URI, loincNumber, displayName);
	}

	protected static class MyContext extends MyBaseContext {
		// nothing
	}

}
