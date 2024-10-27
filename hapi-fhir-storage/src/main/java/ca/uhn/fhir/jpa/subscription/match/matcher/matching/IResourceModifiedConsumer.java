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
package ca.uhn.fhir.jpa.subscription.match.matcher.matching;

import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import ca.uhn.fhir.subscription.api.IResourceModifiedConsumerWithRetries;
import org.springframework.messaging.MessageDeliveryException;

/**
 * The implementer of this interface should submit the result of an operation on a resource
 * to the subscription processing pipeline.
 */
public interface IResourceModifiedConsumer {

	/**
	 *  Process a message by submitting it to the processing pipeline.  The message is assumed to have been successfully
	 *  submitted unless a {@link MessageDeliveryException} is thrown by the underlying support.  The exception should be allowed to
	 *  propagate for client handling and potential re-submission through the {@link IResourceModifiedConsumerWithRetries}.
	 *
	 * @param theMsg The message to submit
	 *
	 * This is an internal API - Use with caution!
	 *
	 */
	void submitResourceModified(ResourceModifiedMessage theMsg);
}
