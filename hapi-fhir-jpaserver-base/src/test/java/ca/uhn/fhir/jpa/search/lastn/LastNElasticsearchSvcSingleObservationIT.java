package ca.uhn.fhir.jpa.search.lastn;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.util.CodeSystemHash;
import ca.uhn.fhir.jpa.search.lastn.config.TestElasticsearchContainerHelper;
import ca.uhn.fhir.jpa.search.lastn.json.CodeJson;
import ca.uhn.fhir.jpa.search.lastn.json.ObservationJson;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.dstu2.resource.Observation;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import ca.uhn.fhir.rest.param.ReferenceAndListParam;
import ca.uhn.fhir.rest.param.ReferenceOrListParam;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.TokenAndListParam;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static java.time.temporal.ChronoUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@Testcontainers
public class LastNElasticsearchSvcSingleObservationIT {

	static ObjectMapper ourMapperNonPrettyPrint;
	final String RESOURCEPID = "123";
	final String SUBJECTID = "Patient/4567";
	final Date EFFECTIVEDTM = new Date();
	final String FIRSTCATEGORYTEXT = "Test Codeable Concept Field for first category";
	final String CATEGORYFIRSTCODINGSYSTEM = "http://mycodes.org/fhir/observation-category";
	final String CATEGORYSECONDCODINGSYSTEM = "http://myalternatecodes.org/fhir/observation-category";
	final String CATEGORYTHIRDCODINGSYSTEM = "http://mysecondaltcodes.org/fhir/observation-category";
	final String FIRSTCATEGORYFIRSTCODINGCODE = "test-heart-rate";
	final String FIRSTCATEGORYFIRSTCODINGDISPLAY = "test-heart-rate display";
	final String FIRSTCATEGORYSECONDCODINGCODE = "test-alt-heart-rate";
	final String FIRSTCATEGORYSECONDCODINGDISPLAY = "test-alt-heart-rate display";
	final String FIRSTCATEGORYTHIRDCODINGCODE = "test-2nd-alt-heart-rate";
	final String FIRSTCATEGORYTHIRDCODINGDISPLAY = "test-2nd-alt-heart-rate display";
	final String SECONDCATEGORYTEXT = "Test Codeable Concept Field for for second category";
	final String SECONDCATEGORYFIRSTCODINGCODE = "test-vital-signs";
	final String SECONDCATEGORYFIRSTCODINGDISPLAY = "test-vital-signs display";
	final String SECONDCATEGORYSECONDCODINGCODE = "test-alt-vitals";
	final String SECONDCATEGORYSECONDCODINGDISPLAY = "test-alt-vitals display";
	final String SECONDCATEGORYTHIRDCODINGCODE = "test-2nd-alt-vitals";
	final String SECONDCATEGORYTHIRDCODINGDISPLAY = "test-2nd-alt-vitals display";
	final String THIRDCATEGORYTEXT = "Test Codeable Concept Field for third category";
	final String THIRDCATEGORYFIRSTCODINGCODE = "test-vital-panel";
	final String THIRDCATEGORYFIRSTCODINGDISPLAY = "test-vitals-panel display";
	final String THIRDCATEGORYSECONDCODINGCODE = "test-alt-vitals-panel";
	final String THIRDCATEGORYSECONDCODINGDISPLAY = "test-alt-vitals display";
	final String THIRDCATEGORYTHIRDCODINGCODE = "test-2nd-alt-vitals-panel";
	final String THIRDCATEGORYTHIRDCODINGDISPLAY = "test-2nd-alt-vitals-panel display";
	final String OBSERVATIONSINGLECODEID = UUID.randomUUID().toString();
	final String OBSERVATIONCODETEXT = "Test Codeable Concept Field for Code";
	final String CODEFIRSTCODINGSYSTEM = "http://mycodes.org/fhir/observation-code";
	final String CODEFIRSTCODINGCODE = "test-code";
	final String CODEFIRSTCODINGDISPLAY = "test-code display";
	final FhirContext myFhirContext = FhirContext.forCached(FhirVersionEnum.R4);

	ElasticsearchSvcImpl elasticsearchSvc;

	@Container
	public static ElasticsearchContainer elasticsearchContainer = TestElasticsearchContainerHelper.getEmbeddedElasticSearch();


	@BeforeEach
	public void before() {
		PartitionSettings partitionSettings = new PartitionSettings();
		partitionSettings.setPartitioningEnabled(false);
		elasticsearchSvc = new ElasticsearchSvcImpl(partitionSettings, elasticsearchContainer.getHost(), elasticsearchContainer.getMappedPort(9200), "", "");
	}

