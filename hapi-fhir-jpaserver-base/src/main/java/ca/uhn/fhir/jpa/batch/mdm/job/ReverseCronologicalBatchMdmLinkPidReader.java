package ca.uhn.fhir.jpa.batch.mdm.job;

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

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.batch.reader.ReverseCronologicalBatchResourcePidReader;
import ca.uhn.fhir.jpa.dao.data.IMdmLinkDao;
import ca.uhn.fhir.jpa.searchparam.ResourceSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.HashSet;
import java.util.Set;

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
public class ReverseCronologicalBatchMdmLinkPidReader extends ReverseCronologicalBatchResourcePidReader {
	@Autowired
	IMdmLinkDao myMdmLinkDao;

	@Override
	protected Set<Long> getNextPidBatch(ResourceSearch resourceSearch) {
		String resourceName = resourceSearch.getResourceName();
		Pageable pageable = PageRequest.of(0, getBatchSize());
		return new HashSet<>(myMdmLinkDao.findPidByResourceNameAndThreshold(resourceName, getCurrentHighThreshold(), pageable));
	}

	@Override
	protected void setDateFromPidFunction(ResourceSearch resourceSearch) {
		setDateExtractorFunction(pid -> myMdmLinkDao.findById(pid).get().getCreated());
	}
}
