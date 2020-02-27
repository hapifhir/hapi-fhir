package ca.uhn.fhir.jpa.dao.r4.corevalidator;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.jpa.dao.DaoRegistry;
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.r4.FhirResourceDaoR4ValidateTest;
import ca.uhn.fhir.jpa.dao.r4.jupiter.BaseJpaR4Test;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.ValidationModeEnum;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.hl7.fhir.convertors.VersionConvertor_10_40;
import org.hl7.fhir.convertors.VersionConvertor_30_40;
import org.hl7.fhir.convertors.VersionConvertor_40_50;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.codesystems.FHIRVersion;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Stream;

import static org.hl7.fhir.r4.model.codesystems.FHIRVersion._1_6_0;
import static org.junit.Assert.fail;

public class CoreValidatorTests extends BaseJpaR4Test {

    private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoR4ValidateTest.class);

    public static final String TEST_FILES_BASE_PATH = "/org/hl7/fhir/testcases/validator/";
    public static final String TEST_MANIFEST_PATH = TEST_FILES_BASE_PATH + "manifest.json";

    @Autowired
    protected DaoRegistry daoRegistry;

    private FhirContext myCtx = FhirContext.forR4();

    @ParameterizedTest(name = "Testing validation for file {0}")
    @MethodSource("data")
    public void runCoreValidationTests(String testFile, TestEntry testEntry) throws IOException {
        myDaoConfig.setAllowExternalReferences(true);
        String temp = testFile;
        TestEntry te = testEntry;

        if (testEntry.getVersion() != null) {
            System.out.println("Resource Version :: " + testEntry.getVersion());
        }

        EncodingEnum encoding = getEncoding(testFile);

        IBaseResource baseResource = loadResource(myCtx, TEST_FILES_BASE_PATH + testFile);
        IFhirResourceDao<? extends IBaseResource> resourceDao = daoRegistry.getResourceDaoOrNull(baseResource.getIdElement().getResourceType());
        getR4Version(baseResource, testEntry.getVersion());
        baseResource.setId(new IdDt());
        OperationOutcome oo = validate(baseResource, encoding, resourceDao, mySrd, myCtx);

        testOutputs(testEntry.getTestResult(), oo);
        System.out.println();

    }

    private static void testOutputs(TestResult result, OperationOutcome oo) {

        int errorCount = 0;
        int warningCount = 0;

        List<OperationOutcome.OperationOutcomeIssueComponent> issues = oo.getIssue();
        for (OperationOutcome.OperationOutcomeIssueComponent o : issues) {
            switch (o.getSeverity()) {
                case ERROR:
                    errorCount++;
                    break;
                case WARNING:
                    warningCount++;
                    break;
                case INFORMATION:
                case FATAL:
                case NULL:
                default:
                    break;
            }
        }

        Assertions.assertEquals(result.getErrorCount(), errorCount);
        Assertions.assertEquals(result.getWarningCount(), warningCount);
    }

    private static EncodingEnum getEncoding(String testFile) {
        EncodingEnum encoding;
        if (testFile.endsWith(".json")) {
            encoding = EncodingEnum.JSON;
        } else {
            encoding = EncodingEnum.XML;
        }
        return encoding;
    }

    private static <T extends IBaseResource> OperationOutcome validate(T input, EncodingEnum enc, IFhirResourceDao resourceDao, ServletRequestDetails mySrd, FhirContext myFhirCtx) {
        String encoded = null;
        MethodOutcome outcome = null;
        ValidationModeEnum mode = ValidationModeEnum.CREATE;
        switch (enc) {
            case JSON:
                encoded = myFhirCtx.newJsonParser().encodeResourceToString(input);
                try {
                    resourceDao.validate(input, null, encoded, EncodingEnum.JSON, mode, null, mySrd);
                } catch (PreconditionFailedException e) {
                    return (OperationOutcome) e.getOperationOutcome();
                }
                break;
            case XML:
                encoded = myFhirCtx.newXmlParser().encodeResourceToString(input);
                try {
                    resourceDao.validate(input, null, encoded, EncodingEnum.XML, mode, null, mySrd);
                } catch (PreconditionFailedException e) {
                    return (OperationOutcome) e.getOperationOutcome();
                }
                break;
        }
        return null;
    }

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
            objects.add(new Object[] { id, examples.get(id)});
        }
        return objects.stream();
    }

    public static String loadStringFromResourceFile(String resourcePath) throws IOException {
        InputStream inputStream = CoreValidatorTests.class.getResourceAsStream(resourcePath);
        //creating an InputStreamReader object
        InputStreamReader isReader = new InputStreamReader(inputStream);
        //Creating a BufferedReader object
        BufferedReader reader = new BufferedReader(isReader);
        StringBuffer sb = new StringBuffer();
        String str;
        while((str = reader.readLine())!= null){
            sb.append(str);
        }
        return sb.toString();
    }

    private IBaseResource getR4Version(IBaseResource resource, String version) {
        if (version != null && version.equals("4.0")) version = "4.0.0";

        FHIRVersion fhirVersion = version == null ? FHIRVersion.NULL : FHIRVersion.fromCode(version);
        switch (fhirVersion) {
            case _0_01:
            case _0_0_80:
            case _0_0_81:
            case _0_0_82:
                // DSTU 1
                throw new IllegalStateException("DSTU1 structure passed in for validation.");
            case _0_4_0:
            case _0_5_0:
            case _0_05:
            case _0_06:
            case _0_11:
            case _1_0_0:
            case _1_0_1:
                return VersionConvertor_10_40.convertResource((org.hl7.fhir.dstu2.model.Resource) resource);
            case _1_0_2:
            case _1_1_0:
            case _1_4_0:
            case _1_6_0:
            case _1_8_0:
            case _3_0_0:
                return VersionConvertor_30_40.convertResource((org.hl7.fhir.dstu3.model.Resource) resource, false);
            case _3_0_1:
            case _3_3_0:
            case _3_5_0:
            case _4_0_0:
                return resource;
            case NULL:
            default:
                return resource;
        }
    }

//    @After
//    public void after() {
//        FhirInstanceValidator val = AopTestUtils.getTargetObject(myValidatorModule);
//        val.setBestPracticeWarningLevel(IResourceValidator.BestPracticeWarningLevel.Warning);
//
//        myDaoConfig.setAllowExternalReferences(new DaoConfig().isAllowExternalReferences());
//        myDaoConfig.setMaximumExpansionSize(DaoConfig.DEFAULT_MAX_EXPANSION_SIZE);
//        myDaoConfig.setPreExpandValueSets(new DaoConfig().isPreExpandValueSets());
//    }

}
