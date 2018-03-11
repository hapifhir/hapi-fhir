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

public class LoincDocumentOntologyHandler implements IRecordHandler {

	public static final String DOCUMENT_ONTOLOGY_CODES_VS_ID = "DOCUMENT_ONTOLOGY_CODES_VS";
	public static final String DOCUMENT_ONTOLOGY_CODES_VS_URI = "http://loinc.org/document-ontology-codes";
	public static final String DOCUMENT_ONTOLOGY_CODES_VS_NAME = "LOINC Document Ontology Codes";
	private final Map<String, TermConcept> myCode2Concept;
	private final TermCodeSystemVersion myCodeSystemVersion;
	private final Set<String> myPropertyNames;
	private final List<ValueSet> myValueSets;
	private final Map<String, ValueSet> myIdToValueSet = new HashMap<>();
	private final Set<String> myCodesInRsnaPlaybookValueSet = new HashSet<>();

	public LoincDocumentOntologyHandler(TermCodeSystemVersion theCodeSystemVersion, Map<String, TermConcept> theCode2concept, Set<String> thePropertyNames, List<ValueSet> theValueSets) {
		myCodeSystemVersion = theCodeSystemVersion;
		myCode2Concept = theCode2concept;
		myPropertyNames = thePropertyNames;
		myValueSets = theValueSets;
	}

	@Override
	public void accept(CSVRecord theRecord) {

		String loincNumber = trim(theRecord.get("LoincNumber"));
		String partNumber = trim(theRecord.get("PartNumber"));
		String partTypeName = trim(theRecord.get("PartTypeName"));
		String partSequenceOrder = trim(theRecord.get("PartSequenceOrder"));
		String partName = trim(theRecord.get("PartName"));

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


	}

}
