package ca.uhn.fhir.tests.integration.karaf.dstu2hl7org;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ValidationResult;
import org.hl7.fhir.dstu2.model.DateType;
import org.hl7.fhir.dstu2.model.Observation;
import org.hl7.fhir.dstu2.model.QuestionnaireResponse;
import org.hl7.fhir.dstu2.model.StringType;
import org.hl7.fhir.instance.hapi.validation.DefaultProfileValidationSupport;
import org.hl7.fhir.instance.hapi.validation.FhirInstanceValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;

import java.io.IOException;

import static ca.uhn.fhir.tests.integration.karaf.PaxExamOptions.*;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.ops4j.pax.exam.CoreOptions.*;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.debugConfiguration;

/**
 * Useful docs about this test: https://ops4j1.jira.com/wiki/display/paxexam/FAQ
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
public class FhirInstanceValidatorTest {
   private FhirInstanceValidator ourValidator = new FhirInstanceValidator(new DefaultProfileValidationSupport());
   private FhirContext ourCtxHl7OrgDstu2 = FhirContext.forDstu2Hl7Org();
   private final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirInstanceValidatorTest.class);

	@Configuration
	public Option[] config() throws IOException {
		return options(
			KARAF.option(),
			WRAP.option(),
			HAPI_FHIR_VALIDATION_HL7ORG_DSTU2.option(),
			mavenBundle().groupId("org.apache.servicemix.bundles").artifactId("org.apache.servicemix.bundles.hamcrest").versionAsInProject(),
			when(false)
				.useOptions(
					debugConfiguration("5005", true))
		);
	}

	@Test
	public void testQuestionnaireResponse() {
		QuestionnaireResponse qr = new QuestionnaireResponse();
		qr.setStatus(QuestionnaireResponse.QuestionnaireResponseStatus.COMPLETED);
		qr.getGroup().addGroup().addQuestion().setLinkId("foo");
		qr.getGroup().addQuestion().setLinkId("bar");

		FhirValidator val = ourCtxHl7OrgDstu2.newValidator();

		val.registerValidatorModule(ourValidator);

		ValidationResult result = val.validateWithResult(qr);

		String encoded = ourCtxHl7OrgDstu2.newJsonParser().setPrettyPrint(true).encodeResourceToString(result.toOperationOutcome());
		ourLog.info(encoded);

		assertTrue(result.isSuccessful());
	}

	@Test
	public void testObservation() {
		Observation o = new Observation();
		o.addIdentifier().setSystem("http://acme.org").setValue("1234");
		o.setStatus(Observation.ObservationStatus.FINAL);
		o.getCode().addCoding().setSystem("http://loinc.org").setCode("12345");
		o.getEncounter().setReference("Encounter/1234");

		FhirValidator val = ourCtxHl7OrgDstu2.newValidator();

		val.registerValidatorModule(ourValidator);

		ValidationResult result = val.validateWithResult(o);

		String encoded = ourCtxHl7OrgDstu2.newJsonParser().setPrettyPrint(true).encodeResourceToString(result.toOperationOutcome());
		ourLog.info(encoded);

		assertTrue(result.isSuccessful());
	}

	@Test
	public void testParametersWithTwoParameters() {
		org.hl7.fhir.dstu2.model.Patient patient = new org.hl7.fhir.dstu2.model.Patient();
		patient.addName().addGiven("James");
		patient.setBirthDateElement(new DateType("2011-02-02"));

		org.hl7.fhir.dstu2.model.Parameters input = new org.hl7.fhir.dstu2.model.Parameters();
		input.addParameter().setName("mode").setValue(new StringType("create"));
		input.addParameter().setName("resource").setResource(patient);

		FhirValidator val = ourCtxHl7OrgDstu2.newValidator();

		val.registerValidatorModule(ourValidator);

		ValidationResult result = val.validateWithResult(input);

		String encoded = ourCtxHl7OrgDstu2.newJsonParser().setPrettyPrint(true).encodeResourceToString(result.toOperationOutcome());
		ourLog.info(encoded);

		assertTrue(result.isSuccessful());
		assertThat(encoded, not(containsString("A parameter must have a value or a resource, but not both")));
	}

	@Test
	public void testParametersHl7OrgDstu2() {
		org.hl7.fhir.dstu2.model.Patient patient = new org.hl7.fhir.dstu2.model.Patient();
		patient.addName().addGiven("James");
		patient.setBirthDateElement(new DateType("2011-02-02"));

		org.hl7.fhir.dstu2.model.Parameters input = new org.hl7.fhir.dstu2.model.Parameters();
		input.addParameter().setName("resource").setResource(patient);

		FhirValidator val = ourCtxHl7OrgDstu2.newValidator();

		val.registerValidatorModule(ourValidator);

		ValidationResult result = val.validateWithResult(input);

		ourLog.info(ourCtxHl7OrgDstu2.newJsonParser().setPrettyPrint(true).encodeResourceToString(result.toOperationOutcome()));
		assertTrue(result.isSuccessful());
	}
}
