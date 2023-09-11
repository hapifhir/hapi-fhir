package ca.uhn.fhir.jpa.mdm.dao;

import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.config.HapiFhirLocalContainerEntityManagerFactoryBean;
import ca.uhn.fhir.jpa.dao.data.IMdmLinkJpaMetricsRepository;
import ca.uhn.fhir.jpa.dao.mdm.MdmMetricSvcJpaImpl;
import ca.uhn.fhir.jpa.mdm.BaseMdmR4Test;
import ca.uhn.fhir.jpa.mdm.helper.MdmLinkHelper;
import ca.uhn.fhir.jpa.mdm.helper.testmodels.MDMState;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.mdm.api.IMdmMetricSvc;
import ca.uhn.fhir.mdm.api.IMdmResourceDaoSvc;
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.api.parameters.GenerateMdmLinkMetricParameters;
import ca.uhn.fhir.mdm.api.parameters.GenerateMdmResourceMetricsParameters;
import ca.uhn.fhir.mdm.model.MdmLinkMetrics;
import ca.uhn.fhir.mdm.model.MdmResourceMetrics;
import ca.uhn.fhir.mdm.util.MdmResourceUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

@ContextConfiguration(classes = {
	MdmMetricSvcJpaIT.TestConfig.class
})
public class MdmMetricSvcJpaIT extends BaseMdmR4Test {

	private static final Logger ourLog = LoggerFactory.getLogger(MdmMetricSvcJpaIT.class);

	@Configuration
	public static class TestConfig {

		@Autowired
		@Qualifier("metricsRepository")
		private IMdmLinkJpaMetricsRepository myJpaRepository;

		@Autowired
		private IMdmResourceDaoSvc myIMdmResourceDaoSvc;

		@Autowired
		private DaoRegistry myDaoRegistry;

		@Autowired
		private HapiFhirLocalContainerEntityManagerFactoryBean myEntityFactory;

		// this has to be provided via spring, or the
		// @Transactional barrier is never invoked
		@Bean
		IMdmMetricSvc mdmMetricSvc() {
			return new MdmMetricSvcJpaImpl(
				myJpaRepository,
				myIMdmResourceDaoSvc,
				myDaoRegistry,
				myEntityFactory
			);
		}
	}

	private static final String ourBasicState = """
					G1, AUTO, MATCH, P1
					G2, AUTO, MATCH, P2,
					G3, AUTO, POSSIBLE_MATCH, P3,
					G4, MANUAL, MATCH, P4
					G2, AUTO, NO_MATCH, P1
					G1, MANUAL, NO_MATCH, P2
					G1, MANUAL, POSSIBLE_MATCH, P3
				""";

	/**
	 * Just a test parameters class for feeding into the tests.
	 */
	public static class LinkMetricTestParameters {
		/**
		 * The initial state (as to be fed into MdmLinkHelper)
		 */
		private String myInitialState;

		/**
		 * The filters for MatchResult
		 */
		private List<MdmMatchResultEnum> myMatchFilters;

		/**
		 * The filters for LinkSource
		 */
		private List<MdmLinkSourceEnum> myLinkSourceEnums;

		/**
		 * The expected metrics to be returned
		 */
		private MdmLinkMetrics myExpectedMetrics;

		public String getInitialState() {
			return myInitialState;
		}

		public void setInitialState(String theInitialState) {
			myInitialState = theInitialState;
		}

		public List<MdmMatchResultEnum> getMatchFilters() {
			if (myMatchFilters == null) {
				myMatchFilters = new ArrayList<>();
			}
			return myMatchFilters;
		}

		public void setMatchFilters(List<MdmMatchResultEnum> theMatchFilters) {
			myMatchFilters = theMatchFilters;
		}

		public List<MdmLinkSourceEnum> getLinkSourceFilters() {
			if (myLinkSourceEnums == null) {
				myLinkSourceEnums = new ArrayList<>();
			}
			return myLinkSourceEnums;
		}

		public void setLinkSourceFilters(List<MdmLinkSourceEnum> theLinkSourceEnums) {
			myLinkSourceEnums = theLinkSourceEnums;
		}

		public MdmLinkMetrics getExpectedMetrics() {
			return myExpectedMetrics;
		}

		public void setExpectedMetrics(MdmLinkMetrics theExpectedMetrics) {
			myExpectedMetrics = theExpectedMetrics;
		}
	}

	public static class ResourceMetricTestParams {
		private String myInitialState;

		private List<String> myBlockedResourceGoldenResourceIds;

		private long myExpectedResourceCount;

		private long myExpectedGoldenResourceCount;

		public String getInitialState() {
			return myInitialState;
		}

		public void setInitialState(String theInitialState) {
			myInitialState = theInitialState;
		}

		public List<String> getBlockedResourceGoldenResourceIds() {
			if (myBlockedResourceGoldenResourceIds == null) {
				myBlockedResourceGoldenResourceIds = new ArrayList<>();
			}
			return myBlockedResourceGoldenResourceIds;
		}

		public void addBlockedResourceGoldenResources(String theBlockedResourceId) {
			getBlockedResourceGoldenResourceIds().add(theBlockedResourceId);
		}

