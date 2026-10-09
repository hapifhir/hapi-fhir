package ca.uhn.fhir.jpa.mdm.svc;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.jpa.mdm.BaseMdmR4Test;
import ca.uhn.fhir.mdm.api.IMdmMatchFinderSvc;
import ca.uhn.fhir.mdm.api.MatchedTarget;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.api.MdmRuleSetEnum;
import ca.uhn.fhir.mdm.model.MdmTransactionContext;
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
		Patient janeDoe = createActivePatient(buildJanePatient(), "Doe");
		createActivePatient(buildPaulPatient(), "Smith");

		List<MatchedTarget> matches = myMdmMatchFinderSvc.getMatchedTargets(
				"Patient", buildPaulPatient(), RequestPartitionId.allPartitions(), MdmRuleSetEnum.MATCH_ONLY,
				new MdmTransactionContext());

		assertSingleMatch(matches, janeDoe, MdmMatchResultEnum.POSSIBLE_MATCH);
	}

	@Test
	void getMatchedTargets_linkRuleSet_scoresWithTheLinkRules() {
		myMdmSettings.setMatchOnlyMdmRules(loadMatchOnlyRules());
		createActivePatient(buildJanePatient(), "Doe");
		Patient paulSmith = createActivePatient(buildPaulPatient(), "Smith");

		List<MatchedTarget> matches = myMdmMatchFinderSvc.getMatchedTargets(
				"Patient", buildPaulPatient(), RequestPartitionId.allPartitions(), MdmRuleSetEnum.MATCH_AND_LINK,
				new MdmTransactionContext());

		assertSingleMatch(matches, paulSmith, MdmMatchResultEnum.POSSIBLE_MATCH);
	}

	@Test
	void getMatchedTargets_withoutRuleSet_usesTheLinkRules() {
		myMdmSettings.setMatchOnlyMdmRules(loadMatchOnlyRules());
		createActivePatient(buildJanePatient(), "Doe");
		Patient paulSmith = createActivePatient(buildPaulPatient(), "Smith");

		List<MatchedTarget> matches = myMdmMatchFinderSvc.getMatchedTargets(
				"Patient", buildPaulPatient(), RequestPartitionId.allPartitions(), new MdmTransactionContext());

		assertSingleMatch(matches, paulSmith, MdmMatchResultEnum.POSSIBLE_MATCH);
	}

	@Test
	void getMatchedTargets_noMatchOnlyRulesConfigured_matchOnlyRuleSetUsesTheLinkRules() {
		createActivePatient(buildJanePatient(), "Doe");
		Patient paulSmith = createActivePatient(buildPaulPatient(), "Smith");

		List<MatchedTarget> matches = myMdmMatchFinderSvc.getMatchedTargets(
				"Patient", buildPaulPatient(), RequestPartitionId.allPartitions(), MdmRuleSetEnum.MATCH_ONLY,
				new MdmTransactionContext());

		assertSingleMatch(matches, paulSmith, MdmMatchResultEnum.POSSIBLE_MATCH);
	}

	@Test
	void getMatchedTargets_matchOnlyRuleSet_eidMatchesOnTheMatchOnlyEidSystem() {
		myMdmSettings.setMatchOnlyMdmRules(loadMatchOnlyRulesWithEidSystem());
		// Same EID value in both EID systems: only the match only EID system matches
		Patient jane =
				createActivePatient(addExternalEID(buildJanePatient(), MATCH_ONLY_EID_SYSTEM, "12345"), "Smith");
		createActivePatient(addExternalEID(buildFrankPatient(), "12345"), "Smith");

		List<MatchedTarget> matches = myMdmMatchFinderSvc.getMatchedTargets(
				"Patient",
				addExternalEID(buildPaulPatient(), MATCH_ONLY_EID_SYSTEM, "12345"),
				RequestPartitionId.allPartitions(),
				MdmRuleSetEnum.MATCH_ONLY,
				new MdmTransactionContext());

		assertThat(matches).hasSize(1);
		assertThat(versionlessId(matches.get(0))).isEqualTo(versionlessId(jane));
		assertThat(matches.get(0).getMatchResult().isEidMatch()).isTrue();
	}

	@Test
	void getMatchedTargets_matchOnlyRuleSet_doesNotEidMatchOnTheLinkEidSystem() {
		myMdmSettings.setMatchOnlyMdmRules(loadMatchOnlyRulesWithEidSystem());
		Patient janeDoe = createActivePatient(buildJanePatient(), "Doe");
		createActivePatient(addExternalEID(buildFrankPatient(), "12345"), "Smith");

		// An EID from the linking EID system is ignored, so matching falls back to the match only candidate search
		List<MatchedTarget> matches = myMdmMatchFinderSvc.getMatchedTargets(
				"Patient",
				addExternalEID(buildPaulPatient(), "12345"),
				RequestPartitionId.allPartitions(),
				MdmRuleSetEnum.MATCH_ONLY,
				new MdmTransactionContext());

		assertSingleMatch(matches, janeDoe, MdmMatchResultEnum.POSSIBLE_MATCH);
	}

	@Test
	void updateMdmLinks_matchOnlyRulesWouldMatch_linksByTheLinkRulesOnly() {
		myMdmSettings.setMatchOnlyMdmRules(loadMatchOnlyRules());
		Patient janeDoe = createActivePatient(buildJanePatient(), "Doe");
		myMdmMatchLinkSvc.updateMdmLinksForMdmSource(janeDoe, createContextForCreate("Patient"));
		Patient paulSmith = createActivePatient(buildPaulPatient(), "Smith");
		myMdmMatchLinkSvc.updateMdmLinksForMdmSource(paulSmith, createContextForCreate("Patient"));

		Patient paulDoe = createPatientAndUpdateLinks(buildPaulPatient().setActive(true));

		// Only the patient found by the linking rules is linked as a POSSIBLE_MATCH
		mdmAssertThat(paulDoe).is_POSSIBLE_MATCH_to(paulSmith);
		List<MdmLink> links = runInTransaction(() -> myMdmLinkDao.findAll());
		assertThat(links)
				.extracting(MdmLink::getMatchResult)
				.containsExactlyInAnyOrder(
						MdmMatchResultEnum.MATCH, MdmMatchResultEnum.MATCH, MdmMatchResultEnum.POSSIBLE_MATCH);
	}

	private Patient createActivePatient(Patient thePatient, String theFamily) {
		thePatient.setActive(true).getNameFirstRep().setFamily(theFamily);
		return createPatient(thePatient);
	}

	private void assertSingleMatch(
			List<MatchedTarget> theMatches, Patient theExpectedPatient, MdmMatchResultEnum theExpectedResult) {
		assertThat(theMatches).hasSize(1);
		assertThat(versionlessId(theMatches.get(0))).isEqualTo(versionlessId(theExpectedPatient));
		assertThat(theMatches.get(0).getMatchResult().getMatchResultEnum()).isEqualTo(theExpectedResult);
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