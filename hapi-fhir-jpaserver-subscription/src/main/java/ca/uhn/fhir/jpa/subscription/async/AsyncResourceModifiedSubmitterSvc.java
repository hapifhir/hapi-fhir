package ca.uhn.fhir.jpa.subscription.async;

/*-
 * #%L
 * HAPI FHIR Subscription Server
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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

import ca.uhn.fhir.jpa.model.config.SubscriptionSettings;
import ca.uhn.fhir.jpa.model.entity.IPersistedResourceModifiedMessage;
import ca.uhn.fhir.subscription.api.IResourceModifiedConsumerWithRetries;
import ca.uhn.fhir.subscription.api.IResourceModifiedMessagePersistenceSvc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

/**
 * The purpose of this service is to submit messages to the processing pipeline for which previous attempts at
 * submission has failed.  See also {@link AsyncResourceModifiedProcessingSchedulerSvc} and {@link IResourceModifiedMessagePersistenceSvc}.
 *
 */
public class AsyncResourceModifiedSubmitterSvc {
	private static final Logger ourLog = LoggerFactory.getLogger(AsyncResourceModifiedSubmitterSvc.class);

	private final IResourceModifiedMessagePersistenceSvc myResourceModifiedMessagePersistenceSvc;
	private final IResourceModifiedConsumerWithRetries myResourceModifiedConsumer;
	private final SubscriptionSettings mySubscriptionSettings;

	public AsyncResourceModifiedSubmitterSvc(
			IResourceModifiedMessagePersistenceSvc theResourceModifiedMessagePersistenceSvc,
			IResourceModifiedConsumerWithRetries theResourceModifiedConsumer,
			SubscriptionSettings theSubscriptionSettings) {
		myResourceModifiedMessagePersistenceSvc = theResourceModifiedMessagePersistenceSvc;
		myResourceModifiedConsumer = theResourceModifiedConsumer;
		mySubscriptionSettings = theSubscriptionSettings;
	}

	public void runDeliveryPass() {
		boolean hasMoreToFetch = false;
		// the page size doubles as the batch size: one page is submitted to the broker as one batch.  The setter
		// guarantees a positive value, so PageRequest.of can never be handed a non positive page size here.
		int limit = mySubscriptionSettings.getSubscriptionSubmissionBatchSize();
		do {
			// we always take the 0th page, because we're deleting the elements as we process them
			Page<IPersistedResourceModifiedMessage> persistedResourceModifiedMsgsPage =
					myResourceModifiedMessagePersistenceSvc.findAllOrderedByCreatedTime(PageRequest.of(0, limit));
			ourLog.debug(
					"Attempting to submit {} resources to consumer channel.",
					persistedResourceModifiedMsgsPage.getTotalElements());

			hasMoreToFetch = persistedResourceModifiedMsgsPage.hasNext();

			List<IPersistedResourceModifiedMessage> batch = persistedResourceModifiedMsgsPage.getContent();
			if (batch.isEmpty()) {
				// nothing left to submit: never issue an empty broker or database round trip
				break;
			}

			// submit the whole page as a single batch so that the cost of synchronizing with the broker, and of
			// deleting the rows, is paid once for the page instead of once per row.
			int processedCount = myResourceModifiedConsumer.submitPersistedResourceModifiedMessages(batch);

			if (processedCount < batch.size()) {
				hasMoreToFetch = false;
			}
		} while (hasMoreToFetch);
	}
}
