package ca.uhn.fhir.util;

import static org.junit.Assert.*;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.stream.FactoryConfigurationError;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;

public class XmlUtilTest {

	@Test
	public void testCreateInputFactoryWithException() {
		XmlUtil.setThrowExceptionForUnitTest(new Error("FOO ERROR"));
		try {
			XmlUtil.newInputFactory();
			fail();
		} catch (Exception e) {
			assertEquals("Unable to initialize StAX - XML processing is disabled", e.getMessage());
		}
	}

	@Test
	public void testCreateOutputFactoryWithException() {
		XmlUtil.setThrowExceptionForUnitTest(new Error("FOO ERROR"));
		try {
			XmlUtil.newOutputFactory();
			fail();
		} catch (Exception e) {
			assertEquals("Unable to initialize StAX - XML processing is disabled", e.getMessage());
		}
	}

	@After
	public void after() {
		XmlUtil.setThrowExceptionForUnitTest(null);
	}

	@Test
	public void testCreateReader() throws Exception {
		XmlUtil.createXmlReader(new StringReader("<a/>"));
	}

	@Test
	public void testCreateWriter() throws Exception {
		XmlUtil.createXmlWriter(new StringWriter());
	}

	@Test
	public void testCreateStreamWriter() throws Exception {
		XmlUtil.createXmlStreamWriter(new StringWriter());
	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
