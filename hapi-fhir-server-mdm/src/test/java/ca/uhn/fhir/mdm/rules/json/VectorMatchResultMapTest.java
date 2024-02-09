package ca.uhn.fhir.mdm.rules.json;

import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.rules.matcher.models.MatchTypeEnum;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class VectorMatchResultMapTest {
	@Test
	public void splitFieldMatchNames() {
		{
			String[] result = VectorMatchResultMap.splitFieldMatchNames("a,b");
			assertThat(result.length).isEqualTo(2);
			assertThat(result[0]).isEqualTo("a");
			assertThat(result[1]).isEqualTo("b");
		}

		{
			String[] result = VectorMatchResultMap.splitFieldMatchNames("a,  b");
			assertThat(result.length).isEqualTo(2);
			assertThat(result[0]).isEqualTo("a");
			assertThat(result[1]).isEqualTo("b");
		}
	}

	@Test
	public void testMatchBeforePossibleMatch() {
		MdmRulesJson mdmRulesJson = new MdmRulesJson();
		MdmMatcherJson matcherJson = new MdmMatcherJson().setAlgorithm(MatchTypeEnum.STRING);
		mdmRulesJson.addMatchField(new MdmFieldMatchJson().setName("given").setResourceType("Patient").setResourcePath("name.given").setMatcher(matcherJson));
		mdmRulesJson.addMatchField(new MdmFieldMatchJson().setName("family").setResourceType("Patient").setResourcePath("name.family").setMatcher(matcherJson));
		mdmRulesJson.addMatchField(new MdmFieldMatchJson().setName("prefix").setResourceType("Patient").setResourcePath("name.prefix").setMatcher(matcherJson));
		mdmRulesJson.putMatchResult("given,family", MdmMatchResultEnum.MATCH);
		mdmRulesJson.putMatchResult("given", MdmMatchResultEnum.POSSIBLE_MATCH);

		VectorMatchResultMap map = new VectorMatchResultMap(mdmRulesJson);
		assertThat(map.get(1L)).isEqualTo(MdmMatchResultEnum.POSSIBLE_MATCH);
		assertThat(map.get(3L)).isEqualTo(MdmMatchResultEnum.MATCH);
		assertThat(map.get(7L)).isEqualTo(MdmMatchResultEnum.MATCH);
	}
}
