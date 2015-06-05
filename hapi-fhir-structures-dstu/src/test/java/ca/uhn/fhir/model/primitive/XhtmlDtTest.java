package ca.uhn.fhir.model.primitive;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

import ca.uhn.fhir.parser.DataFormatException;

public class XhtmlDtTest {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(XhtmlDtTest.class);

	@Test
	public void testRoundtripTiny() {
		String div = "<div>xmlns=&quot;http://hl7.org/fhir&quot;</div>";

		XhtmlDt x = new XhtmlDt();
		x.setValueAsString(div);

		String actual = x.getValueAsString();

		ourLog.info("Expected {}", div.replace("\r", "").replace("\n", "\\n"));
		ourLog.info("Actual   {}", actual.replace("\r\n", "\\r\\n").replace("\n", "\\n"));

		assertEquals(div.replace("\r", ""), actual);

	}

	@Test
	public void testRoundtrip() {
		String div = "<div><pre>\r\n&lt;<a title=\"Prospective warnings of potential issues when providing care to the patient.\" class=\"dict\" href=\"alert-definitions.html#Alert\"><b>Alert</b></a> xmlns=&quot;http://hl7.org/fhir&quot;&gt; <span style=\"float: right\"><a title=\"Documentation for this format\" href=\"formats.html\"><img alt=\"doco\" src=\"help.png\"/></a></span>\r\n &lt;!-- from <a href=\"resources.html\">Resource</a>: <a href=\"extensibility.html\">extension</a>, <a href=\"extensibility.html#modifierExtension\">modifierExtension</a>, language, <a href=\"narrative.html#Narrative\">text</a>, and <a href=\"references.html#contained\">contained</a> --&gt;\r\n &lt;<a title=\"Identifier assigned to the alert for external use (outside the FHIR environment).\" class=\"dict\" href=\"alert-definitions.html#Alert.identifier\"><b>identifier</b></a>&gt;<span style=\"color: Gray\">&lt;!--</span> <span style=\"color: brown\"><b>0..*</b></span> <span style=\"color: darkgreen\"><a href=\"datatypes.html#Identifier\">Identifier</a></span> <span style=\"color: navy\">Business identifier</span><span style=\"color: Gray\"> --&gt;</span>&lt;/identifier&gt;\r\n &lt;<a title=\"Allows an alert to be divided into different categories like clinical, administrative etc.\" class=\"dict\" href=\"alert-definitions.html#Alert.category\"><b>category</b></a>&gt;<span style=\"color: Gray\">&lt;!--</span> <span style=\"color: brown\"><b>0..1</b></span> <span style=\"color: darkgreen\"><a href=\"datatypes.html#CodeableConcept\">CodeableConcept</a></span> <span style=\"color: navy\">Clinical, administrative, etc.</span><span style=\"color: Gray\"> --&gt;</span>&lt;/category&gt;\r\n &lt;<a title=\"Supports basic workflow.\" class=\"dict\" href=\"alert-definitions.html#Alert.status\"><b>status</b></a> value=&quot;[<span style=\"color: darkgreen\"><a href=\"datatypes.html#code\">code</a></span>]&quot;/&gt;<span style=\"color: Gray\">&lt;!--</span> <span style=\"color: brown\"><b>1..1</b></span> <span style=\"color: navy\"><a style=\"color: navy\" href=\"alert-status.html\">active | inactive | entered in error</a></span><span style=\"color: Gray\"> --&gt;</span>\r\n &lt;<a title=\"The person who this alert concerns.\" class=\"dict\" href=\"alert-definitions.html#Alert.subject\"><b>subject</b></a>&gt;<span style=\"color: Gray\">&lt;!--</span> <span style=\"color: brown\"><b>1..1</b></span> <span style=\"color: darkgreen\"><a href=\"references.html#Resource\">Resource</a>(<a href=\"patient.html#Patient\">Patient</a>)</span> <span style=\"color: navy\">Who is alert about?</span><span style=\"color: Gray\"> --&gt;</span>&lt;/subject&gt;\r\n &lt;<a title=\"The person or device that created the alert.\" class=\"dict\" href=\"alert-definitions.html#Alert.author\"><b>author</b></a>&gt;<span style=\"color: Gray\">&lt;!--</span> <span style=\"color: brown\"><b>0..1</b></span> <span style=\"color: darkgreen\"><a href=\"references.html#Resource\">Resource</a>(<a href=\"practitioner.html#Practitioner\">Practitioner</a>|<a href=\"patient.html#Patient\">Patient</a>|<a href=\"device.html#Device\">Device</a>)</span> <span style=\"color: navy\">Alert creator</span><span style=\"color: Gray\"> --&gt;</span>&lt;/author&gt;\r\n &lt;<a title=\"The textual component of the alert to display to the user.\" class=\"dict\" href=\"alert-definitions.html#Alert.note\"><b>note</b></a> value=&quot;[<span style=\"color: darkgreen\"><a href=\"datatypes.html#string\">string</a></span>]&quot;/&gt;<span style=\"color: Gray\">&lt;!--</span> <span style=\"color: brown\"><b>1..1</b></span> <span style=\"color: navy\">Text of alert</span><span style=\"color: Gray\"> --&gt;</span>\r\n&lt;/Alert&gt;\r\n</pre></div>";

		XhtmlDt x = new XhtmlDt();
		x.setValueAsString(div);

		XhtmlDt x2 = new XhtmlDt();
		x2.setValue(x.getValue());

		String actual = x2.getValueAsString();

		ourLog.info("Expected {}", div.replace("\r", "").replace("\n", "\\n"));
		ourLog.info("Actual   {}", actual.replace("\r\n", "\\r\\n").replace("\n", "\\n"));

		assertEquals(div.replace("\r", ""), actual);

	}

	@Test
	public void testBasicCharacterEntity() {
		String input = "amp &amp;";

		XhtmlDt x = new XhtmlDt();
		x.setValueAsString(input);

		assertEquals("<div>amp &amp;</div>", x.getValueAsString());
	}

	@Test
	public void testOnlyProcessingDirective() {
		XhtmlDt x = new XhtmlDt();
		x.setValueAsString("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
		assertEquals(null, x.getValue());
		assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>", x.getValueAsString());
	}

	@Test
	public void testCharacterEntities() {
		String input = "&amp; Sect: &sect; uuml: &uuml; &Uuml; Trade: &trade;";

		XhtmlDt x = new XhtmlDt();
		x.setValueAsString(input);

		// <div>Sect: § uuml: ü Ü</div>
		// <div>Sect: &sect; uuml: &uuml; &Uuml;</div>
		assertEquals("<div>" + input + "</div>", x.getValueAsString());

		XhtmlDt x2 = new XhtmlDt();
		x2.setValue(x.getValue());
		assertEquals("<div>&amp; Sect: § uuml: ü Ü Trade: ™</div>", x2.getValueAsString());

	}

	/**
	 * #175
	 */
	@Test
	public void testCharacterEntityUnknown() {
		String input = "Trade &AAAAA;";

		XhtmlDt x = new XhtmlDt();
		try {
			x.setValueAsString(input);
			fail();
		} catch (DataFormatException e) {
			assertThat(e.toString(), containsString("AAAA"));
		}
	}

}
