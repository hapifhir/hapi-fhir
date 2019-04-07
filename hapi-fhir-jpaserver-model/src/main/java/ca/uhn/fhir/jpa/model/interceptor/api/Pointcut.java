package ca.uhn.fhir.jpa.model.interceptor.api;

/*-
 * #%L
 * HAPI FHIR Model
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

import ca.uhn.fhir.jpa.model.search.PerformanceMessage;
import ca.uhn.fhir.jpa.model.search.SearchRuntimeDetails;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Value for {@link Hook#value()}
 */
public enum Pointcut {

	/**
	 * This pointcut will be called once when a given interceptor is registered
	 */
	REGISTERED,

	/**
	 * Invoked whenever a persisted resource has been modified and is being submitted to the
	 * subscription processing pipeline. This method is called before the resource is placed
	 * on any queues for processing and executes synchronously during the resource modification
	 * operation itself, so it should return quickly.
	 * <p>
	 * Hooks may accept the following parameters:
	 * <ul>
	 * <li>ca.uhn.fhir.jpa.subscription.module.ResourceModifiedMessage - Hooks may modify this parameter. This will affect the checking process.</li>
	 * </ul>
	 * </p>
	 * <p>
	 * Hooks may return <code>void</code> or may return a <code>boolean</code>. If the method returns
	 * <code>void</code> or <code>true</code>, processing will continue normally. If the method
	 * returns <code>false</code>, subscription processing will not proceed for the given resource;
	 * </p>
	 */
	SUBSCRIPTION_RESOURCE_MODIFIED("ca.uhn.fhir.jpa.subscription.module.ResourceModifiedMessage"),

	/**
	 * Invoked any time that a resource is matched by an individual subscription, and
	 * is about to be queued for delivery.
	 * <p>
	 * Hooks may make changes to the delivery payload, or make changes to the
	 * canonical subscription such as adding headers, modifying the channel
	 * endpoint, etc.
	 * </p>
	 * Hooks may accept the following parameters:
	 * <ul>
	 * <li>ca.uhn.fhir.jpa.subscription.module.CanonicalSubscription</li>
	 * <li>ca.uhn.fhir.jpa.subscription.module.subscriber.ResourceDeliveryMessage</li>
	 * <li>ca.uhn.fhir.jpa.subscription.module.matcher.SubscriptionMatchResult</li>
	 * </ul>
	 * <p>
	 * Hooks may return <code>void</code> or may return a <code>boolean</code>. If the method returns
	 * <code>void</code> or <code>true</code>, processing will continue normally. If the method
	 * returns <code>false</code>, delivery will be aborted.
	 * </p>
	 */
	SUBSCRIPTION_RESOURCE_MATCHED("ca.uhn.fhir.jpa.subscription.module.CanonicalSubscription", "ca.uhn.fhir.jpa.subscription.module.subscriber.ResourceDeliveryMessage", "ca.uhn.fhir.jpa.subscription.module.matcher.SubscriptionMatchResult"),


	/**
	 * Invoked whenever a persisted resource was checked against all active subscriptions, and did not
	 * match any.
	 * <p>
	 * Hooks may accept the following parameters:
	 * <ul>
	 * <li>ca.uhn.fhir.jpa.subscription.module.ResourceModifiedMessage - Hooks should not modify this parameter as changes will not have any effect.</li>
	 * </ul>
	 * </p>
	 * <p>
	 * Hooks should return <code>void</code>.
	 * </p>
	 */
	SUBSCRIPTION_RESOURCE_DID_NOT_MATCH_ANY_SUBSCRIPTIONS("ca.uhn.fhir.jpa.subscription.module.ResourceModifiedMessage"),


	/**
	 * Invoked immediately before the delivery of a subscription, and right before any channel-specific
	 * hooks are invoked (e.g. {@link #SUBSCRIPTION_BEFORE_REST_HOOK_DELIVERY}.
	 * <p>
	 * Hooks may make changes to the delivery payload, or make changes to the
	 * canonical subscription such as adding headers, modifying the channel
	 * endpoint, etc.
	 * </p>
	 * Hooks may accept the following parameters:
	 * <ul>
	 * <li>ca.uhn.fhir.jpa.subscription.module.CanonicalSubscription</li>
	 * <li>ca.uhn.fhir.jpa.subscription.module.subscriber.ResourceDeliveryMessage</li>
	 * </ul>
	 * <p>
	 * Hooks may return <code>void</code> or may return a <code>boolean</code>. If the method returns
	 * <code>void</code> or <code>true</code>, processing will continue normally. If the method
	 * returns <code>false</code>, processing will be aborted.
	 * </p>
	 */
	SUBSCRIPTION_BEFORE_DELIVERY("ca.uhn.fhir.jpa.subscription.module.CanonicalSubscription", "ca.uhn.fhir.jpa.subscription.module.subscriber.ResourceDeliveryMessage"),

