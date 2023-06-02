package ca.uhn.fhir.mdm.rules.svc;

import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.mdm.api.MdmMatchOutcome;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.rules.json.MdmFieldMatchJson;
import ca.uhn.fhir.mdm.rules.json.MdmMatcherJson;
import ca.uhn.fhir.mdm.rules.json.MdmRulesJson;
import ca.uhn.fhir.mdm.rules.matcher.MdmMatcherEnum;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FhirPathResourceMatcherR4Test extends BaseMdmRulesR4Test {
	private static final String MATCH_FIELDS = PATIENT_GIVEN_FIRST + "," + PATIENT_GIVEN;
	private Patient myLeft;
	private Patient myRight;

	@Override
	@BeforeEach
	public void before() {
		super.before();
		when(mySearchParamRetriever.getActiveSearchParam("Patient", "birthdate")).thenReturn(mock(RuntimeSearchParam.class));
		when(mySearchParamRetriever.getActiveSearchParam("Patient", "identifier")).thenReturn(mock(RuntimeSearchParam.class));
		when(mySearchParamRetriever.getActiveSearchParam("Patient", "active")).thenReturn(mock(RuntimeSearchParam.class));

		{
			myLeft = new Patient();
			HumanName name = myLeft.addName();
			name.addGiven("Gary");
			name.addGiven("John");
			myLeft.setId("Patient/1");
		}
		{
			myRight = new Patient();
			HumanName name = myRight.addName();
			name.addGiven("John");
			name.addGiven("Gary");
			myRight.setId("Patient/2");
		}
	}


	@Test
	public void testFhirPathOrderedMatches() {
		MdmResourceMatcherSvc matcherSvc = buildMatcher(buildOrderedGivenNameRules(MdmMatcherEnum.STRING));

		myLeft = new Patient();
		HumanName name = myLeft.addName();
		name.addGiven("Gary");
		name.addGiven("John");
		myLeft.setId("Patient/1");

		myRight = new Patient();
		HumanName name2 = myRight.addName();
		name2.addGiven("John");
		name2.addGiven("Gary");
		myRight.setId("Patient/2");

		MdmMatchOutcome result = matcherSvc.match(myLeft, myRight);
		assertMatchResult(MdmMatchResultEnum.NO_MATCH, 0L, 0.0, false, false, result);

		myRight = new Patient();
		name = myRight.addName();
		name.addGiven("John");
		name.addGiven("Gary");
		myRight.setId("Patient/2");

		myLeft = new Patient();
		name2 = myLeft.addName();
		name2.addGiven("Frank");
		name2.addGiven("Gary");
		myLeft.setId("Patient/1");

		result = matcherSvc.match(myLeft, myRight);
		assertMatchResult(MdmMatchResultEnum.POSSIBLE_MATCH, 1L, 1.0, false, false, result);

	}

	@Test
	public void testStringMatchResult() {
		MdmResourceMatcherSvc matcherSvc = buildMatcher(buildOrderedGivenNameRules(MdmMatcherEnum.STRING));
		MdmMatchOutcome result = matcherSvc.match(myLeft, myRight);
		assertMatchResult(MdmMatchResultEnum.NO_MATCH, 0L, 0.0, false, false, result);
	}

	protected MdmRulesJson buildOrderedGivenNameRules(MdmMatcherEnum theMatcherEnum) {
		MdmFieldMatchJson firstGivenNameMatchField = new MdmFieldMatchJson()
			.setName(PATIENT_GIVEN_FIRST)
			.setResourceType("Patient")
			.setFhirPath("name.given.first()")
			.setMatcher(new MdmMatcherJson().setAlgorithm(theMatcherEnum));

		MdmFieldMatchJson secondGivenNameMatchField = new MdmFieldMatchJson()
			.setName(PATIENT_GIVEN)
			.setResourceType("Patient")
			.setFhirPath("name.given[1]")
			.setMatcher(new MdmMatcherJson().setAlgorithm(theMatcherEnum));

		MdmRulesJson retval = new MdmRulesJson();
		retval.setVersion("test version");
		retval.addMatchField(secondGivenNameMatchField);
		retval.addMatchField(firstGivenNameMatchField);
		retval.setMdmTypes(Arrays.asList("Patient"));
		retval.putMatchResult(MATCH_FIELDS, MdmMatchResultEnum.MATCH);
		retval.putMatchResult(PATIENT_GIVEN_FIRST, MdmMatchResultEnum.POSSIBLE_MATCH);
		retval.putMatchResult(PATIENT_GIVEN, MdmMatchResultEnum.POSSIBLE_MATCH);
		return retval;
	}
}
