package ca.uhn.fhir.jpa.term.loinc;

import ca.uhn.fhir.jpa.entity.TermConcept;
import org.hl7.fhir.r4.model.ValueSet;

import java.util.List;
import java.util.Map;

public class LoincTop2000LabResultsSiHandler extends BaseLoincTop2000LabResultsHandler {

	public static final String TOP_2000_SI_VS_ID = "TOP-2000-LABRESULTS-SI";
	public static final String TOP_2000_SI_VS_URI = "http://loinc.org/top-2000-lab-results-si";
	public static final String TOP_2000_SI_VS_NAME = "Top 2000 Lab Results SI";

	public LoincTop2000LabResultsSiHandler(Map<String, TermConcept> theCode2concept, List<ValueSet> theValueSets) {
		super(theCode2concept, theValueSets, TOP_2000_SI_VS_ID, TOP_2000_SI_VS_URI, TOP_2000_SI_VS_NAME);
	}


}
