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
package ca.uhn.fhir.jpa.mdm.util;

import ca.uhn.fhir.jpa.mdm.models.LinkMetricTestParameters;
import ca.uhn.fhir.jpa.mdm.models.LinkScoreMetricTestParams;
import ca.uhn.fhir.jpa.mdm.models.ResourceMetricTestParams;
import ca.uhn.fhir.mdm.api.BaseMdmMetricSvc;
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.model.MdmMetrics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * This provides parameter methods for the {@link ca.uhn.fhir.jpa.mdm.IMdmMetricSvcTest}.
 */
public class MdmMetricSvcTestUtil {

	public static final String OUR_BASIC_STATE =
			"""
					G1, AUTO, MATCH, P1
					G2, AUTO, MATCH, P2,
					G3, AUTO, POSSIBLE_MATCH, P3,
					G4, MANUAL, MATCH, P4
					G2, AUTO, NO_MATCH, P1
					G1, MANUAL, NO_MATCH, P2
					G1, MANUAL, POSSIBLE_MATCH, P3
				""";

	/**
	 * Parameters supplied to {@link ca.uhn.fhir.jpa.mdm.IMdmMetricSvcTest#test_generateLinkMetrics_multipleInputs(LinkMetricTestParameters)}
	 */
	public static List<LinkMetricTestParameters> linkMetricsParameters() {
		List<LinkMetricTestParameters> params = new ArrayList<>();

		// 1
		{
			LinkMetricTestParameters testParameters = new LinkMetricTestParameters();
			testParameters.setInitialState(OUR_BASIC_STATE);
			MdmMetrics metrics = new MdmMetrics();
			metrics.addMetric(MdmMatchResultEnum.MATCH, MdmLinkSourceEnum.AUTO, 2);
			metrics.addMetric(MdmMatchResultEnum.MATCH, MdmLinkSourceEnum.MANUAL, 1);
			metrics.addMetric(MdmMatchResultEnum.NO_MATCH, MdmLinkSourceEnum.AUTO, 1);
			metrics.addMetric(MdmMatchResultEnum.NO_MATCH, MdmLinkSourceEnum.MANUAL, 1);
			metrics.addMetric(MdmMatchResultEnum.POSSIBLE_MATCH, MdmLinkSourceEnum.AUTO, 1);
			metrics.addMetric(MdmMatchResultEnum.POSSIBLE_MATCH, MdmLinkSourceEnum.MANUAL, 1);
			testParameters.setExpectedMetrics(metrics);
			params.add(testParameters);
		}

		// 2
		{
			// link source filter
			LinkMetricTestParameters testParameters = new LinkMetricTestParameters();
			testParameters.setInitialState(OUR_BASIC_STATE);
			testParameters.setLinkSourceFilters(Arrays.asList(MdmLinkSourceEnum.AUTO));
			MdmMetrics metrics = new MdmMetrics();
			metrics.addMetric(MdmMatchResultEnum.MATCH, MdmLinkSourceEnum.AUTO, 2);
			metrics.addMetric(MdmMatchResultEnum.NO_MATCH, MdmLinkSourceEnum.AUTO, 1);
			metrics.addMetric(MdmMatchResultEnum.POSSIBLE_MATCH, MdmLinkSourceEnum.AUTO, 1);
			testParameters.setExpectedMetrics(metrics);
			params.add(testParameters);
		}

		// 3
		{
			// match result filter
			LinkMetricTestParameters testParameters = new LinkMetricTestParameters();
			testParameters.setInitialState(OUR_BASIC_STATE);
			testParameters.setMatchFilters(Arrays.asList(MdmMatchResultEnum.MATCH, MdmMatchResultEnum.POSSIBLE_MATCH));
			MdmMetrics metrics = new MdmMetrics();
			metrics.addMetric(MdmMatchResultEnum.MATCH, MdmLinkSourceEnum.AUTO, 2);
			metrics.addMetric(MdmMatchResultEnum.MATCH, MdmLinkSourceEnum.MANUAL, 1);
			metrics.addMetric(MdmMatchResultEnum.POSSIBLE_MATCH, MdmLinkSourceEnum.AUTO, 1);
			metrics.addMetric(MdmMatchResultEnum.POSSIBLE_MATCH, MdmLinkSourceEnum.MANUAL, 1);
			testParameters.setExpectedMetrics(metrics);
			params.add(testParameters);
		}

		// 4
		{
			// match result and link source filters
			LinkMetricTestParameters testParameters = new LinkMetricTestParameters();
			testParameters.setInitialState(OUR_BASIC_STATE);
			testParameters.setMatchFilters(Arrays.asList(MdmMatchResultEnum.MATCH));
			testParameters.setLinkSourceFilters(Arrays.asList(MdmLinkSourceEnum.MANUAL));
			MdmMetrics metrics = new MdmMetrics();
			metrics.addMetric(MdmMatchResultEnum.MATCH, MdmLinkSourceEnum.MANUAL, 1);
			testParameters.setExpectedMetrics(metrics);
			params.add(testParameters);
		}

		// 5
		{
			// no initial state
			LinkMetricTestParameters testParameters = new LinkMetricTestParameters();
			testParameters.setInitialState("");
			MdmMetrics metrics = new MdmMetrics();
			testParameters.setExpectedMetrics(metrics);
			params.add(testParameters);
		}

		// 6
		{
			// initial state with filters to omit all values
			LinkMetricTestParameters testParameters = new LinkMetricTestParameters();
			testParameters.setInitialState("""
					G1, AUTO, NO_MATCH, P1
					G2, MANUAL, MATCH, P2
				""");
			testParameters.setMatchFilters(Arrays.asList(MdmMatchResultEnum.MATCH));
			testParameters.setLinkSourceFilters(Arrays.asList(MdmLinkSourceEnum.AUTO));
			testParameters.setExpectedMetrics(new MdmMetrics());
			params.add(testParameters);
		}

		// 7
		{
			// initial state with filters to omit some values
			LinkMetricTestParameters testParameters = new LinkMetricTestParameters();
			testParameters.setInitialState("""
					G1, AUTO, NO_MATCH, P1
					G2, MANUAL, MATCH, P2
				""");
			testParameters.setMatchFilters(Arrays.asList(MdmMatchResultEnum.NO_MATCH));
			testParameters.setLinkSourceFilters(Arrays.asList(MdmLinkSourceEnum.AUTO));
			MdmMetrics metrics = new MdmMetrics();
			metrics.addMetric(MdmMatchResultEnum.NO_MATCH, MdmLinkSourceEnum.AUTO, 1);
			testParameters.setExpectedMetrics(metrics);
			params.add(testParameters);
		}

		return params;
	}

