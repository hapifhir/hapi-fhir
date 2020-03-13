package ca.uhn.fhir.empi.rules;

import ca.uhn.fhir.empi.BaseTest;
import ca.uhn.fhir.empi.rules.metric.DistanceMetricEnum;
import ca.uhn.fhir.jpa.util.JsonUtil;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Set;

import static junit.framework.TestCase.assertEquals;

public class EmpiRulesJsonTest extends BaseTest {
	private static final Logger ourLog = LoggerFactory.getLogger(EmpiRulesJsonTest.class);
	public static final String PATIENT_LAST = "patient-last";
	public static final double EXPECTED_WEIGHT = 0.23;
	private EmpiRulesJson myRules;

	@Before
	public void before() {
		super.before();

		myRules = new EmpiRulesJson();
		myRules.addMatchField(new EmpiFieldMatchJson(PATIENT_GIVEN, "Patient", "name.given", DistanceMetricEnum.COSINE, NAME_THRESHOLD));
		myRules.addMatchField(new EmpiFieldMatchJson(PATIENT_LAST, "Patient", "name.last", DistanceMetricEnum.JARO_WINKLER, NAME_THRESHOLD));
	}

	@Test
	public void testSerDeser() throws IOException {

		String json = JsonUtil.serialize(myRules);
		ourLog.info(json);
		EmpiRulesJson rulesDeser = JsonUtil.deserialize(json, EmpiRulesJson.class);
		assertEquals(2, rulesDeser.size());
		EmpiFieldMatchJson second = rulesDeser.get(1);
		assertEquals("name.last", second.getResourcePath());
		assertEquals(DistanceMetricEnum.JARO_WINKLER, second.getMetric());
	}

	@Test
	public void testWeightMap() {
		Set<String> allFields = Set.of(PATIENT_GIVEN, PATIENT_LAST);
		myRules.putWeight(allFields, EXPECTED_WEIGHT);
		assertEquals(EXPECTED_WEIGHT, myRules.getWeight(3L));
	}
}
