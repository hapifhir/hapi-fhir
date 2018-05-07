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
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

import org.apache.commons.io.IOUtils;
import org.hamcrest.core.IsNot;
import org.hamcrest.core.StringContains;
import org.hamcrest.text.StringContainsInOrder;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.INarrative;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.BundleEntry;
import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.dstu.composite.AddressDt;
import ca.uhn.fhir.model.dstu.composite.HumanNameDt;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.resource.Binary;
import ca.uhn.fhir.model.dstu.resource.Conformance;
import ca.uhn.fhir.model.dstu.resource.Conformance.RestResource;
import ca.uhn.fhir.model.dstu.resource.DiagnosticReport;
import ca.uhn.fhir.model.dstu.resource.ListResource;
import ca.uhn.fhir.model.dstu.resource.Location;
import ca.uhn.fhir.model.dstu.resource.Observation;
import ca.uhn.fhir.model.dstu.resource.Organization;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.resource.Profile;
import ca.uhn.fhir.model.dstu.resource.Query;
import ca.uhn.fhir.model.dstu.resource.Questionnaire;
import ca.uhn.fhir.model.dstu.resource.Remittance;
import ca.uhn.fhir.model.dstu.resource.Specimen;
import ca.uhn.fhir.model.dstu.resource.ValueSet;
import ca.uhn.fhir.model.dstu.resource.ValueSet.Define;
import ca.uhn.fhir.model.dstu.resource.ValueSet.DefineConcept;
import ca.uhn.fhir.model.dstu.valueset.AddressUseEnum;
import ca.uhn.fhir.model.dstu.valueset.IdentifierUseEnum;
import ca.uhn.fhir.model.dstu.valueset.NarrativeStatusEnum;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.DateDt;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.DecimalDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.model.primitive.XhtmlDt;
import ca.uhn.fhir.narrative.INarrativeGenerator;
import ca.uhn.fhir.parser.IParserErrorHandler.IParseLocation;
import ca.uhn.fhir.parser.json.JsonLikeValue;
import ca.uhn.fhir.parser.json.JsonLikeValue.ScalarType;
import ca.uhn.fhir.parser.json.JsonLikeValue.ValueType;
import ca.uhn.fhir.util.TestUtil;
import net.sf.json.JSON;
import net.sf.json.JSONSerializer;

public class JsonParserTest {
	private static FhirContext ourCtx;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(JsonParserTest.class);

	private void parseAndEncode(String name) throws IOException {
		String msg = IOUtils.toString(XmlParser.class.getResourceAsStream(name), StandardCharsets.UTF_8);
		// ourLog.info(msg);

		msg = msg.replace("\"div\": \"<div>", "\"div\":\"<div xmlns=\\\"http://www.w3.org/1999/xhtml\\\">");
		
		IParser p = ourCtx.newJsonParser();
		Profile res = p.parseResource(Profile.class, msg);

		String encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(res);
		// ourLog.info(encoded);

		JSON expected = JSONSerializer.toJSON(msg.trim());
		JSON actual = JSONSerializer.toJSON(encoded.trim());

		String exp = expected.toString().replace("\\r\\n", "\\n"); // .replace("&sect;", "§");
		String act = actual.toString().replace("\\r\\n", "\\n");

		ourLog.info("Expected: {}", exp);
		ourLog.info("Actual  : {}", act);

		assertEquals(exp, act);
	}

	
	@Test
	public void testDecimalPrecisionPreserved() {
		String number = "52.3779939997090374535378485873776474764643249869328698436986235758587";

		Location loc = new Location();
		// This is far more precision than is realistic, but let's see if we preserve it
		loc.getPosition().setLatitude(new DecimalDt(number));

		String encoded = ourCtx.newJsonParser().encodeResourceToString(loc);
		Location parsed = ourCtx.newJsonParser().parseResource(Location.class, encoded);

		assertEquals(number, parsed.getPosition().getLatitude().getValueAsString());
	}


	@Test
	public void testDecimalPrecisionPreservedInResource() {
		Remittance obs = new Remittance();
		obs.addService().setRate(new DecimalDt("0.10000"));
		
		String output = ourCtx.newJsonParser().encodeResourceToString(obs);
		
		ourLog.info(output);
		assertEquals("{\"resourceType\":\"Remittance\",\"service\":[{\"rate\":0.10000}]}", output);
		
		obs = ourCtx.newJsonParser().parseResource(Remittance.class, output);
		assertEquals("0.10000", obs.getService().get(0).getRate().getValueAsString());
	}
	
	@Test
	public void testParseStringWithNewlineUnencoded() {
		Observation obs = new Observation();
		obs.setValue(new StringDt("line1\\nline2"));
		
		String output = ourCtx.newJsonParser().encodeResourceToString(obs);
		
		ourLog.info(output);
		assertEquals("{\"resourceType\":\"Observation\",\"valueString\":\"line1\\\\nline2\"}", output);
		
		obs = ourCtx.newJsonParser().parseResource(Observation.class, output);
		assertEquals("line1\\nline2", ((StringDt)obs.getValue()).getValue());
	}

	@Test
	public void testParseStringWithNewlineEncoded() {
		Observation obs = new Observation();
		obs.setValue(new StringDt("line1\nline2"));
		
		String output = ourCtx.newJsonParser().encodeResourceToString(obs);
		
		ourLog.info(output);
		assertEquals("{\"resourceType\":\"Observation\",\"valueString\":\"line1\\nline2\"}", output);
		
		obs = ourCtx.newJsonParser().parseResource(Observation.class, output);
		assertEquals("line1\nline2", ((StringDt)obs.getValue()).getValue());
	}
	
