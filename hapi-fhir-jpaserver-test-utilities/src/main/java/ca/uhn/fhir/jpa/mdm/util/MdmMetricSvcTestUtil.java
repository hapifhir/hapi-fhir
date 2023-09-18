package ca.uhn.fhir.jpa.mdm.util;

import ca.uhn.fhir.jpa.mdm.models.LinkMetricTestParameters;
import ca.uhn.fhir.jpa.mdm.models.LinkScoreMetricTestParams;
import ca.uhn.fhir.jpa.mdm.models.ResourceMetricTestParams;
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.model.MdmLinkMetrics;
import ca.uhn.fhir.mdm.model.MdmLinkScoreMetrics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
			MdmLinkMetrics metrics = new MdmLinkMetrics();
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
			MdmLinkMetrics metrics = new MdmLinkMetrics();
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
			MdmLinkMetrics metrics = new MdmLinkMetrics();
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
			MdmLinkMetrics metrics = new MdmLinkMetrics();
			metrics.addMetric(MdmMatchResultEnum.MATCH, MdmLinkSourceEnum.MANUAL, 1);
			testParameters.setExpectedMetrics(metrics);
			params.add(testParameters);
		}

		// 5
		{
			// no initial state
			LinkMetricTestParameters testParameters = new LinkMetricTestParameters();
			testParameters.setInitialState("");
			MdmLinkMetrics metrics = new MdmLinkMetrics();
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
			testParameters.setExpectedMetrics(new MdmLinkMetrics());
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
			MdmLinkMetrics metrics = new MdmLinkMetrics();
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
			p.setScores(Arrays.asList(2D, 2D, 1D));
			MdmLinkScoreMetrics metrics = new MdmLinkScoreMetrics();
			metrics.setResourceType("Patient");
			metrics.addScore("2.0", 2L);
			metrics.addScore("1.0", 1L);
			p.setExpectedLinkDataMetrics(metrics);
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
			p.setScores(Arrays.asList(null, 1D));
			MdmLinkScoreMetrics metrics = new MdmLinkScoreMetrics();
			metrics.setResourceType("Patient");
			metrics.addScore("NULL", 1L);
			metrics.addScore("1.0", 1L);
			p.setExpectedLinkDataMetrics(metrics);
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
			p.setScores(Arrays.asList(2D, 2D, 1D, 3D));
			p.addMatchType(MdmMatchResultEnum.POSSIBLE_MATCH);
			MdmLinkScoreMetrics metrics = new MdmLinkScoreMetrics();
			metrics.setResourceType("Patient");
			metrics.addScore("2.0", 1L);
			metrics.addScore("1.0", 1L);
			p.setExpectedLinkDataMetrics(metrics);
			parameters.add(p);
		}

		// 4
		{
			// no links
			LinkScoreMetricTestParams p = new LinkScoreMetricTestParams();
			p.setInitialState("");
			MdmLinkScoreMetrics metrics = new MdmLinkScoreMetrics();
			metrics.setResourceType("Patient");
			p.setExpectedLinkDataMetrics(metrics);
			parameters.add(p);
		}

		return parameters;
	}
}
