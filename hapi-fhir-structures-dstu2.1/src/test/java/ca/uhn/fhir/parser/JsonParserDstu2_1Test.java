package ca.uhn.fhir.parser;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.empty;
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
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;

import org.apache.commons.io.IOUtils;
import org.hamcrest.Matchers;
import org.hamcrest.core.StringContains;
import org.hl7.fhir.dstu2016may.model.*;
import org.hl7.fhir.dstu2016may.model.Address.AddressUse;
import org.hl7.fhir.dstu2016may.model.Address.AddressUseEnumFactory;
import org.hl7.fhir.dstu2016may.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.dstu2016may.model.Bundle.BundleType;
import org.hl7.fhir.dstu2016may.model.Condition.ConditionVerificationStatus;
import org.hl7.fhir.dstu2016may.model.Conformance.UnknownContentCode;
import org.hl7.fhir.dstu2016may.model.Enumeration;
import org.hl7.fhir.dstu2016may.model.Enumerations.AdministrativeGender;
import org.hl7.fhir.dstu2016may.model.Identifier.IdentifierUse;
import org.hl7.fhir.dstu2016may.model.Observation.ObservationStatus;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.utilities.xhtml.XhtmlNode;
import org.junit.*;
import org.mockito.ArgumentCaptor;

import com.google.common.collect.Sets;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.narrative.DefaultThymeleafNarrativeGenerator;
import ca.uhn.fhir.parser.IParserErrorHandler.IParseLocation;
import ca.uhn.fhir.parser.PatientWithExtendedContactDstu3.CustomContactComponent;
import ca.uhn.fhir.parser.XmlParserDstu2_1Test.TestPatientFor327;
import ca.uhn.fhir.util.TestUtil;
import net.sf.json.*;

public class JsonParserDstu2_1Test {
	private static FhirContext ourCtx = FhirContext.forDstu2_1();
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(JsonParserDstu2_1Test.class);