	/**
	 * Parameters supplied to {@link ca.uhn.fhir.jpa.mdm.IMdmMetricSvcTest#test_generateResourceMetrics_multipleInputs(ResourceMetricTestParams)}
	 */
	public static List<ResourceMetricTestParams> resourceMetricParameters() {
		List<ResourceMetricTestParams> params = new ArrayList<>();

		// 1
		{
			// a mix of golden, regular, and blocked resources
			ResourceMetricTestParams p = new ResourceMetricTestParams();
			p.setInitialState(
					"""
				G1, AUTO, MATCH, P1
				G2, AUTO, MATCH, P2
				G2, AUTO, MATCH, P1,
				G3, AUTO, MATCH, P3
			""");
			p.addBlockedResourceGoldenResources("G2");
			p.addBlockedResourceGoldenResources("G3");
			p.setExpectedResourceCount(6);
			p.setExpectedGoldenResourceCount(3);
			params.add(p);
		}

		// 2
		{
			// 2 non-golden, 1 golden
			ResourceMetricTestParams p = new ResourceMetricTestParams();
			p.setInitialState("""
				G1, AUTO, MATCH, P1,
				G1, MANUAL, MATCH, P2
			""");
			p.setExpectedResourceCount(3);
			p.setExpectedGoldenResourceCount(1);
			params.add(p);
		}

		// 3
		{
			// 2 golden, 1 non-golden
			ResourceMetricTestParams p = new ResourceMetricTestParams();
			p.setInitialState("""
				G1, AUTO, MATCH, P1
				G2, AUTO, POSSIBLE_DUPLICATE, G1
			""");
			p.setExpectedGoldenResourceCount(2);
			p.setExpectedResourceCount(3);
			params.add(p);
		}

		// 4
		{
			// 2 golden, 1 blocked, 0 non-golden
			ResourceMetricTestParams p = new ResourceMetricTestParams();
			p.setInitialState("""
				G1, AUTO, POSSIBLE_DUPLICATE, G2
			""");
			p.addBlockedResourceGoldenResources("G1");
			p.setExpectedResourceCount(2);
			p.setExpectedGoldenResourceCount(2);
			params.add(p);
		}

		// 5
		{
			// no resources
			ResourceMetricTestParams p = new ResourceMetricTestParams();
			p.setInitialState("");
			params.add(p);
		}

		return params;
	}

