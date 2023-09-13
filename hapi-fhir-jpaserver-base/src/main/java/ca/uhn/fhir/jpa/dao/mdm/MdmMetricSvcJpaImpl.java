package ca.uhn.fhir.jpa.dao.mdm;

import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.data.IMdmLinkJpaMetricsRepository;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.mdm.api.BaseMdmMetricSvc;
import ca.uhn.fhir.mdm.api.IMdmMetricSvc;
import ca.uhn.fhir.mdm.api.IMdmResourceDaoSvc;
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.api.parameters.GenerateMdmLinkMetricParameters;
import ca.uhn.fhir.mdm.api.parameters.GenerateMdmResourceMetricsParameters;
import ca.uhn.fhir.mdm.api.parameters.GenerateScoreMetricsParameters;
import ca.uhn.fhir.mdm.api.parameters.GetGoldenResourceCountParameters;
import ca.uhn.fhir.mdm.model.MdmGoldenResourceCount;
import ca.uhn.fhir.mdm.model.MdmLinkDataMetrics;
import ca.uhn.fhir.mdm.model.MdmLinkMetrics;
import ca.uhn.fhir.mdm.model.MdmResourceMetrics;
import ca.uhn.fhir.rest.api.SearchTotalModeEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

public class MdmMetricSvcJpaImpl extends BaseMdmMetricSvc {

	private final IMdmLinkJpaMetricsRepository myJpaRepository;

	public MdmMetricSvcJpaImpl(
		IMdmLinkJpaMetricsRepository theRepository,
		IMdmResourceDaoSvc theResourceDaoSvc,
		DaoRegistry theDaoRegistry
	) {
		super(theResourceDaoSvc, theDaoRegistry);
		myJpaRepository = theRepository;
	}

	@Transactional
	@Override
	public MdmLinkMetrics generateLinkMetrics(GenerateMdmLinkMetricParameters theParameters) {
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
		MdmLinkMetrics metrics = new MdmLinkMetrics();
		metrics.setResourceType(theParameters.getResourceType());
		for (Object[] row : data) {
			MdmMatchResultEnum matchResult = (MdmMatchResultEnum) row[0];
			MdmLinkSourceEnum source = (MdmLinkSourceEnum) row[1];
			long count = (Long) row[2];
			metrics.addMetric(matchResult, source, count);
		}
		return metrics;
	}


	@Transactional
	@Override
	public MdmLinkDataMetrics generateLinkScoreMetrics(GenerateScoreMetricsParameters theParameters) {
		String resourceType = theParameters.getResourceType();

		List<MdmMatchResultEnum> matchResultTypes = theParameters.getMatchTypes();

		// if no result type filter, add all result types
		if (matchResultTypes.isEmpty()) {
			matchResultTypes = Arrays.asList(MdmMatchResultEnum.values());
		}

		Object[][] data = myJpaRepository.generateScoreMetrics(resourceType, matchResultTypes);

		MdmLinkDataMetrics metrics = new MdmLinkDataMetrics();
		metrics.setResourceType(resourceType);
		for (Object[] row : data) {
			Double scoreValue = (Double) row[0];
			Long scoreCount = (Long) row[1];
			String scoreStr = scoreValue == null ? "NULL" : Double.toString(scoreValue);
			metrics.addScore(scoreStr, scoreCount);
		}

		return metrics;
	}
}
