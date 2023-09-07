package ca.uhn.fhir.jpa.mdm.dao;

import ca.uhn.fhir.jpa.config.HapiFhirLocalContainerEntityManagerFactoryBean;
import ca.uhn.fhir.jpa.dao.data.IMdmLinkJpaMetricsRepository;
import ca.uhn.fhir.jpa.dao.mdm.MdmMetricSvcJpaImpl;
import ca.uhn.fhir.jpa.mdm.BaseMdmR4Test;
import ca.uhn.fhir.jpa.mdm.helper.MdmLinkHelper;
import ca.uhn.fhir.jpa.mdm.helper.testmodels.MDMState;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.mdm.api.IMdmMetricSvc;
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.api.parameters.MdmGenerateMetricParameters;
import ca.uhn.fhir.mdm.model.MdmMetrics;
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
import org.springframework.jdbc.core.JdbcTemplate;
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
		private HapiFhirLocalContainerEntityManagerFactoryBean myEntityFactory;

		// this has to be provided via spring, or the
		// @Transactional barrier is never invoked
		@Bean
		IMdmMetricSvc mdmMetricSvc() {
			return new MdmMetricSvcJpaImpl(myJpaRepository, myEntityFactory);
		}
	}

	/**
	 * Just a test parameters class for feeding into the tests.
	 */
	public static class MetricTestParameters {
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
		private MdmMetrics myExpectedMetrics;

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

		public MdmMetrics getExpectedMetrics() {
			return myExpectedMetrics;
		}

		public void setExpectedMetrics(MdmMetrics theExpectedMetrics) {
			myExpectedMetrics = theExpectedMetrics;
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
	private static List<MetricTestParameters> metricsParameters() {
		List<MetricTestParameters> params = new ArrayList<>();
		String basicState = """
					G1, AUTO, MATCH, P1
					G2, AUTO, MATCH, P2,
					G3, AUTO, POSSIBLE_MATCH, P3,
					G4, MANUAL, MATCH, P4
					G2, AUTO, NO_MATCH, P1
					G1, MANUAL, NO_MATCH, P2
					G1, MANUAL, POSSIBLE_MATCH, P3
				""";

		// 1
		{
			MetricTestParameters testParameters = new MetricTestParameters();
			testParameters.setInitialState(basicState);
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
			MetricTestParameters testParameters = new MetricTestParameters();
			testParameters.setInitialState(basicState);
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
			MetricTestParameters testParameters = new MetricTestParameters();
			testParameters.setInitialState(basicState);
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
			MetricTestParameters testParameters = new MetricTestParameters();
			testParameters.setInitialState(basicState);
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
			MetricTestParameters testParameters = new MetricTestParameters();
			testParameters.setInitialState("");
			MdmMetrics metrics = new MdmMetrics();
			testParameters.setExpectedMetrics(metrics);
			params.add(testParameters);
		}

		// 6
		{
			// initial state with filters to omit all values
			MetricTestParameters testParameters = new MetricTestParameters();
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
			MetricTestParameters testParameters = new MetricTestParameters();
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

	@ParameterizedTest
	@MethodSource("metricsParameters")
	public void testMdmMetricsService_multipleInputs(MetricTestParameters theParameters) {
		// setup
		ourLog.info(theParameters.getInitialState());
		if (StringUtils.isNotBlank(theParameters.getInitialState())) {
			// we can only initialize the state if there is a state to initialize
			MDMState<Patient, JpaPid> state = new MDMState<>();
			state.setInputState(theParameters.getInitialState());
			myLinkHelper.setup(state);
		}

		// all tests use Patient resource type
		MdmGenerateMetricParameters parameters = new MdmGenerateMetricParameters("Patient");
		for (MdmLinkSourceEnum linkSource : theParameters.getLinkSourceFilters()) {
			parameters.addLinkSourceFilter(linkSource);
		}
		for (MdmMatchResultEnum matchResultEnum : theParameters.getMatchFilters()) {
			parameters.addMatchResultFilter(matchResultEnum);
		}

		// test
		MdmMetrics metrics = mySvc.generateMetrics(parameters);

		// verify
		assertNotNull(metrics);

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

	private String getComparingMetrics(MdmMetrics theActual, MdmMetrics theExpected) {
		return String.format("\nExpected: \n%s - \nActual: \n%s", getStringMetrics(theExpected), getStringMetrics(theActual));
	}

	private String getStringMetrics(MdmMetrics theMetrics) {
		try {
			return myObjectMapper.writeValueAsString(theMetrics);
		} catch (JsonProcessingException ex) {
			// we've failed anyway - we might as well display the exception
			fail(ex);
			return "NOT PARSEABLE!";
		}
	}
}
