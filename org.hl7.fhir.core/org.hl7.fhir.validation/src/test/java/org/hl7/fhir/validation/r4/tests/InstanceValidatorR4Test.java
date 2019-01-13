package org.hl7.fhir.validation.r4.tests;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.util.TestUtil;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.ReaderInputStream;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.exceptions.TerminologyServiceException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.conformance.ProfileUtilities;
import org.hl7.fhir.r4.context.IWorkerContext;
import org.hl7.fhir.r4.elementmodel.Manager;
import org.hl7.fhir.r4.hapi.ctx.DefaultProfileValidationSupport;
import org.hl7.fhir.r4.hapi.ctx.HapiWorkerContext;
import org.hl7.fhir.r4.hapi.ctx.IValidationSupport.CodeValidationResult;
import org.hl7.fhir.r4.model.*;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.CodeSystem.ConceptDefinitionComponent;
import org.hl7.fhir.r4.model.Observation.ObservationStatus;
import org.hl7.fhir.r4.model.StructureDefinition.StructureDefinitionKind;
import org.hl7.fhir.r4.model.ValueSet.ConceptSetComponent;
import org.hl7.fhir.r4.model.ValueSet.ValueSetExpansionComponent;
import org.hl7.fhir.r4.terminologies.ValueSetExpander;
import org.hl7.fhir.r4.utils.FHIRPathEngine;
import org.hl7.fhir.r4.utils.IResourceValidator;
import org.hl7.fhir.r4.validation.InstanceValidator;
import org.hl7.fhir.utilities.validation.ValidationMessage;
import org.hl7.fhir.utilities.xhtml.XhtmlNode;
import org.junit.*;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.*;
import java.util.zip.GZIPInputStream;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class InstanceValidatorR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(InstanceValidatorR4Test.class);
	private static DefaultProfileValidationSupport myDefaultValidationSupport = new DefaultProfileValidationSupport();
	private static FhirContext ourCtx = FhirContext.forR4();
	@Rule
	public TestRule watcher = new TestWatcher() {
		@Override
		protected void starting(Description description) {
			ourLog.info("Starting test: " + description.getMethodName());
		}
	};
	private InstanceValidator myInstanceVal;

	private Map<String, ValueSetExpansionComponent> mySupportedCodeSystemsForExpansion;
	private ArrayList<String> myValidConcepts;
	private Set<String> myValidSystems = new HashSet<>();
	private DefaultProfileValidationSupport myDefaultProfileValidationSupport = new DefaultProfileValidationSupport();

	@Mock
	private IWorkerContext myWorkerContext;
	@Mock
	private FHIRPathEngine.IEvaluationContext myEvaluationContext;
	private FhirContext myCtx = FhirContext.forR4();

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

		List<ValidationMessage> output = validateAndReturnResult(resource);
		List<ValidationMessage> nonInfo = logResultsAndReturnNonInformationalOnes(output);
		assertThat(nonInfo, hasSize(2));
	}

	private List<ValidationMessage> validateAndReturnResult(Resource theResource) {
		ArrayList<ValidationMessage> issues = new ArrayList<>();
		try {
			myInstanceVal.validate(null, issues, theResource);
		} catch (FHIRException e) {
			ourLog.error("Failure during validation", e);
			fail("Failure during validation" + e.toString());
		}
		return issues;
	}

	private List<ValidationMessage> validateAndReturnResult(String theResource) {
		ArrayList<ValidationMessage> issues = new ArrayList<>();
		try {
			ReaderInputStream stringInputStream = new ReaderInputStream(new StringReader(theResource), Charsets.UTF_8);
			Manager.FhirFormat fhirFormat = Manager.FhirFormat.XML;
			myInstanceVal.validate(null, issues, stringInputStream, fhirFormat);
		} catch (FHIRException e) {
			ourLog.error("Failure during validation", e);
			fail("Failure during validation" + e.toString());
		}
		return issues;
	}

	@SuppressWarnings("unchecked")
	@Before
	public void before() throws TerminologyServiceException {
		myInstanceVal = new InstanceValidator(myWorkerContext, myEvaluationContext);

		mySupportedCodeSystemsForExpansion = new HashMap<>();

		myValidConcepts = new ArrayList<>();

		when(myWorkerContext.expandVS(nullable(ConceptSetComponent.class), anyBoolean())).thenAnswer(theInvocation -> {
			ConceptSetComponent arg = (ConceptSetComponent) theInvocation.getArguments()[1];
			ValueSetExpansionComponent retVal = mySupportedCodeSystemsForExpansion.get(arg.getSystem());
			if (retVal == null) {
				ValueSetExpander.ValueSetExpansionOutcome outcome = myDefaultValidationSupport.expandValueSet(ourCtx, arg);
				return outcome;
			}
			ourLog.debug("expandValueSet({}) : {}", new Object[]{theInvocation.getArguments()[0], retVal});

			ValueSet valueset = new ValueSet();
			valueset.setExpansion(retVal);
			return new ValueSetExpander.ValueSetExpansionOutcome(valueset);
		});

		when(myWorkerContext.supportsSystem(anyString())).thenAnswer(theInvocation -> {
			String system = (String) theInvocation.getArguments()[0];
			boolean retVal = myValidSystems.contains(system);
			ourLog.debug("isCodeSystemSupported({}) : {}", new Object[]{theInvocation.getArguments()[1], retVal});
			return retVal;
		});

		when(myWorkerContext.fetchResource(nullable(Class.class), nullable(String.class))).thenAnswer(new Answer<IBaseResource>() {
			@Override
			public IBaseResource answer(InvocationOnMock theInvocation) throws Throwable {
				IBaseResource retVal;
				String id = (String) theInvocation.getArguments()[1];
				Class<IBaseResource> type = (Class<IBaseResource>) theInvocation.getArguments()[0];
				retVal = myDefaultValidationSupport.fetchResource(myCtx, type, id);
				ourLog.debug("fetchResource({}, {}) : {}", theInvocation.getArguments()[1], id, retVal);
				return retVal;
			}
		});

		when(myWorkerContext.validateCode(nullable(String.class), nullable(String.class), nullable(String.class))).thenAnswer(new Answer<CodeValidationResult>() {
			@Override
			public CodeValidationResult answer(InvocationOnMock theInvocation) throws Throwable {
				String system = (String) theInvocation.getArguments()[0];
				String code = (String) theInvocation.getArguments()[1];
				CodeValidationResult retVal;
				if (myValidConcepts.contains(system + "___" + code)) {
					retVal = new CodeValidationResult(new ConceptDefinitionComponent(new CodeType(code)));
				} else {
					retVal = myDefaultValidationSupport.validateCode(myCtx, system, code, (String) theInvocation.getArguments()[2]);
				}
				ourLog.debug("validateCode({}, {}, {}) : {}", system, code, (String) theInvocation.getArguments()[2], retVal);
				return retVal;
			}
		});
		when(myWorkerContext.fetchCodeSystem(nullable(String.class))).thenAnswer(new Answer<CodeSystem>() {
			@Override
			public CodeSystem answer(InvocationOnMock theInvocation) throws Throwable {
				CodeSystem retVal = myDefaultValidationSupport.fetchCodeSystem((FhirContext) theInvocation.getArguments()[0], (String) theInvocation.getArguments()[1]);
				ourLog.debug("fetchCodeSystem({}) : {}", new Object[]{(String) theInvocation.getArguments()[0], retVal});
				return retVal;
			}
		});
		when(myWorkerContext.fetchTypeDefinition(nullable(String.class))).thenAnswer(new Answer<StructureDefinition>() {
			@Override
			public StructureDefinition answer(InvocationOnMock theInvocation) throws Throwable {
				String url = (String) theInvocation.getArguments()[0];
				StructureDefinition retVal = myDefaultValidationSupport.fetchStructureDefinition(myCtx, url);
				ourLog.debug("fetchStructureDefinition({}) : {}", new Object[]{url, retVal});
				return retVal;
			}
		});
		when(myWorkerContext.allStructures()).thenAnswer(new Answer<List<StructureDefinition>>() {
			@Override
			public List<StructureDefinition> answer(InvocationOnMock theInvocation) throws Throwable {
				List<StructureDefinition> retVal = myDefaultValidationSupport.fetchAllStructureDefinitions(myCtx);
				ourLog.debug("fetchAllStructureDefinitions()", new Object[]{});
				return retVal;
			}
		});

	}

	private Object defaultString(Integer theLocationLine) {
		return theLocationLine != null ? theLocationLine.toString() : "";
	}

	private StructureDefinition loadStructureDefinition(String theResName) throws IOException, FHIRException {
		StructureDefinition derived = ourCtx.newXmlParser().parseResource(StructureDefinition.class, IOUtils.toString(InstanceValidatorR4Test.class.getResourceAsStream(theResName), Charsets.UTF_8));
		StructureDefinition base = myDefaultProfileValidationSupport.fetchStructureDefinition(ourCtx, derived.getBaseDefinition());
		Validate.notNull(base);

		IWorkerContext worker = new HapiWorkerContext(ourCtx, myDefaultProfileValidationSupport);
		List<ValidationMessage> issues = new ArrayList<>();
		ProfileUtilities profileUtilities = new ProfileUtilities(worker, issues, null);
		profileUtilities.generateSnapshot(base, derived, "", "");

		return derived;
	}

	private List<ValidationMessage> logResultsAndReturnAll(List<ValidationMessage> theOutput) {
		List<ValidationMessage> retVal = new ArrayList<>();

		int index = 0;
		for (ValidationMessage next : theOutput) {
			ourLog.info("Result {}: {} - {}:{} {} - {}",
				index, next.getLevel(), defaultString(next.getLine()), defaultString(next.getCol()), next.getLocation(), next.getMessage());
			index++;

			retVal.add(next);
		}
		return retVal;
	}

	@Test
	public void testValidateCodeWithTailingSpace() {
		Patient p = new Patient();
		p.getMaritalStatus()
			.addCoding()
			.setSystem("http://foo")
			.setCode("AA  ");

		List<ValidationMessage> result = validateAndReturnResult(p);
		List<ValidationMessage> all = logResultsAndReturnErrorOnes(result);
		assertEquals("The code 'AA  ' is not valid (whitespace rules)", all.get(0).getMessage());

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

		List<ValidationMessage> result = validateAndReturnResult(input);
		List<ValidationMessage> all = logResultsAndReturnNonInformationalOnes(result);
		assertEquals("Primitive types must have a value that is not empty", all.get(0).getMessage());
	}

	/**
	 * See #942
	 */
	@Test
	public void testValidateDoesntEnforceBestPracticesByDefault() throws FHIRException {
		Observation input = new Observation();
		input.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);
		input.setStatus(ObservationStatus.AMENDED);
		input.getCode().addCoding().setSystem("http://loinc.org").setCode("1234").setDisplay("FOO");

		InstanceValidator instanceModule;
		InstanceValidator val;
		List<ValidationMessage> all;
		List<ValidationMessage> result;

		// With BPs disabled
		val = new InstanceValidator(myWorkerContext, myEvaluationContext);
		val.setBestPracticeWarningLevel(IResourceValidator.BestPracticeWarningLevel.Hint);
		result = new ArrayList<>();
		val.validate(null, result, input);
		all = logResultsAndReturnNonInformationalOnes(result);
		assertThat(all, empty());

		// With BPs enabled
		val = new InstanceValidator(myWorkerContext, myEvaluationContext);
		val.setBestPracticeWarningLevel(IResourceValidator.BestPracticeWarningLevel.Error);
		result = new ArrayList<>();
		val.validate(null, result, input);
		all = logResultsAndReturnNonInformationalOnes(result);
		assertEquals("All observations should have a subject", all.get(0).getMessage());
	}


	private List<ValidationMessage> logResultsAndReturnNonInformationalOnes(List<ValidationMessage> theErrors) {
		List<ValidationMessage> retVal = new ArrayList<>();

		int index = 0;
		for (ValidationMessage next : theErrors) {
			ourLog.info("Result {}: {} - {} - {}", index, next.getLevel(), next.getLocation(), next.getMessage());
			index++;

			if (next.getLevel() != ValidationMessage.IssueSeverity.INFORMATION) {
				retVal.add(next);
			}
		}

		return retVal;
	}

	private List<ValidationMessage> logResultsAndReturnErrorOnes(List<ValidationMessage> theOutput) {
		List<ValidationMessage> retVal = new ArrayList<>();

		int index = 0;
		for (ValidationMessage next : theOutput) {
			ourLog.info("Result {}: {} - {} - {}", index, next.getLevel(), next.getLocation(), next.getMessage());
			index++;

			if (next.getLevel().ordinal() > ValidationMessage.IssueSeverity.WARNING.ordinal()) {
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

		List<ValidationMessage> output = validateAndReturnResult(encoded);
		List<ValidationMessage> errors = logResultsAndReturnNonInformationalOnes(output);
		assertEquals(1, errors.size());
		assertEquals("The value \"%%%2@()()\" is not a valid Base64 value", errors.get(0).getMessage());

	}

	@Test
	public void testValidateBundleWithNoFullUrl() throws IOException {
		String encoded = IOUtils.toString(InstanceValidatorR4Test.class.getResourceAsStream("/r4/r4-caredove-bundle.json"));

		List<ValidationMessage> output = validateAndReturnResult(encoded);
		List<ValidationMessage> errors = logResultsAndReturnNonInformationalOnes(output);
		assertEquals(48, errors.size());
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

		List<ValidationMessage> output = validateAndReturnResult(encoded);
		List<ValidationMessage> errors = logResultsAndReturnErrorOnes(output);
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

		List<ValidationMessage> result = validateAndReturnResult(procedure);
		List<ValidationMessage> issues = logResultsAndReturnErrorOnes(result);
		assertThat(issues, empty());
	}

	/**
	 * See #531
	 */
	@Test
	public void testContactPointSystemUrlWorks() {
		Patient p = new Patient();
		p.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);
		ContactPoint t = p.addTelecom();
		t.setSystem(ContactPoint.ContactPointSystem.URL);
		t.setValue("http://infoway-inforoute.ca");

		List<ValidationMessage> results = validateAndReturnResult(p);
		List<ValidationMessage> outcome = logResultsAndReturnNonInformationalOnes(results);
		assertThat(outcome, empty());

	}

	/**
	 * See #872
	 */
	@Test
	public void testExtensionUrlWithHl7Url() throws IOException {
		String input = IOUtils.toString(InstanceValidator.class.getResourceAsStream("/bug872-ext-with-hl7-url.json"), Charsets.UTF_8);
		List<ValidationMessage> output = validateAndReturnResult(input);
		List<ValidationMessage> nonInfo = logResultsAndReturnNonInformationalOnes(output);
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
		String input = IOUtils.toString(InstanceValidatorR4Test.class.getResourceAsStream("/r4/diagnosticreport-example-gingival-mass.json"), Constants.CHARSET_UTF8);
		List<ValidationMessage> output = validateAndReturnResult(input);
		List<ValidationMessage> errors = logResultsAndReturnErrorOnes(output);
		assertEquals(1, errors.size());
		assertEquals("Code http://loinc.org/1-8 was not validated because the code system is not present", errors.get(0).getMessage());
	}

	@Test
	@Ignore
	public void testValidateBigRawJsonResource() throws Exception {
		InputStream stream = InstanceValidatorR4Test.class.getResourceAsStream("/r4/conformance.json.gz");
		stream = new GZIPInputStream(stream);
		String input = IOUtils.toString(stream);

		long start = System.currentTimeMillis();
		List<ValidationMessage> output = null;
		int passes = 1;
		for (int i = 0; i < passes; i++) {
			ourLog.info("Pass {}", i + 1);
			output = validateAndReturnResult(input);
		}

		long delay = System.currentTimeMillis() - start;
		long per = delay / passes;

		logResultsAndReturnAll(output);

		ourLog.info("Took {} ms -- {}ms / pass", delay, per);
	}

	@Test
	@Ignore
	public void testValidateBuiltInProfiles() throws Exception {
		Bundle bundle;
		String name = "profiles-resources";
		ourLog.info("Uploading " + name);
		String vsContents;
		vsContents = IOUtils.toString(InstanceValidatorR4Test.class.getResourceAsStream("/org/hl7/fhir/r4/model/profile/" + name + ".xml"), "UTF-8");

		TreeSet<String> ids = new TreeSet<>();

		bundle = ourCtx.newXmlParser().parseResource(Bundle.class, vsContents);
		for (BundleEntryComponent i : bundle.getEntry()) {
			Resource next = i.getResource();
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

			List<ValidationMessage> output = validateAndReturnResult(next);
			List<ValidationMessage> results = logResultsAndReturnAll(output);

			// This isn't a validator problem but a definition problem.. it should get fixed at some point and
			// we can remove this. Tracker #17207 was filed about this
			// https://gforge.hl7.org/gf/project/fhir/tracker/?action=TrackerItemEdit&tracker_item_id=17207
			if (next.getId().equalsIgnoreCase("http://hl7.org/fhir/OperationDefinition/StructureDefinition-snapshot")) {
				assertEquals(1, results.size());
				assertEquals("A search type can only be specified for parameters of type string [searchType.exists() implies type = 'string']", results.get(0).getMessage());
				continue;
			}


			List<ValidationMessage> errors = logResultsAndReturnNonInformationalOnes(results);
			assertThat("Failed to validate " + i.getFullUrl() + " - " + errors, errors, empty());
		}

		ourLog.info("Validated the following:\n{}", ids);
	}

	@Test
	public void testValidateBundleWithNoType() throws Exception {
		String vsContents = IOUtils.toString(InstanceValidatorR4Test.class.getResourceAsStream("/r4/bundle-with-no-type.json"), "UTF-8");

		List<ValidationMessage> output = validateAndReturnResult(vsContents);
		logResultsAndReturnNonInformationalOnes(output);
		assertThat(output.toString(), containsString("Element 'Bundle.type': minimum required = 1"));
	}

	@Test
	@Ignore
	public void testValidateBundleWithObservations() throws Exception {
		String name = "profiles-resources";
		ourLog.info("Uploading " + name);
		String inputString;
		inputString = IOUtils.toString(InstanceValidatorR4Test.class.getResourceAsStream("/brian_reinhold_bundle.json"), "UTF-8");
		Bundle bundle = ourCtx.newJsonParser().parseResource(Bundle.class, inputString);

		FHIRPathEngine fp = new FHIRPathEngine(myWorkerContext);
		List<Base> fpOutput;
		BooleanType bool;

		fpOutput = fp.evaluate(bundle.getEntry().get(0).getResource(), "component.where(code = %resource.code).empty()");
		assertEquals(1, fpOutput.size());
		bool = (BooleanType) fpOutput.get(0);
		assertTrue(bool.getValue());

		List<ValidationMessage> output = validateAndReturnResult(inputString);
		List<ValidationMessage> errors = logResultsAndReturnNonInformationalOnes(output);
		assertThat(errors, empty());

	}

	@Test
	@Ignore
	public void testValidateDocument() throws Exception {
		String vsContents = IOUtils.toString(InstanceValidatorR4Test.class.getResourceAsStream("/sample-document.xml"), "UTF-8");

		List<ValidationMessage> output = validateAndReturnResult(vsContents);
		List<ValidationMessage> result = logResultsAndReturnNonInformationalOnes(output);
		assertTrue(result.isEmpty());
	}

	@Test
	public void testValidateProfileWithExtension() throws IOException, FHIRException {

		// Prepopulate SDs
		loadStructureDefinition("/r4/myconsent-profile.xml");
		loadStructureDefinition("/r4/myconsent-ext.xml");

		Consent input = ourCtx.newJsonParser().parseResource(Consent.class, IOUtils.toString(InstanceValidatorR4Test.class.getResourceAsStream("/r4/myconsent-resource.json")));

		input.getPolicyFirstRep().setAuthority("http://foo");
		input.getScope()
			.getCodingFirstRep()
			.setSystem("http://terminology.hl7.org/CodeSystem/consentscope")
			.setCode("adr");
		input.addCategory()
			.getCodingFirstRep()
			.setSystem("http://terminology.hl7.org/CodeSystem/consentcategorycodes")
			.setCode("acd");

		// Should pass
		List<ValidationMessage> output = validateAndReturnResult(input);
		List<ValidationMessage> all = logResultsAndReturnErrorOnes(output);
		assertEquals(all.toString(), 0, all.size());

		// Now with the wrong datatype
		input.getExtensionsByUrl("http://hl7.org/fhir/StructureDefinition/PruebaExtension").get(0).setValue(new CodeType("AAA"));

		// Should fail
		output = validateAndReturnResult(input);
		all = logResultsAndReturnErrorOnes(output);
		assertThat(all.toString(), containsString("definition allows for the types [string] but found type code"));

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

		List<ValidationMessage> output = validateAndReturnResult(input);
		assertEquals(output.toString(), 0, output.size());
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

		List<ValidationMessage> output = validateAndReturnResult(input);
		assertEquals(output.toString(), 1, output.size());
		ourLog.info(output.get(0).getLocation());
		ourLog.info(output.get(0).getMessage());
		assertEquals("/Patient", output.get(0).getLocation());
		assertEquals("Unrecognised property '@foo'", output.get(0).getMessage());
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

		List<ValidationMessage> output = validateAndReturnResult(encoded);
		assertEquals(output.toString(), 1, output.size());

		assertEquals("Unknown extension http://hl7.org/fhir/v3/ethnicity", output.get(0).getMessage());
		assertEquals(ValidationMessage.IssueSeverity.INFORMATION, output.get(0).getLevel());
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
		List<ValidationMessage> output = validateAndReturnResult(encoded);
		assertEquals(output.toString(), 1, output.size());

		assertEquals("The extension http://hl7.org/fhir/v3/ethnicity is unknown, and not allowed here", output.get(0).getMessage());
		assertEquals(ValidationMessage.IssueSeverity.ERROR, output.get(0).getLevel());
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

		List<ValidationMessage> output = validateAndReturnResult(input);
		assertEquals(output.toString(), 0, output.size());
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

		List<ValidationMessage> output = validateAndReturnResult(input);
		assertEquals(output.toString(), 1, output.size());
		ourLog.info(output.get(0).getLocation());
		ourLog.info(output.get(0).getMessage());
		assertEquals("/f:Patient", output.get(0).getLocation());
		assertEquals("Undefined element 'foo'", output.get(0).getMessage());
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

		List<ValidationMessage> output = validateAndReturnResult(input);
		List<ValidationMessage> messages = logResultsAndReturnNonInformationalOnes(output);
		assertEquals(output.toString(), 3, messages.size());
		assertThat(messages.get(0).getMessage(), containsString("Element must have some content"));
		assertThat(messages.get(1).getMessage(), containsString("Primitive types must have a value or must have child extensions"));
		assertThat(messages.get(2).getMessage(), containsString("All FHIR elements must have a @value or children [hasValue() or (children().count() > id.count())]"));
	}

	@Test
	public void testValidateRawXmlResourceWithPrimitiveContainingOnlyAnExtension() {
		// @formatter:off
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
		// @formatter:on

		List<ValidationMessage> output = validateAndReturnResult(input);
		List<ValidationMessage> res = logResultsAndReturnNonInformationalOnes(output);
		assertEquals(output.toString(), 0, res.size());
	}

	@Test
	public void testValidateRawXmlWithMissingRootNamespace() {
		//@formatter:off
		String input = ""
			+ "<Patient>"
			+ "    <text>"
			+ "        <status value=\"generated\"/>"
			+ "        <div xmlns=\"http://www.w3.org/1999/xhtml\">Some narrative</div>"
			+ "    </text>"
			+ "    <name>"
			+ "        <use value=\"official\"/>"
			+ "        <family value=\"Doe\"/>"
			+ "        <given value=\"John\"/>"
			+ "    </name>"
			+ "    <gender value=\"male\"/>"
			+ "    <birthDate value=\"1974-12-25\"/>"
			+ "</Patient>";
		//@formatter:on

		List<ValidationMessage> output = validateAndReturnResult(input);
		assertEquals(output.toString(), 1, output.size());
		assertEquals("This cannot be parsed as a FHIR object (no namespace)", output.get(0).getMessage());
		ourLog.info(output.get(0).getLocation());
	}

	/**
	 * A reference with only an identifier should be valid
	 */
	@Test
	public void testValidateReferenceWithDisplayValid() throws Exception {
		Patient p = new Patient();
		p.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);
		p.getManagingOrganization().setDisplay("HELLO");

		List<ValidationMessage> output = validateAndReturnResult(p);
		List<ValidationMessage> nonInfo = logResultsAndReturnNonInformationalOnes(output);
		assertThat(nonInfo, empty());
	}

	/**
	 * A reference with only an identifier should be valid
	 */
	@Test
	public void testValidateReferenceWithIdentifierValid() throws Exception {
		Patient p = new Patient();
		p.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);
		p.getManagingOrganization().getIdentifier().setSystem("http://acme.org");
		p.getManagingOrganization().getIdentifier().setValue("foo");

		List<ValidationMessage> output = validateAndReturnResult(p);
		List<ValidationMessage> nonInfo = logResultsAndReturnNonInformationalOnes(output);
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

		List<ValidationMessage> results = validateAndReturnResult(rp);
		List<ValidationMessage> outcome = logResultsAndReturnNonInformationalOnes(results);
		assertThat(outcome, empty());

		/*
		 * Now a bad code
		 */
		rp = new RelatedPerson();
		rp.getPatient().setReference("Patient/1");
		rp.addRelationship().addCoding().setSystem("http://terminology.hl7.org/CodeSystem/v2-0131").setCode("GAGAGAGA");

		results = validateAndReturnResult(rp);
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

		List<ValidationMessage> output = validateAndReturnResult(input);
		List<ValidationMessage> errors = logResultsAndReturnAll(output);

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

		List<ValidationMessage> output = validateAndReturnResult(input);
		List<ValidationMessage> errors = logResultsAndReturnNonInformationalOnes(output);

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

		List<ValidationMessage> output = validateAndReturnResult(input);
		List<ValidationMessage> errors = logResultsAndReturnNonInformationalOnes(output);
		assertEquals(errors.toString(), 1, errors.size());
		assertEquals("StructureDefinition reference \"http://foo/structuredefinition/myprofile\" could not be resolved", errors.get(0).getMessage());
	}

	@Test
	public void testValidateResourceFailingInvariant() {
		Observation input = new Observation();
		input.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);

		// Has a value, but not a status (which is required)
		input.getCode().addCoding().setSystem("http://loinc.org").setCode("12345");
		input.setValue(new StringType("AAA"));

		List<ValidationMessage> output = validateAndReturnResult(input);
		assertThat(output.size(), greaterThan(0));
		assertEquals("Profile http://hl7.org/fhir/StructureDefinition/Observation, Element 'Observation.status': minimum required = 1, but only found 0", output.get(0).getMessage());

	}

	@Test
	public void testValidateResourceWithDefaultValueset() {
		Observation input = new Observation();

		input.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);
		input.setStatus(ObservationStatus.FINAL);
		input.getCode().setText("No code here!");

		ourLog.info(ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(input));

		List<ValidationMessage> output = validateAndReturnResult(input);
		assertEquals(output.size(), 0);
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
		List<ValidationMessage> output = validateAndReturnResult(input);
		List<ValidationMessage> issues = logResultsAndReturnNonInformationalOnes(output);
		assertEquals(
			"The value provided ('notvalidcode') is not in the value set http://hl7.org/fhir/ValueSet/observation-status|4.0.0 (http://hl7.org/fhir/ValueSet/observation-status, and a code is required from this value set) (error message = Unknown code[notvalidcode] in system[null])",
			issues.get(0).getMessage());
	}

	@Test
	public void testValidateResourceWithExampleBindingCodeValidationFailing() {
		Observation input = new Observation();
		input.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);

		input.setStatus(ObservationStatus.FINAL);
		input.getCode().addCoding().setSystem("http://loinc.org").setCode("12345");

		List<ValidationMessage> output = validateAndReturnResult(input);
		List<ValidationMessage> errors = logResultsAndReturnNonInformationalOnes(output);
		assertEquals(errors.toString(), 0, errors.size());

	}

	@Test
	public void testValidateResourceWithExampleBindingCodeValidationFailingNonLoinc() {
		Observation input = new Observation();
		input.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);

		addValidConcept("http://acme.org", "12345");

		input.setStatus(ObservationStatus.FINAL);
		input.getCode().addCoding().setSystem("http://acme.org").setCode("9988877");

		List<ValidationMessage> output = validateAndReturnResult(input);
		List<ValidationMessage> errors = logResultsAndReturnAll(output);
		assertThat(errors.toString(), errors.size(), greaterThan(0));
		assertEquals("Unknown code: http://acme.org / 9988877", errors.get(0).getMessage());

	}

	@Test
	public void testValidateResourceWithExampleBindingCodeValidationPassingLoinc() {
		Observation input = new Observation();
		input.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);

		addValidConcept("http://loinc.org", "12345");

		input.setStatus(ObservationStatus.FINAL);
		input.getCode().addCoding().setSystem("http://loinc.org").setCode("12345");

		List<ValidationMessage> output = validateAndReturnResult(input);
		List<ValidationMessage> errors = logResultsAndReturnNonInformationalOnes(output);
		assertEquals(errors.toString(), 0, errors.size());
	}

	@Test
	public void testValidateResourceWithExampleBindingCodeValidationPassingLoincWithExpansion() {
		Observation input = new Observation();
		input.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);

		ValueSetExpansionComponent expansionComponent = new ValueSetExpansionComponent();
		expansionComponent.addContains().setSystem("http://loinc.org").setCode("12345").setDisplay("Some display code");

		mySupportedCodeSystemsForExpansion.put("http://loinc.org", expansionComponent);
		addValidConcept("http://loinc.org", "12345");

		input.setStatus(ObservationStatus.FINAL);
		input.getCode().addCoding().setSystem("http://loinc.org").setCode("1234");

		List<ValidationMessage> output = validateAndReturnResult(input);
		List<ValidationMessage> errors = logResultsAndReturnNonInformationalOnes(output);
		assertEquals(1, errors.size());
		assertEquals("Unknown code: http://loinc.org / 1234", errors.get(0).getMessage());
	}

	@Test
	public void testValidateResourceWithExampleBindingCodeValidationPassingNonLoinc() {
		Observation input = new Observation();
		input.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);

		addValidConcept("http://acme.org", "12345");

		input.setStatus(ObservationStatus.FINAL);
		input.getCode().addCoding().setSystem("http://acme.org").setCode("12345");

		List<ValidationMessage> output = validateAndReturnResult(input);
		List<ValidationMessage> errors = logResultsAndReturnAll(output);
		assertEquals(errors.toString(), 0, errors.size());
	}

	@Test
	public void testValidateResourceWithValuesetExpansionBad() {

		Patient p = new Patient();
		p.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);
		p.addIdentifier().setSystem("http://example.com/").setValue("12345").getType().addCoding().setSystem("http://example.com/foo/bar").setCode("bar");

		List<ValidationMessage> output = validateAndReturnResult(p);
		List<ValidationMessage> all = logResultsAndReturnAll(output);
		assertEquals(1, all.size());
		assertEquals("Patient.identifier.type", all.get(0).getLocation());
		assertEquals(
			"Code http://example.com/foo/bar/bar was not validated because the code system is not present",
			all.get(0).getMessage());
		assertEquals(ValidationMessage.IssueSeverity.WARNING, all.get(0).getLevel());

	}

	@Test
	public void testMultiplePerformer() {
		Observation o = new Observation();
		Practitioner p1 = new Practitioner();
		Practitioner p2 = new Practitioner();

		o.addPerformer(new Reference(p1));
		o.addPerformer(new Reference(p2));

		List<ValidationMessage> output = validateAndReturnResult(o);
		List<ValidationMessage> valMessages = logResultsAndReturnAll(output);
		List<String> messages = new ArrayList<>();
		for (String msg : messages) {
			messages.add(msg);
		}
		assertThat(messages, not(hasItem("All observations should have a performer")));
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

		List<ValidationMessage> output = validateAndReturnResult(patient);
		List<ValidationMessage> all = logResultsAndReturnAll(output);
		assertEquals(0, all.size());
	}

	@Test
	@Ignore
	public void testValidateStructureDefinition() throws IOException {
		String input = IOUtils.toString(InstanceValidatorR4Test.class.getResourceAsStream("/sdc-questionnaire.profile.xml"));

		List<ValidationMessage> output = validateAndReturnResult(input);
		logResultsAndReturnAll(output);

		assertEquals(output.toString(), 3, output.size());
		ourLog.info(output.get(0).getLocation());
		ourLog.info(output.get(0).getMessage());
	}

	@AfterClass
	public static void afterClassClearContext() {
		myDefaultValidationSupport.flush();
		myDefaultValidationSupport = null;
		TestUtil.clearAllStaticFieldsForUnitTest();
	}


}
