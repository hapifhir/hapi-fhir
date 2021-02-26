package ca.uhn.fhir.jpa.search.lastn;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.search.lastn.config.TestElasticsearchContainerHelper;
import ca.uhn.fhir.jpa.search.lastn.json.CodeJson;
import ca.uhn.fhir.jpa.search.lastn.json.ObservationJson;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.param.DateAndListParam;
import ca.uhn.fhir.rest.param.DateOrListParam;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import ca.uhn.fhir.rest.param.ReferenceAndListParam;
import ca.uhn.fhir.rest.param.ReferenceOrListParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.TokenAndListParam;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.TokenParamModifier;
import ca.uhn.fhir.test.utilities.docker.RequiresDocker;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.hl7.fhir.r4.model.Observation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith({SpringExtension.class})
@RequiresDocker
@Testcontainers
public class LastNElasticsearchSvcMultipleObservationsIT {

	static private final Calendar baseObservationDate = new GregorianCalendar();
	private static ObjectMapper ourMapperNonPrettyPrint;

	private static boolean indexLoaded = false;

	private final Map<String, Map<String, List<Date>>> createdPatientObservationMap = new HashMap<>();

	private final FhirContext myFhirContext = FhirContext.forCached(FhirVersionEnum.R4);


	@Container
	public static ElasticsearchContainer elasticsearchContainer = TestElasticsearchContainerHelper.getEmbeddedElasticSearch();



	private ElasticsearchSvcImpl elasticsearchSvc;

	@BeforeEach
	public void before() throws IOException {
		PartitionSettings partitionSettings = new PartitionSettings();
		partitionSettings.setPartitioningEnabled(false);
		elasticsearchSvc = new ElasticsearchSvcImpl(partitionSettings, elasticsearchContainer.getHost(), elasticsearchContainer.getMappedPort(9200), "", "");

		if (!indexLoaded) {
			createMultiplePatientsAndObservations();
			indexLoaded = true;
		}
	}

	@AfterEach
	public void after() throws IOException {
		elasticsearchSvc.close();
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

		List<ObservationJson> observations = elasticsearchSvc.executeLastNWithAllFieldsForTest(searchParameterMap, myFhirContext);

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
		TokenParam categoryParam = new TokenParam(null, "test-heart-rate");
		searchParameterMap.add(Observation.SP_CATEGORY, buildTokenAndListParam(categoryParam));
		TokenParam codeParam = new TokenParam(null, "test-code-1");
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

		// Check case match
		searchParameterMap.add(Observation.SP_SUBJECT, buildReferenceAndListParam(subjectParam));
		TokenParam categoryParam = new TokenParam("Heart");
		categoryParam.setModifier(TokenParamModifier.TEXT);
		searchParameterMap.add(Observation.SP_CATEGORY, buildTokenAndListParam(categoryParam));
		TokenParam codeParam = new TokenParam("Code1");
		codeParam.setModifier(TokenParamModifier.TEXT);
		searchParameterMap.add(Observation.SP_CODE, buildTokenAndListParam(codeParam));
		searchParameterMap.setLastNMax(100);

		List<String> observations = elasticsearchSvc.executeLastN(searchParameterMap, myFhirContext, 100);

		assertEquals(5, observations.size());

		// Check case not match
		searchParameterMap = new SearchParameterMap();
		searchParameterMap.add(Observation.SP_SUBJECT, buildReferenceAndListParam(subjectParam));
		categoryParam = new TokenParam("heart");
		categoryParam.setModifier(TokenParamModifier.TEXT);
		searchParameterMap.add(Observation.SP_CATEGORY, buildTokenAndListParam(categoryParam));
		codeParam = new TokenParam("code1");
		codeParam.setModifier(TokenParamModifier.TEXT);
		searchParameterMap.add(Observation.SP_CODE, buildTokenAndListParam(codeParam));
		searchParameterMap.setLastNMax(100);

		observations = elasticsearchSvc.executeLastN(searchParameterMap, myFhirContext, 100);

		assertEquals(5, observations.size());

		// Check hyphenated strings
		searchParameterMap = new SearchParameterMap();
		searchParameterMap.add(Observation.SP_SUBJECT, buildReferenceAndListParam(subjectParam));
		categoryParam = new TokenParam("heart-rate");
		categoryParam.setModifier(TokenParamModifier.TEXT);
		searchParameterMap.add(Observation.SP_CATEGORY, buildTokenAndListParam(categoryParam));
		codeParam = new TokenParam("code1");
		codeParam.setModifier(TokenParamModifier.TEXT);
		searchParameterMap.add(Observation.SP_CODE, buildTokenAndListParam(codeParam));
		searchParameterMap.setLastNMax(100);

		observations = elasticsearchSvc.executeLastN(searchParameterMap, myFhirContext, 100);

		assertEquals(5, observations.size());

		// Check partial strings
		searchParameterMap = new SearchParameterMap();
		searchParameterMap.add(Observation.SP_SUBJECT, buildReferenceAndListParam(subjectParam));
		categoryParam = new TokenParam("hear");
		categoryParam.setModifier(TokenParamModifier.TEXT);
		searchParameterMap.add(Observation.SP_CATEGORY, buildTokenAndListParam(categoryParam));
		codeParam = new TokenParam("1-obs");
		codeParam.setModifier(TokenParamModifier.TEXT);
		searchParameterMap.add(Observation.SP_CODE, buildTokenAndListParam(codeParam));
		searchParameterMap.setLastNMax(100);

		observations = elasticsearchSvc.executeLastN(searchParameterMap, myFhirContext, 100);

		assertEquals(5, observations.size());

	}

