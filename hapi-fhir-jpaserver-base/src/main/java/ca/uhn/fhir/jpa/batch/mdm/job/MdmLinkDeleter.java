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

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.dao.data.IMdmLinkDao;
import ca.uhn.fhir.jpa.dao.expunge.PartitionRunner;
import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

/**
 * Take MdmLink pids in and output golden resource pids out
 */

public class MdmLinkDeleter implements ItemProcessor<List<Long>, List<Long>> {
	public static final String PROCESS_NAME = "MdmClear";
	public static final String THREAD_PREFIX = "mdmClear";
	private static final Logger ourLog = LoggerFactory.getLogger(MdmLinkDeleter.class);
	@Autowired
	protected PlatformTransactionManager myTxManager;
	@Autowired
	IMdmLinkDao myMdmLinkDao;
	@Autowired
	DaoConfig myDaoConfig;

	@Override
	public List<Long> process(List<Long> thePidList) throws Exception {
		ConcurrentLinkedQueue<Long> goldenPidAggregator = new ConcurrentLinkedQueue<>();
		PartitionRunner partitionRunner = new PartitionRunner(PROCESS_NAME, THREAD_PREFIX, myDaoConfig.getReindexBatchSize(), myDaoConfig.getReindexThreadCount());
		partitionRunner.runInPartitionedThreads(ResourcePersistentId.fromLongList(thePidList), pids -> removeLinks(pids, goldenPidAggregator));
		return new ArrayList<>(goldenPidAggregator);
	}

	private void removeLinks(List<ResourcePersistentId> pidList, ConcurrentLinkedQueue<Long> theGoldenPidAggregator) {
		TransactionTemplate txTemplate = new TransactionTemplate(myTxManager);

		txTemplate.executeWithoutResult(t -> theGoldenPidAggregator.addAll(deleteMdmLinksAndReturnGoldenResourcePids(ResourcePersistentId.toLongList(pidList))));
	}

	public List<Long> deleteMdmLinksAndReturnGoldenResourcePids(List<Long> thePids) {
		List<MdmLink> links = myMdmLinkDao.findAllById(thePids);
		Set<Long> goldenResources = links.stream().map(MdmLink::getGoldenResourcePid).collect(Collectors.toSet());
		//TODO GGG this is probably invalid... we are essentially looking for GOLDEN -> GOLDEN links, which are either POSSIBLE_DUPLICATE
		//and REDIRECT
		goldenResources.addAll(links.stream()
			.filter(link -> link.getMatchResult().equals(MdmMatchResultEnum.REDIRECT)
				|| link.getMatchResult().equals(MdmMatchResultEnum.POSSIBLE_DUPLICATE))
			.map(MdmLink::getSourcePid).collect(Collectors.toSet()));
		ourLog.info("Deleting {} MDM link records...", links.size());
		myMdmLinkDao.deleteAll(links);
		ourLog.info("{} MDM link records deleted", links.size());
		return new ArrayList<>(goldenResources);
	}
}
