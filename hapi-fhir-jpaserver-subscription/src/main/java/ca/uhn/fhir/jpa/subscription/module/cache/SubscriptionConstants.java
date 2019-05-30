package ca.uhn.fhir.jpa.subscription.module.cache;

/*-
 * #%L
 * HAPI FHIR Subscription Server
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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

import org.hl7.fhir.instance.model.Subscription;

public class SubscriptionConstants {

	/**
	 * <p>
	 * This extension should be of type <code>string</code> and should be
	 * placed on the <code>Subscription.channel</code> element
	 * </p>
	 */
	public static final String EXT_SUBSCRIPTION_EMAIL_FROM = "http://hapifhir.io/fhir/StructureDefinition/subscription-email-from";

	/**
	 * <p>
	 * This extension should be of type <code>string</code> and should be
	 * placed on the <code>Subscription.channel</code> element
	 * </p>
	 */
	public static final String EXT_SUBSCRIPTION_SUBJECT_TEMPLATE = "http://hapifhir.io/fhir/StructureDefinition/subscription-email-subject-template";


	/**
	 * This extension URL indicates whether a REST HOOK delivery should
	 * include the version ID when delivering.
	 * <p>
	 * This extension should be of type <code>boolean</code> and should be
	 * placed on the <code>Subscription.channel</code> element.
	 * </p>
	 */
	public static final String EXT_SUBSCRIPTION_RESTHOOK_STRIP_VERSION_IDS = "http://hapifhir.io/fhir/StructureDefinition/subscription-resthook-strip-version-ids";

	/**
	 * This extension URL indicates whether a REST HOOK delivery should
	 * reload the resource and deliver the latest version always. This
	 * could be useful for example if a resource which triggers a
	 * subscription gets updated many times in short succession and there
	 * is no value in delivering the older versions.
	 * <p>
	 * Note that if the resource is now deleted, this may cause
	 * the delivery to be cancelled altogether.
	 * </p>
	 *
	 * <p>
	 * This extension should be of type <code>boolean</code> and should be
	 * placed on the <code>Subscription.channel</code> element.
	 * </p>
	 */
	public static final String EXT_SUBSCRIPTION_RESTHOOK_DELIVER_LATEST_VERSION = "http://hapifhir.io/fhir/StructureDefinition/subscription-resthook-deliver-latest-version";

	/**
	 * Indicate which strategy will be used to match this subscription
	 */

	public static final String EXT_SUBSCRIPTION_MATCHING_STRATEGY = "http://hapifhir.io/fhir/StructureDefinition/subscription-matching-strategy";


	/**
	 * The number of threads used in subscription channel processing
	 */
	public static final int MATCHING_CHANNEL_CONCURRENT_CONSUMERS = 5;
	public static final int DELIVERY_CHANNEL_CONCURRENT_CONSUMERS = 5;

	/**
	 * The maximum number of subscriptions that can be active at once
	 */

	public static final int MAX_SUBSCRIPTION_RESULTS = 1000;

	/**
	 * The size of the queue used for sending resources to the subscription matching processor and by each subscription delivery queue
	 */

	public static final int DELIVERY_EXECUTOR_QUEUE_SIZE = 1000;
	public static final String SUBSCRIPTION_STATUS = "Subscription.status";
	public static final String SUBSCRIPTION_TYPE = "Subscription.channel.type";
	public static final String REQUESTED_STATUS = Subscription.SubscriptionStatus.REQUESTED.toCode();
	public static final String ACTIVE_STATUS = Subscription.SubscriptionStatus.ACTIVE.toCode();
}
