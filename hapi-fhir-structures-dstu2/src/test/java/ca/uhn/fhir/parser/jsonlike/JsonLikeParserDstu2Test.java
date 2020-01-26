package ca.uhn.fhir.parser.jsonlike;

import java.io.StringReader;

import org.apache.commons.io.IOUtils;
import org.junit.AfterClass;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IJsonLikeParser;
import ca.uhn.fhir.parser.json.GsonProviderFactory;
import ca.uhn.fhir.parser.json.GsonStructure;
import ca.uhn.fhir.parser.json.JsonLikeProviderFactory;
import ca.uhn.fhir.parser.json.JsonLikeStructure;
import ca.uhn.fhir.util.TestUtil;

public class JsonLikeParserDstu2Test {
	private static FhirContext ourCtx = FhirContext.forDstu2();
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(JsonLikeParserDstu2Test.class);

	/**
	 * Test for #146
	 */
	@Test
	public void testJsonLikeParseAndEncodeBundleFromXmlToJson() throws Exception {
		String content = IOUtils.toString(JsonLikeParserDstu2Test.class.getResourceAsStream("/bundle-example2.xml"));

		ca.uhn.fhir.model.dstu2.resource.Bundle parsed = ourCtx.newXmlParser().parseResource(ca.uhn.fhir.model.dstu2.resource.Bundle.class, content);

		String encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(parsed);
		ourLog.info(encoded);
		
		JsonLikeProviderFactory providerFactory = new GsonProviderFactory();
		JsonLikeStructure jsonLikeStructure = providerFactory.createJsonLikeStructure(new StringReader(encoded));
		
		IJsonLikeParser jsonLikeparser = (IJsonLikeParser)ourCtx.newJsonParser();
		
		ca.uhn.fhir.model.dstu2.resource.Bundle bundle = jsonLikeparser.parseResource(ca.uhn.fhir.model.dstu2.resource.Bundle.class, jsonLikeStructure);
		
		jsonLikeparser = (IJsonLikeParser)ourCtx.newJsonParser();
		bundle = jsonLikeparser.parseResource(ca.uhn.fhir.model.dstu2.resource.Bundle.class, new StringReader(encoded));
		
		jsonLikeparser = (IJsonLikeParser)ourCtx.newJsonParser();
		jsonLikeparser.setJsonLikeProviderFactory(providerFactory);
		bundle = jsonLikeparser.parseResource(ca.uhn.fhir.model.dstu2.resource.Bundle.class, new StringReader(encoded));
	}
	@Test
	public void testJsonLikeParseWithDefaultProviderFactory() throws Exception {
		String content = IOUtils.toString(JsonLikeParserDstu2Test.class.getResourceAsStream("/bundle-example2.xml"));
		ca.uhn.fhir.model.dstu2.resource.Bundle parsed = ourCtx.newXmlParser().parseResource(ca.uhn.fhir.model.dstu2.resource.Bundle.class, content);
		String encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(parsed);
		ourLog.info(encoded);
		
		IJsonLikeParser jsonLikeparser = (IJsonLikeParser)ourCtx.newJsonParser();
		ca.uhn.fhir.model.dstu2.resource.Bundle bundle = jsonLikeparser.parseResource(ca.uhn.fhir.model.dstu2.resource.Bundle.class, new StringReader(encoded));
	}

	@Test
	public void testJsonLikeParseWithGsonProviderFactory() throws Exception {
		String content = IOUtils.toString(JsonLikeParserDstu2Test.class.getResourceAsStream("/bundle-example2.xml"));
		ca.uhn.fhir.model.dstu2.resource.Bundle parsed = ourCtx.newXmlParser().parseResource(ca.uhn.fhir.model.dstu2.resource.Bundle.class, content);
		String encoded = ourCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(parsed);
		ourLog.info(encoded);
		
		JsonLikeProviderFactory providerFactory = new GsonProviderFactory();
		IJsonLikeParser jsonLikeparser = (IJsonLikeParser)ourCtx.newJsonParser();
		jsonLikeparser.setJsonLikeProviderFactory(providerFactory);
		
		ca.uhn.fhir.model.dstu2.resource.Bundle bundle = jsonLikeparser.parseResource(ca.uhn.fhir.model.dstu2.resource.Bundle.class, new StringReader(encoded));
	}
	
	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}
}
