package ca.uhn.fhir.jpa.batch2.jobs.term.custom;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.entity.TermConceptProperty;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import jakarta.annotation.Nonnull;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.hl7.fhir.common.hapi.validation.util.TermConceptPropertyTypeEnum;
import org.hl7.fhir.r4.model.Coding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static ca.uhn.fhir.jpa.batch2.jobs.term.base.BaseExpandDistributionIntoFilesStep.newCsvFormat;
import static org.apache.commons.lang3.ObjectUtils.getIfNull;

public class CustomTerminologyCsvBuilder {

	private Map<String, TermConcept> myCodeToConcept = new LinkedHashMap<>();
	private Map<String, String> myParentCodeToChildCode = new LinkedHashMap<>();

	public String getHierarchyCsv() {
		List<String> headers = List.of(
				ImportCustomTerminologyStep4HandleHierarchy.PARENT, ImportCustomTerminologyStep4HandleHierarchy.CHILD);
		Function<Map.Entry<String, String>, List<List<Object>>> renderer =
				c -> List.of(List.of(c.getKey(), c.getValue()));
		return renderCsv(headers, myParentCodeToChildCode.entrySet(), renderer);
	}

	public String getConceptsCsv() {
		List<String> headers = List.of(
				ImportCustomTerminologyStep2HandleConcepts.CODE, ImportCustomTerminologyStep2HandleConcepts.DISPLAY);
		Function<TermConcept, List<List<Object>>> renderer =
				c -> List.of(List.of(c.getCode(), getIfNull(c.getDisplay(), "")));
		return renderCsv(headers, myCodeToConcept.values(), renderer);
	}

	public String getPropertiesCsv() {
		List<String> headers = List.of(
				ImportCustomTerminologyStep3HandleProperties.KEY,
				ImportCustomTerminologyStep3HandleProperties.CODE,
				ImportCustomTerminologyStep3HandleProperties.TYPE,
				ImportCustomTerminologyStep3HandleProperties.VALUE);
		Function<TermConcept, List<List<Object>>> renderer = c -> {
			List<List<Object>> rows = new ArrayList<>();
			for (TermConceptProperty property : c.getProperties()) {
				String value =
						switch (property.getType()) {
							case STRING, DATETIME, DECIMAL, INTEGER, BOOLEAN, CODE -> property.getValue();
							case CODING -> {
								Coding coding = new Coding();
								coding.setSystem(property.getCodeSystem());
								coding.setCode(property.getValue());
								coding.setDisplay(property.getDisplay());
								yield FhirContext.forR4Cached()
										.newJsonParser()
										.setPrettyPrint(false)
										.encodeToString(coding);
							}
						};
				rows.add(List.of(
						property.getKey(), c.getCode(), property.getType().getDatatype(), value));
			}
			return rows;
		};
		return renderCsv(headers, myCodeToConcept.values(), renderer);
	}

	@Nonnull
	private <T> String renderCsv(
			List<String> theHeaders, Collection<T> theValues, Function<T, List<List<Object>>> theRenderer) {
		CSVFormat format = newCsvFormat(',', '"');
		StringBuilder target = new StringBuilder();
		try (CSVPrinter csvPrinter = new CSVPrinter(target, format)) {

			csvPrinter.printRecord(theHeaders);
			csvPrinter.println();

			for (T concept : theValues) {
				for (List<Object> row : theRenderer.apply(concept)) {
					csvPrinter.printRecord(row);
				}
			}

		} catch (IOException theE) {
			// This shouldn't happen
			throw new InternalErrorException(Msg.code(1) + theE.getMessage(), theE);
		}

		return target.toString();
	}

	public ConceptBuilder addConcept(String theCode) {
		TermConcept concept = new TermConcept();
		concept.setCode(theCode);
		myCodeToConcept.put(theCode, concept);
		return new ConceptBuilder(concept);
	}

	public class ConceptBuilder {

		private final TermConcept myConcept;

		public ConceptBuilder(TermConcept theConcept) {
			myConcept = theConcept;
		}

		public ConceptBuilder withDisplay(String theDisplay) {
			myConcept.setDisplay(theDisplay);
			return this;
		}

		public ConceptBuilder withProperty(String theKey, TermConceptPropertyTypeEnum theType, String theValue) {
			TermConceptProperty property = new TermConceptProperty();
			property.setKey(theKey);
			property.setType(theType);
			property.setValue(theValue);
			myConcept.getProperties().add(property);
			return this;
		}

		public ConceptBuilder withPropertyCoding(String theKey, String theSystem, String theCode, String theDisplay) {
			TermConceptProperty property = new TermConceptProperty();
			property.setKey(theKey);
			property.setType(TermConceptPropertyTypeEnum.CODING);
			property.setCodeSystem(theSystem);
			property.setValue(theCode);
			property.setDisplay(theDisplay);
			myConcept.getProperties().add(property);
			return this;
		}

		public ConceptBuilder withParent(String theParent) {
			myParentCodeToChildCode.put(theParent, myConcept.getCode());
			return this;
		}
	}
}
