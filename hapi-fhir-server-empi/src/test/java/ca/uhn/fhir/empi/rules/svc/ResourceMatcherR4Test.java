package ca.uhn.fhir.empi.rules.svc;

import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.empi.api.EmpiMatchOutcome;
import ca.uhn.fhir.empi.api.EmpiMatchResultEnum;
import ca.uhn.fhir.empi.rules.json.EmpiFieldMatchJson;
import ca.uhn.fhir.empi.rules.json.EmpiMatcherJson;
import ca.uhn.fhir.empi.rules.json.EmpiRulesJson;
import ca.uhn.fhir.empi.rules.matcher.EmpiMatcherEnum;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ResourceMatcherR4Test extends BaseEmpiRulesR4Test {
	private static final String PATIENT_PHONE = "phone";
	private static final String MATCH_FIELDS = PATIENT_GIVEN + "," + PATIENT_FAMILY + "," + PATIENT_PHONE;
	public static final String PHONE_NUMBER = "123 456789";
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
			name.addGiven("zulaiha");
			name.setFamily("namadega");
			myLeft.addTelecom().setValue(PHONE_NUMBER);
			myLeft.setId("Patient/1");
		}
		{
			myRight = new Patient();
			HumanName name = myRight.addName();
			name.addGiven("zulaiha");
			name.setFamily("namaedga");
			myRight.addTelecom().setValue(PHONE_NUMBER);
			myRight.setId("Patient/2");
		}
	}

	@Test
	public void testMetaphoneMatchResult() {
		EmpiResourceMatcherSvc matcherSvc = buildMatcher(buildNamePhoneRules(EmpiMatcherEnum.METAPHONE));
		EmpiMatchOutcome result = matcherSvc.match(myLeft, myRight);
		assertMatchResult(EmpiMatchResultEnum.MATCH, 7L, 3.0, false, false, result);
	}

	@Test
	public void testStringMatchResult() {
		EmpiResourceMatcherSvc matcherSvc = buildMatcher(buildNamePhoneRules(EmpiMatcherEnum.STRING));
		EmpiMatchOutcome result = matcherSvc.match(myLeft, myRight);
		assertMatchResult(EmpiMatchResultEnum.NO_MATCH, 5L, 2.0, false, false, result);
	}

	protected EmpiRulesJson buildNamePhoneRules(EmpiMatcherEnum theMatcherEnum) {
		EmpiFieldMatchJson lastNameMatchField = new EmpiFieldMatchJson()
			.setName(PATIENT_FAMILY)
			.setResourceType("Patient")
			.setResourcePath("name.family")
			.setMatcher(new EmpiMatcherJson().setAlgorithm(theMatcherEnum));

		EmpiFieldMatchJson firstNameMatchField = new EmpiFieldMatchJson()
			.setName(PATIENT_GIVEN)
			.setResourceType("Patient")
			.setResourcePath("name.given")
			.setMatcher(new EmpiMatcherJson().setAlgorithm(theMatcherEnum));

		EmpiFieldMatchJson phoneField = new EmpiFieldMatchJson()
			.setName(PATIENT_PHONE)
			.setResourceType("Patient")
			.setResourcePath("telecom.value")
			.setMatcher(new EmpiMatcherJson().setAlgorithm(EmpiMatcherEnum.STRING));

		EmpiRulesJson retval = new EmpiRulesJson();
		retval.setVersion("test version");
		retval.addMatchField(firstNameMatchField);
		retval.addMatchField(lastNameMatchField);
		retval.addMatchField(phoneField);
		retval.putMatchResult(MATCH_FIELDS, EmpiMatchResultEnum.MATCH);
		return retval;
	}
}
