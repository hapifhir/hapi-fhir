package ca.uhn.fhir.jpa.term.loinc;

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

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.trim;

public class LoincAnswerListLinkHandler implements IRecordHandler {

	private final Map<String, TermConcept> myCode2Concept;
	private final Map<String, ValueSet> myIdToValueSet = new HashMap<>();

	public LoincAnswerListLinkHandler(Map<String, TermConcept> theCode2concept, List<ValueSet> theValueSets) {
		myCode2Concept = theCode2concept;
		for (ValueSet next : theValueSets) {
			myIdToValueSet.put(next.getId(), next);
		}
	}

	@Override
	public void accept(CSVRecord theRecord) {
		String applicableContext = trim(theRecord.get("ApplicableContext"));

		/*
		 * Per Dan V's Notes:
		 *
		 * Note: in our current format, we support binding of the same
		 * LOINC term to different answer lists depending on the panel
		 * context. I don’t believe there’s a way to handle that in
		 * the current FHIR spec, so I might suggest we discuss either
		 * only binding the “default” (non-context specific) list or
		 * if multiple bindings could be supported.
		 */
		if (isNotBlank(applicableContext)) {
			return;
		}

		String answerListId = trim(theRecord.get("AnswerListId"));
		if (isBlank(answerListId)) {
			return;
		}

		String loincNumber = trim(theRecord.get("LoincNumber"));
		if (isBlank(loincNumber)) {
			return;
		}

		TermConcept loincCode = myCode2Concept.get(loincNumber);
		if (loincCode != null) {
			loincCode.addProperty("answer-list", answerListId);
		}

		TermConcept answerListCode = myCode2Concept.get(answerListId);
		if (answerListCode != null) {
			answerListCode.addProperty("answers-for", loincNumber);
		}

	}

}
