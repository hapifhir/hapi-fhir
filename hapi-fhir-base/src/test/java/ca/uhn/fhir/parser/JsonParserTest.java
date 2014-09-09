package ca.uhn.fhir.parser;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sf.json.JSON;
import net.sf.json.JSONSerializer;

import org.apache.commons.io.IOUtils;
import org.hamcrest.core.IsNot;
import org.hamcrest.core.StringContains;
import org.hamcrest.text.StringContainsInOrder;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.BundleEntry;
import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.dstu.composite.AddressDt;
import ca.uhn.fhir.model.dstu.composite.HumanNameDt;
import ca.uhn.fhir.model.dstu.composite.NarrativeDt;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.resource.Binary;
import ca.uhn.fhir.model.dstu.resource.Conformance;
import ca.uhn.fhir.model.dstu.resource.Conformance.RestResource;
import ca.uhn.fhir.model.dstu.resource.DiagnosticReport;
import ca.uhn.fhir.model.dstu.resource.Observation;
import ca.uhn.fhir.model.dstu.resource.Organization;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.resource.Query;
import ca.uhn.fhir.model.dstu.resource.Specimen;
import ca.uhn.fhir.model.dstu.resource.ValueSet;
import ca.uhn.fhir.model.dstu.resource.ValueSet.Define;
import ca.uhn.fhir.model.dstu.resource.ValueSet.DefineConcept;
import ca.uhn.fhir.model.dstu.valueset.AddressUseEnum;
import ca.uhn.fhir.model.dstu.valueset.NarrativeStatusEnum;
import ca.uhn.fhir.model.primitive.DecimalDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.primitive.XhtmlDt;
import ca.uhn.fhir.narrative.INarrativeGenerator;


public class JsonParserTest
{


  private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(JsonParserTest.class);

  private static FhirContext ourCtx;



  @Test
  public void testEncodeNarrativeBlockInBundle()
  {
    final Patient p = new Patient();
    p.addIdentifier("foo", "bar");
    p.getText().setStatus(NarrativeStatusEnum.GENERATED);
    p.getText().setDiv("<div>hello</div>");

    final Bundle b = new Bundle();
    b.getTotalResults().setValue(123);
    b.addEntry().setResource(p);

    String out = ourCtx.newJsonParser().setPrettyPrint(true).encodeBundleToString(b);
    ourLog.info(out);
    assertThat(out, containsString("<div>hello</div>"));

    p.getText().setDiv("<xhtml:div xmlns:xhtml=\"http://www.w3.org/1999/xhtml\">hello</xhtml:div>");
    out = ourCtx.newJsonParser().setPrettyPrint(true).encodeBundleToString(b);
    ourLog.info(out);
    // Backslashes need to be escaped because they are in a JSON value
    assertThat(out, containsString("<xhtml:div xmlns:xhtml=\\\"http://www.w3.org/1999/xhtml\\\">hello</xhtml:div>"));

  }



  @Test
  public void testEncodingNullExtension()
  {
    final Patient p = new Patient();
    final ExtensionDt extension = new ExtensionDt(false, "http://foo#bar");
    p.addUndeclaredExtension(extension);
    String str = ourCtx.newJsonParser().encodeResourceToString(p);

    assertEquals("{\"resourceType\":\"Patient\"}", str);

    extension.setValue(new StringDt());

    str = ourCtx.newJsonParser().encodeResourceToString(p);
    assertEquals("{\"resourceType\":\"Patient\"}", str);

    extension.setValue(new StringDt(""));

    str = ourCtx.newJsonParser().encodeResourceToString(p);
    assertEquals("{\"resourceType\":\"Patient\"}", str);

  }



  @Test
  public void testParseSingleQuotes()
  {
    try
    {
      ourCtx.newJsonParser().parseBundle("{ 'resourceType': 'Bundle' }");
      fail();
    }
    catch (final DataFormatException e)
    {
      // Should be an error message about how single quotes aren't valid JSON
      assertThat(e.getMessage(), containsString("double quote"));
    }
  }



  @Test
  public void testEncodeExtensionInCompositeElement()
  {

    final Conformance c = new Conformance();
    c.addRest().getSecurity().addUndeclaredExtension(false, "http://foo", new StringDt("AAA"));

    String encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(c);
    ourLog.info(encoded);

    encoded = ourCtx.newJsonParser().setPrettyPrint(false).encodeResourceToString(c);
    ourLog.info(encoded);
    assertEquals(
        encoded,
        "{\"resourceType\":\"Conformance\",\"rest\":[{\"security\":{\"extension\":[{\"url\":\"http://foo\",\"valueString\":\"AAA\"}]}}]}");

  }



