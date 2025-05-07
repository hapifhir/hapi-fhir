package ca.uhn.fhir.jpa.provider.validation.performance;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.jpa.provider.validation.performance.MetricCapturingVersionCanonicalizer.CanonicalizationMetrics;
import ca.uhn.fhir.jpa.validation.JpaValidationSupportChain;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import ca.uhn.fhir.util.ClasspathUtil;
import ca.uhn.fhir.util.StopWatch;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ResultSeverityEnum;
import ca.uhn.fhir.validation.SingleValidationMessage;
import ca.uhn.fhir.validation.ValidationResult;
import org.apache.commons.text.StringSubstitutor;
import org.hl7.fhir.common.hapi.validation.support.*;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.*;
import org.hl7.fhir.r4.model.OperationOutcome.IssueSeverity;
import org.hl7.fhir.r4.model.OperationOutcome.OperationOutcomeIssueComponent;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class ValidationCanonicalizationTest extends BaseResourceProviderR4Test {

	@Autowired
	private FhirInstanceValidator myInstanceValidator;

	@Autowired
	private JpaValidationSupportChain myJpaValidationSupportChain;

	private static final int NUM_RUNS = 10;

//	private static final int NUM_CONCEPTS = 1000;
	private static final int NUM_CONCEPTS = 50_000;
//	private static final int NUM_CONCEPTS = 100_000;

	private static final Logger ourLog = LoggerFactory.getLogger(ValidationCanonicalizationTest.class);
	private static final FhirContext ourFhirContext = FhirContext.forR4Cached();
	private static final MetricCapturingVersionCanonicalizer ourVersionCanonicalizer = new MetricCapturingVersionCanonicalizer(ourFhirContext);

	private StructureDefinition myStructureDefinition;
	private List<StructureDefinition> myAllStructureDefinitions;

	private CodeSystem myCodeSystem1;
	private CodeSystem myCodeSystem2;
	private List<CodeSystem> myAllCodeSystems;

	private ValueSet myValueSet1;
	private ValueSet myValueSet2;
	private ValueSet myValueSetCombined;
	private List<ValueSet> myAllValueSets;

	@BeforeEach
	public void beforeEach() {
		setVersionCanonicalizer();

		myStructureDefinition = ClasspathUtil.loadResource(ourFhirContext, StructureDefinition.class, "/validation/structure-definitions/procedure-structuredefinition.json");
		myAllStructureDefinitions = List.of(myStructureDefinition);

		myCodeSystem1 = CreateTerminologyTestUtil.createCodeSystem("codesystem-1", "Code System One", CodeSystem.CodeSystemContentMode.COMPLETE, NUM_CONCEPTS);
		myCodeSystem2 = CreateTerminologyTestUtil.createCodeSystem("codesystem-2", "Code System Two", CodeSystem.CodeSystemContentMode.COMPLETE, NUM_CONCEPTS);
		myAllCodeSystems = List.of(myCodeSystem1, myCodeSystem2);

		myValueSet1 = CreateTerminologyTestUtil.createValueSetFromCodeSystemConcepts("valueset-1", "Value Set One", myCodeSystem1);
		// ensures expansion functionality is tested
		myValueSet2 = CreateTerminologyTestUtil.createValueSetFromCodeSystemUrl("valueset-2", "Value Set Two", myCodeSystem2.getUrl());
		myValueSetCombined = CreateTerminologyTestUtil.createValueSetFromValueSets("valueset-combined", "Value Set Combined", myValueSet1, myValueSet2);
		myAllValueSets = List.of(myValueSet1, myValueSet2, myValueSetCombined);

		List<IBaseResource> allResources = new ArrayList<>();
		Stream.of(myAllCodeSystems, myAllValueSets, myAllStructureDefinitions).flatMap(List::stream).forEach(allResources::add);
		for (IBaseResource resource : allResources) {
			myClient.update().resource(resource).execute();
			ourLog.info("Loaded Resource {}", resource.getIdElement().getIdPart());
		}
	}

	@AfterEach
	public void afterEach() {
		resetMetrics();
	}

	@Nested
	@Order(1)
	class ValidateCanonicalization {

		@Test
		public void testCanonicalization() {
			Procedure procedure = createProcedure();
			List<TestRun> testRuns = new ArrayList<>();

			for (int runNumber = 1; runNumber <= NUM_RUNS; runNumber++) {
				ourLog.info("Start Run #{}", runNumber);
				resetMetrics();

				StopWatch sw = new StopWatch();
				MethodOutcome methodOutcome = myClient.validate().resource(procedure).execute();

				TestRun testRun = new TestRun(runNumber, ourVersionCanonicalizer.getMetrics(), sw.getMillis());
				testRuns.add(testRun);

				logMetrics(testRun);

				assertInstanceOf(OperationOutcome.class, methodOutcome.getOperationOutcome());
				assertHasInvalidCodeError((OperationOutcome) methodOutcome.getOperationOutcome());
			}

			logSummary(testRuns);
		}

		private void assertHasInvalidCodeError(OperationOutcome theOperationOutcome) {
			List<OperationOutcomeIssueComponent> issues = theOperationOutcome.getIssue();
			assertEquals(3, issues.size());

			OperationOutcomeIssueComponent issue1 = issues.get(0);
			assertEquals(IssueSeverity.ERROR, issue1.getSeverity());

			Map<String, String> formatValues = Map.of("conceptNumber", String.valueOf(NUM_CONCEPTS));
			assertEquals("Parameters.parameter[0].resource/*Procedure/null*/.code", issue1.getLocation().get(0).getValue());
			String expectedMessage1 = "None of the codings provided are in the value set 'Value Set Combined' (http://acme.org/ValueSet/valueset-combined|1), and a coding from this value set is required) (codes = http://acme.org/CodeSystem/codesystem-1#codesystem-1-concept-${conceptNumber}, http://acme.org/CodeSystem/codesystem-2#codesystem-2-concept-${conceptNumber}, http://acme.org/invalid#invalid)";
			assertEquals(formatMessage(expectedMessage1, formatValues), issue1.getDiagnostics());

			OperationOutcomeIssueComponent issue2 = issues.get(1);
			assertEquals(IssueSeverity.INFORMATION, issue2.getSeverity());
			assertEquals("Parameters.parameter[0].resource/*Procedure/null*/.code.coding[2]", issue2.getLocation().get(0).getValue());
			String expectedMessage2 = "This element does not match any known slice defined in the profile http://example.org/fhir/StructureDefinition/TestProcedure|1.0.0 (this may not be a problem, but you should check that it's not intended to match a slice) - Does not match slice 'slice1' (discriminator: ($this memberOf 'http://acme.org/ValueSet/valueset-1'))";
			assertEquals(expectedMessage2, issue2.getDiagnostics());

			OperationOutcomeIssueComponent issue3 = issues.get(2);
			assertEquals(IssueSeverity.INFORMATION, issue3.getSeverity());
			assertEquals("Parameters.parameter[0].resource/*Procedure/null*/.code.coding[2]", issue3.getLocation().get(0).getValue());
			String expectedMessage3 = "This element does not match any known slice defined in the profile http://example.org/fhir/StructureDefinition/TestProcedure|1.0.0 (this may not be a problem, but you should check that it's not intended to match a slice) - Does not match slice 'slice2' (discriminator: ($this memberOf 'http://acme.org/ValueSet/valueset-2'))";
			assertEquals(expectedMessage3, issue3.getDiagnostics());
		}
	}

	private String formatMessage(String theMessage, Map<String, String> theFormatValues){
		return StringSubstitutor.replace(theMessage, theFormatValues);
	}

	@Nested
	@Order(2)
	class RemoteTerminologyValidateCanonicalization {

		private FhirValidator myValidator;

		@BeforeEach
		public void beforeEach() {
			myValidator = createValidator();
		}

		@Test
		public void testCanonicalization() {
			Procedure procedure = createProcedure();
			List<TestRun> testRuns = new ArrayList<>();

			for (int runNumber = 1; runNumber <= NUM_RUNS; runNumber++) {
				ourLog.info("Start Run #{}", runNumber);
				resetMetrics();

				StopWatch sw = new StopWatch();
				ValidationResult validationResult = myValidator.validateWithResult(procedure);

				TestRun run = new TestRun(runNumber, ourVersionCanonicalizer.getMetrics(), sw.getMillis());
				testRuns.add(run);

				logMetrics(run);
				assertValidationErrors(validationResult);
			}

			logSummary(testRuns);
		}

		private FhirValidator createValidator() {
			ValidationSupportChain supportChain = new ValidationSupportChain();

			RemoteTerminologyServiceValidationSupport remoteTermSupport = new RemoteTerminologyServiceValidationSupport(ourFhirContext, myServerBase);
			remoteTermSupport.addClientInterceptor(new LoggingInterceptor());

			supportChain.addValidationSupport(remoteTermSupport);
			supportChain.addValidationSupport(new DefaultProfileValidationSupport(ourFhirContext));
			supportChain.addValidationSupport(new SnapshotGeneratingValidationSupport(ourFhirContext));

			PrePopulatedValidationSupport prePopulatedSupport = new PrePopulatedValidationSupport(ourFhirContext);
			for (StructureDefinition structureDefinition : myAllStructureDefinitions) {
				prePopulatedSupport.addStructureDefinition(structureDefinition);
				ourLog.info("Loaded StructureDefinition: {}", structureDefinition.getId());
			}

			supportChain.addValidationSupport(prePopulatedSupport);
			setVersionCanonicalizer(supportChain.getValidationSupports());

			FhirInstanceValidator module = new FhirInstanceValidator(supportChain);
			FhirValidator validator = ourFhirContext.newValidator();
			validator.registerValidatorModule(module);
			return validator;
		}

		private void assertValidationErrors(ValidationResult theValidationResult) {
			assertFalse(theValidationResult.isSuccessful());
			assertEquals(2, theValidationResult.getMessages().size());

			SingleValidationMessage message1 = theValidationResult.getMessages().get(0);
			assertEquals(ResultSeverityEnum.ERROR, message1.getSeverity());
			assertEquals("Procedure.code", message1.getLocationString());
			String expectedMessage1 = "None of the codings provided are in the value set 'Value Set Combined' (http://acme.org/ValueSet/valueset-combined|1), and a coding from this value set is required) (codes = http://acme.org/CodeSystem/codesystem-1#codesystem-1-concept-100000, http://acme.org/CodeSystem/codesystem-2#codesystem-2-concept-100000, http://acme.org/invalid#invalid)";
			assertEquals(expectedMessage1, message1.getMessage());

			SingleValidationMessage message2 = theValidationResult.getMessages().get(1);
			assertEquals(ResultSeverityEnum.INFORMATION, message2.getSeverity());
			assertEquals("Procedure.code.coding[2]", message2.getLocationString());
			String expectedMessage2 = "This element does not match any known slice defined in the profile http://example.org/fhir/StructureDefinition/TestProcedure|1.0.0 (this may not be a problem, but you should check that it's not intended to match a slice)";
			assertEquals(expectedMessage2, message2.getMessage());

		}
	}

	private void setVersionCanonicalizer() {
		// initialize VersionSpecificWorkerContextWrapper
		myClient.validate().resource(new Patient().setActive(true)).execute();

		myInstanceValidator.getWorkerContext().setVersionCanonicalizer(ourVersionCanonicalizer);
		setVersionCanonicalizer(myJpaValidationSupportChain.getValidationSupports());
	}

	private void setVersionCanonicalizer(List<IValidationSupport> theSupports) {
		for (IValidationSupport support : theSupports) {
			if (support instanceof InMemoryTerminologyServerValidationSupport inMemory) {
				inMemory.setVersionCanonicalizer(ourVersionCanonicalizer);
			}
			if (support instanceof CommonCodeSystemsTerminologyService commonCodeSystem) {
				commonCodeSystem.setVersionCanonicalizer(ourVersionCanonicalizer);
			}
			if (support instanceof SnapshotGeneratingValidationSupport snapshotGenerating) {
				snapshotGenerating.setVersionCanonicalizer(ourVersionCanonicalizer);
			}
		}
	}

	private Procedure createProcedure() {
		Procedure procedure = new Procedure();
		procedure.getText().setStatus(Narrative.NarrativeStatus.GENERATED).setDivAsString("<div xmlns=\"http://www.w3.org/1999/xhtml\">Empty</div>");
		procedure.getMeta().addProfile(myStructureDefinition.getUrl());
		procedure.setStatus(Procedure.ProcedureStatus.INPROGRESS);

		int lastConceptIndex = NUM_CONCEPTS - 1;

		// add code from ValueSet 1 (has all concepts in include)
		assertEquals(1, myValueSet1.getCompose().getInclude().size());
		assertEquals(NUM_CONCEPTS, myValueSet1.getCompose().getInclude().get(0).getConcept().size());
		ValueSet.ConceptReferenceComponent valueSet1LastConcept = myValueSet1.getCompose().getInclude().get(0).getConcept().get(lastConceptIndex);
		procedure.getCode().addCoding()
				.setSystem(myCodeSystem1.getUrl())
				.setCode(valueSet1LastConcept.getCode())
				.setDisplay(valueSet1LastConcept.getDisplay());

		// add code from ValueSet 2 (has no concepts included and must be expanded)
		assertEquals(1, myValueSet2.getCompose().getInclude().size());
		assertTrue(myValueSet2.getCompose().getInclude().get(0).getConcept().isEmpty());
		procedure.getCode().addCoding()
				.setSystem(myCodeSystem2.getUrl())
				.setCode("codesystem-2-concept-" + NUM_CONCEPTS)
				.setDisplay("Code System Two Concept " + NUM_CONCEPTS);

		// add invalid code to ensure validation is functioning properly
		procedure.getCode().addCoding().setSystem("http://acme.org/invalid").setCode("invalid").setDisplay("Invalid");

		Patient subject = new Patient();
		subject.setId("subject-1");
		procedure.setSubject(new Reference(subject));
		return procedure;
	}

	private void logSummary(List<TestRun> theTestRuns) {
		long totalTime = 0L;
		long max = 0L;
		long totalConversionTime = 0L;
		long totalInvocations = 0L;

		for (TestRun testRun : theTestRuns){
			long elapsedTime = testRun.getElapsedTime();
			if (elapsedTime > max){
				max = elapsedTime;
			}
			totalTime += elapsedTime;
			totalConversionTime += testRun.getTotalConversionTime();
			totalInvocations += testRun.getTotalInvocations();
		}

		ourLog.info("\n===== RUNS: {} | TOTAL TIME: {}ms | MAX: {}ms | CONVERSION TIME: {}ms | TOTAL INVOCATIONS: {} =====",
				NUM_RUNS, totalTime, max, totalConversionTime, totalInvocations);
	}

	private void resetMetrics() {
		ourVersionCanonicalizer.resetMetrics();
		assertTrue(ourVersionCanonicalizer.getCanonicalizationMethods().isEmpty());
	}

	private void logMetrics(TestRun theTestRun) {
		ourLog.info("=== End Run #{} - Validated resource in: {}ms ===", theTestRun.getNumber(), theTestRun.getElapsedTime());

		CanonicalizationMethodInvocation.ElapsedTimeComparator comparator = new CanonicalizationMethodInvocation.ElapsedTimeComparator();
		Predicate<CanonicalizationMethodInvocation> filter = invocation -> !invocation.getResourceId().startsWith("StructureDefinition");

		ourVersionCanonicalizer.getCanonicalizationMethods().forEach(metric -> {
			String metrics = metric.writeMetrics(10_000, comparator, filter);
			ourLog.info("{}", metrics);
		});
	}

	static class TestRun {
		private final int myNumber;
		private final CanonicalizationMetrics myCanonicalizationMetrics;
		private final long myElapsedTime;

		public TestRun(int theNumber, CanonicalizationMetrics theCanonicalizationMetrics, long theElapsedTime) {
			myNumber = theNumber;
			myCanonicalizationMetrics = theCanonicalizationMetrics;
			myElapsedTime = theElapsedTime;
		}

		public int getNumber() {
			return myNumber;
		}

		public long getElapsedTime() {
			return myElapsedTime;
		}

		public long getTotalConversionTime() {
			return myCanonicalizationMetrics.getTotalConversionTime();
		}

		public long getTotalInvocations(){
			return myCanonicalizationMetrics.getTotalInvocations();
		}
	}
}
