package ca.uhn.fhir.jpa.mdm.dao;

import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.config.HapiFhirLocalContainerEntityManagerFactoryBean;
import ca.uhn.fhir.jpa.dao.data.IMdmLinkJpaMetricsRepository;
import ca.uhn.fhir.jpa.dao.mdm.MdmMetricSvcJpaImpl;
import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.jpa.mdm.BaseMdmR4Test;
import ca.uhn.fhir.jpa.mdm.IMdmMetricSvcTest;
import ca.uhn.fhir.jpa.mdm.helper.MdmLinkHelper;
import ca.uhn.fhir.jpa.mdm.helper.testmodels.MDMState;
import ca.uhn.fhir.jpa.mdm.models.GenerateMetricsTestParameters;
import ca.uhn.fhir.jpa.mdm.models.LinkMetricTestParameters;
import ca.uhn.fhir.jpa.mdm.models.LinkScoreMetricTestParams;
import ca.uhn.fhir.jpa.mdm.models.ResourceMetricTestParams;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.mdm.api.IMdmMetricSvc;
import ca.uhn.fhir.mdm.model.MdmMetrics;
import ca.uhn.fhir.mdm.util.MdmResourceUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManagerFactory;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;


@ContextConfiguration(classes = {
	MdmMetricSvcJpaIT.TestConfig.class
})
public class MdmMetricSvcJpaIT extends BaseMdmR4Test implements IMdmMetricSvcTest {

	private static final Logger ourLog = LoggerFactory.getLogger(MdmMetricSvcJpaIT.class);

	@Configuration
	public static class TestConfig {

		@Autowired
		@Qualifier("metricsRepository")
		private IMdmLinkJpaMetricsRepository myJpaRepository;

		@Autowired
		private DaoRegistry myDaoRegistry;

		@Autowired
		private EntityManagerFactory myEntityManagerFactory;

		@Autowired
		private HapiFhirLocalContainerEntityManagerFactoryBean myEntityFactory;

		// this has to be provided via spring, or the
		// @Transactional barrier is never invoked
		@Bean
		IMdmMetricSvc mdmMetricSvc() {
			return new MdmMetricSvcJpaImpl(
				myJpaRepository,
				myDaoRegistry,
				myEntityManagerFactory
			);
		}
	}

	private final ObjectMapper myObjectMapper = new ObjectMapper();

	@Autowired
	private MdmLinkHelper myLinkHelper;

	@Autowired
	private IMdmMetricSvc mySvc;

	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();
	}

	@Override
	public IMdmMetricSvc getMetricsSvc() {
		return mySvc;
	}

	@Override
	public void generateMdmMetricsSetup(GenerateMetricsTestParameters theParameters) {
		if (StringUtils.isNotBlank(theParameters.getInitialState())) {
			MDMState<Patient, JpaPid> state = new MDMState<>();
			state.setInputState(theParameters.getInitialState());
			myLinkHelper.setup(state);

			// update scores if needed
			setupScores(theParameters.getScores());
		}
	}

	@Override
	public void generateLinkMetricsSetup(LinkMetricTestParameters theParameters) {
		ourLog.info(theParameters.getInitialState());
		if (StringUtils.isNotBlank(theParameters.getInitialState())) {
			// we can only initialize the state if there is a state to initialize
			MDMState<Patient, JpaPid> state = new MDMState<>();
			state.setInputState(theParameters.getInitialState());
			myLinkHelper.setup(state);
		}
	}

	@Override
	public void generateResourceMetricsSetup(ResourceMetricTestParams theParams) {
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
	}

	@Override
	public void generateLinkScoreMetricsSetup(LinkScoreMetricTestParams theParams) {
		MDMState<Patient, JpaPid> state = new MDMState<>();
		String initialState = theParams.getInitialState();

		if (StringUtils.isNotBlank(initialState)) {
			state.setInputState(initialState);

			myLinkHelper.setup(state);

			// update scores if needed
			setupScores(theParams.getScores());
		}
	}

	private void setupScores(List<Double> theParams) {
		List<MdmLink> links = myMdmLinkDao.findAll();
		for (int i = 0; i < theParams.size() && i < links.size(); i++) {
			Double score = theParams.get(i);
			MdmLink link = links.get(i);
			link.setScore(score);
			myMdmLinkDao.save(link);
		}
	}

	@Override
	public String getStringMetrics(MdmMetrics theMetrics) {
		try {
			return myObjectMapper.writeValueAsString(theMetrics);
		} catch (JsonProcessingException ex) {
			// we've failed anyway - we might as well display the exception
			fail(ex);
			return "NOT PARSEABLE!";
		}
	}
}
