package ca.uhn.fhir.subscription.api;

/*-
 * #%L
 * HAPI FHIR Storage api
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

import ca.uhn.fhir.jpa.model.entity.IPersistedResourceModifiedMessage;
import ca.uhn.fhir.jpa.subscription.match.matcher.matching.IResourceModifiedConsumer;

import java.util.List;

/**
 * The implementer of this interface participates in the retry upon failure mechanism for messages submitted
 * to the subscription processing pipeline.
 */
public interface IResourceModifiedConsumerWithRetries {

	/**
	 * The implementer of this method should submit the ResourceModifiedMessage represented the IPersistedResourceModifiedMessage
	 * to a broker (see {@link IResourceModifiedConsumer}) and if submission succeeds, delete the IPersistedResourceModifiedMessage.
	 *
	 * @param thePersistedResourceModifiedMessage A IPersistedResourceModifiedMessage requiring submission.
	 * @return Whether the message was successfully submitted to the broker.
	 */
	boolean submitPersisedResourceModifiedMessage(
			IPersistedResourceModifiedMessage thePersistedResourceModifiedMessage);

	/**
	 * The implementer of this method should submit the whole batch of ResourceModifiedMessage represented by
	 * <code>thePersistedResourceModifiedMessages</code> to a broker (see {@link IResourceModifiedConsumer}) as a single
	 * unit of work, and if submission succeeds, delete all of the IPersistedResourceModifiedMessage.  The point of this
	 * method is to amortize the cost of synchronizing with the broker (and with the database) across the whole batch
	 * instead of paying it once per message.
	 * <p>
	 * The batch is all-or-nothing: when submission fails, no IPersistedResourceModifiedMessage may be deleted, since the
	 * subscription pipeline tolerates delivering a message more than once but never tolerates losing one.
	 * </p>
	 *
	 * @param thePersistedResourceModifiedMessages The IPersistedResourceModifiedMessage requiring submission, in the
	 *                                             order in which they should be submitted.
	 * @return The number of IPersistedResourceModifiedMessage which were successfully processed, which is
	 * <code>0</code> when the batch was rolled back.
	 * @since 8.12.0
	 */
	int submitPersistedResourceModifiedMessages(
			List<IPersistedResourceModifiedMessage> thePersistedResourceModifiedMessages);
}
