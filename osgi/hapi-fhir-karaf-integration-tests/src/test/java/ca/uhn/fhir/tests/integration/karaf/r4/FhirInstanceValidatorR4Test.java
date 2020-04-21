package ca.uhn.fhir.tests.integration.karaf.r4;

import java.io.IOException;
import java.util.List;
import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.r4.hapi.ctx.DefaultProfileValidationSupport;
import org.hl7.fhir.r4.hapi.ctx.HapiWorkerContext;
import org.hl7.fhir.r4.model.Base;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.r4.utils.FHIRPathEngine;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;

import static ca.uhn.fhir.tests.integration.karaf.PaxExamOptions.HAPI_FHIR_VALIDATION_R4;
import static ca.uhn.fhir.tests.integration.karaf.PaxExamOptions.KARAF;
import static ca.uhn.fhir.tests.integration.karaf.PaxExamOptions.WRAP;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.options;
import static org.ops4j.pax.exam.CoreOptions.when;
import static org.ops4j.pax.exam.karaf.options.KarafDistributionOption.debugConfiguration;

/**
 * Useful docs about this test: https://ops4j1.jira.com/wiki/display/paxexam/FAQ
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
public class FhirInstanceValidatorR4Test {

	private FhirContext ourCtx = FhirContext.forR4();
	private FHIRPathEngine ourEngine = new FHIRPathEngine(new HapiWorkerContext(ourCtx, new DefaultProfileValidationSupport()));
	private final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirInstanceValidatorR4Test.class);

	@Configuration
	public Option[] config() throws IOException {
		return options(
			KARAF.option(),
			HAPI_FHIR_VALIDATION_R4.option(),
			mavenBundle().groupId("org.apache.servicemix.bundles").artifactId("org.apache.servicemix.bundles.hamcrest").versionAsInProject(),
			WRAP.option(),
			when(false)
				.useOptions(
					debugConfiguration("5005", true))
		);
	}

	@Test
	public void testAs() throws Exception {
		Observation obs = new Observation();
		obs.setValue(new StringType("FOO"));

		List<Base> value = ourEngine.evaluate(obs, "Observation.value.as(String)");
		assertEquals(1, value.size());
		assertEquals("FOO", ((StringType)value.get(0)).getValue());
	}

	@Test
	public void testExistsWithNoValue() throws FHIRException {
		Patient patient = new Patient();
		patient.setDeceased(new BooleanType());
		List<Base> eval = ourEngine.evaluate(patient, "Patient.deceased.exists()");
		ourLog.info(eval.toString());
		assertFalse(((BooleanType)eval.get(0)).getValue());
	}

	@Test
	public void testExistsWithValue() throws FHIRException {
		Patient patient = new Patient();
		patient.setDeceased(new BooleanType(false));
		List<Base> eval = ourEngine.evaluate(patient, "Patient.deceased.exists()");
		ourLog.info(eval.toString());
		assertTrue(((BooleanType)eval.get(0)).getValue());
	}

	@Test
	public void testConcatenation() throws FHIRException {
		String exp = "Patient.name.family & '.'";

		Patient p = new Patient();
		p.addName().setFamily("TEST");
		String result = ourEngine.evaluateToString(p, exp);
		assertEquals("TEST.", result);
	}

	@Test
	public void testConcatenationFunction() throws FHIRException {
		String exp = "element.first().path.startsWith(%resource.type) and element.tail().all(path.startsWith(%resource.type&'.'))";

		StructureDefinition sd = new StructureDefinition();
		StructureDefinition.StructureDefinitionDifferentialComponent diff = sd.getDifferential();

		diff.addElement().setPath("Patient.name");


		Patient p = new Patient();
		p.addName().setFamily("TEST");
		List<Base> result = ourEngine.evaluate(null, p, diff, exp);
		ourLog.info(result.toString());
//		assertEquals("TEST.", result);
	}

}
