package ca.uhn.fhir.parser;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.emptyOrNullString;
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
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.ExtensionDt;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.api.Tag;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.base.composite.BaseCodingDt;
import ca.uhn.fhir.model.dstu2.composite.AnnotationDt;
import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.composite.ContainedDt;
import ca.uhn.fhir.model.dstu2.composite.DurationDt;
import ca.uhn.fhir.model.dstu2.composite.ElementDefinitionDt;
import ca.uhn.fhir.model.dstu2.composite.ElementDefinitionDt.Binding;
import ca.uhn.fhir.model.dstu2.composite.HumanNameDt;
import ca.uhn.fhir.model.dstu2.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu2.composite.QuantityDt;
import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu2.composite.SimpleQuantityDt;
import ca.uhn.fhir.model.dstu2.resource.AllergyIntolerance;
import ca.uhn.fhir.model.dstu2.resource.Binary;
import ca.uhn.fhir.model.dstu2.resource.Bundle.Entry;
import ca.uhn.fhir.model.dstu2.resource.Bundle.Link;
import ca.uhn.fhir.model.dstu2.resource.Composition;
import ca.uhn.fhir.model.dstu2.resource.DataElement;
import ca.uhn.fhir.model.dstu2.resource.DiagnosticReport;
import ca.uhn.fhir.model.dstu2.resource.Encounter;
import ca.uhn.fhir.model.dstu2.resource.Medication;
import ca.uhn.fhir.model.dstu2.resource.MedicationOrder;
import ca.uhn.fhir.model.dstu2.resource.MedicationStatement;
import ca.uhn.fhir.model.dstu2.resource.Observation;
import ca.uhn.fhir.model.dstu2.resource.Organization;
import ca.uhn.fhir.model.dstu2.resource.Parameters;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.valueset.AddressUseEnum;
import ca.uhn.fhir.model.dstu2.valueset.AdministrativeGenderEnum;
import ca.uhn.fhir.model.dstu2.valueset.BundleTypeEnum;
import ca.uhn.fhir.model.dstu2.valueset.ContactPointSystemEnum;
import ca.uhn.fhir.model.dstu2.valueset.DocumentReferenceStatusEnum;
import ca.uhn.fhir.model.dstu2.valueset.IdentifierTypeCodesEnum;
import ca.uhn.fhir.model.dstu2.valueset.IdentifierUseEnum;
import ca.uhn.fhir.model.dstu2.valueset.MaritalStatusCodesEnum;
import ca.uhn.fhir.model.dstu2.valueset.NameUseEnum;
import ca.uhn.fhir.model.dstu2.valueset.ObservationRelationshipTypeEnum;
import ca.uhn.fhir.model.dstu2.valueset.ObservationStatusEnum;
import ca.uhn.fhir.model.primitive.DateDt;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.primitive.MarkdownDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.parser.IParserErrorHandler.IParseLocation;
import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.rest.server.Constants;

public class XmlParserDstu2Test {
	private static final FhirContext ourCtx = FhirContext.forDstu2();
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(XmlParserDstu2Test.class);

	
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

		Bundle b = ourCtx.newXmlParser().parseBundle(bundle);
		assertEquals(1, b.getEntries().size());

