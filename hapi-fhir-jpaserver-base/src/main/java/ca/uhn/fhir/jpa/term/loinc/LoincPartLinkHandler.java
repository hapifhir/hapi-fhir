package ca.uhn.fhir.jpa.term.loinc;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static org.apache.commons.lang3.StringUtils.trim;

public class LoincPartLinkHandler implements IRecordHandler {

	private static final Logger ourLog = LoggerFactory.getLogger(LoincPartLinkHandler.class);
	private final Map<String, TermConcept> myCode2Concept;
	private final TermCodeSystemVersion myCodeSystemVersion;
	private Long myPartCount;

	public LoincPartLinkHandler(TermCodeSystemVersion theCodeSystemVersion, Map<String, TermConcept> theCode2concept) {
		myCodeSystemVersion = theCodeSystemVersion;
		myCode2Concept = theCode2concept;
	}

	@Override
	public void accept(CSVRecord theRecord) {

		String loincNumber = trim(theRecord.get("LoincNumber"));
		String longCommonName = trim(theRecord.get("LongCommonName"));
		String partNumber = trim(theRecord.get("PartNumber"));

		TermConcept loincConcept = myCode2Concept.get(loincNumber);
		TermConcept partConcept = myCode2Concept.get(partNumber);

		if (loincConcept == null) {
			ourLog.warn("No loinc code: {}", loincNumber);
			return;
		}
		if (partConcept == null) {
			if (myPartCount == null) {
				myPartCount = myCode2Concept
					.keySet()
					.stream()
					.filter(t->t.startsWith("LP"))
					.count();
			}
			ourLog.debug("No part code: {} - Have {} part codes", partNumber, myPartCount);
			return;
		}

		// For now we're ignoring these

	}
}
