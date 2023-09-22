package ca.uhn.fhir.jpa.mdm;

import ca.uhn.fhir.jpa.mdm.models.GenerateMetricsTestParameters;
import ca.uhn.fhir.jpa.mdm.models.LinkMetricTestParameters;
import ca.uhn.fhir.jpa.mdm.models.LinkScoreMetricTestParams;
import ca.uhn.fhir.jpa.mdm.models.ResourceMetricTestParams;
import ca.uhn.fhir.jpa.mdm.util.MdmMetricSvcTestUtil;
import ca.uhn.fhir.mdm.api.IMdmMetricSvc;
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.api.params.GenerateMdmMetricsParameters;
import ca.uhn.fhir.mdm.model.MdmMetrics;
import ca.uhn.fhir.mdm.model.MdmResourceMetrics;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests the various metrics returned by IMdmMetricSvc
 * Because of the way these metrics are broken down in 3 different ways,
 * these results are tested separately, even though there is a single
 * entry point.
 */
public interface IMdmMetricSvcTest {

	IMdmMetricSvc getMetricsSvc();

	void generateMdmMetricsSetup(GenerateMetricsTestParameters theParameters);

	@Test
	default void generateMdmMetrics_generalTest_happyPath() {
		// setup
		GenerateMetricsTestParameters testParameters = new GenerateMetricsTestParameters();
		testParameters.setInitialState(MdmMetricSvcTestUtil.OUR_BASIC_STATE);
		testParameters.setScores(Arrays.asList(0.1, 0.2, 0.3, 0.4));

		generateMdmMetricsSetup(testParameters);

		// test
		GenerateMdmMetricsParameters parameters = new GenerateMdmMetricsParameters("Patient");
		MdmMetrics results = getMetricsSvc().generateMdmMetrics(parameters);

		// verify
		assertNotNull(results);
		assertEquals("Patient", results.getResourceType());
		assertEquals(4, results.getGoldenResourcesCount());
		assertEquals(4, results.getSourceResourcesCount());
		assertEquals(0, results.getExcludedResources());

		Map<MdmMatchResultEnum, Map<MdmLinkSourceEnum, Long>> map = results.getMatchTypeToLinkToCountMap();
		// See OUR_BASIC_STATE
		assertEquals(3, map.size());
		for (MdmMatchResultEnum matchResult : new MdmMatchResultEnum[] { MdmMatchResultEnum.MATCH, MdmMatchResultEnum.NO_MATCH, MdmMatchResultEnum.POSSIBLE_MATCH }) {
			assertTrue(map.containsKey(matchResult));
			Map<MdmLinkSourceEnum, Long> source2Count = map.get(matchResult);
			assertNotNull(source2Count);
			for (MdmLinkSourceEnum ls : MdmLinkSourceEnum.values()) {
				assertNotNull(source2Count.get(ls));
			}
		}
	}

	void generateLinkMetricsSetup(LinkMetricTestParameters theParameters);

