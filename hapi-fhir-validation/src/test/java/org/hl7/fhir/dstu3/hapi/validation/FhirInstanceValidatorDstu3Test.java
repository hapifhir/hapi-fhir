package org.hl7.fhir.dstu3.hapi.validation;

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
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.util.TestUtil;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ResultSeverityEnum;
import ca.uhn.fhir.validation.SingleValidationMessage;
import ca.uhn.fhir.validation.ValidationResult;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.hl7.fhir.dstu3.hapi.ctx.DefaultProfileValidationSupport;
import org.hl7.fhir.dstu3.hapi.ctx.HapiWorkerContext;
import org.hl7.fhir.dstu3.hapi.ctx.IValidationSupport;
import org.hl7.fhir.dstu3.hapi.ctx.IValidationSupport.CodeValidationResult;
import org.hl7.fhir.dstu3.model.*;
import org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.dstu3.model.CodeSystem.ConceptDefinitionComponent;
import org.hl7.fhir.dstu3.model.Enumerations.PublicationStatus;
import org.hl7.fhir.dstu3.model.Observation.ObservationStatus;
import org.hl7.fhir.dstu3.model.Questionnaire.QuestionnaireItemComponent;
import org.hl7.fhir.dstu3.model.Questionnaire.QuestionnaireItemType;
import org.hl7.fhir.dstu3.model.StructureDefinition.StructureDefinitionKind;
import org.hl7.fhir.dstu3.model.ValueSet.ConceptSetComponent;
import org.hl7.fhir.dstu3.model.ValueSet.ValueSetExpansionComponent;
import org.hl7.fhir.dstu3.utils.FHIRPathEngine;
import org.hl7.fhir.dstu3.utils.StructureMapUtilities;
import org.hl7.fhir.instance.model.api.IBaseResource;
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
import java.util.*;
import java.util.zip.GZIPInputStream;