	@Test
	public void testEncodeAndParseExtensions() throws Exception {

		Patient patient = new Patient();
		patient.addIdentifier().setUse(IdentifierUseEnum.OFFICIAL).setSystem("urn:example").setValue("7000135");

		ExtensionDt ext = new ExtensionDt();
		ext.setUrl("http://example.com/extensions#someext");
		ext.setValue(new DateTimeDt("2011-01-02T11:13:15"));
		patient.addUndeclaredExtension(ext);

		ExtensionDt parent = new ExtensionDt().setUrl("http://example.com#parent");
		patient.addUndeclaredExtension(parent);
		ExtensionDt child1 = new ExtensionDt().setUrl("http://example.com#child").setValue(new StringDt("value1"));
		parent.addUndeclaredExtension(child1);
		ExtensionDt child2 = new ExtensionDt().setUrl("http://example.com#child").setValue(new StringDt("value2"));
		parent.addUndeclaredExtension(child2);

		ExtensionDt modExt = new ExtensionDt();
		modExt.setUrl("http://example.com/extensions#modext");
		modExt.setValue(new DateDt("1995-01-02"));
		modExt.setModifier(true);
		patient.addUndeclaredExtension(modExt);

		HumanNameDt name = patient.addName();
		name.addFamily("Blah");
		StringDt given = name.addGiven();
		given.setValue("Joe");
		ExtensionDt ext2 = new ExtensionDt().setUrl("http://examples.com#givenext").setValue(new StringDt("given"));
		given.addUndeclaredExtension(ext2);

		StringDt given2 = name.addGiven();
		given2.setValue("Shmoe");
		ExtensionDt given2ext = new ExtensionDt().setUrl("http://examples.com#givenext_parent");
		given2.addUndeclaredExtension(given2ext);
		given2ext.addUndeclaredExtension(new ExtensionDt().setUrl("http://examples.com#givenext_child").setValue(new StringDt("CHILD")));

		String output = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(patient);
		ourLog.info(output);

		String enc = ourCtx.newJsonParser().encodeResourceToString(patient);
		assertThat(enc, org.hamcrest.Matchers.stringContainsInOrder("{\"resourceType\":\"Patient\",",
				"\"extension\":[{\"url\":\"http://example.com/extensions#someext\",\"valueDateTime\":\"2011-01-02T11:13:15\"}",
				"{\"url\":\"http://example.com#parent\",\"extension\":[{\"url\":\"http://example.com#child\",\"valueString\":\"value1\"},{\"url\":\"http://example.com#child\",\"valueString\":\"value2\"}]}"));
		assertThat(enc, org.hamcrest.Matchers.stringContainsInOrder("\"modifierExtension\":[" + "{" + "\"url\":\"http://example.com/extensions#modext\"," + "\"valueDate\":\"1995-01-02\"" + "}" + "],"));
		assertThat(enc,
				containsString("\"_given\":[" + "{" + "\"extension\":[" + "{" + "\"url\":\"http://examples.com#givenext\"," + "\"valueString\":\"given\"" + "}" + "]" + "}," + "{" + "\"extension\":[" + "{"
						+ "\"url\":\"http://examples.com#givenext_parent\"," + "\"extension\":[" + "{" + "\"url\":\"http://examples.com#givenext_child\"," + "\"valueString\":\"CHILD\"" + "}" + "]" + "}"
						+ "]" + "}"));

		/*
		 * Now parse this back
		 */

		Patient parsed = ourCtx.newJsonParser().parseResource(Patient.class, enc);
		ext = parsed.getUndeclaredExtensions().get(0);
		assertEquals("http://example.com/extensions#someext", ext.getUrl());
		assertEquals("2011-01-02T11:13:15", ((DateTimeDt) ext.getValue()).getValueAsString());

		parent = patient.getUndeclaredExtensions().get(1);
		assertEquals("http://example.com#parent", parent.getUrl());
		assertNull(parent.getValue());
		child1 = parent.getExtension().get(0);
		assertEquals("http://example.com#child", child1.getUrl());
		assertEquals("value1", ((StringDt) child1.getValue()).getValueAsString());
		child2 = parent.getExtension().get(1);
		assertEquals("http://example.com#child", child2.getUrl());
		assertEquals("value2", ((StringDt) child2.getValue()).getValueAsString());

		modExt = parsed.getUndeclaredModifierExtensions().get(0);
		assertEquals("http://example.com/extensions#modext", modExt.getUrl());
		assertEquals("1995-01-02", ((DateDt) modExt.getValue()).getValueAsString());

		name = parsed.getName().get(0);

		ext2 = name.getGiven().get(0).getUndeclaredExtensions().get(0);
		assertEquals("http://examples.com#givenext", ext2.getUrl());
		assertEquals("given", ((StringDt) ext2.getValue()).getValueAsString());

		given2ext = name.getGiven().get(1).getUndeclaredExtensions().get(0);
		assertEquals("http://examples.com#givenext_parent", given2ext.getUrl());
		assertNull(given2ext.getValue());
		ExtensionDt given2ext2 = given2ext.getExtension().get(0);
		assertEquals("http://examples.com#givenext_child", given2ext2.getUrl());
		assertEquals("CHILD", ((StringDt) given2ext2.getValue()).getValue());

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

		InstantDt pub = InstantDt.withCurrentTime();
		Thread.sleep(2);

		Patient p1 = new Patient();
		p1.addName().addFamily("Family1");
		BundleEntry entry = b.addEntry();
		entry.getId().setValue("1");
		entry.setResource(p1);
		entry.getSummary().setValueAsString("this is the summary");

		Patient p2 = new Patient();
		p2.addName().addFamily("Family2");
		entry = b.addEntry();
		entry.getId().setValue("2");
		entry.setLinkAlternate(new StringDt("http://foo/bar"));
		entry.setResource(p2);

		BundleEntry deletedEntry = b.addEntry();
		deletedEntry.setId(new IdDt("Patient/3"));
		InstantDt nowDt = InstantDt.withCurrentTime();
		deletedEntry.setDeleted(nowDt);

		String bundleString = ourCtx.newJsonParser().setPrettyPrint(true).encodeBundleToString(b);
		ourLog.info(bundleString);

		List<String> strings = new ArrayList<String>();
		strings.addAll(Arrays.asList("\"id\": \"1\""));
		strings.addAll(Arrays.asList("this is the summary"));
		strings.addAll(Arrays.asList("\"id\": \"2\"", "\"rel\": \"alternate\"", "\"href\": \"http://foo/bar\""));
		strings.addAll(Arrays.asList("\"deleted\": \"" + nowDt.getValueAsString() + "\"", "\"id\": \"Patient/3\""));
		assertThat(bundleString, StringContainsInOrder.stringContainsInOrder(strings));

		b.getEntries().remove(2);
		bundleString = ourCtx.newJsonParser().setPrettyPrint(true).encodeBundleToString(b);
		assertThat(bundleString, not(containsString("deleted")));

	}

	@Test
	public void testEncodeBundleCategory() {

		Bundle b = new Bundle();

		BundleEntry e = b.addEntry();
		e.setResource(new Patient());
		b.addCategory("scheme", "term", "label");

		String val = ourCtx.newJsonParser().setPrettyPrint(false).encodeBundleToString(b);
		ourLog.info(val);

		assertThat(val, StringContains.containsString("\"category\":[{\"term\":\"term\",\"label\":\"label\",\"scheme\":\"scheme\"}]"));
		assertThat(val, not(containsString("text")));

		b = ourCtx.newJsonParser().parseBundle(val);
		assertEquals(1, b.getEntries().size());
		assertEquals(1, b.getCategories().size());
		assertEquals("term", b.getCategories().get(0).getTerm());
		assertEquals("label", b.getCategories().get(0).getLabel());
		assertEquals("scheme", b.getCategories().get(0).getScheme());
		assertNull(b.getEntries().get(0).getResource());

	}

	@Test
	public void testEncodeBundleEntryCategory() {

		Bundle b = new Bundle();
		BundleEntry e = b.addEntry();
		e.setResource(new Patient());
		e.addCategory("scheme", "term", "label");

		String val = ourCtx.newJsonParser().setPrettyPrint(false).encodeBundleToString(b);
		ourLog.info(val);

		assertThat(val, StringContains.containsString("\"category\":[{\"term\":\"term\",\"label\":\"label\",\"scheme\":\"scheme\"}]"));

		b = ourCtx.newJsonParser().parseBundle(val);
		assertEquals(1, b.getEntries().size());
		assertEquals(1, b.getEntries().get(0).getCategories().size());
		assertEquals("term", b.getEntries().get(0).getCategories().get(0).getTerm());
		assertEquals("label", b.getEntries().get(0).getCategories().get(0).getLabel());
		assertEquals("scheme", b.getEntries().get(0).getCategories().get(0).getScheme());
		assertNull(b.getEntries().get(0).getResource());

	}

