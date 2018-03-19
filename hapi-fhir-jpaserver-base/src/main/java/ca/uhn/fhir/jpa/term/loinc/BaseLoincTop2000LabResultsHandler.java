package ca.uhn.fhir.jpa.term.loinc;

import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.term.IHapiTerminologyLoaderSvc;
import ca.uhn.fhir.jpa.term.IRecordHandler;
import org.apache.commons.csv.CSVRecord;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.ValueSet;

import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.trim;

public class BaseLoincTop2000LabResultsHandler extends BaseHandler implements IRecordHandler {

	private String myValueSetId;
	private String myValueSetUri;
	private String myValueSetName;

	public BaseLoincTop2000LabResultsHandler(Map<String, TermConcept> theCode2concept, List<ValueSet> theValueSets, String theValueSetId, String theValueSetUri, String theValueSetName, List<ConceptMap> theConceptMaps) {
		super(theCode2concept, theValueSets, theConceptMaps);
		myValueSetId = theValueSetId;
		myValueSetUri = theValueSetUri;
		myValueSetName = theValueSetName;
	}

	@Override
	public void accept(CSVRecord theRecord) {
		String loincNumber = trim(theRecord.get("LOINC #"));
		String displayName = trim(theRecord.get("Long Common Name"));

		ValueSet valueSet = getValueSet(myValueSetId, myValueSetUri, myValueSetName);
		addCodeAsIncludeToValueSet(valueSet, IHapiTerminologyLoaderSvc.LOINC_URI, loincNumber, displayName);
	}

}
