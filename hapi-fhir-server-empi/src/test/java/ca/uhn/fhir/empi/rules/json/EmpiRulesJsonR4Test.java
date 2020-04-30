package ca.uhn.fhir.empi.rules.json;

import ca.uhn.fhir.empi.BaseR4Test;
import ca.uhn.fhir.empi.api.EmpiMatchResultEnum;
import ca.uhn.fhir.util.JsonUtil;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;

public class EmpiRulesJsonR4Test extends BaseR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(EmpiRulesJsonR4Test.class);
	private EmpiRulesJson myRules;

	@Before
	public void before() {
		super.before();

		myRules = buildActiveBirthdateIdRules();
	}

	@Test
	public void testSerDeser() throws IOException {
		String json = JsonUtil.serialize(myRules);
		ourLog.info(json);
		EmpiRulesJson rulesDeser = JsonUtil.deserialize(json, EmpiRulesJson.class);
		assertEquals(2, rulesDeser.size());
		assertEquals(EmpiMatchResultEnum.MATCH, rulesDeser.getMatchResult(myBothNameFields));
		EmpiFieldMatchJson second = rulesDeser.get(1);
		assertEquals("name.family", second.getResourcePath());
		TestCase.assertEquals(DistanceMetricEnum.JARO_WINKLER, second.getMetric());
	}

	@Test
	public void testMatchResultMap() {
		assertEquals(EmpiMatchResultEnum.MATCH, myRules.getMatchResult(3L));
	}

	@Test
	public void getVector() {
		VectorMatchResultMap vectorMatchResultMap = myRules.getVectorMatchResultMapForUnitTest();
		assertEquals(1, vectorMatchResultMap.getVector(PATIENT_GIVEN));
		assertEquals(2, vectorMatchResultMap.getVector(PATIENT_LAST));
		assertEquals(3, vectorMatchResultMap.getVector(String.join(",", PATIENT_GIVEN, PATIENT_LAST)));
		assertEquals(3, vectorMatchResultMap.getVector(String.join(", ", PATIENT_GIVEN, PATIENT_LAST)));
		assertEquals(3, vectorMatchResultMap.getVector(String.join(",  ", PATIENT_GIVEN, PATIENT_LAST)));
		assertEquals(3, vectorMatchResultMap.getVector(String.join(", \n ", PATIENT_GIVEN, PATIENT_LAST)));
		try {
			vectorMatchResultMap.getVector("bad");
			fail();
		} catch (IllegalArgumentException e) {
			assertEquals("There is no matchField with name bad", e.getMessage());
		}
	}
}
