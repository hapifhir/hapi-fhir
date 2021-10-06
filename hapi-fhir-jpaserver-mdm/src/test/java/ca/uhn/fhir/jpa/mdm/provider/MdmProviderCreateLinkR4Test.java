package ca.uhn.fhir.jpa.mdm.provider;
import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.mdm.api.MdmConstants;
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.util.MessageHelper;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class MdmProviderCreateLinkR4Test extends BaseLinkR4Test {

	@Autowired
	private MessageHelper myMessageHelper;

	@Test
	public void testCreateLinkWithMatchResult() {
		assertLinkCount(1);

		Patient patient = createPatient(buildPatientWithNameAndId("PatientGiven", "ID.PatientGiven.123"), true, false);
		StringType patientId = new StringType(patient.getIdElement().getValue());

		Patient sourcePatient = createPatient(buildPatientWithNameAndId("SourcePatientGiven", "ID.SourcePatientGiven.123"), true, false);
		StringType sourcePatientId = new StringType(sourcePatient.getIdElement().getValue());

		myMdmProvider.createLink(sourcePatientId, patientId, MATCH_RESULT, myRequestDetails);
		assertLinkCount(2);

		List<MdmLink> links = myMdmLinkDaoSvc.findMdmLinksBySourceResource(patient);
		assertEquals(MdmLinkSourceEnum.MANUAL, links.get(0).getLinkSource());
		assertEquals(MdmMatchResultEnum.MATCH, links.get(0).getMatchResult());
	}

	@Test
	public void testCreateLinkWithNullMatchResult() {
		assertLinkCount(1);

		Patient patient = createPatient(buildPatientWithNameAndId("PatientGiven", "ID.PatientGiven.123"), true, false);
		StringType patientId = new StringType(patient.getIdElement().getValue());

		Patient sourcePatient = createPatient(buildPatientWithNameAndId("SourcePatientGiven", "ID.SourcePatientGiven.123"), true, false);
		StringType sourcePatientId = new StringType(sourcePatient.getIdElement().getValue());

		myMdmProvider.createLink(sourcePatientId, patientId, null, myRequestDetails);
		assertLinkCount(2);

		List<MdmLink> links = myMdmLinkDaoSvc.findMdmLinksBySourceResource(patient);
		assertEquals(MdmLinkSourceEnum.MANUAL, links.get(0).getLinkSource());
		assertEquals(MdmMatchResultEnum.MATCH, links.get(0).getMatchResult());
	}

	@Test
	public void testCreateLinkTwiceWithDifferentGoldenResourceAndMatchResult() {
		assertLinkCount(1);

		Patient patient = createPatient(buildPatientWithNameAndId("PatientGiven", "ID.PatientGiven.123"), true, false);
		StringType patientId = new StringType(patient.getIdElement().getValue());

		Patient sourcePatient = createPatient(buildPatientWithNameAndId("SourcePatientGiven", "ID.SourcePatientGiven.123"), true, false);
		StringType sourcePatientId = new StringType(sourcePatient.getIdElement().getValue());

		myMdmProvider.createLink(sourcePatientId, patientId, MATCH_RESULT, myRequestDetails);

		Patient sourcePatient2 = createPatient(buildPatientWithNameAndId("SourcePatientGiven2", "ID.SourcePatientGiven.123.2"), true, false);
		StringType sourcePatientId2 = new StringType(sourcePatient2.getIdElement().getValue());

		try {
			myMdmProvider.createLink(sourcePatientId2, patientId, MATCH_RESULT, myRequestDetails);
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), endsWith("Use $mdm-query-links to see more details."));
		}
	}

	@Test
	public void testCreateLinkTwiceWithDifferentGoldenResourceAndNoMatchResult() {
		assertLinkCount(1);

		Patient patient = createPatient(buildPatientWithNameAndId("PatientGiven", "ID.PatientGiven.123"), true, false);
		StringType patientId = new StringType(patient.getIdElement().getValue());

		Patient sourcePatient = createPatient(buildPatientWithNameAndId("SourcePatientGiven", "ID.SourcePatientGiven.123"), true, false);
		StringType sourcePatientId = new StringType(sourcePatient.getIdElement().getValue());

		myMdmProvider.createLink(sourcePatientId, patientId, MATCH_RESULT, myRequestDetails);

		Patient sourcePatient2 = createPatient(buildPatientWithNameAndId("SourcePatientGiven2", "ID.SourcePatientGiven.123.2"), true, false);
		StringType sourcePatientId2 = new StringType(sourcePatient2.getIdElement().getValue());

		myMdmProvider.createLink(sourcePatientId2, patientId, NO_MATCH_RESULT, myRequestDetails);

		assertLinkCount(3);
		List<MdmLink> links = myMdmLinkDaoSvc.findMdmLinksBySourceResource(patient);
		assertEquals(MdmLinkSourceEnum.MANUAL, links.get(1).getLinkSource());
		assertEquals(MdmMatchResultEnum.NO_MATCH, links.get(1).getMatchResult());
	}

	@Test
	public void testCreateExistentLink() {
		assertLinkCount(1);
		try {
			myMdmProvider.createLink(mySourcePatientId, myPatientId, MATCH_RESULT,myRequestDetails);
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), startsWith("Link already exists"));
		}
		assertLinkCount(1);
	}

	@Test
	public void testCreateIllegalResultPD() {
		try {
			myMdmProvider.createLink(mySourcePatientId, myPatientId, POSSIBLE_DUPLICATE_RESULT, myRequestDetails);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("$mdm-create-link illegal matchResult value 'POSSIBLE_DUPLICATE'.  Must be NO_MATCH, MATCH or POSSIBLE_MATCH", e.getMessage());
		}
	}

	@Test
	public void testCreateIllegalFirstArg() {
		try {
			myMdmProvider.createLink(new StringType(""), myPatientId, MATCH_RESULT, myRequestDetails);
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), endsWith(" must have form <resourceType>/<id> where <id> is the id of the resource"));
		}
	}

	@Test
	public void testCreateIllegalSecondArg() {
		try {
			myMdmProvider.createLink(myPatientId, new StringType(""), MATCH_RESULT, myRequestDetails);
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), endsWith(" must have form <resourceType>/<id>  where <id> is the id of the resource and <resourceType> is the type of the resource"));
		}
	}

	@Test
	public void testCreateStrangePatient() {
		Patient patient = createPatient();
		try {
			myMdmProvider.createLink(new StringType(patient.getIdElement().getValue()), myPatientId, MATCH_RESULT, myRequestDetails);
			fail();
		} catch (InvalidRequestException e) {
			String expectedMessage = myMessageHelper.getMessageForUnmanagedResource();
			assertEquals(expectedMessage, e.getMessage());
		}
	}

	@Test
	public void testExcludedGoldenResource() {
		Patient patient = new Patient();
		patient.getMeta().addTag().setSystem(MdmConstants.SYSTEM_MDM_MANAGED).setCode(MdmConstants.CODE_NO_MDM_MANAGED);
		createPatient(patient);
		try {
			myMdmProvider.createLink(mySourcePatientId, new StringType(patient.getIdElement().getValue()), MATCH_RESULT, myRequestDetails);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals(myMessageHelper.getMessageForUnsupportedSourceResource(), e.getMessage());
		}
	}
}
