package ca.uhn.fhir.jpa.mdm.svc;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.jpa.mdm.BaseMdmR4Test;
import ca.uhn.fhir.mdm.api.IMdmMatchFinderSvc;
import ca.uhn.fhir.mdm.api.MatchedTarget;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.api.MdmRuleSetEnum;
import ca.uhn.fhir.mdm.rules.json.MdmRulesJson;
import ca.uhn.fhir.util.ClasspathUtil;
import ca.uhn.fhir.util.JsonUtil;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * {@link MdmMatchFinderSvcImpl} scores with the rules of the {@link MdmRuleSetEnum} it is given: the match operation
 * rules for {@code $match}, and the linking rules for everything that creates MDM links.
 * <p>
 * The match operation rules used here look for candidates by family name and score a shared family name as a
 * POSSIBLE_MATCH. The linking rules look for candidates by birthdate and identifier, so they never see Jane
 * Doe as a candidate for Paul Doe.
 * </p>
 */
// Created by Claude Opus 5.5
public class MdmMatchFinderSvcRuleSetR4Test extends BaseMdmR4Test {

	@Autowired
	private IMdmMatchFinderSvc myMdmMatchFinderSvc;

	@Override
	@AfterEach
	public void after() throws IOException {
		myMdmSettings.setMatchOperationMdmRules(null);
		super.after();
	}

	@Test
	void getMatchedTargets_matchOperationRuleSet_scoresWithTheMatchOperationRules() {
		myMdmSettings.setMatchOperationMdmRules(loadMatchOperationRules());
		Patient jane = createPatient(buildJanePatient().setActive(true));

		List<MatchedTarget> matches = myMdmMatchFinderSvc.getMatchedTargets(
				"Patient", buildPaulPatient(), RequestPartitionId.allPartitions(), MdmRuleSetEnum.MATCH_OPERATION);

		assertThat(matches).hasSize(1);
		MatchedTarget match = matches.get(0);
		assertThat(versionlessId(match)).isEqualTo(versionlessId(jane));
		assertThat(match.getMatchResult().getMatchResultEnum()).isEqualTo(MdmMatchResultEnum.POSSIBLE_MATCH);
	}

	@Test
	void getMatchedTargets_linkRuleSet_ignoresTheMatchOperationRules() {
		myMdmSettings.setMatchOperationMdmRules(loadMatchOperationRules());
		createPatient(buildJanePatient().setActive(true));

		List<MatchedTarget> matches = myMdmMatchFinderSvc.getMatchedTargets(
				"Patient", buildPaulPatient(), RequestPartitionId.allPartitions(), MdmRuleSetEnum.LINK);

		assertThat(matches).isEmpty();
	}

	@Test
	void getMatchedTargets_withoutRuleSet_usesTheLinkRules() {
		myMdmSettings.setMatchOperationMdmRules(loadMatchOperationRules());
		createPatient(buildJanePatient().setActive(true));

		List<MatchedTarget> matches = myMdmMatchFinderSvc.getMatchedTargets(
				"Patient", buildPaulPatient(), RequestPartitionId.allPartitions());

		assertThat(matches).isEmpty();
	}

	@Test
	void getMatchedTargets_noMatchOperationRulesConfigured_matchOperationRuleSetUsesTheLinkRules() {
		Patient jane = createPatient(buildJanePatient().setActive(true));

		List<MatchedTarget> matches = myMdmMatchFinderSvc.getMatchedTargets(
				"Patient", buildJanePatient(), RequestPartitionId.allPartitions(), MdmRuleSetEnum.MATCH_OPERATION);

		// Given and family name agree, which the linking rules score as a MATCH
		assertThat(matches).hasSize(1);
		assertThat(versionlessId(matches.get(0))).isEqualTo(versionlessId(jane));
		assertThat(matches.get(0).getMatchResult().getMatchResultEnum()).isEqualTo(MdmMatchResultEnum.MATCH);
	}

	@Test
	void updateMdmLinks_matchOperationRulesWouldMatch_linksByTheLinkRulesOnly() {
		myMdmSettings.setMatchOperationMdmRules(loadMatchOperationRules());
		Patient jane = createPatientAndUpdateLinks(buildJanePatient().setActive(true));
		Patient paul = createPatientAndUpdateLinks(buildPaulPatient().setActive(true));

		// Each patient gets a Golden Resource of its own, and no link records the match operation POSSIBLE_MATCH
		mdmAssertThat(paul).is_not_MATCH_to(jane);
		List<MdmLink> links = runInTransaction(() -> myMdmLinkDao.findAll());
		assertThat(links).hasSize(2).allSatisfy(link -> assertThat(link.getMatchResult())
				.isEqualTo(MdmMatchResultEnum.MATCH));
	}

	private MdmRulesJson loadMatchOperationRules() {
		return JsonUtil.deserialize(
				ClasspathUtil.loadResource("mdm/mdm-rules-match-operation.json"), MdmRulesJson.class);
	}

	private String versionlessId(MatchedTarget theMatch) {
		return theMatch.getTarget().getIdElement().toUnqualifiedVersionless().getValue();
	}

	private String versionlessId(Patient thePatient) {
		return thePatient.getIdElement().toUnqualifiedVersionless().getValue();
	}
}