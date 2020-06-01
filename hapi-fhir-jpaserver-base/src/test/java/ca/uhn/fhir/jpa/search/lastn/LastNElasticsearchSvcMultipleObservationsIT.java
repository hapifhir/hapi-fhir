package ca.uhn.fhir.jpa.search.lastn;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.param.*;
import ca.uhn.fhir.jpa.search.lastn.config.TestElasticsearchConfig;
import ca.uhn.fhir.jpa.search.lastn.json.CodeJson;
import ca.uhn.fhir.jpa.search.lastn.json.ObservationJson;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.hl7.fhir.r4.model.Observation;
import org.junit.*;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestElasticsearchConfig.class})
public class LastNElasticsearchSvcMultipleObservationsIT {

	@Autowired
	private ElasticsearchSvcImpl elasticsearchSvc;

	private static ObjectMapper ourMapperNonPrettyPrint;

	private final Map<String, Map<String, List<Date>>> createdPatientObservationMap = new HashMap<>();

	private FhirContext myFhirContext = FhirContext.forR4();


	@BeforeClass
	public static void beforeClass() {
		ourMapperNonPrettyPrint = new ObjectMapper();
		ourMapperNonPrettyPrint.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		ourMapperNonPrettyPrint.disable(SerializationFeature.INDENT_OUTPUT);
		ourMapperNonPrettyPrint.disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET);
	}

	@Before
	public void before() throws IOException {
		createMultiplePatientsAndObservations();
	}

	@After
	public void after() throws IOException {
		elasticsearchSvc.deleteAllDocuments(ElasticsearchSvcImpl.OBSERVATION_INDEX);
		elasticsearchSvc.deleteAllDocuments(ElasticsearchSvcImpl.OBSERVATION_CODE_INDEX);
	}

	@Test
	public void testLastNAllPatientsQuery() {

		// execute Observation ID search (Composite Aggregation) last 3 observations for each patient
		SearchParameterMap searchParameterMap = new SearchParameterMap();
		ReferenceParam subjectParam = new ReferenceParam("Patient", "", "0");
		searchParameterMap.add(Observation.SP_SUBJECT, buildReferenceAndListParam(subjectParam));
		subjectParam = new ReferenceParam("Patient", "", "1");
		searchParameterMap.add(Observation.SP_SUBJECT, buildReferenceAndListParam(subjectParam));
		subjectParam = new ReferenceParam("Patient", "", "2");
		searchParameterMap.add(Observation.SP_SUBJECT, buildReferenceAndListParam(subjectParam));
		subjectParam = new ReferenceParam("Patient", "", "3");
		searchParameterMap.add(Observation.SP_SUBJECT, buildReferenceAndListParam(subjectParam));
		subjectParam = new ReferenceParam("Patient", "", "4");
		searchParameterMap.add(Observation.SP_SUBJECT, buildReferenceAndListParam(subjectParam));
		subjectParam = new ReferenceParam("Patient", "", "5");
		searchParameterMap.add(Observation.SP_SUBJECT, buildReferenceAndListParam(subjectParam));
		subjectParam = new ReferenceParam("Patient", "", "6");
		searchParameterMap.add(Observation.SP_SUBJECT, buildReferenceAndListParam(subjectParam));
		subjectParam = new ReferenceParam("Patient", "", "7");
		searchParameterMap.add(Observation.SP_SUBJECT, buildReferenceAndListParam(subjectParam));
		subjectParam = new ReferenceParam("Patient", "", "8");
		searchParameterMap.add(Observation.SP_SUBJECT, buildReferenceAndListParam(subjectParam));
		subjectParam = new ReferenceParam("Patient", "", "9");
		searchParameterMap.add(Observation.SP_SUBJECT, buildReferenceAndListParam(subjectParam));
		searchParameterMap.setLastNMax(3);

		List<ObservationJson> observations = elasticsearchSvc.executeLastNWithAllFields(searchParameterMap, myFhirContext);

		assertEquals(60, observations.size());

		// Observation documents should be grouped by subject, then by observation code, and then sorted by effective date/time
		// within each observation code. Verify the grouping by creating a nested Map.
		Map<String, Map<String, List<Date>>> queriedPatientObservationMap = new HashMap<>();
		ObservationJson previousObservationJson = null;
		for (ObservationJson observationJson : observations) {
			assertNotNull(observationJson.getIdentifier());
			assertNotNull(observationJson.getSubject());
			assertNotNull(observationJson.getCode_concept_id());
			assertNotNull(observationJson.getEffectiveDtm());
			if (previousObservationJson == null) {
				ArrayList<Date> observationDates = new ArrayList<>();
				observationDates.add(observationJson.getEffectiveDtm());
				Map<String, List<Date>> codeObservationMap = new HashMap<>();
				codeObservationMap.put(observationJson.getCode_concept_id(), observationDates);
				queriedPatientObservationMap.put(observationJson.getSubject(), codeObservationMap);
			} else if (observationJson.getSubject().equals(previousObservationJson.getSubject())) {
				if (observationJson.getCode_concept_id().equals(previousObservationJson.getCode_concept_id())) {
					queriedPatientObservationMap.get(observationJson.getSubject()).get(observationJson.getCode_concept_id()).
						add(observationJson.getEffectiveDtm());
				} else {
					Map<String, List<Date>> codeObservationDateMap = queriedPatientObservationMap.get(observationJson.getSubject());
					// Ensure that code concept was not already retrieved out of order for this subject/patient.
					assertFalse(codeObservationDateMap.containsKey(observationJson.getCode_concept_id()));
					ArrayList<Date> observationDates = new ArrayList<>();
					observationDates.add(observationJson.getEffectiveDtm());
					codeObservationDateMap.put(observationJson.getCode_concept_id(), observationDates);
				}
			} else {
				// Ensure that subject/patient was not already retrieved out of order
				assertFalse(queriedPatientObservationMap.containsKey(observationJson.getSubject()));
				ArrayList<Date> observationDates = new ArrayList<>();
				observationDates.add(observationJson.getEffectiveDtm());
				Map<String, List<Date>> codeObservationMap = new HashMap<>();
				codeObservationMap.put(observationJson.getCode_concept_id(), observationDates);
				queriedPatientObservationMap.put(observationJson.getSubject(), codeObservationMap);
			}
			previousObservationJson = observationJson;
		}

		// Finally check that only the most recent effective date/time values were returned and in the correct order.
		for (String subjectId : queriedPatientObservationMap.keySet()) {
			Map<String, List<Date>> queriedObservationCodeMap = queriedPatientObservationMap.get(subjectId);
			Map<String, List<Date>> createdObservationCodeMap = createdPatientObservationMap.get(subjectId);
			for (String observationCode : queriedObservationCodeMap.keySet()) {
				List<Date> queriedObservationDates = queriedObservationCodeMap.get(observationCode);
				List<Date> createdObservationDates = createdObservationCodeMap.get(observationCode);
				for (int dateIdx = 0; dateIdx < queriedObservationDates.size(); dateIdx++) {
					assertEquals(createdObservationDates.get(dateIdx), queriedObservationDates.get(dateIdx));
				}
			}
		}

	}

	@Test
	public void testLastNMultiPatientMultiCodeHashMultiCategoryHash() {
		// Multiple Subject references
		SearchParameterMap searchParameterMap = new SearchParameterMap();
		ReferenceParam subjectParam1 = new ReferenceParam("Patient", "", "3");
		ReferenceParam subjectParam2 = new ReferenceParam("Patient", "", "5");
		searchParameterMap.add(Observation.SP_SUBJECT, buildReferenceAndListParam(subjectParam1, subjectParam2));
		TokenParam categoryParam1 = new TokenParam("http://mycodes.org/fhir/observation-category", "test-heart-rate");
		TokenParam categoryParam2 = new TokenParam("http://mycodes.org/fhir/observation-category", "test-vital-signs");
		searchParameterMap.add(Observation.SP_CATEGORY, buildTokenAndListParam(categoryParam1, categoryParam2));
		TokenParam codeParam1 = new TokenParam("http://mycodes.org/fhir/observation-code", "test-code-1");
		TokenParam codeParam2 = new TokenParam("http://mycodes.org/fhir/observation-code", "test-code-2");
		searchParameterMap.add(Observation.SP_CODE, buildTokenAndListParam(codeParam1, codeParam2));
		searchParameterMap.setLastNMax(100);

		List<String> observations = elasticsearchSvc.executeLastN(searchParameterMap, myFhirContext, 100);

		assertEquals(20, observations.size());

		// Repeat with multiple Patient parameter
		searchParameterMap = new SearchParameterMap();
		ReferenceParam patientParam1 = new ReferenceParam("Patient", "", "8");
		ReferenceParam patientParam2 = new ReferenceParam("Patient", "", "6");
		searchParameterMap.add(Observation.SP_PATIENT, buildReferenceAndListParam(patientParam1, patientParam2));
		searchParameterMap.add(Observation.SP_CATEGORY, buildTokenAndListParam(categoryParam1, categoryParam2));
		searchParameterMap.add(Observation.SP_CODE, buildTokenAndListParam(codeParam1, codeParam2));
		searchParameterMap.setLastNMax(100);

		observations = elasticsearchSvc.executeLastN(searchParameterMap, myFhirContext, 100);

		assertEquals(20, observations.size());

	}

	private ReferenceAndListParam buildReferenceAndListParam(ReferenceParam... theReference) {
		ReferenceOrListParam myReferenceOrListParam = new ReferenceOrListParam();
		for (ReferenceParam referenceParam : theReference) {
			myReferenceOrListParam.addOr(referenceParam);
		}
		return new ReferenceAndListParam().addAnd(myReferenceOrListParam);
	}

	private TokenAndListParam buildTokenAndListParam(TokenParam... theToken) {
		TokenOrListParam myTokenOrListParam = new TokenOrListParam();
		for (TokenParam tokenParam : theToken) {
			myTokenOrListParam.addOr(tokenParam);
		}
		return new TokenAndListParam().addAnd(myTokenOrListParam);
	}

	@Test
	public void testLastNCodeCodeOnlyCategoryCodeOnly() {
		// Include subject
		SearchParameterMap searchParameterMap = new SearchParameterMap();
		ReferenceParam subjectParam = new ReferenceParam("Patient", "", "3");
		searchParameterMap.add(Observation.SP_SUBJECT, buildReferenceAndListParam(subjectParam));
		TokenParam categoryParam = new TokenParam("test-heart-rate");
		searchParameterMap.add(Observation.SP_CATEGORY, buildTokenAndListParam(categoryParam));
		TokenParam codeParam = new TokenParam("test-code-1");
		searchParameterMap.add(Observation.SP_CODE, buildTokenAndListParam(codeParam));
		searchParameterMap.setLastNMax(100);

		List<String> observations = elasticsearchSvc.executeLastN(searchParameterMap, myFhirContext, 100);

		assertEquals(5, observations.size());

	}

	@Test
	public void testLastNCodeSystemOnlyCategorySystemOnly() {
		// Include subject and patient
		SearchParameterMap searchParameterMap = new SearchParameterMap();
		ReferenceParam subjectParam = new ReferenceParam("Patient", "", "3");
		searchParameterMap.add(Observation.SP_SUBJECT, buildReferenceAndListParam(subjectParam));
		TokenParam categoryParam = new TokenParam("http://mycodes.org/fhir/observation-category", null);
		searchParameterMap.add(Observation.SP_CATEGORY, buildTokenAndListParam(categoryParam));
		TokenParam codeParam = new TokenParam("http://mycodes.org/fhir/observation-code", null);
		searchParameterMap.add(Observation.SP_CODE, buildTokenAndListParam(codeParam));
		searchParameterMap.setLastNMax(100);

		List<String> observations = elasticsearchSvc.executeLastN(searchParameterMap, myFhirContext, 100);

		assertEquals(10, observations.size());
	}

	@Test
	public void testLastNCodeCodeTextCategoryTextOnly() {
		SearchParameterMap searchParameterMap = new SearchParameterMap();
		ReferenceParam subjectParam = new ReferenceParam("Patient", "", "3");
		searchParameterMap.add(Observation.SP_SUBJECT, buildReferenceAndListParam(subjectParam));
		TokenParam categoryParam = new TokenParam("test-heart-rate display");
		categoryParam.setModifier(TokenParamModifier.TEXT);
		searchParameterMap.add(Observation.SP_CATEGORY, buildTokenAndListParam(categoryParam));
		TokenParam codeParam = new TokenParam("test-code-1 display");
		codeParam.setModifier(TokenParamModifier.TEXT);
		searchParameterMap.add(Observation.SP_CODE, buildTokenAndListParam(codeParam));
		searchParameterMap.setLastNMax(100);

		List<String> observations = elasticsearchSvc.executeLastN(searchParameterMap, myFhirContext, 100);

		assertEquals(5, observations.size());

	}

	@Test
	public void testLastNNoMatchQueries() {
		// Invalid Patient
		SearchParameterMap searchParameterMap = new SearchParameterMap();
		ReferenceParam patientParam = new ReferenceParam("Patient", "", "10");
		searchParameterMap.add(Observation.SP_PATIENT, buildReferenceAndListParam(patientParam));
		TokenParam categoryParam = new TokenParam("http://mycodes.org/fhir/observation-category", "test-heart-rate");
		searchParameterMap.add(Observation.SP_CATEGORY, buildTokenAndListParam(categoryParam));
		TokenParam codeParam = new TokenParam("http://mycodes.org/fhir/observation-code", "test-code-1");
		searchParameterMap.add(Observation.SP_CODE, buildTokenAndListParam(codeParam));
		searchParameterMap.setLastNMax(100);

		List<String> observations = elasticsearchSvc.executeLastN(searchParameterMap, myFhirContext, 100);
		assertEquals(0, observations.size());

		// Invalid subject
		searchParameterMap = new SearchParameterMap();
		ReferenceParam subjectParam = new ReferenceParam("Patient", "", "10");
		searchParameterMap.add(Observation.SP_SUBJECT, buildReferenceAndListParam(subjectParam));
		categoryParam = new TokenParam("http://mycodes.org/fhir/observation-category", "test-heart-rate");
		searchParameterMap.add(Observation.SP_CATEGORY, buildTokenAndListParam(categoryParam));
		codeParam = new TokenParam("http://mycodes.org/fhir/observation-code", "test-code-1");
		searchParameterMap.add(Observation.SP_CODE, buildTokenAndListParam(codeParam));
		searchParameterMap.setLastNMax(100);

		observations = elasticsearchSvc.executeLastN(searchParameterMap, myFhirContext, 100);
		assertEquals(0, observations.size());

		// Invalid observation code
		searchParameterMap = new SearchParameterMap();
		subjectParam = new ReferenceParam("Patient", "", "9");
		searchParameterMap.add(Observation.SP_SUBJECT, buildReferenceAndListParam(subjectParam));
		categoryParam = new TokenParam("http://mycodes.org/fhir/observation-category", "test-heart-rate");
		searchParameterMap.add(Observation.SP_CATEGORY, buildTokenAndListParam(categoryParam));
		codeParam = new TokenParam("http://mycodes.org/fhir/observation-code", "test-code-999");
		searchParameterMap.add(Observation.SP_CODE, buildTokenAndListParam(codeParam));
		searchParameterMap.setLastNMax(100);

		observations = elasticsearchSvc.executeLastN(searchParameterMap, myFhirContext, 100);
		assertEquals(0, observations.size());

		// Invalid category code
		searchParameterMap = new SearchParameterMap();
		subjectParam = new ReferenceParam("Patient", "", "9");
		searchParameterMap.add(Observation.SP_SUBJECT, buildReferenceAndListParam(subjectParam));
		categoryParam = new TokenParam("http://mycodes.org/fhir/observation-category", "test-not-a-category");
		searchParameterMap.add(Observation.SP_CATEGORY, buildTokenAndListParam(categoryParam));
		codeParam = new TokenParam("http://mycodes.org/fhir/observation-code", "test-code-1");
		searchParameterMap.add(Observation.SP_CODE, buildTokenAndListParam(codeParam));
		searchParameterMap.setLastNMax(100);

		observations = elasticsearchSvc.executeLastN(searchParameterMap, myFhirContext, 100);
		assertEquals(0, observations.size());

	}

	private void createMultiplePatientsAndObservations() throws IOException {
		// Create CodeableConcepts for two Codes, each with three codings.
		String codeableConceptId1 = UUID.randomUUID().toString();
		CodeableConcept codeableConceptField1 = new CodeableConcept().setText("Test Codeable Concept Field for First Code");
		codeableConceptField1.addCoding(new Coding("http://mycodes.org/fhir/observation-code", "test-code-1", "test-code-1 display"));
		CodeJson codeJson1 = new CodeJson(codeableConceptField1, codeableConceptId1);
		String codeJson1Document = ourMapperNonPrettyPrint.writeValueAsString(codeJson1);

		String codeableConceptId2 = UUID.randomUUID().toString();
		CodeableConcept codeableConceptField2 = new CodeableConcept().setText("Test Codeable Concept Field for Second Code");
		codeableConceptField2.addCoding(new Coding("http://mycodes.org/fhir/observation-code", "test-code-2", "test-code-2 display"));
		CodeJson codeJson2 = new CodeJson(codeableConceptField2, codeableConceptId2);
		String codeJson2Document = ourMapperNonPrettyPrint.writeValueAsString(codeJson2);

		// Create CodeableConcepts for two categories, each with three codings.
		List<Coding> category1 = new ArrayList<>();
		// Create three codings and first category CodeableConcept
		category1.add(new Coding("http://mycodes.org/fhir/observation-category", "test-heart-rate", "test-heart-rate display"));
		category1.add(new Coding("http://myalternatecodes.org/fhir/observation-category", "test-alt-heart-rate", "test-alt-heart-rate display"));
		category1.add(new Coding("http://mysecondaltcodes.org/fhir/observation-category", "test-2nd-alt-heart-rate", "test-2nd-alt-heart-rate display"));
		List<CodeableConcept> categoryConcepts1 = new ArrayList<>();
		CodeableConcept categoryCodeableConcept1 = new CodeableConcept().setText("Test Codeable Concept Field for first category");
		categoryCodeableConcept1.setCoding(category1);
		categoryConcepts1.add(categoryCodeableConcept1);
		// Create three codings and second category CodeableConcept
		List<Coding> category2 = new ArrayList<>();
		category2.add(new Coding("http://mycodes.org/fhir/observation-category", "test-vital-signs", "test-vital-signs display"));
		category2.add(new Coding("http://myalternatecodes.org/fhir/observation-category", "test-alt-vitals", "test-alt-vitals display"));
		category2.add(new Coding("http://mysecondaltcodes.org/fhir/observation-category", "test-2nd-alt-vitals", "test-2nd-alt-vitals display"));
		List<CodeableConcept> categoryConcepts2 = new ArrayList<>();
		CodeableConcept categoryCodeableConcept2 = new CodeableConcept().setText("Test Codeable Concept Field for second category");
		categoryCodeableConcept2.setCoding(category2);
		categoryConcepts2.add(categoryCodeableConcept2);

		for (int patientCount = 0; patientCount < 10; patientCount++) {

			String subject = String.valueOf(patientCount);

			for (int entryCount = 0; entryCount < 10; entryCount++) {

				ObservationJson observationJson = new ObservationJson();
				String identifier = String.valueOf((entryCount + patientCount * 10));
				observationJson.setIdentifier(identifier);
				observationJson.setSubject(subject);

				if (entryCount % 2 == 1) {
					observationJson.setCategories(categoryConcepts1);
					observationJson.setCode(codeableConceptField1);
					observationJson.setCode_concept_id(codeableConceptId1);
					assertTrue(elasticsearchSvc.performIndex(ElasticsearchSvcImpl.OBSERVATION_CODE_INDEX, codeableConceptId1, codeJson1Document, ElasticsearchSvcImpl.CODE_DOCUMENT_TYPE));
				} else {
					observationJson.setCategories(categoryConcepts2);
					observationJson.setCode(codeableConceptField2);
					observationJson.setCode_concept_id(codeableConceptId2);
					assertTrue(elasticsearchSvc.performIndex(ElasticsearchSvcImpl.OBSERVATION_CODE_INDEX, codeableConceptId2, codeJson2Document, ElasticsearchSvcImpl.CODE_DOCUMENT_TYPE));
				}

				Calendar observationDate = new GregorianCalendar();
				observationDate.add(Calendar.HOUR, -10 + entryCount);
				Date effectiveDtm = observationDate.getTime();
				observationJson.setEffectiveDtm(effectiveDtm);

				String observationDocument = ourMapperNonPrettyPrint.writeValueAsString(observationJson);
				assertTrue(elasticsearchSvc.performIndex(ElasticsearchSvcImpl.OBSERVATION_INDEX, identifier, observationDocument, ElasticsearchSvcImpl.OBSERVATION_DOCUMENT_TYPE));

				if (createdPatientObservationMap.containsKey(subject)) {
					Map<String, List<Date>> observationCodeMap = createdPatientObservationMap.get(subject);
					if (observationCodeMap.containsKey(observationJson.getCode_concept_id())) {
						List<Date> observationDates = observationCodeMap.get(observationJson.getCode_concept_id());
						// Want dates to be sorted in descending order
						observationDates.add(0, effectiveDtm);
						// Only keep the three most recent dates for later check.
						if (observationDates.size() > 3) {
							observationDates.remove(3);
						}
					} else {
						ArrayList<Date> observationDates = new ArrayList<>();
						observationDates.add(effectiveDtm);
						observationCodeMap.put(observationJson.getCode_concept_id(), observationDates);
					}
				} else {
					ArrayList<Date> observationDates = new ArrayList<>();
					observationDates.add(effectiveDtm);
					Map<String, List<Date>> codeObservationMap = new HashMap<>();
					codeObservationMap.put(observationJson.getCode_concept_id(), observationDates);
					createdPatientObservationMap.put(subject, codeObservationMap);
				}
			}
		}

		try {
			Thread.sleep(2000L);
		} catch (InterruptedException theE) {
			theE.printStackTrace();
		}

	}

	@Test
	public void testLastNNoParamsQuery() {
		SearchParameterMap searchParameterMap = new SearchParameterMap();
		searchParameterMap.setLastNMax(1);
		List<ObservationJson> observations = elasticsearchSvc.executeLastNWithAllFields(searchParameterMap, myFhirContext);

		assertEquals(2, observations.size());

		String observationCode1 = observations.get(0).getCode_coding_code_system_hash();
		String observationCode2 = observations.get(1).getCode_coding_code_system_hash();

		assertNotEquals(observationCode1, observationCode2);

	}


}
