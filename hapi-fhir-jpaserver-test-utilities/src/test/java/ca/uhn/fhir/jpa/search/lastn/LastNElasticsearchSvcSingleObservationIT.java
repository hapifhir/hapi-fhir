package ca.uhn.fhir.jpa.search.lastn;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.util.CodeSystemHash;
import ca.uhn.fhir.jpa.search.elastic.TestElasticsearchContainerHelper;
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
import ca.uhn.fhir.test.utilities.docker.RequiresDocker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import static ca.uhn.fhir.jpa.search.lastn.LastNTestDataGenerator.CATEGORYFIRSTCODINGSYSTEM;
import static ca.uhn.fhir.jpa.search.lastn.LastNTestDataGenerator.CATEGORYSECONDCODINGSYSTEM;
import static ca.uhn.fhir.jpa.search.lastn.LastNTestDataGenerator.CATEGORYTHIRDCODINGSYSTEM;
import static ca.uhn.fhir.jpa.search.lastn.LastNTestDataGenerator.CODEFIRSTCODINGCODE;
import static ca.uhn.fhir.jpa.search.lastn.LastNTestDataGenerator.CODEFIRSTCODINGDISPLAY;
import static ca.uhn.fhir.jpa.search.lastn.LastNTestDataGenerator.CODEFIRSTCODINGSYSTEM;
import static ca.uhn.fhir.jpa.search.lastn.LastNTestDataGenerator.FIRSTCATEGORYFIRSTCODINGCODE;
import static ca.uhn.fhir.jpa.search.lastn.LastNTestDataGenerator.FIRSTCATEGORYFIRSTCODINGDISPLAY;
import static ca.uhn.fhir.jpa.search.lastn.LastNTestDataGenerator.FIRSTCATEGORYSECONDCODINGCODE;
import static ca.uhn.fhir.jpa.search.lastn.LastNTestDataGenerator.FIRSTCATEGORYSECONDCODINGDISPLAY;
import static ca.uhn.fhir.jpa.search.lastn.LastNTestDataGenerator.FIRSTCATEGORYTEXT;
import static ca.uhn.fhir.jpa.search.lastn.LastNTestDataGenerator.FIRSTCATEGORYTHIRDCODINGCODE;
import static ca.uhn.fhir.jpa.search.lastn.LastNTestDataGenerator.FIRSTCATEGORYTHIRDCODINGDISPLAY;
import static ca.uhn.fhir.jpa.search.lastn.LastNTestDataGenerator.OBSERVATIONSINGLECODEID;
import static ca.uhn.fhir.jpa.search.lastn.LastNTestDataGenerator.OBSERVATION_CODE_CONCEPT_TEXT_1;
import static ca.uhn.fhir.jpa.search.lastn.LastNTestDataGenerator.SECONDCATEGORYFIRSTCODINGCODE;
import static ca.uhn.fhir.jpa.search.lastn.LastNTestDataGenerator.SECONDCATEGORYFIRSTCODINGDISPLAY;
import static ca.uhn.fhir.jpa.search.lastn.LastNTestDataGenerator.SECONDCATEGORYSECONDCODINGCODE;
import static ca.uhn.fhir.jpa.search.lastn.LastNTestDataGenerator.SECONDCATEGORYSECONDCODINGDISPLAY;
import static ca.uhn.fhir.jpa.search.lastn.LastNTestDataGenerator.SECONDCATEGORYTEXT;
import static ca.uhn.fhir.jpa.search.lastn.LastNTestDataGenerator.SECONDCATEGORYTHIRDCODINGCODE;
import static ca.uhn.fhir.jpa.search.lastn.LastNTestDataGenerator.SECONDCATEGORYTHIRDCODINGDISPLAY;
import static ca.uhn.fhir.jpa.search.lastn.LastNTestDataGenerator.SINGLE_OBSERVATION_RESOURCE_PID;
import static ca.uhn.fhir.jpa.search.lastn.LastNTestDataGenerator.SINGLE_OBSERVATION_SUBJECT_ID;
import static ca.uhn.fhir.jpa.search.lastn.LastNTestDataGenerator.TEST_BASELINE_TIMESTAMP;
import static ca.uhn.fhir.jpa.search.lastn.LastNTestDataGenerator.THIRDCATEGORYFIRSTCODINGCODE;
import static ca.uhn.fhir.jpa.search.lastn.LastNTestDataGenerator.THIRDCATEGORYFIRSTCODINGDISPLAY;
import static ca.uhn.fhir.jpa.search.lastn.LastNTestDataGenerator.THIRDCATEGORYSECONDCODINGCODE;
import static ca.uhn.fhir.jpa.search.lastn.LastNTestDataGenerator.THIRDCATEGORYSECONDCODINGDISPLAY;
import static ca.uhn.fhir.jpa.search.lastn.LastNTestDataGenerator.THIRDCATEGORYTEXT;
import static ca.uhn.fhir.jpa.search.lastn.LastNTestDataGenerator.THIRDCATEGORYTHIRDCODINGCODE;
import static ca.uhn.fhir.jpa.search.lastn.LastNTestDataGenerator.THIRDCATEGORYTHIRDCODINGDISPLAY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@RequiresDocker
@Testcontainers
public class LastNElasticsearchSvcSingleObservationIT {

