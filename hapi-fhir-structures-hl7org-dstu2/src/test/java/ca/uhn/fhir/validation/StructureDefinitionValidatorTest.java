package ca.uhn.fhir.validation;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.List;

import org.hl7.fhir.instance.model.Patient;
import org.hl7.fhir.instance.validation.ValidationMessage;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.EncodingEnum;

public class StructureDefinitionValidatorTest {

	private static FhirContext ourCtx = FhirContext.forDstu2Hl7Org();
	
	@Test
	public void testValidateJsonResource() {
		String input = "{"
				+ "\"resourceType\":\"Patient\","
				+ "\"id\":\"123\""
				+ "}";
		
		StructureDefinitionValidator val = new StructureDefinitionValidator(ourCtx);
		List<ValidationMessage> output = val.validate(input, EncodingEnum.JSON, Patient.class);
		assertEquals(output.toString(), 0, output.size());
	}

	@Test
	public void testValidateJsonResourceBadAttributes() {
		String input = "{"
				+ "\"resourceType\":\"Patient\","
				+ "\"id\":\"123\","
				+ "\"foo\":\"123\""
				+ "}";
		
		
		StructureDefinitionValidator val = new StructureDefinitionValidator(ourCtx);
		List<ValidationMessage> output = val.validate(input, EncodingEnum.JSON, Patient.class);
		assertEquals(output.toString(), 1, output.size());
		assertThat(output.get(0).toXML(), stringContainsInOrder("/foo", "Element is unknown"));
	}

	@Test
	public void testValidateXmlResource() {
		String input = "<Patient xmlns=\"http://hl7.org/fhir\">"
				+ "<id value=\"123\"/>"
				+ "</Patient>";
		
		StructureDefinitionValidator val = new StructureDefinitionValidator(ourCtx);
		List<ValidationMessage> output = val.validate(input, EncodingEnum.XML, Patient.class);
		assertEquals(output.toString(), 0, output.size());
	}


	@Test
	public void testValidateXmlResourceBadAttributes() {
		String input = "<Patient xmlns=\"http://hl7.org/fhir\">"
				+ "<id value=\"123\"/>"
				+ "<foo value=\"222\"/>"
				+ "</Patient>";
		
		StructureDefinitionValidator val = new StructureDefinitionValidator(ourCtx);
		List<ValidationMessage> output = val.validate(input, EncodingEnum.XML, Patient.class);
		assertEquals(output.toString(), 1, output.size());
		assertThat(output.get(0).toXML(), stringContainsInOrder("/f:Patient/f:foo", "Element is unknown"));
	}
}
