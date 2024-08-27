package ca.uhn.fhir.model.dstu2;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.parser.IParser;
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ModelParseTest {
	static final Logger ourLog = LoggerFactory.getLogger(ModelParseTest.class);
	static FhirContext ourFhirContext = FhirContext.forDstu2Cached();

	@ParameterizedTest
	@MethodSource("getParsers")
	void testTagRoundTrip(IParser theParser) {
	    // given
		Patient resource = new Patient();
		IBaseCoding tag = resource.getMeta().addTag();
		tag.setCode("code");
		tag.setDisplay("display");
		tag.setSystem("oid:123");
		tag.setVersion("v1");
		tag.setUserSelected(true);

		// when
		String string = theParser.encodeResourceToString(resource);
		ourLog.info("encoded: {}", string);
		Patient bundleOut = theParser.parseResource(Patient.class, string);

		// then
		List<? extends IBaseCoding> tags = bundleOut.getMeta().getTag();
		assertThat(tags.size()).as("tag is present").isEqualTo(1);
		IBaseCoding tagOut = tags.get(0);
		assertEquals("code", tagOut.getCode());
		assertEquals("display", tagOut.getDisplay());
		assertEquals("oid:123", tagOut.getSystem());
		assertEquals("v1", tagOut.getVersion());
		assertEquals(true, tagOut.getUserSelected());
	}

	@ParameterizedTest
	@MethodSource("getParsers")
	void testSecurityRoundTrip(IParser theParser) {
		// given
		Patient resource = new Patient();
		IBaseCoding coding = resource.getMeta().addSecurity();
		coding.setCode("code");
		coding.setDisplay("display");
		coding.setSystem("oid:123");
		coding.setVersion("v1");
		coding.setUserSelected(true);

		// when
		String string = theParser.encodeResourceToString(resource);
		ourLog.info("encoded: {}", string);
		Patient bundleOut = theParser.parseResource(Patient.class, string);

		// then
		List<? extends IBaseCoding> labels = bundleOut.getMeta().getSecurity();
		assertThat(labels.size()).as("security is present").isEqualTo(1);
		IBaseCoding codingOut = labels.get(0);
		assertEquals("code", codingOut.getCode());
		assertEquals("display", codingOut.getDisplay());
		assertEquals("oid:123", codingOut.getSystem());
		assertEquals("v1", codingOut.getVersion());
		assertEquals(true, codingOut.getUserSelected());
	}

	public static List<IParser> getParsers() {
		return List.of(
			ourFhirContext.newJsonParser(),
			// ourFhirContext.newRDFParser(), dstu2 doesn't support RDF
			ourFhirContext.newXmlParser()
		);
	}
}
