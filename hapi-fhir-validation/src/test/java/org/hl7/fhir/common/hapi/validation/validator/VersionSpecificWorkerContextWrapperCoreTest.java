package org.hl7.fhir.common.hapi.validation.validator;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.test.utilities.LoggingExtension;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.hapi.converters.canonical.VersionCanonicalizer;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.hl7.fhir.common.hapi.validation.support.CommonCodeSystemsTerminologyService;
import org.hl7.fhir.common.hapi.validation.support.InMemoryTerminologyServerValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.UnknownCodeSystemWarningValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain;
import org.hl7.fhir.convertors.factory.VersionConvertorFactory_10_50;
import org.hl7.fhir.convertors.factory.VersionConvertorFactory_14_50;
import org.hl7.fhir.convertors.factory.VersionConvertorFactory_30_50;
import org.hl7.fhir.convertors.factory.VersionConvertorFactory_40_50;
import org.hl7.fhir.exceptions.DefinitionException;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.exceptions.FHIRFormatError;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.formats.JsonParser;
import org.hl7.fhir.r5.formats.XmlParser;
import org.hl7.fhir.r5.model.CodeSystem;
import org.hl7.fhir.r5.model.Constants;
import org.hl7.fhir.r5.model.Parameters;
import org.hl7.fhir.r5.model.Resource;
import org.hl7.fhir.r5.model.ValueSet;
import org.hl7.fhir.r5.test.utils.TestingUtilities;
import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.utilities.json.model.JsonObject;
import org.hl7.fhir.validation.special.TxServiceTestHelper;
import org.hl7.fhir.validation.special.TxTestData;
import org.hl7.fhir.validation.special.TxTestSetup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.quality.Strictness;
import org.mockito.stubbing.Answer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

/**
 This test class is included as useful debugging code.

 It is not currently intended to pass.

 It will remain disabled so as not to impede the build, but can be enabled in order to debug issues relating to the
 validation of codes and code systems.

 This test class was derived from an original test in org.hl7.fhir.core:

 https://github.com/hapifhir/org.hl7.fhir.core/blob/6.3.7/org.hl7.fhir.validation/src/test/java/org/hl7/fhir/terminology/tests/TerminologyServiceTests.java

 The original series of tests is loaded from the fhir-test-cases repository:

 https://github.com/FHIR/fhir-test-cases/tree/1.5.7/tx

 In the original test cases were executed using the core implementation of IWorkerContext, which is passed to this
 utility method:

 https://github.com/hapifhir/org.hl7.fhir.core/blob/d81fc5d82d0297d1a6f4b38bd9b81e52f859eb4f/org.hl7.fhir.validation/src/main/java/org/hl7/fhir/validation/special/TxServiceTestHelper.java#L25

 This executes a diff of the actual and expected results. In the core implementation of IWorkerContext, an actual server
 is used for terminology expansion and validation, and actual test results are meant to equal the expected ones.

 In HAPI,  we use a completely different implementation of IWorkerContext, with error messages that differ from the core
 implementation. InMemoryTerminologyServerValidationSupport for example, should not produce the same error code as a
 FHIR terminology server. Thus, these tests will produce different error messages, and will almost always fail. However,
 they can be used to debug the validation process, as well as to compare our validation results to the core.

 You can produce diffs in a local directory by setting the TX_SERVICE_TEST_DIFF_TARGET environment variable.

 The code below is left in the last 'working' state. It may contain dead code and other artifacts of being derived from
 the core library. It may have to be updated to suit a particular debugging task. The documentation above is meant to
 provide some guidance on how to derive similar debugging code from the org.hl7.fhir.core test cases.
 */
@Disabled
public class VersionSpecificWorkerContextWrapperCoreTest {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(VersionSpecificWorkerContextWrapperCoreTest.class);

	private final Map<String, ValueSet> myValueSets = new HashMap<>();

	private final Map<String, CodeSystem> myCodeSystems = new HashMap<>();
	private static FhirContext ourCtx = FhirContext.forR5();
	private static DefaultProfileValidationSupport myDefaultValidationSupport = new DefaultProfileValidationSupport(ourCtx);
	@RegisterExtension
	public LoggingExtension myLoggingExtension = new LoggingExtension();
	private FhirInstanceValidator myInstanceVal;
	private Map<String, ValueSet.ValueSetExpansionComponent> mySupportedCodeSystemsForExpansion;
	private FhirValidator myVal;
	private ValidationSupportChain myValidationSupport;


