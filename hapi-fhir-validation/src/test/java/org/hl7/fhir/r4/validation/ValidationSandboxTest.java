package org.hl7.fhir.r4.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.util.ClasspathUtil;
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
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ValidationSandboxTest {

	private static final Logger ourLog = LoggerFactory.getLogger(ValidationSandboxTest.class);

	private static final String CODE_SYSTEMS_DIRECTORY = "/sandbox/code-systems";
	private static final String VALUE_SETS_DIRECTORY = "/sandbox/value-sets";
	private static final String STRUCTURE_DEFINITIONS_DIRECTORY = "/sandbox/structure-definitions";

	private static final FhirContext ourFhirContext = FhirContext.forR4Cached();
	private static FhirValidator myValidator;

	@BeforeAll
	public static void beforeAll(){
		configureValidator();
	}

	@Test
	public void testValidatorWithCustomProfile() {
		// Profile defined in /sandbox/structure-definitions/test-observation.json
		Observation observation = new Observation();
		observation.getMeta().addProfile("http://example.org/fhir/StructureDefinition/TestObservation");

		// missing code
		ValidationResult validationResult = myValidator.validateWithResult(observation);
		List<SingleValidationMessage> validationErrors = getValidationMessagesBySeverity(validationResult.getMessages(), ResultSeverityEnum.ERROR);
		assertFalse(validationResult.isSuccessful());
		assertEquals(1, validationErrors.size());
		String expectedErrorMessage = "Observation.code: minimum required = 1, but only found 0 (from http://example.org/fhir/StructureDefinition/TestObservation)";
		assertEquals(expectedErrorMessage, validationErrors.get(0).getMessage());

		// valid code from CodeSystem 1 (/sandbox/code-systems/code-system-1.json)
		observation.getCode().addCoding().setCode("value-1").setSystem("http://acme.org/code-system-1");
		validationResult = myValidator.validateWithResult(observation);
		assertTrue(validationResult.isSuccessful());

		// code from CodeSystem 1 not included in the ValueSet (/sandbox/value-sets/value-set-1.json)
		observation.getCode().getCoding().clear();
		assertTrue(observation.getCode().getCoding().isEmpty());
		observation.getCode().addCoding().setCode("value-2").setSystem("http://acme.org/code-system-1");
		validationResult = myValidator.validateWithResult(observation);
		assertFalse(validationResult.isSuccessful());
		validationErrors = getValidationMessagesBySeverity(validationResult.getMessages(), ResultSeverityEnum.ERROR);
		assertEquals(1, validationErrors.size());
		expectedErrorMessage = "None of the codings provided are in the value set 'ValueSet 1' (http://acme.org/value-set-1), and a coding from this value set is required) (codes = http://acme.org/code-system-1#value-2)";
		assertEquals(expectedErrorMessage, validationErrors.get(0).getMessage());

		// valid code from CodeSystem 2 (/sandbox/code-systems/code-system-2.json)
		observation.getCode().getCoding().clear();
		assertTrue(observation.getCode().getCoding().isEmpty());
		observation.getCode().addCoding().setCode("value-2").setSystem("http://acme.org/code-system-2");
		validationResult = myValidator.validateWithResult(observation);
		assertTrue(validationResult.isSuccessful());
	}

	private static List<SingleValidationMessage> getValidationMessagesBySeverity(
		List<SingleValidationMessage> theValidationMessages, ResultSeverityEnum theSeverity) {
		return theValidationMessages.stream().filter(m -> m.getSeverity() == theSeverity).collect(Collectors.toList());
	}

	private static void configureValidator() {
		ValidationSupportChain supportChain = new ValidationSupportChain();
		supportChain.addValidationSupport(new DefaultProfileValidationSupport(ourFhirContext));
		supportChain.addValidationSupport(new SnapshotGeneratingValidationSupport(ourFhirContext));
		supportChain.addValidationSupport(new InMemoryTerminologyServerValidationSupport(ourFhirContext));
		supportChain.addValidationSupport(new CommonCodeSystemsTerminologyService(ourFhirContext));

		PrePopulatedValidationSupport prePopulatedSupport = new PrePopulatedValidationSupport(ourFhirContext);
		IParser parser = ourFhirContext.newJsonParser();
		addCodeSystems(prePopulatedSupport, parser);
		addValueSets(prePopulatedSupport, parser);
		addStructureDefinitions(prePopulatedSupport, parser);

		supportChain.addValidationSupport(prePopulatedSupport);

		FhirInstanceValidator module = new FhirInstanceValidator(supportChain);
		myValidator = ourFhirContext.newValidator();
		myValidator.registerValidatorModule(module);
	}

	private static void addCodeSystems(PrePopulatedValidationSupport prePopulatedSupport, IParser theParser) {
		List<String> fileNames = getFileNamesFromDirectory(CODE_SYSTEMS_DIRECTORY);
		for (String fileName : fileNames) {
			String path = CODE_SYSTEMS_DIRECTORY + "/" + fileName;
			String json = ClasspathUtil.loadResource(path);
			CodeSystem codeSystem = theParser.parseResource(CodeSystem.class, json);
			prePopulatedSupport.addCodeSystem(codeSystem);
			ourLog.info("Loaded CodeSystem: {}", path);
		}
	}

	private static void addValueSets(PrePopulatedValidationSupport prePopulatedSupport, IParser theParser) {
		List<String> fileNames = getFileNamesFromDirectory(VALUE_SETS_DIRECTORY);
		for (String fileName : fileNames) {
			String path = VALUE_SETS_DIRECTORY + "/" + fileName;
			String json = ClasspathUtil.loadResource(path);
			ValueSet valueSet = theParser.parseResource(ValueSet.class, json);
			prePopulatedSupport.addValueSet(valueSet);
			ourLog.info("Loaded ValueSet: {}", path);
		}
	}

	private static void addStructureDefinitions(PrePopulatedValidationSupport prePopulatedSupport, IParser theParser) {
		List<String> fileNames = getFileNamesFromDirectory(STRUCTURE_DEFINITIONS_DIRECTORY);
		for (String fileName : fileNames) {
			String path = STRUCTURE_DEFINITIONS_DIRECTORY + "/" + fileName;
			String json = ClasspathUtil.loadResource(path);
			StructureDefinition structureDefinition = theParser.parseResource(StructureDefinition.class, json);
			prePopulatedSupport.addStructureDefinition(structureDefinition);
			ourLog.info("Loaded StructureDefinition: {}", path);
		}
	}
	private static List<String> getFileNamesFromDirectory(String theDirectory){
		List<String> filenames = new ArrayList<>();
		try (InputStream in = ValidationSandboxTest.class.getResourceAsStream(theDirectory);
			 BufferedReader br = new BufferedReader(new InputStreamReader(in))){
			String resource;
			while ((resource = br.readLine()) != null) {
				filenames.add(resource);
			}
		} catch (IOException e){
			throw new RuntimeException(e);
		}
		return filenames;
	}
}
