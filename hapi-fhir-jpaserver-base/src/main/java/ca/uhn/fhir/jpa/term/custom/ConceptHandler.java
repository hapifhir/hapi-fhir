package ca.uhn.fhir.jpa.term.custom;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.trim;

public class ConceptHandler implements IRecordHandler {

	private static final Logger ourLog = LoggerFactory.getLogger(ConceptHandler.class);
	private final Map<String, TermConcept> myCode2Concept;
	private final TermCodeSystemVersion myCodeSystemVersion;

	public ConceptHandler(Map<String, TermConcept> theCode2concept, TermCodeSystemVersion theCodeSystemVersion) {
		myCode2Concept = theCode2concept;
		myCodeSystemVersion = theCodeSystemVersion;
	}

	@Override
	public void accept(CSVRecord theRecord) {
		String code = trim(theRecord.get("CODE"));
		if (isNotBlank(code)) {
			String display = trim(theRecord.get("DISPLAY"));

			Validate.isTrue(!myCode2Concept.containsKey(code), "The code %s has appeared more than once", code);

			TermConcept concept = TerminologyLoaderSvcImpl.getOrCreateConcept(myCodeSystemVersion, myCode2Concept, code);
			concept.setCode(code);
			concept.setDisplay(display);

			myCode2Concept.put(code, concept);
		}
	}
}
