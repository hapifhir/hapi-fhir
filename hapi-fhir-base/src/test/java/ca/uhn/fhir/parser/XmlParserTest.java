package ca.uhn.fhir.parser;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.hamcrest.core.IsNot;
import org.hamcrest.core.StringContains;
import org.hamcrest.text.StringContainsInOrder;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.ResourceWithExtensionsA;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.BundleEntry;
import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.dstu.composite.AddressDt;
import ca.uhn.fhir.model.dstu.composite.HumanNameDt;
import ca.uhn.fhir.model.dstu.composite.NarrativeDt;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.resource.Conformance;
import ca.uhn.fhir.model.dstu.resource.Conformance.RestResource;
import ca.uhn.fhir.model.dstu.resource.DiagnosticReport;
import ca.uhn.fhir.model.dstu.resource.Observation;
import ca.uhn.fhir.model.dstu.resource.Organization;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.resource.Specimen;
import ca.uhn.fhir.model.dstu.resource.ValueSet;
import ca.uhn.fhir.model.dstu.valueset.AddressUseEnum;
import ca.uhn.fhir.model.dstu.valueset.AdministrativeGenderCodesEnum;
import ca.uhn.fhir.model.dstu.valueset.IdentifierUseEnum;
import ca.uhn.fhir.model.dstu.valueset.NarrativeStatusEnum;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.DecimalDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.primitive.XhtmlDt;
import ca.uhn.fhir.narrative.INarrativeGenerator;
import ca.uhn.fhir.parser.JsonParserTest.MyPatientWithOneDeclaredAddressExtension;
import ca.uhn.fhir.parser.JsonParserTest.MyPatientWithOneDeclaredExtension;

public class XmlParserTest {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(XmlParserTest.class);
	private static FhirContext ourCtx;

	@Test
	public void testEncodeBoundCode() {

		Patient patient = new Patient();
		patient.addAddress().setUse(AddressUseEnum.HOME);

		patient.getGender().setValueAsEnum(AdministrativeGenderCodesEnum.M);

		String val = new FhirContext().newXmlParser().encodeResourceToString(patient);
		ourLog.info(val);

	}

	@Test
	public void testTagList() {
		
		//@formatter:off
		String tagListStr = "<taglist xmlns=\"http://hl7.org/fhir\"> \n" + 
				"    <category term=\"term0\" label=\"label0\" scheme=\"scheme0\" /> \n" + 
				"    <category term=\"term1\" label=\"label1\" scheme=\"\" /> \n" + 
				"    <category term=\"term2\" label=\"label2\" /> \n" + 
				"</taglist>";
		//@formatter:on
		
		TagList tagList = new FhirContext().newXmlParser().parseTagList(tagListStr);
		assertEquals(3, tagList.size());
		assertEquals("term0", tagList.get(0).getTerm());
		assertEquals("label0", tagList.get(0).getLabel());
		assertEquals("scheme0", tagList.get(0).getScheme());
		assertEquals("term1", tagList.get(1).getTerm());
		assertEquals("label1", tagList.get(1).getLabel());
		assertEquals(null, tagList.get(1).getScheme());
		assertEquals("term2", tagList.get(2).getTerm());
		assertEquals("label2", tagList.get(2).getLabel());
		assertEquals(null, tagList.get(2).getScheme());
		
		/*
		 * Encode
		 */

		//@formatter:off
		String expected = "<taglist xmlns=\"http://hl7.org/fhir\">" + 
				"<category term=\"term0\" label=\"label0\" scheme=\"scheme0\"/>" + 
				"<category term=\"term1\" label=\"label1\"/>" + 
				"<category term=\"term2\" label=\"label2\"/>" + 
				"</taglist>";
		//@formatter:on
		
		String encoded = new FhirContext().newXmlParser().encodeTagListToString(tagList);
		assertEquals(expected,encoded);
		
	}
	
	
	@Test
	public void testTotalResultsUsingOldNamespace() {

		//@formatter:off
		String bundle = "<feed xmlns=\"http://www.w3.org/2005/Atom\">\n" + 
				"  <title>Search results for Patient</title>\n" + 
				"  <id>urn:uuid:374f2876-0da7-4441-87da-526e2fc624f8</id>\n" + 
				"  <totalResults xmlns=\"http://purl.org/atompub/tombstones/1.0\">15</totalResults>\n" + 
				"  <updated>2014-05-04T13:19:47.027-04:00</updated>\n" + 
				"  <author>\n" + 
				"    <name>AEGIS Wildfhir Server</name>\n" + 
				"  </author>" +
				"</feed>";
		//@formatter:off

		Bundle bundleR = new FhirContext().newXmlParser().parseBundle(bundle);
		assertEquals(15, bundleR.getTotalResults().getValue().intValue());
	}

