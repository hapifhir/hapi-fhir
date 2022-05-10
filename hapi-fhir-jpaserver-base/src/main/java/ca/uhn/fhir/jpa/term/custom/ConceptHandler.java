package ca.uhn.fhir.jpa.term.custom;

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

import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.term.IZipContentsHandlerCsv;
import ca.uhn.fhir.jpa.term.TermLoaderSvcImpl;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.trim;

public class ConceptHandler implements IZipContentsHandlerCsv {

	private static final Logger ourLog = LoggerFactory.getLogger(ConceptHandler.class);
	public static final String CODE = "CODE";
	public static final String DISPLAY = "DISPLAY";
	private final Map<String, TermConcept> myCode2Concept;

	public ConceptHandler(Map<String, TermConcept> theCode2concept) {
		myCode2Concept = theCode2concept;
	}

	@Override
	public void accept(CSVRecord theRecord) {
		String code = trim(theRecord.get(CODE));
		if (isNotBlank(code)) {
			String display = trim(theRecord.get(DISPLAY));

			Validate.isTrue(!myCode2Concept.containsKey(code), "The code %s has appeared more than once", code);

			TermConcept concept = TermLoaderSvcImpl.getOrCreateConcept(myCode2Concept, code);
			concept.setCode(code);
			concept.setDisplay(display);

			myCode2Concept.put(code, concept);
		}
	}
}
