package ca.uhn.fhir.util;

import java.io.StringReader;
import java.io.StringWriter;

import org.junit.AfterClass;
import org.junit.Test;

public class XmlUtilTest {

	
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
