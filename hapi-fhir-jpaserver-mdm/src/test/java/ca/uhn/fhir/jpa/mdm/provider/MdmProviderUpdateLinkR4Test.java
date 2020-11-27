package ca.uhn.fhir.jpa.mdm.provider;

import ca.uhn.fhir.mdm.api.MdmConstants;
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.util.MessageHelper;
import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

public class MdmProviderUpdateLinkR4Test extends BaseLinkR4Test {

	@Autowired
	private MessageHelper myMessageHelper;

	@Test
	public void testUpdateLinkNoMatch() {
		assertLinkCount(1);
		myMdmProviderR4.updateLink(mySourcePatientId, myPatientId, NO_MATCH_RESULT, myRequestDetails);
		assertLinkCount(2);

		List<MdmLink> links = getPatientLinks();
		assertEquals(MdmLinkSourceEnum.MANUAL, links.get(0).getLinkSource());
		assertEquals(MdmMatchResultEnum.NO_MATCH, links.get(0).getMatchResult());
		assertEquals(MdmLinkSourceEnum.AUTO, links.get(1).getLinkSource());
		assertEquals(MdmMatchResultEnum.MATCH, links.get(1).getMatchResult());
		assertNotEquals(links.get(0).getGoldenResourcePid(), links.get(1).getGoldenResourcePid());
	}

	@Test
	public void testUpdateLinkMatch() {
		assertLinkCount(1);
		myMdmProviderR4.updateLink(mySourcePatientId, myPatientId, MATCH_RESULT, myRequestDetails);
		assertLinkCount(1);

		List<MdmLink> links = getPatientLinks();
		assertEquals(MdmLinkSourceEnum.MANUAL, links.get(0).getLinkSource());
		assertEquals(MdmMatchResultEnum.MATCH, links.get(0).getMatchResult());
	}

	@Test
	public void testUpdateLinkTwiceFailsDueToWrongVersion() {
		myMdmProviderR4.updateLink(mySourcePatientId, myPatientId, MATCH_RESULT, myRequestDetails);

		materiallyChangeGoldenPatient();

		try {
			myMdmProviderR4.updateLink(mySourcePatientId, myPatientId, NO_MATCH_RESULT, myRequestDetails);
			fail();
		} catch (ResourceVersionConflictException e) {
			assertThat(e.getMessage(), matchesPattern("Requested resource Patient/\\d+/_history/1 is not the latest version.  Latest version is Patient/\\d+/_history/2"));
		}
	}

	private void materiallyChangeGoldenPatient() {
		Patient materiallyChangedSourcePatientThatShouldTriggerVersionChange = (Patient) mySourcePatient;
		materiallyChangedSourcePatientThatShouldTriggerVersionChange.getNameFirstRep().setFamily("NEW LAST NAME");
		myPatientDao.update(materiallyChangedSourcePatientThatShouldTriggerVersionChange);
	}

	@Test
	public void testUpdateLinkTwiceDoesNotThrowValidationErrorWhenNoVersionIsProvided() {
		myMdmProviderR4.updateLink(mySourcePatientId, myPatientId, MATCH_RESULT, myRequestDetails);
		Patient patient = (Patient) myMdmProviderR4.updateLink(myVersionlessGodlenResourceId, myPatientId, NO_MATCH_RESULT, myRequestDetails);
		assertNotNull(patient); // if this wasn't allowed - a validation exception would be thrown
	}

	@Test
	public void testUnlinkLink() {
		myMdmProviderR4.updateLink(mySourcePatientId, myPatientId, NO_MATCH_RESULT, myRequestDetails);

		materiallyChangeGoldenPatient();

		try {
			myMdmProviderR4.updateLink(mySourcePatientId, myPatientId, MATCH_RESULT, myRequestDetails);
			fail();
		} catch (ResourceVersionConflictException e) {
			assertThat(e.getMessage(), matchesPattern("Requested resource Patient/\\d+/_history/1 is not the latest version.  Latest version is Patient/\\d+/_history/2"));
		}
	}

	@Test
	public void testUpdateIllegalResultForPossibleMatch() {
		try {
			myMdmProviderR4.updateLink(mySourcePatientId, myPatientId, POSSIBLE_MATCH_RESULT, myRequestDetails);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("$mdm-update-link illegal matchResult value 'POSSIBLE_MATCH'.  Must be NO_MATCH or MATCH", e.getMessage());
		}
	}

	@Test
	public void testUpdateIllegalResultPD() {
		try {
			myMdmProviderR4.updateLink(mySourcePatientId, myPatientId, POSSIBLE_DUPLICATE_RESULT, myRequestDetails);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("$mdm-update-link illegal matchResult value 'POSSIBLE_DUPLICATE'.  Must be NO_MATCH or MATCH", e.getMessage());
		}
	}

	@Test
	public void testUpdateIllegalSecondArg() {
		try {
			myMdmProviderR4.updateLink(myPatientId, new StringType(""), NO_MATCH_RESULT, myRequestDetails);
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), endsWith(" must have form <resourceType>/<id>  where <id> is the id of the resource and <resourceType> is the type of the resource"));
		}
	}

	@Test
	public void testUpdateIllegalFirstArg() {
		try {
			myMdmProviderR4.updateLink(new StringType(""), myPatientId, NO_MATCH_RESULT, myRequestDetails);
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), endsWith(" must have form <resourceType>/<id> where <id> is the id of the resource"));
		}
	}

	@Test
	public void testAttemptingToModifyANonExistentLinkFails() {
		try {
			myMdmProviderR4.updateLink(mySourcePatientId, mySourcePatientId, NO_MATCH_RESULT, myRequestDetails);
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), startsWith("No link"));
		}
	}

	@Test
	public void testUpdateStrangePerson() {
		Patient person = createPatient();
		try {
			myMdmProviderR4.updateLink(new StringType(person.getIdElement().getValue()), myPatientId, NO_MATCH_RESULT, myRequestDetails);
			fail();
		} catch (InvalidRequestException e) {
			String expectedMessage = myMessageHelper.getMessageForUnmanagedResource();
			assertEquals(expectedMessage, e.getMessage());
		}
	}

	@Test
	public void testExcludedPerson() {
		Patient patient = new Patient();
		patient.getMeta().addTag().setSystem(MdmConstants.SYSTEM_MDM_MANAGED).setCode(MdmConstants.CODE_NO_MDM_MANAGED);
		createPatient(patient);
		try {
			myMdmProviderR4.updateLink(mySourcePatientId, new StringType(patient.getIdElement().getValue()), NO_MATCH_RESULT, myRequestDetails);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("The target is marked with the " + MdmConstants.CODE_NO_MDM_MANAGED + " tag which means it may not be MDM linked.", e.getMessage());
		}
	}
}