	/**
	 * Invoked immediately after the delivery of a subscription, and right before any channel-specific
	 * hooks are invoked (e.g. {@link #SUBSCRIPTION_AFTER_REST_HOOK_DELIVERY}.
	 * <p>
	 * Hooks may accept the following parameters:
	 * </p>
	 * <ul>
	 * <li>ca.uhn.fhir.jpa.subscription.module.CanonicalSubscription</li>
	 * <li>ca.uhn.fhir.jpa.subscription.module.subscriber.ResourceDeliveryMessage</li>
	 * </ul>
	 * <p>
	 * Hooks should return <code>void</code>.
	 * </p>
	 */
	SUBSCRIPTION_AFTER_DELIVERY("ca.uhn.fhir.jpa.subscription.module.CanonicalSubscription", "ca.uhn.fhir.jpa.subscription.module.subscriber.ResourceDeliveryMessage"),

	/**
	 * Invoked immediately after the attempted delivery of a subscription, if the delivery
	 * failed.
	 * <p>
	 * Hooks may accept the following parameters:
	 * </p>
	 * <ul>
	 * <li>java.lang.Exception - The exception that caused the failure.  Note this could be an exception thrown by a SUBSCRIPTION_BEFORE_DELIVERY or SUBSCRIPTION_AFTER_DELIVERY interceptor</li>
	 * <li>ca.uhn.fhir.jpa.subscription.module.subscriber.ResourceDeliveryMessage - the message that triggered the exception</li>
	 * <li>java.lang.Exception</li>
	 * </ul>
	 * <p>
	 * Hooks may return <code>void</code> or may return a <code>boolean</code>. If the method returns
	 * <code>void</code> or <code>true</code>, processing will continue normally, meaning that
	 * an exception will be thrown by the delivery mechanism. This typically means that the
	 * message will be returned to the processing queue. If the method
	 * returns <code>false</code>, processing will be aborted and no further action will be
	 * taken for the delivery.
	 * </p>
	 */
	SUBSCRIPTION_AFTER_DELIVERY_FAILED("ca.uhn.fhir.jpa.subscription.module.subscriber.ResourceDeliveryMessage", "java.lang.Exception"),

	/**
	 * Invoked immediately after the delivery of a REST HOOK subscription.
	 * <p>
	 * When this hook is called, all processing is complete so this hook should not
	 * make any changes to the parameters.
	 * </p>
	 * Hooks may accept the following parameters:
	 * <ul>
	 * <li>ca.uhn.fhir.jpa.subscription.module.CanonicalSubscription</li>
	 * <li>ca.uhn.fhir.jpa.subscription.module.subscriber.ResourceDeliveryMessage</li>
	 * </ul>
	 * <p>
	 * Hooks should return <code>void</code>.
	 * </p>
	 */
	SUBSCRIPTION_AFTER_REST_HOOK_DELIVERY("ca.uhn.fhir.jpa.subscription.module.CanonicalSubscription", "ca.uhn.fhir.jpa.subscription.module.subscriber.ResourceDeliveryMessage"),

	/**
	 * Invoked immediately before the delivery of a REST HOOK subscription.
	 * <p>
	 * Hooks may make changes to the delivery payload, or make changes to the
	 * canonical subscription such as adding headers, modifying the channel
	 * endpoint, etc.
	 * </p>
	 * Hooks may accept the following parameters:
	 * <ul>
	 * <li>ca.uhn.fhir.jpa.subscription.module.CanonicalSubscription</li>
	 * <li>ca.uhn.fhir.jpa.subscription.module.subscriber.ResourceDeliveryMessage</li>
	 * </ul>
	 * <p>
	 * Hooks may return <code>void</code> or may return a <code>boolean</code>. If the method returns
	 * <code>void</code> or <code>true</code>, processing will continue normally. If the method
	 * returns <code>false</code>, processing will be aborted.
	 * </p>
	 */
	SUBSCRIPTION_BEFORE_REST_HOOK_DELIVERY("ca.uhn.fhir.jpa.subscription.module.CanonicalSubscription", "ca.uhn.fhir.jpa.subscription.module.subscriber.ResourceDeliveryMessage"),

