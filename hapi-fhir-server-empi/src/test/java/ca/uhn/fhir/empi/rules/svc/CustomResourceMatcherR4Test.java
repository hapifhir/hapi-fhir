package ca.uhn.fhir.empi.rules.svc;

import ca.uhn.fhir.empi.BaseR4Test;
import ca.uhn.fhir.empi.api.EmpiMatchResultEnum;
import ca.uhn.fhir.empi.rules.json.EmpiFieldMatchJson;
import ca.uhn.fhir.empi.rules.json.EmpiRulesJson;
import ca.uhn.fhir.empi.rules.metric.EmpiMetricEnum;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class CustomResourceMatcherR4Test extends BaseR4Test {

	public static final String FIELD_EXACT_MATCH_NAME = EmpiMetricEnum.NAME_ANY_ORDER.name();
	private static Patient ourJohnHenry;
	private static Patient ourJohnHENRY;
	private static Patient ourJaneHenry;
	private static Patient ourJohnSmith;
	private static Patient ourJohnBillyHenry;
	private static Patient ourBillyJohnHenry;
	private static Patient ourHenryJohn;
	private static Patient ourHenryJOHN;

	@Test
	public void testExactNameAnyOrder() {
		EmpiResourceMatcherSvc nameAnyOrderMatcher = buildMatcher(buildNameRules(EmpiMetricEnum.NAME_ANY_ORDER, true));
		assertMatch(EmpiMatchResultEnum.MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourJohnHenry));
		assertMatch(EmpiMatchResultEnum.MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourHenryJohn));
		assertMatch(EmpiMatchResultEnum.NO_MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourHenryJOHN));
		assertMatch(EmpiMatchResultEnum.NO_MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourJohnHENRY));
		assertMatch(EmpiMatchResultEnum.NO_MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourJaneHenry));
		assertMatch(EmpiMatchResultEnum.NO_MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourJohnSmith));
		assertMatch(EmpiMatchResultEnum.MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourJohnBillyHenry));
		assertMatch(EmpiMatchResultEnum.MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourBillyJohnHenry));
	}

	@Test
	public void testNormalizedNameAnyOrder() {
		EmpiResourceMatcherSvc nameAnyOrderMatcher = buildMatcher(buildNameRules(EmpiMetricEnum.NAME_ANY_ORDER, false));
		assertMatch(EmpiMatchResultEnum.MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourJohnHenry));
		assertMatch(EmpiMatchResultEnum.MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourHenryJohn));
		assertMatch(EmpiMatchResultEnum.MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourHenryJOHN));
		assertMatch(EmpiMatchResultEnum.MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourJohnHENRY));
		assertMatch(EmpiMatchResultEnum.NO_MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourJaneHenry));
		assertMatch(EmpiMatchResultEnum.NO_MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourJohnSmith));
		assertMatch(EmpiMatchResultEnum.MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourJohnBillyHenry));
		assertMatch(EmpiMatchResultEnum.MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourBillyJohnHenry));
	}

	@Test
	public void testExactNameFirstAndLast() {
		EmpiResourceMatcherSvc nameAnyOrderMatcher = buildMatcher(buildNameRules(EmpiMetricEnum.NAME_FIRST_AND_LAST, true));
		assertMatch(EmpiMatchResultEnum.MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourJohnHenry));
		assertMatchResult(EmpiMatchResultEnum.MATCH, 1L, 1.0, false, false, nameAnyOrderMatcher.match(ourJohnHenry, ourJohnHenry));
		assertMatch(EmpiMatchResultEnum.NO_MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourHenryJohn));
		assertMatch(EmpiMatchResultEnum.NO_MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourHenryJOHN));
		assertMatch(EmpiMatchResultEnum.NO_MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourJohnHENRY));
		assertMatch(EmpiMatchResultEnum.NO_MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourJaneHenry));
		assertMatch(EmpiMatchResultEnum.NO_MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourJohnSmith));
		assertMatch(EmpiMatchResultEnum.MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourJohnBillyHenry));
		assertMatch(EmpiMatchResultEnum.MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourBillyJohnHenry));
	}

	@Test
	public void testNormalizedNameFirstAndLast() {
		EmpiResourceMatcherSvc nameAnyOrderMatcher = buildMatcher(buildNameRules(EmpiMetricEnum.NAME_FIRST_AND_LAST, false));
		assertMatch(EmpiMatchResultEnum.MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourJohnHenry));
		assertMatch(EmpiMatchResultEnum.NO_MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourHenryJohn));
		assertMatch(EmpiMatchResultEnum.NO_MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourHenryJOHN));
		assertMatch(EmpiMatchResultEnum.MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourJohnHENRY));
		assertMatch(EmpiMatchResultEnum.NO_MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourJaneHenry));
		assertMatch(EmpiMatchResultEnum.NO_MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourJohnSmith));
		assertMatch(EmpiMatchResultEnum.MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourJohnBillyHenry));
		assertMatch(EmpiMatchResultEnum.MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourBillyJohnHenry));
	}

	private EmpiRulesJson buildNameRules(EmpiMetricEnum theExactNameAnyOrder, boolean theExact) {
		EmpiFieldMatchJson nameAnyOrderFieldMatch = new EmpiFieldMatchJson()
			.setName(FIELD_EXACT_MATCH_NAME)
			.setResourceType("Patient")
			.setResourcePath("name")
			.setMetric(theExactNameAnyOrder)
			.setExact(theExact);

		EmpiRulesJson retval = new EmpiRulesJson();
		retval.addMatchField(nameAnyOrderFieldMatch);
		retval.putMatchResult(FIELD_EXACT_MATCH_NAME, EmpiMatchResultEnum.MATCH);

		return retval;
	}

	@BeforeAll
	public static void beforeClass() {
		ourJohnHenry = buildPatientWithNames("Henry", "John");
		ourJohnHENRY = buildPatientWithNames("HENRY", "John");
		ourJaneHenry = buildPatientWithNames("Henry", "Jane");
		ourJohnSmith = buildPatientWithNames("Smith", "John");
		ourJohnBillyHenry = buildPatientWithNames("Henry", "John", "Billy");
		ourBillyJohnHenry = buildPatientWithNames("Henry", "Billy", "John");
		ourHenryJohn = buildPatientWithNames("John", "Henry");
		ourHenryJOHN = buildPatientWithNames("JOHN", "Henry");
	}

	protected static Patient buildPatientWithNames(String theFamilyName, String... theGivenNames) {
		Patient patient = new Patient();
		HumanName name = patient.addName();
		name.setFamily(theFamilyName);
		for (String givenName : theGivenNames) {
			name.addGiven(givenName);
		}
		patient.setId("Patient/1");
		return patient;
	}
}
