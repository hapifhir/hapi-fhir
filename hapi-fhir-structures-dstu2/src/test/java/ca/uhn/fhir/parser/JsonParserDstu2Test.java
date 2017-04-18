package ca.uhn.fhir.parser;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.stringContainsInOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;

import org.apache.commons.io.IOUtils;
import org.hamcrest.Matchers;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.internal.stubbing.answers.ThrowsException;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Sets;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.*;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.base.composite.BaseCodingDt;
import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.composite.HumanNameDt;
import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu2.resource.*;
import ca.uhn.fhir.model.dstu2.resource.Bundle.Entry;
import ca.uhn.fhir.model.dstu2.valueset.AdministrativeGenderEnum;
import ca.uhn.fhir.model.dstu2.valueset.BundleTypeEnum;
import ca.uhn.fhir.model.dstu2.valueset.ConditionVerificationStatusEnum;
import ca.uhn.fhir.model.dstu2.valueset.IdentifierUseEnum;
import ca.uhn.fhir.model.dstu2.valueset.MaritalStatusCodesEnum;
import ca.uhn.fhir.model.dstu2.valueset.ObservationStatusEnum;
import ca.uhn.fhir.model.dstu2.valueset.UnknownContentCodeEnum;
import ca.uhn.fhir.model.primitive.BooleanDt;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.DateDt;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.parser.IParserErrorHandler.IParseLocation;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.util.TestUtil;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import net.sf.json.JSON;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;

public class JsonParserDstu2Test {
	private static FhirContext ourCtx = FhirContext.forDstu2();
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(JsonParserDstu2Test.class);

	@Test
	public void testOverrideResourceIdWithBundleEntryFullUrlDisabled_ConfiguredOnFhirContext() {
		try {
			String tmp = "{\"resourceType\":\"Bundle\",\"entry\":[{\"fullUrl\":\"http://lalaland.org/patient/pat1\",\"resource\":{\"resourceType\":\"Patient\",\"id\":\"patxuzos\"}}]}";
			ourCtx.getParserOptions().setOverrideResourceIdWithBundleEntryFullUrl(false);
			ca.uhn.fhir.model.dstu2.resource.Bundle bundle = (ca.uhn.fhir.model.dstu2.resource.Bundle) ourCtx.newJsonParser().parseResource(tmp);
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
			ourCtx = FhirContext.forDstu2();
		}
	}
	
	@Test
	public void testOverrideResourceIdWithBundleEntryFullUrlDisabled_ConfiguredOnParser() {
		try {
			String tmp = "{\"resourceType\":\"Bundle\",\"entry\":[{\"fullUrl\":\"http://lalaland.org/patient/pat1\",\"resource\":{\"resourceType\":\"Patient\",\"id\":\"patxuzos\"}}]}";
			ca.uhn.fhir.model.dstu2.resource.Bundle bundle = (ca.uhn.fhir.model.dstu2.resource.Bundle) ourCtx.newJsonParser().setOverrideResourceIdWithBundleEntryFullUrl(false).parseResource(tmp);
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
			ourCtx = FhirContext.forDstu2();
		}
	}
	
	/**
	 * See #544
	 */
	@Test
	public void testBundleStitchReferencesByUuid() throws Exception {
		ca.uhn.fhir.model.dstu2.resource.Bundle bundle = new ca.uhn.fhir.model.dstu2.resource.Bundle();
		
		DocumentManifest dm = new DocumentManifest();
		dm.getSubject().setReference("urn:uuid:96e85cca-9797-45d6-834a-c4eb27f331d3");
		bundle.addEntry().setResource(dm);
		
		Patient patient = new Patient();
		patient.addName().addFamily("FAMILY");
		bundle.addEntry().setResource(patient).setFullUrl("urn:uuid:96e85cca-9797-45d6-834a-c4eb27f331d3");
		
		String encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(bundle);
		ourLog.info(encoded);
		
		bundle = ourCtx.newJsonParser().parseResource(ca.uhn.fhir.model.dstu2.resource.Bundle.class, encoded);
		dm = (DocumentManifest) bundle.getEntry().get(0).getResource();
		
		assertEquals("urn:uuid:96e85cca-9797-45d6-834a-c4eb27f331d3", dm.getSubject().getReference().getValue());
		
		Patient subject = (Patient) dm.getSubject().getResource();
		assertNotNull(subject);
		assertEquals("FAMILY", subject.getNameFirstRep().getFamilyAsSingleString());
	}

	@Test
	public void testContainedResourceInExtensionUndeclared() {
		Patient p = new Patient();
		p.addName().addFamily("PATIENT");

		Organization o = new Organization();
		o.setName("ORG");
		p.addUndeclaredExtension(new ExtensionDt(false, "urn:foo", new ResourceReferenceDt(o)));

		String str = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p);
		ourLog.info(str);

		p = ourCtx.newJsonParser().parseResource(Patient.class, str);
		assertEquals("PATIENT", p.getName().get(0).getFamily().get(0).getValue());

