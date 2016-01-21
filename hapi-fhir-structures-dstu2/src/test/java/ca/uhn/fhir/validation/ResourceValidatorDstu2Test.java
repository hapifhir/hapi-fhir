package ca.uhn.fhir.validation;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.text.SimpleDateFormat;

import org.apache.commons.io.IOUtils;
import org.hamcrest.core.StringContains;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.dstu2.composite.TimingDt;
import ca.uhn.fhir.model.dstu2.resource.AllergyIntolerance;
import ca.uhn.fhir.model.dstu2.resource.MedicationOrder;
import ca.uhn.fhir.model.dstu2.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.valueset.ContactPointSystemEnum;
import ca.uhn.fhir.model.dstu2.valueset.UnitsOfTimeEnum;
import ca.uhn.fhir.model.primitive.DateDt;

public class ResourceValidatorDstu2Test {

	private static FhirContext ourCtx = FhirContext.forDstu2();
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResourceValidatorDstu2Test.class);

	private FhirValidator createFhirValidator() {
		
		AllergyIntolerance allergy = new AllergyIntolerance();
		allergy.getSubstance().addCoding().setCode("some substance");
		
		FhirValidator val = ourCtx.newValidator();
		val.setValidateAgainstStandardSchema(true);
		val.setValidateAgainstStandardSchematron(true);
		return val;
	}

	/**
	 * See issue #50
	 */
	@Test
	public void testOutOfBoundsDate() {
		Patient p = new Patient();
		p.setBirthDate(new DateDt("2000-15-31"));

		String encoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(p);
		ourLog.info(encoded);

		assertThat(encoded, StringContains.containsString("2000-15-31"));

		p = ourCtx.newXmlParser().parseResource(Patient.class, encoded);
		assertEquals("2000-15-31", p.getBirthDateElement().getValueAsString());
		assertEquals("2001-03-31", new SimpleDateFormat("yyyy-MM-dd").format(p.getBirthDate()));

		ValidationResult result = ourCtx.newValidator().validateWithResult(p);
		String resultString = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(result.toOperationOutcome());
		ourLog.info(resultString);

		assertEquals(2, ((OperationOutcome)result.toOperationOutcome()).getIssue().size());
		assertThat(resultString, StringContains.containsString("2000-15-31"));
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testSchemaBundleValidator() throws IOException {
		String res = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("bundle-example.json"));
		Bundle b = ourCtx.newJsonParser().parseBundle(res);

		FhirValidator val = createFhirValidator();

		val.validate(b);

		MedicationOrder p = (MedicationOrder) b.getEntries().get(0).getResource();
		TimingDt timing = new TimingDt();
		timing.getRepeat().setDuration(123);
		timing.getRepeat().setDurationUnits((UnitsOfTimeEnum)null);
		p.getDosageInstructionFirstRep().setTiming(timing);

		try {
			val.validate(b);
			fail();
		} catch (ValidationFailureException e) {
			String encoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(e.getOperationOutcome());
			ourLog.info(encoded);
			assertThat(encoded, containsString("tim-1:"));
		}
	}

	@Test
	public void testSchemaBundleValidatorFails() throws IOException {
		String res = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("bundle-example.json"));
		Bundle b = ourCtx.newJsonParser().parseBundle(res);

		FhirValidator val = createFhirValidator();

		ValidationResult validationResult = val.validateWithResult(b);
		assertTrue(validationResult.isSuccessful());

		MedicationOrder p = (MedicationOrder) b.getEntries().get(0).getResource();
		TimingDt timing = new TimingDt();
		timing.getRepeat().setDuration(123);
		timing.getRepeat().setDurationUnits((UnitsOfTimeEnum)null);
		p.getDosageInstructionFirstRep().setTiming(timing);
		
		validationResult = val.validateWithResult(b);
		assertFalse(validationResult.isSuccessful());
		OperationOutcome operationOutcome = (OperationOutcome) validationResult.toOperationOutcome();
		String encoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(operationOutcome);
		ourLog.info(encoded);
		assertThat(encoded, containsString("tim-1:"));
	}

	@Test
	public void testSchemaBundleValidatorIsSuccessful() throws IOException {
		String res = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("bundle-example.json"));
		Bundle b = ourCtx.newJsonParser().parseBundle(res);

		ourLog.info(ourCtx.newXmlParser().setPrettyPrint(true).encodeBundleToString(b));
		
		FhirValidator val = createFhirValidator();

		ValidationResult result = val.validateWithResult(b);

		OperationOutcome operationOutcome = (OperationOutcome) result.toOperationOutcome();
		
		assertTrue(result.toString(), result.isSuccessful());
		assertNotNull(operationOutcome);
		assertEquals(1, operationOutcome.getIssue().size());
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testSchemaResourceValidator() throws IOException {
		String res = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("patient-example-dicom.json"));
		Patient p = ourCtx.newJsonParser().parseResource(Patient.class, res);

		ourLog.info(ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(p));
		
		FhirValidator val = ourCtx.newValidator();
		val.setValidateAgainstStandardSchema(true);
		val.setValidateAgainstStandardSchematron(false);

		val.validate(p);

		p.getAnimal().getBreed().setText("The Breed");
		try {
			val.validate(p);
			fail();
		} catch (ValidationFailureException e) {
			OperationOutcome operationOutcome = (OperationOutcome) e.getOperationOutcome();
			ourLog.info(ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(operationOutcome));
			assertEquals(1, operationOutcome.getIssue().size());
			assertThat(operationOutcome.getIssueFirstRep().getDetailsElement().getValue(), containsString("cvc-complex-type"));
		}
	}

	@Test
	public void testSchematronResourceValidator() throws IOException {
		String res = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("patient-example-dicom.json"));
		Patient p = ourCtx.newJsonParser().parseResource(Patient.class, res);

		FhirValidator val = ourCtx.newValidator();
		val.setValidateAgainstStandardSchema(false);
		val.setValidateAgainstStandardSchematron(true);

		ValidationResult validationResult = val.validateWithResult(p);
		assertTrue(validationResult.isSuccessful());

		p.getTelecomFirstRep().setValue("123-4567");
		validationResult = val.validateWithResult(p);
		assertFalse(validationResult.isSuccessful());
		OperationOutcome operationOutcome = (OperationOutcome) validationResult.toOperationOutcome();
		ourLog.info(ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(operationOutcome));
		assertEquals(1, operationOutcome.getIssue().size());
		assertThat(operationOutcome.getIssueFirstRep().getDiagnostics(), containsString("cpt-2:"));

		p.getTelecomFirstRep().setSystem(ContactPointSystemEnum.EMAIL);
		validationResult = val.validateWithResult(p);
		assertTrue(validationResult.isSuccessful());
	}
}
