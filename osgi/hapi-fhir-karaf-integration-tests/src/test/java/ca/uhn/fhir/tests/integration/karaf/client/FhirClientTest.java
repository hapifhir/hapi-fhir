package ca.uhn.fhir.tests.integration.karaf.client;

import java.io.IOException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.PreferReturnEnum;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.hl7.fhir.dstu3.model.Patient;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;

import static ca.uhn.fhir.tests.integration.karaf.PaxExamOptions.HAPI_FHIR_CLIENT;
import static ca.uhn.fhir.tests.integration.karaf.PaxExamOptions.HAPI_FHIR_DSTU3;
import static ca.uhn.fhir.tests.integration.karaf.PaxExamOptions.KARAF;
import static ca.uhn.fhir.tests.integration.karaf.PaxExamOptions.WRAP;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.options;
import static org.ops4j.pax.exam.CoreOptions.when;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.debugConfiguration;

/**
 * Useful docs about this test: https://ops4j1.jira.com/wiki/display/paxexam/FAQ
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
@Disabled(value = "Relies on external service being up and running")
public class FhirClientTest {

	private FhirContext fhirContext = FhirContext.forDstu3();
	private IGenericClient client   = fhirContext.newRestfulGenericClient("http://fhirtest.uhn.ca/baseDstu3");
	private final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirClientTest.class);

	@Configuration
	public Option[] config() throws IOException {
		return options(
			KARAF.option(),
			HAPI_FHIR_CLIENT.option(),
			HAPI_FHIR_DSTU3.option(),
			mavenBundle().groupId("org.apache.servicemix.bundles").artifactId("org.apache.servicemix.bundles.hamcrest").versionAsInProject(),
			WRAP.option(),
			when(false)
				.useOptions(
					debugConfiguration("5005", true))
		);
	}

	@Test
	public void createPatient() throws Exception {
		Patient patient = new Patient();
		patient.setId("PATIENTID");
		patient.getMeta().addProfile("http://BAR");
		patient.addName().addGiven("GIVEN");
		MethodOutcome execute = client.create().resource(patient).prefer(PreferReturnEnum.REPRESENTATION).execute();
		ourLog.info(execute.toString());
	}

}
