package ca.uhn.fhir.jpa.term.loinc;

import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.term.IRecordHandler;
import org.apache.commons.csv.CSVRecord;
import org.hl7.fhir.r4.model.ValueSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.trim;

public class LoincPartLinkHandler implements IRecordHandler {

	private final Map<String, TermConcept> myCode2Concept;
	private final TermCodeSystemVersion myCodeSystemVersion;

	public LoincPartLinkHandler(TermCodeSystemVersion theCodeSystemVersion, Map<String, TermConcept> theCode2concept) {
		myCodeSystemVersion = theCodeSystemVersion;
		myCode2Concept = theCode2concept;
	}

	@Override
	public void accept(CSVRecord theRecord) {

		String loincNumber = trim(theRecord.get("LoincNumber"));
		String longCommonName = trim(theRecord.get("LongCommonName"));
		String partNumber = trim(theRecord.get("PartNumber"));
		String partDisplayName = trim(theRecord.get("PartDisplayName"));
		String status = trim(theRecord.get("Status"));

		TermConcept loincConcept = myCode2Concept.get(loincNumber);
		TermConcept partConcept = myCode2Concept.get(partNumber);

		if (loincConcept==null) {
			ourLog.warn("No loinc code: {}", loincNumber);
			return;
		}
		if (partConcept==null) {
			ourLog.warn("No part code: {}", partNumber);
			return;
		}

		partConcept.addProperty();

	}
private static final Logger ourLog = LoggerFactory.getLogger(LoincPartLinkHandler.class);
}