	@Test
	public void testLastNNoMatchQueries() {

		ReferenceParam validPatientParam = new ReferenceParam("Patient", "", "9");
		TokenParam validCategoryCodeParam = new TokenParam("http://mycodes.org/fhir/observation-category", "test-heart-rate");
		TokenParam validObservationCodeParam = new TokenParam("http://mycodes.org/fhir/observation-code", "test-code-1");
		DateParam validDateParam = new DateParam(ParamPrefixEnum.EQUAL, new Date(baseObservationDate.getTimeInMillis() - (9 * 3600 * 1000)));

		// Ensure that valid parameters are indeed valid
		SearchParameterMap searchParameterMap = new SearchParameterMap();
		searchParameterMap.add(Observation.SP_PATIENT, buildReferenceAndListParam(validPatientParam));
		searchParameterMap.add(Observation.SP_CATEGORY, buildTokenAndListParam(validCategoryCodeParam));
		searchParameterMap.add(Observation.SP_CODE, buildTokenAndListParam(validObservationCodeParam));
		searchParameterMap.add(Observation.SP_DATE, validDateParam);
		searchParameterMap.setLastNMax(100);

		List<String> observations = elasticsearchSvc.executeLastN(searchParameterMap, myFhirContext, 100);
		assertEquals(1, observations.size());

		// Invalid Patient
		searchParameterMap = new SearchParameterMap();
		ReferenceParam patientParam = new ReferenceParam("Patient", "", "10");
		searchParameterMap.add(Observation.SP_PATIENT, buildReferenceAndListParam(patientParam));
		searchParameterMap.add(Observation.SP_CATEGORY, buildTokenAndListParam(validCategoryCodeParam));
		searchParameterMap.add(Observation.SP_CODE, buildTokenAndListParam(validObservationCodeParam));
		searchParameterMap.add(Observation.SP_DATE, validDateParam);
		searchParameterMap.setLastNMax(100);

		observations = elasticsearchSvc.executeLastN(searchParameterMap, myFhirContext, 100);
		assertEquals(0, observations.size());

		// Invalid subject
		searchParameterMap = new SearchParameterMap();
		searchParameterMap.add(Observation.SP_SUBJECT, buildReferenceAndListParam(patientParam));
		searchParameterMap.add(Observation.SP_CATEGORY, buildTokenAndListParam(validCategoryCodeParam));
		searchParameterMap.add(Observation.SP_CODE, buildTokenAndListParam(validObservationCodeParam));
		searchParameterMap.add(Observation.SP_DATE, validDateParam);
		searchParameterMap.setLastNMax(100);

		observations = elasticsearchSvc.executeLastN(searchParameterMap, myFhirContext, 100);
		assertEquals(0, observations.size());

		// Invalid observation code
		searchParameterMap = new SearchParameterMap();
		searchParameterMap.add(Observation.SP_SUBJECT, buildReferenceAndListParam(validPatientParam));
		searchParameterMap.add(Observation.SP_CATEGORY, buildTokenAndListParam(validCategoryCodeParam));
		TokenParam codeParam = new TokenParam("http://mycodes.org/fhir/observation-code", "test-code-999");
		searchParameterMap.add(Observation.SP_CODE, buildTokenAndListParam(codeParam));
		searchParameterMap.add(Observation.SP_DATE, validDateParam);
		searchParameterMap.setLastNMax(100);

		observations = elasticsearchSvc.executeLastN(searchParameterMap, myFhirContext, 100);
		assertEquals(0, observations.size());

		// Invalid category code
		searchParameterMap = new SearchParameterMap();
		searchParameterMap.add(Observation.SP_SUBJECT, buildReferenceAndListParam(validPatientParam));
		TokenParam categoryParam = new TokenParam("http://mycodes.org/fhir/observation-category", "test-not-a-category");
		searchParameterMap.add(Observation.SP_CATEGORY, buildTokenAndListParam(categoryParam));
		searchParameterMap.add(Observation.SP_CODE, buildTokenAndListParam(validObservationCodeParam));
		searchParameterMap.add(Observation.SP_DATE, validDateParam);
		searchParameterMap.setLastNMax(100);

		observations = elasticsearchSvc.executeLastN(searchParameterMap, myFhirContext, 100);
		assertEquals(0, observations.size());

		// Invalid date
		searchParameterMap = new SearchParameterMap();
		searchParameterMap.add(Observation.SP_SUBJECT, buildReferenceAndListParam(validPatientParam));
		searchParameterMap.add(Observation.SP_CATEGORY, buildTokenAndListParam(validCategoryCodeParam));
		searchParameterMap.add(Observation.SP_CODE, buildTokenAndListParam(validObservationCodeParam));
		searchParameterMap.add(Observation.SP_DATE, new DateParam(ParamPrefixEnum.GREATERTHAN, baseObservationDate.getTime()));
		searchParameterMap.setLastNMax(100);
		observations = elasticsearchSvc.executeLastN(searchParameterMap, myFhirContext, 100);
		assertEquals(0, observations.size());

	}

