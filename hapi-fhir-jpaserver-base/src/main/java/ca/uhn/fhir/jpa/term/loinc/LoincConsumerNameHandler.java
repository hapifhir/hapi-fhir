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

import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.term.IZipContentsHandlerCsv;
import org.apache.commons.csv.CSVRecord;

import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.trim;

public class LoincConsumerNameHandler implements IZipContentsHandlerCsv {

	private final Map<String, TermConcept> myCode2Concept;

	public LoincConsumerNameHandler(Map<String, TermConcept> theCode2concept) {
		myCode2Concept = theCode2concept;
	}

	@Override
	public void accept(CSVRecord theRecord) {
		
		String loincNumber = trim(theRecord.get("LoincNumber"));
		if (isBlank(loincNumber)) {
			return;
		}

		String consumerName = trim(theRecord.get("ConsumerName"));
		if (isBlank(consumerName)) {
			return;
		}
		
		TermConcept loincCode = myCode2Concept.get(loincNumber);
		if (loincCode == null) {
			return;
		}
			
		loincCode.addDesignation()
		    .setUseDisplay("ConsumerName")
		    .setValue(consumerName);
	}

}
