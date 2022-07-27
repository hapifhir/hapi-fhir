package ca.uhn.fhir.tests.integration.karaf.dstu2hl7org;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.api.AddProfileTagEnum;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.Constants;
import org.apache.commons.io.IOUtils;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.core.IsNot;
import org.hamcrest.core.StringContains;
import org.hamcrest.text.StringContainsInOrder;
import org.hl7.fhir.dstu2.model.Address;
import org.hl7.fhir.dstu2.model.CodeableConcept;
import org.hl7.fhir.dstu2.model.Composition;
import org.hl7.fhir.dstu2.model.DiagnosticReport;
import org.hl7.fhir.dstu2.model.EnumFactory;
import org.hl7.fhir.dstu2.model.Enumeration;
import org.hl7.fhir.dstu2.model.InstantType;
import org.hl7.fhir.dstu2.model.Narrative;
import org.hl7.fhir.dstu2.model.Organization;
import org.hl7.fhir.dstu2.model.Patient;
import org.hl7.fhir.dstu2.model.PrimitiveType;
import org.hl7.fhir.dstu2.model.Reference;
import org.hl7.fhir.dstu2.model.Resource;
import org.hl7.fhir.dstu2.model.Specimen;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;
import org.xml.sax.SAXException;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.builder.Input;
import org.xmlunit.diff.ComparisonControllers;
import org.xmlunit.diff.DefaultNodeMatcher;
import org.xmlunit.diff.Diff;
import org.xmlunit.diff.ElementSelectors;

import static ca.uhn.fhir.tests.integration.karaf.PaxExamOptions.HAPI_FHIR_HL7ORG_DSTU2;
import static ca.uhn.fhir.tests.integration.karaf.PaxExamOptions.KARAF;
import static ca.uhn.fhir.tests.integration.karaf.PaxExamOptions.WRAP;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.options;
import static org.ops4j.pax.exam.CoreOptions.when;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.debugConfiguration;


