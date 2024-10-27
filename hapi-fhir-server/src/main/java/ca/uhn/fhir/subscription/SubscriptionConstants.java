/*-
 * #%L
 * HAPI FHIR - Server Framework
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
package ca.uhn.fhir.subscription;

public class SubscriptionConstants {

	/**
	 * The number of threads used in subscription channel processing
	 */
	public static final int MATCHING_CHANNEL_CONCURRENT_CONSUMERS = 5;

	public static final int DELIVERY_CHANNEL_CONCURRENT_CONSUMERS = 2;

	/**
	 * The maximum number of subscriptions that can be active at once
	 */
	public static final int MAX_SUBSCRIPTION_RESULTS = 10000;

	/**
	 * The size of the queue used for sending resources to the subscription matching processor and by each subscription delivery queue
	 */
	public static final int DELIVERY_EXECUTOR_QUEUE_SIZE = 1000;

	public static final String SUBSCRIPTION_STATUS = "Subscription.status";
	public static final String SUBSCRIPTION_TYPE = "Subscription.channel.type";
	// These STATUS codes are unchanged from DSTU2 Subscription onwards
	public static final String REQUESTED_STATUS = "requested";
	public static final String ACTIVE_STATUS = "active";
	public static final String ERROR_STATUS = "error";
	public static final String SUBSCRIPTION_TOPIC_PROFILE_URL =
			"http://hl7.org/fhir/uv/subscriptions-backport/StructureDefinition/backport-subscription";
	public static final String SUBSCRIPTION_TOPIC_FILTER_URL =
			"http://hl7.org/fhir/uv/subscriptions-backport/StructureDefinition/backport-filter-criteria";
	public static final String SUBSCRIPTION_TOPIC_CHANNEL_HEARTBEAT_PERIOD_URL =
			"http://hl7.org/fhir/uv/subscriptions-backport/StructureDefinition/backport-heartbeat-period";
	public static final String SUBSCRIPTION_TOPIC_CHANNEL_TIMEOUT_URL =
			"http://hl7.org/fhir/uv/subscriptions-backport/StructureDefinition/backport-timeout";
	public static final String SUBSCRIPTION_TOPIC_CHANNEL_MAX_COUNT =
			"http://hl7.org/fhir/uv/subscriptions-backport/StructureDefinition/backport-max-count";
	public static final String SUBSCRIPTION_TOPIC_CHANNEL_PAYLOAD_CONTENT =
			"http://hl7.org/fhir/uv/subscriptions-backport/StructureDefinition/backport-payload-content";
	public static final String SUBSCRIPTION_TOPIC_STATUS =
			"http://hl7.org/fhir/uv/subscriptions-backport/StructureDefinition/backport-subscription-status-r4";
}