  @Test
  public void testEncodeExtensionInPrimitiveElement()
  {

    Conformance c = new Conformance();
    c.getAcceptUnknown().addUndeclaredExtension(false, "http://foo", new StringDt("AAA"));

    String encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(c);
    ourLog.info(encoded);

    encoded = ourCtx.newJsonParser().setPrettyPrint(false).encodeResourceToString(c);
    ourLog.info(encoded);
    assertEquals(
        encoded,
        "{\"resourceType\":\"Conformance\",\"_acceptUnknown\":[{\"extension\":[{\"url\":\"http://foo\",\"valueString\":\"AAA\"}]}]}");

    // Now with a value
    ourLog.info("---------------");

    c = new Conformance();
    c.getAcceptUnknown().setValue(true);
    c.getAcceptUnknown().addUndeclaredExtension(false, "http://foo", new StringDt("AAA"));

    encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(c);
    ourLog.info(encoded);

    encoded = ourCtx.newJsonParser().setPrettyPrint(false).encodeResourceToString(c);
    ourLog.info(encoded);
    assertEquals(
        encoded,
        "{\"resourceType\":\"Conformance\",\"acceptUnknown\":true,\"_acceptUnknown\":[{\"extension\":[{\"url\":\"http://foo\",\"valueString\":\"AAA\"}]}]}");

  }



  @Test
  public void testEncodeExtensionInResourceElement()
  {

    final Conformance c = new Conformance();
    // c.addRest().getSecurity().addUndeclaredExtension(false, "http://foo", new StringDt("AAA"));
    c.addUndeclaredExtension(false, "http://foo", new StringDt("AAA"));

    String encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(c);
    ourLog.info(encoded);

    encoded = ourCtx.newJsonParser().setPrettyPrint(false).encodeResourceToString(c);
    ourLog.info(encoded);
    assertEquals(encoded,
        "{\"resourceType\":\"Conformance\",\"extension\":[{\"url\":\"http://foo\",\"valueString\":\"AAA\"}]}");

  }



  @Test
  public void testEncodeBinaryResource()
  {

    final Binary patient = new Binary();
    patient.setContentType("foo");
    patient.setContent(new byte[] { 1, 2, 3, 4 });

    final String val = ourCtx.newJsonParser().encodeResourceToString(patient);
    assertEquals("{\"resourceType\":\"Binary\",\"contentType\":\"foo\",\"content\":\"AQIDBA==\"}", val);

  }



  @Test
  public void testParseEmptyNarrative() throws ConfigurationException, DataFormatException, IOException
  {
    // @formatter:off
    final String text = "{\n" + "    \"resourceType\" : \"Patient\",\n" + "    \"extension\" : [\n" + "      {\n"
        + "        \"url\" : \"http://clairol.org/colour\",\n" + "        \"valueCode\" : \"B\"\n" + "      }\n"
        + "    ],\n" + "    \"text\" : {\n"
        + "      \"div\" : \"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\"?>\"\n" + "    }" + "}";
    // @formatter:on

    final Patient res = (Patient) ourCtx.newJsonParser().parseResource(text);
    final String value = res.getText().getDiv().getValueAsString();

    assertNull(value);
  }



  @Test
  public void testNestedContainedResources()
  {

    final Observation A = new Observation();
    A.getName().setText("A");

    final Observation B = new Observation();
    B.getName().setText("B");
    A.addRelated().setTarget(new ResourceReferenceDt(B));

    final Observation C = new Observation();
    C.getName().setText("C");
    B.addRelated().setTarget(new ResourceReferenceDt(C));

    final String str = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(A);
    ourLog.info(str);

    assertThat(str, stringContainsInOrder(Arrays.asList("\"text\":\"B\"", "\"text\":\"C\"", "\"text\":\"A\"")));

    // Only one (outer) contained block
    final int idx0 = str.indexOf("\"contained\"");
    final int idx1 = str.indexOf("\"contained\"", idx0 + 1);

    assertNotEquals(-1, idx0);
    assertEquals(-1, idx1);

    final Observation obs = ourCtx.newJsonParser().parseResource(Observation.class, str);
    assertEquals("A", obs.getName().getText().getValue());

    final Observation obsB = (Observation) obs.getRelatedFirstRep().getTarget().getResource();
    assertEquals("B", obsB.getName().getText().getValue());

    final Observation obsC = (Observation) obsB.getRelatedFirstRep().getTarget().getResource();
    assertEquals("C", obsC.getName().getText().getValue());

  }



  @Test
  public void testParseQuery()
  {
    final String msg = "{\n" + "  \"resourceType\": \"Query\",\n" + "  \"text\": {\n"
        + "    \"status\": \"generated\",\n" + "    \"div\": \"<div>[Put rendering here]</div>\"\n" + "  },\n"
        + "  \"identifier\": \"urn:uuid:42b253f5-fa17-40d0-8da5-44aeb4230376\",\n" + "  \"parameter\": [\n" + "    {\n"
        + "      \"url\": \"http://hl7.org/fhir/query#_query\",\n" + "      \"valueString\": \"example\"\n" + "    }\n"
        + "  ]\n" + "}";
    final Query query = ourCtx.newJsonParser().parseResource(Query.class, msg);

    assertEquals("urn:uuid:42b253f5-fa17-40d0-8da5-44aeb4230376", query.getIdentifier().getValueAsString());
    assertEquals("http://hl7.org/fhir/query#_query", query.getParameterFirstRep().getUrlAsString());
    assertEquals("example", query.getParameterFirstRep().getValueAsPrimitive().getValueAsString());

  }



