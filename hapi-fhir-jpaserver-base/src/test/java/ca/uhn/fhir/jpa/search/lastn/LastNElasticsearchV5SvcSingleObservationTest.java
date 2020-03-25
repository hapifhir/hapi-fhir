package ca.uhn.fhir.jpa.search.lastn;

import ca.uhn.fhir.jpa.search.lastn.config.TestElasticsearchV5Config;
import ca.uhn.fhir.jpa.search.lastn.json.CodeJson;
import ca.uhn.fhir.jpa.search.lastn.json.ObservationJson;
import ca.uhn.fhir.jpa.search.lastn.util.CodeSystemHash;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.TokenParam;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestElasticsearchV5Config.class } )
public class LastNElasticsearchV5SvcSingleObservationTest {

    @Autowired
    ElasticsearchV5SvcImpl elasticsearchSvc;

    static ObjectMapper ourMapperNonPrettyPrint;

    final String RESOURCEPID = "123";
    final String SUBJECTID = "4567";
    final String SUBJECTTYPEANDID = "Patient/4567";
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
    final String CODESECONDCODINGSYSTEM = "http://myalternatecodes.org/fhir/observation-code";
    final String CODESECONDCODINGCODE = "test-alt-code";
    final String CODESECONDCODINGDISPLAY = "test-alt-code display";
    final String CODETHIRDCODINGSYSTEM = "http://mysecondaltcodes.org/fhir/observation-code";
    final String CODETHIRDCODINGCODE = "test-second-alt-code";
    final String CODETHIRDCODINGDISPLAY = "test-second-alt-code display";

