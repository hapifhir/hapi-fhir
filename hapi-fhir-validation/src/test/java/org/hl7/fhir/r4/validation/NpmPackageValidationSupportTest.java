package org.hl7.fhir.r4.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.fhirpath.BaseValidationTestWithInlineMocks;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ValidationResult;
import jakarta.annotation.Nonnull;
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
import java.util.Map;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NpmPackageValidationSupportTest extends BaseValidationTestWithInlineMocks {

	private static final Logger ourLog = LoggerFactory.getLogger(NpmPackageValidationSupportTest.class);
	private FhirContext myFhirContext = FhirContext.forR4Cached();

	private Map<String, byte[]> EXPECTED_BINARIES_MAP = Map.of(
		"dummyBinary1.txt", "myDummyContent1".getBytes(),
		"dummyBinary2.txt", "myDummyContent2".getBytes()
	);

	@Test
	public void testValidateWithPackage() throws IOException {

		// Create an NPM Package Support module and load one package in from
		// the classpath
		NpmPackageValidationSupport npmPackageSupport = getNpmPackageValidationSupport("classpath:package/UK.Core.r4-1.1.0.tgz");

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
		assertThat(outcomeSerialized).contains("Patient.identifier:nhsNumber.value: minimum required = 1, but only found 0");

	}

	@Nonnull
	private NpmPackageValidationSupport getNpmPackageValidationSupport(String theClasspath) throws IOException {
		NpmPackageValidationSupport npmPackageSupport = new NpmPackageValidationSupport(myFhirContext);
		npmPackageSupport.loadPackageFromClasspath(theClasspath);
		return npmPackageSupport;
	}

	@Test
	public void loadPackageFromClasspath_normally_loadsExpectedBinaries() throws IOException {
		NpmPackageValidationSupport npmPackageSupport = getNpmPackageValidationSupport("classpath:package/dummy-package-with-binaries.tgz");

		for (Map.Entry<String, byte[]> entry : EXPECTED_BINARIES_MAP.entrySet()) {
			byte[] expectedBytes = entry.getValue();
			byte[] actualBytes = npmPackageSupport.fetchBinary(entry.getKey());
			assertThat(actualBytes).containsExactly(expectedBytes);
		}
	}

	@Test
	public void testValidateIheMhdPackage() throws IOException {
		ValidationSupportChain validationSupportChain = new ValidationSupportChain();
		validationSupportChain.addValidationSupport(getNpmPackageValidationSupport("classpath:package/ihe.iti.mhd.tgz"));
		validationSupportChain.addValidationSupport(new DefaultProfileValidationSupport(myFhirContext));
		validationSupportChain.addValidationSupport(new CommonCodeSystemsTerminologyService(myFhirContext));
		validationSupportChain.addValidationSupport(new InMemoryTerminologyServerValidationSupport(myFhirContext));
		validationSupportChain.addValidationSupport(new SnapshotGeneratingValidationSupport(myFhirContext));

		CachingValidationSupport validationSupport = new CachingValidationSupport(validationSupportChain);

		FhirValidator validator = myFhirContext.newValidator();
		FhirInstanceValidator instanceValidator = new FhirInstanceValidator(validationSupport);
		validator.registerValidatorModule(instanceValidator);

		String bundle = loadResource("/r4/mhd_minimal_provide_document_bundle.json");
		ValidationResult validationResult = validator.validateWithResult(bundle);

		assertEquals(3, validationResult.getMessages().size());

		assertTrue(validationResult.isSuccessful());

		String outcomeSerialized = myFhirContext.newJsonParser()
			.setPrettyPrint(true)
			.encodeResourceToString(validationResult.toOperationOutcome());
		ourLog.info(outcomeSerialized);

		assertThat(outcomeSerialized).contains("Terminology_TX_ValueSet_NotFound");
	}
}