	/**
	 * Parameters supplied to {@link ca.uhn.fhir.jpa.mdm.IMdmMetricSvcTest#generateLinkScoreMetricsSetup(LinkScoreMetricTestParams)}
	 */
	public static List<LinkScoreMetricTestParams> linkScoreParameters() {
		List<LinkScoreMetricTestParams> parameters = new ArrayList<>();

		// 1
		{
			// score counts
			LinkScoreMetricTestParams p = new LinkScoreMetricTestParams();
			p.setInitialState(
					"""
				G1, AUTO, MATCH, P1
				G2, AUTO, POSSIBLE_MATCH, P2,
				G3, AUTO, POSSIBLE_MATCH, P1
			""");
			p.setScores(Arrays.asList(.2D, .2D, .1D));
			MdmMetrics metrics = new MdmMetrics();
			metrics.setResourceType("Patient");
			populateScoreIntoMetrics(p, metrics);
			p.setExpectedMetrics(metrics);
			parameters.add(p);
		}

		// 2
		{
			// a null score
			LinkScoreMetricTestParams p = new LinkScoreMetricTestParams();
			p.setInitialState("""
				G1, AUTO, POSSIBLE_MATCH, P1,
				G2, AUTO, POSSIBLE_MATCH, P2
			""");
			p.setScores(Arrays.asList(null, 0.02D));
			MdmMetrics metrics = new MdmMetrics();
			metrics.setResourceType("Patient");
			populateScoreIntoMetrics(p, metrics);
			p.setExpectedMetrics(metrics);
			parameters.add(p);
		}

		// 3
		{
			// match type filtering
			LinkScoreMetricTestParams p = new LinkScoreMetricTestParams();
			p.setInitialState(
					"""
				G1, AUTO, POSSIBLE_MATCH, P1
				G2, AUTO, MATCH, P2
				G3, AUTO, POSSIBLE_MATCH, P3
				G4, AUTO, MATCH, P4
			""");
			p.setScores(Arrays.asList(0.4D, 0.4D, 0.1D, 0.3D));
			p.addMatchType(MdmMatchResultEnum.POSSIBLE_MATCH);
			MdmMetrics metrics = new MdmMetrics();
			metrics.setResourceType("Patient");
			populateScoreIntoMetrics(p, metrics);
			p.setExpectedMetrics(metrics);
			parameters.add(p);
		}

		// 4
		{
			// no links
			LinkScoreMetricTestParams p = new LinkScoreMetricTestParams();
			p.setInitialState("");
			MdmMetrics metrics = new MdmMetrics();
			metrics.setResourceType("Patient");
			p.setExpectedMetrics(metrics);
			populateScoreIntoMetrics(p, metrics);
			parameters.add(p);
		}

		return parameters;
	}

	private static void populateScoreIntoMetrics(LinkScoreMetricTestParams p, MdmMetrics metrics) {
		String initialState = p.getInitialState();
		Map<Integer, MdmMatchResultEnum> indexToMatchResult = new HashMap<>();
		if (isNotBlank(initialState)) {
			String[] states = initialState.split("\n");
			int len = states.length;
			for (int i = 0; i < len; i++) {
				String state = states[i];
				String[] values = state.split(",");
				indexToMatchResult.put(i, MdmMatchResultEnum.valueOf(values[2].trim()));
			}
		}

		Map<Double, Long> score2Count = new HashMap<>();
		long nullCount = 0;
		for (int i = 0; i < p.getScores().size(); i++) {
			MdmMatchResultEnum matchResult = indexToMatchResult.get(i);
			// if it's not a filtered value, add it to the expected metrics
			if (p.getMatchFilter().isEmpty() || p.getMatchFilter().contains(matchResult)) {
				Double d = p.getScores().get(i);
				if (d == null) {
					nullCount++;
				} else {
					if (!score2Count.containsKey(d)) {
						score2Count.put(d, 0L);
					}
					score2Count.put(d, score2Count.get(d) + 1);
				}
			}
		}
		metrics.addScore(BaseMdmMetricSvc.NULL_VALUE, nullCount);
		for (int i = 0; i < BaseMdmMetricSvc.BUCKETS; i++) {
			double bucket = (double) Math.round((float) (100 * (i + 1)) / BaseMdmMetricSvc.BUCKETS) / 100;
			long count = 0;
			// TODO - do not add it if the corresponding link does not have
			// the correct MATCH_RESULT value
			if (score2Count.containsKey(bucket)) {
				count = score2Count.get(bucket);
			}
			if (i == 0) {
				metrics.addScore(String.format(BaseMdmMetricSvc.FIRST_BUCKET, bucket), count);
			} else {
				metrics.addScore(
						String.format(BaseMdmMetricSvc.NTH_BUCKET, (float) i / BaseMdmMetricSvc.BUCKETS, bucket),
						count);
			}
		}
	}
}
