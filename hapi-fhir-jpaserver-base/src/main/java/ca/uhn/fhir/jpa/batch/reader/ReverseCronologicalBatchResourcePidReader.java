package ca.uhn.fhir.jpa.batch.reader;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.batch.job.model.PartitionedUrl;
import ca.uhn.fhir.jpa.batch.job.model.RequestListJson;
import ca.uhn.fhir.jpa.dao.IResultIterator;
import ca.uhn.fhir.jpa.dao.ISearchBuilder;
import ca.uhn.fhir.jpa.dao.SearchBuilderFactory;
import ca.uhn.fhir.jpa.model.search.SearchRuntimeDetails;
import ca.uhn.fhir.jpa.partition.SystemRequestDetails;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.ResourceSearch;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.SortOrderEnum;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.param.DateRangeParam;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * This Spring Batch reader takes 4 parameters:
 * {@link #JOB_PARAM_REQUEST_LIST}: A list of URLs to search for along with the partitions those searches should be performed on
 * {@link #JOB_PARAM_BATCH_SIZE}: The number of resources to return with each search.  If ommitted, {@link DaoConfig#getExpungeBatchSize} will be used.
 * {@link #JOB_PARAM_START_TIME}: The latest timestamp of resources to search for
 * <p>
 * The reader will return at most {@link #JOB_PARAM_BATCH_SIZE} pids every time it is called, or null
 * once no more matching resources are available.  It returns the resources in reverse chronological order
 * and stores where it's at in the Spring Batch execution context with the key {@link #CURRENT_THRESHOLD_HIGH}
 * appended with "." and the index number of the url list item it has gotten up to.  This is to permit
 * restarting jobs that use this reader so it can pick up where it left off.
 */
public class ReverseCronologicalBatchResourcePidReader implements ItemReader<List<Long>>, ItemStream {
	private static final Logger ourLog = LoggerFactory.getLogger(ReverseCronologicalBatchResourcePidReader.class);

	public static final String JOB_PARAM_REQUEST_LIST = "url-list";
	public static final String JOB_PARAM_BATCH_SIZE = "batch-size";
	public static final String JOB_PARAM_START_TIME = "start-time";

	public static final String CURRENT_URL_INDEX = "current.url-index";
	public static final String CURRENT_THRESHOLD_HIGH = "current.threshold-high";

	@Autowired
	private FhirContext myFhirContext;
	@Autowired
	private MatchUrlService myMatchUrlService;
	@Autowired
	private DaoRegistry myDaoRegistry;
	@Autowired
	private DaoConfig myDaoConfig;
	private final PidAccumulator myPidAccumulator = new PidAccumulator();

	private List<PartitionedUrl> myPartitionedUrls;
	private Integer myBatchSize;
	private final Map<Integer, Date> myThresholdHighByUrlIndex = new HashMap<>();
	private final Map<Integer, Set<Long>> myAlreadyProcessedPidsWithHighDate = new HashMap<>();
	@Autowired
	private SearchBuilderFactory mySearchBuilderFactory;
	private int myUrlIndex = 0;
	private Date myStartTime;

	@Autowired
	public void setRequestListJson(@Value("#{jobParameters['" + JOB_PARAM_REQUEST_LIST + "']}") String theRequestListJson) {
		RequestListJson requestListJson = RequestListJson.fromJson(theRequestListJson);
		myPartitionedUrls = requestListJson.getPartitionedUrls();
	}

	@Autowired
	public void setBatchSize(@Value("#{jobParameters['" + JOB_PARAM_BATCH_SIZE + "']}") Integer theBatchSize) {
		myBatchSize = theBatchSize;
	}

	@Autowired
	public void setStartTime(@Value("#{jobParameters['" + JOB_PARAM_START_TIME + "']}") Date theStartTime) {
		myStartTime = theStartTime;
	}

	@Override
	public List<Long> read() throws Exception {
		while (myUrlIndex < myPartitionedUrls.size()) {
			List<Long> nextBatch = getNextBatch(myPartitionedUrls.get(myUrlIndex).getRequestPartitionId());
			if (nextBatch.isEmpty()) {
				++myUrlIndex;
				continue;
			}

			return nextBatch;
		}
		return null;
	}

	private List<Long> getNextBatch(RequestPartitionId theRequestPartitionId) {
		ResourceSearch resourceSearch = myMatchUrlService.getResourceSearch(myPartitionedUrls.get(myUrlIndex).getUrl(), theRequestPartitionId);
		SearchParameterMap map = buildSearchParameterMap(resourceSearch);

		// Perform the search
		IFhirResourceDao<?> dao = myDaoRegistry.getResourceDao(resourceSearch.getResourceName());
		IResultIterator resultIter = performSearch(resourceSearch, dao);
		Set<Long> newPids = new LinkedHashSet<>();
		Set<Long> alreadySeenPids = myAlreadyProcessedPidsWithHighDate.computeIfAbsent(myUrlIndex, i -> new HashSet<>());

		do {
			List<Long> pids = resultIter.getNextResultBatch(myBatchSize).stream().map(ResourcePersistentId::getIdAsLong).collect(Collectors.toList());
			newPids.addAll(pids);
			newPids.removeAll(alreadySeenPids);
		} while (newPids.size() < myBatchSize && resultIter.hasNext());

		if (ourLog.isDebugEnabled()) {
			ourLog.debug("Search for {}{} returned {} results", resourceSearch.getResourceName(), map.toNormalizedQueryString(myFhirContext), newPids.size());
			ourLog.debug("Results: {}", newPids);
		}

		myPidAccumulator.setDateFromPid(pid -> {
			IBaseResource oldestResource = dao.readByPid(new ResourcePersistentId(pid));
			return oldestResource.getMeta().getLastUpdated();
		});

		List<Long> retval = new ArrayList<>(newPids);
		myPidAccumulator.setThresholds(myThresholdHighByUrlIndex.get(myUrlIndex), myAlreadyProcessedPidsWithHighDate.get(myUrlIndex), retval);

		return retval;
	}

	// FIXME KHS move out to class to simplify mocks
	private IResultIterator performSearch(ResourceSearch resourceSearch, IFhirResourceDao<?> dao) {
		final ISearchBuilder sb = mySearchBuilderFactory.newSearchBuilder(dao, resourceSearch.getResourceName(), resourceSearch.getResourceType());
		sb.setFetchSize(myBatchSize);
		SystemRequestDetails requestDetails = buildSystemRequestDetails();
		SearchRuntimeDetails searchRuntimeDetails = new SearchRuntimeDetails(requestDetails, UUID.randomUUID().toString());
		IResultIterator resultIter = sb.createQuery(resourceSearch.getSearchParameterMap(), searchRuntimeDetails, requestDetails, resourceSearch.getRequestPartitionId());
		return resultIter;
	}

	@Nonnull
	private SearchParameterMap buildSearchParameterMap(ResourceSearch resourceSearch) {
		SearchParameterMap map = resourceSearch.getSearchParameterMap();
		map.setLastUpdated(new DateRangeParam().setUpperBoundInclusive(myThresholdHighByUrlIndex.get(myUrlIndex)));
		map.setLoadSynchronousUpTo(myBatchSize);
		map.setSort(new SortSpec(Constants.PARAM_LASTUPDATED, SortOrderEnum.DESC));
		return map;
	}

	@Nonnull
	private SystemRequestDetails buildSystemRequestDetails() {
		SystemRequestDetails retval = new SystemRequestDetails();
		retval.setRequestPartitionId(myPartitionedUrls.get(myUrlIndex).getRequestPartitionId());
		return retval;
	}

	@Override
	public void open(ExecutionContext executionContext) throws ItemStreamException {
		if (myBatchSize == null) {
			myBatchSize = myDaoConfig.getExpungeBatchSize();
		}
		if (executionContext.containsKey(CURRENT_URL_INDEX)) {
			myUrlIndex = new Long(executionContext.getLong(CURRENT_URL_INDEX)).intValue();
		}
		for (int index = 0; index < myPartitionedUrls.size(); ++index) {
			String key = highKey(index);
			if (executionContext.containsKey(key)) {
				myThresholdHighByUrlIndex.put(index, new Date(executionContext.getLong(key)));
			} else {
				myThresholdHighByUrlIndex.put(index, myStartTime);
			}
		}
	}

	private static String highKey(int theIndex) {
		return CURRENT_THRESHOLD_HIGH + "." + theIndex;
	}

	@Override
	public void update(ExecutionContext executionContext) throws ItemStreamException {
		executionContext.putLong(CURRENT_URL_INDEX, myUrlIndex);
		for (int index = 0; index < myPartitionedUrls.size(); ++index) {
			Date date = myThresholdHighByUrlIndex.get(index);
			if (date != null) {
				executionContext.putLong(highKey(index), date.getTime());
			}
		}
	}

	@Override
	public void close() throws ItemStreamException {
	}
}
