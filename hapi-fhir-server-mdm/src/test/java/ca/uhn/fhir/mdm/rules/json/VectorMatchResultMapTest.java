package ca.uhn.fhir.mdm.rules.json;

import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.rules.matcher.models.MatchTypeEnum;
import ca.uhn.fhir.mdm.rules.matcher.util.MatchRuleUtil;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
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

	/**
	 * One map is shared by every MDM thread, so looking up vectors not seen before must be safe from many threads at
	 * once.
	 */
	@Test
	void get_fromManyThreadsAtOnce_returnsCorrectResults() throws Exception {
		MdmRulesJson mdmRulesJson = new MdmRulesJson();
		mdmRulesJson.setVersion("1");
		MdmMatcherJson matcherJson = new MdmMatcherJson().setAlgorithm(MatchTypeEnum.STRING);
		mdmRulesJson.addMatchField(new MdmFieldMatchJson().setName("given").setResourceType("Patient").setResourcePath("name.given").setMatcher(matcherJson));
		mdmRulesJson.addMatchField(new MdmFieldMatchJson().setName("family").setResourceType("Patient").setResourcePath("name.family").setMatcher(matcherJson));
		mdmRulesJson.putMatchResult("given,family", MdmMatchResultEnum.MATCH);
		mdmRulesJson.putMatchResult("given", MdmMatchResultEnum.POSSIBLE_MATCH);
		VectorMatchResultMap map = new VectorMatchResultMap(mdmRulesJson);

		int threadCount = 8;
		int vectorsPerThread = 50_000;
		CyclicBarrier startTogether = new CyclicBarrier(threadCount);
		ExecutorService executor = Executors.newFixedThreadPool(threadCount);
		try {
			List<Future<?>> futures = new ArrayList<>();
			for (int t = 0; t < threadCount; t++) {
				long firstVector = (long) t * vectorsPerThread;
				futures.add(executor.submit(() -> {
					startTogether.await();
					for (long vector = firstVector; vector < firstVector + vectorsPerThread; vector++) {
						assertThat(map.get(vector)).as("vector %s", vector).isEqualTo(expectedResult(vector));
					}
					return null;
				}));
			}
			for (Future<?> future : futures) {
				future.get(60, TimeUnit.SECONDS);
			}
		} finally {
			executor.shutdownNow();
		}
	}

	private static MdmMatchResultEnum expectedResult(long theVector) {
		if ((theVector & 3L) == 3L) {
			return MdmMatchResultEnum.MATCH;
		}
		if ((theVector & 1L) == 1L) {
			return MdmMatchResultEnum.POSSIBLE_MATCH;
		}
		return MdmMatchResultEnum.NO_MATCH;
	}
}