	@AfterEach
	public void after() throws IOException {
		elasticsearchSvc.deleteAllDocumentsForTest(ElasticsearchSvcImpl.OBSERVATION_INDEX);
		elasticsearchSvc.deleteAllDocumentsForTest(ElasticsearchSvcImpl.OBSERVATION_CODE_INDEX);
		elasticsearchSvc.refreshIndex(ElasticsearchSvcImpl.OBSERVATION_INDEX);
		elasticsearchSvc.refreshIndex(ElasticsearchSvcImpl.OBSERVATION_CODE_INDEX);
	}

	@Test
	public void testSingleObservationQuery() throws IOException {

		createSingleObservation();

		SearchParameterMap searchParameterMap = new SearchParameterMap();
		ReferenceParam subjectParam = new ReferenceParam("Patient", "", SUBJECTID);
		searchParameterMap.add(Observation.SP_SUBJECT, new ReferenceAndListParam().addAnd(new ReferenceOrListParam().addOr(subjectParam)));
		TokenParam categoryParam = new TokenParam(CATEGORYFIRSTCODINGSYSTEM, FIRSTCATEGORYFIRSTCODINGCODE);
		searchParameterMap.add(Observation.SP_CATEGORY, new TokenAndListParam().addAnd(new TokenOrListParam().addOr(categoryParam)));
		TokenParam codeParam = new TokenParam(CODEFIRSTCODINGSYSTEM, CODEFIRSTCODINGCODE);
		searchParameterMap.add(Observation.SP_CODE, new TokenAndListParam().addAnd(new TokenOrListParam().addOr(codeParam)));
		searchParameterMap.add(Observation.SP_DATE, new DateParam(ParamPrefixEnum.EQUAL, EFFECTIVEDTM));

		searchParameterMap.setLastNMax(3);

		// execute Observation ID search
		List<String> observationIdsOnly = elasticsearchSvc.executeLastN(searchParameterMap, myFhirContext, 100);

		assertEquals(1, observationIdsOnly.size());
		assertEquals(RESOURCEPID, observationIdsOnly.get(0));

		// execute Observation search for all search fields
		List<ObservationJson> observations = elasticsearchSvc.executeLastNWithAllFieldsForTest(searchParameterMap, myFhirContext);

		validateFullObservationSearch(observations);
	}

