package ca.uhn.fhir.jpa.mdm.svc;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.mdm.BaseMdmR4Test;
import ca.uhn.fhir.mdm.api.IMdmLink;
import ca.uhn.fhir.mdm.api.IMdmLinkUpdaterSvc;
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchOutcome;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.model.MdmTransactionContext;
import ca.uhn.fhir.mdm.util.MessageHelper;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MdmLinkUpdaterSvcImplIT extends BaseMdmR4Test {

	public static final String TEST_RSC_PATH = "mdm/mdm-link-update/";
	public static final String Patient_A_JSON_PATH = TEST_RSC_PATH + "patient-A.json/";
	public static final String Patient_B_JSON_PATH = TEST_RSC_PATH + "patient-B.json/";
	public static final String Patient_C_JSON_PATH = TEST_RSC_PATH + "patient-C.json/";

	@Autowired
	private IMdmLinkUpdaterSvc myMdmLinkUpdaterSvc;

	@Autowired
	private MdmResourceDaoSvc myMdmResourceDaoSvc;

	@Autowired
	private MessageHelper myMessageHelper;



	@Test
	void testUpdateLinkToMatchWhenAnotherLinkToDifferentGoldenExistsMustFail() throws Exception {
		// create Patient A -> MATCH GR A
		Patient patientA = createPatientFromJsonInputFile(Patient_A_JSON_PATH);
		// create Patient B -> MATCH GR B
		Patient patientB = createPatientFromJsonInputFile(Patient_B_JSON_PATH);

		Patient goldenA = getGoldenFor(patientA);
		Patient goldenB = getGoldenFor(patientB);

		// create Patient C -> no MATCH link. Only POSSIBLE_MATCH GR A and POSSIBLE_MATCH GR B and
		Patient patientC = createPatientFromJsonInputFileWithPossibleMatches( List.of(goldenA, goldenB) );

		MdmTransactionContext mdmTransactionContext = getPatientUpdateLinkContext();
		// update POSSIBLE_MATCH Patient C -> GR A to MATCH (should work OK)
		myMdmLinkUpdaterSvc.updateLink(goldenA, patientC, MdmMatchResultEnum.MATCH, mdmTransactionContext);

		// update POSSIBLE_MATCH Patient C -> GR B to MATCH (should throw exception)
		InvalidRequestException thrown = assertThrows(InvalidRequestException.class,
			() -> myMdmLinkUpdaterSvc.updateLink(goldenB, patientC, MdmMatchResultEnum.MATCH, mdmTransactionContext));

		String expectedExceptionMessage = Msg.code(2218) + myMessageHelper.getMessageForAlreadyAcceptedLink(goldenA, patientC);
		assertEquals(expectedExceptionMessage, thrown.getMessage());
	}

	@Test
	void testUpdateLinkToNoMatchWhenAnotherLinkToDifferentGoldenExistsShouldNotFail() throws Exception {
		// create Patient A -> MATCH GR A
		Patient patientA = createPatientFromJsonInputFile(Patient_A_JSON_PATH);
		// create Patient B -> MATCH GR B
		Patient patientB = createPatientFromJsonInputFile(Patient_B_JSON_PATH);

		Patient goldenA = getGoldenFor(patientA);
		Patient goldenB = getGoldenFor(patientB);

		// create Patient C -> no MATCH link. Only POSSIBLE_MATCH GR A and POSSIBLE_MATCH GR B
		Patient patientC = createPatientFromJsonInputFileWithPossibleMatches( List.of(goldenA, goldenB) );
		MdmTransactionContext mdmTransactionContext = getPatientUpdateLinkContext();

		// update POSSIBLE_MATCH Patient C -> GR A to MATCH (should work OK)
		myMdmLinkUpdaterSvc.updateLink(goldenA, patientC, MdmMatchResultEnum.MATCH, mdmTransactionContext);

		// update POSSIBLE_MATCH Patient C -> GR B to NO_MATCH (should work OK)
		myMdmLinkUpdaterSvc.updateLink(goldenB, patientC, MdmMatchResultEnum.NO_MATCH, mdmTransactionContext);
	}

	private Patient createPatientFromJsonInputFileWithPossibleMatches(List<Patient> theGoldens) throws Exception {
		Patient patient = createPatientFromJsonInputFile(Patient_C_JSON_PATH, false);
		for (Patient golden : theGoldens) {
			myMdmLinkDaoSvc.createOrUpdateLinkEntity(golden, patient, MdmMatchOutcome.POSSIBLE_MATCH, MdmLinkSourceEnum.AUTO, new MdmTransactionContext());
		}
		return patient;
	}


	private MdmTransactionContext getPatientUpdateLinkContext() {
		MdmTransactionContext ctx = new MdmTransactionContext();
		ctx.setRestOperation(MdmTransactionContext.OperationType.UPDATE_LINK);
		ctx.setResourceType("Patient");
		return ctx;
	}

	private Patient getGoldenFor(Patient thePatient) {
		Optional<? extends IMdmLink> patientALink = myMdmLinkDaoSvc.findMdmLinkBySource(thePatient);
		assertTrue(patientALink.isPresent());
		Patient golden = (Patient) myMdmResourceDaoSvc.readGoldenResourceByPid(patientALink.get().getGoldenResourcePersistenceId(), "Patient");
		assertNotNull(golden);
		return golden;
	}


	private Patient createPatientFromJsonInputFile(String thePath)  throws Exception {
		return createPatientFromJsonInputFile(thePath, true);
	}

	private Patient createPatientFromJsonInputFile(String thePath, boolean theCreateGolden)  throws Exception {
		File jsonInputUrl = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + thePath);
		String jsonPatient = Files.readString(Paths.get(jsonInputUrl.toURI()), StandardCharsets.UTF_8);

		Patient patient = (Patient) myFhirContext.newJsonParser().parseResource(jsonPatient);
		DaoMethodOutcome daoOutcome = myPatientDao.create(patient, new SystemRequestDetails());

		if (theCreateGolden) {
			myMdmMatchLinkSvc.updateMdmLinksForMdmSource(patient, createContextForCreate("Patient"));
		}

		return (Patient) daoOutcome.getResource();
	}
}
