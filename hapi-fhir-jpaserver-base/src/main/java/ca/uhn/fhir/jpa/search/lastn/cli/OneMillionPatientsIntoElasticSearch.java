package ca.uhn.fhir.jpa.search.lastn.cli;

import ca.uhn.fhir.jpa.search.lastn.ElasticsearchBulkIndexSvcImpl;
import ca.uhn.fhir.jpa.search.lastn.IndexConstants;
import ca.uhn.fhir.jpa.search.lastn.json.CodeJson;
import ca.uhn.fhir.jpa.search.lastn.json.ObservationJson;
import ca.uhn.fhir.jpa.search.lastn.util.SimpleStopWatch;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.lang3.RandomStringUtils;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;

import java.io.IOException;
import java.util.*;

public class OneMillionPatientsIntoElasticSearch {
//    private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(OneMillionPatientsIntoElasticSearch.class);

    private static final ObjectMapper ourMapperNonPrettyPrint;

    static {
        ourMapperNonPrettyPrint = new ObjectMapper();
        ourMapperNonPrettyPrint.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        ourMapperNonPrettyPrint.disable(SerializationFeature.INDENT_OUTPUT);
        ourMapperNonPrettyPrint.disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET);
    }

    public static void main(String[] theArgs) throws IOException {

        ElasticsearchBulkIndexSvcImpl elasticsearchSvc = new ElasticsearchBulkIndexSvcImpl("localhost",9301, "elastic", "changeme");
        try {

            SimpleStopWatch stopwatch = new SimpleStopWatch();

            List<String> observationCodeIds = new ArrayList<>();
            List<CodeableConcept> observationCodes = new ArrayList<>();

            for (int codeCount = 0; codeCount < 1000; codeCount++) {
                String code = RandomStringUtils.random(10,true,true);
                Coding codeCoding = new Coding("http://mycodes.org/fhir/observation-code", code, "test observation code " + code);
                CodeableConcept codeConcept = new CodeableConcept();
                codeConcept.getCoding().add(codeCoding);
                codeConcept.setText("test observation code concept " + code);
                String codeableConceptId = UUID.randomUUID().toString();
                observationCodeIds.add(codeableConceptId);
                observationCodes.add(codeConcept);
                CodeJson codeDocument = new CodeJson(codeConcept, codeableConceptId);
                String printedObservationDocument = ourMapperNonPrettyPrint.writeValueAsString(codeDocument);
                System.out.println(printedObservationDocument);
                System.out.println(codeableConceptId);
                elasticsearchSvc.addToBulkIndexRequest("code_index", codeableConceptId, printedObservationDocument, IndexConstants.CODE_DOCUMENT_TYPE);
                if ((codeCount+1)%250 == 0) {
                    elasticsearchSvc.executeBulkIndex();
                    long elapsedTime = stopwatch.getElapsedTime();
                    stopwatch.restart();
                    System.out.println("Elapsed processing time = " + elapsedTime/1000 + "s");
                    System.out.println("Average processing time/code = " + elapsedTime/5000 + "ms");
                }
            }

            for (int patientCount = 0; patientCount < 1000000 ; patientCount++) {
                String subject = "Patient/"+UUID.randomUUID().toString();
                ArrayList<CodeableConcept> observationCodesSubSet = new ArrayList<>();
                ArrayList<String> observationCodeIdsSubSet = new ArrayList<>();
                for (int observationCount = 0; observationCount < 15 ; observationCount++) {
                    int codeIndex = (int) (1000 * Math.random());
                    observationCodesSubSet.add(observationCodes.get(codeIndex));
                    observationCodeIdsSubSet.add(observationCodeIds.get(codeIndex));
                }
                int repeatedCodeIndex = (int) (1000 * Math.random());
                CodeableConcept repeatedCoding = observationCodes.get(repeatedCodeIndex);
                for (int observationCount = 0; observationCount < 10 ; observationCount++ ) {
                    observationCodesSubSet.add(repeatedCoding);
                    observationCodeIdsSubSet.add(observationCodeIds.get(repeatedCodeIndex));
                }
                int entryCount = 0;
                for (int codingCount=0; codingCount<observationCodesSubSet.size(); codingCount++) {
                    String nextResourceId = UUID.randomUUID().toString();
                    ObservationJson observationDocument = new ObservationJson();
                    observationDocument.setIdentifier(nextResourceId);
                    observationDocument.setSubject(subject);
                    List<CodeableConcept> category = new ArrayList<>();
                    Coding categoryCoding = new Coding("http://mycodes.org/fhir/category-code", "test-category-code", "test category display");
                    CodeableConcept categoryCodeableConcept = new CodeableConcept();
                    categoryCodeableConcept.getCoding().add(categoryCoding);
                    categoryCodeableConcept.setText("Test Category CodeableConcept Text");
                    category.add(categoryCodeableConcept);
                    observationDocument.setCategories(category);
                    observationDocument.setCode(observationCodesSubSet.get(codingCount));
                    observationDocument.setCode_concept_id(observationCodeIdsSubSet.get(codingCount));
                    Calendar observationDate = new GregorianCalendar();
                    observationDate.add(Calendar.HOUR, -10 + entryCount);
                    entryCount++;
                    Date effectiveDtm = observationDate.getTime();
                    observationDocument.setEffectiveDtm(effectiveDtm);

                    String printedObservationDocument = ourMapperNonPrettyPrint.writeValueAsString(observationDocument);
                    elasticsearchSvc.addToBulkIndexRequest("observation_index", nextResourceId, printedObservationDocument, IndexConstants.OBSERVATION_DOCUMENT_TYPE );
                }
                if ((patientCount+1)%100 == 0) {
                    System.out.println("Entries created = " + (patientCount+1)*25);
                }
                if ((patientCount+1)%250 == 0) {
                    elasticsearchSvc.executeBulkIndex();
                    long elapsedTime = stopwatch.getElapsedTime();
                    stopwatch.restart();
                    System.out.println("Elapsed processing time = " + elapsedTime/1000 + "s");
                    System.out.println("Average processing time/observation = " + elapsedTime/5000 + "ms");
                }


            }

            if (elasticsearchSvc.bulkRequestPending()) {
                elasticsearchSvc.executeBulkIndex();
            }

            //       ourLog.info("Upload complete");
        } finally {
            elasticsearchSvc.closeClient();
        }

    }

}