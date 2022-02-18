package ca.uhn.fhir.tests.integration.karaf.dstu2hl7org;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import ca.uhn.fhir.context.FhirContext;
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
import org.hl7.fhir.dstu2.model.Address.AddressUse;
import org.hl7.fhir.dstu2.model.Address.AddressUseEnumFactory;
import org.hl7.fhir.dstu2.model.Binary;
import org.hl7.fhir.dstu2.model.Bundle;
import org.hl7.fhir.dstu2.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.dstu2.model.CodeableConcept;
import org.hl7.fhir.dstu2.model.Conformance;
import org.hl7.fhir.dstu2.model.Conformance.UnknownContentCode;
import org.hl7.fhir.dstu2.model.DateTimeType;
import org.hl7.fhir.dstu2.model.DateType;
import org.hl7.fhir.dstu2.model.DecimalType;
import org.hl7.fhir.dstu2.model.DiagnosticReport;
import org.hl7.fhir.dstu2.model.EnumFactory;
import org.hl7.fhir.dstu2.model.Enumeration;
import org.hl7.fhir.dstu2.model.Extension;
import org.hl7.fhir.dstu2.model.HumanName;
import org.hl7.fhir.dstu2.model.Identifier.IdentifierUse;
import org.hl7.fhir.dstu2.model.InstantType;
import org.hl7.fhir.dstu2.model.List_;
import org.hl7.fhir.dstu2.model.Narrative.NarrativeStatus;
import org.hl7.fhir.dstu2.model.Observation;
import org.hl7.fhir.dstu2.model.Organization;
import org.hl7.fhir.dstu2.model.Patient;
import org.hl7.fhir.dstu2.model.Patient.ContactComponent;
import org.hl7.fhir.dstu2.model.PrimitiveType;
import org.hl7.fhir.dstu2.model.Reference;
import org.hl7.fhir.dstu2.model.Specimen;
import org.hl7.fhir.dstu2.model.StringType;
import org.hl7.fhir.dstu2.model.ValueSet;
import org.hl7.fhir.dstu2.model.ValueSet.ConceptDefinitionComponent;
import org.hl7.fhir.dstu2.model.ValueSet.ValueSetCodeSystemComponent;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.utilities.xhtml.XhtmlNode;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;
import org.xml.sax.SAXException;

import static ca.uhn.fhir.tests.integration.karaf.PaxExamOptions.HAPI_FHIR_HL7ORG_DSTU2;
import static ca.uhn.fhir.tests.integration.karaf.PaxExamOptions.KARAF;
import static ca.uhn.fhir.tests.integration.karaf.PaxExamOptions.WRAP;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
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
public class JsonParserHl7OrgDstu2Test {

