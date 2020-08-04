package ca.uhn.fhir.empi.rules.json;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.empi.api.EmpiMatchResultEnum;
import ca.uhn.fhir.empi.rules.metric.EmpiMetricEnum;
import ca.uhn.fhir.empi.rules.svc.BaseEmpiRulesR4Test;
import ca.uhn.fhir.util.JsonUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class EmpiRulesJsonR4Test extends BaseEmpiRulesR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(EmpiRulesJsonR4Test.class);
	private EmpiRulesJson myRules;

	@Override
	@BeforeEach
	public void before() {
		super.before();

		myRules = buildActiveBirthdateIdRules();
	}

	@Test
	public void testValidate() throws IOException {
		EmpiRulesJson rules = new EmpiRulesJson();
		try {
			JsonUtil.serialize(rules);
		} catch (NullPointerException e) {
			assertThat(e.getMessage(), containsString("version may not be blank"));
		}
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
		assertEquals(EmpiMetricEnum.JARO_WINKLER, second.getMetric());
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
		} catch (ConfigurationException e) {
			assertEquals("There is no matchField with name bad", e.getMessage());
		}
	}
}
