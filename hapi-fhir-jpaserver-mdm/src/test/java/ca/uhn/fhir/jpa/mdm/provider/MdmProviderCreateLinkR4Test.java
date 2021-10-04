package ca.uhn.fhir.jpa.mdm.provider;
import ca.uhn.fhir.mdm.api.MdmConstants;
import ca.uhn.fhir.mdm.util.MessageHelper;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.endsWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class MdmProviderCreateLinkR4Test extends BaseLinkR4Test {

	@Autowired
	private MessageHelper myMessageHelper;

	@Test
	public void testCreateLinkIfLinkIsNotPresent() {
		assertLinkCount(1);

		Patient patient = createPatient(buildPatientWithNameAndId("PatientGiven", "ID.PatientGiven.123"), true, false);
		StringType patientId = new StringType(patient.getIdElement().getValue());

		Patient sourcePatient = createPatient(buildPatientWithNameAndId("SourcePatientGiven", "ID.SourcePatientGiven.123"), true, false);
		StringType sourcePatientId = new StringType(sourcePatient.getIdElement().getValue());

		myMdmProvider.createLink(sourcePatientId, patientId, myRequestDetails);
		assertLinkCount(2);
	}

	@Test
	public void testCreateLinkIfLinkIsPresent() {
		assertLinkCount(1);
		myMdmProvider.createLink(mySourcePatientId, myPatientId, myRequestDetails);
		assertLinkCount(1);
	}

	@Test
	public void testCreateIllegalFirstArg() {
		try {
			myMdmProvider.createLink(new StringType(""), myPatientId, myRequestDetails);
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), endsWith(" must have form <resourceType>/<id> where <id> is the id of the resource"));
		}
	}

	@Test
	public void testCreateIllegalSecondArg() {
		try {
			myMdmProvider.createLink(myPatientId, new StringType(""), myRequestDetails);
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), endsWith(" must have form <resourceType>/<id>  where <id> is the id of the resource and <resourceType> is the type of the resource"));
		}
	}

	@Test
	public void testCreateStrangePatient() {
		Patient patient = createPatient();
		try {
			myMdmProvider.createLink(new StringType(patient.getIdElement().getValue()), myPatientId, myRequestDetails);
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
			myMdmProvider.createLink(mySourcePatientId, new StringType(patient.getIdElement().getValue()), myRequestDetails);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals(myMessageHelper.getMessageForUnsupportedSourceResource(), e.getMessage());
		}
	}
}
