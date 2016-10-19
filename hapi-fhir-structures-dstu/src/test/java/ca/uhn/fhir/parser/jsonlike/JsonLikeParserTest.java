package ca.uhn.fhir.parser.jsonlike;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.BundleEntry;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.dstu.resource.DiagnosticReport;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.parser.IJsonLikeParser;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.parser.JsonParser;
import ca.uhn.fhir.parser.StrictErrorHandler;
import ca.uhn.fhir.parser.XmlParser;
import ca.uhn.fhir.parser.json.GsonStructure;
import ca.uhn.fhir.parser.json.JsonLikeStructure;
import ca.uhn.fhir.parser.json.JsonLikeWriter;
import ca.uhn.fhir.util.TestUtil;
import net.sf.json.JSON;
import net.sf.json.JSONSerializer;

public class JsonLikeParserTest {
	private static FhirContext ourCtx;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(JsonLikeParserTest.class);

	@Test
	public void testJsonLikeSimpleBundleEncode() throws InterruptedException, IOException {
		String xmlString = IOUtils.toString(JsonParser.class.getResourceAsStream("/atom-document-large.xml"), Charset.forName("UTF-8"));
		Bundle obs = ourCtx.newXmlParser().parseBundle(xmlString);

		IJsonLikeParser jsonLikeParser = (IJsonLikeParser)ourCtx.newJsonParser().setPrettyPrint(true);
		StringWriter stringWriter = new StringWriter();
		JsonLikeStructure jsonLikeStructure = new GsonStructure();
		JsonLikeWriter jsonLikeWriter = jsonLikeStructure.getJsonLikeWriter(stringWriter);
		jsonLikeParser.encodeBundleToJsonLikeWriter(obs, jsonLikeWriter);
		String encoded = stringWriter.toString();
		ourLog.info(encoded);
	}

	@Test
	public void testJsonLikeSimpleResourceEncode() throws IOException {

		String xmlString = IOUtils.toString(JsonParser.class.getResourceAsStream("/example-patient-general.xml"), Charset.forName("UTF-8"));
		IParser parser = ourCtx.newXmlParser();
		parser.setParserErrorHandler(new StrictErrorHandler());
		Patient obs = parser.parseResource(Patient.class, xmlString);

		IJsonLikeParser jsonLikeParser = (IJsonLikeParser)ourCtx.newJsonParser();
		StringWriter stringWriter = new StringWriter();
		JsonLikeStructure jsonLikeStructure = new GsonStructure();
		JsonLikeWriter jsonLikeWriter = jsonLikeStructure.getJsonLikeWriter(stringWriter);
		jsonLikeParser.encodeResourceToJsonLikeWriter(obs, jsonLikeWriter);
		String encoded = stringWriter.toString();
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
	public void testJsonLikeSimpleResourceParse() throws DataFormatException, IOException {

		String msg = IOUtils.toString(XmlParser.class.getResourceAsStream("/example-patient-general.json"));

		IJsonLikeParser jsonLikeParser = (IJsonLikeParser)ourCtx.newJsonParser().setPrettyPrint(true);
		StringReader reader = new StringReader(msg);
		JsonLikeStructure jsonLikeStructure = new GsonStructure();
		jsonLikeStructure.load(reader);
		
		Patient res = jsonLikeParser.parseResource(Patient.class, jsonLikeStructure);

		assertEquals(2, res.getUndeclaredExtensions().size());
		assertEquals(1, res.getUndeclaredModifierExtensions().size());

	}
	
	@Test
	public void testJsonLikeParseBundle() throws DataFormatException, IOException {

		String msg = IOUtils.toString(XmlParser.class.getResourceAsStream("/atom-document-large.json"));

		IJsonLikeParser jsonLikeParser = (IJsonLikeParser)ourCtx.newJsonParser().setPrettyPrint(true);
		StringReader reader = new StringReader(msg);
		JsonLikeStructure jsonLikeStructure = new GsonStructure();
		jsonLikeStructure.load(reader);
		
		Bundle bundle = jsonLikeParser.parseBundle(jsonLikeStructure);
		
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
	public void testJsonLikeTagList() throws IOException {

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

		IJsonLikeParser jsonLikeParser = (IJsonLikeParser)ourCtx.newJsonParser().setPrettyPrint(true);
		StringReader reader = new StringReader(tagListStr);
		JsonLikeStructure jsonLikeStructure = new GsonStructure();
		jsonLikeStructure.load(reader);
		
		TagList tagList = jsonLikeParser.parseTagList(jsonLikeStructure);
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

		jsonLikeParser = (IJsonLikeParser)ourCtx.newJsonParser();
		StringWriter stringWriter = new StringWriter();
		jsonLikeStructure = new GsonStructure();
		JsonLikeWriter jsonLikeWriter = jsonLikeStructure.getJsonLikeWriter(stringWriter);
		jsonLikeParser.encodeTagListToJsonLikeWriter(tagList, jsonLikeWriter);
		String encoded = stringWriter.toString();
		assertEquals(expected, encoded);

	}
	

	@BeforeClass
	public static void beforeClass() {
		ourCtx = FhirContext.forDstu1();
	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
