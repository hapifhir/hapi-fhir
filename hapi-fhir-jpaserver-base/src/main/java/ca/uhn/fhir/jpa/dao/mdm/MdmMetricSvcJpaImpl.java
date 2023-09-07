package ca.uhn.fhir.jpa.dao.mdm;

import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.config.HapiFhirLocalContainerEntityManagerFactoryBean;
import ca.uhn.fhir.jpa.dao.data.IMdmLinkJpaMetricsRepository;
import ca.uhn.fhir.jpa.dao.data.IMdmLinkJpaRepository;
import ca.uhn.fhir.mdm.api.IMdmMetricSvc;
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.api.parameters.MdmGenerateMetricParameters;
import ca.uhn.fhir.mdm.model.MdmMetrics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

public class MdmMetricSvcJpaImpl implements IMdmMetricSvc {

	private final IMdmLinkJpaMetricsRepository myJpaRepository;

	private final HapiFhirLocalContainerEntityManagerFactoryBean myEntityFactory;

//	@Autowired
//	private MdmResourceDaoSvc

	public MdmMetricSvcJpaImpl(
		IMdmLinkJpaMetricsRepository theRepository,
		HapiFhirLocalContainerEntityManagerFactoryBean theEntityFactory
	) {
		myJpaRepository = theRepository;
		myEntityFactory = theEntityFactory;
	}

	@Transactional
	@Override
	public MdmMetrics generateMetrics(MdmGenerateMetricParameters theParameters) {
		List<MdmLinkSourceEnum> linkSources = theParameters.getLinkSourceFilters();
		List<MdmMatchResultEnum> matchResults = theParameters.getMatchResultFilters();

		if (linkSources.isEmpty()) {
			linkSources = Arrays.asList(MdmLinkSourceEnum.values());
		}
		if (matchResults.isEmpty()) {
			matchResults = Arrays.asList(MdmMatchResultEnum.values());
		}

		Object[][] data = myJpaRepository.generateMetrics(
			theParameters.getResourceType(),
			linkSources,
			matchResults
		);
		MdmMetrics metrics = new MdmMetrics();
		metrics.setResourceType(theParameters.getResourceType());
		for (Object[] row : data) {
			MdmMatchResultEnum matchResult = (MdmMatchResultEnum) row[0];
			MdmLinkSourceEnum source = (MdmLinkSourceEnum) row[1];
			long count = (Long)row[2];
			metrics.addMetric(matchResult, source, count);
		}
		return metrics;
	}

	@Override
	public void metrics2() {
		IFhirResourceDao dao = myDaoRegistry.getResourceDao("Patient");


	}
}
