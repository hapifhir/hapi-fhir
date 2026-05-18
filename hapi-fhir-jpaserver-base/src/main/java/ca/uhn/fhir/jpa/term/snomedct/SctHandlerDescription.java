/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.term.snomedct;

import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.term.IZipContentsHandlerCsv;
import org.apache.commons.csv.CSVRecord;

import java.util.Map;
import java.util.Set;

public final class SctHandlerDescription implements IZipContentsHandlerCsv {
	// https://docs.snomed.org/snomed-international-documents/snomed-ct-glossary/f/fully-specified-name
	public static final String SNOMED_CONCEPT_FULLY_SPECIFIED_NAME = "900000000000003001";
	// https://docs.snomed.org/snomed-international-documents/snomed-ct-glossary/s/synonym
	public static final String SNOMED_CONCEPT_SYNONYM = "900000000000013009";

	private final Map<String, TermConcept> myCode2concept;
	private final TermCodeSystemVersion myCodeSystemVersion;
	private final Set<String> myValidConceptIds;

	public SctHandlerDescription(
			Set<String> theValidConceptIds,
			Map<String, TermConcept> theCode2concept,
			TermCodeSystemVersion theCodeSystemVersion) {
		myCode2concept = theCode2concept;
		myCodeSystemVersion = theCodeSystemVersion;
		myValidConceptIds = theValidConceptIds;
	}

	@Override
	public void accept(CSVRecord theRecord) {
		boolean active = "1".equals(theRecord.get("active"));
		if (!active) {
			return;
		}
		String conceptId = theRecord.get("conceptId");
		if (!myValidConceptIds.contains(conceptId)) {
			return;
		}

		String term = theRecord.get("term");
		String typeId = theRecord.get("typeId");
		String languageCode = theRecord.get("languageCode");
		String moduleId = theRecord.get("moduleId");
		boolean isSynonym = SNOMED_CONCEPT_SYNONYM.equals(typeId);

		TermConcept concept = myCode2concept.computeIfAbsent(
				conceptId, (id) -> new TermConcept().setCode(id).setCodeSystemVersion(myCodeSystemVersion));

		// To be fully correct, this case would need to consider the English refset
		// and look up the ID of the current term to check
		// if the acceptabilityId is set to 900000000000548007 (preferred).
		if (isSynonym && "en".equals(languageCode)) {
			concept.setDisplay(term);
			concept.addPropertyCode("module", moduleId);
		}
		if ((isSynonym || SNOMED_CONCEPT_FULLY_SPECIFIED_NAME.equals(typeId))
				&& concept.getDesignations().stream()
						.noneMatch(d -> d.getLanguage().equals(languageCode)
								&& d.getValue().equals(term)
								&& d.getUseCode().equals(typeId))) {
			concept.addDesignation()
					.setValue(term)
					.setLanguage(languageCode)
					.setUseSystem("http://snomed.info/sct")
					.setUseCode(typeId)
					.setUseDisplay(isSynonym ? "Synonym" : "Fully specified name");
		}
	}
}
