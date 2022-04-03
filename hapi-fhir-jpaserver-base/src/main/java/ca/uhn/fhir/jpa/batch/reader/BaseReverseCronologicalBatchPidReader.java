package ca.uhn.fhir.jpa.batch.reader;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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
import ca.uhn.fhir.jpa.batch.CommonBatchJobConfig;
import ca.uhn.fhir.jpa.batch.config.BatchConstants;
import ca.uhn.fhir.jpa.batch.job.MultiUrlJobParameterValidator;
import ca.uhn.fhir.jpa.batch.job.model.PartitionedUrl;
import ca.uhn.fhir.jpa.batch.job.model.RequestListJson;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.ResourceSearch;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.SortOrderEnum;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
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
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * This Spring Batch reader takes 4 parameters:
 * {@link BatchConstants#JOB_PARAM_REQUEST_LIST}: A list of URLs to search for along with the partitions those searches should be performed on
 * {@link BatchConstants#JOB_PARAM_BATCH_SIZE}: The number of resources to return with each search.  If ommitted, {@link DaoConfig#getExpungeBatchSize} will be used.
 * {@link BatchConstants#JOB_PARAM_START_TIME}: The latest timestamp of entities to search for
 * <p>
 * The reader will return at most {@link BatchConstants#JOB_PARAM_BATCH_SIZE} pids every time it is called, or null
 * once no more matching entities are available.  It returns the resources in reverse chronological order
 * and stores where it's at in the Spring Batch execution context with the key {@link BatchConstants#CURRENT_THRESHOLD_HIGH}
 * appended with "." and the index number of the url list item it has gotten up to.  This is to permit
 * restarting jobs that use this reader so it can pick up where it left off.
 */
public abstract class BaseReverseCronologicalBatchPidReader implements ItemReader<List<Long>>, ItemStream {
	private static final Logger ourLog = LoggerFactory.getLogger(ReverseCronologicalBatchResourcePidReader.class);
	private final BatchDateThresholdUpdater myBatchDateThresholdUpdater = new BatchDateThresholdUpdater();
	private final Map<Integer, Date> myThresholdHighByUrlIndex = new HashMap<>();
	private final Map<Integer, Set<Long>> myAlreadyProcessedPidsWithHighDate = new HashMap<>();
	@Autowired
	private FhirContext myFhirContext;
	@Autowired
	private MatchUrlService myMatchUrlService;
	private List<PartitionedUrl> myPartitionedUrls;
	private Integer myBatchSize;
	private int myUrlIndex = 0;
	private Date myStartTime;

	private static String highKey(int theIndex) {
		return BatchConstants.CURRENT_THRESHOLD_HIGH + "." + theIndex;
	}

	@Nonnull
	public static JobParameters buildJobParameters(String theOperationName, Integer theBatchSize, RequestListJson theRequestListJson) {
		Map<String, JobParameter> map = new HashMap<>();
		map.put(MultiUrlJobParameterValidator.JOB_PARAM_OPERATION_NAME, new JobParameter(theOperationName));
		map.put(BatchConstants.JOB_PARAM_REQUEST_LIST, new JobParameter(theRequestListJson.toJson()));
		map.put(BatchConstants.JOB_PARAM_START_TIME, new JobParameter(DateUtils.addMinutes(new Date(), CommonBatchJobConfig.MINUTES_IN_FUTURE_TO_PROCESS_FROM)));
		if (theBatchSize != null) {
			map.put(BatchConstants.JOB_PARAM_BATCH_SIZE, new JobParameter(theBatchSize.longValue()));
		}
		JobParameters parameters = new JobParameters(map);
		return parameters;
	}

	@Autowired
	public void setRequestListJson(@Value("#{jobParameters['" + BatchConstants.JOB_PARAM_REQUEST_LIST + "']}") String theRequestListJson) {
		RequestListJson requestListJson = RequestListJson.fromJson(theRequestListJson);
		myPartitionedUrls = requestListJson.getPartitionedUrls();
	}

	@Autowired
	public void setStartTime(@Value("#{jobParameters['" + BatchConstants.JOB_PARAM_START_TIME + "']}") Date theStartTime) {
		myStartTime = theStartTime;
	}

	@Override
	public List<Long> read() throws Exception {
		while (myUrlIndex < myPartitionedUrls.size()) {
			List<Long> nextBatch = getNextBatch();
			if (nextBatch.isEmpty()) {
				++myUrlIndex;
				continue;
			}

			return nextBatch;
		}
		return null;
	}

	protected List<Long> getNextBatch() {
		RequestPartitionId requestPartitionId = myPartitionedUrls.get(myUrlIndex).getRequestPartitionId();
		ResourceSearch resourceSearch = myMatchUrlService.getResourceSearch(myPartitionedUrls.get(myUrlIndex).getUrl(), requestPartitionId);
		myAlreadyProcessedPidsWithHighDate.putIfAbsent(myUrlIndex, new HashSet<>());
		Set<Long> newPids = getNextPidBatch(resourceSearch);

		if (ourLog.isDebugEnabled()) {
			ourLog.debug("Search for {}{} returned {} results", resourceSearch.getResourceName(), resourceSearch.getSearchParameterMap().toNormalizedQueryString(myFhirContext), newPids.size());
			ourLog.debug("Results: {}", newPids);
		}

		setDateFromPidFunction(resourceSearch);

		List<Long> retval = new ArrayList<>(newPids);
		Date newThreshold = myBatchDateThresholdUpdater.updateThresholdAndCache(getCurrentHighThreshold(), myAlreadyProcessedPidsWithHighDate.get(myUrlIndex), retval);
		myThresholdHighByUrlIndex.put(myUrlIndex, newThreshold);

		return retval;
	}

	protected Date getCurrentHighThreshold() {
		return myThresholdHighByUrlIndex.get(myUrlIndex);
	}

	protected void setDateExtractorFunction(Function<Long, Date> theDateExtractorFunction) {
		myBatchDateThresholdUpdater.setDateFromPid(theDateExtractorFunction);
	}

	protected void addDateCountAndSortToSearch(ResourceSearch resourceSearch) {
		SearchParameterMap map = resourceSearch.getSearchParameterMap();
		DateRangeParam rangeParam = getDateRangeParam(resourceSearch);
		map.setLastUpdated(rangeParam);
		map.setLoadSynchronousUpTo(myBatchSize);
		map.setSort(new SortSpec(Constants.PARAM_LASTUPDATED, SortOrderEnum.DESC));
	}

	/**
	 * Evaluates the passed in {@link ResourceSearch} to see if it contains a non-null {@link DateRangeParam}.
	 *
	 * If one such {@link DateRangeParam} exists, we use that to determine the upper and lower bounds for the returned
	 * {@link DateRangeParam}. The {@link DateRangeParam#getUpperBound()} is compared to the
	 * {@link BaseReverseCronologicalBatchPidReader#getCurrentHighThreshold()}, and the lower of the two date values
	 * is used.
	 *
	 * If no {@link DateRangeParam} is set, we use the local {@link BaseReverseCronologicalBatchPidReader#getCurrentHighThreshold()}
	 * to create a {@link DateRangeParam}.
	 * @param resourceSearch The {@link ResourceSearch} to check.
	 * @return {@link DateRangeParam}
	 */
	private DateRangeParam getDateRangeParam(ResourceSearch resourceSearch) {
		DateRangeParam rangeParam = resourceSearch.getSearchParameterMap().getLastUpdated();
		if (rangeParam != null) {
			if (rangeParam.getUpperBound() == null) {
				rangeParam.setUpperBoundInclusive(getCurrentHighThreshold());
			} else {
				Date theUpperBound = (getCurrentHighThreshold() == null || rangeParam.getUpperBound().getValue().before(getCurrentHighThreshold()))
					? rangeParam.getUpperBound().getValue() : getCurrentHighThreshold();
				rangeParam.setUpperBoundInclusive(theUpperBound);
			}
		} else {
			rangeParam = new DateRangeParam().setUpperBoundInclusive(getCurrentHighThreshold());
		}
		return rangeParam;
	}

	@Override
	public void open(ExecutionContext executionContext) throws ItemStreamException {
		if (executionContext.containsKey(BatchConstants.CURRENT_URL_INDEX)) {
			myUrlIndex = new Long(executionContext.getLong(BatchConstants.CURRENT_URL_INDEX)).intValue();
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

	@Override
	public void update(ExecutionContext executionContext) throws ItemStreamException {
		executionContext.putLong(BatchConstants.CURRENT_URL_INDEX, myUrlIndex);
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

	protected Integer getBatchSize() {
		return myBatchSize;
	}

	@Autowired
	public void setBatchSize(@Value("#{jobParameters['" + BatchConstants.JOB_PARAM_BATCH_SIZE + "']}") Integer theBatchSize) {
		myBatchSize = theBatchSize;
	}

	protected Set<Long> getAlreadySeenPids() {
		return myAlreadyProcessedPidsWithHighDate.get(myUrlIndex);
	}

	protected abstract Set<Long> getNextPidBatch(ResourceSearch resourceSearch);

	protected abstract void setDateFromPidFunction(ResourceSearch resourceSearch);
}
