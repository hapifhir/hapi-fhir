package ca.uhn.fhir.jpa.mdm.svc;

import ca.uhn.fhir.jpa.mdm.BaseMdmR4Test;
import ca.uhn.fhir.mdm.api.IMdmSurvivorshipService;
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchOutcome;
import ca.uhn.fhir.mdm.model.MdmTransactionContext;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MdmSurvivorshipSvcImplIT extends BaseMdmR4Test {

	@Autowired
	private IMdmSurvivorshipService myMdmSurvivorshipService;

	@Test
	public void testRulesOnCreate() {
		Patient p1 = buildFrankPatient();
		Patient p2 = new Patient();

		myMdmSurvivorshipService.applySurvivorshipRulesToGoldenResource(p1, p2, new MdmTransactionContext(MdmTransactionContext.OperationType.CREATE_RESOURCE));

		assertFalse(p2.hasIdElement());
		assertTrue(p2.getIdentifier().isEmpty());
		assertTrue(p2.getMeta().isEmpty());

		assertTrue(p1.getNameFirstRep().equalsDeep(p2.getNameFirstRep()));
		assertNull(p2.getBirthDate());
		assertEquals(p1.getTelecom().size(), p2.getTelecom().size());
		assertTrue(p2.getTelecomFirstRep().equalsDeep(p1.getTelecomFirstRep()));
	}

	@Test
	public void testRulesOnMerge() {
		Patient p1 = buildFrankPatient();
		String p1Name = p1.getNameFirstRep().getNameAsSingleString();
		Patient p2 = buildPaulPatient();
		String p2Name = p2.getNameFirstRep().getNameAsSingleString();

		myMdmSurvivorshipService.applySurvivorshipRulesToGoldenResource(p1, p2, new MdmTransactionContext(MdmTransactionContext.OperationType.MERGE_GOLDEN_RESOURCES));

		assertFalse(p2.hasIdElement());
		assertFalse(p2.getIdentifier().isEmpty());
		assertTrue(p2.getMeta().isEmpty());

		assertEquals(2, p2.getName().size());
		assertEquals(p2Name, p2.getName().get(0).getNameAsSingleString());
		assertEquals(p1Name, p2.getName().get(1).getNameAsSingleString());
		assertNull(p2.getBirthDate());

		assertEquals(p1.getTelecom().size(), p1.getTelecom().size());
		assertTrue(p2.getTelecomFirstRep().equalsDeep(p1.getTelecomFirstRep()));
	}

	@Test
	public void matchingPatientsWith_NON_Numeric_Ids_matches_doesNotThrow_NumberFormatException() {
		final Patient frankPatient1 = buildFrankPatient();
		frankPatient1.setId("patA");
		myPatientDao.update(frankPatient1, new SystemRequestDetails());
		final Patient frankPatient2 = buildFrankPatient();
		frankPatient2.setId("patB");
		myPatientDao.update(frankPatient2, new SystemRequestDetails());
		final Patient goldenPatient = buildFrankPatient();
		myPatientDao.create(goldenPatient, new SystemRequestDetails());

		myMdmLinkDaoSvc.createOrUpdateLinkEntity(goldenPatient, frankPatient1, MdmMatchOutcome.NEW_GOLDEN_RESOURCE_MATCH, MdmLinkSourceEnum.MANUAL, createContextForCreate("Patient"));
		myMdmLinkDaoSvc.createOrUpdateLinkEntity(goldenPatient, frankPatient2, MdmMatchOutcome.NEW_GOLDEN_RESOURCE_MATCH, MdmLinkSourceEnum.MANUAL, createContextForCreate("Patient"));

		myMdmSurvivorshipService.rebuildGoldenResourceWithSurvivorshipRules(goldenPatient, new MdmTransactionContext(MdmTransactionContext.OperationType.UPDATE_LINK));
	}
}