	@Test
	public void testEncodeContained() {
		IParser xmlParser = ourCtx.newJsonParser().setPrettyPrint(true);

		// Create an organization, note that the organization does not have an ID
		Organization org = new Organization();
		org.getName().setValue("Contained Test Organization");

		// Create a patient
		Patient patient = new Patient();
		patient.setId("Patient/1333");
		patient.addIdentifier("urn:mrns", "253345");

		// Put the organization as a reference in the patient resource
		patient.getManagingOrganization().setResource(org);

		String encoded = xmlParser.encodeResourceToString(patient);
		ourLog.info(encoded);
		assertThat(encoded, stringContainsInOrder(Arrays.asList("\"contained\": [", "\"id\": \"1\"", "\"identifier\"", "\"reference\": \"#1\"")));

		// Create a bundle with just the patient resource
		List<IResource> resources = new ArrayList<IResource>();
		resources.add(patient);
		Bundle b = Bundle.withResources(resources, ourCtx, "http://example.com/base");

		// Encode the bundle
		encoded = xmlParser.encodeBundleToString(b);
		ourLog.info(encoded);
		assertThat(encoded, stringContainsInOrder(Arrays.asList("\"contained\": [", "\"id\": \"1\"", "\"identifier\"", "\"reference\": \"#1\"")));

		// Re-parse the bundle
		patient = (Patient) xmlParser.parseResource(xmlParser.encodeResourceToString(patient));
		assertEquals("#1", patient.getManagingOrganization().getReference().getValue());

		assertNotNull(patient.getManagingOrganization().getResource());
		org = (Organization) patient.getManagingOrganization().getResource();
		assertEquals("#1", org.getId().getValue());
		assertEquals("Contained Test Organization", org.getName().getValue());

		// And re-encode a second time
		encoded = xmlParser.encodeResourceToString(patient);
		ourLog.info(encoded);
		assertThat(encoded, stringContainsInOrder(Arrays.asList("\"contained\": [", "\"id\": \"1\"", "\"identifier\"", "\"reference\": \"#1\"")));
		assertThat(encoded, not(stringContainsInOrder(Arrays.asList("\"contained\":", "[", "\"contained\":"))));

		// And re-encode once more, with the references cleared
		patient.getContained().getContainedResources().clear();
		patient.getManagingOrganization().setReference((IdDt) null);
		encoded = xmlParser.encodeResourceToString(patient);
		ourLog.info(encoded);
		assertThat(encoded, stringContainsInOrder(Arrays.asList("\"contained\": [", "\"id\": \"1\"", "\"identifier\"", "\"reference\": \"#1\"")));
		assertThat(encoded, not(stringContainsInOrder(Arrays.asList("\"contained\":", "[", "\"contained\":"))));

		// And re-encode once more, with the references cleared and a manually set local ID
		patient.getContained().getContainedResources().clear();
		patient.getManagingOrganization().setReference((IdDt) null);
		patient.getManagingOrganization().getResource().setId(new IdDt("#333"));
		encoded = xmlParser.encodeResourceToString(patient);
		ourLog.info(encoded);
		assertThat(encoded, stringContainsInOrder(Arrays.asList("\"contained\": [", "\"id\": \"333\"", "\"identifier\"", "\"reference\": \"#333\"")));
		assertThat(encoded, not(stringContainsInOrder(Arrays.asList("\"contained\":", "[", "\"contained\":"))));

	}

