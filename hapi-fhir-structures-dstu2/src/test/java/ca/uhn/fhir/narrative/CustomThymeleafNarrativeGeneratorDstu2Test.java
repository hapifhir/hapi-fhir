package ca.uhn.fhir.narrative;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.Practitioner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

public class CustomThymeleafNarrativeGeneratorDstu2Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(CustomThymeleafNarrativeGeneratorDstu2Test.class);

	private final FhirContext myCtx = FhirContext.forDstu2Cached();

	@AfterEach
	public void after() {
		myCtx.setNarrativeGenerator(null);
	}

	@Test
	public void testGenerator() {

//		CustomThymeleafNarrativeGenerator gen = new CustomThymeleafNarrativeGenerator("file:src/test/resources/narrative/customnarrative.properties");
		CustomThymeleafNarrativeGenerator gen = new CustomThymeleafNarrativeGenerator("classpath:narrative/customnarrative_dstu2.properties");
		myCtx.setNarrativeGenerator(gen);

		Practitioner p = new Practitioner();
		p.addIdentifier().setSystem("sys").setValue("val1");
		p.addIdentifier().setSystem("sys").setValue("val2");
		p.addAddress().addLine("line1").addLine("line2");
		p.getName().addFamily("fam1").addGiven("given");

		gen.populateResourceNarrative(myCtx, p);

		String actual = p.getText().getDiv().getValueAsString();
		ourLog.info(actual);

		assertThat(actual, containsString("<h1>Name</h1><div class=\"nameElement\">given <b>FAM1 </b></div><h1>Address</h1><div><span>line1 </span><br/><span>line2 </span><br/></div></div>"));

	}
}
