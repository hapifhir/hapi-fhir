package ca.uhn.fhir.jpa.batch.mdm.job;

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
import ca.uhn.fhir.jpa.batch.reader.BaseReverseCronologicalBatchPidReader;
import ca.uhn.fhir.jpa.dao.data.IMdmLinkDao;
import ca.uhn.fhir.jpa.searchparam.ResourceSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This is the same as the parent class, except it operates on MdmLink entities instead of resource entities
 */
public class ReverseCronologicalBatchMdmLinkPidReader extends BaseReverseCronologicalBatchPidReader {
	@Autowired
	IMdmLinkDao myMdmLinkDao;

	@Override
	protected Set<Long> getNextPidBatch(ResourceSearch resourceSearch) {
		String resourceName = resourceSearch.getResourceName();
		Pageable pageable = PageRequest.of(0, getBatchSize());
		RequestPartitionId requestPartitionId = resourceSearch.getRequestPartitionId();
		if (requestPartitionId.isAllPartitions()){
			return new HashSet<>(myMdmLinkDao.findPidByResourceNameAndThreshold(resourceName, getCurrentHighThreshold(), pageable));
		}
		List<Integer> partitionIds = requestPartitionId.getPartitionIds();
		//Expand out the list to handle the REDIRECT/POSSIBLE DUPLICATE ones.
		return new HashSet<>(myMdmLinkDao.findPidByResourceNameAndThresholdAndPartitionId(resourceName, getCurrentHighThreshold(), partitionIds, pageable));
	}

	@Override
	protected void setDateFromPidFunction(ResourceSearch resourceSearch) {
		setDateExtractorFunction(pid -> myMdmLinkDao.findById(pid).get().getCreated());
	}
}
