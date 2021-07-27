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

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.batch.job.MultiUrlProcessorJobConfig;
import ca.uhn.fhir.jpa.dao.data.IResourceTableDao;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This Spring Batch reader takes 4 parameters:
 * {@link #JOB_PARAM_BATCH_SIZE}: The number of resources to return with each search.  If ommitted, {@link DaoConfig#getExpungeBatchSize} will be used.
 * {@link #JOB_PARAM_START_TIME}: The latest timestamp of resources to search for
 * <p>
 * The reader will return at most {@link #JOB_PARAM_BATCH_SIZE} pids every time it is called, or null
 * once no more matching resources are available.  It returns the resources in reverse chronological order
 * appended with "." and the index number of the url list item it has gotten up to.  This is to permit
 * restarting jobs that use this reader so it can pick up where it left off.
 */
public class CronologicalBatchAllResourcePidReader implements ItemReader<List<Long>>, ItemStream {
	public static final String JOB_PARAM_BATCH_SIZE = "batch-size";
	public static final String JOB_PARAM_START_TIME = "start-time";
	// FIXME KHS use this
	public static final String JOB_PARAM_REQUEST_PARTITION = "request-partition";
	public static final String CURRENT_THRESHOLD_LOW = "current.threshold-low";

	private static final Logger ourLog = LoggerFactory.getLogger(CronologicalBatchAllResourcePidReader.class);
	private static final Date BEGINNING_OF_TIME = new Date(0);

	@Autowired
	private IResourceTableDao myResourceTableDao;
	@Autowired
	private DaoConfig myDaoConfig;

	private Integer myBatchSize;
	private Date myThresholdLow = BEGINNING_OF_TIME;
	private Set<Long> myAlreadyProcessedPidsWithLowDate;
	private Date myStartTime;

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
		List<Long> nextBatch = getNextBatch();
		return nextBatch.isEmpty() ? null : nextBatch;
	}

	// FIXME KHS test
	private List<Long> getNextBatch() {
		PageRequest page = PageRequest.of(0, myBatchSize);
		// FIXME KHS consolidate with other one
		List<Long> retval;
		Slice<Long> slice;
		do {
			slice = myResourceTableDao.findIdsOfResourcesWithinUpdatedRangeOrderedFromOldest(page, myThresholdLow, myStartTime);
			retval = new ArrayList<>(slice.getContent());
			Set<Long> alreadyProcessed = myAlreadyProcessedPidsWithLowDate;
			if (alreadyProcessed != null) {
				ourLog.debug("Removing resources that have already been processed: {}", alreadyProcessed);
				retval.removeAll(alreadyProcessed);
			}
			page = page.next();
		} while (retval.isEmpty() && slice.hasNext());

		if (ourLog.isDebugEnabled()) {
			ourLog.debug("Results: {}", retval);
		}

		setThresholds(retval);

		return retval;
	}

	// FIXME KHS test
	private void setThresholds(List<Long> retval) {
		if (retval.isEmpty()) {
			return;
		}

		// Adjust the low threshold to be the latest resource in the batch we found
		Long pidOfLatestResourceInBatch = retval.get(retval.size() - 1);
		ResourceTable entity = myResourceTableDao.findById(pidOfLatestResourceInBatch).orElseThrow(IllegalStateException::new);
		Date latestUpdatedDate = entity.getUpdatedDate();

		// The latest date has changed, create a new cache to store pids with that date
		if (myThresholdLow != latestUpdatedDate) {
			myAlreadyProcessedPidsWithLowDate = new HashSet<>();
		}
		myAlreadyProcessedPidsWithLowDate.add(pidOfLatestResourceInBatch);
		myThresholdLow = latestUpdatedDate;
		if (retval.size() <= 1) {
			return;
		}

		// There is more than one resource in this batch
		for (int index = retval.size() - 2; index >= 0; --index) {
			Long pid = retval.get(index);
			entity = myResourceTableDao.findById(pid).orElseThrow(IllegalStateException::new);
			if (!latestUpdatedDate.equals(entity.getUpdatedDate())) {
				break;
			}
			myAlreadyProcessedPidsWithLowDate.add(pid);
		}
	}

	@Override
	public void open(ExecutionContext executionContext) throws ItemStreamException {
		if (myBatchSize == null) {
			myBatchSize = myDaoConfig.getExpungeBatchSize();
		}
		if (executionContext.containsKey(CURRENT_THRESHOLD_LOW)) {
			myThresholdLow = new Date(executionContext.getLong(CURRENT_THRESHOLD_LOW));
		}
	}

	@Override
	public void update(ExecutionContext executionContext) throws ItemStreamException {
		executionContext.putLong(CURRENT_THRESHOLD_LOW, myThresholdLow.getTime());
	}

	@Override
	public void close() throws ItemStreamException {
	}

	public static JobParameters buildJobParameters(Integer theBatchSize, RequestPartitionId theRequestPartitionId) {
		Map<String, JobParameter> map = new HashMap<>();
		map.put(CronologicalBatchAllResourcePidReader.JOB_PARAM_REQUEST_PARTITION, new JobParameter(theRequestPartitionId.toString()));
		map.put(CronologicalBatchAllResourcePidReader.JOB_PARAM_START_TIME, new JobParameter(DateUtils.addMinutes(new Date(), MultiUrlProcessorJobConfig.MINUTES_IN_FUTURE_TO_PROCESS_FROM)));
		if (theBatchSize != null) {
			map.put(CronologicalBatchAllResourcePidReader.JOB_PARAM_BATCH_SIZE, new JobParameter(theBatchSize.longValue()));
		}
		JobParameters parameters = new JobParameters(map);
		return parameters;
	}
}
