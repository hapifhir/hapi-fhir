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
import ca.uhn.fhir.mdm.api.IMdmLinkSvc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.SliceImpl;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Take MdmLink pids in and output golden resource pids out
 */

public class MdmLinkDeleter implements ItemProcessor<List<Long>, List<Long>> {
	public static final String PROCESS_NAME = "Reindexing";
	public static final String THREAD_PREFIX = "reindex";
	private static final Logger ourLog = LoggerFactory.getLogger(MdmLinkDeleter.class);
	@Autowired
	protected PlatformTransactionManager myTxManager;
	@Autowired
	IMdmLinkSvc myMdmLinkSvc;
	@Autowired
	DaoConfig myDaoConfig;

	@Override
	public List<Long> process(List<Long> thePidList) throws Exception {
		ConcurrentLinkedQueue<Long> goldenPidAggregator = new ConcurrentLinkedQueue<>();

		PartitionRunner partitionRunner = new PartitionRunner(PROCESS_NAME, THREAD_PREFIX, myDaoConfig.getReindexBatchSize(), myDaoConfig.getReindexThreadCount());

		// Note that since our chunk size is 1, there will always be exactly one list
		partitionRunner.runInPartitionedThreads(new SliceImpl<>(thePidList), pids -> removeLinks(thePidList, goldenPidAggregator));

		return new ArrayList<>(goldenPidAggregator);
	}

	private void removeLinks(List<Long> pidList, ConcurrentLinkedQueue<Long> theGoldenPidAggregator) {
		TransactionTemplate txTemplate = new TransactionTemplate(myTxManager);
		txTemplate.executeWithoutResult(t -> theGoldenPidAggregator.addAll(myMdmLinkSvc.deleteAllMdmLinksAndReturnGoldenResourcePids(pidList)));
	}
}
