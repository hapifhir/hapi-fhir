package ca.uhn.fhir.jpa.term.loinc;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.entity.TermConceptProperty;
import ca.uhn.fhir.jpa.term.IZipContentsHandlerCsv;
import ca.uhn.fhir.jpa.term.api.ITermLoaderSvc;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.apache.commons.csv.CSVRecord;
import org.hl7.fhir.r4.model.CodeSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.trim;

public class LoincPartLinkHandler implements IZipContentsHandlerCsv {

	private static final Logger ourLog = LoggerFactory.getLogger(LoincPartLinkHandler.class);
	private final Map<String, TermConcept> myCode2Concept;
	private final TermCodeSystemVersion myCodeSystemVersion;
	private final Map<String, CodeSystem.PropertyType> myPropertyNames;
	private Long myPartCount;

	public LoincPartLinkHandler(TermCodeSystemVersion theCodeSystemVersion, Map<String, TermConcept> theCode2concept, Map<String, CodeSystem.PropertyType> thePropertyNames) {
		myCodeSystemVersion = theCodeSystemVersion;
		myCode2Concept = theCode2concept;
		myPropertyNames = thePropertyNames;
	}

	@Override
	public void accept(CSVRecord theRecord) {

		String loincNumber = trim(theRecord.get("LoincNumber"));
		String property = trim(theRecord.get("Property"));
		String partName = trim(theRecord.get("PartName"));
		String partNumber = trim(theRecord.get("PartNumber"));

		/*
		 * Property has the form http://loinc.org/property/COMPONENT
		 * but we want just the COMPONENT part
		 */
		int lastSlashIdx = property.lastIndexOf("/");
		String propertyPart = property.substring(lastSlashIdx + 1);

		TermConcept loincConcept = myCode2Concept.get(loincNumber);
		if (loincConcept == null) {
			throw new InternalErrorException(Msg.code(913) + "Unknown loinc code: " + loincNumber);
		}

		CodeSystem.PropertyType propertyType = myPropertyNames.get(propertyPart);
		if (propertyType == null) {
			return;
		}

		String expectedValue;
		if (propertyType == CodeSystem.PropertyType.STRING) {
			expectedValue = partName;
		} else if (propertyType == CodeSystem.PropertyType.CODING) {
			expectedValue = partNumber;
		} else {
			throw new InternalErrorException(Msg.code(914) + "Don't know how to handle property of type: " + propertyType);
		}

		Optional<TermConceptProperty> existingProprty = loincConcept
			.getProperties()
			.stream()
			.filter(t -> t.getKey().equals(propertyPart))
			.filter(t -> t.getValue().equals(expectedValue))
			.findFirst();
		if (existingProprty.isPresent()) {
			return;
		}

		ourLog.debug("Adding new property {} = {}", propertyPart, partNumber);
		if (propertyType == CodeSystem.PropertyType.STRING) {
			loincConcept.addPropertyString(propertyPart, partName);
		} else {
			loincConcept.addPropertyCoding(propertyPart, ITermLoaderSvc.LOINC_URI, partNumber, partName);
		}

	}
}
