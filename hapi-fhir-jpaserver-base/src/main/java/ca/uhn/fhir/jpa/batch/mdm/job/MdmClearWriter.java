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
import ca.uhn.fhir.jpa.dao.expunge.PartitionRunner;
import ca.uhn.fhir.mdm.api.IMdmClearSvc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.SliceImpl;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

/**
 * Reindex the provided list of pids of resources
 */

public class MdmClearWriter implements ItemWriter<List<Long>> {
	public static final String PROCESS_NAME = "Reindexing";
	public static final String THREAD_PREFIX = "reindex";
	private static final Logger ourLog = LoggerFactory.getLogger(MdmClearWriter.class);
	@Autowired
	protected PlatformTransactionManager myTxManager;
	// FIXME KHS rewrite
	@Autowired
	IMdmClearSvc myMdmClearSvc;
	@Autowired
	DaoConfig myDaoConfig;

	@Override
	public void write(List<? extends List<Long>> thePidLists) throws Exception {

		ourLog.info(">>> DELETING {}", thePidLists.get(0));

		PartitionRunner partitionRunner = new PartitionRunner(PROCESS_NAME, THREAD_PREFIX, myDaoConfig.getReindexBatchSize(), myDaoConfig.getReindexThreadCount());

		// Note that since our chunk size is 1, there will always be exactly one list
		for (List<Long> pidList : thePidLists) {
			partitionRunner.runInPartitionedThreads(new SliceImpl<>(pidList), pids -> removeLinks(pidList));
		}
	}

	private void removeLinks(List<Long> pidList) {
		TransactionTemplate txTemplate = new TransactionTemplate(myTxManager);
		txTemplate.executeWithoutResult(t -> myMdmClearSvc.removeLinkAndGoldenResources(pidList));
	}
}
