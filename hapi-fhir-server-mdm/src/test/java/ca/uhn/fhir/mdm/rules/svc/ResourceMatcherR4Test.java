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

public class ResourceMatcherR4Test extends BaseMdmRulesR4Test {
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
		when(mySearchParamRetriever.getActiveSearchParam("Practitioner", "identifier")).thenReturn(mock(RuntimeSearchParam.class));
		when(mySearchParamRetriever.getActiveSearchParam("Medication", "identifier")).thenReturn(mock(RuntimeSearchParam.class));
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
		MdmResourceMatcherSvc matcherSvc = buildMatcher(buildNamePhoneRules(MdmMatcherEnum.METAPHONE));
			MdmMatchOutcome result = matcherSvc.match(myLeft, myRight);
			assertMatchResult(MdmMatchResultEnum.MATCH, 7L, 3.0, false, false, result);
	}

	@Test
	public void testStringMatchResult() {
		MdmResourceMatcherSvc matcherSvc = buildMatcher(buildNamePhoneRules(MdmMatcherEnum.STRING));
		MdmMatchOutcome result = matcherSvc.match(myLeft, myRight);
		assertMatchResult(MdmMatchResultEnum.NO_MATCH, 5L, 2.0, false, false, result);
	}

	protected MdmRulesJson buildNamePhoneRules(MdmMatcherEnum theMatcherEnum) {
		MdmFieldMatchJson lastNameMatchField = new MdmFieldMatchJson()
			.setName(PATIENT_FAMILY)
			.setResourceType("Patient")
			.setResourcePath("name.family")
			.setMatcher(new MdmMatcherJson().setAlgorithm(theMatcherEnum));

		MdmFieldMatchJson firstNameMatchField = new MdmFieldMatchJson()
			.setName(PATIENT_GIVEN)
			.setResourceType("Patient")
			.setResourcePath("name.given")
			.setMatcher(new MdmMatcherJson().setAlgorithm(theMatcherEnum));

		MdmFieldMatchJson phoneField = new MdmFieldMatchJson()
			.setName(PATIENT_PHONE)
			.setResourceType("Patient")
			.setResourcePath("telecom.value")
			.setMatcher(new MdmMatcherJson().setAlgorithm(MdmMatcherEnum.STRING));

		MdmRulesJson retval = new MdmRulesJson();
		retval.setVersion("test version");
		retval.addMatchField(firstNameMatchField);
		retval.addMatchField(lastNameMatchField);
		retval.setMdmTypes(Arrays.asList("Patient", "Practitioner", "Medication"));
		retval.addMatchField(phoneField);
		retval.putMatchResult(MATCH_FIELDS, MdmMatchResultEnum.MATCH);
		return retval;
	}
}
