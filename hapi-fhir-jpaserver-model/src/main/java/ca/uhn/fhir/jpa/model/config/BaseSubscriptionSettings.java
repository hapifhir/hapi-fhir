/*-
 * #%L
 * HAPI FHIR JPA Model
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
package ca.uhn.fhir.jpa.model.config;

import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.collections4.CollectionUtils;
import org.hl7.fhir.dstu2.model.Subscription;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public abstract class BaseSubscriptionSettings {
	public static final String DEFAULT_EMAIL_FROM_ADDRESS = "noreply@unknown.com";
	public static final String DEFAULT_WEBSOCKET_CONTEXT_PATH = "/websocket";

	private final Set<Subscription.SubscriptionChannelType> mySupportedSubscriptionTypes = new HashSet<>();
	private String myEmailFromAddress = DEFAULT_EMAIL_FROM_ADDRESS;
	private String myWebsocketContextPath = DEFAULT_WEBSOCKET_CONTEXT_PATH;
	private boolean myQualifySubscriptionMatchingChannelName = true;
	private boolean myCrossPartitionSubscriptionEnabled = true;
	private boolean myEnableInMemorySubscriptionMatching = true;
	private boolean myTriggerSubscriptionsForNonVersioningChanges;

	/**
	 * @since 6.8.0
	 * Prevents any non IN-MEMORY Search params from being created by users.
	 */
	private boolean myAllowOnlyInMemorySubscriptions = false;

	/**
	 * This setting indicates which subscription channel types are supported by the server.  Any subscriptions submitted
	 * to the server matching these types will be activated.
	 */
	public void addSupportedSubscriptionType(Subscription.SubscriptionChannelType theSubscriptionChannelType) {
		mySupportedSubscriptionTypes.add(theSubscriptionChannelType);
	}

	/**
	 * This setting indicates which subscription channel types are supported by the server.  Any subscriptions submitted
	 * to the server matching these types will be activated.
	 */
	public Set<Subscription.SubscriptionChannelType> getSupportedSubscriptionTypes() {
		return Collections.unmodifiableSet(mySupportedSubscriptionTypes);
	}

	/**
	 * Indicate whether a subscription channel type is supported by this server.
	 *
	 * @return true if at least one subscription channel type is supported by this server false otherwise.
	 */
	public boolean hasSupportedSubscriptionTypes() {
		return CollectionUtils.isNotEmpty(mySupportedSubscriptionTypes);
	}

	@VisibleForTesting
	public void clearSupportedSubscriptionTypesForUnitTest() {
		mySupportedSubscriptionTypes.clear();
	}

	/**
	 * If e-mail subscriptions are supported, the From address used when sending e-mails
	 */
	public String getEmailFromAddress() {
		return myEmailFromAddress;
	}

	/**
	 * Set the from address used for sending emails when using email subscriptions
	 */
	public void setEmailFromAddress(String theEmailFromAddress) {
		myEmailFromAddress = theEmailFromAddress;
	}

	/**
	 * If websocket subscriptions are enabled, this specifies the context path that listens to them.  Default value "/websocket".
	 */
	public String getWebsocketContextPath() {
		return myWebsocketContextPath;
	}

	/**
	 * Set the websocket endpoint context path to use when websocket subscriptions are enabled.  Default value "/websocket".
	 */
	public void setWebsocketContextPath(String theWebsocketContextPath) {
		myWebsocketContextPath = theWebsocketContextPath;
	}

	/**
	 * This setting returns whether the channel name should be qualified or not.
	 *
	 * @return whether the channel name is qualified or not
	 * @since 6.4.0
	 */
	public boolean isQualifySubscriptionMatchingChannelName() {
		return myQualifySubscriptionMatchingChannelName;
	}

	/**
	 * This setting controls whether the channel name
	 * should be qualified or not.
	 * Default is true, ie, the channel name will be qualified.
	 *
	 * @since 6.4.0
	 */
	public void setQualifySubscriptionMatchingChannelName(boolean theQualifySubscriptionMatchingChannelName) {
		myQualifySubscriptionMatchingChannelName = theQualifySubscriptionMatchingChannelName;
	}

	/**
	 * If enabled, the server will support cross-partition subscription.
	 * This subscription will be the responsible for all the requests from all the partitions on this server.
	 * For example, if the server has 3 partitions, P1, P2, P3
	 * The subscription will live in the DEFAULT partition. Resource posted to DEFAULT, P1, P2, and P3 will trigger this subscription.
	 * <p>
	 * Default is <code>false</code>
	 * </p>
	 *
	 * @since 5.7.0
	 */
	public boolean isCrossPartitionSubscriptionEnabled() {
		return myCrossPartitionSubscriptionEnabled;
	}

	/**
	 * If enabled, the server will support cross-partition subscription.
	 * This subscription will be the responsible for all the requests from all the partitions on this server.
	 * For example, if the server has 3 partitions, P1, P2, P3
	 * The subscription will live in the DEFAULT partition. Resource posted to DEFAULT, P1, P2, and P3 will trigger this subscription.
	 * <p>
	 * Default is <code>false</code>
	 * </p>
	 *
	 * @since 5.7.0
	 */
	public void setCrossPartitionSubscriptionEnabled(boolean theAllowCrossPartitionSubscription) {
		myCrossPartitionSubscriptionEnabled = theAllowCrossPartitionSubscription;
	}
	/**
	 * If set to true, the server will prevent the creation of Subscriptions which cannot be evaluated IN-MEMORY. This can improve
	 * overall server performance.
	 *
	 * @since 6.8.0
	 */
	public void setOnlyAllowInMemorySubscriptions(boolean theAllowOnlyInMemorySearchParams) {
		myAllowOnlyInMemorySubscriptions = theAllowOnlyInMemorySearchParams;
	}

	/**
	 * If set to true, the server will prevent the creation of Subscriptions which cannot be evaluated IN-MEMORY. This can improve
	 * overall server performance.
	 *
	 * @since 6.8.0
	 * @return Returns the value of {@link #setOnlyAllowInMemorySubscriptions(boolean)}
	 */
	public boolean isOnlyAllowInMemorySubscriptions() {
		return myAllowOnlyInMemorySubscriptions;
	}

	/**
	 * If set to <code>false</code> (default is true) the server will not use
	 * in-memory subscription searching and instead use the database matcher for all subscription
	 * criteria matching.
	 * <p>
	 * When there are subscriptions registered
	 * on the server, the default behaviour is to compare the changed resource to the
	 * subscription criteria directly in-memory without going out to the database.
	 * Certain types of subscription criteria, e.g. chained references of queries with
	 * qualifiers or prefixes, are not supported by the in-memory matcher and will fall back
	 * to a database matcher.
	 * <p>
	 * The database matcher performs a query against the
	 * database by prepending ?id=XYZ to the subscription criteria where XYZ is the id of the changed entity
	 *
	 * @since 3.6.1
	 */
	public boolean isEnableInMemorySubscriptionMatching() {
		return myEnableInMemorySubscriptionMatching;
	}

	/**
	 * If set to <code>false</code> (default is true) the server will not use
	 * in-memory subscription searching and instead use the database matcher for all subscription
	 * criteria matching.
	 * <p>
	 * When there are subscriptions registered
	 * on the server, the default behaviour is to compare the changed resource to the
	 * subscription criteria directly in-memory without going out to the database.
	 * Certain types of subscription criteria, e.g. chained references of queries with
	 * qualifiers or prefixes, are not supported by the in-memory matcher and will fall back
	 * to a database matcher.
	 * <p>
	 * The database matcher performs a query against the
	 * database by prepending ?id=XYZ to the subscription criteria where XYZ is the id of the changed entity
	 *
	 * @since 3.6.1
	 */
	public void setEnableInMemorySubscriptionMatching(boolean theEnableInMemorySubscriptionMatching) {
		myEnableInMemorySubscriptionMatching = theEnableInMemorySubscriptionMatching;
	}

	/**
	 * If set to true (default is false) then subscriptions will be triggered for resource updates even if they
	 * do not trigger a new version (e.g. $meta-add and $meta-delete).
	 *
	 * @since 5.5.0
	 */
	public boolean isTriggerSubscriptionsForNonVersioningChanges() {
		return myTriggerSubscriptionsForNonVersioningChanges;
	}

	/**
	 * If set to true (default is false) then subscriptions will be triggered for resource updates even if they
	 * do not trigger a new version (e.g. $meta-add and $meta-delete).
	 *
	 * @since 5.5.0
	 */
	public void setTriggerSubscriptionsForNonVersioningChanges(boolean theTriggerSubscriptionsForNonVersioningChanges) {
		myTriggerSubscriptionsForNonVersioningChanges = theTriggerSubscriptionsForNonVersioningChanges;
	}
}