		public long getExpectedResourceCount() {
			return myExpectedResourceCount;
		}

		public void setExpectedResourceCount(long theExpectedResourceCount) {
			myExpectedResourceCount = theExpectedResourceCount;
		}

		public long getExpectedGoldenResourceCount() {
			return myExpectedGoldenResourceCount;
		}

		public void setExpectedGoldenResourceCount(long theExpectedGoldenResourceCount) {
			myExpectedGoldenResourceCount = theExpectedGoldenResourceCount;
		}

		public long getExpectedBlockedResourceCount() {
			return getBlockedResourceGoldenResourceIds().size();
		}
	}

	//////

	private final ObjectMapper myObjectMapper = new ObjectMapper();

	@Autowired
	private MdmLinkHelper myLinkHelper;

	@Autowired
	private IMdmMetricSvc mySvc;

	@BeforeEach
	public void before() throws Exception {
		super.before();
	}

	/**
	 * Parameter supplying method
	 */
	private static List<LinkMetricTestParameters> linkMetricsParameters() {
		List<LinkMetricTestParameters> params = new ArrayList<>();

		// 1
		{
			LinkMetricTestParameters testParameters = new LinkMetricTestParameters();
			testParameters.setInitialState(ourBasicState);
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
			testParameters.setInitialState(ourBasicState);
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
			testParameters.setInitialState(ourBasicState);
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
			testParameters.setInitialState(ourBasicState);
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

	@ParameterizedTest
	@MethodSource("linkMetricsParameters")
	public void testMdmMetricsService_multipleInputs(LinkMetricTestParameters theParameters) {
		// setup
		ourLog.info(theParameters.getInitialState());
		if (StringUtils.isNotBlank(theParameters.getInitialState())) {
			// we can only initialize the state if there is a state to initialize
			MDMState<Patient, JpaPid> state = new MDMState<>();
			state.setInputState(theParameters.getInitialState());
			myLinkHelper.setup(state);
		}

		// all tests use Patient resource type
		GenerateMdmLinkMetricParameters parameters = new GenerateMdmLinkMetricParameters("Patient");
		for (MdmLinkSourceEnum linkSource : theParameters.getLinkSourceFilters()) {
			parameters.addLinkSourceFilter(linkSource);
		}
		for (MdmMatchResultEnum matchResultEnum : theParameters.getMatchFilters()) {
			parameters.addMatchResultFilter(matchResultEnum);
		}

		// test
		MdmLinkMetrics metrics = mySvc.generateLinkMetrics(parameters);

		// verify
		assertNotNull(metrics);

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

	private static List<ResourceMetricTestParams> resourceMetricParameters() {
		List<ResourceMetricTestParams> params = new ArrayList<>();

		// 1
		{
			// a mix of golden, regular, and blocked resources
			ResourceMetricTestParams p = new ResourceMetricTestParams();
			p.setInitialState("""
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

	@ParameterizedTest
	@MethodSource("resourceMetricParameters")
	public void testResourceMetrics(ResourceMetricTestParams theParams) {
		// setup
		MDMState<Patient, JpaPid> state = new MDMState<>();
		String initialState = theParams.getInitialState();
		if (StringUtils.isNotBlank(initialState)) {
			state.setInputState(initialState);

			for (String forcedBlockedGRId : theParams.getBlockedResourceGoldenResourceIds()) {
				Patient gr = new Patient();
				gr.setActive(true);
				gr.setId("Patient/" + forcedBlockedGRId);
				MdmResourceUtil.setMdmManaged(gr);
				MdmResourceUtil.setGoldenResource(gr);
				MdmResourceUtil.setGoldenResourceAsBlockedResourceGoldenResource(gr);

				Patient p = createPatient(gr, true, false);
				state.addParameter(forcedBlockedGRId, p);
			}

			myLinkHelper.setup(state);
		}

		GenerateMdmResourceMetricsParameters resourceMetricsParameters = new GenerateMdmResourceMetricsParameters("Patient");

		// test
		MdmResourceMetrics results = mySvc.generateResourceMetrics(resourceMetricsParameters);

		// verify
		assertNotNull(results);
		assertEquals("Patient", results.getResourceType());
		assertEquals(theParams.getExpectedResourceCount(), results.getSourceResourcesCount() + results.getGoldenResourcesCount());
		assertEquals(theParams.getExpectedBlockedResourceCount(), results.getExcludedResources());
		assertEquals(theParams.getExpectedGoldenResourceCount(), results.getGoldenResourcesCount());
	}

	private String getComparingMetrics(MdmLinkMetrics theActual, MdmLinkMetrics theExpected) {
		return String.format("\nExpected: \n%s - \nActual: \n%s", getStringMetrics(theExpected), getStringMetrics(theActual));
	}

	private String getStringMetrics(MdmLinkMetrics theMetrics) {
		try {
			return myObjectMapper.writeValueAsString(theMetrics);
		} catch (JsonProcessingException ex) {
			// we've failed anyway - we might as well display the exception
			fail(ex);
			return "NOT PARSEABLE!";
		}
	}
}