	@Test
	public void testEncodeExtensionWithResourceContent() {
		IParser parser = new FhirContext().newXmlParser();

		Patient patient = new Patient();
		patient.addAddress().setUse(AddressUseEnum.HOME);
		patient.addUndeclaredExtension(false, "urn:foo", new ResourceReferenceDt("Organization/123"));

		String val = parser.encodeResourceToString(patient);
		ourLog.info(val);
		assertThat(val, StringContains.containsString("<extension url=\"urn:foo\"><valueResource><reference value=\"Organization/123\"/></valueResource></extension>"));

		Patient actual = parser.parseResource(Patient.class, val);
		assertEquals(AddressUseEnum.HOME, patient.getAddressFirstRep().getUse().getValueAsEnum());
		List<ExtensionDt> ext = actual.getUndeclaredExtensionsByUrl("urn:foo");
		assertEquals(1, ext.size());
		ResourceReferenceDt ref = (ResourceReferenceDt) ext.get(0).getValue();
		assertEquals("Organization/123", ref.getReference().getValue());

	}

	@Test
	public void testEncodeDeclaredExtensionWithResourceContent() {
		IParser parser = new FhirContext().newXmlParser();

		MyPatientWithOneDeclaredExtension patient = new MyPatientWithOneDeclaredExtension();
		patient.addAddress().setUse(AddressUseEnum.HOME);
		patient.setFoo(new ResourceReferenceDt("Organization/123"));

		String val = parser.encodeResourceToString(patient);
		ourLog.info(val);
		assertThat(val, StringContains.containsString("<extension url=\"urn:foo\"><valueResource><reference value=\"Organization/123\"/></valueResource></extension>"));

		MyPatientWithOneDeclaredExtension actual = parser.parseResource(MyPatientWithOneDeclaredExtension.class, val);
		assertEquals(AddressUseEnum.HOME, patient.getAddressFirstRep().getUse().getValueAsEnum());
		ResourceReferenceDt ref = actual.getFoo();
		assertEquals("Organization/123", ref.getReference().getValue());

	}

	@Test
	public void testEncodeDeclaredExtensionWithAddressContent() {
		IParser parser = new FhirContext().newXmlParser();

		MyPatientWithOneDeclaredAddressExtension patient = new MyPatientWithOneDeclaredAddressExtension();
		patient.addAddress().setUse(AddressUseEnum.HOME);
		patient.setFoo(new AddressDt().addLine("line1"));

		String val = parser.encodeResourceToString(patient);
		ourLog.info(val);
		assertThat(val, StringContains.containsString("<extension url=\"urn:foo\"><valueAddress><line value=\"line1\"/></valueAddress></extension>"));

		MyPatientWithOneDeclaredAddressExtension actual = parser.parseResource(MyPatientWithOneDeclaredAddressExtension.class, val);
		assertEquals(AddressUseEnum.HOME, patient.getAddressFirstRep().getUse().getValueAsEnum());
		AddressDt ref = actual.getFoo();
		assertEquals("line1", ref.getLineFirstRep().getValue());

	}

	@Test
	public void testEncodeUndeclaredExtensionWithAddressContent() {
		IParser parser = new FhirContext().newXmlParser();

		Patient patient = new Patient();
		patient.addAddress().setUse(AddressUseEnum.HOME);
		patient.addUndeclaredExtension(false, "urn:foo", new AddressDt().addLine("line1"));

		String val = parser.encodeResourceToString(patient);
		ourLog.info(val);
		assertThat(val, StringContains.containsString("<extension url=\"urn:foo\"><valueAddress><line value=\"line1\"/></valueAddress></extension>"));

		MyPatientWithOneDeclaredAddressExtension actual = parser.parseResource(MyPatientWithOneDeclaredAddressExtension.class, val);
		assertEquals(AddressUseEnum.HOME, patient.getAddressFirstRep().getUse().getValueAsEnum());
		AddressDt ref = actual.getFoo();
		assertEquals("line1", ref.getLineFirstRep().getValue());

	}

