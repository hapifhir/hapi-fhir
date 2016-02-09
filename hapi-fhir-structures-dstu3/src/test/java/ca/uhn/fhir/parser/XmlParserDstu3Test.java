package ca.uhn.fhir.parser;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.stringContainsInOrder;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.hamcrest.collection.IsEmptyCollection;
import org.hamcrest.core.StringContains;
import org.hamcrest.text.StringContainsInOrder;
import org.hl7.fhir.dstu3.model.AllergyIntolerance;
import org.hl7.fhir.dstu3.model.Annotation;
import org.hl7.fhir.dstu3.model.Binary;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.CodeType;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.Composition;
import org.hl7.fhir.dstu3.model.ConceptMap;
import org.hl7.fhir.dstu3.model.DataElement;
import org.hl7.fhir.dstu3.model.DateTimeType;
import org.hl7.fhir.dstu3.model.DateType;
import org.hl7.fhir.dstu3.model.DiagnosticReport;
import org.hl7.fhir.dstu3.model.DocumentManifest;
import org.hl7.fhir.dstu3.model.Duration;
import org.hl7.fhir.dstu3.model.ElementDefinition;
import org.hl7.fhir.dstu3.model.Encounter;
import org.hl7.fhir.dstu3.model.Extension;
import org.hl7.fhir.dstu3.model.HumanName;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Identifier;
import org.hl7.fhir.dstu3.model.InstantType;
import org.hl7.fhir.dstu3.model.Medication;
import org.hl7.fhir.dstu3.model.MedicationOrder;
import org.hl7.fhir.dstu3.model.MedicationStatement;
import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.dstu3.model.Organization;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.Quantity;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.SimpleQuantity;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.dstu3.model.UriType;
import org.hl7.fhir.dstu3.model.ValueSet;
import org.hl7.fhir.dstu3.model.Address.AddressUse;
import org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.dstu3.model.Bundle.BundleLinkComponent;
import org.hl7.fhir.dstu3.model.Bundle.BundleType;
import org.hl7.fhir.dstu3.model.ContactPoint.ContactPointSystem;
import org.hl7.fhir.dstu3.model.ElementDefinition.ElementDefinitionBindingComponent;
import org.hl7.fhir.dstu3.model.Enumerations.AdministrativeGender;
import org.hl7.fhir.dstu3.model.Enumerations.DocumentReferenceStatus;
import org.hl7.fhir.dstu3.model.HumanName.NameUse;
import org.hl7.fhir.dstu3.model.Identifier.IdentifierUse;
import org.hl7.fhir.dstu3.model.Observation.ObservationRelationshipType;
import org.hl7.fhir.dstu3.model.Observation.ObservationStatus;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.narrative.DefaultThymeleafNarrativeGenerator;
import ca.uhn.fhir.parser.IParserErrorHandler.IParseLocation;
import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.rest.server.Constants;

public class XmlParserDstu3Test {
	private static final FhirContext ourCtx = FhirContext.forDstu3();
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(XmlParserDstu3Test.class);

	@After
	public void after() {
		ourCtx.setNarrativeGenerator(null);
	}

	@Test
	public void testParseAndEncodeCommentsOnExtensions() {
		//@formatter:off
		String input = 
				"<Patient xmlns=\"http://hl7.org/fhir\">\n" + 
				"  <!-- comment 1 -->\n" +
				"  <id value=\"someid\"/>\n" + 
				"  <!-- comment 2 -->\n" +
				"  <extension url=\"urn:patientext:att\">\n" + 
				"    <!-- comment 3 -->\n" +
				"    <valueAttachment>\n" + 
				"      <!-- comment 4 -->\n" +
				"      <contentType value=\"aaaa\"/>\n" + 
				"      <data value=\"AAAA\"/>\n" + 
				"      <!-- comment 5 -->\n" +
				"    </valueAttachment>\n" + 
				"    <!-- comment 6 -->\n" +
				"  </extension>\n" + 
				"  <!-- comment 7 -->\n" +
				"</Patient>";
		
		Patient pat = ourCtx.newXmlParser().parseResource(Patient.class, input);
		String output = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(pat);
		ourLog.info(output);
		
		assertThat(output, stringContainsInOrder(
			"<Patient xmlns=\"http://hl7.org/fhir\">", 
			"  <!-- comment 1 -->",
			"  <id value=\"someid\"/>", 
			"  <!-- comment 2 -->",
			"  <extension url=\"urn:patientext:att\">", 
			"    <!-- comment 3 -->",
			"    <valueAttachment>", 
			"      <!-- comment 4 -->",
			"      <contentType value=\"aaaa\"/>", 
			"      <data value=\"AAAA\"/>", 
			"      <!-- comment 5 -->",
			"    </valueAttachment>", 
			"    <!-- comment 6 -->",
			"  </extension>", 
			"  <!-- comment 7 -->",
			"</Patient>"
		));
		
		output = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(pat);
		ourLog.info(output);

		assertThat(output, stringContainsInOrder(
			"{", 
			"  \"resourceType\":\"Patient\",", 
			"  \"id\":\"someid\",", 
			"  \"_id\":{", 
			"    \"fhir_comments\":[", 
			"      \" comment 1 \"", 
			"    ]", 
			"  },", 
			"  \"extension\":[", 
			"    {", 
			"      \"fhir_comments\":[", 
			"        \" comment 2 \",", 
			"        \" comment 7 \"", 
			"      ],", 
			"      \"url\":\"urn:patientext:att\",", 
			"      \"valueAttachment\":{", 
			"        \"fhir_comments\":[", 
			"          \" comment 3 \",", 
			"          \" comment 6 \"", 
			"        ],", 
			"        \"contentType\":\"aaaa\",", 
			"        \"_contentType\":{", 
			"          \"fhir_comments\":[", 
			"            \" comment 4 \"", 
			"          ]", 
			"        },", 
			"        \"data\":\"AAAA\",", 
			"        \"_data\":{", 
			"          \"fhir_comments\":[", 
			"            \" comment 5 \"", 
			"          ]", 
			"        }", 
			"      }", 
			"    }", 
			"  ]", 
			"}" 
		));
		
		//@formatter:on
	}
	
	
	@Test
	public void testBundleWithBinary() {
		//@formatter:off
		String bundle = "<Bundle xmlns=\"http://hl7.org/fhir\">\n" + 
			"   <meta/>\n" + 
			"   <base value=\"http://localhost:52788\"/>\n" + 
			"   <total value=\"1\"/>\n" + 
			"   <link>\n" + 
			"      <relation value=\"self\"/>\n" + 
			"      <url value=\"http://localhost:52788/Binary?_pretty=true\"/>\n" + 
			"   </link>\n" + 
			"   <entry>\n" + 
			"      <resource>\n" + 
			"         <Binary xmlns=\"http://hl7.org/fhir\">\n" + 
			"            <id value=\"1\"/>\n" + 
			"            <meta/>\n" + 
			"            <contentType value=\"text/plain\"/>\n" + 
			"            <content value=\"AQIDBA==\"/>\n" + 
			"         </Binary>\n" + 
			"      </resource>\n" + 
			"   </entry>\n" + 
			"</Bundle>";
		//@formatter:on

		Bundle b = ourCtx.newXmlParser().parseResource(Bundle.class, bundle);
		assertEquals(1, b.getEntry().size());

		Binary bin = (Binary) b.getEntry().get(0).getResource();
		assertArrayEquals(new byte[] { 1, 2, 3, 4 }, bin.getContent());

	}
	
	@Test
	public void testContainedResourceInExtensionUndeclared() {
		Patient p = new Patient();
		p.addName().addFamily("PATIENT");

		Organization o = new Organization();
		o.setName("ORG");
		p.addExtension(new Extension("urn:foo", new Reference(o)));

		String str = ourCtx.newXmlParser().encodeResourceToString(p);
		ourLog.info(str);

		p = ourCtx.newXmlParser().parseResource(Patient.class, str);
		assertEquals("PATIENT", p.getName().get(0).getFamily().get(0).getValue());

		List<Extension> exts = p.getExtensionsByUrl("urn:foo");
		assertEquals(1, exts.size());
		Reference rr = (Reference) exts.get(0).getValue();
		o = (Organization) rr.getResource();
		assertEquals("ORG", o.getName());
	}
	
	@Test
	public void testDuration() {
		Encounter enc = new Encounter();
		Duration duration = new Duration();
		duration.setUnit("day").setValue(123L);
		enc.setLength(duration);

		String str = ourCtx.newXmlParser().encodeResourceToString(enc);
		ourLog.info(str);

		assertThat(str, not(containsString("meta")));
		assertThat(str, containsString("<length><value value=\"123\"/><unit value=\"day\"/></length>"));
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
		assertEquals("#1", patient.getManagingOrganization().getReference());

		assertNotNull(patient.getManagingOrganization().getResource());
		org = (Organization) patient.getManagingOrganization().getResource();
		assertEquals("#1", org.getIdElement().getValue());
		assertEquals("Contained Test Organization", org.getName());

		// And re-encode a second time
		encoded = xmlParser.encodeResourceToString(patient);
		ourLog.info(encoded);
		assertThat(encoded, stringContainsInOrder(Arrays.asList("<contained>", "<Organization ", "<id value=\"1\"/>", "</Organization", "</contained>", "<reference value=\"#1\"/>")));
		assertThat(encoded, not(stringContainsInOrder(Arrays.asList("<contained>", "<Org", "<contained>"))));
		assertThat(encoded, containsString("<reference value=\"#1\"/>"));

		// And re-encode once more, with the references cleared
		patient.getContained().clear();
		patient.getManagingOrganization().setReference((String) null);
		encoded = xmlParser.encodeResourceToString(patient);
		ourLog.info(encoded);
		assertThat(encoded, stringContainsInOrder(Arrays.asList("<contained>", "<Organization ", "<id value=\"1\"/>", "</Organization", "</contained>", "<reference value=\"#1\"/>")));
		assertThat(encoded, not(stringContainsInOrder(Arrays.asList("<contained>", "<Org", "<contained>"))));
		assertThat(encoded, containsString("<reference value=\"#1\"/>"));

		// And re-encode once more, with the references cleared and a manually set local ID
		patient.getContained().clear();
		patient.getManagingOrganization().setReference((String) null);
		patient.getManagingOrganization().getResource().setId(("#333"));
		encoded = xmlParser.encodeResourceToString(patient);
		ourLog.info(encoded);
		assertThat(encoded, stringContainsInOrder(Arrays.asList("<contained>", "<Organization ", "<id value=\"333\"/>", "</Organization", "</contained>", "<reference value=\"#333\"/>")));
		assertThat(encoded, not(stringContainsInOrder(Arrays.asList("<contained>", "<Org", "<contained>"))));

	}

