package ca.uhn.fhir.util;

import static org.junit.Assert.fail;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;

import org.hl7.fhir.dstu3.model.Patient;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.parser.DataFormatException;

public class XmlUtilDstu3Test {

	private static FhirContext ourCtx = FhirContext.forDstu3();
	private Patient myPatient;
	
	@After
	public void after() {
		XmlUtil.setThrowExceptionForUnitTest(null);
	}
	
	@Before
	public void before() {
		myPatient = new Patient();
		myPatient.setId("1");
	}
	
	@Test
	public void testParseMalformed() {
		try {
			ourCtx.newXmlParser().parseResource("AAAAA");
			fail();
		} catch (DataFormatException e) {
			// good
		}
	}
	
	public void testXmlFactoryThrowsXmlStreamException() {
		XmlUtil.setThrowExceptionForUnitTest(new XMLStreamException("FOO"));
		
		try {
			ourCtx.newXmlParser().parseResource("AAAAA");
			fail();
		} catch (DataFormatException e) {
			// good
		}
		try {
			ourCtx.newXmlParser().encodeResourceToString(myPatient);
			fail();
		} catch (ConfigurationException e) {
			// good
		}
		try {
			ourCtx.newXmlParser().encodeBundleToString(new Bundle());
			fail();
		} catch (ConfigurationException e) {
			// good
		}
	}
	
	public void testXmlFactoryThrowsFactoryConfigurationError() {
		XmlUtil.setThrowExceptionForUnitTest(new FactoryConfigurationError("FOO"));
		
		try {
			ourCtx.newXmlParser().parseResource("AAAAA");
			fail();
		} catch (DataFormatException e) {
			// good
		}
		try {
			ourCtx.newXmlParser().encodeResourceToString(myPatient);
			fail();
		} catch (ConfigurationException e) {
			// good
		}
	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}
	
}
