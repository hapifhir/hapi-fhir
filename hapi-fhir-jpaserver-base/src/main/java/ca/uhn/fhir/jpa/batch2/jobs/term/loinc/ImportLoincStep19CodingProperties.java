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
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.trim;
import static org.hl7.fhir.common.hapi.validation.support.ValidationConstants.LOINC_GENERIC_CODE_SYSTEM_URL;

/**
 * Handler to process coding type properties 'AskAtOrderEntry' and 'AssociatedObservations'.
 *
 * These properties are added in a specific handler which is involved after all TermConcepts
 * are created, because they require a 'display' value associated to other TermConcept (pointed by the 'code'
 * property value), which requires that concept to have been created.
 */
public class ImportLoincStep19CodingProperties extends BaseImportLoincStepWithValueSetsAndConceptMaps<BaseImportLoincStepWithValueSetsAndConceptMaps.MyBaseContext> {
	private static final Logger ourLog = LoggerFactory.getLogger(ImportLoincStep19CodingProperties.class);

	private static final String ASK_AT_ORDER_ENTRY_PROP_NAME = "AskAtOrderEntry";
	private static final String ASSOCIATED_OBSERVATIONS_PROP_NAME = "AssociatedObservations";
	private static final String LOINC_NUM = "LOINC_NUM";

	@Autowired
	private IValidationSupport myValidationSupport;

	@Override
	protected MyBaseContext newContextObject(StepExecutionDetails<ImportLoincJobParameters, ImportLoincFileSetJson> theStepExecutionDetails) {
		return new MyBaseContext(theStepExecutionDetails);
	}

	@Nonnull
	@Override
	protected List<LoincFileNameSpecification> getFilesToProcess() {
		return List.of(
			new LoincFileNameSpecification(LOINC_CONSUMER_NAME_FILE, LOINC_CONSUMER_NAME_FILE_DEFAULT)
		);
	}

	@Override
	protected void handleRecord(StepExecutionDetails<ImportLoincJobParameters, ImportLoincFileSetJson> theStepExecutionDetails, ImportLoincJobParameters theJobParameters, MyBaseContext theContext, CSVRecord theRecord, CodeSystem theCodeSystemToPopulate, ImportLoincFileSetJson theData, String theSourceFilename) {
		if (!anyValidProperty(theContext)) {
			return;
		}

		String code = trim(theRecord.get(LOINC_NUM));
		if (isBlank(code)) {
			return;
		}

		String askAtOrderEntryValue = trim(theRecord.get(ASK_AT_ORDER_ENTRY_PROP_NAME));
		String associatedObservationsValue = trim(theRecord.get(ASSOCIATED_OBSERVATIONS_PROP_NAME));

		// any of the record properties have a valid value?
		if (isBlank(askAtOrderEntryValue) && isBlank(associatedObservationsValue)) {
			return;
		}

		CodeSystem.ConceptDefinitionComponent srcTermConcept = getOrAddConcept(theContext, theCodeSystemToPopulate, code);

		if (isNotBlank(askAtOrderEntryValue)) {
			addCodingProperties(theData, srcTermConcept, ASK_AT_ORDER_ENTRY_PROP_NAME, askAtOrderEntryValue);
		}

		if (isNotBlank(associatedObservationsValue)) {
			addCodingProperties(theData, srcTermConcept, ASSOCIATED_OBSERVATIONS_PROP_NAME, associatedObservationsValue);
		}
	}

	/**
	 * Validates that at least one ot target properties is defined in the loinc.xml file and is of the type "CODING"
	 */
	private boolean anyValidProperty(MyBaseContext theContext) {
		CodeSystem.PropertyType askAtOrderEntryPropType = theContext.getPropertyNameToType().get(ASK_AT_ORDER_ENTRY_PROP_NAME);
		CodeSystem.PropertyType associatedObservationsPropType =
			theContext.getPropertyNameToType().get(ASSOCIATED_OBSERVATIONS_PROP_NAME);

		return askAtOrderEntryPropType == CodeSystem.PropertyType.CODING
			|| associatedObservationsPropType == CodeSystem.PropertyType.CODING;
	}

	private void addCodingProperties(ImportLoincFileSetJson theData, CodeSystem.ConceptDefinitionComponent theConceptToPopulate, String thePropertyName, String thePropertyValue) {
		List<String> propertyCodeValues = parsePropertyCodeValues(thePropertyValue);
		for (String propertyCodeValue : propertyCodeValues) {

			String version = theData.getCodeSystemStagingVersionId();
			LookupCodeRequest request = new LookupCodeRequest(LOINC_GENERIC_CODE_SYSTEM_URL + "|" + version, propertyCodeValue);
			IValidationSupport.LookupCodeResult lookupResponse = myValidationSupport.lookupCode(new ValidationSupportContext(myValidationSupport), request);
			if (lookupResponse == null || !lookupResponse.isFound()) {
				ourLog.error(
					"Couldn't find TermConcept for code: '{}'. Display property set to blank for property: '{}'",
					propertyCodeValue,
					thePropertyName);
				continue;
			}

			ourLog.trace("Adding coding property: {} to concept.code {}", thePropertyName, theConceptToPopulate.getCode());
			theConceptToPopulate
				.addProperty()
				.setCode(thePropertyName)
				.setValue(new Coding(ITermLoaderSvc.LOINC_URI, propertyCodeValue, lookupResponse.getCodeDisplay()));
		}
	}

	private List<String> parsePropertyCodeValues(String theValue) {
		StringTokenizer tokenizer = new StringTokenizer(theValue, ";");
		tokenizer.setIgnoreEmptyTokens(true);
		tokenizer.setTrimmerMatcher(new StringTrimmingTrimmerMatcher());
		return tokenizer.getTokenList();
	}

}
