package ca.uhn.fhir.jpa.subscription.async;

/*-
 * #%L
 * HAPI FHIR Subscription Server
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	public AsyncResourceModifiedSubmitterSvc(
			IResourceModifiedMessagePersistenceSvc theResourceModifiedMessagePersistenceSvc,
			IResourceModifiedConsumerWithRetries theResourceModifiedConsumer) {
		myResourceModifiedMessagePersistenceSvc = theResourceModifiedMessagePersistenceSvc;
		myResourceModifiedConsumer = theResourceModifiedConsumer;
	}

	public void runDeliveryPass() {

		List<IPersistedResourceModifiedMessage> allPersistedResourceModifiedMessages =
				myResourceModifiedMessagePersistenceSvc.findAllOrderedByCreatedTime();
		ourLog.debug(
				"Attempting to submit {} resources to consumer channel.", allPersistedResourceModifiedMessages.size());

		for (IPersistedResourceModifiedMessage persistedResourceModifiedMessage :
				allPersistedResourceModifiedMessages) {

			boolean wasProcessed =
					myResourceModifiedConsumer.submitPersisedResourceModifiedMessage(persistedResourceModifiedMessage);

			if (!wasProcessed) {
				break;
			}
		}
	}
}
