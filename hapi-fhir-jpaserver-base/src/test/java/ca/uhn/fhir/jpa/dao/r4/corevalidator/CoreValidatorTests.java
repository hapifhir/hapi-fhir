package ca.uhn.fhir.jpa.dao.r4.corevalidator;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.DaoRegistry;
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.r4.FhirResourceDaoR4ValidateTest;
import ca.uhn.fhir.jpa.dao.r4.corevalidator.utils.ConvertorHelper;
import ca.uhn.fhir.jpa.dao.r4.corevalidator.utils.CoreValidatorTestUtils;
import ca.uhn.fhir.jpa.dao.r4.corevalidator.utils.XMLUtils;
import ca.uhn.fhir.jpa.dao.r4.jupiter.BaseJpaR4Test;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.EncodingEnum;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.elementmodel.Manager;
import org.hl7.fhir.r4.formats.XmlParser;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Stream;

public class CoreValidatorTests extends BaseJpaR4Test {

    private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoR4ValidateTest.class);
    private static final String TEST_FILES_BASE_PATH = "/org/hl7/fhir/testcases/validator/";
    private static final String TEST_MANIFEST_PATH = TEST_FILES_BASE_PATH + "manifest.json";

    @Autowired
    protected DaoRegistry daoRegistry;

    protected FhirContext myCtx = FhirContext.forR4();

    /**
     * This is the method data source for the testing data for used in the {@link CoreValidatorTests#runCoreValidationTests(String, TestEntry)}
     * method. It loads the main manifest.json file and parses it into a {@link Stream} of filenames as {@link String}
     * and corresponding test data as {@link TestEntry}.
     *
     * @return {@link Stream} of test data.
     * @throws IOException If no manifest file can be found in resources.
     */
    private static Stream<Object[]> data() throws IOException {

        Gson gson = new Gson();
        String contents = loadStringFromResourceFile(TEST_MANIFEST_PATH);

        Map<String, TestEntry> examples = new HashMap<>();
        JsonObject manifest = (JsonObject) new JsonParser().parse(contents);
        for (Map.Entry<String, JsonElement> e : manifest.getAsJsonObject("test-cases").entrySet()) {
            examples.put(e.getKey(), gson.fromJson(e.getValue().toString(), TestEntry.class));
        }

        List<String> names = new ArrayList<>(examples.size());
        names.addAll(examples.keySet());
        Collections.sort(names);

        List<Object[]> objects = new ArrayList<>(examples.size());
        for (String id : names) {
            objects.add(new Object[]{id, examples.get(id)});
        }
        return objects.stream();
    }

    /**
     * Loads the string data from the file at the given resource path.
     *
     * @param resourcePath Path location for the resource file to read in.
     * @return {@link String} of the file contents.
     * @throws IOException if no such file exists at the given path.
     */
    public static String loadStringFromResourceFile(String resourcePath) throws IOException {
        InputStream inputStream = CoreValidatorTests.class.getResourceAsStream(resourcePath);
        //creating an InputStreamReader object
        InputStreamReader isReader = new InputStreamReader(inputStream);
        //Creating a BufferedReader object
        BufferedReader reader = new BufferedReader(isReader);
        StringBuffer sb = new StringBuffer();
        String str;
        while ((str = reader.readLine()) != null) {
            sb.append(str);
        }
        return sb.toString();
    }

    @DisplayName("Core Library Validation")
    @ParameterizedTest(name = "Test #{index} -> Testing validation for file {0}")
    @MethodSource("data")
    public void runCoreValidationTests(String testFile, TestEntry testEntry) throws Exception {
        myDaoConfig.setAllowExternalReferences(true);
        String temp = testFile;
        TestEntry te = testEntry;

        if (testEntry.getVersion() != null) {
            System.out.println("Resource Version :: " + testEntry.getVersion());
        }

        EncodingEnum encoding = CoreValidatorTestUtils.getEncoding(testFile);

//        if (testFile.contains("bundle")) {
//            System.out.println("!!!!!!!!!!!!! THIS IS A BUNDLE TEST");
//            String loadedFile = loadResource(TEST_FILES_BASE_PATH + testFile);
//            switch (encoding) {
//                case XML:
//                    Manager.makeParser()
//                    XMLUtils.TestMethod(TEST_FILES_BASE_PATH + testFile);
//                    break;
//                case JSON:
//
//                    break;
//                default:
//                    throw new IllegalStateException("Document type not one of JSON or XML");
//            }
//        }

            IBaseResource baseResource = loadResource(myCtx, TEST_FILES_BASE_PATH + testFile);

            String s = loadResource(TEST_FILES_BASE_PATH + testFile);
            String name = myCtx.getResourceDefinition(baseResource).getName();
            IFhirResourceDao<? extends IBaseResource> resourceDao = daoRegistry.getResourceDaoOrNull(name);
            IBaseResource r4Version = ConvertorHelper.getR4Version(baseResource, testEntry);
            baseResource.setId(new IdDt());
            OperationOutcome oo = CoreValidatorTestUtils.validate(baseResource, s, encoding, resourceDao, mySrd, myCtx);

            CoreValidatorTestUtils.testOutputs(testEntry.getTestResult(), oo);


        System.out.println();
    }

}
