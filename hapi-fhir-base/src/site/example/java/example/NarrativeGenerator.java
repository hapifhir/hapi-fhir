package example;

import java.io.IOException;

import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.narrative.CustomThymeleafNarrativeGenerator;

public class NarrativeGenerator {

	@Test
	public void testGenerator() throws IOException {

//START SNIPPET: gen
String propFile = "classpath:/com/foo/customnarrative.properties";
CustomThymeleafNarrativeGenerator gen = new CustomThymeleafNarrativeGenerator(propFile);

FhirContext ctx = new FhirContext();
ctx.setNarrativeGenerator(gen);
//END SNIPPET: gen

	
	}
}
