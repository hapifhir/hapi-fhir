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
		assertThat(tagOut.getCode()).isEqualTo("code");
		assertThat(tagOut.getDisplay()).isEqualTo("display");
		assertThat(tagOut.getSystem()).isEqualTo("oid:123");
		assertThat(tagOut.getVersion()).isEqualTo("v1");
		assertThat(tagOut.getUserSelected()).isEqualTo(true);
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
		assertThat(codingOut.getCode()).isEqualTo("code");
		assertThat(codingOut.getDisplay()).isEqualTo("display");
		assertThat(codingOut.getSystem()).isEqualTo("oid:123");
		assertThat(codingOut.getVersion()).isEqualTo("v1");
		assertThat(codingOut.getUserSelected()).isEqualTo(true);
	}

	public static List<IParser> getParsers() {
		return List.of(
			ourFhirContext.newJsonParser(),
			// ourFhirContext.newRDFParser(), dstu2 doesn't support RDF
			ourFhirContext.newXmlParser()
		);
	}
}
