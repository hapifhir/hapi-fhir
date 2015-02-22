package ca.uhn.fhir.parser;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.stringContainsInOrder;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.hamcrest.core.IsNot;
import org.hamcrest.core.StringContains;
import org.hamcrest.text.StringContainsInOrder;
import org.hl7.fhir.instance.model.Address;
import org.hl7.fhir.instance.model.Address.AddressUse;
import org.hl7.fhir.instance.model.Binary;
import org.hl7.fhir.instance.model.Bundle;
import org.hl7.fhir.instance.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.instance.model.CodeableConcept;
import org.hl7.fhir.instance.model.Composition;
import org.hl7.fhir.instance.model.Conformance;
import org.hl7.fhir.instance.model.Conformance.ConformanceRestResourceComponent;
import org.hl7.fhir.instance.model.DateTimeType;
import org.hl7.fhir.instance.model.DateType;
import org.hl7.fhir.instance.model.DecimalType;
import org.hl7.fhir.instance.model.DiagnosticReport;
import org.hl7.fhir.instance.model.DocumentManifest;
import org.hl7.fhir.instance.model.DocumentManifest.DocumentReferenceStatus;
import org.hl7.fhir.instance.model.Extension;
import org.hl7.fhir.instance.model.HumanName;
import org.hl7.fhir.instance.model.IBaseResource;
import org.hl7.fhir.instance.model.IPrimitiveType;
import org.hl7.fhir.instance.model.Identifier;
import org.hl7.fhir.instance.model.Identifier.IdentifierUse;
import org.hl7.fhir.instance.model.InstantType;
import org.hl7.fhir.instance.model.Narrative.NarrativeStatus;
import org.hl7.fhir.instance.model.Observation;
import org.hl7.fhir.instance.model.Organization;
import org.hl7.fhir.instance.model.Patient;
import org.hl7.fhir.instance.model.Profile;
import org.hl7.fhir.instance.model.Reference;
import org.hl7.fhir.instance.model.Resource;
import org.hl7.fhir.instance.model.Specimen;
import org.hl7.fhir.instance.model.StringType;
import org.hl7.fhir.instance.model.ValueSet;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.SAXException;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.base.composite.BaseNarrativeDt;
import ca.uhn.fhir.narrative.INarrativeGenerator;
import ca.uhn.fhir.parser.JsonParserTest.MyPatientWithOneDeclaredAddressExtension;
import ca.uhn.fhir.parser.JsonParserTest.MyPatientWithOneDeclaredExtension;

public class XmlParserTest {

	private static FhirContext ourCtx;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(XmlParserTest.class);

	@BeforeClass
	public static void beforeClass2() {
		 System.setProperty("file.encoding", "ISO-8859-1");
	}
	
	@Test
	public void testProfileWithBoundCode() throws IOException {
		String content = IOUtils.toString(XmlParserTest.class.getResourceAsStream("/DMIXAuditException.xml"), "UTF-8");
		ourCtx.newXmlParser().parseResource(Profile.class, content);	
	}
	
	@Test
	public void testEncodeBinaryWithNoContentType() {
		Binary b = new Binary();
		b.setContent(new byte[] {1,2,3,4});
		
		String output = ourCtx.newXmlParser().encodeResourceToString(b);
		ourLog.info(output);
		
		assertEquals("<Binary xmlns=\"http://hl7.org/fhir\">AQIDBA==</Binary>", output);
	}
	
	
	@Test
	public void testEncodeNonContained() {
		// Create an organization
		Organization org = new Organization();
		org.setId("Organization/65546");
		org.getNameElement().setValue("Contained Test Organization");

		// Create a patient
		Patient patient = new Patient();
		patient.setId("Patient/1333");
		patient.addIdentifier().setSystem("urn:mrns").setValue("253345");
		patient.getManagingOrganization().setResource(org);
		
		// Create a list containing both resources. In a server method, you might just
		// return this list, but here we will create a bundle to encode.
		List<Resource> resources = new ArrayList<Resource>();
		resources.add(org);
		resources.add(patient);		
		
		// Create a bundle with both
		Bundle b = new Bundle();
		b.addEntry().setResource(org);
		b.addEntry().setResource(patient);
		
		// Encode the buntdle
		String encoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(b);
		ourLog.info(encoded);
		assertThat(encoded, not(containsString("<contained>")));
		assertThat(encoded, containsString("<reference value=\"Organization/65546\"/>"));
		
		encoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(patient);
		ourLog.info(encoded);
		assertThat(encoded, not(containsString("<contained>")));
		assertThat(encoded, containsString("<reference value=\"Organization/65546\"/>"));
		
		
	}
	
