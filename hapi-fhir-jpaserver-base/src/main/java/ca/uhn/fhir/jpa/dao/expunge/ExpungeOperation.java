package ca.uhn.fhir.jpa.dao.expunge;

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
import ca.uhn.fhir.jpa.api.model.ExpungeOptions;
import ca.uhn.fhir.jpa.api.model.ExpungeOutcome;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Scope("prototype")
public class ExpungeOperation implements Callable<ExpungeOutcome> {
	private static final Logger ourLog = LoggerFactory.getLogger(ExpungeService.class);
	public static final String PROCESS_NAME = "Expunging";
	public static final String THREAD_PREFIX = "expunge";

	@Autowired
	private IResourceExpungeService myExpungeDaoService;
	@Autowired
	private DaoConfig myDaoConfig;

	private final String myResourceName;
	private final Long myResourceId;
	private final Long myVersion;
	private final ExpungeOptions myExpungeOptions;
	private final RequestDetails myRequestDetails;
	private final AtomicInteger myRemainingCount;

	public ExpungeOperation(String theResourceName, Long theResourceId, Long theVersion, ExpungeOptions theExpungeOptions, RequestDetails theRequestDetails) {
		myResourceName = theResourceName;
		myResourceId = theResourceId;
		myVersion = theVersion;
		myExpungeOptions = theExpungeOptions;
		myRequestDetails = theRequestDetails;
		myRemainingCount = new AtomicInteger(myExpungeOptions.getLimit());
	}

	@Override
	public ExpungeOutcome call() {
		if (myExpungeOptions.isExpungeDeletedResources() && myVersion == null) {
			expungeDeletedResources();
			if (expungeLimitReached()) {
				return expungeOutcome();
			}
		}

		if (myExpungeOptions.isExpungeOldVersions()) {
			expungeOldVersions();
			if (expungeLimitReached()) {
				return expungeOutcome();
			}
		}

		return expungeOutcome();
	}

	private void expungeDeletedResources() {
		Slice<Long> resourceIds = findHistoricalVersionsOfDeletedResources();

		deleteHistoricalVersions(resourceIds);
		if (expungeLimitReached()) {
			return;
		}

		deleteCurrentVersionsOfDeletedResources(resourceIds);
	}

	private Slice<Long> findHistoricalVersionsOfDeletedResources() {
		Slice<Long> retVal = myExpungeDaoService.findHistoricalVersionsOfDeletedResources(myResourceName, myResourceId, myRemainingCount.get());
		ourLog.debug("Found {} historical versions", retVal.getSize());
		return retVal;
	}

	private Slice<Long> findHistoricalVersionsOfNonDeletedResources() {
		return myExpungeDaoService.findHistoricalVersionsOfNonDeletedResources(myResourceName, myResourceId, myVersion, myRemainingCount.get());
	}

	private boolean expungeLimitReached() {
		boolean expungeLimitReached = myRemainingCount.get() <= 0;
		if (expungeLimitReached) {
			ourLog.debug("Expunge limit has been hit - Stopping operation");
		}
		return expungeLimitReached;
	}

	private void expungeOldVersions() {
		Slice<Long> historicalIds = findHistoricalVersionsOfNonDeletedResources();

		getPartitionRunner().runInPartitionedThreads(historicalIds, partition -> myExpungeDaoService.expungeHistoricalVersions(myRequestDetails, partition, myRemainingCount));
	}

	private PartitionRunner getPartitionRunner() {
		return new PartitionRunner(PROCESS_NAME, THREAD_PREFIX, myDaoConfig.getExpungeBatchSize(), myDaoConfig.getExpungeThreadCount());
	}

	private void deleteCurrentVersionsOfDeletedResources(Slice<Long> theResourceIds) {
		getPartitionRunner().runInPartitionedThreads(theResourceIds, partition -> myExpungeDaoService.expungeCurrentVersionOfResources(myRequestDetails, partition, myRemainingCount));
	}

	private void deleteHistoricalVersions(Slice<Long> theResourceIds) {
		getPartitionRunner().runInPartitionedThreads(theResourceIds, partition -> myExpungeDaoService.expungeHistoricalVersionsOfIds(myRequestDetails, partition, myRemainingCount));
	}

	private ExpungeOutcome expungeOutcome() {
		return new ExpungeOutcome().setDeletedCount(myExpungeOptions.getLimit() - myRemainingCount.get());
	}

}
