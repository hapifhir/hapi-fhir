/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.dao.expunge;

import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.model.ExpungeOptions;
import ca.uhn.fhir.jpa.api.model.ExpungeOutcome;
import ca.uhn.fhir.jpa.dao.tx.HapiTransactionService;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import com.google.common.annotations.VisibleForTesting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Scope("prototype")
public class ExpungeOperation implements Callable<ExpungeOutcome> {
	private static final Logger ourLog = LoggerFactory.getLogger(ExpungeService.class);
	public static final String PROCESS_NAME = "Expunging";
	public static final String THREAD_PREFIX = "expunge";

	@Autowired
	private IResourceExpungeService myResourceExpungeService;

	@Autowired
	private JpaStorageSettings myStorageSettings;

	private final String myResourceName;
	private final IResourcePersistentId myResourceId;
	private final ExpungeOptions myExpungeOptions;
	private final RequestDetails myRequestDetails;
	private final AtomicInteger myRemainingCount;

	@Autowired
	private HapiTransactionService myTxService;

	public ExpungeOperation(
			String theResourceName,
			IResourcePersistentId theResourceId,
			ExpungeOptions theExpungeOptions,
			RequestDetails theRequestDetails) {
		myResourceName = theResourceName;
		myResourceId = theResourceId;
		myExpungeOptions = theExpungeOptions;
		myRequestDetails = theRequestDetails;
		myRemainingCount = new AtomicInteger(myExpungeOptions.getLimit());
	}

	@Override
	public ExpungeOutcome call() {
		if (myExpungeOptions.isExpungeDeletedResources()
				&& (myResourceId == null || myResourceId.getVersion() == null)) {
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
		List<IResourcePersistentId> resourceIds = findHistoricalVersionsOfDeletedResources();

		deleteHistoricalVersions(resourceIds);
		if (expungeLimitReached()) {
			return;
		}

		deleteCurrentVersionsOfDeletedResources(resourceIds);
	}

	private List<IResourcePersistentId> findHistoricalVersionsOfDeletedResources() {
		List<IResourcePersistentId> retVal = getPartitionAwareSupplier()
				.supplyInPartitionedContext(() -> myResourceExpungeService.findHistoricalVersionsOfDeletedResources(
						myResourceName, myResourceId, myRemainingCount.get()));

		ourLog.debug("Found {} historical versions", retVal.size());
		return retVal;
	}

	private boolean expungeLimitReached() {
		boolean expungeLimitReached = myRemainingCount.get() <= 0;
		if (expungeLimitReached) {
			ourLog.debug("Expunge limit has been hit - Stopping operation");
		}
		return expungeLimitReached;
	}

	private void expungeOldVersions() {
		List<IResourcePersistentId> historicalIds = getPartitionAwareSupplier()
				.supplyInPartitionedContext(() -> myResourceExpungeService.findHistoricalVersionsOfNonDeletedResources(
						myResourceName, myResourceId, myRemainingCount.get()));

		getPartitionRunner()
				.runInPartitionedThreads(
						historicalIds,
						partition -> myResourceExpungeService.expungeHistoricalVersions(
								myRequestDetails, partition, myRemainingCount));
	}

	private PartitionAwareSupplier getPartitionAwareSupplier() {
		return new PartitionAwareSupplier(myTxService, myRequestDetails);
	}

	private PartitionRunner getPartitionRunner() {
		return new PartitionRunner(
				PROCESS_NAME,
				THREAD_PREFIX,
				myStorageSettings.getExpungeBatchSize(),
				myStorageSettings.getExpungeThreadCount(),
				myTxService,
				myRequestDetails);
	}

	private void deleteCurrentVersionsOfDeletedResources(List<IResourcePersistentId> theResourceIds) {
		getPartitionRunner()
				.runInPartitionedThreads(
						theResourceIds,
						partition -> myResourceExpungeService.expungeCurrentVersionOfResources(
								myRequestDetails, partition, myRemainingCount));
	}

	private void deleteHistoricalVersions(List<IResourcePersistentId> theResourceIds) {
		getPartitionRunner()
				.runInPartitionedThreads(
						theResourceIds,
						partition -> myResourceExpungeService.expungeHistoricalVersionsOfIds(
								myRequestDetails, partition, myRemainingCount));
	}

	private ExpungeOutcome expungeOutcome() {
		return new ExpungeOutcome().setDeletedCount(myExpungeOptions.getLimit() - myRemainingCount.get());
	}

	@VisibleForTesting
	public void setHapiTransactionServiceForTesting(HapiTransactionService theHapiTransactionService) {
		myTxService = theHapiTransactionService;
	}

	@VisibleForTesting
	public void setStorageSettingsForTesting(JpaStorageSettings theStorageSettings) {
		myStorageSettings = theStorageSettings;
	}

	@VisibleForTesting
	public void setExpungeDaoServiceForTesting(IResourceExpungeService theIResourceExpungeService) {
		myResourceExpungeService = theIResourceExpungeService;
	}
}
