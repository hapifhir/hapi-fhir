package ca.uhn.fhir.validation;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.hl7.fhir.instance.model.Observation;
import org.hl7.fhir.instance.model.StringType;
import org.junit.Before;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;

public class FhirInstanceValidatorIntegrationTest {

	private static FhirContext ourCtx = FhirContext.forDstu2Hl7Org();
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirInstanceValidatorIntegrationTest.class);
	private FhirInstanceValidator myInstanceVal;
	private FhirValidator myVal;
	
	@Before
	public void before() {
		myVal = ourCtx.newValidator();
		myVal.setValidateAgainstStandardSchema(false);
		myVal.setValidateAgainstStandardSchematron(false);
		
		myInstanceVal = new FhirInstanceValidator();
		myVal.registerValidatorModule(myInstanceVal);
	}

	@Test
	public void testValidateJsonResource() {
		//@formatter:off
		String input = "{"
				+ "\"resourceType\":\"Patient\","
				+ "\"id\":\"123\""
				+ "}";
		//@formatter:on

		ValidationResult output = myVal.validateWithResult(input);
		assertEquals(output.toString(), 0, output.getMessages().size());
	}

	@Test
	public void testValidateResourceFailingInvariant() {
		Observation input = new Observation();
		
		// Has a value, but not a status (which is required)
		input.getCode().addCoding().setSystem("http://loinc.org").setCode("12345");
		input.setValue(new StringType("AAA"));
		
		ValidationResult output = myVal.validateWithResult(input);
		assertThat(output.getMessages().size(), greaterThan(0));
		assertEquals("Element '/f:Observation.status': minimum required = 1, but only found 0", output.getMessages().get(0).getMessage());
		
	}
	
	@Test
	public void testValidateJsonResourceBadAttributes() {
		//@formatter:off
		String input = "{"
				+ "\"resourceType\":\"Patient\","
				+ "\"id\":\"123\","
				+ "\"foo\":\"123\""
				+ "}";
		//@formatter:on

		ValidationResult output = myVal.validateWithResult(input);
		assertEquals(output.toString(), 1, output.getMessages().size());
		ourLog.info(output.getMessages().get(0).getLocationString());
		ourLog.info(output.getMessages().get(0).getMessage());
		assertEquals("/foo", output.getMessages().get(0).getLocationString());
		assertEquals("Element is unknown or does not match any slice", output.getMessages().get(0).getMessage());
	}


	@Test
	public void testValidateXmlResource() {
		//@formatter:off
		String input = "<Patient xmlns=\"http://hl7.org/fhir\">"
				+ "<id value=\"123\"/>"
				+ "</Patient>";
		//@formatter:on
		
		ValidationResult output = myVal.validateWithResult(input);
		assertEquals(output.toString(), 0, output.getMessages().size());
	}
	
	@Test
	public void testValidateXmlResourceBadAttributes() {
		//@formatter:off
		String input = "<Patient xmlns=\"http://hl7.org/fhir\">"
				+ "<id value=\"123\"/>"
				+ "<foo value=\"222\"/>"
				+ "</Patient>";
		//@formatter:on
		
		ValidationResult output = myVal.validateWithResult(input);
		assertEquals(output.toString(), 1, output.getMessages().size());
		ourLog.info(output.getMessages().get(0).getLocationString());
		ourLog.info(output.getMessages().get(0).getMessage());
		assertEquals("/f:Patient/f:foo", output.getMessages().get(0).getLocationString());
		assertEquals("Element is unknown or does not match any slice", output.getMessages().get(0).getMessage());
	}
}
