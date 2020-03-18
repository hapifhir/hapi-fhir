package ca.uhn.fhir.jpa.dao.r4.corevalidator;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.DaoMethodOutcome;
import ca.uhn.fhir.jpa.dao.DaoRegistry;
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.r4.FhirResourceDaoR4ValidateTest;
import ca.uhn.fhir.jpa.dao.r4.corevalidator.gson.TestEntry;
import ca.uhn.fhir.jpa.dao.r4.corevalidator.gson.TestResult;
import ca.uhn.fhir.jpa.dao.r4.corevalidator.utils.ConvertorHelper;
import ca.uhn.fhir.jpa.dao.r4.corevalidator.utils.CoreValidatorTestUtils;
import ca.uhn.fhir.jpa.dao.r4.corevalidator.utils.ParsingUtils;
import ca.uhn.fhir.jpa.dao.r4.jupiter.BaseJpaR4Test;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.parser.LenientErrorHandler;
import ca.uhn.fhir.rest.api.EncodingEnum;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.hapi.validation.FhirInstanceValidator;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r5.utils.IResourceValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

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
	private static TestEntry value;

	private static Set<String> classtypes = new HashSet<>();

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
			//if (id.contains("type-ref-checked.xml")) {
			//if (true) {
			if (ConvertorHelper.shouldTest(examples.get(id))) {
				objects.add(new Object[]{id, examples.get(id)});
			}
			//}
		}
		System.out.println();
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

		OperationOutcome mainOperationOutcome = new OperationOutcome();

		myFhirCtx.setParserErrorHandler(new LenientErrorHandler());
		validator.setAssumeValidRestReferences(testEntry.getProfile().getAssumeValidRestReferences());
		validator.setAllowExamples(testEntry.getAllowExamples());

		myDaoConfig.setAllowExternalReferences(true);

		if (testEntry.getUsesTest()) {
			String resourceAsString = loadResource(TEST_FILES_BASE_PATH + testFile);
			Assertions.assertNotNull(resourceAsString, "Could not load resource string from file <" + testFile + ">");
			validator.setValidatorResourceFetcher(new TestResourceFetcher(testEntry));

			if (testEntry.getProfiles() != null) {
				for (String filename : testEntry.getProfiles()) {
					String resource = loadResource(TEST_FILES_BASE_PATH + filename);
					IParser parser = EncodingEnum.detectEncoding(resource).newParser(myFhirCtx);
					IBaseResource resourceParsed = parser.parseResource(resource);
					String resourceName;
					if (resourceParsed.getIdElement().getResourceType() == null) {
						resourceName = myFhirCtx.getResourceDefinition(resourceParsed).getImplementingClass().getName();
						resourceName = resourceName.substring(resourceName.lastIndexOf('.') + 1);
						resourceParsed.setId(new IdDt().setParts(null, resourceName, UUID.randomUUID().toString(), "1"));
					}
					IFhirResourceDao dao = myDaoRegistry.getResourceDao(resourceParsed.getIdElement().getResourceType());
					dao.update(resourceParsed);
				}
			}

			if (testEntry.getProfile().getSource() != null) {
				String resource = loadResource(TEST_FILES_BASE_PATH + testEntry.getProfile().getSource());
				IParser parser = EncodingEnum.detectEncoding(resource).newParser(myFhirCtx);
				IBaseResource resourceParsed = parser.parseResource(resource);
				String resourceName = null;
				if (resourceParsed.getIdElement().getResourceType() == null) {
					resourceName = myFhirCtx.getResourceDefinition(resourceParsed).getImplementingClass().getName();
					resourceName = resourceName.substring(resourceName.lastIndexOf('.') + 1);
					resourceParsed.setId(new IdDt().setParts(null, resourceName, UUID.randomUUID().toString(), "1"));
				}
				IFhirResourceDao dao = myDaoRegistry.getResourceDao(resourceParsed.getIdElement().getResourceType());
				DaoMethodOutcome daoMethodOutcome = dao.update(resourceParsed);
				mainOperationOutcome = (OperationOutcome) daoMethodOutcome.getOperationOutcome();
				if (testEntry.getProfile().getTestResult() != null) {
					testEntry.getTestResult().merge(testEntry.getProfile().getTestResult());
				}
			}

			OperationOutcome validationResult = validate(testFile, testEntry.getTestResult(), resourceAsString, null);

			mainOperationOutcome = CoreValidatorTestUtils.mergeOperationOutcomes(validationResult, mainOperationOutcome);
			CoreValidatorTestUtils.testOutputs(testEntry.getTestResult(), mainOperationOutcome);

		}
	}

	protected OperationOutcome validate(String testFile, TestResult testResult, String resourceAsString, String testProfile) {
		String resourceName = null;
		OperationOutcome operationOutcome = new OperationOutcome();

		try {
			resourceName = extractResourceName(testFile, resourceAsString);
			IFhirResourceDao<? extends IBaseResource> resourceDao = daoRegistry.getResourceDaoOrNull(resourceName);
			operationOutcome = CoreValidatorTestUtils.validate(myCtx, testProfile, resourceName, resourceAsString, resourceDao);
		} catch (Exception e) {
			operationOutcome.addIssue().setSeverity(OperationOutcome.IssueSeverity.ERROR).setDiagnostics(e.getMessage());
		}
		return operationOutcome;
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

}
