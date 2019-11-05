package org.hl7.fhir.instance.hapi.validation;

import org.hl7.fhir.dstu2.model.Questionnaire;
import org.hl7.fhir.dstu2.model.ValueSet;

import java.util.HashMap;
import java.util.Map;

public class WorkerContext {
	private HashMap<String, ValueSet> myValueSets = new HashMap<>();
	private HashMap<String, Questionnaire> myQuestionnaires = new HashMap<>();

	public Map<String, ValueSet> getValueSets() {
		return myValueSets;
	}

	public Map<String, Questionnaire> getQuestionnaires() {
		return myQuestionnaires;
	}
}