	@Test
	public void testEncodeContainedWithNarrativeIsSuppresed() throws Exception {
		IParser parser = ourCtx.newXmlParser().setPrettyPrint(true);

		// Create an organization, note that the organization does not have an ID
		Organization org = new Organization();
		org.getNameElement().setValue("Contained Test Organization");
		org.getText().setDivAsString("<div>FOOBAR</div>");

		// Create a patient
		Patient patient = new Patient();
		patient.setId("Patient/1333");
		patient.addIdentifier().setSystem("urn:mrns").setValue( "253345");
		patient.getText().setDivAsString("<div>BARFOO</div>");
		patient.getManagingOrganization().setResource(org);
		
		String encoded = parser.encodeResourceToString(patient);
		ourLog.info(encoded);
		assertThat(encoded, not(containsString("FOOBAR")));
		assertThat(encoded, (containsString("BARFOO")));
		
	}	

	
	@Test
	public void testEncodeContained() {
		IParser xmlParser = ourCtx.newXmlParser().setPrettyPrint(true);
		
		// Create an organization, note that the organization does not have an ID
		Organization org = new Organization();
		org.getNameElement().setValue("Contained Test Organization");

		// Create a patient
		Patient patient = new Patient();
		patient.setId("Patient/1333");
		patient.addIdentifier().setSystem("urn:mrns").setValue("253345");
		
		// Put the organization as a reference in the patient resource
		patient.getManagingOrganization().setResource(org);
		
		String encoded = xmlParser.encodeResourceToString(patient);
		ourLog.info(encoded);
		assertThat(encoded, containsString("<contained>"));
		assertThat(encoded, containsString("<reference value=\"#1\"/>"));
		
		// Create a bundle with just the patient resource
		Bundle b = new Bundle();
		b.addEntry().setResource(patient);
		
		// Encode the buntdle
		encoded = xmlParser.encodeResourceToString(b);
		ourLog.info(encoded);
		assertThat(encoded, stringContainsInOrder(Arrays.asList("<contained>","id=\"1\"", "</contained>")));
		assertThat(encoded, containsString("<reference value=\"#1\"/>"));
		assertThat(encoded, stringContainsInOrder(Arrays.asList("<entry>", "</entry>")));
		assertThat(encoded, not(stringContainsInOrder(Arrays.asList("<entry>", "</entry>", "<entry>"))));
		
		// Re-parse the bundle
		patient = (Patient) xmlParser.parseResource(xmlParser.encodeResourceToString(patient));
		assertEquals("#1", patient.getManagingOrganization().getReference());
		
		assertNotNull(patient.getManagingOrganization().getResource());
		org = (Organization) patient.getManagingOrganization().getResource();
		assertEquals("#1", org.getId());
		assertEquals("Contained Test Organization", org.getName());
		
		// And re-encode a second time
		encoded = xmlParser.encodeResourceToString(patient);
		ourLog.info(encoded);
		assertThat(encoded, stringContainsInOrder(Arrays.asList("<contained>", "<Organization ", "id=\"1\"", "</Organization", "</contained>", "<reference value=\"#1\"/>")));
		assertThat(encoded, not(stringContainsInOrder(Arrays.asList("<contained>",  "<Org", "<contained>"))));
		assertThat(encoded, containsString("<reference value=\"#1\"/>"));

		// And re-encode once more, with the references cleared
		patient.getContained().clear();
		patient.getManagingOrganization().setReference(null);
		encoded = xmlParser.encodeResourceToString(patient);
		ourLog.info(encoded);
		assertThat(encoded, stringContainsInOrder(Arrays.asList("<contained>", "<Organization ", "id=\"1\"", "</Organization", "</contained>", "<reference value=\"#1\"/>")));
		assertThat(encoded, not(stringContainsInOrder(Arrays.asList("<contained>",  "<Org", "<contained>"))));
		assertThat(encoded, containsString("<reference value=\"#1\"/>"));

		// And re-encode once more, with the references cleared and a manually set local ID
		patient.getContained().clear();
		patient.getManagingOrganization().setReference(null);
		patient.getManagingOrganization().getResource().setId(("#333"));
		encoded = xmlParser.encodeResourceToString(patient);
		ourLog.info(encoded);
		assertThat(encoded, stringContainsInOrder(Arrays.asList("<contained>", "<Organization ", "id=\"333\"", "</Organization", "</contained>", "<reference value=\"#333\"/>")));
		assertThat(encoded, not(stringContainsInOrder(Arrays.asList("<contained>",  "<Org", "<contained>"))));
		
	}
	
	
	/**
	 * Thanks to Alexander Kley!
	 */
	@Test
	public void testParseContainedBinaryResource() {
		byte[] bin = new byte[] {0,1,2,3,4};
	    final Binary binary = new Binary();
	    binary.setContentType("PatientConsent").setContent( bin);
//	    binary.setId(UUID.randomUUID().toString());
	    
	    DocumentManifest manifest = new DocumentManifest();
//	    manifest.setId(UUID.randomUUID().toString());
	    CodeableConcept cc = new CodeableConcept();
	    cc.addCoding().setSystem("mySystem").setCode( "PatientDocument");
		manifest.setType(cc);
	    manifest.setMasterIdentifier(new Identifier().setSystem("mySystem").setValue( UUID.randomUUID().toString()));
	    manifest.addContent().setResource(binary);
	    manifest.setStatus(DocumentReferenceStatus.CURRENT);
	    
	    String encoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(manifest);
	    ourLog.info(encoded);
	    assertThat(encoded, StringContainsInOrder.stringContainsInOrder(Arrays.asList("contained>","<Binary", "</contained>")));
	    
	    DocumentManifest actual = ourCtx.newXmlParser().parseResource(DocumentManifest.class, encoded);
	    assertEquals(1, actual.getContained().size());
	    assertEquals(1, actual.getContent().size());
	    assertNotNull(actual.getContent().get(0).getResource());
	    
	}
	
	
	@Test
	public void testComposition() {
		
		Composition comp = new Composition();
		comp.setId("1");
		
		ourCtx.newXmlParser().encodeResourceToString(comp);
		ourCtx.newXmlParser().encodeResourceToString(comp);
		ourCtx.newXmlParser().encodeResourceToString(comp);
		ourCtx.newXmlParser().encodeResourceToString(comp);
		
//		comp.
		
	}
	
	

	@Test
	public void testEncodeBinaryResource() {

		Binary patient = new Binary();
		patient.setContentType("foo");
		patient.setContent(new byte[] { 1, 2, 3, 4 });

		String val = ourCtx.newXmlParser().encodeResourceToString(patient);
		assertEquals("<Binary xmlns=\"http://hl7.org/fhir\" contentType=\"foo\">AQIDBA==</Binary>", val);

	}

	@Test
	public void testEncodeBoundCode() {

		Patient patient = new Patient();
		patient.addAddress().setUse(AddressUse.HOME);

		patient.getGenderElement().setValue(org.hl7.fhir.instance.model.Patient.AdministrativeGender.MALE);

		String val = ourCtx.newXmlParser().encodeResourceToString(patient);
		ourLog.info(val);

		assertThat(val, containsString("home"));
		assertThat(val, containsString("male"));
	}

