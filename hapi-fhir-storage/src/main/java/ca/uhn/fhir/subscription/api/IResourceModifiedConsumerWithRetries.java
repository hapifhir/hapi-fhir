package ca.uhn.fhir.subscription.api;

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

import ca.uhn.fhir.jpa.model.entity.IPersistedResourceModifiedMessage;
import ca.uhn.fhir.jpa.subscription.match.matcher.matching.IResourceModifiedConsumer;

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
}
