package ca.uhn.fhir.empi.rules.svc;

import ca.uhn.fhir.empi.BaseTest;
import ca.uhn.fhir.empi.api.EmpiMatchResultEnum;
import ca.uhn.fhir.empi.rules.json.DistanceMetricEnum;
import ca.uhn.fhir.empi.rules.json.EmpiFieldMatchJson;
import ca.uhn.fhir.empi.rules.json.EmpiRulesJson;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Patient;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CustomResourceComparatorTest extends BaseTest {

	public static final String FIELD_EXACT_MATCH_NAME = DistanceMetricEnum.EXACT_NAME_ANY_ORDER.name();

	@Test
	public void testNameAnyOrder() {
		EmpiResourceComparatorSvc nameAnyOrderComparator = buildComparator(buildNameAnyOrderRules(DistanceMetricEnum.EXACT_NAME_ANY_ORDER));
		Patient johnHenry = buildPatientWithNames("Henry", "John");
		Patient henryJohn = buildPatientWithNames("John", "Henry");
		EmpiMatchResultEnum result = nameAnyOrderComparator.compare(johnHenry, henryJohn);
		assertEquals(EmpiMatchResultEnum.MATCH, result);
	}

	@Test


	protected Patient buildPatientWithNames(String theFamilyName, String... theGivenNames) {
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
