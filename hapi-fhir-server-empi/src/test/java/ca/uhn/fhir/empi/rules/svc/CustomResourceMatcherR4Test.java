package ca.uhn.fhir.empi.rules.svc;

import ca.uhn.fhir.empi.BaseR4Test;
import ca.uhn.fhir.empi.api.EmpiMatchResultEnum;
import ca.uhn.fhir.empi.rules.json.EmpiFieldMatchJson;
import ca.uhn.fhir.empi.rules.json.EmpiRulesJson;
import ca.uhn.fhir.empi.rules.metric.EmpiMetricEnum;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Patient;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

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

	@BeforeClass
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

	@Test
	public void testExactNameAnyOrder() {
		EmpiResourceMatcherSvc nameAnyOrderMatcher = buildMatcher(buildNameRules(EmpiMetricEnum.NAME_ANY_ORDER, true));
		assertEquals(EmpiMatchResultEnum.MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourJohnHenry));
		assertEquals(EmpiMatchResultEnum.MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourHenryJohn));
		assertEquals(EmpiMatchResultEnum.NO_MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourHenryJOHN));
		assertEquals(EmpiMatchResultEnum.NO_MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourJohnHENRY));
		assertEquals(EmpiMatchResultEnum.NO_MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourJaneHenry));
		assertEquals(EmpiMatchResultEnum.NO_MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourJohnSmith));
		assertEquals(EmpiMatchResultEnum.MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourJohnBillyHenry));
		assertEquals(EmpiMatchResultEnum.MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourBillyJohnHenry));
	}

	@Test
	public void testNormalizedNameAnyOrder() {
		EmpiResourceMatcherSvc nameAnyOrderMatcher = buildMatcher(buildNameRules(EmpiMetricEnum.NAME_ANY_ORDER, false));
		assertEquals(EmpiMatchResultEnum.MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourJohnHenry));
		assertEquals(EmpiMatchResultEnum.MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourHenryJohn));
		assertEquals(EmpiMatchResultEnum.MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourHenryJOHN));
		assertEquals(EmpiMatchResultEnum.MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourJohnHENRY));
		assertEquals(EmpiMatchResultEnum.NO_MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourJaneHenry));
		assertEquals(EmpiMatchResultEnum.NO_MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourJohnSmith));
		assertEquals(EmpiMatchResultEnum.MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourJohnBillyHenry));
		assertEquals(EmpiMatchResultEnum.MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourBillyJohnHenry));
	}


	@Test
	public void testExactNameFirstAndLast() {
		EmpiResourceMatcherSvc nameAnyOrderMatcher = buildMatcher(buildNameRules(EmpiMetricEnum.NAME_FIRST_AND_LAST, true));
		assertEquals(EmpiMatchResultEnum.MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourJohnHenry));
		assertEquals(EmpiMatchResultEnum.NO_MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourHenryJohn));
		assertEquals(EmpiMatchResultEnum.NO_MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourHenryJOHN));
		assertEquals(EmpiMatchResultEnum.NO_MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourJohnHENRY));
		assertEquals(EmpiMatchResultEnum.NO_MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourJaneHenry));
		assertEquals(EmpiMatchResultEnum.NO_MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourJohnSmith));
		assertEquals(EmpiMatchResultEnum.MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourJohnBillyHenry));
		assertEquals(EmpiMatchResultEnum.MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourBillyJohnHenry));
	}

	@Test
	public void testNormalizedNameFirstAndLast() {
		EmpiResourceMatcherSvc nameAnyOrderMatcher = buildMatcher(buildNameRules(EmpiMetricEnum.NAME_FIRST_AND_LAST, false));
		assertEquals(EmpiMatchResultEnum.MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourJohnHenry));
		assertEquals(EmpiMatchResultEnum.NO_MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourHenryJohn));
		assertEquals(EmpiMatchResultEnum.NO_MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourHenryJOHN));
		assertEquals(EmpiMatchResultEnum.MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourJohnHENRY));
		assertEquals(EmpiMatchResultEnum.NO_MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourJaneHenry));
		assertEquals(EmpiMatchResultEnum.NO_MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourJohnSmith));
		assertEquals(EmpiMatchResultEnum.MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourJohnBillyHenry));
		assertEquals(EmpiMatchResultEnum.MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourBillyJohnHenry));
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
}
