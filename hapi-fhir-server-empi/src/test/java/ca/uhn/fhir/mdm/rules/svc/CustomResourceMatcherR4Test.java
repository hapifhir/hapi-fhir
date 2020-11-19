package ca.uhn.fhir.mdm.rules.svc;

import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.mdm.BaseR4Test;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.rules.json.MdmFieldMatchJson;
import ca.uhn.fhir.mdm.rules.json.MdmMatcherJson;
import ca.uhn.fhir.mdm.rules.json.MdmRulesJson;
import ca.uhn.fhir.mdm.rules.matcher.MdmMatcherEnum;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CustomResourceMatcherR4Test extends BaseR4Test {

	public static final String FIELD_EXACT_MATCH_NAME = MdmMatcherEnum.NAME_ANY_ORDER.name();
	private static Patient ourJohnHenry;
	private static Patient ourJohnHENRY;
	private static Patient ourJaneHenry;
	private static Patient ourJohnSmith;
	private static Patient ourJohnBillyHenry;
	private static Patient ourBillyJohnHenry;
	private static Patient ourHenryJohn;
	private static Patient ourHenryJOHN;

	@BeforeEach
	public void before() {
		when(mySearchParamRetriever.getActiveSearchParam("Patient", "identifier")).thenReturn(mock(RuntimeSearchParam.class));
		when(mySearchParamRetriever.getActiveSearchParam("Practitioner", "identifier")).thenReturn(mock(RuntimeSearchParam.class));
		when(mySearchParamRetriever.getActiveSearchParam("Medication", "identifier")).thenReturn(mock(RuntimeSearchParam.class));
		when(mySearchParamRetriever.getActiveSearchParam("AllergyIntolerance", "identifier")).thenReturn(null);
	}

	@Test
	public void testExactNameAnyOrder() {
		MdmResourceMatcherSvc nameAnyOrderMatcher = buildMatcher(buildNameRules(MdmMatcherEnum.NAME_ANY_ORDER, true));
		assertMatch(MdmMatchResultEnum.MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourJohnHenry));
		assertMatch(MdmMatchResultEnum.MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourHenryJohn));
		assertMatch(MdmMatchResultEnum.NO_MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourHenryJOHN));
		assertMatch(MdmMatchResultEnum.NO_MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourJohnHENRY));
		assertMatch(MdmMatchResultEnum.NO_MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourJaneHenry));
		assertMatch(MdmMatchResultEnum.NO_MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourJohnSmith));
		assertMatch(MdmMatchResultEnum.MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourJohnBillyHenry));
		assertMatch(MdmMatchResultEnum.MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourBillyJohnHenry));
	}

	@Test
	public void testNormalizedNameAnyOrder() {
		MdmResourceMatcherSvc nameAnyOrderMatcher = buildMatcher(buildNameRules(MdmMatcherEnum.NAME_ANY_ORDER, false));
		assertMatch(MdmMatchResultEnum.MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourJohnHenry));
		assertMatch(MdmMatchResultEnum.MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourHenryJohn));
		assertMatch(MdmMatchResultEnum.MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourHenryJOHN));
		assertMatch(MdmMatchResultEnum.MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourJohnHENRY));
		assertMatch(MdmMatchResultEnum.NO_MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourJaneHenry));
		assertMatch(MdmMatchResultEnum.NO_MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourJohnSmith));
		assertMatch(MdmMatchResultEnum.MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourJohnBillyHenry));
		assertMatch(MdmMatchResultEnum.MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourBillyJohnHenry));
	}

	@Test
	public void testExactNameFirstAndLast() {
		MdmResourceMatcherSvc nameAnyOrderMatcher = buildMatcher(buildNameRules(MdmMatcherEnum.NAME_FIRST_AND_LAST, true));
		assertMatch(MdmMatchResultEnum.MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourJohnHenry));
		assertMatchResult(MdmMatchResultEnum.MATCH, 1L, 1.0, false, false, nameAnyOrderMatcher.match(ourJohnHenry, ourJohnHenry));
		assertMatch(MdmMatchResultEnum.NO_MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourHenryJohn));
		assertMatch(MdmMatchResultEnum.NO_MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourHenryJOHN));
		assertMatch(MdmMatchResultEnum.NO_MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourJohnHENRY));
		assertMatch(MdmMatchResultEnum.NO_MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourJaneHenry));
		assertMatch(MdmMatchResultEnum.NO_MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourJohnSmith));
		assertMatch(MdmMatchResultEnum.MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourJohnBillyHenry));
		assertMatch(MdmMatchResultEnum.MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourBillyJohnHenry));
	}

	@Test
	public void testNormalizedNameFirstAndLast() {
		MdmResourceMatcherSvc nameAnyOrderMatcher = buildMatcher(buildNameRules(MdmMatcherEnum.NAME_FIRST_AND_LAST, false));
		assertMatch(MdmMatchResultEnum.MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourJohnHenry));
		assertMatch(MdmMatchResultEnum.NO_MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourHenryJohn));
		assertMatch(MdmMatchResultEnum.NO_MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourHenryJOHN));
		assertMatch(MdmMatchResultEnum.MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourJohnHENRY));
		assertMatch(MdmMatchResultEnum.NO_MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourJaneHenry));
		assertMatch(MdmMatchResultEnum.NO_MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourJohnSmith));
		assertMatch(MdmMatchResultEnum.MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourJohnBillyHenry));
		assertMatch(MdmMatchResultEnum.MATCH, nameAnyOrderMatcher.match(ourJohnHenry, ourBillyJohnHenry));
	}

	private MdmRulesJson buildNameRules(MdmMatcherEnum theAlgorithm, boolean theExact) {
		MdmMatcherJson matcherJson = new MdmMatcherJson().setAlgorithm(theAlgorithm).setExact(theExact);
		MdmFieldMatchJson nameAnyOrderFieldMatch = new MdmFieldMatchJson()
			.setName(FIELD_EXACT_MATCH_NAME)
			.setResourceType("Patient")
			.setResourcePath("name")
			.setMatcher(matcherJson);

		MdmRulesJson retval = new MdmRulesJson();
		retval.addMatchField(nameAnyOrderFieldMatch);
		retval.setMdmTypes(Arrays.asList("Patient", "Practitioner", "Medication"));
		retval.putMatchResult(FIELD_EXACT_MATCH_NAME, MdmMatchResultEnum.MATCH);

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
