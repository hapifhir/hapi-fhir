package ca.uhn.fhir.jpa.term.loinc;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.trim;

import java.util.Map;

import org.apache.commons.csv.CSVRecord;

import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.entity.TermConceptDesignation;
import ca.uhn.fhir.jpa.term.IZipContentsHandlerCsv;
import ca.uhn.fhir.jpa.term.api.ITermLoaderSvc;
import ca.uhn.fhir.util.StringUtil;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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

public class LoincLinguisticVariantHandler implements IZipContentsHandlerCsv {

	private final Map<String, TermConcept> myCode2Concept;
	private final String myLanguageCode;

	public LoincLinguisticVariantHandler(Map<String, TermConcept> theCode2Concept, String theLanguageCode) {
		myCode2Concept = theCode2Concept;
		myLanguageCode = theLanguageCode;
	}

	@Override
	public void accept(CSVRecord theRecord) {
		
		String loincNumber = trim(theRecord.get("LOINC_NUM"));
		if (isBlank(loincNumber)) {
			return;
		}

		TermConcept concept = myCode2Concept.get(loincNumber);
		if (concept == null) {
			return;
		}
	
		addDesignation(theRecord, concept, "COMPONENT");
		addDesignation(theRecord, concept, "PROPERTY");
		addDesignation(theRecord, concept, "TIME_ASPCT");
		addDesignation(theRecord, concept, "SYSTEM");
		addDesignation(theRecord, concept, "SCALE_TYP");
		
		addDesignation(theRecord, concept, "METHOD_TYP");
		addDesignation(theRecord, concept, "CLASS");
		addDesignation(theRecord, concept, "SHORTNAME");
		addDesignation(theRecord, concept, "LONG_COMMON_NAME");
		addDesignation(theRecord, concept, "RELATEDNAMES2");
		
		addDesignation(theRecord, concept, "LinguisticVariantDisplayName");
		
	}

	private void addDesignation(CSVRecord theRecord, TermConcept concept, String fieldName) {
		
		String field = trim(theRecord.get(fieldName));
		if (isBlank(field)) {
			return;
		}
		
		// this is for loinc only, some fields are exceed the max length for the designation
		// truncate the field after the last semicolon before max length
		field = StringUtil.truncToMax(field, ";", TermConceptDesignation.MAX_VAL_LENGTH);
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
