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
import static org.junit.Assert.fail;

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
import org.hl7.fhir.instance.model.Address;
import org.hl7.fhir.instance.model.Address.AddressUse;
import org.hl7.fhir.instance.model.Binary;
import org.hl7.fhir.instance.model.Bundle;
import org.hl7.fhir.instance.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.instance.model.Conformance;
import org.hl7.fhir.instance.model.DateTimeType;
import org.hl7.fhir.instance.model.DateType;
import org.hl7.fhir.instance.model.DecimalType;
import org.hl7.fhir.instance.model.DiagnosticReport;
import org.hl7.fhir.instance.model.Extension;
import org.hl7.fhir.instance.model.HumanName;
import org.hl7.fhir.instance.model.IBaseResource;
import org.hl7.fhir.instance.model.IPrimitiveType;
import org.hl7.fhir.instance.model.Identifier.IdentifierUse;
import org.hl7.fhir.instance.model.InstantType;
import org.hl7.fhir.instance.model.List_;
import org.hl7.fhir.instance.model.Narrative.NarrativeStatus;
import org.hl7.fhir.instance.model.Observation;
import org.hl7.fhir.instance.model.Organization;
import org.hl7.fhir.instance.model.Patient;
import org.hl7.fhir.instance.model.Profile;
import org.hl7.fhir.instance.model.Reference;
import org.hl7.fhir.instance.model.Specimen;
import org.hl7.fhir.instance.model.StringType;
import org.hl7.fhir.instance.model.ValueSet;
import org.hl7.fhir.instance.model.ValueSet.ConceptDefinitionComponent;
import org.hl7.fhir.instance.model.ValueSet.ValueSetDefineComponent;
import org.hl7.fhir.instance.model.annotations.Child;
import org.hl7.fhir.instance.model.annotations.ResourceDef;
import org.hl7.fhir.utilities.xhtml.XhtmlNode;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.base.composite.BaseNarrativeDt;
import ca.uhn.fhir.narrative.INarrativeGenerator;

public class JsonParserTest {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(JsonParserTest.class);
	private static FhirContext ourCtx;

