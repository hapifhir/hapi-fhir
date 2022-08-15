package ca.uhn.fhir.jpa.term.loinc;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.trim;
import static org.apache.commons.lang3.StringUtils.trimToEmpty;

import java.util.Map;

import org.apache.commons.csv.CSVRecord;

import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.term.IZipContentsHandlerCsv;
import ca.uhn.fhir.jpa.term.api.ITermLoaderSvc;

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
	
		// The following should be created as designations for each term:
        // COMPONENT:PROPERTY:TIME_ASPCT:SYSTEM:SCALE_TYP:METHOD_TYP (as colon-separated concatenation - FormalName)
        // SHORTNAME
        // LONG_COMMON_NAME
        // LinguisticVariantDisplayName
			
		//-- add formalName designation
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
		
		//-- other designations
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