	/**
	 * Invoked whenever a persisted resource (a resource that has just been stored in the
	 * database via a create/update/patch/etc.) is about to be checked for whether any subscriptions
	 * were triggered as a result of the operation.
	 * <p>
	 * Hooks may accept the following parameters:
	 * <ul>
	 * <li>ca.uhn.fhir.jpa.subscription.module.ResourceModifiedMessage - Hooks may modify this parameter. This will affect the checking process.</li>
	 * </ul>
	 * </p>
	 * <p>
	 * Hooks may return <code>void</code> or may return a <code>boolean</code>. If the method returns
	 * <code>void</code> or <code>true</code>, processing will continue normally. If the method
	 * returns <code>false</code>, processing will be aborted.
	 * </p>
	 */
	SUBSCRIPTION_BEFORE_PERSISTED_RESOURCE_CHECKED("ca.uhn.fhir.jpa.subscription.module.ResourceModifiedMessage"),


	/**
	 * Invoked whenever a persisted resource (a resource that has just been stored in the
	 * database via a create/update/patch/etc.) has been checked for whether any subscriptions
	 * were triggered as a result of the operation.
	 * <p>
	 * Hooks may accept the following parameters:
	 * <ul>
	 * <li>ca.uhn.fhir.jpa.subscription.module.ResourceModifiedMessage - This parameter should not be modified as processing is complete when this hook is invoked.</li>
	 * </ul>
	 * </p>
	 * <p>
	 * Hooks should return <code>void</code>.
	 * </p>
	 */
	SUBSCRIPTION_AFTER_PERSISTED_RESOURCE_CHECKED("ca.uhn.fhir.jpa.subscription.module.ResourceModifiedMessage"),

	/**
	 * Invoked immediately after an active subscription is "registered". In HAPI FHIR, when
	 * a subscription
	 * <p>
	 * Hooks may make changes to the canonicalized subscription and this will have an effect
	 * on processing across this server. Note however that timing issues may occur, since the
	 * subscription is already technically live by the time this hook is called.
	 * </p>
	 * Hooks may accept the following parameters:
	 * <ul>
	 * <li>ca.uhn.fhir.jpa.subscription.module.CanonicalSubscription</li>
	 * </ul>
	 * <p>
	 * Hooks should return <code>void</code>.
	 * </p>
	 */
	SUBSCRIPTION_AFTER_ACTIVE_SUBSCRIPTION_REGISTERED("ca.uhn.fhir.jpa.subscription.module.CanonicalSubscription"),

	/**
	 * Invoked before a resource will be created, immediately before the resource
	 * is persisted to the database.
	 * <p>
	 * Hooks will have access to the contents of the resource being created
	 * and may choose to make modifications to it. These changes will be
	 * reflected in permanent storage.
	 * </p>
	 * Hooks may accept the following parameters:
	 * <ul>
	 * <li>org.hl7.fhir.instance.model.api.IBaseResource</li>
	 * </ul>
	 * <p>
	 * Hooks should return <code>void</code>.
	 * </p>
	 */
	OP_PRESTORAGE_RESOURCE_CREATED("org.hl7.fhir.instance.model.api.IBaseResource"),

	/**
	 * Invoked before a resource will be created, immediately before the transaction
	 * is committed (after all validation and other business rules have successfully
	 * completed, and any other database activity is complete.
	 * <p>
	 * Hooks will have access to the contents of the resource being created
	 * but should generally not make any
	 * changes as storage has already occurred. Changes will not be reflected
	 * in storage, but may be reflected in the HTTP response.
	 * </p>
	 * Hooks may accept the following parameters:
	 * <ul>
	 * <li>org.hl7.fhir.instance.model.api.IBaseResource</li>
	 * </ul>
	 * <p>
	 * Hooks should return <code>void</code>.
	 * </p>
	 */
	OP_PRECOMMIT_RESOURCE_CREATED("org.hl7.fhir.instance.model.api.IBaseResource"),

