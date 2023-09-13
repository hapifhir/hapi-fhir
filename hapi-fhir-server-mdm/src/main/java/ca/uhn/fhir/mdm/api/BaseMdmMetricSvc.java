package ca.uhn.fhir.mdm.api;

import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.mdm.api.parameters.GenerateMdmResourceMetricsParameters;
import ca.uhn.fhir.mdm.api.parameters.GetGoldenResourceCountParameters;
import ca.uhn.fhir.mdm.model.MdmGoldenResourceCount;
import ca.uhn.fhir.mdm.model.MdmResourceMetrics;
import ca.uhn.fhir.rest.api.SearchTotalModeEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;

public abstract class BaseMdmMetricSvc implements IMdmMetricSvc {

	protected final IMdmResourceDaoSvc myResourceDaoSvc;

	protected final DaoRegistry myDaoRegistry;

	public BaseMdmMetricSvc(
		IMdmResourceDaoSvc theResourceDaoSvc,
		DaoRegistry theDaoRegistry
	) {
		myResourceDaoSvc = theResourceDaoSvc;
		myDaoRegistry = theDaoRegistry;
	}

	@SuppressWarnings("rawtypes")
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
