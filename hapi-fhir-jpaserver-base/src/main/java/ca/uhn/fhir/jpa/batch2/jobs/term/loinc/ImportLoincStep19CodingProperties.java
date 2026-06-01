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
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ImportTerminologyJobParameters;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.ImportTerminologyMetadataAttachmentJson;
import ca.uhn.fhir.jpa.batch2.jobs.term.base.TerminologyFileSetJson;
import ca.uhn.fhir.jpa.searchparam.extractor.StringTrimmingTrimmerMatcher;
import ca.uhn.fhir.jpa.term.api.ITermLoaderSvc;
import jakarta.annotation.Nonnull;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.text.StringTokenizer;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.Coding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static ca.uhn.fhir.jpa.batch2.jobs.term.loinc.LoincUploadPropertiesEnum.LOINC_FILE;
import static ca.uhn.fhir.jpa.batch2.jobs.term.loinc.LoincUploadPropertiesEnum.LOINC_FILE_DEFAULT;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.trim;

/**
 * Handler to process coding type properties 'AskAtOrderEntry' and 'AssociatedObservations'.
 *
 * These properties are added in a specific handler which is involved after all TermConcepts
 * are created, because they require a 'display' value associated to other TermConcept (pointed by the 'code'
 * property value), which requires that concept to have been created.
 *
 * @see ImportLoincJobAppCtx#importLoincStep19CodingProperties()
 */
public class ImportLoincStep19CodingProperties extends BaseImportLoincStep<BaseImportLoincStep.MyBaseContext> {
	private static final Logger ourLog = LoggerFactory.getLogger(ImportLoincStep19CodingProperties.class);

	private static final String ASK_AT_ORDER_ENTRY_PROP_NAME = "AskAtOrderEntry";
	private static final String ASSOCIATED_OBSERVATIONS_PROP_NAME = "AssociatedObservations";
	private static final String LOINC_NUM = "LOINC_NUM";

	@Override
	protected MyBaseContext newContextObject(
			StepExecutionDetails<ImportTerminologyJobParameters, TerminologyFileSetJson> theStepExecutionDetails) {
		return new MyBaseContext();
	}

	@Nonnull
	@Override
	public List<LoincFileNameSpecification> getFilesToProcess(
			StepExecutionDetails<ImportTerminologyJobParameters, ?> theStepExecutionDetails) {
		return List.of(new LoincFileNameSpecification(
				FileHandlingType.CSV_SPLIT_WITH_REPEAT_HEADER_50000_LINE_CHUNKS, LOINC_FILE, LOINC_FILE_DEFAULT));
	}

	@Override
	protected void handleRecord(
			StepExecutionDetails<ImportTerminologyJobParameters, TerminologyFileSetJson> theStepExecutionDetails,
			ImportTerminologyMetadataAttachmentJson theJobMetadata,
			ImportTerminologyJobParameters theJobParameters,
			MyBaseContext theContext,
			CSVRecord theRecord,
			CodeSystem theCodeSystemToPopulate,
			TerminologyFileSetJson theData,
			String theSourceFilename) {
		if (!anyValidProperty(theContext, theJobMetadata)) {
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

		CodeSystem.ConceptDefinitionComponent srcTermConcept = getOrAddConcept(theContext, code);

		if (isNotBlank(askAtOrderEntryValue)) {
			addCodingProperties(theJobMetadata, srcTermConcept, ASK_AT_ORDER_ENTRY_PROP_NAME, askAtOrderEntryValue);
		}

		if (isNotBlank(associatedObservationsValue)) {
			addCodingProperties(
					theJobMetadata, srcTermConcept, ASSOCIATED_OBSERVATIONS_PROP_NAME, associatedObservationsValue);
		}
	}

	/**
	 * Validates that at least one ot target properties is defined in the loinc.xml file and is of the type "CODING"
	 */
	private boolean anyValidProperty(MyBaseContext theContext, ImportTerminologyMetadataAttachmentJson theJobMetadata) {
		CodeSystem.PropertyType askAtOrderEntryPropType =
				theContext.getPropertyNameToType(theJobMetadata).get(ASK_AT_ORDER_ENTRY_PROP_NAME);
		CodeSystem.PropertyType associatedObservationsPropType =
				theContext.getPropertyNameToType(theJobMetadata).get(ASSOCIATED_OBSERVATIONS_PROP_NAME);

		return askAtOrderEntryPropType == CodeSystem.PropertyType.CODING
				|| associatedObservationsPropType == CodeSystem.PropertyType.CODING;
	}

	private void addCodingProperties(
			ImportTerminologyMetadataAttachmentJson theJobMetadata,
			CodeSystem.ConceptDefinitionComponent theConceptToPopulate,
			String thePropertyName,
			String thePropertyValue) {
		List<String> propertyCodeValues = parsePropertyCodeValues(thePropertyValue);
		for (String propertyCodeValue : propertyCodeValues) {

			IValidationSupport.LookupCodeResult lookupResponse =
					lookupPreExistingConcept(theJobMetadata, propertyCodeValue);
			if (lookupResponse == null || !lookupResponse.isFound()) {
				ourLog.error(
						"Couldn't find TermConcept for code: '{}'. Display property set to blank for property: '{}'",
						propertyCodeValue,
						thePropertyName);
				continue;
			}

			ourLog.trace(
					"Adding coding property: {} to concept.code {}", thePropertyName, theConceptToPopulate.getCode());
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
