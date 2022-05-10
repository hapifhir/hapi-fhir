package ca.uhn.fhir.util;

import ca.uhn.fhir.i18n.Msg;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class XmlUtilTest {

	@Test
	public void testCreateInputFactoryWithException() {
		XmlUtil.setThrowExceptionForUnitTest(new Error("FOO ERROR"));
		try {
			XmlUtil.newInputFactory();
			fail();
		} catch (Exception e) {
			assertEquals(Msg.code(1753) + "Unable to initialize StAX - XML processing is disabled", e.getMessage());
		}
	}

	@Test
	public void testCreateOutputFactoryWithException() {
		XmlUtil.setThrowExceptionForUnitTest(new Error("FOO ERROR"));
		try {
			XmlUtil.newOutputFactory();
			fail();
		} catch (Exception e) {
			assertEquals(Msg.code(1754) + "Unable to initialize StAX - XML processing is disabled", e.getMessage());
		}
	}

	@AfterEach
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

	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.randomizeLocaleAndTimezone();
	}

}