	private static final String VALIDATE_CODE_OPERATION = "validate-code";

	private static final String VALIDATE_CODESYSTEM_OPERATION = "cs-validate-code";

	private static final String EXPAND_OPERATION = "expand";

	private VersionSpecificWorkerContextWrapper wrapper;

	VersionCanonicalizer versionCanonicalizer = new VersionCanonicalizer(FhirContext.forR5Cached());


	@BeforeEach
	public void before() {
		myVal = ourCtx.newValidator();
		myVal.setValidateAgainstStandardSchema(false);
		myVal.setValidateAgainstStandardSchematron(false);

		IValidationSupport mockSupport = mock(IValidationSupport.class, withSettings().strictness(Strictness.LENIENT));
		when(mockSupport.getFhirContext()).thenReturn(ourCtx);


		when(mockSupport.fetchResource(eq(ValueSet.class), nullable(String.class)))
			.thenAnswer(new Answer<ValueSet>() {
							@Override
							public ValueSet answer(InvocationOnMock theInvocation) {
								String url = (String) theInvocation.getArguments()[1];

								ourLog.info("Looking for valueSet: " + url + "... " + (myValueSets.containsKey(url) ? "found" : "not found"));
								return myValueSets.get(url);
							}
						}
			);

		when(mockSupport.fetchValueSet(nullable(String.class)))
			.thenAnswer(new Answer<ValueSet>() {
							@Override
							public ValueSet answer(InvocationOnMock theInvocation) {
								String url = (String) theInvocation.getArguments()[0];

								ourLog.info("Looking for valueSet: " + url + "... " + (myValueSets.containsKey(url) ? "found" : "not found"));
								return myValueSets.get(url);
							}
						}
			);

		when(mockSupport.fetchCodeSystem(nullable(String.class)))
			.thenAnswer(new Answer<IBaseResource>() {
							@Override
							public IBaseResource answer(InvocationOnMock theInvocation) {
								String url = (String) theInvocation.getArguments()[0];

								ourLog.info("Looking for codeSystem: " + url);
								return  myCodeSystems.get(url);
							}
						}
			);

		UnknownCodeSystemWarningValidationSupport unknownCodeSystemWarningValidationSupport = new UnknownCodeSystemWarningValidationSupport(ourCtx);
		unknownCodeSystemWarningValidationSupport.setNonExistentCodeSystemSeverity(IValidationSupport.IssueSeverity.WARNING);
		myValidationSupport =
			new ValidationSupportChain(
				mockSupport,
				myDefaultValidationSupport,
				new InMemoryTerminologyServerValidationSupport(ourCtx),
				new CommonCodeSystemsTerminologyService(ourCtx),
				unknownCodeSystemWarningValidationSupport);
		myInstanceVal = new FhirInstanceValidator(myValidationSupport);

		wrapper = new VersionSpecificWorkerContextWrapper(new ValidationSupportContext(myValidationSupport), versionCanonicalizer);
		wrapper.setExpansionParameters(new Parameters());
	}

	public static Stream<Arguments> argumentSource() throws IOException {
		TxTestData data = TxTestData.loadTestDataFromDefaultClassPath();

		return data.getTestData().stream()
			.filter( entry -> {
					TxTestSetup testSetup = (TxTestSetup) entry[1];
					return testSetup.getTest().asString("operation").equals("validate-code")
						|| testSetup.getTest().asString("operation").equals("cs-validate-code");
			}
			)
			.map(
			entry ->
				Arguments.of(entry[0], data, entry[1]));
	}

