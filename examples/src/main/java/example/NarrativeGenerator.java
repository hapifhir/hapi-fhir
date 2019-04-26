package example;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.narrative.CustomThymeleafNarrativeGenerator;

@SuppressWarnings("unused")
public class NarrativeGenerator {

	public void testGenerator() {

//START SNIPPET: gen
FhirContext ctx = FhirContext.forDstu2();
String propFile = "classpath:/com/foo/customnarrative.properties";
CustomThymeleafNarrativeGenerator gen = new CustomThymeleafNarrativeGenerator(propFile);

ctx.setNarrativeGenerator(gen);
//END SNIPPET: gen

	
	}
}
