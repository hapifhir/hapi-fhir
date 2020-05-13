package ca.uhn.fhir.jpa.empi.provider;

import ca.uhn.fhir.empi.api.EmpiConstants;
import ca.uhn.fhir.empi.api.EmpiLinkSourceEnum;
import ca.uhn.fhir.empi.api.EmpiMatchResultEnum;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Person;
import org.hl7.fhir.r4.model.StringType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class EmpiProviderUpdateLinkR4Test extends BaseLinkR4Test {


	@Test
	public void testUpdateLinkHappyPath() {
		myEmpiProviderR4.updateLink(myPersonId, myPatientId, myNoMatch, myRequestDetails);

		myLink = getLink();
		assertEquals(EmpiLinkSourceEnum.MANUAL, myLink.getLinkSource());
		assertEquals(EmpiMatchResultEnum.NO_MATCH, myLink.getMatchResult());
	}

	@Test
	public void testUpdateIllegalResultPM() {
		try {
			myEmpiProviderR4.updateLink(myPersonId, myPatientId, myPossibleMatch, myRequestDetails);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("Match Result may only be set to NO_MATCH or MATCH", e.getMessage());
		}
	}

	@Test
	public void testUpdateIllegalResultPD() {
		try {
			myEmpiProviderR4.updateLink(myPersonId, myPatientId, myPossibleDuplicate, myRequestDetails);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("Match Result may only be set to NO_MATCH or MATCH", e.getMessage());
		}
	}

	@Test
	public void testUpdateIllegalFirstArg() {
		try {
			myEmpiProviderR4.updateLink(myPatientId, myPatientId, myNoMatch, myRequestDetails);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("personIdToDelete must have form Person/<id> where <id> is the id of the person", e.getMessage());
		}
	}

	@Test
	public void testUpdateIllegalSecondArg() {
		try {
			myEmpiProviderR4.updateLink(myPersonId, myPersonId, myNoMatch, myRequestDetails);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("personIdToKeep must have form Patient/<id> or Practitioner/<id> where <id> is the id of the resource", e.getMessage());
		}
	}

	@Test
	public void testUpdateStrangePerson() {
		Person person = createPerson();
		try {
			myEmpiProviderR4.updateLink(new StringType(person.getId()), myPatientId, myNoMatch, myRequestDetails);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("Only EMPI Managed Person resources may be updated via this operation.  The Person resource provided is not tagged as managed by hapi-empi", e.getMessage());
		}
	}

	@Test
	public void testExcludedPerson() {
		Patient patient = new Patient();
		patient.getMeta().addTag().setSystem(EmpiConstants.SYSTEM_EMPI_MANAGED).setCode(EmpiConstants.CODE_NO_EMPI_MANAGED);
		createPatient(patient);
		try {
			myEmpiProviderR4.updateLink(myPersonId, new StringType(patient.getId()), myNoMatch, myRequestDetails);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("The target is marked with the " + EmpiConstants.CODE_NO_EMPI_MANAGED + " tag which means it may not be EMPI linked.", e.getMessage());
		}
	}
}
