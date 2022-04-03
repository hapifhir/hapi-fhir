package ca.uhn.fhir.parser.jsonlike;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IJsonLikeParser;
import ca.uhn.fhir.parser.json.JsonLikeStructure;
import ca.uhn.fhir.parser.json.jackson.JacksonStructure;
import ca.uhn.fhir.util.TestUtil;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.io.StringReader;

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
		
		JsonLikeStructure jsonLikeStructure = new JacksonStructure();
		jsonLikeStructure.load(new StringReader(encoded));
		
		IJsonLikeParser jsonLikeparser = (IJsonLikeParser)ourCtx.newJsonParser();
		
		ca.uhn.fhir.model.dstu2.resource.Bundle bundle = jsonLikeparser.parseResource(ca.uhn.fhir.model.dstu2.resource.Bundle.class, jsonLikeStructure);
		
	}
	
	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.randomizeLocaleAndTimezone();
	}
}
