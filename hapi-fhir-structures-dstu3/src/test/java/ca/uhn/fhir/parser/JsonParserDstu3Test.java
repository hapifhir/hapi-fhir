package ca.uhn.fhir.parser;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.stringContainsInOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.hamcrest.Matchers;
import org.hl7.fhir.dstu3.model.Binary;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.dstu3.model.Bundle.BundleType;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.Condition;
import org.hl7.fhir.dstu3.model.Condition.ConditionVerificationStatus;
import org.hl7.fhir.dstu3.model.Conformance;
import org.hl7.fhir.dstu3.model.DateTimeType;
import org.hl7.fhir.dstu3.model.DateType;
import org.hl7.fhir.dstu3.model.DecimalType;
import org.hl7.fhir.dstu3.model.DiagnosticReport;
import org.hl7.fhir.dstu3.model.Enumerations.AdministrativeGender;
import org.hl7.fhir.dstu3.model.Extension;
import org.hl7.fhir.dstu3.model.HumanName;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Identifier.IdentifierUse;
import org.hl7.fhir.dstu3.model.Linkage;
import org.hl7.fhir.dstu3.model.Medication;
import org.hl7.fhir.dstu3.model.MedicationOrder;
import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.dstu3.model.Observation.ObservationStatus;
import org.hl7.fhir.dstu3.model.Parameters;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.dstu3.model.Quantity;
import org.hl7.fhir.dstu3.model.QuestionnaireResponse;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.dstu3.model.UriType;
import org.hl7.fhir.utilities.xhtml.XhtmlNode;
import org.junit.After;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.narrative.DefaultThymeleafNarrativeGenerator;
import ca.uhn.fhir.rest.server.Constants;
import net.sf.json.JSON;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;

public class JsonParserDstu3Test {
	private static final FhirContext ourCtx = FhirContext.forDstu3();
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(JsonParserDstu3Test.class);

	@After
	public void after() {
		ourCtx.setNarrativeGenerator(null);
	}

