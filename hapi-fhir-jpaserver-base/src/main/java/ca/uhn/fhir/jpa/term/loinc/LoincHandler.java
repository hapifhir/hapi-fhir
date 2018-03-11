package ca.uhn.fhir.jpa.term.loinc;

import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.term.IRecordHandler;
import ca.uhn.fhir.jpa.term.TerminologyLoaderSvcImpl;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.Validate;

import java.util.Map;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.StringUtils.trim;

public class LoincHandler implements IRecordHandler {

	private final Map<String, TermConcept> myCode2Concept;
	private final TermCodeSystemVersion myCodeSystemVersion;
	private final Set<String> myPropertyNames;

	public LoincHandler(TermCodeSystemVersion theCodeSystemVersion, Map<String, TermConcept> theCode2concept, Set<String> thePropertyNames) {
		myCodeSystemVersion = theCodeSystemVersion;
		myCode2Concept = theCode2concept;
		myPropertyNames = thePropertyNames;
	}

	@Override
	public void accept(CSVRecord theRecord) {
		String code = trim(theRecord.get("LOINC_NUM"));
		if (isNotBlank(code)) {
			String longCommonName = trim(theRecord.get("LONG_COMMON_NAME"));
			String shortName = trim(theRecord.get("SHORTNAME"));
			String consumerName = trim(theRecord.get("CONSUMER_NAME"));
			String display = TerminologyLoaderSvcImpl.firstNonBlank(longCommonName, shortName, consumerName);

			TermConcept concept = new TermConcept(myCodeSystemVersion, code);
			concept.setDisplay(display);

			for (String nextPropertyName : myPropertyNames) {
				if (!theRecord.toMap().containsKey(nextPropertyName)) {
					continue;
				}
				String nextPropertyValue = theRecord.get(nextPropertyName);
				if (isNotBlank(nextPropertyValue)) {
					concept.addPropertyString(nextPropertyName, nextPropertyValue);
				}
			}

			Validate.isTrue(!myCode2Concept.containsKey(code));
			myCode2Concept.put(code, concept);
		}
	}

}