  @Test
  public void testEncodeQuery()
  {
    final Query q = new Query();
    final ExtensionDt parameter = q.addParameter();
    parameter.setUrl("http://hl7.org/fhir/query#_query").setValue(new StringDt("example"));

    final String val = new FhirContext().newJsonParser().encodeResourceToString(q);
    ourLog.info(val);

    // @formatter:off
    final String expected = "{" + "\"resourceType\":\"Query\"," + "\"parameter\":[" + "{"
        + "\"url\":\"http://hl7.org/fhir/query#_query\"," + "\"valueString\":\"example\"" + "}" + "]" + "}";
    // @formatter:on

    ourLog.info("Expect: {}", expected);
    ourLog.info("Got   : {}", val);
    assertEquals(expected, val);

  }



  @Test
  public void testParseBinaryResource()
  {

    final Binary val = ourCtx.newJsonParser().parseResource(Binary.class,
        "{\"resourceType\":\"Binary\",\"contentType\":\"foo\",\"content\":\"AQIDBA==\"}");
    assertEquals("foo", val.getContentType());
    assertArrayEquals(new byte[] { 1, 2, 3, 4 }, val.getContent());

  }



  @Test
  public void testTagList()
  {

    // @formatter:off
    final String tagListStr = "{\n" + "  \"resourceType\" : \"TagList\", " + "  \"category\" : [" + "    { "
        + "      \"term\" : \"term0\", " + "      \"label\" : \"label0\", " + "      \"scheme\" : \"scheme0\" "
        + "    }," + "    { " + "      \"term\" : \"term1\", " + "      \"label\" : \"label1\", "
        + "      \"scheme\" : null " + "    }," + "    { " + "      \"term\" : \"term2\", "
        + "      \"label\" : \"label2\" " + "    }" + "  ] " + "}";
    // @formatter:on

    final TagList tagList = new FhirContext().newJsonParser().parseTagList(tagListStr);
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

    // @formatter:off
    final String expected = "{" + "\"resourceType\":\"TagList\"," + "\"category\":[" + "{" + "\"term\":\"term0\","
        + "\"label\":\"label0\"," + "\"scheme\":\"scheme0\"" + "}," + "{" + "\"term\":\"term1\","
        + "\"label\":\"label1\"" + "}," + "{" + "\"term\":\"term2\"," + "\"label\":\"label2\"" + "}" + "]" + "}";
    // @formatter:on

    final String encoded = new FhirContext().newJsonParser().encodeTagListToString(tagList);
    assertEquals(expected, encoded);

  }



  @Test
  public void testEncodeBundleCategory()
  {

    Bundle b = new Bundle();
    final BundleEntry e = b.addEntry();
    e.setResource(new Patient());
    b.addCategory().setLabel("label").setTerm("term").setScheme("scheme");

    final String val = new FhirContext().newJsonParser().setPrettyPrint(false).encodeBundleToString(b);
    ourLog.info(val);

    assertThat(val,
        StringContains.containsString("\"category\":[{\"term\":\"term\",\"label\":\"label\",\"scheme\":\"scheme\"}]"));

    b = new FhirContext().newJsonParser().parseBundle(val);
    assertEquals(1, b.getEntries().size());
    assertEquals(1, b.getCategories().size());
    assertEquals("term", b.getCategories().get(0).getTerm());
    assertEquals("label", b.getCategories().get(0).getLabel());
    assertEquals("scheme", b.getCategories().get(0).getScheme());
    assertNull(b.getEntries().get(0).getResource());

  }



  @Test
  public void testEncodeBundleEntryCategory()
  {

    Bundle b = new Bundle();
    final BundleEntry e = b.addEntry();
    e.setResource(new Patient());
    e.addCategory().setLabel("label").setTerm("term").setScheme("scheme");

    final String val = new FhirContext().newJsonParser().setPrettyPrint(false).encodeBundleToString(b);
    ourLog.info(val);

    assertThat(val,
        StringContains.containsString("\"category\":[{\"term\":\"term\",\"label\":\"label\",\"scheme\":\"scheme\"}]"));

    b = new FhirContext().newJsonParser().parseBundle(val);
    assertEquals(1, b.getEntries().size());
    assertEquals(1, b.getEntries().get(0).getCategories().size());
    assertEquals("term", b.getEntries().get(0).getCategories().get(0).getTerm());
    assertEquals("label", b.getEntries().get(0).getCategories().get(0).getLabel());
    assertEquals("scheme", b.getEntries().get(0).getCategories().get(0).getScheme());
    assertNull(b.getEntries().get(0).getResource());

  }



  @Test
  public void testEncodeContainedResources() throws IOException
  {

    final String msg = IOUtils.toString(XmlParser.class.getResourceAsStream("/contained-diagnosticreport.xml"));
    final IParser p = ourCtx.newXmlParser();
    final DiagnosticReport res = p.parseResource(DiagnosticReport.class, msg);

    final String encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(res);
    ourLog.info(encoded);

  }



