package ca.uhn.fhir.mdm.api;

import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.mdm.api.params.GenerateMdmMetricsParameters;
import ca.uhn.fhir.mdm.api.params.GenerateMdmResourceMetricsParameters;
import ca.uhn.fhir.mdm.model.MdmResourceMetrics;
import ca.uhn.fhir.mdm.util.MdmSearchParamBuildingUtils;
import ca.uhn.fhir.rest.api.SearchTotalModeEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;

public abstract class BaseMdmMetricSvc implements IMdmMetricSvc {

	/**
	 * Count of numbered buckets.
	 * There will also be a NULL bucket, so there will be a total
	 * of BUCKETS + 1 buckets.
	 */
	public static final int BUCKETS = 100;

	public static final String NULL_VALUE = "NULL";

	public static final String FIRST_BUCKET = "x_<_%.2f";

	public static final String NTH_BUCKET = "%.2f_<_x_<=_%.2f";

	protected final DaoRegistry myDaoRegistry;

	public BaseMdmMetricSvc(DaoRegistry theDaoRegistry) {
		myDaoRegistry = theDaoRegistry;
	}

	protected MdmResourceMetrics generateResourceMetrics(GenerateMdmMetricsParameters theParameters) {
		String resourceType = theParameters.getResourceType();
		IFhirResourceDao dao = myDaoRegistry.getResourceDao(resourceType);

		IBundleProvider outcome = null;
		SearchParameterMap map = null;

		MdmResourceMetrics metrics = new MdmResourceMetrics();
		metrics.setResourceType(resourceType);

		// find golden resources
		map = MdmSearchParamBuildingUtils.buildBasicGoldenResourceSearchParameterMap(resourceType);
		setCountOnly(map);
		outcome = dao.search(map, new SystemRequestDetails());
		metrics.setGoldenResourcesCount(outcome.size());

		// find blocked resources
		map = MdmSearchParamBuildingUtils.buildSearchParameterForBlockedResourceCount(resourceType);
		setCountOnly(map);
		outcome = dao.search(map, new SystemRequestDetails());
		metrics.setExcludedResources(outcome.size());

		// find all resources
		map = new SearchParameterMap();
		setCountOnly(map);
		outcome = dao.search(map, new SystemRequestDetails());
		metrics.setSourceResourcesCount(outcome.size() - metrics.getGoldenResourcesCount());

		return metrics;
	}

	private void setCountOnly(SearchParameterMap theMap) {
		theMap.setCount(0);
		theMap.setLoadSynchronous(true);
		theMap.setSearchTotalMode(SearchTotalModeEnum.ACCURATE);
	}
}
