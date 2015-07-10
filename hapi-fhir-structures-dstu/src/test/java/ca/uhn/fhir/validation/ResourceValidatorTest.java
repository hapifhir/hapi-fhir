package ca.uhn.fhir.validation;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.apache.commons.io.IOUtils;
import org.hamcrest.core.StringContains;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.dstu.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.valueset.ContactSystemEnum;
import ca.uhn.fhir.model.primitive.DateTimeDt;

public class ResourceValidatorTest {

	private static FhirContext ourCtx = FhirContext.forDstu1();
	private static Locale ourDefaultLocale;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResourceValidatorTest.class);

	@Test
	public void testSchemaResourceValidator() throws IOException {
		String res = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("patient-example-dicom.xml"));
		Patient p = ourCtx.newXmlParser().parseResource(Patient.class, res);

		FhirValidator val = ourCtx.newValidator();
		val.setValidateAgainstStandardSchema(true);
		val.setValidateAgainstStandardSchematron(false);

		val.validate(p);

		p.getAnimal().getBreed().setText("The Breed");
		try {
			val.validate(p);
			fail();
		} catch (ValidationFailureException e) {
			OperationOutcome outcome = (OperationOutcome) e.getOperationOutcome();
			ourLog.info(ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(outcome));
			assertEquals(1, outcome.getIssue().size());
			assertThat(outcome.getIssueFirstRep().getDetailsElement().getValue(), containsString("cvc-complex-type.2.4.a"));
		}
	}
	
	@BeforeClass
	public static void beforeClass() {
		/*
		 * We cache the default locale, but temporarily set it to a random value during this test. This helps ensure that there are no
		 * language specific dependencies in the test. 
		 */
		ourDefaultLocale = Locale.getDefault();
		
		Locale newLocale = Locale.GERMANY;
		Locale.setDefault(newLocale);
		
		ourLog.info("Tests are running in locale: " + newLocale.getDisplayName());
	}
	
	public static void afterClass() {
		Locale.setDefault(ourDefaultLocale);
	}
	
	/**
	 * See issue #50
	 */
	@Test
	public void testOutOfBoundsDate() {
		Patient p = new Patient();
		p.setBirthDate(new DateTimeDt("2000-15-31"));

		String encoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(p);
		ourLog.info(encoded);

		assertThat(encoded, StringContains.containsString("2000-15-31"));

		p = ourCtx.newXmlParser().parseResource(Patient.class, encoded);
		assertEquals("2000-15-31", p.getBirthDate().getValueAsString());
		assertEquals("2001-03-31", new SimpleDateFormat("yyyy-MM-dd").format(p.getBirthDate().getValue()));

		ValidationResult result = ourCtx.newValidator().validateWithResult(p);
		String resultString = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(result.toOperationOutcome());
		ourLog.info(resultString);

		assertEquals(2, ((OperationOutcome)result.toOperationOutcome()).getIssue().size());
		assertThat(resultString, StringContains.containsString("cvc-datatype-valid.1.2.3"));
	}

	@Test
	public void testSchemaBundleValidator() throws IOException {
		String res = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("atom-document-large.xml"));
		Bundle b = ourCtx.newXmlParser().parseBundle(res);

		FhirValidator val = createFhirValidator();

		val.validate(b);

		Patient p = (Patient) b.getEntries().get(0).getResource();
		p.getTelecomFirstRep().setValue("123-4567");

		try {
			val.validate(b);
			fail();
		} catch (ValidationFailureException e) {
			OperationOutcome outcome = (OperationOutcome) e.getOperationOutcome();
			ourLog.info(ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(outcome));
			assertEquals(1, outcome.getIssue().size());
			assertThat(outcome.getIssueFirstRep().getDetailsElement().getValue(), containsString("Inv-2:"));
		}
	}

	@Test
	public void testSchematronResourceValidator() throws IOException {
		String res = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("patient-example-dicom.xml"));
		Patient p = ourCtx.newXmlParser().parseResource(Patient.class, res);

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
		assertThat(operationOutcome.getIssueFirstRep().getDetails().getValue(), containsString("Inv-2:"));

		p.getTelecomFirstRep().setSystem(ContactSystemEnum.EMAIL);
		validationResult = val.validateWithResult(p);
		assertTrue(validationResult.isSuccessful());
	}

	@Test
	public void testSchemaBundleValidatorIsSuccessful() throws IOException {
		String res = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("atom-document-large.xml"));
		Bundle b = ourCtx.newXmlParser().parseBundle(res);

		FhirValidator val = createFhirValidator();

		ValidationResult result = val.validateWithResult(b);
		OperationOutcome operationOutcome = (OperationOutcome) result.toOperationOutcome();
		ourLog.info(ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(operationOutcome));
		
		
		assertTrue(result.isSuccessful());
		assertNotNull(operationOutcome);
		assertEquals(0, operationOutcome.getIssue().size());
	}

	@Test
	public void testSchemaBundleValidatorFails() throws IOException {
		String res = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("atom-document-large.xml"));
		Bundle b = ourCtx.newXmlParser().parseBundle(res);

		FhirValidator val = createFhirValidator();

		ValidationResult validationResult = val.validateWithResult(b);
		assertTrue(validationResult.isSuccessful());

		Patient p = (Patient) b.getEntries().get(0).getResource();
		p.getTelecomFirstRep().setValue("123-4567");
		validationResult = val.validateWithResult(b);
		assertFalse(validationResult.isSuccessful());
		OperationOutcome operationOutcome = (OperationOutcome) validationResult.toOperationOutcome();
		ourLog.info(ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(operationOutcome));
		assertEquals(1, operationOutcome.getIssue().size());
		assertThat(operationOutcome.getIssueFirstRep().getDetails().getValue(), containsString("Inv-2:"));
	}

	private FhirValidator createFhirValidator() {
		FhirValidator val = ourCtx.newValidator();
		val.setValidateAgainstStandardSchema(true);
		val.setValidateAgainstStandardSchematron(true);
		return val;
	}
}