  @Test
  public void testEncodeContainedResourcesMore()
  {

    final DiagnosticReport rpt = new DiagnosticReport();
    final Specimen spm = new Specimen();
    spm.getText().setDiv("AAA");
    rpt.addSpecimen().setResource(spm);

    final IParser p = new FhirContext(DiagnosticReport.class).newJsonParser().setPrettyPrint(true);
    final String str = p.encodeResourceToString(rpt);

    ourLog.info(str);
    assertThat(str, StringContains.containsString("<div>AAA</div>"));
    final String substring = "\"reference\":\"#";
    assertThat(str, StringContains.containsString(substring));

    final int idx = str.indexOf(substring) + substring.length();
    final int idx2 = str.indexOf('"', idx + 1);
    final String id = str.substring(idx, idx2);
    assertThat(str, StringContains.containsString("\"id\":\"" + id + "\""));
    assertThat(str, IsNot.not(StringContains.containsString("<?xml version='1.0'?>")));

  }



  @Test
  public void testEncodeDeclaredExtensionWithAddressContent()
  {
    final IParser parser = new FhirContext().newJsonParser();

    final MyPatientWithOneDeclaredAddressExtension patient = new MyPatientWithOneDeclaredAddressExtension();
    patient.addAddress().setUse(AddressUseEnum.HOME);
    patient.setFoo(new AddressDt().addLine("line1"));

    final String val = parser.encodeResourceToString(patient);
    ourLog.info(val);
    assertThat(val,
        StringContains.containsString("\"extension\":[{\"url\":\"urn:foo\",\"valueAddress\":{\"line\":[\"line1\"]}}]"));

    final MyPatientWithOneDeclaredAddressExtension actual = parser.parseResource(
        MyPatientWithOneDeclaredAddressExtension.class, val);
    assertEquals(AddressUseEnum.HOME, patient.getAddressFirstRep().getUse().getValueAsEnum());
    final AddressDt ref = actual.getFoo();
    assertEquals("line1", ref.getLineFirstRep().getValue());

  }



  @Test
  public void testEncodeDeclaredExtensionWithResourceContent()
  {
    final IParser parser = new FhirContext().newJsonParser();

    final MyPatientWithOneDeclaredExtension patient = new MyPatientWithOneDeclaredExtension();
    patient.addAddress().setUse(AddressUseEnum.HOME);
    patient.setFoo(new ResourceReferenceDt("Organization/123"));

    final String val = parser.encodeResourceToString(patient);
    ourLog.info(val);
    assertThat(
        val,
        StringContains
            .containsString("\"extension\":[{\"url\":\"urn:foo\",\"valueResource\":{\"reference\":\"Organization/123\"}}]"));

    final MyPatientWithOneDeclaredExtension actual = parser.parseResource(MyPatientWithOneDeclaredExtension.class, val);
    assertEquals(AddressUseEnum.HOME, patient.getAddressFirstRep().getUse().getValueAsEnum());
    final ResourceReferenceDt ref = actual.getFoo();
    assertEquals("Organization/123", ref.getReference().getValue());

  }



  @Test
  public void testEncodeExtensionOnEmptyElement() throws Exception
  {

    final ValueSet valueSet = new ValueSet();
    valueSet.addTelecom().addUndeclaredExtension(false, "http://foo", new StringDt("AAA"));

    final String encoded = ourCtx.newJsonParser().encodeResourceToString(valueSet);
    assertThat(encoded,
        containsString("\"telecom\":[{\"extension\":[{\"url\":\"http://foo\",\"valueString\":\"AAA\"}]}"));

  }



  @Test
  public void testEncodeExt() throws Exception
  {

    final ValueSet valueSet = new ValueSet();
    valueSet.setId("123456");

    final Define define = valueSet.getDefine();
    final DefineConcept code = define.addConcept();
    code.setCode("someCode");
    code.setDisplay("someDisplay");
    code.addUndeclaredExtension(false, "urn:alt", new StringDt("alt name"));

    final String encoded = ourCtx.newJsonParser().encodeResourceToString(valueSet);
    ourLog.info(encoded);

    assertThat(encoded, not(containsString("123456")));
    assertEquals(
        "{\"resourceType\":\"ValueSet\",\"define\":{\"concept\":[{\"extension\":[{\"url\":\"urn:alt\",\"valueString\":\"alt name\"}],\"code\":\"someCode\",\"display\":\"someDisplay\"}]}}",
        encoded);

  }