		Binary bin = (Binary) b.getEntries().get(0).getResource();
		assertArrayEquals(new byte[] { 1, 2, 3, 4 }, bin.getContent());

	}

	@Test
	public void testChoiceTypeWithProfiledType() {
		//@formatter:off
		String input = "<Patient xmlns=\"http://hl7.org/fhir\">\n" + 
			"        <extension url=\"http://example.com\">\n" + 
			"          <valueMarkdown value=\"THIS IS MARKDOWN\"/>\n" + 
			"        </extension>\n" + 
			"</Patient>";
		//@formatter:on

		Patient parsed = ourCtx.newXmlParser().parseResource(Patient.class, input);
		assertEquals(1, parsed.getUndeclaredExtensions().size());
		ExtensionDt ext = parsed.getUndeclaredExtensions().get(0);
		assertEquals("http://example.com", ext.getUrl());
		assertEquals("THIS IS MARKDOWN", ((MarkdownDt) ext.getValue()).getValue());

		String encoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(parsed);
		assertThat(encoded, containsString("<valueMarkdown value=\"THIS IS MARKDOWN\"/>"));
	}

	@Test
	public void testChoiceTypeWithProfiledType2() {
		Parameters par = new Parameters();
		par.addParameter().setValue((StringDt) new StringDt().setValue("ST"));
		par.addParameter().setValue((MarkdownDt) new MarkdownDt().setValue("MD"));

		String str = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(par);
		ourLog.info(str);

		assertThat(str, stringContainsInOrder("<valueString value=\"ST\"/>", "<valueMarkdown value=\"MD\"/>"));

		par = ourCtx.newXmlParser().parseResource(Parameters.class, str);
		assertEquals(2, par.getParameter().size());
		assertEquals(StringDt.class, par.getParameter().get(0).getValue().getClass());
		assertEquals(MarkdownDt.class, par.getParameter().get(1).getValue().getClass());
	}

	@Test
	public void testContainedResourceInExtensionUndeclared() {
		Patient p = new Patient();
		p.addName().addFamily("PATIENT");

		Organization o = new Organization();
		o.setName("ORG");
		p.addUndeclaredExtension(new ExtensionDt(false, "urn:foo", new ResourceReferenceDt(o)));

		String str = ourCtx.newXmlParser().encodeResourceToString(p);
		ourLog.info(str);

		p = ourCtx.newXmlParser().parseResource(Patient.class, str);
		assertEquals("PATIENT", p.getName().get(0).getFamily().get(0).getValue());

		List<ExtensionDt> exts = p.getUndeclaredExtensionsByUrl("urn:foo");
		assertEquals(1, exts.size());
		ResourceReferenceDt rr = (ResourceReferenceDt) exts.get(0).getValue();
		o = (Organization) rr.getResource();
		assertEquals("ORG", o.getName());
	}

	@Test
	public void testDuration() {
		Encounter enc = new Encounter();
		DurationDt duration = new DurationDt();
		duration.setUnit("day").setValue(123L);
		enc.setLength(duration);

		String str = ourCtx.newXmlParser().encodeResourceToString(enc);
		ourLog.info(str);

		assertThat(str, not(containsString("meta")));
		assertThat(str, containsString("<length><value value=\"123\"/><unit value=\"day\"/></length>"));
	}

	@Test
	public void testEncodeAndParseBundleWithoutResourceIds() {
		Organization org = new Organization();
		org.addIdentifier().setSystem("urn:system").setValue("someval");

		Bundle bundle = Bundle.withSingleResource(org);
		String str = ourCtx.newXmlParser().encodeBundleToString(bundle);
		ourLog.info(str);

		Bundle parsed = ourCtx.newXmlParser().parseBundle(str);
		assertThat(parsed.getEntries().get(0).getResource().getId().getValue(), emptyOrNullString());
		assertTrue(parsed.getEntries().get(0).getResource().getId().isEmpty());
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
		ca.uhn.fhir.model.dstu2.resource.Bundle b = new ca.uhn.fhir.model.dstu2.resource.Bundle();
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
		assertEquals("#1", patient.getManagingOrganization().getReference().getValue());

		assertNotNull(patient.getManagingOrganization().getResource());
		org = (Organization) patient.getManagingOrganization().getResource();
		assertEquals("#1", org.getId().getValue());
		assertEquals("Contained Test Organization", org.getName());

		// And re-encode a second time
		encoded = xmlParser.encodeResourceToString(patient);
		ourLog.info(encoded);
		assertThat(encoded, stringContainsInOrder(Arrays.asList("<contained>", "<Organization ", "<id value=\"1\"/>", "</Organization", "</contained>", "<reference value=\"#1\"/>")));
		assertThat(encoded, not(stringContainsInOrder(Arrays.asList("<contained>", "<Org", "<contained>"))));
		assertThat(encoded, containsString("<reference value=\"#1\"/>"));

		// And re-encode once more, with the references cleared
		patient.getContained().getContainedResources().clear();
		patient.getManagingOrganization().setReference((String) null);
		encoded = xmlParser.encodeResourceToString(patient);
		ourLog.info(encoded);
		assertThat(encoded, stringContainsInOrder(Arrays.asList("<contained>", "<Organization ", "<id value=\"1\"/>", "</Organization", "</contained>", "<reference value=\"#1\"/>")));
		assertThat(encoded, not(stringContainsInOrder(Arrays.asList("<contained>", "<Org", "<contained>"))));
		assertThat(encoded, containsString("<reference value=\"#1\"/>"));

		// And re-encode once more, with the references cleared and a manually set local ID
		patient.getContained().getContainedResources().clear();
		patient.getManagingOrganization().setReference((String) null);
		patient.getManagingOrganization().getResource().setId(("#333"));
		encoded = xmlParser.encodeResourceToString(patient);
		ourLog.info(encoded);
		assertThat(encoded, stringContainsInOrder(Arrays.asList("<contained>", "<Organization ", "<id value=\"333\"/>", "</Organization", "</contained>", "<reference value=\"#333\"/>")));
		assertThat(encoded, not(stringContainsInOrder(Arrays.asList("<contained>", "<Org", "<contained>"))));

	}

	@Test
	public void testEncodeAndParseExtensionOnResourceReference() {
		DataElement de = new DataElement();
		Binding b = de.addElement().getBinding();
		b.setDescription("BINDING");

		Organization o = new Organization();
		o.setName("ORG");
		b.addUndeclaredExtension(new ExtensionDt(false, "urn:foo", new ResourceReferenceDt(o)));

		String str = ourCtx.newXmlParser().encodeResourceToString(de);
		ourLog.info(str);

		de = ourCtx.newXmlParser().parseResource(DataElement.class, str);
		b = de.getElement().get(0).getBinding();
		assertEquals("BINDING", b.getDescription());

		List<ExtensionDt> exts = b.getUndeclaredExtensionsByUrl("urn:foo");
		assertEquals(1, exts.size());
		ResourceReferenceDt rr = (ResourceReferenceDt) exts.get(0).getValue();
		o = (Organization) rr.getResource();
		assertEquals("ORG", o.getName());

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

	/**
	 * See #216
	 */
	@Test
	public void testEncodeAndParseIdentifierDstu2() {
		IParser xmlParser = ourCtx.newXmlParser().setPrettyPrint(true);

		Patient patient = new Patient();
		patient.addIdentifier().setSystem("SYS").setValue("VAL").setType(IdentifierTypeCodesEnum.MR);

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
		assertThat(patient.getIdentifier().get(0).getType().getValueAsEnum(), contains(IdentifierTypeCodesEnum.MR));
		assertEquals("http://hl7.org/fhir/v2/0203", patient.getIdentifier().get(0).getType().getCoding().get(0).getSystem());
		assertEquals("MR", patient.getIdentifier().get(0).getType().getCoding().get(0).getCode());
	}

	@Test
	public void testEncodeAndParseMetaProfileAndTags() {
		Patient p = new Patient();
		p.addName().addFamily("FAMILY");

		List<IdDt> profiles = new ArrayList<IdDt>();
		profiles.add(new IdDt("http://foo/Profile1"));
		profiles.add(new IdDt("http://foo/Profile2"));
		ResourceMetadataKeyEnum.PROFILES.put(p, profiles);

		TagList tagList = new TagList();
		tagList.addTag("scheme1", "term1", "label1");
		tagList.addTag("scheme2", "term2", "label2");
		ResourceMetadataKeyEnum.TAG_LIST.put(p, tagList);

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
		List<IdDt> gotLabels = ResourceMetadataKeyEnum.PROFILES.get(parsed);

		assertEquals(2, gotLabels.size());

		IdDt label = (IdDt) gotLabels.get(0);
		assertEquals("http://foo/Profile1", label.getValue());
		label = (IdDt) gotLabels.get(1);
		assertEquals("http://foo/Profile2", label.getValue());

		tagList = ResourceMetadataKeyEnum.TAG_LIST.get(parsed);
		assertEquals(2, tagList.size());

		assertEquals(new Tag("scheme1", "term1", "label1"), tagList.get(0));
		assertEquals(new Tag("scheme2", "term2", "label2"), tagList.get(1));
	}

	@Test
	public void testEncodeAndParseMetaProfiles() {
		Patient p = new Patient();
		p.addName().addFamily("FAMILY");

		TagList tagList = new TagList();
		tagList.addTag("scheme1", "term1", "label1");
		tagList.addTag("scheme2", "term2", "label2");
		ResourceMetadataKeyEnum.TAG_LIST.put(p, tagList);

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
		List<IdDt> gotLabels = ResourceMetadataKeyEnum.PROFILES.get(parsed);
		assertNull(gotLabels);

		tagList = ResourceMetadataKeyEnum.TAG_LIST.get(parsed);
		assertEquals(2, tagList.size());

		assertEquals(new Tag("scheme1", "term1", "label1"), tagList.get(0));
		assertEquals(new Tag("scheme2", "term2", "label2"), tagList.get(1));
	}

	/**
	 * Test for #233
	 */
	@Test
	public void testEncodeAndParseProfiledDatatype() {
		MedicationOrder mo = new MedicationOrder();
		mo.addDosageInstruction().getTiming().getRepeat().setBounds(new DurationDt().setCode("code"));
		String out = ourCtx.newXmlParser().encodeResourceToString(mo);
		ourLog.info(out);
		assertThat(out, containsString("</boundsQuantity>"));

		mo = ourCtx.newXmlParser().parseResource(MedicationOrder.class, out);
		DurationDt duration = (DurationDt) mo.getDosageInstruction().get(0).getTiming().getRepeat().getBounds();
		assertEquals("code", duration.getCode());
	}

	/**
	 * See #216 - Profiled datatypes should use their unprofiled parent type as the choice[x] name
	 * 
	 * Disabled because we reverted this change after a conversation with Grahame
	 */
	@Test
	public void testEncodeAndParseProfiledDatatypeChoice() throws Exception {
		IParser xmlParser = ourCtx.newXmlParser();

		String input = IOUtils.toString(XmlParser.class.getResourceAsStream("/medicationstatement_invalidelement.xml"));
		MedicationStatement ms = xmlParser.parseResource(MedicationStatement.class, input);
		SimpleQuantityDt q = (SimpleQuantityDt) ms.getDosage().get(0).getQuantity();
		assertEquals("1", q.getValueElement().getValueAsString());

		String output = xmlParser.encodeResourceToString(ms);
		assertThat(output, containsString("<quantityQuantity><value value=\"1\"/></quantityQuantity>"));
	}

	@Test
	public void testEncodeAndParseSecurityLabels() {
		Patient p = new Patient();
		p.addName().addFamily("FAMILY");

		List<BaseCodingDt> labels = new ArrayList<BaseCodingDt>();
		labels.add(new CodingDt().setSystem("SYSTEM1").setCode("CODE1").setDisplay("DISPLAY1").setUserSelected(true).setVersion("VERSION1"));
		labels.add(new CodingDt().setSystem("SYSTEM2").setCode("CODE2").setDisplay("DISPLAY2").setUserSelected(false).setVersion("VERSION2"));

		ResourceMetadataKeyEnum.SECURITY_LABELS.put(p, labels);

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
			"<userSelected value=\"true\"/>", 
			"</security>", 
			"<security>", 
			"<system value=\"SYSTEM2\"/>", 
			"<version value=\"VERSION2\"/>", 
			"<code value=\"CODE2\"/>", 
			"<display value=\"DISPLAY2\"/>", 
			"<userSelected value=\"false\"/>", 
			"</security>",
			"</meta>", 
			"<name>", 
			"<family value=\"FAMILY\"/>", 
			"</name>", 
			"</Patient>"));
		//@formatter:on

		Patient parsed = ourCtx.newXmlParser().parseResource(Patient.class, enc);
		List<BaseCodingDt> gotLabels = ResourceMetadataKeyEnum.SECURITY_LABELS.get(parsed);

		assertEquals(2, gotLabels.size());

		CodingDt label = (CodingDt) gotLabels.get(0);
		assertEquals("SYSTEM1", label.getSystem());
		assertEquals("CODE1", label.getCode());
		assertEquals("DISPLAY1", label.getDisplay());
		assertEquals(true, label.getUserSelected());
		assertEquals("VERSION1", label.getVersion());

		label = (CodingDt) gotLabels.get(1);
		assertEquals("SYSTEM2", label.getSystem());
		assertEquals("CODE2", label.getCode());
		assertEquals("DISPLAY2", label.getDisplay());
		assertEquals(false, label.getUserSelected());
		assertEquals("VERSION2", label.getVersion());
	}

	/**
	 * See #103
	 */
	@Test
	public void testEncodeAndReEncodeContainedJson() {
		Composition comp = new Composition();
		comp.addSection().addEntry().setResource(new AllergyIntolerance().setNote(new AnnotationDt().setText("Section0_Allergy0")));
		comp.addSection().addEntry().setResource(new AllergyIntolerance().setNote(new AnnotationDt().setText("Section1_Allergy0")));
		comp.addSection().addEntry().setResource(new AllergyIntolerance().setNote(new AnnotationDt().setText("Section2_Allergy0")));

		IParser parser = ourCtx.newJsonParser().setPrettyPrint(true);

		String string = parser.encodeResourceToString(comp);
		ourLog.info(string);

		Composition parsed = parser.parseResource(Composition.class, string);
		parsed.getSection().remove(0);

		string = parser.encodeResourceToString(parsed);
		ourLog.info(string);

		parsed = parser.parseResource(Composition.class, string);
		assertEquals(2, parsed.getContained().getContainedResources().size());
	}

	/**
	 * See #103
	 */
	@Test
	public void testEncodeAndReEncodeContainedXml() {
		Composition comp = new Composition();
		comp.addSection().addEntry().setResource(new AllergyIntolerance().setNote(new AnnotationDt().setText("Section0_Allergy0")));
		comp.addSection().addEntry().setResource(new AllergyIntolerance().setNote(new AnnotationDt().setText("Section1_Allergy0")));
		comp.addSection().addEntry().setResource(new AllergyIntolerance().setNote(new AnnotationDt().setText("Section2_Allergy0")));

		IParser parser = ourCtx.newXmlParser().setPrettyPrint(true);

		String string = parser.encodeResourceToString(comp);
		ourLog.info(string);

		Composition parsed = parser.parseResource(Composition.class, string);
		parsed.getSection().remove(0);

		string = parser.encodeResourceToString(parsed);
		ourLog.info(string);

		parsed = parser.parseResource(Composition.class, string);
		assertEquals(2, parsed.getContained().getContainedResources().size());
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
	public void testEncodeBundleOldStyleContainingResourceWithUuidBase() {
		Patient p = new Patient();
		p.setId(IdDt.newRandomUuid());
		p.addName().addFamily("PATIENT");

		Bundle b = new Bundle();
		b.addEntry().setResource(p);

		String encoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeBundleToString(b);
		ourLog.info(encoded);
		assertThat(encoded, stringContainsInOrder("<Bundle", "<entry>", "<fullUrl value=\"" + p.getId().getValue() + "\"/>", "<Patient"));
		assertThat(encoded, not(containsString("<id value=\"" + p.getId().getIdPart() + "\"/>")));
	}

	@Test
	public void testEncodeBundleOldStyleContainingResourceWithUuidBaseBundleBaseIsSet() {
		Patient p = new Patient();
		p.setId(IdDt.newRandomUuid());
		p.addName().addFamily("PATIENT");

		Bundle b = new Bundle();
		b.getLinkBase().setValue("urn:uuid:");
		b.addEntry().setResource(p);

		String encoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeBundleToString(b);
		ourLog.info(encoded);
		// Base element has been removed!
		assertThat(encoded, not(stringContainsInOrder("<Bundle", "<entry>", "<base value=\"", "<Patient", "<id value=")));
	}

	@Test
	public void testEncodeBundleWithContained() {
		DiagnosticReport rpt = new DiagnosticReport();
		rpt.addResult().setResource(new Observation().setCode(new CodeableConceptDt().setText("Sharp1")).setId("#1"));
		rpt.addResult().setResource(new Observation().setCode(new CodeableConceptDt().setText("Uuid1")).setId("urn:uuid:UUID1"));

		ca.uhn.fhir.model.dstu2.resource.Bundle b = new ca.uhn.fhir.model.dstu2.resource.Bundle();
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
		CodeableConceptDt codeDt = new CodeableConceptDt("urn:sys", "code1");

		// Adding medication to Contained.
		Medication medResource = new Medication();
		medResource.setCode(codeDt);
		medResource.setId("#" + String.valueOf(medId));
		ArrayList<IResource> medResList = new ArrayList<IResource>();
		medResList.add(medResource);
		ContainedDt medContainedDt = new ContainedDt();
		medContainedDt.setContainedResources(medResList);
		medicationPrescript.setContained(medContainedDt);

		// Medication reference. This should point to the contained resource.
		ResourceReferenceDt medRefDt = new ResourceReferenceDt("#" + medId);
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
		CodeableConceptDt codeDt = new CodeableConceptDt("urn:sys", "code1");
		
		// Adding medication to Contained.
		Medication medResource = new Medication();
		// No ID set
		medResource.setCode(codeDt);

		// Medication reference. This should point to the contained resource.
		ResourceReferenceDt medRefDt = new ResourceReferenceDt();
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
		CodeableConceptDt codeDt = new CodeableConceptDt("urn:sys", "code1");

		// Adding medication to Contained.
		Medication medResource = new Medication();
		medResource.setCode(codeDt);
		medResource.setId(String.valueOf(medId)); // ID does not start with '#'
		ArrayList<IResource> medResList = new ArrayList<IResource>();
		medResList.add(medResource);
		ContainedDt medContainedDt = new ContainedDt();
		medContainedDt.setContainedResources(medResList);
		medicationPrescript.setContained(medContainedDt);

		// Medication reference. This should point to the contained resource.
		ResourceReferenceDt medRefDt = new ResourceReferenceDt("#" + medId);
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
		org.getText().setDiv("<div>FOOBAR</div>");

		// Create a patient
		Patient patient = new Patient();
		patient.setId("Patient/1333");
		patient.addIdentifier().setSystem("urn:mrns").setValue("253345");
		patient.getText().setDiv("<div>BARFOO</div>");
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
		p.setId(new IdDt("urn:uuid:42795ed8-041f-4ebf-b6f4-78ef6f64c2f2"));
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
		TagList tagList = new TagList();
		tagList.addTag(null, null, null);
		tagList.addTag(null, null, "Label");
		
		Patient p = new Patient();
		ResourceMetadataKeyEnum.TAG_LIST.put(p, tagList);
		
		String encoded = ourCtx.newXmlParser().encodeResourceToString(p);
		assertThat(encoded, not(containsString("tag")));
	}

	/**
	 * #158
	 */
	@Test
	public void testEncodeEmptyTag2() {
		TagList tagList = new TagList();
		tagList.addTag("scheme", "code", null);
		tagList.addTag(null, null, "Label");
		
		Patient p = new Patient();
		ResourceMetadataKeyEnum.TAG_LIST.put(p, tagList);
		
		String encoded = ourCtx.newXmlParser().encodeResourceToString(p);
		assertThat(encoded, containsString("tag"));
		assertThat(encoded, containsString("scheme"));
		assertThat(encoded, not(containsString("Label")));
	}
	
	@Test
	public void testEncodeExtensionWithResourceContent() {
		IParser parser = ourCtx.newXmlParser();

		Patient patient = new Patient();
		patient.addAddress().setUse(AddressUseEnum.HOME);
		patient.addUndeclaredExtension(false, "urn:foo", new ResourceReferenceDt("Organization/123"));

		String val = parser.encodeResourceToString(patient);
		ourLog.info(val);
		assertThat(val, StringContains.containsString("<extension url=\"urn:foo\"><valueReference><reference value=\"Organization/123\"/></valueReference></extension>"));

		Patient actual = parser.parseResource(Patient.class, val);
		assertEquals(AddressUseEnum.HOME.getCode(), patient.getAddress().get(0).getUse());
		List<ExtensionDt> ext = actual.getUndeclaredExtensions();
		assertEquals(1, ext.size());
		ResourceReferenceDt ref = (ResourceReferenceDt) ext.get(0).getValue();
		assertEquals("Organization/123", ref.getReference().getValue());

	}

	
	@Test
	public void testEncodeNarrativeSuppressed() {
		Patient patient = new Patient();
		patient.setId("Patient/1/_history/1");
		patient.getText().setDiv("<div>THE DIV</div>");
		patient.addName().addFamily("FAMILY");
		patient.setMaritalStatus(MaritalStatusCodesEnum.D);

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
		ca.uhn.fhir.model.dstu2.resource.Bundle b = new ca.uhn.fhir.model.dstu2.resource.Bundle();
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
		patient.addName().addGiven("Patty").setUse(NameUseEnum.NICKNAME);
		patient.addTelecom().setSystem(ContactPointSystemEnum.EMAIL).setValue("patpain@ehealthinnovation.org");
		patient.setGender(AdministrativeGenderEnum.FEMALE);
		patient.setBirthDate(new DateDt("2001-10-13"));

		DateTimeDt obsEffectiveTime = new DateTimeDt("2015-04-11T12:22:01-04:00");

		Observation obsParent = new Observation();
		obsParent.setId("phitcc_obs_bp_parent");
		obsParent.getSubject().setResource(patient);
		obsParent.setStatus(ObservationStatusEnum.FINAL);
		obsParent.setEffective(obsEffectiveTime);

		Observation obsSystolic = new Observation();
		obsSystolic.setId("phitcc_obs_bp_dia");
		obsSystolic.getSubject().setResource(patient);
		obsSystolic.setEffective(obsEffectiveTime);
		obsParent.addRelated().setType(ObservationRelationshipTypeEnum.HAS_MEMBER).setTarget(new ResourceReferenceDt(obsSystolic));

		Observation obsDiastolic = new Observation();
		obsDiastolic.setId("phitcc_obs_bp_dia");
		obsDiastolic.getSubject().setResource(patient);
		obsDiastolic.setEffective(obsEffectiveTime);
		obsParent.addRelated().setType(ObservationRelationshipTypeEnum.HAS_MEMBER).setTarget(new ResourceReferenceDt(obsDiastolic));
		
		String str = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(obsParent);
		ourLog.info(str);
		
		assertThat(str, containsString("<reference value=\"Patient/phitcc_pat_normal\"/>"));
		assertThat(str, containsString("<reference value=\"Observation/phitcc_obs_bp_dia\"/>"));
	}

	@Test
	public void testEncodeSummary() {
		Patient patient = new Patient();
		patient.setId("Patient/1/_history/1");
		patient.getText().setDiv("<div>THE DIV</div>");
		patient.addName().addFamily("FAMILY");
		patient.setMaritalStatus(MaritalStatusCodesEnum.D);

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
		patient.getText().setDiv("<div>THE DIV</div>");
		patient.addName().addFamily("FAMILY");
		patient.setMaritalStatus(MaritalStatusCodesEnum.D);

		TagList tl = new TagList();
		tl.add(new Tag("foo", "bar"));
		ResourceMetadataKeyEnum.TAG_LIST.put(patient, tl);
		
		String encoded = ourCtx.newXmlParser().setPrettyPrint(true).setSummaryMode(true).encodeResourceToString(patient);
		ourLog.info(encoded);
		
		assertThat(encoded, containsString("<Patient"));
		assertThat(encoded, stringContainsInOrder("<tag>", "<system value=\"foo\"/>", "<code value=\"bar\"/>", "</tag>"));
		assertThat(encoded, stringContainsInOrder("<tag>", "<system value=\"" + Constants.TAG_SUBSETTED_SYSTEM + "\"/>", "<code value=\"" + Constants.TAG_SUBSETTED_CODE+"\"/>", "</tag>"));
		assertThat(encoded, not(containsString("THE DIV")));
		assertThat(encoded, containsString("family"));
		assertThat(encoded, not(containsString("maritalStatus")));
	}

	@Test
	public void testEncodeWithEncodeElements() throws Exception {
		String content = IOUtils.toString(XmlParserDstu2Test.class.getResourceAsStream("/bundle-example.xml"));
		
		Patient patient = new Patient();
		patient.addName().addFamily("FAMILY");
		patient.addAddress().addLine("LINE1");
		
		ca.uhn.fhir.model.dstu2.resource.Bundle bundle = new ca.uhn.fhir.model.dstu2.resource.Bundle();
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
	public void testMoreExtensions() throws Exception {

		Patient patient = new Patient();
		patient.addIdentifier().setUse(IdentifierUseEnum.OFFICIAL).setSystem("urn:example").setValue("7000135");

		ExtensionDt ext = new ExtensionDt();
		ext.setUrl("http://example.com/extensions#someext");
		ext.setValue(new DateTimeDt("2011-01-02T11:13:15"));

		// Add the extension to the resource
		patient.addUndeclaredExtension(ext);
		// END SNIPPET: resourceExtension

		// START SNIPPET: resourceStringExtension
		HumanNameDt name = patient.addName();
		name.addFamily("Shmoe");
		StringDt given = name.addGiven();
		given.setValue("Joe");
		ExtensionDt ext2 = new ExtensionDt().setUrl("http://examples.com#givenext").setValue(new StringDt("given"));
		given.addUndeclaredExtension(ext2);

		StringDt given2 = name.addGiven();
		given2.setValue("Shmoe");
		ExtensionDt given2ext = new ExtensionDt().setUrl("http://examples.com#givenext_parent");
		given2.addUndeclaredExtension(given2ext);
		ExtensionDt givenExtChild = new ExtensionDt();
		givenExtChild.setUrl("http://examples.com#givenext_child").setValue(new StringDt("CHILD"));
		given2ext.addUndeclaredExtension(givenExtChild);
		// END SNIPPET: resourceStringExtension

		// START SNIPPET: subExtension
		ExtensionDt parent = new ExtensionDt().setUrl("http://example.com#parent");
		patient.addUndeclaredExtension(parent);

		ExtensionDt child1 = new ExtensionDt().setUrl("http://example.com#child").setValue(new StringDt("value1"));
		parent.addUndeclaredExtension(child1);

		ExtensionDt child2 = new ExtensionDt().setUrl("http://example.com#child").setValue(new StringDt("value1"));
		parent.addUndeclaredExtension(child2);
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

	@Test
	public void testParseAndEncodeBundle() throws Exception {
		String content = IOUtils.toString(XmlParserDstu2Test.class.getResourceAsStream("/bundle-example.xml"));

		Bundle parsed = ourCtx.newXmlParser().parseBundle(content);
		assertEquals("Bundle/example/_history/1", parsed.getId().getValue());
		assertEquals("1", parsed.getResourceMetadata().get(ResourceMetadataKeyEnum.VERSION));
		assertEquals("1", parsed.getId().getVersionIdPart());
		assertEquals(new InstantDt("2014-08-18T01:43:30Z"), parsed.getResourceMetadata().get(ResourceMetadataKeyEnum.UPDATED));
		assertEquals("searchset", parsed.getType().getValue());
		assertEquals(3, parsed.getTotalResults().getValue().intValue());
		assertEquals("https://example.com/base/MedicationOrder?patient=347&searchId=ff15fd40-ff71-4b48-b366-09c706bed9d0&page=2", parsed.getLinkNext().getValue());
		assertEquals("https://example.com/base/MedicationOrder?patient=347&_include=MedicationOrder.medication", parsed.getLinkSelf().getValue());

		assertEquals(2, parsed.getEntries().size());
		assertEquals("http://foo?search", parsed.getEntries().get(0).getLinkSearch().getValue());

		assertEquals("http://example.com/base/MedicationOrder/3123/_history/1", parsed.getEntries().get(0).getLinkAlternate().getValue());
		MedicationOrder p = (MedicationOrder) parsed.getEntries().get(0).getResource();
		assertEquals("Patient/347", p.getPatient().getReference().getValue());
		assertEquals("2014-08-16T05:31:17Z", ResourceMetadataKeyEnum.UPDATED.get(p).getValueAsString());
		assertEquals("http://example.com/base/MedicationOrder/3123/_history/1", p.getId().getValue());

		Medication m = (Medication) parsed.getEntries().get(1).getResource();
		assertEquals("http://example.com/base/Medication/example", m.getId().getValue());
		assertSame(((ResourceReferenceDt)p.getMedication()).getResource(), m);

		String reencoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeBundleToString(parsed);
		ourLog.info(reencoded);

		Diff d = new Diff(new StringReader(content), new StringReader(reencoded));
		assertTrue(d.toString(), d.identical());

	}

	@Test
	public void testParseAndEncodeBundleNewStyle() throws Exception {
		String content = IOUtils.toString(XmlParserDstu2Test.class.getResourceAsStream("/bundle-example.xml"));

		IParser newXmlParser = ourCtx.newXmlParser();
		ca.uhn.fhir.model.dstu2.resource.Bundle parsed = newXmlParser.parseResource(ca.uhn.fhir.model.dstu2.resource.Bundle.class, content);
		assertEquals("Bundle/example/_history/1", parsed.getId().getValue());
		assertEquals("1", parsed.getResourceMetadata().get(ResourceMetadataKeyEnum.VERSION));
		assertEquals(new InstantDt("2014-08-18T01:43:30Z"), parsed.getResourceMetadata().get(ResourceMetadataKeyEnum.UPDATED));
		assertEquals("searchset", parsed.getType());
		assertEquals(3, parsed.getTotal().intValue());
		assertEquals("https://example.com/base/MedicationOrder?patient=347&searchId=ff15fd40-ff71-4b48-b366-09c706bed9d0&page=2", parsed.getLink().get(0).getUrlElement().getValueAsString());
		assertEquals("https://example.com/base/MedicationOrder?patient=347&_include=MedicationOrder.medication", parsed.getLink().get(1).getUrlElement().getValueAsString());

		assertEquals(2, parsed.getEntry().size());
		assertEquals("alternate", parsed.getEntry().get(0).getLink().get(0).getRelation());
		assertEquals("http://example.com/base/MedicationOrder/3123/_history/1", parsed.getEntry().get(0).getLink().get(0).getUrl());
		assertEquals("http://foo?search", parsed.getEntry().get(0).getRequest().getUrlElement().getValueAsString());

		MedicationOrder p = (MedicationOrder) parsed.getEntry().get(0).getResource();
		assertEquals("Patient/347", p.getPatient().getReference().getValue());
		assertEquals("2014-08-16T05:31:17Z", ResourceMetadataKeyEnum.UPDATED.get(p).getValueAsString());
		assertEquals("http://example.com/base/MedicationOrder/3123/_history/1", p.getId().getValue());
//		assertEquals("3123", p.getId().getValue());

		Medication m = (Medication) parsed.getEntry().get(1).getResource();
		assertEquals("http://example.com/base/Medication/example", m.getId().getValue());
		assertSame(((ResourceReferenceDt)p.getMedication()).getResource(), m);

		String reencoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(parsed);
		ourLog.info(reencoded);

		Diff d = new Diff(new StringReader(content), new StringReader(reencoded));
		assertTrue(d.toString(), d.identical());

	}
	
	@Test
	public void testParseAndEncodeComments() throws IOException {
		//@formatter:off
		String input = "<Patient xmlns=\"http://hl7.org/fhir\">\n" + 
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
				"</Patient>";
		//@formatter:off

		Patient res = ourCtx.newXmlParser().parseResource(Patient.class, input);
		res.getFormatCommentsPre();
		assertEquals("Patient/pat1", res.getId().getValue());
		assertEquals("654321", res.getIdentifier().get(0).getValue());
		assertEquals(true, res.getActive());
		
		assertThat(res.getIdentifier().get(0).getFormatCommentsPre(), contains("identifier comment 1", "identifier comment 2"));
		assertThat(res.getIdentifier().get(0).getUseElement().getFormatCommentsPre(), contains("use comment 1", "use comment 2"));
		
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
				"<!--identifier comment 1-->",
				"<!--identifier comment 2-->", 
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
	public void testParseAndEncodeExtensionOnResourceReference() {
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

		ElementDefinitionDt elem = de.getElement().get(0);
		Binding b = elem.getBinding();
		// assertEquals("All codes representing the gender of a person.", b.getDescription());

		ResourceReferenceDt ref = (ResourceReferenceDt) b.getValueSet();
		assertEquals("#2179414", ref.getReference().getValue());

		assertEquals(2, ref.getUndeclaredExtensions().size());
		ExtensionDt ext = ref.getUndeclaredExtensions().get(0);
		assertEquals("http://hl7.org/fhir/StructureDefinition/11179-permitted-value-valueset", ext.getUrl());
		assertEquals(ResourceReferenceDt.class, ext.getValue().getClass());
		assertEquals("#2179414-permitted", ((ResourceReferenceDt) ext.getValue()).getReference().getValue());

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

		ca.uhn.fhir.model.dstu2.resource.Bundle parsed = ourCtx.newXmlParser().parseResource(ca.uhn.fhir.model.dstu2.resource.Bundle.class, input);
		assertEquals("urn:oid:0.1.2.3", parsed.getEntry().get(0).getResource().getId().getValue());

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

		ca.uhn.fhir.model.dstu2.resource.Bundle parsed = ourCtx.newXmlParser().parseResource(ca.uhn.fhir.model.dstu2.resource.Bundle.class, input);
		assertEquals("urn:oid:0.1.2.3", parsed.getEntry().get(0).getResource().getId().getValue());
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

		ca.uhn.fhir.model.dstu2.resource.Bundle parsed = ourCtx.newXmlParser().parseResource(ca.uhn.fhir.model.dstu2.resource.Bundle.class, input);
		assertEquals("urn:uuid:0.1.2.3", parsed.getEntry().get(0).getResource().getId().getValue());

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

		parsed = ourCtx.newXmlParser().parseResource(ca.uhn.fhir.model.dstu2.resource.Bundle.class, input);
		assertEquals("urn:uuid:0.1.2.3", parsed.getEntry().get(0).getResource().getId().getValue());

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

		Bundle b = ourCtx.newXmlParser().parseBundle(bundle);
		assertEquals(1, b.getEntries().size());

	}

	@Test
	public void testParseBundleOldWithPlaceholderIds() {
		//@formatter:off
		String input = "<Bundle xmlns=\"http://hl7.org/fhir\">\n" + 
				"    <id value=\"ringholm1430996763590912\"/>\n" + 
				"    <entry>\n" +
				"        <base value=\"urn:oid:\"/>\n" +
				"        <resource>\n" + 
				"            <Provenance>\n" + 
				"                <id value=\"0.1.2.3\"/>\n" + 
				"            </Provenance>\n" + 
				"        </resource>\n" + 
				"    </entry>\n" + 
				"</Bundle>\n";
		//@formatter:on		

		Bundle parsed = ourCtx.newXmlParser().parseBundle(input);
		assertEquals("urn:oid:0.1.2.3", parsed.getEntries().get(0).getResource().getId().getValue());

		//@formatter:off
		input = "<Bundle xmlns=\"http://hl7.org/fhir\">\n" + 
				"    <id value=\"ringholm1430996763590912\"/>\n" + 
				"    <entry>\n" +
				"        <base value=\"urn:oid\"/>\n" + // no trailing :, invalid but we'll be nice
				"        <resource>\n" + 
				"            <Provenance>\n" + 
				"                <id value=\"0.1.2.3\"/>\n" + 
				"            </Provenance>\n" + 
				"        </resource>\n" + 
				"    </entry>\n" + 
				"</Bundle>\n";
		//@formatter:on		

		parsed = ourCtx.newXmlParser().parseBundle(input);
		assertEquals("urn:oid:0.1.2.3", parsed.getEntries().get(0).getResource().getId().getValue());

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
		String input = IOUtils.toString(XmlParserDstu2Test.class.getResourceAsStream("/bundle_orion.xml"));
		ca.uhn.fhir.model.dstu2.resource.Bundle parsed = ourCtx.newXmlParser().parseResource(ca.uhn.fhir.model.dstu2.resource.Bundle.class, input);

		Link link = parsed.getLink().get(0);
		assertEquals("just trying add link", link.getRelation());
		assertEquals("blarion", link.getUrl());

		Entry entry = parsed.getEntry().get(0);
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

		ca.uhn.fhir.model.dstu2.resource.Bundle bundle = ourCtx.newXmlParser().parseResource(ca.uhn.fhir.model.dstu2.resource.Bundle.class, input);
		assertEquals("http://localhost:58402/fhir/context/Patient/1/_history/3", bundle.getEntry().get(0).getResource().getId().getValue());
		assertEquals("http://localhost:58402/fhir/context/Patient/1/_history/2", bundle.getEntry().get(1).getResource().getId().getValue());
		assertEquals("http://localhost:58402/fhir/context/Patient/1/_history/1", bundle.getEntry().get(2).getResource().getId().getValue());
	}

	/**
	 * see #144 and #146
	 */
	@Test
	public void testParseContained() {

		FhirContext c = FhirContext.forDstu2();
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
		assertEquals("patient family", p.getNameFirstRep().getFamilyAsSingleString());
	}

	/**
	 * Thanks to Alexander Kley!
	 */
	@Test
	public void testParseContainedBinaryResource() {
		byte[] bin = new byte[] { 0, 1, 2, 3, 4 };
		final Binary binary = new Binary();
		binary.setContentType("PatientConsent").setContent(bin);
		// binary.setId(UUID.randomUUID().toString());

		ca.uhn.fhir.model.dstu2.resource.DocumentManifest manifest = new ca.uhn.fhir.model.dstu2.resource.DocumentManifest();
		// manifest.setId(UUID.randomUUID().toString());
		CodeableConceptDt cc = new CodeableConceptDt();
		cc.addCoding().setSystem("mySystem").setCode("PatientDocument");
		manifest.setType(cc);
		manifest.setMasterIdentifier(new IdentifierDt().setSystem("mySystem").setValue(UUID.randomUUID().toString()));
		manifest.addContent().setP(new ResourceReferenceDt(binary));
		manifest.setStatus(DocumentReferenceStatusEnum.CURRENT);

		String encoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(manifest);
		ourLog.info(encoded);
		assertThat(encoded, StringContainsInOrder.stringContainsInOrder(Arrays.asList("contained>", "<Binary", "</contained>")));

		ca.uhn.fhir.model.dstu2.resource.DocumentManifest actual = ourCtx.newXmlParser().parseResource(ca.uhn.fhir.model.dstu2.resource.DocumentManifest.class, encoded);
		assertEquals(1, actual.getContained().getContainedResources().size());
		assertEquals(1, actual.getContent().size());
		assertNotNull(((ResourceReferenceDt) actual.getContent().get(0).getP()).getResource());

	}

	@Test
	public void testParseInvalidTextualNumber() {
		Observation obs = new Observation();
		obs.setValue(new QuantityDt().setValue(1234));
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
		assertThat(patient.getIdentifier().get(0).getType().getValueAsEnum(), IsEmptyCollection.empty());

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
			"               <lastUpdated value=\"2001-02-22T11:22:33-05:00\"/>\n" +
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

		Bundle b = ourCtx.newXmlParser().parseBundle(bundle);
		assertEquals(1, b.getEntries().size());

		Patient pt = (Patient) b.getEntries().get(0).getResource();
		assertEquals("http://foo/fhirBase2/Patient/1/_history/2", pt.getId().getValue());
		assertEquals("2012-01-02", pt.getBirthDateElement().getValueAsString());
		assertEquals("0.123", ResourceMetadataKeyEnum.ENTRY_SCORE.get(pt).getValueAsString());
		assertEquals("match", ResourceMetadataKeyEnum.ENTRY_SEARCH_MODE.get(pt).getCode());
		assertEquals("POST", ResourceMetadataKeyEnum.ENTRY_TRANSACTION_METHOD.get(pt).getCode());
		assertEquals("http://foo/Patient?identifier=value", ResourceMetadataKeyEnum.LINK_SEARCH.get(pt));
		assertEquals("2001-02-22T11:22:33-05:00", ResourceMetadataKeyEnum.UPDATED.get(pt).getValueAsString());

		Bundle toBundle = new Bundle();
		toBundle.getLinkBase().setValue("http://foo/fhirBase1");
		toBundle.getTotalResults().setValue(1);
		toBundle.getLinkSelf().setValue("http://localhost:52788/Binary?_pretty=true");

		toBundle.addResource(pt, ourCtx, "http://foo/fhirBase1");
		String reEncoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeBundleToString(toBundle);

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
		ca.uhn.fhir.model.dstu2.resource.Bundle b = ourCtx.newXmlParser().parseResource(ca.uhn.fhir.model.dstu2.resource.Bundle.class, input);

		InstantDt updated = ResourceMetadataKeyEnum.UPDATED.get(b);
		assertEquals("2015-06-22T15:48:57.554-04:00", updated.getValueAsString());

	}

	@Test
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
		patient.setId(new IdDt("Patient", patientId));
		patient.addName().addGiven("John").addFamily("Smith");
		patient.setGender(AdministrativeGenderEnum.MALE);
		patient.setBirthDate(new DateDt("1987-04-16"));

		// Bundle
		ca.uhn.fhir.model.dstu2.resource.Bundle bundle = new ca.uhn.fhir.model.dstu2.resource.Bundle();
		bundle.setType(BundleTypeEnum.COLLECTION);
		bundle.addEntry().setResource(patient);

		String bundleText = xmlParser.encodeResourceToString(bundle);
		ourLog.info(bundleText);

		ca.uhn.fhir.model.dstu2.resource.Bundle reincarnatedBundle = xmlParser.parseResource(ca.uhn.fhir.model.dstu2.resource.Bundle.class, bundleText);
		Patient reincarnatedPatient = reincarnatedBundle.getAllPopulatedChildElementsOfType(Patient.class).get(0);

		assertEquals("Patient", patient.getId().getResourceType());
		assertEquals("Patient", reincarnatedPatient.getId().getResourceType());
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
