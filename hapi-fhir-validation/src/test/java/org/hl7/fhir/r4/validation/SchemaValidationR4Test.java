package org.hl7.fhir.r4.validation;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.util.TestUtil;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.ValidationResult;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.TransformerFactory;
import javax.xml.xpath.XPathFactory;

import java.security.CodeSource;
import java.text.MessageFormat;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class SchemaValidationR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(SchemaValidationR4Test.class);
	private static FhirContext ourCtx = FhirContext.forDstu3();

	@BeforeEach
	public void before() {
		ourLog.info(getJaxpImplementationInfo("DocumentBuilderFactory", DocumentBuilderFactory.newInstance().getClass()));
		ourLog.info(getJaxpImplementationInfo("XPathFactory", XPathFactory.newInstance().getClass()));
		ourLog.info(getJaxpImplementationInfo("TransformerFactory", TransformerFactory.newInstance().getClass()));
		ourLog.info(getJaxpImplementationInfo("SAXParserFactory", SAXParserFactory.newInstance().getClass()));

		// The following code can be used to force the built in schema parser to be used
		//		System.setProperty("jaxp.debug", "1");
		//		System.setProperty("javax.xml.validation.SchemaFactory:http://www.w3.org/2001/XMLSchema", "com.sun.org.apache.xerces.internal.jaxp.validation.XMLSchemaFactory");
	}

	/**
	 * See #339
	 * <p>
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

		/*
		 * If this starts failing, check if xerces (or another simiar library) has slipped in
		 * to the classpath as a dependency. The logs in the @Before method should include this:
		 * DocumentBuilderFactory implementation: com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl loaded from: Java Runtime
		 */

		assertFalse(result.isSuccessful());
		assertThat(encoded, containsString("passwd"));
		assertThat(encoded, containsString("accessExternalDTD"));
	}

	private static String getJaxpImplementationInfo(String componentName, Class componentClass) {
		CodeSource source = componentClass.getProtectionDomain().getCodeSource();
		return MessageFormat.format(
			"{0} implementation: {1} loaded from: {2}",
			componentName,
			componentClass.getName(),
			source == null ? "Java Runtime" : source.getLocation());
	}

	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.randomizeLocaleAndTimezone();
	}
}