  @Test
  public void testEncodeExtensionWithResourceContent()
  {
    final IParser parser = new FhirContext().newJsonParser();

    final Patient patient = new Patient();
    patient.addAddress().setUse(AddressUseEnum.HOME);
    patient.addUndeclaredExtension(false, "urn:foo", new ResourceReferenceDt("Organization/123"));

    final String val = parser.encodeResourceToString(patient);
    ourLog.info(val);
    assertThat(
        val,
        StringContains
            .containsString("\"extension\":[{\"url\":\"urn:foo\",\"valueResource\":{\"reference\":\"Organization/123\"}}]"));

    final Patient actual = parser.parseResource(Patient.class, val);
    assertEquals(AddressUseEnum.HOME, patient.getAddressFirstRep().getUse().getValueAsEnum());
    final List<ExtensionDt> ext = actual.getUndeclaredExtensionsByUrl("urn:foo");
    assertEquals(1, ext.size());
    final ResourceReferenceDt ref = (ResourceReferenceDt) ext.get(0).getValue();
    assertEquals("Organization/123", ref.getReference().getValue());

  }



  @Test
  public void testEncodeInvalidChildGoodException()
  {
    final Observation obs = new Observation();
    obs.setValue(new DecimalDt(112.22));

    final IParser p = new FhirContext(Observation.class).newJsonParser();

    try
    {
      p.encodeResourceToString(obs);
    }
    catch (final DataFormatException e)
    {
      assertThat(e.getMessage(), StringContains.containsString("DecimalDt"));
    }
  }



  @Test
  public void testEncodeResourceRef() throws DataFormatException
  {

    final Patient patient = new Patient();
    patient.setManagingOrganization(new ResourceReferenceDt());

    final IParser p = new FhirContext().newJsonParser();
    String str = p.encodeResourceToString(patient);
    assertThat(str, IsNot.not(StringContains.containsString("managingOrganization")));

    patient.setManagingOrganization(new ResourceReferenceDt("Organization/123"));
    str = p.encodeResourceToString(patient);
    assertThat(str, StringContains.containsString("\"managingOrganization\":{\"reference\":\"Organization/123\"}"));

    final Organization org = new Organization();
    org.addIdentifier().setSystem("foo").setValue("bar");
    patient.setManagingOrganization(new ResourceReferenceDt(org));
    str = p.encodeResourceToString(patient);
    assertThat(str, StringContains.containsString("\"contained\":[{\"resourceType\":\"Organization\""));

  }



  @Test
  public void testEncodeUndeclaredExtensionWithAddressContent()
  {
    final IParser parser = new FhirContext().newJsonParser();

    final Patient patient = new Patient();
    patient.addAddress().setUse(AddressUseEnum.HOME);
    patient.addUndeclaredExtension(false, "urn:foo", new AddressDt().addLine("line1"));

    final String val = parser.encodeResourceToString(patient);
    ourLog.info(val);
    assertThat(val,
        StringContains.containsString("\"extension\":[{\"url\":\"urn:foo\",\"valueAddress\":{\"line\":[\"line1\"]}}]"));

    final MyPatientWithOneDeclaredAddressExtension actual = parser.parseResource(
        MyPatientWithOneDeclaredAddressExtension.class, val);
    assertEquals(AddressUseEnum.HOME, patient.getAddressFirstRep().getUse().getValueAsEnum());
    final AddressDt ref = actual.getFoo();
    assertEquals("line1", ref.getLineFirstRep().getValue());

  }



  @Test
  public void testExtensionOnComposite() throws Exception
  {

    final Patient patient = new Patient();

    final HumanNameDt name = patient.addName();
    name.addFamily().setValue("Shmoe");
    final HumanNameDt given = name.addGiven("Joe");
    final ExtensionDt ext2 = new ExtensionDt(false, "http://examples.com#givenext", new StringDt("Hello"));
    given.addUndeclaredExtension(ext2);
    final String enc = new FhirContext().newJsonParser().encodeResourceToString(patient);
    ourLog.info(enc);
    assertEquals(
        "{\"resourceType\":\"Patient\",\"name\":[{\"extension\":[{\"url\":\"http://examples.com#givenext\",\"valueString\":\"Hello\"}],\"family\":[\"Shmoe\"],\"given\":[\"Joe\"]}]}",
        enc);

    final IParser newJsonParser = new FhirContext().newJsonParser();
    final StringReader reader = new StringReader(enc);
    final Patient parsed = newJsonParser.parseResource(Patient.class, reader);

    ourLog.info(new FhirContext().newXmlParser().setPrettyPrint(true).encodeResourceToString(parsed));

    assertEquals(1, parsed.getNameFirstRep().getUndeclaredExtensionsByUrl("http://examples.com#givenext").size());
    final ExtensionDt ext = parsed.getNameFirstRep().getUndeclaredExtensionsByUrl("http://examples.com#givenext")
        .get(0);
    assertEquals("Hello", ext.getValueAsPrimitive().getValue());

  }



