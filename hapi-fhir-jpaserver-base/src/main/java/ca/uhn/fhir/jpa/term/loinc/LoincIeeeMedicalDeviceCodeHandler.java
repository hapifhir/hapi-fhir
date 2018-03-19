package ca.uhn.fhir.jpa.term.loinc;

import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.term.IHapiTerminologyLoaderSvc;
import ca.uhn.fhir.jpa.term.IRecordHandler;
import org.apache.commons.csv.CSVRecord;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.ValueSet;

import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.trim;

public class LoincIeeeMedicalDeviceCodeHandler extends BaseHandler implements IRecordHandler {

	public static final String LOINC_IEEE_CM_ID = "LOINC-IEEE-MEDICAL-DEVICE-CM";
	public static final String LOINC_IEEE_CM_URI = "http://loinc.org/fhir/loinc-ieee-device-code-mappings";
	public static final String LOINC_IEEE_CM_NAME = "LOINC/IEEE Device Code Mappings";

	/**
	 * Constructor
	 */
	public LoincIeeeMedicalDeviceCodeHandler(Map<String, TermConcept> theCode2concept, List<ValueSet> theValueSets, List<ConceptMap> theConceptMaps) {
		super(theCode2concept, theValueSets, theConceptMaps);
	}

	@Override
	public void accept(CSVRecord theRecord) {

		String loincNumber = trim(theRecord.get("LOINC_NUM"));
		String longCommonName = trim(theRecord.get("LOINC_LONG_COMMON_NAME"));
		String ieeeCode = trim(theRecord.get("IEEE_CF_CODE10"));
		String ieeeDisplayName = trim(theRecord.get("IEEE_REFID"));

		// LOINC Part -> IEEE 11073:10101 Mappings
		String sourceCodeSystemUri = IHapiTerminologyLoaderSvc.LOINC_URI;
		String targetCodeSystemUri = IHapiTerminologyLoaderSvc.IEEE_11073_10101_URI;
		addConceptMapEntry(
			new ConceptMapping()
				.setConceptMapId(LOINC_IEEE_CM_ID)
				.setConceptMapUri(LOINC_IEEE_CM_URI)
				.setConceptMapName(LOINC_IEEE_CM_NAME)
				.setSourceCodeSystem(sourceCodeSystemUri)
				.setSourceCode(loincNumber)
				.setSourceDisplay(longCommonName)
				.setTargetCodeSystem(targetCodeSystemUri)
				.setTargetCode(ieeeCode)
				.setTargetDisplay(ieeeDisplayName)
				.setEquivalence(Enumerations.ConceptMapEquivalence.EQUAL));

	}


}