/**
 * Useful docs about this test: https://ops4j1.jira.com/wiki/display/paxexam/FAQ
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
public class XmlParserHl7OrgDstu2Test {

	private final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(XmlParserHl7OrgDstu2Test.class);
	private FhirContext ourCtx = FhirContext.forDstu2Hl7Org();

   @Configuration
   public Option[] config() throws IOException {
      return options(
      	KARAF.option(),
			WRAP.option(),
			HAPI_FHIR_HL7ORG_DSTU2.option(),
			mavenBundle().groupId("org.xmlunit").artifactId("xmlunit-core").versionAsInProject(),
			mavenBundle().groupId("org.apache.servicemix.bundles").artifactId("org.apache.servicemix.bundles.hamcrest").versionAsInProject(),
			when(false)
         	.useOptions(
            	debugConfiguration("5005", true))
      );
   }

	@Test
	public void testOverrideResourceIdWithBundleEntryFullUrlEnabled() {
		String tmp = "<Bundle xmlns=\"http://hl7.org/fhir\"><entry><fullUrl value=\"http://lalaland.org/patient/pat1\"/><resource><Patient xmlns=\"http://hl7.org/fhir\"><id value=\"patxuzos\"/></Patient></resource></entry></Bundle>";
		org.hl7.fhir.dstu2.model.Bundle bundle = (org.hl7.fhir.dstu2.model.Bundle) ourCtx.newXmlParser().parseResource(tmp);
		assertEquals(1, bundle.getEntry().size());
		{
			org.hl7.fhir.dstu2.model.Patient o1 = (org.hl7.fhir.dstu2.model.Patient) bundle.getEntry().get(0).getResource();
			IIdType o1Id = o1.getIdElement();
			assertEquals("http://lalaland.org", o1Id.getBaseUrl());
			assertEquals("patient", o1Id.getResourceType());
			assertEquals("pat1", o1Id.getIdPart());
			assertFalse(o1Id.hasVersionIdPart());
		}
	}

	@Test
	public void testOverrideResourceIdWithBundleEntryFullUrlDisabled_ConfiguredOnFhirContext() {
		String tmp = "<Bundle xmlns=\"http://hl7.org/fhir\"><entry><fullUrl value=\"http://lalaland.org/patient/pat1\"/><resource><Patient xmlns=\"http://hl7.org/fhir\"><id value=\"patxuzos\"/></Patient></resource></entry></Bundle>";
		ourCtx.getParserOptions().setOverrideResourceIdWithBundleEntryFullUrl(false);
		org.hl7.fhir.dstu2.model.Bundle bundle = (org.hl7.fhir.dstu2.model.Bundle) ourCtx.newXmlParser().parseResource(tmp);
		assertEquals(1, bundle.getEntry().size());
		{
			org.hl7.fhir.dstu2.model.Patient o1 = (org.hl7.fhir.dstu2.model.Patient) bundle.getEntry().get(0).getResource();
			IIdType o1Id = o1.getIdElement();
			assertFalse(o1Id.hasBaseUrl());
			assertEquals("Patient", o1Id.getResourceType());
			assertEquals("patxuzos", o1Id.getIdPart());
			assertFalse(o1Id.hasVersionIdPart());
		}
	}

	@Test
	public void testOverrideResourceIdWithBundleEntryFullUrlDisabled_ConfiguredOnParser() {
		String tmp = "<Bundle xmlns=\"http://hl7.org/fhir\"><entry><fullUrl value=\"http://lalaland.org/patient/pat1\"/><resource><Patient xmlns=\"http://hl7.org/fhir\"><id value=\"patxuzos\"/></Patient></resource></entry></Bundle>";
		org.hl7.fhir.dstu2.model.Bundle bundle = (org.hl7.fhir.dstu2.model.Bundle) ourCtx.newXmlParser().setOverrideResourceIdWithBundleEntryFullUrl(false).parseResource(tmp);
		assertEquals(1, bundle.getEntry().size());
		{
			org.hl7.fhir.dstu2.model.Patient o1 = (org.hl7.fhir.dstu2.model.Patient) bundle.getEntry().get(0).getResource();
			IIdType o1Id = o1.getIdElement();
			assertFalse(o1Id.hasBaseUrl());
			assertEquals("Patient", o1Id.getResourceType());
			assertEquals("patxuzos", o1Id.getIdPart());
			assertFalse(o1Id.hasVersionIdPart());
		}
	}

	@Test
	public void testComposition() {

		Composition comp = new Composition();
		comp.setId("1");

		ourCtx.newXmlParser().encodeResourceToString(comp);
		ourCtx.newXmlParser().encodeResourceToString(comp);
		ourCtx.newXmlParser().encodeResourceToString(comp);
		ourCtx.newXmlParser().encodeResourceToString(comp);

		// comp.

	}

	@Test
	public void testContainedResourceInExtensionUndeclared() {
		org.hl7.fhir.dstu2.model.Patient p = new org.hl7.fhir.dstu2.model.Patient();
		p.addName().addFamily("PATIENT");

		Organization o = new Organization();
		o.setName("ORG");
		p.addExtension().setUrl("urn:foo").setValue(new org.hl7.fhir.dstu2.model.Reference(o));

		String str = ourCtx.newXmlParser().encodeResourceToString(p);
		ourLog.info(str);

		p = ourCtx.newXmlParser().parseResource(org.hl7.fhir.dstu2.model.Patient.class, str);
		assertEquals("PATIENT", p.getName().get(0).getFamily().get(0).getValue());

		List<org.hl7.fhir.dstu2.model.Extension> exts = p.getExtension();
		assertEquals(1, exts.size());
		org.hl7.fhir.dstu2.model.Reference rr = (org.hl7.fhir.dstu2.model.Reference) exts.get(0).getValue();
		o = (Organization) rr.getResource();
		assertEquals("ORG", o.getName());
	}

	@Test
	public void testDuplicateContainedResources() {

		org.hl7.fhir.dstu2.model.Observation resA = new org.hl7.fhir.dstu2.model.Observation();
		resA.getCode().setText("A");

		org.hl7.fhir.dstu2.model.Observation resB = new org.hl7.fhir.dstu2.model.Observation();
		resB.getCode().setText("B");
		resB.addRelated().setTarget(new org.hl7.fhir.dstu2.model.Reference(resA));
		resB.addRelated().setTarget(new org.hl7.fhir.dstu2.model.Reference(resA));

		String encoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(resB);
		ourLog.info(encoded);

		assertThat(encoded,
			stringContainsInOrder(Arrays.asList("<contained>", "<Observation", "</Observation>", "</contained>")));
		assertThat(encoded, not(stringContainsInOrder(
			Arrays.asList("<contained>", "<Observation", "</Observation>", "<Obser", "</contained>"))));

	}

	@Test
	public void testEncodeAndParseContained() {
		IParser xmlParser = ourCtx.newXmlParser().setPrettyPrint(true);

		// Create an organization, note that the organization does not have an ID
		Organization org = new Organization();
		org.getNameElement().setValue("Contained Test Organization");

		// Create a patient
		org.hl7.fhir.dstu2.model.Patient patient = new org.hl7.fhir.dstu2.model.Patient();
		patient.setId("Patient/1333");
		patient.addIdentifier().setSystem("urn:mrns").setValue("253345");

		// Put the organization as a reference in the patient resource
		patient.getManagingOrganization().setResource(org);

		String encoded = xmlParser.encodeResourceToString(patient);
		ourLog.info(encoded);
		assertThat(encoded, containsString("<contained>"));
		assertThat(encoded, containsString("<reference value=\"#1\"/>"));

		// Create a bundle with just the patient resource
		org.hl7.fhir.dstu2.model.Bundle b = new org.hl7.fhir.dstu2.model.Bundle();
		b.addEntry().setResource(patient);

		// Encode the bundle
		encoded = xmlParser.encodeResourceToString(b);
		ourLog.info(encoded);
		assertThat(encoded, stringContainsInOrder(Arrays.asList("<contained>", "<id value=\"1\"/>", "</contained>")));
		assertThat(encoded, containsString("<reference value=\"#1\"/>"));
		assertThat(encoded, stringContainsInOrder(Arrays.asList("<entry>", "</entry>")));
		assertThat(encoded, not(stringContainsInOrder(Arrays.asList("<entry>", "</entry>", "<entry>"))));

		// Re-parse the bundle
		patient = (org.hl7.fhir.dstu2.model.Patient) xmlParser.parseResource(xmlParser.encodeResourceToString(patient));
		assertEquals("#1", patient.getManagingOrganization().getReferenceElement().getValue());

		assertNotNull(patient.getManagingOrganization().getResource());
		org = (Organization) patient.getManagingOrganization().getResource();
		assertEquals("#1", org.getIdElement().getValue());
		assertEquals("Contained Test Organization", org.getName());

		// And re-encode a second time
		encoded = xmlParser.encodeResourceToString(patient);
		ourLog.info(encoded);
		assertThat(encoded, stringContainsInOrder(Arrays.asList("<contained>", "<Organization ", "<id value=\"1\"/>",
			"</Organization", "</contained>", "<reference value=\"#1\"/>")));
		assertThat(encoded, not(stringContainsInOrder(Arrays.asList("<contained>", "<Org", "<contained>"))));
		assertThat(encoded, containsString("<reference value=\"#1\"/>"));

		// And re-encode once more, with the references cleared
		patient.getContained().clear();
		patient.getManagingOrganization().setReference(null);
		encoded = xmlParser.encodeResourceToString(patient);
		ourLog.info(encoded);
		assertThat(encoded, stringContainsInOrder(Arrays.asList("<contained>", "<Organization ", "<id value=\"1\"/>",
			"</Organization", "</contained>", "<reference value=\"#1\"/>")));
		assertThat(encoded, not(stringContainsInOrder(Arrays.asList("<contained>", "<Org", "<contained>"))));
		assertThat(encoded, containsString("<reference value=\"#1\"/>"));

		// And re-encode once more, with the references cleared and a manually set
		// local ID
		patient.getContained().clear();
		patient.getManagingOrganization().setReference(null);
		patient.getManagingOrganization().getResource().setId(("#333"));
		encoded = xmlParser.encodeResourceToString(patient);
		ourLog.info(encoded);
		assertThat(encoded, stringContainsInOrder(Arrays.asList("<contained>", "<Organization ", "<id value=\"333\"/>",
			"</Organization", "</contained>", "<reference value=\"#333\"/>")));
		assertThat(encoded, not(stringContainsInOrder(Arrays.asList("<contained>", "<Org", "<contained>"))));

	}

	@Test
	public void testEncodeAndParseExtensions() throws Exception {

		org.hl7.fhir.dstu2.model.Patient patient = new org.hl7.fhir.dstu2.model.Patient();
		patient.addIdentifier().setUse(org.hl7.fhir.dstu2.model.Identifier.IdentifierUse.OFFICIAL).setSystem("urn:example").setValue("7000135");

		org.hl7.fhir.dstu2.model.Extension ext = new org.hl7.fhir.dstu2.model.Extension();
		ext.setUrl("http://example.com/extensions#someext");
		ext.setValue(new org.hl7.fhir.dstu2.model.DateTimeType("2011-01-02T11:13:15"));
		patient.getExtension().add(ext);

		org.hl7.fhir.dstu2.model.Extension parent = new org.hl7.fhir.dstu2.model.Extension().setUrl("http://example.com#parent");
		patient.getExtension().add(parent);
		org.hl7.fhir.dstu2.model.Extension child1 = new org.hl7.fhir.dstu2.model.Extension().setUrl("http://example.com#child").setValue(new org.hl7.fhir.dstu2.model.StringType("value1"));
		parent.getExtension().add(child1);
		org.hl7.fhir.dstu2.model.Extension child2 = new org.hl7.fhir.dstu2.model.Extension().setUrl("http://example.com#child").setValue(new org.hl7.fhir.dstu2.model.StringType("value2"));
		parent.getExtension().add(child2);

		org.hl7.fhir.dstu2.model.Extension modExt = new org.hl7.fhir.dstu2.model.Extension();
		modExt.setUrl("http://example.com/extensions#modext");
		modExt.setValue(new org.hl7.fhir.dstu2.model.DateType("1995-01-02"));
		patient.getModifierExtension().add(modExt);

		org.hl7.fhir.dstu2.model.HumanName name = patient.addName();
		name.addFamily("Blah");
		org.hl7.fhir.dstu2.model.StringType given = name.addGivenElement();
		given.setValue("Joe");
		org.hl7.fhir.dstu2.model.Extension ext2 = new org.hl7.fhir.dstu2.model.Extension().setUrl("http://examples.com#givenext").setValue(new org.hl7.fhir.dstu2.model.StringType("given"));
		given.getExtension().add(ext2);

		org.hl7.fhir.dstu2.model.StringType given2 = name.addGivenElement();
		given2.setValue("Shmoe");
		org.hl7.fhir.dstu2.model.Extension given2ext = new org.hl7.fhir.dstu2.model.Extension().setUrl("http://examples.com#givenext_parent");
		given2.getExtension().add(given2ext);
		given2ext.addExtension().setUrl("http://examples.com#givenext_child").setValue(new org.hl7.fhir.dstu2.model.StringType("CHILD"));

		String output = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(patient);
		ourLog.info(output);

		String enc = ourCtx.newXmlParser().encodeResourceToString(patient);
		assertThat(enc, containsString(
			"<Patient xmlns=\"http://hl7.org/fhir\"><extension url=\"http://example.com/extensions#someext\"><valueDateTime value=\"2011-01-02T11:13:15\"/></extension>"));
		assertThat(enc, containsString(
			"<modifierExtension url=\"http://example.com/extensions#modext\"><valueDate value=\"1995-01-02\"/></modifierExtension>"));
		assertThat(enc, containsString(
			"<extension url=\"http://example.com#parent\"><extension url=\"http://example.com#child\"><valueString value=\"value1\"/></extension><extension url=\"http://example.com#child\"><valueString value=\"value2\"/></extension></extension>"));
		assertThat(enc, containsString(
			"<given value=\"Joe\"><extension url=\"http://examples.com#givenext\"><valueString value=\"given\"/></extension></given>"));
		assertThat(enc, containsString(
			"<given value=\"Shmoe\"><extension url=\"http://examples.com#givenext_parent\"><extension url=\"http://examples.com#givenext_child\"><valueString value=\"CHILD\"/></extension></extension></given>"));

    /*
     * Now parse this back
     */

		org.hl7.fhir.dstu2.model.Patient parsed = ourCtx.newXmlParser().parseResource(org.hl7.fhir.dstu2.model.Patient.class, enc);
		ext = parsed.getExtension().get(0);
		assertEquals("http://example.com/extensions#someext", ext.getUrl());
		assertEquals("2011-01-02T11:13:15", ((org.hl7.fhir.dstu2.model.DateTimeType) ext.getValue()).getValueAsString());

		parent = patient.getExtension().get(1);
		assertEquals("http://example.com#parent", parent.getUrl());
		assertNull(parent.getValue());
		child1 = parent.getExtension().get(0);
		assertEquals("http://example.com#child", child1.getUrl());
		assertEquals("value1", ((org.hl7.fhir.dstu2.model.StringType) child1.getValue()).getValueAsString());
		child2 = parent.getExtension().get(1);
		assertEquals("http://example.com#child", child2.getUrl());
		assertEquals("value2", ((org.hl7.fhir.dstu2.model.StringType) child2.getValue()).getValueAsString());

		modExt = parsed.getModifierExtension().get(0);
		assertEquals("http://example.com/extensions#modext", modExt.getUrl());
		assertEquals("1995-01-02", ((org.hl7.fhir.dstu2.model.DateType) modExt.getValue()).getValueAsString());

		name = parsed.getName().get(0);

		ext2 = name.getGiven().get(0).getExtension().get(0);
		assertEquals("http://examples.com#givenext", ext2.getUrl());
		assertEquals("given", ((org.hl7.fhir.dstu2.model.StringType) ext2.getValue()).getValueAsString());

		given2ext = name.getGiven().get(1).getExtension().get(0);
		assertEquals("http://examples.com#givenext_parent", given2ext.getUrl());
		assertNull(given2ext.getValue());
		org.hl7.fhir.dstu2.model.Extension given2ext2 = given2ext.getExtension().get(0);
		assertEquals("http://examples.com#givenext_child", given2ext2.getUrl());
		assertEquals("CHILD", ((org.hl7.fhir.dstu2.model.StringType) given2ext2.getValue()).getValue());

	}

	@Test
	public void testEncodeBinaryResource() {

		org.hl7.fhir.dstu2.model.Binary patient = new org.hl7.fhir.dstu2.model.Binary();
		patient.setContentType("foo");
		patient.setContent(new byte[] { 1, 2, 3, 4 });

		String val = ourCtx.newXmlParser().encodeResourceToString(patient);
		assertEquals(
			"<Binary xmlns=\"http://hl7.org/fhir\"><contentType value=\"foo\"/><content value=\"AQIDBA==\"/></Binary>",
			val);

	}

	@Test
	public void testEncodeBinaryWithNoContentType() {
		org.hl7.fhir.dstu2.model.Binary b = new org.hl7.fhir.dstu2.model.Binary();
		b.setContent(new byte[] { 1, 2, 3, 4 });

		String output = ourCtx.newXmlParser().encodeResourceToString(b);
		ourLog.info(output);

		assertEquals("<Binary xmlns=\"http://hl7.org/fhir\"><content value=\"AQIDBA==\"/></Binary>", output);
	}

	@Test
	public void testEncodeBoundCode() {

		org.hl7.fhir.dstu2.model.Patient patient = new org.hl7.fhir.dstu2.model.Patient();
		patient.addAddress().setUse(Address.AddressUse.HOME);

		patient.getGenderElement().setValue(org.hl7.fhir.dstu2.model.Enumerations.AdministrativeGender.MALE);

		String val = ourCtx.newXmlParser().encodeResourceToString(patient);
		ourLog.info(val);

		assertThat(val, containsString("home"));
		assertThat(val, containsString("male"));
	}

	@Test
	public void testEncodeBundle() throws InterruptedException {
		org.hl7.fhir.dstu2.model.Bundle b = new org.hl7.fhir.dstu2.model.Bundle();
		b.getMeta().addTag().setSystem("http://hl7.org/fhir/tag").setCode("http://hl7.org/fhir/tag/message")
			.setDisplay("Message");

		InstantType pub = InstantType.withCurrentTime();
		b.getMeta().setLastUpdatedElement(pub);

		org.hl7.fhir.dstu2.model.Patient p1 = new org.hl7.fhir.dstu2.model.Patient();
		p1.addName().addFamily("Family1");
		org.hl7.fhir.dstu2.model.Bundle.BundleEntryComponent entry = b.addEntry();
		p1.getIdElement().setValue("1");
		entry.setResource(p1);

		org.hl7.fhir.dstu2.model.Patient p2 = new org.hl7.fhir.dstu2.model.Patient();
		p2.addName().addFamily("Family2");
		entry = b.addEntry();
		p2.getIdElement().setValue("2");
		entry.setResource(p2);

		String bundleString = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(b);
		ourLog.info(bundleString);

		// @formatter:on
		String[] strings = { "<Bundle xmlns=\"http://hl7.org/fhir\">",
			"<lastUpdated value=\"" + pub.getValueAsString() + "\"/>", "<Patient xmlns=\"http://hl7.org/fhir\">",
			"<id value=\"1\"/>", "<Patient xmlns=\"http://hl7.org/fhir\">", "<id value=\"2\"/>" };
		// @formatter:off

		assertThat(bundleString, stringContainsInOrder(strings));
	}

	@Test
	public void testEncodeBundleCategory() {

		org.hl7.fhir.dstu2.model.Bundle b = new org.hl7.fhir.dstu2.model.Bundle();
		org.hl7.fhir.dstu2.model.Bundle.BundleEntryComponent e = b.addEntry();
		e.setResource(new org.hl7.fhir.dstu2.model.Patient());
		e.getResource().getMeta().addTag().setSystem("scheme").setCode("term").setDisplay("label");

		String val = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(b);
		ourLog.info(val);

		// @formatter:off
		assertThat(val, stringContainsInOrder("<tag>", "<system value=\"scheme\"/>", "<code value=\"term\"/>",
			"<display value=\"label\"/>", "</tag>"));
		// @formatter:on

		b = ourCtx.newXmlParser().parseResource(org.hl7.fhir.dstu2.model.Bundle.class, val);
		assertEquals(1, b.getEntry().size());
		assertEquals(1, b.getEntry().get(0).getResource().getMeta().getTag().size());
		assertEquals("scheme", b.getEntry().get(0).getResource().getMeta().getTag().get(0).getSystem());
		assertEquals("term", b.getEntry().get(0).getResource().getMeta().getTag().get(0).getCode());
		assertEquals("label", b.getEntry().get(0).getResource().getMeta().getTag().get(0).getDisplay());
	}

	private Matcher<? super String> stringContainsInOrder(java.lang.String... substrings) {
		return Matchers.stringContainsInOrder(Arrays.asList(substrings));
	}

	private Matcher<? super String> stringContainsInOrder(List<String> substrings) {
		return Matchers.stringContainsInOrder(substrings);
	}

	@Test
	public void testEncodeContainedAndIncludedResources() {

		DiagnosticReport rpt = new DiagnosticReport();
		rpt.getCode().setText("Report");

		Specimen spm = new Specimen();
		spm.addIdentifier().setValue("Report1ContainedSpecimen1");
		rpt.addSpecimen().setResource(spm);

		IParser p = ourCtx.newXmlParser().setPrettyPrint(true);
		String str = p.encodeResourceToString(rpt);

		ourLog.info(str);

	}

	@Test
	public void testEncodeContainedResources() throws Exception {

		DiagnosticReport rpt = new DiagnosticReport();
		Specimen spm = new Specimen();
		spm.addIdentifier().setSystem("urn").setValue("123");
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
		assertThat(str, stringContainsInOrder("<Specimen xmlns=\"http://hl7.org/fhir\">", "<id value=\"" + id + "\"/>"));
		assertThat(str, IsNot.not(StringContains.containsString("<?xml version='1.0'?>")));

	}

	@Test
	public void testEncodeContainedWithNarrative() throws Exception {
		IParser parser = ourCtx.newXmlParser().setPrettyPrint(true);

		// Create an organization, note that the organization does not have an ID
		Organization org = new Organization();
		org.getNameElement().setValue("Contained Test Organization");
		org.getText().setDivAsString("<div>FOOBAR</div>");

		// Create a patient
		org.hl7.fhir.dstu2.model.Patient patient = new org.hl7.fhir.dstu2.model.Patient();
		patient.setId("Patient/1333");
		patient.addIdentifier().setSystem("urn:mrns").setValue("253345");
		patient.getText().setDivAsString("<div>BARFOO</div>");
		patient.getManagingOrganization().setResource(org);

		String encoded = parser.encodeResourceToString(patient);
		ourLog.info(encoded);

		assertThat(encoded, stringContainsInOrder("<Patient", "<text>",
			"<div xmlns=\"http://www.w3.org/1999/xhtml\">BARFOO</div>", "<contained>", "<Organization", "</Organization"));
		assertThat(encoded,
			not(stringContainsInOrder("<Patient", "<text>", "<contained>", "<Organization", "<text", "</Organization")));

		assertThat(encoded, not(containsString("FOOBAR")));
		assertThat(encoded, (containsString("BARFOO")));

	}

	@Test
	public void testEncodeDeclaredExtensionWithAddressContent() {
		IParser parser = ourCtx.newXmlParser();

		MyPatientWithOneDeclaredAddressExtension patient = new MyPatientWithOneDeclaredAddressExtension();
		patient.addAddress().setUse(Address.AddressUse.HOME);
		patient.setFoo(new Address().addLine("line1"));

		String val = parser.encodeResourceToString(patient);
		ourLog.info(val);
		assertThat(val, StringContains
			.containsString("<extension url=\"urn:foo\"><valueAddress><line value=\"line1\"/></valueAddress></extension>"));

		MyPatientWithOneDeclaredAddressExtension actual = parser
			.parseResource(MyPatientWithOneDeclaredAddressExtension.class, val);
		assertEquals(Address.AddressUse.HOME, patient.getAddress().get(0).getUse());
		Address ref = actual.getFoo();
		assertEquals("line1", ref.getLine().get(0).getValue());

	}

	@Test
	public void testEncodeDeclaredExtensionWithResourceContent() {
		IParser parser = ourCtx.newXmlParser();

		MyPatientWithOneDeclaredExtension patient = new MyPatientWithOneDeclaredExtension();
		patient.addAddress().setUse(Address.AddressUse.HOME);
		patient.setFoo(new org.hl7.fhir.dstu2.model.Reference("Organization/123"));

		String val = parser.encodeResourceToString(patient);
		ourLog.info(val);
		assertThat(val, StringContains.containsString(
			"<extension url=\"urn:foo\"><valueReference><reference value=\"Organization/123\"/></valueReference></extension>"));

		MyPatientWithOneDeclaredExtension actual = parser.parseResource(MyPatientWithOneDeclaredExtension.class, val);
		assertEquals(Address.AddressUse.HOME, patient.getAddress().get(0).getUse());
		org.hl7.fhir.dstu2.model.Reference ref = actual.getFoo();
		assertEquals("Organization/123", ref.getReferenceElement().getValue());

	}

	/**
	 * #158
	 */
	@Test
	public void testEncodeEmptyTag() {
		org.hl7.fhir.dstu2.model.Patient p = new org.hl7.fhir.dstu2.model.Patient();
		p.getMeta().addTag();

		String encoded = ourCtx.newXmlParser().encodeResourceToString(p);
		assertThat(encoded, not(containsString("tag")));

		// With tag

		p = new org.hl7.fhir.dstu2.model.Patient();
		p.getMeta().addTag().setSystem("sys").setCode("code");

		encoded = ourCtx.newXmlParser().encodeResourceToString(p);
		assertThat(encoded, (containsString("tag")));
	}

	@Test
	public void testEncodeEscapedChars() {

		org.hl7.fhir.dstu2.model.Patient p = new org.hl7.fhir.dstu2.model.Patient();
		p.addName().addFamily("and <>&ü");

		String enc = ourCtx.newXmlParser().encodeResourceToString(p);
		ourLog.info(enc);

		p = ourCtx.newXmlParser().parseResource(org.hl7.fhir.dstu2.model.Patient.class, enc);
		assertEquals("and <>&ü", p.getName().get(0).getFamily().get(0).getValue());

		p = ourCtx.newXmlParser().parseResource(org.hl7.fhir.dstu2.model.Patient.class,
			"<Patient xmlns=\"http://hl7.org/fhir\"><name><family value=\"quot &quot;\"/></name></Patient>");
		assertEquals("quot \"", p.getName().get(0).getFamily().get(0).getValue());

	}

	@Test
	public void testEncodeEscapedExtendedChars() {
		org.hl7.fhir.dstu2.model.Patient p = ourCtx.newXmlParser().parseResource(org.hl7.fhir.dstu2.model.Patient.class,
			"<Patient xmlns=\"http://hl7.org/fhir\"><name><family value=\"uuml &uuml;\"/></name></Patient>");
		assertEquals("uuml ü", p.getName().get(0).getFamily().get(0).getValue());
	}

	@Test
	public void testEncodeExtensionUndeclaredNonModifier() {
		org.hl7.fhir.dstu2.model.Observation obs = new org.hl7.fhir.dstu2.model.Observation();
		obs.setId("1");
		obs.getMeta().addProfile("http://profile");
		org.hl7.fhir.dstu2.model.Extension ext = obs.addExtension();
		ext.setUrl("http://exturl").setValue(new org.hl7.fhir.dstu2.model.StringType("ext_url_value"));

		obs.getCode().setText("CODE");

		IParser parser = ourCtx.newXmlParser();

		String output = parser.setPrettyPrint(true).encodeResourceToString(obs);
		ourLog.info(output);

		// @formatter:off
		assertThat(output, stringContainsInOrder("<id value=\"1\"/>", "<meta>", "<profile value=\"http://profile\"/>",
			"<extension url=\"http://exturl\">", "<valueString value=\"ext_url_value\"/>", "<text value=\"CODE\"/>"));
		assertThat(output, not(stringContainsInOrder("<url value=\"http://exturl\"/>")));
		// @formatter:on

		obs = parser.parseResource(org.hl7.fhir.dstu2.model.Observation.class, output);
		assertEquals(1, obs.getExtension().size());
		assertEquals("http://exturl", obs.getExtension().get(0).getUrl());
		assertEquals("ext_url_value", ((org.hl7.fhir.dstu2.model.StringType) obs.getExtension().get(0).getValue()).getValue());
	}

	@Test
	public void testEncodeExtensionUndeclaredNonModifierWithChildExtension() {
		org.hl7.fhir.dstu2.model.Observation obs = new org.hl7.fhir.dstu2.model.Observation();
		obs.setId("1");
		obs.getMeta().addProfile("http://profile");
		org.hl7.fhir.dstu2.model.Extension ext = obs.addExtension();
		ext.setUrl("http://exturl");

		org.hl7.fhir.dstu2.model.Extension subExt = ext.addExtension();
		subExt.setUrl("http://subext").setValue(new org.hl7.fhir.dstu2.model.StringType("sub_ext_value"));

		obs.getCode().setText("CODE");

		IParser parser = ourCtx.newXmlParser();

		String output = parser.setPrettyPrint(true).encodeResourceToString(obs);
		ourLog.info(output);

		// @formatter:off
		assertThat(output,
			stringContainsInOrder("<id value=\"1\"/>", "<meta>", "<profile value=\"http://profile\"/>",
				"<extension url=\"http://exturl\">", "<extension url=\"http://subext\">",
				"<valueString value=\"sub_ext_value\"/>", "<text value=\"CODE\"/>"));
		assertThat(output, not(stringContainsInOrder("<url value=\"http://exturl\"/>")));
		// @formatter:on

		obs = parser.parseResource(org.hl7.fhir.dstu2.model.Observation.class, output);
		assertEquals(1, obs.getExtension().size());
		assertEquals("http://exturl", obs.getExtension().get(0).getUrl());
		assertEquals(1, obs.getExtension().get(0).getExtension().size());
		assertEquals("http://subext", obs.getExtension().get(0).getExtension().get(0).getUrl());
		assertEquals("sub_ext_value", ((org.hl7.fhir.dstu2.model.StringType) obs.getExtension().get(0).getExtension().get(0).getValue()).getValue());
	}

	/**
	 * See #327
	 */
	@Test
	public void testEncodeExtensionWithContainedResource() {

		TestPatientFor327 patient = new TestPatientFor327();
		patient.setBirthDateElement(new org.hl7.fhir.dstu2.model.DateType("2016-04-14"));

		List<org.hl7.fhir.dstu2.model.Reference> conditions = new ArrayList<org.hl7.fhir.dstu2.model.Reference>();
		org.hl7.fhir.dstu2.model.Condition condition = new org.hl7.fhir.dstu2.model.Condition();
		condition.addBodySite().setText("BODY SITE");
		conditions.add(new org.hl7.fhir.dstu2.model.Reference(condition));
		patient.setCondition(conditions);

		String encoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(patient);
		ourLog.info(encoded);

		//@formatter:off
		assertThat(encoded, stringContainsInOrder(
			"<Patient xmlns=\"http://hl7.org/fhir\">",
			"<contained>",
			"<Condition xmlns=\"http://hl7.org/fhir\">",
			"<id value=\"1\"/>",
			"<bodySite>",
			"<text value=\"BODY SITE\"/>",
			"</bodySite>",
			"</Condition>",
			"</contained>",
			"<extension url=\"testCondition\">",
			"<valueReference>",
			"<reference value=\"#1\"/>",
			"</valueReference>",
			"</extension>",
			"<birthDate value=\"2016-04-14\"/>",
			"</Patient>"
		));
		//@formatter:on
	}

	@Test
	public void testEncodeExtensionWithResourceContent() {
		IParser parser = ourCtx.newXmlParser();

		org.hl7.fhir.dstu2.model.Patient patient = new org.hl7.fhir.dstu2.model.Patient();
		patient.addAddress().setUse(Address.AddressUse.HOME);
		patient.addExtension().setUrl("urn:foo").setValue(new org.hl7.fhir.dstu2.model.Reference().setReference("Organization/123"));

		String val = parser.encodeResourceToString(patient);
		ourLog.info(val);
		assertThat(val, StringContains.containsString(
			"<extension url=\"urn:foo\"><valueReference><reference value=\"Organization/123\"/></valueReference></extension>"));

		org.hl7.fhir.dstu2.model.Patient actual = parser.parseResource(org.hl7.fhir.dstu2.model.Patient.class, val);
		assertEquals(Address.AddressUse.HOME, patient.getAddress().get(0).getUse());
		List<org.hl7.fhir.dstu2.model.Extension> ext = actual.getExtension();
		assertEquals(1, ext.size());
		org.hl7.fhir.dstu2.model.Reference ref = (org.hl7.fhir.dstu2.model.Reference) ext.get(0).getValue();
		assertEquals("Organization/123", ref.getReferenceElement().getValue());

	}

	@Test
	public void testEncodeInvalidChildGoodException() {
		org.hl7.fhir.dstu2.model.Observation obs = new org.hl7.fhir.dstu2.model.Observation();
		obs.setValue(new org.hl7.fhir.dstu2.model.DecimalType(112.22));

		IParser p = ourCtx.newJsonParser();

		try {
			p.encodeResourceToString(obs);
		} catch (DataFormatException e) {
			assertThat(e.getMessage(), StringContains.containsString("DecimalType"));
		}
	}

	@Test
	public void testEncodeNarrativeSuppressed() throws Exception {
		org.hl7.fhir.dstu2.model.Patient patient = new org.hl7.fhir.dstu2.model.Patient();
		patient.setId("Patient/1/_history/1");
		patient.getText().setDivAsString("<div>THE DIV</div>");
		patient.addName().addFamily("FAMILY");
		patient.getMaritalStatus().addCoding().setCode("D");

		String encoded = ourCtx.newXmlParser().setPrettyPrint(true).setSuppressNarratives(true)
			.encodeResourceToString(patient);
		ourLog.info(encoded);

		assertThat(encoded, containsString("<Patient"));
		assertThat(encoded, stringContainsInOrder("<tag>", "<system value=\"" + Constants.TAG_SUBSETTED_SYSTEM_DSTU3 + "\"/>",
			"<code value=\"" + Constants.TAG_SUBSETTED_CODE + "\"/>", "</tag>"));
		assertThat(encoded, not(containsString("text")));
		assertThat(encoded, not(containsString("THE DIV")));
		assertThat(encoded, containsString("family"));
		assertThat(encoded, containsString("maritalStatus"));
	}

	@Test
	public void testEncodeNonContained() {
		// Create an organization
		Organization org = new Organization();
		org.setId("Organization/65546");
		org.getNameElement().setValue("Contained Test Organization");

		// Create a patient
		org.hl7.fhir.dstu2.model.Patient patient = new org.hl7.fhir.dstu2.model.Patient();
		patient.setId("Patient/1333");
		patient.addIdentifier().setSystem("urn:mrns").setValue("253345");
		patient.getManagingOrganization().setResource(org);

		// Create a list containing both resources. In a server method, you might
		// just
		// return this list, but here we will create a bundle to encode.
		List<Resource> resources = new ArrayList<Resource>();
		resources.add(org);
		resources.add(patient);

		// Create a bundle with both
		org.hl7.fhir.dstu2.model.Bundle b = new org.hl7.fhir.dstu2.model.Bundle();
		b.addEntry().setResource(org);
		b.addEntry().setResource(patient);

		// Encode the buntdle
		String encoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(b);
		ourLog.info(encoded);
		assertThat(encoded, not(containsString("<contained>")));
		assertThat(encoded, stringContainsInOrder("<Organization", "<id value=\"65546\"/>", "</Organization>"));
		assertThat(encoded, containsString("<reference value=\"Organization/65546\"/>"));
		assertThat(encoded, stringContainsInOrder("<Patient", "<id value=\"1333\"/>", "</Patient>"));

		encoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(patient);
		ourLog.info(encoded);
		assertThat(encoded, not(containsString("<contained>")));
		assertThat(encoded, containsString("<reference value=\"Organization/65546\"/>"));

	}

	@Test
	public void testEncodePrettyPrint() throws Exception {

		org.hl7.fhir.dstu2.model.Patient patient = new org.hl7.fhir.dstu2.model.Patient();
		patient.getText().setDivAsString("<div>\n  <i>  hello     <pre>\n  LINE1\n  LINE2</pre></i>\n\n\n\n</div>");
		patient.addName().addFamily("Family").addGiven("Given");

		// @formatter:off
		String encoded = ourCtx.newXmlParser().setPrettyPrint(false).encodeResourceToString(patient);
		ourLog.info(encoded);
    /*
     * Note at least one space is placed where any whitespace was, as it is hard
     * to tell what whitespace had no purpose
     */
		String expected = "<Patient xmlns=\"http://hl7.org/fhir\"><text><div xmlns=\"http://www.w3.org/1999/xhtml\">"
			+ " <i> hello " + "<pre>\n  LINE1\n  LINE2</pre>"
			+ "</i> </div></text><name><family value=\"Family\"/><given value=\"Given\"/></name></Patient>";
		assertEquals(expected, encoded);

		encoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(patient);
		ourLog.info(encoded);
		expected = "<Patient xmlns=\"http://hl7.org/fhir\">\n" + "   <text>\n"
			+ "      <div xmlns=\"http://www.w3.org/1999/xhtml\"> \n" + "         <i> hello \n"
			+ "            <pre>\n  LINE1\n  LINE2</pre>\n" + "         </i> \n" + "      </div>\n" + "   </text>\n"
			+ "   <name>\n" + "      <family value=\"Family\"/>\n" + "      <given value=\"Given\"/>\n" + "   </name>\n"
			+ "</Patient>";
		// @formatter:on

		// Whitespace should be preserved and not reformatted in narrative blocks
		assertEquals(expected, encoded);

	}

	@Test
	public void testEncodeResourceRef() throws DataFormatException {

		org.hl7.fhir.dstu2.model.Patient patient = new org.hl7.fhir.dstu2.model.Patient();
		patient.setManagingOrganization(new org.hl7.fhir.dstu2.model.Reference());

		IParser p = ourCtx.newXmlParser();
		String str = p.encodeResourceToString(patient);
		assertThat(str, IsNot.not(StringContains.containsString("managingOrganization")));

		org.hl7.fhir.dstu2.model.Reference ref = new org.hl7.fhir.dstu2.model.Reference();
		ref.setReference("Organization/123");
		ref.setDisplay("DISPLAY!");
		patient.setManagingOrganization(ref);
		str = p.encodeResourceToString(patient);
		assertThat(str, StringContains.containsString(
			"<managingOrganization><reference value=\"Organization/123\"/><display value=\"DISPLAY!\"/></managingOrganization>"));

		Organization org = new Organization();
		org.addIdentifier().setSystem("foo").setValue("bar");
		patient.setManagingOrganization(new org.hl7.fhir.dstu2.model.Reference(org));
		str = p.encodeResourceToString(patient);
		assertThat(str, StringContains.containsString("<contained><Organization"));

	}

	@Test
	public void testEncodeSummary() throws Exception {
		org.hl7.fhir.dstu2.model.Patient patient = new org.hl7.fhir.dstu2.model.Patient();
		patient.setId("Patient/1/_history/1");
		patient.getText().setDivAsString("<div>THE DIV</div>");
		patient.addName().addFamily("FAMILY");
		patient.getMaritalStatus().addCoding().setCode("D");

		String encoded = ourCtx.newXmlParser().setPrettyPrint(true).setSummaryMode(true).encodeResourceToString(patient);
		ourLog.info(encoded);

		assertThat(encoded, containsString("<Patient"));
		assertThat(encoded, stringContainsInOrder("<tag>", "<system value=\"" + Constants.TAG_SUBSETTED_SYSTEM_DSTU3 + "\"/>",
			"<code value=\"" + Constants.TAG_SUBSETTED_CODE + "\"/>", "</tag>"));
		assertThat(encoded, not(containsString("THE DIV")));
		assertThat(encoded, containsString("family"));
		assertThat(encoded, not(containsString("maritalStatus")));
	}

	@Test
	public void testEncodeSummary2() throws Exception {
		org.hl7.fhir.dstu2.model.Patient patient = new org.hl7.fhir.dstu2.model.Patient();
		patient.setId("Patient/1/_history/1");
		patient.getText().setDivAsString("<div>THE DIV</div>");
		patient.addName().addFamily("FAMILY");
		patient.setMaritalStatus(new CodeableConcept().setText("D"));

		patient.getMeta().addTag().setSystem("foo").setCode("bar");

		String encoded = ourCtx.newXmlParser().setPrettyPrint(true).setSummaryMode(true).encodeResourceToString(patient);
		ourLog.info(encoded);

		assertThat(encoded, containsString("<Patient"));
		assertThat(encoded, stringContainsInOrder("<tag>", "<system value=\"foo\"/>", "<code value=\"bar\"/>", "</tag>"));
		assertThat(encoded, stringContainsInOrder("<tag>", "<system value=\"" + Constants.TAG_SUBSETTED_SYSTEM_DSTU3 + "\"/>",
			"<code value=\"" + Constants.TAG_SUBSETTED_CODE + "\"/>", "</tag>"));
		assertThat(encoded, not(containsString("THE DIV")));
		assertThat(encoded, containsString("family"));
		assertThat(encoded, not(containsString("maritalStatus")));
	}

	@Test
	public void testEncodeUndeclaredExtensionWithAddressContent() {
		IParser parser = ourCtx.newXmlParser();

		org.hl7.fhir.dstu2.model.Patient patient = new org.hl7.fhir.dstu2.model.Patient();
		patient.addAddress().setUse(Address.AddressUse.HOME);
		patient.addExtension().setUrl("urn:foo").setValue(new Address().addLine("line1"));

		String val = parser.encodeResourceToString(patient);
		ourLog.info(val);
		assertThat(val, StringContains
			.containsString("<extension url=\"urn:foo\"><valueAddress><line value=\"line1\"/></valueAddress></extension>"));

		MyPatientWithOneDeclaredAddressExtension actual = parser
			.parseResource(MyPatientWithOneDeclaredAddressExtension.class, val);
		assertEquals(Address.AddressUse.HOME, patient.getAddress().get(0).getUse());
		Address ref = actual.getFoo();
		assertEquals("line1", ref.getLine().get(0).getValue());

	}

	@Test
	public void testEncodeUndeclaredExtensionWithEnumerationContent() {
		IParser parser = ourCtx.newXmlParser();

		org.hl7.fhir.dstu2.model.Patient patient = new org.hl7.fhir.dstu2.model.Patient();
		patient.addAddress().setUse(Address.AddressUse.HOME);
		EnumFactory<Address.AddressUse> fact = new Address.AddressUseEnumFactory();
		PrimitiveType<Address.AddressUse> enumeration = new Enumeration<Address.AddressUse>(fact).setValue(Address.AddressUse.HOME);
		patient.addExtension().setUrl("urn:foo").setValue(enumeration);

		String val = parser.encodeResourceToString(patient);
		ourLog.info(val);
		assertThat(val,
			StringContains.containsString("<extension url=\"urn:foo\"><valueCode value=\"home\"/></extension>"));

		MyPatientWithOneDeclaredEnumerationExtension actual = parser
			.parseResource(MyPatientWithOneDeclaredEnumerationExtension.class, val);
		assertEquals(Address.AddressUse.HOME, patient.getAddress().get(0).getUse());
		Enumeration<Address.AddressUse> ref = actual.getFoo();
		assertEquals("home", ref.getValue().toCode());

	}

	@Test
	public void testEncodingNullExtension() {
		org.hl7.fhir.dstu2.model.Patient p = new org.hl7.fhir.dstu2.model.Patient();
		org.hl7.fhir.dstu2.model.Extension extension = new org.hl7.fhir.dstu2.model.Extension().setUrl("http://foo#bar");
		p.getExtension().add(extension);
		String str = ourCtx.newXmlParser().encodeResourceToString(p);

		assertEquals("<Patient xmlns=\"http://hl7.org/fhir\"/>", str);

		extension.setValue(new org.hl7.fhir.dstu2.model.StringType());

		str = ourCtx.newXmlParser().encodeResourceToString(p);
		assertEquals("<Patient xmlns=\"http://hl7.org/fhir\"/>", str);

		extension.setValue(new org.hl7.fhir.dstu2.model.StringType(""));

		str = ourCtx.newXmlParser().encodeResourceToString(p);
		assertEquals("<Patient xmlns=\"http://hl7.org/fhir\"/>", str);

	}

	@Test
	public void testExtensionOnComposite() throws Exception {

		org.hl7.fhir.dstu2.model.Patient patient = new org.hl7.fhir.dstu2.model.Patient();

		org.hl7.fhir.dstu2.model.HumanName name = patient.addName();
		name.addFamily("Shmoe");
		org.hl7.fhir.dstu2.model.HumanName given = name.addGiven("Joe");
		org.hl7.fhir.dstu2.model.Extension ext2 = new org.hl7.fhir.dstu2.model.Extension().setUrl("http://examples.com#givenext").setValue(new org.hl7.fhir.dstu2.model.StringType("Hello"));
		given.getExtension().add(ext2);
		String output = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(patient);
		ourLog.info(output);

		String enc = ourCtx.newXmlParser().encodeResourceToString(patient);
		assertThat(enc, containsString(
			"<name><extension url=\"http://examples.com#givenext\"><valueString value=\"Hello\"/></extension><family value=\"Shmoe\"/><given value=\"Joe\"/></name>"));

		org.hl7.fhir.dstu2.model.Patient parsed = ourCtx.newXmlParser().parseResource(org.hl7.fhir.dstu2.model.Patient.class, new StringReader(enc));
		assertEquals(1, parsed.getName().get(0).getExtension().size());
		org.hl7.fhir.dstu2.model.Extension ext = parsed.getName().get(0).getExtension().get(0);
		assertEquals("Hello", ((IPrimitiveType<?>) ext.getValue()).getValue());

	}

	@Test
	public void testExtensionOnPrimitive() throws Exception {

		org.hl7.fhir.dstu2.model.Patient patient = new org.hl7.fhir.dstu2.model.Patient();

		org.hl7.fhir.dstu2.model.HumanName name = patient.addName();
		org.hl7.fhir.dstu2.model.StringType family = name.addFamilyElement();
		family.setValue("Shmoe");

		org.hl7.fhir.dstu2.model.Extension ext2 = new org.hl7.fhir.dstu2.model.Extension().setUrl("http://examples.com#givenext").setValue(new org.hl7.fhir.dstu2.model.StringType("Hello"));
		family.getExtension().add(ext2);
		String output = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(patient);
		ourLog.info(output);

		String enc = ourCtx.newXmlParser().encodeResourceToString(patient);
		assertThat(enc, containsString(
			"<name><family value=\"Shmoe\"><extension url=\"http://examples.com#givenext\"><valueString value=\"Hello\"/></extension></family></name>"));

		org.hl7.fhir.dstu2.model.Patient parsed = ourCtx.newXmlParser().parseResource(org.hl7.fhir.dstu2.model.Patient.class, new StringReader(enc));
		assertEquals(1, parsed.getName().get(0).getFamily().get(0).getExtension().size());
		org.hl7.fhir.dstu2.model.Extension ext = parsed.getName().get(0).getFamily().get(0).getExtension().get(0);
		assertEquals("Hello", ((IPrimitiveType<?>) ext.getValue()).getValue());

	}

	@Test
	public void testExtensions() throws DataFormatException {

		MyPatientHl7Org patient = new MyPatientHl7Org();
		patient.setPetName(new org.hl7.fhir.dstu2.model.StringType("Fido"));
		patient.getImportantDates().add(new org.hl7.fhir.dstu2.model.DateTimeType("2010-01-02"));
		patient.getImportantDates().add(new org.hl7.fhir.dstu2.model.DateTimeType("2014-01-26T11:11:11"));

		patient.addName().addFamily("Smith");

		IParser p = ourCtx.newXmlParser();
		String str = p.encodeResourceToString(patient);

		ourLog.info(str);

		assertThat(str, StringContains.containsString("<Patient xmlns=\"http://hl7.org/fhir\">"));
		assertThat(str, StringContains.containsString(
			"<extension url=\"http://example.com/dontuse#petname\"><valueString value=\"Fido\"/></extension>"));
		assertThat(str, StringContains.containsString(
			"<modifierExtension url=\"http://example.com/dontuse#importantDates\"><valueDateTime value=\"2010-01-02\"/></modifierExtension>"));
		assertThat(str, StringContains.containsString(
			"<modifierExtension url=\"http://example.com/dontuse#importantDates\"><valueDateTime value=\"2014-01-26T11:11:11\"/></modifierExtension>"));
		assertThat(str, StringContains.containsString("<name><family value=\"Smith\"/></name>"));

	}

	@Test
	public void testLoadAndAncodeMessage() throws Exception {

		// @formatter:off
		String msg = "<Patient xmlns=\"http://hl7.org/fhir\">"
			+ "<text><status value=\"generated\"/><div xmlns=\"http://www.w3.org/1999/xhtml\">John Cardinal:            444333333 </div></text>"
			+ "<identifier><system value=\"http://orionhealth.com/mrn\"/><value value=\"PRP1660\"/></identifier>"
			+ "<active value=\"true\"/>"
			+ "<name><use value=\"official\"/><family value=\"Cardinal\"/><given value=\"John\"/></name>"
			+ "<name><family value=\"Kramer\"/><given value=\"Doe\" /></name>"
			+ "<telecom><system value=\"phone\"/><value value=\"555-555-2004\" /><use value=\"work\"/></telecom>"
			+ "<gender value=\"male\"/>" + "<address><use value=\"home\"/><line value=\"2222 Home Street\"/></address>"
			+ "</Patient>";
		// @formatter:on

		org.hl7.fhir.dstu2.model.Patient patient = ourCtx.newXmlParser().parseResource(org.hl7.fhir.dstu2.model.Patient.class, msg);

		assertEquals(Narrative.NarrativeStatus.GENERATED, patient.getText().getStatus());
		assertThat(patient.getText().getDiv().getValueAsString(), containsString(">John Cardinal:            444333333 <"));
		assertEquals("PRP1660", patient.getIdentifier().get(0).getValue());

		String encoded = ourCtx.newXmlParser().encodeResourceToString(patient);

		ourLog.info(ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(patient));

		ourLog.info("Expected: {}", msg);
		ourLog.info("Actual:   {}", encoded);

		compareXml(msg, encoded);

	}

	@Test
	public void testLoadAndEncodeDeclaredExtensions()
		throws ConfigurationException, DataFormatException, SAXException, IOException {
		IParser p = ourCtx.newXmlParser();
		ourCtx.setAddProfileTagWhenEncoding(AddProfileTagEnum.NEVER);

		// @formatter:off
		String msg = "<ResourceWithExtensionsA xmlns=\"http://hl7.org/fhir\">\n" + "	<extension url=\"http://foo/#f1\">\n"
			+ "		<valueString value=\"Foo1Value\"/>\n" + "	</extension>\n" + "	<extension url=\"http://foo/#f1\">\n"
			+ "		<valueString value=\"Foo1Value2\"/>\n" + "	</extension>\n" + "	<extension url=\"http://bar/#b1\">\n"
			+ "		<extension url=\"http://bar/#b1/1\">\n" + "			<valueDate value=\"2013-01-01\"/>\n"
			+ "		</extension>\n" + "		<extension url=\"http://bar/#b1/2\">\n"
			+ "			<extension url=\"http://bar/#b1/2/1\">\n" + "				<valueDate value=\"2013-01-02\"/>\n"
			+ "			</extension>\n" + "			<extension url=\"http://bar/#b1/2/1\">\n"
			+ "				<valueDate value=\"2013-01-12\"/>\n" + "			</extension>\n"
			+ "			<extension url=\"http://bar/#b1/2/2\">\n" + "				<valueDate value=\"2013-01-03\"/>\n"
			+ "			</extension>\n" + "		</extension>\n" + "	</extension>\n"
			+ " <modifierExtension url=\"http://foo/#f2\">\n" + "   <valueString value=\"Foo2Value1\"/>\n"
			+ " </modifierExtension>\n" + "	<identifier>\n" + "		<value value=\"IdentifierLabel\"/>\n" + "	</identifier>\n"
			+ "</ResourceWithExtensionsA>";
		// @formatter:on

		ResourceWithExtensionsA resource = (ResourceWithExtensionsA) p.parseResource(ResourceWithExtensionsA.class, msg);
		assertEquals("IdentifierLabel", resource.getIdentifier().get(0).getValue());
		assertEquals("Foo1Value", resource.getFoo1().get(0).getValue());
		assertEquals("Foo1Value2", resource.getFoo1().get(1).getValue());
		assertEquals("Foo2Value1", resource.getFoo2().getValue());
		assertEquals("2013-01-01", resource.getBar1().get(0).getBar11().get(0).getValueAsString());
		assertEquals("2013-01-02", resource.getBar1().get(0).getBar12().get(0).getBar121().get(0).getValueAsString());
		assertEquals("2013-01-12", resource.getBar1().get(0).getBar12().get(0).getBar121().get(1).getValueAsString());
		assertEquals("2013-01-03", resource.getBar1().get(0).getBar12().get(0).getBar122().get(0).getValueAsString());

		String encoded = p.setPrettyPrint(true).encodeResourceToString(resource);
		ourLog.info(encoded);

		compareXml(msg, encoded);
	}

	@Test
	public void testLoadAndEncodeUndeclaredExtensions()
		throws ConfigurationException, DataFormatException, SAXException, IOException {
		IParser p = ourCtx.newXmlParser();

		// @formatter:off
		String msg = "<Patient xmlns=\"http://hl7.org/fhir\">\n" + "	<extension url=\"http://foo/#f1\">\n"
			+ "		<valueString value=\"Foo1Value\"/>\n" + "	</extension>\n" + "	<extension url=\"http://foo/#f1\">\n"
			+ "		<valueString value=\"Foo1Value2\"/>\n" + "	</extension>\n" + "	<extension url=\"http://bar/#b1\">\n"
			+ "		<extension url=\"http://bar/#b1/1\">\n" + "			<valueDate value=\"2013-01-01\"/>\n"
			+ "		</extension>\n" + "		<extension url=\"http://bar/#b1/2\">\n"
			+ "			<extension url=\"http://bar/#b1/2/1\">\n" + "				<valueDate value=\"2013-01-02\"/>\n"
			+ "			</extension>\n" + "			<extension url=\"http://bar/#b1/2/1\">\n"
			+ "				<valueDate value=\"2013-01-12\"/>\n" + "			</extension>\n"
			+ "			<extension url=\"http://bar/#b1/2/2\">\n" + "				<valueDate value=\"2013-01-03\"/>\n"
			+ "			</extension>\n" + "		</extension>\n" + "	</extension>\n"
			+ "	<modifierExtension url=\"http://foo/#f2\">\n" + "		<valueString value=\"Foo2Value1\"/>\n"
			+ "	</modifierExtension>\n" + "	<identifier>\n" + "		<value value=\"IdentifierLabel\"/>\n" + "	</identifier>\n"
			+ "</Patient>";
		// @formatter:on

		org.hl7.fhir.dstu2.model.Patient resource = (org.hl7.fhir.dstu2.model.Patient) p.parseResource(msg);
		assertEquals("IdentifierLabel", resource.getIdentifier().get(0).getValue());
		assertEquals("Foo1Value", ((IPrimitiveType<?>) resource.getExtension().get(0).getValue()).getValueAsString());
		assertEquals("Foo1Value2", ((IPrimitiveType<?>) resource.getExtension().get(1).getValue()).getValueAsString());
		assertEquals("Foo2Value1",
			((IPrimitiveType<?>) resource.getModifierExtension().get(0).getValue()).getValueAsString());

		assertEquals("2013-01-01",
			((IPrimitiveType<?>) resource.getExtension().get(2).getExtension().get(0).getValue()).getValueAsString());
		assertEquals("2013-01-02",
			((IPrimitiveType<?>) resource.getExtension().get(2).getExtension().get(1).getExtension().get(0).getValue())
				.getValueAsString());

		String encoded = p.encodeResourceToString(resource);
		ourLog.info(encoded);

		compareXml(msg, encoded);
	}

	@Test
	public void testMoreExtensions() throws Exception {

		org.hl7.fhir.dstu2.model.Patient patient = new org.hl7.fhir.dstu2.model.Patient();
		patient.addIdentifier().setUse(org.hl7.fhir.dstu2.model.Identifier.IdentifierUse.OFFICIAL).setSystem("urn:example").setValue("7000135");

		org.hl7.fhir.dstu2.model.Extension ext = new org.hl7.fhir.dstu2.model.Extension();
		ext.setUrl("http://example.com/extensions#someext");
		ext.setValue(new org.hl7.fhir.dstu2.model.DateTimeType("2011-01-02T11:13:15"));

		// Add the extension to the resource
		patient.getExtension().add(ext);
		// END SNIPPET: resourceExtension

		// START SNIPPET: resourceStringExtension
		org.hl7.fhir.dstu2.model.HumanName name = patient.addName();
		name.addFamily("Shmoe");
		org.hl7.fhir.dstu2.model.StringType given = name.addGivenElement();
		given.setValue("Joe");
		org.hl7.fhir.dstu2.model.Extension ext2 = new org.hl7.fhir.dstu2.model.Extension().setUrl("http://examples.com#givenext").setValue(new org.hl7.fhir.dstu2.model.StringType("given"));
		given.getExtension().add(ext2);

		org.hl7.fhir.dstu2.model.StringType given2 = name.addGivenElement();
		given2.setValue("Shmoe");
		org.hl7.fhir.dstu2.model.Extension given2ext = new org.hl7.fhir.dstu2.model.Extension().setUrl("http://examples.com#givenext_parent");
		given2.getExtension().add(given2ext);
		given2ext.addExtension().setUrl("http://examples.com#givenext_child").setValue(new org.hl7.fhir.dstu2.model.StringType("CHILD"));
		// END SNIPPET: resourceStringExtension

		// START SNIPPET: subExtension
		org.hl7.fhir.dstu2.model.Extension parent = new org.hl7.fhir.dstu2.model.Extension().setUrl("http://example.com#parent");
		patient.getExtension().add(parent);

		org.hl7.fhir.dstu2.model.Extension child1 = new org.hl7.fhir.dstu2.model.Extension().setUrl("http://example.com#child").setValue(new org.hl7.fhir.dstu2.model.StringType("value1"));
		parent.getExtension().add(child1);

		org.hl7.fhir.dstu2.model.Extension child2 = new org.hl7.fhir.dstu2.model.Extension().setUrl("http://example.com#child").setValue(new org.hl7.fhir.dstu2.model.StringType("value1"));
		parent.getExtension().add(child2);
		// END SNIPPET: subExtension

		String output = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(patient);
		ourLog.info(output);

		String enc = ourCtx.newXmlParser().encodeResourceToString(patient);
		assertThat(enc, containsString(
			"<Patient xmlns=\"http://hl7.org/fhir\"><extension url=\"http://example.com/extensions#someext\"><valueDateTime value=\"2011-01-02T11:13:15\"/></extension>"));
		assertThat(enc, containsString(
			"<extension url=\"http://example.com#parent\"><extension url=\"http://example.com#child\"><valueString value=\"value1\"/></extension><extension url=\"http://example.com#child\"><valueString value=\"value1\"/></extension></extension>"));
		assertThat(enc, containsString(
			"<given value=\"Joe\"><extension url=\"http://examples.com#givenext\"><valueString value=\"given\"/></extension></given>"));
		assertThat(enc, containsString(
			"<given value=\"Shmoe\"><extension url=\"http://examples.com#givenext_parent\"><extension url=\"http://examples.com#givenext_child\"><valueString value=\"CHILD\"/></extension></extension></given>"));
	}

	@Test
	public void testNestedContainedResources() {

		org.hl7.fhir.dstu2.model.Observation A = new org.hl7.fhir.dstu2.model.Observation();
		A.getCode().setText("A");

		org.hl7.fhir.dstu2.model.Observation B = new org.hl7.fhir.dstu2.model.Observation();
		B.getCode().setText("B");
		A.addRelated().setTarget(new org.hl7.fhir.dstu2.model.Reference(B));

		org.hl7.fhir.dstu2.model.Observation C = new org.hl7.fhir.dstu2.model.Observation();
		C.getCode().setText("C");
		B.addRelated().setTarget(new org.hl7.fhir.dstu2.model.Reference(C));

		String str = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(A);
		ourLog.info(str);

		assertThat(str,
			stringContainsInOrder(Arrays.asList("<text value=\"B\"/>", "<text value=\"C\"/>", "<text value=\"A\"/>")));
		assertThat(str, stringContainsInOrder(Arrays.asList("<contained>", "</contained>", "<contained>", "</contained>")));

		org.hl7.fhir.dstu2.model.Observation obs = ourCtx.newXmlParser().parseResource(org.hl7.fhir.dstu2.model.Observation.class, str);
		assertEquals("A", obs.getCode().getText());

		org.hl7.fhir.dstu2.model.Observation obsB = (org.hl7.fhir.dstu2.model.Observation) obs.getRelated().get(0).getTarget().getResource();
		assertEquals("B", obsB.getCode().getText());

		org.hl7.fhir.dstu2.model.Observation obsC = (org.hl7.fhir.dstu2.model.Observation) obsB.getRelated().get(0).getTarget().getResource();
		assertEquals("C", obsC.getCode().getText());

	}

	@Test
	public void testParseBinaryResource() {

		org.hl7.fhir.dstu2.model.Binary val = ourCtx.newXmlParser().parseResource(org.hl7.fhir.dstu2.model.Binary.class,
			"<Binary xmlns=\"http://hl7.org/fhir\"><contentType value=\"foo\"/><content value=\"AQIDBA==\"/></Binary>");
		assertEquals("foo", val.getContentType());
		assertArrayEquals(new byte[] { 1, 2, 3, 4 }, val.getContent());

	}

	/**
	 * Thanks to Alexander Kley!
	 */
	@Test
	public void testParseContainedBinaryResource() throws Exception {
		byte[] bin = new byte[] { 0, 1, 2, 3, 4 };
		final org.hl7.fhir.dstu2.model.Binary binary = new org.hl7.fhir.dstu2.model.Binary();
		binary.setContentType("PatientConsent").setContent(bin);
		// binary.setId(UUID.randomUUID().toString());

		org.hl7.fhir.dstu2.model.DocumentManifest manifest = new org.hl7.fhir.dstu2.model.DocumentManifest();
		// manifest.setId(UUID.randomUUID().toString());
		CodeableConcept cc = new CodeableConcept();
		cc.addCoding().setSystem("mySystem").setCode("PatientDocument");
		manifest.setType(cc);
		manifest.setMasterIdentifier(new org.hl7.fhir.dstu2.model.Identifier().setSystem("mySystem").setValue(UUID.randomUUID().toString()));
		manifest.addContent().setP(new org.hl7.fhir.dstu2.model.Reference(binary));
		manifest.setStatus(org.hl7.fhir.dstu2.model.Enumerations.DocumentReferenceStatus.CURRENT);

		String encoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(manifest);
		ourLog.info(encoded);
		assertThat(encoded,
			StringContainsInOrder.stringContainsInOrder(Arrays.asList("contained>", "<Binary", "</contained>")));

		org.hl7.fhir.dstu2.model.DocumentManifest actual = ourCtx.newXmlParser().parseResource(org.hl7.fhir.dstu2.model.DocumentManifest.class, encoded);
		assertEquals(1, actual.getContained().size());
		assertEquals(1, actual.getContent().size());

    /*
     * If this fails, the child named "p" in DocumentManifest is missing the
     * type IBaseResource in its definition... This isn't being auto added right
     * now, need to figure out why
     *
     * @Child(name = "p", type = {Attachment.class, IBaseResource.class},
     * order=1, min=1, max=1, modifier=false, summary=true)
     */
		assertNotNull(actual.getContent().get(0).getPReference().getResource());

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

	@Test
	public void testParseLanguage() {
		String input = "<Patient xmlns=\"http://hl7.org/fhir\"><language value=\"zh-CN\"/><text><status value=\"generated\"/><div xmlns=\"http://www.w3.org/1999/xhtml\"><div class=\"hapiHeaderText\"> 海生 <b>王 </b></div><table class=\"hapiPropertyTable\"><tbody><tr><td>Identifier</td><td>URNo</td></tr><tr><td>Address</td><td><span>99 Houston Road </span><br/><span>BENTLEIGH </span><span>Victoria </span></td></tr><tr><td>Date of birth</td><td><span>01 January 1997</span></td></tr></tbody></table></div></text><identifier><use value=\"usual\"/><label value=\"URNo\"/><value value=\"89532\"/></identifier><name><text value=\"王海生\"/><family value=\"王\"/><given value=\"海生\"/></name><telecom><system value=\"phone\"/><value value=\"9899 9878\"/><use value=\"home\"/></telecom><telecom><system value=\"email\"/><value value=\"zimmerman@datacorp.com.au\"/><use value=\"home\"/></telecom><gender><coding><system value=\"http://hl7.org/fhir/v3/AdministrativeGender\"/><code value=\"M\"/><display value=\"Male\"/></coding><text value=\"Male\"/></gender><birthDate value=\"1997-01-01\"/><address><use value=\"home\"/><text value=\"99 Houston Road, BENTLEIGH, 3204\"/><line value=\"99 Houston Road\"/><city value=\"BENTLEIGH\"/><state value=\"Victoria\"/><zip value=\"3204\"/><period><start value=\"2006-06-16\"/></period></address><active value=\"true\"/></Patient>";
		org.hl7.fhir.dstu2.model.Patient pt = ourCtx.newXmlParser().parseResource(org.hl7.fhir.dstu2.model.Patient.class, input);

		assertEquals("zh-CN", pt.getLanguage());
	}

	@Test
	public void testParseWithXmlHeader() throws ConfigurationException, DataFormatException {
		IParser p = ourCtx.newXmlParser();

		// @formatter:off
		String msg = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<Patient xmlns=\"http://hl7.org/fhir\">\n"
			+ "	<identifier>\n" + "		<value value=\"IdentifierLabel\"/>\n" + "	</identifier>\n" + "</Patient>";
		// @formatter:on

		org.hl7.fhir.dstu2.model.Patient resource = (org.hl7.fhir.dstu2.model.Patient) p.parseResource(msg);
		assertEquals("IdentifierLabel", resource.getIdentifier().get(0).getValue());
	}

	@Test
	public void testReEncode() throws SAXException, IOException {

		// @formatter:off
		String msg = "<Patient xmlns=\"http://hl7.org/fhir\">"
			+ "<identifier><system value=\"http://orionhealth.com/mrn\" /><value value=\"PRP1660\" /></identifier>"
			+ "</Patient>";
		// @formatter:on

		org.hl7.fhir.dstu2.model.Patient patient1 = ourCtx.newXmlParser().parseResource(org.hl7.fhir.dstu2.model.Patient.class, msg);
		String encoded1 = ourCtx.newXmlParser().encodeResourceToString(patient1);

		ourLog.info("Expected: {}", msg);
		ourLog.info("Actual:   {}", encoded1);

		compareXml(msg, encoded1);
	}

	@Test
	public void testSimpleResourceEncode() throws IOException, SAXException {

		String xmlString = IOUtils.toString(
			XmlParserHl7OrgDstu2Test.class.getResourceAsStream("/example-patient-general-hl7orgdstu2.json"),
			Charset.forName("UTF-8"));
		org.hl7.fhir.dstu2.model.Patient obs = ourCtx.newJsonParser().parseResource(org.hl7.fhir.dstu2.model.Patient.class, xmlString);

		List<org.hl7.fhir.dstu2.model.Extension> undeclaredExtensions = obs.getContact().get(0).getName().getFamily().get(0).getExtension();
		org.hl7.fhir.dstu2.model.Extension undeclaredExtension = undeclaredExtensions.get(0);
		assertEquals("http://hl7.org/fhir/Profile/iso-21090#qualifier", undeclaredExtension.getUrl());

		ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToWriter(obs, new OutputStreamWriter(System.out));

		IParser jsonParser = ourCtx.newXmlParser();
		String encoded = jsonParser.encodeResourceToString(obs);
		ourLog.info(encoded);

		String jsonString = IOUtils.toString(
			XmlParserHl7OrgDstu2Test.class.getResourceAsStream("/example-patient-general-hl7orgdstu2.xml"),
			Charset.forName("UTF-8"));

		String expected = (jsonString);
		String actual = (encoded.trim());

		compareXml(expected, actual);
	}

	@Test
	public void testBaseUrlFooResourceCorrectlySerializedInExtensionValueReference() {
		String refVal = "http://my.org/FooBar";

		org.hl7.fhir.dstu2.model.Patient fhirPat = new org.hl7.fhir.dstu2.model.Patient();
		fhirPat.addExtension().setUrl("x1").setValue(new org.hl7.fhir.dstu2.model.Reference(refVal));

		IParser parser = ourCtx.newXmlParser();

		String output = parser.encodeResourceToString(fhirPat);
		System.out.println("output: " + output);

		// Deserialize then check that valueReference value is still correct
		fhirPat = parser.parseResource(org.hl7.fhir.dstu2.model.Patient.class, output);

		List<org.hl7.fhir.dstu2.model.Extension> extlst = fhirPat.getExtension();
		assertEquals(1, extlst.size());
		assertEquals(refVal, ((org.hl7.fhir.dstu2.model.Reference) extlst.get(0).getValue()).getReference());
	}

	@ResourceDef(name = "Patient")
	public static class TestPatientFor327 extends Patient {

		private static final long serialVersionUID = 1L;

		@Child(name = "testCondition")
		@ca.uhn.fhir.model.api.annotation.Extension(url = "testCondition", definedLocally = true, isModifier = false)
		private List<org.hl7.fhir.dstu2.model.Reference> testConditions = null;

		public List<org.hl7.fhir.dstu2.model.Reference> getConditions() {
			return this.testConditions;
		}

		public void setCondition(List<org.hl7.fhir.dstu2.model.Reference> ref) {
			this.testConditions = ref;
		}
	}

	public static void compareXml(String content, String reEncoded) {
		Diff d = DiffBuilder.compare(Input.fromString(content))
			.withTest(Input.fromString(reEncoded))
			.withNodeMatcher(new DefaultNodeMatcher(ElementSelectors.byNameAndText))
			.checkForSimilar()
			.ignoreWhitespace()
			.ignoreComments()
			.withComparisonController(ComparisonControllers.Default)
			.build();

		assertTrue(d.toString(), !d.hasDifferences());
	}

	@ResourceDef(name = "Patient")
	public static class MyPatientWithOneDeclaredAddressExtension extends Patient {

		private static final long serialVersionUID = 1L;

		@Child(order = 0, name = "foo")
		@ca.uhn.fhir.model.api.annotation.Extension(url = "urn:foo", definedLocally = true, isModifier = false)
		private Address myFoo;

		public Address getFoo() {
			return myFoo;
		}

		public void setFoo(Address theFoo) {
			myFoo = theFoo;
		}

	}

	@ResourceDef(name = "Patient")
	public static class MyPatientWithOneDeclaredExtension extends Patient {

		private static final long serialVersionUID = 1L;

		@Child(order = 0, name = "foo")
		@ca.uhn.fhir.model.api.annotation.Extension(url = "urn:foo", definedLocally = true, isModifier = false)
		private Reference myFoo;

		public Reference getFoo() {
			return myFoo;
		}

		public void setFoo(Reference theFoo) {
			myFoo = theFoo;
		}

	}

}