  @Test
  public void testExtensionOnPrimitive() throws Exception
  {

    final Patient patient = new Patient();

    final HumanNameDt name = patient.addName();
    final StringDt family = name.addFamily();
    family.setValue("Shmoe");

    final ExtensionDt ext2 = new ExtensionDt(false, "http://examples.com#givenext", new StringDt("Hello"));
    family.addUndeclaredExtension(ext2);
    final String enc = new FhirContext().newJsonParser().encodeResourceToString(patient);
    ourLog.info(enc);
    // @formatter:off
    assertThat(enc, containsString(("{\n" + "    \"resourceType\":\"Patient\",\n" + "    \"name\":[\n" + "        {\n"
        + "            \"family\":[\n" + "                \"Shmoe\"\n" + "            ],\n"
        + "            \"_family\":[\n" + "                {\n" + "                    \"extension\":[\n"
        + "                        {\n" + "                            \"url\":\"http://examples.com#givenext\",\n"
        + "                            \"valueString\":\"Hello\"\n" + "                        }\n"
        + "                    ]\n" + "                }\n" + "            ]\n" + "        }\n" + "    ]\n" + "}")
        .replace("\n", "").replaceAll(" +", "")));
    // @formatter:on

    final Patient parsed = new FhirContext().newJsonParser().parseResource(Patient.class, new StringReader(enc));
    assertEquals(1,
        parsed.getNameFirstRep().getFamilyFirstRep().getUndeclaredExtensionsByUrl("http://examples.com#givenext")
            .size());
    final ExtensionDt ext = parsed.getNameFirstRep().getFamilyFirstRep()
        .getUndeclaredExtensionsByUrl("http://examples.com#givenext").get(0);
    assertEquals("Hello", ext.getValueAsPrimitive().getValue());

  }



  @Test
  public void testNarrativeGeneration() throws DataFormatException, IOException
  {

    final Patient patient = new Patient();
    patient.addName().addFamily("Smith");
    final Organization org = new Organization();
    patient.getManagingOrganization().setResource(org);

    final INarrativeGenerator gen = mock(INarrativeGenerator.class);
    final XhtmlDt xhtmlDt = new XhtmlDt("<div>help</div>");
    final NarrativeDt nar = new NarrativeDt(xhtmlDt, NarrativeStatusEnum.GENERATED);
    when(gen.generateNarrative(eq("http://hl7.org/fhir/profiles/Patient"), eq(patient))).thenReturn(nar);

    final FhirContext context = new FhirContext();
    context.setNarrativeGenerator(gen);
    final IParser p = context.newJsonParser();
    p.encodeResourceToWriter(patient, new OutputStreamWriter(System.out));
    final String str = p.encodeResourceToString(patient);

    ourLog.info(str);

    assertThat(str, StringContains.containsString(",\"text\":{\"status\":\"generated\",\"div\":\"<div>help</div>\"},"));
  }



  @Test
  public void testParseBundle() throws DataFormatException, IOException
  {

    final String msg = IOUtils.toString(XmlParser.class.getResourceAsStream("/atom-document-large.json"));
    final IParser p = ourCtx.newJsonParser();
    final Bundle bundle = p.parseBundle(msg);

    assertEquals(1, bundle.getCategories().size());
    assertEquals("http://scheme", bundle.getCategories().get(0).getScheme());
    assertEquals("http://term", bundle.getCategories().get(0).getTerm());
    assertEquals("label", bundle.getCategories().get(0).getLabel());

    final String encoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeBundleToString(bundle);
    ourLog.info(encoded);

    assertEquals(
        "http://fhir.healthintersections.com.au/open/DiagnosticReport/_search?_format=application/json+fhir&search-id=46d5f0e7-9240-4d4f-9f51-f8ac975c65&search-sort=_id",
        bundle.getLinkSelf().getValue());
    assertEquals("urn:uuid:0b754ff9-03cf-4322-a119-15019af8a3", bundle.getBundleId().getValue());

    final BundleEntry entry = bundle.getEntries().get(0);
    assertEquals("http://fhir.healthintersections.com.au/open/DiagnosticReport/101", entry.getId().getValue());
    assertEquals("http://fhir.healthintersections.com.au/open/DiagnosticReport/101/_history/1", entry.getLinkSelf()
        .getValue());
    assertEquals("2014-03-10T11:55:59Z", entry.getUpdated().getValueAsString());

    final DiagnosticReport res = (DiagnosticReport) entry.getResource();
    assertEquals("Complete Blood Count", res.getName().getText().getValue());

  }



  @Test
  public void testParseBundleFromHI() throws DataFormatException, IOException
  {

    final String msg = IOUtils.toString(XmlParser.class.getResourceAsStream("/bundle.json"));
    final IParser p = ourCtx.newJsonParser();
    final Bundle bundle = p.parseBundle(msg);

    final String encoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeBundleToString(bundle);
    ourLog.info(encoded);

    final BundleEntry entry = bundle.getEntries().get(0);

    final Patient res = (Patient) entry.getResource();
    assertEquals("444111234", res.getIdentifierFirstRep().getValue().getValue());

    final BundleEntry deletedEntry = bundle.getEntries().get(3);
    assertEquals("2014-06-20T20:15:49Z", deletedEntry.getDeletedAt().getValueAsString());

  }



  /**
   * This sample has extra elements in <searchParam> that are not actually a part of the spec any more..
   */
  @Test
  public void testParseFuroreMetadataWithExtraElements() throws IOException
  {
    final String msg = IOUtils.toString(JsonParserTest.class.getResourceAsStream("/furore-conformance.json"));

    final IParser p = ourCtx.newJsonParser();
    final Conformance conf = p.parseResource(Conformance.class, msg);
    final RestResource res = conf.getRestFirstRep().getResourceFirstRep();
    assertEquals("_id", res.getSearchParam().get(1).getName().getValue());
  }



