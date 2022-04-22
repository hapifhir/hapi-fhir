package ca.uhn.fhir.jpa.search.lastn;

import ca.uhn.fhir.jpa.search.lastn.json.CodeJson;
import ca.uhn.fhir.jpa.search.lastn.json.ObservationJson;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LastNTestDataGenerator {

	public static final long TEST_BASELINE_TIMESTAMP = new Date().toInstant().toEpochMilli();

	public static final String SINGLE_OBSERVATION_RESOURCE_PID = "123";
	public static final String SINGLE_OBSERVATION_SUBJECT_ID = "Patient/4567";

	public static final String FIRSTCATEGORYTEXT = "Test Codeable Concept Field for first category";
	public static final String CATEGORYFIRSTCODINGSYSTEM = "http://mycodes.org/fhir/observation-category";
	public static final String CATEGORYSECONDCODINGSYSTEM = "http://myalternatecodes.org/fhir/observation-category";
	public static final String CATEGORYTHIRDCODINGSYSTEM = "http://mysecondaltcodes.org/fhir/observation-category";
	public static final String FIRSTCATEGORYFIRSTCODINGCODE = "test-heart-rate";
	public static final String FIRSTCATEGORYFIRSTCODINGDISPLAY = "Test Heart Rate";
	public static final String FIRSTCATEGORYSECONDCODINGCODE = "test-alt-heart-rate";
	public static final String FIRSTCATEGORYSECONDCODINGDISPLAY = "Test HeartRate";
	public static final String FIRSTCATEGORYTHIRDCODINGCODE = "test-2nd-alt-heart-rate";
	public static final String FIRSTCATEGORYTHIRDCODINGDISPLAY = "Test Heart-Rate";
	public static final String SECONDCATEGORYTEXT = "Test Codeable Concept Field for for second category";
	public static final String SECONDCATEGORYFIRSTCODINGCODE = "test-vital-signs";
	public static final String SECONDCATEGORYFIRSTCODINGDISPLAY = "Test Vital Signs";
	public static final String SECONDCATEGORYSECONDCODINGCODE = "test-alt-vitals";
	public static final String SECONDCATEGORYSECONDCODINGDISPLAY = "Test Vital-Signs";
	public static final String SECONDCATEGORYTHIRDCODINGCODE = "test-2nd-alt-vitals";
	public static final String SECONDCATEGORYTHIRDCODINGDISPLAY = "Test Vitals";
	public static final String THIRDCATEGORYTEXT = "Test Codeable Concept Field for third category";
	public static final String THIRDCATEGORYFIRSTCODINGCODE = "test-vital-panel";
	public static final String THIRDCATEGORYFIRSTCODINGDISPLAY = "test-vitals-panel display";
	public static final String THIRDCATEGORYSECONDCODINGCODE = "test-alt-vitals-panel";
	public static final String THIRDCATEGORYSECONDCODINGDISPLAY = "test-alt-vitals display";
	public static final String THIRDCATEGORYTHIRDCODINGCODE = "test-2nd-alt-vitals-panel";
	public static final String THIRDCATEGORYTHIRDCODINGDISPLAY = "test-2nd-alt-vitals-panel display";
	public static final String OBSERVATIONSINGLECODEID = UUID.randomUUID().toString();
	public static final String OBSERVATION_CODE_CONCEPT_TEXT_1 = "Test Codeable Concept Field for First Code";
	public static final String OBSERVATION_CODE_CONCEPT_TEXT_2 = "Test Codeable Concept Field for Second Code";
	public static final String CODEFIRSTCODINGSYSTEM = "http://mycodes.org/fhir/observation-code";
	public static final String CODEFIRSTCODINGCODE = "test-code-1";
	public static final String CODEFIRSTCODINGDISPLAY = "1-Observation Code1";
	public static final String CODE_SECOND_CODING_SYSTEM = "http://mycodes.org/fhir/observation-code";
	public static final String CODE_SECOND_CODING_CODE = "test-code-2";
	public static final String CODE_SECOND_CODING_DISPLAY = "2-Observation Code2";

	public static ObservationJson createSingleObservationJson() {
		ObservationJson indexedObservation = new ObservationJson();
		indexedObservation.setIdentifier(SINGLE_OBSERVATION_RESOURCE_PID);
		indexedObservation.setSubject(SINGLE_OBSERVATION_SUBJECT_ID);
		indexedObservation.setEffectiveDtm(new Date(TEST_BASELINE_TIMESTAMP));

		indexedObservation.setCategories(createCategoryCodeableConcepts());

		// Create CodeableConcept for Code
		CodeJson codeableConceptField = new CodeJson();
		codeableConceptField.setCodeableConceptId(OBSERVATIONSINGLECODEID);
		codeableConceptField.setCodeableConceptText(OBSERVATION_CODE_CONCEPT_TEXT_1);
		codeableConceptField.addCoding(CODEFIRSTCODINGSYSTEM, CODEFIRSTCODINGCODE, CODEFIRSTCODINGDISPLAY);

		indexedObservation.setCode(codeableConceptField);

		return indexedObservation;
	}

	private static List<CodeJson> createCategoryCodeableConcepts() {
		CodeJson categoryCodeableConcept1 = new CodeJson();
		categoryCodeableConcept1.setCodeableConceptText(FIRSTCATEGORYTEXT);
		categoryCodeableConcept1.addCoding(CATEGORYFIRSTCODINGSYSTEM, FIRSTCATEGORYFIRSTCODINGCODE, FIRSTCATEGORYFIRSTCODINGDISPLAY);
		categoryCodeableConcept1.addCoding(CATEGORYSECONDCODINGSYSTEM, FIRSTCATEGORYSECONDCODINGCODE, FIRSTCATEGORYSECONDCODINGDISPLAY);
		categoryCodeableConcept1.addCoding(CATEGORYTHIRDCODINGSYSTEM, FIRSTCATEGORYTHIRDCODINGCODE, FIRSTCATEGORYTHIRDCODINGDISPLAY);

		CodeJson categoryCodeableConcept2 = new CodeJson();
		categoryCodeableConcept2.setCodeableConceptText(SECONDCATEGORYTEXT);
		categoryCodeableConcept2.addCoding(CATEGORYFIRSTCODINGSYSTEM, SECONDCATEGORYFIRSTCODINGCODE, SECONDCATEGORYFIRSTCODINGDISPLAY);
		categoryCodeableConcept2.addCoding(CATEGORYSECONDCODINGSYSTEM, SECONDCATEGORYSECONDCODINGCODE, SECONDCATEGORYSECONDCODINGDISPLAY);
		categoryCodeableConcept2.addCoding(CATEGORYTHIRDCODINGSYSTEM, SECONDCATEGORYTHIRDCODINGCODE, SECONDCATEGORYTHIRDCODINGDISPLAY);

		CodeJson categoryCodeableConcept3 = new CodeJson();
		categoryCodeableConcept3.setCodeableConceptText(THIRDCATEGORYTEXT);
		categoryCodeableConcept3.addCoding(CATEGORYFIRSTCODINGSYSTEM, THIRDCATEGORYFIRSTCODINGCODE, THIRDCATEGORYFIRSTCODINGDISPLAY);
		categoryCodeableConcept3.addCoding(CATEGORYSECONDCODINGSYSTEM, THIRDCATEGORYSECONDCODINGCODE, THIRDCATEGORYSECONDCODINGDISPLAY);
		categoryCodeableConcept3.addCoding(CATEGORYTHIRDCODINGSYSTEM, THIRDCATEGORYTHIRDCODINGCODE, THIRDCATEGORYTHIRDCODINGDISPLAY);

		return Arrays.asList(categoryCodeableConcept1, categoryCodeableConcept2, categoryCodeableConcept3);
	}

	public static List<ObservationJson> createMultipleObservationJson(List<Integer> thePatientIds) {

		// CodeableConcept 1 - with 3 codings
		String codeableConceptId1 = UUID.randomUUID().toString();
		CodeJson codeJson1 = new CodeJson();
		codeJson1.setCodeableConceptId(codeableConceptId1);
		codeJson1.setCodeableConceptText(OBSERVATION_CODE_CONCEPT_TEXT_1);
		codeJson1.addCoding(CODEFIRSTCODINGSYSTEM, CODEFIRSTCODINGCODE, CODEFIRSTCODINGDISPLAY);

		// CodeableConcept 2 - with 3 codings
		String codeableConceptId2 = UUID.randomUUID().toString();
		CodeJson codeJson2 = new CodeJson();
		codeJson2.setCodeableConceptId(codeableConceptId2);
		codeJson2.setCodeableConceptText(OBSERVATION_CODE_CONCEPT_TEXT_2);
		codeJson2.addCoding(CODE_SECOND_CODING_SYSTEM, CODE_SECOND_CODING_CODE, CODE_SECOND_CODING_DISPLAY);

		List<CodeJson> categoryCodeableConcepts = createCategoryCodeableConcepts();
		// CategoryCodeableConcept 1 - with 3 codings
		List<CodeJson> categoryConcepts1 = Collections.singletonList(categoryCodeableConcepts.get(0));

		// CateogryCodeableConcept 2 - with 3 codings
		List<CodeJson> categoryConcepts2 = Collections.singletonList(categoryCodeableConcepts.get(1));

		// Pair CodeableConcept 1 + CategoryCodeableConcept 1 for odd numbered observation
		// Pair CodeableConcept 2 + CategoryCodeableConcept 2 for even numbered observation

		// For each patient - create 10 observations
		return thePatientIds.stream()
			.flatMap(patientId -> IntStream.range(0, 10)
				.mapToObj(index -> {
					ObservationJson observationJson = new ObservationJson();
					String identifier = String.valueOf((index + patientId * 10L));
					observationJson.setIdentifier(identifier);
					observationJson.setSubject(String.valueOf(patientId));
					if (index % 2 == 1) {
						observationJson.setCategories(categoryConcepts1);
						observationJson.setCode(codeJson1);
					} else {
						observationJson.setCategories(categoryConcepts2);
						observationJson.setCode(codeJson2);
					}
					Date effectiveDtm = new Date(TEST_BASELINE_TIMESTAMP - ((10L - index) * 3600L * 1000L));
					observationJson.setEffectiveDtm(effectiveDtm);
					return observationJson;
				}))
			.collect(Collectors.toList());
	}

}
