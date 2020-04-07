package ca.uhn.fhir.empi.rules.svc;

import ca.uhn.fhir.empi.BaseTest;
import ca.uhn.fhir.empi.rules.json.DistanceMetricEnum;
import ca.uhn.fhir.empi.rules.json.EmpiFieldMatchJson;
import ca.uhn.fhir.empi.rules.json.EmpiRulesJson;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Patient;
import org.junit.Test;

import static ca.uhn.fhir.empi.rules.svc.EmpiResourceComparatorSvcTest.NAME_DELTA;
import static org.junit.Assert.assertEquals;

public class CustomResourceComparatorTest extends BaseTest {

	public static final String FIELD_EXACT_MATCH_NAME = DistanceMetricEnum.EXACT_NAME_ANY_ORDER.name();
	public static final double EXPECTED_MATCH_WEIGHT = 9.1;


	@Test
	public void testNameAnyOrder() {
		EmpiResourceComparatorSvc nameAnyOrderComparator = buildComparator(buildNameAnyOrderRules());
		Patient johnHenry = buildJohnHenry();
		Patient henryJohn = buildHenryJohn();
		double result = nameAnyOrderComparator.compare(johnHenry, henryJohn);
		assertEquals(EXPECTED_MATCH_WEIGHT, result, NAME_DELTA);
	}

	protected Patient buildJohnHenry() {
		Patient patient = new Patient();
		HumanName name = patient.addName();
		name.addGiven("John");
		name.setFamily("Henry");
		patient.setId("Patient/1");
		return patient;
	}

	protected Patient buildHenryJohn() {
		Patient patient = new Patient();
		HumanName name = patient.addName();
		name.addGiven("Henry");
		name.setFamily("John");
		patient.setId("Patient/2");
		return patient;
	}

	private EmpiRulesJson buildNameAnyOrderRules() {
		EmpiFieldMatchJson nameAnyOrderFieldMatch = new EmpiFieldMatchJson()
			.setName(FIELD_EXACT_MATCH_NAME)
			.setResourceType("Patient")
			.setResourcePath("name")
			.setMetric(DistanceMetricEnum.EXACT_NAME_ANY_ORDER)
			.setMatchThreshold(NAME_THRESHOLD);

		EmpiRulesJson retval = new EmpiRulesJson();
		retval.addMatchField(nameAnyOrderFieldMatch);
		retval.putWeight(FIELD_EXACT_MATCH_NAME, EXPECTED_MATCH_WEIGHT);
		retval.setMatchThreshold(MATCH_THRESHOLD);
		retval.setNoMatchThreshold(NO_MATCH_THRESHOLD);

		return retval;
	}
}
