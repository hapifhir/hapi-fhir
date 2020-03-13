package ca.uhn.fhir.empi.rules;

import ca.uhn.fhir.empi.BaseTest;
import ca.uhn.fhir.empi.rules.metric.DistanceMetricEnum;
import ca.uhn.fhir.jpa.util.JsonUtil;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;

public class EmpiRulesJsonTest extends BaseTest {
	private static final Logger ourLog = LoggerFactory.getLogger(EmpiRulesJsonTest.class);
	public static final String PATIENT_LAST = "patient-last";
	public static final double EXPECTED_WEIGHT = 0.23;
	private EmpiRulesJson myRules;
	private String myAllFields;

	@Before
	public void before() {
		super.before();

		myRules = new EmpiRulesJson();
		myRules.addMatchField(new EmpiFieldMatchJson(PATIENT_GIVEN, "Patient", "name.given", DistanceMetricEnum.COSINE, NAME_THRESHOLD));
		myRules.addMatchField(new EmpiFieldMatchJson(PATIENT_LAST, "Patient", "name.last", DistanceMetricEnum.JARO_WINKLER, NAME_THRESHOLD));
		myAllFields = String.join(",", PATIENT_GIVEN, PATIENT_LAST);
		myRules.putWeight(myAllFields, EXPECTED_WEIGHT);
	}

	@Test
	public void testSerDeser() throws IOException {
		String json = JsonUtil.serialize(myRules);
		ourLog.info(json);
		EmpiRulesJson rulesDeser = JsonUtil.deserialize(json, EmpiRulesJson.class);
		assertEquals(2, rulesDeser.size());
		assertEquals(EXPECTED_WEIGHT, rulesDeser.getWeight(myAllFields));
		EmpiFieldMatchJson second = rulesDeser.get(1);
		assertEquals("name.last", second.getResourcePath());
		assertEquals(DistanceMetricEnum.JARO_WINKLER, second.getMetric());
	}

	@Test
	public void testWeightMap() {
		assertEquals(EXPECTED_WEIGHT, myRules.getWeight(3L));
	}

	@Test
	public void getVector() {
		VectorWeightMap vectorWeightMap = myRules.getVectorWeightMap();
		assertEquals(1, vectorWeightMap.getVector(PATIENT_GIVEN));
		assertEquals(2, vectorWeightMap.getVector(PATIENT_LAST));
		assertEquals(3, vectorWeightMap.getVector(String.join(",", PATIENT_GIVEN, PATIENT_LAST)));
		assertEquals(3, vectorWeightMap.getVector(String.join(", ", PATIENT_GIVEN, PATIENT_LAST)));
		assertEquals(3, vectorWeightMap.getVector(String.join(",  ", PATIENT_GIVEN, PATIENT_LAST)));
		assertEquals(3, vectorWeightMap.getVector(String.join(", \n ", PATIENT_GIVEN, PATIENT_LAST)));
		try {
			vectorWeightMap.getVector("bad");
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("There is no matchField with name bad", e.getMessage());
		}
	}
}
