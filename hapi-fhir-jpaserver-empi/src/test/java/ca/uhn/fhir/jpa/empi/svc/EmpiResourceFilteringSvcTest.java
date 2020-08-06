package ca.uhn.fhir.jpa.empi.svc;

import ca.uhn.fhir.jpa.empi.BaseEmpiR4Test;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class EmpiResourceFilteringSvcTest extends BaseEmpiR4Test {

	@Autowired
	private EmpiResourceFilteringSvc myEmpiResourceFilteringSvc;

	@Test
	public void testFilterResourcesWhichHaveNoRelevantAttributes() {
		Patient patient = new Patient();
		patient.setDeceased(new BooleanType(true)); //EMPI rules defined do not care about the deceased attribute.

		//SUT
		boolean shouldBeProcessed = myEmpiResourceFilteringSvc.shouldBeProcessed(patient);

		assertThat(shouldBeProcessed, is(equalTo(false)));
	}

	@Test
	public void testDoNotFilterResourcesWithEMPIAttributes() {
		Patient patient = new Patient();
		patient.addIdentifier().setValue("Hey I'm an ID! rules defined in empi-rules.json care about me!");

		//SUT
		boolean shouldBeProcessed = myEmpiResourceFilteringSvc.shouldBeProcessed(patient);

		assertThat(shouldBeProcessed, is(equalTo(true)));
	}
}
