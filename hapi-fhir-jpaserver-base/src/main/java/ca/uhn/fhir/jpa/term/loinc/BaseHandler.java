package ca.uhn.fhir.jpa.term.loinc;

import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.term.IRecordHandler;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.ValueSet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isBlank;

abstract class BaseHandler implements IRecordHandler {

	private final List<ValueSet> myValueSets;
	private final Map<String, ValueSet> myIdToValueSet = new HashMap<>();
	private final Map<String, TermConcept> myCode2Concept;

	BaseHandler(Map<String, TermConcept> theCode2Concept, List<ValueSet> theValueSets) {
		myValueSets = theValueSets;
		myCode2Concept = theCode2Concept;
	}

	void addCodeAsIncludeToValueSet(ValueSet theVs, String theCodeSystemUrl, String theCode, String theDisplayName) {
		ValueSet.ConceptSetComponent include = null;
		for (ValueSet.ConceptSetComponent next : theVs.getCompose().getInclude()) {
			if (next.getSystem().equals(theCodeSystemUrl)) {
				include = next;
				break;
			}
		}
		if (include == null) {
			include = theVs.getCompose().addInclude();
			include.setSystem(theCodeSystemUrl);
		}

		boolean found = false;
		for (ValueSet.ConceptReferenceComponent next : include.getConcept()) {
			if (next.getCode().equals(theCode)) {
				found = true;
			}
		}
		if (!found) {

			String displayName = theDisplayName;
			if (isBlank(displayName)) {
				for (TermConcept next : myCode2Concept.values()) {
					if (next.getCode().equals(theCode)) {
						displayName = next.getDisplay();
					}
				}
			}

			include
				.addConcept()
				.setCode(theCode)
				.setDisplay(displayName);

		}
	}

	ValueSet getValueSet(String theValueSetId, String theValueSetUri, String theValueSetName) {
		ValueSet vs;
		if (!myIdToValueSet.containsKey(theValueSetId)) {
			vs = new ValueSet();
			vs.setUrl(theValueSetUri);
			vs.setId(theValueSetId);
			vs.setName(theValueSetName);
			vs.setStatus(Enumerations.PublicationStatus.ACTIVE);
			myIdToValueSet.put(theValueSetId, vs);
			myValueSets.add(vs);
		} else {
			vs = myIdToValueSet.get(theValueSetId);
		}
		return vs;
	}


}
