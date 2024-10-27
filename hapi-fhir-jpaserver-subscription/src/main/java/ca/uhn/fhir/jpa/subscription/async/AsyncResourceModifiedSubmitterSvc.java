package ca.uhn.fhir.jpa.subscription.async;

/*-
 * #%L
 * HAPI FHIR Subscription Server
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

import ca.uhn.fhir.jpa.model.entity.IPersistedResourceModifiedMessage;
import ca.uhn.fhir.subscription.api.IResourceModifiedConsumerWithRetries;
import ca.uhn.fhir.subscription.api.IResourceModifiedMessagePersistenceSvc;
import com.google.common.annotations.VisibleForTesting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * The purpose of this service is to submit messages to the processing pipeline for which previous attempts at
 * submission has failed.  See also {@link AsyncResourceModifiedProcessingSchedulerSvc} and {@link IResourceModifiedMessagePersistenceSvc}.
 *
 */
public class AsyncResourceModifiedSubmitterSvc {
	private static final Logger ourLog = LoggerFactory.getLogger(AsyncResourceModifiedSubmitterSvc.class);

	public static final int MAX_LIMIT = 1000;

	private final IResourceModifiedMessagePersistenceSvc myResourceModifiedMessagePersistenceSvc;
	private final IResourceModifiedConsumerWithRetries myResourceModifiedConsumer;

	public AsyncResourceModifiedSubmitterSvc(
			IResourceModifiedMessagePersistenceSvc theResourceModifiedMessagePersistenceSvc,
			IResourceModifiedConsumerWithRetries theResourceModifiedConsumer) {
		myResourceModifiedMessagePersistenceSvc = theResourceModifiedMessagePersistenceSvc;
		myResourceModifiedConsumer = theResourceModifiedConsumer;
	}

	public void runDeliveryPass() {
		boolean hasMoreToFetch = false;
		int limit = getLimit();
		do {
			// we always take the 0th page, because we're deleting the elements as we process them
			Page<IPersistedResourceModifiedMessage> persistedResourceModifiedMsgsPage =
					myResourceModifiedMessagePersistenceSvc.findAllOrderedByCreatedTime(PageRequest.of(0, limit));
			ourLog.debug(
					"Attempting to submit {} resources to consumer channel.",
					persistedResourceModifiedMsgsPage.getTotalElements());

			hasMoreToFetch = persistedResourceModifiedMsgsPage.hasNext();

			for (IPersistedResourceModifiedMessage persistedResourceModifiedMessage :
					persistedResourceModifiedMsgsPage) {
				boolean wasProcessed = myResourceModifiedConsumer.submitPersisedResourceModifiedMessage(
						persistedResourceModifiedMessage);

				if (!wasProcessed) {
					// we're not fetching anymore no matter what
					hasMoreToFetch = false;
					break;
				}
			}
		} while (hasMoreToFetch);
	}

	@VisibleForTesting
	public static int getLimit() {
		return MAX_LIMIT;
	}
}
