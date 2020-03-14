package ca.uhn.fhir.igpack.parser;


import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.igpacks.parser.IgPackParserDstu3;
import org.hl7.fhir.dstu3.model.ValueSet;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class IgPackParserDstu3Test {
	private static final Logger ourLog = LoggerFactory.getLogger(IgPackParserDstu3Test.class);

	@Test
	public void testParseIg() {

		FhirContext ctx = FhirContext.forDstu3();
		IgPackParserDstu3 igParser = new IgPackParserDstu3(ctx);

		IValidationSupport result = igParser.parseIg(IgPackParserDstu3Test.class.getResourceAsStream("/us-core-stu3-validator.pack"), "US-Core STU3");

		assertNotNull(result.fetchResource(ValueSet.class, "http://hl7.org/fhir/us/core/ValueSet/simple-language"));
		assertEquals(50, result.fetchAllConformanceResources().size());
	}

}