    @BeforeClass
    public static void beforeClass() {
        ourMapperNonPrettyPrint = new ObjectMapper();
        ourMapperNonPrettyPrint.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        ourMapperNonPrettyPrint.disable(SerializationFeature.INDENT_OUTPUT);
        ourMapperNonPrettyPrint.disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET);

    }

    //    @Before
    public void before() throws IOException {
        elasticsearchSvc.deleteAllDocuments(IndexConstants.OBSERVATION_INDEX);
        elasticsearchSvc.deleteAllDocuments(IndexConstants.CODE_INDEX);
    }

    @After
    public void after() throws IOException {
        elasticsearchSvc.deleteAllDocuments(IndexConstants.OBSERVATION_INDEX);
        elasticsearchSvc.deleteAllDocuments(IndexConstants.CODE_INDEX);
    }
    @Test
    public void testSingleObservationQuery() throws IOException {

        createSingleObservation();

        SearchParameterMap searchParameterMap = new SearchParameterMap();
        ReferenceParam subjectParam = new ReferenceParam("Patient", "", SUBJECTID);
        searchParameterMap.add("subject", subjectParam);
        TokenParam categoryParam = new TokenParam(CATEGORYFIRSTCODINGSYSTEM, FIRSTCATEGORYFIRSTCODINGCODE);
        searchParameterMap.add("category", categoryParam);
        TokenParam codeParam = new TokenParam(CODEFIRSTCODINGSYSTEM, CODEFIRSTCODINGCODE);
        searchParameterMap.add("code", codeParam);

        // execute Observation ID search - Terms Aggregation
/*        SearchRequest searchRequestIdsOnly = elasticsearchSvc.buildObservationTermsSearchRequest(1000, searchParameterMap, 3);
        SearchResponse responseIds = elasticsearchSvc.executeSearchRequest(searchRequestIdsOnly);
        List<ObservationJson> observationIdsOnly = elasticsearchSvc.buildObservationTermsResults(responseIds);

        assertEquals(1, observationIdsOnly.size());
        ObservationJson observationIdOnly = observationIdsOnly.get(0);
        assertEquals(RESOURCEPID, observationIdOnly.getIdentifier());

        // execute full Observation search - Terms Aggregation
        SearchRequest searchRequestAllFields = elasticsearchSvc.buildObservationAllFieldsTermsSearchRequest(1000, searchParameterMap, 3);
        SearchResponse response = elasticsearchSvc.executeSearchRequest(searchRequestAllFields);
        List<ObservationJson> observations = elasticsearchSvc.buildObservationTermsResults(response);

        validateFullObservationSearch(observations);
*/
    }

    private void validateFullObservationSearch(List<ObservationJson> observations) throws IOException {

        assertEquals(1, observations.size());
        ObservationJson observation = observations.get(0);
        assertEquals(RESOURCEPID, observation.getIdentifier());

        assertEquals(SUBJECTTYPEANDID, observation.getSubject());
        assertEquals(RESOURCEPID, observation.getIdentifier());
        assertEquals(EFFECTIVEDTM, observation.getEffectiveDtm());
        assertEquals(OBSERVATIONSINGLECODEID, observation.getCode_concept_id());

        List<String> category_concept_text_values = observation.getCategory_concept_text();
        assertEquals(3,category_concept_text_values.size());
        assertEquals(FIRSTCATEGORYTEXT, category_concept_text_values.get(0));
        assertEquals(SECONDCATEGORYTEXT, category_concept_text_values.get(1));
        assertEquals(THIRDCATEGORYTEXT, category_concept_text_values.get(2));

        List<List<String>> category_codings_systems = observation.getCategory_coding_system();
        assertEquals(3,category_codings_systems.size());
        List<String> category_coding_systems = category_codings_systems.get(0);
        assertEquals(3, category_coding_systems.size());
        assertEquals(CATEGORYFIRSTCODINGSYSTEM, category_coding_systems.get(0));
        assertEquals(CATEGORYSECONDCODINGSYSTEM, category_coding_systems.get(1));
        assertEquals(CATEGORYTHIRDCODINGSYSTEM, category_coding_systems.get(2));
        category_codings_systems.get(1);
        assertEquals(3, category_coding_systems.size());
        assertEquals(CATEGORYFIRSTCODINGSYSTEM, category_coding_systems.get(0));
        assertEquals(CATEGORYSECONDCODINGSYSTEM, category_coding_systems.get(1));
        assertEquals(CATEGORYTHIRDCODINGSYSTEM, category_coding_systems.get(2));
        category_codings_systems.get(2);
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

        List<String> code_coding_systems = observation.getCode_coding_system();
        assertEquals(3,code_coding_systems.size());
        assertEquals(CODEFIRSTCODINGSYSTEM, code_coding_systems.get(0));
        assertEquals(CODESECONDCODINGSYSTEM, code_coding_systems.get(1));
        assertEquals(CODETHIRDCODINGSYSTEM, code_coding_systems.get(2));

        List<String> code_coding_codes = observation.getCode_coding_code();
        assertEquals(3, code_coding_codes.size());
        assertEquals(CODEFIRSTCODINGCODE, code_coding_codes.get(0));
        assertEquals(CODESECONDCODINGCODE, code_coding_codes.get(1));
        assertEquals(CODETHIRDCODINGCODE, code_coding_codes.get(2));

        List<String> code_coding_display = observation.getCode_coding_display();
        assertEquals(3, code_coding_display.size());
        assertEquals(CODEFIRSTCODINGDISPLAY, code_coding_display.get(0));
        assertEquals(CODESECONDCODINGDISPLAY, code_coding_display.get(1));
        assertEquals(CODETHIRDCODINGDISPLAY, code_coding_display.get(2));

        List<String> code_coding_code_system_hash = observation.getCode_coding_code_system_hash();
        assertEquals(3, code_coding_code_system_hash.size());
        assertEquals(String.valueOf(CodeSystemHash.hashCodeSystem(CODEFIRSTCODINGSYSTEM, CODEFIRSTCODINGCODE)), code_coding_code_system_hash.get(0));
        assertEquals(String.valueOf(CodeSystemHash.hashCodeSystem(CODESECONDCODINGSYSTEM, CODESECONDCODINGCODE)), code_coding_code_system_hash.get(1));
        assertEquals(String.valueOf(CodeSystemHash.hashCodeSystem(CODETHIRDCODINGSYSTEM, CODETHIRDCODINGCODE)), code_coding_code_system_hash.get(2));

        // Retrieve all Observation codes
/*        SearchRequest searchRequest = elasticsearchSvc.buildObservationCodesSearchRequest(1000);
        SearchResponse response = elasticsearchSvc.executeSearchRequest(searchRequest);
        List<CodeJson> codes = elasticsearchSvc.buildCodeResult(response);
        assertEquals(1, codes.size());
        CodeJson persistedObservationCode = codes.get(0);

        String persistedCodeConceptID = persistedObservationCode.getCodeableConceptId();
        assertEquals(OBSERVATIONSINGLECODEID, persistedCodeConceptID);
        String persistedCodeConceptText = persistedObservationCode.getCodeableConceptText();
        assertEquals(OBSERVATIONCODETEXT, persistedCodeConceptText);

        List<String> persistedCodeCodingSystems = persistedObservationCode.getCoding_system();
        assertEquals(3,persistedCodeCodingSystems.size());
        assertEquals(CODEFIRSTCODINGSYSTEM, persistedCodeCodingSystems.get(0));
        assertEquals(CODESECONDCODINGSYSTEM, persistedCodeCodingSystems.get(1));
        assertEquals(CODETHIRDCODINGSYSTEM, persistedCodeCodingSystems.get(2));

        List<String> persistedCodeCodingCodes = persistedObservationCode.getCoding_code();
        assertEquals(3, persistedCodeCodingCodes.size());
        assertEquals(CODEFIRSTCODINGCODE, persistedCodeCodingCodes.get(0));
        assertEquals(CODESECONDCODINGCODE, persistedCodeCodingCodes.get(1));
        assertEquals(CODETHIRDCODINGCODE, persistedCodeCodingCodes.get(2));

        List<String> persistedCodeCodingDisplays = persistedObservationCode.getCoding_display();
        assertEquals(3, persistedCodeCodingDisplays.size());
        assertEquals(CODEFIRSTCODINGDISPLAY, persistedCodeCodingDisplays.get(0));
        assertEquals(CODESECONDCODINGDISPLAY, persistedCodeCodingDisplays.get(1));
        assertEquals(CODETHIRDCODINGDISPLAY, persistedCodeCodingDisplays.get(2));

        List<String> persistedCodeCodingCodeSystemHashes = persistedObservationCode.getCoding_code_system_hash();
        assertEquals(3, persistedCodeCodingCodeSystemHashes.size());
        assertEquals(String.valueOf(CodeSystemHash.hashCodeSystem(CODEFIRSTCODINGSYSTEM, CODEFIRSTCODINGCODE)), persistedCodeCodingCodeSystemHashes.get(0));
        assertEquals(String.valueOf(CodeSystemHash.hashCodeSystem(CODESECONDCODINGSYSTEM, CODESECONDCODINGCODE)), persistedCodeCodingCodeSystemHashes.get(1));
        assertEquals(String.valueOf(CodeSystemHash.hashCodeSystem(CODETHIRDCODINGSYSTEM, CODETHIRDCODINGCODE)), persistedCodeCodingCodeSystemHashes.get(2));
*/

    }

    private void createSingleObservation() throws IOException {
        ObservationJson indexedObservation = new ObservationJson();
        indexedObservation.setIdentifier(RESOURCEPID);
        indexedObservation.setSubject(SUBJECTTYPEANDID);
        indexedObservation.setEffectiveDtm(EFFECTIVEDTM);

        // Add three CodeableConcepts for category
        List<CodeableConcept> categoryConcepts = new ArrayList<>();
        // Create three codings and first category CodeableConcept
        List<Coding> category1 = new ArrayList<>();
        CodeableConcept categoryCodeableConcept1 = new CodeableConcept().setText(FIRSTCATEGORYTEXT);
        category1.add(new Coding(CATEGORYFIRSTCODINGSYSTEM, FIRSTCATEGORYFIRSTCODINGCODE, FIRSTCATEGORYFIRSTCODINGDISPLAY));
        category1.add(new Coding(CATEGORYSECONDCODINGSYSTEM, FIRSTCATEGORYSECONDCODINGCODE, FIRSTCATEGORYSECONDCODINGDISPLAY));
        category1.add(new Coding(CATEGORYTHIRDCODINGSYSTEM, FIRSTCATEGORYTHIRDCODINGCODE, FIRSTCATEGORYTHIRDCODINGDISPLAY));
        categoryCodeableConcept1.setCoding(category1);
        categoryConcepts.add(categoryCodeableConcept1);
        // Create three codings and second category CodeableConcept
        List<Coding> category2 = new ArrayList<>();
        CodeableConcept categoryCodeableConcept2 = new CodeableConcept().setText(SECONDCATEGORYTEXT);
        category2.add(new Coding(CATEGORYFIRSTCODINGSYSTEM, SECONDCATEGORYFIRSTCODINGCODE, SECONDCATEGORYFIRSTCODINGDISPLAY));
        category2.add(new Coding(CATEGORYSECONDCODINGSYSTEM, SECONDCATEGORYSECONDCODINGCODE, SECONDCATEGORYSECONDCODINGDISPLAY));
        category2.add(new Coding(CATEGORYTHIRDCODINGSYSTEM, SECONDCATEGORYTHIRDCODINGCODE, SECONDCATEGORYTHIRDCODINGDISPLAY));
        categoryCodeableConcept2.setCoding(category2);
        categoryConcepts.add(categoryCodeableConcept2);
        // Create three codings and third category CodeableConcept
        List<Coding> category3 = new ArrayList<>();
        CodeableConcept categoryCodeableConcept3 = new CodeableConcept().setText(THIRDCATEGORYTEXT);
        category3.add(new Coding(CATEGORYFIRSTCODINGSYSTEM, THIRDCATEGORYFIRSTCODINGCODE, THIRDCATEGORYFIRSTCODINGDISPLAY));
        category3.add(new Coding(CATEGORYSECONDCODINGSYSTEM, THIRDCATEGORYSECONDCODINGCODE, THIRDCATEGORYSECONDCODINGDISPLAY));
        category3.add(new Coding(CATEGORYTHIRDCODINGSYSTEM, THIRDCATEGORYTHIRDCODINGCODE, THIRDCATEGORYTHIRDCODINGDISPLAY));
        categoryCodeableConcept3.setCoding(category3);
        categoryConcepts.add(categoryCodeableConcept3);
        indexedObservation.setCategories(categoryConcepts);

        // Create CodeableConcept for Code with three codings.
        indexedObservation.setCode_concept_id(OBSERVATIONSINGLECODEID);
        CodeableConcept codeableConceptField = new CodeableConcept().setText(OBSERVATIONCODETEXT);
        codeableConceptField.addCoding(new Coding(CODEFIRSTCODINGSYSTEM, CODEFIRSTCODINGCODE, CODEFIRSTCODINGDISPLAY));
        codeableConceptField.addCoding(new Coding(CODESECONDCODINGSYSTEM, CODESECONDCODINGCODE, CODESECONDCODINGDISPLAY));
        codeableConceptField.addCoding(new Coding(CODETHIRDCODINGSYSTEM, CODETHIRDCODINGCODE, CODETHIRDCODINGDISPLAY));
        indexedObservation.setCode(codeableConceptField);

        String observationDocument = ourMapperNonPrettyPrint.writeValueAsString(indexedObservation);
        assertTrue(elasticsearchSvc.performIndex(IndexConstants.OBSERVATION_INDEX, RESOURCEPID, observationDocument, IndexConstants.OBSERVATION_DOCUMENT_TYPE));

        CodeJson observationCode = new CodeJson(codeableConceptField, OBSERVATIONSINGLECODEID);
        String codeDocument = ourMapperNonPrettyPrint.writeValueAsString(observationCode);
        assertTrue(elasticsearchSvc.performIndex(IndexConstants.CODE_INDEX, OBSERVATIONSINGLECODEID, codeDocument, IndexConstants.CODE_DOCUMENT_TYPE));

        try {
            Thread.sleep(1000L);
        } catch (InterruptedException theE) {
            theE.printStackTrace();
        }

    }

}
