package ca.uhn.fhir.empi.rules.svc;

import ca.uhn.fhir.empi.BaseR4Test;
import ca.uhn.fhir.empi.api.EmpiMatchResultEnum;
import ca.uhn.fhir.empi.rules.json.DistanceMetricEnum;
import ca.uhn.fhir.empi.rules.json.EmpiFieldMatchJson;
import ca.uhn.fhir.empi.rules.json.EmpiRulesJson;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CustomResourceComparatorR4Test extends BaseR4Test {

	public static final String FIELD_EXACT_MATCH_NAME = DistanceMetricEnum.EXACT_NAME_ANY_ORDER.name();
	private static Patient ourJohnHenry;
	private static Patient ourJohnHENRY;
	private static Patient ourJaneHenry;
	private static Patient ourJohnSmith;
	private static Patient ourJohnBillyHenry;
	private static Patient ourBillyJohnHenry;
	private static Patient ourHenryJohn;
	private static Patient ourHenryJOHN;

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

	@Test
	public void testExactNameAnyOrder() {
		EmpiResourceComparatorSvc nameAnyOrderComparator = buildComparator(buildNameAnyOrderRules(DistanceMetricEnum.EXACT_NAME_ANY_ORDER));
		assertEquals(EmpiMatchResultEnum.MATCH, nameAnyOrderComparator.compare(ourJohnHenry, ourJohnHenry));
		assertEquals(EmpiMatchResultEnum.MATCH, nameAnyOrderComparator.compare(ourJohnHenry, ourHenryJohn));
		assertEquals(EmpiMatchResultEnum.NO_MATCH, nameAnyOrderComparator.compare(ourJohnHenry, ourHenryJOHN));
		assertEquals(EmpiMatchResultEnum.NO_MATCH, nameAnyOrderComparator.compare(ourJohnHenry, ourJohnHENRY));
		assertEquals(EmpiMatchResultEnum.NO_MATCH, nameAnyOrderComparator.compare(ourJohnHenry, ourJaneHenry));
		assertEquals(EmpiMatchResultEnum.NO_MATCH, nameAnyOrderComparator.compare(ourJohnHenry, ourJohnSmith));
		assertEquals(EmpiMatchResultEnum.MATCH, nameAnyOrderComparator.compare(ourJohnHenry, ourJohnBillyHenry));
		assertEquals(EmpiMatchResultEnum.MATCH, nameAnyOrderComparator.compare(ourJohnHenry, ourBillyJohnHenry));
	}

	@Test
	public void testStandardNameAnyOrder() {
		EmpiResourceComparatorSvc nameAnyOrderComparator = buildComparator(buildNameAnyOrderRules(DistanceMetricEnum.STANDARD_NAME_ANY_ORDER));
		assertEquals(EmpiMatchResultEnum.MATCH, nameAnyOrderComparator.compare(ourJohnHenry, ourJohnHenry));
		assertEquals(EmpiMatchResultEnum.MATCH, nameAnyOrderComparator.compare(ourJohnHenry, ourHenryJohn));
		assertEquals(EmpiMatchResultEnum.MATCH, nameAnyOrderComparator.compare(ourJohnHenry, ourHenryJOHN));
		assertEquals(EmpiMatchResultEnum.MATCH, nameAnyOrderComparator.compare(ourJohnHenry, ourJohnHENRY));
		assertEquals(EmpiMatchResultEnum.NO_MATCH, nameAnyOrderComparator.compare(ourJohnHenry, ourJaneHenry));
		assertEquals(EmpiMatchResultEnum.NO_MATCH, nameAnyOrderComparator.compare(ourJohnHenry, ourJohnSmith));
		assertEquals(EmpiMatchResultEnum.MATCH, nameAnyOrderComparator.compare(ourJohnHenry, ourJohnBillyHenry));
		assertEquals(EmpiMatchResultEnum.MATCH, nameAnyOrderComparator.compare(ourJohnHenry, ourBillyJohnHenry));
	}


	@Test
	public void testExactNameFirstAndLast() {
		EmpiResourceComparatorSvc nameAnyOrderComparator = buildComparator(buildNameAnyOrderRules(DistanceMetricEnum.EXACT_NAME_FIRST_AND_LAST));
		assertEquals(EmpiMatchResultEnum.MATCH, nameAnyOrderComparator.compare(ourJohnHenry, ourJohnHenry));
		assertEquals(EmpiMatchResultEnum.NO_MATCH, nameAnyOrderComparator.compare(ourJohnHenry, ourHenryJohn));
		assertEquals(EmpiMatchResultEnum.NO_MATCH, nameAnyOrderComparator.compare(ourJohnHenry, ourHenryJOHN));
		assertEquals(EmpiMatchResultEnum.MATCH, nameAnyOrderComparator.compare(ourJohnHenry, ourJohnHENRY));
		assertEquals(EmpiMatchResultEnum.NO_MATCH, nameAnyOrderComparator.compare(ourJohnHenry, ourJaneHenry));
		assertEquals(EmpiMatchResultEnum.NO_MATCH, nameAnyOrderComparator.compare(ourJohnHenry, ourJohnSmith));
		assertEquals(EmpiMatchResultEnum.MATCH, nameAnyOrderComparator.compare(ourJohnHenry, ourJohnBillyHenry));
		assertEquals(EmpiMatchResultEnum.MATCH, nameAnyOrderComparator.compare(ourJohnHenry, ourBillyJohnHenry));
	}

	@Test
	public void testStandardNameFirstAndLast() {
		EmpiResourceComparatorSvc nameAnyOrderComparator = buildComparator(buildNameAnyOrderRules(DistanceMetricEnum.STANDARD_NAME_FIRST_AND_LAST));
		assertEquals(EmpiMatchResultEnum.MATCH, nameAnyOrderComparator.compare(ourJohnHenry, ourJohnHenry));
		assertEquals(EmpiMatchResultEnum.NO_MATCH, nameAnyOrderComparator.compare(ourJohnHenry, ourHenryJohn));
		assertEquals(EmpiMatchResultEnum.NO_MATCH, nameAnyOrderComparator.compare(ourJohnHenry, ourHenryJOHN));
		assertEquals(EmpiMatchResultEnum.NO_MATCH, nameAnyOrderComparator.compare(ourJohnHenry, ourJohnHENRY));
		assertEquals(EmpiMatchResultEnum.NO_MATCH, nameAnyOrderComparator.compare(ourJohnHenry, ourJaneHenry));
		assertEquals(EmpiMatchResultEnum.NO_MATCH, nameAnyOrderComparator.compare(ourJohnHenry, ourJohnSmith));
		assertEquals(EmpiMatchResultEnum.MATCH, nameAnyOrderComparator.compare(ourJohnHenry, ourJohnBillyHenry));
		assertEquals(EmpiMatchResultEnum.MATCH, nameAnyOrderComparator.compare(ourJohnHenry, ourBillyJohnHenry));
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

	private EmpiRulesJson buildNameAnyOrderRules(DistanceMetricEnum theExactNameAnyOrder) {
		EmpiFieldMatchJson nameAnyOrderFieldMatch = new EmpiFieldMatchJson()
			.setName(FIELD_EXACT_MATCH_NAME)
			.setResourceType("Patient")
			.setResourcePath("name")
			.setMetric(theExactNameAnyOrder)
			.setMatchThreshold(NAME_THRESHOLD);

		EmpiRulesJson retval = new EmpiRulesJson();
		retval.addMatchField(nameAnyOrderFieldMatch);
		retval.putMatchResult(FIELD_EXACT_MATCH_NAME, EmpiMatchResultEnum.MATCH);

		return retval;
	}
}
