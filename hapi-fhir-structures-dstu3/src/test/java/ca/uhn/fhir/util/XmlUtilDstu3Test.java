package ca.uhn.fhir.util;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;

import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.utilities.xml.XMLUtil;
import org.junit.*;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.DataFormatException;
import org.xml.sax.SAXException;

import java.io.IOException;

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

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}
	
}
