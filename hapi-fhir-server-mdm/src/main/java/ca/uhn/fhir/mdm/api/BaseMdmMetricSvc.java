/*-
 * #%L
 * HAPI FHIR - Master Data Management
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.mdm.api;

import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.mdm.api.params.GenerateMdmMetricsParameters;
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

	/**
	 * The NULL label
	 */
	public static final String NULL_VALUE = "NULL";

	/**
	 * The label for the first bucket
	 */
	public static final String FIRST_BUCKET = "x_<_%.2f";

	/**
	 * The label for the nth bucket (2... buckets)
	 */
	public static final String NTH_BUCKET = "%.2f_<_x_<=_%.2f";

	protected final DaoRegistry myDaoRegistry;

	public BaseMdmMetricSvc(DaoRegistry theDaoRegistry) {
		myDaoRegistry = theDaoRegistry;
	}

	protected double getBucket(int theBucketId) {
		return (double) Math.round((float) (100 * theBucketId) / BUCKETS) / 100;
	}

	protected MdmResourceMetrics generateResourceMetrics(GenerateMdmMetricsParameters theParameters) {
		String resourceType = theParameters.getResourceType();
		@SuppressWarnings("rawtypes")
		IFhirResourceDao dao = myDaoRegistry.getResourceDao(resourceType);

		// TODO
		/*
		 * We are using 3 different queries to count:
		 * * all resources
		 * * all golden resources
		 * * all blocked resources.
		 *
		 * This is inefficient and if we want, we can speed it up with
		 * a custom query in the future.
		 */
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
