package ca.uhn.fhir.util;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.instance.model.api.IBaseDatatype;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.PrimitiveType;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExtensionUtilTest {

	private static final String EXT_URL = "http://magic.com/extensions";

	private static FhirContext ourFhirContext = FhirContext.forR4();

	@Test
	void testExtensionsWork() {
		Patient p1 = new Patient();
		assertFalse(ExtensionUtil.hasExtension(p1, EXT_URL));
		ExtensionUtil.setExtensionAsString(ourFhirContext, p1, EXT_URL, "value");
		assertTrue(ExtensionUtil.hasExtension(p1, EXT_URL));
	}

	@Test
	void testExtensionTypesWork() {
		Patient p1 = new Patient();
		assertFalse(ExtensionUtil.hasExtension(p1, EXT_URL));
		ExtensionUtil.setExtension(ourFhirContext, p1, EXT_URL, "integer", "1");

		assertTrue(ExtensionUtil.hasExtension(p1, EXT_URL));
		assertEquals(1, ExtensionUtil.getExtensionsByUrl(p1, EXT_URL).size());

		IBaseDatatype ext = ExtensionUtil.getExtensionByUrl(p1, EXT_URL).getValue();
		assertEquals("integer", ext.fhirType());
		assertEquals("1", ((PrimitiveType) ext).asStringValue());
	}

	@Test
	void testAddExtension() {
		Patient p = new Patient();
		assertNotNull(ExtensionUtil.addExtension(p));
		assertNotNull(ExtensionUtil.addExtension(p, "myUrl"));

		assertEquals(2, p.getExtension().size());
		assertEquals("myUrl", p.getExtension().get(1).getUrl());
	}

	@Test
	void testHasExtension() {
		Patient p = new Patient();
		p.addExtension("URL", new StringType("VALUE"));

		assertTrue(ExtensionUtil.hasExtension(p, "URL"));
		assertTrue(ExtensionUtil.hasExtension(p, "URL", "VALUE"));
	}

	@Test
	void testClearExtension() {
		Patient p = new Patient();
		p.addExtension("URL", new StringType("VALUE"));
		p.addExtension("URL2", new StringType("VALUE2"));

		ExtensionUtil.clearExtensionsByUrl(p, "URL");

		assertEquals(1, p.getExtension().size());
		assertFalse(ExtensionUtil.hasExtension(p, "URL"));
		assertTrue(ExtensionUtil.hasExtension(p, "URL2"));

		ExtensionUtil.clearAllExtensions(p);

		assertEquals(0, p.getExtension().size());
	}
}
