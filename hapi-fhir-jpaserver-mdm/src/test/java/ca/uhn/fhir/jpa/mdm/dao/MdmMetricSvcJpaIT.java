package ca.uhn.fhir.jpa.mdm.dao;

import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
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
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.mdm.api.IMdmMetricSvc;
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.api.params.GenerateMdmMetricsParameters;
import ca.uhn.fhir.mdm.model.MdmMetrics;
import ca.uhn.fhir.mdm.util.MdmResourceUtil;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.json.JsonMapper;
import jakarta.persistence.EntityManagerFactory;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
		private EntityManagerFactory myEntityManagerFactory;

		// this has to be provided via spring, or the
		// @Transactional barrier is never invoked
		@Bean
		IMdmMetricSvc mdmMetricSvc(DaoRegistry theDaoRegistry) {
			return new MdmMetricSvcJpaImpl(
				myJpaRepository,
				theDaoRegistry,
				myEntityManagerFactory
			);
		}
	}

	private final JsonMapper myJsonMapper = new JsonMapper();

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
			return myJsonMapper.writeValueAsString(theMetrics);
		} catch (JacksonException ex) {
			// we've failed anyway - we might as well display the exception
			fail(ex);
			return "NOT PARSEABLE!";
		}
	}

	@Test
	public void generateMdmMetrics_withRequestPartitionId_scopesLinkAndScoreMetricsToThatPartition() {
		// two MATCH/AUTO Patient links; place one in partition 1 and the other in partition 2
		MDMState<Patient, JpaPid> state = new MDMState<>();
		state.setInputState("""
			G1, AUTO, MATCH, P1
			G2, AUTO, MATCH, P2
			""");
		myLinkHelper.setup(state);

		List<MdmLink> links = myMdmLinkDao.findAll();
		assertEquals(2, links.size());
		links.get(0).setScore(0.8);
		links.get(1).setScore(0.8);
		myMdmLinkDao.save(links.get(0));
		myMdmLinkDao.save(links.get(1));

		// Place one link in partition 1 and the other in partition 2. Partitioning is not enabled in
		// this test context, so the entity's PARTITION_ID is not persisted through a normal save; set the
		// column directly to exercise the metrics queries' partition filter.
		Long pidInPartition1 = links.get(0).getId().getId();
		Long pidInPartition2 = links.get(1).getId().getId();
		runInTransaction(() -> {
			myEntityManager
					.createNativeQuery("UPDATE MPI_LINK SET PARTITION_ID = 1 WHERE PID = :pid")
					.setParameter("pid", pidInPartition1)
					.executeUpdate();
			myEntityManager
					.createNativeQuery("UPDATE MPI_LINK SET PARTITION_ID = 2 WHERE PID = :pid")
					.setParameter("pid", pidInPartition2)
					.executeUpdate();
		});

		GenerateMdmMetricsParameters parameters = new GenerateMdmMetricsParameters("Patient");
		parameters.setRequestPartitionId(RequestPartitionId.fromPartitionIds(1));

		MdmMetrics metrics = mySvc.generateMdmMetrics(parameters);

		// link metrics must count only the partition-1 link
		Long matchAutoCount = metrics.getMatchTypeToLinkToCountMap()
				.getOrDefault(MdmMatchResultEnum.MATCH, Map.of())
				.get(MdmLinkSourceEnum.AUTO);
		assertEquals(1L, matchAutoCount);

		// score metrics must likewise count only the partition-1 link
		long totalScored = metrics.getScoreCounts().values().stream().mapToLong(Long::longValue).sum();
		assertEquals(1L, totalScored);
	}
}
