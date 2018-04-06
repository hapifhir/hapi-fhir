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
import ca.uhn.fhir.jpa.term.IRecordHandler;
import ca.uhn.fhir.jpa.term.TerminologyLoaderSvcImpl;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.Validate;

import java.util.Map;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.trim;

public class LoincHandler implements IRecordHandler {

	private final Map<String, TermConcept> myCode2Concept;
	private final TermCodeSystemVersion myCodeSystemVersion;
	private final Set<String> myPropertyNames;

	public LoincHandler(TermCodeSystemVersion theCodeSystemVersion, Map<String, TermConcept> theCode2concept, Set<String> thePropertyNames) {
		myCodeSystemVersion = theCodeSystemVersion;
		myCode2Concept = theCode2concept;
		myPropertyNames = thePropertyNames;
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

			for (String nextPropertyName : myPropertyNames) {
				if (!theRecord.toMap().containsKey(nextPropertyName)) {
					continue;
				}
				String nextPropertyValue = theRecord.get(nextPropertyName);
				if (isNotBlank(nextPropertyValue)) {
					concept.addPropertyString(nextPropertyName, nextPropertyValue);
				}
			}

			Validate.isTrue(!myCode2Concept.containsKey(code), "The code %s has appeared more than once", code);
			myCode2Concept.put(code, concept);
		}
	}

}
