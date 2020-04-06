package ca.uhn.fhir.empi.rules.svc;

import ca.uhn.fhir.empi.BaseTest;
import ca.uhn.fhir.empi.rules.json.DistanceMetricEnum;
import ca.uhn.fhir.empi.rules.json.EmpiFieldMatchJson;
import ca.uhn.fhir.empi.rules.json.EmpiRulesJson;
import org.hl7.fhir.r4.model.Patient;
import org.junit.Test;

import static ca.uhn.fhir.empi.rules.svc.EmpiResourceComparatorSvcTest.NAME_DELTA;
import static org.junit.Assert.assertEquals;

public class CustomResourceComparatorTest extends BaseTest {

	@Test
	public void testNameAnyOrder() {
		EmpiResourceComparatorSvc nameAnyOrderComparator = buildComparator(buildNameAnyOrderRules());
		Patient johnHenry = buildJohnHenry();
		Patient henryJohn = buildHenryJohn();
		double result = nameAnyOrderComparator.compare(johnHenry, henryJohn);
		assertEquals(1.0, result, NAME_DELTA);
	}

	protected Patient buildJohnHenry() {
		Patient patient = new Patient();
		patient.addName().addGiven("John");
		patient.addName().setFamily("Henry");
		patient.setId("Patient/1");
		return patient;
	}

	protected Patient buildHenryJohn() {
		Patient patient = new Patient();
		patient.addName().addGiven("Henry");
		patient.addName().setFamily("John");
		patient.setId("Patient/2");
		return patient;
	}

	private EmpiRulesJson buildNameAnyOrderRules() {
		EmpiFieldMatchJson nameAnyOrderFieldMatch = new EmpiFieldMatchJson()
			.setName(PATIENT_LAST)
			.setResourceType("Patient")
			.setResourcePath("name")
			.setMetric(DistanceMetricEnum.NAME_ANY_ORDER)
			.setMatchThreshold(NAME_THRESHOLD);

		EmpiRulesJson retval = new EmpiRulesJson();
		retval.addMatchField(nameAnyOrderFieldMatch);
		retval.setMatchThreshold(MATCH_THRESHOLD);
		retval.setNoMatchThreshold(NO_MATCH_THRESHOLD);

		return retval;
	}
}