	@Test
	public void testEncodeAndParseExtensionOnCode() {
		Organization o = new Organization();
		o.setName("ORG");
		o.addExtension(new Extension("urn:foo", new CodeType("acode")));

		String str = ourCtx.newXmlParser().encodeResourceToString(o);
		ourLog.info(str);
		assertThat(str, containsString("<valueCode value=\"acode\"/>"));

		o = ourCtx.newXmlParser().parseResource(Organization.class, str);

		List<Extension> exts = o.getExtensionsByUrl("urn:foo");
		assertEquals(1, exts.size());
		CodeType code = (CodeType) exts.get(0).getValue();
		assertEquals("acode", code.getValue());

	}

	
	@Test
	public void testEncodeAndParseExtensionOnReference() {
		DataElement de = new DataElement();
		ElementDefinitionBindingComponent b = de.addElement().getBinding();
		b.setDescription("BINDING");

		Organization o = new Organization();
		o.setName("ORG");
		b.addExtension(new Extension("urn:foo", new Reference(o)));

		String str = ourCtx.newXmlParser().encodeResourceToString(de);
		ourLog.info(str);

		de = ourCtx.newXmlParser().parseResource(DataElement.class, str);
		b = de.getElement().get(0).getBinding();
		assertEquals("BINDING", b.getDescription());

		List<Extension> exts = b.getExtensionsByUrl("urn:foo");
		assertEquals(1, exts.size());
		Reference rr = (Reference) exts.get(0).getValue();
		o = (Organization) rr.getResource();
		assertEquals("ORG", o.getName());

	}
	
	
	@Test
	public void testEncodeAndParseExtensions() throws Exception {

		Patient patient = new Patient();
		patient.addIdentifier().setUse(IdentifierUse.OFFICIAL).setSystem("urn:example").setValue("7000135");

		Extension ext = new Extension();
		ext.setUrl("http://example.com/extensions#someext");
		ext.setValue(new DateTimeType("2011-01-02T11:13:15"));
		patient.addExtension(ext);

		Extension parent = new Extension().setUrl("http://example.com#parent");
		patient.addExtension(parent);
		Extension child1 = new Extension().setUrl("http://example.com#child").setValue(new StringType("value1"));
		parent.addExtension(child1);
		Extension child2 = new Extension().setUrl("http://example.com#child").setValue(new StringType("value2"));
		parent.addExtension(child2);

		Extension modExt = new Extension();
		modExt.setUrl("http://example.com/extensions#modext");
		modExt.setValue(new DateType("1995-01-02"));
		patient.addModifierExtension(modExt);

		HumanName name = patient.addName();
		name.addFamily("Blah");
		StringType given = name.addGivenElement();
		given.setValue("Joe");
		Extension ext2 = new Extension().setUrl("http://examples.com#givenext").setValue(new StringType("given"));
		given.addExtension(ext2);

		StringType given2 = name.addGivenElement();
		given2.setValue("Shmoe");
		Extension given2ext = new Extension().setUrl("http://examples.com#givenext_parent");
		given2.addExtension(given2ext);
		given2ext.addExtension(new Extension().setUrl("http://examples.com#givenext_child").setValue(new StringType("CHILD")));

		String output = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(patient);
		ourLog.info(output);

		String enc = ourCtx.newXmlParser().encodeResourceToString(patient);
		assertThat(enc, containsString("<Patient xmlns=\"http://hl7.org/fhir\"><extension url=\"http://example.com/extensions#someext\"><valueDateTime value=\"2011-01-02T11:13:15\"/></extension>"));
		assertThat(enc, containsString("<modifierExtension url=\"http://example.com/extensions#modext\"><valueDate value=\"1995-01-02\"/></modifierExtension>"));
		assertThat(enc, containsString(
				"<extension url=\"http://example.com#parent\"><extension url=\"http://example.com#child\"><valueString value=\"value1\"/></extension><extension url=\"http://example.com#child\"><valueString value=\"value2\"/></extension></extension>"));
		assertThat(enc, containsString("<given value=\"Joe\"><extension url=\"http://examples.com#givenext\"><valueString value=\"given\"/></extension></given>"));
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
	 * See #216
	 */
	@Test
	public void testEncodeAndParseIdentifierDstu2() {
		IParser xmlParser = ourCtx.newXmlParser().setPrettyPrint(true);

		Patient patient = new Patient();
		patient.addIdentifier().setSystem("SYS").setValue("VAL").setType(new CodeableConcept().addCoding(new Coding().setSystem("http://hl7.org/fhir/v2/0203").setCode("MR")));

		String out = xmlParser.encodeResourceToString(patient);
		ourLog.info(out);

		//@formatter:off
		assertThat(out, stringContainsInOrder("<identifier>", 
				"<type>",
				"<coding>",
				"<system value=\"http://hl7.org/fhir/v2/0203\"/>", 
				"<code value=\"MR\"/>", 
				"</coding>", 
				"</type>",
				"<system value=\"SYS\"/>", 
				"<value value=\"VAL\"/>", 
				"</identifier>"));
		//@formatter:on

		patient = ourCtx.newXmlParser().parseResource(Patient.class, out);
		assertEquals("http://hl7.org/fhir/v2/0203", patient.getIdentifier().get(0).getType().getCoding().get(0).getSystem());
		assertEquals("MR", patient.getIdentifier().get(0).getType().getCoding().get(0).getCode());
	}

	@Test
	public void testEncodeAndParseMetaProfileAndTags() {
		Patient p = new Patient();
		p.addName().addFamily("FAMILY");

		p.getMeta().addProfile("http://foo/Profile1");
		p.getMeta().addProfile("http://foo/Profile2");

		p.getMeta().addTag().setSystem("scheme1").setCode("term1").setDisplay("label1");
		p.getMeta().addTag().setSystem("scheme2").setCode("term2").setDisplay("label2");

		p.getMeta().addSecurity().setSystem("sec_scheme1").setCode("sec_term1").setDisplay("sec_label1");
		p.getMeta().addSecurity().setSystem("sec_scheme2").setCode("sec_term2").setDisplay("sec_label2");

		String enc = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(p);
		ourLog.info(enc);

		//@formatter:off
		assertThat(enc, stringContainsInOrder("<Patient xmlns=\"http://hl7.org/fhir\">", 
			"<meta>",
			"<meta>",
			"<profile value=\"http://foo/Profile1\"/>",
			"<profile value=\"http://foo/Profile2\"/>",
			"<tag>",
			"<system value=\"scheme1\"/>",
			"<code value=\"term1\"/>",
			"<display value=\"label1\"/>",
			"</tag>",
			"<tag>",
			"<system value=\"scheme2\"/>",
			"<code value=\"term2\"/>",
			"<display value=\"label2\"/>",
			"</tag>",
			"</meta>",
			"</meta>",
			"<name>",
			"<family value=\"FAMILY\"/>",
			"</name>", 
			"</Patient>"));
		//@formatter:on

		Patient parsed = ourCtx.newXmlParser().parseResource(Patient.class, enc);
		List<UriType> gotLabels = parsed.getMeta().getProfile();
		assertEquals(2, gotLabels.size());
		UriType label = (UriType) gotLabels.get(0);
		assertEquals("http://foo/Profile1", label.getValue());
		label = (UriType) gotLabels.get(1);
		assertEquals("http://foo/Profile2", label.getValue());

		List<Coding> tagList = parsed.getMeta().getTag();
		assertEquals(2, tagList.size());
		assertEquals("scheme1", tagList.get(0).getSystem());
		assertEquals("term1", tagList.get(0).getCode());
		assertEquals("label1", tagList.get(0).getDisplay());
		assertEquals("scheme2", tagList.get(1).getSystem());
		assertEquals("term2", tagList.get(1).getCode());
		assertEquals("label2", tagList.get(1).getDisplay());

		tagList = parsed.getMeta().getSecurity();
		assertEquals(2, tagList.size());
		assertEquals("sec_scheme1", tagList.get(0).getSystem());
		assertEquals("sec_term1", tagList.get(0).getCode());
		assertEquals("sec_label1", tagList.get(0).getDisplay());
		assertEquals("sec_scheme2", tagList.get(1).getSystem());
		assertEquals("sec_term2", tagList.get(1).getCode());
		assertEquals("sec_label2", tagList.get(1).getDisplay());
	}

	@Test
	public void testEncodeAndParseMetaProfiles() {
		Patient p = new Patient();
		p.addName().addFamily("FAMILY");

		p.getMeta().addTag().setSystem("scheme1").setCode("term1").setDisplay("label1");
		p.getMeta().addTag().setSystem("scheme2").setCode("term2").setDisplay("label2");

		String enc = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(p);
		ourLog.info(enc);

		//@formatter:off
		assertThat(enc, stringContainsInOrder("<Patient xmlns=\"http://hl7.org/fhir\">", 
			"<meta>",
			"<meta>",
			"<tag>",
			"<system value=\"scheme1\"/>",
			"<code value=\"term1\"/>",
			"<display value=\"label1\"/>",
			"</tag>",
			"<tag>",
			"<system value=\"scheme2\"/>",
			"<code value=\"term2\"/>",
			"<display value=\"label2\"/>",
			"</tag>",
			"</meta>",
			"</meta>",
			"<name>",
			"<family value=\"FAMILY\"/>",
			"</name>", 
			"</Patient>"));
		//@formatter:on

		Patient parsed = ourCtx.newXmlParser().parseResource(Patient.class, enc);
		assertThat(parsed.getMeta().getProfile(), empty());

		List<Coding> tagList = parsed.getMeta().getTag();
		assertEquals(2, tagList.size());
		assertEquals("scheme1", tagList.get(0).getSystem());
		assertEquals("term1", tagList.get(0).getCode());
		assertEquals("label1", tagList.get(0).getDisplay());
		assertEquals("scheme2", tagList.get(1).getSystem());
		assertEquals("term2", tagList.get(1).getCode());
		assertEquals("label2", tagList.get(1).getDisplay());
	}

	/**
	 * Test for #233
	 */
	@Test
	public void testEncodeAndParseProfiledDatatype() {
		MedicationOrder mo = new MedicationOrder();
		mo.addDosageInstruction().getTiming().getRepeat().setBounds(new Duration().setCode("code"));
		String out = ourCtx.newXmlParser().encodeResourceToString(mo);
		ourLog.info(out);
		assertThat(out, containsString("</boundsQuantity>"));

		mo = ourCtx.newXmlParser().parseResource(MedicationOrder.class, out);
		Duration duration = (Duration) mo.getDosageInstruction().get(0).getTiming().getRepeat().getBounds();
		assertEquals("code", duration.getCode());
	}

	/**
	 * See #216 - Profiled datatypes should use their unprofiled parent type as the choice[x] name
	 */
	@Test
	public void testEncodeAndParseProfiledDatatypeChoice() throws Exception {
		IParser xmlParser = ourCtx.newXmlParser();

		MedicationStatement ms = new MedicationStatement();
		ms.addDosage().setQuantity(new SimpleQuantity().setValue(123));

		String output = xmlParser.encodeResourceToString(ms);
		assertThat(output, containsString("<quantityQuantity><value value=\"123\"/></quantityQuantity>"));
	}

	@Test
	public void testEncodeAndParseSecurityLabels() {
		Patient p = new Patient();
		p.addName().addFamily("FAMILY");

		List<Coding> labels = new ArrayList<Coding>();
		labels.add(new Coding().setSystem("SYSTEM1").setCode("CODE1").setDisplay("DISPLAY1").setVersion("VERSION1"));
		labels.add(new Coding().setSystem("SYSTEM2").setCode("CODE2").setDisplay("DISPLAY2").setVersion("VERSION2"));
		p.getMeta().getSecurity().addAll(labels);

		String enc = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(p);
		ourLog.info(enc);

		//@formatter:off
		assertThat(enc, stringContainsInOrder("<Patient xmlns=\"http://hl7.org/fhir\">", 
			"<meta>", 
			"<security>", 
			"<system value=\"SYSTEM1\"/>", 
			"<version value=\"VERSION1\"/>", 
			"<code value=\"CODE1\"/>", 
			"<display value=\"DISPLAY1\"/>", 
			"</security>", 
			"<security>", 
			"<system value=\"SYSTEM2\"/>", 
			"<version value=\"VERSION2\"/>", 
			"<code value=\"CODE2\"/>", 
			"<display value=\"DISPLAY2\"/>", 
			"</security>",
			"</meta>", 
			"<name>", 
			"<family value=\"FAMILY\"/>", 
			"</name>", 
			"</Patient>"));
		//@formatter:on

		Patient parsed = ourCtx.newXmlParser().parseResource(Patient.class, enc);
		List<Coding> gotLabels = parsed.getMeta().getSecurity();

		assertEquals(2, gotLabels.size());

		Coding label = (Coding) gotLabels.get(0);
		assertEquals("SYSTEM1", label.getSystem());
		assertEquals("CODE1", label.getCode());
		assertEquals("DISPLAY1", label.getDisplay());
		assertEquals("VERSION1", label.getVersion());

		label = (Coding) gotLabels.get(1);
		assertEquals("SYSTEM2", label.getSystem());
		assertEquals("CODE2", label.getCode());
		assertEquals("DISPLAY2", label.getDisplay());
		assertEquals("VERSION2", label.getVersion());
	}

	/**
	 * See #103
	 */
	@Test
	public void testEncodeAndReEncodeContainedJson() {
		Composition comp = new Composition();
		comp.addSection().addEntry().setResource(new AllergyIntolerance().setNote(new Annotation().setText("Section0_Allergy0")));
		comp.addSection().addEntry().setResource(new AllergyIntolerance().setNote(new Annotation().setText("Section1_Allergy0")));
		comp.addSection().addEntry().setResource(new AllergyIntolerance().setNote(new Annotation().setText("Section2_Allergy0")));

		IParser parser = ourCtx.newJsonParser().setPrettyPrint(true);

		String string = parser.encodeResourceToString(comp);
		ourLog.info(string);

		Composition parsed = parser.parseResource(Composition.class, string);
		parsed.getSection().remove(0);

		string = parser.encodeResourceToString(parsed);
		ourLog.info(string);

		parsed = parser.parseResource(Composition.class, string);
		assertEquals(2, parsed.getContained().size());
	}

	/**
	 * See #103
	 */
	@Test
	public void testEncodeAndReEncodeContainedXml() {
		Composition comp = new Composition();
		comp.addSection().addEntry().setResource(new AllergyIntolerance().setNote(new Annotation().setText("Section0_Allergy0")));
		comp.addSection().addEntry().setResource(new AllergyIntolerance().setNote(new Annotation().setText("Section1_Allergy0")));
		comp.addSection().addEntry().setResource(new AllergyIntolerance().setNote(new Annotation().setText("Section2_Allergy0")));

		IParser parser = ourCtx.newXmlParser().setPrettyPrint(true);

		String string = parser.encodeResourceToString(comp);
		ourLog.info(string);

		Composition parsed = parser.parseResource(Composition.class, string);
		parsed.getSection().remove(0);

		string = parser.encodeResourceToString(parsed);
		ourLog.info(string);

		parsed = parser.parseResource(Composition.class, string);
		assertEquals(2, parsed.getContained().size());
	}

	@Test
	public void testEncodeBinaryWithNoContentType() {
		Binary b = new Binary();
		b.setContent(new byte[] { 1, 2, 3, 4 });

		String output = ourCtx.newXmlParser().encodeResourceToString(b);
		ourLog.info(output);

		assertEquals("<Binary xmlns=\"http://hl7.org/fhir\"><content value=\"AQIDBA==\"/></Binary>", output);
	}

	@Test
	public void testEncodeBundleWithContained() {
		DiagnosticReport rpt = new DiagnosticReport();
		rpt.addResult().setResource(new Observation().setCode(new CodeableConcept().setText("Sharp1")).setId("#1"));
		rpt.addResult().setResource(new Observation().setCode(new CodeableConcept().setText("Uuid1")).setId("urn:uuid:UUID1"));

		Bundle b = new Bundle();
		b.addEntry().setResource(rpt);

		String encoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(b);
		ourLog.info(encoded);

		assertThat(encoded, stringContainsInOrder("<DiagnosticReport", "<contained", "<Observation", "<text value=\"Sharp1\"", "</DiagnosticReport"));
		assertThat(encoded, not(stringContainsInOrder("<DiagnosticReport", "<contained", "<Observation", "<contained", "<Observation", "</DiagnosticReport")));
	}

	/**
	 * See #113
	 */
	@Test
	public void testEncodeContainedResources() {

		MedicationOrder medicationPrescript = new MedicationOrder();

		String medId = "123";
		CodeableConcept codeDt = new CodeableConcept().addCoding(new Coding().setSystem("urn:sys").setCode("code1"));

		// Adding medication to Contained.
		Medication medResource = new Medication();
		medResource.setCode(codeDt);
		medResource.setId("#" + String.valueOf(medId));
		medicationPrescript.getContained().add(medResource);

		// Medication reference. This should point to the contained resource.
		Reference medRefDt = new Reference("#" + medId);
		medRefDt.setDisplay("MedRef");
		medicationPrescript.setMedication(medRefDt);

		IParser p = ourCtx.newXmlParser().setPrettyPrint(true);
		String encoded = p.encodeResourceToString(medicationPrescript);
		ourLog.info(encoded);

		// @formatter:on
		assertThat(encoded,
				stringContainsInOrder("<MedicationOrder xmlns=\"http://hl7.org/fhir\">", "<contained>", "<Medication xmlns=\"http://hl7.org/fhir\">", "<id value=\"123\"/>", "<code>", "<coding>",
						"<system value=\"urn:sys\"/>", "<code value=\"code1\"/>", "</coding>", "</code>", "</Medication>", "</contained>", "<medicationReference>", "<reference value=\"#123\"/>",
						"<display value=\"MedRef\"/>", "</medicationReference>", "</MedicationOrder>"));
		//@formatter:off

	}

	/**
	 * See #113
	 */
	@Test
	public void testEncodeContainedResourcesAutomatic() {
		
		MedicationOrder medicationPrescript = new MedicationOrder();
		String nameDisp = "MedRef";
		CodeableConcept codeDt = new CodeableConcept().addCoding(new Coding("urn:sys", "code1", null));
		
		// Adding medication to Contained.
		Medication medResource = new Medication();
		// No ID set
		medResource.setCode(codeDt);

		// Medication reference. This should point to the contained resource.
		Reference medRefDt = new Reference();
		medRefDt.setDisplay(nameDisp);
		// Resource reference set, but no ID
		medRefDt.setResource(medResource);
		medicationPrescript.setMedication(medRefDt);
		
		IParser p = ourCtx.newXmlParser().setPrettyPrint(true);
		String encoded = p.encodeResourceToString(medicationPrescript);
		ourLog.info(encoded);
		
		//@formatter:on
		assertThat(encoded,
				stringContainsInOrder("<MedicationOrder xmlns=\"http://hl7.org/fhir\">", "<contained>", "<Medication xmlns=\"http://hl7.org/fhir\">", "<id value=\"1\"/>", "<code>", "<coding>",
						"<system value=\"urn:sys\"/>", "<code value=\"code1\"/>", "</coding>", "</code>", "</Medication>", "</contained>", "<medicationReference>", "<reference value=\"#1\"/>",
						"<display value=\"MedRef\"/>", "</medicationReference>", "</MedicationOrder>"));
		//@formatter:off
	}

	/**
	 * See #113
	 */
	@Test
	public void testEncodeContainedResourcesManualContainUsingNonLocalId() {
		
		MedicationOrder medicationPrescript = new MedicationOrder();
		
		String medId = "123";
		CodeableConcept codeDt = new CodeableConcept().addCoding(new Coding("urn:sys", "code1", null));

		// Adding medication to Contained.
		Medication medResource = new Medication();
		medResource.setCode(codeDt);
		medResource.setId(String.valueOf(medId)); // ID does not start with '#'
		medicationPrescript.getContained().add(medResource);

		// Medication reference. This should point to the contained resource.
		Reference medRefDt = new Reference("#" + medId);
		medRefDt.setDisplay("MedRef");
		medicationPrescript.setMedication(medRefDt);
		
		IParser p = ourCtx.newXmlParser().setPrettyPrint(true);
		String encoded = p.encodeResourceToString(medicationPrescript);
		ourLog.info(encoded);
		
		//@formatter:on
		assertThat(encoded,
				stringContainsInOrder("<MedicationOrder xmlns=\"http://hl7.org/fhir\">", "<contained>", "<Medication xmlns=\"http://hl7.org/fhir\">", "<id value=\"123\"/>", "<code>", "<coding>",
						"<system value=\"urn:sys\"/>", "<code value=\"code1\"/>", "</coding>", "</code>", "</Medication>", "</contained>", "<medicationReference>", "<reference value=\"#123\"/>",
						"<display value=\"MedRef\"/>", "</medicationReference>", "</MedicationOrder>"));
		//@formatter:off

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
		patient.addIdentifier().setSystem("urn:mrns").setValue("253345");
		patient.getText().setDivAsString("<div>BARFOO</div>");
		patient.getManagingOrganization().setResource(org);

		String encoded = parser.encodeResourceToString(patient);
		ourLog.info(encoded);
		
		assertThat(encoded, stringContainsInOrder("<Patient", "<text>", "<div xmlns=\"http://www.w3.org/1999/xhtml\">BARFOO</div>", "<contained>", "<Organization", "</Organization"));
		assertThat(encoded, not(stringContainsInOrder("<Patient", "<text>", "<contained>", "<Organization", "<text", "</Organization")));
		
		assertThat(encoded, not(containsString("FOOBAR")));
		assertThat(encoded, (containsString("BARFOO")));

	}

	

	@Test
	public void testEncodeDoesntIncludeUuidId() {
		Patient p = new Patient();
		p.setId(new IdType("urn:uuid:42795ed8-041f-4ebf-b6f4-78ef6f64c2f2"));
		p.addIdentifier().setSystem("ACME");
		
		String actual = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(p);
		assertThat(actual, not(containsString("78ef6f64c2f2")));
	}

	@Test
	public void testEncodeEmptyBinary() {
		String output = ourCtx.newXmlParser().encodeResourceToString(new Binary());
		assertEquals("<Binary xmlns=\"http://hl7.org/fhir\"/>", output);
	}

	/**
	 * #158
	 */
	@Test
	public void testEncodeEmptyTag() {
		ArrayList<Coding> tagList = new ArrayList<Coding>();
		tagList.add(new Coding());
		tagList.add(new Coding().setDisplay("Label"));
		
		Patient p = new Patient();
		p.getMeta().getTag().addAll(tagList);
		
		String encoded = ourCtx.newXmlParser().encodeResourceToString(p);
		assertThat(encoded, not(containsString("tag")));
	}

	/**
	 * #158
	 */
	@Test
	public void testEncodeEmptyTag2() {
		ArrayList<Coding> tagList = new ArrayList<Coding>();
		tagList.add(new Coding().setSystem("scheme").setCode("code"));
		tagList.add(new Coding().setDisplay("Label"));

		Patient p = new Patient();
		p.getMeta().getTag().addAll(tagList);
		
		String encoded = ourCtx.newXmlParser().encodeResourceToString(p);
		assertThat(encoded, containsString("tag"));
		assertThat(encoded, containsString("scheme"));
		assertThat(encoded, not(containsString("Label")));
	}

	@Test
	public void testEncodeExtensionWithResourceContent() {
		IParser parser = ourCtx.newXmlParser();

		Patient patient = new Patient();
		patient.addAddress().setUse(AddressUse.HOME);
		patient.addExtension(new Extension("urn:foo", new Reference("Organization/123")));

		String val = parser.encodeResourceToString(patient);
		ourLog.info(val);
		assertThat(val, StringContains.containsString("<extension url=\"urn:foo\"><valueReference><reference value=\"Organization/123\"/></valueReference></extension>"));

		Patient actual = parser.parseResource(Patient.class, val);
		assertEquals(AddressUse.HOME, patient.getAddress().get(0).getUse());
		List<Extension> ext = actual.getExtension();
		assertEquals(1, ext.size());
		Reference ref = (Reference) ext.get(0).getValue();
		assertEquals("Organization/123", ref.getReference());

	}
	
	@Test
	public void testEncodeNarrativeSuppressed() {
		Patient patient = new Patient();
		patient.setId("Patient/1/_history/1");
		patient.getText().setDivAsString("<div>THE DIV</div>");
		patient.addName().addFamily("FAMILY");
		patient.setMaritalStatus(new CodeableConcept().addCoding(new Coding().setCode("D")));
		patient.setMaritalStatus(new CodeableConcept().addCoding(new Coding().setCode("D")));

		String encoded = ourCtx.newXmlParser().setPrettyPrint(true).setSuppressNarratives(true).encodeResourceToString(patient);
		ourLog.info(encoded);
		
		assertThat(encoded, containsString("<Patient"));
		assertThat(encoded, stringContainsInOrder("<tag>", "<system value=\"" + Constants.TAG_SUBSETTED_SYSTEM + "\"/>", "<code value=\"" + Constants.TAG_SUBSETTED_CODE+"\"/>", "</tag>"));
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
		
		// Create a list containing both resources. In a server method, you might just
		// return this list, but here we will create a bundle to encode.
		List<IBaseResource> resources = new ArrayList<IBaseResource>();
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
	public void testEncodeReferenceUsingUnqualifiedResourceWorksCorrectly() {
		
		Patient patient = new Patient();
		patient.setId("phitcc_pat_normal");
		patient.addName().addGiven("Patty").setUse(NameUse.NICKNAME);
		patient.addTelecom().setSystem(ContactPointSystem.EMAIL).setValue("patpain@ehealthinnovation.org");
		patient.setGender(AdministrativeGender.FEMALE);
		patient.setBirthDateElement(new DateType("2001-10-13"));

		DateTimeType obsEffectiveTime = new DateTimeType("2015-04-11T12:22:01-04:00");

		Observation obsParent = new Observation();
		obsParent.setId("phitcc_obs_bp_parent");
		obsParent.getSubject().setResource(patient);
		obsParent.setStatus(ObservationStatus.FINAL);
		obsParent.setEffective(obsEffectiveTime);

		Observation obsSystolic = new Observation();
		obsSystolic.setId("phitcc_obs_bp_dia");
		obsSystolic.getSubject().setResource(patient);
		obsSystolic.setEffective(obsEffectiveTime);
		obsParent.addRelated().setType(ObservationRelationshipType.HASMEMBER).setTarget(new Reference(obsSystolic));

		Observation obsDiastolic = new Observation();
		obsDiastolic.setId("phitcc_obs_bp_dia");
		obsDiastolic.getSubject().setResource(patient);
		obsDiastolic.setEffective(obsEffectiveTime);
		obsParent.addRelated().setType(ObservationRelationshipType.HASMEMBER).setTarget(new Reference(obsDiastolic));
		
		String str = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(obsParent);
		ourLog.info(str);
		
		assertThat(str, containsString("<reference value=\"Patient/phitcc_pat_normal\"/>"));
		assertThat(str, containsString("<reference value=\"Observation/phitcc_obs_bp_dia\"/>"));
	}

	
	@Test
	public void testEncodeSummary() {
		Patient patient = new Patient();
		patient.setId("Patient/1/_history/1");
		patient.getText().setDivAsString("<div>THE DIV</div>");
		patient.addName().addFamily("FAMILY");
		patient.setMaritalStatus(new CodeableConcept().addCoding(new Coding().setCode("D")));

		String encoded = ourCtx.newXmlParser().setPrettyPrint(true).setSummaryMode(true).encodeResourceToString(patient);
		ourLog.info(encoded);
		
		assertThat(encoded, containsString("<Patient"));
		assertThat(encoded, stringContainsInOrder("<tag>", "<system value=\"" + Constants.TAG_SUBSETTED_SYSTEM + "\"/>", "<code value=\"" + Constants.TAG_SUBSETTED_CODE+"\"/>", "</tag>"));
		assertThat(encoded, not(containsString("THE DIV")));
		assertThat(encoded, containsString("family"));
		assertThat(encoded, not(containsString("maritalStatus")));
	}

	@Test
	public void testEncodeSummary2() {
		Patient patient = new Patient();
		patient.setId("Patient/1/_history/1");
		patient.getText().setDivAsString("<div>THE DIV</div>");
		patient.addName().addFamily("FAMILY");
		patient.setMaritalStatus(new CodeableConcept().addCoding(new Coding().setCode("D")));

		patient.getMeta().addTag().setSystem("foo").setCode("bar");
		
		String encoded = ourCtx.newXmlParser().setPrettyPrint(true).setSummaryMode(true).encodeResourceToString(patient);
		ourLog.info(encoded);
		
		assertThat(encoded, containsString("<Patient"));
		assertThat(encoded, stringContainsInOrder("<tag>", "<system value=\"foo\"/>", "<code value=\"bar\"/>", "</tag>"));
		assertThat(encoded, stringContainsInOrder("<tag>", "<system value=\"" + Constants.TAG_SUBSETTED_SYSTEM + "\"/>", "<code value=\"" + Constants.TAG_SUBSETTED_CODE+"\"/>", "</tag>"));
		assertThat(encoded, not(containsString("THE DIV")));
		assertThat(encoded, containsString("family"));
		assertThat(encoded, not(containsString("maritalStatus")));
	}


	@Test @Ignore
	public void testEncodeWithEncodeElements() throws Exception {
		String content = IOUtils.toString(XmlParserDstu3Test.class.getResourceAsStream("/bundle-example.xml"));
		
		Patient patient = new Patient();
		patient.addName().addFamily("FAMILY");
		patient.addAddress().addLine("LINE1");
		
		Bundle bundle = new Bundle();
		bundle.setTotal(100);
		bundle.addEntry().setResource(patient);

		{
		IParser p = ourCtx.newXmlParser();
		p.setEncodeElements(new HashSet<String>(Arrays.asList("Patient.name", "Bundle.entry")));
		p.setPrettyPrint(true);
		String out = p.encodeResourceToString(bundle);
		ourLog.info(out);
		assertThat(out, not(containsString("total")));
		assertThat(out, (containsString("Patient")));
		assertThat(out, (containsString("name")));
		assertThat(out, not(containsString("address")));
		}
		{
		IParser p = ourCtx.newXmlParser();
		p.setEncodeElements(new HashSet<String>(Arrays.asList("Patient.name")));
		p.setEncodeElementsAppliesToResourceTypes(new HashSet<String>(Arrays.asList("Patient")));
		p.setPrettyPrint(true);
		String out = p.encodeResourceToString(bundle);
		ourLog.info(out);
		assertThat(out, (containsString("total")));
		assertThat(out, (containsString("Patient")));
		assertThat(out, (containsString("name")));
		assertThat(out, not(containsString("address")));
		}
		{
		IParser p = ourCtx.newXmlParser();
		p.setEncodeElements(new HashSet<String>(Arrays.asList("Patient")));
		p.setEncodeElementsAppliesToResourceTypes(new HashSet<String>(Arrays.asList("Patient")));
		p.setPrettyPrint(true);
		String out = p.encodeResourceToString(bundle);
		ourLog.info(out);
		assertThat(out, (containsString("total")));
		assertThat(out, (containsString("Patient")));
		assertThat(out, (containsString("name")));
		assertThat(out, (containsString("address")));
		}
		
	}

	@Test
	public void testEncodeWithNarrative() {
		Patient p = new Patient();
		p.addName().addFamily("Smith").addGiven("John");
		
		ourCtx.setNarrativeGenerator(new DefaultThymeleafNarrativeGenerator());
		
		String output = ourCtx.newXmlParser().encodeResourceToString(p);
		ourLog.info(output);
		
		assertThat(output, containsString("<text><status value=\"generated\"/><div xmlns=\"http://www.w3.org/1999/xhtml\"><div class=\"hapiHeaderText\"> John <b>SMITH </b>"));
	}

	@Test
	public void testMoreExtensions() throws Exception {

		Patient patient = new Patient();
		patient.addIdentifier().setUse(IdentifierUse.OFFICIAL).setSystem("urn:example").setValue("7000135");

		Extension ext = new Extension();
		ext.setUrl("http://example.com/extensions#someext");
		ext.setValue(new DateTimeType("2011-01-02T11:13:15"));

		// Add the extension to the resource
		patient.addExtension(ext);
		// END SNIPPET: resourceExtension

		// START SNIPPET: resourceStringExtension
		HumanName name = patient.addName();
		name.addFamily("Shmoe");
		StringType given = name.addGivenElement();
		given.setValue("Joe");
		Extension ext2 = new Extension().setUrl("http://examples.com#givenext").setValue(new StringType("given"));
		given.addExtension(ext2);

		StringType given2 = name.addGivenElement();
		given2.setValue("Shmoe");
		Extension given2ext = new Extension().setUrl("http://examples.com#givenext_parent");
		given2.addExtension(given2ext);
		Extension givenExtChild = new Extension();
		givenExtChild.setUrl("http://examples.com#givenext_child").setValue(new StringType("CHILD"));
		given2ext.addExtension(givenExtChild);
		// END SNIPPET: resourceStringExtension

		// START SNIPPET: subExtension
		Extension parent = new Extension().setUrl("http://example.com#parent");
		patient.addExtension(parent);

		Extension child1 = new Extension().setUrl("http://example.com#child").setValue(new StringType("value1"));
		parent.addExtension(child1);

		Extension child2 = new Extension().setUrl("http://example.com#child").setValue(new StringType("value1"));
		parent.addExtension(child2);
		// END SNIPPET: subExtension

		String output = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(patient);
		ourLog.info(output);

		String enc = ourCtx.newXmlParser().encodeResourceToString(patient);
		assertThat(enc, containsString("<Patient xmlns=\"http://hl7.org/fhir\"><extension url=\"http://example.com/extensions#someext\"><valueDateTime value=\"2011-01-02T11:13:15\"/></extension>"));
		assertThat(
				enc,
				containsString("<extension url=\"http://example.com#parent\"><extension url=\"http://example.com#child\"><valueString value=\"value1\"/></extension><extension url=\"http://example.com#child\"><valueString value=\"value1\"/></extension></extension>"));
		assertThat(enc, containsString("<given value=\"Joe\"><extension url=\"http://examples.com#givenext\"><valueString value=\"given\"/></extension></given>"));
		assertThat(enc, containsString("<given value=\"Shmoe\"><extension url=\"http://examples.com#givenext_parent\"><extension url=\"http://examples.com#givenext_child\"><valueString value=\"CHILD\"/></extension></extension></given>"));
	}

	
	@Test
	public void testOmitResourceId() {
		Patient p = new Patient();
		p.setId("123");
		p.addName().addFamily("ABC");
		
		assertThat(ourCtx.newXmlParser().encodeResourceToString(p), stringContainsInOrder("123", "ABC"));
		assertThat(ourCtx.newXmlParser().setOmitResourceId(true).encodeResourceToString(p), containsString("ABC"));
		assertThat(ourCtx.newXmlParser().setOmitResourceId(true).encodeResourceToString(p), not(containsString("123")));
	}

	@Test @Ignore
	public void testParseAndEncodeBundle() throws Exception {
		String content = IOUtils.toString(XmlParserDstu3Test.class.getResourceAsStream("/bundle-example.xml"));

		Bundle parsed = ourCtx.newXmlParser().parseResource(Bundle.class, content);
		assertEquals("Bundle/example/_history/1", parsed.getIdElement().getValue());
		assertEquals("1", parsed.getMeta().getVersionId());
		assertEquals("1", parsed.getIdElement().getVersionIdPart());
		assertEquals(("2014-08-18T01:43:30Z"), parsed.getMeta().getLastUpdatedElement().getValueAsString());
		assertEquals("searchset", parsed.getType().toCode());
		assertEquals(3, parsed.getTotal());
		assertEquals("https://example.com/base/MedicationOrder?patient=347&searchId=ff15fd40-ff71-4b48-b366-09c706bed9d0&page=2", parsed.getLink("next").getUrl());
		assertEquals("https://example.com/base/MedicationOrder?patient=347&_include=MedicationOrder.medication", parsed.getLink("self").getUrl());

		assertEquals(2, parsed.getEntry().size());
		assertEquals("http://foo?search", parsed.getEntry().get(0).getLink("search").getUrl());

		assertEquals("http://example.com/base/MedicationOrder/3123/_history/1", parsed.getEntry().get(0).getLink("alternate").getUrl());
		MedicationOrder p = (MedicationOrder) parsed.getEntry().get(0).getResource();
		assertEquals("Patient/347", p.getPatient().getReference());
		assertEquals("2014-08-16T05:31:17Z", p.getMeta().getLastUpdatedElement().getValueAsString());
		assertEquals("http://example.com/base/MedicationOrder/3123/_history/1", p.getId());

		Medication m = (Medication) parsed.getEntry().get(1).getResource();
		assertEquals("http://example.com/base/Medication/example", m.getId());
		assertSame(((Reference)p.getMedication()).getResource(), m);

		String reencoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(parsed);
		ourLog.info(reencoded);

		Diff d = new Diff(new StringReader(content), new StringReader(reencoded));
		assertTrue(d.toString(), d.identical());

	}

	@Test @Ignore
	public void testParseAndEncodeBundleNewStyle() throws Exception {
		String content = IOUtils.toString(XmlParserDstu3Test.class.getResourceAsStream("/bundle-example.xml"));

		IParser newXmlParser = ourCtx.newXmlParser();
		Bundle parsed = newXmlParser.parseResource(Bundle.class, content);
		assertEquals("Bundle/example/_history/1", parsed.getIdElement().getValue());
		assertEquals("1", parsed.getIdElement().getVersionIdPart());
		assertEquals("2014-08-18T01:43:30Z", parsed.getMeta().getLastUpdatedElement().getValueAsString());
		assertEquals("searchset", parsed.getType());
		assertEquals(3, parsed.getTotal());
		assertEquals("https://example.com/base/MedicationOrder?patient=347&searchId=ff15fd40-ff71-4b48-b366-09c706bed9d0&page=2", parsed.getLink().get(0).getUrlElement().getValueAsString());
		assertEquals("https://example.com/base/MedicationOrder?patient=347&_include=MedicationOrder.medication", parsed.getLink().get(1).getUrlElement().getValueAsString());

		assertEquals(2, parsed.getEntry().size());
		assertEquals("alternate", parsed.getEntry().get(0).getLink().get(0).getRelation());
		assertEquals("http://example.com/base/MedicationOrder/3123/_history/1", parsed.getEntry().get(0).getLink().get(0).getUrl());
		assertEquals("http://foo?search", parsed.getEntry().get(0).getRequest().getUrlElement().getValueAsString());

		MedicationOrder p = (MedicationOrder) parsed.getEntry().get(0).getResource();
		assertEquals("Patient/347", p.getPatient().getReference());
		assertEquals("2014-08-16T05:31:17Z", p.getMeta().getLastUpdatedElement().getValueAsString());
		assertEquals("http://example.com/base/MedicationOrder/3123/_history/1", p.getId());
//		assertEquals("3123", p.getId());

		Medication m = (Medication) parsed.getEntry().get(1).getResource();
		assertEquals("http://example.com/base/Medication/example", m.getId());
		assertSame(((Reference)p.getMedication()).getResource(), m);

		String reencoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(parsed);
		ourLog.info(reencoded);

		Diff d = new Diff(new StringReader(content), new StringReader(reencoded));
		assertTrue(d.toString(), d.identical());

	}
	
	@Test
	public void testParseAndEncodeComments() throws IOException {
		//@formatter:off
		String input = "<Patient xmlns=\"http://hl7.org/fhir\">\n" + 
				"  <!--pre resource comment-->" +
				"  <id value=\"pat1\"/>\n" + 
				"  <text>\n" + 
				"    <status value=\"generated\"/>\n" + 
				"    <div xmlns=\"http://www.w3.org/1999/xhtml\">\n" + 
				"\n" + 
				"      <p>Patient Donald DUCK @ Acme Healthcare, Inc. MR = 654321</p>\n" + 
				"\n" + 
				"    </div>\n" + 
				"  </text>\n" + 
				"  <!--identifier comment 1-->\n" +
				"  <!--identifier comment 2-->\n" +
				"  <identifier>\n" + 
				"    <!--use comment 1-->\n" +
				"    <!--use comment 2-->\n" +
				"    <use value=\"usual\"/>\n" + 
				"    <type>\n" + 
				"      <coding>\n" + 
				"        <system value=\"http://hl7.org/fhir/v2/0203\"/>\n" + 
				"        <code value=\"MR\"/>\n" + 
				"      </coding>\n" + 
				"    </type>\n" + 
				"    <system value=\"urn:oid:0.1.2.3.4.5.6.7\"/>\n" + 
				"    <value value=\"654321\"/>\n" + 
				"  </identifier>\n" + 
				"  <active value=\"true\"/>" +
				"  <!--post resource comment-->" +
				"</Patient>";
		//@formatter:off

		Patient res = ourCtx.newXmlParser().parseResource(Patient.class, input);
		res.getFormatCommentsPre();
		assertEquals("Patient/pat1", res.getId());
		assertEquals("654321", res.getIdentifier().get(0).getValue());
		assertEquals(true, res.getActive());
		
		assertThat(res.getIdElement().getFormatCommentsPre(), contains("pre resource comment"));
		assertThat(res.getIdentifier().get(0).getFormatCommentsPre(), contains("identifier comment 1", "identifier comment 2"));
		assertThat(res.getIdentifier().get(0).getUseElement().getFormatCommentsPre(), contains("use comment 1", "use comment 2"));
		assertThat(res.getActiveElement().getFormatCommentsPost(), contains("post resource comment"));
		
		String encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(res);
		ourLog.info(encoded);
		
		//@formatter:off
		assertThat(encoded, stringContainsInOrder(
				"\"identifier\":[", 
				"{",
				"\"fhir_comments\":",
				"[",
				"\"identifier comment 1\"",
				",",
				"\"identifier comment 2\"",
				"]",
				"\"use\":\"usual\",", 
				"\"_use\":{", 
				"\"fhir_comments\":",
				"[",
				"\"use comment 1\"",
				",",
				"\"use comment 2\"",
				"]",
				"},",
				"\"type\"" 
		));
		//@formatter:off
		
		encoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(res);
		ourLog.info(encoded);
		
		//@formatter:off
		assertThat(encoded, stringContainsInOrder(
				"<Patient xmlns=\"http://hl7.org/fhir\">", 
				"<id value=\"pat1\"/>", 
				"<text>", 
				"<status value=\"generated\"/>", 
				"<div xmlns=\"http://www.w3.org/1999/xhtml\"> ", 
				"<p>Patient Donald DUCK @ Acme Healthcare, Inc. MR = 654321</p> ", 
				"</div>", 
				"</text>",
				" <!--identifier comment 1-->\n",
				" <!--identifier comment 2-->", 
				"<identifier>",
				"<!--use comment 1-->",
				"<!--use comment 2-->", 
				"<use value=\"usual\"/>", 
				"<type>", 
				"<coding>", 
				"<system value=\"http://hl7.org/fhir/v2/0203\"/>", 
				"<code value=\"MR\"/>", 
				"</coding>", 
				"</type>", 
				"<system value=\"urn:oid:0.1.2.3.4.5.6.7\"/>", 
				"<value value=\"654321\"/>", 
				"</identifier>", 
				"<active value=\"true\"/>", 
				"</Patient>" 
		));
		//@formatter:off

	}

	@Test
	public void testParseAndEncodeExtensionOnReference() {
		//@formatter:off
		String input = 
				"<DataElement>" + 
					"<id value=\"gender\"/>"+ 
					"<contained>"+ 
						"<ValueSet>"+ 
						"<id value=\"2179414\"/>"+ 
						"<url value=\"2179414\"/>"+ 
						"<version value=\"1.0\"/>"+ 
						"<status value=\"active\"/>"+ 
						"<description value=\"All codes representing the gender of a person.\"/>"+ 
						"<compose>"+ 
						"<include>"+ 
						"<system value=\"http://ncit.nci.nih.gov\"/>"+ 
						"<concept>"+ 
						"<code value=\"C17998\"/>"+ 
						"<display value=\"Unknown\"/>"+ 
						"</concept>"+ 
						"<concept>"+ 
						"<code value=\"C20197\"/>"+ 
						"<display value=\"Male\"/>"+ 
						"</concept>"+ 
						"<concept>"+ 
						"<code value=\"C16576\"/>"+ 
						"<display value=\"Female\"/>"+ 
						"</concept>"+ 
						"<concept>"+ 
						"<code value=\"C38046\"/>"+ 
						"<display value=\"Not specified\"/>"+ 
						"</concept>"+ 
						"</include>"+ 
						"</compose>"+ 
						"</ValueSet>"+ 
					"</contained>"+ 
					"<contained>"+ 
					"<ValueSet>"+ 
						"<id value=\"2179414-permitted\"/>"+ 
						"<status value=\"active\"/>"+ 
						"<codeSystem>"+ 
							"<system value=\"http://example.org/fhir/2179414\"/>"+ 
							"<caseSensitive value=\"true\"/>"+ 
							"<concept>"+ 
							"<code value=\"0\"/>"+ 
							"</concept>"+ 
							"<concept>"+ 
							"<code value=\"1\"/>"+ 
							"</concept>"+ 
							"<concept>"+ 
							"<code value=\"2\"/>"+ 
							"</concept>"+ 
							"<concept>"+ 
							"<code value=\"3\"/>"+ 
							"</concept>"+ 
						"</codeSystem>"+ 
						"</ValueSet>"+ 
					"</contained>"+ 
					"<contained>"+ 
						"<ConceptMap>"+ 
						"<id value=\"2179414-cm\"/>"+ 
						"<status value=\"active\"/>"+ 
						"<sourceReference>"+ 
						"<reference value=\"#2179414\"/>"+ 
						"</sourceReference>"+ 
						"<targetReference>"+ 
						"<reference value=\"#2179414-permitted\"/>"+ 
						"</targetReference>"+ 
						"<element>"+ 
						"<code value=\"C17998\"/>"+ 
						"<target>"+ 
						"<code value=\"0\"/>"+ 
						"<equivalence value=\"equal\"/>"+ 
						"</target>"+ 
						"</element>"+ 
						"<element>"+ 
						"<code value=\"C20197\"/>"+ 
						"<target>"+ 
						"<code value=\"1\"/>"+ 
						"<equivalence value=\"equal\"/>"+ 
						"</target>"+ 
						"</element>"+ 
						"<element>"+ 
						"<code value=\"C16576\"/>"+ 
						"<target>"+ 
						"<code value=\"2\"/>"+ 
						"<equivalence value=\"equal\"/>"+ 
						"</target>"+ 
						"</element>"+ 
						"<element>"+ 
						"<code value=\"C38046\"/>"+ 
						"<target>"+ 
						"<code value=\"3\"/>"+ 
						"<equivalence value=\"equal\"/>"+ 
						"</target>"+ 
						"</element>"+ 
					"</ConceptMap>"+ 
					"</contained>"+ 
					"<identifier>"+ 
						"<value value=\"2179650\"/>"+ 
					"</identifier>"+ 
					"<version value=\"1.0\"/>"+ 
					"<name value=\"Gender Code\"/>"+ 
					"<status value=\"active\"/>"+ 
					"<publisher value=\"DCP\"/>"+ 
					"<useContext>"+ 
						"<coding>"+ 
						"<system value=\"http://example.org/FBPP\"/>"+ 
						"<display value=\"FBPP Pooled Database\"/>"+ 
						"</coding>"+ 
						"<coding>"+ 
						"<system value=\"http://example.org/PhenX\"/>"+ 
						"<display value=\"Demographics\"/>"+ 
						"</coding>"+ 
						"<coding>"+ 
						"<system value=\"http://example.org/EligibilityCriteria\"/>"+ 
						"<display value=\"Pt. Administrative\"/>"+ 
						"</coding>"+ 
						"<coding>"+ 
						"<system value=\"http://example.org/UAMSClinicalResearch\"/>"+ 
						"<display value=\"UAMS New CDEs\"/>"+ 
						"</coding>"+ 
						"<coding>"+ 
						"<system value=\"http://example.org/PhenX\"/>"+ 
						"<display value=\"Substance Abuse and \"/>"+ 
						"</coding>"+ 
						"<coding>"+ 
						"<system value=\"http://example.org/Category\"/>"+ 
						"<display value=\"CSAERS Adverse Event\"/>"+ 
						"</coding>"+ 
						"<coding>"+ 
						"<system value=\"http://example.org/PhenX\"/>"+ 
						"<display value=\"Core: Tier 1\"/>"+ 
						"</coding>"+ 
						"<coding>"+ 
						"<system value=\"http://example.org/Category\"/>"+ 
						"<display value=\"Case Report Forms\"/>"+ 
						"</coding>"+ 
						"<coding>"+ 
						"<system value=\"http://example.org/Category\"/>"+ 
						"<display value=\"CSAERS Review Set\"/>"+ 
						"</coding>"+ 
						"<coding>"+ 
						"<system value=\"http://example.org/Demonstration%20Applications\"/>"+ 
						"<display value=\"CIAF\"/>"+ 
						"</coding>"+ 
						"<coding>"+ 
						"<system value=\"http://example.org/NIDA%20CTN%20Usage\"/>"+ 
						"<display value=\"Clinical Research\"/>"+ 
						"</coding>"+ 
						"<coding>"+ 
						"<system value=\"http://example.org/NIDA%20CTN%20Usage\"/>"+ 
						"<display value=\"Electronic Health Re\"/>"+ 
						"</coding>"+ 
						"<coding>"+ 
						"<system value=\"http://example.org/Condition\"/>"+ 
						"<display value=\"Barretts Esophagus\"/>"+ 
						"</coding>"+ 
						"<coding>"+ 
						"<system value=\"http://example.org/Condition\"/>"+ 
						"<display value=\"Bladder Cancer\"/>"+ 
						"</coding>"+ 
						"<coding>"+ 
						"<system value=\"http://example.org/Condition\"/>"+ 
						"<display value=\"Oral Leukoplakia\"/>"+ 
						"</coding>"+ 
						"<coding>"+ 
						"<system value=\"http://example.org/Condition\"/>"+ 
						"<display value=\"Sulindac for Breast\"/>"+ 
						"</coding>"+ 
					"</useContext>"+ 
					"<element>"+ 
						"<extension url=\"http://hl7.org/fhir/StructureDefinition/minLength\">"+ 
							"<valueInteger value=\"1\"/>"+ 
						"</extension>"+ 
						"<extension url=\"http://hl7.org/fhir/StructureDefinition/elementdefinition-question\">"+ 
							"<valueString value=\"Gender\"/>"+ 
						"</extension>"+ 
						"<path value=\"Gender\"/>"+ 
						"<definition value=\"The code representing the gender of a person.\"/>"+ 
						"<type>"+ 
						"<code value=\"CodeableConcept\"/>"+ 
						"</type>"+ 
						"<maxLength value=\"13\"/>"+ 
						"<binding>"+ 
							"<strength value=\"required\"/>"+ 
							"<valueSetReference>"+ 
							"<extension url=\"http://hl7.org/fhir/StructureDefinition/11179-permitted-value-valueset\">"+ 
							"<valueReference>"+ 
							"<reference value=\"#2179414-permitted\"/>"+ 
							"</valueReference>"+ 
							"</extension>"+ 
							"<extension url=\"http://hl7.org/fhir/StructureDefinition/11179-permitted-value-conceptmap\">"+ 
							"<valueReference>"+ 
							"<reference value=\"#2179414-cm\"/>"+ 
							"</valueReference>"+ 
							"</extension>"+ 
							"<reference value=\"#2179414\"/>"+ 
							"</valueSetReference>"+ 
						"</binding>"+ 
					"</element>"+ 
				"</DataElement>";
		//@formatter:on
		DataElement de = ourCtx.newXmlParser().parseResource(DataElement.class, input);
		String output = ourCtx.newXmlParser().encodeResourceToString(de).replace(" xmlns=\"http://hl7.org/fhir\"", "");

		ElementDefinition elem = de.getElement().get(0);
		ElementDefinitionBindingComponent b = elem.getBinding();
		// assertEquals("All codes representing the gender of a person.", b.getDescription());

		Reference ref = (Reference) b.getValueSet();
		assertEquals("#2179414", ref.getReference());

		assertEquals(2, ref.getExtension().size());
		Extension ext = ref.getExtension().get(0);
		assertEquals("http://hl7.org/fhir/StructureDefinition/11179-permitted-value-valueset", ext.getUrl());
		assertEquals(Reference.class, ext.getValue().getClass());
		assertEquals("#2179414-permitted", ((Reference) ext.getValue()).getReference());
		assertEquals(ValueSet.class, ((Reference) ext.getValue()).getResource().getClass());

		ext = ref.getExtension().get(1);
		assertEquals("http://hl7.org/fhir/StructureDefinition/11179-permitted-value-conceptmap", ext.getUrl());
		assertEquals(Reference.class, ext.getValue().getClass());
		assertEquals("#2179414-cm", ((Reference) ext.getValue()).getReference());
		assertEquals(ConceptMap.class, ((Reference) ext.getValue()).getResource().getClass());
		
		
		ourLog.info(ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(de));

		assertThat(output, containsString("http://hl7.org/fhir/StructureDefinition/11179-permitted-value-valueset"));

		ourLog.info("Expected: {}", input);
		ourLog.info("Actual  : {}", output);
		assertEquals(input, output);
	}
	
	
	@Test
	public void testParseBundleNewWithPlaceholderIds() {
		//@formatter:off
		String input = "<Bundle xmlns=\"http://hl7.org/fhir\">\n" + 
				"    <id value=\"ringholm1430996763590912\"/>\n" + 
				"    <entry>\n" +
				"        <fullUrl value=\"urn:oid:0.1.2.3\"/>\n" +
				"        <resource>\n" + 
				"            <Provenance>\n" + 
				"                <id value=\"0.1.2.3\"/>\n" + 
				"            </Provenance>\n" + 
				"        </resource>\n" + 
				"    </entry>\n" + 
				"</Bundle>\n";
		//@formatter:on		

		Bundle parsed = ourCtx.newXmlParser().parseResource(Bundle.class, input);
		assertEquals("urn:oid:0.1.2.3", parsed.getEntry().get(0).getResource().getIdElement().getValue());

	}

	@Test
	public void testParseBundleNewWithPlaceholderIdsInBase1() {
		//@formatter:off
		String input = "<Bundle xmlns=\"http://hl7.org/fhir\">\n" + 
				"    <id value=\"ringholm1430996763590912\"/>\n" + 
				"    <entry>\n" +
				"        <fullUrl value=\"urn:oid:0.1.2.3\"/>\n" +
				"        <resource>\n" + 
				"            <Provenance>\n" + 
				"                <id value=\"0.1.2.3\"/>\n" + 
				"            </Provenance>\n" + 
				"        </resource>\n" + 
				"    </entry>\n" + 
				"</Bundle>\n";
		//@formatter:on		

		Bundle parsed = ourCtx.newXmlParser().parseResource(Bundle.class, input);
		assertEquals("urn:oid:0.1.2.3", parsed.getEntry().get(0).getResource().getIdElement().getValue());
	}

	@Test
	public void testParseBundleNewWithPlaceholderIdsInBase2() {
		//@formatter:off
		String input = "<Bundle xmlns=\"http://hl7.org/fhir\">\n" + 
				"    <id value=\"ringholm1430996763590912\"/>\n" + 
				"    <entry>\n" +
				"        <fullUrl value=\"urn:uuid:0.1.2.3\"/>\n" +
				"        <resource>\n" + 
				"            <Provenance>\n" + 
				"                <id value=\"0.1.2.3\"/>\n" + 
				"            </Provenance>\n" + 
				"        </resource>\n" + 
				"    </entry>\n" + 
				"</Bundle>\n";
		//@formatter:on		

		Bundle parsed = ourCtx.newXmlParser().parseResource(Bundle.class, input);
		assertEquals("urn:uuid:0.1.2.3", parsed.getEntry().get(0).getResource().getIdElement().getValue());

		//@formatter:off
		input = "<Bundle xmlns=\"http://hl7.org/fhir\">\n" + 
				"    <id value=\"ringholm1430996763590912\"/>\n" + 
				"    <entry>\n" +
				"        <fullUrl value=\"urn:uuid:0.1.2.3\"/>\n" +
				"        <resource>\n" + 
				"            <Provenance>\n" + 
				"                <id value=\"0.1.2.3\"/>\n" + 
				"            </Provenance>\n" + 
				"        </resource>\n" + 
				"    </entry>\n" + 
				"</Bundle>\n";
		//@formatter:on		

		parsed = ourCtx.newXmlParser().parseResource(Bundle.class, input);
		assertEquals("urn:uuid:0.1.2.3", parsed.getEntry().get(0).getResource().getIdElement().getValue());

	}

	@Test
	public void testParseBundleOldStyleWithUnknownLinks() throws Exception {
		//@formatter:off
		String bundle = "<Bundle xmlns=\"http://hl7.org/fhir\">\n" + 
			"   <base value=\"http://foo/fhirBase1\"/>\n" + 
			"   <total value=\"1\"/>\n" + 
			"   <link>\n" + 
			"      <relation value=\"foo\"/>\n" + 
			"      <url value=\"http://localhost:52788/Binary?_pretty=true\"/>\n" + 
			"   </link>\n" + 
			"   <entry>\n" + 
			"   <link>\n" + 
			"      <relation value=\"bar\"/>\n" + 
			"      <url value=\"http://localhost:52788/Binary?_pretty=true\"/>\n" + 
			"   </link>\n" + 
			"      <resource>\n" + 
			"         <Patient xmlns=\"http://hl7.org/fhir\">\n" + 
			"            <id value=\"1\"/>\n" + 
			"            <meta>\n" +
			"               <versionId value=\"2\"/>\n" +
			"               <lastUpdated value=\"2001-02-22T11:22:33-05:00\"/>\n" +
			"            </meta>\n" + 
			"            <birthDate value=\"2012-01-02\"/>\n" + 
			"         </Patient>\n" + 
			"      </resource>\n" + 
			"   </entry>\n" + 
			"</Bundle>";
		//@formatter:on

		Bundle b = (Bundle) ourCtx.newXmlParser().parseResource(bundle);
		assertEquals(1, b.getEntry().size());

	}

	@Test
	public void testParseBundleOldWithPlaceholderIds() {
		//@formatter:off
		String input = "<Bundle xmlns=\"http://hl7.org/fhir\">\n" + 
				"    <id value=\"ringholm1430996763590912\"/>\n" + 
				"    <entry>\n" +
				"        <fullUrl value=\"urn:oid:0.1.2.3\"/>\n" +
				"        <resource>\n" + 
				"            <Provenance>\n" + 
				"                <id value=\"0.1.2.3\"/>\n" + 
				"            </Provenance>\n" + 
				"        </resource>\n" + 
				"    </entry>\n" + 
				"</Bundle>\n";
		//@formatter:on		

		Bundle parsed = (Bundle) ourCtx.newXmlParser().parseResource(input);
		assertEquals("urn:oid:0.1.2.3", parsed.getEntry().get(0).getResource().getId());

	}

	@Test
	public void testParseBundleWithBinary() {
		// TODO: implement this test, make sure we handle ID and meta correctly in Binary
	}

	/**
	 * See #191
	 */
	@Test
	public void testParseBundleWithLinksOfUnknownRelation() throws Exception {
		String input = IOUtils.toString(XmlParserDstu3Test.class.getResourceAsStream("/bundle_orion.xml"));
		Bundle parsed = ourCtx.newXmlParser().parseResource(Bundle.class, input);

		BundleLinkComponent link = parsed.getLink().get(0);
		assertEquals("just trying add link", link.getRelation());
		assertEquals("blarion", link.getUrl());

		BundleEntryComponent entry = parsed.getEntry().get(0);
		link = entry.getLink().get(0);
		assertEquals("orionhealth.edit", link.getRelation());
		assertEquals("Observation", link.getUrl());
	}

	@Test
	public void testParseBundleWithResourceId() {
		//@formatter:off
		String input = "<Bundle xmlns=\"http://hl7.org/fhir\">"
				+ "<entry><fullUrl value=\"http://localhost:58402/fhir/context/Patient/1\"/><resource><Patient xmlns=\"http://hl7.org/fhir\"><id value=\"1\"/><meta><versionId value=\"3\"/><lastUpdated value=\"2015-09-11T23:35:43.273-04:00\"/></meta><name><family value=\"testHistoryWithDeletedResource\"/></name></Patient></resource></entry>"
				+ "<entry><fullUrl value=\"http://localhost:58402/fhir/context/Patient/1\"/><resource><Patient xmlns=\"http://hl7.org/fhir\"><id value=\"1\"/><meta><versionId value=\"2\"/><lastUpdated value=\"2015-09-11T23:35:42.849-04:00\"/></meta><name><family value=\"testHistoryWithDeletedResource\"/></name></Patient></resource></entry>"
				+ "<entry><fullUrl value=\"http://localhost:58402/fhir/context/Patient/1\"/><resource><Patient xmlns=\"http://hl7.org/fhir\"><id value=\"1\"/><meta><versionId value=\"1\"/><lastUpdated value=\"2015-09-11T23:35:42.295-04:00\"/></meta><name><family value=\"testHistoryWithDeletedResource\"/></name></Patient></resource></entry>"
				+ "</Bundle>\n";
		//@formatter:on

		Bundle bundle = ourCtx.newXmlParser().parseResource(Bundle.class, input);
		assertEquals("http://localhost:58402/fhir/context/Patient/1/_history/3", bundle.getEntry().get(0).getResource().getIdElement().getValue());
		assertEquals("http://localhost:58402/fhir/context/Patient/1/_history/2", bundle.getEntry().get(1).getResource().getIdElement().getValue());
		assertEquals("http://localhost:58402/fhir/context/Patient/1/_history/1", bundle.getEntry().get(2).getResource().getIdElement().getValue());
	}

	/**
	 * see #144 and #146
	 */
	@Test @Ignore
	public void testParseContained() {

		FhirContext c = FhirContext.forDstu3();
		IParser parser = c.newXmlParser().setPrettyPrint(true);

		Observation o = new Observation();
		o.getCode().setText("obs text");

		Patient p = new Patient();
		p.addName().addFamily("patient family");
		o.getSubject().setResource(p);

		String enc = parser.encodeResourceToString(o);
		ourLog.info(enc);

		//@formatter:off
		assertThat(enc, stringContainsInOrder(
			"<Observation xmlns=\"http://hl7.org/fhir\">",
			"<contained>",
			"<Patient xmlns=\"http://hl7.org/fhir\">",
			"<id value=\"1\"/>",
			"</contained>",
			"<reference value=\"#1\"/>"
			));
		//@formatter:on

		o = parser.parseResource(Observation.class, enc);
		assertEquals("obs text", o.getCode().getText());

		assertNotNull(o.getSubject().getResource());
		p = (Patient) o.getSubject().getResource();
		assertEquals("patient family", p.getName().get(0).getFamily().get(0).getValue());
	}

	/**
	 * Thanks to Alexander Kley!
	 */
	@Test
	public void testParseContainedBinaryResource() {
		byte[] bin = new byte[] { 0, 1, 2, 3, 4 };
		final Binary binary = new Binary();
		binary.setContentType("PatientConsent").setContent(bin);

		DocumentManifest manifest = new DocumentManifest();
		CodeableConcept cc = new CodeableConcept();
		cc.addCoding().setSystem("mySystem").setCode("PatientDocument");
		manifest.setType(cc);
		manifest.setMasterIdentifier(new Identifier().setSystem("mySystem").setValue(UUID.randomUUID().toString()));
		manifest.addContent().setP(new Reference(binary));
		manifest.setStatus(DocumentReferenceStatus.CURRENT);

		String encoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(manifest);
		ourLog.info(encoded);
		assertThat(encoded, StringContainsInOrder.stringContainsInOrder(Arrays.asList("contained>", "<Binary", "</contained>")));

		DocumentManifest actual = ourCtx.newXmlParser().parseResource(DocumentManifest.class, encoded);
		assertEquals(1, actual.getContained().size());
		assertEquals(1, actual.getContent().size());

		/*
		 * If this fails, it's possibe the DocumentManifest structure is wrong:
		 * It should be
		 *         @Child(name = "p", type = {Attachment.class, ValueSet.class}, order=1, min=1, max=1, modifier=false, summary=true)
		 */
		assertNotNull(((Reference) actual.getContent().get(0).getP()).getResource());
	}

	@Test
	public void testParseInvalidTextualNumber() {
		Observation obs = new Observation();
		obs.setValue(new Quantity().setValue(1234));
		String encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(obs);
		encoded = encoded.replace("1234", "\"1234\"");
		ourLog.info(encoded);
		ourCtx.newJsonParser().parseResource(encoded);
	}

	/**
	 * See #216
	 */
	@Test
	public void testParseMalformedIdentifierDstu2() {

		// This was changed from 0.5 to 1.0.0

		//@formatter:off
		String out = "<Patient xmlns=\"http://hl7.org/fhir\">\n" + 
				"   <identifier>\n" + 
				"      <type value=\"MRN\"/>\n" + 
				"      <system value=\"SYS\"/>\n" + 
				"      <value value=\"VAL\"/>\n" + 
				"   </identifier>\n" + 
				"</Patient>";
		//@formatter:on

		IParserErrorHandler errorHandler = mock(IParserErrorHandler.class);

		IParser p = ourCtx.newXmlParser();
		p.setParserErrorHandler(errorHandler);

		Patient patient = p.parseResource(Patient.class, out);
		assertThat(patient.getIdentifier().get(0).getType().getCoding(), IsEmptyCollection.empty());

		ArgumentCaptor<String> capt = ArgumentCaptor.forClass(String.class);
		verify(errorHandler, times(1)).unknownAttribute(any(IParseLocation.class), capt.capture());

		assertEquals("value", capt.getValue());
	}

	@Test
	public void testParseMetadata() throws Exception {
		//@formatter:off
		String bundle = "<Bundle xmlns=\"http://hl7.org/fhir\">\n" + 
			"   <total value=\"1\"/>\n" + 
			"   <link>\n" + 
			"      <relation value=\"self\"/>\n" + 
			"      <url value=\"http://localhost:52788/Binary?_pretty=true\"/>\n" + 
			"   </link>\n" + 
			"   <entry>\n" + 
			"      <fullUrl value=\"http://foo/fhirBase2/Patient/1/_history/2\"/>\n" + 
			"      <resource>\n" + 
			"         <Patient xmlns=\"http://hl7.org/fhir\">\n" + 
			"            <id value=\"1\"/>\n" + 
			"            <meta>\n" +
			"               <versionId value=\"2\"/>\n" +
			"               <lastUpdated value=\"2001-02-22T09:22:33-07:00\"/>\n" +
			"            </meta>\n" + 
			"            <birthDate value=\"2012-01-02\"/>\n" + 
			"         </Patient>\n" + 
			"      </resource>\n" + 
			"      <search>\n" +
			"         <mode value=\"match\"/>\n" +
			"         <score value=\"0.123\"/>\n" +
			"      </search>\n" +
			"      <request>\n" +
			"         <method value=\"POST\"/>\n" +
			"         <url value=\"http://foo/Patient?identifier=value\"/>\n" +
			"      </request>\n" +
			"   </entry>\n" + 
			"</Bundle>";
		//@formatter:on

		Bundle b = ourCtx.newXmlParser().parseResource(Bundle.class, bundle);
		assertEquals(1, b.getEntry().size());

		BundleEntryComponent entry = b.getEntry().get(0);
		Patient pt = (Patient) entry.getResource();
		assertEquals("http://foo/fhirBase2/Patient/1/_history/2", pt.getIdElement().getValue());
		assertEquals("2012-01-02", pt.getBirthDateElement().getValueAsString());
		assertEquals("0.123", entry.getSearch().getScore().toString());
		assertEquals("match", entry.getSearch().getMode().toCode());
		assertEquals("POST", entry.getRequest().getMethod().toCode());
		assertEquals("http://foo/Patient?identifier=value", entry.getRequest().getUrl());
		assertEquals("2001-02-22T09:22:33-07:00", pt.getMeta().getLastUpdatedElement().getValueAsString());

		IParser p = ourCtx.newXmlParser().setPrettyPrint(true);
		String reEncoded = p.encodeResourceToString(b);
		ourLog.info(reEncoded);

		Diff d = new Diff(new StringReader(bundle), new StringReader(reEncoded));
		assertTrue(d.toString(), d.identical());

	}

	@Test
	public void testParseMetaUpdatedDate() {
		//@formatter:off
		String input = "<Bundle xmlns=\"http://hl7.org/fhir\">\n" + 
				"   <id value=\"e2ee823b-ee4d-472d-b79d-495c23f16b99\"/>\n" + 
				"   <meta>\n" + 
				"      <lastUpdated value=\"2015-06-22T15:48:57.554-04:00\"/>\n" + 
				"   </meta>\n" + 
				"   <type value=\"searchset\"/>\n" + 
				"   <base value=\"http://localhost:58109/fhir/context\"/>\n" + 
				"   <total value=\"0\"/>\n" + 
				"   <link>\n" + 
				"      <relation value=\"self\"/>\n" + 
				"      <url value=\"http://localhost:58109/fhir/context/Patient?_pretty=true\"/>\n" + 
				"   </link>\n" + 
				"</Bundle>";
		//@formatter:on
		Bundle b = ourCtx.newXmlParser().parseResource(Bundle.class, input);

		InstantType updated = b.getMeta().getLastUpdatedElement();
		assertEquals("2015-06-22T15:48:57.554-04:00", updated.getValueAsString());

	}

	//TODO: this should work
	@Test
	@Ignore
	public void testParseNarrative() throws Exception {
		//@formatter:off
		String htmlNoNs = "<div>AAA<b>BBB</b>CCC</div>";
		String htmlNs = htmlNoNs.replace("<div>", "<div xmlns=\"http://www.w3.org/1999/xhtml\">"); 
		String res= "<Patient xmlns=\"http://hl7.org/fhir\">\n" + 
				"   <id value=\"1333\"/>\n" + 
				"   <text>\n" + 
				"      " + htmlNs + "\n" +
				"   </text>\n" + 
				"</Patient>";
		//@formatter:on

		Patient p = ourCtx.newXmlParser().parseResource(Patient.class, res);
		assertEquals(htmlNs, p.getText().getDiv().getValueAsString());
	}

	/**
	 * See #163
	 */
	@Test
	public void testParseResourceType() {
		IParser xmlParser = ourCtx.newXmlParser().setPrettyPrint(true);

		// Patient
		Patient patient = new Patient();
		String patientId = UUID.randomUUID().toString();
		patient.setId(new IdType("Patient", patientId));
		patient.addName().addGiven("John").addFamily("Smith");
		patient.setGender(AdministrativeGender.MALE);
		patient.setBirthDateElement(new DateType("1987-04-16"));

		// Bundle
		Bundle bundle = new Bundle();
		bundle.setType(BundleType.COLLECTION);
		bundle.addEntry().setResource(patient);

		String bundleText = xmlParser.encodeResourceToString(bundle);
		ourLog.info(bundleText);

		Bundle reincarnatedBundle = xmlParser.parseResource(Bundle.class, bundleText);
		Patient reincarnatedPatient = (Patient) reincarnatedBundle.getEntry().get(0).getResource();

		assertEquals("Patient", patient.getIdElement().getResourceType());
		assertEquals("Patient", reincarnatedPatient.getIdElement().getResourceType());
	}

	@BeforeClass
	public static void beforeClass() {
		XMLUnit.setIgnoreAttributeOrder(true);
		XMLUnit.setIgnoreComments(true);
		XMLUnit.setIgnoreWhitespace(true);
	}

	public static void main(String[] args) {
		IGenericClient c = ourCtx.newRestfulGenericClient("http://fhir-dev.healthintersections.com.au/open");
		// c.registerInterceptor(new LoggingInterceptor(true));
		c.read().resource("Patient").withId("324").execute();
	}

}
