package ca.uhn.fhir.jpa.term.loinc;

import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.term.IHapiTerminologyLoaderSvc;
import ca.uhn.fhir.jpa.term.IRecordHandler;
import org.apache.commons.csv.CSVRecord;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.ValueSet;

import java.util.*;

import static org.apache.commons.lang3.StringUtils.trim;

public class LoincUniversalOrderSetHandler extends BaseHandler implements IRecordHandler {

	public static final String VS_ID = "loinc-universal-order-set-vs";
	public static final String VS_URI = "http://loinc.org/fhir/loinc-universal-order-set";
	public static final String VS_NAME = "LOINC Universal Order Set";

	public LoincUniversalOrderSetHandler(Map<String, TermConcept> theCode2concept, List<ValueSet> theValueSets, List<ConceptMap> theConceptMaps) {
		super(theCode2concept, theValueSets, theConceptMaps);
	}

	@Override
	public void accept(CSVRecord theRecord) {
		String loincNumber = trim(theRecord.get("LOINC_NUM"));
		String displayName = trim(theRecord.get("LONG_COMMON_NAME"));
		String orderObs = trim(theRecord.get("ORDER_OBS"));

		ValueSet valueSet = getValueSet(VS_ID, VS_URI, VS_NAME);
		addCodeAsIncludeToValueSet(valueSet, IHapiTerminologyLoaderSvc.LOINC_URI, loincNumber, displayName);
	}


}
