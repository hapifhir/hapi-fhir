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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.model.config.SubscriptionSettings;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.messaging.BaseResourceMessage;
import ca.uhn.fhir.rest.server.util.CompositeInterceptorBroadcaster;
import ca.uhn.fhir.subscription.api.IResourceModifiedMessagePersistenceSvc;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 *
 * This interceptor is responsible for submitting operations on resources to the subscription pipeline.
 *
 */
@Interceptor
public class SubscriptionMatcherInterceptor {
	private static final Logger ourLog = LoggerFactory.getLogger(SubscriptionMatcherInterceptor.class);

	@Autowired
	private FhirContext myFhirContext;

	@Autowired
	private IInterceptorBroadcaster myInterceptorBroadcaster;

	@Autowired
	private SubscriptionSettings mySubscriptionSettings;

	@Autowired
	private IRequestPartitionHelperSvc myRequestPartitionHelperSvc;

	@Autowired
	private IResourceModifiedMessagePersistenceSvc myResourceModifiedMessagePersistenceSvc;

	/**
	 * Constructor
	 */
	public SubscriptionMatcherInterceptor() {
		super();
	}

	@Hook(Pointcut.STORAGE_PRECOMMIT_RESOURCE_CREATED)
	public void resourceCreated(IBaseResource theResource, RequestDetails theRequest) {

		processResourceModifiedEvent(theResource, ResourceModifiedMessage.OperationTypeEnum.CREATE, theRequest);
	}

	@Hook(Pointcut.STORAGE_PRECOMMIT_RESOURCE_DELETED)
	public void resourceDeleted(IBaseResource theResource, RequestDetails theRequest) {

		processResourceModifiedEvent(theResource, ResourceModifiedMessage.OperationTypeEnum.DELETE, theRequest);
	}

	@Hook(Pointcut.STORAGE_PRECOMMIT_RESOURCE_UPDATED)
	public void resourceUpdated(IBaseResource theOldResource, IBaseResource theNewResource, RequestDetails theRequest) {
		boolean dontTriggerSubscriptionWhenVersionsAreTheSame =
				!mySubscriptionSettings.isTriggerSubscriptionsForNonVersioningChanges();
		boolean resourceVersionsAreTheSame = isSameResourceVersion(theOldResource, theNewResource);

		if (dontTriggerSubscriptionWhenVersionsAreTheSame && resourceVersionsAreTheSame) {
			return;
		}

		processResourceModifiedEvent(theNewResource, ResourceModifiedMessage.OperationTypeEnum.UPDATE, theRequest);
	}

	/**
	 * This is an internal API - Use with caution!
	 *
	 * This method will create a {@link ResourceModifiedMessage}, persist it and arrange for its delivery to the
	 * subscription pipeline after the resource was committed.  The message is persisted to provide asynchronous submission
	 * in the event where submission would fail.
	 */
	protected void processResourceModifiedEvent(
			IBaseResource theNewResource,
			ResourceModifiedMessage.OperationTypeEnum theOperationType,
			RequestDetails theRequest) {

		ResourceModifiedMessage msg = createResourceModifiedMessage(theNewResource, theOperationType, theRequest);

		// Interceptor call: SUBSCRIPTION_RESOURCE_MODIFIED
		HookParams params = new HookParams().add(ResourceModifiedMessage.class, msg);
		boolean outcome = CompositeInterceptorBroadcaster.doCallHooks(
				myInterceptorBroadcaster, theRequest, Pointcut.SUBSCRIPTION_RESOURCE_MODIFIED, params);

		if (!outcome) {
			return;
		}

		processResourceModifiedMessage(msg);
	}

	protected void processResourceModifiedMessage(ResourceModifiedMessage theResourceModifiedMessage) {
		//	persist the message for async submission to the processing pipeline. see {@link
		// AsyncResourceModifiedProcessingSchedulerSvc}
		myResourceModifiedMessagePersistenceSvc.persist(theResourceModifiedMessage);
	}

	protected ResourceModifiedMessage createResourceModifiedMessage(
			IBaseResource theNewResource,
			BaseResourceMessage.OperationTypeEnum theOperationType,
			RequestDetails theRequest) {
		// Even though the resource is being written, the subscription will be interacting with it by effectively
		// "reading" it so we set the RequestPartitionId as a read request
		RequestPartitionId requestPartitionId = myRequestPartitionHelperSvc.determineReadPartitionForRequestForRead(
				theRequest, theNewResource.getIdElement());
		return new ResourceModifiedMessage(
				myFhirContext, theNewResource, theOperationType, theRequest, requestPartitionId);
	}

	private boolean isSameResourceVersion(IBaseResource theOldResource, IBaseResource theNewResource) {
		if (isNull(theOldResource) || isNull(theNewResource)) {
			return false;
		}

		String oldVersion = theOldResource.getIdElement().getVersionIdPart();
		String newVersion = theNewResource.getIdElement().getVersionIdPart();

		if (isBlank(oldVersion) || isBlank(newVersion)) {
			return false;
		}

		return oldVersion.equals(newVersion);
	}

	public void setFhirContext(FhirContext theCtx) {
		myFhirContext = theCtx;
	}
}
