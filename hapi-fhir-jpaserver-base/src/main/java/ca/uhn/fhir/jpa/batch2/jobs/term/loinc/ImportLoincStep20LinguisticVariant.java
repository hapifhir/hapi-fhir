package ca.uhn.fhir.jpa.batch2.jobs.term.loinc;

import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.term.api.ITermLoaderSvc;
import ca.uhn.fhir.jpa.util.ResourceCountCache;
import jakarta.annotation.Nonnull;
import org.apache.commons.csv.CSVRecord;
import org.hl7.fhir.r4.model.CodeSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_LINGUISTIC_VARIANTS_FILE;
import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.LOINC_LINGUISTIC_VARIANTS_FILE_DEFAULT;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.trim;
import static org.apache.commons.lang3.StringUtils.trimToEmpty;

public class ImportLoincStep20LinguisticVariant extends BaseImportLoincStepWithValueSetsAndConceptMaps<BaseImportLoincStepWithValueSetsAndConceptMaps.MyBaseContext> {
	private static final Logger ourLog = LoggerFactory.getLogger(ImportLoincStep20LinguisticVariant.class);

	private static final String ASK_AT_ORDER_ENTRY_PROP_NAME = "AskAtOrderEntry";
	private static final String ASSOCIATED_OBSERVATIONS_PROP_NAME = "AssociatedObservations";
	private static final String LOINC_NUM = "LOINC_NUM";
	public static final Pattern LINGUISTIC_VARIANT_FILENAME_PATTERN = Pattern.compile(".*LinguisticVariants/[a-zA-Z0-9]+LinguisticVariant.csv");

	@Autowired
	private IValidationSupport myValidationSupport;

	// FIXME: drop
	private Map<String, TermConcept> myCode2Concept;
	// FIXME: drop
	private String myLanguageCode;

	@Override
	protected MyBaseContext newContextObject(StepExecutionDetails<LoincJobImportParameters, ImportLoincFileSetJson> theStepExecutionDetails) {
		return new MyBaseContext(theStepExecutionDetails);
	}

	@Nonnull
	@Override
	protected List<LoincFileNameSpecification> getFilesToProcess() {
		return List.of(
			new LoincFileNameSpecification(LINGUISTIC_VARIANT_FILENAME_PATTERN)
		);
	}

	@Override
	protected void handleRecord(LoincJobImportParameters theJobParameters, MyBaseContext theContext, CSVRecord theRecord, CodeSystem theCodeSystemToPopulate, ImportLoincFileSetJson theData) {
		String loincNumber = trim(theRecord.get("LOINC_NUM"));
		if (isBlank(loincNumber)) {
			return;
		}

		TermConcept concept = myCode2Concept.get(loincNumber);
		if (concept == null) {
			return;
		}

		// The following should be created as designations for each term:
		// COMPONENT:PROPERTY:TIME_ASPCT:SYSTEM:SCALE_TYP:METHOD_TYP (as colon-separated concatenation - FormalName)
		// SHORTNAME
		// LONG_COMMON_NAME
		// LinguisticVariantDisplayName

		// -- add formalName designation
		StringBuilder fullySpecifiedName = new StringBuilder();
		fullySpecifiedName.append(trimToEmpty(theRecord.get("COMPONENT") + ":"));
		fullySpecifiedName.append(trimToEmpty(theRecord.get("PROPERTY") + ":"));
		fullySpecifiedName.append(trimToEmpty(theRecord.get("TIME_ASPCT") + ":"));
		fullySpecifiedName.append(trimToEmpty(theRecord.get("SYSTEM") + ":"));
		fullySpecifiedName.append(trimToEmpty(theRecord.get("SCALE_TYP") + ":"));
		fullySpecifiedName.append(trimToEmpty(theRecord.get("METHOD_TYP")));

		String fullySpecifiedNameStr = fullySpecifiedName.toString();

		// skip if COMPONENT, PROPERTY, TIME_ASPCT, SYSTEM, SCALE_TYP and METHOD_TYP are all empty
		if (!fullySpecifiedNameStr.equals(":::::")) {
			concept.addDesignation()
				.setLanguage(myLanguageCode)
				.setUseSystem(ITermLoaderSvc.LOINC_URI)
				.setUseCode("FullySpecifiedName")
				.setUseDisplay("FullySpecifiedName")
				.setValue(fullySpecifiedNameStr);
		}

		// -- other designations
		addDesignation(theRecord, concept, "SHORTNAME");
		addDesignation(theRecord, concept, "LONG_COMMON_NAME");
		addDesignation(theRecord, concept, "LinguisticVariantDisplayName");
	}

	private void addDesignation(CSVRecord theRecord, TermConcept concept, String fieldName) {

		String field = trim(theRecord.get(fieldName));
		if (isBlank(field)) {
			return;
		}

		concept.addDesignation()
			.setLanguage(myLanguageCode)
			.setUseSystem(ITermLoaderSvc.LOINC_URI)
			.setUseCode(fieldName)
			.setUseDisplay(fieldName)
			.setValue(field);
	}

}
