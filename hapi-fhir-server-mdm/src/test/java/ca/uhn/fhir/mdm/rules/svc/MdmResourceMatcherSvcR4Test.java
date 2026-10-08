package ca.uhn.fhir.mdm.rules.svc;

import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.mdm.api.MdmMatchOutcome;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.api.MdmRuleSetEnum;
import ca.uhn.fhir.mdm.rules.config.MdmRuleValidator;
import ca.uhn.fhir.mdm.rules.config.MdmSettings;
import ca.uhn.fhir.mdm.rules.json.MdmRulesJson;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MdmResourceMatcherSvcR4Test extends BaseMdmRulesR4Test {
	private MdmResourceMatcherSvc myMdmResourceMatcherSvc;
	private Patient myJohn;
	private Patient myJohny;

	@Override
	@BeforeEach
	public void before() {
		super.before();

		when(mySearchParamRetriever.getActiveSearchParam(eq("Patient"), eq("birthdate"), any())).thenReturn(mock(RuntimeSearchParam.class));
		when(mySearchParamRetriever.getActiveSearchParam(eq("Patient"), eq("identifier"), any())).thenReturn(mock(RuntimeSearchParam.class));
		when(mySearchParamRetriever.getActiveSearchParam(eq("Practitioner"), eq("identifier"), any())).thenReturn(mock(RuntimeSearchParam.class));
		when(mySearchParamRetriever.getActiveSearchParam(eq("Medication"), eq("identifier"), any())).thenReturn(mock(RuntimeSearchParam.class));
		when(mySearchParamRetriever.getActiveSearchParam(eq("Patient"), eq("active"), any())).thenReturn(mock(RuntimeSearchParam.class));

		myMdmResourceMatcherSvc = buildMatcher(buildActiveBirthdateIdRules());

		myJohn = buildJohn();
		myJohny = buildJohny();
	}

	@Test
	public void testCompareFirstNameMatch() {
		MdmMatchOutcome result = myMdmResourceMatcherSvc.match(myJohn, myJohny);
		assertMatchResult(MdmMatchResultEnum.POSSIBLE_MATCH, 1L, 0.816, false, false, result);
	}

	@Test
	public void testCompareBothNamesMatch() {
		myJohn.addName().setFamily("Smith");
		myJohny.addName().setFamily("Smith");
		MdmMatchOutcome result = myMdmResourceMatcherSvc.match(myJohn, myJohny);
		assertMatchResult(MdmMatchResultEnum.MATCH, 3L, 1.816, false, false, result);
	}

	@Test
	public void testMatchResult() {
		assertMatchResult(MdmMatchResultEnum.POSSIBLE_MATCH, 1L, 0.816, false, false, myMdmResourceMatcherSvc.getMatchResult(myJohn, myJohny));
		myJohn.addName().setFamily("Smith");
		myJohny.addName().setFamily("Smith");
		assertMatchResult(MdmMatchResultEnum.MATCH, 3L, 1.816, false, false, myMdmResourceMatcherSvc.getMatchResult(myJohn, myJohny));
		Patient patient3 = new Patient();
		patient3.setId("Patient/3");
		patient3.addName().addGiven("Henry");
		assertMatchResult(MdmMatchResultEnum.NO_MATCH, 0L, 0.0, false, false, myMdmResourceMatcherSvc.getMatchResult(myJohn, patient3));
	}

	@Test
	public void testScoreOnlySummedWhenMatchFieldMatches() {
		MdmMatchOutcome outcome = myMdmResourceMatcherSvc.getMatchResult(myJohn, myJohny);
		assertMatchResult(MdmMatchResultEnum.POSSIBLE_MATCH, 1L, 0.816, false, false, outcome);

		myJohn.addName().setFamily("Smith");
		myJohny.addName().setFamily("htims");
		outcome = myMdmResourceMatcherSvc.getMatchResult(myJohn, myJohny);
		assertMatchResult(MdmMatchResultEnum.POSSIBLE_MATCH, 1L, 0.816, false, false, outcome);

		myJohny.addName().setFamily("Smith");
		outcome = myMdmResourceMatcherSvc.getMatchResult(myJohn, myJohny);
		assertMatchResult(MdmMatchResultEnum.MATCH, 3L, 1.816, false, false, outcome);
	}

	@Test
	void testMatchOnlyRuleSet_scoresWithTheMatchOnlyRules() {
		MdmSettings mdmSettings = buildMdmSettings().setMatchOnlyMdmRules(buildGivenNameIsMatchRules());
		MdmResourceMatcherSvc matcher = buildMatcher(mdmSettings);

		assertMatch(MdmMatchResultEnum.POSSIBLE_MATCH, matcher.getMatchResult(myJohn, myJohny, MdmRuleSetEnum.MATCH_AND_LINK));
		assertMatch(MdmMatchResultEnum.MATCH, matcher.getMatchResult(myJohn, myJohny, MdmRuleSetEnum.MATCH_ONLY));
	}

	@Test
	void testMatchOnlyRuleSet_matchOnlyRulesReplaced_scoresWithTheNewRules() {
		MdmSettings mdmSettings = buildMdmSettings();
		MdmResourceMatcherSvc matcher = buildMatcher(mdmSettings);
		// No match only rules: the linking rules score the given name alone as a POSSIBLE_MATCH
		assertMatch(MdmMatchResultEnum.POSSIBLE_MATCH, matcher.getMatchResult(myJohn, myJohny, MdmRuleSetEnum.MATCH_ONLY));

		mdmSettings.setMatchOnlyMdmRules(buildGivenNameIsMatchRules());

		assertMatch(MdmMatchResultEnum.MATCH, matcher.getMatchResult(myJohn, myJohny, MdmRuleSetEnum.MATCH_ONLY));
	}

	private MdmSettings buildMdmSettings() {
		return new MdmSettings(new MdmRuleValidator(ourFhirContext, mySearchParamRetriever, myIMatcherFactory, mySimilarityFactory))
			.setMdmRules(buildActiveBirthdateIdRules());
	}

	private MdmResourceMatcherSvc buildMatcher(MdmSettings theMdmSettings) {
		return new MdmResourceMatcherSvc(ourFhirContext, myIMatcherFactory, mySimilarityFactory, theMdmSettings);
	}

	private MdmRulesJson buildGivenNameIsMatchRules() {
		MdmRulesJson rules = buildActiveBirthdateIdRules();
		rules.putMatchResult(PATIENT_GIVEN, MdmMatchResultEnum.MATCH);
		return rules;
	}
}
