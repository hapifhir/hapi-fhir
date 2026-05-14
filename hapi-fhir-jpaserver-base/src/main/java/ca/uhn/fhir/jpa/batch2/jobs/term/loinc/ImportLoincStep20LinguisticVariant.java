package ca.uhn.fhir.jpa.batch2.jobs.term.loinc;

import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.jpa.term.api.ITermLoaderSvc;
import jakarta.annotation.Nonnull;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.Coding;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.trim;
import static org.apache.commons.lang3.StringUtils.trimToEmpty;

public class ImportLoincStep20LinguisticVariant extends BaseImportLoincStepWithValueSetsAndConceptMaps<BaseImportLoincStepWithValueSetsAndConceptMaps.MyBaseContext> {
	public static final Pattern LINGUISTIC_VARIANT_FILENAME_PATTERN = Pattern.compile(".*LinguisticVariants/[a-zA-Z0-9]+LinguisticVariant.csv");

	@Autowired
	private IValidationSupport myValidationSupport;

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
	protected void handleRecord(StepExecutionDetails<LoincJobImportParameters, ImportLoincFileSetJson> theStepExecutionDetails, LoincJobImportParameters theJobParameters, MyBaseContext theContext, CSVRecord theRecord, CodeSystem theCodeSystemToPopulate, ImportLoincFileSetJson theData, String theSourceFilename) {
		String loincNumber = trim(theRecord.get("LOINC_NUM"));
		if (isBlank(loincNumber)) {
			return;
		}

		// loinc-ver/v269/AccessoryFiles/LinguisticVariants/deAT24LinguisticVariant.csv

		// FIXME: extract pattern as constant
		Pattern pattern = Pattern.compile(".*LinguisticVariants/([a-z]{2})([A-Z]{2})([0-9]+)LinguisticVariant.csv");
		Matcher matcher = pattern.matcher(theSourceFilename);
		Validate.isTrue(matcher.matches(), "Unexpected filename: %s", theSourceFilename);

		String language = matcher.group(1);
		String country = matcher.group(2);
		String languageCode = language + "-" + country;

		CodeSystem.ConceptDefinitionComponent concept = getOrAddConcept(theContext, theCodeSystemToPopulate, loincNumber);

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
				.setLanguage(languageCode)
				.setUse(new Coding(ITermLoaderSvc.LOINC_URI, "FullySpecifiedName", "FullySpecifiedName"))
				.setValue(fullySpecifiedNameStr);
		}

		// -- other designations
		addDesignation(theRecord, languageCode, concept, "SHORTNAME");
		addDesignation(theRecord, languageCode, concept, "LONG_COMMON_NAME");
		addDesignation(theRecord, languageCode, concept, "LinguisticVariantDisplayName");
	}

	private void addDesignation(CSVRecord theRecord, String theLanguageCode, CodeSystem.ConceptDefinitionComponent theConcept, String theFieldName) {
		String field = trim(theRecord.get(theFieldName));
		if (isBlank(field)) {
			return;
		}

		theConcept.addDesignation()
			.setLanguage(theLanguageCode)
			.setUse(new Coding(ITermLoaderSvc.LOINC_URI, theFieldName, theFieldName))
			.setValue(field);
	}

}