	@Test
	public void testEncodeBundleResultCount() {

		Bundle b = new Bundle();
		b.getTotalResults().setValue(123);

		String val = new FhirContext().newXmlParser().setPrettyPrint(true).encodeBundleToString(b);
		ourLog.info(val);

		assertThat(val, StringContains.containsString("<os:totalResults xmlns:os=\"http://a9.com/-/spec/opensearch/1.1/\">123</os:totalResults>"));

	}

	@Test
	public void testEncodeBundleCategory() {

		Bundle b = new Bundle();
		BundleEntry e = b.addEntry();
		e.setResource(new Patient());
		e.addCategory().setLabel("label").setTerm("term").setScheme("scheme");

		String val = new FhirContext().newXmlParser().setPrettyPrint(true).encodeBundleToString(b);
		ourLog.info(val);

		assertThat(val, StringContains.containsString("<category term=\"term\" label=\"label\" scheme=\"scheme\"/>"));

		b = new FhirContext().newXmlParser().parseBundle(val);
		assertEquals(1, b.getEntries().size());
		assertEquals(1, b.getEntries().get(0).getCategories().size());
		assertEquals("term", b.getEntries().get(0).getCategories().get(0).getTerm());
		assertEquals("label", b.getEntries().get(0).getCategories().get(0).getLabel());
		assertEquals("scheme", b.getEntries().get(0).getCategories().get(0).getScheme());
		assertNull(b.getEntries().get(0).getResource());

	}

	@Test
	public void testEncodeBundle() {
		Bundle b= new Bundle();
		
		Patient p1 = new Patient();
		p1.addName().addFamily("Family1");
		BundleEntry entry = b.addEntry();
		entry.getId().setValue("1");
		entry.setResource(p1);

		Patient p2 = new Patient();
		p2.addName().addFamily("Family2");
		entry = b.addEntry();
		entry.getId().setValue("2");
		entry.setResource(p2);
		
		BundleEntry deletedEntry = b.addEntry();
		deletedEntry.setId(new IdDt("Patient/3"));
		deletedEntry.setDeleted(InstantDt.withCurrentTime());
		
		String bundleString = ourCtx.newXmlParser().setPrettyPrint(true).encodeBundleToString(b);
		ourLog.info(bundleString);

		List<String> strings = new ArrayList<String>();
		strings.addAll(Arrays.asList("<entry>", "<id>1</id>", "</entry>"));
		strings.addAll(Arrays.asList("<entry>", "<id>2</id>", "</entry>"));
		strings.addAll(Arrays.asList("<at:deleted-entry", "ref=\"Patient/3", "/>"));
		assertThat(bundleString, StringContainsInOrder.stringContainsInOrder(strings));
		
	}
	
	@Test
	public void testEncodeContainedResources() {

		DiagnosticReport rpt = new DiagnosticReport();
		Specimen spm = new Specimen();
		spm.getText().setDiv("AAA");
		rpt.addSpecimen().setResource(spm);

		IParser p = new FhirContext(DiagnosticReport.class).newXmlParser().setPrettyPrint(true);
		String str = p.encodeResourceToString(rpt);

		ourLog.info(str);
		assertThat(str, StringContains.containsString("<div xmlns=\"http://www.w3.org/1999/xhtml\">AAA</div>"));
		assertThat(str, StringContains.containsString("reference value=\"#"));

		int idx = str.indexOf("reference value=\"#") + "reference value=\"#".length();
		int idx2 = str.indexOf('"', idx + 1);
		String id = str.substring(idx, idx2);
		assertThat(str, StringContains.containsString("<Specimen xmlns=\"http://hl7.org/fhir\" id=\"" + id + "\">"));
		assertThat(str, IsNot.not(StringContains.containsString("<?xml version='1.0'?>")));

	}

	@Test
	public void testEncodeInvalidChildGoodException() {
		Observation obs = new Observation();
		obs.setValue(new DecimalDt(112.22));

		IParser p = new FhirContext(Observation.class).newJsonParser();

		try {
			p.encodeResourceToString(obs);
		} catch (DataFormatException e) {
			assertThat(e.getMessage(), StringContains.containsString("PeriodDt"));
		}
	}