	@After
	public void after() {
		ourCtx.setNarrativeGenerator(null);
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
			ourCtx = FhirContext.forDstu2_1();
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
			ourCtx = FhirContext.forDstu2_1();
		}
	}

	/**
	 * #480
	 */
	@Test
	public void testEncodeEmptyValue() {
		QuestionnaireResponse qr = new QuestionnaireResponse();
		qr.setId("123");
		qr.getAuthoredElement().setValueAsString("");
		qr.addItem().setLinkIdElement(new StringType());
		qr.getItem().get(0).addItem().setLinkIdElement(new StringType(""));
		qr.getItem().get(0).addItem().setLinkIdElement(new StringType("LINKID"));

		String encoded = ourCtx.newJsonParser().encodeResourceToString(qr);
		ourLog.info(encoded);

		assertThat(encoded, stringContainsInOrder("123"));
		assertThat(encoded, not(stringContainsInOrder("\"\"")));
		assertThat(encoded, not(stringContainsInOrder("null")));
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

		String output = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(patient);
		ourLog.info(output);

		String enc = ourCtx.newJsonParser().encodeResourceToString(patient);
		assertThat(enc, Matchers.stringContainsInOrder("{\"resourceType\":\"Patient\",", "\"extension\":[{\"url\":\"http://example.com/extensions#someext\",\"valueDateTime\":\"2011-01-02T11:13:15\"}",
				"{\"url\":\"http://example.com#parent\",\"extension\":[{\"url\":\"http://example.com#child\",\"valueString\":\"value1\"},{\"url\":\"http://example.com#child\",\"valueString\":\"value2\"}]}"));
		assertThat(enc, Matchers.stringContainsInOrder("\"modifierExtension\":[" + "{" + "\"url\":\"http://example.com/extensions#modext\"," + "\"valueDate\":\"1995-01-02\"" + "}" + "],"));
		assertThat(enc,
				containsString("\"_given\":[" + "{" + "\"extension\":[" + "{" + "\"url\":\"http://examples.com#givenext\"," + "\"valueString\":\"given\"" + "}" + "]" + "}," + "{" + "\"extension\":[" + "{"
						+ "\"url\":\"http://examples.com#givenext_parent\"," + "\"extension\":[" + "{" + "\"url\":\"http://examples.com#givenext_child\"," + "\"valueString\":\"CHILD\"" + "}" + "]" + "}"
						+ "]" + "}"));

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
	public void testEncodeAndParseMetaProfileAndTags() {
		Patient p = new Patient();
		p.addName().addFamily("FAMILY");

		p.getMeta().addProfile("http://foo/Profile1");
		p.getMeta().addProfile("http://foo/Profile2");

		p.getMeta().addTag().setSystem("scheme1").setCode("term1").setDisplay("label1");
		p.getMeta().addTag().setSystem("scheme2").setCode("term2").setDisplay("label2");

		p.getMeta().addSecurity().setSystem("sec_scheme1").setCode("sec_term1").setDisplay("sec_label1");
		p.getMeta().addSecurity().setSystem("sec_scheme2").setCode("sec_term2").setDisplay("sec_label2");

		String enc = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p);
		ourLog.info(enc);

		//@formatter:off
		assertThat(enc, stringContainsInOrder("\"meta\": {", 
				"\"profile\": [", 
				"\"http://foo/Profile1\",", 
				"\"http://foo/Profile2\"", 
				"],", 
				"\"security\": [", 
				"{", 
				"\"system\": \"sec_scheme1\",", 
				"\"code\": \"sec_term1\",", 
				"\"display\": \"sec_label1\"", 
				"},", 
				"{", 
				"\"system\": \"sec_scheme2\",", 
				"\"code\": \"sec_term2\",", 
				"\"display\": \"sec_label2\"", 
				"}", 
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

		List<UriType> gotLabels = parsed.getMeta().getProfile();
		assertEquals(2, gotLabels.size());
		UriType label = gotLabels.get(0);
		assertEquals("http://foo/Profile1", label.getValue());
		label = gotLabels.get(1);
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

	/**
	 * See #336
	 */
	@Test
	public void testEncodeAndParseNullPrimitiveWithExtensions() {

		Patient p = new Patient();
		p.setId("patid");
		HumanName name = p.addName();
		name.addGivenElement().setValue(null).setId("f0").addExtension(new Extension("http://foo", new StringType("FOOEXT0")));
		name.addGivenElement().setValue("V1").setId("f1").addExtension((Extension) new Extension("http://foo", new StringType("FOOEXT1")).setId("ext1id"));
		name.addGivenElement(); // this one shouldn't get encoded
		name.addGivenElement().setValue(null).addExtension(new Extension("http://foo", new StringType("FOOEXT3")));
		name.setId("nameid");

		String output = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p);
		ourLog.info(output);

		output = ourCtx.newJsonParser().setPrettyPrint(false).encodeResourceToString(p);
		String expected = "{\"resourceType\":\"Patient\",\"id\":\"patid\",\"name\":[{\"id\":\"nameid\",\"given\":[null,\"V1\",null],\"_given\":[{\"id\":\"f0\",\"extension\":[{\"url\":\"http://foo\",\"valueString\":\"FOOEXT0\"}]},{\"id\":\"f1\",\"extension\":[{\"id\":\"ext1id\",\"url\":\"http://foo\",\"valueString\":\"FOOEXT1\"}]},{\"extension\":[{\"url\":\"http://foo\",\"valueString\":\"FOOEXT3\"}]}]}]}";
		
		ourLog.info("Exp: {}", expected);
		ourLog.info("Act: {}", output);
		
		assertEquals(expected, output);

		p = ourCtx.newJsonParser().parseResource(Patient.class, output);
		assertEquals("patid", p.getIdElement().getIdPart());

		name = p.getName().get(0);
		assertEquals("nameid", name.getId());
		assertEquals(3, name.getGiven().size());

		assertEquals(null, name.getGiven().get(0).getValue());
		assertEquals("V1", name.getGiven().get(1).getValue());
		assertEquals(null, name.getGiven().get(2).getValue());

		assertEquals("f0", name.getGiven().get(0).getId());
		assertEquals("f1", name.getGiven().get(1).getId());
		assertEquals(null, name.getGiven().get(2).getId());

		assertEquals(1, name.getGiven().get(0).getExtension().size());
		assertEquals("http://foo", name.getGiven().get(0).getExtension().get(0).getUrl());
		assertEquals("FOOEXT0", ((StringType) name.getGiven().get(0).getExtension().get(0).getValue()).getValue());
		assertEquals(null, name.getGiven().get(0).getExtension().get(0).getId());

		assertEquals(1, name.getGiven().get(1).getExtension().size());
		assertEquals("http://foo", name.getGiven().get(1).getExtension().get(0).getUrl());
		assertEquals("FOOEXT1", ((StringType) name.getGiven().get(1).getExtension().get(0).getValue()).getValue());
		assertEquals("ext1id", name.getGiven().get(1).getExtension().get(0).getId());

		assertEquals(1, name.getGiven().get(2).getExtension().size());
		assertEquals("http://foo", name.getGiven().get(2).getExtension().get(0).getUrl());
		assertEquals("FOOEXT3", ((StringType) name.getGiven().get(2).getExtension().get(0).getValue()).getValue());
		assertEquals(null, name.getGiven().get(2).getExtension().get(0).getId());

	}



	@Test
	public void testEncodeBundleNewBundleNoText() {

		Bundle b = new Bundle();

		BundleEntryComponent e = b.addEntry();
		e.setResource(new Patient());

		String val = ourCtx.newJsonParser().setPrettyPrint(false).encodeResourceToString(b);
		ourLog.info(val);
		assertThat(val, not(containsString("text")));

		val = ourCtx.newXmlParser().setPrettyPrint(false).encodeResourceToString(b);
		ourLog.info(val);
		assertThat(val, not(containsString("text")));

	}

	/**
	 * See #326
	 */
	@Test
	public void testEncodeContainedResource() {
		Patient patient = new Patient();
		patient.getBirthDateElement().setValueAsString("2016-04-05");
		patient.addExtension().setUrl("test").setValue(new Reference(new Condition()));

		String encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(patient);
		ourLog.info(encoded);

		//@formatter:off
		assertThat(encoded, stringContainsInOrder(
			"{",
				"\"resourceType\": \"Patient\",",
				"\"contained\": [",
					"{",
					"\"resourceType\": \"Condition\",",
					"\"id\": \"1\"",
					"}",
				"],",
				"\"extension\": [",
					"{",
					"\"url\": \"test\",",
					"\"valueReference\": {",
					"\"reference\": \"#1\"",
					"}",
					"}",
				"],",
				"\"birthDate\": \"2016-04-05\"",
			"}"
		));
		//@formatter:on
	}

	@Test
	public void testEncodeDoesntIncludeUuidId() {
		Patient p = new Patient();
		p.setId(new IdType("urn:uuid:42795ed8-041f-4ebf-b6f4-78ef6f64c2f2"));
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
		ArrayList<Coding> tagList = new ArrayList<Coding>();
		tagList.add(new Coding());
		tagList.add(new Coding().setDisplay("Label"));

		Patient p = new Patient();
		p.getMeta().getTag().addAll(tagList);

		String encoded = ourCtx.newJsonParser().encodeResourceToString(p);
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

		String encoded = ourCtx.newJsonParser().encodeResourceToString(p);
		assertThat(encoded, containsString("tag"));
		assertThat(encoded, containsString("scheme"));
		assertThat(encoded, not(containsString("Label")));
	}

	@Test
	public void testEncodeExtendedInfrastructureComponent() {
		IParser parser = ourCtx.newJsonParser();

		PatientWithExtendedContactDstu3 patient = new PatientWithExtendedContactDstu3();
		patient.setId("123");

		CustomContactComponent customContactComponent = new CustomContactComponent();
		customContactComponent.getEyeColour().setValue("EYE");
		customContactComponent.getName().addFamily("FAMILY");
		patient.getCustomContact().add(customContactComponent);

		String val = parser.encodeResourceToString(patient);
		ourLog.info(val);

		assertEquals(
				"{\"resourceType\":\"Patient\",\"id\":\"123\",\"contact\":[{\"extension\":[{\"url\":\"http://foo.com/contact-eyecolour\",\"valueIdentifier\":{\"value\":\"EYE\"}}],\"name\":{\"family\":[\"FAMILY\"]}}]}",
				val);

		FhirContext newCtx = FhirContext.forDstu2_1();
		PatientWithExtendedContactDstu3 actual = newCtx.newJsonParser().parseResource(PatientWithExtendedContactDstu3.class, val);
		assertEquals("EYE", actual.getCustomContact().get(0).getEyeColour().getValue());

	}

	@Test
	public void testEncodeExtensionInPrimitiveElement() {

		Conformance c = new Conformance();
		c.getAcceptUnknownElement().addExtension().setUrl("http://foo").setValue(new StringType("AAA"));

		String encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(c);
		ourLog.info(encoded);

		encoded = ourCtx.newJsonParser().setPrettyPrint(false).encodeResourceToString(c);
		ourLog.info(encoded);
		assertEquals(encoded, "{\"resourceType\":\"Conformance\",\"_acceptUnknown\":{\"extension\":[{\"url\":\"http://foo\",\"valueString\":\"AAA\"}]}}");

		// Now with a value
		ourLog.info("---------------");

		c = new Conformance();
		c.getAcceptUnknownElement().setValue(UnknownContentCode.ELEMENTS);
		c.getAcceptUnknownElement().addExtension().setUrl("http://foo").setValue(new StringType("AAA"));

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
		Extension ext = obs.addExtension();
		ext.setUrl("http://exturl").setValue(new StringType("ext_url_value"));

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

		String encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(patient);
		ourLog.info(encoded);

		//@formatter:off
		assertThat(encoded, stringContainsInOrder(
			"\"resourceType\": \"Patient\"", 
			"\"contained\": [",
					"\"resourceType\": \"Condition\"", 
					"\"id\": \"1\"",
					"\"bodySite\": [",
					"\"text\": \"BODY SITE\"",
			"\"extension\": [", 
				"\"url\": \"testCondition\",",
				"\"valueReference\": {",
					"\"reference\": \"#1\"", 
			"\"birthDate\": \"2016-04-14\"", 
			"}"
		));
		//@formatter:on
	}

	@Test
	public void testEncodeHistoryEncodeVersionsAtPath1() {
		ourCtx = FhirContext.forDstu2_1();

		assertNull(ourCtx.newJsonParser().getStripVersionsFromReferences());

		Patient p = new Patient();
		p.setManagingOrganization(new Reference("http://foo.com/Organization/2/_history/1"));

		IParser parser = ourCtx.newJsonParser();

		parser.setDontStripVersionsFromReferencesAtPaths("Patient.managingOrganization");
		String enc = parser.setPrettyPrint(true).encodeResourceToString(p);
		ourLog.info(enc);
		assertThat(enc, containsString("\"reference\": \"http://foo.com/Organization/2/_history/1\""));
	}

	@Test
	public void testEncodeHistoryEncodeVersionsAtPath2() {
		ourCtx = FhirContext.forDstu2_1();

		assertNull(ourCtx.newJsonParser().getStripVersionsFromReferences());
		assertTrue(ourCtx.getParserOptions().isStripVersionsFromReferences());

		Patient p = new Patient();
		p.setManagingOrganization(new Reference("http://foo.com/Organization/2/_history/1"));

		IParser parser = ourCtx.newJsonParser();

		parser.setDontStripVersionsFromReferencesAtPaths("AuditEvent.entity.reference");
		String enc = parser.setPrettyPrint(true).encodeResourceToString(p);
		ourLog.info(enc);
		assertThat(enc, containsString("\"reference\": \"http://foo.com/Organization/2\""));
	}

	@Test
	public void testEncodeHistoryEncodeVersionsAtPath3() {
		ourCtx = FhirContext.forDstu2_1();

		assertNull(ourCtx.newJsonParser().getStripVersionsFromReferences());

		AuditEvent auditEvent = new AuditEvent();
		auditEvent.addEntity().setReference(new Reference("http://foo.com/Organization/2/_history/1"));

		IParser parser = ourCtx.newJsonParser();

		parser.setDontStripVersionsFromReferencesAtPaths("AuditEvent.entity.reference");
		String enc = parser.setPrettyPrint(true).encodeResourceToString(auditEvent);
		ourLog.info(enc);
		assertThat(enc, containsString("\"reference\": \"http://foo.com/Organization/2/_history/1\""));

		parser.setDontStripVersionsFromReferencesAtPaths(new ArrayList<String>());
		enc = parser.setPrettyPrint(true).encodeResourceToString(auditEvent);
		ourLog.info(enc);
		assertThat(enc, containsString("\"reference\": \"http://foo.com/Organization/2\""));

		parser.setDontStripVersionsFromReferencesAtPaths((String[]) null);
		enc = parser.setPrettyPrint(true).encodeResourceToString(auditEvent);
		ourLog.info(enc);
		assertThat(enc, containsString("\"reference\": \"http://foo.com/Organization/2\""));

		parser.setDontStripVersionsFromReferencesAtPaths((List<String>) null);
		enc = parser.setPrettyPrint(true).encodeResourceToString(auditEvent);
		ourLog.info(enc);
		assertThat(enc, containsString("\"reference\": \"http://foo.com/Organization/2\""));
	}

	@Test
	public void testEncodeHistoryEncodeVersionsAtPathUsingOptions() {
		ourCtx = FhirContext.forDstu2_1();

		assertNull(ourCtx.newJsonParser().getStripVersionsFromReferences());
		assertTrue(ourCtx.getParserOptions().isStripVersionsFromReferences());
		assertThat(ourCtx.getParserOptions().getDontStripVersionsFromReferencesAtPaths(), empty());

		Patient p = new Patient();
		p.setManagingOrganization(new Reference("http://foo.com/Organization/2/_history/1"));

		IParser parser = ourCtx.newJsonParser();

		ourCtx.getParserOptions().setDontStripVersionsFromReferencesAtPaths("Patient.managingOrganization");
		String enc = parser.setPrettyPrint(true).encodeResourceToString(p);
		ourLog.info(enc);
		assertThat(enc, containsString("\"reference\": \"http://foo.com/Organization/2/_history/1\""));

		ourCtx.getParserOptions().setDontStripVersionsFromReferencesAtPaths(Arrays.asList("Patient.managingOrganization"));
		enc = parser.setPrettyPrint(true).encodeResourceToString(p);
		ourLog.info(enc);
		assertThat(enc, containsString("\"reference\": \"http://foo.com/Organization/2/_history/1\""));

		ourCtx.getParserOptions().setDontStripVersionsFromReferencesAtPaths(new HashSet<String>(Arrays.asList("Patient.managingOrganization")));
		enc = parser.setPrettyPrint(true).encodeResourceToString(p);
		ourLog.info(enc);
		assertThat(enc, containsString("\"reference\": \"http://foo.com/Organization/2/_history/1\""));
	}

	@Test
	public void testEncodeHistoryStripVersionsFromReferences() {
		ourCtx = FhirContext.forDstu2_1();

		assertNull(ourCtx.newJsonParser().getStripVersionsFromReferences());

		Patient p = new Patient();
		p.setManagingOrganization(new Reference("http://foo.com/Organization/2/_history/1"));

		IParser parser = ourCtx.newJsonParser();
		String enc = parser.setPrettyPrint(true).encodeResourceToString(p);
		ourLog.info(enc);
		assertThat(enc, containsString("\"reference\": \"http://foo.com/Organization/2\""));

		parser.setStripVersionsFromReferences(false);
		enc = parser.setPrettyPrint(true).encodeResourceToString(p);
		ourLog.info(enc);
		assertThat(enc, containsString("\"reference\": \"http://foo.com/Organization/2/_history/1\""));

		ourCtx = FhirContext.forDstu2_1();
	}

	@Test
	public void testEncodeHistoryStripVersionsFromReferencesFromContext() {
		ourCtx = FhirContext.forDstu2_1();

		assertTrue(ourCtx.getParserOptions().isStripVersionsFromReferences());

		Patient p = new Patient();
		p.setManagingOrganization(new Reference("http://foo.com/Organization/2/_history/1"));

		IParser parser = ourCtx.newJsonParser();
		String enc = parser.setPrettyPrint(true).encodeResourceToString(p);
		ourLog.info(enc);
		assertThat(enc, containsString("\"reference\": \"http://foo.com/Organization/2\""));

		ourCtx.getParserOptions().setStripVersionsFromReferences(false);
		enc = parser.setPrettyPrint(true).encodeResourceToString(p);
		ourLog.info(enc);
		assertThat(enc, containsString("\"reference\": \"http://foo.com/Organization/2/_history/1\""));

		parser.setStripVersionsFromReferences(true);
		enc = parser.setPrettyPrint(true).encodeResourceToString(p);
		ourLog.info(enc);
		assertThat(enc, containsString("\"reference\": \"http://foo.com/Organization/2\""));

		ourCtx = FhirContext.forDstu2_1();
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
		assertThat(output, containsString("\"div\": \"<div xmlns=\\\"http://www.w3.org/1999/xhtml\\\">VALUE</div>\""));
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
		assertThat(encoded, stringContainsInOrder(ca.uhn.fhir.rest.api.Constants.TAG_SUBSETTED_SYSTEM_DSTU3, ca.uhn.fhir.rest.api.Constants.TAG_SUBSETTED_CODE));
		assertThat(encoded, not(containsString("text")));
		assertThat(encoded, not(containsString("THE DIV")));
		assertThat(encoded, containsString("family"));
		assertThat(encoded, containsString("maritalStatus"));
	}

	@Test
	public void testEncodeParametersWithId() {
		Parameters reqParms = new Parameters();
		IdType patient = new IdType(1);
		reqParms.addParameter().setName("patient").setValue(patient);

		String enc = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(reqParms);
		ourLog.info(enc);

		assertThat(enc, containsString("\"valueId\": \"1\""));
	}

	@Test
	public void testEncodeSummary() {
		Patient patient = new Patient();
		patient.setId("Patient/1/_history/1");
		patient.getText().setDivAsString("<div>THE DIV</div>");
		patient.addName().addFamily("FAMILY");
		patient.addPhoto().setTitle("green");
		patient.getMaritalStatus().addCoding().setCode("D");

		ourLog.info(ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(patient));

		String encoded = ourCtx.newJsonParser().setPrettyPrint(true).setSummaryMode(true).encodeResourceToString(patient);
		ourLog.info(encoded);

		assertThat(encoded, containsString("Patient"));
		assertThat(encoded, stringContainsInOrder("\"tag\"", "\"system\": \"" + ca.uhn.fhir.rest.api.Constants.TAG_SUBSETTED_SYSTEM_DSTU3 + "\",", "\"code\": \"" + ca.uhn.fhir.rest.api.Constants.TAG_SUBSETTED_CODE + "\""));
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
		patient.getMaritalStatus().addCoding().setCode("D");

		patient.getMeta().addTag().setSystem("foo").setCode("bar");

		String encoded = ourCtx.newJsonParser().setPrettyPrint(true).setSummaryMode(true).encodeResourceToString(patient);
		ourLog.info(encoded);

		assertThat(encoded, containsString("Patient"));
		assertThat(encoded, stringContainsInOrder("\"tag\"", "\"system\": \"foo\",", "\"code\": \"bar\"", "\"system\": \"" + ca.uhn.fhir.rest.api.Constants.TAG_SUBSETTED_SYSTEM_DSTU3 + "\"",
				"\"code\": \"" + ca.uhn.fhir.rest.api.Constants.TAG_SUBSETTED_CODE + "\""));
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

		pt.getMeta().addTag().setSystem("scheme").setCode("term").setDisplay("display");

		String enc = ourCtx.newJsonParser().encodeResourceToString(pt);
		ourLog.info(enc);

		assertEquals("{\"resourceType\":\"Patient\",\"meta\":{\"tag\":[{\"system\":\"scheme\",\"code\":\"term\",\"display\":\"display\"}]},\"identifier\":[{\"system\":\"sys\",\"value\":\"val\"}]}",
				enc);

	}

	/**
	 * See #241
	 */
	@Test
	public void testEncodeThenParseShouldNotAddSpuriousId() throws Exception {
		Condition condition = new Condition().setVerificationStatus(ConditionVerificationStatus.CONFIRMED);
		Bundle bundle = new Bundle();
		BundleEntryComponent entry = new Bundle.BundleEntryComponent();
		entry.setId("123");
		entry.setResource(condition);
		bundle.getEntry().add(entry);
		IParser parser = ourCtx.newJsonParser();
		String json = parser.encodeResourceToString(bundle);
		ourLog.info(json);
		bundle = (Bundle) parser.parseResource(json);

		assertEquals("123", bundle.getEntry().get(0).getId());

		condition = (Condition) bundle.getEntry().get(0).getResource();
		assertEquals(null, condition.getId());
	}

	@Test
	public void testEncodeUndeclaredBlock() throws Exception {
		FooMessageHeader.FooMessageSourceComponent source = new FooMessageHeader.FooMessageSourceComponent();
		source.getMessageHeaderApplicationId().setValue("APPID");
		source.setName("NAME");

		FooMessageHeader header = new FooMessageHeader();
		header.setSource(source);

		Bundle bundle = new Bundle();
		bundle.addEntry().setResource(header);

		IParser p = ourCtx.newJsonParser();
		p.setPrettyPrint(true);

		String encode = p.encodeResourceToString(bundle);
		ourLog.info(encode);

		assertThat(encode, containsString("\"value\": \"APPID\""));
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

		MyPatientWithOneDeclaredEnumerationExtensionDstu3 actual = parser.parseResource(MyPatientWithOneDeclaredEnumerationExtensionDstu3.class, val);
		assertEquals(AddressUse.HOME, patient.getAddress().get(0).getUse());
		Enumeration<AddressUse> ref = actual.getFoo();
		assertEquals("home", ref.getValue().toCode());

	}

	@Test
	public void testEncodeWithDontEncodeElements() throws Exception {
		Patient patient = new Patient();
		patient.setId("123");

		patient.getMeta().addProfile(("http://profile"));
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
			assertThat(out, not(containsString("SUBSETTED")));
		}
	}

	@Test
	public void testEncodeWithNarrative() {
		Patient p = new Patient();
		p.addName().addFamily("Smith").addGiven("John");

		ourCtx.setNarrativeGenerator(new DefaultThymeleafNarrativeGenerator());

		String output = ourCtx.newJsonParser().encodeResourceToString(p);
		ourLog.info(output);

		assertThat(output, containsString("\"text\":{\"status\":\"generated\",\"div\":\"<div xmlns=\\\"http://www.w3.org/1999/xhtml\\\"><div class=\\\"hapiHeaderText\\\">John <b>SMITH </b></div>"));
	}

	@Test
	public void testEncodingNullExtension() {
		Patient p = new Patient();
		Extension extension = new Extension("http://foo#bar");
		p.addExtension(extension);
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
	public void testExponentDoesntGetEncodedAsSuch() {
		Observation obs = new Observation();
		obs.setValue(new Quantity().setValue(new BigDecimal("0.000000000000000100")));

		String str = ourCtx.newJsonParser().encodeResourceToString(obs);
		ourLog.info(str);

		assertEquals("{\"resourceType\":\"Observation\",\"valueQuantity\":{\"value\":0.000000000000000100}}", str);
	}

	@Test
	public void testExponentParseWorks() {
		String input = "{\"resourceType\":\"Observation\",\"valueQuantity\":{\"value\":0.0000000000000001}}";
		Observation obs = ourCtx.newJsonParser().parseResource(Observation.class, input);

		assertEquals("0.0000000000000001", ((Quantity) obs.getValue()).getValueElement().getValueAsString());

		String str = ourCtx.newJsonParser().encodeResourceToString(obs);
		ourLog.info(str);
		assertEquals("{\"resourceType\":\"Observation\",\"valueQuantity\":{\"value\":0.0000000000000001}}", str);
	}

	/**
	 * #516
	 */
	@Test(expected = DataFormatException.class)
	public void testInvalidEnumValue() {
		String res = "{ \"resourceType\": \"ValueSet\", \"url\": \"http://sample/ValueSet/education-levels\", \"version\": \"1\", \"name\": \"Education Levels\", \"status\": \"draft\", \"compose\": { \"include\": [ { \"filter\": [ { \"property\": \"n\", \"op\": \"n\", \"value\": \"365460000\" } ], \"system\": \"http://snomed.info/sct\" } ], \"exclude\": [ { \"concept\": [ { \"code\": \"224298008\" }, { \"code\": \"365460000\" }, { \"code\": \"473462005\" }, { \"code\": \"424587006\" } ], \"system\": \"http://snomed.info/sct\" } ] }, \"description\": \"A selection of Education Levels\", \"text\": { \"status\": \"generated\", \"div\": \"<div xmlns=\\\"http://www.w3.org/1999/xhtml\\\"><h2>Education Levels</h2><tt>http://csiro.au/ValueSet/education-levels</tt><p>A selection of Education Levels</p></div>\" }, \"experimental\": true, \"date\": \"2016-07-26\" }";
		IParser parser = ourCtx.newJsonParser();
		parser.setParserErrorHandler(new StrictErrorHandler());
		ValueSet parsed = parser.parseResource(ValueSet.class, res);
		fail("DataFormat Invalid attribute exception should be thrown");
	}

	@Test
	public void testInvalidEnumValueBlank() {
		IParserErrorHandler errorHandler = mock(IParserErrorHandler.class);
		
		String res = "{ \"resourceType\": \"Patient\", \"gender\": \"\" }";
		IParser parser = ourCtx.newJsonParser();
		parser.setParserErrorHandler(errorHandler);
		Patient parsed = parser.parseResource(Patient.class, res);
		
		assertEquals(null, parsed.getGenderElement().getValue());
		assertEquals(null, parsed.getGenderElement().getValueAsString());
		
		ArgumentCaptor<String> msgCaptor = ArgumentCaptor.forClass(String.class);
		verify(errorHandler, times(1)).invalidValue(isNull(IParseLocation.class), eq(""), msgCaptor.capture());
		assertEquals("Attribute values must not be empty (\"\")", msgCaptor.getValue());
		
		String encoded = ourCtx.newJsonParser().encodeResourceToString(parsed);
		assertEquals("{\"resourceType\":\"Patient\"}", encoded);
	}

	@Test
	public void testInvalidEnumValueInvalid() {
		IParserErrorHandler errorHandler = mock(IParserErrorHandler.class);
		
		String res = "{ \"resourceType\": \"Patient\", \"gender\": \"foo\" }";
		IParser parser = ourCtx.newJsonParser();
		parser.setParserErrorHandler(errorHandler);
		Patient parsed = parser.parseResource(Patient.class, res);
		
		assertEquals(null, parsed.getGenderElement().getValue());
		assertEquals("foo", parsed.getGenderElement().getValueAsString());
		
		ArgumentCaptor<String> msgCaptor = ArgumentCaptor.forClass(String.class);
		verify(errorHandler, times(1)).invalidValue(isNull(IParseLocation.class), eq("foo"), msgCaptor.capture());
		assertEquals("Unknown AdministrativeGender code 'foo'", msgCaptor.getValue());
		
		String encoded = ourCtx.newJsonParser().encodeResourceToString(parsed);
		assertEquals("{\"resourceType\":\"Patient\",\"gender\":\"foo\"}", encoded);		
	}

	@Test
	public void testInvalidDateTimeValueInvalid() throws Exception {
		IParserErrorHandler errorHandler = mock(IParserErrorHandler.class);
		
		String res = "{ \"resourceType\": \"Observation\", \"valueDateTime\": \"foo\" }";
		IParser parser = ourCtx.newJsonParser();
		parser.setParserErrorHandler(errorHandler);
		Observation parsed = parser.parseResource(Observation.class, res);
		
		assertEquals(null, parsed.getValueDateTimeType().getValue());
		assertEquals("foo", parsed.getValueDateTimeType().getValueAsString());
		
		ArgumentCaptor<String> msgCaptor = ArgumentCaptor.forClass(String.class);
		verify(errorHandler, times(1)).invalidValue(isNull(IParseLocation.class), eq("foo"), msgCaptor.capture());
		assertEquals("Invalid date/time format: \"foo\"", msgCaptor.getValue());
		
		String encoded = ourCtx.newJsonParser().encodeResourceToString(parsed);
		assertEquals("{\"resourceType\":\"Observation\",\"valueDateTime\":\"foo\"}", encoded);		
	}

	/**
	 * #65
	 */
	@Test
	public void testJsonPrimitiveWithExtensionEncoding() {

		QuestionnaireResponse parsed = new QuestionnaireResponse();
		parsed.addItem().setLinkId("value123");
		parsed.getItem().get(0).getLinkIdElement().addExtension(new Extension("http://123", new StringType("HELLO")));

		String encoded = ourCtx.newJsonParser().setPrettyPrint(false).encodeResourceToString(parsed);
		ourLog.info(encoded);
		assertThat(encoded, containsString("{\"linkId\":\"value123\",\"_linkId\":{\"extension\":[{\"url\":\"http://123\",\"valueString\":\"HELLO\"}]}}"));

	}

	@Test
	public void testLinkage() {
		Linkage l = new Linkage();
		l.addItem().getResource().setDisplay("FOO");
		String out = ourCtx.newXmlParser().encodeResourceToString(l);
		ourLog.info(out);
		assertEquals("<Linkage xmlns=\"http://hl7.org/fhir\"><item><resource><display value=\"FOO\"/></resource></item></Linkage>", out);
	}

	// FIXME: this should pass
	@Test
	@Ignore
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

	// TODO: this should pass
	@Test
	@Ignore
	public void testNamespacePreservationParse() throws Exception {
		String input = "{\"resourceType\":\"Patient\",\"text\":{\"div\":\"<xhtml:div xmlns:xhtml=\\\"http://www.w3.org/1999/xhtml\\\"><xhtml:img src=\\\"foo\\\"/>@fhirabend</xhtml:div>\"}}";
		Patient parsed = ourCtx.newJsonParser().parseResource(Patient.class, input);
		XhtmlNode div = parsed.getText().getDiv();

		assertEquals("<xhtml:div xmlns:xhtml=\"http://www.w3.org/1999/xhtml\"><xhtml:img src=\"foo\"/>@fhirabend</xhtml:div>", div.getValueAsString());

		String encoded = ourCtx.newXmlParser().encodeResourceToString(parsed);
		assertEquals("<Patient xmlns=\"http://hl7.org/fhir\"><text><xhtml:div xmlns:xhtml=\"http://www.w3.org/1999/xhtml\"><xhtml:img src=\"foo\"/>@fhirabend</xhtml:div></text></Patient>", encoded);
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
	@Ignore
	public void testParseAndEncodeBundle() throws Exception {
		String content = IOUtils.toString(JsonParserDstu2_1Test.class.getResourceAsStream("/bundle-example.json"), StandardCharsets.UTF_8);

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
		assertSame(((Reference) p.getMedication()).getResource(), m);

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

	/**
	 * Test for #146
	 */
	@Test
	@Ignore
	public void testParseAndEncodeBundleFromXmlToJson() throws Exception {
		String content = IOUtils.toString(JsonParserDstu2_1Test.class.getResourceAsStream("/bundle-example2.xml"), StandardCharsets.UTF_8);

		Bundle parsed = ourCtx.newXmlParser().parseResource(Bundle.class, content);

		MedicationOrder p = (MedicationOrder) parsed.getEntry().get(0).getResource();
		assertEquals("#med", ((Reference) p.getMedication()).getReference());

		Medication m = (Medication) ((Reference) p.getMedication()).getResource();
		assertNotNull(m);
		assertEquals("#med", m.getIdElement().getValue());
		assertEquals(1, p.getContained().size());
		assertSame(m, p.getContained().get(0));

		String reencoded = ourCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(parsed);
		ourLog.info(reencoded);
		assertThat(reencoded, containsString("contained"));

		reencoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(parsed);
		ourLog.info(reencoded);
		assertThat(reencoded, containsString("contained"));
	}

	@Test
	@Ignore
	public void testParseAndEncodeBundleNewStyle() throws Exception {
		String content = IOUtils.toString(JsonParserDstu2_1Test.class.getResourceAsStream("/bundle-example.json"), StandardCharsets.UTF_8);

		Bundle parsed = ourCtx.newJsonParser().parseResource(Bundle.class, content);
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
		// assertEquals("3123", p.getId());

		Medication m = (Medication) parsed.getEntry().get(1).getResource();
		assertEquals("http://example.com/base/Medication/example", m.getId());
		assertSame(((Reference) p.getMedication()).getResource(), m);

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

		Bundle parsed = ourCtx.newJsonParser().parseResource(Bundle.class, input);

		String encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(parsed);
		ourLog.info(encoded);

		assertEquals("urn:uuid:180f219f-97a8-486d-99d9-ed631fe4fc57", parsed.getEntry().get(0).getResource().getIdElement().getValue());
		assertEquals(null, parsed.getEntry().get(0).getResource().getIdElement().getBaseUrl());
		assertEquals("urn:uuid:180f219f-97a8-486d-99d9-ed631fe4fc57", parsed.getEntry().get(0).getResource().getIdElement().getIdPart());
		assertThat(encoded, not(containsString("\"id\":\"180f219f-97a8-486d-99d9-ed631fe4fc57\"")));
	}

	@Test
	public void testParseAndEncodeComments() {
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
		assertEquals("Patient/pat1", res.getId());
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
		patient.setId(new IdType("http://base/Binary/11/_history/22"));
		patient.setContentType("foo");
		patient.setContent(new byte[] { 1, 2, 3, 4 });

		String val = ourCtx.newJsonParser().encodeResourceToString(patient);

		String expected = "{\"resourceType\":\"Binary\",\"id\":\"11\",\"meta\":{\"versionId\":\"22\"},\"contentType\":\"foo\",\"content\":\"AQIDBA==\"}";
		ourLog.info("Expected: {}", expected);
		ourLog.info("Actual  : {}", val);
		assertEquals(expected, val);
	}



	/**
	 * see #144 and #146
	 */
	@Test
	@Ignore
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
			"\"resourceType\":\"Observation\"",
			"\"contained\":[",
			"\"resourceType\":\"Patient\",",
			"\"id\":\"1\"",
			"\"reference\":\"#1\""
			));
		//@formatter:on

		o = parser.parseResource(Observation.class, enc);
		assertEquals("obs text", o.getCode().getText());

		assertNotNull(o.getSubject().getResource());
		p = (Patient) o.getSubject().getResource();
		assertEquals("patient family", p.getName().get(0).getFamilyAsSingleString());
	}


	/**
	 * See #342
	 */
	@Test()
	public void testParseInvalid() {
		try {
			ourCtx.newJsonParser().parseResource("FOO");
			fail();
		} catch (DataFormatException e) {
			assertEquals("Failed to parse JSON content, error was: Content does not appear to be FHIR JSON, first non-whitespace character was: 'F' (must be '{')", e.getMessage());
		}
		try {
			ourCtx.newJsonParser().parseResource("[\"aaa\"]");
			fail();
		} catch (DataFormatException e) {
			assertEquals("Failed to parse JSON content, error was: Content does not appear to be FHIR JSON, first non-whitespace character was: '[' (must be '{')", e.getMessage());
		}

		assertEquals(Bundle.class, ourCtx.newJsonParser().parseResource("  {\"resourceType\" : \"Bundle\"}").getClass());

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
		assertEquals(1, parsed.getExtension().size());
		assertEquals(null, parsed.getExtension().get(0).getUrl());
		assertEquals("2011-01-02T11:13:15", ((PrimitiveType<?>)parsed.getExtension().get(0).getValue()).getValueAsString());

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
		assertEquals(1, parsed.getModifierExtension().size());
		assertEquals(null, parsed.getModifierExtension().get(0).getUrl());
		assertEquals("2011-01-02T11:13:15", ((PrimitiveType<?>)parsed.getModifierExtension().get(0).getValue()).getValueAsString());

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

		Bundle b = ourCtx.newJsonParser().parseResource(Bundle.class, bundle);
		assertEquals(1, b.getEntry().size());

		BundleEntryComponent entry = b.getEntry().get(0);
		Patient pt = (Patient) entry.getResource();
		assertEquals("http://foo/fhirBase2/Patient/1/_history/2", pt.getIdElement().getValue());
		assertEquals("2012-01-02", pt.getBirthDateElement().getValueAsString());
		assertEquals("0.123", entry.getSearch().getScore().toString());
		assertEquals("match", entry.getSearch().getMode().toCode());
		assertEquals("POST", entry.getRequest().getMethod().toCode());
		assertEquals("http://foo/Patient?identifier=value", entry.getRequest().getUrl());
		assertEquals("2001-02-22T11:22:33-05:00", pt.getMeta().getLastUpdatedElement().getValueAsString());

		String reEncoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(b);

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
		assertEquals(null, basic.getText().getDivAsString());

		input = "{\"resourceType\":\"Basic\",\"id\":\"1\",\"text\":{\"status\":\"generated\",\"div\":\"<div></div>\"}}";
		basic = ourCtx.newJsonParser().parseResource(Basic.class, input);
		assertEquals(null, basic.getText().getDivAsString());

		input = "{\"resourceType\":\"Basic\",\"id\":\"1\",\"text\":{\"status\":\"generated\",\"div\":\"<div> </div>\"}}";
		basic = ourCtx.newJsonParser().parseResource(Basic.class, input);
		assertEquals("<div xmlns=\"http://www.w3.org/1999/xhtml\"> </div>", basic.getText().getDivAsString());

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
		patient.setId(new IdType("Patient", patientId));
		patient.addName().addGiven("John").addFamily("Smith");
		patient.setGender(AdministrativeGender.MALE);
		patient.setBirthDateElement(new DateType("1987-04-16"));

		// Bundle
		Bundle bundle = new Bundle();
		bundle.setType(BundleType.COLLECTION);
		bundle.addEntry().setResource(patient);

		String bundleText = jsonParser.encodeResourceToString(bundle);
		ourLog.info(bundleText);

		Bundle reincarnatedBundle = jsonParser.parseResource(Bundle.class, bundleText);
		Patient reincarnatedPatient = (Patient) reincarnatedBundle.getEntry().get(0).getResource();

		assertEquals("Patient", patient.getIdElement().getResourceType());
		assertEquals("Patient", reincarnatedPatient.getIdElement().getResourceType());
	}

	/**
	 * See #207
	 */
	@Test
	public void testParseResourceWithInvalidType() {
		String input = "{" + "\"resourceType\":\"Patient\"," + "\"contained\":[" + "    {" + "       \"rezType\":\"Organization\"" + "    }" + "  ]" + "}";

		IParser jsonParser = ourCtx.newJsonParser().setPrettyPrint(true);
		try {
			jsonParser.parseResource(input);
			fail();
		} catch (DataFormatException e) {
			assertEquals("Missing required element 'resourceType' from JSON resource object, unable to parse", e.getMessage());
		}
	}

	/**
	 * See #344
	 */
	@Test
	public void testParserIsCaseSensitive() {
		Observation obs = new Observation();
		SampledData data = new SampledData();
		data.setData("1 2 3");
		data.setOrigin((SimpleQuantity) new SimpleQuantity().setValue(0L));
		data.setPeriod(1000L);
		obs.setValue(data);

		IParser p = ourCtx.newJsonParser().setPrettyPrint(true).setParserErrorHandler(new StrictErrorHandler());
		String encoded = p.encodeResourceToString(obs);
		ourLog.info(encoded);

		p.parseResource(encoded);

		try {
			p.parseResource(encoded.replace("Observation", "observation"));
			fail();
		} catch (DataFormatException e) {
			assertEquals("Unknown resource type 'observation': Resource names are case sensitive, found similar name: 'Observation'", e.getMessage());
		}

		try {
			p.parseResource(encoded.replace("valueSampledData", "valueSampleddata"));
			fail();
		} catch (DataFormatException e) {
			assertEquals("Unknown element 'valueSampleddata' found during parse", e.getMessage());
		}
	}

	@Test
	public void testParseWithPrecision() {
		String input = "{\"resourceType\":\"Observation\",\"valueQuantity\":{\"value\":0.000000000000000100}}";
		Observation obs = ourCtx.newJsonParser().parseResource(Observation.class, input);

		DecimalType valueElement = ((Quantity) obs.getValue()).getValueElement();
		assertEquals("0.000000000000000100", valueElement.getValueAsString());

		String str = ourCtx.newJsonParser().encodeResourceToString(obs);
		ourLog.info(str);
		assertEquals("{\"resourceType\":\"Observation\",\"valueQuantity\":{\"value\":0.000000000000000100}}", str);
	}

	@Test(expected = DataFormatException.class)
	public void testParseWithTrailingContent() throws Exception {
		//@formatter:off
		String bundle = "{\n" + 
			"  \"resourceType\" : \"Bundle\",\n" + 
			"  \"total\" : 1\n" + 
			"}}";
		//@formatter:on

		Bundle b = ourCtx.newJsonParser().parseResource(Bundle.class, bundle);
	}

	@Test
	@Ignore
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
	 * See #144 and #146
	 */
	@Test
	public void testReportSerialize() {

		ReportObservationDstu2_1 obsv = new ReportObservationDstu2_1();
		obsv.getCode().addCoding().setCode("name");
		obsv.setValue(new StringType("value test"));
		obsv.setStatus(ObservationStatus.FINAL);
		obsv.addIdentifier().setSystem("System").setValue("id value");

		DiagnosticReport report = new DiagnosticReport();
		report.getContained().add(obsv);
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

		ReportObservationDstu2_1 obsv = new ReportObservationDstu2_1();
		obsv.getCode().addCoding().setCode("name");
		obsv.setValue(new StringType("value test"));
		obsv.setStatus(ObservationStatus.FINAL);
		obsv.addIdentifier().setSystem("System").setValue("id value");

		DiagnosticReport report = new DiagnosticReport();
		report.getContained().add(obsv);

		obsv.setId("#123");
		report.addResult().setReference("#123");

		IParser parser = ourCtx.newXmlParser().setPrettyPrint(true);
		String message = parser.encodeResourceToString(report);
		ourLog.info(message);
		Assert.assertThat(message, containsString("contained"));
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

		List<Extension> extlst = fhirPat.getExtensionsByUrl("x1");
		Assert.assertEquals(1, extlst.size());
		Assert.assertEquals(refVal, ((Reference) extlst.get(0).getValue()).getReference());
	}



	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}
}