  @Test
  public void testParseWithContained() throws DataFormatException, IOException
  {

    final String msg = IOUtils.toString(XmlParser.class.getResourceAsStream("/diagnostic-report.json"));
    final IParser p = ourCtx.newJsonParser();
    // ourLog.info("Reading in message: {}", msg);
    final DiagnosticReport res = p.parseResource(DiagnosticReport.class, msg);

    final String encoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(res);
    ourLog.info(encoded);

    final ResourceReferenceDt reference = res.getResult().get(1);
    final Observation obs = (Observation) reference.getResource();

    assertEquals("789-8", obs.getName().getCoding().get(0).getCode().getValue());
  }



  @BeforeClass
  public static void beforeClass()
  {
    ourCtx = new FhirContext();
  }



  @Test
  public void testParseBundleDeletedEntry()
  {

    // @formatter:off
    final String bundleString = "{" + "\"resourceType\":\"Bundle\"," + "\"totalResults\":\"1\"," + "\"entry\":[" + "{"
        + "\"deleted\":\"2012-05-29T23:45:32+00:00\"," + "\"id\":\"http://fhir.furore.com/fhir/Patient/1\","
        + "\"link\":[" + "{" + "\"rel\":\"self\"," + "\"href\":\"http://fhir.furore.com/fhir/Patient/1/_history/2\""
        + "}" + "]" + "}" + "]" + "}";
    // @formatter:on

    final Bundle bundle = ourCtx.newJsonParser().parseBundle(bundleString);
    final BundleEntry entry = bundle.getEntries().get(0);
    assertEquals("2012-05-29T23:45:32+00:00", entry.getDeletedAt().getValueAsString());
    assertEquals("http://fhir.furore.com/fhir/Patient/1/_history/2", entry.getLinkSelf().getValue());
    assertEquals("1", entry.getResource().getId().getIdPart());
    assertEquals("2", entry.getResource().getId().getVersionIdPart());
    assertEquals(new InstantDt("2012-05-29T23:45:32+00:00"),
        entry.getResource().getResourceMetadata().get(ResourceMetadataKeyEnum.DELETED_AT));

    // Now encode

    ourLog.info(ourCtx.newJsonParser().setPrettyPrint(true).encodeBundleToString(bundle));
    final String encoded = ourCtx.newJsonParser().encodeBundleToString(bundle);
    assertEquals(bundleString, encoded);

  }



  @Test
  public void testEncodeBundle() throws InterruptedException
  {
    final Bundle b = new Bundle();

    final InstantDt pub = InstantDt.withCurrentTime();
    b.setPublished(pub);
    Thread.sleep(2);

    final Patient p1 = new Patient();
    p1.addName().addFamily("Family1");
    BundleEntry entry = b.addEntry();
    entry.getId().setValue("1");
    entry.setResource(p1);

    final Patient p2 = new Patient();
    p2.addName().addFamily("Family2");
    entry = b.addEntry();
    entry.getId().setValue("2");
    entry.setLinkAlternate(new StringDt("http://foo/bar"));
    entry.setResource(p2);

    final BundleEntry deletedEntry = b.addEntry();
    deletedEntry.setId(new IdDt("Patient/3"));
    final InstantDt nowDt = InstantDt.withCurrentTime();
    deletedEntry.setDeleted(nowDt);

    String bundleString = ourCtx.newJsonParser().setPrettyPrint(true).encodeBundleToString(b);
    ourLog.info(bundleString);

    final List<String> strings = new ArrayList<String>();
    strings.addAll(Arrays.asList("\"published\":\"" + pub.getValueAsString() + "\""));
    strings.addAll(Arrays.asList("\"id\":\"1\""));
    strings.addAll(Arrays.asList("\"id\":\"2\"", "\"rel\":\"alternate\"", "\"href\":\"http://foo/bar\""));
    strings.addAll(Arrays.asList("\"deleted\":\"" + nowDt.getValueAsString() + "\"", "\"id\":\"Patient/3\""));
    assertThat(bundleString, StringContainsInOrder.stringContainsInOrder(strings));

    b.getEntries().remove(2);
    bundleString = ourCtx.newJsonParser().setPrettyPrint(true).encodeBundleToString(b);
    assertThat(bundleString, not(containsString("deleted")));

  }



  @Test
  public void testSimpleBundleEncode() throws IOException
  {

    final String xmlString = IOUtils.toString(JsonParser.class.getResourceAsStream("/atom-document-large.xml"),
        Charset.forName("UTF-8"));
    final Bundle obs = ourCtx.newXmlParser().parseBundle(xmlString);

    final String encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeBundleToString(obs);
    ourLog.info(encoded);

  }