	/**
	 * Invoked before a resource will be created
	 * <p>
	 * Hooks will have access to the contents of the resource being deleted
	 * but should not make any changes as storage has already occurred
	 * </p>
	 * Hooks may accept the following parameters:
	 * <ul>
	 * <li>org.hl7.fhir.instance.model.api.IBaseResource</li>
	 * </ul>
	 * <p>
	 * Hooks should return <code>void</code>.
	 * </p>
	 */
	OP_PRECOMMIT_RESOURCE_DELETED("org.hl7.fhir.instance.model.api.IBaseResource"),

	/**
	 * Invoked before a resource will be updated, immediately before the transaction
	 * is committed (after all validation and other business rules have successfully
	 * completed, and any other database activity is complete.
	 * <p>
	 * Hooks will have access to the contents of the resource being updated
	 * (both the previous and new contents) but should generally not make any
	 * changes as storage has already occurred. Changes will not be reflected
	 * in storage, but may be reflected in the HTTP response.
	 * </p>
	 * Hooks may accept the following parameters:
	 * <ul>
	 * <li>org.hl7.fhir.instance.model.api.IBaseResource (previous contents)</li>
	 * <li>org.hl7.fhir.instance.model.api.IBaseResource (new contents)</li>
	 * </ul>
	 * <p>
	 * Hooks should return <code>void</code>.
	 * </p>
	 */
	OP_PRECOMMIT_RESOURCE_UPDATED("org.hl7.fhir.instance.model.api.IBaseResource", "org.hl7.fhir.instance.model.api.IBaseResource"),


	/**
	 * Invoked before a resource will be updated, immediately before the resource
	 * is persisted to the database.
	 * <p>
	 * Hooks will have access to the contents of the resource being updated
	 * (both the previous and new contents) and may choose to make modifications
	 * to the new contents of the resource. These changes will be reflected in
	 * permanent storage.
	 * </p>
	 * Hooks may accept the following parameters:
	 * <ul>
	 * <li>org.hl7.fhir.instance.model.api.IBaseResource (previous contents)</li>
	 * <li>org.hl7.fhir.instance.model.api.IBaseResource (new contents)</li>
	 * </ul>
	 * <p>
	 * Hooks should return <code>void</code>.
	 * </p>
	 */
	OP_PRESTORAGE_RESOURCE_UPDATED("org.hl7.fhir.instance.model.api.IBaseResource", "org.hl7.fhir.instance.model.api.IBaseResource"),

	/**
	 * Invoked when a resource may be returned to the user, whether as a part of a READ,
	 * a SEARCH, or even as the response to a CREATE/UPDATE, etc.
	 * <p>
	 * This hook is invoked when a resource has been loaded by the storage engine and
	 * is being returned to the HTTP stack for response. This is not a guarantee that the
	 * client will ultimately see it, since filters/headers/etc may affect what
	 * is returned but if a resource is loaded it is likely to be used.
	 * Note also that caching may affect whether this pointcut is invoked.
	 * </p>
	 * <p>
	 * Hooks will have access to the contents of the resource being returned
	 * and may choose to make modifications. These changes will be reflected in
	 * returned resource but have no effect on storage.
	 * </p>
	 * Hooks may accept the following parameters:
	 * <ul>
	 * <li>org.hl7.fhir.instance.model.api.IBaseResource (the resource being returned)</li>
	 * </ul>
	 * <p>
	 * Hooks should return <code>void</code>.
	 * </p>
	 */
	RESOURCE_MAY_BE_RETURNED("org.hl7.fhir.instance.model.api.IBaseResource"),

	/**
	 * Note that this is a performance tracing hook. Use with caution in production
	 * systems, since calling it may (or may not) carry a cost.
	 * <p>
	 * This hook is invoked when a search has returned the very first result
	 * from the database. The timing on this call can be a good indicator of how
	 * performant a query is in general.
	 * </p>
	 * Hooks may accept the following parameters:
	 * <ul>
	 * <li>
	 * ca.uhn.fhir.jpa.model.search.SearchRuntimeDetails - Contains details about the search being
	 * performed. Hooks should not modify this object.
	 * </li>
	 * </ul>
	 * <p>
	 * Hooks should return <code>void</code>.
	 * </p>
	 */
	PERFTRACE_SEARCH_FIRST_RESULT_LOADED(SearchRuntimeDetails.class.getName()),

