/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.jpa.batch2.jobs.term.loinc;

import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ImportTerminologyMetadataAttachmentJson;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyFileSetJson;
import ca.uhn.fhir.jpa.term.api.ITermLoaderSvc;
import com.google.common.base.Splitter;
import jakarta.annotation.Nonnull;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.Coding;

import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.trim;
import static org.apache.commons.lang3.StringUtils.trimToEmpty;

/**
 * Because linguistic variant files are huge, take a long time to process, and aren't
 * needed for many use cases, we don't process them by default and this step ends up
 * being a no-op. But they can be enabled by adding a property to the job properties
 * with a key of {@link LoincUploadPropertiesEnum#LOINC_LINGUISTIC_VARIANTS_CODES}
 * and a value containing the language+country+code of the linguistic variants to
 * process, separated by commas. For example:
 * <code>deAT24, frCA8</code>
 */
public class ImportLoincStep20LinguisticVariant extends BaseImportLoincStep<BaseImportLoincStep.MyBaseContext> {

	public static final Pattern LINGUISTIC_VARIANT_FILENAME_PATTERN =
			Pattern.compile(".*LinguisticVariants/([a-z]{2})([A-Z]{2})([0-9]+)LinguisticVariant.csv");

	@Override
	public boolean mustFindFile() {
		return false;
	}

	@Override
	protected MyBaseContext newContextObject(
			StepExecutionDetails<ImportLoincJobParameters, TerminologyFileSetJson> theStepExecutionDetails) {
		return new MyBaseContext();
	}

	@Nonnull
	@Override
	protected List<LoincFileNameSpecification> getFilesToProcess(
			StepExecutionDetails<ImportLoincJobParameters, ?> theStepExecutionDetails) {
		Properties jobProperties = getJobProperties(theStepExecutionDetails);
		String linguisticCodes = jobProperties.getProperty(
				LoincUploadPropertiesEnum.LOINC_LINGUISTIC_VARIANTS_CODES.getCode(),
				LoincUploadPropertiesEnum.LOINC_LINGUISTIC_VARIANTS_CODES_DEFAULT.getCode());
		if (linguisticCodes.isBlank()) {
			return List.of();
		}

		Set<String> codes = Splitter.on(",")
				.trimResults()
				.omitEmptyStrings()
				.splitToStream(linguisticCodes)
				.collect(Collectors.toSet());
		Predicate<String> tester = (String theCode) -> {
			Matcher matcher = LINGUISTIC_VARIANT_FILENAME_PATTERN.matcher(theCode);
			if (matcher.matches()) {
				return codes.contains(matcher.group(1) + matcher.group(2) + matcher.group(3));
			}
			return false;
		};

		return List.of(
				new LoincFileNameSpecification(FileHandlingType.CSV_SPLIT_WITH_REPEAT_HEADER_1000_LINE_CHUNKS, tester));
	}

	@Override
	protected void handleRecord(
			StepExecutionDetails<ImportLoincJobParameters, TerminologyFileSetJson> theStepExecutionDetails,
			ImportTerminologyMetadataAttachmentJson theJobMetadata,
			ImportLoincJobParameters theJobParameters,
			MyBaseContext theContext,
			CSVRecord theRecord,
			CodeSystem theCodeSystemToPopulate,
			TerminologyFileSetJson theData,
			String theSourceFilename) {
		String loincNumber = trim(theRecord.get("LOINC_NUM"));
		if (isBlank(loincNumber)) {
			return;
		}

		Matcher matcher = LINGUISTIC_VARIANT_FILENAME_PATTERN.matcher(theSourceFilename);
		Validate.isTrue(matcher.matches(), "Unexpected filename: %s", theSourceFilename);

		String language = matcher.group(1);
		String country = matcher.group(2);
		String languageCode = language + "-" + country;

		CodeSystem.ConceptDefinitionComponent concept =
				getOrAddConcept(theContext, loincNumber);

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

	private void addDesignation(
			CSVRecord theRecord,
			String theLanguageCode,
			CodeSystem.ConceptDefinitionComponent theConcept,
			String theFieldName) {
		String field = trim(theRecord.get(theFieldName));
		if (isBlank(field)) {
			return;
		}

		theConcept
				.addDesignation()
				.setLanguage(theLanguageCode)
				.setUse(new Coding(ITermLoaderSvc.LOINC_URI, theFieldName, theFieldName))
				.setValue(field);
	}
}
