package ca.uhn.fhir.jpa.term.loinc;

import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.term.IHapiTerminologyLoaderSvc;
import ca.uhn.fhir.jpa.term.IRecordHandler;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.apache.commons.csv.CSVRecord;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.ValueSet;

import java.util.*;

import static org.apache.commons.lang3.StringUtils.trim;

public class LoincDocumentOntologyHandler extends BaseHandler implements IRecordHandler {

	public static final String DOCUMENT_ONTOLOGY_CODES_VS_ID = "DOCUMENT_ONTOLOGY_CODES_VS";
	public static final String DOCUMENT_ONTOLOGY_CODES_VS_URI = "http://loinc.org/document-ontology-codes";
	public static final String DOCUMENT_ONTOLOGY_CODES_VS_NAME = "LOINC Document Ontology Codes";
	private final Map<String, TermConcept> myCode2Concept;
	private final TermCodeSystemVersion myCodeSystemVersion;
	private final Set<String> myPropertyNames;

	public LoincDocumentOntologyHandler(TermCodeSystemVersion theCodeSystemVersion, Map<String, TermConcept> theCode2concept, Set<String> thePropertyNames, List<ValueSet> theValueSets) {
		super(theCode2concept, theValueSets);
		myCodeSystemVersion = theCodeSystemVersion;
		myCode2Concept = theCode2concept;
		myPropertyNames = thePropertyNames;
	}

	@Override
	public void accept(CSVRecord theRecord) {

		String loincNumber = trim(theRecord.get("LoincNumber"));
		String partNumber = trim(theRecord.get("PartNumber"));
		String partTypeName = trim(theRecord.get("PartTypeName"));
		String partSequenceOrder = trim(theRecord.get("PartSequenceOrder"));
		String partName = trim(theRecord.get("PartName"));

		// RSNA Codes VS
		ValueSet vs = getValueSet(DOCUMENT_ONTOLOGY_CODES_VS_ID, DOCUMENT_ONTOLOGY_CODES_VS_URI, DOCUMENT_ONTOLOGY_CODES_VS_NAME);
		addCodeAsIncludeToValueSet(vs, IHapiTerminologyLoaderSvc.LOINC_URL, loincNumber, null);

		// Part Properties
		String loincCodePropName;
		switch (partTypeName) {
			case "Document.Kind":
				loincCodePropName = "document-kind";
				break;
			case "Document.Role":
				loincCodePropName = "document-role";
				break;
			case "Document.Setting":
				loincCodePropName = "document-setting";
				break;
			case "Document.SubjectMatterDomain":
				loincCodePropName = "document-subject-matter-domain";
				break;
			case "Document.TypeOfService":
				loincCodePropName = "document-type-of-service";
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