public class FhirInstanceValidatorDstu3Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirInstanceValidatorDstu3Test.class);
	private static DefaultProfileValidationSupport myDefaultValidationSupport = new DefaultProfileValidationSupport();
	private static FhirContext ourCtx = FhirContext.forDstu3();
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
	private Set<String> myValidSystems = new HashSet<>();
	private HashMap<String, StructureDefinition> myStructureDefinitions;
	private HashMap<String, CodeSystem> myCodeSystems;
	private HashMap<String, ValueSet> myValueSets;
	private HashMap<String, Questionnaire> myQuestionnaires;

	private void addValidConcept(String theSystem, String theCode) {
		myValidSystems.add(theSystem);
		myValidConcepts.add(theSystem + "___" + theCode);
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

		when(myMockSupport.expandValueSet(any(FhirContext.class), nullable(ConceptSetComponent.class))).thenAnswer(new Answer<ValueSetExpansionComponent>() {
			@Override
			public ValueSetExpansionComponent answer(InvocationOnMock theInvocation) {
				ConceptSetComponent arg = (ConceptSetComponent) theInvocation.getArguments()[0];
				ValueSetExpansionComponent retVal = mySupportedCodeSystemsForExpansion.get(arg.getSystem());
				if (retVal == null) {
					retVal = myDefaultValidationSupport.expandValueSet(nullable(FhirContext.class), arg);
				}
				ourLog.debug("expandValueSet({}) : {}", new Object[] {theInvocation.getArguments()[0], retVal});
				return retVal;
			}
		});
		when(myMockSupport.isCodeSystemSupported(nullable(FhirContext.class), nullable(String.class))).thenAnswer(new Answer<Boolean>() {
			@Override
			public Boolean answer(InvocationOnMock theInvocation) {
				String url = (String) theInvocation.getArguments()[1];
				boolean retVal = myValidSystems.contains(url);
				ourLog.debug("isCodeSystemSupported({}) : {}", new Object[] {url, retVal});
				if (retVal == false) {
					retVal = myCodeSystems.containsKey(url);
				}
				return retVal;
			}
		});
		when(myMockSupport.fetchResource(nullable(FhirContext.class), nullable(Class.class), nullable(String.class))).thenAnswer(new Answer<IBaseResource>() {
			@Override
			public IBaseResource answer(InvocationOnMock theInvocation) throws Throwable {
				IBaseResource retVal = null;
				Class<?> type = (Class<?>) theInvocation.getArguments()[1];
				String id = (String) theInvocation.getArguments()[2];
				if ("Questionnaire/q_jon".equals(id)) {
					retVal = ourCtx.newJsonParser().parseResource(IOUtils.toString(FhirInstanceValidatorDstu3Test.class.getResourceAsStream("/q_jon.json"), Charsets.UTF_8));
				} else {

					if (StructureDefinition.class.equals(type)) {
						retVal = myStructureDefinitions.get(id);
					}
					if (ValueSet.class.equals(type)) {
						retVal = myValueSets.get(id);
					}
					if (CodeSystem.class.equals(type)) {
						retVal = myCodeSystems.get(id);
					}
					if (Questionnaire.class.equals(type)) {
						retVal = myQuestionnaires.get(id);
					}

					if (retVal == null) {
						retVal = myDefaultValidationSupport.fetchResource((FhirContext) theInvocation.getArguments()[0], (Class<IBaseResource>) theInvocation.getArguments()[1], id);
					}
				}
				if (retVal == null) {
					ourLog.info("fetchResource({}, {}) : {}", type, id, retVal);
				}
				return retVal;
			}
		});
		when(myMockSupport.validateCode(nullable(FhirContext.class), nullable(String.class), nullable(String.class), nullable(String.class))).thenAnswer(new Answer<CodeValidationResult>() {
			@Override
			public CodeValidationResult answer(InvocationOnMock theInvocation) {
				FhirContext ctx = (FhirContext) theInvocation.getArguments()[0];
				String system = (String) theInvocation.getArguments()[1];
				String code = (String) theInvocation.getArguments()[2];
				CodeValidationResult retVal;
				if (myValidConcepts.contains(system + "___" + code)) {
					retVal = new CodeValidationResult(new ConceptDefinitionComponent(new CodeType(code)));
				} else if (myCodeSystems.containsKey(system)) {
					CodeSystem cs = myCodeSystems.get(system);
					Optional<ConceptDefinitionComponent> found = cs.getConcept().stream().filter(t -> t.getCode().equals(code)).findFirst();
					retVal = found.map(t->new CodeValidationResult(t)).orElse(null);
				} else {
					retVal = myDefaultValidationSupport.validateCode(ctx, system, code, (String) theInvocation.getArguments()[2]);
				}
				ourLog.debug("validateCode({}, {}, {}) : {}", new Object[] {system, code, theInvocation.getArguments()[2], retVal});
				return retVal;
			}
		});
		when(myMockSupport.fetchCodeSystem(nullable(FhirContext.class), nullable(String.class))).thenAnswer(new Answer<CodeSystem>() {
			@Override
			public CodeSystem answer(InvocationOnMock theInvocation) {
				CodeSystem retVal;

				String id = (String) theInvocation.getArguments()[1];
				retVal = myCodeSystems.get(id);

				if (retVal == null) {
					retVal = myDefaultValidationSupport.fetchCodeSystem((FhirContext) theInvocation.getArguments()[0], id);
				}

				if (retVal == null) {
					ourLog.info("fetchCodeSystem({}) : {}", new Object[]{id, retVal});
				}
				return retVal;
			}
		});
		myStructureDefinitions = new HashMap<>();
		myValueSets = new HashMap<>();
		myCodeSystems = new HashMap<>();
		myQuestionnaires = new HashMap<>();
		when(myMockSupport.fetchStructureDefinition(nullable(FhirContext.class), nullable(String.class))).thenAnswer(new Answer<StructureDefinition>() {
			@Override
			public StructureDefinition answer(InvocationOnMock theInvocation) {
				String url = (String) theInvocation.getArguments()[1];
				StructureDefinition retVal = myStructureDefinitions.get(url);
				if (retVal == null) {
					retVal = myDefaultValidationSupport.fetchStructureDefinition((FhirContext) theInvocation.getArguments()[0], url);
				}
				ourLog.info("fetchStructureDefinition({}) : {}", new Object[] {url, retVal});
				return retVal;
			}
		});
		when(myMockSupport.fetchAllStructureDefinitions(nullable(FhirContext.class))).thenAnswer(new Answer<List<StructureDefinition>>() {
			@Override
			public List<StructureDefinition> answer(InvocationOnMock theInvocation) {
				List<StructureDefinition> retVal = myDefaultValidationSupport.fetchAllStructureDefinitions((FhirContext) theInvocation.getArguments()[0]);
				ourLog.debug("fetchAllStructureDefinitions()", new Object[] {});
				return retVal;
			}
		});

	}

	private Object defaultString(Integer theLocationLine) {
		return theLocationLine != null ? theLocationLine.toString() : "";
	}

	private String loadResource(String theFileName) throws IOException {
		return IOUtils.toString(FhirInstanceValidatorDstu3Test.class.getResourceAsStream(theFileName), Charsets.UTF_8);
	}

	private <T extends IBaseResource> T loadResource(String theFilename, Class<T> theType) throws IOException {
		return ourCtx.newJsonParser().parseResource(theType, loadResource(theFilename));
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
		t.setSystem(org.hl7.fhir.dstu3.model.ContactPoint.ContactPointSystem.URL);
		t.setValue("http://infoway-inforoute.ca");

		ValidationResult results = myVal.validateWithResult(p);
		List<SingleValidationMessage> outcome = logResultsAndReturnNonInformationalOnes(results);
		assertThat(outcome, empty());

	}

	/**
	 * See #703
	 */
	@Test
	public void testDstu3UsesLatestDefinitions() throws IOException {
		String input = IOUtils.toString(FhirInstanceValidatorDstu3Test.class.getResourceAsStream("/bug703.json"), Charsets.UTF_8);

		ValidationResult results = myVal.validateWithResult(input);
		List<SingleValidationMessage> outcome = logResultsAndReturnNonInformationalOnes(results);
		assertThat(outcome, empty());

	}

	@Test
	public void testValidateQuestionnaire() throws IOException {
		CodeSystem csYesNo = loadResource("/dstu3/fmc01-cs-yesnounk.json", CodeSystem.class);
		myCodeSystems.put(csYesNo.getUrl(), csYesNo);
		CodeSystem csBinderRecommended = loadResource("/dstu3/fmc01-cs-binderrecommended.json", CodeSystem.class);
		myCodeSystems.put(csBinderRecommended.getUrl(), csBinderRecommended);
		ValueSet vsBinderRequired = loadResource("/dstu3/fmc01-vs-binderrecommended.json", ValueSet.class);
		myValueSets.put(vsBinderRequired.getUrl(), vsBinderRequired);
		myValueSets.put("ValueSet/" +vsBinderRequired.getIdElement().getIdPart(), vsBinderRequired);
		ValueSet vsYesNo = loadResource("/dstu3/fmc01-vs-yesnounk.json", ValueSet.class);
		myValueSets.put(vsYesNo.getUrl(), vsYesNo);
		myValueSets.put("ValueSet/" + vsYesNo.getIdElement().getIdPart(), vsYesNo);
		Questionnaire q = loadResource("/dstu3/fmc01-questionnaire.json", Questionnaire.class);
		myQuestionnaires.put("Questionnaire/" + q.getIdElement().getIdPart(), q);

		QuestionnaireResponse qr = loadResource("/dstu3/fmc01-questionnaireresponse.json", QuestionnaireResponse.class);
		ValidationResult result = myVal.validateWithResult(qr);
		List<SingleValidationMessage> errors = logResultsAndReturnNonInformationalOnes(result);
		assertThat(errors, empty());

	}

	@Test
	public void testValidateQuestionnaire03() throws IOException {
		CodeSystem csYesNo = loadResource("/dstu3/fmc01-cs-yesnounk.json", CodeSystem.class);
		myCodeSystems.put(csYesNo.getUrl(), csYesNo);
		CodeSystem csBinderRecommended = loadResource("/dstu3/fmc03-cs-binderrecommend.json", CodeSystem.class);
		myCodeSystems.put(csBinderRecommended.getUrl(), csBinderRecommended);

		ValueSet vsBinderRequired = loadResource("/dstu3/fmc03-vs-binderrecommend.json", ValueSet.class);
		myValueSets.put(vsBinderRequired.getUrl(), vsBinderRequired);
		myValueSets.put("ValueSet/" +vsBinderRequired.getIdElement().getIdPart(), vsBinderRequired);
		ValueSet vsYesNo = loadResource("/dstu3/fmc03-vs-fmcyesno.json", ValueSet.class);
		myValueSets.put(vsYesNo.getUrl(), vsYesNo);
		myValueSets.put("ValueSet/" + vsYesNo.getIdElement().getIdPart(), vsYesNo);
		Questionnaire q = loadResource("/dstu3/fmc03-questionnaire.json", Questionnaire.class);
		myQuestionnaires.put("Questionnaire/" + q.getIdElement().getIdPart(), q);

		QuestionnaireResponse qr = loadResource("/dstu3/fmc03-questionnaireresponse.json", QuestionnaireResponse.class);
		ValidationResult result = myVal.validateWithResult(qr);
		List<SingleValidationMessage> errors = logResultsAndReturnAll(result);
		assertThat(errors, empty());

	}

	@Test
	public void testValidateQuestionnaireWithEnableWhenAndSubItems_ShouldNotBeEnabled() throws IOException {
		CodeSystem csYesNo = loadResource("/dstu3/fmc01-cs-yesnounk.json", CodeSystem.class);
		myCodeSystems.put(csYesNo.getUrl(), csYesNo);
		CodeSystem csBinderRecommended = loadResource("/dstu3/fmc02-cs-binderrecomm.json", CodeSystem.class);
		myCodeSystems.put(csBinderRecommended.getUrl(), csBinderRecommended);
		ValueSet vsBinderRequired = loadResource("/dstu3/fmc02-vs-binderrecomm.json", ValueSet.class);
		myValueSets.put(vsBinderRequired.getUrl(), vsBinderRequired);
		myValueSets.put("ValueSet/" +vsBinderRequired.getIdElement().getIdPart(), vsBinderRequired);
		ValueSet vsYesNo = loadResource("/dstu3/fmc01-vs-yesnounk.json", ValueSet.class);
		myValueSets.put(vsYesNo.getUrl(), vsYesNo);
		myValueSets.put("ValueSet/" + vsYesNo.getIdElement().getIdPart(), vsYesNo);
		Questionnaire q = loadResource("/dstu3/fmc02-questionnaire.json", Questionnaire.class);
		myQuestionnaires.put("Questionnaire/" + q.getIdElement().getIdPart(), q);

		QuestionnaireResponse qr = loadResource("/dstu3/fmc02-questionnaireresponse-01.json", QuestionnaireResponse.class);
		ValidationResult result = myVal.validateWithResult(qr);
		List<SingleValidationMessage> errors = logResultsAndReturnNonInformationalOnes(result);
		assertThat(errors.get(0).getMessage(), containsString("Item has answer, even though it is not enabled (item id = 'BO_ConsDrop')"));
		assertEquals(1, errors.size());
	}

	@Test
	public void testValidateQuestionnaireWithEnableWhenAndSubItems_ShouldBeEnabled() throws IOException {
		CodeSystem csYesNo = loadResource("/dstu3/fmc01-cs-yesnounk.json", CodeSystem.class);
		myCodeSystems.put(csYesNo.getUrl(), csYesNo);
		CodeSystem csBinderRecommended = loadResource("/dstu3/fmc02-cs-binderrecomm.json", CodeSystem.class);
		myCodeSystems.put(csBinderRecommended.getUrl(), csBinderRecommended);
		ValueSet vsBinderRequired = loadResource("/dstu3/fmc02-vs-binderrecomm.json", ValueSet.class);
		myValueSets.put(vsBinderRequired.getUrl(), vsBinderRequired);
		myValueSets.put("ValueSet/" +vsBinderRequired.getIdElement().getIdPart(), vsBinderRequired);
		ValueSet vsYesNo = loadResource("/dstu3/fmc01-vs-yesnounk.json", ValueSet.class);
		myValueSets.put(vsYesNo.getUrl(), vsYesNo);
		myValueSets.put("ValueSet/" + vsYesNo.getIdElement().getIdPart(), vsYesNo);
		Questionnaire q = loadResource("/dstu3/fmc02-questionnaire.json", Questionnaire.class);
		myQuestionnaires.put("Questionnaire/" + q.getIdElement().getIdPart(), q);

		QuestionnaireResponse qr = loadResource("/dstu3/fmc02-questionnaireresponse-02.json", QuestionnaireResponse.class);
		ValidationResult result = myVal.validateWithResult(qr);
		List<SingleValidationMessage> errors = logResultsAndReturnNonInformationalOnes(result);
		assertThat(errors, empty());
	}

	/**
	 * See #872
	 */
	@Test
	public void testExtensionUrlWithHl7Url() throws IOException {
		String input = IOUtils.toString(FhirInstanceValidatorDstu3Test.class.getResourceAsStream("/bug872-ext-with-hl7-url.json"), Charsets.UTF_8);
		ValidationResult output = myVal.validateWithResult(input);
		List<SingleValidationMessage> nonInfo = logResultsAndReturnNonInformationalOnes(output);
		assertThat(nonInfo, empty());
	}

	@Test
	public void testGoal() {
		Goal goal = new Goal();
		goal.setSubject(new Reference("Patient/123"));
		goal.setDescription(new CodeableConcept().addCoding(new Coding("http://foo", "some other goal", "")));
		goal.setStatus(Goal.GoalStatus.INPROGRESS);

		ValidationResult results = myVal.validateWithResult(goal);
		List<SingleValidationMessage> outcome = logResultsAndReturnNonInformationalOnes(results);
		assertEquals(0, outcome.size());
	}

	/**
	 * An invalid local reference should not cause a ServiceException.
	 */
	@Test
	public void testInvalidLocalReference() {
		Questionnaire resource = new Questionnaire();
		resource.setStatus(PublicationStatus.ACTIVE);

		QuestionnaireItemComponent item = new QuestionnaireItemComponent();
		item.setLinkId("linkId-1");
		item.setType(QuestionnaireItemType.CHOICE);
		item.setOptions(new Reference("#invalid-ref"));
		resource.addItem(item);

		ValidationResult output = myVal.validateWithResult(resource);
		List<SingleValidationMessage> nonInfo = logResultsAndReturnNonInformationalOnes(output);
		assertThat(nonInfo, hasSize(2));
	}

	@Test
	public void testIsNoTerminologyChecks() {
		assertFalse(myInstanceVal.isNoTerminologyChecks());
		myInstanceVal.setNoTerminologyChecks(true);
		assertTrue(myInstanceVal.isNoTerminologyChecks());
	}

	/**
	 * See #824
	 */
	@Test
	public void testValidateBadCodeForRequiredBinding() throws IOException {
		StructureDefinition fiphrPefStu3 = ourCtx.newJsonParser().parseResource(StructureDefinition.class, loadResource("/dstu3/bug824-profile-fiphr-pef-stu3.json"));
		myStructureDefinitions.put("http://phr.kanta.fi/StructureDefinition/fiphr-pef-stu3", fiphrPefStu3);

		StructureDefinition fiphrDevice = ourCtx.newJsonParser().parseResource(StructureDefinition.class, loadResource("/dstu3/bug824-fiphr-device.json"));
		myStructureDefinitions.put("http://phr.kanta.fi/StructureDefinition/fiphr-device", fiphrDevice);

		StructureDefinition fiphrCreatingApplication = ourCtx.newJsonParser().parseResource(StructureDefinition.class, loadResource("/dstu3/bug824-creatingapplication.json"));
		myStructureDefinitions.put("http://phr.kanta.fi/StructureDefinition/fiphr-ext-creatingapplication", fiphrCreatingApplication);

		StructureDefinition fiphrBoolean = ourCtx.newJsonParser().parseResource(StructureDefinition.class, loadResource("/dstu3/bug824-fiphr-boolean.json"));
		myStructureDefinitions.put("http://phr.kanta.fi/StructureDefinition/fiphr-boolean", fiphrBoolean);

		StructureDefinition medContext = ourCtx.newJsonParser().parseResource(StructureDefinition.class, loadResource("/dstu3/bug824-fiphr-medicationcontext.json"));
		myStructureDefinitions.put("http://phr.kanta.fi/StructureDefinition/fiphr-medicationcontext", medContext);

		StructureDefinition fiphrVitalSigns = ourCtx.newJsonParser().parseResource(StructureDefinition.class, loadResource("/dstu3/bug824-fiphr-vitalsigns-stu3.json"));
		myStructureDefinitions.put("http://phr.kanta.fi/StructureDefinition/fiphr-vitalsigns-stu3", fiphrVitalSigns);

		CodeSystem csObservationMethod = ourCtx.newJsonParser().parseResource(CodeSystem.class, loadResource("/dstu3/bug824-fhirphr-cs-observationmethod.json"));
		myCodeSystems.put("http://phr.kanta.fi/fiphr-cs-observationmethod", csObservationMethod);

		ValueSet vsObservationMethod = ourCtx.newJsonParser().parseResource(ValueSet.class, loadResource("/dstu3/bug824-vs-observaionmethod.json"));
		myValueSets.put("http://phr.kanta.fi/ValueSet/fiphr-vs-observationmethod", vsObservationMethod);

		ValueSet vsVitalSigns = ourCtx.newJsonParser().parseResource(ValueSet.class, loadResource("/dstu3/bug824-vs-vitalsigns.json"));
		myValueSets.put("http://phr.kanta.fi/ValueSet/fiphr-vs-vitalsigns", vsVitalSigns);

		ValueSet vsMedicationContext = ourCtx.newJsonParser().parseResource(ValueSet.class, loadResource("/dstu3/bug824-vs-medicationcontext.json"));
		myValueSets.put("http://phr.kanta.fi/ValueSet/fiphr-vs-medicationcontext", vsMedicationContext);

		ValueSet vsConfidentiality = ourCtx.newJsonParser().parseResource(ValueSet.class, loadResource("/dstu3/bug824-vs-confidentiality.json"));
		myValueSets.put("http://phr.kanta.fi/ValueSet/fiphr-vs-confidentiality", vsConfidentiality);

		CodeSystem csMedicationContext = ourCtx.newJsonParser().parseResource(CodeSystem.class, loadResource("/dstu3/bug824-fhirphr-cs-medicationcontext.json"));
		myCodeSystems.put("http://phr.kanta.fi/fiphr-cs-medicationcontext", csMedicationContext);

		String input = loadResource("/dstu3/bug824-resource.json");
		ValidationResult output = myVal.validateWithResult(input);
		List<SingleValidationMessage> issues = logResultsAndReturnNonInformationalOnes(output);

		assertThat(issues.toString(), containsString("None of the codes provided are in the value set http://phr.kanta.fi/ValueSet/fiphr-vs-medicationcontext"));
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
	// @Ignore
	public void testValidateBuiltInProfiles() throws Exception {
		org.hl7.fhir.dstu3.model.Bundle bundle;
		String name = "profiles-resources";
		ourLog.info("Uploading " + name);
		String vsContents;
		vsContents = IOUtils.toString(FhirInstanceValidatorDstu3Test.class.getResourceAsStream("/org/hl7/fhir/dstu3/model/profile/" + name + ".xml"), "UTF-8");

		TreeSet<String> ids = new TreeSet<String>();

		bundle = ourCtx.newXmlParser().parseResource(org.hl7.fhir.dstu3.model.Bundle.class, vsContents);
		for (BundleEntryComponent i : bundle.getEntry()) {
			org.hl7.fhir.dstu3.model.Resource next = i.getResource();
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
			List<SingleValidationMessage> errors = logResultsAndReturnNonInformationalOnes(output);
			assertThat("Failed to validate " + i.getFullUrl() + " - " + errors, errors, empty());
		}

		ourLog.info("Validated the following:\n{}", ids);
	}

	@Test
	public void testValidateBundleWithNoType() throws Exception {
		String vsContents = IOUtils.toString(FhirInstanceValidatorDstu3Test.class.getResourceAsStream("/dstu3/bundle-with-no-type.json"), "UTF-8");

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
		inputString = IOUtils.toString(FhirInstanceValidatorDstu3Test.class.getResourceAsStream("/brian_reinhold_bundle.json"), "UTF-8");
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

	/**
	 * See #851
	 */
	@Test
	public void testValidateCoding() {
		ImagingStudy is = new ImagingStudy();
		is.setUid("urn:oid:1.2.3.4");
		is.getPatient().setReference("Patient/1");

		is.getModalityListFirstRep().setSystem("http://dicom.nema.org/resources/ontology/DCM");
		is.getModalityListFirstRep().setCode("BAR");
		is.getModalityListFirstRep().setDisplay("Hello");

		ValidationResult results = myVal.validateWithResult(is);
		List<SingleValidationMessage> outcome = logResultsAndReturnNonInformationalOnes(results);
		assertEquals(2, outcome.size());
		assertEquals("Unknown code: http://dicom.nema.org/resources/ontology/DCM / BAR", outcome.get(0).getMessage());

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
	public void testValidateDocument() throws Exception {
		String vsContents = IOUtils.toString(FhirInstanceValidatorDstu3Test.class.getResourceAsStream("/sample-document.xml"), "UTF-8");

		ValidationResult output = myVal.validateWithResult(vsContents);
		logResultsAndReturnNonInformationalOnes(output);
		assertTrue(output.isSuccessful());
	}

	/**
	 * See #739
	 */
	@Test
	public void testValidateMedicationIngredient() throws IOException {
		String input = IOUtils.toString(FhirInstanceValidatorDstu3Test.class.getResourceAsStream("/dstu3/bug739.json"), Charsets.UTF_8);

		ValidationResult results = myVal.validateWithResult(input);
		List<SingleValidationMessage> outcome = logResultsAndReturnNonInformationalOnes(results);
		assertThat(outcome.toString(), containsString("Element 'Medication.ingredient[0].item[x]': minimum required = 1"));

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
		String input = "<Patient xmlns=\"http://hl7.org/fhir\"><name><given/></name></Patient>";

		ValidationResult output = myVal.validateWithResult(input);
		ourLog.info(ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(output.toOperationOutcome()));
		assertEquals(output.toString(), 3, output.getMessages().size());
		assertThat(output.getMessages().get(0).getMessage(), containsString("Element must have some content"));
		assertThat(output.getMessages().get(1).getMessage(), containsString("Primitive types must have a value or must have child extensions"));
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
	public void testValidateReferenceWithDisplayValid() {
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
	public void testValidateReferenceWithIdentifierValid() {
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
		assertThat(errors.toString(), containsString("StructureDefinition reference \"http://foo/structuredefinition/myprofile\" could not be resolved"));
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
		assertEquals("Patient.identifier[0].type", all.get(0).getLocationString());
		assertEquals(
			"None of the codes provided are in the value set http://hl7.org/fhir/ValueSet/identifier-type (http://hl7.org/fhir/ValueSet/identifier-type, and a code should come from this value set unless it has no suitable code) (codes = http://example.com/foo/bar#bar)",
			all.get(0).getMessage());
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
		String input = loadResource("/sdc-questionnaire.profile.xml");

		ValidationResult output = myVal.validateWithResult(input);
		logResultsAndReturnAll(output);

		assertEquals(output.toString(), 3, output.getMessages().size());
		ourLog.info(output.getMessages().get(0).getLocationString());
		ourLog.info(output.getMessages().get(0).getMessage());
	}

	@Test
	public void testValueWithWhitespace() throws IOException {
		String input = IOUtils.toString(FhirInstanceValidatorDstu3Test.class.getResourceAsStream("/dstu3-rick-test.json"), Charsets.UTF_8);

		ValidationResult results = myVal.validateWithResult(input);
		List<SingleValidationMessage> outcome = logResultsAndReturnNonInformationalOnes(results);
		assertEquals(1, outcome.size());
		assertThat(outcome.toString(), containsString("value should not start or finish with whitespace"));

	}

	@AfterClass
	public static void afterClassClearContext() {
		myDefaultValidationSupport.flush();
		myDefaultValidationSupport = null;
		TestUtil.clearAllStaticFieldsForUnitTest();
	}


}
