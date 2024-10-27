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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.trim;

/**
 * Handles addition of MAP_TO properties to TermConcepts
 */
public class LoincMapToHandler implements IZipContentsHandlerCsv {
	private static final Logger ourLog = LoggerFactory.getLogger(LoincMapToHandler.class);

	public static final String CONCEPT_CODE_PROP_NAME = "LOINC";
	public static final String MAP_TO_PROP_NAME = "MAP_TO";
	public static final String DISPLAY_PROP_NAME = "COMMENT";

	private final Map<String, TermConcept> myCode2Concept;

	public LoincMapToHandler(Map<String, TermConcept> theCode2concept) {
		myCode2Concept = theCode2concept;
	}

	@Override
	public void accept(CSVRecord theRecord) {
		String code = trim(theRecord.get(CONCEPT_CODE_PROP_NAME));
		String mapTo = trim(theRecord.get(MAP_TO_PROP_NAME));
		String display = trim(theRecord.get(DISPLAY_PROP_NAME));

		if (isBlank(code)) {
			ourLog.warn("MapTo record was found with a blank '" + CONCEPT_CODE_PROP_NAME + "' property");
			return;
		}

		if (isBlank(mapTo)) {
			ourLog.warn("MapTo record was found with a blank '" + MAP_TO_PROP_NAME + "' property");
			return;
		}

		TermConcept concept = myCode2Concept.get(code);
		if (concept == null) {
			ourLog.warn("A TermConcept was not found for MapTo '" + CONCEPT_CODE_PROP_NAME + "' property: '" + code
					+ "' MapTo record ignored.");
			return;
		}

		concept.addPropertyCoding(MAP_TO_PROP_NAME, ITermLoaderSvc.LOINC_URI, mapTo, display);
		ourLog.trace(
				"Adding " + MAP_TO_PROP_NAME + " coding property: {} to concept.code {}", mapTo, concept.getCode());
	}
}
