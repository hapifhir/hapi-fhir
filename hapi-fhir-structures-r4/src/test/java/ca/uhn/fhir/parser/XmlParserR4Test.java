package ca.uhn.fhir.parser;

import static org.junit.Assert.assertNotEquals;

import org.hl7.fhir.r4.model.Composition;
import org.hl7.fhir.r4.model.Narrative;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.uhn.fhir.context.FhirContext;

public class XmlParserR4Test {
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
	
}
