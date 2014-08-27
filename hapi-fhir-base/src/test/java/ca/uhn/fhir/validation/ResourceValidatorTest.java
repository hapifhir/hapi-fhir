package ca.uhn.fhir.validation;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.valueset.ContactSystemEnum;

public class ResourceValidatorTest {

	private static FhirContext ourCtx = new FhirContext();
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResourceValidatorTest.class);

	@Test
	public void testSchemaValidator() throws IOException {
		String res = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("patient-example-dicom.xml"));
		Patient p = ourCtx.newXmlParser().parseResource(Patient.class, res);

		FhirValidator val = ourCtx.newValidator();
		val.setValidateBaseSchema(true);
		val.setValidateBaseSchematron(false);
		
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
	public void testSchematronValidator() throws IOException {
//		System.setProperty("javax.xml.transform.TransformerFactory", "org.apache.xalan.processor.TransformerFactoryImpl ");
//		System.setProperty("javax.xml.transform.TransformerFactory", "net.sf.saxon.TransformerFactoryImpl");
		
		String res = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("patient-example-dicom.xml"));
		Patient p = ourCtx.newXmlParser().parseResource(Patient.class, res);

		FhirValidator val = ourCtx.newValidator();
		val.setValidateBaseSchema(false);
		val.setValidateBaseSchematron(true);

		val.validate(p);

		p.getTelecomFirstRep().setValue("123-4567");
		try {
			val.validate(p);
			fail();
		} catch (ValidationFailureException e) {
			ourLog.info(ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(e.getOperationOutcome()));
			assertEquals(1, e.getOperationOutcome().getIssue().size());
			assertThat(e.getOperationOutcome().getIssueFirstRep().getDetails().getValue(), containsString("Inv-2: A system is required if a value is provided."));
		}
		
		p.getTelecomFirstRep().setSystem(ContactSystemEnum.EMAIL);
		val.validate(p);
	}
	
}
