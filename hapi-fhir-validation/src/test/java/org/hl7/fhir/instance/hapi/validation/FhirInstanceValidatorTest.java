package org.hl7.fhir.instance.hapi.validation;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;

import org.hl7.fhir.instance.model.*;
import org.hl7.fhir.instance.model.Observation.ObservationStatus;
import org.hl7.fhir.instance.model.QuestionnaireResponse.QuestionnaireResponseStatus;
import org.junit.*;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.Parameters;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.DateDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.util.TestUtil;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ValidationResult;

public class FhirInstanceValidatorTest {

	private static FhirInstanceValidator ourValidator = new FhirInstanceValidator(new DefaultProfileValidationSupport());
	private static FhirContext ourCtxDstu2 = FhirContext.forDstu2();
	private static FhirContext ourCtxHl7OrgDstu2 = FhirContext.forDstu2Hl7Org();
	
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
	@Ignore
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

	/*
	 * {
  "resourceType": "Observation",
  "meta": {
    "profile": [
      "http://example.com/foo/bar/testValidateResourceContainingProfileDeclarationJson"
    ]
  },
  "identifier": [
    {
      "system": "http://acme",
      "value": "12345"
    }
  ],
  "status": "final",
  "code": {
    "coding": [
      {
        "system": "http://loinc.org",
        "code": "12345"
      }
    ]
  },
  "encounter": {
    "reference": "http://foo.com/Encounter/9"
  }
}
	 */
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

	@Test
	public void testParametersWithTwoParameters() {
		org.hl7.fhir.instance.model.Patient patient = new org.hl7.fhir.instance.model.Patient();
		patient.addName().addGiven("James");
		patient.setBirthDateElement(new DateType("2011-02-02"));

		org.hl7.fhir.instance.model.Parameters input = new org.hl7.fhir.instance.model.Parameters();
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
	@Ignore
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
	public void testParametersHl7OrgDstu2() {
		org.hl7.fhir.instance.model.Patient patient = new org.hl7.fhir.instance.model.Patient();
		patient.addName().addGiven("James");
		patient.setBirthDateElement(new DateType("2011-02-02"));

		org.hl7.fhir.instance.model.Parameters input = new org.hl7.fhir.instance.model.Parameters();
		input.addParameter().setName("resource").setResource(patient);

		FhirValidator val = ourCtxHl7OrgDstu2.newValidator();
		
		val.registerValidatorModule(ourValidator);
		
		ValidationResult result = val.validateWithResult(input);
		
		ourLog.info(ourCtxHl7OrgDstu2.newJsonParser().setPrettyPrint(true).encodeResourceToString(result.toOperationOutcome()));
		assertTrue(result.isSuccessful());
	}

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirInstanceValidatorTest.class);
	
	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