	@Test
	public void testLastNEffectiveDates() {
		Date highDate = new Date(baseObservationDate.getTimeInMillis() - (3600 * 1000));
		Date lowDate = new Date(baseObservationDate.getTimeInMillis() - (10 * 3600 * 1000));

		SearchParameterMap searchParameterMap = new SearchParameterMap();
		ReferenceParam subjectParam = new ReferenceParam("Patient", "", "3");
		searchParameterMap.add(Observation.SP_SUBJECT, buildReferenceAndListParam(subjectParam));
		DateParam dateParam = new DateParam(ParamPrefixEnum.EQUAL, lowDate);
		searchParameterMap.add(Observation.SP_DATE, dateParam);
		searchParameterMap.setLastNMax(100);
		List<String> observations = elasticsearchSvc.executeLastN(searchParameterMap, myFhirContext, 100);
		assertEquals(1, observations.size());

		searchParameterMap = new SearchParameterMap();
		searchParameterMap.add(Observation.SP_SUBJECT, buildReferenceAndListParam(subjectParam));
		dateParam = new DateParam(ParamPrefixEnum.GREATERTHAN_OR_EQUALS, lowDate);
		searchParameterMap.add(Observation.SP_DATE, dateParam);
		searchParameterMap.setLastNMax(100);
		observations = elasticsearchSvc.executeLastN(searchParameterMap, myFhirContext, 100);
		assertEquals(10, observations.size());

		searchParameterMap = new SearchParameterMap();
		searchParameterMap.add(Observation.SP_SUBJECT, buildReferenceAndListParam(subjectParam));
		dateParam = new DateParam(ParamPrefixEnum.GREATERTHAN, lowDate);
		searchParameterMap.add(Observation.SP_DATE, dateParam);
		searchParameterMap.setLastNMax(100);
		observations = elasticsearchSvc.executeLastN(searchParameterMap, myFhirContext, 100);
		assertEquals(9, observations.size());

		searchParameterMap = new SearchParameterMap();
		searchParameterMap.add(Observation.SP_SUBJECT, buildReferenceAndListParam(subjectParam));
		dateParam = new DateParam(ParamPrefixEnum.STARTS_AFTER, lowDate);
		searchParameterMap.add(Observation.SP_DATE, dateParam);
		searchParameterMap.setLastNMax(100);
		observations = elasticsearchSvc.executeLastN(searchParameterMap, myFhirContext, 100);
		assertEquals(9, observations.size());

		searchParameterMap = new SearchParameterMap();
		searchParameterMap.add(Observation.SP_SUBJECT, buildReferenceAndListParam(subjectParam));
		dateParam = new DateParam(ParamPrefixEnum.LESSTHAN_OR_EQUALS, highDate);
		searchParameterMap.add(Observation.SP_DATE, dateParam);
		searchParameterMap.setLastNMax(100);
		observations = elasticsearchSvc.executeLastN(searchParameterMap, myFhirContext, 100);
		assertEquals(10, observations.size());

		searchParameterMap = new SearchParameterMap();
		searchParameterMap.add(Observation.SP_SUBJECT, buildReferenceAndListParam(subjectParam));
		dateParam = new DateParam(ParamPrefixEnum.LESSTHAN, highDate);
		searchParameterMap.add(Observation.SP_DATE, dateParam);
		searchParameterMap.setLastNMax(100);
		observations = elasticsearchSvc.executeLastN(searchParameterMap, myFhirContext, 100);
		assertEquals(9, observations.size());

		searchParameterMap = new SearchParameterMap();
		searchParameterMap.add(Observation.SP_SUBJECT, buildReferenceAndListParam(subjectParam));
		dateParam = new DateParam(ParamPrefixEnum.ENDS_BEFORE, highDate);
		searchParameterMap.add(Observation.SP_DATE, dateParam);
		searchParameterMap.setLastNMax(100);
		observations = elasticsearchSvc.executeLastN(searchParameterMap, myFhirContext, 100);
		assertEquals(9, observations.size());

		searchParameterMap = new SearchParameterMap();
		searchParameterMap.add(Observation.SP_SUBJECT, buildReferenceAndListParam(subjectParam));
		DateParam startDateParam = new DateParam(ParamPrefixEnum.GREATERTHAN, new Date(baseObservationDate.getTimeInMillis() - (4 * 3600 * 1000)));
		DateAndListParam dateAndListParam = new DateAndListParam();
		dateAndListParam.addAnd(new DateOrListParam().addOr(startDateParam));
		dateParam = new DateParam(ParamPrefixEnum.LESSTHAN_OR_EQUALS, highDate);
		dateAndListParam.addAnd(new DateOrListParam().addOr(dateParam));
		searchParameterMap.add(Observation.SP_DATE, dateAndListParam);
		searchParameterMap.setLastNMax(100);
		observations = elasticsearchSvc.executeLastN(searchParameterMap, myFhirContext, 100);
		assertEquals(3, observations.size());

		searchParameterMap = new SearchParameterMap();
		searchParameterMap.add(Observation.SP_SUBJECT, buildReferenceAndListParam(subjectParam));
		startDateParam = new DateParam(ParamPrefixEnum.GREATERTHAN, new Date(baseObservationDate.getTimeInMillis() - (4 * 3600 * 1000)));
		searchParameterMap.add(Observation.SP_DATE, startDateParam);
		dateParam = new DateParam(ParamPrefixEnum.LESSTHAN, lowDate);
		searchParameterMap.add(Observation.SP_DATE, dateParam);
		searchParameterMap.setLastNMax(100);
		observations = elasticsearchSvc.executeLastN(searchParameterMap, myFhirContext, 100);
		assertEquals(0, observations.size());

	}