  @Test
  public void testSimpleParse() throws DataFormatException, IOException
  {

    final String msg = IOUtils.toString(XmlParser.class.getResourceAsStream("/example-patient-general.json"));
    final IParser p = ourCtx.newJsonParser();
    // ourLog.info("Reading in message: {}", msg);
    final Patient res = p.parseResource(Patient.class, msg);

    assertEquals(2, res.getUndeclaredExtensions().size());
    assertEquals(1, res.getUndeclaredModifierExtensions().size());

    final String encoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(res);
    ourLog.info(encoded);

  }



  // @Test
  public void testSimpleResourceEncode() throws IOException
  {

    final String xmlString = IOUtils.toString(JsonParser.class.getResourceAsStream("/example-patient-general.xml"),
        Charset.forName("UTF-8"));
    final Patient obs = ourCtx.newXmlParser().parseResource(Patient.class, xmlString);

    final List<ExtensionDt> undeclaredExtensions = obs.getContact().get(0).getName().getFamily().get(0)
        .getUndeclaredExtensions();
    final ExtensionDt undeclaredExtension = undeclaredExtensions.get(0);
    assertEquals("http://hl7.org/fhir/Profile/iso-21090#qualifier", undeclaredExtension.getUrl().getValue());

    ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToWriter(obs, new OutputStreamWriter(System.out));

    final IParser jsonParser = ourCtx.newJsonParser();
    final String encoded = jsonParser.encodeResourceToString(obs);
    ourLog.info(encoded);

    final String jsonString = IOUtils.toString(JsonParser.class.getResourceAsStream("/example-patient-general.json"));

    final JSON expected = JSONSerializer.toJSON(jsonString);
    final JSON actual = JSONSerializer.toJSON(encoded.trim());

    ourLog.info("Expected: {}", expected);
    ourLog.info("Actual  : {}", actual);
    assertEquals(expected.toString(), actual.toString());

  }



  /**
   * HAPI FHIR < 0.6 incorrectly used "resource" instead of "reference"
   */
  @Test
  public void testParseWithIncorrectReference() throws IOException
  {
    String jsonString = IOUtils.toString(JsonParser.class.getResourceAsStream("/example-patient-general.json"));
    jsonString = jsonString.replace("\"reference\"", "\"resource\"");
    final Patient parsed = ourCtx.newJsonParser().parseResource(Patient.class, jsonString);
    assertEquals("Organization/1", parsed.getManagingOrganization().getReference().getValue());
  }



  // @Test
  public void testSimpleResourceEncodeWithCustomType() throws IOException
  {

    final FhirContext fhirCtx = new FhirContext(MyObservationWithExtensions.class);
    final String xmlString = IOUtils.toString(JsonParser.class.getResourceAsStream("/example-patient-general.xml"),
        Charset.forName("UTF-8"));
    final MyObservationWithExtensions obs = fhirCtx.newXmlParser().parseResource(MyObservationWithExtensions.class,
        xmlString);

    assertEquals(0, obs.getAllUndeclaredExtensions().size());
    assertEquals("aaaa", obs.getExtAtt().getContentType().getValue());
    assertEquals("str1", obs.getMoreExt().getStr1().getValue());
    assertEquals("2011-01-02", obs.getModExt().getValueAsString());

    final List<ExtensionDt> undeclaredExtensions = obs.getContact().get(0).getName().getFamily().get(0)
        .getUndeclaredExtensions();
    final ExtensionDt undeclaredExtension = undeclaredExtensions.get(0);
    assertEquals("http://hl7.org/fhir/Profile/iso-21090#qualifier", undeclaredExtension.getUrl().getValue());

    final IParser jsonParser = fhirCtx.newJsonParser().setPrettyPrint(true);
    final String encoded = jsonParser.encodeResourceToString(obs);
    ourLog.info(encoded);

    final String jsonString = IOUtils.toString(JsonParser.class.getResourceAsStream("/example-patient-general.json"));

    final JSON expected = JSONSerializer.toJSON(jsonString);
    final JSON actual = JSONSerializer.toJSON(encoded.trim());

    ourLog.info("Expected: {}", expected);
    ourLog.info("Actual  : {}", actual);
    assertEquals(expected.toString(), actual.toString());

  }





  @ResourceDef(name = "Patient")
  public static class MyPatientWithOneDeclaredAddressExtension extends Patient
  {


    @Child(order = 0, name = "foo")
    @Extension(url = "urn:foo", definedLocally = true, isModifier = false)
    private AddressDt myFoo;



    public AddressDt getFoo()
    {
      return this.myFoo;
    }



    public void setFoo(final AddressDt theFoo)
    {
      this.myFoo = theFoo;
    }

  }





  @ResourceDef(name = "Patient")
  public static class MyPatientWithOneDeclaredExtension extends Patient
  {


    @Child(order = 0, name = "foo")
    @Extension(url = "urn:foo", definedLocally = true, isModifier = false)
    private ResourceReferenceDt myFoo;



    public ResourceReferenceDt getFoo()
    {
      return this.myFoo;
    }



    public void setFoo(final ResourceReferenceDt theFoo)
    {
      this.myFoo = theFoo;
    }

  }

}
