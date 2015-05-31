package ca.uhn.fhir.validation;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.List;

import org.hl7.fhir.instance.model.Patient;
import org.hl7.fhir.instance.validation.ValidationMessage;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;

public class InstanceValidatorTest {

	private static FhirContext ourCtx = FhirContext.forDstu2Hl7Org();
	
	@Test
	public void testValidateXmlResource() {
		String input = "<Patient xmlns=\"http://hl7.org/fhir\">"
				+ "<id value=\"123\"/>"
				+ "</Patient>";
		
		InstanceValidator val = new InstanceValidator(ourCtx);
		List<ValidationMessage> output = val.validate(input, Patient.class);
		assertEquals(output.toString(), 0, output.size());
	}
	
	@Test
	public void testValidateXmlResourceBadAttributes() {
		String input = "<Patient xmlns=\"http://hl7.org/fhir\">"
				+ "<id value=\"123\"/>"
				+ "<foo value=\"222\"/>"
				+ "</Patient>";
		
		InstanceValidator val = new InstanceValidator(ourCtx);
		List<ValidationMessage> output = val.validate(input, Patient.class);
		assertEquals(output.toString(), 1, output.size());
		assertThat(output.toString(), containsString("/f:Patient/f:foo Element is unknown"));
	}

}
