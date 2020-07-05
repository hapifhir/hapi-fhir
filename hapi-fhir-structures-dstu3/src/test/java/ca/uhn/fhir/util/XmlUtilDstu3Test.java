package ca.uhn.fhir.util;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.DataFormatException;
import org.hl7.fhir.dstu3.model.Patient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

public class XmlUtilDstu3Test {

	private static FhirContext ourCtx = FhirContext.forDstu3();
	private Patient myPatient;
	
	@AfterEach
	public void after() {
		XmlUtil.setThrowExceptionForUnitTest(null);
	}
	
	@BeforeEach
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

	@Test
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
	}

	@Test
	public void testXmlFactoryThrowsFactoryConfigurationError() {
		XmlUtil.setThrowExceptionForUnitTest(new FactoryConfigurationError("FOO"));
		
		try {
			ourCtx.newXmlParser().parseResource("AAAAA");
			fail();
		} catch (ConfigurationException e) {
			// good
		}
	}

	@Test
	public void testApplyUnsupportedFeature() throws IOException, SAXException {
		assertNotNull(XmlUtil.parseDocument("<document></document>"));

		XmlUtil.setThrowExceptionForUnitTest(new ParserConfigurationException("AA"));
		assertNotNull(XmlUtil.parseDocument("<document></document>"));
	}

	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}
	
}
