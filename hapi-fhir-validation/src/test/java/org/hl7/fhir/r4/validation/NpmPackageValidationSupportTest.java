package org.hl7.fhir.r4.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ValidationResult;
import org.hl7.fhir.common.hapi.validation.support.CachingValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.CommonCodeSystemsTerminologyService;
import org.hl7.fhir.common.hapi.validation.support.InMemoryTerminologyServerValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.NpmPackageValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.SnapshotGeneratingValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

public class NpmPackageValidationSupportTest {

	private static final Logger ourLog = LoggerFactory.getLogger(NpmPackageValidationSupportTest.class);
	private FhirContext myFhirContext = FhirContext.forR4Cached();

	@Test
	public void testValidateWithPackage() throws IOException {

		// Create an NPM Package Support module and load one package in from
		// the classpath
		NpmPackageValidationSupport npmPackageSupport = new NpmPackageValidationSupport(myFhirContext);
		npmPackageSupport.loadPackageFromClasspath("classpath:package/UK.Core.r4-1.1.0.tgz");

		// Create a support chain including the NPM Package Support
		ValidationSupportChain validationSupportChain = new ValidationSupportChain(
			npmPackageSupport,
			new DefaultProfileValidationSupport(myFhirContext),
			new CommonCodeSystemsTerminologyService(myFhirContext),
			new InMemoryTerminologyServerValidationSupport(myFhirContext),
			new SnapshotGeneratingValidationSupport(myFhirContext)
		);
		CachingValidationSupport validationSupport = new CachingValidationSupport(validationSupportChain);

		// Create a validator
		FhirValidator validator = myFhirContext.newValidator();
		FhirInstanceValidator instanceValidator = new FhirInstanceValidator(validationSupport);
		validator.registerValidatorModule(instanceValidator);

		// Create a test patient to validate
		Patient patient = new Patient();
		patient.getMeta().addProfile("https://fhir.nhs.uk/R4/StructureDefinition/UKCore-Patient");
		// System but not value set for NHS identifier (this should generate an error)
		patient.addIdentifier().setSystem("https://fhir.nhs.uk/Id/nhs-number");

		// Perform the validation
		ValidationResult outcome = validator.validateWithResult(patient);

		String outcomeSerialized = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(outcome.toOperationOutcome());
		ourLog.info(outcomeSerialized);
		assertThat(outcomeSerialized, containsString("Patient.identifier:nhsNumber.value: minimum required = 1, but only found 0"));

	}

}