	@Test
	public void testLinkage() {
		Linkage l = new Linkage();
		l.addItem().getResource().setDisplay("FOO");
		String out = ourCtx.newXmlParser().encodeResourceToString(l);
		ourLog.info(out);
		assertEquals("<Linkage xmlns=\"http://hl7.org/fhir\"><item><resource><display value=\"FOO\"/></resource></item></Linkage>", out);
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
		assertThat(enc, containsString("\"_given\":[" + "{" + "\"extension\":[" + "{" + "\"url\":\"http://examples.com#givenext\"," + "\"valueString\":\"given\"" + "}" + "]" + "}," + "{" + "\"extension\":[" + "{" + "\"url\":\"http://examples.com#givenext_parent\"," + "\"extension\":[" + "{"
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
		assertThat(enc, stringContainsInOrder("\"meta\":{", 
				"\"profile\":[", 
				"\"http://foo/Profile1\",", 
				"\"http://foo/Profile2\"", 
				"],", 
				"\"security\":[", 
				"{", 
				"\"system\":\"sec_scheme1\",", 
				"\"code\":\"sec_term1\",", 
				"\"display\":\"sec_label1\"", 
				"},", 
				"{", 
				"\"system\":\"sec_scheme2\",", 
				"\"code\":\"sec_term2\",", 
				"\"display\":\"sec_label2\"", 
				"}", 
				"],", 
				"\"tag\":[", 
				"{", 
				"\"system\":\"scheme1\",", 
				"\"code\":\"term1\",", 
				"\"display\":\"label1\"", 
				"},", 
				"{", 
				"\"system\":\"scheme2\",", 
				"\"code\":\"term2\",", 
				"\"display\":\"label2\"", 
				"}", 
				"]", 
				"},"));
		//@formatter:on

		Patient parsed = ourCtx.newJsonParser().parseResource(Patient.class, enc);

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
	public void testEncodeAndParseSecurityLabels() {
		Patient p = new Patient();
		p.addName().addFamily("FAMILY");

		List<Coding> labels = new ArrayList<Coding>();
		labels.add(new Coding().setSystem("SYSTEM1").setCode("CODE1").setDisplay("DISPLAY1").setVersion("VERSION1"));
		labels.add(new Coding().setSystem("SYSTEM2").setCode("CODE2").setDisplay("DISPLAY2").setVersion("VERSION2"));
		p.getMeta().getSecurity().addAll(labels);

		String enc = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(p);
		ourLog.info(enc);

		//@formatter:off
		assertEquals("{\n" + 
			"    \"resourceType\":\"Patient\",\n" + 
			"    \"meta\":{\n" + 
			"        \"security\":[\n" + 
			"            {\n" + 
			"                \"system\":\"SYSTEM1\",\n" + 
			"                \"version\":\"VERSION1\",\n" + 
			"                \"code\":\"CODE1\",\n" + 
			"                \"display\":\"DISPLAY1\"\n" + 
			"            },\n" + 
			"            {\n" + 
			"                \"system\":\"SYSTEM2\",\n" + 
			"                \"version\":\"VERSION2\",\n" + 
			"                \"code\":\"CODE2\",\n" + 
			"                \"display\":\"DISPLAY2\"\n" + 
			"            }\n" + 
			"        ]\n" + 
			"    },\n" + 
			"    \"name\":[\n" + 
			"        {\n" + 
			"            \"family\":[\n" + 
			"                \"FAMILY\"\n" + 
			"            ]\n" + 
			"        }\n" + 
			"    ]\n" + 
			"}", enc.trim());
		//@formatter:on

		Patient parsed = ourCtx.newJsonParser().parseResource(Patient.class, enc);
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
	public void testEncodeNarrativeSuppressed() throws Exception {
		Patient patient = new Patient();
		patient.setId("Patient/1/_history/1");
		patient.getText().setDivAsString("<div>THE DIV</div>");
		patient.addName().addFamily("FAMILY");
		patient.getMaritalStatus().addCoding().setCode("D");

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
	public void testEncodeParametersWithId() {
		Parameters reqParms = new Parameters();
		IdType patient = new IdType(1);
		reqParms.addParameter().setName("patient").setValue(patient);

		String enc = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(reqParms);
		ourLog.info(enc);

		assertThat(enc, containsString("\"valueId\":\"1\""));
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
		assertThat(encoded, stringContainsInOrder("\"tag\"", "\"system\":\"" + Constants.TAG_SUBSETTED_SYSTEM + "\",", "\"code\":\"" + Constants.TAG_SUBSETTED_CODE + "\""));
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
		assertThat(encoded, stringContainsInOrder("\"tag\"", "\"system\":\"foo\",", "\"code\":\"bar\"", "\"system\":\"" + Constants.TAG_SUBSETTED_SYSTEM + "\"", "\"code\":\"" + Constants.TAG_SUBSETTED_CODE + "\""));
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

		assertEquals("{\"resourceType\":\"Patient\",\"meta\":{\"tag\":[{\"system\":\"scheme\",\"code\":\"term\",\"display\":\"display\"}]},\"identifier\":[{\"system\":\"sys\",\"value\":\"val\"}]}", enc);

	}

	// see #241
	@Test
	public void testEncodeThenParseShouldNotAddSpuriousId() throws Exception {
		Condition condition = new Condition().setVerificationStatus(ConditionVerificationStatus.CONFIRMED);
		Bundle bundle = new Bundle();
		BundleEntryComponent entry = new Bundle.BundleEntryComponent();
		entry.setIdElement(IdType.newRandomUuid());
		entry.setResource(condition);
		bundle.getEntry().add(entry);
		IParser parser = ourCtx.newJsonParser();
		String json = parser.encodeResourceToString(bundle);
		ourLog.info(json);
		bundle = (Bundle) parser.parseResource(json);
		assertThat(json, not(containsString("\"id\"")));
	}

	@Test
	public void testEncodeWithNarrative() {
		Patient p = new Patient();
		p.addName().addFamily("Smith").addGiven("John");

		ourCtx.setNarrativeGenerator(new DefaultThymeleafNarrativeGenerator());

		String output = ourCtx.newJsonParser().encodeResourceToString(p);
		ourLog.info(output);

		assertThat(output, containsString("\"text\":{\"status\":\"generated\",\"div\":\"<div><div class=\\\"hapiHeaderText\\\"> John <b>SMITH </b></div>"));
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

		assertEquals("<xhtml:div xmlns:xhtml=\"http://www.w3.org/1999/xhtml\"><xhtml:img src=\"foo\"/>@fhirabend</xhtml:div>", parsed.getText().getDiv().getValueAsString());

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
		String content = IOUtils.toString(JsonParserDstu3Test.class.getResourceAsStream("/bundle-example.json"));

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
		String content = IOUtils.toString(JsonParserDstu3Test.class.getResourceAsStream("/bundle-example2.xml"));

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
		String content = IOUtils.toString(JsonParserDstu3Test.class.getResourceAsStream("/bundle-example.json"));

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
		assertEquals("urn:uuid:", parsed.getEntry().get(0).getResource().getIdElement().getBaseUrl());
		assertEquals("180f219f-97a8-486d-99d9-ed631fe4fc57", parsed.getEntry().get(0).getResource().getIdElement().getIdPart());
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
		assertEquals("Patient/pat1", res.getId());
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
	}

	@Test
	public void testParseBundleWithBinary() {
		Binary patient = new Binary();
		patient.setId(new IdType("http://base/Binary/11/_history/22"));
		patient.setContentType("foo");
		patient.setContent(new byte[] { 1, 2, 3, 4 });

		String val = ourCtx.newJsonParser().encodeResourceToString(patient);

		String expected = "{\"resourceType\":\"Binary\",\"id\":\"11\",\"contentType\":\"foo\",\"content\":\"AQIDBA==\"}";
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
		assertEquals("patient family", p.getName().get(0).getFamily().get(0).getValue());
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

		ReportObservationDstu3 obsv = new ReportObservationDstu3();
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

		ReportObservationDstu3 obsv = new ReportObservationDstu3();
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
}
