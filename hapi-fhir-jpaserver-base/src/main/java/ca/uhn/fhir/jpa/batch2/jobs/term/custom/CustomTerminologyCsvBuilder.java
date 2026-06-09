package ca.uhn.fhir.jpa.batch2.jobs.term.custom;

import ca.uhn.fhir.jpa.batch2.jobs.term.base.BaseExpandDistributionIntoFilesStep;
import ca.uhn.fhir.jpa.entity.TermConcept;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static ca.uhn.fhir.jpa.batch2.jobs.term.base.BaseExpandDistributionIntoFilesStep.newCsvFormat;

public class CustomTerminologyCsvBuilder {

	private Map<String, TermConcept> myCodeToConcept = new LinkedHashMap<>();
	private Map<String, String> myParentCodeToChildCode = new LinkedHashMap<>();

	public String getConceptsCsv() {
		CSVFormat format = newCsvFormat(',', '"');
		StringBuilder target = new StringBuilder();
		try {
			CSVPrinter csvPrinter = new CSVPrinter(target, format);
			csvPrinter.printHeaders();

			for (TermConcept concept : myCodeToConcept.values()) {
				csvPrinter.printRecord(concept.getCode(), concept.getDisplay());
			}
		} catch (IOException theE) {
			throw new RuntimeException(theE);
		}

	}

	public ConceptBuilder addConcept(String theCode) {
		TermConcept concept = new TermConcept();
		concept.setCode(theCode);
		myCodeToConcept.put(theCode, concept);
		return new ConceptBuilder(concept);
	}

	public void addParentChildRelationship(String theParentCode, String theChildCode) {
		myParentCodeToChildCode.put(theParentCode, theChildCode);
	}


	public static class ConceptBuilder {

		private final TermConcept myConcept;

		public ConceptBuilder(TermConcept theConcept) {
			myConcept = theConcept;
		}

		public void withDisplay(String theDisplay) {
			myConcept.setDisplay(theDisplay);
		}

	}
}
