package ca.uhn.fhir.narrative;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

import ca.uhn.fhir.model.dstu.composite.NarrativeDt;
import ca.uhn.fhir.model.dstu.resource.Practitioner;

public class CustomThymeleafNarrativeGeneratorTest {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(CustomThymeleafNarrativeGeneratorTest.class);

	@Test
	public void testGenerator() {

		CustomThymeleafNarrativeGenerator gen = new CustomThymeleafNarrativeGenerator("file:src/test/resources/narrative/customnarrative.properties");

		Practitioner p = new Practitioner();
		p.addIdentifier("sys", "val1");
		p.addIdentifier("sys", "val2");
		p.getAddress().addLine("line1").addLine("line2");
		p.getName().addFamily("fam1").addGiven("given");

		NarrativeDt narrative = gen.generateNarrative(p);

		String actual = narrative.getDiv().getValueAsString();
		ourLog.info(actual);

		assertThat(actual, containsString("<h1>Name</h1><div class=\"nameElement\"> given <b>FAM1 </b></div><h1>Address</h1><div><span>line1 </span><br /><span>line2 </span><br /></div></div>"));
		
	}
}
