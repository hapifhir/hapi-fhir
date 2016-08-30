package ca.uhn.fhir.validation;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.zip.GZIPInputStream;

import org.apache.commons.io.IOUtils;
import org.hl7.fhir.dstu3.hapi.validation.*;
import org.hl7.fhir.dstu3.hapi.validation.IValidationSupport.CodeValidationResult;
import org.hl7.fhir.dstu3.model.*;
import org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.dstu3.model.CodeSystem.ConceptDefinitionComponent;
import org.hl7.fhir.dstu3.model.Observation.ObservationStatus;
import org.hl7.fhir.dstu3.model.ValueSet.ConceptSetComponent;
import org.hl7.fhir.dstu3.model.ValueSet.ValueSetExpansionComponent;
import org.hl7.fhir.dstu3.utils.FluentPathEngine;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.*;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.util.TestUtil;

public class FhirInstanceValidatorDstu3Test {

	private static DefaultProfileValidationSupport myDefaultValidationSupport = new DefaultProfileValidationSupport();
	private static FhirContext ourCtx = FhirContext.forDstu3();
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirInstanceValidatorDstu3Test.class);
	private FhirInstanceValidator myInstanceVal;
	private IValidationSupport myMockSupport;

	private Map<String, ValueSetExpansionComponent> mySupportedCodeSystemsForExpansion;
	private FhirValidator myVal;
	private ArrayList<String> myValidConcepts;
	private Set<String> myValidSystems = new HashSet<String>();
	@Rule
	public TestRule watcher = new TestWatcher() {
		protected void starting(Description description) {
			ourLog.info("Starting test: " + description.getMethodName());
		}
	};

	private void addValidConcept(String theSystem, String theCode) {
		myValidSystems.add(theSystem);
		myValidConcepts.add(theSystem + "___" + theCode);
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
		rp.getPatient().setReference("Patient/1");
		rp.getRelationship().addCoding().setSystem("http://hl7.org/fhir/v2/0131").setCode("c");
		
		ValidationResult results = myVal.validateWithResult(rp);
		List<SingleValidationMessage> outcome = logResultsAndReturnNonInformationalOnes(results);
		assertThat(outcome, empty());

		/*
		 * Code system is case insensitive, so try with capital C
		 */
		rp = new RelatedPerson();
		rp.getPatient().setReference("Patient/1");
		rp.getRelationship().addCoding().setSystem("http://hl7.org/fhir/v2/0131").setCode("C");
		
		results = myVal.validateWithResult(rp);
		outcome = logResultsAndReturnNonInformationalOnes(results);
		assertThat(outcome, empty());

		
		/*
		 * Now a bad code
		 */
		rp = new RelatedPerson();
		rp.getPatient().setReference("Patient/1");
		rp.getRelationship().addCoding().setSystem("http://hl7.org/fhir/v2/0131").setCode("GAGAGAGA");
		
		results = myVal.validateWithResult(rp);
		outcome = logResultsAndReturnNonInformationalOnes(results);
		assertThat(outcome, not(empty()));

	}
	
	@Test
	// @Ignore
	public void testValidateBuiltInProfiles() throws Exception {
		org.hl7.fhir.dstu3.model.Bundle bundle;
		String name = "profiles-resources";
		ourLog.info("Uploading " + name);
		String vsContents;
		vsContents = IOUtils.toString(FhirInstanceValidatorDstu3Test.class.getResourceAsStream("/org/hl7/fhir/instance/model/dstu3/profile/" + name + ".xml"), "UTF-8");

		TreeSet<String> ids = new TreeSet<String>();

		bundle = ourCtx.newXmlParser().parseResource(org.hl7.fhir.dstu3.model.Bundle.class, vsContents);
		for (BundleEntryComponent i : bundle.getEntry()) {
			org.hl7.fhir.dstu3.model.Resource next = i.getResource();
			ids.add(next.getId());

			ourLog.info("Validating {}", next.getId());
			ourLog.info(ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(next));

			ValidationResult output = myVal.validateWithResult(next);
			List<SingleValidationMessage> errors = logResultsAndReturnNonInformationalOnes(output);
			assertThat("Failed to validate " + i.getFullUrl() + " - " + errors, errors, empty());
		}

		ourLog.info("Validated the following:\n{}", ids);
	}

	/**
	 * FHIRPathEngine was throwing Error...
	 */
	@Test
	public void testValidateCrucibleCarePlan() throws Exception {
		org.hl7.fhir.dstu3.model.Bundle bundle;
		String name = "profiles-resources";
		ourLog.info("Uploading " + name);
		String vsContents;
		vsContents = IOUtils.toString(FhirInstanceValidatorDstu3Test.class.getResourceAsStream("/crucible-condition.xml"), "UTF-8");

		ValidationResult output = myVal.validateWithResult(vsContents);
		List<SingleValidationMessage> errors = logResultsAndReturnNonInformationalOnes(output);
	}

	@Test
	@Ignore
	public void testValidateBundleWithObservations() throws Exception {
		String name = "profiles-resources";
		ourLog.info("Uploading " + name);
		String inputString;
		inputString = IOUtils.toString(FhirInstanceValidatorDstu3Test.class.getResourceAsStream("/brian_reinhold_bundle.json"), "UTF-8");
		Bundle bundle = ourCtx.newJsonParser().parseResource(Bundle.class, inputString);

		FluentPathEngine fp = new FluentPathEngine(new HapiWorkerContext(ourCtx, myDefaultValidationSupport));
		List<Base> fpOutput;
		BooleanType bool;

		fpOutput = fp.evaluate(bundle.getEntry().get(0).getResource(), "component.where(code = %resource.code).empty()");
		assertEquals(1, fpOutput.size());
		bool = (BooleanType) fpOutput.get(0);
		assertTrue(bool.getValue());
		//		
		//		fpOutput = fp.evaluate(bundle, "component.where(code = %resource.code).empty()");
		//		assertEquals(1, fpOutput.size());
		//		bool = (BooleanType) fpOutput.get(0);
		//		assertTrue(bool.getValue());

		ValidationResult output = myVal.validateWithResult(inputString);
		List<SingleValidationMessage> errors = logResultsAndReturnNonInformationalOnes(output);
		assertThat(errors, empty());

	}

	@Test
	public void testValidateDocument() throws Exception {
		String vsContents = IOUtils.toString(FhirInstanceValidatorDstu3Test.class.getResourceAsStream("/sample-document.xml"), "UTF-8");

		ValidationResult output = myVal.validateWithResult(vsContents);
		logResultsAndReturnNonInformationalOnes(output);
		assertTrue(output.isSuccessful());
	}

	@SuppressWarnings("unchecked")
	@Before
	public void before() {
		myVal = ourCtx.newValidator();
		myVal.setValidateAgainstStandardSchema(false);
		myVal.setValidateAgainstStandardSchematron(false);

		myMockSupport = mock(IValidationSupport.class);
		ValidationSupportChain validationSupport = new ValidationSupportChain(myMockSupport, myDefaultValidationSupport);
		myInstanceVal = new FhirInstanceValidator(validationSupport);

		myVal.registerValidatorModule(myInstanceVal);

		mySupportedCodeSystemsForExpansion = new HashMap<String, ValueSet.ValueSetExpansionComponent>();

		myValidConcepts = new ArrayList<String>();

		when(myMockSupport.expandValueSet(any(FhirContext.class), any(ConceptSetComponent.class))).thenAnswer(new Answer<ValueSetExpansionComponent>() {
			@Override
			public ValueSetExpansionComponent answer(InvocationOnMock theInvocation) throws Throwable {
				ConceptSetComponent arg = (ConceptSetComponent) theInvocation.getArguments()[0];
				ValueSetExpansionComponent retVal = mySupportedCodeSystemsForExpansion.get(arg.getSystem());
				if (retVal == null) {
					retVal = myDefaultValidationSupport.expandValueSet(any(FhirContext.class), arg);
				}
				ourLog.info("expandValueSet({}) : {}", new Object[] { theInvocation.getArguments()[0], retVal });
				return retVal;
			}
		});
		when(myMockSupport.isCodeSystemSupported(any(FhirContext.class), any(String.class))).thenAnswer(new Answer<Boolean>() {
			@Override
			public Boolean answer(InvocationOnMock theInvocation) throws Throwable {
				boolean retVal = myValidSystems.contains(theInvocation.getArguments()[1]);
				ourLog.info("isCodeSystemSupported({}) : {}", new Object[] { theInvocation.getArguments()[1], retVal });
				return retVal;
			}
		});
		when(myMockSupport.fetchResource(any(FhirContext.class), any(Class.class), any(String.class))).thenAnswer(new Answer<IBaseResource>() {
			@Override
			public IBaseResource answer(InvocationOnMock theInvocation) throws Throwable {
				IBaseResource retVal;
				String id = (String) theInvocation.getArguments()[2];
				if ("Questionnaire/q_jon".equals(id)) {
					retVal = ourCtx.newJsonParser().parseResource(IOUtils.toString(FhirInstanceValidatorDstu3Test.class.getResourceAsStream("/q_jon.json")));
				} else {
					retVal = myDefaultValidationSupport.fetchResource((FhirContext) theInvocation.getArguments()[0], (Class<IBaseResource>) theInvocation.getArguments()[1], id);
				}
				ourLog.info("fetchResource({}, {}) : {}", new Object[] { theInvocation.getArguments()[1], id, retVal });
				return retVal;
			}
		});
		when(myMockSupport.validateCode(any(FhirContext.class), any(String.class), any(String.class), any(String.class))).thenAnswer(new Answer<CodeValidationResult>() {
			@Override
			public CodeValidationResult answer(InvocationOnMock theInvocation) throws Throwable {
				FhirContext ctx = (FhirContext) theInvocation.getArguments()[0];
				String system = (String) theInvocation.getArguments()[1];
				String code = (String) theInvocation.getArguments()[2];
				CodeValidationResult retVal;
				if (myValidConcepts.contains(system + "___" + code)) {
					retVal = new CodeValidationResult(new ConceptDefinitionComponent(new CodeType(code)));
				} else {
					retVal = myDefaultValidationSupport.validateCode(ctx, system, code, (String) theInvocation.getArguments()[2]);
				}
				ourLog.info("validateCode({}, {}, {}) : {}", new Object[] { system, code, (String) theInvocation.getArguments()[2], retVal });
				return retVal;
			}
		});
		when(myMockSupport.fetchCodeSystem(any(FhirContext.class), any(String.class))).thenAnswer(new Answer<CodeSystem>() {
			@Override
			public CodeSystem answer(InvocationOnMock theInvocation) throws Throwable {
				CodeSystem retVal = myDefaultValidationSupport.fetchCodeSystem((FhirContext) theInvocation.getArguments()[0], (String) theInvocation.getArguments()[1]);
				ourLog.info("fetchCodeSystem({}) : {}", new Object[] { (String) theInvocation.getArguments()[1], retVal });
				return retVal;
			}
		});
		when(myMockSupport.fetchStructureDefinition(any(FhirContext.class), any(String.class))).thenAnswer(new Answer<StructureDefinition>() {
			@Override
			public StructureDefinition answer(InvocationOnMock theInvocation) throws Throwable {
				StructureDefinition retVal = myDefaultValidationSupport.fetchStructureDefinition((FhirContext) theInvocation.getArguments()[0], (String) theInvocation.getArguments()[1]);
				ourLog.info("fetchStructureDefinition({}) : {}", new Object[] { (String) theInvocation.getArguments()[1], retVal });
				return retVal;
			}
		});
		when(myMockSupport.fetchAllStructureDefinitions(any(FhirContext.class))).thenAnswer(new Answer<List<StructureDefinition>>() {
			@Override
			public List<StructureDefinition> answer(InvocationOnMock theInvocation) throws Throwable {
				List<StructureDefinition> retVal = myDefaultValidationSupport.fetchAllStructureDefinitions((FhirContext) theInvocation.getArguments()[0]);
				ourLog.info("fetchAllStructureDefinitions()", new Object[] {});
				return retVal;
			}
		});

	}

	private Object defaultString(Integer theLocationLine) {
		return theLocationLine != null ? theLocationLine.toString() : "";
	}

	private List<SingleValidationMessage> logResultsAndReturnAll(ValidationResult theOutput) {
		List<SingleValidationMessage> retVal = new ArrayList<SingleValidationMessage>();

		int index = 0;
		for (SingleValidationMessage next : theOutput.getMessages()) {
			ourLog.info("Result {}: {} - {}:{} {} - {}", new Object[] { index, next.getSeverity(), defaultString(next.getLocationLine()), defaultString(next.getLocationCol()), next.getLocationString(), next.getMessage() });
			index++;

			retVal.add(next);
		}

		return retVal;
	}

	private List<SingleValidationMessage> logResultsAndReturnNonInformationalOnes(ValidationResult theOutput) {
		List<SingleValidationMessage> retVal = new ArrayList<SingleValidationMessage>();

		int index = 0;
		for (SingleValidationMessage next : theOutput.getMessages()) {
			ourLog.info("Result {}: {} - {} - {}", new Object[] { index, next.getSeverity(), next.getLocationString(), next.getMessage() });
			index++;

			if (next.getSeverity() != ResultSeverityEnum.INFORMATION) {
				retVal.add(next);
			}
		}

		return retVal;
	}

	@Test
	public void testValidateBigRawJsonResource() throws Exception {
		InputStream stream = FhirInstanceValidatorDstu3Test.class.getResourceAsStream("/conformance.json.gz");
		stream = new GZIPInputStream(stream);
		String input = IOUtils.toString(stream);

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
	public void testValidateQuestionnaireResponse() throws IOException {
		String input = IOUtils.toString(FhirInstanceValidatorDstu3Test.class.getResourceAsStream("/qr_jon.xml"));

		ValidationResult output = myVal.validateWithResult(input);
		logResultsAndReturnAll(output);

		assertThat(output.getMessages().toString(), containsString("Items not of type group should not have items - Item with linkId 5.1 of type BOOLEAN has 1 item(s)"));
	}

	@Test
	public void testValidateRawJsonResource() {
		//@formatter:off
		String input = "{" + "\"resourceType\":\"Patient\"," + "\"id\":\"123\"" + "}";
		//@formatter:on

		ValidationResult output = myVal.validateWithResult(input);
		assertEquals(output.toString(), 0, output.getMessages().size());
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
		
		ValidationResult output = myVal.validateWithResult(input);
		assertEquals(output.toString(), 1, output.getMessages().size());
		assertEquals("This cannot be parsed as a FHIR object (no namespace)", output.getMessages().get(0).getMessage());
		ourLog.info(output.getMessages().get(0).getLocationString());
	}
	
	@Test
	public void testValidateRawJsonResourceBadAttributes() {
		//@formatter:off
		String input = 
			"{" + 
			"\"resourceType\":\"Patient\"," + 
			"\"id\":\"123\"," +
			"\"foo\":\"123\"" + 
			"}";
		//@formatter:on

		ValidationResult output = myVal.validateWithResult(input);
		assertEquals(output.toString(), 1, output.getMessages().size());
		ourLog.info(output.getMessages().get(0).getLocationString());
		ourLog.info(output.getMessages().get(0).getMessage());
		assertEquals("/Patient", output.getMessages().get(0).getLocationString());
		assertEquals("Unrecognised property '@foo'", output.getMessages().get(0).getMessage());
	}

	@Test
	public void testValidateRawJsonResourceFromExamples() throws Exception {
		// @formatter:off
		String input = IOUtils.toString(FhirInstanceValidator.class.getResourceAsStream("/testscript-search.json"));
		// @formatter:on

		ValidationResult output = myVal.validateWithResult(input);
		logResultsAndReturnNonInformationalOnes(output);
		// assertEquals(output.toString(), 1, output.getMessages().size());
		// ourLog.info(output.getMessages().get(0).getLocationString());
		// ourLog.info(output.getMessages().get(0).getMessage());
		// assertEquals("/foo", output.getMessages().get(0).getLocationString());
		// assertEquals("Element is unknown or does not match any slice", output.getMessages().get(0).getMessage());
	}

	@Test
	public void testValidateRawXmlResource() {
		// @formatter:off
		String input = "<Patient xmlns=\"http://hl7.org/fhir\">" + "<id value=\"123\"/>" + "</Patient>";
		// @formatter:on

		ValidationResult output = myVal.validateWithResult(input);
		assertEquals(output.toString(), 0, output.getMessages().size());
	}

	@Test
	public void testValidateRawXmlResourceWithEmptyPrimitive() {
		// @formatter:off
		String input = "<Patient xmlns=\"http://hl7.org/fhir\"><name><given/></name></Patient>";
		// @formatter:on

		ValidationResult output = myVal.validateWithResult(input);
		assertEquals(output.toString(), 1, output.getMessages().size());
		assertThat(output.getMessages().get(0).getMessage(), containsString("Element must have some content"));
	}

	@Test
	public void testValidateRawXmlResourceWithPrimitiveContainingOnlyAnExtension() {
		// @formatter:off
		String input = "<ActivityDefinition xmlns=\"http://hl7.org/fhir\">\n" + 
				"                        <id value=\"referralToMentalHealthCare\"/>\n" + 
				"                        <status value=\"draft\"/>\n" + 
				"                        <description value=\"refer to primary care mental-health integrated care program for evaluation and treatment of mental health conditions now\"/>\n" + 
				"                        <relatedResource>\n" + 
				"                                <type value=\"citation\"/>\n" + 
				"                                <document>\n" + 
				"                                        <url value=\"blah blah blah\"/>\n" + 
				"                                        <title value=\"citation title\"/>\n" + 
				"                                </document>\n" + 
				"                                <resource>\n" + 
				"                                        <reference value=\"DocumentReference/123\"/> <!-- DocumentReference -->\n" + 
				"                                        <display value=\"citation title\"/>\n" + 
				"                                </resource>\n" + 
				"                        </relatedResource>\n" + 
				"                        <category value=\"referral\"/>\n" + 
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
				"                        <participantType value=\"practitioner\"/>\n" + 
				"                </ActivityDefinition>";
		// @formatter:on

		ValidationResult output = myVal.validateWithResult(input);
		List<SingleValidationMessage> res = logResultsAndReturnNonInformationalOnes(output);
		assertEquals(output.toString(), 0, res.size());
	}

	@Test
	public void testValidateRawXmlResourceBadAttributes() {
		//@formatter:off
    String input = "<Patient xmlns=\"http://hl7.org/fhir\">" + "<id value=\"123\"/>" + "<foo value=\"222\"/>"
        + "</Patient>";
    	//@formatter:on

		ValidationResult output = myVal.validateWithResult(input);
		assertEquals(output.toString(), 1, output.getMessages().size());
		ourLog.info(output.getMessages().get(0).getLocationString());
		ourLog.info(output.getMessages().get(0).getMessage());
		assertEquals("/f:Patient", output.getMessages().get(0).getLocationString());
		assertEquals("Undefined element 'foo\"", output.getMessages().get(0).getMessage());
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

		myInstanceVal.setValidationSupport(myMockSupport);
		ValidationResult output = myVal.validateWithResult(input);
		List<SingleValidationMessage> errors = logResultsAndReturnAll(output);

		assertThat(errors.toString(), containsString("warning"));
		assertThat(errors.toString(), containsString("Unknown code: http://loinc.org / 12345"));
	}

	@Test
	public void testValidateResourceContainingProfileDeclaration() {
		addValidConcept("http://loinc.org", "12345");

		Observation input = new Observation();
		input.getMeta().addProfile("http://hl7.org/fhir/StructureDefinition/devicemetricobservation");

		input.addIdentifier().setSystem("http://acme").setValue("12345");
		input.getEncounter().setReference("http://foo.com/Encounter/9");
		input.setStatus(ObservationStatus.FINAL);
		input.getCode().addCoding().setSystem("http://loinc.org").setCode("12345");

		myInstanceVal.setValidationSupport(myMockSupport);
		ValidationResult output = myVal.validateWithResult(input);
		List<SingleValidationMessage> errors = logResultsAndReturnNonInformationalOnes(output);

		assertThat(errors.toString(), containsString("Element 'Observation.subject': minimum required = 1, but only found 0"));
		assertThat(errors.toString(), containsString("Element encounter @ Observation: max allowed = 0, but found 1"));
		assertThat(errors.toString(), containsString("Element 'Observation.device': minimum required = 1, but only found 0"));
		assertThat(errors.toString(), containsString(""));
	}

	@Test
	public void testValidateResourceContainingProfileDeclarationDoesntResolve() {
		addValidConcept("http://loinc.org", "12345");

		Observation input = new Observation();
		input.getMeta().addProfile("http://foo/myprofile");

		input.getCode().addCoding().setSystem("http://loinc.org").setCode("12345");
		input.setStatus(ObservationStatus.FINAL);

		myInstanceVal.setValidationSupport(myMockSupport);
		ValidationResult output = myVal.validateWithResult(input);
		List<SingleValidationMessage> errors = logResultsAndReturnNonInformationalOnes(output);
		assertEquals(errors.toString(), 1, errors.size());
		assertEquals("StructureDefinition reference \"http://foo/myprofile\" could not be resolved", errors.get(0).getMessage());
	}

	@Test
	public void testValidateResourceFailingInvariant() {
		Observation input = new Observation();

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

		input.setStatus(ObservationStatus.FINAL);
		input.getCode().setText("No code here!");

		ourLog.info(ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(input));

		ValidationResult output = myVal.validateWithResult(input);
		assertEquals(output.getMessages().size(), 0);
	}

	@Test
//	@Ignore 
	// TODO: reenable
	public void testValidateResourceWithDefaultValuesetBadCode() {
		//@formatter:off
		String input = 
			"<Observation xmlns=\"http://hl7.org/fhir\">\n" +
			"   <status value=\"notvalidcode\"/>\n" + 
			"   <code>\n" +
			"      <text value=\"No code here!\"/>\n" + 
			"   </code>\n" + 
			"</Observation>";
		//@formatter:on
		ValidationResult output = myVal.validateWithResult(input);
		logResultsAndReturnAll(output);
		assertEquals("The value provided ('notvalidcode') is not in the value set http://hl7.org/fhir/ValueSet/observation-status (http://hl7.org/fhir/ValueSet/observation-status, and a code is required from this value set)", output.getMessages().get(0).getMessage());
	}

	@Test
	public void testValidateResourceWithExampleBindingCodeValidationFailing() {
		Observation input = new Observation();

		myInstanceVal.setValidationSupport(myMockSupport);

		input.setStatus(ObservationStatus.FINAL);
		input.getCode().addCoding().setSystem("http://loinc.org").setCode("12345");

		ValidationResult output = myVal.validateWithResult(input);
		List<SingleValidationMessage> errors = logResultsAndReturnNonInformationalOnes(output);
		assertEquals(errors.toString(), 0, errors.size());

	}

	@Test
	public void testValidateResourceWithExampleBindingCodeValidationPassingLoinc() {
		Observation input = new Observation();

		myInstanceVal.setValidationSupport(myMockSupport);
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

		ValueSetExpansionComponent expansionComponent = new ValueSetExpansionComponent();
		expansionComponent.addContains().setSystem("http://loinc.org").setCode("12345").setDisplay("Some display code");

		mySupportedCodeSystemsForExpansion.put("http://loinc.org", expansionComponent);
		myInstanceVal.setValidationSupport(myMockSupport);
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

		myInstanceVal.setValidationSupport(myMockSupport);
		addValidConcept("http://acme.org", "12345");

		input.setStatus(ObservationStatus.FINAL);
		input.getCode().addCoding().setSystem("http://acme.org").setCode("12345");

		ValidationResult output = myVal.validateWithResult(input);
		List<SingleValidationMessage> errors = logResultsAndReturnAll(output);
		assertEquals(errors.toString(), 0, errors.size());
	}

	@Test
	public void testValidateResourceWithExampleBindingCodeValidationFailingNonLoinc() {
		Observation input = new Observation();

		myInstanceVal.setValidationSupport(myMockSupport);
		addValidConcept("http://acme.org", "12345");

		input.setStatus(ObservationStatus.FINAL);
		input.getCode().addCoding().setSystem("http://acme.org").setCode("9988877");

		ValidationResult output = myVal.validateWithResult(input);
		List<SingleValidationMessage> errors = logResultsAndReturnAll(output);
		assertThat(errors.toString(), errors.size(), greaterThan(0));
		assertEquals("Unknown code: http://acme.org / 9988877", errors.get(0).getMessage());
		
		
	}

	@Test
	public void testValidateResourceWithValuesetExpansionBad() {

		Patient patient = new Patient();
		patient.addIdentifier().setSystem("http://example.com/").setValue("12345").getType().addCoding().setSystem("http://example.com/foo/bar").setCode("bar");

		ValidationResult output = myVal.validateWithResult(patient);
		List<SingleValidationMessage> all = logResultsAndReturnAll(output);
		assertEquals(1, all.size());
		assertEquals("Patient.identifier.type", all.get(0).getLocationString());
		assertEquals("None of the codes provided are in the value set http://hl7.org/fhir/ValueSet/identifier-type (http://hl7.org/fhir/ValueSet/identifier-type, and a code should come from this value set unless it has no suitable code)", all.get(0).getMessage());
		assertEquals(ResultSeverityEnum.WARNING, all.get(0).getSeverity());

	}

	@Test
	public void testValidateResourceWithValuesetExpansionGood() {
		Patient patient = new Patient();
		patient.addIdentifier().setSystem("http://system").setValue("12345").getType().addCoding().setSystem("http://hl7.org/fhir/v2/0203").setCode("MR");

		ValidationResult output = myVal.validateWithResult(patient);
		List<SingleValidationMessage> all = logResultsAndReturnAll(output);
		assertEquals(0, all.size());
	}

	@Test
	@Ignore
	public void testValidateStructureDefinition() throws IOException {
		String input = IOUtils.toString(FhirInstanceValidatorDstu3Test.class.getResourceAsStream("/sdc-questionnaire.profile.xml"));

		ValidationResult output = myVal.validateWithResult(input);
		logResultsAndReturnAll(output);

		assertEquals(output.toString(), 3, output.getMessages().size());
		ourLog.info(output.getMessages().get(0).getLocationString());
		ourLog.info(output.getMessages().get(0).getMessage());
	}

	@AfterClass
	public static void afterClassClearContext() {
		myDefaultValidationSupport.flush();
		myDefaultValidationSupport = null;
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
