package ca.uhn.fhir.jpa.batch2.jobs.term.loinc;

import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.LookupCodeRequest;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.jpa.searchparam.extractor.StringTrimmingTrimmerMatcher;
import ca.uhn.fhir.jpa.term.api.ITermLoaderSvc;
import jakarta.annotation.Nonnull;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.text.StringTokenizer;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.Coding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_CONSUMER_NAME_FILE;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_CONSUMER_NAME_FILE_DEFAULT;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_LINGUISTIC_VARIANTS_FILE;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_LINGUISTIC_VARIANTS_FILE_DEFAULT;
import static org.apache.commons.lang3.StringUtils.*;
import static org.hl7.fhir.common.hapi.validation.support.ValidationConstants.LOINC_GENERIC_CODE_SYSTEM_URL;

public class ImportLoincStep20LinguisticVariants extends BaseImportLoincStepWithValueSetsAndConceptMaps<BaseImportLoincStepWithValueSetsAndConceptMaps.MyBaseContext> {
	private static final Logger ourLog = LoggerFactory.getLogger(ImportLoincStep20LinguisticVariants.class);

	private static final String ASK_AT_ORDER_ENTRY_PROP_NAME = "AskAtOrderEntry";
	private static final String ASSOCIATED_OBSERVATIONS_PROP_NAME = "AssociatedObservations";
	private static final String LOINC_NUM = "LOINC_NUM";

	@Autowired
	private IValidationSupport myValidationSupport;

	@Override
	protected MyBaseContext newContextObject(StepExecutionDetails<LoincJobImportParameters, ImportLoincFileSetJson> theStepExecutionDetails) {
		return new MyBaseContext(theStepExecutionDetails);
	}

	@Nonnull
	@Override
	protected List<PropertyNameAndDefault> getFilesToProcess() {
		return List.of(
			new PropertyNameAndDefault(LOINC_LINGUISTIC_VARIANTS_FILE, LOINC_LINGUISTIC_VARIANTS_FILE_DEFAULT)
		);
	}

	@Override
	protected void handleRecord(LoincJobImportParameters theJobParameters, MyBaseContext theContext, CSVRecord theRecord, CodeSystem theCodeSystemToPopulate, ImportLoincFileSetJson theData) {

	}

}
