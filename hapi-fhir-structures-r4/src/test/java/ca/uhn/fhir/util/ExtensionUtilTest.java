package ca.uhn.fhir.util;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.instance.model.api.IBaseDatatype;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;

class ExtensionUtilTest {

	private static final String EXT_URL = "http://magic.com/extensions";

	private static FhirContext ourFhirContext = FhirContext.forR4();

	@Test
	void testExtensionsWork() {
		Patient p1 = new Patient();
		assertFalse(ExtensionUtil.hasExtension(p1, EXT_URL));
		ExtensionUtil.setExtension(ourFhirContext, p1, EXT_URL, "value");
		assertTrue(ExtensionUtil.hasExtension(p1, EXT_URL));
	}

	@Test
	void testExtensionTypesWork() {
		Patient p1 = new Patient();
		assertFalse(ExtensionUtil.hasExtension(p1, EXT_URL));
		ExtensionUtil.setExtension(ourFhirContext, p1, EXT_URL, "integer", "1");

		assertTrue(ExtensionUtil.hasExtension(p1, EXT_URL));
		assertEquals(1, ExtensionUtil.getExtensions(p1, EXT_URL).size());

		IBaseDatatype ext = ExtensionUtil.getExtension(p1, EXT_URL).getValue();
		assertEquals("1", ext.toString());
	}
}
