package ca.uhn.fhir.jpa.dao.r4.corevalidator;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.DaoRegistry;
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.r4.FhirResourceDaoR4ValidateTest;
import ca.uhn.fhir.jpa.dao.r4.corevalidator.gson.TestEntry;
import ca.uhn.fhir.jpa.dao.r4.corevalidator.gson.TestResult;
import ca.uhn.fhir.jpa.dao.r4.corevalidator.utils.ConvertorHelper;
import ca.uhn.fhir.jpa.dao.r4.corevalidator.utils.CoreValidatorTestUtils;
import ca.uhn.fhir.jpa.dao.r4.corevalidator.utils.ParsingUtils;
import ca.uhn.fhir.jpa.dao.r4.jupiter.BaseJpaR4Test;
import ca.uhn.fhir.rest.api.EncodingEnum;
import com.google.gson.*;
import org.hl7.fhir.exceptions.DefinitionException;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.exceptions.FHIRFormatError;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.hapi.validation.FhirInstanceValidator;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r5.conformance.ProfileUtilities;
import org.hl7.fhir.r5.model.Resource;
import org.hl7.fhir.r5.model.StructureDefinition;
import org.hl7.fhir.r5.test.utils.TestingUtilities;
import org.hl7.fhir.r5.utils.IResourceValidator;
import org.hl7.fhir.utilities.validation.ValidationMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.util.*;
import java.util.stream.Stream;

public class CoreValidatorTests extends BaseJpaR4Test {

    private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoR4ValidateTest.class);
    private static final String TEST_FILES_BASE_PATH = "/org/hl7/fhir/testcases/validator/";
    private static final String TEST_MANIFEST_PATH = TEST_FILES_BASE_PATH + "manifest.json";
    private static TestEntry value;

    @Autowired
    protected DaoRegistry daoRegistry;

    @Autowired
    protected FhirInstanceValidator validator;

    protected FhirContext myCtx = FhirContext.forR4();

    @BeforeEach
    public void beforeEach() {
        validator.setBestPracticeWarningLevel(IResourceValidator.BestPracticeWarningLevel.Ignore);
        validator.setAnyExtensionsAllowed(true);
    }

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
            value = gson.fromJson(e.getValue().toString(), TestEntry.class);
            examples.put(e.getKey(), value);
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
    public void runCoreValidationTests(String testFile, TestEntry testEntry) throws IOException {

        //if (!testFile.equals("allergy.json")) Assertions.fail();

        validator.setAssumeValidRestReferences(testEntry.getProfile().getAssumeValidRestReferences());
        validator.setAllowExamples(testEntry.getAllowExamples());

        myDaoConfig.setAllowExternalReferences(true);

        if (testEntry.getUsesTest()) {
            String resourceAsString = loadResource(TEST_FILES_BASE_PATH + testFile);
            Assertions.assertNotNull(resourceAsString, "Could not load resource string from file <" + testFile + ">");
            validator.setValidatorResourceFetcher(new TestResourceFetcher(testEntry));

//            if (testEntry.getProfiles() != null) {
//                for (String filename : testEntry.getProfiles()) {
//                    String contents = TestingUtilities.loadTestResource("validator", filename);
//                    StructureDefinition sd = loadProfile(filename, contents, v, messages);
//                    vali.getContext().cacheResource(sd);
//                }
//            }

            String profileFilename = CoreValidatorTestUtils.getProfileFilename(testEntry);
            String testProfile = profileFilename == null ? null : loadResource(TEST_FILES_BASE_PATH + CoreValidatorTestUtils.getProfileFilename(testEntry));
            if (testProfile != null) {
                validate(testFile, testEntry.getProfile().getTestResult(), resourceAsString, testProfile, ConvertorHelper.shouldTest(testEntry));
            }
            validate(testFile, testEntry.getTestResult(), resourceAsString, null, ConvertorHelper.shouldTest(testEntry));
        }
    }

    protected void validate(String testFile, TestResult testResult, String resourceAsString, String testProfile, boolean shouldTest) {
        String resourceName = null;
        OperationOutcome operationOutcome = new OperationOutcome();

        try {
            resourceName = extractResourceName(testFile, resourceAsString);
            IFhirResourceDao<? extends IBaseResource> resourceDao = daoRegistry.getResourceDaoOrNull(resourceName);
            operationOutcome = CoreValidatorTestUtils.validate(myCtx, testProfile, resourceName, resourceAsString, resourceDao);
        } catch (Exception e) {
            operationOutcome.addIssue().setSeverity(OperationOutcome.IssueSeverity.ERROR).setDiagnostics(e.getMessage());
        }

        if (shouldTest) {
            CoreValidatorTestUtils.testOutputs(testResult, operationOutcome);
        }
    }

    protected String extractResourceName(String testFile, String resourceAsString) throws RuntimeException {
        String resourceName = null;

        try {
            IBaseResource baseResource = loadResource(myCtx, TEST_FILES_BASE_PATH + testFile);
            resourceName = myCtx.getResourceDefinition(baseResource).getName();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        if (resourceName == null) {
            EncodingEnum encoding = CoreValidatorTestUtils.getEncoding(testFile);
            resourceName = ParsingUtils.getResourceName(encoding, resourceAsString);
        }
        return resourceName;
    }

    public StructureDefinition loadProfile(String filename, String contents, String v, List<ValidationMessage> messages)  throws IOException, FHIRException {
        StructureDefinition sd = (StructureDefinition) loadResource(filename, contents, v);
        ProfileUtilities pu = new ProfileUtilities(TestingUtilities.context(), messages, null);
        if (!sd.hasSnapshot()) {
            StructureDefinition base = TestingUtilities.context().fetchResource(StructureDefinition.class, sd.getBaseDefinition());
            pu.generateSnapshot(base, sd, sd.getUrl(), null, sd.getTitle());
        }
        for (Resource r: sd.getContained()) {
            if (r instanceof StructureDefinition) {
                StructureDefinition childSd = (StructureDefinition)r;
                if (!childSd.hasSnapshot()) {
                    StructureDefinition base = TestingUtilities.context().fetchResource(StructureDefinition.class, childSd.getBaseDefinition());
                    pu.generateSnapshot(base, childSd, childSd.getUrl(), null, childSd.getTitle());
                }
            }
        }
        return sd;
    }

}