	private void validateFullObservationSearch(List<ObservationJson> observations) throws IOException {

		assertEquals(1, observations.size());
		ObservationJson observation = observations.get(0);
		assertEquals(RESOURCEPID, observation.getIdentifier());

		assertEquals(SUBJECTID, observation.getSubject());
		assertEquals(RESOURCEPID, observation.getIdentifier());
		assertEquals(EFFECTIVEDTM, observation.getEffectiveDtm());
		assertEquals(OBSERVATIONSINGLECODEID, observation.getCode_concept_id());

		List<String> category_concept_text_values = observation.getCategory_concept_text();
		assertEquals(3, category_concept_text_values.size());
		assertEquals(FIRSTCATEGORYTEXT, category_concept_text_values.get(0));
		assertEquals(SECONDCATEGORYTEXT, category_concept_text_values.get(1));
		assertEquals(THIRDCATEGORYTEXT, category_concept_text_values.get(2));

		List<List<String>> category_codings_systems = observation.getCategory_coding_system();
		assertEquals(3, category_codings_systems.size());
		List<String> category_coding_systems = category_codings_systems.get(0);
		assertEquals(3, category_coding_systems.size());
		assertEquals(CATEGORYFIRSTCODINGSYSTEM, category_coding_systems.get(0));
		assertEquals(CATEGORYSECONDCODINGSYSTEM, category_coding_systems.get(1));
		assertEquals(CATEGORYTHIRDCODINGSYSTEM, category_coding_systems.get(2));
		category_coding_systems = category_codings_systems.get(1);
		assertEquals(3, category_coding_systems.size());
		assertEquals(CATEGORYFIRSTCODINGSYSTEM, category_coding_systems.get(0));
		assertEquals(CATEGORYSECONDCODINGSYSTEM, category_coding_systems.get(1));
		assertEquals(CATEGORYTHIRDCODINGSYSTEM, category_coding_systems.get(2));
		category_coding_systems = category_codings_systems.get(2);
		assertEquals(3, category_coding_systems.size());
		assertEquals(CATEGORYFIRSTCODINGSYSTEM, category_coding_systems.get(0));
		assertEquals(CATEGORYSECONDCODINGSYSTEM, category_coding_systems.get(1));
		assertEquals(CATEGORYTHIRDCODINGSYSTEM, category_coding_systems.get(2));

		List<List<String>> category_codings_codes = observation.getCategory_coding_code();
		assertEquals(3, category_codings_codes.size());
		List<String> category_coding_codes = category_codings_codes.get(0);
		assertEquals(3, category_coding_codes.size());
		assertEquals(FIRSTCATEGORYFIRSTCODINGCODE, category_coding_codes.get(0));
		assertEquals(FIRSTCATEGORYSECONDCODINGCODE, category_coding_codes.get(1));
		assertEquals(FIRSTCATEGORYTHIRDCODINGCODE, category_coding_codes.get(2));
		category_coding_codes = category_codings_codes.get(1);
		assertEquals(3, category_coding_codes.size());
		assertEquals(SECONDCATEGORYFIRSTCODINGCODE, category_coding_codes.get(0));
		assertEquals(SECONDCATEGORYSECONDCODINGCODE, category_coding_codes.get(1));
		assertEquals(SECONDCATEGORYTHIRDCODINGCODE, category_coding_codes.get(2));
		category_coding_codes = category_codings_codes.get(2);
		assertEquals(3, category_coding_codes.size());
		assertEquals(THIRDCATEGORYFIRSTCODINGCODE, category_coding_codes.get(0));
		assertEquals(THIRDCATEGORYSECONDCODINGCODE, category_coding_codes.get(1));
		assertEquals(THIRDCATEGORYTHIRDCODINGCODE, category_coding_codes.get(2));

		List<List<String>> category_codings_displays = observation.getCategory_coding_display();
		assertEquals(3, category_codings_displays.size());
		List<String> category_coding_displays = category_codings_displays.get(0);
		assertEquals(FIRSTCATEGORYFIRSTCODINGDISPLAY, category_coding_displays.get(0));
		assertEquals(FIRSTCATEGORYSECONDCODINGDISPLAY, category_coding_displays.get(1));
		assertEquals(FIRSTCATEGORYTHIRDCODINGDISPLAY, category_coding_displays.get(2));
		category_coding_displays = category_codings_displays.get(1);
		assertEquals(3, category_coding_displays.size());
		assertEquals(SECONDCATEGORYFIRSTCODINGDISPLAY, category_coding_displays.get(0));
		assertEquals(SECONDCATEGORYSECONDCODINGDISPLAY, category_coding_displays.get(1));
		assertEquals(SECONDCATEGORYTHIRDCODINGDISPLAY, category_coding_displays.get(2));
		category_coding_displays = category_codings_displays.get(2);
		assertEquals(3, category_coding_displays.size());
		assertEquals(THIRDCATEGORYFIRSTCODINGDISPLAY, category_coding_displays.get(0));
		assertEquals(THIRDCATEGORYSECONDCODINGDISPLAY, category_coding_displays.get(1));
		assertEquals(THIRDCATEGORYTHIRDCODINGDISPLAY, category_coding_displays.get(2));

		List<List<String>> category_codings_code_system_hashes = observation.getCategory_coding_code_system_hash();
		assertEquals(3, category_codings_code_system_hashes.size());
		List<String> category_coding_code_system_hashes = category_codings_code_system_hashes.get(0);
		assertEquals(String.valueOf(CodeSystemHash.hashCodeSystem(CATEGORYFIRSTCODINGSYSTEM, FIRSTCATEGORYFIRSTCODINGCODE)), category_coding_code_system_hashes.get(0));
		assertEquals(String.valueOf(CodeSystemHash.hashCodeSystem(CATEGORYSECONDCODINGSYSTEM, FIRSTCATEGORYSECONDCODINGCODE)), category_coding_code_system_hashes.get(1));
		assertEquals(String.valueOf(CodeSystemHash.hashCodeSystem(CATEGORYTHIRDCODINGSYSTEM, FIRSTCATEGORYTHIRDCODINGCODE)), category_coding_code_system_hashes.get(2));
		category_coding_code_system_hashes = category_codings_code_system_hashes.get(1);
		assertEquals(3, category_coding_code_system_hashes.size());
		assertEquals(String.valueOf(CodeSystemHash.hashCodeSystem(CATEGORYFIRSTCODINGSYSTEM, SECONDCATEGORYFIRSTCODINGCODE)), category_coding_code_system_hashes.get(0));
		assertEquals(String.valueOf(CodeSystemHash.hashCodeSystem(CATEGORYSECONDCODINGSYSTEM, SECONDCATEGORYSECONDCODINGCODE)), category_coding_code_system_hashes.get(1));
		assertEquals(String.valueOf(CodeSystemHash.hashCodeSystem(CATEGORYTHIRDCODINGSYSTEM, SECONDCATEGORYTHIRDCODINGCODE)), category_coding_code_system_hashes.get(2));
		category_coding_code_system_hashes = category_codings_code_system_hashes.get(2);
		assertEquals(3, category_coding_code_system_hashes.size());
		assertEquals(String.valueOf(CodeSystemHash.hashCodeSystem(CATEGORYFIRSTCODINGSYSTEM, THIRDCATEGORYFIRSTCODINGCODE)), category_coding_code_system_hashes.get(0));
		assertEquals(String.valueOf(CodeSystemHash.hashCodeSystem(CATEGORYSECONDCODINGSYSTEM, THIRDCATEGORYSECONDCODINGCODE)), category_coding_code_system_hashes.get(1));
		assertEquals(String.valueOf(CodeSystemHash.hashCodeSystem(CATEGORYTHIRDCODINGSYSTEM, THIRDCATEGORYTHIRDCODINGCODE)), category_coding_code_system_hashes.get(2));

		String code_concept_text_values = observation.getCode_concept_text();
		assertEquals(OBSERVATIONCODETEXT, code_concept_text_values);

		String code_coding_systems = observation.getCode_coding_system();
		assertEquals(CODEFIRSTCODINGSYSTEM, code_coding_systems);

		String code_coding_codes = observation.getCode_coding_code();
		assertEquals(CODEFIRSTCODINGCODE, code_coding_codes);

		String code_coding_display = observation.getCode_coding_display();
		assertEquals(CODEFIRSTCODINGDISPLAY, code_coding_display);

		String code_coding_code_system_hash = observation.getCode_coding_code_system_hash();
		assertEquals(String.valueOf(CodeSystemHash.hashCodeSystem(CODEFIRSTCODINGSYSTEM, CODEFIRSTCODINGCODE)), code_coding_code_system_hash);

		// Retrieve all Observation codes
		List<CodeJson> codes = elasticsearchSvc.queryAllIndexedObservationCodesForTest();
		assertEquals(1, codes.size());
		CodeJson persistedObservationCode = codes.get(0);

		String persistedCodeConceptID = persistedObservationCode.getCodeableConceptId();
		assertEquals(OBSERVATIONSINGLECODEID, persistedCodeConceptID);
		String persistedCodeConceptText = persistedObservationCode.getCodeableConceptText();
		assertEquals(OBSERVATIONCODETEXT, persistedCodeConceptText);

		List<String> persistedCodeCodingSystems = persistedObservationCode.getCoding_system();
		assertEquals(1, persistedCodeCodingSystems.size());
		assertEquals(CODEFIRSTCODINGSYSTEM, persistedCodeCodingSystems.get(0));

		List<String> persistedCodeCodingCodes = persistedObservationCode.getCoding_code();
		assertEquals(1, persistedCodeCodingCodes.size());
		assertEquals(CODEFIRSTCODINGCODE, persistedCodeCodingCodes.get(0));

		List<String> persistedCodeCodingDisplays = persistedObservationCode.getCoding_display();
		assertEquals(1, persistedCodeCodingDisplays.size());
		assertEquals(CODEFIRSTCODINGDISPLAY, persistedCodeCodingDisplays.get(0));

		List<String> persistedCodeCodingCodeSystemHashes = persistedObservationCode.getCoding_code_system_hash();
		assertEquals(1, persistedCodeCodingCodeSystemHashes.size());
		assertEquals(String.valueOf(CodeSystemHash.hashCodeSystem(CODEFIRSTCODINGSYSTEM, CODEFIRSTCODINGCODE)), persistedCodeCodingCodeSystemHashes.get(0));


	}

