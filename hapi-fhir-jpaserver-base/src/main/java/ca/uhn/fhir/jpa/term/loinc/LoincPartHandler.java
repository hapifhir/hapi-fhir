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

import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.term.IZipContentsHandlerCsv;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.Validate;

import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.trim;

public class LoincPartHandler implements IZipContentsHandlerCsv {

	private final Map<String, TermConcept> myCode2Concept;
	private final TermCodeSystemVersion myCodeSystemVersion;
	private final Map<PartTypeAndPartName, String> myPartTypeAndPartNameToPartNumber = new HashMap<>();

	public LoincPartHandler(TermCodeSystemVersion theCodeSystemVersion, Map<String, TermConcept> theCode2concept) {
		myCodeSystemVersion = theCodeSystemVersion;
		myCode2Concept = theCode2concept;
	}

	@Override
	public void accept(CSVRecord theRecord) {

		// this is the code for the list (will repeat)
		String partNumber = trim(theRecord.get("PartNumber"));
		String partTypeName = trim(theRecord.get("PartTypeName"));
		String partName = trim(theRecord.get("PartName"));
		String partDisplayName = trim(theRecord.get("PartDisplayName"));

		// Per Dan's note, we include deprecated parts
//		String status = trim(theRecord.get("Status"));
//		if (!"ACTIVE".equals(status)) {
//			return;
//		}

		PartTypeAndPartName partTypeAndPartName = new PartTypeAndPartName(partTypeName, partName);
		String previousValue = myPartTypeAndPartNameToPartNumber.put(partTypeAndPartName, partNumber);
		Validate.isTrue(previousValue == null, "Already had part: " + partTypeAndPartName);

		TermConcept concept = myCode2Concept.get(partNumber);
		if (concept == null) {
			concept = new TermConcept(myCodeSystemVersion, partNumber);
			concept.setDisplay(partName);
			myCode2Concept.put(partNumber, concept);
		}

		if (isNotBlank(partDisplayName)) {
			concept.addDesignation()
				.setConcept(concept)
				.setUseDisplay("PartDisplayName")
				.setValue(partDisplayName);
		}

	}

	public Map<PartTypeAndPartName, String> getPartTypeAndPartNameToPartNumber() {
		return myPartTypeAndPartNameToPartNumber;
	}


}
