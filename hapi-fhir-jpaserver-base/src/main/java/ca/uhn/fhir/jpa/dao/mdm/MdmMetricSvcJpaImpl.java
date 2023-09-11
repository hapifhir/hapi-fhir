package ca.uhn.fhir.jpa.dao.mdm;

import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.config.HapiFhirLocalContainerEntityManagerFactoryBean;
import ca.uhn.fhir.jpa.dao.data.IMdmLinkJpaMetricsRepository;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.mdm.api.IMdmMetricSvc;
import ca.uhn.fhir.mdm.api.IMdmResourceDaoSvc;
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.api.parameters.GenerateMdmLinkMetricParameters;
import ca.uhn.fhir.mdm.api.parameters.GenerateMdmResourceMetricsParameters;
import ca.uhn.fhir.mdm.api.parameters.GetGoldenResourceCountParameters;
import ca.uhn.fhir.mdm.model.MdmGoldenResourceCount;
import ca.uhn.fhir.mdm.model.MdmLinkMetrics;
import ca.uhn.fhir.mdm.model.MdmResourceMetrics;
import ca.uhn.fhir.rest.api.SearchTotalModeEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

public class MdmMetricSvcJpaImpl implements IMdmMetricSvc {

	private final IMdmLinkJpaMetricsRepository myJpaRepository;

	private final HapiFhirLocalContainerEntityManagerFactoryBean myEntityFactory;

	private final IMdmResourceDaoSvc myResourceDaoSvc;
	
	private final DaoRegistry myDaoRegistry;

	public MdmMetricSvcJpaImpl(
		IMdmLinkJpaMetricsRepository theRepository,
		IMdmResourceDaoSvc theResourceDaoSvc,
		DaoRegistry theDaoRegistry,
		HapiFhirLocalContainerEntityManagerFactoryBean theEntityFactory
	) {
		myJpaRepository = theRepository;
		myEntityFactory = theEntityFactory;
		myDaoRegistry = theDaoRegistry;
		myResourceDaoSvc = theResourceDaoSvc;
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
			long count = (Long)row[2];
			metrics.addMetric(matchResult, source, count);
		}
		return metrics;
	}

	@Override
	public MdmResourceMetrics generateResourceMetrics(GenerateMdmResourceMetricsParameters theParameters) {
		String resourceType = theParameters.getResourceType();

		GetGoldenResourceCountParameters resourceCountParameters = new GetGoldenResourceCountParameters();
		resourceCountParameters.setResourceType(resourceType);
		MdmGoldenResourceCount grCountResult = myResourceDaoSvc.getGoldenResourceCounts(resourceCountParameters);

		SearchParameterMap searchParameterMap = new SearchParameterMap();
		searchParameterMap.setLoadSynchronous(true);
		searchParameterMap.setCount(0);
		searchParameterMap.setSearchTotalMode(SearchTotalModeEnum.ACCURATE);
		IFhirResourceDao dao = myDaoRegistry.getResourceDao(resourceType);
		IBundleProvider result = dao.search(searchParameterMap, new SystemRequestDetails());

		MdmResourceMetrics metrics = new MdmResourceMetrics();
		metrics.setResourceType(resourceType);
		metrics.setGoldenResourcesCount(grCountResult.getGoldenResourceCount());
		metrics.setExcludedResources(grCountResult.getBlockListedGoldenResourceCount());
		metrics.setSourceResourcesCount(result.size() - metrics.getGoldenResourcesCount());

		return metrics;
	}
}
