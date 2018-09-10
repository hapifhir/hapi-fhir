package org.hl7.fhir.r4.validation;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.util.TestUtil;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ResultSeverityEnum;
import ca.uhn.fhir.validation.SingleValidationMessage;
import ca.uhn.fhir.validation.ValidationResult;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.dstu3.hapi.validation.ResourceValidatorDstu3Test;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.conformance.ProfileUtilities;
import org.hl7.fhir.r4.context.IWorkerContext;
import org.hl7.fhir.r4.hapi.ctx.DefaultProfileValidationSupport;
import org.hl7.fhir.r4.hapi.ctx.HapiWorkerContext;
import org.hl7.fhir.r4.hapi.ctx.IValidationSupport;
import org.hl7.fhir.r4.hapi.ctx.IValidationSupport.CodeValidationResult;
import org.hl7.fhir.r4.hapi.validation.FhirInstanceValidator;
import org.hl7.fhir.r4.model.Base;
import org.hl7.fhir.r4.model.Base64BinaryType;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.hapi.validation.CachingValidationSupport;
import org.hl7.fhir.r4.hapi.validation.FhirInstanceValidator;
import org.hl7.fhir.r4.hapi.validation.PrePopulatedValidationSupport;
import org.hl7.fhir.r4.hapi.validation.ValidationSupportChain;
import org.hl7.fhir.r4.model.*;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeSystem.ConceptDefinitionComponent;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.Consent;
import org.hl7.fhir.r4.model.ContactPoint;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.Media;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Observation.ObservationStatus;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Period;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.Procedure;
import org.hl7.fhir.r4.model.QuestionnaireResponse;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.RelatedPerson;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.r4.model.StructureDefinition.StructureDefinitionKind;
import org.hl7.fhir.r4.model.ValueSet.ConceptSetComponent;
import org.hl7.fhir.r4.model.ValueSet.ValueSetExpansionComponent;
import org.hl7.fhir.r4.utils.FHIRPathEngine;
import org.hl7.fhir.r4.utils.IResourceValidator;
import org.hl7.fhir.utilities.validation.ValidationMessage;
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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;

