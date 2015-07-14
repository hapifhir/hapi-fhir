package ca.uhn.fhir.validation;

import static org.hamcrest.Matchers.stringContainsInOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.hl7.fhir.instance.validation.ValidationMessage;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.EncodingEnum;

public class FhirInstanceValidatorIntegrationTest {

	private static FhirContext ourCtx = FhirContext.forDstu2Hl7Org();
	
	@Test
	public void testValidateJsonResource() {
		String input = "{"
				+ "\"resourceType\":\"Patient\","
				+ "\"id\":\"123\""
				+ "}";
		
		FhirInstanceValidator val = new FhirInstanceValidator();
		List<ValidationMessage> output = val.validate(ourCtx, input, EncodingEnum.JSON);
		assertEquals(output.toString(), 0, output.size());
	}

	@Test
	public void testValidateJsonResourceBadAttributes() {
		String input = "{"
				+ "\"resourceType\":\"Patient\","
				+ "\"id\":\"123\","
				+ "\"foo\":\"123\""
				+ "}";
		
		
		FhirInstanceValidator val = new FhirInstanceValidator();
		List<ValidationMessage> output = val.validate(ourCtx, input, EncodingEnum.JSON);
		assertEquals(output.toString(), 1, output.size());
		assertThat(output.get(0).toXML(), stringContainsInOrder("/foo", "Element is unknown"));
	}

	@Test
	public void testValidateXmlResource() {
		String input = "<Patient xmlns=\"http://hl7.org/fhir\">"
				+ "<id value=\"123\"/>"
				+ "</Patient>";
		
		FhirInstanceValidator val = new FhirInstanceValidator();
		List<ValidationMessage> output = val.validate(ourCtx, input, EncodingEnum.XML);
		assertEquals(output.toString(), 0, output.size());
	}


	@Test
	public void testValidateXmlResourceBadAttributes() {
		String input = "<Patient xmlns=\"http://hl7.org/fhir\">"
				+ "<id value=\"123\"/>"
				+ "<foo value=\"222\"/>"
				+ "</Patient>";
		
		FhirInstanceValidator val = new FhirInstanceValidator();
		List<ValidationMessage> output = val.validate(ourCtx, input, EncodingEnum.XML);
		assertEquals(output.toString(), 1, output.size());
		assertThat(output.get(0).toXML(), stringContainsInOrder("/f:Patient/f:foo", "Element is unknown"));
	}
}
