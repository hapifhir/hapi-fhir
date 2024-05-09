package ca.uhn.fhir.jpa.mdm.svc;

import ca.uhn.fhir.jpa.mdm.BaseMdmR4Test;
import ca.uhn.fhir.mdm.api.IMdmSurvivorshipService;
import ca.uhn.fhir.mdm.model.MdmTransactionContext;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
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
		assertThat(p2.getIdentifier()).isEmpty();
		assertTrue(p2.getMeta().isEmpty());

		assertTrue(p1.getNameFirstRep().equalsDeep(p2.getNameFirstRep()));
		assertNull(p2.getBirthDate());
		assertThat(p2.getTelecom()).hasSize(p1.getTelecom().size());
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
		assertThat(p2.getIdentifier()).isNotEmpty();
		assertTrue(p2.getMeta().isEmpty());

		assertThat(p2.getName()).hasSize(2);
		assertEquals(p2Name, p2.getName().get(0).getNameAsSingleString());
		assertEquals(p1Name, p2.getName().get(1).getNameAsSingleString());
		assertNull(p2.getBirthDate());

		assertThat(p1.getTelecom()).hasSize(p1.getTelecom().size());
		assertTrue(p2.getTelecomFirstRep().equalsDeep(p1.getTelecomFirstRep()));
	}
}
