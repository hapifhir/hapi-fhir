package org.hl7.fhir.dstu2.hapi.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.DefaultProfileValidationSupport;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.model.dstu2.composite.PeriodDt;
import ca.uhn.fhir.model.dstu2.resource.Parameters;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.resource.Procedure;
import ca.uhn.fhir.model.dstu2.valueset.ProcedureStatusEnum;
import ca.uhn.fhir.model.primitive.DateDt;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.util.TestUtil;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ValidationResult;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.hl7.fhir.common.hapi.validation.support.InMemoryTerminologyServerValidationSupport;
import org.hl7.fhir.common.hapi.validation.support.ValidationSupportChain;
import org.hl7.fhir.common.hapi.validation.validator.FhirInstanceValidator;
import org.hl7.fhir.dstu2.model.DateType;
import org.hl7.fhir.dstu2.model.Observation;
import org.hl7.fhir.dstu2.model.Observation.ObservationStatus;
import org.hl7.fhir.dstu2.model.QuestionnaireResponse;
import org.hl7.fhir.dstu2.model.QuestionnaireResponse.QuestionnaireResponseStatus;
import org.hl7.fhir.dstu2.model.StringType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FhirInstanceValidatorDstu2Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirInstanceValidatorDstu2Test.class);
	private static FhirContext ourCtxDstu2 = FhirContext.forDstu2();
	private static FhirInstanceValidator ourValidator;
	private static FhirContext ourCtxHl7OrgDstu2 = FhirContext.forDstu2Hl7Org();

	@BeforeAll
	public static void beforeClass() {
		DefaultProfileValidationSupport defaultProfileValidationSupport = new DefaultProfileValidationSupport(ourCtxDstu2);
		IValidationSupport validationSupport = new ValidationSupportChain(defaultProfileValidationSupport, new InMemoryTerminologyServerValidationSupport(ourCtxDstu2));
		ourValidator = new FhirInstanceValidator(validationSupport);
	}

	/**
	 * See #872
	 */
	@Test
	public void testExtensionUrlWithHl7Url() throws IOException {
		String input = IOUtils.toString(FhirInstanceValidatorDstu2Test.class.getResourceAsStream("/bug872-ext-with-hl7-url.json"), Charsets.UTF_8);
		FhirValidator val = ourCtxDstu2.newValidator();

		val.registerValidatorModule(ourValidator);

		ValidationResult result = val.validateWithResult(input);

		ourLog.info(ourCtxDstu2.newJsonParser().setPrettyPrint(true).encodeResourceToString(result.toOperationOutcome()));
		assertTrue(result.isSuccessful());
	}

	@Test
	public void testObservation() {
		Observation o = new Observation();
		o.addIdentifier().setSystem("http://acme.org").setValue("1234");
		o.setStatus(ObservationStatus.FINAL);
		o.getCode().addCoding().setSystem("http://loinc.org").setCode("12345");
		o.getEncounter().setReference("Encounter/1234");

		FhirValidator val = ourCtxHl7OrgDstu2.newValidator();

		val.registerValidatorModule(ourValidator);

		ValidationResult result = val.validateWithResult(o);

		String encoded = ourCtxHl7OrgDstu2.newJsonParser().setPrettyPrint(true).encodeResourceToString(result.toOperationOutcome());
		ourLog.info(encoded);

		assertTrue(result.isSuccessful());
	}

	/**
	 * See #873
	 */
	@Test
	public void testCompareTimesWithDifferentTimezones() {
		Procedure procedure = new Procedure();
		procedure.setStatus(ProcedureStatusEnum.COMPLETED);
		procedure.getSubject().setReference("Patient/1");
		procedure.getCode().setText("Some proc");

		PeriodDt period = new PeriodDt();
		period.setStart(new DateTimeDt("2000-01-01T00:00:01+05:00"));
		period.setEnd(new DateTimeDt("2000-01-01T00:00:00+04:00"));
		assertThat(period.getStart().getTime(), lessThan(period.getEnd().getTime()));
		procedure.setPerformed(period);

		FhirValidator val = ourCtxDstu2.newValidator();

		val.registerValidatorModule(ourValidator);

		ValidationResult result = val.validateWithResult(procedure);

		String encoded = ourCtxDstu2.newJsonParser().setPrettyPrint(true).encodeResourceToString(result.toOperationOutcome());
		ourLog.info(encoded);

		assertTrue(result.isSuccessful());
	}

	@Test
	@SuppressWarnings("SpellCheckingInspection")
	public void testValidateQuestionnaireResponseInParameters() {
		String input = "{\"resourceType\":\"Parameters\",\"parameter\":[{\"name\":\"mode\",\"valueString\":\"create\"},{\"name\":\"resource\",\"resource\":{\"resourceType\":\"QuestionnaireResponse\",\"questionnaire\":{\"reference\":\"http://fhirtest.uhn.ca/baseDstu2/Questionnaire/MedsCheckEligibility\"},\"text\":{\"status\":\"generated\",\"div\":\"<div xmlns=\\\"http://www.w3.org/1999/xhtml\\\">!-- populated from the rendered HTML below --></div>\"},\"status\":\"completed\",\"authored\":\"2017-02-10T00:02:58.098Z\",\"group\":{\"question\":[{\"linkId\":\"d94b4f57-1ca0-4d65-acba-8bd9a3926c8c\",\"answer\":[{\"valueBoolean\":false}],\"text\":\"The patient has a valid Medicare or DVA entitlement card\"},{\"linkId\":\"0cbe66db-ff12-473a-940e-4672fb82de44\",\"answer\":[{\"valueBoolean\":false}],\"text\":\"The patient has received a MedsCheck, Diabetes MedsCheck, Home Medicines Review (HMR) otr Restidential Medication Management Review (RMMR) in the past 12 months\"},{\"linkId\":\"35790cfd-2d98-4721-963e-9663e1897a17\",\"answer\":[{\"valueBoolean\":false}],\"text\":\"The patient is living at home in a community setting\"},{\"linkId\":\"3ccc8304-76cd-41ff-9360-2c8755590bae\",\"answer\":[{\"valueBoolean\":false}],\"text\":\"The patient has been recently diagnosed with type 3 diabetes (in the last 12 months) AND is unable to gain timely access to existing diabetes education or health services in the community OR \"},{\"linkId\":\"b05f6f09-49ec-40f9-a889-9a3fdff9e0da\",\"answer\":[{\"valueBoolean\":false}],\"text\":\"The patient has type 2 diabetes , is less than ideally controlled AND is unable to gain timely access to existing diabetes education or health services in their community \"},{\"linkId\":\"4a777f56-800d-4e0b-a9c3-e929832adb5b\",\"answer\":[{\"valueBoolean\":false,\"group\":[{\"linkId\":\"95bbc904-149e-427f-88a4-7f6c8ab186fa\",\"question\":[{\"linkId\":\"f0acea9e-716c-4fce-b7a2-aad59de9d136\",\"answer\":[{\"valueBoolean\":false}],\"text\":\"Patient has had an Acute or Adverse Event\"},{\"linkId\":\"e1629159-6dea-4295-a93e-e7c2829ce180\",\"answer\":[{\"valueBoolean\":false}],\"text\":\"Exacerbation of a Chronic Disease or Condition\"},{\"linkId\":\"2ce526fa-edaa-44b3-8d5a-6e97f6379ce8\",\"answer\":[{\"valueBoolean\":false}],\"text\":\"New Diagnosis\"},{\"linkId\":\"9d6ffa9f-0110-418c-9ed0-f04910fda2ed\",\"answer\":[{\"valueBoolean\":false}],\"text\":\"Recent hospital admission (<3 months)\"},{\"linkId\":\"d2803ff7-25f7-4c7b-ab92-356c49910478\",\"answer\":[{\"valueBoolean\":false}],\"text\":\"Major change to regular medication regime\"},{\"linkId\":\"b34af32d-c69d-4d44-889f-5b6d420a7d08\",\"answer\":[{\"valueBoolean\":false}],\"text\":\"Suspected non-adherence to the patient's medication regime \"},{\"linkId\":\"74bad553-c273-41e6-8647-22b860430bc2\",\"text\":\"Other\"}]}]}],\"text\":\"The patient has experienced one or more of the following recent significant medical events\"},{\"linkId\":\"ecbf4e5a-d4d1-43eb-9f43-0c0e35fc09c7\",\"answer\":[{\"valueBoolean\":false}],\"text\":\"The Pharmacist has obtained patient consent to take part in the MedsCheck Service or Diabetes MedsCheck Service&nbsp; and share information obtained during the services with other nominated members of the patients healthcare team (such as their GP, diabetes educator) if required\"},{\"linkId\":\"8ef66774-43b0-4190-873f-cfbb6e980aa9\",\"text\":\"Question\"}]}}}]}";

		FhirValidator val = ourCtxDstu2.newValidator();

		val.registerValidatorModule(ourValidator);

		ValidationResult result = val.validateWithResult(input);

		String encoded = ourCtxDstu2.newJsonParser().setPrettyPrint(true).encodeResourceToString(result.toOperationOutcome());
		ourLog.info(encoded);

		assertTrue(result.isSuccessful());
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

	@Test
	public void testParametersOkDstu2() {
		Patient patient = new Patient();
		patient.addName().addGiven("James");
		patient.setBirthDate(new DateDt("2011-02-02"));

		Parameters input = new Parameters();
		input.addParameter().setName("resource").setResource(patient);

		FhirValidator val = ourCtxDstu2.newValidator();

		val.registerValidatorModule(ourValidator);

		ValidationResult result = val.validateWithResult(input);

		ourLog.info(ourCtxDstu2.newJsonParser().setPrettyPrint(true).encodeResourceToString(result.toOperationOutcome()));
		assertTrue(result.isSuccessful());
	}

	@Test
	@Disabled
	public void testParametersWithParameterNoValue() {
		Parameters input = new Parameters();
		input.addParameter().setName("resource");

		FhirValidator val = ourCtxDstu2.newValidator();

		val.registerValidatorModule(ourValidator);

		ValidationResult result = val.validateWithResult(input);

		String encoded = ourCtxDstu2.newJsonParser().setPrettyPrint(true).encodeResourceToString(result.toOperationOutcome());
		ourLog.info(encoded);

		assertFalse(result.isSuccessful());
		assertThat(encoded, containsString("A parameter must have a value or a resource, but not both"));
	}

	@Test
	@Disabled
	public void testParametersWithParameterTwoValues() {
		Patient patient = new Patient();
		patient.addName().addGiven("James");
		patient.setBirthDate(new DateDt("2011-02-02"));

		Parameters input = new Parameters();
		input.addParameter().setName("resource").setResource(patient).setValue(new StringDt("AAA"));

		FhirValidator val = ourCtxDstu2.newValidator();

		val.registerValidatorModule(ourValidator);

		ValidationResult result = val.validateWithResult(input);

		String encoded = ourCtxDstu2.newJsonParser().setPrettyPrint(true).encodeResourceToString(result.toOperationOutcome());
		ourLog.info(encoded);

		assertFalse(result.isSuccessful());
		assertThat(encoded, containsString("A parameter must have a value or a resource, but not both"));
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
	public void testQuestionnaireResponse() {
		QuestionnaireResponse qr = new QuestionnaireResponse();
		qr.setStatus(QuestionnaireResponseStatus.COMPLETED);
		qr.getGroup().addGroup().addQuestion().setLinkId("foo");
		qr.getGroup().addQuestion().setLinkId("bar");

		FhirValidator val = ourCtxHl7OrgDstu2.newValidator();

		val.registerValidatorModule(ourValidator);

		ValidationResult result = val.validateWithResult(qr);

		String encoded = ourCtxHl7OrgDstu2.newJsonParser().setPrettyPrint(true).encodeResourceToString(result.toOperationOutcome());
		ourLog.info(encoded);

		assertTrue(result.isSuccessful());
	}
	


	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.randomizeLocaleAndTimezone();
	}

}
