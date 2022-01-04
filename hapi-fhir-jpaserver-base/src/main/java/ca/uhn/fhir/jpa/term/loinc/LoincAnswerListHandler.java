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
import ca.uhn.fhir.jpa.term.api.ITermLoaderSvc;
import org.apache.commons.csv.CSVRecord;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.ValueSet;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import static ca.uhn.fhir.jpa.term.loinc.LoincUploadPropertiesEnum.*;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.trim;

public class LoincAnswerListHandler extends BaseLoincHandler {

	private final Map<String, TermConcept> myCode2Concept;
	private final TermCodeSystemVersion myCodeSystemVersion;

	public LoincAnswerListHandler(TermCodeSystemVersion theCodeSystemVersion, Map<String, TermConcept> theCode2concept,
			List<ValueSet> theValueSets, List<ConceptMap> theConceptMaps, Properties theUploadProperties, String theCopyrightStatement) {
		super(theCode2concept, theValueSets, theConceptMaps, theUploadProperties, theCopyrightStatement);
		myCodeSystemVersion = theCodeSystemVersion;
		myCode2Concept = theCode2concept;
	}

	@Override
	public void accept(CSVRecord theRecord) {

		// this is the code for the list (will repeat)
		String answerListId = trim(theRecord.get("AnswerListId"));
		String answerListName = trim(theRecord.get("AnswerListName"));
		String answerListOid = trim(theRecord.get("AnswerListOID"));
		String externallyDefined = trim(theRecord.get("ExtDefinedYN"));
		String extenrallyDefinedCs = trim(theRecord.get("ExtDefinedAnswerListCodeSystem"));
		String externallyDefinedLink = trim(theRecord.get("ExtDefinedAnswerListLink"));
		// this is the code for the actual answer (will not repeat)
		String answerString = trim(theRecord.get("AnswerStringId"));
		String sequenceNumber = trim(theRecord.get("SequenceNumber"));
		String displayText = trim(theRecord.get("DisplayText"));
		String extCodeId = trim(theRecord.get("ExtCodeId"));
		String extCodeDisplayName = trim(theRecord.get("ExtCodeDisplayName"));
		String extCodeSystem = trim(theRecord.get("ExtCodeSystem"));
		String extCodeSystemVersion = trim(theRecord.get("ExtCodeSystemVersion"));


		// Answer list code
		if (!myCode2Concept.containsKey(answerListId)) {
			TermConcept concept = new TermConcept(myCodeSystemVersion, answerListId);
			concept.setDisplay(answerListName);
			myCode2Concept.put(answerListId, concept);
		}

		// Answer list ValueSet
		String valueSetId;
		String codeSystemVersionId = myUploadProperties.getProperty(LOINC_CODESYSTEM_VERSION.getCode());
		if (codeSystemVersionId != null) {
			valueSetId = answerListId + "-" + codeSystemVersionId;
		} else {
			valueSetId = answerListId;
		}
		ValueSet vs = getValueSet(valueSetId, "http://loinc.org/vs/" + answerListId, answerListName, LOINC_ANSWERLIST_VERSION.getCode());
		if (vs.getIdentifier().isEmpty()) {
			vs.addIdentifier()
				.setSystem("urn:ietf:rfc:3986")
				.setValue("urn:oid:" + answerListOid);
		}

		if (isNotBlank(answerString)) {

			// Answer code
			if (!myCode2Concept.containsKey(answerString)) {
				TermConcept concept = new TermConcept(myCodeSystemVersion, answerString);
				concept.setDisplay(displayText);
				if (isNotBlank(sequenceNumber) && sequenceNumber.matches("^[0-9]$")) {
					concept.setSequence(Integer.parseInt(sequenceNumber));
				}
				myCode2Concept.put(answerString, concept);
			}

			vs
				.getCompose()
				.getIncludeFirstRep()
				.setSystem(ITermLoaderSvc.LOINC_URI)
				.setVersion(codeSystemVersionId)
				.addConcept()
				.setCode(answerString)
				.setDisplay(displayText);

		}

	}

}
