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

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.batch.CommonBatchJobConfig;
import ca.uhn.fhir.jpa.dao.data.IResourceTableDao;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import com.fasterxml.jackson.core.JsonProcessingException;
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
 * This Spring Batch reader takes 3 parameters:
 * {@link #JOB_PARAM_BATCH_SIZE}: The number of resources to return with each search.
 * {@link #JOB_PARAM_START_TIME}: The latest timestamp of resources to search for
 * {@link #JOB_PARAM_REQUEST_PARTITION}: (optional) The partition of resources to read
 * <p>
 * The reader will return at most {@link #JOB_PARAM_BATCH_SIZE} pids every time it is called, or null
 * once no more matching resources are available.  It returns the resources in reverse chronological order
 * appended with "." and the index number of the url list item it has gotten up to.  This is to permit
 * restarting jobs that use this reader so it can pick up where it left off.
 */
public class CronologicalBatchAllResourcePidReader implements ItemReader<List<Long>>, ItemStream {
	public static final String JOB_PARAM_BATCH_SIZE = "batch-size";
	public static final String JOB_PARAM_START_TIME = "start-time";
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
	private final BatchDateThresholdUpdater myBatchDateThresholdUpdater = new BatchDateThresholdUpdater(this::dateFromPid);
	private final Set<Long> myAlreadyProcessedPidsWithLowDate = new HashSet<>();
	private Date myStartTime;
	private RequestPartitionId myRequestPartitionId;

	@Autowired
	public void setBatchSize(@Value("#{jobParameters['" + JOB_PARAM_BATCH_SIZE + "']}") Integer theBatchSize) {
		myBatchSize = theBatchSize;
	}

	@Autowired
	public void setStartTime(@Value("#{jobParameters['" + JOB_PARAM_START_TIME + "']}") Date theStartTime) {
		myStartTime = theStartTime;
	}

	public static JobParameters buildJobParameters(Integer theBatchSize, RequestPartitionId theRequestPartitionId) {
		Map<String, JobParameter> map = new HashMap<>();
		map.put(CronologicalBatchAllResourcePidReader.JOB_PARAM_REQUEST_PARTITION, new JobParameter(theRequestPartitionId.toJson()));
		map.put(CronologicalBatchAllResourcePidReader.JOB_PARAM_START_TIME, new JobParameter(DateUtils.addMinutes(new Date(), CommonBatchJobConfig.MINUTES_IN_FUTURE_TO_PROCESS_FROM)));
		if (theBatchSize != null) {
			map.put(CronologicalBatchAllResourcePidReader.JOB_PARAM_BATCH_SIZE, new JobParameter(theBatchSize.longValue()));
		}
		JobParameters parameters = new JobParameters(map);
		return parameters;
	}

	@Override
	public List<Long> read() throws Exception {
		List<Long> nextBatch = getNextBatch();
		return nextBatch.isEmpty() ? null : nextBatch;
	}

	private Date dateFromPid(Long thePid) {
		ResourceTable entity = myResourceTableDao.findById(thePid).orElseThrow(IllegalStateException::new);
		return entity.getUpdatedDate();
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

	private List<Long> getNextBatch() {
		PageRequest page = PageRequest.of(0, myBatchSize);
		List<Long> retval = new ArrayList<>();
		Slice<Long> slice;
		do {
			if (myRequestPartitionId == null || myRequestPartitionId.isAllPartitions()) {
				slice = myResourceTableDao.findIdsOfResourcesWithinUpdatedRangeOrderedFromOldest(page, myThresholdLow, myStartTime);
			} else {
				slice = myResourceTableDao.findIdsOfPartitionedResourcesWithinUpdatedRangeOrderedFromOldest(page, myThresholdLow, myStartTime, myRequestPartitionId.getFirstPartitionIdOrNull());
			}
			retval.addAll(slice.getContent());
			retval.removeAll(myAlreadyProcessedPidsWithLowDate);
			page = page.next();
		} while (retval.size() < myBatchSize && slice.hasNext());

		if (ourLog.isDebugEnabled()) {
			ourLog.debug("Results: {}", retval);
		}
		myThresholdLow = myBatchDateThresholdUpdater.updateThresholdAndCache(myThresholdLow, myAlreadyProcessedPidsWithLowDate, retval);
		return retval;
	}

	@Autowired
	public void setRequestPartitionId(@Value("#{jobParameters['" + JOB_PARAM_REQUEST_PARTITION + "']}") String theRequestPartitionIdJson) throws JsonProcessingException {
		if (theRequestPartitionIdJson == null) {
			return;
		}
		myRequestPartitionId = RequestPartitionId.fromJson(theRequestPartitionIdJson);
	}
}
