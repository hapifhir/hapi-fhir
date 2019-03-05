package example;

import java.io.IOException;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.narrative.CustomThymeleafNarrativeGenerator;

public class NarrativeGenerator {

	public void testGenerator() throws IOException {

//START SNIPPET: gen
FhirContext ctx = FhirContext.forDstu2();
String propFile = "classpath:/com/foo/customnarrative.properties";
CustomThymeleafNarrativeGenerator gen = new CustomThymeleafNarrativeGenerator(ctx, propFile);

ctx.setNarrativeGenerator(gen);
//END SNIPPET: gen

	
	}
}
