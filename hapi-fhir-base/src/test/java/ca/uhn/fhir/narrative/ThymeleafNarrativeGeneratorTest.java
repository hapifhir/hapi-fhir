package ca.uhn.fhir.narrative;

import java.io.IOException;
import java.util.Date;

import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.narrative.ThymeleafNarrativeGenerator;

public class ThymeleafNarrativeGeneratorTest {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ThymeleafNarrativeGeneratorTest.class);

	@Test
	public void testGeneratePatient() throws IOException {
		FhirContext ctx = new FhirContext();

		Patient value = new Patient();
		
		value.addIdentifier().setSystem("urn:names").setValue("123456");
		value.addName().addFamily("blow").addGiven("joe").addGiven("john");
		value.getAddressFirstRep().addLine("123 Fake Street").addLine("Unit 1");
		value.getAddressFirstRep().setCity("Toronto").setState("ON").setCountry("Canada");
		
		value.setBirthDate(new Date(), TemporalPrecisionEnum.DAY);
		
		ThymeleafNarrativeGenerator gen = new ThymeleafNarrativeGenerator();
		String output = gen.generateNarrative("http://hl7.org/fhir/profiles/Patient", value).getDiv().getValueAsString();

		ourLog.info(output);
	}

}
