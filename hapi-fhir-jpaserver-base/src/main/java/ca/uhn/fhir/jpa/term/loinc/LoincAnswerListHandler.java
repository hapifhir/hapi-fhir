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
import ca.uhn.fhir.jpa.term.IHapiTerminologyLoaderSvc;
import ca.uhn.fhir.jpa.term.IRecordHandler;
import org.apache.commons.csv.CSVRecord;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.ValueSet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.trim;

public class LoincAnswerListHandler implements IRecordHandler {

	private final Map<String, TermConcept> myCode2Concept;
	private final TermCodeSystemVersion myCodeSystemVersion;
	private final Set<String> myPropertyNames;
	private final List<ValueSet> myValueSets;
	private final Map<String, ValueSet> myIdToValueSet = new HashMap<>();

	public LoincAnswerListHandler(TermCodeSystemVersion theCodeSystemVersion, Map<String, TermConcept> theCode2concept, Set<String> thePropertyNames, List<ValueSet> theValueSets) {
		myCodeSystemVersion = theCodeSystemVersion;
		myCode2Concept = theCode2concept;
		myPropertyNames = thePropertyNames;
		myValueSets = theValueSets;
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

		// Answer code
		if (!myCode2Concept.containsKey(answerString)) {
			TermConcept concept = new TermConcept(myCodeSystemVersion, answerString);
			concept.setDisplay(displayText);
			if (isNotBlank(sequenceNumber) && sequenceNumber.matches("^[0-9]$")) {
				concept.setSequence(Integer.parseInt(sequenceNumber));
			}
			myCode2Concept.put(answerString, concept);
		}

		// Answer list ValueSet
		ValueSet vs;
		if (!myIdToValueSet.containsKey(answerListId)) {
			vs = new ValueSet();
			vs.setUrl("urn:oid:" + answerListOid);
			vs.addIdentifier()
				.setSystem(IHapiTerminologyLoaderSvc.LOINC_URI)
				.setValue(answerListId);
			vs.setId(answerListId);
			vs.setName(answerListName);
			vs.setStatus(Enumerations.PublicationStatus.ACTIVE);
			myIdToValueSet.put(answerListId, vs);
			myValueSets.add(vs);
		} else {
			vs = myIdToValueSet.get(answerListId);
		}
		vs
			.getCompose()
			.getIncludeFirstRep()
			.setSystem(IHapiTerminologyLoaderSvc.LOINC_URI)
			.addConcept()
			.setCode(answerString)
			.setDisplay(displayText);
	}

}