	@Test
	public void testEncodeBundle() throws InterruptedException {
		Bundle b = new Bundle();
		b.getMeta().addTag().setSystem("http://hl7.org/fhir/tag").setCode( "http://hl7.org/fhir/tag/message").setDisplay("Message");

		InstantType pub = InstantType.withCurrentTime();
		b.getMeta().setLastUpdatedElement(pub);

		Patient p1 = new Patient();
		p1.addName().addFamily("Family1");
		BundleEntryComponent entry = b.addEntry();
		p1.getIdElement().setValue("1");
		entry.setResource(p1);

		Patient p2 = new Patient();
		p2.addName().addFamily("Family2");
		entry = b.addEntry();
		p2.getIdElement().setValue("2");
		entry.setResource(p2);

		String bundleString = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(b);
		ourLog.info(bundleString);

		List<String> strings = new ArrayList<String>();
		strings.addAll(Arrays.asList("<published>", pub.getValueAsString(), "</published>"));
		strings.add("<category term=\"http://hl7.org/fhir/tag/message\" label=\"Message\" scheme=\"http://hl7.org/fhir/tag\"/>");
		strings.addAll(Arrays.asList("<entry>", "<id>1</id>", "</Patient>", "<summary type=\"xhtml\">", "<div", "</entry>"));
		strings.addAll(Arrays.asList("<entry>", "<id>2</id>", "<link rel=\"alternate\" href=\"http://foo/bar\"/>", "<link rel=\"search\" href=\"http://foo/bar/search\"/>", "</entry>"));
		assertThat(bundleString, StringContainsInOrder.stringContainsInOrder(strings));
		assertThat(bundleString, not(containsString("at:by")));

	}

	@Test
	public void testEncodeBundleCategory() {

		Bundle b = new Bundle();
		BundleEntryComponent e = b.addEntry();
		e.setResource(new Patient());
		e.getResource().getMeta().addTag().setSystem("scheme").setCode( "term").setDisplay( "label");

		String val = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(b);
		ourLog.info(val);

		assertThat(val, StringContains.containsString("<category term=\"term\" label=\"label\" scheme=\"scheme\"/>"));

		b = ourCtx.newXmlParser().parseResource(Bundle.class, val);
		assertEquals(1, b.getEntry().size());
		assertEquals(1, b.getEntry().get(0).getResource().getMeta().getTag().size());
		assertEquals("scheme", b.getEntry().get(0).getResource().getMeta().getTag().get(0).getSystem());
		assertEquals("term", b.getEntry().get(0).getResource().getMeta().getTag().get(0).getCode());
		assertEquals("label", b.getEntry().get(0).getResource().getMeta().getTag().get(0).getDisplay());
		assertNull(b.getEntry().get(0).getResource());

	}


	@Test
	public void testEncodeEscapedChars() {

		Patient p = new Patient();
		p.addName().addFamily("and <>&ü");

		String enc = ourCtx.newXmlParser().encodeResourceToString(p);
		ourLog.info(enc);

		p = ourCtx.newXmlParser().parseResource(Patient.class, enc);
		assertEquals("and <>&ü", p.getName().get(0).getFamily().get(0).getValue());

		p = ourCtx.newXmlParser().parseResource(Patient.class, "<Patient xmlns=\"http://hl7.org/fhir\"><name><family value=\"quot &quot;\"/></name></Patient>");
		assertEquals("quot \"", p.getName().get(0).getFamily().get(0).getValue());

	}

	@Test
	public void testEncodeEscapedExtendedChars() {
		Patient p = ourCtx.newXmlParser().parseResource(Patient.class, "<Patient xmlns=\"http://hl7.org/fhir\"><name><family value=\"uuml &uuml;\"/></name></Patient>");
		assertEquals("uuml ü", p.getName().get(0).getFamily().get(0).getValue());
	}

	@Test
	public void testEncodeContainedAndIncludedResources() {

		DiagnosticReport rpt = new DiagnosticReport();
		rpt.getName().setText("Report");

		Specimen spm = new Specimen();
		spm.addIdentifier().setLabel("Report1ContainedSpecimen1");
		rpt.addSpecimen().setResource(spm);

		IParser p = ourCtx.newXmlParser().setPrettyPrint(true);
		String str = p.encodeResourceToString(rpt);

		ourLog.info(str);

	}

