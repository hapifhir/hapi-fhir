package ca.uhn.fhir.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringReader;

import org.apache.commons.io.IOUtils;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.ResourceWithExtensionsA;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.BundleEntry;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.resource.Observation;
import ca.uhn.fhir.model.dstu.resource.ValueSet;

public class XmlParserTest {

	@Test
	public void testParseBundle() {
		
		//@formatter:off
		String summaryText = 				
				"<div xmlns=\"http://www.w3.org/1999/xhtml\">\n" + 
				"      <p>Value set \"LOINC Codes for Cholesterol\": This is an example value set that includes \n" + 
				"        all the LOINC codes for serum cholesterol from v2.36. \n" + 
				"        Developed by: FHIR project team (example)</p></div>"; 

		String msg = "<feed xmlns=\"http://www.w3.org/2005/Atom\">\n" + 
				"  <title>FHIR Core Valuesets</title>\n" + 
				"  <id>http://hl7.org/fhir/profile/valuesets</id>\n" + 
				"  <link href=\"http://hl7.org/implement/standards/fhir/valuesets.xml\" rel=\"self\"/>\n" + 
				"  <updated>2014-02-10T04:11:24.435-00:00</updated>\n" + 
				"  <entry>\n" + 
				"    <title>Valueset &quot;256a5231-a2bb-49bd-9fea-f349d428b70d&quot; to support automated processing</title>\n" + 
				"    <id>http://hl7.org/fhir/valueset/256a5231-a2bb-49bd-9fea-f349d428b70d</id>\n" + 
				"    <link href=\"http://hl7.org/implement/standards/fhir/valueset/256a5231-a2bb-49bd-9fea-f349d428b70d\" rel=\"self\"/>\n" + 
				"    <updated>2014-02-10T04:10:46.987-00:00</updated>\n" + 
				"    <author>\n" + 
				"      <name>HL7, Inc (FHIR Project)</name>\n" + 
				"      <uri>http://hl7.org/fhir</uri>\n" + 
				"    </author>\n" + 
				"    <published>2014-02-10T04:10:46.987-00:00</published>\n" + 
				"    <content type=\"text/xml\">\n" + 
				"      <ValueSet xmlns=\"http://hl7.org/fhir\">\n" + 
				"        <text>\n" + 
				"          <status value=\"generated\"/>" +
				"        </text>\n" + 
				"        <identifier value=\"256a5231-a2bb-49bd-9fea-f349d428b70d\"/>\n" + 
				"        <version value=\"20120613\"/>\n" + 
				"        <name value=\"LOINC Codes for Cholesterol\"/>\n" + 
				"        <publisher value=\"FHIR project team (example)\"/>\n" + 
				"        <telecom>\n" + 
				"          <system value=\"url\"/>\n" + 
				"          <value value=\"http://hl7.org/fhir\"/>\n" + 
				"        </telecom>\n" + 
				"        <description value=\"This is an example value set that includes        all the LOINC codes for serum cholesterol from v2.36\"/>\n" + 
				"        <status value=\"draft\"/>\n" + 
				"        <experimental value=\"true\"/>\n" + 
				"        <date value=\"2012-06-13\"/>\n" + 
				"        <compose>\n" + 
				"          <include>\n" + 
				"            <system value=\"http://loinc.org\"/>\n" + 
				"            <version value=\"2.36\"/>\n" + 
				"            <code value=\"14647-2\"/>\n" + 
				"            <code value=\"2093-3\"/>\n" + 
				"            <code value=\"35200-5\"/>\n" + 
				"            <code value=\"9342-7\"/>\n" + 
				"          </include>\n" + 
				"        </compose>\n" + 
				"      </ValueSet>\n" + 
				"    </content>\n" + 
				"    <summary type=\"xhtml\">"+
				summaryText +
				"    </summary>\n" + 
				"  </entry>" +
				"</feed>";
		//@formatter:on
		
		XmlParser p = new FhirContext(ValueSet.class).newXmlParser();
		Bundle bundle = p.parseBundle(msg);
		
		assertEquals("FHIR Core Valuesets", bundle.getTitle().getValue());
		assertEquals("http://hl7.org/implement/standards/fhir/valuesets.xml", bundle.getLinkSelf().getValue());
		assertEquals("2014-02-10T04:11:24.435+00:00", bundle.getUpdated().getValueAsString());
		assertEquals(1, bundle.getEntries().size());
		
		BundleEntry entry = bundle.getEntries().get(0);
		assertEquals("HL7, Inc (FHIR Project)", entry.getAuthorName().getValue());
		assertEquals("http://hl7.org/fhir/valueset/256a5231-a2bb-49bd-9fea-f349d428b70d", entry.getId().getValue());
		
		ValueSet resource = (ValueSet) entry.getResource();
		assertEquals("LOINC Codes for Cholesterol", resource.getName().getValue());
		assertEquals(summaryText.trim(), entry.getSummary().getValueAsString().trim());
	}
	