	@Test
	public void testEncodeResourceRef() throws DataFormatException {

		Patient patient = new Patient();
		patient.setManagingOrganization(new ResourceReferenceDt());

		IParser p = new FhirContext().newXmlParser();
		String str = p.encodeResourceToString(patient);
		assertThat(str, IsNot.not(StringContains.containsString("managingOrganization")));

		patient.setManagingOrganization(new ResourceReferenceDt("Organization/123"));
		str = p.encodeResourceToString(patient);
		assertThat(str, StringContains.containsString("<managingOrganization><reference value=\"Organization/123\"/></managingOrganization>"));

		Organization org = new Organization();
		org.addIdentifier().setSystem("foo").setValue("bar");
		patient.setManagingOrganization(new ResourceReferenceDt(org));
		str = p.encodeResourceToString(patient);
		assertThat(str, StringContains.containsString("<contained><Organization"));

	}

	@Test
	public void testExtensions() throws DataFormatException {

		MyPatient patient = new MyPatient();
		patient.setPetName(new StringDt("Fido"));
		patient.getImportantDates().add(new DateTimeDt("2010-01-02"));
		patient.getImportantDates().add(new DateTimeDt("2014-01-26T11:11:11"));

		patient.addName().addFamily("Smith");

		IParser p = new FhirContext().newXmlParser();
		String str = p.encodeResourceToString(patient);

		ourLog.info(str);

		assertThat(str, StringContains.containsString("<Patient xmlns=\"http://hl7.org/fhir\">"));
		assertThat(str, StringContains.containsString("<extension url=\"http://example.com/dontuse#petname\"><valueString value=\"Fido\"/></extension>"));
		assertThat(str, StringContains.containsString("<modifierExtension url=\"http://example.com/dontuse#importantDates\"><valueDateTime value=\"2010-01-02\"/></modifierExtension>"));
		assertThat(str, StringContains.containsString("<modifierExtension url=\"http://example.com/dontuse#importantDates\"><valueDateTime value=\"2014-01-26T11:11:11\"/></modifierExtension>"));
		assertThat(str, StringContains.containsString("<name><family value=\"Smith\"/></name>"));
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

		Patient patient = ourCtx.newXmlParser().parseResource(Patient.class, msg);

		assertEquals(NarrativeStatusEnum.GENERATED, patient.getText().getStatus().getValueAsEnum());
		assertEquals("<div xmlns=\"http://www.w3.org/1999/xhtml\">John Cardinal:            444333333        </div>", patient.getText().getDiv().getValueAsString());
		assertEquals("PRP1660", patient.getIdentifier().get(0).getValue().getValueAsString());

		String encoded = ourCtx.newXmlParser().encodeResourceToString(patient);

		Diff d = new Diff(new StringReader(msg), new StringReader(encoded));
		assertTrue(d.toString(), d.identical());

	}

