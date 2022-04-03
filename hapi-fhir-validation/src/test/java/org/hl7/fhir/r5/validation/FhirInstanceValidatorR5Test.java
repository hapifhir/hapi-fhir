package org.hl7.fhir.r5.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.ConceptValidationOptions;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.context.support.ValueSetExpansionOptions;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.test.utilities.LoggingExtension;
import ca.uhn.fhir.util.TestUtil;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ResultSeverityEnum;
import ca.uhn.fhir.validation.SingleValidationMessage;
import ca.uhn.fhir.validation.ValidationResult;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.hl7.fhir.common.hapi.validation.support.CachingValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.CommonCodeSystemsTerminologyService;
import org.hl7.fhir.common.hapi.validation.support.InMemoryTerminologyServerValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.Base64BinaryType;
import org.hl7.fhir.r5.model.CodeSystem;
import org.hl7.fhir.r5.model.CodeType;
import org.hl7.fhir.r5.model.ContactPoint;
import org.hl7.fhir.r5.model.DateTimeType;
import org.hl7.fhir.r5.model.DocumentReference;
import org.hl7.fhir.r5.model.Enumerations;
import org.hl7.fhir.r5.model.Extension;
import org.hl7.fhir.r5.model.Narrative;
import org.hl7.fhir.r5.model.Observation;
import org.hl7.fhir.r5.model.Patient;
import org.hl7.fhir.r5.model.Period;
import org.hl7.fhir.r5.model.Practitioner;
import org.hl7.fhir.r5.model.Procedure;
import org.hl7.fhir.r5.model.QuestionnaireResponse;
import org.hl7.fhir.r5.model.Reference;
import org.hl7.fhir.r5.model.RelatedPerson;
import org.hl7.fhir.r5.model.StringType;
import org.hl7.fhir.r5.model.StructureDefinition;
import org.hl7.fhir.r5.model.ValueSet;
import org.hl7.fhir.r5.model.ValueSet.ValueSetExpansionComponent;
import org.hl7.fhir.r5.terminologies.ValueSetExpander;
import org.hl7.fhir.r5.utils.validation.IResourceValidator;
import org.hl7.fhir.r5.utils.validation.IValidationPolicyAdvisor;
import org.hl7.fhir.r5.utils.validation.IValidatorResourceFetcher;
import org.hl7.fhir.r5.utils.validation.constants.BestPracticeWarningLevel;
import org.hl7.fhir.r5.utils.validation.constants.ReferenceValidationPolicy;
import org.hl7.fhir.utilities.xhtml.XhtmlNode;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FhirInstanceValidatorR5Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirInstanceValidatorR5Test.class);
	private static FhirContext ourCtx = FhirContext.forR5();
	private static DefaultProfileValidationSupport myDefaultValidationSupport = new DefaultProfileValidationSupport(ourCtx);
	@RegisterExtension
	public LoggingExtension myLoggingExtension = new LoggingExtension();
	private FhirInstanceValidator myInstanceVal;
	private IValidationSupport myMockSupport;
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
	@BeforeEach
	public void before() {
		myVal = ourCtx.newValidator();
		myVal.setValidateAgainstStandardSchema(false);
		myVal.setValidateAgainstStandardSchematron(false);

		myMockSupport = mock(IValidationSupport.class);
		when(myMockSupport.getFhirContext()).thenReturn(ourCtx);
		myValidationSupport = new CachingValidationSupport(new ValidationSupportChain(myMockSupport, myDefaultValidationSupport, new InMemoryTerminologyServerValidationSupport(ourCtx), new CommonCodeSystemsTerminologyService(ourCtx)));
		myInstanceVal = new FhirInstanceValidator(myValidationSupport);

		myVal.registerValidatorModule(myInstanceVal);

		mySupportedCodeSystemsForExpansion = new HashMap<>();

		myValidConcepts = new ArrayList<>();

		when(myMockSupport.expandValueSet(any(), nullable(ValueSetExpansionOptions.class), nullable(IBaseResource.class))).thenAnswer(theInvocation -> {
			ValueSet arg = (ValueSet) theInvocation.getArgument(2, IBaseResource.class);
			ValueSetExpansionComponent retVal = mySupportedCodeSystemsForExpansion.get(arg.getCompose().getIncludeFirstRep().getSystem());
			if (retVal == null) {
				IValidationSupport.ValueSetExpansionOutcome outcome = myDefaultValidationSupport.expandValueSet(new ValidationSupportContext(myDefaultValidationSupport), null, arg);
				return outcome;
			}
			ourLog.debug("expandValueSet({}) : {}", new Object[]{theInvocation.getArguments()[0], retVal});

			ValueSet valueset = new ValueSet();
			valueset.setExpansion(retVal);
			return new ValueSetExpander.ValueSetExpansionOutcome(valueset);
		});
		when(myMockSupport.isCodeSystemSupported(any(), nullable(String.class))).thenAnswer(new Answer<Boolean>() {
			@Override
			public Boolean answer(InvocationOnMock theInvocation) throws Throwable {
				boolean retVal = myValidSystems.contains(theInvocation.getArguments()[1]);
				ourLog.debug("isCodeSystemSupported({}) : {}", new Object[]{theInvocation.getArguments()[1], retVal});
				return retVal;
			}
		});
		when(myMockSupport.fetchResource(nullable(Class.class), nullable(String.class))).thenAnswer(new Answer<IBaseResource>() {
			@Override
			public IBaseResource answer(InvocationOnMock theInvocation) throws Throwable {
				IBaseResource retVal;
				String id = (String) theInvocation.getArguments()[1];
				Class<IBaseResource> clazz = (Class<IBaseResource>) theInvocation.getArguments()[0];
				if ("Questionnaire/q_jon".equals(id)) {
					retVal = ourCtx.newJsonParser().parseResource(IOUtils.toString(FhirInstanceValidatorR5Test.class.getResourceAsStream("/q_jon.json")));
				} else {
					retVal = myDefaultValidationSupport.fetchResource(clazz, id);
				}
				ourLog.debug("fetchResource({}, {}) : {}", clazz, id, retVal);
				return retVal;
			}
		});
		when(myMockSupport.validateCode(any(), any(), nullable(String.class), nullable(String.class), nullable(String.class), nullable(String.class))).thenAnswer(new Answer<IValidationSupport.CodeValidationResult>() {
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
					return new IValidationSupport.CodeValidationResult().setSeverity(IValidationSupport.IssueSeverity.ERROR).setMessage("Unknown code");
				} else {
					retVal = myDefaultValidationSupport.validateCode(new ValidationSupportContext(myDefaultValidationSupport), options, system, code, display, valueSetUrl);
				}
				ourLog.debug("validateCode({}, {}, {}, {}) : {}", system, code, display, valueSetUrl, retVal);
				return retVal;
			}
		});
		when(myMockSupport.fetchCodeSystem(nullable(String.class))).thenAnswer(new Answer<CodeSystem>() {
			@Override
			public CodeSystem answer(InvocationOnMock theInvocation) {
				String codeSystem = (String) theInvocation.getArguments()[0];
				CodeSystem retVal = (CodeSystem) myDefaultValidationSupport.fetchCodeSystem(codeSystem);
				ourLog.debug("fetchCodeSystem({}) : {}", new Object[]{codeSystem, retVal});
				return retVal;
			}
		});
		when(myMockSupport.fetchStructureDefinition(nullable(String.class))).thenAnswer(new Answer<StructureDefinition>() {
			@Override
			public StructureDefinition answer(InvocationOnMock theInvocation) {
				StructureDefinition retVal = (StructureDefinition) myDefaultValidationSupport.fetchStructureDefinition((String) theInvocation.getArguments()[1]);
				ourLog.debug("fetchStructureDefinition({}) : {}", new Object[]{theInvocation.getArguments()[0], retVal});
				return retVal;
			}
		});
		when(myMockSupport.fetchAllStructureDefinitions()).thenAnswer(new Answer<List<StructureDefinition>>() {
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

	private List<SingleValidationMessage> logResultsAndReturnAll(ValidationResult theOutput) {
		List<SingleValidationMessage> retVal = new ArrayList<SingleValidationMessage>();

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

		FhirValidator val = ourCtx.newValidator();
		val.registerValidatorModule(new FhirInstanceValidator(myValidationSupport));

		ValidationResult result = val.validateWithResult(input);
		List<SingleValidationMessage> all = logResultsAndReturnAll(result);
		assertFalse(result.isSuccessful());
		assertEquals("ele-1: 'All FHIR elements must have a @value or children' Rule 'All FHIR elements must have a @value or children' Failed", all.get(0).getMessage());
	}

	/**
	 * See #942
	 */
	@Test
	public void testValidateDoesntEnforceBestPracticesByDefault() {
		Observation input = new Observation();
		input.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);
		input.setStatus(Enumerations.ObservationStatus.AMENDED);
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
		BestPracticeWarningLevel level = BestPracticeWarningLevel.Error;
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
		DocumentReference med = new DocumentReference();
		med.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);
		med.getContentFirstRep().getAttachment().setContentType(Constants.CT_OCTET_STREAM);
		med.getContentFirstRep().getAttachment().setDataElement(value);
		med.getContentFirstRep().getAttachment().setTitle("bbbb syst");
		med.setStatus(Enumerations.DocumentReferenceStatus.CURRENT);
		String encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(med);

		encoded = encoded.replace(value.getValueAsString(), "%%%2@()()");

		ourLog.info("Encoded: {}", encoded);

		ValidationResult output = myVal.validateWithResult(encoded);
		List<SingleValidationMessage> errors = logResultsAndReturnNonInformationalOnes(output);
		assertEquals(1, errors.size(), ()->errors.toString());
		assertEquals("The value '%%%2@()()' is not a valid Base64 value", errors.get(0).getMessage());

	}

	@Test
	public void testBase64Valid() {
		Base64BinaryType value = new Base64BinaryType(new byte[]{2, 3, 4, 5, 6, 7, 8, 9, 8, 7, 6, 5, 4, 3, 2, 1});
		DocumentReference med = new DocumentReference();
		med.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);
		med.getContentFirstRep().getAttachment().setContentType(Constants.CT_OCTET_STREAM);
		med.getContentFirstRep().getAttachment().setDataElement(value);
		med.getContentFirstRep().getAttachment().setTitle("bbbb syst");
		med.setStatus(Enumerations.DocumentReferenceStatus.CURRENT);
		String encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(med);

		ourLog.info("Encoded: {}", encoded);

		ValidationResult output = myVal.validateWithResult(encoded);
		List<SingleValidationMessage> errors = logResultsAndReturnNonInformationalOnes(output);
		assertEquals(0, errors.size(), ()->errors.toString());

	}

	@Test
	public void testCompareTimesWithDifferentTimezones() {
		Procedure procedure = new Procedure();
		procedure.setStatus(Enumerations.EventStatus.COMPLETED);
		procedure.getSubject().setReference("Patient/1");
		procedure.getCode().setText("Some proc");

		Period period = new Period();
		period.setStartElement(new DateTimeType("2000-01-01T00:00:01+05:00"));
		period.setEndElement(new DateTimeType("2000-01-01T00:00:00+04:00"));
		assertThat(period.getStart().getTime(), lessThan(period.getEnd().getTime()));
		procedure.setOccurrence(period);

		FhirValidator val = ourCtx.newValidator();
		val.registerValidatorModule(new FhirInstanceValidator(myValidationSupport));

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
		t.setSystem(org.hl7.fhir.r5.model.ContactPoint.ContactPointSystem.URL);
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
	public void testInvocationOfValidatorFetcher() throws IOException {

		String input = IOUtils.toString(FhirInstanceValidator.class.getResourceAsStream("/vitals.json"), Charsets.UTF_8);

		IValidatorResourceFetcher resourceFetcher = mock(IValidatorResourceFetcher.class);
		IValidationPolicyAdvisor policyAdvisor = mock(IValidationPolicyAdvisor.class);

		when(policyAdvisor.policyForReference(any(), any(), any(), any())).thenReturn(ReferenceValidationPolicy.CHECK_TYPE_IF_EXISTS);
		myInstanceVal.setValidatorResourceFetcher(resourceFetcher);
		myInstanceVal.setValidatorPolicyAdvisor(policyAdvisor);
		myVal.validateWithResult(input);

		//verify(resourceFetcher, times(13)).resolveURL(any(), any(), anyString(), anyString(), anyString());
		verify(policyAdvisor, times(4)).policyForReference(any(), any(), anyString(), anyString());
		//verify(resourceFetcher, times(3)).fetch(any(), any(), anyString());
	}

	@Test
	public void testIsNoTerminologyChecks() {
		assertFalse(myInstanceVal.isNoTerminologyChecks());
		myInstanceVal.setNoTerminologyChecks(true);
		assertTrue(myInstanceVal.isNoTerminologyChecks());
	}


	@Test
	public void testValidateRawJsonResource() {
		String input = "{" +
			"  \"resourceType\":\"Patient\"," +
			"  \"text\": {\n" +
			"    \"status\": \"generated\",\n" +
			"    \"div\": \"<div xmlns=\\\"http://www.w3.org/1999/xhtml\\\">HELLO</div>\"\n" +
			"  },\n" +
			"  \"id\":\"123\"" +
			"}";

		ValidationResult output = myVal.validateWithResult(input);
		assertEquals(0, output.getMessages().size(), output.toString());
	}

	@Test
	public void testValidateRawJsonResourceBadAttributes() {
		String input =
			"{" +
				"\"resourceType\":\"Patient\"," +
				"  \"text\": {\n" +
				"    \"status\": \"generated\",\n" +
				"    \"div\": \"<div xmlns=\\\"http://www.w3.org/1999/xhtml\\\">HELLO</div>\"\n" +
				"  },\n" +
				"\"id\":\"123\"," +
				"\"foo\":\"123\"" +
				"}";

		ValidationResult output = myVal.validateWithResult(input);
		assertEquals(1, output.getMessages().size(), output.toString());
		ourLog.info(output.getMessages().get(0).getLocationString());
		ourLog.info(output.getMessages().get(0).getMessage());
		assertEquals("Patient", output.getMessages().get(0).getLocationString());
		assertEquals("Unrecognised property '@foo'", output.getMessages().get(0).getMessage());
	}

	@Test
	@Disabled
	public void testValidateRawJsonResourceFromExamples() throws Exception {
		// @formatter:off
		String input = IOUtils.toString(FhirInstanceValidator.class.getResourceAsStream("/testscript-search.json"), Charsets.UTF_8);
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
		assertEquals(1, output.getMessages().size(), output.toString());

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
		assertEquals( 1, output.getMessages().size(), output.toString());

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
		assertEquals(0, output.getMessages().size(), output.toString());
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
		assertEquals( 1, output.getMessages().size(), output.toString());
		ourLog.info(output.getMessages().get(0).getLocationString());
		ourLog.info(output.getMessages().get(0).getMessage());
		assertEquals("/f:Patient", output.getMessages().get(0).getLocationString());
		assertEquals("Undefined element 'foo'", output.getMessages().get(0).getMessage());
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
		assertEquals( 3, messages.size(), output.toString());
		assertThat(messages.get(0).getMessage(), containsString("Element must have some content"));
		assertThat(messages.get(1).getMessage(), containsString("ele-1: 'All FHIR elements must have a @value or children' Rule 'All FHIR elements must have a @value or children' Failed"));
		assertThat(messages.get(2).getMessage(), containsString("Primitive types must have a value or must have child extensions"));
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

		ValidationResult output = myVal.validateWithResult(input);
		List<SingleValidationMessage> res = logResultsAndReturnNonInformationalOnes(output);
		assertEquals(1, res.size(), output.toString());
		assertEquals("A code with no system has no defined meaning. A system should be provided", res.get(0).getMessage());
	}

	@Test
	public void testValidateRawXmlWithMissingRootNamespace() {
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

		ValidationResult output = myVal.validateWithResult(input);
		assertEquals(1, output.getMessages().size(), output.toString());
		assertEquals("This does not appear to be a FHIR resource (unknown namespace/name 'noNamespace::Patient')", output.getMessages().get(0).getMessage());
		ourLog.info(output.getMessages().get(0).getLocationString());
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
		input.setStatus(Enumerations.ObservationStatus.FINAL);
		input.getCode().addCoding().setSystem("http://loinc.org").setCode("12345");

		myInstanceVal.setValidationSupport(myValidationSupport);
		ValidationResult output = myVal.validateWithResult(input);
		List<SingleValidationMessage> errors = logResultsAndReturnAll(output);

		assertEquals(ResultSeverityEnum.ERROR, errors.get(0).getSeverity());
		assertEquals("Unknown code for 'http://loinc.org#12345'", errors.get(0).getMessage());
	}

	@Test
	public void testValidateResourceContainingProfileDeclaration() {
		addValidConcept("http://loinc.org", "12345");

		Observation input = new Observation();
		input.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);
		input.getMeta().addProfile("http://hl7.org/fhir/StructureDefinition/devicemetricobservation");

		input.addIdentifier().setSystem("http://acme").setValue("12345");
		input.getEncounter().setReference("http://foo.com/Encounter/9");
		input.setStatus(Enumerations.ObservationStatus.FINAL);
		input.getCode().addCoding().setSystem("http://loinc.org").setCode("12345");

		myInstanceVal.setValidationSupport(myValidationSupport);
		ValidationResult output = myVal.validateWithResult(input);
		List<SingleValidationMessage> errors = logResultsAndReturnNonInformationalOnes(output);

		assertThat(errors.toString(), containsString("Observation.subject: minimum required = 1, but only found 0"));
		assertThat(errors.toString(), containsString("Observation.encounter: max allowed = 0, but found 1"));
		assertThat(errors.toString(), containsString("Observation.device: minimum required = 1, but only found 0"));
	}

	@Test
	public void testValidateResourceContainingProfileDeclarationDoesntResolve() {
		addValidConcept("http://loinc.org", "12345");

		Observation input = new Observation();
		input.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);
		input.getMeta().addProfile("http://foo/structuredefinition/myprofile");

		input.getCode().addCoding().setSystem("http://loinc.org").setCode("12345");
		input.setStatus(Enumerations.ObservationStatus.FINAL);

		myInstanceVal.setValidationSupport(myValidationSupport);
		ValidationResult output = myVal.validateWithResult(input);
		List<SingleValidationMessage> errors = logResultsAndReturnNonInformationalOnes(output);
		assertThat(errors.toString(), containsString("Profile reference 'http://foo/structuredefinition/myprofile' has not been checked because it is unknown"));
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
		assertEquals("Observation.status: minimum required = 1, but only found 0 (from http://hl7.org/fhir/StructureDefinition/Observation)", output.getMessages().get(0).getMessage());

	}

	@Test
	public void testValidateResourceWithDefaultValueset() {
		Observation input = new Observation();

		input.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);
		input.setStatus(Enumerations.ObservationStatus.FINAL);
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
		assertThat(
			output.getMessages().get(0).getMessage(),
			containsString("The value provided ('notvalidcode') is not in the value set 'ObservationStatus' (http://hl7.org/fhir/ValueSet/observation-status|4.6.0), and a code is required from this value set) (error message = Unknown code 'notvalidcode' for in-memory expansion of ValueSet 'http://hl7.org/fhir/ValueSet/observation-status')")
			);
	}

	@Test
	public void testValidateResourceWithExampleBindingCodeValidationFailing() {
		Observation input = new Observation();
		input.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);

		myInstanceVal.setValidationSupport(myValidationSupport);

		input.setStatus(Enumerations.ObservationStatus.FINAL);
		input.getCode().addCoding().setSystem("http://loinc.org").setCode("12345");

		ValidationResult output = myVal.validateWithResult(input);
		List<SingleValidationMessage> errors = logResultsAndReturnNonInformationalOnes(output);
		assertEquals( 0, errors.size(), errors.toString());

	}

	@Test
	public void testValidateResourceWithExampleBindingCodeValidationFailingNonLoinc() {
		Observation input = new Observation();
		input.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);

		myInstanceVal.setValidationSupport(myValidationSupport);
		addValidConcept("http://acme.org", "12345");

		input.setStatus(Enumerations.ObservationStatus.FINAL);
		input.getCode().addCoding().setSystem("http://acme.org").setCode("9988877");

		ValidationResult output = myVal.validateWithResult(input);
		List<SingleValidationMessage> errors = logResultsAndReturnAll(output);
		assertThat(errors.toString(), errors.size(), greaterThan(0));
		assertEquals("Unknown code for 'http://acme.org#9988877'", errors.get(0).getMessage());

	}

	@Test
	public void testValidateResourceWithExampleBindingCodeValidationPassingLoinc() {
		Observation input = new Observation();
		input.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);

		myInstanceVal.setValidationSupport(myValidationSupport);
		addValidConcept("http://loinc.org", "12345");

		input.setStatus(Enumerations.ObservationStatus.FINAL);
		input.getCode().addCoding().setSystem("http://loinc.org").setCode("12345");

		ValidationResult output = myVal.validateWithResult(input);
		List<SingleValidationMessage> errors = logResultsAndReturnNonInformationalOnes(output);
		assertEquals( 0, errors.size(),errors.toString());
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

		input.setStatus(Enumerations.ObservationStatus.FINAL);
		input.getCode().addCoding().setSystem("http://loinc.org").setCode("1234");

		ValidationResult output = myVal.validateWithResult(input);
		List<SingleValidationMessage> errors = logResultsAndReturnNonInformationalOnes(output);
		assertEquals(1, errors.size());
		assertEquals("Unknown code for 'http://loinc.org#1234'", errors.get(0).getMessage());
	}

	@Test
	public void testValidateResourceWithExampleBindingCodeValidationPassingNonLoinc() {
		Observation input = new Observation();
		input.getText().setDiv(new XhtmlNode().setValue("<div>AA</div>")).setStatus(Narrative.NarrativeStatus.GENERATED);

		myInstanceVal.setValidationSupport(myValidationSupport);
		addValidConcept("http://acme.org", "12345");

		input.setStatus(Enumerations.ObservationStatus.FINAL);
		input.getCode().addCoding().setSystem("http://acme.org").setCode("12345");

		ValidationResult output = myVal.validateWithResult(input);
		List<SingleValidationMessage> errors = logResultsAndReturnAll(output);
		assertEquals( 0, errors.size(), errors.toString());
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
		assertEquals(
			"None of the codings provided are in the value set 'IdentifierType' (http://hl7.org/fhir/ValueSet/identifier-type), and a coding should come from this value set unless it has no suitable code (note that the validator cannot judge what is suitable) (codes = http://example.com/foo/bar#bar)",
			all.get(0).getMessage());
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

		ValidationResult output = myVal.validateWithResult(patient);
		List<SingleValidationMessage> all = logResultsAndReturnAll(output);
		assertEquals(0, all.size());
	}



	@Test
	@Disabled
	public void testValidateStructureDefinition() throws IOException {
		String input = IOUtils.toString(FhirInstanceValidatorR5Test.class.getResourceAsStream("/sdc-questionnaire.profile.xml"));

		ValidationResult output = myVal.validateWithResult(input);
		logResultsAndReturnAll(output);

		assertEquals( 3, output.getMessages().size(), output.toString());
		ourLog.info(output.getMessages().get(0).getLocationString());
		ourLog.info(output.getMessages().get(0).getMessage());
	}

	@AfterAll
	public static void afterClassClearContext() {
		myDefaultValidationSupport.flush();
		myDefaultValidationSupport = null;
		TestUtil.randomizeLocaleAndTimezone();
	}


}
