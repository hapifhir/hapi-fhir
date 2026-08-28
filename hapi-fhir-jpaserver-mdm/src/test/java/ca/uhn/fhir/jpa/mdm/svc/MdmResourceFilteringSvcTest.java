package ca.uhn.fhir.jpa.mdm.svc;

import ca.uhn.fhir.jpa.mdm.BaseMdmR4Test;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static ca.uhn.fhir.util.HapiExtensions.EXT_RESOURCE_PLACEHOLDER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MdmResourceFilteringSvcTest extends BaseMdmR4Test {

	@Autowired
	private MdmResourceFilteringSvc myMdmResourceFilteringSvc;

	@Test
	public void testFilterResourcesWhichHaveNoRelevantAttributes() {
		Patient patient = new Patient();
		patient.setDeceased(new BooleanType(true)); // MDM rules defined do not care about the deceased attribute.

		//SUT
		boolean shouldBeProcessed = myMdmResourceFilteringSvc.shouldBeProcessed(patient);

		assertEquals(false, shouldBeProcessed);
	}

	@Test
	public void testDoNotFilterResourcesWithMdmAttributes() {
		Patient patient = new Patient();
		patient.addIdentifier().setValue("Hey I'm an ID! rules defined in mdm-rules.json care about me!");

		//SUT
		boolean shouldBeProcessed = myMdmResourceFilteringSvc.shouldBeProcessed(patient);

		assertEquals(true, shouldBeProcessed);
	}

	@Test
	void shouldBeProcessed_withPlaceholderResource_skipsUnfilledProcessesFilled() {
		myMdmSettings.setIgnorePlaceholderResources(true);

		Patient placeholder = new Patient();
		placeholder.addExtension(EXT_RESOURCE_PLACEHOLDER, new BooleanType(true));
		placeholder.addIdentifier().setValue("123");
		assertFalse(myMdmResourceFilteringSvc.shouldBeProcessed(placeholder));

		Patient filledIn = new Patient();   // no extension — the update replaces the body
		filledIn.addIdentifier().setValue("123");
		assertTrue(myMdmResourceFilteringSvc.shouldBeProcessed(filledIn));
	}
}