	@Test
	public void testLoadAndEncodeExtensions() throws ConfigurationException, DataFormatException, SAXException, IOException {
		FhirContext ctx = new FhirContext(ResourceWithExtensionsA.class);
		XmlParser p = new XmlParser(ctx);

		//@formatter:off
		String msg = "<ResourceWithExtensionsA xmlns=\"http://hl7.org/fhir\">\n" + 
				"	<extension url=\"http://foo/1\">\n" + 
				"		<valueString value=\"Foo1Value\"/>\n" + 
				"	</extension>\n" + 
				"	<extension url=\"http://foo/1\">\n" + 
				"		<valueString value=\"Foo1Value2\"/>\n" + 
				"	</extension>\n" + 
				"	<extension url=\"http://foo/2\">\n" + 
				"		<valueString value=\"Foo2Value1\"/>\n" + 
				"	</extension>\n" + 
				"	<extension url=\"http://bar/1\">\n" + 
				"		<extension url=\"http://bar/1/1\">\n" +
				"			<valueDate value=\"2013-01-01\"/>\n" +
				"		</extension>\n" + 
				"		<extension url=\"http://bar/1/2\">\n" + 
				"			<extension url=\"http://bar/1/2/1\">\n" + 
				"				<valueDate value=\"2013-01-02\"/>\n" +
				"			</extension>\n" + 
				"			<extension url=\"http://bar/1/2/1\">\n" + 
				"				<valueDate value=\"2013-01-12\"/>\n" +
				"			</extension>\n" + 
				"			<extension url=\"http://bar/1/2/2\">\n" + 
				"				<valueDate value=\"2013-01-03\"/>\n" +
				"			</extension>\n" + 
				"		</extension>\n" + 
				"	</extension>\n" + 
				"	<identifier>\n" + 
				"		<label value=\"IdentifierLabel\"/>\n" + 
				"		<period />\n" + // this line can be removed once the parser encoding doesn't spit it out 
				"	</identifier>\n" + 
				"</ResourceWithExtensionsA>";
		//@formatter:on

		ResourceWithExtensionsA resource = (ResourceWithExtensionsA) p.parseResource(msg);
		assertEquals("IdentifierLabel", resource.getIdentifier().get(0).getLabel().getValue());
		assertEquals("Foo1Value", resource.getFoo1().get(0).getValue());
		assertEquals("Foo1Value2", resource.getFoo1().get(1).getValue());
		assertEquals("Foo2Value1", resource.getFoo2().getValue());
		assertEquals("2013-01-01", resource.getBar1().get(0).getBar11().get(0).getValueAsString());
		assertEquals("2013-01-02", resource.getBar1().get(0).getBar12().get(0).getBar121().get(0).getValueAsString());
		assertEquals("2013-01-12", resource.getBar1().get(0).getBar12().get(0).getBar121().get(1).getValueAsString());
		assertEquals("2013-01-03", resource.getBar1().get(0).getBar12().get(0).getBar122().get(0).getValueAsString());

		String encoded = p.encodeResourceToString(resource);
		ourLog.info(encoded);

		Diff d = new Diff(new StringReader(msg), new StringReader(encoded));
		assertTrue(d.toString(), d.identical());
	}

	@BeforeClass
	public static void beforeClass() {
		XMLUnit.setIgnoreAttributeOrder(true);
		XMLUnit.setIgnoreComments(true);
		XMLUnit.setIgnoreWhitespace(true);
	}
	
	@Test
	public void testLoadObservation() throws ConfigurationException, DataFormatException, IOException {

		FhirContext ctx = new FhirContext(Observation.class);
		XmlParser p = new XmlParser(ctx);

		IResource resource = p.parseResource(IOUtils.toString(XmlParserTest.class.getResourceAsStream("/observation-example-eeg.xml")));

		String result = p.encodeResourceToString(resource);
		ourLog.info(result);
	}

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(XmlParserTest.class);
}
