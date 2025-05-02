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

	private FhirValidator myValidator;

	@BeforeEach
	public void beforeEach(){
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
		procedure.getMeta().addProfile("http://example.org/fhir/StructureDefinition/TestProcedure");
		procedure.setStatus(Procedure.ProcedureStatus.INPROGRESS);
		procedure.getCode().addCoding().setSystem("http://acme.org/CodeSystem/large-codesystem").setCode("concept-100000").setDisplay("Le Concept 100000");

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


	private static void logSummary(long totalTime, long max, long totalConversionTime) {
		ourLog.info("\n===== RUNS: {} | TOTAL TIME: {}ms | MAX: {}ms | AVERAGE TIME: {}ms | CONVERSION TIME: {}ms =====", NUM_RUNS, totalTime, max, totalTime / NUM_RUNS, totalConversionTime);
	}

	private static void assertHasInvalidCodeError(ValidationResult validationResult) {
//		ourLog.info("===Validation Messages ({})===", validationResult.getMessages().size());
//		validationResult.getMessages().forEach(message -> ourLog.info("[{}:{} at {}]", message.getSeverity(), message.getMessage(), message.getLocationString()));

		assertFalse(validationResult.isSuccessful());
		assertEquals(2, validationResult.getMessages().size());

		SingleValidationMessage message1 = validationResult.getMessages().get(0);
		assertEquals(ResultSeverityEnum.ERROR, message1.getSeverity());
		assertEquals("Procedure.code", message1.getLocationString());
		String expectedMessage1 = "None of the codings provided are in the value set 'Large ValueSet' (http://acme.org/ValueSet/large-valueset|1), and a coding from this value set is required) (codes = http://acme.org/CodeSystem/large-codesystem#concept-100000, http://acme.org/invalid#invalid)";
		assertEquals(expectedMessage1, message1.getMessage());

		SingleValidationMessage message2 = validationResult.getMessages().get(1);
		assertEquals(ResultSeverityEnum.INFORMATION, message2.getSeverity());
		assertEquals("Procedure.code.coding[1]", message2.getLocationString());
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
		CodeSystem loadedCodeSystem = addCodeSystems(prePopulatedSupport);
		addValueSets(prePopulatedSupport, loadedCodeSystem);
		addStructureDefinitions(prePopulatedSupport);

		supportChain.addValidationSupport(prePopulatedSupport);

		FhirInstanceValidator module = new FhirInstanceValidator(supportChain, ourVersionCanonicalizer);
		FhirValidator validator = ourFhirContext.newValidator();
		validator.registerValidatorModule(module);
		return validator;
	}

	private static CodeSystem addCodeSystems(PrePopulatedValidationSupport prePopulatedSupport) {
		CodeSystem codeSystem = LargeTerminologyUtil.createLargeCodeSystem(NUM_CONCEPTS);
		prePopulatedSupport.addCodeSystem(codeSystem);
		ourLog.info("Loaded CodeSystem with {} concepts", NUM_CONCEPTS);
		return codeSystem;
	}

	private static void addValueSets(PrePopulatedValidationSupport prePopulatedSupport, CodeSystem theLoadedCodeSystem) {
		ValueSet valueSet = LargeTerminologyUtil.createLargeValueSet(theLoadedCodeSystem);
		prePopulatedSupport.addValueSet(valueSet);
		ourLog.info("Loaded ValueSet with {} concepts", theLoadedCodeSystem.getConcept().size());
	}

	private static void addStructureDefinitions(PrePopulatedValidationSupport prePopulatedSupport) {
		String path = "/validation/structure-definitions/procedure-structuredefinition.json";
		StructureDefinition structureDefinition = ClasspathUtil.loadResource(ourFhirContext, StructureDefinition.class, path);
		prePopulatedSupport.addStructureDefinition(structureDefinition);
		ourLog.info("Loaded StructureDefinition: {}", path);
	}
}
