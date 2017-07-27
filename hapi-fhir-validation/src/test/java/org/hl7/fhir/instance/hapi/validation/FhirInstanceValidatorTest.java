package org.hl7.fhir.instance.hapi.validation;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.Parameters;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.DateDt;
import ca.uhn.fhir.util.TestUtil;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ValidationResult;

public class FhirInstanceValidatorTest {

	private static FhirContext ourCtx = FhirContext.forDstu2();
	
	@Test
	public void testParameters() {
		Patient patient = new Patient();
		patient.addName().addGiven("James");
		patient.setBirthDate(new DateDt("2011-02-02"));

		Parameters input = new Parameters();
		input.addParameter().setName("resource").setResource(patient);

		FhirValidator val = ourCtx.newValidator();
		
		val.registerValidatorModule(new FhirInstanceValidator(new DefaultProfileValidationSupport()));
		
		ValidationResult result = val.validateWithResult(input);
		
		ourLog.info(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(result.toOperationOutcome()));
		assertTrue(result.isSuccessful());
	}
	
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirInstanceValidatorTest.class);
	
	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
