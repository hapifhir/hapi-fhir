package ca.uhn.fhir.jpa.search.lastn.cli;

import ca.uhn.fhir.jpa.search.lastn.json.IdJson;
import ca.uhn.fhir.jpa.search.lastn.json.IndexJson;
import ca.uhn.fhir.jpa.search.lastn.json.ObservationJson;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class MulticodeObservationsIntoElasticSearch {
    private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(MulticodeObservationsIntoElasticSearch.class);

    private static final ObjectMapper ourMapperNonPrettyPrint;

    static {
        ourMapperNonPrettyPrint = new ObjectMapper();
        ourMapperNonPrettyPrint.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        ourMapperNonPrettyPrint.disable(SerializationFeature.INDENT_OUTPUT);
        ourMapperNonPrettyPrint.disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET);
    }

    public static void main(String[] theArgs) {

        for (int patientCount = 0; patientCount < 10 ; patientCount++) {

            String subject = "Patient/"+UUID.randomUUID().toString();

            for ( int entryCount = 0; entryCount < 10 ; entryCount++ ) {
                String nextResourceId = UUID.randomUUID().toString();

                IdJson id = new IdJson(nextResourceId);
                IndexJson documentIndex = new IndexJson(id);

                ObservationJson observationDocument = new ObservationJson();
                observationDocument.setIdentifier(nextResourceId);
                observationDocument.setSubject(subject);
                // Add three CodeableConcepts for category
                List<CodeableConcept> category = new ArrayList<>();
                // Create three codings and first category CodeableConcept
                Coding categoryCoding1_1 = new Coding("http://mycodes.org/fhir/observation-category", "test-heart-rate", "test heart-rate");
                Coding categoryCoding1_2 = new Coding("http://myalternatecodes.org/fhir/observation-category", "test-alt-heart-rate", "test alternate heart-rate");
                Coding categoryCoding1_3 = new Coding("http://mysecondaltcodes.org/fhir/observation-category", "test-2nd-alt-heart-rate", "test second alternate heart-rate");
                CodeableConcept categoryCodeableConcept1 = new CodeableConcept();
                categoryCodeableConcept1.getCoding().add(categoryCoding1_1);
                categoryCodeableConcept1.getCoding().add(categoryCoding1_2);
                categoryCodeableConcept1.getCoding().add(categoryCoding1_3);
                categoryCodeableConcept1.setText("Heart Rate Codeable Concept");
                category.add(categoryCodeableConcept1);
                // Create three codings and second category CodeableConcept
                Coding categoryCoding2_1 = new Coding("http://mycodes.org/fhir/observation-category", "test-vital-signs", "test vital signs");
                Coding categoryCoding2_2 = new Coding("http://myalternatecodes.org/fhir/observation-category", "test-alt-vitals", "test alternate vital signs");
                Coding categoryCoding2_3 = new Coding("http://mysecondaltcodes.org/fhir/observation-category", "test-2nd-alt-vitals", "test second alternate vital signs");
                CodeableConcept categoryCodeableConcept2 = new CodeableConcept();
                categoryCodeableConcept2.getCoding().add(categoryCoding2_1);
                categoryCodeableConcept2.getCoding().add(categoryCoding2_2);
                categoryCodeableConcept2.getCoding().add(categoryCoding2_3);
                categoryCodeableConcept2.setText("Vital Signs Codeable Concept");
                category.add(categoryCodeableConcept2);
                // Create three codings and third category CodeableConcept
                Coding categoryCoding3_1 = new Coding("http://mycodes.org/fhir/observation-category", "test-vitals-panel", "test vital signs panel");
                Coding categoryCoding3_2 = new Coding("http://myalternatecodes.org/fhir/observation-category", "test-alt-vitals-panel", "test alternate vital signs panel");
                Coding categoryCoding3_3 = new Coding("http://mysecondaltcodes.org/fhir/observation-category", "test-2nd-alt-vitals-panel", "test second alternate vital signs panel");
                CodeableConcept categoryCodeableConcept3 = new CodeableConcept();
                categoryCodeableConcept3.getCoding().add(categoryCoding3_1);
                categoryCodeableConcept3.getCoding().add(categoryCoding3_2);
                categoryCodeableConcept3.getCoding().add(categoryCoding3_3);
                categoryCodeableConcept3.setText("Vital Signs Panel Codeable Concept");
                category.add(categoryCodeableConcept3);
                observationDocument.setCategories(category);

                Coding codeCoding1 = new Coding("http://mycodes.org/fhir/observation-code", "test-code", "test observation code");
                Coding codeCoding2 = new Coding("http://myalternatecodes.org/fhir/observation-code", "test-alt-code", "test observation code");
                Coding codeCoding3 = new Coding("http://mysecondaltcodes.org/fhir/observation-code", "test-second-alt-code", "test observation code");
                CodeableConcept code = new CodeableConcept();
                code.getCoding().add(codeCoding1);
                code.getCoding().add(codeCoding2);
                code.getCoding().add(codeCoding3);
                code.setText("Observation code CodeableConcept");
                observationDocument.setCode(code);
                observationDocument.setCode_concept_id("multicode_test_normalized_code");

                Date effectiveDtm = new Date();
                observationDocument.setEffectiveDtm(effectiveDtm);

                StringWriter stringWriter = new StringWriter();

                File outputFile = new File("Observations_multiplecodes.json");
                try {
                    FileOutputStream outputStream = new FileOutputStream(outputFile, true);
                    ourMapperNonPrettyPrint.writeValue(stringWriter, documentIndex);
                    stringWriter.append('\n');
                    ourMapperNonPrettyPrint.writeValue(stringWriter, observationDocument);
                    ourMapperNonPrettyPrint.writeValue(outputStream, documentIndex);
                    outputStream.write('\n');
                    ourMapperNonPrettyPrint.writeValue(outputStream, observationDocument);
                    outputStream.write('\n');
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException theE) {
                    theE.printStackTrace();
                }
            }

        }

        ourLog.info("Upload complete");

    }

}