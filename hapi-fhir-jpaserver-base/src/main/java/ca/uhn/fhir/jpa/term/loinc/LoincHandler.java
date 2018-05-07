package ca.uhn.fhir.jpa.term.loinc;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2018 University Health Network
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

import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.term.IHapiTerminologyLoaderSvc;
import ca.uhn.fhir.jpa.term.IRecordHandler;
import ca.uhn.fhir.jpa.term.TerminologyLoaderSvcImpl;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.r4.model.CodeSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.trim;

public class LoincHandler implements IRecordHandler {

	private final Map<String, TermConcept> myCode2Concept;
	private final TermCodeSystemVersion myCodeSystemVersion;
	private final Map<String, CodeSystem.PropertyType> myPropertyNames;
	private final Map<PartTypeAndPartName, String> myPartTypeAndPartNameToPartNumber;

	public LoincHandler(TermCodeSystemVersion theCodeSystemVersion, Map<String, TermConcept> theCode2concept, Map<String, CodeSystem.PropertyType> thePropertyNames, Map<PartTypeAndPartName, String> thePartTypeAndPartNameToPartNumber) {
		myCodeSystemVersion = theCodeSystemVersion;
		myCode2Concept = theCode2concept;
		myPropertyNames = thePropertyNames;
		myPartTypeAndPartNameToPartNumber = thePartTypeAndPartNameToPartNumber;
	}

	@Override
	public void accept(CSVRecord theRecord) {
		String code = trim(theRecord.get("LOINC_NUM"));
		if (isNotBlank(code)) {
			String longCommonName = trim(theRecord.get("LONG_COMMON_NAME"));
			String shortName = trim(theRecord.get("SHORTNAME"));
			String consumerName = trim(theRecord.get("CONSUMER_NAME"));
			String display = TerminologyLoaderSvcImpl.firstNonBlank(longCommonName, shortName, consumerName);

			TermConcept concept = new TermConcept(myCodeSystemVersion, code);
			concept.setDisplay(display);

			if (!display.equalsIgnoreCase(shortName)) {
				concept
					.addDesignation()
					.setUseDisplay("ShortName")
					.setValue(shortName);
			}

			for (String nextPropertyName : myPropertyNames.keySet()) {
				if (!theRecord.toMap().containsKey(nextPropertyName)) {
					continue;
				}

				CodeSystem.PropertyType nextPropertyType = myPropertyNames.get(nextPropertyName);

				String nextPropertyValue = theRecord.get(nextPropertyName);
				if (isNotBlank(nextPropertyValue)) {
					nextPropertyValue = trim(nextPropertyValue);

					switch (nextPropertyType) {
						case STRING:
							concept.addPropertyString(nextPropertyName, nextPropertyValue);
							break;
						case CODING:
							PartTypeAndPartName key = new PartTypeAndPartName(nextPropertyName, nextPropertyValue);
							String partNumber = myPartTypeAndPartNameToPartNumber.get(key);

							if (partNumber == null && nextPropertyName.equals("TIME_ASPCT")) {
								key = new PartTypeAndPartName("TIME", nextPropertyValue);
								partNumber = myPartTypeAndPartNameToPartNumber.get(key);
							}
							if (partNumber == null && nextPropertyName.equals("METHOD_TYP")) {
								key = new PartTypeAndPartName("METHOD", nextPropertyValue);
								partNumber = myPartTypeAndPartNameToPartNumber.get(key);
							}
							if (partNumber == null && nextPropertyName.equals("SCALE_TYP")) {
								key = new PartTypeAndPartName("SCALE", nextPropertyValue);
								partNumber = myPartTypeAndPartNameToPartNumber.get(key);
							}

							if (partNumber == null && nextPropertyName.equals("SYSTEM") && nextPropertyValue.startsWith("^")) {
								continue;
							}

//							Validate.notBlank(partNumber, "Unknown part: " + key);
							if (isNotBlank(partNumber)) {
								concept.addPropertyCoding(nextPropertyName, IHapiTerminologyLoaderSvc.LOINC_URI, partNumber, nextPropertyValue);
							} else {
								ourLog.warn("Unable to find part code with TYPE[{}] and NAME[{}]", key.getPartType(), key.getPartName());
							}
							break;
						case CODE:
						case INTEGER:
						case BOOLEAN:
						case DATETIME:
						case NULL:
							throw new InternalErrorException("Don't know how to handle LOINC property of type: " + nextPropertyType);
					}

				}
			}

			Validate.isTrue(!myCode2Concept.containsKey(code), "The code %s has appeared more than once", code);
			myCode2Concept.put(code, concept);
		}
	}
private static final Logger ourLog = LoggerFactory.getLogger(LoincHandler.class);
}
