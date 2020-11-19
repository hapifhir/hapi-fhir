package ca.uhn.fhir.mdm.rules.json;

import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.rules.matcher.MdmMatcherEnum;
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
		MdmRulesJson mdmRulesJson = new MdmRulesJson();
		MdmMatcherJson matcherJson = new MdmMatcherJson().setAlgorithm(MdmMatcherEnum.STRING);
		mdmRulesJson.addMatchField(new MdmFieldMatchJson().setName("given").setResourceType("Patient").setResourcePath("name.given").setMatcher(matcherJson));
		mdmRulesJson.addMatchField(new MdmFieldMatchJson().setName("family").setResourceType("Patient").setResourcePath("name.family").setMatcher(matcherJson));
		mdmRulesJson.addMatchField(new MdmFieldMatchJson().setName("prefix").setResourceType("Patient").setResourcePath("name.prefix").setMatcher(matcherJson));
		mdmRulesJson.putMatchResult("given,family", MdmMatchResultEnum.MATCH);
		mdmRulesJson.putMatchResult("given", MdmMatchResultEnum.POSSIBLE_MATCH);

		VectorMatchResultMap map = new VectorMatchResultMap(mdmRulesJson);
		assertEquals(MdmMatchResultEnum.POSSIBLE_MATCH, map.get(1L));
		assertEquals(MdmMatchResultEnum.MATCH, map.get(3L));
		assertEquals(MdmMatchResultEnum.MATCH, map.get(7L));
	}
}
