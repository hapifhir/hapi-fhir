package ca.uhn.fhir.jpa.mdm.svc;

import ca.uhn.fhir.jpa.mdm.BaseMdmR4Test;
import ca.uhn.fhir.mdm.api.IMdmLinkUpdaterSvc;
import ca.uhn.fhir.mdm.model.MdmTransactionContext;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static ca.uhn.fhir.mdm.api.MdmMatchResultEnum.MATCH;
import static ca.uhn.fhir.mdm.api.MdmMatchResultEnum.NO_MATCH;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class MdmLinkUpdaterSvcImplTest extends BaseMdmR4Test {
	@Autowired
	private IMdmLinkUpdaterSvc myMdmLinkUpdaterSvc;

	@Test
	public void testUpdateLinkNoMatch() {
		// setup

		Patient jane = createPatientAndUpdateLinks(addExternalEID(buildJanePatient(), EID_1));
		Patient originalJaneGolden = getGoldenResourceFromTargetResource(jane);

		MdmTransactionContext mdmCtx = buildUpdateLinkMdmTransactionContext();

		myMdmLinkUpdaterSvc.updateLink(originalJaneGolden, jane, NO_MATCH, mdmCtx);
		Patient newJaneGolden = getGoldenResourceFromTargetResource(jane);

		assertNotEquals(newJaneGolden.getId(), originalJaneGolden.getId());

		assertLinkCount(2);

		assertLinksMatchResult(NO_MATCH, MATCH);
		assertLinksCreatedNewResource(true, true);
		assertLinksMatchedByEid(false, false);
	}
}