	@ParameterizedTest
	@MethodSource("ca.uhn.fhir.jpa.mdm.util.MdmMetricSvcTestUtil#linkMetricsParameters")
	default void test_generateLinkMetrics_multipleInputs(LinkMetricTestParameters theParameters) {
		// setup
		generateLinkMetricsSetup(theParameters);

		// all tests use Patient resource type
		GenerateMdmMetricsParameters parameters = new GenerateMdmMetricsParameters("Patient");
		for (MdmLinkSourceEnum linkSource : theParameters.getLinkSourceFilters()) {
			parameters.addLinkSource(linkSource);
		}
		for (MdmMatchResultEnum matchResultEnum : theParameters.getMatchFilters()) {
			parameters.addMatchResult(matchResultEnum);
		}

		// test
		MdmMetrics metrics = getMetricsSvc().generateMdmMetrics(parameters);

		// verify
		assertNotNull(metrics);
		assertEquals(metrics.getResourceType(), "Patient");

		MdmMetrics expectedMetrics = theParameters.getExpectedMetrics();

		Supplier<String> err = () -> getComparingMetrics(metrics, expectedMetrics);

		Map<MdmMatchResultEnum, Map<MdmLinkSourceEnum, Long>> actual = metrics.getMatchTypeToLinkToCountMap();
		Map<MdmMatchResultEnum, Map<MdmLinkSourceEnum, Long>> expected = expectedMetrics.getMatchTypeToLinkToCountMap();
		assertEquals(expected, actual, err.get());

		for (MdmMatchResultEnum matchResult : MdmMatchResultEnum.values()) {
			assertEquals(expected.containsKey(matchResult), actual.containsKey(matchResult), err.get());
			if (actual.containsKey(matchResult)) {
				Map<MdmLinkSourceEnum, Long> actualMatch = actual.get(matchResult);
				Map<MdmLinkSourceEnum, Long> expectedMatch = expected.get(matchResult);
				assertEquals(expectedMatch, actualMatch, err.get());
				for (MdmLinkSourceEnum linkSource : MdmLinkSourceEnum.values()) {
					assertEquals(expectedMatch.get(linkSource), actualMatch.get(linkSource), err.get());
				}
			}
		}
	}

	void generateResourceMetricsSetup(ResourceMetricTestParams theParams);

	@ParameterizedTest
	@MethodSource("ca.uhn.fhir.jpa.mdm.util.MdmMetricSvcTestUtil#resourceMetricParameters")
	default void test_generateResourceMetrics_multipleInputs(ResourceMetricTestParams theParams) {
		// setup
		generateResourceMetricsSetup(theParams);

		// test
		GenerateMdmMetricsParameters parameters = new GenerateMdmMetricsParameters("Patient");
		MdmResourceMetrics results = getMetricsSvc().generateMdmMetrics(parameters);

		// verify
		assertNotNull(results);
		assertEquals("Patient", results.getResourceType());
		assertEquals(
				theParams.getExpectedResourceCount(),
				results.getSourceResourcesCount() + results.getGoldenResourcesCount());
		assertEquals(theParams.getExpectedBlockedResourceCount(), results.getExcludedResources());
		assertEquals(theParams.getExpectedGoldenResourceCount(), results.getGoldenResourcesCount());
	}

	void generateLinkScoreMetricsSetup(LinkScoreMetricTestParams theParams);

	@ParameterizedTest
	@MethodSource("ca.uhn.fhir.jpa.mdm.util.MdmMetricSvcTestUtil#linkScoreParameters")
	default void test_generateLinkScoreMetrics_multipleInputs(LinkScoreMetricTestParams theParams) {
		// setup
		generateLinkScoreMetricsSetup(theParams);

		GenerateMdmMetricsParameters scoreMetricsParameters = new GenerateMdmMetricsParameters("Patient");
		for (MdmMatchResultEnum matchType : theParams.getMatchFilter()) {
			scoreMetricsParameters.addMatchResult(matchType);
		}

		// test
		MdmMetrics actualMetrics = getMetricsSvc().generateMdmMetrics(scoreMetricsParameters);

		// verify
		assertNotNull(actualMetrics);
		assertEquals("Patient", actualMetrics.getResourceType());

		MdmMetrics expectedMetrics = theParams.getExpectedMetrics();

		Map<String, Long> actual = actualMetrics.getScoreCounts();
		Map<String, Long> expected = expectedMetrics.getScoreCounts();
		assertEquals(expected.size(), actual.size());
		for (String score : expected.keySet()) {
			assertTrue(actual.containsKey(score), String.format("Score of %s is not in results", score));
			assertEquals(expected.get(score), actual.get(score));
		}
	}

	private String getComparingMetrics(MdmMetrics theActual, MdmMetrics theExpected) {
		return String.format(
				"\nExpected: \n%s - \nActual: \n%s", getStringMetrics(theExpected), getStringMetrics(theActual));
	}

	String getStringMetrics(MdmMetrics theMetrics);
}
