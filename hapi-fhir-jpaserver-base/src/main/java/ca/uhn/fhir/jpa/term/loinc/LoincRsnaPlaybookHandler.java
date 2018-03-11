package ca.uhn.fhir.jpa.term.loinc;

import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.term.IHapiTerminologyLoaderSvc;
import ca.uhn.fhir.jpa.term.IRecordHandler;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.apache.commons.csv.CSVRecord;
import org.hl7.fhir.r4.model.CanonicalType;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.ValueSet;

import java.util.*;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.trim;

public class LoincRsnaPlaybookHandler implements IRecordHandler {

	public static final String RSNA_CODES_VS_ID = "RSNA-LOINC-CODES-VS";
	public static final String RSNA_CODES_VS_URI = "http://loinc.org/rsna-codes";
	public static final String RSNA_CODES_VS_NAME = "RSNA Playbook";
	public static final String RID_MAPPING_CM_ID = "LOINC-TO-RID-CODES-CM";
	public static final String RID_MAPPING_CM_URI = "http://loinc.org/rid-codes";
	public static final String RID_MAPPING_CM_NAME = "RSNA Playbook RID Codes Mapping";
	public static final String RID_CS_URI = "http://rid";
	public static final String RPID_MAPPING_CM_ID = "LOINC-TO-RPID-CODES-CM";
	public static final String RPID_MAPPING_CM_URI = "http://loinc.org/rpid-codes";
	public static final String RPID_MAPPING_CM_NAME = "RSNA Playbook RPID Codes Mapping";
	public static final String RPID_CS_URI = "http://rpid";
	private final Map<String, TermConcept> myCode2Concept;
	private final TermCodeSystemVersion myCodeSystemVersion;
	private final Set<String> myPropertyNames;
	private final List<ValueSet> myValueSets;
	private final Map<String, ValueSet> myIdToValueSet = new HashMap<>();
	private final List<ConceptMap> myConceptMaps;
	private final Set<String> myCodesInRsnaPlaybookValueSet = new HashSet<>();
	private final Map<String, ConceptMap> myIdToConceptMaps = new HashMap<>();

	/**
	 * Constructor
	 */
	public LoincRsnaPlaybookHandler(TermCodeSystemVersion theCodeSystemVersion, Map<String, TermConcept> theCode2concept, Set<String> thePropertyNames, List<ValueSet> theValueSets, List<ConceptMap> theConceptMaps) {
		myCodeSystemVersion = theCodeSystemVersion;
		myCode2Concept = theCode2concept;
		myPropertyNames = thePropertyNames;
		myValueSets = theValueSets;
		myConceptMaps = theConceptMaps;
	}

	@Override
	public void accept(CSVRecord theRecord) {

		String loincNumber = trim(theRecord.get("LoincNumber"));
		String longCommonName = trim(theRecord.get("LongCommonName"));
		String partNumber = trim(theRecord.get("PartNumber"));
		String partTypeName = trim(theRecord.get("PartTypeName"));
		String partName = trim(theRecord.get("PartName"));
		String partSequenceOrder = trim(theRecord.get("PartSequenceOrder"));
		String rid = trim(theRecord.get("RID"));
		String preferredName = trim(theRecord.get("PreferredName"));
		String rpid = trim(theRecord.get("RPID"));
		String longName = trim(theRecord.get("LongName"));

		// RSNA Codes VS
		ValueSet vs;
		if (!myIdToValueSet.containsKey(RSNA_CODES_VS_ID)) {
			vs = new ValueSet();
			vs.setUrl(RSNA_CODES_VS_URI);
			vs.setId(RSNA_CODES_VS_ID);
			vs.setName(RSNA_CODES_VS_NAME);
			vs.setStatus(Enumerations.PublicationStatus.ACTIVE);
			myIdToValueSet.put(RSNA_CODES_VS_ID, vs);
			myValueSets.add(vs);
		} else {
			vs = myIdToValueSet.get(RSNA_CODES_VS_ID);
		}

		if (!myCodesInRsnaPlaybookValueSet.contains(loincNumber)) {
			vs
				.getCompose()
				.getIncludeFirstRep()
				.setSystem(IHapiTerminologyLoaderSvc.LOINC_URL)
				.addConcept()
				.setCode(loincNumber)
				.setDisplay(longCommonName);
			myCodesInRsnaPlaybookValueSet.add(loincNumber);
		}

		String loincCodePropName;
		switch (partTypeName) {
			case "Rad.Anatomic Location.Region Imaged":
				loincCodePropName = "rad-anatomic-location-region-imaged";
				break;
			case "Rad.Anatomic Location.Imaging Focus":
				loincCodePropName = "rad-anatomic-location-imaging-focus";
				break;
			case "Rad.Modality.Modality type":
				loincCodePropName = "rad-modality-modality-type";
				break;
			default:
				throw new InternalErrorException("Unknown PartTypeName: " + partTypeName);
		}

		TermConcept code = myCode2Concept.get(loincNumber);
		if (code != null) {
			code.addPropertyCoding(loincCodePropName, IHapiTerminologyLoaderSvc.LOINC_URL, partNumber, partName);
		}

		// LOINC Part -> Radlex RID code mappings
		addMapping(partNumber, partName, RID_MAPPING_CM_ID, RID_MAPPING_CM_URI, RID_MAPPING_CM_NAME, RID_CS_URI, rid, preferredName, Enumerations.ConceptMapEquivalence.EQUAL);

		// LOINC Term -> Radlex RPID code mappings
		addMapping(loincNumber, longCommonName, RPID_MAPPING_CM_ID, RPID_MAPPING_CM_URI, RPID_MAPPING_CM_NAME, RPID_CS_URI, rpid, longName, Enumerations.ConceptMapEquivalence.EQUAL);

	}

	private void addMapping(String theLoincNumber, String theLongCommonName, String theConceptMapId, String theConceptMapUri, String theConceptMapName, String theTargetCodeSystemUri, String theTargetCode, String theTargetDisplay, Enumerations.ConceptMapEquivalence theEquivalence) {
		if (isNotBlank(theTargetCode)) {

			ConceptMap conceptMap;
			if (!myIdToConceptMaps.containsKey(theConceptMapId)) {
				conceptMap = new ConceptMap();
				conceptMap.setId(theConceptMapId);
				conceptMap.setUrl(theConceptMapUri);
				conceptMap.setName(theConceptMapName);
				conceptMap.setSource(new CanonicalType(IHapiTerminologyLoaderSvc.LOINC_URL));
				conceptMap.setTarget(new CanonicalType(theTargetCodeSystemUri));
				myIdToConceptMaps.put(theConceptMapId, conceptMap);
				myConceptMaps.add(conceptMap);
			} else {
				conceptMap = myIdToConceptMaps.get(theConceptMapId);
			}

			ConceptMap.SourceElementComponent source = null;
			ConceptMap.ConceptMapGroupComponent group = conceptMap.getGroupFirstRep();
			for (ConceptMap.SourceElementComponent next : group.getElement()) {
				if (next.getCode().equals(theLoincNumber)) {
					source = next;
				}
			}
			if (source == null) {
				source = group.addElement();
				source.setCode(theLoincNumber);
				source.setDisplay(theLongCommonName);
			}

			boolean found = false;
			for (ConceptMap.TargetElementComponent next : source.getTarget()) {
				if (next.getCode().equals(theTargetCode)) {
					found = true;
				}
			}
			if (!found) {
				source
					.addTarget()
					.setCode(theTargetCode)
					.setDisplay(theTargetDisplay)
					.setEquivalence(theEquivalence);
			}
		}
	}

}
