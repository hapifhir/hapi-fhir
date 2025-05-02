package org.hl7.fhir.r4.validation.performance;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.util.ClasspathUtil;
import ca.uhn.fhir.util.StopWatch;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ResultSeverityEnum;
import ca.uhn.fhir.validation.SingleValidationMessage;
import ca.uhn.fhir.validation.ValidationResult;
import org.hl7.fhir.common.hapi.validation.support.CommonCodeSystemsTerminologyService;
import org.hl7.fhir.common.hapi.validation.support.InMemoryTerminologyServerValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.PrePopulatedValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.SnapshotGeneratingValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.Narrative;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Procedure;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ValidationCanonicalizationTest {

	private static final int NUM_RUNS = 10;
	private static final int NUM_CONCEPTS = 100_000;

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

	private FhirValidator myValidator;


	@BeforeEach
	public void beforeEach(){
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

		myValidator = configureValidator();
	}

	@Test
	public void testConversionCache_disabled() {
		validationTestCase(false);
	}

//	@Test
//	public void testConversionCache_enabled() {
//		validationTestCase(true);
//	}

	private void validationTestCase(boolean theUseConversionCache) {
		// FIXME
//		ConverterMetricUtil.setUseCache(theUseConversionCache);

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


		long totalTime = 0L;
		AtomicLong totalConversionTime = new AtomicLong();
		long max = 0L;

		logMetrics();

		for (int run = 1; run <= NUM_RUNS; run++){
			resetMetrics();

			StopWatch sw = new StopWatch();
			ValidationResult validationResult = myValidator.validateWithResult(procedure);
			long millis = sw.getMillis();

			ourLog.info("=== Run #{} - Validated resource in: {}ms ===", run, millis);

			totalTime += millis;
			ourVersionCanonicalizer.getMetrics().forEach(m -> totalConversionTime.addAndGet(m.getElapsedTime()));

			if (millis > max){
				max = millis;
			}

			logMetrics();
			assertHasInvalidCodeError(validationResult);
		}

		logSummary(totalTime, max, totalConversionTime.get());
	}


	private void logSummary(long totalTime, long max, long totalConversionTime) {
		ourLog.info("\n===== RUNS: {} | TOTAL TIME: {}ms | MAX: {}ms | AVERAGE TIME: {}ms | CONVERSION TIME: {}ms =====", NUM_RUNS, totalTime, max, totalTime / NUM_RUNS, totalConversionTime);
	}

	private void assertHasInvalidCodeError(ValidationResult validationResult) {
//		ourLog.info("===Validation Messages ({})===", validationResult.getMessages().size());
//		validationResult.getMessages().forEach(message -> ourLog.info("[{}:{} at {}]", message.getSeverity(), message.getMessage(), message.getLocationString()));

		assertFalse(validationResult.isSuccessful());
		assertEquals(2, validationResult.getMessages().size());

		SingleValidationMessage message1 = validationResult.getMessages().get(0);
		assertEquals(ResultSeverityEnum.ERROR, message1.getSeverity());
		assertEquals("Procedure.code", message1.getLocationString());
		String expectedMessage1 = "None of the codings provided are in the value set 'Value Set Combined' (http://acme.org/ValueSet/valueset-combined|1), and a coding from this value set is required) (codes = http://acme.org/CodeSystem/codesystem-1#codesystem-1-concept-100000, http://acme.org/CodeSystem/codesystem-2#codesystem-2-concept-100000, http://acme.org/invalid#invalid)";
		assertEquals(expectedMessage1, message1.getMessage());

		SingleValidationMessage message2 = validationResult.getMessages().get(1);
		assertEquals(ResultSeverityEnum.INFORMATION, message2.getSeverity());
		assertEquals("Procedure.code.coding[2]", message2.getLocationString());
		String expectedMessage2= "This element does not match any known slice defined in the profile http://example.org/fhir/StructureDefinition/TestProcedure|1.0.0 (this may not be a problem, but you should check that it's not intended to match a slice)";
		assertEquals(expectedMessage2, message2.getMessage());
	}

	private void resetMetrics(){
		ourVersionCanonicalizer.resetMetrics();
		assertTrue(ourVersionCanonicalizer.getMetrics().isEmpty());
	}

	private void logMetrics(){
		ourVersionCanonicalizer.getMetrics().forEach(metric -> {
			String metrics = metric.writeMetrics(10_000, new ConverterInvocation.ElapsedTimeComparator());
			ourLog.info("{}", metrics);
		});
	}

	private FhirValidator configureValidator(){
		ValidationSupportChain supportChain = new ValidationSupportChain();
		supportChain.addValidationSupport(new DefaultProfileValidationSupport(ourFhirContext));
		supportChain.addValidationSupport(new SnapshotGeneratingValidationSupport(ourFhirContext, ourVersionCanonicalizer));
		supportChain.addValidationSupport(new InMemoryTerminologyServerValidationSupport(ourFhirContext, ourVersionCanonicalizer));
		supportChain.addValidationSupport(new CommonCodeSystemsTerminologyService(ourFhirContext, ourVersionCanonicalizer));

		PrePopulatedValidationSupport prePopulatedSupport = new PrePopulatedValidationSupport(ourFhirContext);
		addCodeSystems(prePopulatedSupport);
		addValueSets(prePopulatedSupport);
		addStructureDefinitions(prePopulatedSupport);

		supportChain.addValidationSupport(prePopulatedSupport);

		FhirInstanceValidator module = new FhirInstanceValidator(supportChain, ourVersionCanonicalizer);
		FhirValidator validator = ourFhirContext.newValidator();
		validator.registerValidatorModule(module);
		return validator;
	}

	private void addCodeSystems(PrePopulatedValidationSupport prePopulatedSupport) {
		for (CodeSystem codeSystem : myAllCodeSystems){
			prePopulatedSupport.addCodeSystem(codeSystem);
			ourLog.info("Loaded CodeSystem {}", codeSystem.getId());
		}
	}

	private void addValueSets(PrePopulatedValidationSupport prePopulatedSupport) {
		for (ValueSet valueSet : myAllValueSets) {
			prePopulatedSupport.addValueSet(valueSet);
			ourLog.info("Loaded ValueSet {}", valueSet.getId());
		}
	}

	private void addStructureDefinitions(PrePopulatedValidationSupport prePopulatedSupport) {
		for (StructureDefinition structureDefinition : myAllStructureDefinitions) {
			prePopulatedSupport.addStructureDefinition(structureDefinition);
			ourLog.info("Loaded StructureDefinition: {}", structureDefinition.getId());
		}
	}
}