	/**
	 * Note that this is a performance tracing hook. Use with caution in production
	 * systems, since calling it may (or may not) carry a cost.
	 * <p>
	 * This hook is invoked when an individual search query SQL SELECT statement
	 * has completed and no more results are available from that query. Note that this
	 * doesn't necessarily mean that no more matching results exist in the database,
	 * since HAPI FHIR JPA batch loads results in to the query cache in chunks in order
	 * to provide predicable results without overloading memory or the database.
	 * </p>
	 * Hooks may accept the following parameters:
	 * <ul>
	 * <li>
	 * ca.uhn.fhir.jpa.model.search.SearchRuntimeDetails - Contains details about the search being
	 * performed. Hooks should not modify this object.
	 * </li>
	 * </ul>
	 * <p>
	 * Hooks should return <code>void</code>.
	 * </p>
	 */
	PERFTRACE_SEARCH_SELECT_COMPLETE(SearchRuntimeDetails.class.getName()),

	/**
	 * Note that this is a performance tracing hook. Use with caution in production
	 * systems, since calling it may (or may not) carry a cost.
	 * <p>
	 * This hook is invoked when a search has failed for any reason. When this pointcut
	 * is invoked, the search has completed unsuccessfully and will not be continued.
	 * </p>
	 * Hooks may accept the following parameters:
	 * <ul>
	 * <li>
	 * ca.uhn.fhir.jpa.model.search.SearchRuntimeDetails - Contains details about the search being
	 * performed. Hooks should not modify this object.
	 * </li>
	 * </ul>
	 * <p>
	 * Hooks should return <code>void</code>.
	 * </p>
	 */
	PERFTRACE_SEARCH_FAILED(SearchRuntimeDetails.class.getName()),

	/**
	 * Note that this is a performance tracing hook. Use with caution in production
	 * systems, since calling it may (or may not) carry a cost.
	 * <p>
	 * This hook is invoked when a search has failed for any reason. When this pointcut
	 * is invoked, a pass in the Search Coordinator has completed successfully, but
	 * not all possible resources have been loaded yet so a future paging request
	 * may trigger a new task that will load further resources.
	 * </p>
	 * Hooks may accept the following parameters:
	 * <ul>
	 * <li>
	 * ca.uhn.fhir.jpa.model.search.SearchRuntimeDetails - Contains details about the search being
	 * performed. Hooks should not modify this object.
	 * </li>
	 * </ul>
	 * <p>
	 * Hooks should return <code>void</code>.
	 * </p>
	 */
	PERFTRACE_SEARCH_PASS_COMPLETE(SearchRuntimeDetails.class.getName()),

	/**
	 * Note that this is a performance tracing hook. Use with caution in production
	 * systems, since calling it may (or may not) carry a cost.
	 * <p>
	 * This hook is invoked when a search has failed for any reason. When this pointcut
	 * is invoked, a pass in the Search Coordinator has completed successfully, and all
	 * possible results have been fetched and loaded into the query cache.
	 * </p>
	 * Hooks may accept the following parameters:
	 * <ul>
	 * <li>
	 * ca.uhn.fhir.jpa.model.search.SearchRuntimeDetails - Contains details about the search being
	 * performed. Hooks should not modify this object.
	 * </li>
	 * </ul>
	 * <p>
	 * Hooks should return <code>void</code>.
	 * </p>
	 */
	PERFTRACE_SEARCH_COMPLETE(SearchRuntimeDetails.class.getName()),


	/**
	 * Note that this is a performance tracing hook. Use with caution in production
	 * systems, since calling it may (or may not) carry a cost.
	 * <p>
	 * This hook is invoked when any informational or warning messages generated by the
	 * SearchCoordinator are created. It is typically used to provide logging
	 * or capture details related to a specific request.
	 * </p>
	 * Hooks may accept the following parameters:
	 * <ul>
	 * <li>
	 * {@link PerformanceMessage} - Contains the message
	 * </li>
	 * </ul>
	 * <p>
	 * Hooks should return <code>void</code>.
	 * </p>
	 */
	PERFTRACE_MESSAGE(PerformanceMessage.class.getName())

	;
	private final List<String> myParameterTypes;

	Pointcut(String... theParameterTypes) {
		myParameterTypes = Collections.unmodifiableList(Arrays.asList(theParameterTypes));
	}

	public List<String> getParameterTypes() {
		return myParameterTypes;
	}
}