	@Test
	public void testLoadAndEncodeDeclaredExtensions() throws ConfigurationException, DataFormatException, SAXException, IOException {
		IParser p = new FhirContext(ResourceWithExtensionsA.class).newXmlParser();

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
		IParser p = ourCtx.newXmlParser();

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

	
	@Test
	public void testParseWithXmlHeader() throws ConfigurationException, DataFormatException {
		IParser p = ourCtx.newXmlParser();

		//@formatter:off
		String msg = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
				"<Patient xmlns=\"http://hl7.org/fhir\">\n" + 
				"	<identifier>\n" + 
				"		<label value=\"IdentifierLabel\"/>\n" + 
				"	</identifier>\n" + 
				"</Patient>";
		//@formatter:on

		Patient resource = (Patient) p.parseResource(msg);
		assertEquals("IdentifierLabel", resource.getIdentifier().get(0).getLabel().getValue());
	}
	
	
	@Test
	public void testLoadObservation() throws ConfigurationException, DataFormatException, IOException {

		IParser p = ourCtx.newXmlParser();

		String string = IOUtils.toString(XmlParserTest.class.getResourceAsStream("/observation-example-eeg.xml"));
		IResource resource = p.parseResource(string);

		String result = p.encodeResourceToString(resource);
		ourLog.info(result);
	}

	@Test
	public void testLoadPatient() throws ConfigurationException, DataFormatException, IOException {

		IParser p = ourCtx.newXmlParser();

		String string = IOUtils.toString(XmlParserTest.class.getResourceAsStream("/patient-example-dicom.xml"));
		IResource resource = p.parseResource(string);

		String result = p.encodeResourceToString(resource);
		ourLog.info(result);

		// Nothing

		string = IOUtils.toString(XmlParserTest.class.getResourceAsStream("/patient-example-us-extensions.xml"));
		resource = p.parseResource(string);

		result = p.encodeResourceToString(resource);
		ourLog.info(result);

	}

	@Test
	public void testLoadQuestionnaire() throws ConfigurationException, DataFormatException, IOException {

		IParser p = ourCtx.newXmlParser();

		String string = IOUtils.toString(XmlParserTest.class.getResourceAsStream("/questionnaire-example.xml"));
		IResource resource = p.parseResource(string);

		String result = p.encodeResourceToString(resource);
		ourLog.info(result);
	}

	@Test
	public void testMessageWithMultipleTypes() throws SAXException, IOException {

		//@formatter:off
		String msg = "<Patient xmlns=\"http://hl7.org/fhir\">" 
				+ "<identifier><label value=\"SSN\" /><system value=\"http://orionhealth.com/mrn\" /><value value=\"PRP1660\" /></identifier>"
				+ "</Patient>";
		//@formatter:on

		Patient patient1 = ourCtx.newXmlParser().parseResource(Patient.class, msg);
		String encoded1 = ourCtx.newXmlParser().encodeResourceToString(patient1);

		ca.uhn.fhir.testmodel.Patient patient2 = ourCtx.newXmlParser().parseResource(ca.uhn.fhir.testmodel.Patient.class, msg);
		String encoded2 = ourCtx.newXmlParser().encodeResourceToString(patient2);

		Diff d = new Diff(new StringReader(encoded1), new StringReader(encoded2));
		assertTrue(d.toString(), d.identical());

	}

	@Test
	public void testNarrativeGeneration() throws DataFormatException {

		Patient patient = new Patient();

		patient.addName().addFamily("Smith");

		INarrativeGenerator gen = mock(INarrativeGenerator.class);
		XhtmlDt xhtmlDt = new XhtmlDt("<div>help</div>");
		NarrativeDt nar = new NarrativeDt(xhtmlDt, NarrativeStatusEnum.GENERATED);
		when(gen.generateNarrative(eq("http://hl7.org/fhir/profiles/Patient"), eq(patient))).thenReturn(nar);

		FhirContext context = new FhirContext();
		context.setNarrativeGenerator(gen);
		IParser p = context.newXmlParser();
		String str = p.encodeResourceToString(patient);

		ourLog.info(str);

		assertThat(str, StringContains.containsString("<Patient xmlns=\"http://hl7.org/fhir\">"));
		assertThat(str, StringContains.containsString("<Patient xmlns=\"http://hl7.org/fhir\">"));
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
				"    <category term=\"term\" label=\"label\" scheme=\"http://foo\"/>\n "+
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
		assertEquals("http://hl7.org/fhir/valueset/256a5231-a2bb-49bd-9fea-f349d428b70d", entry.getId().getValue());
		assertEquals(1, entry.getCategories().size());
		assertEquals("term", entry.getCategories().get(0).getTerm());
		assertEquals("label", entry.getCategories().get(0).getLabel());
		assertEquals("http://foo", entry.getCategories().get(0).getScheme());

		ValueSet resource = (ValueSet) entry.getResource();
		assertEquals("LOINC Codes for Cholesterol", resource.getName().getValue());
		assertEquals(summaryText.trim(), entry.getSummary().getValueAsString().trim());

		TagList tl = (TagList) resource.getResourceMetadata().get(ResourceMetadataKeyEnum.TAG_LIST);
		assertEquals(1, tl.size());
		assertEquals("term", tl.get(0).getTerm());
		assertEquals("label", tl.get(0).getLabel());
		assertEquals("http://foo", tl.get(0).getScheme());

		assertEquals("256a5231-a2bb-49bd-9fea-f349d428b70d", resource.getId().getUnqualifiedId());

		msg = msg.replace("<link href=\"http://hl7.org/implement/standards/fhir/valueset/256a5231-a2bb-49bd-9fea-f349d428b70d\" rel=\"self\"/>", "<link href=\"http://hl7.org/implement/standards/fhir/valueset/256a5231-a2bb-49bd-9fea-f349d428b70d/_history/12345\" rel=\"self\"/>");
		entry = p.parseBundle(msg).getEntries().get(0);
		resource = (ValueSet) entry.getResource();
		assertEquals("256a5231-a2bb-49bd-9fea-f349d428b70d", resource.getId().getUnqualifiedId());
		assertEquals("12345", resource.getId().getUnqualifiedVersionId());
		assertEquals("12345", ((IdDt)resource.getResourceMetadata().get(ResourceMetadataKeyEnum.VERSION_ID)).getUnqualifiedVersionId());

	}

	@Test
	public void testParseBundleDeletedEntry() {

		//@formatter:off
		String msg = "<feed xmlns=\"http://www.w3.org/2005/Atom\">" + 
				"<title>FHIR Core Valuesets</title>" + 
				"<id>http://hl7.org/fhir/profile/valuesets</id>" + 
				"<link rel=\"self\" href=\"http://hl7.org/implement/standards/fhir/valuesets.xml\"/>" + 
				"<updated>2014-02-10T04:11:24.435+00:00</updated>" +
				"<at:deleted-entry xmlns:at=\"http://purl.org/atompub/tombstones/1.0\" ref=\"http://foo/Patient/1\" when=\"2013-02-10T04:11:24.435+00:00\">" + 
				"<link rel=\"self\" href=\"http://foo/Patient/1/_history/2\"/>" + 
				"</at:deleted-entry>" +
				"</feed>";
		//@formatter:on

		IParser p = ourCtx.newXmlParser();
		Bundle bundle = p.parseBundle(msg);

		BundleEntry entry = bundle.getEntries().get(0);
		assertEquals("http://foo/Patient/1", entry.getId().getValue());
		assertEquals("2013-02-10T04:11:24.435+00:00", entry.getDeletedAt().getValueAsString());
		assertEquals("http://foo/Patient/1/_history/2", entry.getLinkSelf().getValue());
		assertEquals("1", entry.getResource().getId().getUnqualifiedId());
		assertEquals("2", entry.getResource().getId().getUnqualifiedVersionId());
		assertEquals("2", ((IdDt)entry.getResource().getResourceMetadata().get(ResourceMetadataKeyEnum.VERSION_ID)).getUnqualifiedVersionId());
		assertEquals(new InstantDt("2013-02-10T04:11:24.435+00:00"), entry.getResource().getResourceMetadata().get(ResourceMetadataKeyEnum.DELETED_AT));
		
		ourLog.info(ourCtx.newXmlParser().setPrettyPrint(true).encodeBundleToString(bundle));
		
		String encoded = ourCtx.newXmlParser().encodeBundleToString(bundle);
		assertEquals(msg,encoded);
		
	}

	
	@Test
	public void testParseBundleLarge() throws IOException {

		String msg = IOUtils.toString(XmlParser.class.getResourceAsStream("/atom-document-large.xml"));
		IParser p = ourCtx.newXmlParser();
		Bundle bundle = p.parseBundle(msg);

		assertEquals("http://spark.furore.com/fhir/_snapshot?id=327d6bb9-83b0-4929-aa91-6dd9c41e587b&start=0&_count=20", bundle.getLinkSelf().getValue());
		assertEquals("Patient resource with id 3216379", bundle.getEntries().get(0).getTitle().getValue());
		assertEquals("http://spark.furore.com/fhir/Patient/3216379", bundle.getEntries().get(0).getId().getValue());
		assertEquals("3216379", bundle.getEntries().get(0).getResource().getId().getUnqualifiedId());

	}

	@Test
	public void testParseContainedResources() throws IOException {

		String msg = IOUtils.toString(XmlParser.class.getResourceAsStream("/contained-diagnosticreport.xml"));
		IParser p = ourCtx.newXmlParser();
		DiagnosticReport bundle = p.parseResource(DiagnosticReport.class, msg);

		ResourceReferenceDt result0 = bundle.getResult().get(0);
		Observation obs = (Observation) result0.getResource();

		assertNotNull(obs);
		assertEquals("718-7", obs.getName().getCoding().get(0).getCode().getValue());

	}

	/**
	 * This sample has extra elements in <searchParam> that are not actually a
	 * part of the spec any more..
	 */
	@Test
	public void testParseFuroreMetadataWithExtraElements() throws IOException {
		String msg = IOUtils.toString(XmlParserTest.class.getResourceAsStream("/furore-conformance.xml"));

		IParser p = new FhirContext(ValueSet.class).newXmlParser();
		Conformance conf = p.parseResource(Conformance.class, msg);
		RestResource res = conf.getRestFirstRep().getResourceFirstRep();
		assertEquals("_id", res.getSearchParam().get(1).getName().getValue());
	}

	@BeforeClass
	public static void beforeClass() {
		XMLUnit.setIgnoreAttributeOrder(true);
		XMLUnit.setIgnoreComments(true);
		XMLUnit.setIgnoreWhitespace(true);
		ourCtx = new FhirContext();
	}

	@Test
	public void testMoreExtensions() throws Exception {

		Patient patient = new Patient();
		patient.addIdentifier(IdentifierUseEnum.OFFICIAL, "urn:example", "7000135", null);

		ExtensionDt ext = new ExtensionDt();
		ext.setModifier(false);
		ext.setUrl("http://example.com/extensions#someext");
		ext.setValue(new DateTimeDt("2011-01-02T11:13:15"));

		// Add the extension to the resource
		patient.addUndeclaredExtension(ext);
		// END SNIPPET: resourceExtension

		// START SNIPPET: resourceStringExtension
		HumanNameDt name = patient.addName();
		name.addFamily().setValue("Shmoe");
		StringDt given = name.addGiven();
		given.setValue("Joe");
		ExtensionDt ext2 = new ExtensionDt(false, "http://examples.com#givenext", new StringDt("given"));
		given.addUndeclaredExtension(ext2);
		// END SNIPPET: resourceStringExtension

		// START SNIPPET: subExtension
		ExtensionDt parent = new ExtensionDt(false, "http://example.com#parent");
		patient.addUndeclaredExtension(parent);

		ExtensionDt child1 = new ExtensionDt(false, "http://example.com#child", new StringDt("value1"));
		parent.addUndeclaredExtension(child1);

		ExtensionDt child2 = new ExtensionDt(false, "http://example.com#child", new StringDt("value1"));
		parent.addUndeclaredExtension(child2);
		// END SNIPPET: subExtension

		String output = new FhirContext().newXmlParser().setPrettyPrint(true).encodeResourceToString(patient);
		ourLog.info(output);

		String enc = new FhirContext().newXmlParser().encodeResourceToString(patient);
		assertThat(enc, containsString("<Patient xmlns=\"http://hl7.org/fhir\"><extension url=\"http://example.com/extensions#someext\"><valueDateTime value=\"2011-01-02T11:13:15\"/></extension>"));
		assertThat(enc, containsString("<extension url=\"http://example.com#parent\"><extension url=\"http://example.com#child\"><valueString value=\"value1\"/></extension><extension url=\"http://example.com#child\"><valueString value=\"value1\"/></extension></extension>"));
		assertThat(enc, containsString("<given value=\"Joe\"><extension url=\"http://examples.com#givenext\"><valueString value=\"given\"/></extension></given>"));
	}

	@Test
	public void testExtensionOnComposite() throws Exception {

		Patient patient = new Patient();

		HumanNameDt name = patient.addName();
		name.addFamily().setValue("Shmoe");
		HumanNameDt given = name.addGiven("Joe");
		ExtensionDt ext2 = new ExtensionDt(false, "http://examples.com#givenext", new StringDt("Hello"));
		given.addUndeclaredExtension(ext2);
		String output = new FhirContext().newXmlParser().setPrettyPrint(true).encodeResourceToString(patient);
		ourLog.info(output);

		String enc = new FhirContext().newXmlParser().encodeResourceToString(patient);
		assertThat(enc, containsString("<name><extension url=\"http://examples.com#givenext\"><valueString value=\"Hello\"/></extension><family value=\"Shmoe\"/><given value=\"Joe\"/></name>"));

		Patient parsed = new FhirContext().newXmlParser().parseResource(Patient.class, new StringReader(enc));
		assertEquals(1, parsed.getNameFirstRep().getUndeclaredExtensionsByUrl("http://examples.com#givenext").size());
		ExtensionDt ext = parsed.getNameFirstRep().getUndeclaredExtensionsByUrl("http://examples.com#givenext").get(0);
		assertEquals("Hello", ext.getValueAsPrimitive().getValue());

	}

	@Test
	public void testExtensionOnPrimitive() throws Exception {

		Patient patient = new Patient();

		HumanNameDt name = patient.addName();
		StringDt family = name.addFamily();
		family.setValue("Shmoe");

		ExtensionDt ext2 = new ExtensionDt(false, "http://examples.com#givenext", new StringDt("Hello"));
		family.addUndeclaredExtension(ext2);
		String output = new FhirContext().newXmlParser().setPrettyPrint(true).encodeResourceToString(patient);
		ourLog.info(output);

		String enc = new FhirContext().newXmlParser().encodeResourceToString(patient);
		assertThat(enc, containsString("<name><family value=\"Shmoe\"><extension url=\"http://examples.com#givenext\"><valueString value=\"Hello\"/></extension></family></name>"));

		Patient parsed = new FhirContext().newXmlParser().parseResource(Patient.class, new StringReader(enc));
		assertEquals(1, parsed.getNameFirstRep().getFamilyFirstRep().getUndeclaredExtensionsByUrl("http://examples.com#givenext").size());
		ExtensionDt ext = parsed.getNameFirstRep().getFamilyFirstRep().getUndeclaredExtensionsByUrl("http://examples.com#givenext").get(0);
		assertEquals("Hello", ext.getValueAsPrimitive().getValue());

	}

	@Test
	public void testSimpleResourceEncode() throws IOException, SAXException {

		String xmlString = IOUtils.toString(JsonParser.class.getResourceAsStream("/example-patient-general.json"), Charset.forName("UTF-8"));
		Patient obs = ourCtx.newJsonParser().parseResource(Patient.class, xmlString);

		List<ExtensionDt> undeclaredExtensions = obs.getContact().get(0).getName().getFamily().get(0).getUndeclaredExtensions();
		ExtensionDt undeclaredExtension = undeclaredExtensions.get(0);
		assertEquals("http://hl7.org/fhir/Profile/iso-21090#qualifier", undeclaredExtension.getUrl().getValue());

		ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToWriter(obs, new OutputStreamWriter(System.out));

		IParser jsonParser = ourCtx.newXmlParser();
		String encoded = jsonParser.encodeResourceToString(obs);
		ourLog.info(encoded);

		String jsonString = IOUtils.toString(JsonParser.class.getResourceAsStream("/example-patient-general.xml"));

		String expected = (jsonString);
		String actual = (encoded.trim());

		Diff d = new Diff(new StringReader(expected), new StringReader(actual));
		assertTrue(d.toString(), d.identical());

	}

	@Test
	public void testSimpleResourceEncodeWithCustomType() throws IOException, SAXException {

		FhirContext fhirCtx = new FhirContext(MyObservationWithExtensions.class);
		String xmlString = IOUtils.toString(JsonParser.class.getResourceAsStream("/example-patient-general.json"), Charset.forName("UTF-8"));
		MyObservationWithExtensions obs = fhirCtx.newJsonParser().parseResource(MyObservationWithExtensions.class, xmlString);

		assertEquals(0, obs.getAllUndeclaredExtensions().size());
		assertEquals("aaaa", obs.getExtAtt().getContentType().getValue());
		assertEquals("str1", obs.getMoreExt().getStr1().getValue());
		assertEquals("2011-01-02", obs.getModExt().getValueAsString());

		List<ExtensionDt> undeclaredExtensions = obs.getContact().get(0).getName().getFamily().get(0).getUndeclaredExtensions();
		ExtensionDt undeclaredExtension = undeclaredExtensions.get(0);
		assertEquals("http://hl7.org/fhir/Profile/iso-21090#qualifier", undeclaredExtension.getUrl().getValue());

		fhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToWriter(obs, new OutputStreamWriter(System.out));

		IParser jsonParser = fhirCtx.newXmlParser();
		String encoded = jsonParser.encodeResourceToString(obs);
		ourLog.info(encoded);

		String jsonString = IOUtils.toString(JsonParser.class.getResourceAsStream("/example-patient-general.xml"));

		String expected = (jsonString);
		String actual = (encoded.trim());

		Diff d = new Diff(new StringReader(expected), new StringReader(actual));
		assertTrue(d.toString(), d.identical());

	}

}
