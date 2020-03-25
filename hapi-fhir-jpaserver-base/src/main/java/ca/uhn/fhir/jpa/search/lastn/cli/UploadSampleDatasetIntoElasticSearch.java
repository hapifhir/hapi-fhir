package ca.uhn.fhir.jpa.search.lastn.cli;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.parser.LenientErrorHandler;
import ca.uhn.fhir.jpa.search.lastn.json.IdJson;
import ca.uhn.fhir.jpa.search.lastn.json.IndexJson;
import ca.uhn.fhir.jpa.search.lastn.json.ObservationJson;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.base.Charsets;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.io.IOUtils;
import org.hl7.fhir.r4.model.*;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.*;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class UploadSampleDatasetIntoElasticSearch {
    private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(UploadSampleDatasetIntoElasticSearch.class);

    private static final ObjectMapper ourMapperNonPrettyPrint;

    static {
        ourMapperNonPrettyPrint = new ObjectMapper();
        ourMapperNonPrettyPrint.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        ourMapperNonPrettyPrint.disable(SerializationFeature.INDENT_OUTPUT);
        ourMapperNonPrettyPrint.disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET);
    }

    public static void main(String[] theArgs) {

        FhirContext myFhirCtx = FhirContext.forR4();
        myFhirCtx.getRestfulClientFactory().setSocketTimeout(120000);

        PathMatchingResourcePatternResolver provider = new PathMatchingResourcePatternResolver();
        final Resource[] bundleResources;
        try {
            bundleResources = provider.getResources("*.json.bz2");
        } catch (IOException e) {
            throw new RuntimeException("Unexpected error during transmission: " + e.toString(), e);
        }

        AtomicInteger index = new AtomicInteger();

        Arrays.stream(bundleResources).forEach(
                resource -> {
                    index.incrementAndGet();

                    InputStream resIs = null;
                    String nextBundleString;
                    try {
                        resIs = resource.getInputStream();
                        resIs = new BZip2CompressorInputStream(resIs);
                        nextBundleString = IOUtils.toString(resIs, Charsets.UTF_8);
                    } catch (IOException e) {
                        ourLog.error("Failure reading: {}", resource.getFilename(), e);
                        return;
                    } finally {
                        try {
                            if (resIs != null) {
                                resIs.close();
                            }
                        } catch (final IOException ioe) {
                            // ignore
                        }
                    }

                    ourLog.info("Uploading {}/{} - {} ({} bytes)", index, bundleResources.length, resource.getFilename(), nextBundleString.length());

                    /*
                     * SMART demo apps rely on the use of LOINC 3141-9 (Body Weight Measured)
                     * instead of LOINC 29463-7 (Body Weight)
                     */
                    nextBundleString = nextBundleString.replace("\"29463-7\"", "\"3141-9\"");

                    IParser parser = myFhirCtx.newJsonParser();
                    parser.setParserErrorHandler(new LenientErrorHandler(false));
                    Bundle bundle = parser.parseResource(Bundle.class, nextBundleString);

                    for (BundleEntryComponent nextEntry : bundle.getEntry()) {

                        /*
                         * Synthea gives resources UUIDs with urn:uuid: prefix, which is only
                         * used for placeholders. We're going to use these as the actual resource
                         * IDs, so we strip the prefix.
                         */
                        String nextResourceId = nextEntry.getFullUrl();
                        if (nextResourceId == null) {
                            nextResourceId = UUID.randomUUID().toString();
                        }

                        nextResourceId = nextResourceId.replace("urn:uuid:", "");
                        nextEntry.getResource().setId(nextResourceId);
                        nextEntry.setFullUrl(nextResourceId);

                        if (nextEntry.getResource().getResourceType().equals(ResourceType.Observation)) {

                            IdJson id = new IdJson(nextResourceId);
                            IndexJson documentIndex = new IndexJson(id);

                            org.hl7.fhir.r4.model.Observation observation = (Observation) nextEntry.getResource();
                            ObservationJson observationDocument = new ObservationJson();
                            observationDocument.setIdentifier(nextResourceId);
                            String subject = "Patient/"+observation.getSubject().getReference();
                            observationDocument.setSubject(subject);
                            List<CodeableConcept> category = observation.getCategory();
                            observationDocument.setCategories(category);
                            observationDocument.setCode_concept_id(category.get(0).getCodingFirstRep().getSystem() + "/" + category.get(0).getCodingFirstRep().getCode());
                            CodeableConcept code = observation.getCode();
                            observationDocument.setCode(code);
                            Date effectiveDtm = observation.getEffectiveDateTimeType().getValue();
                            observationDocument.setEffectiveDtm(effectiveDtm);

                            StringWriter stringWriter = new StringWriter();
                            File outputFile = new File("Observations.json");
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
                            System.out.println(stringWriter.toString());

                        }

                    }

                }
        );

        ourLog.info("Upload complete");

    }

}