  private FhirContext ourCtx = FhirContext.forDstu2Hl7Org();
  private final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(JsonParserHl7OrgDstu2Test.class);

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
		try {
			String tmp = "{\"resourceType\":\"Bundle\",\"entry\":[{\"fullUrl\":\"http://lalaland.org/patient/pat1\",\"resource\":{\"resourceType\":\"Patient\",\"id\":\"patxuzos\"}}]}";
			Bundle bundle = (Bundle) ourCtx.newJsonParser().parseResource(tmp);
			assertEquals(1, bundle.getEntry().size());
			{
				Patient o1 = (Patient) bundle.getEntry().get(0).getResource();
				IIdType o1Id = o1.getIdElement();
				assertEquals("http://lalaland.org", o1Id.getBaseUrl());
				assertEquals("patient", o1Id.getResourceType());
				assertEquals("pat1", o1Id.getIdPart());
				assertFalse(o1Id.hasVersionIdPart());
			}
		} finally {
			// ensure we cleanup ourCtx so other tests continue to work
			ourCtx = FhirContext.forDstu2Hl7Org();
		}
	}

	@Test
	public void testOverrideResourceIdWithBundleEntryFullUrlDisabled_ConfiguredOnFhirContext() {
		try {
			String tmp = "{\"resourceType\":\"Bundle\",\"entry\":[{\"fullUrl\":\"http://lalaland.org/patient/pat1\",\"resource\":{\"resourceType\":\"Patient\",\"id\":\"patxuzos\"}}]}";
			ourCtx.getParserOptions().setOverrideResourceIdWithBundleEntryFullUrl(false);
			Bundle bundle = (Bundle) ourCtx.newJsonParser().parseResource(tmp);
			assertEquals(1, bundle.getEntry().size());
			{
				Patient o1 = (Patient) bundle.getEntry().get(0).getResource();
				IIdType o1Id = o1.getIdElement();
				assertFalse(o1Id.hasBaseUrl());
				assertEquals("Patient", o1Id.getResourceType());
				assertEquals("patxuzos", o1Id.getIdPart());
				assertFalse(o1Id.hasVersionIdPart());
			}
		} finally {
			// ensure we cleanup ourCtx so other tests continue to work
			ourCtx = FhirContext.forDstu2Hl7Org();
		}
	}

	@Test
	public void testOverrideResourceIdWithBundleEntryFullUrlDisabled_ConfiguredOnParser() {
		try {
			String tmp = "{\"resourceType\":\"Bundle\",\"entry\":[{\"fullUrl\":\"http://lalaland.org/patient/pat1\",\"resource\":{\"resourceType\":\"Patient\",\"id\":\"patxuzos\"}}]}";
			Bundle bundle = (Bundle) ourCtx.newJsonParser().setOverrideResourceIdWithBundleEntryFullUrl(false).parseResource(tmp);
			assertEquals(1, bundle.getEntry().size());
			{
				Patient o1 = (Patient) bundle.getEntry().get(0).getResource();
				IIdType o1Id = o1.getIdElement();
				assertFalse(o1Id.hasBaseUrl());
				assertEquals("Patient", o1Id.getResourceType());
				assertEquals("patxuzos", o1Id.getIdPart());
				assertFalse(o1Id.hasVersionIdPart());
			}
		} finally {
			// ensure we cleanup ourCtx so other tests continue to work
			ourCtx = FhirContext.forDstu2Hl7Org();
		}
	}

  @Test
  public void testEncodeUndeclaredExtensionWithEnumerationContent() {
    IParser parser = ourCtx.newJsonParser();

    Patient patient = new Patient();
    patient.addAddress().setUse(AddressUse.HOME);
    EnumFactory<AddressUse> fact = new AddressUseEnumFactory();
    PrimitiveType<AddressUse> enumeration = new Enumeration<AddressUse>(fact).setValue(AddressUse.HOME);
    patient.addExtension().setUrl("urn:foo").setValue(enumeration);

    String val = parser.encodeResourceToString(patient);
    ourLog.info(val);
    assertThat(val, StringContains.containsString("\"extension\":[{\"url\":\"urn:foo\",\"valueCode\":\"home\"}]"));

    MyPatientWithOneDeclaredEnumerationExtension actual = parser.parseResource(MyPatientWithOneDeclaredEnumerationExtension.class, val);
    assertEquals(AddressUse.HOME, patient.getAddress().get(0).getUse());
    Enumeration<AddressUse> ref = actual.getFoo();
    assertEquals("home", ref.getValue().toCode());

  }

  @Test
  public void testEncodeNarrativeSuppressed() throws Exception {
    Patient patient = new Patient();
    patient.setId("Patient/1/_history/1");
    patient.getText().setDivAsString("<div>THE DIV</div>");
    patient.addName().addFamily("FAMILY");
    patient.getMaritalStatus().addCoding().setCode("D");

    String encoded = ourCtx.newJsonParser().setPrettyPrint(true).setSuppressNarratives(true).encodeResourceToString(patient);
    ourLog.info(encoded);

    assertThat(encoded, containsString("Patient"));
    assertThat(encoded, stringContainsInOrder(Constants.TAG_SUBSETTED_SYSTEM_DSTU3, Constants.TAG_SUBSETTED_CODE));
    assertThat(encoded, not(containsString("text")));
    assertThat(encoded, not(containsString("THE DIV")));
    assertThat(encoded, containsString("family"));
    assertThat(encoded, containsString("maritalStatus"));
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
    Extension child1 = new Extension().setUrl("http://example.com#child").setValue(new StringType("value1"));
    parent.getExtension().add(child1);
    Extension child2 = new Extension().setUrl("http://example.com#child").setValue(new StringType("value2"));
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

    String output = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(patient);
    ourLog.info(output);

    String enc = ourCtx.newJsonParser().encodeResourceToString(patient);
    assertThat(enc,
        stringContainsInOrder("{\"resourceType\":\"Patient\",", "\"extension\":[{\"url\":\"http://example.com/extensions#someext\",\"valueDateTime\":\"2011-01-02T11:13:15\"}",
            "{\"url\":\"http://example.com#parent\",\"extension\":[{\"url\":\"http://example.com#child\",\"valueString\":\"value1\"},{\"url\":\"http://example.com#child\",\"valueString\":\"value2\"}]}"));
    assertThat(enc, stringContainsInOrder("\"modifierExtension\":[" + "{" + "\"url\":\"http://example.com/extensions#modext\"," + "\"valueDate\":\"1995-01-02\"" + "}" + "],"));
    assertThat(enc, containsString("\"_given\":[" + "{" + "\"extension\":[" + "{" + "\"url\":\"http://examples.com#givenext\"," + "\"valueString\":\"given\"" + "}" + "]" + "}," + "{"
        + "\"extension\":[" + "{" + "\"url\":\"http://examples.com#givenext_parent\"," + "\"extension\":[" + "{"
        + "\"url\":\"http://examples.com#givenext_child\"," + "\"valueString\":\"CHILD\"" + "}" + "]" + "}" + "]" + "}"));

    /*
     * Now parse this back
     */

    Patient parsed = ourCtx.newJsonParser().parseResource(Patient.class, enc);
    ext = parsed.getExtension().get(0);
    assertEquals("http://example.com/extensions#someext", ext.getUrl());
    assertEquals("2011-01-02T11:13:15", ((DateTimeType) ext.getValue()).getValueAsString());

    parent = patient.getExtension().get(1);
    assertEquals("http://example.com#parent", parent.getUrl());
    assertNull(parent.getValue());
    child1 = parent.getExtension().get(0);
    assertEquals("http://example.com#child", child1.getUrl());
    assertEquals("value1", ((StringType) child1.getValue()).getValueAsString());
    child2 = parent.getExtension().get(1);
    assertEquals("http://example.com#child", child2.getUrl());
    assertEquals("value2", ((StringType) child2.getValue()).getValueAsString());

    modExt = parsed.getModifierExtension().get(0);
    assertEquals("http://example.com/extensions#modext", modExt.getUrl());
    assertEquals("1995-01-02", ((DateType) modExt.getValue()).getValueAsString());

    name = parsed.getName().get(0);

    ext2 = name.getGiven().get(0).getExtension().get(0);
    assertEquals("http://examples.com#givenext", ext2.getUrl());
    assertEquals("given", ((StringType) ext2.getValue()).getValueAsString());

    given2ext = name.getGiven().get(1).getExtension().get(0);
    assertEquals("http://examples.com#givenext_parent", given2ext.getUrl());
    assertNull(given2ext.getValue());
    Extension given2ext2 = given2ext.getExtension().get(0);
    assertEquals("http://examples.com#givenext_child", given2ext2.getUrl());
    assertEquals("CHILD", ((StringType) given2ext2.getValue()).getValue());

  }

  @Test
  public void testEncodeBinaryResource() {

    Binary patient = new Binary();
    patient.setContentType("foo");
    patient.setContent(new byte[] { 1, 2, 3, 4 });

    String val = ourCtx.newJsonParser().encodeResourceToString(patient);
    assertEquals("{\"resourceType\":\"Binary\",\"contentType\":\"foo\",\"content\":\"AQIDBA==\"}", val);

  }

  @Test
  public void testEncodeBundle() throws InterruptedException {
    Bundle b = new Bundle();

    InstantType pub = InstantType.now();
    b.getMeta().setLastUpdatedElement(pub);
    Thread.sleep(2);

    Patient p1 = new Patient();
    p1.addName().addFamily("Family1");
    p1.setId("1");
    BundleEntryComponent entry = b.addEntry();
    entry.setResource(p1);

    Patient p2 = new Patient();
    p2.setId("Patient/2");
    p2.addName().addFamily("Family2");
    entry = b.addEntry();
    entry.setResource(p2);

    BundleEntryComponent deletedEntry = b.addEntry();
    Patient dp = new Patient();
    deletedEntry.setResource(dp);

    dp.setId(("3"));
    InstantType nowDt = InstantType.withCurrentTime();
    dp.getMeta().setLastUpdatedElement(nowDt);

    String bundleString = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(b);
    ourLog.info(bundleString);

    // List<String> strings = new ArrayList<String>();
    // strings.addAll(Arrays.asList("\"published\":\"" + pub.getValueAsString() + "\""));
    // strings.addAll(Arrays.asList("\"id\":\"1\""));
    // strings.addAll(Arrays.asList("\"id\":\"2\"", "\"rel\":\"alternate\"", "\"href\":\"http://foo/bar\""));
    // strings.addAll(Arrays.asList("\"deleted\":\"" + nowDt.getValueAsString() + "\"", "\"id\":\"Patient/3\""));

    //@formatter:off
		String[] strings = new String[] {
			"\"resourceType\": \"Bundle\",",
			"\"lastUpdated\": \"" + pub.getValueAsString() + "\"",
			"\"entry\": [",
			"\"resource\": {",
			"\"id\": \"1\"",
			"\"resource\": {",
			"\"id\": \"2\"",
			"\"resource\": {",
			"\"id\": \"3\"",
			"\"meta\": {",
			"\"lastUpdated\": \"" + nowDt.getValueAsString() + "\""
		};
		//@formatter:off
		assertThat(bundleString, StringContainsInOrder.stringContainsInOrder(Arrays.asList(strings)));

		b.getEntry().remove(2);
		bundleString = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(b);
		assertThat(bundleString, not(containsString("deleted")));

	}


	@Test
	public void testEncodeBundleCategory() {

		Bundle b = new Bundle();
		BundleEntryComponent e = b.addEntry();

		Patient pt = new Patient();
		pt.addIdentifier().setSystem("idsystem");
		e.setResource(pt);

		b.getMeta().addTag().setSystem("scheme").setCode("term").setDisplay("label");

		String val = ourCtx.newJsonParser().setPrettyPrint(false).encodeResourceToString(b);
		ourLog.info(val);

		assertThat(val, StringContains.containsString("\"tag\":[{\"system\":\"scheme\",\"code\":\"term\",\"display\":\"label\"}]"));
		b = ourCtx.newJsonParser().parseResource(Bundle.class, val);
		assertEquals(1, b.getMeta().getTag().size());
		assertEquals("scheme", b.getMeta().getTag().get(0).getSystem());
		assertEquals("term", b.getMeta().getTag().get(0).getCode());
		assertEquals("label", b.getMeta().getTag().get(0).getDisplay());

		assertNotNull(b.getEntry().get(0).getResource());
		Patient p = (Patient) b.getEntry().get(0).getResource();
		assertEquals("idsystem", p.getIdentifier().get(0).getSystem());

	}


	@Test
	public void testEncodeBundleEntryCategory() {

		Bundle b = new Bundle();
		BundleEntryComponent e = b.addEntry();
		e.setResource(new Patient());
		e.getResource().getMeta().addTag().setSystem("scheme").setCode( "term").setDisplay( "label");

		String val = ourCtx.newJsonParser().setPrettyPrint(false).encodeResourceToString(b);
		ourLog.info(val);

		assertThat(val, StringContains.containsString("{\"resourceType\":\"Bundle\",\"entry\":[{\"resource\":{\"resourceType\":\"Patient\",\"meta\":{\"tag\":[{\"system\":\"scheme\",\"code\":\"term\",\"display\":\"label\"}]}}}]}"));

		b = ourCtx.newJsonParser().parseResource(Bundle.class, val);
		assertEquals(1, b.getEntry().size());
		assertEquals(1, b.getEntry().get(0).getResource().getMeta().getTag().size());
		assertEquals("scheme", b.getEntry().get(0).getResource().getMeta().getTag().get(0).getSystem());
		assertEquals("term", b.getEntry().get(0).getResource().getMeta().getTag().get(0).getCode());
		assertEquals("label", b.getEntry().get(0).getResource().getMeta().getTag().get(0).getDisplay());

	}


	@Test
	public void testEncodeContained() {
		IParser jsonParser = ourCtx.newJsonParser().setPrettyPrint(true);

		// Create an organization, note that the organization does not have an ID
		Organization org = new Organization();
		org.getNameElement().setValue("Contained Test Organization");

		// Create a patient
		Patient patient = new Patient();
		patient.setId("Patient/1333");
		patient.addIdentifier().setSystem("urn:mrns").setValue("253345");

		// Put the organization as a reference in the patient resource
		patient.getManagingOrganization().setResource(org);

		String encoded = jsonParser.encodeResourceToString(patient);
		ourLog.info(encoded);
		assertThat(encoded, stringContainsInOrder(Arrays.asList("\"contained\": [", "\"id\": \"1\"", "\"identifier\"", "\"reference\": \"#1\"")));

		// Create a bundle with just the patient resource
		Bundle b = new Bundle();
		b.addEntry().setResource(patient);

		// Encode the bundle
		encoded = jsonParser.encodeResourceToString(b);
		ourLog.info(encoded);
		assertThat(encoded, stringContainsInOrder(Arrays.asList("\"contained\": [", "\"id\": \"1\"", "\"identifier\"", "\"reference\": \"#1\"")));

		// Re-parse the bundle
		patient = (Patient) jsonParser.parseResource(jsonParser.encodeResourceToString(patient));
		assertEquals("#1", patient.getManagingOrganization().getReference());

		assertNotNull(patient.getManagingOrganization().getResource());
		org = (Organization) patient.getManagingOrganization().getResource();
		assertEquals("#1", org.getIdElement().getValue());
		assertEquals("Contained Test Organization", org.getName());

		// And re-encode a second time
		encoded = jsonParser.encodeResourceToString(patient);
		ourLog.info(encoded);
		assertThat(encoded, stringContainsInOrder(Arrays.asList("\"contained\": [", "\"id\": \"1\"", "\"identifier\"", "\"reference\": \"#1\"")));
		assertThat(encoded, not(stringContainsInOrder(Arrays.asList("\"contained\":", "[", "\"contained\":"))));

		// And re-encode once more, with the references cleared
		patient.getContained().clear();
		patient.getManagingOrganization().setReference(null);
		encoded = jsonParser.encodeResourceToString(patient);
		ourLog.info(encoded);
		assertThat(encoded, stringContainsInOrder(Arrays.asList("\"contained\": [", "\"id\": \"1\"", "\"identifier\"", "\"reference\": \"#1\"")));
		assertThat(encoded, not(stringContainsInOrder(Arrays.asList("\"contained\":", "[", "\"contained\":"))));

		// And re-encode once more, with the references cleared and a manually set local ID
		patient.getContained().clear();
		patient.getManagingOrganization().setReference(null);
		patient.getManagingOrganization().getResource().setId(("#333"));
		encoded = jsonParser.encodeResourceToString(patient);
		ourLog.info(encoded);
		assertThat(encoded, stringContainsInOrder(Arrays.asList("\"contained\": [", "\"id\": \"333\"", "\"identifier\"", "\"reference\": \"#333\"")));
		assertThat(encoded, not(stringContainsInOrder(Arrays.asList("\"contained\":", "[", "\"contained\":"))));

	}

	@Test
	public void testEncodeContained__() {
		// Create an organization
		Organization org = new Organization();
		org.getNameElement().setValue("Contained Test Organization");

		// Create a patient
		Patient patient = new Patient();
		patient.setId("Patient/1333");
		patient.addIdentifier().setSystem("urn:mrns").setValue( "253345");
		patient.getManagingOrganization().setResource(org);

		// Create a bundle with just the patient resource
		Bundle b = new Bundle();
		b.addEntry().setResource(patient);

		// Encode the buntdle
		String encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(b);
		ourLog.info(encoded);
		assertThat(encoded, stringContainsInOrder(Arrays.asList("\"contained\"", "resourceType\": \"Organization", "id\": \"1\"")));
		assertThat(encoded, containsString("reference\": \"#1\""));

		encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(patient);
		ourLog.info(encoded);
		assertThat(encoded, stringContainsInOrder(Arrays.asList("\"contained\"", "resourceType\": \"Organization", "id\": \"1\"")));
		assertThat(encoded, containsString("reference\": \"#1\""));
	}

	@Test
	public void testEncodeContainedResourcesMore() throws Exception {

		DiagnosticReport rpt = new DiagnosticReport();
		Specimen spm = new Specimen();
		rpt.getText().setDivAsString("AAA");
		rpt.addSpecimen().setResource(spm);

		IParser p = ourCtx.newJsonParser().setPrettyPrint(true);
		String str = p.encodeResourceToString(rpt);

		ourLog.info(str);
		assertThat(str, StringContains.containsString("<div xmlns=\\\"http://www.w3.org/1999/xhtml\\\">AAA</div>"));
		String substring = "\"reference\": \"#";
		assertThat(str, StringContains.containsString(substring));

		int idx = str.indexOf(substring) + substring.length();
		int idx2 = str.indexOf('"', idx + 1);
		String id = str.substring(idx, idx2);
		assertThat(str, StringContains.containsString("\"id\": \"" + id + "\""));
		assertThat(str, IsNot.not(StringContains.containsString("<?xml version='1.0'?>")));

	}

	@Test
	public void testEncodeContainedWithNarrative() throws Exception {
		IParser parser = ourCtx.newJsonParser().setPrettyPrint(true);

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
		assertThat(encoded, (containsString("FOOBAR")));
		assertThat(encoded, (containsString("BARFOO")));

	}



	@Test
	public void testEncodeDeclaredExtensionWithAddressContent() {
		IParser parser = ourCtx.newJsonParser();

		MyPatientWithOneDeclaredAddressExtension patient = new MyPatientWithOneDeclaredAddressExtension();
		patient.addAddress().setUse(AddressUse.HOME);
		patient.setFoo(new Address().addLine("line1"));

		String val = parser.encodeResourceToString(patient);
		ourLog.info(val);
		assertThat(val, StringContains.containsString("\"extension\":[{\"url\":\"urn:foo\",\"valueAddress\":{\"line\":[\"line1\"]}}]"));

		MyPatientWithOneDeclaredAddressExtension actual = parser.parseResource(MyPatientWithOneDeclaredAddressExtension.class, val);
		assertEquals(AddressUse.HOME, patient.getAddress().get(0).getUse());
		Address ref = actual.getFoo();
		assertEquals("line1", ref.getLine().get(0).getValue());

	}

	@Test
	public void testEncodeDeclaredExtensionWithResourceContent() {
		IParser parser = ourCtx.newJsonParser();

		MyPatientWithOneDeclaredExtension patient = new MyPatientWithOneDeclaredExtension();
		patient.addAddress().setUse(AddressUse.HOME);
		patient.setFoo(new Reference("Organization/123"));

		String val = parser.encodeResourceToString(patient);
		ourLog.info(val);
		assertThat(val, StringContains.containsString("\"extension\":[{\"url\":\"urn:foo\",\"valueReference\":{\"reference\":\"Organization/123\"}}]"));

		MyPatientWithOneDeclaredExtension actual = parser.parseResource(MyPatientWithOneDeclaredExtension.class, val);
		assertEquals(AddressUse.HOME, patient.getAddress().get(0).getUse());
		Reference ref = actual.getFoo();
		assertEquals("Organization/123", ref.getReference());

	}

	@Test
	public void testEncodeExt() throws Exception {

		ValueSet valueSet = new ValueSet();
		valueSet.setId("123456");

		ValueSetCodeSystemComponent define = valueSet.getCodeSystem();
		ConceptDefinitionComponent code = define.addConcept();
		code.setCode("someCode");
		code.setDisplay("someDisplay");
		code.addExtension().setUrl("urn:alt").setValue( new StringType("alt name"));


		String encoded = ourCtx.newJsonParser().encodeResourceToString(valueSet);
		ourLog.info(encoded);

		assertThat(encoded, (containsString("123456")));
		assertEquals(
				"{\"resourceType\":\"ValueSet\",\"id\":\"123456\",\"codeSystem\":{\"concept\":[{\"extension\":[{\"url\":\"urn:alt\",\"valueString\":\"alt name\"}],\"code\":\"someCode\",\"display\":\"someDisplay\"}]}}",
				encoded);

	}

	@Test
	public void testEncodeExtensionInCompositeElement() {

		Conformance c = new Conformance();
		c.addRest().getSecurity().addExtension().setUrl("http://foo").setValue(new StringType("AAA"));

		String encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(c);
		ourLog.info(encoded);

		encoded = ourCtx.newJsonParser().setPrettyPrint(false).encodeResourceToString(c);
		ourLog.info(encoded);
		assertEquals(encoded, "{\"resourceType\":\"Conformance\",\"rest\":[{\"security\":{\"extension\":[{\"url\":\"http://foo\",\"valueString\":\"AAA\"}]}}]}");

	}



	@Test
	public void testEncodeExtensionInPrimitiveElement() {

		Conformance c = new Conformance();
		c.getAcceptUnknownElement().addExtension().setUrl( "http://foo").setValue( new StringType("AAA"));

		String encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(c);
		ourLog.info(encoded);

		encoded = ourCtx.newJsonParser().setPrettyPrint(false).encodeResourceToString(c);
		ourLog.info(encoded);
		assertEquals(encoded, "{\"resourceType\":\"Conformance\",\"_acceptUnknown\":{\"extension\":[{\"url\":\"http://foo\",\"valueString\":\"AAA\"}]}}");

		// Now with a value
		ourLog.info("---------------");

		c = new Conformance();
		c.getAcceptUnknownElement().setValue(UnknownContentCode.EXTENSIONS);
		c.getAcceptUnknownElement().addExtension().setUrl("http://foo").setValue( new StringType("AAA"));

		encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(c);
		ourLog.info(encoded);

		encoded = ourCtx.newJsonParser().setPrettyPrint(false).encodeResourceToString(c);
		ourLog.info(encoded);
		assertEquals(encoded, "{\"resourceType\":\"Conformance\",\"acceptUnknown\":\"extensions\",\"_acceptUnknown\":{\"extension\":[{\"url\":\"http://foo\",\"valueString\":\"AAA\"}]}}");

	}

	@Test
	public void testEncodeExtensionInResourceElement() {

		Conformance c = new Conformance();
		// c.addRest().getSecurity().addUndeclaredExtension(false, "http://foo", new StringType("AAA"));
		c.addExtension().setUrl("http://foo").setValue( new StringType("AAA"));

		String encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(c);
		ourLog.info(encoded);

		encoded = ourCtx.newJsonParser().setPrettyPrint(false).encodeResourceToString(c);
		ourLog.info(encoded);
		assertEquals(encoded, "{\"resourceType\":\"Conformance\",\"extension\":[{\"url\":\"http://foo\",\"valueString\":\"AAA\"}]}");

	}

	@Test
	public void testEncodeExtensionOnEmptyElement() throws Exception {

		ValueSet valueSet = new ValueSet();
		valueSet.addUseContext().addExtension().setUrl("http://foo").setValue( new StringType("AAA"));

		String encoded = ourCtx.newJsonParser().encodeResourceToString(valueSet);
		assertThat(encoded, containsString("\"useContext\":[{\"extension\":[{\"url\":\"http://foo\",\"valueString\":\"AAA\"}]}"));

	}


	@Test
	public void testEncodeExtensionWithResourceContent() {
		IParser parser = ourCtx.newJsonParser();

		Patient patient = new Patient();
		patient.addAddress().setUse(AddressUse.HOME);
		patient.addExtension().setUrl("urn:foo").setValue( new Reference("Organization/123"));

		String val = parser.encodeResourceToString(patient);
		ourLog.info(val);
		assertThat(val, StringContains.containsString("\"extension\":[{\"url\":\"urn:foo\",\"valueReference\":{\"reference\":\"Organization/123\"}}]"));

		Patient actual = parser.parseResource(Patient.class, val);
		assertEquals(AddressUse.HOME, patient.getAddress().get(0).getUse());
		List<Extension> ext = actual.getExtension();
		assertEquals(1, ext.size());
		Reference ref = (Reference) ext.get(0).getValue();
		assertEquals("Organization/123", ref.getReference());

	}

	@Test
	public void testEncodeIds() {
		Patient pt = new Patient();
		pt.addIdentifier().setSystem("sys").setValue( "val");

		List_ list = new List_();
		list.setId("listId");
		list.addEntry().setItem(new Reference(pt)).setDeleted(true);

		String enc = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(list);
		ourLog.info(enc);

		assertThat(enc, containsString("\"id\": \"1\""));

		List_ parsed = ourCtx.newJsonParser().parseResource(List_.class,enc);
		assertEquals(Patient.class, parsed.getEntry().get(0).getItem().getResource().getClass());
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
		p.getText().setDivAsString("<div>AAA</div>");

		Bundle b = new Bundle();
		b.setTotal(123);
		b.addEntry().setResource(p);

		String str = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(b);
		ourLog.info(str);
		assertThat(str, StringContains.containsString("<div xmlns=\\\"http://www.w3.org/1999/xhtml\\\">AAA</div>"));

		p.getText().setDivAsString("<xhtml:div xmlns:xhtml=\"http://www.w3.org/1999/xhtml\">hello</xhtml:div>");
		str = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(b);
		ourLog.info(str);
		// Backslashes need to be escaped because they are in a JSON value
		assertThat(str, containsString(">hello<"));

	}

	@Test
	public void testEncodeNonContained() {
		Organization org = new Organization();
		org.setId("Organization/65546");
		org.getNameElement().setValue("Contained Test Organization");

		Patient patient = new Patient();
		patient.setId("Patient/1333");
		patient.addIdentifier().setSystem("urn:mrns").setValue("253345");
		patient.getManagingOrganization().setResource(org);

		Bundle b = new Bundle();
		b.addEntry().setResource(patient);

		String encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(b);
		ourLog.info(encoded);
		assertThat(encoded, not(containsString("contained")));
		assertThat(encoded, containsString("\"reference\": \"Organization/65546\""));

		encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(patient);
		ourLog.info(encoded);
		assertThat(encoded, not(containsString("contained")));
		assertThat(encoded, containsString("\"reference\": \"Organization/65546\""));
	}




	@Test
	public void testEncodeResourceRef() throws DataFormatException {

		Patient patient = new Patient();
		patient.setManagingOrganization(new Reference());

		IParser p = ourCtx.newJsonParser();
		String str = p.encodeResourceToString(patient);
		assertThat(str, IsNot.not(StringContains.containsString("managingOrganization")));

		patient.setManagingOrganization(new Reference("Organization/123"));
		str = p.encodeResourceToString(patient);
		assertThat(str, StringContains.containsString("\"managingOrganization\":{\"reference\":\"Organization/123\"}"));

		Organization org = new Organization();
		org.addIdentifier().setSystem("foo").setValue("bar");
		patient.setManagingOrganization(new Reference(org));
		str = p.encodeResourceToString(patient);
		assertThat(str, StringContains.containsString("\"contained\":[{\"resourceType\":\"Organization\""));

	}

	@Test
	public void testEncodeSummary() throws Exception {
		Patient patient = new Patient();
		patient.setId("Patient/1/_history/1");
		patient.getText().setDivAsString("<div>THE DIV</div>");
		patient.addName().addFamily("FAMILY");
		patient.setMaritalStatus(new CodeableConcept().setText("D"));

		String encoded = ourCtx.newJsonParser().setPrettyPrint(true).setSummaryMode(true).encodeResourceToString(patient);
		ourLog.info(encoded);

		assertThat(encoded, containsString("Patient"));
		assertThat(encoded, stringContainsInOrder("\"tag\"",
				"\"system\": \"" + Constants.TAG_SUBSETTED_SYSTEM_DSTU3 + "\",", "\"code\": \"" + Constants.TAG_SUBSETTED_CODE+"\","));
		assertThat(encoded, not(containsString("THE DIV")));
		assertThat(encoded, containsString("family"));
		assertThat(encoded, not(containsString("maritalStatus")));
	}

	@Test
	public void testEncodeSummary2() throws Exception {
		Patient patient = new Patient();
		patient.setId("Patient/1/_history/1");
		patient.getText().setDivAsString("<div>THE DIV</div>");
		patient.addName().addFamily("FAMILY");
		patient.setMaritalStatus(new CodeableConcept().setText("D"));

		patient.getMeta().addTag().setSystem("foo").setCode("bar");

		String encoded = ourCtx.newJsonParser().setPrettyPrint(true).setSummaryMode(true).encodeResourceToString(patient);
		ourLog.info(encoded);

		assertThat(encoded, containsString("Patient"));
		assertThat(encoded, stringContainsInOrder("\"tag\"",
				"\"system\": \"foo\",", "\"code\": \"bar\"",
				"\"system\": \"" + Constants.TAG_SUBSETTED_SYSTEM_DSTU3 + "\",", "\"code\": \"" + Constants.TAG_SUBSETTED_CODE+"\","));
		assertThat(encoded, not(containsString("THE DIV")));
		assertThat(encoded, containsString("family"));
		assertThat(encoded, not(containsString("maritalStatus")));
	}

	@Test
	public void testEncodeUndeclaredExtensionWithAddressContent() {
		IParser parser = ourCtx.newJsonParser();

		Patient patient = new Patient();
		patient.addAddress().setUse(AddressUse.HOME);
		patient.addExtension().setUrl("urn:foo").setValue(new Address().addLine("line1"));

		String val = parser.encodeResourceToString(patient);
		ourLog.info(val);
		assertThat(val, StringContains.containsString("\"extension\":[{\"url\":\"urn:foo\",\"valueAddress\":{\"line\":[\"line1\"]}}]"));

		MyPatientWithOneDeclaredAddressExtension actual = parser.parseResource(MyPatientWithOneDeclaredAddressExtension.class, val);
		assertEquals(AddressUse.HOME, patient.getAddress().get(0).getUse());
		Address ref = actual.getFoo();
		assertEquals("line1", ref.getLine().get(0).getValue());

	}

	@Test
	public void testEncodingNullExtension() {
		Patient p = new Patient();
		Extension extension = new Extension().setUrl("http://foo#bar");
		p.getExtension().add(extension);
		String str = ourCtx.newJsonParser().encodeResourceToString(p);

		assertEquals("{\"resourceType\":\"Patient\"}", str);

		extension.setValue(new StringType());

		str = ourCtx.newJsonParser().encodeResourceToString(p);
		assertEquals("{\"resourceType\":\"Patient\"}", str);

		extension.setValue(new StringType(""));

		str = ourCtx.newJsonParser().encodeResourceToString(p);
		assertEquals("{\"resourceType\":\"Patient\"}", str);

	}

	@Test
	public void testExtensionOnComposite() throws Exception {

		Patient patient = new Patient();

		HumanName name = patient.addName();
		name.addFamily("Shmoe");
		HumanName given = name.addGiven("Joe");
		Extension ext2 = new Extension().setUrl("http://examples.com#givenext").setValue( new StringType("Hello"));
		given.getExtension().add(ext2);
		String enc = ourCtx.newJsonParser().encodeResourceToString(patient);
		ourLog.info(enc);
		assertEquals("{\"resourceType\":\"Patient\",\"name\":[{\"extension\":[{\"url\":\"http://examples.com#givenext\",\"valueString\":\"Hello\"}],\"family\":[\"Shmoe\"],\"given\":[\"Joe\"]}]}", enc);

		IParser newJsonParser = ourCtx.newJsonParser();
		StringReader reader = new StringReader(enc);
		Patient parsed = newJsonParser.parseResource(Patient.class, reader);

		ourLog.info(ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(parsed));

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

		family.addExtension().setUrl("http://examples.com#givenext").setValue( new StringType("Hello"));
		String enc = ourCtx.newJsonParser().encodeResourceToString(patient);
		ourLog.info(enc);
		//@formatter:off
		assertThat(enc, containsString(("{\n" +
				"    \"resourceType\":\"Patient\",\n" +
				"    \"name\":[\n" +
				"        {\n" +
				"            \"family\":[\n" +
				"                \"Shmoe\"\n" +
				"            ],\n" +
				"            \"_family\":[\n" +
				"                {\n" +
				"                    \"extension\":[\n" +
				"                        {\n" +
				"                            \"url\":\"http://examples.com#givenext\",\n" +
				"                            \"valueString\":\"Hello\"\n" +
				"                        }\n" +
				"                    ]\n" +
				"                }\n" +
				"            ]\n" +
				"        }\n" +
				"    ]\n" +
				"}").replace("\n", "").replaceAll(" +", "")));
		//@formatter:on

    Patient parsed = ourCtx.newJsonParser().parseResource(Patient.class, new StringReader(enc));
    assertEquals(1, parsed.getName().get(0).getFamily().get(0).getExtension().size());
    Extension ext = parsed.getName().get(0).getFamily().get(0).getExtension().get(0);
    assertEquals("Hello", ((IPrimitiveType<?>) ext.getValue()).getValue());

  }

  @Test
  public void testMoreExtensions() throws Exception {

    Patient patient = new Patient();
    patient.addIdentifier().setUse(IdentifierUse.OFFICIAL).setSystem("urn:example").setValue("7000135");

    Extension ext = new Extension();
    ext.setUrl("http://example.com/extensions#someext");
    ext.setValue(new DateTimeType("2011-01-02T11:13:15"));

    // Add the extension to the resource
    patient.getExtension().add(ext);
    // END SNIPPET: resourceExtension

    // START SNIPPET: resourceStringExtension
    HumanName name = patient.addName();
    name.addFamily("Shmoe");
    StringType given = name.addGivenElement();
    given.setValue("Joe");
    Extension ext2 = new Extension().setUrl("http://examples.com#givenext").setValue(new StringType("given"));
    given.getExtension().add(ext2);

    StringType given2 = name.addGivenElement();
    given2.setValue("Shmoe");
    Extension given2ext = new Extension().setUrl("http://examples.com#givenext_parent");
    given2.getExtension().add(given2ext);
    given2ext.addExtension().setUrl("http://examples.com#givenext_child").setValue(new StringType("CHILD"));
    // END SNIPPET: resourceStringExtension

    // START SNIPPET: subExtension
    Extension parent = new Extension().setUrl("http://example.com#parent");
    patient.getExtension().add(parent);

    Extension child1 = new Extension().setUrl("http://example.com#child").setValue(new StringType("value1"));
    parent.getExtension().add(child1);

    Extension child2 = new Extension().setUrl("http://example.com#child").setValue(new StringType("value1"));
    parent.getExtension().add(child2);
    // END SNIPPET: subExtension

    String output = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(patient);
    ourLog.info(output);

    String enc = ourCtx.newJsonParser().encodeResourceToString(patient);
    //@formatter:off
		assertThat(enc, containsString(("{" +
				"\"resourceType\":\"Patient\"," +
				"    \"extension\":[" +
				"        {" +
				"            \"url\":\"http://example.com/extensions#someext\"," +
				"            \"valueDateTime\":\"2011-01-02T11:13:15\"" +
				"        }," +
				"        {" +
				"            \"url\":\"http://example.com#parent\"," +
				"            \"extension\":[" +
				"                {" +
				"                    \"url\":\"http://example.com#child\"," +
				"                    \"valueString\":\"value1\"" +
				"                }," +
				"                {" +
				"                    \"url\":\"http://example.com#child\"," +
				"                    \"valueString\":\"value1\"" +
				"                }" +
				"            ]" +
				"        }" +
				"    ]").replace(" ", "")));
		//@formatter:on

    //@formatter:off
		assertThat(enc, containsString((
				"            \"given\":[" +
				"                \"Joe\"," +
				"                \"Shmoe\"" +
				"            ]," +
				"            \"_given\":[" +
				"                {" +
				"                    \"extension\":[" +
				"                        {" +
				"                            \"url\":\"http://examples.com#givenext\"," +
				"                            \"valueString\":\"given\"" +
				"                        }" +
				"                    ]" +
				"                }," +
				"                {" +
				"                    \"extension\":[" +
				"                        {" +
				"                            \"url\":\"http://examples.com#givenext_parent\"," +
				"                            \"extension\":[" +
				"                                {" +
				"                                    \"url\":\"http://examples.com#givenext_child\"," +
				"                                    \"valueString\":\"CHILD\"" +
				"                                }" +
				"                            ]" +
				"                        }" +
				"                    ]" +
				"                }" +
				"").replace(" ", "")));
		//@formatter:on
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

    String str = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(A);
    ourLog.info(str);

    assertThat(str, stringContainsInOrder(Arrays.asList("\"text\": \"B\"", "\"text\": \"C\"", "\"text\": \"A\"")));

    // Only one (outer) contained block
    int idx0 = str.indexOf("\"contained\"");
    int idx1 = str.indexOf("\"contained\"", idx0 + 1);

    assertNotEquals(-1, idx0);
    assertEquals(-1, idx1);

    Observation obs = ourCtx.newJsonParser().parseResource(Observation.class, str);
    assertEquals("A", obs.getCode().getTextElement().getValue());

    Observation obsB = (Observation) obs.getRelated().get(0).getTarget().getResource();
    assertEquals("B", obsB.getCode().getTextElement().getValue());

    Observation obsC = (Observation) obsB.getRelated().get(0).getTarget().getResource();
    assertEquals("C", obsC.getCode().getTextElement().getValue());

  }

  @Test
  public void testParseBinaryResource() {

    Binary val = ourCtx.newJsonParser().parseResource(Binary.class, "{\"resourceType\":\"Binary\",\"contentType\":\"foo\",\"content\":\"AQIDBA==\"}");
    assertEquals("foo", val.getContentType());
    assertArrayEquals(new byte[] { 1, 2, 3, 4 }, val.getContent());

  }

  @Test
  public void testParseEmptyNarrative() throws Exception {
    //@formatter:off
		String text = "{\n" +
				"    \"resourceType\" : \"Patient\",\n" +
				"    \"extension\" : [\n" +
				"      {\n" +
				"        \"url\" : \"http://clairol.org/colour\",\n" +
				"        \"valueCode\" : \"B\"\n" +
				"      }\n" +
				"    ],\n" +
				"    \"text\" : {\n" +
				"      \"div\" : \"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\"?>\"\n" +
				"    }" +
				"}";
		//@formatter:on

    Patient res = (Patient) ourCtx.newJsonParser().parseResource(text);
    XhtmlNode div = res.getText().getDiv();
    String value = div.getValueAsString();

    assertNull(value);
    List<XhtmlNode> childNodes = div.getChildNodes();
    assertTrue(childNodes == null || childNodes.isEmpty());
  }

  @Test
  public void testParseSimpleBundle() {
    String bundle = "{\"resourceType\":\"Bundle\",\"entry\":[{\"resource\":{\"resourceType\":\"Patient\",\"identifier\":[{\"system\":\"idsystem\"}]}}]}";
    Bundle b = ourCtx.newJsonParser().parseResource(Bundle.class, bundle);

    assertNotNull(b.getEntry().get(0).getResource());
    Patient p = (Patient) b.getEntry().get(0).getResource();
    assertEquals("idsystem", p.getIdentifier().get(0).getSystem());
  }

  @Test
  public void testParseSingleQuotes() {
    ourCtx.newJsonParser().parseResource(Bundle.class, "{ \"resourceType\": \"Bundle\" }");
    ourCtx.newJsonParser().parseResource(Bundle.class, "{ 'resourceType': 'Bundle' }");
  }

  @Test
  public void testSimpleParse() throws DataFormatException, IOException {

    String msg = IOUtils.toString(JsonParserHl7OrgDstu2Test.class.getResourceAsStream("/example-patient-general-hl7orgdstu2.json"));
    IParser p = ourCtx.newJsonParser();
    // ourLog.info("Reading in message: {}", msg);
    Patient res = p.parseResource(Patient.class, msg);

    assertEquals(2, res.getExtension().size());
    assertEquals(1, res.getModifierExtension().size());

    String encoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(res);
    ourLog.info(encoded);

  }

  @Test
  public void testParsePrimitiveExtension() {
    //@formatter:off
	  String input = "{\n" +
	      "    \"resourceType\":\"Patient\",\n" +
	      "    \"contact\":[\n" +
	      "        {\n" +
	      "            \"name\":{\n" +
	      "                \"family\":[\n" +
	      "                    \"du\",\n" +
	      "                    \"Marché\"\n" +
	      "                ],\n" +
	      "                \"_family\":[\n" +
	      "                    {\n" +
	      "                        \"extension\":[\n" +
	      "                            {\n" +
	      "                                \"url\":\"http://hl7.org/fhir/Profile/iso-21090#qualifier\",\n" +
	      "                                \"valueCode\":\"VV\"\n" +
	      "                            }\n" +
	      "                        ]\n" +
	      "                    },\n" +
	      "                    null\n" +
	      "                ]\n" +
	      "            }\n" +
	      "        }\n" +
	      "    ]\n" +
	      "}";
    //@formatter:off

	  Patient p = ourCtx.newJsonParser().parseResource(Patient.class, input);
	  ContactComponent contact = p.getContact().get(0);
	  StringType family = contact.getName().getFamily().get(0);

	  assertEquals("du", family.getValueAsString());
	  assertEquals(1, family.getExtension().size());
	}


	@Test
	public void testSimpleResourceEncodeWithCustomType() throws IOException, SAXException {

		String jsonString = IOUtils.toString(JsonParserHl7OrgDstu2Test.class.getResourceAsStream("/example-patient-general-hl7orgdstu2.json"), Charset.forName("UTF-8"));
		MyObservationWithExtensions obs = ourCtx.newJsonParser().parseResource(MyObservationWithExtensions.class, jsonString);

		{
    ContactComponent contact = obs.getContact().get(0);
    StringType family = contact.getName().getFamily().get(0);
    assertEquals("du", family.getValueAsString());
    assertEquals(1, family.getExtension().size());
		}

		assertEquals(0, obs.getExtension().size());
		assertEquals("aaaa", obs.getExtAtt().getContentType());
		assertEquals("str1", obs.getMoreExt().getStr1().getValue());
		assertEquals("2011-01-02", obs.getModExt().getValueAsString());

		List<Extension> undeclaredExtensions = obs.getContact().get(0).getName().getFamily().get(0).getExtension();
		Extension undeclaredExtension = undeclaredExtensions.get(0);
		assertEquals("http://hl7.org/fhir/Profile/iso-21090#qualifier", undeclaredExtension.getUrl());

		IParser xmlParser = ourCtx.newXmlParser();
		String encoded = xmlParser.encodeResourceToString(obs);
		encoded = encoded.replaceAll("<!--.*-->", "").replace("\n", "").replace("\r", "").replaceAll(">\\s+<", "><");

		String xmlString = IOUtils.toString(JsonParserHl7OrgDstu2Test.class.getResourceAsStream("/example-patient-general-hl7orgdstu2.xml"), Charset.forName("UTF-8"));
		xmlString = xmlString.replaceAll("<!--.*-->", "").replace("\n", "").replace("\r", "").replaceAll(">\\s+<", "><");

		ourLog.info("Expected: " + xmlString);
		ourLog.info("Actual  : " + encoded);

		String expected = (xmlString);
		String actual = (encoded.trim());

		XmlParserHl7OrgDstu2Test.compareXml(expected, actual);
	}


	@Test
	public void testBaseUrlFooResourceCorrectlySerializedInExtensionValueReference() {
		String refVal = "http://my.org/FooBar";

		Patient fhirPat = new Patient();
		fhirPat.addExtension().setUrl("x1").setValue(new Reference(refVal));

		IParser parser = ourCtx.newJsonParser();

		String output = parser.encodeResourceToString(fhirPat);
		System.out.println("output: " + output);

		// Deserialize then check that valueReference value is still correct
		fhirPat = parser.parseResource(Patient.class, output);

		List<Extension> extlst = fhirPat.getExtension();
		assertEquals(1, extlst.size());
		assertEquals(refVal, ((Reference) extlst.get(0).getValue()).getReference());
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

	private Matcher<? super String> stringContainsInOrder(java.lang.String... substrings) {
		return Matchers.stringContainsInOrder(Arrays.asList(substrings));
	}

	private Matcher<? super String> stringContainsInOrder(List<String> substrings) {
		return Matchers.stringContainsInOrder(substrings);
	}

}
