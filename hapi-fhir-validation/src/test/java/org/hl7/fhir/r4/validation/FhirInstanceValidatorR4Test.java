package org.hl7.fhir.r4.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.ConceptValidationOptions;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.ValueSetExpansionOptions;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.test.BaseTest;
import ca.uhn.fhir.util.TestUtil;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ResultSeverityEnum;
import ca.uhn.fhir.validation.SingleValidationMessage;
import ca.uhn.fhir.validation.ValidationResult;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.common.hapi.validation.support.CachingValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.CommonCodeSystemsTerminologyService;
import org.hl7.fhir.common.hapi.validation.support.PrePopulatedValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.InMemoryTerminologyServerValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.conformance.ProfileUtilities;
import org.hl7.fhir.r4.context.IWorkerContext;
import org.hl7.fhir.r4.hapi.ctx.HapiWorkerContext;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.hl7.fhir.r4.model.*;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.Observation.ObservationStatus;
import org.hl7.fhir.r4.model.StructureDefinition.StructureDefinitionKind;
import org.hl7.fhir.r4.model.ValueSet.ValueSetExpansionComponent;
import org.hl7.fhir.r4.terminologies.ValueSetExpander;
import org.hl7.fhir.r4.utils.FHIRPathEngine;
import org.hl7.fhir.r5.utils.IResourceValidator;
import org.hl7.fhir.utilities.validation.ValidationMessage;
import org.hl7.fhir.utilities.xhtml.XhtmlNode;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FhirInstanceValidatorR4Test extends BaseTest {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirInstanceValidatorR4Test.class);
	private static FhirContext ourCtx = FhirContext.forR4();
	private static DefaultProfileValidationSupport myDefaultValidationSupport = new DefaultProfileValidationSupport(ourCtx);
	@Rule
	public TestRule watcher = new TestWatcher() {
		@Override
		protected void starting(Description description) {
			ourLog.info("Starting test: " + description.getMethodName());
		}
	};
	private FhirInstanceValidator myInstanceVal;
	private Map<String, ValueSetExpansionComponent> mySupportedCodeSystemsForExpansion;
	private FhirValidator myVal;
	private ArrayList<String> myValidConcepts;
	private Set<String> myValidSystems = new HashSet<>();
	private CachingValidationSupport myValidationSupport;

	private void addValidConcept(String theSystem, String theCode) {
		myValidSystems.add(theSystem);
		myValidConcepts.add(theSystem + "___" + theCode);
	}

	/**
	 * An invalid local reference should not cause a ServiceException.
	 */
	@Test
	public void testInvalidLocalReference() {
		QuestionnaireResponse resource = new QuestionnaireResponse();
		resource.setStatus(QuestionnaireResponse.QuestionnaireResponseStatus.COMPLETED);
		resource.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);

		resource.setSubject(new Reference("#invalid-ref"));

		ValidationResult output = myVal.validateWithResult(resource);
		List<SingleValidationMessage> nonInfo = logResultsAndReturnNonInformationalOnes(output);
		assertThat(nonInfo, hasSize(2));
	}

	@SuppressWarnings("unchecked")
	@Before
	public void before() {
		myVal = ourCtx.newValidator();
		myVal.setValidateAgainstStandardSchema(false);
		myVal.setValidateAgainstStandardSchematron(false);

		IValidationSupport mockSupport = mock(IValidationSupport.class);
		when(mockSupport.getFhirContext()).thenReturn(ourCtx);

		ValidationSupportChain chain = new ValidationSupportChain(myDefaultValidationSupport, mockSupport, new InMemoryTerminologyServerValidationSupport(ourCtx), new CommonCodeSystemsTerminologyService(ourCtx));
		myValidationSupport = new CachingValidationSupport(chain);
		myInstanceVal = new FhirInstanceValidator(myValidationSupport);

		myVal.registerValidatorModule(myInstanceVal);

		mySupportedCodeSystemsForExpansion = new HashMap<>();

		myValidConcepts = new ArrayList<>();

		when(mockSupport.expandValueSet(any(), nullable(ValueSetExpansionOptions.class), any())).thenAnswer(t -> {
			ValueSet arg = (ValueSet) t.getArgument(2, IBaseResource.class);
			ValueSetExpansionComponent retVal = mySupportedCodeSystemsForExpansion.get(arg.getCompose().getIncludeFirstRep().getSystem());
			if (retVal == null) {
				IBaseResource outcome = myDefaultValidationSupport.expandValueSet(myDefaultValidationSupport, null, arg).getValueSet();
				return outcome;
			}
			ourLog.debug("expandValueSet({}) : {}", new Object[]{t.getArguments()[0], retVal});

			ValueSet valueset = new ValueSet();
			valueset.setExpansion(retVal);
			return new ValueSetExpander.ValueSetExpansionOutcome(valueset);
		});
		when(mockSupport.isCodeSystemSupported(any(), nullable(String.class))).thenAnswer(new Answer<Boolean>() {
			@Override
			public Boolean answer(InvocationOnMock theInvocation) {
				String argument = theInvocation.getArgument(1, String.class);
				boolean retVal = myValidSystems.contains(argument);
				ourLog.debug("isCodeSystemSupported({}) : {}", argument, retVal);
				return retVal;
			}
		});
		when(mockSupport.fetchResource(nullable(Class.class), nullable(String.class))).thenAnswer(new Answer<IBaseResource>() {
			@Override
			public IBaseResource answer(InvocationOnMock theInvocation) throws Throwable {
				IBaseResource retVal;
				Class<IBaseResource> clazz = (Class<IBaseResource>) theInvocation.getArguments()[0];
				String id = theInvocation.getArgument(1, String.class);
				if ("Questionnaire/q_jon".equals(id)) {
					retVal = ourCtx.newJsonParser().parseResource(loadResource("/q_jon.json"));
				} else {
					retVal = myDefaultValidationSupport.fetchResource(clazz, id);
				}
				ourLog.debug("fetchResource({}, {}) : {}", clazz, id, retVal);
				return retVal;
			}
		});
		when(mockSupport.validateCode(any(), any(), nullable(String.class), nullable(String.class), nullable(String.class), nullable(String.class))).thenAnswer(new Answer<IValidationSupport.CodeValidationResult>() {
			@Override
			public IValidationSupport.CodeValidationResult answer(InvocationOnMock theInvocation) {
				ConceptValidationOptions options = theInvocation.getArgument(1, ConceptValidationOptions.class);
				String system = theInvocation.getArgument(2, String.class);
				String code = theInvocation.getArgument(3, String.class);
				String display = theInvocation.getArgument(4, String.class);
				String valueSetUrl = theInvocation.getArgument(5, String.class);
				IValidationSupport.CodeValidationResult retVal;
				if (myValidConcepts.contains(system + "___" + code)) {
					retVal = new IValidationSupport.CodeValidationResult().setCode(code);
				} else if (myValidSystems.contains(system)) {
					return new IValidationSupport.CodeValidationResult().setSeverityCode(ValidationMessage.IssueSeverity.WARNING.toCode()).setMessage("Unknown code: " + system + " / " + code);
				} else {
					retVal = myDefaultValidationSupport.validateCode(myDefaultValidationSupport, options, system, code, display, valueSetUrl);
				}
				ourLog.debug("validateCode({}, {}, {}, {}) : {}", system, code, display, valueSetUrl, retVal);
				return retVal;
			}
		});
		when(mockSupport.fetchCodeSystem(nullable(String.class))).thenAnswer(new Answer<CodeSystem>() {
			@Override
			public CodeSystem answer(InvocationOnMock theInvocation) {
				CodeSystem retVal = (CodeSystem) myDefaultValidationSupport.fetchCodeSystem((String) theInvocation.getArguments()[0]);
				ourLog.debug("fetchCodeSystem({}) : {}", new Object[]{theInvocation.getArguments()[0], retVal});
				return retVal;
			}
		});
		when(mockSupport.fetchStructureDefinition(nullable(String.class))).thenAnswer(new Answer<IBaseResource>() {
			@Override
			public IBaseResource answer(InvocationOnMock theInvocation) {
				IBaseResource retVal = myDefaultValidationSupport.fetchStructureDefinition((String) theInvocation.getArguments()[0]);
				ourLog.debug("fetchStructureDefinition({}) : {}", new Object[]{theInvocation.getArguments()[0], retVal});
				return retVal;
			}
		});
		when(mockSupport.fetchAllStructureDefinitions()).thenAnswer(new Answer<List<StructureDefinition>>() {
			@Override
			public List<StructureDefinition> answer(InvocationOnMock theInvocation) {
				List<StructureDefinition> retVal = myDefaultValidationSupport.fetchAllStructureDefinitions();
				ourLog.debug("fetchAllStructureDefinitions()", new Object[]{});
				return retVal;
			}
		});

	}

	private Object defaultString(Integer theLocationLine) {
		return theLocationLine != null ? theLocationLine.toString() : "";
	}

	private StructureDefinition loadStructureDefinition(DefaultProfileValidationSupport theDefaultValSupport, String theResName) throws IOException, FHIRException {
		StructureDefinition derived = loadResource(ourCtx, StructureDefinition.class, theResName);
		StructureDefinition base = (StructureDefinition) theDefaultValSupport.fetchStructureDefinition(derived.getBaseDefinition());
		Validate.notNull(base);

		IWorkerContext worker = new HapiWorkerContext(ourCtx, theDefaultValSupport);
		List<ValidationMessage> issues = new ArrayList<>();
		ProfileUtilities profileUtilities = new ProfileUtilities(worker, issues, null);
		profileUtilities.generateSnapshot(base, derived, "", "", "");

		return derived;
	}

	private List<SingleValidationMessage> logResultsAndReturnAll(ValidationResult theOutput) {
		List<SingleValidationMessage> retVal = new ArrayList<>();

		int index = 0;
		for (SingleValidationMessage next : theOutput.getMessages()) {
			ourLog.info("Result {}: {} - {}:{} {} - {}",
				index, next.getSeverity(), defaultString(next.getLocationLine()), defaultString(next.getLocationCol()), next.getLocationString(), next.getMessage());
			index++;

			retVal.add(next);
		}
		return retVal;
	}

	@Test
	public void testValidateCodeWithTailingSpace() {
		Patient p = new Patient();
		p
			.getMaritalStatus()
			.addCoding()
			.setSystem("http://foo")
			.setCode("AA  ");

		FhirValidator val = ourCtx.newValidator();
		val.registerValidatorModule(new FhirInstanceValidator(myValidationSupport));

		ValidationResult result = val.validateWithResult(p);
		List<SingleValidationMessage> all = logResultsAndReturnErrorOnes(result);
		assertFalse(result.isSuccessful());
		assertEquals("The code \"AA  \" is not valid (whitespace rules)", all.get(0).getMessage());

	}

	/**
	 * See #938
	 */
	@Test
	public void testValidateEmptyElement() {
		String input = "<Patient xmlns=\"http://hl7.org/fhir\">" +
			"  <text>\n" +
			"    <status value=\"generated\"/>\n" +
			"    <div xmlns=\"http://www.w3.org/1999/xhtml\">AAA</div>\n" +
			"  </text>" +
			"  <active value=\"\"/>" +
			"</Patient>";

		FhirValidator val = ourCtx.newValidator();
		val.registerValidatorModule(new FhirInstanceValidator(myValidationSupport));

		ValidationResult result = val.validateWithResult(input);
		List<SingleValidationMessage> all = logResultsAndReturnAll(result);
		assertFalse(result.isSuccessful());
		assertEquals("@value cannot be empty", all.get(0).getMessage());
	}

	/**
	 * See #1740
	 */
	@Test
	public void testValidateScalarInRepeatableField() {
		String operationDefinition = "{\n" +
			"  \"resourceType\": \"OperationDefinition\",\n" +
			"  \"name\": \"Questionnaire\",\n" +
			"  \"status\": \"draft\",\n" +
			"  \"kind\" : \"operation\",\n" +
			"  \"code\": \"populate\",\n" +
			"  \"resource\": \"Patient\",\n" + // should be array
			"  \"system\": false,\n" + " " +
			" \"type\": false,\n" +
			"  \"instance\": true\n" +
			"}";

		FhirValidator val = ourCtx.newValidator();
		val.registerValidatorModule(new FhirInstanceValidator(myDefaultValidationSupport));

		ValidationResult result = val.validateWithResult(operationDefinition);
		List<SingleValidationMessage> all = logResultsAndReturnAll(result);
		assertFalse(result.isSuccessful());
		assertEquals("This property must be an Array, not a a primitive property", all.get(0).getMessage());
	}

	/**
	 * See #1676 - We should ignore schema location
	 */
	@Test
	public void testValidateResourceWithSchemaLocation() {
		String input = "<Patient xmlns=\"http://hl7.org/fhir\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://hl7.org/fhir ../../schema/foo.xsd\">" +
			"  <text>\n" +
			"    <status value=\"generated\"/>\n" +
			"    <div xmlns=\"http://www.w3.org/1999/xhtml\">AAA</div>\n" +
			"  </text>" +
			"  <active value=\"true\"/>" +
			"</Patient>";

		FhirValidator val = ourCtx.newValidator();
		val.registerValidatorModule(new FhirInstanceValidator(myValidationSupport));

		ValidationResult result = val.validateWithResult(input);
		logResultsAndReturnAll(result);
		assertTrue(result.isSuccessful());
	}

	/**
	 * See #942
	 */
	@Test
	public void testValidateDoesntEnforceBestPracticesByDefault() {
		Observation input = new Observation();
		input.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);
		input.setStatus(ObservationStatus.AMENDED);
		input.getCode().addCoding().setSystem("http://loinc.org").setCode("1234").setDisplay("FOO");

		FhirInstanceValidator instanceModule;
		FhirValidator val;
		ValidationResult result;
		List<SingleValidationMessage> all;

		// With BPs disabled
		val = ourCtx.newValidator();
		instanceModule = new FhirInstanceValidator(myValidationSupport);
		val.registerValidatorModule(instanceModule);
		result = val.validateWithResult(input);
		all = logResultsAndReturnAll(result);
		assertTrue(result.isSuccessful());
		assertThat(all, empty());

		// With BPs enabled
		val = ourCtx.newValidator();
		instanceModule = new FhirInstanceValidator(myValidationSupport);
		IResourceValidator.BestPracticeWarningLevel level = IResourceValidator.BestPracticeWarningLevel.Error;
		instanceModule.setBestPracticeWarningLevel(level);
		val.registerValidatorModule(instanceModule);
		result = val.validateWithResult(input);
		all = logResultsAndReturnAll(result);
		assertFalse(result.isSuccessful());
		assertEquals("All observations should have a subject", all.get(0).getMessage());
	}


	private List<SingleValidationMessage> logResultsAndReturnNonInformationalOnes(ValidationResult theOutput) {
		List<SingleValidationMessage> retVal = new ArrayList<>();

		int index = 0;
		for (SingleValidationMessage next : theOutput.getMessages()) {
			ourLog.info("Result {}: {} - {} - {}", index, next.getSeverity(), next.getLocationString(), next.getMessage());
			index++;

			if (next.getSeverity() != ResultSeverityEnum.INFORMATION) {
				retVal.add(next);
			}
		}

		return retVal;
	}

	private List<SingleValidationMessage> logResultsAndReturnErrorOnes(ValidationResult theOutput) {
		List<SingleValidationMessage> retVal = new ArrayList<>();

		int index = 0;
		for (SingleValidationMessage next : theOutput.getMessages()) {
			ourLog.info("Result {}: {} - {} - {}", index, next.getSeverity(), next.getLocationString(), next.getMessage());
			index++;

			if (next.getSeverity().ordinal() > ResultSeverityEnum.WARNING.ordinal()) {
				retVal.add(next);
			}
		}

		return retVal;
	}

	@Test
	public void testBase64Invalid() {
		Base64BinaryType value = new Base64BinaryType(new byte[]{2, 3, 4, 5, 6, 7, 8, 9, 8, 7, 6, 5, 4, 3, 2, 1});
		Media med = new Media();
		med.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);
		med.getContent().setContentType(Constants.CT_OCTET_STREAM);
		med.getContent().setDataElement(value);
		med.getContent().setTitle("bbbb syst");
		med.setStatus(Media.MediaStatus.NOTDONE);
		String encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(med);

		encoded = encoded.replace(value.getValueAsString(), "%%%2@()()");

		ourLog.info("Encoded: {}", encoded);

		ValidationResult output = myVal.validateWithResult(encoded);
		List<SingleValidationMessage> errors = logResultsAndReturnNonInformationalOnes(output);
		assertEquals(1, errors.size());
		assertEquals("The value \"%%%2@()()\" is not a valid Base64 value", errors.get(0).getMessage());

	}

	@Test
	public void testValidateBundleWithNoFullUrl() throws IOException {
		String encoded = loadResource("/r4/r4-caredove-bundle.json");

		ValidationResult output = myVal.validateWithResult(encoded);
		List<SingleValidationMessage> errors = logResultsAndReturnNonInformationalOnes(output);
		errors = errors
			.stream()
			.filter(t -> t.getMessage().contains("Bundle entry missing fullUrl"))
			.collect(Collectors.toList());
		assertEquals(5, errors.size());
	}

	@Test
	public void testBase64Valid() {
		Base64BinaryType value = new Base64BinaryType(new byte[]{2, 3, 4, 5, 6, 7, 8, 9, 8, 7, 6, 5, 4, 3, 2, 1});
		Media med = new Media();
		med.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);
		med.getContent().setContentType(Constants.CT_OCTET_STREAM);
		med.getContent().setDataElement(value);
		med.getContent().setTitle("bbbb syst");
		med.setStatus(Media.MediaStatus.NOTDONE);
		String encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(med);

		ourLog.info("Encoded: {}", encoded);

		ValidationResult output = myVal.validateWithResult(encoded);
		List<SingleValidationMessage> errors = logResultsAndReturnNonInformationalOnes(output);
		assertEquals(0, errors.size());

	}

	/**
	 * See #873
	 */
	@Test
	public void testCompareTimesWithDifferentTimezones() {
		Procedure procedure = new Procedure();
		procedure.setStatus(Procedure.ProcedureStatus.COMPLETED);
		procedure.getSubject().setReference("Patient/1");
		procedure.getCode().setText("Some proc");

		Period period = new Period();
		period.setStartElement(new DateTimeType("2000-01-01T00:00:01+05:00"));
		period.setEndElement(new DateTimeType("2000-01-01T00:00:00+04:00"));
		assertThat(period.getStart().getTime(), lessThan(period.getEnd().getTime()));
		procedure.setPerformed(period);

		FhirValidator val = ourCtx.newValidator();
		val.registerValidatorModule(myInstanceVal);

		ValidationResult result = val.validateWithResult(procedure);

		String encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(result.toOperationOutcome());
		ourLog.info(encoded);

		assertTrue(result.isSuccessful());
	}

	/**
	 * See #531
	 */
	@Test
	public void testContactPointSystemUrlWorks() {
		Patient p = new Patient();
		p.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);
		ContactPoint t = p.addTelecom();
		t.setSystem(org.hl7.fhir.r4.model.ContactPoint.ContactPointSystem.URL);
		t.setValue("http://infoway-inforoute.ca");

		ValidationResult results = myVal.validateWithResult(p);
		List<SingleValidationMessage> outcome = logResultsAndReturnNonInformationalOnes(results);
		assertThat(outcome, empty());

	}

	/**
	 * See #872
	 */
	@Test
	public void testExtensionUrlWithHl7Url() throws IOException {
		String input = IOUtils.toString(FhirInstanceValidator.class.getResourceAsStream("/bug872-ext-with-hl7-url.json"), Charsets.UTF_8);
		ValidationResult output = myVal.validateWithResult(input);
		List<SingleValidationMessage> nonInfo = logResultsAndReturnNonInformationalOnes(output);
		assertThat(nonInfo, empty());
	}

	@Test
	public void testIsNoTerminologyChecks() {
		assertFalse(myInstanceVal.isNoTerminologyChecks());
		myInstanceVal.setNoTerminologyChecks(true);
		assertTrue(myInstanceVal.isNoTerminologyChecks());
	}

	@Test
	public void testLargeBase64() throws IOException {
		String input = IOUtils.toString(FhirInstanceValidatorR4Test.class.getResourceAsStream("/r4/diagnosticreport-example-gingival-mass.json"), Constants.CHARSET_UTF8);
		ValidationResult output = myVal.validateWithResult(input);
		List<SingleValidationMessage> errors = logResultsAndReturnAll(output);
		assertEquals(1, errors.size());
		assertEquals("None of the codes provided are in the value set http://hl7.org/fhir/ValueSet/report-codes (http://hl7.org/fhir/ValueSet/report-codes, and a code is recommended to come from this value set) (codes = http://loinc.org#1-8)", errors.get(0).getMessage());
	}

	@Test
	@Ignore
	public void testValidateBigRawJsonResource() throws Exception {
		String input = super.loadCompressedResource("/conformance.json.gz");

		long start = System.currentTimeMillis();
		ValidationResult output = null;
		int passes = 1;
		for (int i = 0; i < passes; i++) {
			ourLog.info("Pass {}", i + 1);
			output = myVal.validateWithResult(input);
		}

		long delay = System.currentTimeMillis() - start;
		long per = delay / passes;

		logResultsAndReturnAll(output);

		ourLog.info("Took {} ms -- {}ms / pass", delay, per);
	}

	@Test
	@Ignore
	public void testValidateBuiltInProfiles() throws Exception {
		org.hl7.fhir.r4.model.Bundle bundle;
		String name = "profiles-resources";
		ourLog.info("Uploading " + name);
		String vsContents = loadResource("/org/hl7/fhir/r4/model/profile/" + name + ".xml");

		TreeSet<String> ids = new TreeSet<>();

		bundle = ourCtx.newXmlParser().parseResource(org.hl7.fhir.r4.model.Bundle.class, vsContents);
		for (BundleEntryComponent i : bundle.getEntry()) {
			org.hl7.fhir.r4.model.Resource next = i.getResource();
			ids.add(next.getId());

			if (next instanceof StructureDefinition) {
				StructureDefinition sd = (StructureDefinition) next;
				if (sd.getKind() == StructureDefinitionKind.LOGICAL) {
					ourLog.info("Skipping logical type: {}", next.getId());
					continue;
				}
			}

			ourLog.info("Validating {}", next.getId());
			ourLog.trace(ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(next));

			ValidationResult output = myVal.validateWithResult(next);
			List<SingleValidationMessage> results = logResultsAndReturnAll(output);

			// This isn't a validator problem but a definition problem.. it should get fixed at some point and
			// we can remove this. Tracker #17207 was filed about this
			// https://gforge.hl7.org/gf/project/fhir/tracker/?action=TrackerItemEdit&tracker_item_id=17207
			if (next.getId().equalsIgnoreCase("http://hl7.org/fhir/OperationDefinition/StructureDefinition-snapshot")) {
				assertEquals(1, results.size());
				assertEquals("A search type can only be specified for parameters of type string [searchType.exists() implies type = 'string']", results.get(0).getMessage());
				continue;
			}


			List<SingleValidationMessage> errors = results
				.stream()
				.filter(t -> t.getSeverity() != ResultSeverityEnum.INFORMATION)
				.collect(Collectors.toList());

			assertThat("Failed to validate " + i.getFullUrl() + " - " + errors, errors, empty());
		}

		ourLog.info("Validated the following:\n{}", ids);
	}

	@Test
	public void testValidateBundleWithNoType() throws Exception {
		String vsContents = loadResource("/r4/bundle-with-no-type.json");

		ValidationResult output = myVal.validateWithResult(vsContents);
		logResultsAndReturnNonInformationalOnes(output);
		assertThat(output.getMessages().toString(), containsString("Element 'Bundle.type': minimum required = 1"));
	}

	/**
	 * See #848
	 */
	@Test
	public void testValidateJsonWithDuplicateEntries() {
		String patient = "{" +
			"\"resourceType\":\"Patient\", " +
			"\"active\": true, " +
			"\"name\":[ {\"family\":\"foo\"} ]," +
			"\"name\":[ {\"family\":\"bar\"} ]" +
			"}";

		ValidationResult output = myVal.validateWithResult(patient);
		logResultsAndReturnNonInformationalOnes(output);
		assertThat(output.getMessages().toString(), containsString("Error parsing JSON source: Duplicated property name"));
	}

	@Test
	@Ignore
	public void testValidateBundleWithObservations() throws Exception {
		String name = "profiles-resources";
		ourLog.info("Uploading " + name);
		String inputString;
		inputString = loadResource("/brian_reinhold_bundle.json");
		Bundle bundle = ourCtx.newJsonParser().parseResource(Bundle.class, inputString);

		FHIRPathEngine fp = new FHIRPathEngine(new HapiWorkerContext(ourCtx, myValidationSupport));
		List<Base> fpOutput;
		BooleanType bool;

		fpOutput = fp.evaluate(bundle.getEntry().get(0).getResource(), "component.where(code = %resource.code).empty()");
		assertEquals(1, fpOutput.size());
		bool = (BooleanType) fpOutput.get(0);
		assertTrue(bool.getValue());
		//
		// fpOutput = fp.evaluate(bundle, "component.where(code = %resource.code).empty()");
		// assertEquals(1, fpOutput.size());
		// bool = (BooleanType) fpOutput.get(0);
		// assertTrue(bool.getValue());

		ValidationResult output = myVal.validateWithResult(inputString);
		List<SingleValidationMessage> errors = logResultsAndReturnNonInformationalOnes(output);
		assertThat(errors, empty());

	}

	@Test
	@Ignore
	public void testValidateDocument() throws Exception {
		String vsContents = loadResource("/sample-document.xml");

		ValidationResult output = myVal.validateWithResult(vsContents);
		logResultsAndReturnNonInformationalOnes(output);
		assertTrue(output.isSuccessful());
	}

	@Test
	public void testValidateProfileWithExtension() throws IOException, FHIRException {
		PrePopulatedValidationSupport valSupport = new PrePopulatedValidationSupport(ourCtx);
		DefaultProfileValidationSupport defaultSupport = new DefaultProfileValidationSupport(ourCtx);
		CachingValidationSupport support = new CachingValidationSupport(new ValidationSupportChain(defaultSupport, valSupport, new InMemoryTerminologyServerValidationSupport(ourCtx)));

		// Prepopulate SDs
		valSupport.addStructureDefinition(loadStructureDefinition(defaultSupport, "/r4/myconsent-profile.xml"));
		valSupport.addStructureDefinition(loadStructureDefinition(defaultSupport, "/r4/myconsent-ext.xml"));

		FhirValidator val = ourCtx.newValidator();
		val.registerValidatorModule(new FhirInstanceValidator(support));

		Consent input = super.loadResource(ourCtx, Consent.class, "/r4/myconsent-resource.json");

		input.getPolicyFirstRep().setAuthority("http://foo");
		//input.setScope(Consent.ConsentScope.ADR);
		input.getScope()
			.getCodingFirstRep()
			.setSystem("http://terminology.hl7.org/CodeSystem/consentscope")
			.setCode("adr");
		input.addCategory()
			.getCodingFirstRep()
			.setSystem("http://terminology.hl7.org/CodeSystem/consentcategorycodes")
			.setCode("acd");


		// Should pass
		ValidationResult output = val.validateWithResult(input);
		List<SingleValidationMessage> all = logResultsAndReturnErrorOnes(output);
		assertEquals(all.toString(), 0, all.size());

		// Now with the wrong datatype
		input.getExtensionsByUrl("http://hl7.org/fhir/StructureDefinition/PruebaExtension").get(0).setValue(new CodeType("AAA"));

		// Should fail
		output = val.validateWithResult(input);
		all = logResultsAndReturnErrorOnes(output);
		assertThat(all.toString(), containsString("definition allows for the types [string] but found type code"));

	}

	@Test
	@Ignore
	public void testValidateQuestionnaireResponse() throws IOException {
		String input = loadResource("/qr_jon.xml");

		ValidationResult output = myVal.validateWithResult(input);
		logResultsAndReturnAll(output);

		assertThat(output.getMessages().toString(), containsString("Items not of type group should not have items - Item with linkId 5.1 of type BOOLEAN has 1 item(s)"));
	}

	@Test
	public void testValidateRawJsonResource() {
		String input = "{" +
			"  \"resourceType\":\"Patient\"," +
			"  \"text\": {\n" +
			"    \"status\": \"generated\",\n" +
			"    \"div\": \"<div xmlns=\\\"http://www.w3.org/1999/xhtml\\\"> </div>\"\n" +
			"  },\n" +
			"  \"id\":\"123\"" +
			"}";

		ValidationResult output = myVal.validateWithResult(input);
		assertEquals(output.toString(), 0, output.getMessages().size());
	}

	@Test
	public void testValidateRawJsonResourceBadAttributes() {
		String input =
			"{" +
				"\"resourceType\":\"Patient\"," +
				"  \"text\": {\n" +
				"    \"status\": \"generated\",\n" +
				"    \"div\": \"<div xmlns=\\\"http://www.w3.org/1999/xhtml\\\"> </div>\"\n" +
				"  },\n" +
				"\"id\":\"123\"," +
				"\"foo\":\"123\"" +
				"}";

		ValidationResult output = myVal.validateWithResult(input);
		assertEquals(output.toString(), 1, output.getMessages().size());
		ourLog.info(output.getMessages().get(0).getLocationString());
		ourLog.info(output.getMessages().get(0).getMessage());
		assertEquals("Patient", output.getMessages().get(0).getLocationString());
		assertEquals("Unrecognised property '@foo'", output.getMessages().get(0).getMessage());

		OperationOutcome operationOutcome = (OperationOutcome) output.toOperationOutcome();
		ourLog.info(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(operationOutcome));
		assertEquals("Unrecognised property '@foo'", operationOutcome.getIssue().get(0).getDiagnostics());
		assertEquals("Patient", operationOutcome.getIssue().get(0).getLocation().get(0).getValue());
		assertEquals("Line 5, Col 24", operationOutcome.getIssue().get(0).getLocation().get(1).getValue());
	}

	@Test
	@Ignore
	public void testValidateRawJsonResourceFromExamples() throws Exception {
		String input = loadResource("/testscript-search.json");

		ValidationResult output = myVal.validateWithResult(input);
		logResultsAndReturnNonInformationalOnes(output);
		// assertEquals(output.toString(), 1, output.getMessages().size());
		// ourLog.info(output.getMessages().get(0).getLocationString());
		// ourLog.info(output.getMessages().get(0).getMessage());
		// assertEquals("/foo", output.getMessages().get(0).getLocationString());
		// assertEquals("Element is unknown or does not match any slice", output.getMessages().get(0).getMessage());
	}

	@Test
	public void testValidateRawJsonResourceWithUnknownExtension() {

		Patient patient = new Patient();
		patient.setId("1");
		patient.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);

		Extension ext = patient.addExtension();
		ext.setUrl("http://hl7.org/fhir/v3/ethnicity");
		ext.setValue(new CodeType("Hispanic or Latino"));

		String encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(patient);
		ourLog.info(encoded);

		/*
		 * {
		 * "resourceType": "Patient",
		 * "id": "1",
		 * "extension": [
		 * {
		 * "url": "http://hl7.org/fhir/v3/ethnicity",
		 * "valueCode": "Hispanic or Latino"
		 * }
		 * ]
		 * }
		 */

		ValidationResult output = myVal.validateWithResult(encoded);
		assertEquals(output.toString(), 1, output.getMessages().size());

		assertEquals("Unknown extension http://hl7.org/fhir/v3/ethnicity", output.getMessages().get(0).getMessage());
		assertEquals(ResultSeverityEnum.INFORMATION, output.getMessages().get(0).getSeverity());
	}

	@Test
	public void testValidateRawJsonResourceWithUnknownExtensionNotAllowed() {

		Patient patient = new Patient();
		patient.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);
		patient.setId("1");

		Extension ext = patient.addExtension();
		ext.setUrl("http://hl7.org/fhir/v3/ethnicity");
		ext.setValue(new CodeType("Hispanic or Latino"));

		String encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(patient);
		ourLog.info(encoded);

		/*
		 * {
		 * "resourceType": "Patient",
		 * "id": "1",
		 * "extension": [
		 * {
		 * "url": "http://hl7.org/fhir/v3/ethnicity",
		 * "valueCode": "Hispanic or Latino"
		 * }
		 * ]
		 * }
		 */

		myInstanceVal.setAnyExtensionsAllowed(false);
		ValidationResult output = myVal.validateWithResult(encoded);
		assertEquals(output.toString(), 1, output.getMessages().size());

		assertEquals("The extension http://hl7.org/fhir/v3/ethnicity is unknown, and not allowed here", output.getMessages().get(0).getMessage());
		assertEquals(ResultSeverityEnum.ERROR, output.getMessages().get(0).getSeverity());
	}

	@Test
	public void testValidateRawXmlResource() {
		String input = "<Patient xmlns=\"http://hl7.org/fhir\">" +
			"<id value=\"123\"/>" +
			"  <text>\n" +
			"    <status value=\"generated\"/>\n" +
			"    <div xmlns=\"http://www.w3.org/1999/xhtml\">AAA</div>\n" +
			"  </text>" +
			"</Patient>";

		ValidationResult output = myVal.validateWithResult(input);
		assertEquals(output.toString(), 0, output.getMessages().size());
	}

	@Test
	public void testValidateRawXmlResourceBadAttributes() {
		String input =
			"<Patient xmlns=\"http://hl7.org/fhir\">" +
				"<id value=\"123\"/>" +
				"  <text>\n" +
				"    <status value=\"generated\"/>\n" +
				"    <div xmlns=\"http://www.w3.org/1999/xhtml\">AAA</div>\n" +
				"  </text>" +
				"<foo value=\"222\"/>" +
				"</Patient>";

		ValidationResult output = myVal.validateWithResult(input);
		assertEquals(output.toString(), 1, output.getMessages().size());
		ourLog.info(output.getMessages().get(0).getLocationString());
		ourLog.info(output.getMessages().get(0).getMessage());
		assertEquals("/f:Patient", output.getMessages().get(0).getLocationString());
		assertEquals("Undefined element 'foo'", output.getMessages().get(0).getMessage());
		assertEquals(28, output.getMessages().get(0).getLocationCol().intValue());
		assertEquals(4, output.getMessages().get(0).getLocationLine().intValue());
	}

	@Test
	public void testValidateRawXmlResourceWithEmptyPrimitive() {
		String input = "<Patient xmlns=\"http://hl7.org/fhir\">" +
			"  <text>\n" +
			"    <status value=\"generated\"/>\n" +
			"    <div xmlns=\"http://www.w3.org/1999/xhtml\">AAA</div>\n" +
			"  </text>" +
			"<name><given/></name>" +
			"</Patient>";

		ValidationResult output = myVal.validateWithResult(input);
		List<SingleValidationMessage> messages = logResultsAndReturnNonInformationalOnes(output);
		assertEquals(output.toString(), 3, messages.size());
		assertThat(messages.get(0).getMessage(), containsString("Element must have some content"));
		assertThat(messages.get(1).getMessage(), containsString("Primitive types must have a value or must have child extensions"));
		assertThat(messages.get(2).getMessage(), containsString("All FHIR elements must have a @value or children [hasValue() or (children().count() > id.count())]"));
	}

	@Test
	public void testValidateRawXmlResourceWithPrimitiveContainingOnlyAnExtension() {
		String input = "<ActivityDefinition xmlns=\"http://hl7.org/fhir\">\n" +
			"                        <id value=\"referralToMentalHealthCare\"/>\n" +
			"  <text>\n" +
			"    <status value=\"generated\"/>\n" +
			"    <div xmlns=\"http://www.w3.org/1999/xhtml\">AAA</div>\n" +
			"  </text>" +
			"                        <name value=\"AAAAA\"/>\n" +
			"                        <status value=\"draft\"/>\n" +
			"                        <description value=\"refer to primary care mental-health integrated care program for evaluation and treatment of mental health conditions now\"/>\n" +
			"                        <code>\n" +
			"                                <coding>\n" +
			"                                        <!-- Error: Connection to http://localhost:960 refused -->\n" +
			"                                        <!--<system value=\"http://snomed.info/sct\"/>-->\n" +
			"                                        <code value=\"306206005\"/>\n" +
			"                                </coding>\n" +
			"                        </code>\n" +
			"                        <!-- Specifying this this way results in a null reference exception in the validator -->\n" +
			"                        <timingTiming>\n" +
			"                                <event>\n" +
			"                                        <extension url=\"http://fhir.org/cql-expression\">\n" +
			"                                                <valueString value=\"Now()\"/>\n" +
			"                                        </extension>\n" +
			"                                </event>\n" +
			"                        </timingTiming>\n" +
			"                </ActivityDefinition>";

		ValidationResult output = myVal.validateWithResult(input);
		List<SingleValidationMessage> res = logResultsAndReturnNonInformationalOnes(output);
		assertEquals(output.toString(), 0, res.size());
	}

	/**
	 * A reference with only an identifier should be valid
	 */
	@Test
	public void testValidateReferenceWithDisplayValid() {
		Patient p = new Patient();
		p.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);
		p.getManagingOrganization().setDisplay("HELLO");

		ValidationResult output = myVal.validateWithResult(p);
		List<SingleValidationMessage> nonInfo = logResultsAndReturnNonInformationalOnes(output);
		assertThat(nonInfo, empty());
	}

	/**
	 * A reference with only an identifier should be valid
	 */
	@Test
	public void testValidateReferenceWithIdentifierValid() {
		Patient p = new Patient();
		p.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);
		p.getManagingOrganization().getIdentifier().setSystem("http://acme.org");
		p.getManagingOrganization().getIdentifier().setValue("foo");

		ValidationResult output = myVal.validateWithResult(p);
		List<SingleValidationMessage> nonInfo = logResultsAndReturnNonInformationalOnes(output);
		assertThat(nonInfo, empty());
	}

	/**
	 * See #370
	 */
	@Test
	public void testValidateRelatedPerson() {

		/*
		 * Try with a code that is in http://hl7.org/fhir/ValueSet/relatedperson-relationshiptype
		 * and therefore should validate
		 */
		RelatedPerson rp = new RelatedPerson();
		rp.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);
		rp.getPatient().setReference("Patient/1");
		rp.addRelationship().addCoding().setSystem("http://terminology.hl7.org/CodeSystem/v2-0131").setCode("C");

		ValidationResult results = myVal.validateWithResult(rp);
		List<SingleValidationMessage> outcome = logResultsAndReturnNonInformationalOnes(results);
		assertThat(outcome, empty());

		/*
		 * Now a bad code
		 */
		rp = new RelatedPerson();
		rp.getPatient().setReference("Patient/1");
		rp.addRelationship().addCoding().setSystem("http://terminology.hl7.org/CodeSystem/v2-0131").setCode("GAGAGAGA");

		results = myVal.validateWithResult(rp);
		outcome = logResultsAndReturnNonInformationalOnes(results);
		assertThat(outcome, not(empty()));

	}

	@Test
	public void testValidateResourceContainingLoincCode() {
		addValidConcept("http://loinc.org", "1234567");

		Observation input = new Observation();
		// input.getMeta().addProfile("http://hl7.org/fhir/StructureDefinition/devicemetricobservation");

		input.addIdentifier().setSystem("http://acme").setValue("12345");
		input.getEncounter().setReference("http://foo.com/Encounter/9");
		input.setStatus(ObservationStatus.FINAL);
		input.getCode().addCoding().setSystem("http://loinc.org").setCode("12345");

		myInstanceVal.setValidationSupport(myValidationSupport);
		ValidationResult output = myVal.validateWithResult(input);
		List<SingleValidationMessage> errors = logResultsAndReturnAll(output);

		assertThat(errors.toString(), containsString("warning"));
		assertThat(errors.toString(), containsString("Unknown code: http://loinc.org / 12345"));
	}

	@Test
	public void testValidateResourceContainingProfileDeclaration() {
		addValidConcept("http://loinc.org", "12345");

		Observation input = new Observation();
		input.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);
		input.getMeta().addProfile("http://hl7.org/fhir/StructureDefinition/devicemetricobservation");

		input.addIdentifier().setSystem("http://acme").setValue("12345");
		input.getEncounter().setReference("http://foo.com/Encounter/9");
		input.setStatus(ObservationStatus.FINAL);
		input.getCode().addCoding().setSystem("http://loinc.org").setCode("12345");

		myInstanceVal.setValidationSupport(myValidationSupport);
		ValidationResult output = myVal.validateWithResult(input);
		List<SingleValidationMessage> errors = logResultsAndReturnNonInformationalOnes(output);

		assertThat(errors.toString(), containsString("Element 'Observation.subject': minimum required = 1, but only found 0"));
		assertThat(errors.toString(), containsString("Element 'Observation.encounter': max allowed = 0, but found 1"));
		assertThat(errors.toString(), containsString("Element 'Observation.device': minimum required = 1, but only found 0"));
		assertThat(errors.toString(), containsString(""));
	}

	@Test
	public void testValidateResourceContainingProfileDeclarationDoesntResolve() {
		addValidConcept("http://loinc.org", "12345");

		Observation input = new Observation();
		input.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);
		input.getMeta().addProfile("http://foo/structuredefinition/myprofile");

		input.getCode().addCoding().setSystem("http://loinc.org").setCode("12345");
		input.setStatus(ObservationStatus.FINAL);

		myInstanceVal.setValidationSupport(myValidationSupport);
		ValidationResult output = myVal.validateWithResult(input);
		List<SingleValidationMessage> errors = logResultsAndReturnNonInformationalOnes(output);

		assertEquals(1, errors.size());
		assertEquals("Profile reference \"http://foo/structuredefinition/myprofile\" could not be resolved, so has not been checked", errors.get(0).getMessage());
		assertEquals(ResultSeverityEnum.ERROR, errors.get(0).getSeverity());
	}

	@Test
	public void testValidateResourceFailingInvariant() {
		Observation input = new Observation();
		input.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);

		// Has a value, but not a status (which is required)
		input.getCode().addCoding().setSystem("http://loinc.org").setCode("12345");
		input.setValue(new StringType("AAA"));

		ValidationResult output = myVal.validateWithResult(input);
		assertThat(output.getMessages().size(), greaterThan(0));
		assertEquals("Profile http://hl7.org/fhir/StructureDefinition/Observation, Element 'Observation.status': minimum required = 1, but only found 0", output.getMessages().get(0).getMessage());

	}

	@Test
	public void testValidateResourceWithDefaultValueset() {
		Observation input = new Observation();

		input.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);
		input.setStatus(ObservationStatus.FINAL);
		input.getCode().setText("No code here!");

		ourLog.info(ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(input));

		ValidationResult output = myVal.validateWithResult(input);
		assertEquals(output.getMessages().size(), 0);
	}

	@Test
	public void testValidateResourceWithDefaultValuesetBadCode() {
		String input =
			"<Observation xmlns=\"http://hl7.org/fhir\">\n" +
				"  <text>\n" +
				"    <status value=\"generated\"/>\n" +
				"    <div xmlns=\"http://www.w3.org/1999/xhtml\">AAA</div>\n" +
				"  </text>" +
				"   <status value=\"notvalidcode\"/>\n" +
				"   <code>\n" +
				"      <text value=\"No code here!\"/>\n" +
				"   </code>\n" +
				"</Observation>";
		ValidationResult output = myVal.validateWithResult(input);
		logResultsAndReturnAll(output);
		assertEquals(
			"The value provided (\"notvalidcode\") is not in the value set http://hl7.org/fhir/ValueSet/observation-status|4.0.0 (http://hl7.org/fhir/ValueSet/observation-status, and a code is required from this value set) (error message = Unknown code 'notvalidcode')",
			output.getMessages().get(0).getMessage());
	}

	@Test
	@Ignore
	public void testValidateDecimalWithTrailingDot() {
		String input = "{" +
			" \"resourceType\": \"Observation\"," +
			" \"status\": \"final\"," +
			" \"subject\": {\"reference\":\"Patient/123\"}," +
			" \"code\": { \"coding\": [{ \"system\":\"http://foo\", \"code\":\"123\" }] }," +
			"        \"referenceRange\": [\n" +
			"          {\n" +
			"            \"low\": {\n" +
			"              \"value\": 210.0,\n" +
			"              \"unit\": \"pg/mL\"\n" +
			"            },\n" +
			"            \"high\": {\n" +
			"              \"value\": 925.,\n" +
			"              \"unit\": \"pg/mL\"\n" +
			"            },\n" +
			"            \"text\": \"210.0-925.\"\n" +
			"          }\n" +
			"        ]" +
			"}";
		ourLog.info(input);
		ValidationResult output = myVal.validateWithResult(input);
		logResultsAndReturnAll(output);
		assertEquals(
			"",
			output.getMessages().get(0).getMessage());
	}

	@Test
	public void testValidateResourceWithExampleBindingCodeValidationFailing() {
		Observation input = new Observation();
		input.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);

		myInstanceVal.setValidationSupport(myValidationSupport);

		input.setStatus(ObservationStatus.FINAL);
		input.getCode().addCoding().setSystem("http://loinc.org").setCode("12345");

		ValidationResult output = myVal.validateWithResult(input);
		List<SingleValidationMessage> errors = logResultsAndReturnNonInformationalOnes(output);
		assertEquals(errors.toString(), 0, errors.size());

	}

	@Test
	public void testValidateResourceWithExampleBindingCodeValidationFailingNonLoinc() {
		Observation input = new Observation();
		input.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);

		myInstanceVal.setValidationSupport(myValidationSupport);
		addValidConcept("http://acme.org", "12345");

		input.setStatus(ObservationStatus.FINAL);
		input.getCode().addCoding().setSystem("http://acme.org").setCode("9988877");

		ValidationResult output = myVal.validateWithResult(input);
		List<SingleValidationMessage> errors = logResultsAndReturnAll(output);
		assertThat(errors.toString(), errors.size(), greaterThan(0));
		assertEquals("Unknown code: http://acme.org / 9988877", errors.get(0).getMessage());

	}

	@Test
	public void testValidateResourceWithExampleBindingCodeValidationPassingLoinc() {
		Observation input = new Observation();
		input.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);

		myInstanceVal.setValidationSupport(myValidationSupport);
		addValidConcept("http://loinc.org", "12345");

		input.setStatus(ObservationStatus.FINAL);
		input.getCode().addCoding().setSystem("http://loinc.org").setCode("12345");

		ValidationResult output = myVal.validateWithResult(input);
		List<SingleValidationMessage> errors = logResultsAndReturnNonInformationalOnes(output);
		assertEquals(errors.toString(), 0, errors.size());
	}

	@Test
	public void testValidateResourceWithExampleBindingCodeValidationPassingLoincWithExpansion() {
		Observation input = new Observation();
		input.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);

		ValueSetExpansionComponent expansionComponent = new ValueSetExpansionComponent();
		expansionComponent.addContains().setSystem("http://loinc.org").setCode("12345").setDisplay("Some display code");

		mySupportedCodeSystemsForExpansion.put("http://loinc.org", expansionComponent);
		myInstanceVal.setValidationSupport(myValidationSupport);
		addValidConcept("http://loinc.org", "12345");

		input.setStatus(ObservationStatus.FINAL);
		input.getCode().addCoding().setSystem("http://loinc.org").setCode("1234");

		ValidationResult output = myVal.validateWithResult(input);
		List<SingleValidationMessage> errors = logResultsAndReturnNonInformationalOnes(output);
		assertEquals(1, errors.size());
		assertEquals("Unknown code: http://loinc.org / 1234", errors.get(0).getMessage());
	}

	@Test
	public void testValidateResourceWithExampleBindingCodeValidationPassingNonLoinc() {
		Observation input = new Observation();
		input.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);

		myInstanceVal.setValidationSupport(myValidationSupport);
		addValidConcept("http://acme.org", "12345");

		input.setStatus(ObservationStatus.FINAL);
		input.getCode().addCoding().setSystem("http://acme.org").setCode("12345");

		ValidationResult output = myVal.validateWithResult(input);
		List<SingleValidationMessage> errors = logResultsAndReturnAll(output);
		assertEquals(errors.toString(), 0, errors.size());
	}

	@Test
	public void testValidateResourceWithValuesetExpansionBad() {

		Patient p = new Patient();
		p.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);
		p.addIdentifier().setSystem("http://example.com/").setValue("12345").getType().addCoding().setSystem("http://example.com/foo/bar").setCode("bar");

		ValidationResult output = myVal.validateWithResult(p);
		List<SingleValidationMessage> all = logResultsAndReturnAll(output);
		assertEquals(1, all.size());
		assertEquals("Patient.identifier[0].type", all.get(0).getLocationString());
		assertThat(all.get(0).getMessage(), containsString("None of the codes provided are in the value set http://hl7.org/fhir/ValueSet/identifier-type"));
		assertEquals(ResultSeverityEnum.WARNING, all.get(0).getSeverity());

	}

	@Test
	public void testMultiplePerformer() {
		Observation o = new Observation();
		Practitioner p1 = new Practitioner();
		Practitioner p2 = new Practitioner();

		o.addPerformer(new Reference(p1));
		o.addPerformer(new Reference(p2));

		ValidationResult output = myVal.validateWithResult(o);
		List<SingleValidationMessage> valMessages = logResultsAndReturnAll(output);
		for (SingleValidationMessage msg : valMessages) {
			assertThat(msg.getMessage(), not(containsString("have a performer")));
		}
	}

	@Test
	public void testValidateResourceWithValuesetExpansionGood() {
		Patient patient = new Patient();
		patient.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);
		patient
			.addIdentifier()
			.setSystem("http://system")
			.setValue("12345")
			.getType()
			.addCoding()
			.setSystem("http://terminology.hl7.org/CodeSystem/v2-0203")
			.setCode("MR");

		ValidationResult output = myVal.validateWithResult(patient);
		List<SingleValidationMessage> all = logResultsAndReturnAll(output);
		assertEquals(0, all.size());
	}

	@Test
	public void testInvocationOfValidatorFetcher() throws IOException {

		String encoded = loadResource("/r4/r4-caredove-bundle.json");

		IResourceValidator.IValidatorResourceFetcher resourceFetcher = mock(IResourceValidator.IValidatorResourceFetcher.class);
		when(resourceFetcher.validationPolicy(any(), anyString(), anyString())).thenReturn(IResourceValidator.ReferenceValidationPolicy.CHECK_TYPE_IF_EXISTS);
		myInstanceVal.setValidatorResourceFetcher(resourceFetcher);
		myVal.validateWithResult(encoded);

		verify(resourceFetcher, times(14)).resolveURL(any(), anyString(), anyString());
		verify(resourceFetcher, times(12)).validationPolicy(any(), anyString(), anyString());
		verify(resourceFetcher, times(12)).fetch(any(), anyString());
	}

	@Test
	@Ignore
	public void testValidateStructureDefinition() throws IOException {
		String input = loadResource("/sdc-questionnaire.profile.xml");

		ValidationResult output = myVal.validateWithResult(input);
		logResultsAndReturnAll(output);

		assertEquals(output.toString(), 3, output.getMessages().size());
		ourLog.info(output.getMessages().get(0).getLocationString());
		ourLog.info(output.getMessages().get(0).getMessage());
	}

	@Test
	public void testValidateCurrency() {
		String input = "{\n" +
			" \"resourceType\": \"Invoice\",\n" +
			" \"status\": \"draft\",\n" +
			" \"date\": \"2020-01-08\",\n" +
			" \"totalGross\": {\n" +
			"  \"value\": 150,\n" +
			"  \"currency\": \"USD\"\n" +
			" }\n" +
			"}";
		ValidationResult output = myVal.validateWithResult(input);
		List<SingleValidationMessage> errors = logResultsAndReturnNonInformationalOnes(output);
		assertEquals(errors.toString(), 0, errors.size());


	}

	@Test
	public void testValidateCurrency_Wrong() {
		String input = "{\n" +
			" \"resourceType\": \"Invoice\",\n" +
			" \"status\": \"draft\",\n" +
			" \"date\": \"2020-01-08\",\n" +
			" \"totalGross\": {\n" +
			"  \"value\": 150,\n" +
			"  \"currency\": \"BLAH\"\n" +
			" }\n" +
			"}";
		ValidationResult output = myVal.validateWithResult(input);
		List<SingleValidationMessage> errors = logResultsAndReturnNonInformationalOnes(output);
		assertEquals(errors.toString(), 1, errors.size());
		assertThat(errors.get(0).getMessage(), containsString("The value provided (\"BLAH\") is not in the value set http://hl7.org/fhir/ValueSet/currencies"));


	}

	@Test
	public void testValidateReferenceTargetType_Correct() {

		AllergyIntolerance allergy = new AllergyIntolerance();
		allergy.getClinicalStatus().addCoding().setSystem("http://terminology.hl7.org/CodeSystem/allergyintolerance-clinical").setCode("active").setDisplay("Active");
		allergy.getVerificationStatus().addCoding().setSystem("http://terminology.hl7.org/CodeSystem/allergyintolerance-verification").setCode("confirmed").setDisplay("Confirmed");
		allergy.setPatient(new Reference("Patient/123"));

		allergy.addNote()
			.setText("This is text")
			.setAuthor(new Reference("Patient/123"));

		ValidationResult output = myVal.validateWithResult(allergy);
		List<SingleValidationMessage> errors = logResultsAndReturnNonInformationalOnes(output);
		assertEquals(errors.toString(), 0, errors.size());

	}

	@Test
	@Ignore
	public void testValidateReferenceTargetType_Incorrect() {

		AllergyIntolerance allergy = new AllergyIntolerance();
		allergy.getClinicalStatus().addCoding().setSystem("http://terminology.hl7.org/CodeSystem/allergyintolerance-clinical").setCode("active").setDisplay("Active");
		allergy.getVerificationStatus().addCoding().setSystem("http://terminology.hl7.org/CodeSystem/allergyintolerance-verification").setCode("confirmed").setDisplay("Confirmed");
		allergy.setPatient(new Reference("Patient/123"));

		allergy.addNote()
			.setText("This is text")
			.setAuthor(new Reference("CodeSystems/123"));

		ValidationResult output = myVal.validateWithResult(allergy);
		List<SingleValidationMessage> errors = logResultsAndReturnNonInformationalOnes(output);
		assertEquals(errors.toString(), 0, errors.size());
		assertThat(errors.get(0).getMessage(), containsString("The value provided (\"BLAH\") is not in the value set http://hl7.org/fhir/ValueSet/currencies"));

	}

	@AfterClass
	public static void afterClassClearContext() {
		myDefaultValidationSupport.flush();
		myDefaultValidationSupport = null;
		TestUtil.clearAllStaticFieldsForUnitTest();
	}


}