	@ParameterizedTest(name = "{0}")
	@MethodSource("argumentSource")
	public void fhirTestCasesCodeValidationTest(String name, TxTestData testData, TxTestSetup setup) throws IOException {
		ourLog.info("Name: " + name);

		for (String s : setup.getSuite().forceArray("setup").asStrings()) {

			Resource res = loadResource(s);
			if ("ValueSet".equals(res.getResourceType().toString())) {
				ValueSet valueSet = (ValueSet)res;
				myValueSets.put(valueSet.getUrl(), valueSet);
			} else if ("CodeSystem".equals(res.getResourceType().toString())) {
				CodeSystem codeSystem = (CodeSystem) res;
				myCodeSystems.put(codeSystem.getUrl(), codeSystem);
			} else {
				ourLog.info("Can't load setup resource: " + s);
			}
		}
		Resource req = loadResource(setup.getTest().asString("request"));
		String fn = setup.getTest().has("response:tx.fhir.org") ? setup.getTest().asString("response:tx.fhir.org") : setup.getTest().asString("response");
		String resp = TestingUtilities.loadTestResource("tx", fn);
		String fp = Utilities.path("[tmp]", "tx", fn);
		JsonObject ext = testData.getExternals() == null ? null : testData.getExternals().getJsonObject(fn);
		File fo = new File(fp);
		if (fo.exists()) {
			fo.delete();
		}
		 if (setup.getTest().asString("operation").equals("validate-code")) {
			String diff = TxServiceTestHelper.getDiffForValidation(setup.getTest().str("name"), wrapper, setup.getTest().asString("name"), req, resp, setup.getTest().asString("Content-Language"), fp, ext, false);
			assertNull(diff, diff);
		} else if (setup.getTest().asString("operation").equals("cs-validate-code")) {
			String diff = TxServiceTestHelper.getDiffForValidation(setup.getTest().str("name"), wrapper, setup.getTest().asString("name"), req, resp, setup.getTest().asString("Content-Language"), fp, ext, true);
			assertNull(diff, diff);
		}
	}

	public Resource loadResource(String filename) throws IOException, FHIRFormatError, FileNotFoundException, FHIRException, DefinitionException {
		final String version = "5.0.0";
		String contents = TestingUtilities.loadTestResource("tx", filename);
		try (InputStream inputStream = IOUtils.toInputStream(contents, Charsets.UTF_8)) {
			if (filename.contains(".json")) {
				if (Constants.VERSION.equals(version) || "5.0".equals(version))
					return new JsonParser().parse(inputStream);
				else if (org.hl7.fhir.dstu3.model.Constants.VERSION.equals(version) || "3.0".equals(version))
					return VersionConvertorFactory_30_50.convertResource(new org.hl7.fhir.dstu3.formats.JsonParser().parse(inputStream));
				else if (org.hl7.fhir.dstu2016may.model.Constants.VERSION.equals(version) || "1.4".equals(version))
					return VersionConvertorFactory_14_50.convertResource(new org.hl7.fhir.dstu2016may.formats.JsonParser().parse(inputStream));
				else if (org.hl7.fhir.dstu2.model.Constants.VERSION.equals(version) || "1.0".equals(version))
					return VersionConvertorFactory_10_50.convertResource(new org.hl7.fhir.dstu2.formats.JsonParser().parse(inputStream));
				else if (org.hl7.fhir.r4.model.Constants.VERSION.equals(version) || "4.0".equals(version))
					return VersionConvertorFactory_40_50.convertResource(new org.hl7.fhir.r4.formats.JsonParser().parse(inputStream));
				else
					throw new FHIRException("unknown version " + version);
			} else {
				if (Constants.VERSION.equals(version) || "5.0".equals(version))
					return new XmlParser().parse(inputStream);
				else if (org.hl7.fhir.dstu3.model.Constants.VERSION.equals(version) || "3.0".equals(version))
					return VersionConvertorFactory_30_50.convertResource(new org.hl7.fhir.dstu3.formats.XmlParser().parse(inputStream));
				else if (org.hl7.fhir.dstu2016may.model.Constants.VERSION.equals(version) || "1.4".equals(version))
					return VersionConvertorFactory_14_50.convertResource(new org.hl7.fhir.dstu2016may.formats.XmlParser().parse(inputStream));
				else if (org.hl7.fhir.dstu2.model.Constants.VERSION.equals(version) || "1.0".equals(version))
					return VersionConvertorFactory_10_50.convertResource(new org.hl7.fhir.dstu2.formats.XmlParser().parse(inputStream));
				else if (org.hl7.fhir.r4.model.Constants.VERSION.equals(version) || "4.0".equals(version))
					return VersionConvertorFactory_40_50.convertResource(new org.hl7.fhir.r4.formats.XmlParser().parse(inputStream));
				else
					throw new FHIRException("unknown version " + version);
			}
		}
	}
}