	@Test
	public void testEncodeNarrativeBlockInBundle() throws Exception {
		Patient p = new Patient();
		p.addIdentifier().setSystem("foo").setValue("bar");
		p.getText().setStatus(NarrativeStatus.GENERATED);
		p.getText().setDivAsString("<div>hello</div>");

		Bundle b = new Bundle();
		b.setTotal(123);
		b.addEntry().setResource(p);

		String out = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(b);
		ourLog.info(out);
		assertThat(out, containsString("<div>hello</div>"));

		p.getText().setDivAsString("<xhtml:div xmlns:xhtml=\"http://www.w3.org/1999/xhtml\">hello</xhtml:div>");
		out = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(b);
		ourLog.info(out);
		// Backslashes need to be escaped because they are in a JSON value
		assertThat(out, containsString("<xhtml:div xmlns:xhtml=\\\"http://www.w3.org/1999/xhtml\\\">hello</xhtml:div>"));

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
		assertThat(enc, org.hamcrest.Matchers.stringContainsInOrder("{\"resourceType\":\"Patient\",",
				"\"extension\":[{\"url\":\"http://example.com/extensions#someext\",\"valueDateTime\":\"2011-01-02T11:13:15\"}",
				"{\"url\":\"http://example.com#parent\",\"extension\":[{\"url\":\"http://example.com#child\",\"valueString\":\"value1\"},{\"url\":\"http://example.com#child\",\"valueString\":\"value2\"}]}"
		));
		assertThat(enc, org.hamcrest.Matchers.stringContainsInOrder("\"modifierExtension\":[" +
				"{" +
				"\"url\":\"http://example.com/extensions#modext\"," +
				"\"valueDate\":\"1995-01-02\"" +
				"}" +
				"],"));
		assertThat(enc, containsString("\"_given\":[" +
				"{" +
				"\"extension\":[" +
				"{" +
				"\"url\":\"http://examples.com#givenext\"," +
				"\"valueString\":\"given\"" +
				"}" +
				"]" +
				"}," +
				"{" +
				"\"extension\":[" +
				"{" +
				"\"url\":\"http://examples.com#givenext_parent\"," +
				"\"extension\":[" +
				"{" +
				"\"url\":\"http://examples.com#givenext_child\"," +
				"\"valueString\":\"CHILD\"" +
				"}" +
				"]" +
				"}" +
				"]" +
				"}"));
		
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
		assertThat(encoded, containsString("\"reference\":\"Organization/65546\""));
		
		encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(patient);
		ourLog.info(encoded);
		assertThat(encoded, not(containsString("contained")));
		assertThat(encoded, containsString("\"reference\":\"Organization/65546\""));
	}
	
	
	@Test
	public void testEncodeIds() {
		Patient pt = new Patient();
		pt.addIdentifier().setSystem("sys").setValue( "val");
		
		List_ list = new List_();
		list.setId("listId");
		list.addEntry().setItem(new Reference(pt)).setDeleted(true);
		
		String enc = ourCtx.newJsonParser().encodeResourceToString(list);
		ourLog.info(enc);
		
		assertThat(enc, containsString("\"id\":\"1\""));
		
		List_ parsed = ourCtx.newJsonParser().parseResource(List_.class,enc);
		assertEquals(Patient.class, parsed.getEntry().get(0).getItem().getResource().getClass());

		enc = enc.replace("\"id\"", "\"_id\"");
		parsed = ourCtx.newJsonParser().parseResource(List_.class,enc);
		assertEquals(Patient.class, parsed.getEntry().get(0).getItem().getResource().getClass());
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
	public void testParseSingleQuotes() {
		try {
			ourCtx.newJsonParser().parseResource(Bundle.class, "{ 'resourceType': 'Bundle' }");
			fail();
		} catch (DataFormatException e) {
			// Should be an error message about how single quotes aren't valid JSON
			assertThat(e.getMessage(), containsString("double quote"));
		}
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
		assertEquals(encoded, "{\"resourceType\":\"Conformance\",\"_acceptUnknown\":[{\"extension\":[{\"url\":\"http://foo\",\"valueString\":\"AAA\"}]}]}");

		// Now with a value
		ourLog.info("---------------");

		c = new Conformance();
		c.getAcceptUnknownElement().setValue(true);
		c.getAcceptUnknownElement().addExtension().setUrl("http://foo").setValue( new StringType("AAA"));

		encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(c);
		ourLog.info(encoded);

		encoded = ourCtx.newJsonParser().setPrettyPrint(false).encodeResourceToString(c);
		ourLog.info(encoded);
		assertEquals(encoded, "{\"resourceType\":\"Conformance\",\"acceptUnknown\":true,\"_acceptUnknown\":[{\"extension\":[{\"url\":\"http://foo\",\"valueString\":\"AAA\"}]}]}");

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
	public void testEncodeBinaryResource() {

		Binary patient = new Binary();
		patient.setContentType("foo");
		patient.setContent(new byte[] { 1, 2, 3, 4 });

		String val = ourCtx.newJsonParser().encodeResourceToString(patient);
		assertEquals("{\"resourceType\":\"Binary\",\"contentType\":\"foo\",\"content\":\"AQIDBA==\"}", val);

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

		assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", value);
		assertTrue(div.getChildNodes().isEmpty());
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

		assertThat(str, stringContainsInOrder(Arrays.asList("\"text\":\"B\"", "\"text\":\"C\"", "\"text\":\"A\"")));

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
	public void testTagList() {

		//@formatter:off
		String tagListStr = "{\n" + 
				"  \"resourceType\" : \"TagList\", " + 
				"  \"category\" : [" + 
				"    { " + 
				"      \"term\" : \"term0\", " + 
				"      \"label\" : \"label0\", " + 
				"      \"scheme\" : \"scheme0\" " + 
				"    }," +
				"    { " + 
				"      \"term\" : \"term1\", " + 
				"      \"label\" : \"label1\", " + 
				"      \"scheme\" : null " + 
				"    }," +
				"    { " + 
				"      \"term\" : \"term2\", " + 
				"      \"label\" : \"label2\" " + 
				"    }" +
				"  ] " + 
				"}";
		//@formatter:on

		TagList tagList = new FhirContext().newJsonParser().parseTagList(tagListStr);
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
		String expected = "{" + 
				"\"resourceType\":\"TagList\"," + 
				"\"category\":[" + 
				"{" + 
				"\"term\":\"term0\"," + 
				"\"label\":\"label0\"," + 
				"\"scheme\":\"scheme0\"" + 
				"}," +
				"{" + 
				"\"term\":\"term1\"," + 
				"\"label\":\"label1\"" + 
				"}," +
				"{" + 
				"\"term\":\"term2\"," + 
				"\"label\":\"label2\"" + 
				"}" +
				"]" + 
				"}";
		//@formatter:on

		String encoded = new FhirContext().newJsonParser().encodeTagListToString(tagList);
		assertEquals(expected, encoded);

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

		assertThat(val, StringContains.containsString("\"category\":[{\"term\":\"term\",\"label\":\"label\",\"scheme\":\"scheme\"}]"));

		b = ourCtx.newJsonParser().parseResource(Bundle.class, val);
		assertEquals(1, b.getEntry().size());
		assertEquals(1, b.getEntry().get(0).getResource().getMeta().getTag().size());
		assertEquals("scheme", b.getEntry().get(0).getResource().getMeta().getTag().get(0).getSystem());
		assertEquals("term", b.getEntry().get(0).getResource().getMeta().getTag().get(0).getCode());
		assertEquals("label", b.getEntry().get(0).getResource().getMeta().getTag().get(0).getDisplay());
		assertNull(b.getEntry().get(0).getResource());

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
		assertThat(encoded, stringContainsInOrder(Arrays.asList("\"contained\"", "resourceType\":\"Organization", "id\":\"1\"")));
		assertThat(encoded, containsString("reference\":\"#1\""));
		
		encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(patient);
		ourLog.info(encoded);
		assertThat(encoded, stringContainsInOrder(Arrays.asList("\"contained\"", "resourceType\":\"Organization", "id\":\"1\"")));
		assertThat(encoded, containsString("reference\":\"#1\""));
	}
	
	@Test
	public void testEncodeContainedWithNarrativeIsSuppresed() throws Exception {
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
		assertThat(encoded, not(containsString("FOOBAR")));
		assertThat(encoded, (containsString("BARFOO")));
		
	}	
	
	@Test
	public void testEncodeContained() {
		IParser xmlParser = ourCtx.newJsonParser().setPrettyPrint(true);
		
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
		assertThat(encoded, stringContainsInOrder(Arrays.asList("\"contained\":[", "\"id\":\"1\"", "\"identifier\"", "\"reference\":\"#1\"")));
		
		// Create a bundle with just the patient resource
		Bundle b = new Bundle();
		b.addEntry().setResource(patient);
		
		// Encode the bundle
		encoded = xmlParser.encodeResourceToString(b);
		ourLog.info(encoded);
		assertThat(encoded, stringContainsInOrder(Arrays.asList("\"contained\":[", "\"id\":\"1\"", "\"identifier\"", "\"reference\":\"#1\"")));
		
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
		assertThat(encoded, stringContainsInOrder(Arrays.asList("\"contained\":[", "\"id\":\"1\"", "\"identifier\"", "\"reference\":\"#1\"")));
		assertThat(encoded, not(stringContainsInOrder(Arrays.asList("\"contained\":", "[", "\"contained\":"))));

		// And re-encode once more, with the references cleared
		patient.getContained().clear();
		patient.getManagingOrganization().setReference(null);
		encoded = xmlParser.encodeResourceToString(patient);
		ourLog.info(encoded);
		assertThat(encoded, stringContainsInOrder(Arrays.asList("\"contained\":[", "\"id\":\"1\"", "\"identifier\"", "\"reference\":\"#1\"")));
		assertThat(encoded, not(stringContainsInOrder(Arrays.asList("\"contained\":", "[", "\"contained\":"))));

		// And re-encode once more, with the references cleared and a manually set local ID
		patient.getContained().clear();
		patient.getManagingOrganization().setReference(null);
		patient.getManagingOrganization().getResource().setId(("#333"));
		encoded = xmlParser.encodeResourceToString(patient);
		ourLog.info(encoded);
		assertThat(encoded, stringContainsInOrder(Arrays.asList("\"contained\":[", "\"id\":\"333\"", "\"identifier\"", "\"reference\":\"#333\"")));
		assertThat(encoded, not(stringContainsInOrder(Arrays.asList("\"contained\":", "[", "\"contained\":"))));
		
	}
	
	

	
	@Test
	public void testEncodeContainedResources() throws IOException {

		String msg = IOUtils.toString(XmlParser.class.getResourceAsStream("/contained-diagnosticreport.xml"));
		IParser p = ourCtx.newXmlParser();
		DiagnosticReport res = p.parseResource(DiagnosticReport.class, msg);

		String encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(res);
		ourLog.info(encoded);

	}

	@Test
	public void testParseJsonProfile() throws IOException {
		parseAndEncode("/patient.profile.json");
		parseAndEncode("/alert.profile.json");
	}

	private void parseAndEncode(String name) throws IOException {
		String msg = IOUtils.toString(XmlParser.class.getResourceAsStream(name));
		ourLog.info(msg);

		IParser p = ourCtx.newJsonParser();
		Profile res = p.parseResource(Profile.class, msg);

		String encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(res);
		ourLog.info(encoded);

		JSON expected = JSONSerializer.toJSON(msg.trim());
		JSON actual = JSONSerializer.toJSON(encoded.trim());

		String exp = expected.toString().replace("\\r\\n", "\\n"); // .replace("&sect;", "§");
		String act = actual.toString().replace("\\r\\n", "\\n");

		ourLog.info("Expected: {}", exp);
		ourLog.info("Actual  : {}", act);

		assertEquals(exp, act);
	}

	@Test
	public void testEncodeContainedResourcesMore() throws Exception {

		DiagnosticReport rpt = new DiagnosticReport();
		Specimen spm = new Specimen();
		rpt.getText().setDivAsString("AAA");
		rpt.addSpecimen().setResource(spm);

		IParser p = new FhirContext(DiagnosticReport.class).newJsonParser().setPrettyPrint(true);
		String str = p.encodeResourceToString(rpt);

		ourLog.info(str);
		assertThat(str, StringContains.containsString("<div>AAA</div>"));
		String substring = "\"reference\":\"#";
		assertThat(str, StringContains.containsString(substring));

		int idx = str.indexOf(substring) + substring.length();
		int idx2 = str.indexOf('"', idx + 1);
		String id = str.substring(idx, idx2);
		assertThat(str, StringContains.containsString("\"id\":\"" + id + "\""));
		assertThat(str, IsNot.not(StringContains.containsString("<?xml version='1.0'?>")));

	}

	@Test
	public void testEncodeDeclaredExtensionWithAddressContent() {
		IParser parser = new FhirContext().newJsonParser();

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
		IParser parser = new FhirContext().newJsonParser();

		MyPatientWithOneDeclaredExtension patient = new MyPatientWithOneDeclaredExtension();
		patient.addAddress().setUse(AddressUse.HOME);
		patient.setFoo(new Reference("Organization/123"));

		String val = parser.encodeResourceToString(patient);
		ourLog.info(val);
		assertThat(val, StringContains.containsString("\"extension\":[{\"url\":\"urn:foo\",\"valueResource\":{\"reference\":\"Organization/123\"}}]"));

		MyPatientWithOneDeclaredExtension actual = parser.parseResource(MyPatientWithOneDeclaredExtension.class, val);
		assertEquals(AddressUse.HOME, patient.getAddress().get(0).getUse());
		Reference ref = actual.getFoo();
		assertEquals("Organization/123", ref.getReference());

	}

	@Test
	public void testEncodeExtensionOnEmptyElement() throws Exception {

		ValueSet valueSet = new ValueSet();
		valueSet.addTelecom().addExtension().setUrl("http://foo").setValue( new StringType("AAA"));

		String encoded = ourCtx.newJsonParser().encodeResourceToString(valueSet);
		assertThat(encoded, containsString("\"telecom\":[{\"extension\":[{\"url\":\"http://foo\",\"valueString\":\"AAA\"}]}"));

	}

	@Test
	public void testEncodeExt() throws Exception {

		ValueSet valueSet = new ValueSet();
		valueSet.setId("123456");

		ValueSetDefineComponent define = valueSet.getDefine();
		ConceptDefinitionComponent code = define.addConcept();
		code.setCode("someCode");
		code.setDisplay("someDisplay");
		code.addExtension().setUrl("urn:alt").setValue( new StringType("alt name"));


		String encoded = ourCtx.newJsonParser().encodeResourceToString(valueSet);
		ourLog.info(encoded);

		assertThat(encoded, (containsString("123456")));
		assertEquals(
				"{\"resourceType\":\"ValueSet\",\"id\":\"123456\",\"define\":{\"concept\":[{\"extension\":[{\"url\":\"urn:alt\",\"valueString\":\"alt name\"}],\"code\":\"someCode\",\"display\":\"someDisplay\"}]}}",
				encoded);

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
		assertThat(enc, containsString("<Patient xmlns=\"http://hl7.org/fhir\"><extension><url value=\"http://example.com/extensions#someext\"/><valueDateTime value=\"2011-01-02T11:13:15\"/></extension>"));
		assertThat(
				enc,
				containsString("<extension><extension><url value=\"http://example.com#child\"/><valueString value=\"value1\"/></extension><extension><url value=\"http://example.com#child\"/><valueString value=\"value1\"/></extension><url value=\"http://example.com#parent\"/></extension>"));
		assertThat(enc, containsString("<given value=\"Joe\"><extension><url value=\"http://examples.com#givenext\"/><valueString value=\"given\"/></extension></given>"));
		assertThat(enc, containsString("<given value=\"Shmoe\"><extension><extension><url value=\"http://examples.com#givenext\"/><valueString value=\"given\"/></extension><url value=\"http://examples.com#givenext_child\"/></extension></given>"));
	}


	@Test
	public void testEncodeExtensionWithResourceContent() {
		IParser parser = new FhirContext().newJsonParser();

		Patient patient = new Patient();
		patient.addAddress().setUse(AddressUse.HOME);
		patient.addExtension().setUrl("urn:foo").setValue( new Reference("Organization/123"));

		String val = parser.encodeResourceToString(patient);
		ourLog.info(val);
		assertThat(val, StringContains.containsString("\"extension\":[{\"url\":\"urn:foo\",\"valueResource\":{\"reference\":\"Organization/123\"}}]"));

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

		IParser p = new FhirContext(Observation.class).newJsonParser();

		try {
			p.encodeResourceToString(obs);
		} catch (DataFormatException e) {
			assertThat(e.getMessage(), StringContains.containsString("DecimalType"));
		}
	}

	@Test
	public void testEncodeResourceRef() throws DataFormatException {

		Patient patient = new Patient();
		patient.setManagingOrganization(new Reference());

		IParser p = new FhirContext().newJsonParser();
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
	public void testEncodeUndeclaredExtensionWithAddressContent() {
		IParser parser = new FhirContext().newJsonParser();

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
	public void testExtensionOnComposite() throws Exception {

		Patient patient = new Patient();

		HumanName name = patient.addName();
		name.addFamily("Shmoe");
		HumanName given = name.addGiven("Joe");
		Extension ext2 = new Extension().setUrl("http://examples.com#givenext").setValue( new StringType("Hello"));
		given.getExtension().add(ext2);
		String enc = new FhirContext().newJsonParser().encodeResourceToString(patient);
		ourLog.info(enc);
		assertEquals("{\"resourceType\":\"Patient\",\"name\":[{\"extension\":[{\"url\":\"http://examples.com#givenext\",\"valueString\":\"Hello\"}],\"family\":[\"Shmoe\"],\"given\":[\"Joe\"]}]}", enc);

		IParser newJsonParser = new FhirContext().newJsonParser();
		StringReader reader = new StringReader(enc);
		Patient parsed = newJsonParser.parseResource(Patient.class, reader);

		ourLog.info(new FhirContext().newXmlParser().setPrettyPrint(true).encodeResourceToString(parsed));

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
		String enc = new FhirContext().newJsonParser().encodeResourceToString(patient);
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

		Patient parsed = new FhirContext().newJsonParser().parseResource(Patient.class, new StringReader(enc));
		assertEquals(1, parsed.getName().get(0).getFamily().get(0).getExtension().size());
		Extension ext = parsed.getName().get(0).getFamily().get(0).getExtension().get(0);
		assertEquals("Hello", ((IPrimitiveType<?>)ext.getValue()).getValue());

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
		IParser p = context.newJsonParser();
		p.encodeResourceToWriter(patient, new OutputStreamWriter(System.out));
		String str = p.encodeResourceToString(patient);

		ourLog.info(str);

		assertThat(str, StringContains.containsString(",\"text\":{\"status\":\"generated\",\"div\":\"<div>help</div>\"},"));
	}

	@Test
	public void testParseBundle() throws DataFormatException, IOException {

		String msg = IOUtils.toString(XmlParser.class.getResourceAsStream("/atom-document-large.json"));
		IParser p = ourCtx.newJsonParser();
		Bundle bundle = p.parseResource(Bundle.class, msg);

		assertEquals(1, bundle.getMeta().getTag().size());
		assertEquals("http://scheme", bundle.getMeta().getTag().get(0).getSystem());
		assertEquals("http://term", bundle.getMeta().getTag().get(0).getCode());
		assertEquals("label", bundle.getMeta().getTag().get(0).getDisplay());

		String encoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(bundle);
		ourLog.info(encoded);

		assertEquals("http://fhir.healthintersections.com.au/open/DiagnosticReport/_search?_format=application/json+fhir&search-id=46d5f0e7-9240-4d4f-9f51-f8ac975c65&search-sort=_id", bundle
				.getLink().get(0).getUrl());
		assertEquals("urn:uuid:0b754ff9-03cf-4322-a119-15019af8a3", bundle.getId());

		BundleEntryComponent entry = bundle.getEntry().get(0);

		DiagnosticReport res = (DiagnosticReport) entry.getResource();
		assertEquals("Complete Blood Count", res.getName().getText());

	}

	@Test
	public void testParseBundleFromHI() throws DataFormatException, IOException {

		String msg = IOUtils.toString(XmlParser.class.getResourceAsStream("/bundle.json"));
		IParser p = ourCtx.newJsonParser();
		Bundle bundle = p.parseResource(Bundle.class, msg);

		String encoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(bundle);
		ourLog.info(encoded);

		BundleEntryComponent entry = bundle.getEntry().get(0);

		Patient res = (Patient) entry.getResource();
		assertEquals("444111234", res.getIdentifier().get(0).getValue());

		BundleEntryComponent deletedEntry = bundle.getEntry().get(3);
		assertEquals(true, deletedEntry.getResource().getMeta().getDeleted());
		assertEquals("2014-06-20T20:15:49Z", deletedEntry.getResource().getMeta().getLastUpdatedElement().getValueAsString());

	}


	@Test
	public void testParseWithContained() throws DataFormatException, IOException {

		String msg = IOUtils.toString(XmlParser.class.getResourceAsStream("/diagnostic-report.json"));
		IParser p = ourCtx.newJsonParser();
		// ourLog.info("Reading in message: {}", msg);
		DiagnosticReport res = p.parseResource(DiagnosticReport.class, msg);

		String encoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(res);
		ourLog.info(encoded);

		Reference reference = res.getResult().get(1);
		Observation obs = (Observation) reference.getResource();

		assertEquals("789-8", obs.getCode().getCoding().get(0).getCode());
	}

	@BeforeClass
	public static void beforeClass() {
		ourCtx = FhirContext.forDstu2Hl7Org();
	}


	@Test
	public void testEncodeBundle() throws InterruptedException {
		Bundle b = new Bundle();

		InstantType pub = InstantType.now();
		b.getMeta().setLastUpdatedElement(pub);
		Thread.sleep(2);

		Patient p1 = new Patient();
		p1.addName().addFamily("Family1");
		BundleEntryComponent entry = b.addEntry();
		entry.getIdElement().setValue("1");
		entry.setResource(p1);

		Patient p2 = new Patient();
		p2.addName().addFamily("Family2");
		entry = b.addEntry();
		entry.getIdElement().setValue("2");
		entry.setResource(p2);

		BundleEntryComponent deletedEntry = b.addEntry();
		Patient dp = new Patient();
		deletedEntry.setResource(dp);
		
		dp.setId(("3"));
		InstantType nowDt = InstantType.withCurrentTime();
		dp.getMeta().setDeleted(true);
		dp.getMeta().setLastUpdatedElement(nowDt);

		String bundleString = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(b);
		ourLog.info(bundleString);

		List<String> strings = new ArrayList<String>();
		strings.addAll(Arrays.asList("\"published\":\"" + pub.getValueAsString() + "\""));
		strings.addAll(Arrays.asList("\"id\":\"1\""));
		strings.addAll(Arrays.asList("\"id\":\"2\"", "\"rel\":\"alternate\"", "\"href\":\"http://foo/bar\""));
		strings.addAll(Arrays.asList("\"deleted\":\"" + nowDt.getValueAsString() + "\"", "\"id\":\"Patient/3\""));
		assertThat(bundleString, StringContainsInOrder.stringContainsInOrder(strings));

		b.getEntry().remove(2);
		bundleString = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(b);
		assertThat(bundleString, not(containsString("deleted")));

	}

	@Test
	public void testSimpleBundleEncode() throws IOException {

		String xmlString = IOUtils.toString(JsonParser.class.getResourceAsStream("/atom-document-large.xml"), Charset.forName("UTF-8"));
		Bundle obs = ourCtx.newXmlParser().parseResource(Bundle.class, xmlString);

		String encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs);
		ourLog.info(encoded);

	}

	@Test
	public void testSimpleParse() throws DataFormatException, IOException {

		String msg = IOUtils.toString(XmlParser.class.getResourceAsStream("/example-patient-general.json"));
		IParser p = ourCtx.newJsonParser();
		// ourLog.info("Reading in message: {}", msg);
		Patient res = p.parseResource(Patient.class, msg);

		assertEquals(2, res.getExtension().size());
		assertEquals(1, res.getModifierExtension().size());

		String encoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(res);
		ourLog.info(encoded);

	}

	@Test
	public void testSimpleResourceEncode() throws IOException {

		String xmlString = IOUtils.toString(JsonParser.class.getResourceAsStream("/example-patient-general.xml"), Charset.forName("UTF-8"));
		Patient obs = ourCtx.newXmlParser().parseResource(Patient.class, xmlString);

		List<Extension> undeclaredExtensions = obs.getContact().get(0).getName().getFamily().get(0).getExtension();
		Extension undeclaredExtension = undeclaredExtensions.get(0);
		assertEquals("http://hl7.org/fhir/Profile/iso-21090#qualifier", undeclaredExtension.getUrl());

		ourLog.info(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs));

		IParser jsonParser = ourCtx.newJsonParser();
		String encoded = jsonParser.encodeResourceToString(obs);
		ourLog.info(encoded);

		String jsonString = IOUtils.toString(JsonParser.class.getResourceAsStream("/example-patient-general.json"), Charset.forName("UTF-8"));

		JSON expected = JSONSerializer.toJSON(jsonString);
		JSON actual = JSONSerializer.toJSON(encoded.trim());

		// The encoded escapes quote marks using XML escaping instead of JSON escaping, which is probably nicer anyhow...
		String exp = expected.toString().replace("\\\"Jim\\\"", "&quot;Jim&quot;");
		String act = actual.toString();

		ourLog.info("Expected: {}", exp);
		ourLog.info("Actual  : {}", act);
		assertEquals(exp, act);

	}

	/**
	 * HAPI FHIR < 0.6 incorrectly used "resource" instead of "reference"
	 */
	@Test
	public void testParseWithIncorrectReference() throws IOException {
		String jsonString = IOUtils.toString(JsonParser.class.getResourceAsStream("/example-patient-general.json"));
		jsonString = jsonString.replace("\"reference\"", "\"resource\"");
		Patient parsed = ourCtx.newJsonParser().parseResource(Patient.class, jsonString);
		assertEquals("Organization/1", parsed.getManagingOrganization().getReference());
	}

	@Test
	public void testSimpleResourceEncodeWithCustomType() throws IOException {

		FhirContext fhirCtx = new FhirContext(MyObservationWithExtensions.class);
		String xmlString = IOUtils.toString(JsonParser.class.getResourceAsStream("/example-patient-general.xml"), Charset.forName("UTF-8"));
		MyObservationWithExtensions obs = fhirCtx.newXmlParser().parseResource(MyObservationWithExtensions.class, xmlString);

		assertEquals(0, obs.getExtension().size());
		assertEquals("aaaa", obs.getExtAtt().getContentType());
		assertEquals("str1", obs.getMoreExt().getStr1().getValue());
		assertEquals("2011-01-02", obs.getModExt().getValueAsString());

		List<Extension> undeclaredExtensions = obs.getContact().get(0).getName().getFamily().get(0).getExtension();
		Extension undeclaredExtension = undeclaredExtensions.get(0);
		assertEquals("http://hl7.org/fhir/Profile/iso-21090#qualifier", undeclaredExtension.getUrl());

		IParser jsonParser = fhirCtx.newJsonParser().setPrettyPrint(true);
		String encoded = jsonParser.encodeResourceToString(obs);
		ourLog.info(encoded);

		String jsonString = IOUtils.toString(JsonParser.class.getResourceAsStream("/example-patient-general.json"), Charset.forName("UTF-8"));

		JSON expected = JSONSerializer.toJSON(jsonString);
		JSON actual = JSONSerializer.toJSON(encoded.trim());

		// The encoded escapes quote marks using XML escaping instead of JSON escaping, which is probably nicer anyhow...
		String exp = expected.toString().replace("\\\"Jim\\\"", "&quot;Jim&quot;");
		String act = actual.toString();

		ourLog.info("Expected: {}", exp);
		ourLog.info("Actual  : {}", act);
		assertEquals(exp, act);

	}

	@ResourceDef(name = "Patient")
	public static class MyPatientWithOneDeclaredAddressExtension extends Patient {

		private static final long serialVersionUID = 1L;
		
		@Child(order = 0, name = "foo")
		@org.hl7.fhir.instance.model.annotations.Extension(url = "urn:foo", definedLocally = true, isModifier = false)
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
		@org.hl7.fhir.instance.model.annotations.Extension(url = "urn:foo", definedLocally = true, isModifier = false)
		private Reference myFoo;

		public Reference getFoo() {
			return myFoo;
		}

		public void setFoo(Reference theFoo) {
			myFoo = theFoo;
		}

	}

}
