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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Value for {@link Hook#value()}
 */
public enum Pointcut {

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
	 * Hooks may return <code>null</code> or may return a <code>boolean</code>. If the method returns
	 * <code>null</code> or <code>true</code>, processing will continue normally. If the method
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
	 * Hooks should return <code>null</code>.
	 * </p>
	 */
	SUBSCRIPTION_AFTER_DELIVERY("ca.uhn.fhir.jpa.subscription.module.CanonicalSubscription", "ca.uhn.fhir.jpa.subscription.module.subscriber.ResourceDeliveryMessage"),

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
	 * Hooks should return <code>null</code>.
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
	 * Hooks may return <code>null</code> or may return a <code>boolean</code>. If the method returns
	 * <code>null</code> or <code>true</code>, processing will continue normally. If the method
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
	 * Hooks may return <code>null</code> or may return a <code>boolean</code>. If the method returns
	 * <code>null</code> or <code>true</code>, processing will continue normally. If the method
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
	 * Hooks should return <code>null</code>.
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
	 * Hooks should return <code>null</code>.
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
	 * Hooks should return <code>null</code>.
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
	 * Hooks should return <code>null</code>.
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
	 * Hooks should return <code>null</code>.
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
	 * Hooks should return <code>null</code>.
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
	 * Hooks should return <code>null</code>.
	 * </p>
	 */
	OP_PRESTORAGE_RESOURCE_UPDATED("org.hl7.fhir.instance.model.api.IBaseResource", "org.hl7.fhir.instance.model.api.IBaseResource");


	private final List<String> myParameterTypes;

	Pointcut(String... theParameterTypes) {
		myParameterTypes = Collections.unmodifiableList(Arrays.asList(theParameterTypes));
	}

	public List<String> getParameterTypes() {
		return myParameterTypes;
	}
}
