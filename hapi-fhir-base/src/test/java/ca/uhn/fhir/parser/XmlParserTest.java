package ca.uhn.fhir.parser;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringReader;

import org.apache.commons.io.IOUtils;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.hamcrest.core.IsNot;
import org.hamcrest.core.StringContains;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.ResourceWithExtensionsA;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.BundleEntry;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.composite.NarrativeDt;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.resource.DiagnosticReport;
import ca.uhn.fhir.model.dstu.resource.Observation;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.resource.Specimen;
import ca.uhn.fhir.model.dstu.resource.ValueSet;
import ca.uhn.fhir.model.dstu.valueset.AddressUseEnum;
import ca.uhn.fhir.model.dstu.valueset.AdministrativeGenderCodesEnum;
import ca.uhn.fhir.model.dstu.valueset.NarrativeStatusEnum;
import ca.uhn.fhir.model.primitive.DecimalDt;

public class XmlParserTest {

	@Test
	public void testParseBundleLarge() throws IOException {
		
		String msg = IOUtils.toString(XmlParser.class.getResourceAsStream("/atom-document-large.xml"));
		IParser p = new FhirContext(Patient.class).newXmlParser();
		Bundle bundle = p.parseBundle(msg);

		assertEquals("http://spark.furore.com/fhir/_snapshot?id=327d6bb9-83b0-4929-aa91-6dd9c41e587b&start=0&_count=20", bundle.getLinkSelf().getValue());
		assertEquals("Patient resource with id 3216379", bundle.getEntries().get(0).getTitle().getValue());
		
	}

	@Test
	public void testEncodeBoundCode() throws IOException {

		Patient patient = new Patient();
		patient.addAddress().setUse(AddressUseEnum.HOME);
		
		patient.getGender().setValueAsEnum(AdministrativeGenderCodesEnum.M);
		
		String val = new FhirContext().newXmlParser().encodeResourceToString(patient);
		ourLog.info(val);
		
	}

	@Test
	public void testEncodeBundleResultCount() throws IOException {

		Bundle b = new Bundle();
		b.getTotalResults().setValue(123);
		
		String val = new FhirContext().newXmlParser().setPrettyPrint(true).encodeBundleToString(b);
		ourLog.info(val);
		
		assertThat(val, StringContains.containsString("<os:totalResults xmlns:os=\"http://a9.com/-/spec/opensearch/1.1/\">123</os:totalResults>"));
		
	}

	
	@Test
	public void testParseContainedResources() throws IOException {
		
		String msg = IOUtils.toString(XmlParser.class.getResourceAsStream("/contained-diagnosticreport.xml"));
		IParser p = new FhirContext(DiagnosticReport.class).newXmlParser();
		DiagnosticReport bundle = p.parseResource(DiagnosticReport.class, msg);
		
		ResourceReferenceDt result0 = bundle.getResult().get(0);
		Observation obs = (Observation) result0.getResource();
		
		assertNotNull(obs);
		assertEquals("718-7", obs.getName().getCoding().get(0).getCode().getValue());
		
	}

	@Test
	public void testEncodeInvalidChildGoodException() throws IOException {
		Observation obs = new Observation();
		obs.setValue(new DecimalDt(112.22));
		
		IParser p = new FhirContext(Observation.class).newXmlParser();
		
		// Should have good error message
		p.encodeResourceToString(obs);
	}

	@Test
	public void testEncodeContainedResources() throws IOException {

		DiagnosticReport rpt = new DiagnosticReport();
		Specimen spm = new Specimen();
		spm.getText().setDiv("AAA");
		rpt.addSpecimen().setResource(spm);
		
		IParser p = new FhirContext(DiagnosticReport.class).newXmlParser().setPrettyPrint(true);
		String str = p.encodeResourceToString(rpt);
		
		ourLog.info(str);
		assertThat(str, StringContains.containsString("<div xmlns=\"\">AAA</div>"));
		assertThat(str, StringContains.containsString("reference value=\"#"));
		
		int idx = str.indexOf("reference value=\"#") + "reference value=\"#".length();
		int idx2 = str.indexOf('"', idx+1);
		String id = str.substring(idx, idx2);
		assertThat(str, StringContains.containsString("<Specimen xmlns=\"http://hl7.org/fhir\" id=\"" + id + "\">"));
		assertThat(str, IsNot.not(StringContains.containsString("<?xml version='1.0'?>")));

	}

	
	
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
		
		IParser p = new FhirContext(ValueSet.class).newXmlParser();
		Bundle bundle = p.parseBundle(msg);
		
