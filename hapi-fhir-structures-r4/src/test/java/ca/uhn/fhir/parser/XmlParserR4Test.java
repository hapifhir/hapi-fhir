package ca.uhn.fhir.parser;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.stringContainsInOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;

import ca.uhn.fhir.test.BaseTest;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Composition;
import org.hl7.fhir.r4.model.MessageHeader;
import org.hl7.fhir.r4.model.Narrative;
import org.hl7.fhir.r4.model.Observation;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uhn.fhir.context.FhirContext;

import java.io.IOException;

public class XmlParserR4Test extends BaseTest {
	private static final Logger ourLog = LoggerFactory.getLogger(XmlParserR4Test.class);
	private static FhirContext ourCtx = FhirContext.forR4();
	
	
	private Composition createComposition(String sectionText) {
		Composition c = new Composition();
		Narrative compositionText = new Narrative().setStatus(Narrative.NarrativeStatus.GENERATED);
		compositionText.setDivAsString("Composition");		
		Narrative compositionSectionText = new Narrative().setStatus(Narrative.NarrativeStatus.GENERATED);
		compositionSectionText.setDivAsString(sectionText);		
		c.setText(compositionText);
		c.addSection().setText(compositionSectionText);
		return c;
	}

	/**
	 * See #402 section.text is overwritten by composition.text
	 */
	@Test
	public void testEncodingTextSection() {

		String sectionText = "sectionText";
		Composition composition = createComposition(sectionText);

		String encoded = ourCtx.newXmlParser().encodeResourceToString(composition);
		ourLog.info(encoded);

		int idx = encoded.indexOf(sectionText);
		assertNotEquals(-1, idx);
	}

	@Test
	public void testEncodeAndParseBundleWithFullUrlAndResourceIdMismatch() {

		MessageHeader header = new MessageHeader();
		header.setId("1.1.1.1");
		header.setDefinition("Hello");

		Bundle input = new Bundle();
		input
			.addEntry()
			.setFullUrl("urn:uuid:0.0.0.0")
			.setResource(header);

		String encoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(input);

		ourLog.info("Encoded: {}", encoded);
		assertThat(encoded, stringContainsInOrder(
			"<fullUrl value=\"urn:uuid:0.0.0.0\"/>",
			"<id value=\"1.1.1.1\"/>"
		));

		input = ourCtx.newXmlParser().parseResource(Bundle.class, encoded);
		assertEquals("urn:uuid:0.0.0.0", input.getEntry().get(0).getFullUrl());
		assertEquals("MessageHeader/1.1.1.1", input.getEntry().get(0).getResource().getId());

	}

	/**
	 * See #1658
	 */
	@Test
	public void testNarrativeLangAttributePreserved() throws IOException {
		Observation obs = loadResource(ourCtx, Observation.class, "/resource-with-lang-in-narrative.xml");
		String encoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(obs);
		assertThat(encoded, containsString("xmlns=\"http://www.w3.org/1999/xhtml\""));
		assertThat(encoded, containsString("lang=\"en-US\""));
		ourLog.info(encoded);
	}


}
