
package ca.uhn.fhir.narrative.template;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.hl7.fhir.dstu3.model.Patient;
import org.junit.jupiter.api.Test;

import ca.uhn.fhir.context.FhirContext;

public class TemplateNarrativeGeneratorTest {

	@Test
	public void testTemplate() throws Exception {
		String template = IOUtils.toString(getClass().getResourceAsStream("/patient.narrative"), StandardCharsets.UTF_8);
		
		Patient input = new Patient();
		input.addName().addFamily("LNAME1");
		input.addName().addFamily("LNAME2");
		input.addName().addGiven("FNAME1");
		input.addName().addGiven("FNAME2");
		
		TemplateNarrativeGenerator gen = new TemplateNarrativeGenerator();
		String output = gen.processLiquid(FhirContext.forDstu3(), template, input);
		
		ourLog.info(output);
	}

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(TemplateNarrativeGeneratorTest.class);
}
