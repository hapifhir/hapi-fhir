package ca.uhn.fhir.jpa.mdm.svc;

import ca.uhn.fhir.jpa.mdm.BaseMdmR4Test;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

import static ca.uhn.fhir.util.HapiExtensions.EXT_RESOURCE_PLACEHOLDER;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MdmResourceFilteringSvcTest extends BaseMdmR4Test {

	@Autowired
	private MdmResourceFilteringSvc myMdmResourceFilteringSvc;

	@Override
	@AfterEach
	public void after() throws IOException {
		myMdmSettings.setIgnorePlaceholderResources(false);
		myMdmSettings.setCertainMatchOnSameEid(true);
		super.after();
	}

	@Test
	public void testFilterResourcesWhichHaveNoRelevantAttributes() {
		Patient patient = new Patient();
		patient.setDeceased(new BooleanType(true)); // MDM rules defined do not care about the deceased attribute.

		//SUT
		boolean shouldBeProcessed = myMdmResourceFilteringSvc.shouldBeProcessed(patient);

		assertFalse(shouldBeProcessed);
	}

	@Test
	public void testDoNotFilterResourcesWithMdmAttributes() {
		Patient patient = new Patient();
		patient.addIdentifier().setValue("Hey I'm an ID! rules defined in mdm-rules.json care about me!");

		//SUT
		boolean shouldBeProcessed = myMdmResourceFilteringSvc.shouldBeProcessed(patient);

		assertTrue(shouldBeProcessed);
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

	@Test
	void shouldBeProcessed_placeholderWithEid_isProcessedForEidMatching() {
		// setup
		myMdmSettings.setIgnorePlaceholderResources(true);

		Patient placeholder = new Patient();
		placeholder.addExtension(EXT_RESOURCE_PLACEHOLDER, new BooleanType(true));
		addExternalEID(placeholder, "eid-1");

		// execute & validate
		assertTrue(myMdmResourceFilteringSvc.shouldBeProcessed(placeholder));
	}

	/**
	 * A placeholder with an EID is processed only so that it can be linked by that EID. Without the EID
	 * lookup the matching rules ignore the placeholder, and it would get a Golden Resource of its own.
	 */
	@Test
	void shouldBeProcessed_placeholderWithEidAndCertainMatchOnSameEidDisabled_isSkipped() {
		// setup
		myMdmSettings.setIgnorePlaceholderResources(true);
		myMdmSettings.setCertainMatchOnSameEid(false);

		Patient placeholder = new Patient();
		placeholder.addExtension(EXT_RESOURCE_PLACEHOLDER, new BooleanType(true));
		addExternalEID(placeholder, "eid-1");

		// execute & validate
		assertFalse(myMdmResourceFilteringSvc.shouldBeProcessed(placeholder));
	}

	@Test
	void shouldBeProcessed_placeholderWithEidAndCertainMatchOnSameEidDisabled_isProcessedWhenPlaceholdersNotIgnored() {
		// setup
		myMdmSettings.setCertainMatchOnSameEid(false);

		Patient placeholder = new Patient();
		placeholder.addExtension(EXT_RESOURCE_PLACEHOLDER, new BooleanType(true));
		addExternalEID(placeholder, "eid-1");

		// execute & validate
		assertTrue(myMdmResourceFilteringSvc.shouldBeProcessed(placeholder));
	}
}
