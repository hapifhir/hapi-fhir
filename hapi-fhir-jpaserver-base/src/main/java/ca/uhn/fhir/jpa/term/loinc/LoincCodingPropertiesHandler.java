/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.term.loinc;

import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.term.IZipContentsHandlerCsv;
import ca.uhn.fhir.jpa.term.api.ITermLoaderSvc;
import org.apache.commons.csv.CSVRecord;
import org.hl7.fhir.r4.model.CodeSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.trim;

/**
 * Handler to process coding type properties 'AskAtOrderEntry' and 'AssociatedObservations'.
 *
 * These properties are added in a specific handler which is involved after all TermConcepts
 * are created, because they require a 'display' value associated to other TermConcept (pointed by the 'code'
 * property value), which require that concept to have been created.
 */
public class LoincCodingPropertiesHandler implements IZipContentsHandlerCsv {
	private static final Logger ourLog = LoggerFactory.getLogger(LoincCodingPropertiesHandler.class);

	public static final String ASK_AT_ORDER_ENTRY_PROP_NAME = "AskAtOrderEntry";
	public static final String ASSOCIATED_OBSERVATIONS_PROP_NAME = "AssociatedObservations";
	public static final String LOINC_NUM = "LOINC_NUM";

	private final Map<String, TermConcept> myCode2Concept;
	private final Map<String, CodeSystem.PropertyType> myPropertyNameTypeMap;

	public LoincCodingPropertiesHandler(
			Map<String, TermConcept> theCode2concept, Map<String, CodeSystem.PropertyType> thePropertyNameTypeMap) {
		myCode2Concept = theCode2concept;
		myPropertyNameTypeMap = thePropertyNameTypeMap;
	}

	@Override
	public void accept(CSVRecord theRecord) {
		if (!anyValidProperty()) {
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

		TermConcept srcTermConcept = myCode2Concept.get(code);

		if (isNotBlank(askAtOrderEntryValue)) {
			addCodingProperties(srcTermConcept, ASK_AT_ORDER_ENTRY_PROP_NAME, askAtOrderEntryValue);
		}

		if (isNotBlank(associatedObservationsValue)) {
			addCodingProperties(srcTermConcept, ASSOCIATED_OBSERVATIONS_PROP_NAME, associatedObservationsValue);
		}
	}

	/**
	 * Validates that at least one ot target properties is defined in loinc.xml file and is of type "CODING"
	 */
	private boolean anyValidProperty() {
		CodeSystem.PropertyType askAtOrderEntryPropType = myPropertyNameTypeMap.get(ASK_AT_ORDER_ENTRY_PROP_NAME);
		CodeSystem.PropertyType associatedObservationsPropType =
				myPropertyNameTypeMap.get(ASSOCIATED_OBSERVATIONS_PROP_NAME);

		return askAtOrderEntryPropType == CodeSystem.PropertyType.CODING
				|| associatedObservationsPropType == CodeSystem.PropertyType.CODING;
	}

	private void addCodingProperties(TermConcept theSrcTermConcept, String thePropertyName, String thePropertyValue) {
		List<String> propertyCodeValues = parsePropertyCodeValues(thePropertyValue);
		for (String propertyCodeValue : propertyCodeValues) {
			TermConcept targetTermConcept = myCode2Concept.get(propertyCodeValue);
			if (targetTermConcept == null) {
				ourLog.error(
						"Couldn't find TermConcept for code: '{}'. Display property set to blank for property: '{}'",
						propertyCodeValue,
						thePropertyName);
				continue;
			}
			theSrcTermConcept.addPropertyCoding(
					thePropertyName, ITermLoaderSvc.LOINC_URI, propertyCodeValue, targetTermConcept.getDisplay());
			ourLog.trace("Adding coding property: {} to concept.code {}", thePropertyName, theSrcTermConcept.getCode());
		}
	}

	private List<String> parsePropertyCodeValues(String theValue) {
		return Arrays.stream(theValue.split(";")).map(String::trim).collect(Collectors.toList());
	}
}
