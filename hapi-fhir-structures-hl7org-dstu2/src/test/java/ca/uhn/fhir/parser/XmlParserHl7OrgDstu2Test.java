package ca.uhn.fhir.parser;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.api.AddProfileTagEnum;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.parser.JsonParserHl7OrgDstu2Test.MyPatientWithOneDeclaredAddressExtension;
import ca.uhn.fhir.parser.JsonParserHl7OrgDstu2Test.MyPatientWithOneDeclaredExtension;
import ca.uhn.fhir.rest.api.Constants;
import net.sf.json.JSON;
import net.sf.json.JSONSerializer;
import org.apache.commons.io.IOUtils;
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
import org.hl7.fhir.dstu2.model.Composition;
import org.hl7.fhir.dstu2.model.Condition;
import org.hl7.fhir.dstu2.model.DateTimeType;
import org.hl7.fhir.dstu2.model.DateType;
import org.hl7.fhir.dstu2.model.DecimalType;
import org.hl7.fhir.dstu2.model.DiagnosticReport;
import org.hl7.fhir.dstu2.model.DocumentManifest;
import org.hl7.fhir.dstu2.model.EnumFactory;
import org.hl7.fhir.dstu2.model.Enumeration;
import org.hl7.fhir.dstu2.model.Enumerations.AdministrativeGender;
import org.hl7.fhir.dstu2.model.Extension;
import org.hl7.fhir.dstu2.model.HumanName;
import org.hl7.fhir.dstu2.model.Identifier;
import org.hl7.fhir.dstu2.model.Identifier.IdentifierUse;
import org.hl7.fhir.dstu2.model.InstantType;
import org.hl7.fhir.dstu2.model.MedicationStatement;
import org.hl7.fhir.dstu2.model.Narrative.NarrativeStatus;
import org.hl7.fhir.dstu2.model.Observation;
import org.hl7.fhir.dstu2.model.Organization;
import org.hl7.fhir.dstu2.model.Patient;
import org.hl7.fhir.dstu2.model.PrimitiveType;
import org.hl7.fhir.dstu2.model.Reference;
import org.hl7.fhir.dstu2.model.Resource;
import org.hl7.fhir.dstu2.model.SimpleQuantity;
import org.hl7.fhir.dstu2.model.Specimen;
import org.hl7.fhir.dstu2.model.StringType;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.builder.Input;
import org.xmlunit.diff.ComparisonControllers;
import org.xmlunit.diff.DefaultNodeMatcher;
import org.xmlunit.diff.Diff;
import org.xmlunit.diff.ElementSelectors;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.stringContainsInOrder;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class XmlParserHl7OrgDstu2Test {

  private static FhirContext ourCtx;
  private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(XmlParserHl7OrgDstu2Test.class);

  @AfterEach
  public void after() {
    ourCtx.setAddProfileTagWhenEncoding(AddProfileTagEnum.ONLY_FOR_CUSTOM);
  }

  private String fixDivNodeText(String htmlNoNs) {
    return htmlNoNs.replace("<div>", "<div xmlns=\"http://www.w3.org/1999/xhtml\">");
  }

  private String fixDivNodeTextJson(String htmlNoNs) {
    return htmlNoNs.replace("<div>", "<div xmlns=\\\"http://www.w3.org/1999/xhtml\\\">");
  }
  
	@Test
	public void testOverrideResourceIdWithBundleEntryFullUrlEnabled() {
		String tmp = "<Bundle xmlns=\"http://hl7.org/fhir\"><entry><fullUrl value=\"http://lalaland.org/patient/pat1\"/><resource><Patient xmlns=\"http://hl7.org/fhir\"><id value=\"patxuzos\"/></Patient></resource></entry></Bundle>";
		Bundle bundle = (Bundle) ourCtx.newXmlParser().parseResource(tmp);
		assertEquals(1, bundle.getEntry().size());
		{
			Patient o1 = (Patient) bundle.getEntry().get(0).getResource();
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
		Bundle bundle = (Bundle) ourCtx.newXmlParser().parseResource(tmp);
		assertEquals(1, bundle.getEntry().size());
		{
			Patient o1 = (Patient) bundle.getEntry().get(0).getResource();
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
		Bundle bundle = (Bundle) ourCtx.newXmlParser().setOverrideResourceIdWithBundleEntryFullUrl(false).parseResource(tmp);
		assertEquals(1, bundle.getEntry().size());
		{
			Patient o1 = (Patient) bundle.getEntry().get(0).getResource();
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
    Patient p = new Patient();
    p.addName().addFamily("PATIENT");

    Organization o = new Organization();
    o.setName("ORG");
    p.addExtension().setUrl("urn:foo").setValue(new Reference(o));

    String str = ourCtx.newXmlParser().encodeResourceToString(p);
    ourLog.info(str);

    p = ourCtx.newXmlParser().parseResource(Patient.class, str);
    assertEquals("PATIENT", p.getName().get(0).getFamily().get(0).getValue());

    List<Extension> exts = p.getExtension();
    assertEquals(1, exts.size());
    Reference rr = (Reference) exts.get(0).getValue();
    o = (Organization) rr.getResource();
    assertEquals("ORG", o.getName());
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

    // Encode the bundle
    encoded = xmlParser.encodeResourceToString(b);
    ourLog.info(encoded);
    assertThat(encoded, stringContainsInOrder(Arrays.asList("<contained>", "<id value=\"1\"/>", "</contained>")));
    assertThat(encoded, containsString("<reference value=\"#1\"/>"));
    assertThat(encoded, stringContainsInOrder(Arrays.asList("<entry>", "</entry>")));
    assertThat(encoded, not(stringContainsInOrder(Arrays.asList("<entry>", "</entry>", "<entry>"))));

    // Re-parse the bundle
    patient = (Patient) xmlParser.parseResource(xmlParser.encodeResourceToString(patient));
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

    Patient parsed = ourCtx.newXmlParser().parseResource(Patient.class, enc);
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

  /**
   * See #216 - Profiled datatypes should use their unprofiled parent type as
   * the choice[x] name
   * <p>
   * Disabled after conversation with Grahame
   */
  @Test
  @Disabled
  public void testEncodeAndParseProfiledDatatypeChoice() throws Exception {
    IParser xmlParser = ourCtx.newXmlParser();

    String input = IOUtils.toString(XmlParser.class.getResourceAsStream("/medicationstatement_invalidelement.xml"));
    MedicationStatement ms = xmlParser.parseResource(MedicationStatement.class, input);
    SimpleQuantity q = (SimpleQuantity) ms.getDosage().get(0).getQuantity();
    assertEquals("1", q.getValueElement().getValueAsString());

    String output = xmlParser.encodeResourceToString(ms);
    assertThat(output, containsString("<quantityQuantity><value value=\"1\"/></quantityQuantity>"));
  }

  @Test
  public void testEncodeBinaryResource() {

    Binary patient = new Binary();
    patient.setContentType("foo");
    patient.setContent(new byte[] { 1, 2, 3, 4 });

    String val = ourCtx.newXmlParser().encodeResourceToString(patient);
    assertEquals(
        "<Binary xmlns=\"http://hl7.org/fhir\"><contentType value=\"foo\"/><content value=\"AQIDBA==\"/></Binary>",
        val);

  }

  // TODO: uncomment with new model updates
  // @Test
  // public void testEncodeAndParseExtensionOnResourceReference() {
  // DataElement de = new DataElement();
  // Binding b = de.addElement().getBinding();
  // b.setName("BINDING");
  //
  // Organization o = new Organization();
  // o.setName("ORG");
  // b.addUndeclaredExtension(new ExtensionDt(false, "urn:foo", new
  // ResourceReferenceDt(o)));
  //
  // String str = ourCtx.newXmlParser().encodeResourceToString(de);
  // ourLog.info(str);
  //
  // de = ourCtx.newXmlParser().parseResource(DataElement.class, str);
  // b = de.getElement().get(0).getBinding();
  // assertEquals("BINDING", b.getName());
  //
  // List<ExtensionDt> exts = b.getUndeclaredExtensionsByUrl("urn:foo");
  // assertEquals(1, exts.size());
  // ResourceReferenceDt rr = (ResourceReferenceDt)exts.get(0).getValue();
  // o = (Organization) rr.getResource();
  // assertEquals("ORG", o.getName());
  //
  // }
  //
  // @Test
  // public void testParseAndEncodeExtensionOnResourceReference() {
  // //@formatter:off
  // String input = "<DataElement>" +
  // "<id value=\"gender\"/>"+
  // "<contained>"+
  // "<ValueSet>"+
  // "<id value=\"2179414\"/>"+
  // "<url value=\"2179414\"/>"+
  // "<version value=\"1.0\"/>"+
  // "<name value=\"Gender Code\"/>"+
  // "<description value=\"All codes representing the gender of a person.\"/>"+
  // "<status value=\"active\"/>"+
  // "<compose>"+
  // "<include>"+
  // "<system value=\"http://ncit.nci.nih.gov\"/>"+
  // "<concept>"+
  // "<code value=\"C17998\"/>"+
  // "<display value=\"Unknown\"/>"+
  // "</concept>"+
  // "<concept>"+
  // "<code value=\"C20197\"/>"+
  // "<display value=\"Male\"/>"+
  // "</concept>"+
  // "<concept>"+
  // "<code value=\"C16576\"/>"+
  // "<display value=\"Female\"/>"+
  // "</concept>"+
  // "<concept>"+
  // "<code value=\"C38046\"/>"+
  // "<display value=\"Not specified\"/>"+
  // "</concept>"+
  // "</include>"+
  // "</compose>"+
  // "</ValueSet>"+
  // "</contained>"+
  // "<contained>"+
  // "<ValueSet>"+
  // "<id value=\"2179414-permitted\"/>"+
  // "<status value=\"active\"/>"+
  // "<define>"+
  // "<system value=\"http://example.org/fhir/2179414\"/>"+
  // "<caseSensitive value=\"true\"/>"+
  // "<concept>"+
  // "<code value=\"0\"/>"+
  // "</concept>"+
  // "<concept>"+
  // "<code value=\"1\"/>"+
  // "</concept>"+
  // "<concept>"+
  // "<code value=\"2\"/>"+
  // "</concept>"+
  // "<concept>"+
  // "<code value=\"3\"/>"+
  // "</concept>"+
  // "</define>"+
  // "</ValueSet>"+
  // "</contained>"+
  // "<contained>"+
  // "<ConceptMap>"+
  // "<id value=\"2179414-cm\"/>"+
  // "<status value=\"active\"/>"+
  // "<sourceReference>"+
  // "<reference value=\"#2179414\"/>"+
  // "</sourceReference>"+
  // "<targetReference>"+
  // "<reference value=\"#2179414-permitted\"/>"+
  // "</targetReference>"+
  // "<element>"+
  // "<code value=\"C17998\"/>"+
  // "<map>"+
  // "<code value=\"0\"/>"+
  // "<equivalence value=\"equal\"/>"+
  // "</map>"+
  // "</element>"+
  // "<element>"+
  // "<code value=\"C20197\"/>"+
  // "<map>"+
  // "<code value=\"1\"/>"+
  // "<equivalence value=\"equal\"/>"+
  // "</map>"+
  // "</element>"+
  // "<element>"+
  // "<code value=\"C16576\"/>"+
  // "<map>"+
  // "<code value=\"2\"/>"+
  // "<equivalence value=\"equal\"/>"+
  // "</map>"+
  // "</element>"+
  // "<element>"+
  // "<code value=\"C38046\"/>"+
  // "<map>"+
  // "<code value=\"3\"/>"+
  // "<equivalence value=\"equal\"/>"+
  // "</map>"+
  // "</element>"+
  // "</ConceptMap>"+
  // "</contained>"+
  // "<identifier>"+
  // "<value value=\"2179650\"/>"+
  // "</identifier>"+
  // "<version value=\"1.0\"/>"+
  // "<name value=\"Gender Code\"/>"+
  // "<useContext>"+
  // "<coding>"+
  // "<system value=\"http://example.org/FBPP\"/>"+
  // "<display value=\"FBPP Pooled Database\"/>"+
  // "</coding>"+
  // "<coding>"+
  // "<system value=\"http://example.org/PhenX\"/>"+
  // "<display value=\"Demographics\"/>"+
  // "</coding>"+
  // "<coding>"+
  // "<system value=\"http://example.org/EligibilityCriteria\"/>"+
  // "<display value=\"Pt. Administrative\"/>"+
  // "</coding>"+
  // "<coding>"+
  // "<system value=\"http://example.org/UAMSClinicalResearch\"/>"+
  // "<display value=\"UAMS New CDEs\"/>"+
  // "</coding>"+
  // "<coding>"+
  // "<system value=\"http://example.org/PhenX\"/>"+
  // "<display value=\"Substance Abuse and \"/>"+
  // "</coding>"+
  // "<coding>"+
  // "<system value=\"http://example.org/Category\"/>"+
  // "<display value=\"CSAERS Adverse Event\"/>"+
  // "</coding>"+
  // "<coding>"+
  // "<system value=\"http://example.org/PhenX\"/>"+
  // "<display value=\"Core: Tier 1\"/>"+
  // "</coding>"+
  // "<coding>"+
  // "<system value=\"http://example.org/Category\"/>"+
  // "<display value=\"Case Report Forms\"/>"+
  // "</coding>"+
  // "<coding>"+
  // "<system value=\"http://example.org/Category\"/>"+
  // "<display value=\"CSAERS Review Set\"/>"+
  // "</coding>"+
  // "<coding>"+
  // "<system value=\"http://example.org/Demonstration%20Applications\"/>"+
  // "<display value=\"CIAF\"/>"+
  // "</coding>"+
  // "<coding>"+
  // "<system value=\"http://example.org/NIDA%20CTN%20Usage\"/>"+
  // "<display value=\"Clinical Research\"/>"+
  // "</coding>"+
  // "<coding>"+
  // "<system value=\"http://example.org/NIDA%20CTN%20Usage\"/>"+
  // "<display value=\"Electronic Health Re\"/>"+
  // "</coding>"+
  // "<coding>"+
  // "<system value=\"http://example.org/Condition\"/>"+
  // "<display value=\"Barretts Esophagus\"/>"+
  // "</coding>"+
  // "<coding>"+
  // "<system value=\"http://example.org/Condition\"/>"+
  // "<display value=\"Bladder Cancer\"/>"+
  // "</coding>"+
  // "<coding>"+
  // "<system value=\"http://example.org/Condition\"/>"+
  // "<display value=\"Oral Leukoplakia\"/>"+
  // "</coding>"+
  // "<coding>"+
  // "<system value=\"http://example.org/Condition\"/>"+
  // "<display value=\"Sulindac for Breast\"/>"+
  // "</coding>"+
  // "</useContext>"+
  // "<status value=\"active\"/>"+
  // "<publisher value=\"DCP\"/>"+
  // "<element>"+
  // "<extension url=\"http://hl7.org/fhir/StructureDefinition/minLength\">"+
  // "<valueInteger value=\"1\"/>"+
  // "</extension>"+
  // "<extension
  // url=\"http://hl7.org/fhir/StructureDefinition/elementdefinition-question\">"+
  // "<valueString value=\"Gender\"/>"+
  // "</extension>"+
  // "<path value=\"Gender\"/>"+
  // "<definition value=\"The code representing the gender of a person.\"/>"+
  // "<type>"+
  // "<code value=\"CodeableConcept\"/>"+
  // "</type>"+
  // "<maxLength value=\"13\"/>"+
  // "<binding>"+
  // "<name value=\"Gender\"/>"+
  // "<strength value=\"required\"/>"+
  // "<valueSetReference>"+
  // "<extension
  // url=\"http://hl7.org/fhir/StructureDefinition/11179-permitted-value-valueset\">"+
  // "<valueReference>"+
  // "<reference value=\"#2179414-permitted\"/>"+
  // "</valueReference>"+
  // "</extension>"+
  // "<extension
  // url=\"http://hl7.org/fhir/StructureDefinition/11179-permitted-value-conceptmap\">"+
  // "<valueReference>"+
  // "<reference value=\"#2179414-cm\"/>"+
  // "</valueReference>"+
  // "</extension>"+
  // "<reference value=\"#2179414\"/>"+
  // "</valueSetReference>"+
  // "</binding>"+
  // "</element>"+
  // "</DataElement>";
  // //@formatter:on
  // DataElement de = ourCtx.newXmlParser().parseResource(DataElement.class,
  // input);
  // String output = ourCtx.newXmlParser().encodeResourceToString(de).replace("
  // xmlns=\"http://hl7.org/fhir\"", "");
  //
  // ElementDefinitionDt elem = de.getElement().get(0);
  // Binding b = elem.getBinding();
  // assertEquals("Gender", b.getName());
  //
  // ResourceReferenceDt ref = (ResourceReferenceDt) b.getValueSet();
  // assertEquals("#2179414", ref.getReference().getValue());
  //
  // assertEquals(2, ref.getUndeclaredExtensions().size());
  // ExtensionDt ext = ref.getUndeclaredExtensions().get(0);
  // assertEquals("http://hl7.org/fhir/StructureDefinition/11179-permitted-value-valueset",
  // ext.getUrl());
  // assertEquals(ResourceReferenceDt.class, ext.getValue().getClass());
  // assertEquals("#2179414-permitted",
  // ((ResourceReferenceDt)ext.getValue()).getReference().getValue());
  //
  // ourLog.info(ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(de));
  //
  // assertThat(output,
  // containsString("http://hl7.org/fhir/StructureDefinition/11179-permitted-value-valueset"));
  //
  // ourLog.info("Expected: {}", input);
  // ourLog.info("Actual : {}", output);
  // assertEquals(input, output);
  // }

  @Test
  public void testEncodeBinaryWithNoContentType() {
    Binary b = new Binary();
    b.setContent(new byte[] { 1, 2, 3, 4 });

    String output = ourCtx.newXmlParser().encodeResourceToString(b);
    ourLog.info(output);

    assertEquals("<Binary xmlns=\"http://hl7.org/fhir\"><content value=\"AQIDBA==\"/></Binary>", output);
  }

  @Test
  public void testEncodeBoundCode() {

    Patient patient = new Patient();
    patient.addAddress().setUse(AddressUse.HOME);

    patient.getGenderElement().setValue(AdministrativeGender.MALE);

    String val = ourCtx.newXmlParser().encodeResourceToString(patient);
    ourLog.info(val);

    assertThat(val, containsString("home"));
    assertThat(val, containsString("male"));
  }

  @Test
  public void testEncodeBundle() throws InterruptedException {
    Bundle b = new Bundle();
    b.getMeta().addTag().setSystem("http://hl7.org/fhir/tag").setCode("http://hl7.org/fhir/tag/message")
        .setDisplay("Message");

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

    // @formatter:on
    String[] strings = { "<Bundle xmlns=\"http://hl7.org/fhir\">",
        "<lastUpdated value=\"" + pub.getValueAsString() + "\"/>", "<Patient xmlns=\"http://hl7.org/fhir\">",
        "<id value=\"1\"/>", "<Patient xmlns=\"http://hl7.org/fhir\">", "<id value=\"2\"/>" };
    // @formatter:off

    assertThat(bundleString, StringContainsInOrder.stringContainsInOrder(strings));
  }

  @Test
  public void testEncodeBundleCategory() {

    Bundle b = new Bundle();
    BundleEntryComponent e = b.addEntry();
    e.setResource(new Patient());
    e.getResource().getMeta().addTag().setSystem("scheme").setCode("term").setDisplay("label");

    String val = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(b);
    ourLog.info(val);

    // @formatter:off
    assertThat(val, stringContainsInOrder("<tag>", "<system value=\"scheme\"/>", "<code value=\"term\"/>",
        "<display value=\"label\"/>", "</tag>"));
    // @formatter:on

    b = ourCtx.newXmlParser().parseResource(Bundle.class, val);
    assertEquals(1, b.getEntry().size());
    assertEquals(1, b.getEntry().get(0).getResource().getMeta().getTag().size());
    assertEquals("scheme", b.getEntry().get(0).getResource().getMeta().getTag().get(0).getSystem());
    assertEquals("term", b.getEntry().get(0).getResource().getMeta().getTag().get(0).getCode());
    assertEquals("label", b.getEntry().get(0).getResource().getMeta().getTag().get(0).getDisplay());
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
    Patient patient = new Patient();
    patient.setId("Patient/1333");
    patient.addIdentifier().setSystem("urn:mrns").setValue("253345");
    patient.getText().setDivAsString("<div>BARFOO</div>");
    patient.getManagingOrganization().setResource(org);

    String encoded = parser.encodeResourceToString(patient);
    ourLog.info(encoded);

    assertThat(encoded, stringContainsInOrder("<Patient", "<text>",
       "<div xmlns=\"http://www.w3.org/1999/xhtml\">BARFOO</div>", "<contained>", "<Organization", "</Organization"));
    assertThat(encoded,
       stringContainsInOrder("<Patient", "<text>", "<contained>", "<Organization", "<text", "</Organization"));

    assertThat(encoded, (containsString("FOOBAR")));
    assertThat(encoded, (containsString("BARFOO")));

  }

  @Test
  public void testEncodeDeclaredExtensionWithAddressContent() {
    IParser parser = ourCtx.newXmlParser();

    MyPatientWithOneDeclaredAddressExtension patient = new MyPatientWithOneDeclaredAddressExtension();
    patient.addAddress().setUse(AddressUse.HOME);
    patient.setFoo(new Address().addLine("line1"));

    String val = parser.encodeResourceToString(patient);
    ourLog.info(val);
    assertThat(val, StringContains
        .containsString("<extension url=\"urn:foo\"><valueAddress><line value=\"line1\"/></valueAddress></extension>"));

    MyPatientWithOneDeclaredAddressExtension actual = parser
        .parseResource(MyPatientWithOneDeclaredAddressExtension.class, val);
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
    assertThat(val, StringContains.containsString(
        "<extension url=\"urn:foo\"><valueReference><reference value=\"Organization/123\"/></valueReference></extension>"));

    MyPatientWithOneDeclaredExtension actual = parser.parseResource(MyPatientWithOneDeclaredExtension.class, val);
    assertEquals(AddressUse.HOME, patient.getAddress().get(0).getUse());
    Reference ref = actual.getFoo();
    assertEquals("Organization/123", ref.getReferenceElement().getValue());

  }

  /**
   * #158
   */
  @Test
  public void testEncodeEmptyTag() {
    Patient p = new Patient();
    p.getMeta().addTag();

    String encoded = ourCtx.newXmlParser().encodeResourceToString(p);
    assertThat(encoded, not(containsString("tag")));

    // With tag

    p = new Patient();
    p.getMeta().addTag().setSystem("sys").setCode("code");

    encoded = ourCtx.newXmlParser().encodeResourceToString(p);
    assertThat(encoded, (containsString("tag")));
  }

  @Test
  public void testEncodeEscapedChars() {

    Patient p = new Patient();
    p.addName().addFamily("and <>&ü");

    String enc = ourCtx.newXmlParser().encodeResourceToString(p);
    ourLog.info(enc);

    p = ourCtx.newXmlParser().parseResource(Patient.class, enc);
    assertEquals("and <>&ü", p.getName().get(0).getFamily().get(0).getValue());

    p = ourCtx.newXmlParser().parseResource(Patient.class,
        "<Patient xmlns=\"http://hl7.org/fhir\"><name><family value=\"quot &quot;\"/></name></Patient>");
    assertEquals("quot \"", p.getName().get(0).getFamily().get(0).getValue());

  }

  @Test
  public void testEncodeEscapedExtendedChars() {
    Patient p = ourCtx.newXmlParser().parseResource(Patient.class,
        "<Patient xmlns=\"http://hl7.org/fhir\"><name><family value=\"uuml &uuml;\"/></name></Patient>");
    assertEquals("uuml ü", p.getName().get(0).getFamily().get(0).getValue());
  }

  @Test
  public void testEncodeExtensionUndeclaredNonModifier() {
    Observation obs = new Observation();
    obs.setId("1");
    obs.getMeta().addProfile("http://profile");
    Extension ext = obs.addExtension();
    ext.setUrl("http://exturl").setValue(new StringType("ext_url_value"));

    obs.getCode().setText("CODE");

    IParser parser = ourCtx.newXmlParser();

    String output = parser.setPrettyPrint(true).encodeResourceToString(obs);
    ourLog.info(output);

    // @formatter:off
    assertThat(output, stringContainsInOrder("<id value=\"1\"/>", "<meta>", "<profile value=\"http://profile\"/>",
        "<extension url=\"http://exturl\">", "<valueString value=\"ext_url_value\"/>", "<text value=\"CODE\"/>"));
    assertThat(output, not(stringContainsInOrder("<url value=\"http://exturl\"/>")));
    // @formatter:on

    obs = parser.parseResource(Observation.class, output);
    assertEquals(1, obs.getExtension().size());
    assertEquals("http://exturl", obs.getExtension().get(0).getUrl());
    assertEquals("ext_url_value", ((StringType) obs.getExtension().get(0).getValue()).getValue());
  }

  @Test
  public void testEncodeExtensionUndeclaredNonModifierWithChildExtension() {
    Observation obs = new Observation();
    obs.setId("1");
    obs.getMeta().addProfile("http://profile");
    Extension ext = obs.addExtension();
    ext.setUrl("http://exturl");

    Extension subExt = ext.addExtension();
    subExt.setUrl("http://subext").setValue(new StringType("sub_ext_value"));

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

    obs = parser.parseResource(Observation.class, output);
    assertEquals(1, obs.getExtension().size());
    assertEquals("http://exturl", obs.getExtension().get(0).getUrl());
    assertEquals(1, obs.getExtension().get(0).getExtension().size());
    assertEquals("http://subext", obs.getExtension().get(0).getExtension().get(0).getUrl());
    assertEquals("sub_ext_value", ((StringType) obs.getExtension().get(0).getExtension().get(0).getValue()).getValue());
  }

  /**
   * See #327
   */
  @Test
  public void testEncodeExtensionWithContainedResource() {

    TestPatientFor327 patient = new TestPatientFor327();
    patient.setBirthDateElement(new DateType("2016-04-14"));

    List<Reference> conditions = new ArrayList<Reference>();
    Condition condition = new Condition();
    condition.addBodySite().setText("BODY SITE");
    conditions.add(new Reference(condition));
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

    Patient patient = new Patient();
    patient.addAddress().setUse(AddressUse.HOME);
    patient.addExtension().setUrl("urn:foo").setValue(new Reference().setReference("Organization/123"));

    String val = parser.encodeResourceToString(patient);
    ourLog.info(val);
    assertThat(val, StringContains.containsString(
        "<extension url=\"urn:foo\"><valueReference><reference value=\"Organization/123\"/></valueReference></extension>"));

    Patient actual = parser.parseResource(Patient.class, val);
    assertEquals(AddressUse.HOME, patient.getAddress().get(0).getUse());
    List<Extension> ext = actual.getExtension();
    assertEquals(1, ext.size());
    Reference ref = (Reference) ext.get(0).getValue();
    assertEquals("Organization/123", ref.getReferenceElement().getValue());

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
  @Disabled
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
  public void testEncodeNarrativeSuppressed() throws Exception {
    Patient patient = new Patient();
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
    Patient patient = new Patient();
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
    Bundle b = new Bundle();
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

    Patient patient = new Patient();
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
    assertThat(str, StringContains.containsString(
        "<managingOrganization><reference value=\"Organization/123\"/><display value=\"DISPLAY!\"/></managingOrganization>"));

    Organization org = new Organization();
    org.addIdentifier().setSystem("foo").setValue("bar");
    patient.setManagingOrganization(new Reference(org));
    str = p.encodeResourceToString(patient);
    assertThat(str, StringContains.containsString("<contained><Organization"));

  }

  @Test
  public void testEncodeSummary() throws Exception {
    Patient patient = new Patient();
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
    Patient patient = new Patient();
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

    Patient patient = new Patient();
    patient.addAddress().setUse(AddressUse.HOME);
    patient.addExtension().setUrl("urn:foo").setValue(new Address().addLine("line1"));

    String val = parser.encodeResourceToString(patient);
    ourLog.info(val);
    assertThat(val, StringContains
        .containsString("<extension url=\"urn:foo\"><valueAddress><line value=\"line1\"/></valueAddress></extension>"));

    MyPatientWithOneDeclaredAddressExtension actual = parser
        .parseResource(MyPatientWithOneDeclaredAddressExtension.class, val);
    assertEquals(AddressUse.HOME, patient.getAddress().get(0).getUse());
    Address ref = actual.getFoo();
    assertEquals("line1", ref.getLine().get(0).getValue());

  }

  @Test
  public void testEncodeUndeclaredExtensionWithEnumerationContent() {
    IParser parser = ourCtx.newXmlParser();

    Patient patient = new Patient();
    patient.addAddress().setUse(AddressUse.HOME);
    EnumFactory<AddressUse> fact = new AddressUseEnumFactory();
    PrimitiveType<AddressUse> enumeration = new Enumeration<AddressUse>(fact).setValue(AddressUse.HOME);
    patient.addExtension().setUrl("urn:foo").setValue(enumeration);

    String val = parser.encodeResourceToString(patient);
    ourLog.info(val);
    assertThat(val,
        StringContains.containsString("<extension url=\"urn:foo\"><valueCode value=\"home\"/></extension>"));

    MyPatientWithOneDeclaredEnumerationExtension actual = parser
        .parseResource(MyPatientWithOneDeclaredEnumerationExtension.class, val);
    assertEquals(AddressUse.HOME, patient.getAddress().get(0).getUse());
    Enumeration<AddressUse> ref = actual.getFoo();
    assertEquals("home", ref.getValue().toCode());

  }

  @Test
  public void testEncodingNullExtension() {
    Patient p = new Patient();
    Extension extension = new Extension().setUrl("http://foo#bar");
    p.getExtension().add(extension);
    String str = ourCtx.newXmlParser().encodeResourceToString(p);

    assertEquals("<Patient xmlns=\"http://hl7.org/fhir\"/>", str);

    extension.setValue(new StringType());

    str = ourCtx.newXmlParser().encodeResourceToString(p);
    assertEquals("<Patient xmlns=\"http://hl7.org/fhir\"/>", str);

    extension.setValue(new StringType(""));

    str = ourCtx.newXmlParser().encodeResourceToString(p);
    assertEquals("<Patient xmlns=\"http://hl7.org/fhir\"/>", str);

  }

  @Test
  public void testExtensionOnComposite() throws Exception {

    Patient patient = new Patient();

    HumanName name = patient.addName();
    name.addFamily("Shmoe");
    HumanName given = name.addGiven("Joe");
    Extension ext2 = new Extension().setUrl("http://examples.com#givenext").setValue(new StringType("Hello"));
    given.getExtension().add(ext2);
    String output = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(patient);
    ourLog.info(output);

    String enc = ourCtx.newXmlParser().encodeResourceToString(patient);
    assertThat(enc, containsString(
        "<name><extension url=\"http://examples.com#givenext\"><valueString value=\"Hello\"/></extension><family value=\"Shmoe\"/><given value=\"Joe\"/></name>"));

    Patient parsed = ourCtx.newXmlParser().parseResource(Patient.class, new StringReader(enc));
    assertEquals(1, parsed.getName().get(0).getExtension().size());
    Extension ext = parsed.getName().get(0).getExtension().get(0);
    assertEquals("Hello", ((IPrimitiveType<?>) ext.getValue()).getValue());

  }

  @Test
  public void testExtensionOnPrimitive() throws Exception {

    Patient patient = new Patient();

    HumanName name = patient.addName();
    StringType family = name.addFamilyElement();
    family.setValue("Shmoe");

    Extension ext2 = new Extension().setUrl("http://examples.com#givenext").setValue(new StringType("Hello"));
    family.getExtension().add(ext2);
    String output = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(patient);
    ourLog.info(output);

    String enc = ourCtx.newXmlParser().encodeResourceToString(patient);
    assertThat(enc, containsString(
        "<name><family value=\"Shmoe\"><extension url=\"http://examples.com#givenext\"><valueString value=\"Hello\"/></extension></family></name>"));

    Patient parsed = ourCtx.newXmlParser().parseResource(Patient.class, new StringReader(enc));
    assertEquals(1, parsed.getName().get(0).getFamily().get(0).getExtension().size());
    Extension ext = parsed.getName().get(0).getFamily().get(0).getExtension().get(0);
    assertEquals("Hello", ((IPrimitiveType<?>) ext.getValue()).getValue());

  }

  @Test
  public void testExtensions() throws DataFormatException {

    MyPatientHl7Org patient = new MyPatientHl7Org();
    patient.setPetName(new StringType("Fido"));
    patient.getImportantDates().add(new DateTimeType("2010-01-02"));
    patient.getImportantDates().add(new DateTimeType("2014-01-26T11:11:11"));

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

    Patient patient = ourCtx.newXmlParser().parseResource(Patient.class, msg);

    assertEquals(NarrativeStatus.GENERATED, patient.getText().getStatus());
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

    Patient resource = (Patient) p.parseResource(msg);
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

    assertThat(str,
        stringContainsInOrder(Arrays.asList("<text value=\"B\"/>", "<text value=\"C\"/>", "<text value=\"A\"/>")));
    assertThat(str, stringContainsInOrder(Arrays.asList("<contained>", "</contained>", "<contained>", "</contained>")));

    Observation obs = ourCtx.newXmlParser().parseResource(Observation.class, str);
    assertEquals("A", obs.getCode().getText());

    Observation obsB = (Observation) obs.getRelated().get(0).getTarget().getResource();
    assertEquals("B", obsB.getCode().getText());

    Observation obsC = (Observation) obsB.getRelated().get(0).getTarget().getResource();
    assertEquals("C", obsC.getCode().getText());

  }

  @Test
  public void testParseBinaryResource() {

    Binary val = ourCtx.newXmlParser().parseResource(Binary.class,
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
    final Binary binary = new Binary();
    binary.setContentType("PatientConsent").setContent(bin);
    // binary.setId(UUID.randomUUID().toString());

    DocumentManifest manifest = new DocumentManifest();
    // manifest.setId(UUID.randomUUID().toString());
    CodeableConcept cc = new CodeableConcept();
    cc.addCoding().setSystem("mySystem").setCode("PatientDocument");
    manifest.setType(cc);
    manifest.setMasterIdentifier(new Identifier().setSystem("mySystem").setValue(UUID.randomUUID().toString()));
    manifest.addContent().setP(new Reference(binary));
    manifest.setStatus(org.hl7.fhir.dstu2.model.Enumerations.DocumentReferenceStatus.CURRENT);

    String encoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(manifest);
    ourLog.info(encoded);
    assertThat(encoded,
        StringContainsInOrder.stringContainsInOrder(Arrays.asList("contained>", "<Binary", "</contained>")));

    DocumentManifest actual = ourCtx.newXmlParser().parseResource(DocumentManifest.class, encoded);
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
    Patient pt = ourCtx.newXmlParser().parseResource(Patient.class, input);

    assertEquals("zh-CN", pt.getLanguage());
  }

  @Test
  @Disabled
  public void testParseNarrative() throws Exception {
    // @formatter:off
    String htmlNoNs = "<div>AAA<b>BBB</b>CCC</div>";
    String htmlNs = fixDivNodeText(htmlNoNs);
    String res = "<Patient xmlns=\"http://hl7.org/fhir\">\n" + "   <id value=\"1333\"/>\n" + "   <text>\n" + "      "
        + htmlNs + "\n" + "   </text>\n" + "</Patient>";
    // @formatter:on

    Patient p = ourCtx.newXmlParser().parseResource(Patient.class, res);
    assertEquals(htmlNs, p.getText().getDivAsString());
  }

  @Test
  public void testParseWithXmlHeader() throws ConfigurationException, DataFormatException {
    IParser p = ourCtx.newXmlParser();

    // @formatter:off
    String msg = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<Patient xmlns=\"http://hl7.org/fhir\">\n"
        + "	<identifier>\n" + "		<value value=\"IdentifierLabel\"/>\n" + "	</identifier>\n" + "</Patient>";
    // @formatter:on

    Patient resource = (Patient) p.parseResource(msg);
    assertEquals("IdentifierLabel", resource.getIdentifier().get(0).getValue());
  }

  @Test
  public void testReEncode() throws SAXException, IOException {

    // @formatter:off
    String msg = "<Patient xmlns=\"http://hl7.org/fhir\">"
        + "<identifier><system value=\"http://orionhealth.com/mrn\" /><value value=\"PRP1660\" /></identifier>"
        + "</Patient>";
    // @formatter:on

    Patient patient1 = ourCtx.newXmlParser().parseResource(Patient.class, msg);
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
    Patient obs = ourCtx.newJsonParser().parseResource(Patient.class, xmlString);

    List<Extension> undeclaredExtensions = obs.getContact().get(0).getName().getFamily().get(0).getExtension();
    Extension undeclaredExtension = undeclaredExtensions.get(0);
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
  public void testSimpleResourceEncodeWithCustomType() throws IOException {

    String xmlString = IOUtils.toString(
        XmlParserHl7OrgDstu2Test.class.getResourceAsStream("/example-patient-general-hl7orgdstu2.xml"),
        Charset.forName("UTF-8"));
    MyObservationWithExtensions obs = ourCtx.newXmlParser().parseResource(MyObservationWithExtensions.class, xmlString);

    assertEquals(0, obs.getExtension().size());
    assertEquals("aaaa", obs.getExtAtt().getContentType());
    assertEquals("str1", obs.getMoreExt().getStr1().getValue());
    assertEquals("2011-01-02", obs.getModExt().getValueAsString());

    List<Extension> undeclaredExtensions = obs.getContact().get(0).getName().getFamily().get(0).getExtension();
    Extension undeclaredExtension = undeclaredExtensions.get(0);
    assertEquals("http://hl7.org/fhir/Profile/iso-21090#qualifier", undeclaredExtension.getUrl());

    IParser jsonParser = ourCtx.newJsonParser().setPrettyPrint(true);
    String encoded = jsonParser.encodeResourceToString(obs);
    ourLog.info(encoded);

    String jsonString = IOUtils.toString(
        XmlParserHl7OrgDstu2Test.class.getResourceAsStream("/example-patient-general-hl7orgdstu2.json"),
        Charset.forName("UTF-8"));

    JSON expected = JSONSerializer.toJSON(jsonString);
    JSON actual = JSONSerializer.toJSON(encoded.trim());

    String exp = fixDivNodeTextJson(expected.toString());
    String act = fixDivNodeTextJson(actual.toString());

    ourLog.info("Expected: {}", exp);
    ourLog.info("Actual  : {}", act);
    assertEquals(exp, act);

  }

	@Test
	public void testBaseUrlFooResourceCorrectlySerializedInExtensionValueReference() {
		String refVal = "http://my.org/FooBar";

		Patient fhirPat = new Patient();
		fhirPat.addExtension().setUrl("x1").setValue(new Reference(refVal));

		IParser parser = ourCtx.newXmlParser();

		String output = parser.encodeResourceToString(fhirPat);
		System.out.println("output: " + output);

		// Deserialize then check that valueReference value is still correct
		fhirPat = parser.parseResource(Patient.class, output);

		List<Extension> extlst = fhirPat.getExtension();
		assertEquals(1, extlst.size());
		assertEquals(refVal, ((Reference) extlst.get(0).getValue()).getReference());
	}

  @BeforeAll
  public static void beforeClass() {
    ourCtx = FhirContext.forDstu2Hl7Org();
  }

  @BeforeAll
  public static void beforeClass2() {
    System.setProperty("file.encoding", "ISO-8859-1");
  }

  @ResourceDef(name = "Patient")
  public static class TestPatientFor327 extends Patient {

    private static final long serialVersionUID = 1L;

    @Child(name = "testCondition")
    @ca.uhn.fhir.model.api.annotation.Extension(url = "testCondition", definedLocally = true, isModifier = false)
    private List<Reference> testConditions = null;

    public List<Reference> getConditions() {
      return this.testConditions;
    }

    public void setCondition(List<Reference> ref) {
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

    assertTrue(!d.hasDifferences(), d.toString());
 }

}