public class FhirInstanceValidatorR4Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirInstanceValidatorR4Test.class);
	private static DefaultProfileValidationSupport myDefaultValidationSupport = new DefaultProfileValidationSupport();
	private static FhirContext ourCtx = FhirContext.forR4();
	@Rule
	public TestRule watcher = new TestWatcher() {
		@Override
		protected void starting(Description description) {
			ourLog.info("Starting test: " + description.getMethodName());
		}
	};
	private FhirInstanceValidator myInstanceVal;
	private IValidationSupport myMockSupport;
	private Map<String, ValueSetExpansionComponent> mySupportedCodeSystemsForExpansion;
	private FhirValidator myVal;
	private ArrayList<String> myValidConcepts;
	private Set<String> myValidSystems = new HashSet<String>();

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

		myMockSupport = mock(IValidationSupport.class);
		CachingValidationSupport validationSupport = new CachingValidationSupport(new ValidationSupportChain(myMockSupport, myDefaultValidationSupport));
		myInstanceVal = new FhirInstanceValidator(validationSupport);

		myVal.registerValidatorModule(myInstanceVal);

		mySupportedCodeSystemsForExpansion = new HashMap<>();

		myValidConcepts = new ArrayList<>();

		when(myMockSupport.expandValueSet(nullable(FhirContext.class), nullable(ConceptSetComponent.class))).thenAnswer(new Answer<ValueSetExpansionComponent>() {
			@Override
			public ValueSetExpansionComponent answer(InvocationOnMock theInvocation) throws Throwable {
				ConceptSetComponent arg = (ConceptSetComponent) theInvocation.getArguments()[ 0 ];
				ValueSetExpansionComponent retVal = mySupportedCodeSystemsForExpansion.get(arg.getSystem());
				if (retVal == null) {
					retVal = myDefaultValidationSupport.expandValueSet(nullable(FhirContext.class), arg);
				}
				ourLog.debug("expandValueSet({}) : {}", new Object[] {theInvocation.getArguments()[ 0 ], retVal});
				return retVal;
			}
		});
		when(myMockSupport.isCodeSystemSupported(nullable(FhirContext.class), nullable(String.class))).thenAnswer(new Answer<Boolean>() {
			@Override
			public Boolean answer(InvocationOnMock theInvocation) throws Throwable {
				boolean retVal = myValidSystems.contains(theInvocation.getArguments()[ 1 ]);
				ourLog.debug("isCodeSystemSupported({}) : {}", new Object[] {theInvocation.getArguments()[ 1 ], retVal});
				return retVal;
			}
		});
		when(myMockSupport.fetchResource(nullable(FhirContext.class), nullable(Class.class), nullable(String.class))).thenAnswer(new Answer<IBaseResource>() {
			@Override
			public IBaseResource answer(InvocationOnMock theInvocation) throws Throwable {
				IBaseResource retVal;
				String id = (String) theInvocation.getArguments()[ 2 ];
				if ("Questionnaire/q_jon".equals(id)) {
					retVal = ourCtx.newJsonParser().parseResource(IOUtils.toString(FhirInstanceValidatorR4Test.class.getResourceAsStream("/q_jon.json")));
				} else {
					retVal = myDefaultValidationSupport.fetchResource((FhirContext) theInvocation.getArguments()[ 0 ], (Class<IBaseResource>) theInvocation.getArguments()[ 1 ], id);
				}
				ourLog.debug("fetchResource({}, {}) : {}", new Object[] {theInvocation.getArguments()[ 1 ], id, retVal});
				return retVal;
			}
		});
		when(myMockSupport.validateCode(nullable(FhirContext.class), nullable(String.class), nullable(String.class), nullable(String.class))).thenAnswer(new Answer<CodeValidationResult>() {
			@Override
			public CodeValidationResult answer(InvocationOnMock theInvocation) throws Throwable {
				FhirContext ctx = (FhirContext) theInvocation.getArguments()[ 0 ];
				String system = (String) theInvocation.getArguments()[ 1 ];
				String code = (String) theInvocation.getArguments()[ 2 ];
				CodeValidationResult retVal;
				if (myValidConcepts.contains(system + "___" + code)) {
					retVal = new CodeValidationResult(new ConceptDefinitionComponent(new CodeType(code)));
				} else {
					retVal = myDefaultValidationSupport.validateCode(ctx, system, code, (String) theInvocation.getArguments()[ 2 ]);
				}
				ourLog.debug("validateCode({}, {}, {}) : {}", new Object[] {system, code, (String) theInvocation.getArguments()[ 2 ], retVal});
				return retVal;
			}
		});
		when(myMockSupport.fetchCodeSystem(nullable(FhirContext.class), nullable(String.class))).thenAnswer(new Answer<CodeSystem>() {
			@Override
			public CodeSystem answer(InvocationOnMock theInvocation) throws Throwable {
				CodeSystem retVal = myDefaultValidationSupport.fetchCodeSystem((FhirContext) theInvocation.getArguments()[ 0 ], (String) theInvocation.getArguments()[ 1 ]);
				ourLog.debug("fetchCodeSystem({}) : {}", new Object[] {(String) theInvocation.getArguments()[ 1 ], retVal});
				return retVal;
			}
		});
		when(myMockSupport.fetchStructureDefinition(nullable(FhirContext.class), nullable(String.class))).thenAnswer(new Answer<StructureDefinition>() {
			@Override
			public StructureDefinition answer(InvocationOnMock theInvocation) throws Throwable {
				StructureDefinition retVal = myDefaultValidationSupport.fetchStructureDefinition((FhirContext) theInvocation.getArguments()[ 0 ], (String) theInvocation.getArguments()[ 1 ]);
				ourLog.debug("fetchStructureDefinition({}) : {}", new Object[] {(String) theInvocation.getArguments()[ 1 ], retVal});
				return retVal;
			}
		});
		when(myMockSupport.fetchAllStructureDefinitions(nullable(FhirContext.class))).thenAnswer(new Answer<List<StructureDefinition>>() {
			@Override
			public List<StructureDefinition> answer(InvocationOnMock theInvocation) throws Throwable {
				List<StructureDefinition> retVal = myDefaultValidationSupport.fetchAllStructureDefinitions((FhirContext) theInvocation.getArguments()[ 0 ]);
				ourLog.debug("fetchAllStructureDefinitions()", new Object[] {});
				return retVal;
			}
		});

	}

	private Object defaultString(Integer theLocationLine) {
		return theLocationLine != null ? theLocationLine.toString() : "";
	}

	private StructureDefinition loadStructureDefinition(DefaultProfileValidationSupport theDefaultValSupport, String theResName) throws IOException, FHIRException {
		StructureDefinition derived = ourCtx.newXmlParser().parseResource(StructureDefinition.class, IOUtils.toString(ResourceValidatorDstu3Test.class.getResourceAsStream(theResName)));
		StructureDefinition base = theDefaultValSupport.fetchStructureDefinition(ourCtx, derived.getBaseDefinition());
		Validate.notNull(base);

		IWorkerContext worker = new HapiWorkerContext(ourCtx, theDefaultValSupport);
		List<ValidationMessage> issues = new ArrayList<>();
		ProfileUtilities profileUtilities = new ProfileUtilities(worker, issues, null);
		profileUtilities.generateSnapshot(base, derived, "", "");

		return derived;
	}

	private List<SingleValidationMessage> logResultsAndReturnAll(ValidationResult theOutput) {
		List<SingleValidationMessage> retVal = new ArrayList<SingleValidationMessage>();

		int index = 0;
		for (SingleValidationMessage next : theOutput.getMessages()) {
			ourLog.info("Result {}: {} - {}:{} {} - {}",
				new Object[] {index, next.getSeverity(), defaultString(next.getLocationLine()), defaultString(next.getLocationCol()), next.getLocationString(), next.getMessage()});
			index++;

			retVal.add(next);
		}
		return retVal;
	}

	/**
	 * See #938
	 */
	@Test
	public void testValidateEmptyElement() {
		String input = "<Patient xmlns=\"http://hl7.org/fhir\">" +
			"<active value=\"\"/>" +
			"</Patient>";

		FhirValidator val = ourCtx.newValidator();
		val.registerValidatorModule(new FhirInstanceValidator(myDefaultValidationSupport));

		ValidationResult result = val.validateWithResult(input);
		List<SingleValidationMessage> all = logResultsAndReturnAll(result);
		assertFalse(result.isSuccessful());
		assertEquals("primitive types must have a value or must have child extensions", all.get(0).getMessage());
	}

	/**
	 * See #942
	 */
	@Test
	public void testValidateDoesntEnforceBestPracticesByDefault() {
		Observation input = new Observation();
		input.setStatus(ObservationStatus.AMENDED);
		input.getCode().addCoding().setSystem("http://loinc.org").setCode("1234").setDisplay("FOO");

		FhirInstanceValidator instanceModule;
		FhirValidator val;
		ValidationResult result;
		List<SingleValidationMessage> all;

		// With BPs disabled
		val = ourCtx.newValidator();
		instanceModule = new FhirInstanceValidator(myDefaultValidationSupport);
		val.registerValidatorModule(instanceModule);
		result = val.validateWithResult(input);
		all = logResultsAndReturnAll(result);
		assertTrue(result.isSuccessful());
		assertThat(all, empty());

		// With BPs enabled
		val = ourCtx.newValidator();
		instanceModule = new FhirInstanceValidator(myDefaultValidationSupport);
		instanceModule.setBestPracticeWarningLevel(IResourceValidator.BestPracticeWarningLevel.Error);
		val.registerValidatorModule(instanceModule);
		result = val.validateWithResult(input);
		all = logResultsAndReturnAll(result);
		assertFalse(result.isSuccessful());
		assertEquals("All observations should have a subject", all.get(0).getMessage());
	}


	private List<SingleValidationMessage> logResultsAndReturnNonInformationalOnes(ValidationResult theOutput) {
		List<SingleValidationMessage> retVal = new ArrayList<SingleValidationMessage>();

		int index = 0;
		for (SingleValidationMessage next : theOutput.getMessages()) {
			ourLog.info("Result {}: {} - {} - {}", new Object[] {index, next.getSeverity(), next.getLocationString(), next.getMessage()});
			index++;

			if (next.getSeverity() != ResultSeverityEnum.INFORMATION) {
				retVal.add(next);
			}
		}

		return retVal;
	}

	private List<SingleValidationMessage> logResultsAndReturnErrorOnes(ValidationResult theOutput) {
		List<SingleValidationMessage> retVal = new ArrayList<SingleValidationMessage>();

		int index = 0;
		for (SingleValidationMessage next : theOutput.getMessages()) {
			ourLog.info("Result {}: {} - {} - {}", new Object[] {index, next.getSeverity(), next.getLocationString(), next.getMessage()});
			index++;

			if (next.getSeverity().ordinal() > ResultSeverityEnum.WARNING.ordinal()) {
				retVal.add(next);
			}
		}

		return retVal;
	}

	@Test
	public void testBase64Invalid() {
		Base64BinaryType value = new Base64BinaryType(new byte[] {2, 3, 4, 5, 6, 7, 8, 9, 8, 7, 6, 5, 4, 3, 2, 1});
		Media med = new Media();
		med.getContent().setContentType("LCws");
		med.getContent().setDataElement(value);
		med.getContent().setTitle("bbbb syst");
		med.setStatus(Media.MediaStatus.ABORTED);
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
		String encoded = IOUtils.toString(FhirInstanceValidatorR4Test.class.getResourceAsStream("/r4/r4-caredove-bundle.json"));

		ValidationResult output = myVal.validateWithResult(encoded);
		List<SingleValidationMessage> errors = logResultsAndReturnNonInformationalOnes(output);
		assertEquals(44, errors.size());
	}

	@Test
	public void testBase64Valid() {
		Base64BinaryType value = new Base64BinaryType(new byte[] {2, 3, 4, 5, 6, 7, 8, 9, 8, 7, 6, 5, 4, 3, 2, 1});
		Media med = new Media();
		med.getContent().setContentType("LCws");
		med.getContent().setDataElement(value);
		med.getContent().setTitle("bbbb syst");
		med.setStatus(Media.MediaStatus.ABORTED);
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
		val.registerValidatorModule(new FhirInstanceValidator(myDefaultValidationSupport));

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
		List<SingleValidationMessage> errors = logResultsAndReturnNonInformationalOnes(output);
		assertEquals(0, errors.size());
	}

	@Test
	@Ignore
	public void testValidateBigRawJsonResource() throws Exception {
		InputStream stream = FhirInstanceValidatorR4Test.class.getResourceAsStream("/conformance.json.gz");
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
	@Ignore
	public void testValidateBuiltInProfiles() throws Exception {
		org.hl7.fhir.r4.model.Bundle bundle;
		String name = "profiles-resources";
		ourLog.info("Uploading " + name);
		String vsContents;
		vsContents = IOUtils.toString(FhirInstanceValidatorR4Test.class.getResourceAsStream("/org/hl7/fhir/r4/model/profile/" + name + ".xml"), "UTF-8");

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
		String vsContents = IOUtils.toString(FhirInstanceValidatorR4Test.class.getResourceAsStream("/r4/bundle-with-no-type.json"), "UTF-8");

		ValidationResult output = myVal.validateWithResult(vsContents);
		logResultsAndReturnNonInformationalOnes(output);
		assertThat(output.getMessages().toString(), containsString("Element 'Bundle.type': minimum required = 1"));
	}

	@Test
	@Ignore
	public void testValidateBundleWithObservations() throws Exception {
		String name = "profiles-resources";
		ourLog.info("Uploading " + name);
		String inputString;
		inputString = IOUtils.toString(FhirInstanceValidatorR4Test.class.getResourceAsStream("/brian_reinhold_bundle.json"), "UTF-8");
		Bundle bundle = ourCtx.newJsonParser().parseResource(Bundle.class, inputString);

		FHIRPathEngine fp = new FHIRPathEngine(new HapiWorkerContext(ourCtx, myDefaultValidationSupport));
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
		String vsContents = IOUtils.toString(FhirInstanceValidatorR4Test.class.getResourceAsStream("/sample-document.xml"), "UTF-8");

		ValidationResult output = myVal.validateWithResult(vsContents);
		logResultsAndReturnNonInformationalOnes(output);
		assertTrue(output.isSuccessful());
	}

	@Test
	public void testValidateProfileWithExtension() throws IOException, FHIRException {
		PrePopulatedValidationSupport valSupport = new PrePopulatedValidationSupport();
		DefaultProfileValidationSupport defaultSupport = new DefaultProfileValidationSupport();
		CachingValidationSupport support = new CachingValidationSupport(new ValidationSupportChain(valSupport, defaultSupport));

		// Prepopulate SDs
		valSupport.addStructureDefinition(loadStructureDefinition(defaultSupport, "/dstu3/myconsent-profile.xml"));
		valSupport.addStructureDefinition(loadStructureDefinition(defaultSupport, "/dstu3/myconsent-ext.xml"));

		FhirValidator val = ourCtx.newValidator();
		val.registerValidatorModule(new FhirInstanceValidator(support));

		Consent input = ourCtx.newJsonParser().parseResource(Consent.class, IOUtils.toString(ResourceValidatorDstu3Test.class.getResourceAsStream("/r4/myconsent-resource.json")));

		input.getPolicyFirstRep().setAuthority("http://foo");
		//input.setScope(Consent.ConsentScope.ADR);
		input.getScope()
			.getCodingFirstRep()
			.setSystem("http://hl7.org/fhir/consentscope")
			.setCode("adr");
		input.addCategory()
			.getCodingFirstRep()
			.setSystem("http://hl7.org/fhir/consentcategorycodes")
			.setCode("acd");

		// Should pass
		ValidationResult output = val.validateWithResult(input);
		List<SingleValidationMessage> all = logResultsAndReturnErrorOnes(output);
		assertEquals(0, all.size());

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
		String input = IOUtils.toString(FhirInstanceValidatorR4Test.class.getResourceAsStream("/qr_jon.xml"));

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
	@Ignore
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
	public void testValidateRawJsonResourceWithUnknownExtension() {

		Patient patient = new Patient();
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

		ValidationResult output = myVal.validateWithResult(encoded);
		assertEquals(output.toString(), 1, output.getMessages().size());

		assertEquals("Unknown extension http://hl7.org/fhir/v3/ethnicity", output.getMessages().get(0).getMessage());
		assertEquals(ResultSeverityEnum.INFORMATION, output.getMessages().get(0).getSeverity());
	}

	@Test
	public void testValidateRawJsonResourceWithUnknownExtensionNotAllowed() {

		Patient patient = new Patient();
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
		// @formatter:off
		String input = "<Patient xmlns=\"http://hl7.org/fhir\">" + "<id value=\"123\"/>" + "</Patient>";
		// @formatter:on

		ValidationResult output = myVal.validateWithResult(input);
		assertEquals(output.toString(), 0, output.getMessages().size());
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
		assertEquals("Undefined element 'foo'", output.getMessages().get(0).getMessage());
	}

	@Test
	public void testValidateRawXmlResourceWithEmptyPrimitive() {
		// @formatter:off
		String input = "<Patient xmlns=\"http://hl7.org/fhir\"><name><given/></name></Patient>";
		// @formatter:on

		ValidationResult output = myVal.validateWithResult(input);
		List<SingleValidationMessage> messages = logResultsAndReturnNonInformationalOnes(output);
		assertEquals(output.toString(), 3, messages.size());
		assertThat(messages.get(0).getMessage(), containsString("Element must have some content"));
		assertThat(messages.get(1).getMessage(), containsString("primitive types must have a value or must have child extensions"));
		assertThat(messages.get(2).getMessage(), containsString("All FHIR elements must have a @value or children [hasValue() | (children().count() > id.count())]"));
	}

	@Test
	public void testValidateRawXmlResourceWithPrimitiveContainingOnlyAnExtension() {
		// @formatter:off
		String input = "<ActivityDefinition xmlns=\"http://hl7.org/fhir\">\n" +
			"                        <id value=\"referralToMentalHealthCare\"/>\n" +
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

		ValidationResult output = myVal.validateWithResult(input);
		assertEquals(output.toString(), 1, output.getMessages().size());
		assertEquals("This cannot be parsed as a FHIR object (no namespace)", output.getMessages().get(0).getMessage());
		ourLog.info(output.getMessages().get(0).getLocationString());
	}

	/**
	 * A reference with only an identifier should be valid
	 */
	@Test
	public void testValidateReferenceWithDisplayValid() throws Exception {
		Patient p = new Patient();
		p.getManagingOrganization().setDisplay("HELLO");

		ValidationResult output = myVal.validateWithResult(p);
		List<SingleValidationMessage> nonInfo = logResultsAndReturnNonInformationalOnes(output);
		assertThat(nonInfo, empty());
	}

	/**
	 * A reference with only an identifier should be valid
	 */
	@Test
	public void testValidateReferenceWithIdentifierValid() throws Exception {
		Patient p = new Patient();
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
		rp.getPatient().setReference("Patient/1");
		rp.addRelationship().addCoding().setSystem("http://hl7.org/fhir/v2/0131").setCode("c");

		ValidationResult results = myVal.validateWithResult(rp);
		List<SingleValidationMessage> outcome = logResultsAndReturnNonInformationalOnes(results);
		assertThat(outcome, empty());

		/*
		 * Code system is case insensitive, so try with capital C
		 */
		rp = new RelatedPerson();
		rp.getPatient().setReference("Patient/1");
		rp.addRelationship().addCoding().setSystem("http://hl7.org/fhir/v2/0131").setCode("C");

		results = myVal.validateWithResult(rp);
		outcome = logResultsAndReturnNonInformationalOnes(results);
		assertThat(outcome, empty());

		/*
		 * Now a bad code
		 */
		rp = new RelatedPerson();
		rp.getPatient().setReference("Patient/1");
		rp.addRelationship().addCoding().setSystem("http://hl7.org/fhir/v2/0131").setCode("GAGAGAGA");

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
		input.getContext().setReference("http://foo.com/Encounter/9");
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
		input.getContext().setReference("http://foo.com/Encounter/9");
		input.setStatus(ObservationStatus.FINAL);
		input.getCode().addCoding().setSystem("http://loinc.org").setCode("12345");

		myInstanceVal.setValidationSupport(myMockSupport);
		ValidationResult output = myVal.validateWithResult(input);
		List<SingleValidationMessage> errors = logResultsAndReturnNonInformationalOnes(output);

		assertThat(errors.toString(), containsString("Element 'Observation.subject': minimum required = 1, but only found 0"));
		assertThat(errors.toString(), containsString("Element 'Observation.context': max allowed = 0, but found 1"));
		assertThat(errors.toString(), containsString("Element 'Observation.device': minimum required = 1, but only found 0"));
		assertThat(errors.toString(), containsString(""));
	}

	@Test
	public void testValidateResourceContainingProfileDeclarationDoesntResolve() {
		addValidConcept("http://loinc.org", "12345");

		Observation input = new Observation();
		input.getMeta().addProfile("http://foo/structuredefinition/myprofile");

		input.getCode().addCoding().setSystem("http://loinc.org").setCode("12345");
		input.setStatus(ObservationStatus.FINAL);

		myInstanceVal.setValidationSupport(myMockSupport);
		ValidationResult output = myVal.validateWithResult(input);
		List<SingleValidationMessage> errors = logResultsAndReturnNonInformationalOnes(output);
		assertEquals(errors.toString(), 1, errors.size());
		assertEquals("StructureDefinition reference \"http://foo/structuredefinition/myprofile\" could not be resolved", errors.get(0).getMessage());
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
		assertEquals(
			"The value provided ('notvalidcode') is not in the value set http://hl7.org/fhir/ValueSet/observation-status (http://hl7.org/fhir/ValueSet/observation-status, and a code is required from this value set) (error message = Unknown code[notvalidcode] in system[null])",
			output.getMessages().get(0).getMessage());
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
	public void testValidateResourceWithValuesetExpansionBad() {

		Patient patient = new Patient();
		patient.addIdentifier().setSystem("http://example.com/").setValue("12345").getType().addCoding().setSystem("http://example.com/foo/bar").setCode("bar");

		ValidationResult output = myVal.validateWithResult(patient);
		List<SingleValidationMessage> all = logResultsAndReturnAll(output);
		assertEquals(1, all.size());
		assertEquals("Patient.identifier.type", all.get(0).getLocationString());
		assertEquals(
			"None of the codes provided are in the value set http://hl7.org/fhir/ValueSet/identifier-type (http://hl7.org/fhir/ValueSet/identifier-type, and a code should come from this value set unless it has no suitable code) (codes = http://example.com/foo/bar#bar)",
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
		patient.addIdentifier().setSystem("http://system").setValue("12345").getType().addCoding().setSystem("http://hl7.org/fhir/v2/0203").setCode("MR");

		ValidationResult output = myVal.validateWithResult(patient);
		List<SingleValidationMessage> all = logResultsAndReturnAll(output);
		assertEquals(0, all.size());
	}

	@Test
	@Ignore
	public void testValidateStructureDefinition() throws IOException {
		String input = IOUtils.toString(FhirInstanceValidatorR4Test.class.getResourceAsStream("/sdc-questionnaire.profile.xml"));

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
