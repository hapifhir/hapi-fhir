package ca.uhn.fhir.empi.rules.json;

import ca.uhn.fhir.empi.api.EmpiMatchResultEnum;
import ca.uhn.fhir.empi.rules.metric.EmpiMatcherEnum;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VectorMatchResultMapTest {
	@Test
	public void splitFieldMatchNames() {
		{
			String[] result = VectorMatchResultMap.splitFieldMatchNames("a,b");
			assertEquals(2, result.length);
			assertEquals("a", result[0]);
			assertEquals("b", result[1]);
		}

		{
			String[] result = VectorMatchResultMap.splitFieldMatchNames("a,  b");
			assertEquals(2, result.length);
			assertEquals("a", result[0]);
			assertEquals("b", result[1]);
		}
	}

	@Test
	public void testMatchBeforePossibleMatch() {
		EmpiRulesJson empiRulesJson = new EmpiRulesJson();
		EmpiMatcherJson matcherJson = new EmpiMatcherJson().setAlgorithm(EmpiMatcherEnum.STRING);
		empiRulesJson.addMatchField(new EmpiFieldMatchJson().setName("given").setResourceType("Patient").setResourcePath("name.given").setMatcher(matcherJson));
		empiRulesJson.addMatchField(new EmpiFieldMatchJson().setName("family").setResourceType("Patient").setResourcePath("name.family").setMatcher(matcherJson));
		empiRulesJson.addMatchField(new EmpiFieldMatchJson().setName("prefix").setResourceType("Patient").setResourcePath("name.prefix").setMatcher(matcherJson));
		empiRulesJson.putMatchResult("given,family", EmpiMatchResultEnum.MATCH);
		empiRulesJson.putMatchResult("given", EmpiMatchResultEnum.POSSIBLE_MATCH);

		VectorMatchResultMap map = new VectorMatchResultMap(empiRulesJson);
		assertEquals(EmpiMatchResultEnum.POSSIBLE_MATCH, map.get(1L));
		assertEquals(EmpiMatchResultEnum.MATCH, map.get(3L));
		assertEquals(EmpiMatchResultEnum.MATCH, map.get(7L));
	}
}