	@Test
	public void testEncodeContained__() {
		// Create an organization
		Organization org = new Organization();
		org.getName().setValue("Contained Test Organization");

		// Create a patient
		Patient patient = new Patient();
		patient.setId("Patient/1333");
		patient.addIdentifier("urn:mrns", "253345");
		patient.getManagingOrganization().setResource(org);

		// Create a bundle with just the patient resource
		List<IResource> resources = new ArrayList<IResource>();
		resources.add(patient);
		Bundle b = Bundle.withResources(resources, ourCtx, "http://example.com/base");

		// Encode the buntdle
		String encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeBundleToString(b);
		ourLog.info(encoded);
		assertThat(encoded, stringContainsInOrder(Arrays.asList("\"contained\"", "resourceType\": \"Organization", "id\": \"1\"")));
		assertThat(encoded, containsString("reference\": \"#1\""));

		encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(patient);
		ourLog.info(encoded);
		assertThat(encoded, stringContainsInOrder(Arrays.asList("\"contained\"", "resourceType\": \"Organization", "id\": \"1\"")));
		assertThat(encoded, containsString("reference\": \"#1\""));
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
	public void testEncodeContainedResourcesMore() {

		DiagnosticReport rpt = new DiagnosticReport();
		Specimen spm = new Specimen();
		rpt.getText().setDiv("AAA");
		rpt.addSpecimen().setResource(spm);

		IParser p = new FhirContext(DiagnosticReport.class).newJsonParser().setPrettyPrint(true);
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
	public void testEncodeContainedWithNarrativeIsSuppresed() {
		IParser parser = ourCtx.newJsonParser().setPrettyPrint(true);

		// Create an organization, note that the organization does not have an ID
		Organization org = new Organization();
		org.getName().setValue("Contained Test Organization");
		org.getText().setDiv("<div>FOOBAR</div>");

		// Create a patient
		Patient patient = new Patient();
		patient.setId("Patient/1333");
		patient.addIdentifier("urn:mrns", "253345");
		patient.getText().setDiv("<div>BARFOO</div>");
		patient.getManagingOrganization().setResource(org);

		String encoded = parser.encodeResourceToString(patient);
		ourLog.info(encoded);
		assertThat(encoded, not(containsString("FOOBAR")));
		assertThat(encoded, (containsString("BARFOO")));

	}

	@Test
	public void testEncodeDeclaredExtensionWithAddressContent() {
		IParser parser = ourCtx.newJsonParser();

		MyPatientWithOneDeclaredAddressExtension patient = new MyPatientWithOneDeclaredAddressExtension();
		patient.addAddress().setUse(AddressUseEnum.HOME);
		patient.setFoo(new AddressDt().addLine("line1"));

		String val = parser.encodeResourceToString(patient);
		ourLog.info(val);
		assertThat(val, StringContains.containsString("\"extension\":[{\"url\":\"urn:foo\",\"valueAddress\":{\"line\":[\"line1\"]}}]"));

		MyPatientWithOneDeclaredAddressExtension actual = parser.parseResource(MyPatientWithOneDeclaredAddressExtension.class, val);
		assertEquals(AddressUseEnum.HOME, patient.getAddressFirstRep().getUse().getValueAsEnum());
		AddressDt ref = actual.getFoo();
		assertEquals("line1", ref.getLineFirstRep().getValue());

	}

	@Test
	public void testEncodeDeclaredExtensionWithResourceContent() {
		IParser parser = ourCtx.newJsonParser();

		MyPatientWithOneDeclaredExtension patient = new MyPatientWithOneDeclaredExtension();
		patient.addAddress().setUse(AddressUseEnum.HOME);
		patient.setFoo(new ResourceReferenceDt("Organization/123"));

		String val = parser.encodeResourceToString(patient);
		ourLog.info(val);
		assertThat(val, StringContains.containsString("\"extension\":[{\"url\":\"urn:foo\",\"valueResource\":{\"reference\":\"Organization/123\"}}]"));

		MyPatientWithOneDeclaredExtension actual = parser.parseResource(MyPatientWithOneDeclaredExtension.class, val);
		assertEquals(AddressUseEnum.HOME, patient.getAddressFirstRep().getUse().getValueAsEnum());
		ResourceReferenceDt ref = actual.getFoo();
		assertEquals("Organization/123", ref.getReference().getValue());

	}

	@Test
	public void testEncodeExt() throws Exception {

		ValueSet valueSet = new ValueSet();
		valueSet.setId("123456");

		Define define = valueSet.getDefine();
		DefineConcept code = define.addConcept();
		code.setCode("someCode");
		code.setDisplay("someDisplay");
		code.addUndeclaredExtension(false, "urn:alt", new StringDt("alt name"));

		IParser parser = ourCtx.newJsonParser();
		String encoded = parser.encodeResourceToString(valueSet);
		ourLog.info(encoded);

		assertThat(encoded, not(containsString("123456")));
		
		String expected = "{\"resourceType\":\"ValueSet\",\"define\":{\"concept\":[{\"extension\":[{\"url\":\"urn:alt\",\"valueString\":\"alt name\"}],\"code\":\"someCode\",\"display\":\"someDisplay\"}]}}";
		ourLog.info("Expected: {}", expected);
		ourLog.info("Actual  : {}", encoded);
		
		assertEquals(expected, encoded);

	}

	@Test
	public void testEncodeExtensionInCompositeElement() {

		Conformance c = new Conformance();
		c.addRest().getSecurity().addUndeclaredExtension(false, "http://foo", new StringDt("AAA"));

		String encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(c);
		ourLog.info(encoded);

		encoded = ourCtx.newJsonParser().setPrettyPrint(false).encodeResourceToString(c);
		ourLog.info(encoded);
		assertEquals(encoded, "{\"resourceType\":\"Conformance\",\"rest\":[{\"security\":{\"extension\":[{\"url\":\"http://foo\",\"valueString\":\"AAA\"}]}}]}");

	}

	@Test
	public void testEncodeExtensionInPrimitiveElement() {

		Conformance c = new Conformance();
		c.getAcceptUnknown().addUndeclaredExtension(false, "http://foo", new StringDt("AAA"));

		String encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(c);
		ourLog.info(encoded);

		encoded = ourCtx.newJsonParser().setPrettyPrint(false).encodeResourceToString(c);
		ourLog.info(encoded);
		assertEquals(encoded, "{\"resourceType\":\"Conformance\",\"_acceptUnknown\":{\"extension\":[{\"url\":\"http://foo\",\"valueString\":\"AAA\"}]}}");

		// Now with a value
		ourLog.info("---------------");

		c = new Conformance();
		c.getAcceptUnknown().setValue(true);
		c.getAcceptUnknown().addUndeclaredExtension(false, "http://foo", new StringDt("AAA"));

		encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(c);
		ourLog.info(encoded);

		encoded = ourCtx.newJsonParser().setPrettyPrint(false).encodeResourceToString(c);
		ourLog.info(encoded);
		assertEquals(encoded, "{\"resourceType\":\"Conformance\",\"acceptUnknown\":true,\"_acceptUnknown\":{\"extension\":[{\"url\":\"http://foo\",\"valueString\":\"AAA\"}]}}");

	}

	@Test
	public void testEncodeExtensionInResourceElement() {

		Conformance c = new Conformance();
		// c.addRest().getSecurity().addUndeclaredExtension(false, "http://foo", new StringDt("AAA"));
		c.addUndeclaredExtension(false, "http://foo", new StringDt("AAA"));

		String encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(c);
		ourLog.info(encoded);

		IParser setPrettyPrint = ourCtx.newJsonParser().setPrettyPrint(false);
		encoded = setPrettyPrint.encodeResourceToString(c);
		String expected = "{\"resourceType\":\"Conformance\",\"extension\":[{\"url\":\"http://foo\",\"valueString\":\"AAA\"}]}";
		
		ourLog.info("Expected: {}", expected);
		ourLog.info("Actual  : {}", encoded);
		
		assertEquals(expected, encoded);

	}

	@Test
	public void testEncodeExtensionOnEmptyElement() throws Exception {

		ValueSet valueSet = new ValueSet();
		valueSet.addTelecom().addUndeclaredExtension(false, "http://foo", new StringDt("AAA"));

		String encoded = ourCtx.newJsonParser().encodeResourceToString(valueSet);
		assertThat(encoded, containsString("\"telecom\":[{\"extension\":[{\"url\":\"http://foo\",\"valueString\":\"AAA\"}]}"));

	}

	@Test
	public void testEncodeExtensionWithResourceContent() {
		IParser parser = ourCtx.newJsonParser();

		Patient patient = new Patient();
		patient.addAddress().setUse(AddressUseEnum.HOME);
		patient.addUndeclaredExtension(false, "urn:foo", new ResourceReferenceDt("Organization/123"));

		String val = parser.encodeResourceToString(patient);
		ourLog.info(val);
		assertThat(val, StringContains.containsString("\"extension\":[{\"url\":\"urn:foo\",\"valueResource\":{\"reference\":\"Organization/123\"}}]"));

		Patient actual = parser.parseResource(Patient.class, val);
		assertEquals(AddressUseEnum.HOME, patient.getAddressFirstRep().getUse().getValueAsEnum());
		List<ExtensionDt> ext = actual.getUndeclaredExtensionsByUrl("urn:foo");
		assertEquals(1, ext.size());
		ResourceReferenceDt ref = (ResourceReferenceDt) ext.get(0).getValue();
		assertEquals("Organization/123", ref.getReference().getValue());

	}

	@Test
	public void testEncodeIds() {
		Patient pt = new Patient();
		pt.addIdentifier("sys", "val");

		ListResource list = new ListResource();
		list.setId("listId");
		list.addEntry().setItem(new ResourceReferenceDt(pt));

		String enc = ourCtx.newJsonParser().encodeResourceToString(list);
		ourLog.info(enc);

		assertThat(enc, containsString("\"id\":\"1\""));

		ListResource parsed = ourCtx.newJsonParser().parseResource(ListResource.class, enc);
		assertEquals(Patient.class, parsed.getEntryFirstRep().getItem().getResource().getClass());
	}

	@Test
	public void testEncodeInvalidChildGoodException() {
		Observation obs = new Observation();
		obs.setValue(new DecimalDt(112.22));

		IParser p = new FhirContext(Observation.class).newJsonParser();

		try {
			p.encodeResourceToString(obs);
		} catch (DataFormatException e) {
			assertThat(e.getMessage(), StringContains.containsString("DecimalDt"));
		}
	}

	@Test
	public void testEncodeNarrativeBlockInBundle() {
		Patient p = new Patient();
		p.addIdentifier("foo", "bar");
		p.getText().setStatus(NarrativeStatusEnum.GENERATED);
		p.getText().setDiv("<div>hello</div>");

		Bundle b = new Bundle();
		b.getTotalResults().setValue(123);
		b.addEntry().setResource(p);

		String out = ourCtx.newJsonParser().setPrettyPrint(true).encodeBundleToString(b);
		ourLog.info(out);
		assertThat(out, containsString("<div xmlns=\\\"http://www.w3.org/1999/xhtml\\\">hello</div>"));

		p.getText().setDiv("<xhtml:div xmlns:xhtml=\"http://www.w3.org/1999/xhtml\">hello</xhtml:div>");
		out = ourCtx.newJsonParser().setPrettyPrint(true).encodeBundleToString(b);
		ourLog.info(out);
		// Backslashes need to be escaped because they are in a JSON value
		assertThat(out, containsString("<xhtml:div xmlns:xhtml=\\\"http://www.w3.org/1999/xhtml\\\">hello</xhtml:div>"));

	}

	@Test
	public void testEncodeNonContained() {
		Organization org = new Organization();
		org.setId("Organization/65546");
		org.getName().setValue("Contained Test Organization");

		Patient patient = new Patient();
		patient.setId("Patient/1333");
		patient.addIdentifier("urn:mrns", "253345");
		patient.getManagingOrganization().setResource(org);

		Bundle b = Bundle.withResources(Collections.singletonList((IResource) patient), ourCtx, "http://foo");
		String encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeBundleToString(b);
		ourLog.info(encoded);
		assertThat(encoded, not(containsString("contained")));
		assertThat(encoded, containsString("\"reference\": \"Organization/65546\""));

		encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(patient);
		ourLog.info(encoded);
		assertThat(encoded, not(containsString("contained")));
		assertThat(encoded, containsString("\"reference\": \"Organization/65546\""));
	}

	@Test
	public void testEncodeOmitsVersionAndBase() {
		Patient p = new Patient();
		p.getManagingOrganization().setReference("http://example.com/base/Patient/1/_history/2");

		String enc;

		enc = ourCtx.newJsonParser().encodeResourceToString(p);
		ourLog.info(enc);
		assertThat(enc, containsString("\"http://example.com/base/Patient/1\""));

		enc = ourCtx.newJsonParser().setServerBaseUrl("http://example.com/base").encodeResourceToString(p);
		ourLog.info(enc);
		assertThat(enc, containsString("\"Patient/1\""));

		enc = ourCtx.newJsonParser().setServerBaseUrl("http://example.com/base2").encodeResourceToString(p);
		ourLog.info(enc);
		assertThat(enc, containsString("\"http://example.com/base/Patient/1\""));
	}

	@Test
	public void testEncodeQuery() {
		Query q = new Query();
		ExtensionDt parameter = q.addParameter();
		parameter.setUrl("http://hl7.org/fhir/query#_query").setValue(new StringDt("example"));

		String val = ourCtx.newJsonParser().encodeResourceToString(q);
		ourLog.info(val);

		//@formatter:off
		String expected = 
				"{" + 
					"\"resourceType\":\"Query\"," + 
					"\"parameter\":[" + 
						"{" + 
							"\"url\":\"http://hl7.org/fhir/query#_query\"," + 
							"\"valueString\":\"example\"" + 
						"}" + 
					"]" + 
				"}";
		//@formatter:on

		ourLog.info("Expect: {}", expected);
		ourLog.info("Got   : {}", val);
		assertEquals(expected, val);

	}

	@Test
	public void testEncodeResourceRef() throws DataFormatException {

		Patient patient = new Patient();
		patient.setManagingOrganization(new ResourceReferenceDt());

		IParser p = ourCtx.newJsonParser();
		String str = p.encodeResourceToString(patient);
		assertThat(str, IsNot.not(StringContains.containsString("managingOrganization")));

		patient.setManagingOrganization(new ResourceReferenceDt("Organization/123"));
		str = p.encodeResourceToString(patient);
		assertThat(str, StringContains.containsString("\"managingOrganization\":{\"reference\":\"Organization/123\"}"));

		Organization org = new Organization();
		org.addIdentifier().setSystem("foo").setValue("bar");
		patient.setManagingOrganization(new ResourceReferenceDt(org));
		str = p.encodeResourceToString(patient);
		assertThat(str, StringContains.containsString("\"contained\":[{\"resourceType\":\"Organization\""));

	}

	@Test
	public void testEncodeUndeclaredExtensionWithAddressContent() {
		IParser parser = ourCtx.newJsonParser();

		Patient patient = new Patient();
		patient.addAddress().setUse(AddressUseEnum.HOME);
		patient.addUndeclaredExtension(false, "urn:foo", new AddressDt().addLine("line1"));

		String val = parser.encodeResourceToString(patient);
		ourLog.info(val);
		assertThat(val, StringContains.containsString("\"extension\":[{\"url\":\"urn:foo\",\"valueAddress\":{\"line\":[\"line1\"]}}]"));

		MyPatientWithOneDeclaredAddressExtension actual = parser.parseResource(MyPatientWithOneDeclaredAddressExtension.class, val);
		assertEquals(AddressUseEnum.HOME, patient.getAddressFirstRep().getUse().getValueAsEnum());
		AddressDt ref = actual.getFoo();
		assertEquals("line1", ref.getLineFirstRep().getValue());

	}

	@Test
	public void testEncodingNullExtension() {
		Patient p = new Patient();
		ExtensionDt extension = new ExtensionDt(false, "http://foo#bar");
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
	public void testExtensionOnComposite() throws Exception {

		Patient patient = new Patient();

		HumanNameDt name = patient.addName();
		name.addFamily().setValue("Shmoe");
		HumanNameDt given = name.addGiven("Joe");
		ExtensionDt ext2 = new ExtensionDt(false, "http://examples.com#givenext", new StringDt("Hello"));
		given.addUndeclaredExtension(ext2);
		String enc = ourCtx.newJsonParser().encodeResourceToString(patient);
		ourLog.info(enc);
		assertEquals("{\"resourceType\":\"Patient\",\"name\":[{\"extension\":[{\"url\":\"http://examples.com#givenext\",\"valueString\":\"Hello\"}],\"family\":[\"Shmoe\"],\"given\":[\"Joe\"]}]}", enc);

		IParser newJsonParser = ourCtx.newJsonParser();
		StringReader reader = new StringReader(enc);
		Patient parsed = newJsonParser.parseResource(Patient.class, reader);

		ourLog.info(ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(parsed));

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
		assertEquals(1, parsed.getNameFirstRep().getFamilyFirstRep().getUndeclaredExtensionsByUrl("http://examples.com#givenext").size());
		ExtensionDt ext = parsed.getNameFirstRep().getFamilyFirstRep().getUndeclaredExtensionsByUrl("http://examples.com#givenext").get(0);
		assertEquals("Hello", ext.getValueAsPrimitive().getValue());

	}

	/**
	 * #65
	 */
	@Test
	public void testJsonPrimitiveWithExtensionEncoding() {
		String res = "{\"resourceType\":\"Questionnaire\",\"status\":\"draft\",\"authored\":\"2014-10-30T14:15:00\",\"subject\":{\"reference\":\"http://www.hl7.org/fhir/Patient/1\"},\"author\":{\"reference\":\"http://www.hl7.org/fhir/Practitioner/1\"},\"name\":{\"text\":\"WDHB Friends and Family Test\"},\"group\":{\"header\":\"Note: This is an anonymous survey, which means you cannot be identified.\",\"_header\":[{\"extension\":[{\"url\":\"http://hl7.org/fhir/Profile/iso-21090#language\",\"valueCode\":\"en\"},{\"url\":\"http://hl7.org/fhir/Profile/iso-21090#string-translation\",\"valueString\":\"è«\\u008bæ³¨æ\\u0084\\u008fï¼\\u009aè¿\\u0099æ\\u0098¯ä¸\\u0080ä¸ªå\\u008c¿å\\u0090\\u008dè°\\u0083æ\\u009f¥ï¼\\u008cè¢«è°\\u0083æ\\u009f¥äººå°\\u0086ä¸\\u008dä¼\\u009aè¢«è¯\\u0086å\\u0088«å\\u0087ºæ\\u009d¥ã\\u0080\\u0082\"},{\"url\":\"http://hl7.org/fhir/Profile/iso-21090#string-translation\",\"valueString\":\"ì\\u009dµëª\\u0085ì\\u009c¼ë¡\\u009c í\\u0095\\u0098ë\\u008a\\u0094 ì\\u0084¤ë¬¸ì¡°ì\\u0082¬ì\\u009d´ë¯\\u0080ë¡\\u009c ì\\u009e\\u0091ì\\u0084±ì\\u009e\\u0090ê°\\u0080 ë\\u0088\\u0084êµ¬ì\\u009d¸ì§\\u0080 ë°\\u009dí\\u0098\\u0080ì§\\u0080ì§\\u0080 ì\\u0095\\u008aì\\u008aµë\\u008b\\u0088ë\\u008b¤.\"}]}],\"question\":[{\"extension\":[{\"url\":\"http://hl7.org/fhir/questionnaire-extensions#answerFormat\",\"valueCode\":\"single-choice\"}],\"text\":\"Are you a patient?\",\"options\":{\"reference\":\"#question1\"}}]}}";
		Questionnaire parsed = ourCtx.newJsonParser().parseResource(Questionnaire.class, res);
		assertEquals("Note: This is an anonymous survey, which means you cannot be identified.", parsed.getGroup().getHeader().getValue());
		assertEquals(1, parsed.getGroup().getHeader().getUndeclaredExtensionsByUrl("http://hl7.org/fhir/Profile/iso-21090#language").size());

		String encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(parsed);
		ourLog.info(encoded);
		assertThat(encoded, containsString("\"_header\": {"));

	}

	@Test
	public void testNarrativeGeneration() throws DataFormatException, IOException {

		Patient patient = new Patient();
		patient.addName().addFamily("Smith");
		Organization org = new Organization();
		patient.getManagingOrganization().setResource(org);

		INarrativeGenerator gen = new INarrativeGenerator() {

			@Override
			public void generateNarrative(FhirContext theContext, IBaseResource theResource, INarrative theNarrative) {
				try {
					theNarrative.setDivAsString("<div>help</div>");
				} catch (Exception e) {
					throw new Error(e);
				}
				theNarrative.setStatusAsString("generated");
			}

		};

		FhirContext context = ourCtx;
		context.setNarrativeGenerator(gen);
		IParser p = context.newJsonParser();
		p.encodeResourceToWriter(patient, new OutputStreamWriter(System.out));
		String str = p.encodeResourceToString(patient);

		ourLog.info(str);

		assertThat(str, StringContains.containsString(",\"text\":{\"status\":\"generated\",\"div\":\"<div xmlns=\\\"http://www.w3.org/1999/xhtml\\\">help</div>\"},"));
	}

	@Before
	public void before() {
		ourCtx.setNarrativeGenerator(null);
	}

	@Test
	public void testNestedContainedResources() {

		Observation A = new Observation();
		A.getName().setText("A");

		Observation B = new Observation();
		B.getName().setText("B");
		A.addRelated().setTarget(new ResourceReferenceDt(B));

		Observation C = new Observation();
		C.getName().setText("C");
		B.addRelated().setTarget(new ResourceReferenceDt(C));

		String str = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(A);
		ourLog.info(str);

		assertThat(str, stringContainsInOrder(Arrays.asList("\"text\": \"B\"", "\"text\": \"C\"", "\"text\": \"A\"")));

		// Only one (outer) contained block
		int idx0 = str.indexOf("\"contained\"");
		int idx1 = str.indexOf("\"contained\"", idx0 + 1);

		assertNotEquals(-1, idx0);
		assertEquals(-1, idx1);

		Observation obs = ourCtx.newJsonParser().parseResource(Observation.class, str);
		assertEquals("A", obs.getName().getText().getValue());

		Observation obsB = (Observation) obs.getRelatedFirstRep().getTarget().getResource();
		assertEquals("B", obsB.getName().getText().getValue());

		Observation obsC = (Observation) obsB.getRelatedFirstRep().getTarget().getResource();
		assertEquals("C", obsC.getName().getText().getValue());

	}

	@Test
	public void testParseBinaryResource() {

		Binary val = ourCtx.newJsonParser().parseResource(Binary.class, "{\"resourceType\":\"Binary\",\"contentType\":\"foo\",\"content\":\"AQIDBA==\"}");
		assertEquals("foo", val.getContentType());
		assertArrayEquals(new byte[] { 1, 2, 3, 4 }, val.getContent());

	}

	@Test
	public void testParseBundle() throws DataFormatException, IOException {

		String msg = IOUtils.toString(XmlParser.class.getResourceAsStream("/atom-document-large.json"));
		IParser p = ourCtx.newJsonParser();
		Bundle bundle = p.parseBundle(msg);

		assertEquals(1, bundle.getCategories().size());
		assertEquals("http://scheme", bundle.getCategories().get(0).getScheme());
		assertEquals("http://term", bundle.getCategories().get(0).getTerm());
		assertEquals("label", bundle.getCategories().get(0).getLabel());

		String encoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeBundleToString(bundle);
		ourLog.info(encoded);

		assertEquals("http://fhir.healthintersections.com.au/open/DiagnosticReport/_search?_format=application/json+fhir&search-id=46d5f0e7-9240-4d4f-9f51-f8ac975c65&search-sort=_id",
				bundle.getLinkSelf().getValue());
		assertEquals("urn:uuid:0b754ff9-03cf-4322-a119-15019af8a3", bundle.getBundleId().getValue());

		BundleEntry entry = bundle.getEntries().get(0);
		assertEquals("http://fhir.healthintersections.com.au/open/DiagnosticReport/101", entry.getId().getValue());
		assertEquals("http://fhir.healthintersections.com.au/open/DiagnosticReport/101/_history/1", entry.getLinkSelf().getValue());
		assertEquals("2014-03-10T11:55:59Z", entry.getUpdated().getValueAsString());

		DiagnosticReport res = (DiagnosticReport) entry.getResource();
		assertEquals("Complete Blood Count", res.getName().getText().getValue());

		assertThat(entry.getSummary().getValueAsString(), containsString("CBC Report for Wile"));

	}

	@Test
	public void testTotalResultsInJsonBundle() {
		String json =
				"{" +
						"	\"resourceType\" : \"Bundle\"," +
						"   \"title\" : \"FHIR Atom Feed\"," +
						"	\"id\" : \"cb095f55-afb0-41e8-89d5-155259b2a032\"," +
						"	\"updated\" : \"2015-09-01T08:52:02.793-04:00\"," +
						"   \"author\": [" +
						"     {" +
						"       \"name\": \"author-name\", " +
						"       \"uri\": \"uri\" " +
						"     }" +
						"   ]," +
						"	\"link\" : [{" +
						"			\"rel\" : \"self\"," +
						"			\"href\" : \"http://fhirtest.uhn.ca/baseDstu1/Patient/_search?family=Perez\"" +
						"		}, {" +
						"			\"rel\" : \"next\"," +
						"			\"href\" : \"http://fhirtest.uhn.ca/baseDstu1?_getpages=c1db1094-cc46-49f1-ae69-4763ba52458b&_getpagesoffset=10&_count=10&_format=json&_pretty=true\"" +
						"		}, {" +
						"			\"rel\" : \"fhir-base\"," +
						"			\"href\" : \"http://fhirtest.uhn.ca/baseDstu1\"" +
						"		}" +
						"	]," +
						"	\"totalResults\" : \"1\"," +
						"	\"entry\" : [{" +
						"			\"title\" : \"GONZALO PEREZ (DNI20425239)\"," +
						"			\"id\" : \"http://fhirtest.uhn.ca/baseDstu1/Patient/34834\"," +
						"			\"link\" : [{" +
						"					\"rel\" : \"self\"," +
						"					\"href\" : \"http://fhirtest.uhn.ca/baseDstu1/Patient/34834/_history/1\"" +
						"				}" +
						"			]," +
						"			\"updated\" : \"2015-06-10T10:39:38.712-04:00\"," +
						"			\"published\" : \"2015-06-10T10:39:38.665-04:00\"," +
						"			\"content\" : {" +
						"				\"resourceType\" : \"Patient\"," +
						"				\"name\" : [{" +
						"						\"family\" : [" +
						"							\"PEREZ\"" +
						"						]," +
						"						\"given\" : [" +
						"							\"GONZALO\"" +
						"						]" +
						"					}" +
						"				]" +
						"			}" +
						"		}" +
						"	]" +
						"}";

		IParser jsonParser = ourCtx.newJsonParser();
		Bundle bundle = jsonParser.parseBundle(json);
		assertEquals("author-name", bundle.getAuthorName().getValue());
		assertEquals("uri", bundle.getAuthorUri().getValue());
		
	}

	@Test
	public void testParseBundleDeletedEntry() {

		//@formatter:off
		String bundleString = 
				"{" + 
					"\"resourceType\":\"Bundle\"," + 
					"\"totalResults\":\"1\"," + 
					"\"entry\":[" +
						"{" + 
							"\"deleted\":\"2012-05-29T23:45:32+00:00\"," + 
							"\"id\":\"http://fhir.furore.com/fhir/Patient/1\"," + 
							"\"link\":[" +
								"{" + 
									"\"rel\":\"self\"," + 
									"\"href\":\"http://fhir.furore.com/fhir/Patient/1/_history/2\"" + 
								"}" +
							"]" + 
						"}" +
					"]" + 
				"}";
		//@formatter:on

		Bundle bundle = ourCtx.newJsonParser().parseBundle(bundleString);
		BundleEntry entry = bundle.getEntries().get(0);
		assertEquals("2012-05-29T23:45:32+00:00", entry.getDeletedAt().getValueAsString());
		assertEquals("http://fhir.furore.com/fhir/Patient/1/_history/2", entry.getLinkSelf().getValue());
		assertEquals("1", entry.getResource().getId().getIdPart());
		assertEquals("2", entry.getResource().getId().getVersionIdPart());
		assertEquals(new InstantDt("2012-05-29T23:45:32+00:00"), entry.getResource().getResourceMetadata().get(ResourceMetadataKeyEnum.DELETED_AT));

		// Now encode

		ourLog.info(ourCtx.newJsonParser().setPrettyPrint(true).encodeBundleToString(bundle));
		String encoded = ourCtx.newJsonParser().encodeBundleToString(bundle);
		assertEquals(bundleString, encoded);

	}

	@Test
	public void testParseBundleFromHI() throws DataFormatException, IOException {

		String msg = IOUtils.toString(XmlParser.class.getResourceAsStream("/bundle.json"));
		IParser p = ourCtx.newJsonParser();
		Bundle bundle = p.parseBundle(msg);

		String encoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeBundleToString(bundle);
		ourLog.info(encoded);

		BundleEntry entry = bundle.getEntries().get(0);

		Patient res = (Patient) entry.getResource();
		assertEquals("444111234", res.getIdentifierFirstRep().getValue().getValue());

		BundleEntry deletedEntry = bundle.getEntries().get(3);
		assertEquals("2014-06-20T20:15:49Z", deletedEntry.getDeletedAt().getValueAsString());

	}

	@Test
	public void testParseEmptyNarrative() throws ConfigurationException, DataFormatException, IOException {
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
		XhtmlDt div = res.getText().getDiv();
		String value = div.getValueAsString();

		assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", value);
		assertEquals(null, div.getValue());
	}

	/**
	 * This sample has extra elements in <searchParam> that are not actually a part of the spec any more..
	 */
	@Test
	public void testParseFuroreMetadataWithExtraElements() throws IOException {
		String msg = IOUtils.toString(JsonParserTest.class.getResourceAsStream("/furore-conformance.json"));

		IParser p = ourCtx.newJsonParser();
		Conformance conf = p.parseResource(Conformance.class, msg);
		RestResource res = conf.getRestFirstRep().getResourceFirstRep();
		assertEquals("_id", res.getSearchParam().get(1).getName().getValue());
	}

	@Test
	public void testParseJsonProfile() throws IOException {
		parseAndEncode("/patient.profile.json");
		parseAndEncode("/alert.profile.json");
	}

	@Test
	public void testParseQuery() {
		String msg = "{\n" + "  \"resourceType\": \"Query\",\n" + "  \"text\": {\n" + "    \"status\": \"generated\",\n" + "    \"div\": \"<div>[Put rendering here]</div>\"\n" + "  },\n"
				+ "  \"identifier\": \"urn:uuid:42b253f5-fa17-40d0-8da5-44aeb4230376\",\n" + "  \"parameter\": [\n" + "    {\n" + "      \"url\": \"http://hl7.org/fhir/query#_query\",\n"
				+ "      \"valueString\": \"example\"\n" + "    }\n" + "  ]\n" + "}";
		Query query = ourCtx.newJsonParser().parseResource(Query.class, msg);

		assertEquals("urn:uuid:42b253f5-fa17-40d0-8da5-44aeb4230376", query.getIdentifier().getValueAsString());
		assertEquals("http://hl7.org/fhir/query#_query", query.getParameterFirstRep().getUrlAsString());
		assertEquals("example", query.getParameterFirstRep().getValueAsPrimitive().getValueAsString());

	}

	@Test
	public void testParseSingleQuotes() {
		assertEquals("1", ourCtx.newJsonParser().parseBundle("{ 'resourceType': 'Bundle', 'id':'1' }").getBundleId().getValue());
	}

	@Test
	public void testParseWithContained() throws DataFormatException, IOException {

		String msg = IOUtils.toString(XmlParser.class.getResourceAsStream("/diagnostic-report.json"));
		IParser p = ourCtx.newJsonParser();
		// ourLog.info("Reading in message: {}", msg);
		DiagnosticReport res = p.parseResource(DiagnosticReport.class, msg);

		String encoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(res);
		ourLog.info(encoded);

		ResourceReferenceDt reference = res.getResult().get(1);
		Observation obs = (Observation) reference.getResource();

		assertEquals("789-8", obs.getName().getCoding().get(0).getCode().getValue());
	}

	/**
	 * HAPI FHIR < 0.6 incorrectly used "resource" instead of "reference"
	 */
	@Test
	@Ignore
	public void testParseWithIncorrectReference() throws IOException {
		String jsonString = IOUtils.toString(JsonParser.class.getResourceAsStream("/example-patient-general.json"));
		jsonString = jsonString.replace("\"reference\"", "\"resource\"");
		Patient parsed = ourCtx.newJsonParser().parseResource(Patient.class, jsonString);
		assertEquals("Organization/1", parsed.getManagingOrganization().getReference().getValue());
	}

	@SuppressWarnings("unused")
	@Test
	public void testParseWithIncorrectResourceType() {
		String input = "{\"resourceType\":\"Patient\"}";
		try {
			IParser parser = ourCtx.newJsonParser();
			Organization o = parser.parseResource(Organization.class, input);
			fail();
		} catch (DataFormatException e) {
			assertThat(e.getMessage(), containsString("expected \"Organization\" but found \"Patient\""));
		}
	}

	@Test
	public void testSimpleBundleEncode() throws IOException {

		String xmlString = IOUtils.toString(JsonParser.class.getResourceAsStream("/atom-document-large.xml"), Charset.forName("UTF-8"));
		Bundle obs = ourCtx.newXmlParser().parseBundle(xmlString);

		String encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeBundleToString(obs);
		ourLog.info(encoded);

	}

	@Test
	public void testSimpleParse() throws DataFormatException, IOException {

		String msg = IOUtils.toString(XmlParser.class.getResourceAsStream("/example-patient-general.json"));
		IParser p = ourCtx.newJsonParser();
		// ourLog.info("Reading in message: {}", msg);
		Patient res = p.parseResource(Patient.class, msg);

		assertEquals(2, res.getUndeclaredExtensions().size());
		assertEquals(1, res.getUndeclaredModifierExtensions().size());

		String encoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(res);
		ourLog.info(encoded);

	}

	@Test
	public void testSimpleResourceEncode() throws IOException {

		String xmlString = IOUtils.toString(JsonParser.class.getResourceAsStream("/example-patient-general.xml"), Charset.forName("UTF-8"));
		IParser parser = ourCtx.newXmlParser();
		parser.setParserErrorHandler(new StrictErrorHandler());
		Patient obs = parser.parseResource(Patient.class, xmlString);

		List<ExtensionDt> undeclaredExtensions = obs.getContact().get(0).getName().getFamily().get(0).getUndeclaredExtensions();
		ExtensionDt undeclaredExtension = undeclaredExtensions.get(0);
		assertEquals("http://hl7.org/fhir/Profile/iso-21090#qualifier", undeclaredExtension.getUrl());
		assertEquals("VV", ((CodeDt)undeclaredExtension.getValue()).getValue());

		ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToWriter(obs, new OutputStreamWriter(System.out));

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
		assertEquals("\nExpected: " + exp + "\nActual  : " + act, exp, act);

	}

	@Test
	public void testSimpleResourceEncodeWithCustomType() throws IOException {

		FhirContext fhirCtx = new FhirContext(MyPatientWithExtensions.class);
		String xmlString = IOUtils.toString(JsonParser.class.getResourceAsStream("/example-patient-general.xml"), Charset.forName("UTF-8"));
		MyPatientWithExtensions obs = fhirCtx.newXmlParser().parseResource(MyPatientWithExtensions.class, xmlString);

		assertEquals(0, obs.getAllUndeclaredExtensions().size());
		assertEquals("aaaa", obs.getExtAtt().getContentType().getValue());
		assertEquals("str1", obs.getMoreExt().getStr1().getValue());
		assertEquals("2011-01-02", obs.getModExt().getValueAsString());

		List<ExtensionDt> undeclaredExtensions = obs.getContact().get(0).getName().getFamily().get(0).getUndeclaredExtensions();
		ExtensionDt undeclaredExtension = undeclaredExtensions.get(0);
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
		assertEquals("\nExpected: " + exp + "\nActual  : " + act, exp, act);
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

		TagList tagList = ourCtx.newJsonParser().parseTagList(tagListStr);
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

		String encoded = ourCtx.newJsonParser().encodeTagListToString(tagList);
		assertEquals(expected, encoded);

	}

	@Test
	public void testCustomUrlExtension() {
		final String expected = "{\"resourceType\":\"Patient\",\"extension\":[{\"url\":\"http://www.example.com/petname\",\"valueString\":\"myName\"}]}";

		final MyPatientWithCustomUrlExtension patient = new MyPatientWithCustomUrlExtension();
		patient.setPetName(new StringDt("myName"));

		final IParser jsonParser = ourCtx.newJsonParser();
		jsonParser.setServerBaseUrl("http://www.example.com");

		final String parsedPatient = jsonParser.encodeResourceToString(patient);
		System.out.println(parsedPatient);
		assertEquals(expected, parsedPatient);

		// Parse with string
		MyPatientWithCustomUrlExtension newPatient = jsonParser.parseResource(MyPatientWithCustomUrlExtension.class, parsedPatient);
		assertEquals("myName", newPatient.getPetName().getValue());

		// Parse with stream
		newPatient = jsonParser.parseResource(MyPatientWithCustomUrlExtension.class, new StringReader(parsedPatient));
		assertEquals("myName", newPatient.getPetName().getValue());

		//Check no NPE if base server not configure
		newPatient = ourCtx.newJsonParser().parseResource(MyPatientWithCustomUrlExtension.class, new StringReader(parsedPatient));
		assertNull("myName", newPatient.getPetName());
		assertEquals("myName", ((StringDt) newPatient.getUndeclaredExtensionsByUrl("http://www.example.com/petname").get(0).getValue()).getValue());
	}

	@Test
	public void testCustomUrlExtensioninBundle() {
		final String expected = "{\"resourceType\":\"Bundle\",\"entry\":[{\"id\":null,\"content\":{\"resourceType\":\"Patient\",\"extension\":[{\"url\":\"http://www.example.com/petname\",\"valueString\":\"myName\"}]}}]}";

		final MyPatientWithCustomUrlExtension patient = new MyPatientWithCustomUrlExtension();
		patient.setPetName(new StringDt("myName"));

		final Bundle bundle = new Bundle();
		final BundleEntry entry = new BundleEntry();
		entry.setResource(patient);
		bundle.addEntry(entry);

		final IParser jsonParser = ourCtx.newJsonParser();
		jsonParser.setServerBaseUrl("http://www.example.com");

		final String parsedBundle = jsonParser.encodeBundleToString(bundle);
		System.out.println(parsedBundle);
		assertEquals(expected, parsedBundle);

		// Parse with string
		Bundle newBundle = jsonParser.parseBundle(parsedBundle);
		assertNotNull(newBundle);
		assertEquals(1, newBundle.getEntries().size());
		Patient newPatient = (Patient) newBundle.getEntries().get(0).getResource();
		assertEquals("myName", ((StringDt) newPatient.getUndeclaredExtensionsByUrl("http://www.example.com/petname").get(0).getValue()).getValue());

		// Parse with stream
		newBundle = jsonParser.parseBundle(new StringReader(parsedBundle));
		assertNotNull(newBundle);
		assertEquals(1, newBundle.getEntries().size());
		newPatient = (Patient) newBundle.getEntries().get(0).getResource();
		assertEquals("myName", ((StringDt) newPatient.getUndeclaredExtensionsByUrl("http://www.example.com/petname").get(0).getValue()).getValue());

	}
  
	@BeforeClass
	public static void beforeClass() {
		ourCtx = FhirContext.forDstu1();
	}

	@ResourceDef(name = "Patient")
	public static class MyPatientWithOneDeclaredAddressExtension extends Patient {

		@Child(order = 0, name = "foo")
		@Extension(url = "urn:foo", definedLocally = true, isModifier = false)
		private AddressDt myFoo;

		public AddressDt getFoo() {
			return myFoo;
		}

		public void setFoo(AddressDt theFoo) {
			myFoo = theFoo;
		}

	}

	@ResourceDef(name = "Patient")
	public static class MyPatientWithOneDeclaredExtension extends Patient {

		@Child(order = 0, name = "foo")
		@Extension(url = "urn:foo", definedLocally = true, isModifier = false)
		private ResourceReferenceDt myFoo;

		public ResourceReferenceDt getFoo() {
			return myFoo;
		}

		public void setFoo(ResourceReferenceDt theFoo) {
			myFoo = theFoo;
		}

	}


	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
