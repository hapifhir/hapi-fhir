package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.narrative.CustomThymeleafNarrativeGenerator;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;

public class FhirResourceDaoR4NarrativeGeneratorTest extends BaseJpaR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(FhirResourceDaoR4NarrativeGeneratorTest.class);

	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();
	}
	@AfterEach
	public void after() {
		myFhirContext.setNarrativeGenerator(null);
	}

	@Test
	public void testNarrativeGenerator_whenNotConfigured_resourceCreatedHasNoNarrative() {
		// setup
		Patient patient = new Patient();
		patient.addIdentifier().setSystem("urn:test").setValue("121212");
		patient.addName().setFamily("Charm").addGiven("May").addGiven("Flower");
		patient.setBirthDate(fromLocalDate(LocalDate.of(1990, Month.APRIL, 30)));

		// execute
		IBaseResource resource = myPatientDao.create(patient).getResource();
		String output = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(resource);
		ourLog.debug("Output:\n{}", output);

		// verify
		assertThat(output).doesNotContain("<text>");
	}

	@Test
	public void testNarrativeGenerator_whenConfiguredWithFhirContext_resourceCreatedContainsNarrative() {
		// setup
		String expectedNarrative = """
			"text": {
			    "status": "generated",
			    "div": "<div xmlns=\\"http://www.w3.org/1999/xhtml\\"><div class=\\"hapiHeaderText\\">May Flower <b>CHARM </b></div><table class=\\"hapiPropertyTable\\"><tbody><tr><td>Date of birth</td><td><span>30 April 1990</span></td></tr></tbody></table></div>"
			  }""";
		CustomThymeleafNarrativeGenerator generator = new CustomThymeleafNarrativeGenerator("classpath:ca/uhn/fhir/narrative/narratives.properties");
		myFhirContext.setNarrativeGenerator(generator);

		Patient patient = new Patient();
		patient.addName().setFamily("Charm").addGiven("May").addGiven("Flower");
		patient.setBirthDate(fromLocalDate(LocalDate.of(1990, Month.APRIL, 30)));

		// execute
		IBaseResource resource = myPatientDao.create(patient).getResource();
		String output = myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(resource);
		ourLog.debug(output);

		// verify
		assertThat(output).contains(expectedNarrative);
	}
}
