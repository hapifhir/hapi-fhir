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
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * {@link MdmMatchFinderSvcImpl} scores with the rules of the {@link MdmRuleSetEnum} it is given: the match only
 * rules for {@code $match}, and the linking rules for everything that creates MDM links.
 * <p>
 * The match only rules used here look for candidates by family name and score a shared family name as a
 * POSSIBLE_MATCH. The linking rules look for candidates by birthdate and identifier, so they never see Jane
 * Doe as a candidate for Paul Doe.
 * </p>
 */
// Created by Claude Opus 5.5
public class MdmMatchFinderSvcRuleSetR4Test extends BaseMdmR4Test {

	private static final String MATCH_ONLY_EID_SYSTEM = "http://company.io/fhir/NamingSystem/match-only-eid";

	@Autowired
	private IMdmMatchFinderSvc myMdmMatchFinderSvc;

	@Override
	@AfterEach
	public void after() throws IOException {
		myMdmSettings.setMatchOnlyMdmRules(null);
		super.after();
	}

	@Test
	void getMatchedTargets_matchOnlyRuleSet_scoresWithTheMatchOnlyRules() {
		myMdmSettings.setMatchOnlyMdmRules(loadMatchOnlyRules());
		Patient jane = createPatient(buildJanePatient().setActive(true));

		List<MatchedTarget> matches = myMdmMatchFinderSvc.getMatchedTargets(
				"Patient", buildPaulPatient(), RequestPartitionId.allPartitions(), MdmRuleSetEnum.MATCH_ONLY);

		assertThat(matches).hasSize(1);
		MatchedTarget match = matches.get(0);
		assertThat(versionlessId(match)).isEqualTo(versionlessId(jane));
		assertThat(match.getMatchResult().getMatchResultEnum()).isEqualTo(MdmMatchResultEnum.POSSIBLE_MATCH);
	}

	@Test
	void getMatchedTargets_linkRuleSet_ignoresTheMatchOnlyRules() {
		myMdmSettings.setMatchOnlyMdmRules(loadMatchOnlyRules());
		createPatient(buildJanePatient().setActive(true));

		List<MatchedTarget> matches = myMdmMatchFinderSvc.getMatchedTargets(
				"Patient", buildPaulPatient(), RequestPartitionId.allPartitions(), MdmRuleSetEnum.MATCH_AND_LINK);

		assertThat(matches).isEmpty();
	}

	@Test
	void getMatchedTargets_withoutRuleSet_usesTheLinkRules() {
		myMdmSettings.setMatchOnlyMdmRules(loadMatchOnlyRules());
		createPatient(buildJanePatient().setActive(true));

		List<MatchedTarget> matches = myMdmMatchFinderSvc.getMatchedTargets(
				"Patient", buildPaulPatient(), RequestPartitionId.allPartitions());

		assertThat(matches).isEmpty();
	}

	@Test
	void getMatchedTargets_noMatchOnlyRulesConfigured_matchOnlyRuleSetUsesTheLinkRules() {
		Patient jane = createPatient(buildJanePatient().setActive(true));

		List<MatchedTarget> matches = myMdmMatchFinderSvc.getMatchedTargets(
				"Patient", buildJanePatient(), RequestPartitionId.allPartitions(), MdmRuleSetEnum.MATCH_ONLY);

		// Given and family name agree, which the linking rules score as a MATCH
		assertThat(matches).hasSize(1);
		assertThat(versionlessId(matches.get(0))).isEqualTo(versionlessId(jane));
		assertThat(matches.get(0).getMatchResult().getMatchResultEnum()).isEqualTo(MdmMatchResultEnum.MATCH);
	}

	@Test
	void getMatchedTargets_matchOnlyRuleSet_eidMatchesOnTheMatchOnlyEidSystem() {
		myMdmSettings.setMatchOnlyMdmRules(loadMatchOnlyRulesWithEidSystem());
		// A different family name keeps Jane out of the match only candidate search, so only the EID can find her
		Patient jane = buildJanePatient().setActive(true);
		jane.getNameFirstRep().setFamily("Smith");
		jane = createPatient(addExternalEID(jane, MATCH_ONLY_EID_SYSTEM, "12345"));

		List<MatchedTarget> matches = myMdmMatchFinderSvc.getMatchedTargets(
				"Patient",
				addExternalEID(buildPaulPatient(), MATCH_ONLY_EID_SYSTEM, "12345"),
				RequestPartitionId.allPartitions(),
				MdmRuleSetEnum.MATCH_ONLY);

		assertThat(matches).hasSize(1);
		assertThat(versionlessId(matches.get(0))).isEqualTo(versionlessId(jane));
		assertThat(matches.get(0).getMatchResult().isEidMatch()).isTrue();
	}

	@Test
	void getMatchedTargets_matchOnlyRuleSet_doesNotEidMatchOnTheLinkEidSystem() {
		myMdmSettings.setMatchOnlyMdmRules(loadMatchOnlyRulesWithEidSystem());
		Patient jane = buildJanePatient().setActive(true);
		jane.getNameFirstRep().setFamily("Smith");
		createPatient(addExternalEID(jane, "12345"));

		// The EID is from the linking rules' EID system, which the match only rules do not declare
		List<MatchedTarget> matches = myMdmMatchFinderSvc.getMatchedTargets(
				"Patient",
				addExternalEID(buildPaulPatient(), "12345"),
				RequestPartitionId.allPartitions(),
				MdmRuleSetEnum.MATCH_ONLY);

		assertThat(matches).isEmpty();
	}

	@Test
	void updateMdmLinks_matchOnlyRulesWouldMatch_linksByTheLinkRulesOnly() {
		myMdmSettings.setMatchOnlyMdmRules(loadMatchOnlyRules());
		Patient jane = createPatientAndUpdateLinks(buildJanePatient().setActive(true));
		Patient paul = createPatientAndUpdateLinks(buildPaulPatient().setActive(true));

		// Each patient gets a Golden Resource of its own, and no link records the match only POSSIBLE_MATCH
		mdmAssertThat(paul).is_not_MATCH_to(jane);
		List<MdmLink> links = runInTransaction(() -> myMdmLinkDao.findAll());
		assertThat(links).hasSize(2).allSatisfy(link -> assertThat(link.getMatchResult())
				.isEqualTo(MdmMatchResultEnum.MATCH));
	}

	private MdmRulesJson loadMatchOnlyRules() {
		return JsonUtil.deserialize(
				ClasspathUtil.loadResource("mdm/mdm-rules-match-only.json"), MdmRulesJson.class);
	}

	private MdmRulesJson loadMatchOnlyRulesWithEidSystem() {
		MdmRulesJson rules = loadMatchOnlyRules();
		rules.setEidSystemsByResourceType(Map.of("Patient", List.of(MATCH_ONLY_EID_SYSTEM)));
		return rules;
	}

	private String versionlessId(MatchedTarget theMatch) {
		return theMatch.getTarget().getIdElement().toUnqualifiedVersionless().getValue();
	}

	private String versionlessId(Patient thePatient) {
		return thePatient.getIdElement().toUnqualifiedVersionless().getValue();
	}
}