package ca.uhn.fhir.jpa.term.loinc;

import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.term.IRecordHandler;
import org.apache.commons.csv.CSVRecord;
import org.hl7.fhir.r4.model.ValueSet;

import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.trim;

public class LoincPartHandler implements IRecordHandler {

	private final Map<String, TermConcept> myCode2Concept;
	private final TermCodeSystemVersion myCodeSystemVersion;
	private final Map<String, ValueSet> myIdToValueSet = new HashMap<>();

	public LoincPartHandler(TermCodeSystemVersion theCodeSystemVersion, Map<String, TermConcept> theCode2concept) {
		myCodeSystemVersion = theCodeSystemVersion;
		myCode2Concept = theCode2concept;
	}

	@Override
	public void accept(CSVRecord theRecord) {

		// this is the code for the list (will repeat)
		String partNumber = trim(theRecord.get("PartNumber"));
		String partTypeName = trim(theRecord.get("PartTypeName"));
		String partName = trim(theRecord.get("PartName"));
		String partDisplayName = trim(theRecord.get("PartDisplayName"));
		String status = trim(theRecord.get("Status"));

		if (!"ACTIVE".equals(status)) {
			return;
		}

		TermConcept concept = new TermConcept(myCodeSystemVersion, partNumber);
		concept.setDisplay(partName);

		myCode2Concept.put(partDisplayName, concept);
	}

}
