package ca.uhn.fhir.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.dstu.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.valueset.ContactSystemEnum;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ResourceValidatorTest {

	private static FhirContext ourCtx = new FhirContext();
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
			ourLog.info(ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(e.getOperationOutcome()));
			assertEquals(1, e.getOperationOutcome().getIssue().size());
			assertThat(e.getOperationOutcome().getIssueFirstRep().getDetails().getValue(), containsString("Invalid content was found starting with element 'breed'"));
		}
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
			ourLog.info(ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(e.getOperationOutcome()));
			assertEquals(1, e.getOperationOutcome().getIssue().size());
			assertThat(e.getOperationOutcome().getIssueFirstRep().getDetails().getValue(), containsString("Inv-2: A system is required if a value is provided."));
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
        OperationOutcome operationOutcome = validationResult.getOperationOutcome();
        ourLog.info(ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(operationOutcome));
        assertEquals(1, operationOutcome.getIssue().size());
        assertThat(operationOutcome.getIssueFirstRep().getDetails().getValue(), containsString("Inv-2: A system is required if a value is provided."));

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
        assertTrue(result.isSuccessful());
        OperationOutcome operationOutcome = result.getOperationOutcome();
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
        OperationOutcome operationOutcome = validationResult.getOperationOutcome();
        ourLog.info(ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(operationOutcome));
        assertEquals(1, operationOutcome.getIssue().size());
        assertThat(operationOutcome.getIssueFirstRep().getDetails().getValue(), containsString("Inv-2: A system is required if a value is provided."));
    }

    private FhirValidator createFhirValidator() {
        FhirValidator val = ourCtx.newValidator();
        val.setValidateAgainstStandardSchema(true);
        val.setValidateAgainstStandardSchematron(true);
        return val;
    }
}
