package ca.uhn.fhir.util;

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.instance.model.api.IBaseDatatype;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.PrimitiveType;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ExtensionUtilTest {

	private static final String EXT_URL = "http://magic.com/extensions";

	private static FhirContext ourFhirContext = FhirContext.forR4();

	@Test
	void testExtensionsWork() {
		Patient p1 = new Patient();
		assertThat(ExtensionUtil.hasExtension(p1, EXT_URL)).isFalse();
		ExtensionUtil.setExtensionAsString(ourFhirContext, p1, EXT_URL, "value");
		assertThat(ExtensionUtil.hasExtension(p1, EXT_URL)).isTrue();
	}

	@Test
	void testExtensionTypesWork() {
		Patient p1 = new Patient();
		assertThat(ExtensionUtil.hasExtension(p1, EXT_URL)).isFalse();
		ExtensionUtil.setExtension(ourFhirContext, p1, EXT_URL, "integer", "1");

		assertThat(ExtensionUtil.hasExtension(p1, EXT_URL)).isTrue();
		assertThat(ExtensionUtil.getExtensionsByUrl(p1, EXT_URL)).hasSize(1);

		IBaseDatatype ext = ExtensionUtil.getExtensionByUrl(p1, EXT_URL).getValue();
		assertThat(ext.fhirType()).isEqualTo("integer");
		assertThat(((PrimitiveType) ext).asStringValue()).isEqualTo("1");
	}

	@Test
	void testAddExtension() {
		Patient p = new Patient();
		assertThat(ExtensionUtil.addExtension(p)).isNotNull();
		assertThat(ExtensionUtil.addExtension(p, "myUrl")).isNotNull();

		assertThat(p.getExtension()).hasSize(2);
		assertThat(p.getExtension().get(1).getUrl()).isEqualTo("myUrl");
	}

	@Test
	void testHasExtension() {
		Patient p = new Patient();
		p.addExtension("URL", new StringType("VALUE"));

		assertThat(ExtensionUtil.hasExtension(p, "URL")).isTrue();
		assertThat(ExtensionUtil.hasExtension(p, "URL", "VALUE")).isTrue();
	}

	@Test
	void testClearExtension() {
		Patient p = new Patient();
		p.addExtension("URL", new StringType("VALUE"));
		p.addExtension("URL2", new StringType("VALUE2"));

		ExtensionUtil.clearExtensionsByUrl(p, "URL");

		assertThat(p.getExtension()).hasSize(1);
		assertThat(ExtensionUtil.hasExtension(p, "URL")).isFalse();
		assertThat(ExtensionUtil.hasExtension(p, "URL2")).isTrue();

		ExtensionUtil.clearAllExtensions(p);

		assertThat(p.getExtension()).isEmpty();
	}
}
