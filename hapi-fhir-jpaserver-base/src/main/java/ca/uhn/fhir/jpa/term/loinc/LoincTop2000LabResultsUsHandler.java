package ca.uhn.fhir.jpa.term.loinc;

import ca.uhn.fhir.jpa.entity.TermConcept;
import org.hl7.fhir.r4.model.ValueSet;

import java.util.List;
import java.util.Map;

public class LoincTop2000LabResultsUsHandler extends BaseLoincTop2000LabResultsHandler {

	public static final String TOP_2000_US_VS_ID = "TOP_2000_LABRESULTS_US";
	public static final String TOP_2000_US_VS_URI = "http://loinc.org/top-2000-lab-results-us";
	public static final String TOP_2000_US_VS_NAME = "Top 2000 Lab Results US";

	public LoincTop2000LabResultsUsHandler(Map<String, TermConcept> theCode2concept, List<ValueSet> theValueSets) {
		super(theCode2concept, theValueSets, TOP_2000_US_VS_ID, TOP_2000_US_VS_URI, TOP_2000_US_VS_NAME);
	}


}