		List<ExtensionDt> exts = p.getUndeclaredExtensionsByUrl("urn:foo");
		assertEquals(1, exts.size());
		ResourceReferenceDt rr = (ResourceReferenceDt) exts.get(0).getValue();
		o = (Organization) rr.getResource();
		assertEquals("ORG", o.getName());
	}

	@Test
	public void testEncodeBundleOldStyleContainingResourceWithUuidBase() {
		Patient p = new Patient();
		p.setId(IdDt.newRandomUuid());
		p.addName().addFamily("PATIENT");

		Bundle b = new Bundle();
		b.addEntry().setResource(p);

		String encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeBundleToString(b);
		ourLog.info(encoded);
		assertThat(encoded, stringContainsInOrder("fullUrl", p.getId().getValue(), "Patient"));
	}

	
	/**
	 * See #308
	 */
	@Test
	public void testDeclaredExtensionsDontProduceWarning() {
		ReportObservation obs = new ReportObservation();
		obs.setReadOnly(true);

		IParser p = ourCtx.newJsonParser();
		p.setParserErrorHandler(mock(IParserErrorHandler.class, new ThrowsException(new IllegalStateException())));

		String encoded = p.encodeResourceToString(obs);
		ourLog.info(encoded);

		obs = p.parseResource(ReportObservation.class, encoded);
		assertEquals(true, obs.getReadOnly().getValue().booleanValue());
	}

	/**
	 * See #390
	 */
	@Test
	public void testEncodeAndParseBundleWithNoEntries() {
		ca.uhn.fhir.model.dstu2.resource.Bundle b = new ca.uhn.fhir.model.dstu2.resource.Bundle();
		b.setId("123");
		String encoded = ourCtx.newJsonParser().encodeResourceToString(b);
		ourLog.info(encoded);

		assertThat(encoded, containsString("123"));
		assertThat(encoded, not(containsString("entry")));

		b = ourCtx.newJsonParser().parseResource(ca.uhn.fhir.model.dstu2.resource.Bundle.class, encoded);
		assertEquals("123", b.getId().getIdPart());
		assertEquals(0, b.getEntry().size());
	}

	/**
	 * #480
	 */
	@Test
	public void testEncodeEmptyValue() {
		QuestionnaireResponse qr = new QuestionnaireResponse();
		qr.setId("123");
		qr.getAuthoredElement().setValueAsString("");
		qr.getGroup().setLinkId(new StringDt());
		qr.getGroup().addQuestion().setLinkId(new StringDt(""));
		qr.getGroup().addQuestion().setLinkId(new StringDt("LINKID"));

		String encoded = ourCtx.newJsonParser().encodeResourceToString(qr);
		ourLog.info(encoded);

		assertThat(encoded, stringContainsInOrder("123"));
		assertThat(encoded, not(stringContainsInOrder("\"\"")));
		assertThat(encoded, not(stringContainsInOrder("null")));
	}

	/**
	 * #480
	 */
	@Test
	public void testParseEmptyValue() {
		String input = "{\"resourceType\":\"QuestionnaireResponse\",\"id\":\"123\",\"authored\":\"\",\"group\":{\"linkId\":\"\"}}";
		IParser parser = ourCtx.newJsonParser();

		parser.setParserErrorHandler(new LenientErrorHandler().setErrorOnInvalidValue(false));
		QuestionnaireResponse qr = parser.parseResource(QuestionnaireResponse.class, input);

		assertEquals("QuestionnaireResponse/123", qr.getIdElement().getValue());
		assertEquals(null, qr.getAuthored());
		assertEquals(null, qr.getAuthoredElement().getValue());
		assertEquals(null, qr.getAuthoredElement().getValueAsString());
		assertEquals(null, qr.getGroup().getLinkId());
		assertEquals(null, qr.getGroup().getLinkIdElement().getValue());
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
		assertThat(enc, Matchers.stringContainsInOrder("{\"resourceType\":\"Patient\",", "\"extension\":[{\"url\":\"http://example.com/extensions#someext\",\"valueDateTime\":\"2011-01-02T11:13:15\"}",
				"{\"url\":\"http://example.com#parent\",\"extension\":[{\"url\":\"http://example.com#child\",\"valueString\":\"value1\"},{\"url\":\"http://example.com#child\",\"valueString\":\"value2\"}]}"));
		assertThat(enc, Matchers.stringContainsInOrder("\"modifierExtension\":[" + "{" + "\"url\":\"http://example.com/extensions#modext\"," + "\"valueDate\":\"1995-01-02\"" + "}" + "],"));
		assertThat(enc, containsString("\"_given\":[" + "{" + "\"extension\":[" + "{" + "\"url\":\"http://examples.com#givenext\"," + "\"valueString\":\"given\"" + "}" + "]" + "}," + "{"
				+ "\"extension\":[" + "{" + "\"url\":\"http://examples.com#givenext_parent\"," + "\"extension\":[" + "{"
				+ "\"url\":\"http://examples.com#givenext_child\"," + "\"valueString\":\"CHILD\"" + "}" + "]" + "}" + "]" + "}"));

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
	public void testEncodeAndParseLanguage() {
		Patient p = new Patient();
		p.setLanguage(new CodeDt("en_CA"));

		String encoded = ourCtx.newJsonParser().encodeResourceToString(p);
		ourLog.info(encoded);

		assertEquals("{\"resourceType\":\"Patient\",\"language\":\"en_CA\"}", encoded);

		p = (Patient) ourCtx.newJsonParser().parseResource(encoded);
		assertEquals("en_CA", p.getLanguage().getValue());

		p = (Patient) ourCtx.newJsonParser().parseResource("{\"resourceType\":\"Patient\",\"language\":[\"en_CA\"]}");
		assertEquals("en_CA", p.getLanguage().getValue());
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

		String enc = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p);
		ourLog.info(enc);

		//@formatter:off
		assertThat(enc, stringContainsInOrder("\"meta\": {", 
				"\"profile\": [", 
				"\"http://foo/Profile1\",", 
				"\"http://foo/Profile2\"", 
				"],", 
				"\"tag\": [", 
				"{", 
				"\"system\": \"scheme1\",", 
				"\"code\": \"term1\",", 
				"\"display\": \"label1\"", 
				"},", 
				"{", 
				"\"system\": \"scheme2\",", 
				"\"code\": \"term2\",", 
				"\"display\": \"label2\"", 
				"}", 
				"]", 
				"},"));
		//@formatter:on

		Patient parsed = ourCtx.newJsonParser().parseResource(Patient.class, enc);
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

	/**
	 * See #336
	 */
	@Test
	public void testEncodeAndParseNullPrimitiveWithExtensions() {

		Patient p = new Patient();
		p.setId("patid");
		HumanNameDt name = p.addName();
		name.addFamily().setValue(null).addUndeclaredExtension(new ExtensionDt(false, "http://foo", new StringDt("FOOEXT0")));
		name.getFamily().get(0).setElementSpecificId("f0");
		name.addFamily().setValue("V1").addUndeclaredExtension((ExtensionDt) new ExtensionDt(false, "http://foo", new StringDt("FOOEXT1")));
		name.getFamily().get(1).setElementSpecificId("f1");
		name.getFamily().get(1).getUndeclaredExtensions().get(0).setElementSpecificId("ext1id");
		name.addFamily(); // this one shouldn't get encoded
		name.addFamily().setValue(null).addUndeclaredExtension(new ExtensionDt(false, "http://foo", new StringDt("FOOEXT3")));
		name.setElementSpecificId("nameid");

		String output = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p);
		ourLog.info(output);

		output = ourCtx.newJsonParser().setPrettyPrint(false).encodeResourceToString(p);
		String expected = "{\"resourceType\":\"Patient\",\"id\":\"patid\",\"name\":[{\"id\":\"nameid\",\"family\":[null,\"V1\",null],\"_family\":[{\"id\":\"f0\",\"extension\":[{\"url\":\"http://foo\",\"valueString\":\"FOOEXT0\"}]},{\"id\":\"f1\",\"extension\":[{\"id\":\"ext1id\",\"url\":\"http://foo\",\"valueString\":\"FOOEXT1\"}]},{\"extension\":[{\"url\":\"http://foo\",\"valueString\":\"FOOEXT3\"}]}]}]}";
		assertEquals(expected, output);

		p = ourCtx.newJsonParser().parseResource(Patient.class, output);
		assertEquals("patid", p.getIdElement().getIdPart());

		name = p.getName().get(0);
		assertEquals("nameid", name.getElementSpecificId());
		assertEquals(3, name.getFamily().size());

		assertEquals(null, name.getFamily().get(0).getValue());
		assertEquals("V1", name.getFamily().get(1).getValue());
		assertEquals(null, name.getFamily().get(2).getValue());

		assertEquals("f0", name.getFamily().get(0).getElementSpecificId());
		assertEquals("f1", name.getFamily().get(1).getElementSpecificId());
		assertEquals(null, name.getFamily().get(2).getElementSpecificId());

		assertEquals(1, name.getFamily().get(0).getAllUndeclaredExtensions().size());
		assertEquals("http://foo", name.getFamily().get(0).getAllUndeclaredExtensions().get(0).getUrl());
		assertEquals("FOOEXT0", ((StringDt) name.getFamily().get(0).getAllUndeclaredExtensions().get(0).getValue()).getValue());
		assertEquals(null, name.getFamily().get(0).getAllUndeclaredExtensions().get(0).getElementSpecificId());

		assertEquals(1, name.getFamily().get(1).getAllUndeclaredExtensions().size());
		assertEquals("http://foo", name.getFamily().get(1).getAllUndeclaredExtensions().get(0).getUrl());
		assertEquals("FOOEXT1", ((StringDt) name.getFamily().get(1).getAllUndeclaredExtensions().get(0).getValue()).getValue());
		assertEquals("ext1id", name.getFamily().get(1).getAllUndeclaredExtensions().get(0).getElementSpecificId());

		assertEquals(1, name.getFamily().get(2).getAllUndeclaredExtensions().size());
		assertEquals("http://foo", name.getFamily().get(2).getAllUndeclaredExtensions().get(0).getUrl());
		assertEquals("FOOEXT3", ((StringDt) name.getFamily().get(2).getAllUndeclaredExtensions().get(0).getValue()).getValue());
		assertEquals(null, name.getFamily().get(2).getAllUndeclaredExtensions().get(0).getElementSpecificId());

	}

	@Test
	public void testEncodeAndParseSecurityLabels() {
		Patient p = new Patient();
		p.addName().addFamily("FAMILY");

		List<BaseCodingDt> labels = new ArrayList<BaseCodingDt>();
		labels.add(new CodingDt().setSystem("SYSTEM1").setCode("CODE1").setDisplay("DISPLAY1").setVersion("VERSION1"));
		labels.add(new CodingDt().setSystem("SYSTEM2").setCode("CODE2").setDisplay("DISPLAY2").setVersion("VERSION2"));

		ResourceMetadataKeyEnum.SECURITY_LABELS.put(p, labels);

		String enc = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p);
		ourLog.info(enc);

		//@formatter:off
		String actual = enc.trim();
		ourLog.info("Actual:\n{}", actual);
		
		assertEquals("{\n" + 
			"  \"resourceType\": \"Patient\",\n" + 
			"  \"meta\": {\n" + 
			"    \"security\": [\n" + 
			"      {\n" + 
			"        \"system\": \"SYSTEM1\",\n" + 
			"        \"version\": \"VERSION1\",\n" + 
			"        \"code\": \"CODE1\",\n" + 
			"        \"display\": \"DISPLAY1\"\n" + 
			"      },\n" + 
			"      {\n" + 
			"        \"system\": \"SYSTEM2\",\n" + 
			"        \"version\": \"VERSION2\",\n" + 
			"        \"code\": \"CODE2\",\n" + 
			"        \"display\": \"DISPLAY2\"\n" + 
			"      }\n" + 
			"    ]\n" + 
			"  },\n" + 
			"  \"name\": [\n" + 
			"    {\n" + 
			"      \"family\": [\n" + 
			"        \"FAMILY\"\n" + 
			"      ]\n" + 
			"    }\n" + 
			"  ]\n" + 
			"}", actual);
		//@formatter:on

		Patient parsed = ourCtx.newJsonParser().parseResource(Patient.class, enc);
		List<BaseCodingDt> gotLabels = ResourceMetadataKeyEnum.SECURITY_LABELS.get(parsed);

		assertEquals(2, gotLabels.size());

		CodingDt label = (CodingDt) gotLabels.get(0);
		assertEquals("SYSTEM1", label.getSystem());
		assertEquals("CODE1", label.getCode());
		assertEquals("DISPLAY1", label.getDisplay());
		assertEquals("VERSION1", label.getVersion());

		label = (CodingDt) gotLabels.get(1);
		assertEquals("SYSTEM2", label.getSystem());
		assertEquals("CODE2", label.getCode());
		assertEquals("DISPLAY2", label.getDisplay());
		assertEquals("VERSION2", label.getVersion());
	}

	@Test
	public void testEncodeBundleNewBundleNoText() {

		ca.uhn.fhir.model.dstu2.resource.Bundle b = new ca.uhn.fhir.model.dstu2.resource.Bundle();
		b.getText().setDiv("");
		b.getText().getStatus().setValueAsString("");
		;

		Entry e = b.addEntry();
		e.setResource(new Patient());

		String val = ourCtx.newJsonParser().setPrettyPrint(false).encodeResourceToString(b);
		ourLog.info(val);
		assertThat(val, not(containsString("text")));

		val = ourCtx.newXmlParser().setPrettyPrint(false).encodeResourceToString(b);
		ourLog.info(val);
		assertThat(val, not(containsString("text")));

	}

	@Test
	public void testEncodeBundleOldBundleNoText() {

		Bundle b = new Bundle();

		BundleEntry e = b.addEntry();
		e.setResource(new Patient());
		b.addCategory("scheme", "term", "label");

		String val = ourCtx.newJsonParser().setPrettyPrint(false).encodeBundleToString(b);
		ourLog.info(val);

		assertThat(val, not(containsString("text")));

		b = ourCtx.newJsonParser().parseBundle(val);
		assertEquals(1, b.getEntries().size());

	}

	/**
	 * Fixing #89
	 */
	@Test
	public void testEncodeBundleWithDeletedEntry() throws ConfigurationException, DataFormatException, IOException {
		Bundle b = ourCtx.newXmlParser().parseBundle(IOUtils.toString(JsonParserDstu2Test.class.getResourceAsStream("/xml-bundle.xml")));
		String val = ourCtx.newJsonParser().encodeBundleToString(b);

		ourLog.info(val);

		//@formatter:off
		assertThat(val, containsString("\"deleted\":{" + 
				"\"type\":\"Patient\"," + 
				"\"resourceId\":\"4384\"," + 
				"\"instant\":\"2015-01-15T11:04:43.054-05:00\"" + 
				"}"));
		//@formatter:on
	}

	@Test
	public void testEncodeDoesntIncludeUuidId() {
		Patient p = new Patient();
		p.setId(new IdDt("urn:uuid:42795ed8-041f-4ebf-b6f4-78ef6f64c2f2"));
		p.addIdentifier().setSystem("ACME");

		String actual = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p);
		assertThat(actual, not(containsString("78ef6f64c2f2")));
	}

	@Test
	public void testEncodeEmptyBinary() {
		String output = ourCtx.newJsonParser().encodeResourceToString(new Binary());
		assertEquals("{\"resourceType\":\"Binary\"}", output);
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

		String encoded = ourCtx.newJsonParser().encodeResourceToString(p);
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

		String encoded = ourCtx.newJsonParser().encodeResourceToString(p);
		assertThat(encoded, containsString("tag"));
		assertThat(encoded, containsString("scheme"));
		assertThat(encoded, not(containsString("Label")));
	}

	@Test
	public void testEncodeExtensionInPrimitiveElement() {

		Conformance c = new Conformance();
		c.getAcceptUnknownElement().addUndeclaredExtension(false, "http://foo", new StringDt("AAA"));

		String encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(c);
		ourLog.info(encoded);

		encoded = ourCtx.newJsonParser().setPrettyPrint(false).encodeResourceToString(c);
		ourLog.info(encoded);
		assertEquals(encoded, "{\"resourceType\":\"Conformance\",\"_acceptUnknown\":{\"extension\":[{\"url\":\"http://foo\",\"valueString\":\"AAA\"}]}}");

		// Now with a value
		ourLog.info("---------------");

		c = new Conformance();
		c.getAcceptUnknownElement().setValueAsEnum(UnknownContentCodeEnum.UNKNOWN_ELEMENTS);
		;
		c.getAcceptUnknownElement().addUndeclaredExtension(false, "http://foo", new StringDt("AAA"));

		encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(c);
		ourLog.info(encoded);

		encoded = ourCtx.newJsonParser().setPrettyPrint(false).encodeResourceToString(c);
		ourLog.info(encoded);
		assertEquals(encoded, "{\"resourceType\":\"Conformance\",\"acceptUnknown\":\"elements\",\"_acceptUnknown\":{\"extension\":[{\"url\":\"http://foo\",\"valueString\":\"AAA\"}]}}");

	}

	@Test
	public void testEncodeExtensionUndeclaredNonModifier() {
		Observation obs = new Observation();
		obs.setId("1");
		obs.getMeta().addProfile("http://profile");
		ExtensionDt ext = obs.addUndeclaredExtension(false, "http://exturl");
		ext.setUrl("http://exturl").setValue(new StringDt("ext_url_value"));

		obs.getCode().setText("CODE");

		IParser parser = ourCtx.newJsonParser();

		String output = parser.setPrettyPrint(true).encodeResourceToString(obs);
		ourLog.info(output);

		//@formatter:off
		assertThat(output, stringContainsInOrder(
			"\"id\": \"1\"",
			"\"meta\"",
			"\"extension\"",
			"\"url\": \"http://exturl\"",
			"\"valueString\": \"ext_url_value\"",
			"\"code\":"
		));
		assertThat(output, not(stringContainsInOrder(
			"\"url\": \"http://exturl\"",
			",",
			"\"url\": \"http://exturl\""
		)));
		//@formatter:on

		obs = parser.parseResource(Observation.class, output);
		assertEquals(1, obs.getUndeclaredExtensions().size());
		assertEquals("http://exturl", obs.getUndeclaredExtensions().get(0).getUrl());
		assertEquals("ext_url_value", ((StringDt) obs.getUndeclaredExtensions().get(0).getValue()).getValue());
	}

	@Test
	public void testEncodeExtensionUndeclaredNonModifierWithChildExtension() {
		Observation obs = new Observation();
		obs.setId("1");
		obs.getMeta().addProfile("http://profile");
		ExtensionDt ext = obs.addUndeclaredExtension(false, "http://exturl");
		ext.setUrl("http://exturl");

		ExtensionDt subExt = ext.addUndeclaredExtension(false, "http://subext");
		subExt.setUrl("http://subext").setValue(new StringDt("sub_ext_value"));

		obs.getCode().setText("CODE");

		IParser parser = ourCtx.newJsonParser();

		String output = parser.setPrettyPrint(true).encodeResourceToString(obs);
		ourLog.info(output);

		//@formatter:off
		assertThat(output, stringContainsInOrder(
				"\"id\": \"1\"",
				"\"meta\"",
				"\"extension\"",
				"\"url\": \"http://exturl\"",
				"\"extension\"",
				"\"url\": \"http://subext\"",
				"\"valueString\": \"sub_ext_value\"",
				"\"code\":"
			));
			assertThat(output, not(stringContainsInOrder(
				"\"url\": \"http://exturl\"",
				",",
				"\"url\": \"http://exturl\""
			)));
		//@formatter:on

		obs = parser.parseResource(Observation.class, output);
		assertEquals(1, obs.getUndeclaredExtensions().size());
		assertEquals("http://exturl", obs.getUndeclaredExtensions().get(0).getUrl());
		assertEquals(1, obs.getUndeclaredExtensions().get(0).getExtension().size());
		assertEquals("http://subext", obs.getUndeclaredExtensions().get(0).getExtension().get(0).getUrl());
		assertEquals("sub_ext_value", ((StringDt) obs.getUndeclaredExtensions().get(0).getExtension().get(0).getValue()).getValue());
	}

	/**
	 * See #428
	 */
	@Test
	public void testEncodeExtensionWithCodeableConcept() {
		Logger logger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
		Level initialLevel = logger.getLevel();
		logger.setLevel(Level.TRACE);
		try {
			Patient p = new Patient();

			CodeableConceptDt cc = new CodeableConceptDt();
			cc.addCoding().setCode("123").setSystem("http://foo").setDisplay("AAA");

			p.addUndeclaredExtension(false, "http://extension", cc);

			ourLog.info(ourCtx.newJsonParser().encodeResourceToString(p));
		} finally {
			logger.setLevel(initialLevel);
		}
	}

	@Test
	public void testEncodeForceResourceId() {
		Patient p = new Patient();
		p.setId("111");
		p.addName().addGiven("GIVEN");

		IParser parser = ourCtx.newJsonParser();
		parser.setEncodeForceResourceId(new IdDt("222"));
		String encoded = parser.encodeResourceToString(p);

		ourLog.info(encoded);

		assertThat(encoded, containsString("222"));
		assertThat(encoded, not(containsString("111")));
	}

	@Test
	public void testEncodeNarrativeShouldIncludeNamespace() {

		Patient p = new Patient();
		p.getText().setDivAsString("<div>VALUE</div>");

		String output = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p);
		ourLog.info(output);
		assertThat(output, containsString("\"div\": \"<div xmlns=\\\"http://www.w3.org/1999/xhtml\\\">VALUE</div>\""));
	}

	@Test
	public void testEncodeNarrativeShouldIncludeNamespaceWithProcessingInstruction() {

		Patient p = new Patient();
		p.getText().setDivAsString("<?xml version=\"1.0\" encoding=\"UTF-8\"?><div>VALUE</div>");

		String output = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p);
		ourLog.info(output);
		assertThat(output, containsString("\"div\": \"<?xml version=\\\"1.0\\\" encoding=\\\"UTF-8\\\"?><div xmlns=\\\"http://www.w3.org/1999/xhtml\\\">VALUE</div>\""));
	}

	@Test
	public void testEncodeNarrativeSuppressed() throws Exception {
		Patient patient = new Patient();
		patient.setId("Patient/1/_history/1");
		patient.getText().setDiv("<div>THE DIV</div>");
		patient.addName().addFamily("FAMILY");
		patient.setMaritalStatus(MaritalStatusCodesEnum.D);

		String encoded = ourCtx.newJsonParser().setPrettyPrint(true).setSuppressNarratives(true).encodeResourceToString(patient);
		ourLog.info(encoded);

		assertThat(encoded, containsString("Patient"));
		assertThat(encoded, stringContainsInOrder(Constants.TAG_SUBSETTED_SYSTEM, Constants.TAG_SUBSETTED_CODE));
		assertThat(encoded, not(containsString("text")));
		assertThat(encoded, not(containsString("THE DIV")));
		assertThat(encoded, containsString("family"));
		assertThat(encoded, containsString("maritalStatus"));
	}

	@Test
	public void testEncodeSummary() {
		Patient patient = new Patient();
		patient.setId("Patient/1/_history/1");
		patient.getText().setDiv("<div>THE DIV</div>");
		patient.addName().addFamily("FAMILY");
		patient.setMaritalStatus(MaritalStatusCodesEnum.D);

		String encoded = ourCtx.newJsonParser().setPrettyPrint(true).setSummaryMode(true).encodeResourceToString(patient);
		ourLog.info(encoded);

		assertThat(encoded, containsString("Patient"));
		assertThat(encoded, stringContainsInOrder("\"tag\"", "\"system\": \"" + Constants.TAG_SUBSETTED_SYSTEM + "\",", "\"code\": \"" + Constants.TAG_SUBSETTED_CODE + "\","));
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

		String encoded = ourCtx.newJsonParser().setPrettyPrint(true).setSummaryMode(true).encodeResourceToString(patient);
		ourLog.info(encoded);

		assertThat(encoded, containsString("Patient"));
		assertThat(encoded, stringContainsInOrder("\"tag\"", "\"system\": \"foo\",", "\"code\": \"bar\"", "\"system\": \"" + Constants.TAG_SUBSETTED_SYSTEM + "\",",
				"\"code\": \"" + Constants.TAG_SUBSETTED_CODE + "\","));
		assertThat(encoded, not(containsString("THE DIV")));
		assertThat(encoded, containsString("family"));
		assertThat(encoded, not(containsString("maritalStatus")));
	}

	/**
	 * See #205
	 */
	@Test
	public void testEncodeTags() {
		Patient pt = new Patient();
		pt.addIdentifier().setSystem("sys").setValue("val");

		TagList tagList = new TagList();
		tagList.addTag("scheme", "term", "display");
		ResourceMetadataKeyEnum.TAG_LIST.put(pt, tagList);

		String enc = ourCtx.newJsonParser().encodeResourceToString(pt);

		String expected = "{\"resourceType\":\"Patient\",\"meta\":{\"tag\":[{\"system\":\"scheme\",\"code\":\"term\",\"display\":\"display\"}]},\"identifier\":[{\"system\":\"sys\",\"value\":\"val\"}]}";

		ourLog.info("Expected: " + expected);
		ourLog.info("Actual  : " + enc);

		assertEquals(expected, enc);

	}

	// see #241
	@Test
	public void testEncodeThenParseShouldNotAddSpuriousId() throws Exception {
		Condition condition = new Condition().setVerificationStatus(ConditionVerificationStatusEnum.CONFIRMED);
		ca.uhn.fhir.model.dstu2.resource.Bundle bundle = new ca.uhn.fhir.model.dstu2.resource.Bundle();
		ca.uhn.fhir.model.dstu2.resource.Bundle.Entry entry = new ca.uhn.fhir.model.dstu2.resource.Bundle.Entry();
		entry.setFullUrl(IdDt.newRandomUuid());
		entry.setResource(condition);
		bundle.getEntry().add(entry);
		IParser parser = ourCtx.newJsonParser();
		String json = parser.encodeResourceToString(bundle);
		ourLog.info(json);
		bundle = (ca.uhn.fhir.model.dstu2.resource.Bundle) parser.parseResource(json);
		assertThat(json, not(containsString("\"id\"")));
	}

	@Test
	public void testEncodeWithDontEncodeElements() throws Exception {
		Patient patient = new Patient();
		patient.setId("123");

		ArrayList<IdDt> list = new ArrayList<IdDt>();
		list.add(new IdDt("http://profile"));
		ResourceMetadataKeyEnum.PROFILES.put(patient, list);
		patient.addName().addFamily("FAMILY").addGiven("GIVEN");
		patient.addAddress().addLine("LINE1");

		{
			IParser p = ourCtx.newJsonParser();
			p.setDontEncodeElements(Sets.newHashSet("*.meta", "*.id"));
			p.setPrettyPrint(true);
			String out = p.encodeResourceToString(patient);
			ourLog.info(out);
			assertThat(out, containsString("Patient"));
			assertThat(out, containsString("name"));
			assertThat(out, containsString("address"));
			assertThat(out, not(containsString("id")));
			assertThat(out, not(containsString("meta")));
		}
		{
			IParser p = ourCtx.newJsonParser();
			p.setDontEncodeElements(Sets.newHashSet("Patient.meta", "Patient.id"));
			p.setPrettyPrint(true);
			String out = p.encodeResourceToString(patient);
			ourLog.info(out);
			assertThat(out, containsString("Patient"));
			assertThat(out, containsString("name"));
			assertThat(out, containsString("address"));
			assertThat(out, not(containsString("id")));
			assertThat(out, not(containsString("meta")));
		}
		{
			IParser p = ourCtx.newJsonParser();
			p.setDontEncodeElements(Sets.newHashSet("Patient.name.family"));
			p.setPrettyPrint(true);
			String out = p.encodeResourceToString(patient);
			ourLog.info(out);
			assertThat(out, containsString("GIVEN"));
			assertThat(out, not(containsString("FAMILY")));
		}
		{
			IParser p = ourCtx.newJsonParser();
			p.setDontEncodeElements(Sets.newHashSet("*.meta", "*.id"));
			p.setPrettyPrint(true);
			String out = p.encodeResourceToString(patient);
			ourLog.info(out);
			assertThat(out, containsString("Patient"));
			assertThat(out, containsString("name"));
			assertThat(out, containsString("address"));
			assertThat(out, not(containsString("id")));
			assertThat(out, not(containsString("meta")));
		}
		{
			IParser p = ourCtx.newJsonParser();
			p.setDontEncodeElements(Sets.newHashSet("Patient.meta"));
			p.setEncodeElements(new HashSet<String>(Arrays.asList("Patient.name")));
			p.setPrettyPrint(true);
			String out = p.encodeResourceToString(patient);
			ourLog.info(out);
			assertThat(out, containsString("Patient"));
			assertThat(out, containsString("name"));
			assertThat(out, containsString("id"));
			assertThat(out, not(containsString("address")));
			assertThat(out, not(containsString("meta")));
		}
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

	/**
	 * #65
	 */
	@Test
	public void testJsonPrimitiveWithExtensionEncoding() {

		QuestionnaireResponse parsed = new QuestionnaireResponse();
		parsed.getGroup().setLinkId("value123");
		parsed.getGroup().getLinkIdElement().addUndeclaredExtension(false, "http://123", new StringDt("HELLO"));

		String encoded = ourCtx.newJsonParser().setPrettyPrint(false).encodeResourceToString(parsed);
		ourLog.info(encoded);
		assertThat(encoded, containsString("{\"linkId\":\"value123\",\"_linkId\":{\"extension\":[{\"url\":\"http://123\",\"valueString\":\"HELLO\"}]}}"));

	}

	@Test
	public void testNamespacePreservationEncode() throws Exception {
		//@formatter:off
		String input = "<Patient xmlns=\"http://hl7.org/fhir\" xmlns:xhtml=\"http://www.w3.org/1999/xhtml\">" + 
				"<text>" + 
				"<xhtml:div>" + 
				"<xhtml:img src=\"foo\"/>" + 
				"@fhirabend" + 
				"</xhtml:div>" + 
				"</text>" + 
				"</Patient>";
		//@formatter:on
		Patient parsed = ourCtx.newXmlParser().parseResource(Patient.class, input);

		String expected = "<xhtml:div xmlns:xhtml=\"http://www.w3.org/1999/xhtml\"><xhtml:img src=\"foo\"/>@fhirabend</xhtml:div>";
		assertEquals(expected, parsed.getText().getDiv().getValueAsString());

		String encoded = ourCtx.newJsonParser().encodeResourceToString(parsed);
		ourLog.info(encoded);
		assertThat(encoded, containsString("\"div\":\"" + expected.replace("\"", "\\\"") + "\""));
	}

	@Test
	public void testNamespacePreservationParse() throws Exception {
		String input = "{\"resourceType\":\"Patient\",\"text\":{\"div\":\"<xhtml:div xmlns:xhtml=\\\"http://www.w3.org/1999/xhtml\\\"><xhtml:img src=\\\"foo\\\"/>@fhirabend</xhtml:div>\"}}";
		Patient parsed = ourCtx.newJsonParser().parseResource(Patient.class, input);

		assertEquals("<xhtml:div xmlns:xhtml=\"http://www.w3.org/1999/xhtml\"><xhtml:img src=\"foo\"/>@fhirabend</xhtml:div>", parsed.getText().getDiv().getValueAsString());

		String encoded = ourCtx.newXmlParser().encodeResourceToString(parsed);
		String expected = "<Patient xmlns=\"http://hl7.org/fhir\"><text><xhtml:div xmlns:xhtml=\"http://www.w3.org/1999/xhtml\"><xhtml:img src=\"foo\"/>@fhirabend</xhtml:div></text></Patient>";

		ourLog.info("Expected: {}", expected);
		ourLog.info("Actual  : {}", encoded);

		assertEquals(expected, encoded);
	}

	@Test
	public void testOmitResourceId() {
		Patient p = new Patient();
		p.setId("123");
		p.addName().addFamily("ABC");

		assertThat(ourCtx.newJsonParser().encodeResourceToString(p), stringContainsInOrder("123", "ABC"));
		assertThat(ourCtx.newJsonParser().setOmitResourceId(true).encodeResourceToString(p), containsString("ABC"));
		assertThat(ourCtx.newJsonParser().setOmitResourceId(true).encodeResourceToString(p), not(containsString("123")));
	}

	@Test
	public void testParseAndEncodeBundle() throws Exception {
		String content = IOUtils.toString(JsonParserDstu2Test.class.getResourceAsStream("/bundle-example.json"));

		Bundle parsed = ourCtx.newJsonParser().parseBundle(content);
		assertEquals("Bundle/example/_history/1", parsed.getId().getValue());
		assertEquals("1", parsed.getResourceMetadata().get(ResourceMetadataKeyEnum.VERSION));
		assertEquals("1", parsed.getId().getVersionIdPart());
		assertEquals(new InstantDt("2014-08-18T01:43:30Z"), parsed.getResourceMetadata().get(ResourceMetadataKeyEnum.UPDATED));
		assertEquals("searchset", parsed.getType().getValue());
		assertEquals(3, parsed.getTotalResults().getValue().intValue());
		assertEquals("https://example.com/base/MedicationOrder?patient=347&searchId=ff15fd40-ff71-4b48-b366-09c706bed9d0&page=2", parsed.getLinkNext().getValue());
		assertEquals("https://example.com/base/MedicationOrder?patient=347&_include=MedicationOrder.medication", parsed.getLinkSelf().getValue());

		assertEquals(2, parsed.getEntries().size());

		MedicationOrder p = (MedicationOrder) parsed.getEntries().get(0).getResource();
		assertEquals("Patient/347", p.getPatient().getReference().getValue());
		assertEquals("2014-08-16T05:31:17Z", ResourceMetadataKeyEnum.UPDATED.get(p).getValueAsString());
		assertEquals("http://example.com/base/MedicationOrder/3123/_history/1", p.getId().getValue());

		Medication m = (Medication) parsed.getEntries().get(1).getResource();
		assertEquals("http://example.com/base/Medication/example", m.getId().getValue());
		assertSame(((ResourceReferenceDt) p.getMedication()).getResource(), m);

		String reencoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeBundleToString(parsed);
		ourLog.info(reencoded);

		JsonConfig cfg = new JsonConfig();

		JSON expected = JSONSerializer.toJSON(content.trim(), cfg);
		JSON actual = JSONSerializer.toJSON(reencoded.trim(), cfg);

		String exp = expected.toString().replace("\\r\\n", "\\n"); // .replace("&sect;", "§");
		String act = actual.toString().replace("\\r\\n", "\\n");

		ourLog.info("Expected: {}", exp);
		ourLog.info("Actual  : {}", act);

		assertEquals(exp, act);

	}

	/**
	 * Test for #146
	 */
	@Test
	public void testParseAndEncodeBundleFromXmlToJson() throws Exception {
		String content = IOUtils.toString(JsonParserDstu2Test.class.getResourceAsStream("/bundle-example2.xml"));

		ca.uhn.fhir.model.dstu2.resource.Bundle parsed = ourCtx.newXmlParser().parseResource(ca.uhn.fhir.model.dstu2.resource.Bundle.class, content);

		MedicationOrder p = (MedicationOrder) parsed.getEntry().get(0).getResource();
		assertEquals("#med", ((ResourceReferenceDt) p.getMedication()).getReference().getValue());

		Medication m = (Medication) ((ResourceReferenceDt) p.getMedication()).getResource();
		assertNotNull(m);
		assertEquals("#med", m.getId().getValue());
		assertEquals(1, p.getContained().getContainedResources().size());
		assertSame(m, p.getContained().getContainedResources().get(0));

		String reencoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(parsed);
		ourLog.info(reencoded);
		assertThat(reencoded, containsString("contained"));

		reencoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(parsed);
		ourLog.info(reencoded);
		assertThat(reencoded, containsString("contained"));
	}

	@Test
	public void testParseAndEncodeBundleNewStyle() throws Exception {
		String content = IOUtils.toString(JsonParserDstu2Test.class.getResourceAsStream("/bundle-example.json"));

		ca.uhn.fhir.model.dstu2.resource.Bundle parsed = ourCtx.newJsonParser().parseResource(ca.uhn.fhir.model.dstu2.resource.Bundle.class, content);
		assertEquals("Bundle/example/_history/1", parsed.getId().getValue());
		assertEquals("1", parsed.getResourceMetadata().get(ResourceMetadataKeyEnum.VERSION));
		assertEquals("1", parsed.getId().getVersionIdPart());
		assertEquals(new InstantDt("2014-08-18T01:43:30Z"), parsed.getResourceMetadata().get(ResourceMetadataKeyEnum.UPDATED));
		assertEquals("searchset", parsed.getType());
		assertEquals(3, parsed.getTotal().intValue());
		assertEquals("https://example.com/base/MedicationOrder?patient=347&searchId=ff15fd40-ff71-4b48-b366-09c706bed9d0&page=2", parsed.getLink().get(0).getUrlElement().getValueAsString());
		assertEquals("https://example.com/base/MedicationOrder?patient=347&_include=MedicationOrder.medication", parsed.getLink().get(1).getUrlElement().getValueAsString());

		assertEquals(2, parsed.getEntry().size());

		MedicationOrder p = (MedicationOrder) parsed.getEntry().get(0).getResource();
		assertEquals("Patient/347", p.getPatient().getReference().getValue());
		assertEquals("2014-08-16T05:31:17Z", ResourceMetadataKeyEnum.UPDATED.get(p).getValueAsString());
		assertEquals("http://example.com/base/MedicationOrder/3123/_history/1", p.getId().getValue());

		Medication m = (Medication) parsed.getEntry().get(1).getResource();
		assertEquals("http://example.com/base/Medication/example", m.getId().getValue());
		assertEquals("Medication/example", ((ResourceReferenceDt) p.getMedication()).getReference().getValue());
		assertSame(((ResourceReferenceDt) p.getMedication()).getResource(), m);

		String reencoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(parsed);
		ourLog.info(reencoded);

		JsonConfig cfg = new JsonConfig();

		JSON expected = JSONSerializer.toJSON(content.trim(), cfg);
		JSON actual = JSONSerializer.toJSON(reencoded.trim(), cfg);

		String exp = expected.toString().replace("\\r\\n", "\\n"); // .replace("&sect;", "§");
		String act = actual.toString().replace("\\r\\n", "\\n");

		ourLog.info("Expected: {}", exp);
		ourLog.info("Actual  : {}", act);

		assertEquals(exp, act);

	}

	@Test
	public void testParseAndEncodeBundleOldStyle() throws Exception {
		String content = IOUtils.toString(JsonParserDstu2Test.class.getResourceAsStream("/bundle-example.json"));

		Bundle parsed = ourCtx.newJsonParser().parseBundle(content);

		assertEquals(new InstantDt("2014-08-18T01:43:30Z"), parsed.getResourceMetadata().get(ResourceMetadataKeyEnum.UPDATED));
		assertEquals("searchset", parsed.getType().getValue());
		assertEquals(3, parsed.getTotalResults().getValue().intValue());

		assertEquals(2, parsed.getEntries().size());

		MedicationOrder p = (MedicationOrder) parsed.getEntries().get(0).getResource();
		assertEquals("Patient/347", p.getPatient().getReference().getValue());
		assertEquals("2014-08-16T05:31:17Z", ResourceMetadataKeyEnum.UPDATED.get(p).getValueAsString());
		assertEquals("http://example.com/base/MedicationOrder/3123/_history/1", p.getId().getValue());

		Medication m = (Medication) parsed.getEntries().get(1).getResource();
		assertEquals("http://example.com/base/Medication/example", m.getId().getValue());
		assertEquals("Medication/example", ((ResourceReferenceDt) p.getMedication()).getReference().getValue());
		assertSame(((ResourceReferenceDt) p.getMedication()).getResource(), m);

		String reencoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeBundleToString(parsed);
		ourLog.info(reencoded);

		JsonConfig cfg = new JsonConfig();

		JSON expected = JSONSerializer.toJSON(content.trim(), cfg);
		JSON actual = JSONSerializer.toJSON(reencoded.trim(), cfg);

		String exp = expected.toString().replace("\\r\\n", "\\n"); // .replace("&sect;", "§");
		String act = actual.toString().replace("\\r\\n", "\\n");

		exp = exp.replace(",\"ifNoneExist\":\"Patient?identifier=234234\"", "");

		ourLog.info("Expected: {}", exp);
		ourLog.info("Actual  : {}", act);

		assertEquals(exp, act);

	}

	@Test
	public void testParseAndEncodeBundleResourceWithComments() throws Exception {
		String content = IOUtils.toString(JsonParserDstu2Test.class.getResourceAsStream("/bundle-transaction2.json"));

		ourCtx.newJsonParser().parseBundle(content);

		ca.uhn.fhir.model.dstu2.resource.Bundle parsed = ourCtx.newJsonParser().parseResource(ca.uhn.fhir.model.dstu2.resource.Bundle.class, content);

		// TODO: preserve comments
	}

	@Test
	public void testParseAndEncodeBundleWithDeletedEntry() {

		Patient res = new Patient();
		res.setId(new IdDt("Patient", "111", "222"));
		ResourceMetadataKeyEnum.DELETED_AT.put(res, new InstantDt("2011-01-01T12:12:22Z"));

		Bundle bundle = new Bundle();
		bundle.addResource(res, ourCtx, "http://foo/base");

		String actual = ourCtx.newJsonParser().encodeBundleToString(bundle);
		ourLog.info(actual);

		String expected = "{\"resourceType\":\"Bundle\",\"entry\":[{\"deleted\":{\"type\":\"Patient\",\"resourceId\":\"111\",\"versionId\":\"222\",\"instant\":\"2011-01-01T12:12:22Z\"}}]}";
		assertEquals(expected, actual);

	}

	@Test
	public void testParseAndEncodeBundleWithUuidBase() {
		//@formatter:off
		String input = 
				"{\n" + 
				"    \"resourceType\":\"Bundle\",\n" + 
				"    \"type\":\"document\",\n" + 
				"    \"entry\":[\n" + 
				"        {\n" + 
				"            \"fullUrl\":\"urn:uuid:180f219f-97a8-486d-99d9-ed631fe4fc57\",\n" + 
				"            \"resource\":{\n" + 
				"                \"resourceType\":\"Composition\",\n" + 
				"                \"id\":\"180f219f-97a8-486d-99d9-ed631fe4fc57\",\n" + 
				"                \"meta\":{\n" + 
				"                    \"lastUpdated\":\"2013-05-28T22:12:21Z\"\n" + 
				"                },\n" + 
				"                \"text\":{\n" + 
				"                    \"status\":\"generated\",\n" + 
				"                    \"div\":\"<div xmlns=\\\"http://www.w3.org/1999/xhtml\\\"><p><b>Generated Narrative with Details</b></p><p><b>id</b>: 180f219f-97a8-486d-99d9-ed631fe4fc57</p><p><b>meta</b>: </p><p><b>date</b>: Feb 1, 2013 12:30:02 PM</p><p><b>type</b>: Discharge Summary from Responsible Clinician <span>(Details : {LOINC code '28655-9' = 'Physician attending Discharge summary)</span></p><p><b>status</b>: final</p><p><b>confidentiality</b>: N</p><p><b>author</b>: <a>Doctor Dave. Generated Summary: 23; Adam Careful </a></p><p><b>encounter</b>: <a>http://fhir.healthintersections.com.au/open/Encounter/doc-example</a></p></div>\"\n" + 
				"                },\n" + 
				"                \"date\":\"2013-02-01T12:30:02Z\",\n" + 
				"                \"type\":{\n" + 
				"                    \"coding\":[\n" + 
				"                        {\n" + 
				"                            \"system\":\"http://loinc.org\",\n" + 
				"                            \"code\":\"28655-9\"\n" + 
				"                        }\n" + 
				"                    ],\n" + 
				"                    \"text\":\"Discharge Summary from Responsible Clinician\"\n" + 
				"                },\n" + 
				"                \"status\":\"final\",\n" + 
				"                \"confidentiality\":\"N\",\n" + 
				"                \"subject\":{\n" + 
				"                    \"reference\":\"http://fhir.healthintersections.com.au/open/Patient/d1\",\n" + 
				"                    \"display\":\"Eve Everywoman\"\n" + 
				"                },\n" + 
				"                \"author\":[\n" + 
				"                    {\n" + 
				"                        \"reference\":\"Practitioner/example\",\n" + 
				"                        \"display\":\"Doctor Dave\"\n" + 
				"                    }\n" + 
				"                ],\n" + 
				"                \"encounter\":{\n" + 
				"                    \"reference\":\"http://fhir.healthintersections.com.au/open/Encounter/doc-example\"\n" + 
				"                },\n" + 
				"                \"section\":[\n" + 
				"                    {\n" + 
				"                        \"title\":\"Reason for admission\",\n" + 
				"                        \"content\":{\n" + 
				"                            \"reference\":\"urn:uuid:d0dd51d3-3ab2-4c84-b697-a630c3e40e7a\"\n" + 
				"                        }\n" + 
				"                    },\n" + 
				"                    {\n" + 
				"                        \"title\":\"Medications on Discharge\",\n" + 
				"                        \"content\":{\n" + 
				"                            \"reference\":\"urn:uuid:673f8db5-0ffd-4395-9657-6da00420bbc1\"\n" + 
				"                        }\n" + 
				"                    },\n" + 
				"                    {\n" + 
				"                        \"title\":\"Known allergies\",\n" + 
				"                        \"content\":{\n" + 
				"                            \"reference\":\"urn:uuid:68f86194-e6e1-4f65-b64a-5314256f8d7b\"\n" + 
				"                        }\n" + 
				"                    }\n" + 
				"                ]\n" + 
				"            }\n" + 
				"        }" +
				"    ]" +
				"}";
		//@formatter:on

		ca.uhn.fhir.model.dstu2.resource.Bundle parsed = ourCtx.newJsonParser().parseResource(ca.uhn.fhir.model.dstu2.resource.Bundle.class, input);

		String encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(parsed);
		ourLog.info(encoded);

		assertEquals("urn:uuid:180f219f-97a8-486d-99d9-ed631fe4fc57", parsed.getEntry().get(0).getResource().getId().getValue());
		assertEquals(null, parsed.getEntry().get(0).getResource().getId().getBaseUrl());
		assertEquals("urn:uuid:180f219f-97a8-486d-99d9-ed631fe4fc57", parsed.getEntry().get(0).getResource().getId().getIdPart());
		assertThat(encoded, not(containsString("\"id\":\"180f219f-97a8-486d-99d9-ed631fe4fc57\"")));
	}

	@Test
	public void testParseAndEncodeComments() throws IOException {
		//@formatter:off
		String input = "{\n" + 
				"  \"resourceType\": \"Patient\",\n" + 
				"  \"id\": \"pat1\",\n" + 
				"  \"text\": {\n" + 
				"    \"status\": \"generated\",\n" + 
				"    \"div\": \"<div>\\n      \\n      <p>Patient Donald DUCK @ Acme Healthcare, Inc. MR = 654321</p>\\n    \\n    </div>\"\n" + 
				"  },\n" + 
				"  \"identifier\": [\n" + 
				"    {\n" + 
				"      \"fhir_comments\":[\"identifier comment 1\",\"identifier comment 2\"],\n" +
				"      \"use\": \"usual\",\n" + 
				"      \"_use\": {\n" + 
				"        \"fhir_comments\":[\"use comment 1\",\"use comment 2\"]\n" +
				"      },\n" + 
				"      \"type\": {\n" + 
				"        \"coding\": [\n" + 
				"          {\n" + 
				"            \"system\": \"http://hl7.org/fhir/v2/0203\",\n" + 
				"            \"code\": \"MR\"\n" + 
				"          }\n" + 
				"        ]\n" + 
				"      },\n" + 
				"      \"system\": \"urn:oid:0.1.2.3.4.5.6.7\",\n" + 
				"      \"value\": \"654321\"\n" + 
				"    }\n" + 
				"  ],\n" + 
				"  \"active\": true" +
				"}";
		//@formatter:off

		Patient res = ourCtx.newJsonParser().parseResource(Patient.class, input);
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
				"\"identifier\": [", 
				"{",
				"\"fhir_comments\":",
				"[",
				"\"identifier comment 1\"",
				",",
				"\"identifier comment 2\"",
				"]",
				"\"use\": \"usual\",", 
				"\"_use\": {", 
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
	}

	@Test
	public void testParseBundleWithBinary() {
		Binary patient = new Binary();
		patient.setId(new IdDt("http://base/Binary/11/_history/22"));
		patient.setContentType("foo");
		patient.setContent(new byte[] { 1, 2, 3, 4 });

		String val = ourCtx.newJsonParser().encodeResourceToString(patient);
		assertEquals("{\"resourceType\":\"Binary\",\"id\":\"11\",\"meta\":{\"versionId\":\"22\"},\"contentType\":\"foo\",\"content\":\"AQIDBA==\"}", val);
	}

	@Test
	public void testParseBundleWithCustomObservationType() {
		ReportObservation obs = new ReportObservation();
		obs.setReadOnly(true);

		IParser p = ourCtx.newJsonParser();
//		p.set
		p.setParserErrorHandler(mock(IParserErrorHandler.class, new ThrowsException(new IllegalStateException())));

		String encoded = p.encodeResourceToString(obs);
		ourLog.info(encoded);

		obs = p.parseResource(ReportObservation.class, encoded);
		assertEquals(true, obs.getReadOnly().getValue().booleanValue());
	}

	/**
	 * see #144 and #146
	 */
	@Test
	public void testParseContained() {

		FhirContext c = FhirContext.forDstu2();
		IParser parser = c.newJsonParser().setPrettyPrint(true);

		Observation o = new Observation();
		o.getCode().setText("obs text");

		Patient p = new Patient();
		p.addName().addFamily("patient family");
		o.getSubject().setResource(p);

		String enc = parser.encodeResourceToString(o);
		ourLog.info(enc);

		//@formatter:off
		assertThat(enc, stringContainsInOrder(
			"\"resourceType\": \"Observation\"",
			"\"contained\": [",
			"\"resourceType\": \"Patient\",",
			"\"id\": \"1\"",
			"\"reference\": \"#1\""
			));
		//@formatter:on

		o = parser.parseResource(Observation.class, enc);
		assertEquals("obs text", o.getCode().getText());

		assertNotNull(o.getSubject().getResource());
		p = (Patient) o.getSubject().getResource();
		assertEquals("patient family", p.getNameFirstRep().getFamilyAsSingleString());
	}

	/**
	 * See #414
	 */
	@Test
	public void testParseJsonExtensionWithoutUrl() {
		//@formatter:off
		String input = 
			"{\"resourceType\":\"Patient\"," +
			"\"extension\":[ {\"valueDateTime\":\"2011-01-02T11:13:15\"} ]" +
			"}";
		//@formatter:on

		IParser parser = ourCtx.newJsonParser();
		parser.setParserErrorHandler(new LenientErrorHandler());
		Patient parsed = (Patient) parser.parseResource(input);
		assertEquals(1, parsed.getAllUndeclaredExtensions().size());
		assertEquals(null, parsed.getAllUndeclaredExtensions().get(0).getUrl());
		assertEquals("2011-01-02T11:13:15", parsed.getAllUndeclaredExtensions().get(0).getValueAsPrimitive().getValueAsString());

		try {
			parser = ourCtx.newJsonParser();
			parser.setParserErrorHandler(new StrictErrorHandler());
			parser.parseResource(input);
			fail();
		} catch (DataFormatException e) {
			assertEquals("Resource is missing required element 'url' in parent element 'extension'", e.getMessage());
		}

	}

	/**
	 * See #414
	 */
	@Test
	public void testParseJsonModifierExtensionWithoutUrl() {
		//@formatter:off
		String input = 
			"{\"resourceType\":\"Patient\"," +
			"\"modifierExtension\":[ {\"valueDateTime\":\"2011-01-02T11:13:15\"} ]" +
			"}";
		//@formatter:on

		IParser parser = ourCtx.newJsonParser();
		parser.setParserErrorHandler(new LenientErrorHandler());
		Patient parsed = (Patient) parser.parseResource(input);
		assertEquals(1, parsed.getAllUndeclaredExtensions().size());
		assertEquals(null, parsed.getAllUndeclaredExtensions().get(0).getUrl());
		assertEquals("2011-01-02T11:13:15", parsed.getAllUndeclaredExtensions().get(0).getValueAsPrimitive().getValueAsString());

		try {
			parser = ourCtx.newJsonParser();
			parser.setParserErrorHandler(new StrictErrorHandler());
			parser.parseResource(input);
			fail();
		} catch (DataFormatException e) {
			assertEquals("Resource is missing required element 'url' in parent element 'modifierExtension'", e.getMessage());
		}

	}

	@Test
	public void testParseMetadata() throws Exception {
		//@formatter:off
		String bundle = "{\n" + 
			"  \"resourceType\" : \"Bundle\",\n" + 
			"  \"total\" : 1,\n" + 
			"   \"link\": [{\n" + 
			"      \"relation\" : \"self\",\n" + 
			"      \"url\" : \"http://localhost:52788/Binary?_pretty=true\"\n" + 
			"   }],\n" + 
			"   \"entry\" : [{\n" + 
			"      \"fullUrl\" : \"http://foo/fhirBase2/Patient/1/_history/2\",\n" +
			"      \"resource\" : {\n" + 
			"         \"resourceType\" : \"Patient\",\n" + 
			"         \"id\" : \"1\",\n" + 
			"         \"meta\" : {\n" +
			"            \"versionId\" : \"2\",\n" +
			"            \"lastUpdated\" : \"2001-02-22T11:22:33-05:00\"\n" +
			"         },\n" + 
			"         \"birthDate\" : \"2012-01-02\"\n" + 
			"      },\n" + 
			"      \"search\" : {\n" +
			"         \"mode\" : \"match\",\n" +
			"         \"score\" : 0.123\n" +
			"      },\n" +
			"      \"request\" : {\n" +
			"         \"method\" : \"POST\",\n" +
			"         \"url\" : \"http://foo/Patient?identifier=value\"\n" +
			"      }\n" +
			"   }]\n" + 
			"}";
		//@formatter:on

		Bundle b = ourCtx.newJsonParser().parseBundle(bundle);
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
		String reEncoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeBundleToString(toBundle);

		JsonConfig cfg = new JsonConfig();

		JSON expected = JSONSerializer.toJSON(bundle.trim(), cfg);
		JSON actual = JSONSerializer.toJSON(reEncoded.trim(), cfg);

		String exp = expected.toString().replace("\\r\\n", "\\n"); // .replace("&sect;", "§");
		String act = actual.toString().replace("\\r\\n", "\\n");

		ourLog.info("Expected: {}", exp);
		ourLog.info("Actual  : {}", act);

		assertEquals(exp, act);

	}

	/**
	 * See #484
	 */
	@Test
	public void testParseNarrativeWithEmptyDiv() {
		String input = "{\"resourceType\":\"Basic\",\"id\":\"1\",\"text\":{\"status\":\"generated\",\"div\":\"<div/>\"}}";
		Basic basic = ourCtx.newJsonParser().parseResource(Basic.class, input);
		assertEquals("<div/>", basic.getText().getDivAsString());
	}

	/**
	 * See #359 - This is the base test with no nulls, other testParseNullsFOO have nulls in them
	 */
	@Test
	public void testParseNullsArray() {
		//@formatter:off
		String input =
			"{\n" + 
			"    \"resourceType\":\"Patient\",\n" +
			"    \"id\":\"123\",\n" +
			"    \"gender\":\"male\",\n" +
			"    \"name\":null\n" + 
			"}";
		//@formatter:on

		Patient patient = ourCtx.newJsonParser().parseResource(Patient.class, input);
		assertEquals("Patient/123", patient.getId().getValue());
		assertEquals(AdministrativeGenderEnum.MALE, patient.getGenderElement().getValueAsEnum());
		assertEquals("", patient.getNameFirstRep().getFamilyAsSingleString());
	}

	/**
	 * See #359 - This is the base test with no nulls, other testParseNullsFOO have nulls in them
	 */
	@Test
	public void testParseNullsNone() {
		//@formatter:off
		String input =
			"{\n" + 
			"    \"resourceType\":\"Patient\",\n" +
			"    \"id\":\"123\",\n" +
			"    \"gender\":\"male\",\n" +
			"    \"name\":[\n" + 
			"        {\n" + 
			"            \"family\":[\n" + 
			"                \"FAMILY\"\n" + 
			"            ]\n" + 
			"        }\n" + 
			"    ]\n" + 
			"}";
		//@formatter:on

		Patient patient = ourCtx.newJsonParser().parseResource(Patient.class, input);
		assertEquals("Patient/123", patient.getId().getValue());
		assertEquals(AdministrativeGenderEnum.MALE, patient.getGenderElement().getValueAsEnum());
		assertEquals("FAMILY", patient.getNameFirstRep().getFamilyAsSingleString());
	}

	/**
	 * See #359 - This is the base test with no nulls, other testParseNullsFOO have nulls in them
	 */
	@Test
	public void testParseNullsObject() {
		//@formatter:off
		String input =
			"{\n" + 
			"    \"resourceType\":\"Patient\",\n" +
			"    \"id\":\"123\",\n" +
			"    \"gender\":\"male\",\n" +
			"    \"name\":[\n" + 
			"        null\n" + 
			"    ]\n" + 
			"}";
		//@formatter:on

		Patient patient = ourCtx.newJsonParser().parseResource(Patient.class, input);
		assertEquals("Patient/123", patient.getId().getValue());
		assertEquals(AdministrativeGenderEnum.MALE, patient.getGenderElement().getValueAsEnum());
		assertEquals("", patient.getNameFirstRep().getFamilyAsSingleString());
	}

	/**
	 * See #359 - Let's be lenient about nulls!
	 */
	@Test
	public void testParseNullsPrimitive() {
		//@formatter:off
		String input =
			"{\n" + 
			"    \"resourceType\":\"Patient\",\n" +
			"    \"id\":null,\n" +
			"    \"gender\":null,\n" +
			"    \"name\":[\n" + 
			"        {\n" + 
			"            \"family\":[\n" + 
			"                \"FAMILY\"\n" + 
			"            ]\n" + 
			"        }\n" + 
			"    ]\n" + 
			"}";
		//@formatter:on

		Patient patient = ourCtx.newJsonParser().parseResource(Patient.class, input);
		assertEquals(null, patient.getId().getValue());
		assertEquals(null, patient.getGenderElement().getValueAsEnum());
		assertEquals("FAMILY", patient.getNameFirstRep().getFamilyAsSingleString());
	}

	@Test
	public void testParsePatientInBundle() {

		String text = "{\"resourceType\":\"Bundle\",\"id\":null,\"base\":\"http://localhost:57931/fhir/contextDev\",\"total\":1,\"link\":[{\"relation\":\"self\",\"url\":\"http://localhost:57931/fhir/contextDev/Patient?identifier=urn%3AMultiFhirVersionTest%7CtestSubmitPatient01&_format=json\"}],\"entry\":[{\"resource\":{\"resourceType\":\"Patient\",\"id\":\"1\",\"meta\":{\"versionId\":\"1\",\"lastUpdated\":\"2014-12-20T18:41:29.706-05:00\"},\"identifier\":[{\"system\":\"urn:MultiFhirVersionTest\",\"value\":\"testSubmitPatient01\"}]}}]}";
		FhirContext ctx = FhirContext.forDstu2();
		Bundle b = ctx.newJsonParser().parseBundle(text);

		IResource patient = b.getEntries().get(0).getResource();
		assertEquals(Patient.class, patient.getClass());

		assertNull(ResourceMetadataKeyEnum.TAG_LIST.get(patient));
		assertNull(ResourceMetadataKeyEnum.PROFILES.get(patient));
	}

	/**
	 * See #163
	 */
	@Test
	public void testParseResourceType() {
		IParser jsonParser = ourCtx.newJsonParser().setPrettyPrint(true);

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

		String bundleText = jsonParser.encodeResourceToString(bundle);
		ourLog.info(bundleText);

		ca.uhn.fhir.model.dstu2.resource.Bundle reincarnatedBundle = jsonParser.parseResource(ca.uhn.fhir.model.dstu2.resource.Bundle.class, bundleText);
		Patient reincarnatedPatient = (Patient) reincarnatedBundle.getEntry().get(0).getResource();

		assertEquals("Patient", patient.getId().getResourceType());
		assertEquals("Patient", reincarnatedPatient.getId().getResourceType());
	}

	/**
	 * See #207
	 */
	@Test
	public void testParseResourceWithInvalidType() {
		//@formatter:off
		String input = "{" + 
			"\"resourceType\":\"Patient\"," + 
			 "\"contained\":[" + 
			 "    {" + 
			 "       \"rezType\":\"Organization\"" + 
			 "    }" + 
			 "  ]" + 
			 "}";
		//@formatter:on

		IParser jsonParser = ourCtx.newJsonParser().setPrettyPrint(true);
		try {
			jsonParser.parseResource(input);
			fail();
		} catch (DataFormatException e) {
			assertEquals("Missing required element 'resourceType' from JSON resource object, unable to parse", e.getMessage());
		}
	}

	@Test
	public void testParseWithExtensions() throws Exception {
		String input = IOUtils.toString(getClass().getResourceAsStream("/patient1.json"));
		IParser parser = ourCtx.newJsonParser();
		IParserErrorHandler peh = mock(IParserErrorHandler.class);
		parser.setParserErrorHandler(peh);
		Patient p = parser.parseResource(Patient.class, input);

		ArgumentCaptor<String> capt = ArgumentCaptor.forClass(String.class);
		verify(peh, times(4)).unknownElement(Mockito.isNull(IParseLocation.class), capt.capture());

		//@formatter:off
		List<String> strings = capt.getAllValues();
		assertThat(strings, contains(
			"extension",
			"extension",
			"modifierExtension",
			"modifierExtension"
		));
		//@formatter:off
		
		assertEquals("Smith", p.getName().get(0).getGiven().get(0).getValue());
	}

	@Test
	public void testParseWithWrongTypeObjectShouldBeArray() throws Exception {
		String input = IOUtils.toString(getClass().getResourceAsStream("/invalid_metadata.json"));
		try {
			ourCtx.newJsonParser().parseResource(Conformance.class, input);
			fail();
		} catch (DataFormatException e) {
			assertEquals("Syntax error parsing JSON FHIR structure: Expected ARRAY at element 'modifierExtension', found 'OBJECT'", e.getMessage());
		}
	}

	/**
	 * See #449
	 */
	@Test
	public void testReferenceEncodingOnCustomType() {
		Organization org = new Organization();
		org.setId("000111");
		org.setName("Owner institution");
		
		ExtendedDevice dev = new ExtendedDevice();
		dev.setId("000222");
		CodingDt devType = new CodingDt();
		
		devType.setSystem("http://devTypeSystem");
		devType.setCode("0");
		dev.getType().addCoding(devType);
		
		ExtensionDt someExt = new ExtensionDt();
		someExt.setUrl("http://extensionsBaseUrl/Device#someExt");
		ResourceReferenceDt orgRef = new ResourceReferenceDt();
		orgRef.setResource(org);
		someExt.setValue(orgRef); //this works
		dev.addUndeclaredExtension(someExt);
		
		dev.getSomeOrg().setResource(org); //this doesn't work
		dev.setSomeOtherOrg(new ResourceReferenceDt(org)); //this almost works, the Organization/ prefix is missing
		
		dev.getOwner().setResource(org); //this works
		
		ca.uhn.fhir.model.dstu2.resource.Bundle bundle = new ca.uhn.fhir.model.dstu2.resource.Bundle();
		bundle.setId(new IdDt("000333"));
		bundle.addEntry().setResource(dev);
		bundle.addEntry().setResource(org);
		
		String encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(bundle);
		ourLog.info(encoded);
		
		assertThat(encoded, containsString("reference\": \"Organization/000111\""));
	}

	/**
	 * See #144 and #146
	 */
	@Test
	public void testReportSerialize() {

		ReportObservation obsv = new ReportObservation();
		obsv.getCode().addCoding().setCode("name");
		obsv.setValue(new StringDt("value test"));
		obsv.setStatus(ObservationStatusEnum.FINAL);
		obsv.addIdentifier().setSystem("System").setValue("id value");

		DiagnosticReport report = new DiagnosticReport();
		report.getContained().getContainedResources().add(obsv);
		report.addResult().setResource(obsv);

		IParser parser = ourCtx.newXmlParser().setPrettyPrint(true);
		String message = parser.encodeResourceToString(report);
		ourLog.info(message);
		Assert.assertThat(message, containsString("contained"));
	}

	/**
	 * See #144 and #146
	 */
	@Test
	public void testReportSerializeWithMatchingId() {

		ReportObservation obsv = new ReportObservation();
		obsv.getCode().addCoding().setCode("name");
		obsv.setValue(new StringDt("value test"));
		obsv.setStatus(ObservationStatusEnum.FINAL);
		obsv.addIdentifier().setSystem("System").setValue("id value");

		DiagnosticReport report = new DiagnosticReport();
		report.getContained().getContainedResources().add(obsv);

		obsv.setId("#123");
		report.addResult().setReference("#123");

		IParser parser = ourCtx.newXmlParser().setPrettyPrint(true);
		String message = parser.encodeResourceToString(report);
		ourLog.info(message);
		Assert.assertThat(message, containsString("contained"));
	}
	
	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}
	
	/**
	 * See #537
	 */
	@Test
	public void testEncodeNestedContained() {
		
		Organization org04 = new Organization();
		org04.setName("LEVEL04");
		
		Organization org03 = new Organization();
		org03.setName("LEVEL03");
		org03.getPartOf().setResource(org04);
		
		Organization org02 = new Organization();
		org02.setName("LEVEL02");
		org02.getPartOf().setResource(org03);
		
		Organization org01 = new Organization();
		org01.setName("LEVEL01");
		org01.getPartOf().setResource(org02);
		
		String encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(org01);
		ourLog.info(encoded);
		
		assertThat(encoded, stringContainsInOrder("LEVEL02","LEVEL03","LEVEL04","LEVEL01" ));
	}
	
	
	/**
	 * See #505
	 */
	@Test
	public void testIncludeResourceWhenEncoding() {
		Condition condition = new Condition();
		condition.setDateRecorded(new DateDt("2011-01-01"));
		
		Goal goal = new Goal();
		goal.setId("Goal1");
		ResourceReferenceDt resourceReferenceDt = new ResourceReferenceDt(condition);
		goal.setAddresses(Collections.singletonList(resourceReferenceDt));

		ca.uhn.fhir.model.dstu2.resource.Bundle bundle = new ca.uhn.fhir.model.dstu2.resource.Bundle();
		Entry entry = bundle.addEntry();
		entry.setResource(goal);
		
		IParser parser = ourCtx.newJsonParser();
		
		String resourceToString = parser.setPrettyPrint(true).encodeResourceToString(bundle);
		ourLog.info(resourceToString);
		
		assertThat(resourceToString, containsString("2011-01-01"));
		
		bundle = parser.parseResource(ca.uhn.fhir.model.dstu2.resource.Bundle.class, resourceToString);
		assertEquals(1, bundle.getEntry().size());
		goal = (Goal) bundle.getEntry().get(0).getResource();
		
		condition = (Condition) goal.getAddresses().get(0).getResource();
		
		assertEquals("2011-01-01", condition.getDateRecordedElement().getValueAsString());
	}

	/**
	 * Test for the url generated based on the server config
	 */
	@Test
	public void testGeneratedUrls() {
		final IParser jsonParser = ourCtx.newJsonParser().setPrettyPrint(true);
		jsonParser.setServerBaseUrl("http://myserver.com");

		final CustomPatientDstu2 patient = new CustomPatientDstu2();
		patient.setHomeless(new BooleanDt(true));

		final String parsedPatient = jsonParser.encodeResourceToString(patient);

		assertTrue(parsedPatient.contains("http://myserver.com/StructureDefinition/Patient"));
		assertTrue(parsedPatient.contains("http://myserver.com/StructureDefinition/homeless"));
	}	
  
	/**
	 * Test for the url generated based on the server config
	 */
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
		assertNull("myName", newPatient.getPetName().getValue());
		assertEquals("myName", ((StringDt) newPatient.getUndeclaredExtensionsByUrl("http://www.example.com/petname").get(0).getValue()).getValue());
	}

	@Test
	public void testCustomUrlExtensioninBundle() {
		final String expected = "{\"resourceType\":\"Bundle\",\"entry\":[{\"resource\":{\"resourceType\":\"Patient\",\"extension\":[{\"url\":\"http://www.example.com/petname\",\"valueString\":\"myName\"}]}}]}";

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
}
