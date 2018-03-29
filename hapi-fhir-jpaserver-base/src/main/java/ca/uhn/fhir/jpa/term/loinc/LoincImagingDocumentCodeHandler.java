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

public class LoincImagingDocumentCodeHandler extends BaseHandler implements IRecordHandler {

	public static final String VS_ID = "loinc-imaging-document-codes";
	public static final String VS_URI = "http://loinc.org/fhir/loinc-imaging-document-codes";
	public static final String VS_NAME = "LOINC Imaging Document Codes";

	public LoincImagingDocumentCodeHandler(Map<String, TermConcept> theCode2concept, List<ValueSet> theValueSets, List<ConceptMap> theConceptMaps) {
		super(theCode2concept, theValueSets, theConceptMaps);
	}

	@Override
	public void accept(CSVRecord theRecord) {
		String loincNumber = trim(theRecord.get("LOINC_NUM"));
		String displayName = trim(theRecord.get("LONG_COMMON_NAME"));

		ValueSet valueSet = getValueSet(VS_ID, VS_URI, VS_NAME);
		addCodeAsIncludeToValueSet(valueSet, IHapiTerminologyLoaderSvc.LOINC_URI, loincNumber, displayName);
	}


}
