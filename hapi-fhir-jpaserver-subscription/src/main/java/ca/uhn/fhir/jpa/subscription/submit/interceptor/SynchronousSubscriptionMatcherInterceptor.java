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
package ca.uhn.fhir.jpa.subscription.submit.interceptor;

import ca.uhn.fhir.jpa.subscription.async.AsyncResourceModifiedProcessingSchedulerSvc;
import ca.uhn.fhir.jpa.subscription.channel.api.PayloadTooLargeException;
import ca.uhn.fhir.jpa.subscription.match.matcher.matching.IResourceModifiedConsumer;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * The purpose of this interceptor is to synchronously submit ResourceModifiedMessage to the
 * subscription processing pipeline, ie, as part of processing the operation on a resource.
 * It is meant to replace the SubscriptionMatcherInterceptor in integrated tests where
 * scheduling is disabled.  See {@link AsyncResourceModifiedProcessingSchedulerSvc}
 * for further details on asynchronous submissions.
 */
public class SynchronousSubscriptionMatcherInterceptor extends SubscriptionMatcherInterceptor {

	@Autowired
	private IResourceModifiedConsumer myResourceModifiedConsumer;

	@Override
	protected void processResourceModifiedMessage(ResourceModifiedMessage theResourceModifiedMessage) {
		if (TransactionSynchronizationManager.isSynchronizationActive()) {
			TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
				@Override
				public int getOrder() {
					return 0;
				}

				@Override
				public void afterCommit() {
					doSubmitResourceModified(theResourceModifiedMessage);
				}
			});
		} else {
			doSubmitResourceModified(theResourceModifiedMessage);
		}
	}

	/**
	 * Submit the message through the broker channel to the matcher.
	 *
	 * Note: most of our integrated tests for subscription assume we can successfully inflate the message and therefore
	 * does not run with an actual database to persist the data. In these cases, submitting the complete message (i.e.
	 * with payload) is OK. However, there are a few tests that do not assume it and do run with an actual DB. For them,
	 * we should null out the payload body before submitting. This try-catch block only covers the case where the
	 * payload is too large, which is enough for now. However, for better practice we might want to consider splitting
	 * this interceptor into two, each for tests with/without DB connection.
	 * @param theResourceModifiedMessage
	 */
	private void doSubmitResourceModified(ResourceModifiedMessage theResourceModifiedMessage) {
		try {
			myResourceModifiedConsumer.submitResourceModified(theResourceModifiedMessage);
		} catch (MessageDeliveryException e) {
			if (e.getCause() instanceof PayloadTooLargeException) {
				theResourceModifiedMessage.setPayloadToNull();
				myResourceModifiedConsumer.submitResourceModified(theResourceModifiedMessage);
			}
		}
	}
}
