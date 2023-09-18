package ca.uhn.fhir.jpa.mdm;

import ca.uhn.fhir.jpa.mdm.models.LinkMetricTestParameters;
import ca.uhn.fhir.jpa.mdm.models.LinkScoreMetricTestParams;
import ca.uhn.fhir.jpa.mdm.models.ResourceMetricTestParams;
import ca.uhn.fhir.mdm.api.IMdmMetricSvc;
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.api.params.GenerateMdmLinkMetricParameters;
import ca.uhn.fhir.mdm.api.params.GenerateMdmResourceMetricsParameters;
import ca.uhn.fhir.mdm.api.params.GenerateScoreMetricsParameters;
import ca.uhn.fhir.mdm.model.MdmLinkMetrics;
import ca.uhn.fhir.mdm.model.MdmLinkScoreMetrics;
import ca.uhn.fhir.mdm.model.MdmResourceMetrics;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Map;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public interface IMdmMetricSvcTest {

	IMdmMetricSvc getMetricsSvc();

	void generateLinkMetricsSetup(LinkMetricTestParameters theParameters);

	@ParameterizedTest
	@MethodSource("ca.uhn.fhir.jpa.mdm.util.MdmMetricSvcTestUtil#linkMetricsParameters")
	default void test_generateLinkMetrics_multipleInputs(LinkMetricTestParameters theParameters) {
		// setup
		generateLinkMetricsSetup(theParameters);

		// all tests use Patient resource type
		GenerateMdmLinkMetricParameters parameters = new GenerateMdmLinkMetricParameters("Patient");
		for (MdmLinkSourceEnum linkSource : theParameters.getLinkSourceFilters()) {
			parameters.addLinkSourceFilter(linkSource);
		}
		for (MdmMatchResultEnum matchResultEnum : theParameters.getMatchFilters()) {
			parameters.addMatchResultFilter(matchResultEnum);
		}

		// test
		MdmLinkMetrics metrics = getMetricsSvc().generateLinkMetrics(parameters);

		// verify
		assertNotNull(metrics);
		assertEquals(metrics.getResourceType(), "Patient");

		MdmLinkMetrics expectedMetrics = theParameters.getExpectedMetrics();

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

		GenerateMdmResourceMetricsParameters resourceMetricsParameters =
				new GenerateMdmResourceMetricsParameters("Patient");

		// test
		MdmResourceMetrics results = getMetricsSvc().generateResourceMetrics(resourceMetricsParameters);

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

		GenerateScoreMetricsParameters scoreMetricsParameters = new GenerateScoreMetricsParameters("Patient");
		for (MdmMatchResultEnum matchType : theParams.getMatchFilter()) {
			scoreMetricsParameters.addMatchType(matchType);
		}
		// test
		MdmLinkScoreMetrics actualMetrics = getMetricsSvc().generateLinkScoreMetrics(scoreMetricsParameters);

		// verify
		assertNotNull(actualMetrics);
		assertEquals("Patient", actualMetrics.getResourceType());

		MdmLinkScoreMetrics expectedMetrics = theParams.getExpectedLinkDataMetrics();

		Map<String, Long> actual = actualMetrics.getScoreCounts();
		Map<String, Long> expected = expectedMetrics.getScoreCounts();
		assertEquals(expected.size(), actual.size());
		for (String score : expected.keySet()) {
			assertTrue(actual.containsKey(score), String.format("Score of %s is not in results", score));
			assertEquals(expected.get(score), actual.get(score));
		}
	}

	private String getComparingMetrics(MdmLinkMetrics theActual, MdmLinkMetrics theExpected) {
		return String.format(
				"\nExpected: \n%s - \nActual: \n%s", getStringMetrics(theExpected), getStringMetrics(theActual));
	}

	String getStringMetrics(MdmLinkMetrics theMetrics);
}
