package ca.uhn.fhir.jpa.empi.provider;

import ca.uhn.fhir.empi.api.EmpiConstants;
import ca.uhn.fhir.empi.api.EmpiLinkSourceEnum;
import ca.uhn.fhir.empi.api.EmpiMatchResultEnum;
import ca.uhn.fhir.jpa.entity.EmpiLink;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Person;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class EmpiProviderUpdateLinkR4Test extends BaseLinkR4Test {
	@Test
	public void testUpdateLinkNoMatch() {
		assertLinkCount(1);
		myEmpiProviderR4.updateLink(mySourcePatientId, myPatientId, NO_MATCH_RESULT, myRequestDetails);
		assertLinkCount(2);

		List<EmpiLink> links = getPatientLinks();
		assertEquals(EmpiLinkSourceEnum.MANUAL, links.get(0).getLinkSource());
		assertEquals(EmpiMatchResultEnum.NO_MATCH, links.get(0).getMatchResult());
		assertEquals(EmpiLinkSourceEnum.AUTO, links.get(1).getLinkSource());
		assertEquals(EmpiMatchResultEnum.MATCH, links.get(1).getMatchResult());
		assertNotEquals(links.get(0).getSourceResourcePid(), links.get(1).getSourceResourcePid());
	}

	@Test
	public void testUpdateLinkMatch() {
		assertLinkCount(1);
		myEmpiProviderR4.updateLink(mySourcePatientId, myPatientId, MATCH_RESULT, myRequestDetails);
		assertLinkCount(1);

		List<EmpiLink> links = getPatientLinks();
		assertEquals(EmpiLinkSourceEnum.MANUAL, links.get(0).getLinkSource());
		assertEquals(EmpiMatchResultEnum.MATCH, links.get(0).getMatchResult());
	}

	@Test
	public void testUpdateLinkTwiceFailsDueToWrongVersion() {
		myEmpiProviderR4.updateLink(mySourcePatientId, myPatientId, MATCH_RESULT, myRequestDetails);
		try {
			myEmpiProviderR4.updateLink(mySourcePatientId, myPatientId, NO_MATCH_RESULT, myRequestDetails);
			fail();
		} catch (ResourceVersionConflictException e) {
			assertThat(e.getMessage(), matchesPattern("Requested resource Person/\\d+/_history/1 is not the latest version.  Latest version is Person/\\d+/_history/2"));
		}
	}

	@Test
	public void testUpdateLinkTwiceWorksWhenNoVersionProvided() {
		myEmpiProviderR4.updateLink(mySourcePatientId, myPatientId, MATCH_RESULT, myRequestDetails);
		Person person = (Person) myEmpiProviderR4.updateLink(myVersionlessPersonId, myPatientId, NO_MATCH_RESULT, myRequestDetails);
		assertThat(person.getLink(), hasSize(0));
	}

	@Test
	public void testUnlinkLink() {
		myEmpiProviderR4.updateLink(mySourcePatientId, myPatientId, NO_MATCH_RESULT, myRequestDetails);
		try {
			myEmpiProviderR4.updateLink(mySourcePatientId, myPatientId, MATCH_RESULT, myRequestDetails);
			fail();
		} catch (ResourceVersionConflictException e) {
			assertThat(e.getMessage(), matchesPattern("Requested resource Person/\\d+/_history/1 is not the latest version.  Latest version is Person/\\d+/_history/2"));
		}
	}

	@Test
	public void testUpdateIllegalResultPM() {
		try {
			myEmpiProviderR4.updateLink(mySourcePatientId, myPatientId, POSSIBLE_MATCH_RESULT, myRequestDetails);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("$empi-update-link illegal matchResult value 'POSSIBLE_MATCH'.  Must be NO_MATCH or MATCH", e.getMessage());
		}
	}

	@Test
	public void testUpdateIllegalResultPD() {
		try {
			myEmpiProviderR4.updateLink(mySourcePatientId, myPatientId, POSSIBLE_DUPLICATE_RESULT, myRequestDetails);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("$empi-update-link illegal matchResult value 'POSSIBLE_DUPLICATE'.  Must be NO_MATCH or MATCH", e.getMessage());
		}
	}

	@Test
	public void testUpdateIllegalFirstArg() {
		try {
			myEmpiProviderR4.updateLink(myPatientId, myPatientId, NO_MATCH_RESULT, myRequestDetails);
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), endsWith(" must have form Person/<id> where <id> is the id of the person"));
		}
	}

	@Test
	public void testUpdateIllegalSecondArg() {
		try {
			myEmpiProviderR4.updateLink(mySourcePatientId, mySourcePatientId, NO_MATCH_RESULT, myRequestDetails);
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), endsWith("must have form Patient/<id> or Practitioner/<id> where <id> is the id of the resource"));
		}
	}

	@Test
	public void testUpdateStrangePerson() {
		Patient person = createUnmanagedSourceResource();
		try {
			myEmpiProviderR4.updateLink(new StringType(person.getIdElement().getValue()), myPatientId, NO_MATCH_RESULT, myRequestDetails);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("Only EMPI Managed Person resources may be updated via this operation.  The Source Resource provided is not tagged as managed by hapi-empi", e.getMessage());
		}
	}

	@Test
	public void testExcludedPerson() {
		Patient patient = new Patient();
		patient.getMeta().addTag().setSystem(EmpiConstants.SYSTEM_MDM_MANAGED).setCode(EmpiConstants.CODE_NO_EMPI_MANAGED);
		createPatient(patient);
		try {
			myEmpiProviderR4.updateLink(mySourcePatientId, new StringType(patient.getIdElement().getValue()), NO_MATCH_RESULT, myRequestDetails);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("The target is marked with the " + EmpiConstants.CODE_NO_EMPI_MANAGED + " tag which means it may not be EMPI linked.", e.getMessage());
		}
	}
}
