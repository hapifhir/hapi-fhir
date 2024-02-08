/*-
 * #%L
 * HAPI FHIR JPA Server Test Utilities
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
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

import static org.assertj.core.api.Assertions.assertThat;

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
		assertThat(results).isNotNull();
		assertThat(results.getResourceType()).isEqualTo("Patient");
		assertThat(results.getGoldenResourcesCount()).isEqualTo(4);
		assertThat(results.getSourceResourcesCount()).isEqualTo(4);
		assertThat(results.getExcludedResources()).isEqualTo(0);

		Map<MdmMatchResultEnum, Map<MdmLinkSourceEnum, Long>> map = results.getMatchTypeToLinkToCountMap();
		// See OUR_BASIC_STATE
		assertThat(map.size()).isEqualTo(3);
		for (MdmMatchResultEnum matchResult : new MdmMatchResultEnum[] {
			MdmMatchResultEnum.MATCH, MdmMatchResultEnum.NO_MATCH, MdmMatchResultEnum.POSSIBLE_MATCH
		}) {
			assertThat(map.containsKey(matchResult)).isTrue();
			Map<MdmLinkSourceEnum, Long> source2Count = map.get(matchResult);
			assertThat(source2Count).isNotNull();
			for (MdmLinkSourceEnum ls : MdmLinkSourceEnum.values()) {
				assertThat(source2Count.get(ls)).isNotNull();
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
		assertThat(metrics).isNotNull();
		assertThat("Patient").isEqualTo(metrics.getResourceType());

		MdmMetrics expectedMetrics = theParameters.getExpectedMetrics();

		Supplier<String> err = () -> getComparingMetrics(metrics, expectedMetrics);

		Map<MdmMatchResultEnum, Map<MdmLinkSourceEnum, Long>> actual = metrics.getMatchTypeToLinkToCountMap();
		Map<MdmMatchResultEnum, Map<MdmLinkSourceEnum, Long>> expected = expectedMetrics.getMatchTypeToLinkToCountMap();
		assertThat(actual).as(err.get()).isEqualTo(expected);

		for (MdmMatchResultEnum matchResult : MdmMatchResultEnum.values()) {
			assertThat(actual.containsKey(matchResult)).as(err.get()).isEqualTo(expected.containsKey(matchResult));
			if (actual.containsKey(matchResult)) {
				Map<MdmLinkSourceEnum, Long> actualMatch = actual.get(matchResult);
				Map<MdmLinkSourceEnum, Long> expectedMatch = expected.get(matchResult);
				assertThat(actualMatch).as(err.get()).isEqualTo(expectedMatch);
				for (MdmLinkSourceEnum linkSource : MdmLinkSourceEnum.values()) {
					assertThat(actualMatch.get(linkSource)).as(err.get()).isEqualTo(expectedMatch.get(linkSource));
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
		assertThat(results).isNotNull();
		assertThat(results.getResourceType()).isEqualTo("Patient");
		assertThat(results.getSourceResourcesCount() + results.getGoldenResourcesCount()).isEqualTo(theParams.getExpectedResourceCount());
		assertThat(results.getExcludedResources()).isEqualTo(theParams.getExpectedBlockedResourceCount());
		assertThat(results.getGoldenResourcesCount()).isEqualTo(theParams.getExpectedGoldenResourceCount());
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
		assertThat(actualMetrics).isNotNull();
		assertThat(actualMetrics.getResourceType()).isEqualTo("Patient");

		MdmMetrics expectedMetrics = theParams.getExpectedMetrics();

		Map<String, Long> actual = actualMetrics.getScoreCounts();
		Map<String, Long> expected = expectedMetrics.getScoreCounts();
		assertThat(actual.size()).isEqualTo(expected.size());
		for (String score : expected.keySet()) {
			assertThat(actual.containsKey(score)).as(String.format("Score of %s is not in results", score)).isTrue();
			assertThat(actual.get(score)).as(score).isEqualTo(expected.get(score));
		}
	}

	private String getComparingMetrics(MdmMetrics theActual, MdmMetrics theExpected) {
		return String.format(
				"\nExpected: \n%s - \nActual: \n%s", getStringMetrics(theExpected), getStringMetrics(theActual));
	}

	String getStringMetrics(MdmMetrics theMetrics);
}
