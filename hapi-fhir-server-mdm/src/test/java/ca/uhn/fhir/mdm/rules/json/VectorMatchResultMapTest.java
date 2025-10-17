package ca.uhn.fhir.mdm.rules.json;

import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.rules.matcher.models.MatchTypeEnum;
import ca.uhn.fhir.mdm.rules.matcher.util.MatchRuleUtil;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
	public void getVector_largeNumberOfFields_overflowTest() {
		// setup
		MdmRulesJson rules = new MdmRulesJson();
		rules.setVersion("1");
		rules.setMdmTypes(List.of("Patient"));

		// log2(N) -> gives number of binary digits needed to store the result in binary
		// (note on math trick: Log10(N)/Log10(2) = Log2(N) by Log rule)
		long maxIntDigits = Math.round(Math.log(Integer.MAX_VALUE) / Math.log(2));

		// we don't actually need a real "rules" object
		// we just need one that has enough field values
		// we can check integer overflow values
		for (int i = 0; i < MatchRuleUtil.MAX_RULE_COUNT; i++) {
			MdmFieldMatchJson fieldMatch = new MdmFieldMatchJson();
			fieldMatch.setName("field_" + i);
			rules.addMatchField(fieldMatch);
		}

		VectorMatchResultMap map = new VectorMatchResultMap(rules);

		// test
		long vectorVal = map.getVector("field_" + maxIntDigits);

		// verify
		assertTrue(vectorVal > 0L, "Vector value " + vectorVal);
	}

	@Test
	public void testMatchBeforePossibleMatch() {
		MdmRulesJson mdmRulesJson = new MdmRulesJson();
		mdmRulesJson.setVersion("1");
		MdmMatcherJson matcherJson = new MdmMatcherJson().setAlgorithm(MatchTypeEnum.STRING);
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