		assertEquals("FHIR Core Valuesets", bundle.getTitle().getValue());
		assertEquals("http://hl7.org/implement/standards/fhir/valuesets.xml", bundle.getLinkSelf().getValue());
		assertEquals("2014-02-10T04:11:24.435+00:00", bundle.getUpdated().getValueAsString());
		assertEquals(1, bundle.getEntries().size());
		
		BundleEntry entry = bundle.getEntries().get(0);
		assertEquals("HL7, Inc (FHIR Project)", entry.getAuthorName().getValue());
		assertEquals("http://hl7.org/fhir/valueset/256a5231-a2bb-49bd-9fea-f349d428b70d", entry.getEntryId().getValue());
		
		ValueSet resource = (ValueSet) entry.getResource();
		assertEquals("LOINC Codes for Cholesterol", resource.getName().getValue());
		assertEquals(summaryText.trim(), entry.getSummary().getValueAsString().trim());
	}
	
	@Test
	public void testLoadAndAncodeMessage() throws SAXException, IOException {

		//@formatter:off
		String msg = "<Patient xmlns=\"http://hl7.org/fhir\">" 
				+ "<text><status value=\"generated\" /><div xmlns=\"http://www.w3.org/1999/xhtml\">John Cardinal:            444333333        </div></text>"
				+ "<identifier><label value=\"SSN\" /><system value=\"http://orionhealth.com/mrn\" /><value value=\"PRP1660\" /></identifier>"
				+ "<name><use value=\"official\" /><family value=\"Cardinal\" /><given value=\"John\" /></name>"
				+ "<name><family value=\"Kramer\" /><given value=\"Doe\" /></name>"
				+ "<telecom><system value=\"phone\" /><value value=\"555-555-2004\" /><use value=\"work\" /></telecom>"
				+ "<gender><coding><system value=\"http://hl7.org/fhir/v3/AdministrativeGender\" /><code value=\"M\" /></coding></gender>"
				+ "<address><use value=\"home\" /><line value=\"2222 Home Street\" /></address><active value=\"true\" />"
				+ "</Patient>";
		//@formatter:on
		
		FhirContext ctx = new FhirContext(Patient.class);
		Patient patient = ctx.newXmlParser().parseResource(Patient.class, msg);
		
		assertEquals(NarrativeStatusEnum.GENERATED, patient.getText().getStatus().getValueAsEnum());
		assertEquals("<div xmlns=\"http://www.w3.org/1999/xhtml\">John Cardinal:            444333333        </div>", patient.getText().getDiv().getValueAsString());
		assertEquals("PRP1660", patient.getIdentifier().get(0).getValue().getValueAsString());
		
		String encoded = ctx.newXmlParser().encodeResourceToString(patient);

		Diff d = new Diff(new StringReader(msg), new StringReader(encoded));
		assertTrue(d.toString(), d.identical());

	}
	

	@Test
	public void testMessageWithMultipleTypes() throws SAXException, IOException {

		//@formatter:off
		String msg = "<Patient xmlns=\"http://hl7.org/fhir\">" 
				+ "<identifier><label value=\"SSN\" /><system value=\"http://orionhealth.com/mrn\" /><value value=\"PRP1660\" /></identifier>"
				+ "</Patient>";
		//@formatter:on
		
		FhirContext ctx = new FhirContext(Patient.class, ca.uhn.fhir.testmodel.Patient.class);
		Patient patient1 = ctx.newXmlParser().parseResource(Patient.class, msg);
		String encoded1 = ctx.newXmlParser().encodeResourceToString(patient1);

		ca.uhn.fhir.testmodel.Patient patient2 = ctx.newXmlParser().parseResource(ca.uhn.fhir.testmodel.Patient.class, msg);
		String encoded2 = ctx.newXmlParser().encodeResourceToString(patient2);

		Diff d = new Diff(new StringReader(encoded1), new StringReader(encoded2));
		assertTrue(d.toString(), d.identical());

	}

	
	@Test
	public void testLoadAndEncodeDeclaredExtensions() throws ConfigurationException, DataFormatException, SAXException, IOException {
		FhirContext ctx = new FhirContext(ResourceWithExtensionsA.class);
		IParser p = new XmlParser(ctx);

		//@formatter:off
		String msg = "<ResourceWithExtensionsA xmlns=\"http://hl7.org/fhir\">\n" + 
				"	<extension url=\"http://foo/#f1\">\n" + 
				"		<valueString value=\"Foo1Value\"/>\n" + 
				"	</extension>\n" + 
				"	<extension url=\"http://foo/#f1\">\n" + 
				"		<valueString value=\"Foo1Value2\"/>\n" + 
				"	</extension>\n" + 
				"	<modifierExtension url=\"http://foo/#f2\">\n" + 
				"		<valueString value=\"Foo2Value1\"/>\n" + 
				"	</modifierExtension>\n" + 
				"	<extension url=\"http://bar/#b1\">\n" + 
				"		<extension url=\"http://bar/#b1/1\">\n" +
				"			<valueDate value=\"2013-01-01\"/>\n" +
				"		</extension>\n" + 
				"		<extension url=\"http://bar/#b1/2\">\n" + 
				"			<extension url=\"http://bar/#b1/2/1\">\n" + 
				"				<valueDate value=\"2013-01-02\"/>\n" +
				"			</extension>\n" + 
				"			<extension url=\"http://bar/#b1/2/1\">\n" + 
				"				<valueDate value=\"2013-01-12\"/>\n" +
				"			</extension>\n" + 
				"			<extension url=\"http://bar/#b1/2/2\">\n" + 
				"				<valueDate value=\"2013-01-03\"/>\n" +
				"			</extension>\n" + 
				"		</extension>\n" + 
				"	</extension>\n" + 
				"	<identifier>\n" + 
				"		<label value=\"IdentifierLabel\"/>\n" + 
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

	@Test
	public void testLoadAndEncodeUndeclaredExtensions() throws ConfigurationException, DataFormatException, SAXException, IOException {
		FhirContext ctx = new FhirContext(Patient.class);
		IParser p = new XmlParser(ctx);

		//@formatter:off
		String msg = "<Patient xmlns=\"http://hl7.org/fhir\">\n" + 
				"	<extension url=\"http://foo/#f1\">\n" + 
				"		<valueString value=\"Foo1Value\"/>\n" + 
				"	</extension>\n" + 
				"	<extension url=\"http://foo/#f1\">\n" + 
				"		<valueString value=\"Foo1Value2\"/>\n" + 
				"	</extension>\n" + 
				"	<extension url=\"http://bar/#b1\">\n" + 
				"		<extension url=\"http://bar/#b1/1\">\n" +
				"			<valueDate value=\"2013-01-01\"/>\n" +
				"		</extension>\n" + 
				"		<extension url=\"http://bar/#b1/2\">\n" + 
				"			<extension url=\"http://bar/#b1/2/1\">\n" + 
				"				<valueDate value=\"2013-01-02\"/>\n" +
				"			</extension>\n" + 
				"			<extension url=\"http://bar/#b1/2/1\">\n" + 
				"				<valueDate value=\"2013-01-12\"/>\n" +
				"			</extension>\n" + 
				"			<extension url=\"http://bar/#b1/2/2\">\n" + 
				"				<valueDate value=\"2013-01-03\"/>\n" +
				"			</extension>\n" + 
				"		</extension>\n" + 
				"	</extension>\n" + 
				"	<modifierExtension url=\"http://foo/#f2\">\n" + 
				"		<valueString value=\"Foo2Value1\"/>\n" + 
				"	</modifierExtension>\n" + 
				"	<identifier>\n" + 
				"		<label value=\"IdentifierLabel\"/>\n" + 
				"	</identifier>\n" + 
				"</Patient>";
		//@formatter:on

		Patient resource = (Patient) p.parseResource(msg);
		assertEquals("IdentifierLabel", resource.getIdentifier().get(0).getLabel().getValue());
		assertEquals("Foo1Value", resource.getUndeclaredExtensions().get(0).getValueAsPrimitive().getValueAsString());
		assertEquals("Foo1Value2", resource.getUndeclaredExtensions().get(1).getValueAsPrimitive().getValueAsString());
		assertEquals("Foo2Value1", resource.getUndeclaredModifierExtensions().get(0).getValueAsPrimitive().getValueAsString());
		
		assertEquals("2013-01-01", resource.getUndeclaredExtensions().get(2).getUndeclaredExtensions().get(0).getValueAsPrimitive().getValueAsString());
		assertEquals("2013-01-02", resource.getUndeclaredExtensions().get(2).getUndeclaredExtensions().get(1).getUndeclaredExtensions().get(0).getValueAsPrimitive().getValueAsString());

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
		IParser p = new XmlParser(ctx);

		String string = IOUtils.toString(XmlParserTest.class.getResourceAsStream("/observation-example-eeg.xml"));
		IResource resource = p.parseResource(string);

		String result = p.encodeResourceToString(resource);
		ourLog.info(result);
	}

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(XmlParserTest.class);
}
