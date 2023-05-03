package ca.uhn.fhir.jpa.mdm.svc;

import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.jpa.mdm.BaseMdmR4Test;
import ca.uhn.fhir.mdm.api.IMdmLinkUpdaterSvc;
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchOutcome;
import ca.uhn.fhir.mdm.model.MdmTransactionContext;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;

import static ca.uhn.fhir.mdm.api.MdmMatchResultEnum.MATCH;
import static ca.uhn.fhir.mdm.api.MdmMatchResultEnum.NO_MATCH;
import static ca.uhn.fhir.mdm.api.MdmMatchResultEnum.POSSIBLE_MATCH;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class MdmLinkUpdaterSvcImplTest extends BaseMdmR4Test {
	@Autowired
	private IMdmLinkUpdaterSvc myMdmLinkUpdaterSvc;

	@Override
	@AfterEach
	public void after() throws IOException {
		super.after();
		myMdmSettings.getMdmRules().setVersion("1");
	}

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

	@Test
	public void testUpdateLinkPossibleMatchSavesNormalizedScore() {
		final Patient goldenPatient = createGoldenPatient(buildJanePatient());
		final Patient patient1 = createPatient(buildJanePatient());
		buildUpdateLinkMdmTransactionContext();

		MdmMatchOutcome matchOutcome = new MdmMatchOutcome(61L, 5.0).setMdmRuleCount(6).setMatchResultEnum(POSSIBLE_MATCH);
		myMdmLinkDaoSvc.createOrUpdateLinkEntity(goldenPatient, patient1, matchOutcome, MdmLinkSourceEnum.MANUAL, createContextForCreate("Patient"));

		final List<MdmLink> targets = myMdmLinkDaoSvc.findMdmLinksByGoldenResource(goldenPatient);
		assertFalse(targets.isEmpty());
		assertEquals(1, targets.size());
		final MdmLink mdmLink = targets.get(0);

		assertEquals(matchOutcome.getNormalizedScore(), mdmLink.getScore());
	}

	@Test
	public void testUpdateLinkMatchAfterVersionChange() {
		myMdmSettings.getMdmRules().setVersion("1");

		final Patient goldenPatient = createGoldenPatient(buildJanePatient());
		final Patient patient1 = createPatient(buildJanePatient());

		final MdmTransactionContext mdmCtx = buildUpdateLinkMdmTransactionContext();

		myMdmLinkDaoSvc.createOrUpdateLinkEntity(goldenPatient, patient1, MdmMatchOutcome.NO_MATCH, MdmLinkSourceEnum.MANUAL, createContextForCreate("Patient"));

		myMdmSettings.getMdmRules().setVersion("2");

		myMdmLinkUpdaterSvc.updateLink(goldenPatient, patient1, MATCH, mdmCtx);

		final List<MdmLink> targets = myMdmLinkDaoSvc.findMdmLinksByGoldenResource(goldenPatient);
		assertFalse(targets.isEmpty());
		assertEquals(1, targets.size());

		final MdmLink mdmLink = targets.get(0);

		assertEquals(patient1.getIdElement().toVersionless().getIdPart(), mdmLink.getSourcePersistenceId().getId().toString());
	}
}