	private final FhirContext myFhirContext = FhirContext.forR4Cached();

	ElasticsearchSvcImpl elasticsearchSvc;

	@Container
	public static ElasticsearchContainer elasticsearchContainer = TestElasticsearchContainerHelper.getEmbeddedElasticSearch();


	@BeforeEach
	public void before() {
		PartitionSettings partitionSettings = new PartitionSettings();
		partitionSettings.setPartitioningEnabled(false);
		elasticsearchSvc = new ElasticsearchSvcImpl(partitionSettings, "http", elasticsearchContainer.getHost() + ":" + elasticsearchContainer.getMappedPort(9200), "", "");
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

		// Create test observation
		ObservationJson indexedObservation = LastNTestDataGenerator.createSingleObservationJson();
		assertTrue(elasticsearchSvc.createOrUpdateObservationIndex(SINGLE_OBSERVATION_RESOURCE_PID, indexedObservation));
		assertTrue(elasticsearchSvc.createOrUpdateObservationCodeIndex(OBSERVATIONSINGLECODEID, indexedObservation.getCode()));

		elasticsearchSvc.refreshIndex(ElasticsearchSvcImpl.OBSERVATION_INDEX);
		elasticsearchSvc.refreshIndex(ElasticsearchSvcImpl.OBSERVATION_CODE_INDEX);

		SearchParameterMap searchParameterMap = new SearchParameterMap();
		ReferenceParam subjectParam = new ReferenceParam("Patient", "", SINGLE_OBSERVATION_SUBJECT_ID);
		searchParameterMap.add(Observation.SP_SUBJECT, new ReferenceAndListParam().addAnd(new ReferenceOrListParam().addOr(subjectParam)));
		TokenParam categoryParam = new TokenParam(CATEGORYFIRSTCODINGSYSTEM, FIRSTCATEGORYFIRSTCODINGCODE);
		searchParameterMap.add(Observation.SP_CATEGORY, new TokenAndListParam().addAnd(new TokenOrListParam().addOr(categoryParam)));
		TokenParam codeParam = new TokenParam(CODEFIRSTCODINGSYSTEM, CODEFIRSTCODINGCODE);
		searchParameterMap.add(Observation.SP_CODE, new TokenAndListParam().addAnd(new TokenOrListParam().addOr(codeParam)));
		searchParameterMap.add(Observation.SP_DATE, new DateParam(ParamPrefixEnum.EQUAL, new Date(TEST_BASELINE_TIMESTAMP)));

		searchParameterMap.setLastNMax(3);

		// execute Observation ID search
		List<String> observationIdsOnly = elasticsearchSvc.executeLastN(searchParameterMap, myFhirContext, 100);

		assertEquals(1, observationIdsOnly.size());
		assertEquals(SINGLE_OBSERVATION_RESOURCE_PID, observationIdsOnly.get(0));

		// execute Observation search for all search fields
		List<ObservationJson> observations = elasticsearchSvc.executeLastNWithAllFieldsForTest(searchParameterMap, myFhirContext);

		validateFullObservationSearch(observations);
	}

	private void validateFullObservationSearch(List<ObservationJson> observations) throws IOException {

		assertEquals(1, observations.size());
		ObservationJson observation = observations.get(0);
		assertEquals(SINGLE_OBSERVATION_RESOURCE_PID, observation.getIdentifier());

		assertEquals(SINGLE_OBSERVATION_SUBJECT_ID, observation.getSubject());
		assertEquals(SINGLE_OBSERVATION_RESOURCE_PID, observation.getIdentifier());
		assertEquals(new Date(TEST_BASELINE_TIMESTAMP), observation.getEffectiveDtm());
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
		assertEquals(OBSERVATION_CODE_CONCEPT_TEXT_1, code_concept_text_values);

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
		assertEquals(OBSERVATION_CODE_CONCEPT_TEXT_1, persistedCodeConceptText);

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

}