	private void createSingleObservation() throws IOException {
		ObservationJson indexedObservation = new ObservationJson();
		indexedObservation.setIdentifier(RESOURCEPID);
		indexedObservation.setSubject(SUBJECTID);
		indexedObservation.setEffectiveDtm(EFFECTIVEDTM);

		// Add three CodeableConcepts for category
		List<CodeJson> categoryConcepts = new ArrayList<>();
		// Create three codings and first category CodeableConcept
		CodeJson categoryCodeableConcept1 = new CodeJson();
		categoryCodeableConcept1.setCodeableConceptText(FIRSTCATEGORYTEXT);
		categoryCodeableConcept1.addCoding(CATEGORYFIRSTCODINGSYSTEM, FIRSTCATEGORYFIRSTCODINGCODE, FIRSTCATEGORYFIRSTCODINGDISPLAY);
		categoryCodeableConcept1.addCoding(CATEGORYSECONDCODINGSYSTEM, FIRSTCATEGORYSECONDCODINGCODE, FIRSTCATEGORYSECONDCODINGDISPLAY);
		categoryCodeableConcept1.addCoding(CATEGORYTHIRDCODINGSYSTEM, FIRSTCATEGORYTHIRDCODINGCODE, FIRSTCATEGORYTHIRDCODINGDISPLAY);
		categoryConcepts.add(categoryCodeableConcept1);
		// Create three codings and second category CodeableConcept
		CodeJson categoryCodeableConcept2 = new CodeJson();
		categoryCodeableConcept2.setCodeableConceptText(SECONDCATEGORYTEXT);
		categoryCodeableConcept2.addCoding(CATEGORYFIRSTCODINGSYSTEM, SECONDCATEGORYFIRSTCODINGCODE, SECONDCATEGORYFIRSTCODINGDISPLAY);
		categoryCodeableConcept2.addCoding(CATEGORYSECONDCODINGSYSTEM, SECONDCATEGORYSECONDCODINGCODE, SECONDCATEGORYSECONDCODINGDISPLAY);
		categoryCodeableConcept2.addCoding(CATEGORYTHIRDCODINGSYSTEM, SECONDCATEGORYTHIRDCODINGCODE, SECONDCATEGORYTHIRDCODINGDISPLAY);

		categoryConcepts.add(categoryCodeableConcept2);
		// Create three codings and third category CodeableConcept
		CodeJson categoryCodeableConcept3 = new CodeJson();
		categoryCodeableConcept3.setCodeableConceptText(THIRDCATEGORYTEXT);
		categoryCodeableConcept3.addCoding(CATEGORYFIRSTCODINGSYSTEM, THIRDCATEGORYFIRSTCODINGCODE, THIRDCATEGORYFIRSTCODINGDISPLAY);
		categoryCodeableConcept3.addCoding(CATEGORYSECONDCODINGSYSTEM, THIRDCATEGORYSECONDCODINGCODE, THIRDCATEGORYSECONDCODINGDISPLAY);
		categoryCodeableConcept3.addCoding(CATEGORYTHIRDCODINGSYSTEM, THIRDCATEGORYTHIRDCODINGCODE, THIRDCATEGORYTHIRDCODINGDISPLAY);
		categoryConcepts.add(categoryCodeableConcept3);
		indexedObservation.setCategories(categoryConcepts);

		// Create CodeableConcept for Code with three codings.
		indexedObservation.setCode_concept_id(OBSERVATIONSINGLECODEID);
		CodeJson codeableConceptField = new CodeJson();
		codeableConceptField.setCodeableConceptText(OBSERVATIONCODETEXT);
		codeableConceptField.addCoding(CODEFIRSTCODINGSYSTEM, CODEFIRSTCODINGCODE, CODEFIRSTCODINGDISPLAY);
		indexedObservation.setCode(codeableConceptField);

		assertTrue(elasticsearchSvc.createOrUpdateObservationIndex(RESOURCEPID, indexedObservation));

		codeableConceptField.setCodeableConceptId(OBSERVATIONSINGLECODEID);
		assertTrue(elasticsearchSvc.createOrUpdateObservationCodeIndex(OBSERVATIONSINGLECODEID, codeableConceptField));

		elasticsearchSvc.refreshIndex(ElasticsearchSvcImpl.OBSERVATION_INDEX);
		elasticsearchSvc.refreshIndex(ElasticsearchSvcImpl.OBSERVATION_CODE_INDEX);
	}

	@BeforeAll
	public static void beforeClass() {
		ourMapperNonPrettyPrint = new ObjectMapper();
		ourMapperNonPrettyPrint.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		ourMapperNonPrettyPrint.disable(SerializationFeature.INDENT_OUTPUT);
		ourMapperNonPrettyPrint.disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET);

	}

}
