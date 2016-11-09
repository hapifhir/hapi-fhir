package ca.uhn.fhir.narrative;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;

import org.junit.AfterClass;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.composite.NarrativeDt;
import ca.uhn.fhir.model.dstu2.resource.Practitioner;
import ca.uhn.fhir.util.TestUtil;

public class CustomThymeleafNarrativeGeneratorDstu2Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(CustomThymeleafNarrativeGeneratorDstu2Test.class);

	private static FhirContext ourCtx = FhirContext.forDstu2();

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}


	@Test
	public void testGenerator() {

//		CustomThymeleafNarrativeGenerator gen = new CustomThymeleafNarrativeGenerator("file:src/test/resources/narrative/customnarrative.properties");
		CustomThymeleafNarrativeGenerator gen = new CustomThymeleafNarrativeGenerator("classpath:narrative/customnarrative_dstu2.properties");
		ourCtx.setNarrativeGenerator(gen);

		Practitioner p = new Practitioner();
		p.addIdentifier().setSystem("sys").setValue("val1");
		p.addIdentifier().setSystem("sys").setValue("val2");
		p.addAddress().addLine("line1").addLine("line2");
		p.getName().addFamily("fam1").addGiven("given");

		NarrativeDt narrative = new NarrativeDt();
		gen.generateNarrative(ourCtx, p, narrative);

		String actual = narrative.getDiv().getValueAsString();
		ourLog.info(actual);

		assertThat(actual, containsString("<h1>Name</h1><div class=\"nameElement\">given <b>FAM1 </b></div><h1>Address</h1><div><span>line1 </span><br/><span>line2 </span><br/></div></div>"));

	}
}
