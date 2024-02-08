package ca.uhn.fhir.jpa.mdm.svc;

import ca.uhn.fhir.jpa.mdm.BaseMdmR4Test;
import ca.uhn.fhir.mdm.api.IMdmSurvivorshipService;
import ca.uhn.fhir.mdm.model.MdmTransactionContext;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class MdmSurvivorshipSvcImplIT extends BaseMdmR4Test {

	@Autowired
	private IMdmSurvivorshipService myMdmSurvivorshipService;

	@Test
	public void testRulesOnCreate() {
		Patient p1 = buildFrankPatient();
		Patient p2 = new Patient();

		myMdmSurvivorshipService.applySurvivorshipRulesToGoldenResource(p1, p2, new MdmTransactionContext(MdmTransactionContext.OperationType.CREATE_RESOURCE));

		assertThat(p2.hasIdElement()).isFalse();
		assertThat(p2.getIdentifier().isEmpty()).isTrue();
		assertThat(p2.getMeta().isEmpty()).isTrue();

		assertThat(p1.getNameFirstRep().equalsDeep(p2.getNameFirstRep())).isTrue();
		assertThat(p2.getBirthDate()).isNull();
		assertThat(p2.getTelecom().size()).isEqualTo(p1.getTelecom().size());
		assertThat(p2.getTelecomFirstRep().equalsDeep(p1.getTelecomFirstRep())).isTrue();
	}

	@Test
	public void testRulesOnMerge() {
		Patient p1 = buildFrankPatient();
		String p1Name = p1.getNameFirstRep().getNameAsSingleString();
		Patient p2 = buildPaulPatient();
		String p2Name = p2.getNameFirstRep().getNameAsSingleString();

		myMdmSurvivorshipService.applySurvivorshipRulesToGoldenResource(p1, p2, new MdmTransactionContext(MdmTransactionContext.OperationType.MERGE_GOLDEN_RESOURCES));

		assertThat(p2.hasIdElement()).isFalse();
		assertThat(p2.getIdentifier().isEmpty()).isFalse();
		assertThat(p2.getMeta().isEmpty()).isTrue();

		assertThat(p2.getName().size()).isEqualTo(2);
		assertThat(p2.getName().get(0).getNameAsSingleString()).isEqualTo(p2Name);
		assertThat(p2.getName().get(1).getNameAsSingleString()).isEqualTo(p1Name);
		assertThat(p2.getBirthDate()).isNull();

		assertThat(p1.getTelecom().size()).isEqualTo(p1.getTelecom().size());
		assertThat(p2.getTelecomFirstRep().equalsDeep(p1.getTelecomFirstRep())).isTrue();
	}
}
