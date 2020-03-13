package ca.uhn.fhir.empi.rules;

import ca.uhn.fhir.empi.BaseTest;
import ca.uhn.fhir.empi.rules.metric.DistanceMetricEnum;
import ca.uhn.fhir.jpa.util.JsonUtil;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static junit.framework.TestCase.assertEquals;

public class EmpiRulesJsonTest extends BaseTest {
	private static final Logger ourLog = LoggerFactory.getLogger(EmpiRulesJsonTest.class);

	@Test
	public void testSerDeser() throws IOException {
		EmpiRulesJson rules = new EmpiRulesJson();
		rules.addMatchField(new EmpiMatchFieldJson("Patient", "name.given", DistanceMetricEnum.COSINE, NAME_THRESHOLD));
		rules.addMatchField(new EmpiMatchFieldJson("Patient", "name.last", DistanceMetricEnum.JARO_WINKLER, NAME_THRESHOLD));

		String json = JsonUtil.serialize(rules);
		ourLog.info(json);
		EmpiRulesJson rulesDeser = JsonUtil.deserialize(json, EmpiRulesJson.class);
		assertEquals(2, rulesDeser.size());
		EmpiMatchFieldJson second = rulesDeser.get(1);
		assertEquals("name.last", second.getResourcePath());
		assertEquals(DistanceMetricEnum.JARO_WINKLER, second.getMetric());
	}
}