	@Test
	public void testEncodeContainedResources() throws Exception {

		DiagnosticReport rpt = new DiagnosticReport();
		Specimen spm = new Specimen();
		spm.addIdentifier().setSystem("urn").setValue( "123");
		rpt.getText().setDivAsString("AAA");
		rpt.addSpecimen().setResource(spm);

		IParser p = ourCtx.newXmlParser().setPrettyPrint(true);
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
	public void testEncodeDeclaredExtensionWithAddressContent() {
		IParser parser = ourCtx.newXmlParser();

		MyPatientWithOneDeclaredAddressExtension patient = new MyPatientWithOneDeclaredAddressExtension();
		patient.addAddress().setUse(AddressUse.HOME);
		patient.setFoo(new Address().addLine("line1"));

		String val = parser.encodeResourceToString(patient);
		ourLog.info(val);
		assertThat(val, StringContains.containsString("<extension url=\"urn:foo\"><valueAddress><line value=\"line1\"/></valueAddress></extension>"));

		MyPatientWithOneDeclaredAddressExtension actual = parser.parseResource(MyPatientWithOneDeclaredAddressExtension.class, val);
		assertEquals(AddressUse.HOME, patient.getAddress().get(0).getUse());
		Address ref = actual.getFoo();
		assertEquals("line1", ref.getLine().get(0).getValue());

	}

	@Test
	public void testEncodeDeclaredExtensionWithResourceContent() {
		IParser parser = ourCtx.newXmlParser();

		MyPatientWithOneDeclaredExtension patient = new MyPatientWithOneDeclaredExtension();
		patient.addAddress().setUse(AddressUse.HOME);
		patient.setFoo(new Reference("Organization/123"));

		String val = parser.encodeResourceToString(patient);
		ourLog.info(val);
		assertThat(val, StringContains.containsString("<extension url=\"urn:foo\"><valueResource><reference value=\"Organization/123\"/></valueResource></extension>"));

		MyPatientWithOneDeclaredExtension actual = parser.parseResource(MyPatientWithOneDeclaredExtension.class, val);
		assertEquals(AddressUse.HOME, patient.getAddress().get(0).getUse());
		Reference ref = actual.getFoo();
		assertEquals("Organization/123", ref.getReference());

	}

	@Test
	public void testEncodeExtensionWithResourceContent() {
		IParser parser = ourCtx.newXmlParser();

		Patient patient = new Patient();
		patient.addAddress().setUse(AddressUse.HOME);
		patient.addExtension().setUrl("urn:foo").setValue( new Reference("Organization/123"));

		String val = parser.encodeResourceToString(patient);
		ourLog.info(val);
		assertThat(val, StringContains.containsString("<extension url=\"urn:foo\"><valueResource><reference value=\"Organization/123\"/></valueResource></extension>"));

		Patient actual = parser.parseResource(Patient.class, val);
		assertEquals(AddressUse.HOME, patient.getAddress().get(0).getUse());
		List<Extension> ext = actual.getExtension();
		assertEquals(1, ext.size());
		Reference ref = (Reference) ext.get(0).getValue();
		assertEquals("Organization/123", ref.getReference());

	}

	@Test
	public void testEncodeInvalidChildGoodException() {
		Observation obs = new Observation();
		obs.setValue(new DecimalType(112.22));

		IParser p = ourCtx.newJsonParser();

		try {
			p.encodeResourceToString(obs);
		} catch (DataFormatException e) {
			assertThat(e.getMessage(), StringContains.containsString("DecimalType"));
		}
	}

	@Test
	public void testEncodeNarrativeBlockInBundle() throws Exception {
		Patient p = new Patient();
		p.addIdentifier().setSystem("foo").setValue("bar");
		p.getText().setStatus(NarrativeStatus.GENERATED);
		p.getText().setDivAsString("<div>hello</div>");

		Bundle b = new Bundle();
		b.setTotal(123);
		b.addEntry().setResource(p);

		String out = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(b);
		ourLog.info(out);
		assertThat(out, containsString("<div xmlns=\"http://www.w3.org/1999/xhtml\">hello</div>"));

		p.getText().setDivAsString("<xhtml:div xmlns:xhtml=\"http://www.w3.org/1999/xhtml\">hello</xhtml:div>");
		out = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(b);
		ourLog.info(out);
		assertThat(out, containsString("<xhtml:div xmlns:xhtml=\"http://www.w3.org/1999/xhtml\">hello</xhtml:div>"));

	}

	@Test
	public void testEncodePrettyPrint() throws Exception {

		Patient patient = new Patient();
		patient.getText().setDivAsString("<div>\n  <i>  hello     <pre>\n  LINE1\n  LINE2</pre></i>\n\n\n\n</div>");
		patient.addName().addFamily("Family").addGiven("Given");

		//@formatter:off
		String encoded = ourCtx.newXmlParser().setPrettyPrint(false).encodeResourceToString(patient);
		ourLog.info(encoded);
		/*
		 * Note at least one space is placed where any whitespace was, as
		 * it is hard to tell what whitespace had no purpose
		 */
		String expected = "<Patient xmlns=\"http://hl7.org/fhir\"><text><div xmlns=\"http://www.w3.org/1999/xhtml\">"
				+ " <i> hello "
				+ "<pre>\n  LINE1\n  LINE2</pre>"
				+ "</i> </div></text><name><family value=\"Family\"/><given value=\"Given\"/></name></Patient>";
		assertEquals(expected, encoded);

		encoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(patient);
		ourLog.info(encoded);
		expected = "<Patient xmlns=\"http://hl7.org/fhir\">\n"
				+ "   <text>\n"
				+ "      <div xmlns=\"http://www.w3.org/1999/xhtml\"> \n"  
				+ "         <i> hello \n" 
				+ "            <pre>\n  LINE1\n  LINE2</pre>\n"
				+ "         </i> \n"
				+ "      </div>\n"
				+ "   </text>\n"
				+ "   <name>\n"
				+ "      <family value=\"Family\"/>\n"
				+ "      <given value=\"Given\"/>\n"
				+ "   </name>\n"
				+ "</Patient>";
		//@formatter:on

		// Whitespace should be preserved and not reformatted in narrative blocks
		assertEquals(expected, encoded);

	}


	@Test
	public void testEncodeResourceRef() throws DataFormatException {

		Patient patient = new Patient();
		patient.setManagingOrganization(new Reference());

		IParser p = ourCtx.newXmlParser();
		String str = p.encodeResourceToString(patient);
		assertThat(str, IsNot.not(StringContains.containsString("managingOrganization")));

		Reference ref = new Reference();
		ref.setReference("Organization/123");
		ref.setDisplay("DISPLAY!");
		patient.setManagingOrganization(ref);
		str = p.encodeResourceToString(patient);
		assertThat(str, StringContains.containsString("<managingOrganization><reference value=\"Organization/123\"/><display value=\"DISPLAY!\"/></managingOrganization>"));

		Organization org = new Organization();
		org.addIdentifier().setSystem("foo").setValue("bar");
		patient.setManagingOrganization(new Reference(org));
		str = p.encodeResourceToString(patient);
		assertThat(str, StringContains.containsString("<contained><Organization"));

	}

	@Test
	public void testEncodeUndeclaredExtensionWithAddressContent() {
		IParser parser = ourCtx.newXmlParser();

		Patient patient = new Patient();
		patient.addAddress().setUse(AddressUse.HOME);
		patient.addExtension().setUrl("urn:foo").setValue(new Address().addLine("line1"));

		String val = parser.encodeResourceToString(patient);
		ourLog.info(val);
		assertThat(val, StringContains.containsString("<extension url=\"urn:foo\"><valueAddress><line value=\"line1\"/></valueAddress></extension>"));

		MyPatientWithOneDeclaredAddressExtension actual = parser.parseResource(MyPatientWithOneDeclaredAddressExtension.class, val);
		assertEquals(AddressUse.HOME, patient.getAddress().get(0).getUse());
		Address ref = actual.getFoo();
		assertEquals("line1", ref.getLine().get(0).getValue());

	}

	@Test
	public void testExtensionOnComposite() throws Exception {

		Patient patient = new Patient();

		HumanName name = patient.addName();
		name.addFamily("Shmoe");
		HumanName given = name.addGiven("Joe");
		Extension ext2 = new Extension().setUrl("http://examples.com#givenext").setValue( new StringType("Hello"));
		given.getExtension().add(ext2);
		String output = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(patient);
		ourLog.info(output);

		String enc = ourCtx.newXmlParser().encodeResourceToString(patient);
		assertThat(enc, containsString("<name><extension url=\"http://examples.com#givenext\"><valueString value=\"Hello\"/></extension><family value=\"Shmoe\"/><given value=\"Joe\"/></name>"));

		Patient parsed = ourCtx.newXmlParser().parseResource(Patient.class, new StringReader(enc));
		assertEquals(1, parsed.getName().get(0).getExtension().size());
		Extension ext = parsed.getName().get(0).getExtension().get(0);
		assertEquals("Hello", ((IPrimitiveType<?>)ext.getValue()).getValue());

	}

	@Test
	public void testExtensionOnPrimitive() throws Exception {

		Patient patient = new Patient();

		HumanName name = patient.addName();
		StringType family = name.addFamilyElement();
		family.setValue("Shmoe");

		Extension ext2 = new Extension().setUrl("http://examples.com#givenext").setValue( new StringType("Hello"));
		family.getExtension().add(ext2);
		String output = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(patient);
		ourLog.info(output);

		String enc = ourCtx.newXmlParser().encodeResourceToString(patient);
		assertThat(enc, containsString("<name><family value=\"Shmoe\"><extension url=\"http://examples.com#givenext\"><valueString value=\"Hello\"/></extension></family></name>"));

		Patient parsed = ourCtx.newXmlParser().parseResource(Patient.class, new StringReader(enc));
		assertEquals(1, parsed.getName().get(0).getFamily().get(0).getExtension().size());
		Extension ext = parsed.getName().get(0).getFamily().get(0).getExtension().get(0);
		assertEquals("Hello", ((IPrimitiveType<?>)ext.getValue()).getValue());

	}

	@Test
	public void testExtensions() throws DataFormatException {

		MyPatient patient = new MyPatient();
		patient.setPetName(new StringType("Fido"));
		patient.getImportantDates().add(new DateTimeType("2010-01-02"));
		patient.getImportantDates().add(new DateTimeType("2014-01-26T11:11:11"));

		patient.addName().addFamily("Smith");

		IParser p = ourCtx.newXmlParser();
		String str = p.encodeResourceToString(patient);

		ourLog.info(str);

		assertThat(str, StringContains.containsString("<Patient xmlns=\"http://hl7.org/fhir\">"));
		assertThat(str, StringContains.containsString("<extension url=\"http://example.com/dontuse#petname\"><valueString value=\"Fido\"/></extension>"));
		assertThat(str, StringContains.containsString("<modifierExtension url=\"http://example.com/dontuse#importantDates\"><valueDateTime value=\"2010-01-02\"/></modifierExtension>"));
		assertThat(str, StringContains.containsString("<modifierExtension url=\"http://example.com/dontuse#importantDates\"><valueDateTime value=\"2014-01-26T11:11:11\"/></modifierExtension>"));
		assertThat(str, StringContains.containsString("<name><family value=\"Smith\"/></name>"));

	}

	@Test
	public void testLoadAndAncodeMessage() throws Exception {

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

		assertEquals(NarrativeStatus.GENERATED, patient.getText().getStatus());
		assertEquals("<div xmlns=\"http://www.w3.org/1999/xhtml\">John Cardinal:            444333333        </div>", patient.getText().getDiv().getValueAsString());
		assertEquals("PRP1660", patient.getIdentifier().get(0).getValue());

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
		assertEquals("IdentifierLabel", resource.getIdentifier().get(0).getLabel());
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
		assertEquals("IdentifierLabel", resource.getIdentifier().get(0).getLabel());
		assertEquals("Foo1Value", ((IPrimitiveType<?>)resource.getExtension().get(0).getValue()).getValueAsString());
		assertEquals("Foo1Value2", ((IPrimitiveType<?>)resource.getExtension().get(1).getValue()).getValueAsString());
		assertEquals("Foo2Value1", ((IPrimitiveType<?>)resource.getModifierExtension().get(0).getValue()).getValueAsString());

		assertEquals("2013-01-01", ((IPrimitiveType<?>)resource.getExtension().get(2).getExtension().get(0).getValue()).getValueAsString());
		assertEquals("2013-01-02", ((IPrimitiveType<?>)resource.getExtension().get(2).getExtension().get(1).getExtension().get(0).getValue()).getValueAsString());

		String encoded = p.encodeResourceToString(resource);
		ourLog.info(encoded);

		Diff d = new Diff(new StringReader(msg), new StringReader(encoded));
		assertTrue(d.toString(), d.identical());
	}

	@Test
	public void testLoadObservation() throws ConfigurationException, DataFormatException, IOException {

		IParser p = ourCtx.newXmlParser();

		String string = IOUtils.toString(XmlParserTest.class.getResourceAsStream("/observation-example-eeg.xml"), Charset.forName("UTF-8"));
		IBaseResource resource = p.parseResource(string);

		String result = p.encodeResourceToString(resource);
		ourLog.info(result);
	}

	
	@Test
	public void testLoadPatient() throws ConfigurationException, DataFormatException, IOException {

		IParser p = ourCtx.newXmlParser();

		String string = IOUtils.toString(XmlParserTest.class.getResourceAsStream("/patient-example-dicom.xml"), Charset.forName("UTF-8"));
		IBaseResource resource = p.parseResource(string);

		String result = p.encodeResourceToString(resource);
		ourLog.info(result);

		// Nothing

		string = IOUtils.toString(XmlParserTest.class.getResourceAsStream("/patient-example-us-extensions.xml"), Charset.forName("UTF-8"));
		resource = p.parseResource(string);

		result = p.encodeResourceToString(resource);
		ourLog.info(result);

	}

	@Test
	public void testLoadQuestionnaire() throws ConfigurationException, DataFormatException, IOException {

		IParser p = ourCtx.newXmlParser();

		String string = IOUtils.toString(XmlParserTest.class.getResourceAsStream("/questionnaire-example.xml"), Charset.forName("UTF-8"));
		IBaseResource resource = p.parseResource(string);

		String result = p.encodeResourceToString(resource);
		ourLog.info(result);
	}

	@Test
	public void testReEncode() throws SAXException, IOException {

		//@formatter:off
		String msg = "<Patient xmlns=\"http://hl7.org/fhir\">" 
				+ "<identifier><label value=\"SSN\" /><system value=\"http://orionhealth.com/mrn\" /><value value=\"PRP1660\" /></identifier>"
				+ "</Patient>";
		//@formatter:on

		Patient patient1 = ourCtx.newXmlParser().parseResource(Patient.class, msg);
		String encoded1 = ourCtx.newXmlParser().encodeResourceToString(patient1);

		Diff d = new Diff(new StringReader(msg), new StringReader(encoded1));
		assertTrue(d.toString(), d.identical());

	}

	@Test
	public void testEncodeAndParseExtensions() throws Exception {

		Patient patient = new Patient();
		patient.addIdentifier().setUse(IdentifierUse.OFFICIAL).setSystem("urn:example").setValue("7000135");

		Extension ext = new Extension();
		ext.setUrl("http://example.com/extensions#someext");
		ext.setValue(new DateTimeType("2011-01-02T11:13:15"));
		patient.getExtension().add(ext);

		Extension parent = new Extension().setUrl("http://example.com#parent");
		patient.getExtension().add(parent);
		Extension child1 = new Extension().setUrl( "http://example.com#child").setValue( new StringType("value1"));
		parent.getExtension().add(child1);
		Extension child2 = new Extension().setUrl( "http://example.com#child").setValue( new StringType("value2"));
		parent.getExtension().add(child2);
		
		Extension modExt = new Extension();
		modExt.setUrl("http://example.com/extensions#modext");
		modExt.setValue(new DateType("1995-01-02"));
		patient.getModifierExtension().add(modExt);

		HumanName name = patient.addName();
		name.addFamily("Blah");
		StringType given = name.addGivenElement();
		given.setValue("Joe");
		Extension ext2 = new Extension().setUrl("http://examples.com#givenext").setValue(new StringType("given"));
		given.getExtension().add(ext2);

		StringType given2 = name.addGivenElement();
		given2.setValue("Shmoe");
		Extension given2ext = new Extension().setUrl("http://examples.com#givenext_parent");
		given2.getExtension().add(given2ext);
		given2ext.addExtension().setUrl("http://examples.com#givenext_child").setValue(new StringType("CHILD"));

		String output = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(patient);
		ourLog.info(output);

		String enc = ourCtx.newXmlParser().encodeResourceToString(patient);
		assertThat(enc, containsString("<Patient xmlns=\"http://hl7.org/fhir\"><extension url=\"http://example.com/extensions#someext\"><valueDateTime value=\"2011-01-02T11:13:15\"/></extension>"));
		assertThat(enc, containsString("<modifierExtension url=\"http://example.com/extensions#modext\"><valueDate value=\"1995-01-02\"/></modifierExtension>"));
		assertThat(
				enc,
				containsString("<extension url=\"http://example.com#parent\"><extension url=\"http://example.com#child\"><valueString value=\"value1\"/></extension><extension url=\"http://example.com#child\"><valueString value=\"value2\"/></extension></extension>"));
		assertThat(enc, containsString("<given value=\"Joe\"><extension url=\"http://examples.com#givenext\"><valueString value=\"given\"/></extension></given>"));
		assertThat(enc, containsString("<given value=\"Shmoe\"><extension url=\"http://examples.com#givenext_parent\"><extension url=\"http://examples.com#givenext_child\"><valueString value=\"CHILD\"/></extension></extension></given>"));
		
		/*
		 * Now parse this back
		 */
		
		Patient parsed =ourCtx.newXmlParser().parseResource(Patient.class, enc); 
		ext = parsed.getExtension().get(0);
		assertEquals("http://example.com/extensions#someext", ext.getUrl());
		assertEquals("2011-01-02T11:13:15", ((DateTimeType)ext.getValue()).getValueAsString());

		parent = patient.getExtension().get(1);
		assertEquals("http://example.com#parent", parent.getUrl());
		assertNull(parent.getValue());
		child1 = parent.getExtension().get(0);
		assertEquals( "http://example.com#child", child1.getUrl());
		assertEquals("value1", ((StringType)child1.getValue()).getValueAsString());
		child2 = parent.getExtension().get(1);
		assertEquals( "http://example.com#child", child2.getUrl());
		assertEquals("value2", ((StringType)child2.getValue()).getValueAsString());

		modExt = parsed.getModifierExtension().get(0);
		assertEquals("http://example.com/extensions#modext", modExt.getUrl());
		assertEquals("1995-01-02", ((DateType)modExt.getValue()).getValueAsString());

		name = parsed.getName().get(0);

		ext2 = name.getGiven().get(0).getExtension().get(0);
		assertEquals("http://examples.com#givenext", ext2.getUrl());
		assertEquals("given", ((StringType)ext2.getValue()).getValueAsString());

		given2ext = name.getGiven().get(1).getExtension().get(0);
		assertEquals("http://examples.com#givenext_parent", given2ext.getUrl());
		assertNull(given2ext.getValue());
		Extension given2ext2 = given2ext.getExtension().get(0);
		assertEquals("http://examples.com#givenext_child", given2ext2.getUrl());
		assertEquals("CHILD", ((StringType)given2ext2.getValue()).getValue());

	}

	@Test
	public void testNarrativeGeneration() throws DataFormatException, IOException {

		Patient patient = new Patient();
		patient.addName().addFamily("Smith");
		Organization org = new Organization();
		patient.getManagingOrganization().setResource(org);

		INarrativeGenerator gen = new INarrativeGenerator() {

			@Override
			public void generateNarrative(String theProfile, IBaseResource theResource, BaseNarrativeDt<?> theNarrative) throws DataFormatException {
				theNarrative.getDiv().setValueAsString("<div>help</div>");
				theNarrative.getStatus().setValueAsString("generated");
			}

			@Override
			public void generateNarrative(IBaseResource theResource, BaseNarrativeDt<?> theNarrative) {
				throw new UnsupportedOperationException();
			}

			@Override
			public String generateTitle(IBaseResource theResource) {
				throw new UnsupportedOperationException();
			}

			@Override
			public String generateTitle(String theProfile, IBaseResource theResource) {
				throw new UnsupportedOperationException();
			}

			@Override
			public void setFhirContext(FhirContext theFhirContext) {
				// nothing
			}};
			
		FhirContext context = new FhirContext();
		context.setNarrativeGenerator(gen);
		IParser p = context.newXmlParser();
		String str = p.encodeResourceToString(patient);

		ourLog.info(str);

		assertThat(str, StringContains.containsString(",\"text\":{\"status\":\"generated\",\"div\":\"<div>help</div>\"},"));
	}

	@Test
	public void testDuplicateContainedResources() {

		Observation resA = new Observation();
		resA.getCode().setText("A");

		Observation resB = new Observation();
		resB.getCode().setText("B");
		resB.addRelated().setTarget(new Reference(resA));
		resB.addRelated().setTarget(new Reference(resA));

		String encoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(resB);
		ourLog.info(encoded);

		assertThat(encoded, stringContainsInOrder(Arrays.asList("<contained>", "<Observation", "</Observation>", "</contained>")));
		assertThat(encoded, not(stringContainsInOrder(Arrays.asList("<contained>", "<Observation", "</Observation>", "<Obser", "</contained>"))));

	}

	@Test
	public void testNestedContainedResources() {

		Observation A = new Observation();
		A.getCode().setText("A");

		Observation B = new Observation();
		B.getCode().setText("B");
		A.addRelated().setTarget(new Reference(B));

		Observation C = new Observation();
		C.getCode().setText("C");
		B.addRelated().setTarget(new Reference(C));

		String str = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(A);
		ourLog.info(str);

		assertThat(str, stringContainsInOrder(Arrays.asList("<text value=\"B\"/>", "<text value=\"C\"/>", "<text value=\"A\"/>")));
		assertThat(str, stringContainsInOrder(Arrays.asList("<contained>", "</contained>")));

		// Only one (outer) contained block
		int idx0 = str.indexOf("<contained>");
		int idx1 = str.indexOf("<contained>", idx0 + 1);
		assertNotEquals(-1, idx0);
		assertEquals(-1, idx1);

		Observation obs = ourCtx.newXmlParser().parseResource(Observation.class, str);
		assertEquals("A", obs.getCode().getText());

		Observation obsB = (Observation) obs.getRelated().get(0).getTarget().getResource();
		assertEquals("B", obsB.getCode().getText());

		Observation obsC = (Observation) obsB.getRelated().get(0).getTarget().getResource();
		assertEquals("C", obsC.getCode().getText());

	}

	@Test
	public void testParseBinaryResource() {

		Binary val = ourCtx.newXmlParser().parseResource(Binary.class, "<Binary xmlns=\"http://hl7.org/fhir\" contentType=\"foo\">AQIDBA==</Binary>");
		assertEquals("foo", val.getContentType());
		assertArrayEquals(new byte[] { 1, 2, 3, 4 }, val.getContent());

	}


	@Test
	public void testParseBundleWithMixedReturnTypes() {
		InputStreamReader str = new InputStreamReader(getClass().getResourceAsStream("/mixed-return-bundle.xml"));
		Bundle b = ourCtx.newXmlParser().parseResource(Bundle.class, str);
		assertEquals(Patient.class, b.getEntry().get(0).getResource().getClass());
		assertEquals(Patient.class, b.getEntry().get(1).getResource().getClass());
		assertEquals(Organization.class, b.getEntry().get(2).getResource().getClass());
	}

	@Test
	public void testParseContainedResources() throws IOException {

		String msg = IOUtils.toString(XmlParser.class.getResourceAsStream("/contained-diagnosticreport.xml"));
		IParser p = ourCtx.newXmlParser();
		DiagnosticReport bundle = p.parseResource(DiagnosticReport.class, msg);

		Reference result0 = bundle.getResult().get(0);
		Observation obs = (Observation) result0.getResource();

		assertNotNull(obs);
		assertEquals("718-7", obs.getCode().getCoding().get(0).getCode());

	}

	@Test
	public void testParseEncodeNarrative() {

		String input = "<Patient xmlns=\"http://hl7.org/fhir\"><text><status value=\"generated\"/><div xmlns=\"http://www.w3.org/1999/xhtml\"><div class=\"hapiHeaderText\"> Donald null <b>DUCK </b></div><table class=\"hapiPropertyTable\"><tbody><tr><td>Identifier</td><td>7000135</td></tr><tr><td>Address</td><td><span>10 Duxon Street </span><br/><span>VICTORIA </span><span>BC </span><span>Can </span></td></tr><tr><td>Date of birth</td><td><span>01 June 1980</span></td></tr></tbody></table></div></text><identifier><use value=\"official\"/><label value=\"University Health Network MRN 7000135\"/><system value=\"urn:oid:2.16.840.1.113883.3.239.18.148\"/><value value=\"7000135\"/><assigner><reference value=\"Organization/1.3.6.1.4.1.12201\"/></assigner></identifier><name><family value=\"Duck\"/><given value=\"Donald\"/></name><telecom><system value=\"phone\"/><use value=\"home\"/></telecom><telecom><system value=\"phone\"/><use value=\"work\"/></telecom><telecom><system value=\"phone\"/><use value=\"mobile\"/></telecom><telecom><system value=\"email\"/><use value=\"home\"/></telecom><gender><coding><system value=\"http://hl7.org/fhir/v3/AdministrativeGender\"/><code value=\"M\"/></coding></gender><birthDate value=\"1980-06-01T00:00:00\"/><address><use value=\"home\"/><line value=\"10 Duxon Street\"/><city value=\"VICTORIA\"/><state value=\"BC\"/><zip value=\"V8N 1Y4\"/><country value=\"Can\"/></address><managingOrganization><reference value=\"Organization/1.3.6.1.4.1.12201\"/></managingOrganization></Patient>";
		IBaseResource res = ourCtx.newXmlParser().parseResource(input);

		String output = ourCtx.newXmlParser().encodeResourceToString(res);

		// Should occur exactly twice (once for the resource, once for the DIV
		assertThat(output, (StringContainsInOrder.stringContainsInOrder(Arrays.asList("Patient xmlns", "div xmlns"))));
		assertThat(output, not(StringContainsInOrder.stringContainsInOrder(Arrays.asList("b xmlns"))));

		output = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(res);

		// Should occur exactly twice (once for the resource, once for the DIV
		assertThat(output, (StringContainsInOrder.stringContainsInOrder(Arrays.asList("Patient xmlns", "div xmlns"))));
		assertThat(output, not(StringContainsInOrder.stringContainsInOrder(Arrays.asList("b xmlns"))));

	}

	/**
	 * This sample has extra elements in <searchParam> that are not actually a part of the spec any more..
	 */
	@Test
	public void testParseFuroreMetadataWithExtraElements() throws IOException {
		String msg = IOUtils.toString(XmlParserTest.class.getResourceAsStream("/furore-conformance.xml"));

		IParser p = new FhirContext(ValueSet.class).newXmlParser();
		Conformance conf = p.parseResource(Conformance.class, msg);
		ConformanceRestResourceComponent res = conf.getRest().get(0).getResource().get(0);
		assertEquals("_id", res.getSearchParam().get(1).getName());
	}

	@Test
	public void testParseLanguage() {
		String input = "<Patient xmlns=\"http://hl7.org/fhir\"><language value=\"zh-CN\"/><text><status value=\"generated\"/><div xmlns=\"http://www.w3.org/1999/xhtml\"><div class=\"hapiHeaderText\"> 海生 <b>王 </b></div><table class=\"hapiPropertyTable\"><tbody><tr><td>Identifier</td><td>URNo</td></tr><tr><td>Address</td><td><span>99 Houston Road </span><br/><span>BENTLEIGH </span><span>Victoria </span></td></tr><tr><td>Date of birth</td><td><span>01 January 1997</span></td></tr></tbody></table></div></text><identifier><use value=\"usual\"/><label value=\"URNo\"/><value value=\"89532\"/></identifier><name><text value=\"王海生\"/><family value=\"王\"/><given value=\"海生\"/></name><telecom><system value=\"phone\"/><value value=\"9899 9878\"/><use value=\"home\"/></telecom><telecom><system value=\"email\"/><value value=\"zimmerman@datacorp.com.au\"/><use value=\"home\"/></telecom><gender><coding><system value=\"http://hl7.org/fhir/v3/AdministrativeGender\"/><code value=\"M\"/><display value=\"Male\"/></coding><text value=\"Male\"/></gender><birthDate value=\"1997-01-01\"/><address><use value=\"home\"/><text value=\"99 Houston Road, BENTLEIGH, 3204\"/><line value=\"99 Houston Road\"/><city value=\"BENTLEIGH\"/><state value=\"Victoria\"/><zip value=\"3204\"/><period><start value=\"2006-06-16\"/></period></address><active value=\"true\"/></Patient>";
		Patient pt = ourCtx.newXmlParser().parseResource(Patient.class, input);

		assertEquals("zh-CN", pt.getLanguage());
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
		assertEquals("IdentifierLabel", resource.getIdentifier().get(0).getLabel());
	}

	@Test
	public void testSimpleResourceEncode() throws IOException, SAXException {

		String xmlString = IOUtils.toString(JsonParser.class.getResourceAsStream("/example-patient-general.json"), Charset.forName("UTF-8"));
		Patient obs = ourCtx.newJsonParser().parseResource(Patient.class, xmlString);

		List<Extension> undeclaredExtensions = obs.getContact().get(0).getName().getFamily().get(0).getExtension();
		Extension undeclaredExtension = undeclaredExtensions.get(0);
		assertEquals("http://hl7.org/fhir/Profile/iso-21090#qualifier", undeclaredExtension.getUrl());

		ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToWriter(obs, new OutputStreamWriter(System.out));

		IParser jsonParser = ourCtx.newXmlParser();
		String encoded = jsonParser.encodeResourceToString(obs);
		ourLog.info(encoded);

		String jsonString = IOUtils.toString(JsonParser.class.getResourceAsStream("/example-patient-general.xml"), Charset.forName("UTF-8"));

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

		assertEquals(0, obs.getExtension().size());
		assertEquals("aaaa", obs.getExtAtt().getContentType());
		assertEquals("str1", obs.getMoreExt().getStr1().getValue());
		assertEquals("2011-01-02", obs.getModExt().getValueAsString());

		List<org.hl7.fhir.instance.model.Extension> undeclaredExtensions = obs.getContact().get(0).getName().getFamily().get(0).getExtension();
		org.hl7.fhir.instance.model.Extension undeclaredExtension = undeclaredExtensions.get(0);
		assertEquals("http://hl7.org/fhir/Profile/iso-21090#qualifier", undeclaredExtension.getUrl());

		fhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToWriter(obs, new OutputStreamWriter(System.out));

		IParser jsonParser = fhirCtx.newXmlParser();
		String encoded = jsonParser.encodeResourceToString(obs);
		ourLog.info(encoded);

		String jsonString = IOUtils.toString(JsonParser.class.getResourceAsStream("/example-patient-general.xml"), Charset.forName("UTF-8"));

		String expected = (jsonString);
		String actual = (encoded.trim());

		Diff d = new Diff(new StringReader(expected), new StringReader(actual));
		assertTrue(d.toString(), d.identical());

	}


	@BeforeClass
	public static void beforeClass() {
		XMLUnit.setIgnoreAttributeOrder(true);
		XMLUnit.setIgnoreComments(true);
		XMLUnit.setIgnoreWhitespace(true);
		ourCtx = new FhirContext();
	}

}
