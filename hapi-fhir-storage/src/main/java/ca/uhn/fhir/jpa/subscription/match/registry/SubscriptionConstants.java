package ca.uhn.fhir.jpa.subscription.match.registry;

/*-
 * #%L
 * HAPI FHIR Storage api
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

import org.hl7.fhir.dstu2.model.Subscription;

public class SubscriptionConstants {


	/**
	 * The number of threads used in subscription channel processing
	 */
	public static final int MATCHING_CHANNEL_CONCURRENT_CONSUMERS = 5;
	public static final int DELIVERY_CHANNEL_CONCURRENT_CONSUMERS = 2;

	/**
	 * The maximum number of subscriptions that can be active at once
	 */

	public static final int MAX_SUBSCRIPTION_RESULTS = 50000;

	/**
	 * The size of the queue used for sending resources to the subscription matching processor and by each subscription delivery queue
	 */

	public static final int DELIVERY_EXECUTOR_QUEUE_SIZE = 1000;
	public static final String SUBSCRIPTION_STATUS = "Subscription.status";
	public static final String SUBSCRIPTION_TYPE = "Subscription.channel.type";
	public static final String REQUESTED_STATUS = Subscription.SubscriptionStatus.REQUESTED.toCode();
	public static final String ACTIVE_STATUS = Subscription.SubscriptionStatus.ACTIVE.toCode();
	public static final String ERROR_STATUS = Subscription.SubscriptionStatus.ERROR.toCode();
}
