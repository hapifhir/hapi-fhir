package org.hl7.fhir.dstu3.hapi.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.util.TestUtil;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ValidationResult;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class SchemaValidationDstu3Test {

	static {

	}

	private static FhirContext ourCtx = FhirContext.forDstu3();

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SchemaValidationDstu3Test.class);

	/**
	 * See #339
	 * 
	 * https://www.owasp.org/index.php/XML_External_Entity_(XXE)_Processing
	 */
	@Test
	public void testXxe() {
		//@formatter:off
		String input =
			"<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n" + 
			"<!DOCTYPE foo [  \n" + 
			"<!ELEMENT foo ANY >\n" + 
			"<!ENTITY xxe SYSTEM \"file:///etc/passwd\" >]>" +
			"<Patient xmlns=\"http://hl7.org/fhir\">" +
				"<text>" + 
					"<status value=\"generated\"/>" +
					"<div xmlns=\"http://www.w3.org/1999/xhtml\">TEXT &xxe; TEXT</div>\n" + 
				"</text>" +
				"<address>" + 
					"<line value=\"FOO\"/>" + 
				"</address>" +
			"</Patient>";
		//@formatter:on

		FhirValidator val = ourCtx.newValidator();
		val.setValidateAgainstStandardSchema(true);
		val.setValidateAgainstStandardSchematron(false);
		ValidationResult result = val.validateWithResult(input);

		String encoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(result.toOperationOutcome());
		ourLog.info(encoded);

		assertFalse(result.isSuccessful());
		assertThat(encoded, containsString("passwd"));
		assertThat(encoded, containsString("accessExternalDTD"));
	}

	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.randomizeLocaleAndTimezone();
	}
}
