package ca.uhn.fhir.parser;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.stringContainsInOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.io.IOException;
import java.net.URL;

import org.hl7.fhir.r4.model.AuditEvent;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Composition;
import org.hl7.fhir.r4.model.DocumentReference;
import org.hl7.fhir.r4.model.MessageHeader;
import org.hl7.fhir.r4.model.Narrative;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.test.BaseTest;

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
	 * Ensure that a contained bundle doesn't cause a crash
	 */
	@Test
	public void testParseAndEncodePreservesContainedResourceOrder() {
		String auditEvent = "<AuditEvent xmlns=\"http://hl7.org/fhir\">\n" +
			"   <contained>\n" +
			"      <Observation xmlns=\"http://hl7.org/fhir\">\n" +
			"         <id value=\"A\"/>\n" +
			"         <identifier>\n" +
			"            <value value=\"A\"/>\n" +
			"         </identifier>\n" +
			"      </Observation>\n" +
			"   </contained>\n" +
			"   <contained>\n" +
			"      <Observation xmlns=\"http://hl7.org/fhir\">\n" +
			"         <id value=\"B\"/>\n" +
			"         <identifier>\n" +
			"            <value value=\"B\"/>\n" +
			"         </identifier>\n" +
			"      </Observation>\n" +
			"   </contained>\n" +
			"   <entity>\n" +
			"      <what>\n" +
			"         <reference value=\"#B\"/>\n" +
			"      </what>\n" +
			"   </entity>\n" +
			"   <entity>\n" +
			"      <what>\n" +
			"         <reference value=\"#A\"/>\n" +
			"      </what>\n" +
			"   </entity>\n" +
			"</AuditEvent>";

		ourLog.info("Input: {}", auditEvent);
		AuditEvent ae = ourCtx.newXmlParser().parseResource(AuditEvent.class, auditEvent);
		assertEquals("#A", ae.getContained().get(0).getId());
		assertEquals("#B", ae.getContained().get(1).getId());
		assertEquals("#B", ae.getEntity().get(0).getWhat().getReference());
		assertEquals("#A", ae.getEntity().get(1).getWhat().getReference());

		String serialized = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(ae);
		assertEquals(auditEvent, serialized);

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

	@Test
	public void testParseBundleWithMultipleNestedContainedResources() throws Exception {
		URL url = Resources.getResource("bundle-with-two-patient-resources.xml");
		String text = Resources.toString(url, Charsets.UTF_8);

		Bundle bundle = ourCtx.newXmlParser().parseResource(Bundle.class, text);
		
		assertEquals("12346", getPatientIdValue(bundle, 0));
		assertEquals("12345", getPatientIdValue(bundle, 1));
	}

	private String getPatientIdValue(Bundle input, int entry) {
		final DocumentReference documentReference = (DocumentReference)input.getEntry().get(entry).getResource();
		final Patient patient = (Patient) documentReference.getSubject().getResource();
		return patient.getIdentifier().get(0).getValue();
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

	/**
	 * Ensure that a contained bundle doesn't cause a crash
	 */
	@Test
	public void testEncodeContainedBundle() {
		String auditEvent = "<AuditEvent xmlns=\"http://hl7.org/fhir\">\n" +
			"   <contained>\n" +
			"      <Bundle xmlns=\"http://hl7.org/fhir\">\n" +
			"         <id value=\"REASONS\"/>\n" +
			"         <entry>\n" +
			"            <resource>\n" +
			"               <Condition xmlns=\"http://hl7.org/fhir\">\n" +
			"                  <id value=\"123\"/>\n" +
			"               </Condition>\n" +
			"            </resource>\n" +
			"         </entry>\n" +
			"      </Bundle>\n" +
			"   </contained>\n" +
			"   <contained>\n" +
			"      <MeasureReport xmlns=\"http://hl7.org/fhir\">\n" +
			"         <id value=\"MRPT5000602611RD\"/>\n" +
			"         <evaluatedResource>\n" +
			"            <reference value=\"#REASONS\"/>\n" +
			"         </evaluatedResource>\n" +
			"      </MeasureReport>\n" +
			"   </contained>\n" +
			"   <entity>\n" +
			"      <what>\n" +
			"         <reference value=\"#MRPT5000602611RD\"/>\n" +
			"      </what>\n" +
			"   </entity>\n" +
			"</AuditEvent>";
		AuditEvent ae = ourCtx.newXmlParser().parseResource(AuditEvent.class, auditEvent);
		String auditEventAsString = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(ae);
		assertEquals(auditEvent, auditEventAsString);
	}
	
	/**
	 * Ensure that a xml:lang attribute is de- and reserialized correctly
	 */
	@Test
	public void testDivXhtmlLangAttribute() {
		String parameters = "<Parameters xmlns=\"http://hl7.org/fhir\">\n" + 
				"   <parameter>\n" + 
				"      <name value=\"resource\"/>\n" + 
				"      <resource>\n" + 
				"         <Patient xmlns=\"http://hl7.org/fhir\">\n" + 
				"            <id value=\"example\"/>\n" + 
				"            <language value=\"de\"/>\n" + 
				"            <text>\n" + 
				"               <status value=\"generated\"/>\n" + 
				"               <div xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"de\" lang=\"de\">42 </div>\n" + 
				"            </text>\n" + 
				"         </Patient>\n" + 
				"      </resource>\n" + 
				"   </parameter>\n" + 
				"</Parameters>";
		Parameters pa = ourCtx.newXmlParser().parseResource(Parameters.class, parameters);
		String parameteresAsString = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(pa);
		assertEquals(parameters, parameteresAsString);
	}



}