	private void createMultiplePatientsAndObservations() throws IOException {
		// Create CodeableConcepts for two Codes, each with three codings.
		String codeableConceptId1 = UUID.randomUUID().toString();
		CodeJson codeJson1 = new CodeJson();
		codeJson1.setCodeableConceptText("Test Codeable Concept Field for First Code");
		codeJson1.setCodeableConceptId(codeableConceptId1);
		codeJson1.addCoding("http://mycodes.org/fhir/observation-code", "test-code-1", "1-Observation Code1");

		String codeableConceptId2 = UUID.randomUUID().toString();
		CodeJson codeJson2 = new CodeJson();
		codeJson2.setCodeableConceptText("Test Codeable Concept Field for Second Code");
		codeJson2.setCodeableConceptId(codeableConceptId1);
		codeJson2.addCoding("http://mycodes.org/fhir/observation-code", "test-code-2", "2-Observation Code2");

		// Create CodeableConcepts for two categories, each with three codings.
		// Create three codings and first category CodeableConcept
		List<CodeJson> categoryConcepts1 = new ArrayList<>();
		CodeJson categoryCodeableConcept1 = new CodeJson();
		categoryCodeableConcept1.setCodeableConceptText("Test Codeable Concept Field for first category");
		categoryCodeableConcept1.addCoding("http://mycodes.org/fhir/observation-category", "test-heart-rate", "Test Heart Rate");
		categoryCodeableConcept1.addCoding("http://myalternatecodes.org/fhir/observation-category", "test-alt-heart-rate", "Test Heartrate");
		categoryCodeableConcept1.addCoding("http://mysecondaltcodes.org/fhir/observation-category", "test-2nd-alt-heart-rate", "Test Heart-Rate");
		categoryConcepts1.add(categoryCodeableConcept1);
		// Create three codings and second category CodeableConcept
		List<CodeJson> categoryConcepts2 = new ArrayList<>();
		CodeJson categoryCodeableConcept2 = new CodeJson();
		categoryCodeableConcept2.setCodeableConceptText("Test Codeable Concept Field for second category");
		categoryCodeableConcept2.addCoding("http://mycodes.org/fhir/observation-category", "test-vital-signs", "Test Vital Signs");
		categoryCodeableConcept2.addCoding("http://myalternatecodes.org/fhir/observation-category", "test-alt-vitals", "Test Vital-Signs");
		categoryCodeableConcept2.addCoding("http://mysecondaltcodes.org/fhir/observation-category", "test-2nd-alt-vitals", "Test Vitals");
		categoryConcepts2.add(categoryCodeableConcept2);

		for (int patientCount = 0; patientCount < 10; patientCount++) {

			String subject = String.valueOf(patientCount);

			for (int entryCount = 0; entryCount < 10; entryCount++) {

				ObservationJson observationJson = new ObservationJson();
				String identifier = String.valueOf((entryCount + patientCount * 10L));
				observationJson.setIdentifier(identifier);
				observationJson.setSubject(subject);

				if (entryCount % 2 == 1) {
					observationJson.setCategories(categoryConcepts1);
					observationJson.setCode(codeJson1);
					observationJson.setCode_concept_id(codeableConceptId1);
					assertTrue(elasticsearchSvc.createOrUpdateObservationCodeIndex(codeableConceptId1, codeJson1));
				} else {
					observationJson.setCategories(categoryConcepts2);
					observationJson.setCode(codeJson2);
					observationJson.setCode_concept_id(codeableConceptId2);
					assertTrue(elasticsearchSvc.createOrUpdateObservationCodeIndex(codeableConceptId2, codeJson2));
				}

				Date effectiveDtm = new Date(baseObservationDate.getTimeInMillis() - ((10L - entryCount) * 3600L * 1000L));
				observationJson.setEffectiveDtm(effectiveDtm);

				assertTrue(elasticsearchSvc.createOrUpdateObservationIndex(identifier, observationJson));

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

		elasticsearchSvc.refreshIndex(ElasticsearchSvcImpl.OBSERVATION_INDEX);
		elasticsearchSvc.refreshIndex(ElasticsearchSvcImpl.OBSERVATION_CODE_INDEX);

	}

	@Test
	public void testLastNNoParamsQuery() {
		SearchParameterMap searchParameterMap = new SearchParameterMap();
		searchParameterMap.setLastNMax(1);
		List<ObservationJson> observations = elasticsearchSvc.executeLastNWithAllFieldsForTest(searchParameterMap, myFhirContext);

		assertEquals(2, observations.size());

		String observationCode1 = observations.get(0).getCode_coding_code_system_hash();
		String observationCode2 = observations.get(1).getCode_coding_code_system_hash();

		assertNotEquals(observationCode1, observationCode2);

	}

	@BeforeAll
	public static void beforeClass() {
		ourMapperNonPrettyPrint = new ObjectMapper();
		ourMapperNonPrettyPrint.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		ourMapperNonPrettyPrint.disable(SerializationFeature.INDENT_OUTPUT);
		ourMapperNonPrettyPrint.disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET);
	}


}
